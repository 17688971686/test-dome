package cs.service.restService;

import cs.common.Constant;
import cs.common.IFResultCode;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.project.Sign;
import cs.domain.sys.SysFile;
import cs.domain.sys.User;
import cs.model.project.SignDto;
import cs.model.sys.SysConfigDto;
import cs.model.sys.SysFileDto;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.project.SignService;
import cs.service.sys.SysConfigService;
import cs.service.sys.SysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static cs.common.Constant.*;

/**
 * 项目接口实现类
 * Created by ldm on 2017/12/20.
 */
@Service
public class SignRestServiceImpl implements SignRestService {

    @Autowired
    private SysFileService sysFileService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private SignService signService;
    @Autowired
    private UserRepo userRepo;
    /**
     * 项目推送
     *
     * @param signDto
     * @return
     */
    @Override
    @Transactional
    public ResultMsg pushProject(SignDto signDto) {
        if (signDto == null) {
            return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SIGN_01.getCode(), IFResultCode.IFMsgCode.SZEC_SIGN_01.getValue());
        }
        if (!Validate.isString(signDto.getFilecode())) {
            return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SIGN_02.getCode(), IFResultCode.IFMsgCode.SZEC_SIGN_02.getValue());
        }
        boolean isHaveFile = false;
        ResultMsg resultMsg = new ResultMsg();
        try {
            String stageCode = "";
            //1、判断项目阶段是否正确
            if (Validate.isString(signDto.getReviewstage())) {
                stageCode = signDto.getReviewstage();
                String stageCHName = Constant.RevireStageKey.getZHCNName(stageCode);
                if (!Validate.isString(stageCHName)) {
                    StringBuffer msgBuffer = new StringBuffer("各阶段对应的标识如下：");
                    msgBuffer.append("(" + Constant.RevireStageKey.KEY_SUG.getValue() + ":" + Constant.STAGE_SUG);
                    msgBuffer.append("|" + Constant.RevireStageKey.KEY_STUDY.getValue() + ":" + Constant.STAGE_STUDY);
                    msgBuffer.append("|" + Constant.RevireStageKey.KEY_BUDGET.getValue() + ":" + Constant.STAGE_BUDGET);
                    msgBuffer.append("|" + Constant.RevireStageKey.KEY_REPORT.getValue() + ":" + Constant.APPLY_REPORT);
                    msgBuffer.append("|" + Constant.RevireStageKey.KEY_HOMELAND.getValue() + ":" + Constant.DEVICE_BILL_HOMELAND);
                    msgBuffer.append("|" + Constant.RevireStageKey.KEY_IMPORT.getValue() + ":" + Constant.DEVICE_BILL_IMPORT);
                    msgBuffer.append("|" + Constant.RevireStageKey.KEY_DEVICE.getValue() + ":" + Constant.IMPORT_DEVICE);
                    msgBuffer.append("|" + Constant.RevireStageKey.KEY_OTHER.getValue() + ":" + Constant.OTHERS + ")");
                    return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SIGN_04.getCode(), IFResultCode.IFMsgCode.SZEC_SIGN_04.getValue() + msgBuffer.toString());
                }
                //对应系统的阶段名称
                signDto.setReviewstage(stageCHName);
                //是否是项目概算流程
                if (Constant.RevireStageKey.KEY_BUDGET.getValue().equals(stageCode)
                        || Validate.isString(signDto.getIschangeEstimate())) {
                    signDto.setIsassistflow(Constant.EnumState.YES.getValue());
                } else {
                    signDto.setIsassistflow(Constant.EnumState.NO.getValue());
                }
            } else {
                return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SIGN_03.getCode(), IFResultCode.IFMsgCode.SZEC_SIGN_03.getValue());
            }
            //1、根据收文编号获取项目信息
            Sign sign = signRepo.findByFilecode(signDto.getFilecode());
            Date now = new Date();
            if (sign == null) {
                sign = new Sign();
                BeanCopierUtils.copyProperties(signDto, sign);
                sign.setSignid(UUID.randomUUID().toString());
                sign.setIsLightUp(Constant.signEnumState.NOLIGHT.getValue());
                sign.setCreatedDate(now);
                sign.setCreatedBy(SUPER_USER);
            } else {
                BeanCopierUtils.copyPropertiesIgnoreNull(signDto, sign);
            }
            sign.setModifiedBy(SUPER_USER);
            sign.setModifiedDate(now);

            //送来时间
            if (Validate.isObject(sign.getReceivedate())) {
                sign.setReceivedate(now);
            }

            //未签收的，改为正式签收
            if (!Validate.isString(sign.getIssign()) || Constant.EnumState.NO.getValue().equals(sign.getIssign())) {
                //正式签收
                sign.setIssign(Constant.EnumState.YES.getValue());
                sign.setSigndate(now);

                SysConfigDto sysConfigDto = sysConfigService.findByKey(stageCode);
                if (sysConfigDto != null && sysConfigDto.getConfigValue() != null) {
                    sign.setReviewdays(Float.parseFloat(sysConfigDto.getConfigValue()));
                    sign.setSurplusdays(Float.parseFloat(sysConfigDto.getConfigValue()));
                } else {
                    //设定默认值，项目建议书和资金申请报告是12天，其他是15天
                    if ((Constant.RevireStageKey.KEY_SUG.getValue()).equals(stageCode)
                            || (Constant.RevireStageKey.KEY_REPORT.getValue()).equals(stageCode)) {
                        //剩余评审天数
                        sign.setSurplusdays(Constant.WORK_DAY_12);
                        //评审天数，完成的时候再结算
                        sign.setReviewdays(Constant.WORK_DAY_12);
                    } else {
                        sign.setSurplusdays(Constant.WORK_DAY_15);
                        sign.setReviewdays(Constant.WORK_DAY_15);
                    }
                }
            }

            //4、默认办理部门（项目建议书、可研为PX，概算为GX，其他为评估）
            if(!Validate.isString(sign.getDealOrgType())){
                if (Constant.STAGE_BUDGET.equals(sign.getReviewstage())) {
                    sign.setDealOrgType(Constant.BusinessType.GX.getValue());
                    sign.setLeaderhandlesug("请（概算一部 概算二部）组织评审。");
                } else {
                    sign.setDealOrgType(Constant.BusinessType.PX.getValue());
                    sign.setLeaderhandlesug("请（评估一部 评估二部 评估一部信息化组）组织评审。");
                }
            }

            if(Validate.isString(sign.getLeaderId())){
                //5、综合部、分管副主任默认办理信息
                List<User> roleList = userRepo.findUserByRoleName(EnumFlowNodeGroupName.VICE_DIRECTOR.getValue());
                for (User user : roleList) {
                    if (sign.getDealOrgType().equals(user.getMngOrgType())) {
                        sign.setLeaderId(user.getId());
                        sign.setLeaderName(user.getDisplayName());
                        sign.setComprehensivehandlesug("请" + (user.getDisplayName()).substring(0, 1) + "主任阅示。");
                        sign.setComprehensiveName("综合部");
                        sign.setComprehensiveDate(new Date());
                        break;
                    }
                }
            }

            //6、收文编号
            if (!Validate.isString(sign.getSignNum())) {
                int maxSeq = signService.findSignMaxSeqByType(sign.getDealOrgType(), sign.getSigndate());
                sign.setSignSeq(maxSeq + 1);
                String signSeqString = (maxSeq + 1) > 999 ? (maxSeq + 1) + "" : String.format("%03d", Integer.valueOf(maxSeq + 1));
                sign.setSignNum(DateUtils.converToString(new Date(), "yyyy") + sign.getDealOrgType() + signSeqString);
            }
            signRepo.save(sign);

            //获取传送过来的附件
            if(Validate.isList(signDto.getSysFileDtoList())){
                isHaveFile = true;
                //连接ftp
                Ftp f = new Ftp();
                PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
                f.setIpAddr(propertyUtil.readProperty(FTP_IP1));
                f.setPort(Integer.parseInt(propertyUtil.readProperty(FTP_PORT1)));
                f.setUserName(propertyUtil.readProperty(FTP_USER));
                f.setPwd(propertyUtil.readProperty(FTP_PWD));
                boolean linkSucess = FtpUtil.connectFtp(f);
                if(linkSucess){
                    StringBuffer fileBuffer = new StringBuffer();
                    int totalFileCount = signDto.getSysFileDtoList().size();
                    List<SysFile> saveFileList = new ArrayList<>(totalFileCount);
                    //读取附件
                    for(int i=0,l=totalFileCount;i<l;i++){
                        SysFileDto sysFileDto = signDto.getSysFileDtoList().get(i);
                        String showName = sysFileDto.getShowName();
                        if(!Validate.isString(sysFileDto.getFileUrl())){
                            fileBuffer.append("【"+showName+"】附件接收失败，没有url!\r\n");
                            continue;
                        }
                        SysFile sysFile = new SysFile();
                        URL url = new URL(sysFileDto.getFileUrl());
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        //设置超时间为3秒
                        conn.setConnectTimeout(600000);
                        //防止屏蔽程序抓取而返回403错误
                        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                        //得到输入流

                        sysFile.setMainId(sign.getSignid());
                        sysFile.setBusinessId(sign.getSignid());
                        sysFile.setMainType(SysFileType.SIGN.getValue());
                        sysFile.setSysBusiType( SysFileType.FGW_FILE.getValue());
                        sysFile.setFileSize(sysFileDto.getFileSize());
                        sysFile.setShowName(sysFileDto.getShowName());
                        sysFile.setFileType(showName.substring(showName.lastIndexOf("."),showName.length()));
                        String relativeFileUrl = "/"+ Constant.SysFileType.SIGN.getValue()+"/"+sign.getSignid()+"/"+Constant.SysFileType.FGW_FILE.getValue();

                        String uploadFileName = Tools.generateRandomFilename().concat(sysFile.getFileType());
                        boolean uploadResult = FtpUtil.uploadFile(relativeFileUrl, uploadFileName, conn.getInputStream());
                        if (uploadResult) {
                            sysFile.setFileUrl(relativeFileUrl + "/" + uploadFileName);
                            sysFile.setCreatedBy(SUPER_USER);
                            sysFile.setModifiedBy(SUPER_USER);
                            sysFile.setCreatedDate(now);
                            sysFile.setModifiedDate(now);
                            sysFile.setFtpIp(f.getIpAddr());
                            sysFile.setPort(f.getPort().toString());
                            sysFile.setFtpUser(f.getUserName());
                            sysFile.setFtpPwd(f.getPwd());
                            sysFile.setFtpBasePath(f.getPath());
                            sysFile.setSysFileId(UUID.randomUUID().toString());
                            sysFile.setBusinessId(sign.getSignid());
                            saveFileList.add(sysFile);

                        } else {
                            sysFile = null;
                            fileBuffer.append("【"+showName+"】附件存储到文件服务器失败!\r\n");
                        }
                    }
                    //保存附件
                    if(Validate.isList(saveFileList)){
                        sysFileService.bathSave(saveFileList);
                    }
                    if(Validate.isString(fileBuffer.toString())){
                        resultMsg.setReMsg(fileBuffer.toString());
                    }
                }else{
                    resultMsg.setReMsg("附件存储失败，无法连接文件服务器！");
                }
            }
            resultMsg.setFlag(true);
            if(Validate.isString(resultMsg.getReMsg())){
                //附件保存有错误
                resultMsg.setReCode(IFResultCode.IFMsgCode.SZEC_SIGN_05.getCode());
                resultMsg.setReMsg(IFResultCode.IFMsgCode.SZEC_SIGN_05.getValue()+resultMsg.getReMsg());
            }else{
                resultMsg.setReCode(IFResultCode.IFMsgCode.SZEC_SAVE_OK.getCode());
                resultMsg.setReMsg(IFResultCode.IFMsgCode.SZEC_SAVE_OK.getValue());
            }
        } catch (Exception e) {
            resultMsg = new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SAVE_ERROR.getCode(), IFResultCode.IFMsgCode.SZEC_SAVE_ERROR.getValue() + e.getMessage());
        }finally {
            if(isHaveFile){
                FtpUtil.closeFtp();
            }
        }

        return resultMsg;
    }
}
