package cs.service.restService;

import com.alibaba.fastjson.JSON;
import cs.ahelper.HttpClientOperate;
import cs.ahelper.HttpResult;
import cs.common.*;
import cs.common.utils.*;
import cs.domain.project.DispatchDoc;
import cs.domain.project.Sign;
import cs.domain.project.WorkProgram;
import cs.domain.sys.Ftp;
import cs.domain.sys.SysFile;
import cs.domain.sys.User;
import cs.model.project.SignDto;
import cs.model.sys.SysConfigDto;
import cs.model.sys.SysFileDto;
import cs.repository.repositoryImpl.project.DispatchDocRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.repository.repositoryImpl.sys.FtpRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.project.SignService;
import cs.service.sys.SysConfigService;
import cs.service.sys.SysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static cs.common.Constant.*;

/**
 * 项目接口实现类
 * Created by ldm on 2017/12/20.
 */
@Service
public class SignRestServiceImpl implements SignRestService {
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private DispatchDocRepo dispatchDocRepo;
    @Autowired
    private SignService signService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private HttpClientOperate httpClientOperate;
    @Autowired
    private SysFileRepo sysFileRepo;
    @Autowired
    private SysFileService sysFileService;
    @Autowired
    private FtpRepo ftpRepo;

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
            if (!Validate.isString(sign.getDealOrgType())) {
                if (Constant.STAGE_BUDGET.equals(sign.getReviewstage())) {
                    sign.setDealOrgType(Constant.BusinessType.GX.getValue());
                    sign.setLeaderhandlesug("请（概算一部 概算二部）组织评审。");
                } else {
                    sign.setDealOrgType(Constant.BusinessType.PX.getValue());
                    sign.setLeaderhandlesug("请（评估一部 评估二部 评估一部信息化组）组织评审。");
                }
            }

            if (!Validate.isString(sign.getLeaderId())) {
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
            if (Validate.isList(signDto.getSysFileDtoList())) {
                isHaveFile = true;
                //连接ftp
                Ftp f = ftpRepo.findById(cs.domain.sys.Ftp_.ipAddr.getName(), sysFileService.findFtpId());
                boolean linkSucess = FtpUtil.connectFtp(f, true);
                if (linkSucess) {
                    StringBuffer fileBuffer = new StringBuffer();
                    int totalFileCount = signDto.getSysFileDtoList().size();
                    List<SysFile> saveFileList = new ArrayList<>(totalFileCount);
                    //读取附件
                    for (int i = 0, l = totalFileCount; i < l; i++) {
                        SysFileDto sysFileDto = signDto.getSysFileDtoList().get(i);
                        String showName = sysFileDto.getShowName();
                        if (!Validate.isString(sysFileDto.getFileUrl())) {
                            fileBuffer.append("【" + showName + "】附件接收失败，没有url!\r\n");
                            continue;
                        }
                        SysFile sysFile = new SysFile();
                        URL url = new URL(sysFileDto.getFileUrl());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        //设置超时间为3秒
                        conn.setConnectTimeout(600000);
                        //防止屏蔽程序抓取而返回403错误
                        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                        //得到输入流

                        sysFile.setMainId(sign.getSignid());
                        sysFile.setBusinessId(sign.getSignid());
                        sysFile.setMainType(SysFileType.SIGN.getValue());
                        sysFile.setSysBusiType(SysFileType.FGW_FILE.getValue());
                        sysFile.setFileSize(sysFileDto.getFileSize());
                        sysFile.setShowName(sysFileDto.getShowName());
                        sysFile.setFileType(showName.substring(showName.lastIndexOf("."), showName.length()));
                        String relativeFileUrl = File.separator + Constant.SysFileType.SIGN.getValue() + File.separator + sign.getSignid() + File.separator + Constant.SysFileType.FGW_FILE.getValue();

                        String uploadFileName = Tools.generateRandomFilename().concat(sysFile.getFileType());
                        boolean uploadResult = FtpUtil.uploadFile(relativeFileUrl, uploadFileName, conn.getInputStream());
                        if (uploadResult) {
                            sysFile.setFileUrl(relativeFileUrl + File.separator + uploadFileName);
                            sysFile.setCreatedBy(SUPER_USER);
                            sysFile.setModifiedBy(SUPER_USER);
                            sysFile.setCreatedDate(now);
                            sysFile.setModifiedDate(now);
                            sysFile.setFtp(f);
                            sysFile.setSysFileId(UUID.randomUUID().toString());
                            sysFile.setBusinessId(sign.getSignid());
                            saveFileList.add(sysFile);

                        } else {
                            sysFile = null;
                            fileBuffer.append("【" + showName + "】附件存储到文件服务器失败!\r\n");
                        }
                    }
                    //保存附件
                    if (Validate.isList(saveFileList)) {
                        sysFileService.bathSave(saveFileList);
                    }
                    if (Validate.isString(fileBuffer.toString())) {
                        resultMsg.setReMsg(fileBuffer.toString());
                    }
                } else {
                    resultMsg.setReMsg("附件存储失败，无法连接文件服务器！");
                }
            }
            resultMsg.setFlag(true);
            if (Validate.isString(resultMsg.getReMsg())) {
                //附件保存有错误
                resultMsg.setReCode(IFResultCode.IFMsgCode.SZEC_SIGN_05.getCode());
                resultMsg.setReMsg(IFResultCode.IFMsgCode.SZEC_SIGN_05.getValue() + resultMsg.getReMsg());
            } else {
                resultMsg.setReCode(IFResultCode.IFMsgCode.SZEC_SAVE_OK.getCode());
                resultMsg.setReMsg(IFResultCode.IFMsgCode.SZEC_SAVE_OK.getValue());
            }
        } catch (Exception e) {
            resultMsg = new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SAVE_ERROR.getCode(), IFResultCode.IFMsgCode.SZEC_SAVE_ERROR.getValue() + e.getMessage());
        } finally {
            if (isHaveFile) {
                FtpUtil.closeFtp();
            }
        }

        return resultMsg;
    }

    /**
     * 回调数据给发改委
     *
     * @param sign
     * @return
     */
    @Override
    public ResultMsg setToFGW(Sign sign, String url, String loaclUrl) {
        String sendUrl = url;
        if (!Validate.isString(sendUrl)) {
            // 接口地址
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            sendUrl = propertyUtil.readProperty(IFResultCode.FGW_PROJECT_IFS);
        }
        if (!Validate.isString(sendUrl)) {
            return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SFGW_01.getCode(), IFResultCode.IFMsgCode.SZEC_SFGW_01.getValue());
        }
        if (!Validate.isString(loaclUrl)) {
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            loaclUrl = propertyUtil.readProperty(IFResultCode.LOCAL_URL);
            if (Validate.isString(loaclUrl) && loaclUrl.endsWith("/")) {
                loaclUrl = loaclUrl.substring(0, loaclUrl.length() - 1);
            }
        }
        //主工作方案
        WorkProgram mainWP = null;
        //发文
        DispatchDoc dispatchDoc = null;
        Map<String, String> params = new HashMap<>();
        try {
            //1、评审意见对象
            Map<String, Object> dataMap = new HashMap<>();
            //2、办理意见
            /**
             * 评审过程办理环节接口对照码
             * 1：收文
             * 2：分办
             * 3：评审方案
             * 4：发文
             */
            ArrayList<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
            //2.1 收文的办理意见
            HashMap<String, Object> psgcMap = new HashMap<String, Object>();
            psgcMap.put("blhj", "1");// 办理环节
            psgcMap.put("psblyj",sign.getLeaderhandlesug());// 办理意见
            psgcMap.put("blr", sign.getLeaderName());// 办理人
            psgcMap.put("blsj", sign.getLeaderDate().getTime());// 办理时间
            dataList.add(psgcMap);

         /*   sign.setFilecode("Z201800001");*/
            dataMap.put("swbh", sign.getFilecode());// 收文编号
            if (Validate.isList(sign.getWorkProgramList())) {
                for (WorkProgram wp : sign.getWorkProgramList()) {
                    if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(wp.getBranchId())) {
                        mainWP = wp;
                        break;
                    }
                }
            } else {
                mainWP = workProgramRepo.findBySignIdAndBranchId(sign.getSignid(), FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue());
            }
            if (mainWP != null) {
                dataMap.put("psfs", IFResultCode.PSFS.getCodeByValue(mainWP.getReviewType()));// 评审方式
                /**
                 * 2018-01-12
                 * 送审日期和送审结束时间不用传了。
                 * 送审日期我默认为调用你们接口成功的时间，结束时间默认为你调用我们接口成功的时间
                 */
                dataMap.put("sssj", (new Date()).getTime());// 送审日期
                dataMap.put("psjssj", (new Date()).getTime());// 评审结束时间

                //2.2工作方案环节处理意见
                psgcMap = new HashMap<String, Object>();
                psgcMap.put("blhj", "3");// 办理环节
                psgcMap.put("psblyj",mainWP.getLeaderSuggesttion());// 办理意见
                psgcMap.put("blr", mainWP.getLeaderName());// 办理人
                psgcMap.put("blsj", mainWP.getLeaderDate().getTime());// 办理时间
                dataList.add(psgcMap);
            }

            if (Validate.isObject(sign.getDispatchDoc())) {
                dispatchDoc = sign.getDispatchDoc();
            } else {
                dispatchDoc = dispatchDocRepo.findById("signid", sign.getSignid());
            }
            if (!Validate.isObject(dispatchDoc)) {
                return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SFGW_03.getCode(), IFResultCode.IFMsgCode.SZEC_SFGW_03.getValue());
            }
            dataMap.put("xmpsyd", dispatchDoc.getReviewAbstract());// 项目评审要点
            dataMap.put("sb", (dispatchDoc.getDeclareValue()).setScale(2, BigDecimal.ROUND_HALF_UP));// 申报投资额（万元）
            dataMap.put("sd", (dispatchDoc.getAuthorizeValue()).setScale(2, BigDecimal.ROUND_HALF_UP));// 审定投资额（万元）
            dataMap.put("hjz", (dispatchDoc.getExtraValue()).setScale(2, BigDecimal.ROUND_HALF_UP));// 核减（增）投资额（万元）
            dataMap.put("hjzl", (dispatchDoc.getExtraRate()).setScale(2, BigDecimal.ROUND_HALF_UP));// 核减（增）率
            dataMap.put("psbz", dispatchDoc.getRemark());// 备注
            dataMap.put("xmjsbyx", dispatchDoc.getProjectBuildNecess());// 项目建设必要性
            dataMap.put("jsnrjgm", dispatchDoc.getBuildSizeContent());// 建设内容及规模
            dataMap.put("tzksjzjly", dispatchDoc.getFundTotalOrigin());// 投资估算及资金来源
            dataMap.put("xyjdgzyq", dispatchDoc.getNextWorkPlan());// 下一阶段工作要求

            //2.3 发文环节处理意见
            psgcMap = new HashMap<String, Object>();
            psgcMap.put("blhj", "4");// 办理环节
            psgcMap.put("psblyj",dispatchDoc.getViceDirectorSuggesttion());// 办理意见
            psgcMap.put("blr", dispatchDoc.getViceDirectorName());// 办理人
            psgcMap.put("blsj", dispatchDoc.getViceDirectorDate().getTime());// 办理时间
            dataList.add(psgcMap);

            //2、评审报告和投资匡算附件是必须上传的
            ArrayList<HashMap<String, Object>> fjList = new ArrayList<HashMap<String, Object>>();
            List<SysFile> fileList = sysFileRepo.findByMainId(sign.getSignid());
            if (Validate.isList(fileList)) {
                List<String> checkNameArr = new ArrayList<>();
                //评审报告附件
                checkNameArr.add("评审意见");
                checkNameArr.add("审核意见");
                fjList = checkFile(fileList,checkNameArr,loaclUrl);
                if(Validate.isList(fjList)){
                    dataMap.put("psbg", fjList);
                }
                //投资匡算附件
                fjList = new ArrayList<HashMap<String, Object>>();
                checkNameArr = new ArrayList<>();
                checkNameArr.add("投资估算表");
                checkNameArr.add("投资匡算表");
                fjList = checkFile(fileList,checkNameArr,loaclUrl);
                if(Validate.isList(fjList)){
                    dataMap.put("tzgsshb", fjList);
                }
            }
            params.put("dataMap", JSON.toJSONString(dataMap));
            params.put("dataList", JSON.toJSONString(dataList));

            HttpResult hst = httpClientOperate.doPost(sendUrl, params);
            FGWResponse fGWResponse = JSON.toJavaObject(JSON.parseObject(hst.getContent()), FGWResponse.class);
            //成功
            if (Constant.EnumState.PROCESS.getValue().equals(fGWResponse.getRestate())) {
                return new ResultMsg(true, IFResultCode.IFMsgCode.SZEC_SEND_OK.getCode(), "项目【" + sign.getProjectname() + "(" + sign.getFilecode() + ")】回传数据给发改委成功！");
            } else {
                return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SEND_ERROR.getCode(),
                        "项目【" + sign.getProjectname() + "(" + sign.getFilecode() + ")】回传数据给发改委失败！" + fGWResponse.getRedes());
            }
        } catch (Exception e) {
            return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_DEAL_ERROR.getCode(),
                    "项目【" + sign.getProjectname() + "(" + sign.getFilecode() + ")】回传数据给发改委异常！" + e.getMessage());
        }
    }

    private ArrayList<HashMap<String, Object>> checkFile(List<SysFile> fileList,List<String> checkNameArr,String loaclUrl){
        ArrayList<HashMap<String, Object>> fjList = new ArrayList<HashMap<String, Object>>();
        List<SysFile> sendList = new ArrayList<>();
        for (int i = 0, l = fileList.size(); i < l; i++) {
            SysFile sendFile = fileList.get(i);
            String showName = sendFile.getShowName().toLowerCase();
            String fileType = sendFile.getFileType().toLowerCase();
            for (String checkName : checkNameArr) {
                if (showName.equals(checkName + fileType)) {
                    sendList.add(sendFile);
                }
            }
        }
        if (Validate.isList(sendList)) {
            for (SysFile sf : sendList) {
                HashMap<String, Object> fjMap = new HashMap<String, Object>();
                fjMap.put("url", loaclUrl + "/file/remoteDownload/" + sf.getSysFileId());
                fjMap.put("filename", sf.getShowName());
                fjMap.put("tempName", sf.getCreatedBy());
                fjList.add(fjMap);
            }
        }
        return fjList;
    }
}
