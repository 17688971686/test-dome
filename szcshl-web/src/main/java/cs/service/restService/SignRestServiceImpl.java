package cs.service.restService;

import cs.common.Constant;
import cs.common.IFResultCode;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.project.Sign;
import cs.domain.sys.SysFile;
import cs.model.project.SignDto;
import cs.model.sys.SysConfigDto;
import cs.model.sys.SysFileDto;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.service.project.SignService;
import cs.service.sys.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
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
    private SignService signService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private SignRepo signRepo;

    /**
     * 项目推送
     *
     * @param signDto
     * @return
     */
    @Override
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
                sign.setModifiedDate(now);
                sign.setCreatedBy("FGW");
                sign.setModifiedBy("FGW");
            } else {
                BeanCopierUtils.copyPropertiesIgnoreNull(signDto, sign);
            }

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
                    int totalFileCount = signDto.getSysFileDtoList().size();
                    List<SysFile> saveFileList = new ArrayList<>(totalFileCount);
                    //读取附件
                    for(int i=0,l=totalFileCount;i<l;i++){
                        SysFileDto sysFileDto = signDto.getSysFileDtoList().get(i);
                        if(!Validate.isString(sysFileDto.getFileUrl())){
                            continue;
                        }
                        SysFile sysFile = new SysFile();
                        URL url = new URL(sysFileDto.getFileUrl());
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        //设置超时间为3秒
                        conn.setConnectTimeout(3*1000);
                        //防止屏蔽程序抓取而返回403错误
                        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                        //得到输入流
                        InputStream inputStream = conn.getInputStream();

                        sysFile.setMainId(sign.getSignid());
                        sysFile.setBusinessId(sign.getSignid());
                        sysFile.setMainType(SysFileType.SIGN.getValue());
                        sysFile.setSysBusiType( SysFileType.FGW_FILE.getValue());
                        sysFile.setFileSize(sysFileDto.getFileSize());
                        String showName = sysFileDto.getShowName();
                        sysFile.setShowName(sysFileDto.getShowName());
                        sysFile.setFileType(showName.substring(showName.lastIndexOf("."),showName.length()));
                        String relativeFileUrl = SysFileUtil.generatRelativeUrl("", sysFile.getMainType(), sign.getSignid(),sysFile.getSysBusiType(), null);

                        String uploadFileName = Tools.generateRandomFilename().concat(sysFile.getFileType());
                        boolean uploadResult = FtpUtil.uploadFile(relativeFileUrl, uploadFileName, inputStream);
                        if (uploadResult) {
                            sysFile.setFileUrl(relativeFileUrl + File.separator + uploadFileName);
                            sysFile.setCreatedBy(SUPER_USER);
                            sysFile.setModifiedBy(SUPER_USER);
                            sysFile.setCreatedDate(now);
                            sysFile.setModifiedDate(now);
                            sysFile.setFtpIp(f.getIpAddr());
                            sysFile.setPort(f.getPort().toString());
                            sysFile.setFtpUser(f.getUserName());
                            sysFile.setFtpPwd(f.getPwd());
                            sysFile.setFtpBasePath(f.getPath());
                            sysFile.setFtpIp(UUID.randomUUID().toString());
                            saveFileList.add(sysFile);

                        } else {

                        }
                    }

                }
            }
            resultMsg =  new ResultMsg(true, IFResultCode.IFMsgCode.SZEC_SAVE_OK.getCode(), IFResultCode.IFMsgCode.SZEC_SAVE_OK.getValue(), sign.getSignid());
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
