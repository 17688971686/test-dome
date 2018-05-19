package cs.service.restService;

import com.alibaba.fastjson.JSON;
import cs.ahelper.HttpClientOperate;
import cs.ahelper.HttpResult;
import cs.common.Constant;
import cs.common.FGWResponse;
import cs.common.IFResultCode;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.project.Sign;
import cs.domain.sys.SysFile;
import cs.domain.sys.User;
import cs.domain.sys.Workday;
import cs.model.project.CommentDto;
import cs.model.project.DispatchDocDto;
import cs.model.project.SignDto;
import cs.model.project.WorkProgramDto;
import cs.model.sys.SysConfigDto;
import cs.model.sys.SysFileDto;
import cs.quartz.unit.DispathUnit;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.sys.FtpRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.project.SignService;
import cs.service.sys.SysConfigService;
import cs.service.sys.SysFileService;
import cs.service.sys.WorkdayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static cs.common.Constant.RevireStageKey.FGW_PRE_PROJECT_IFS;
import static cs.common.Constant.RevireStageKey.RETURN_FGW_URL;
import static cs.common.Constant.SUPER_USER;
import static cs.common.FlowConstant.*;

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
    private SignService signService;
    @Autowired
    private HttpClientOperate httpClientOperate;
    @Autowired
    private SysFileRepo sysFileRepo;
    @Autowired
    private SysFileService sysFileService;

    @Autowired
    private WorkdayService workdayService;

    /**
     * 项目推送
     *
     * @param signDto
     * @return
     */
    @Override
    @Transactional
    public ResultMsg pushProject(SignDto signDto, boolean isGetFiles) {
        if (signDto == null) {
            return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SIGN_01.getCode(), IFResultCode.IFMsgCode.SZEC_SIGN_01.getValue());
        }
        if (!Validate.isString(signDto.getFilecode())) {
            return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SIGN_02.getCode(), IFResultCode.IFMsgCode.SZEC_SIGN_02.getValue());
        }
        ResultMsg resultMsg = null;
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

            resultMsg = signService.createSign(signDto);
            /*//1、根据收文编号获取项目信息
            Sign sign = signRepo.findByFilecode(signDto.getFilecode(), Constant.EnumState.DELETE.getValue());
            Date now = new Date();
            boolean isExistPro = false, isLoginUser = Validate.isString(SessionUtil.getUserId());
            if (!Validate.isObject(sign) || !Validate.isString(sign.getSignid())) {
                sign = new Sign();
                BeanCopierUtils.copyProperties(signDto, sign);
                sign.setSignid(UUID.randomUUID().toString());
                sign.setIsLightUp(Constant.signEnumState.NOLIGHT.getValue());
                sign.setCreatedDate(now);
                sign.setCreatedBy(isLoginUser ? SessionUtil.getUserId() : SUPER_USER);
                //这里的送件人，默认为流程发起人，而不是委里项目的送件人
                sign.setSendusersign(isLoginUser ? SessionUtil.getDisplayName() : "");
                //送来时间
                sign.setReceivedate(now);
                //初始化办理部门信息
                signService.initSignDeptInfo(sign);
                //设定状态
                sign.setSignState(Constant.EnumState.NORMAL.getValue());
            } else {
                isExistPro = true;
                BeanCopierUtils.copyPropertiesIgnoreNull(signDto, sign);
            }
            sign.setModifiedBy(isLoginUser ? SessionUtil.getUserId() : SUPER_USER);
            sign.setModifiedDate(now);

            //送来时间
            if (!Validate.isObject(sign.getReceivedate())) {
                sign.setReceivedate(now);
            }

            //未签收的，改为正式签收
            if (!Validate.isString(sign.getIssign()) || Constant.EnumState.NO.getValue().equals(sign.getIssign())) {
                //预签收状态也要改
                sign.setIspresign(Constant.EnumState.YES.getValue());
                //正式签收
                sign.setIssign(Constant.EnumState.YES.getValue());
                sign.setSigndate(now);

                //计算评审天数
                Float totalReviewDays = Constant.WORK_DAY_15;
                SysConfigDto sysConfigDto = sysConfigService.findByKey(stageCode);
                if (sysConfigDto != null && sysConfigDto.getConfigValue() != null) {
                    totalReviewDays = Float.parseFloat(sysConfigDto.getConfigValue());
                } else if ((Constant.RevireStageKey.KEY_SUG.getValue()).equals(stageCode) || (Constant.RevireStageKey.KEY_REPORT.getValue()).equals(stageCode)) {
                    totalReviewDays = Constant.WORK_DAY_12;
                }
                //剩余评审天数
                sign.setSurplusdays(totalReviewDays);
                sign.setTotalReviewdays(totalReviewDays);
                sign.setReviewdays(0f);

                //计算预发文日期
                //1、先获取从签收日期后的30天之间的工作日情况
                List<Workday> workdayList = workdayService.getBetweenTimeDay(sign.getSigndate() , DateUtils.addDay(sign.getSigndate() , 30));
                int totalDays = (new Float(totalReviewDays)).intValue();
                Date expectdispatchdate = DispathUnit.dispathDate(workdayList , sign.getSigndate() ,totalDays);
                sign.setExpectdispatchdate(expectdispatchdate);
            }

            //6、收文编号
            if (!Validate.isString(sign.getSignNum())) {
                signService.initSignNum(sign);
            }
            signRepo.save(sign);*/

            if(resultMsg.isFlag()){
                boolean isLoginUser = Validate.isString(SessionUtil.getUserId());
                Sign sign = (Sign) resultMsg.getReObj();
                checkDownLoadFile(resultMsg, isGetFiles, sign.getSignid(), signDto.getSysFileDtoList(), isLoginUser ? SessionUtil.getUserId() : SUPER_USER, Constant.SysFileType.SIGN.getValue(), Constant.SysFileType.FGW_FILE.getValue());
            }
        } catch (Exception e) {
            resultMsg = new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SAVE_ERROR.getCode(), IFResultCode.IFMsgCode.SZEC_SAVE_ERROR.getValue() + e.getMessage());
        } finally {

        }
        return resultMsg;
    }

    public void checkDownLoadFile(ResultMsg resultMsg, boolean isGetFiles, String businessId, List<SysFileDto> sysFileDtoList, String userId, String mainType, String busiType) {
        //如果获取附件
        if (isGetFiles) {
            //获取传送过来的附件
            ResultMsg fileResult = sysFileService.downRemoteFile(businessId, sysFileDtoList, userId, mainType, busiType);
            if (fileResult.isFlag()) {
                resultMsg.setFlag(true);
                resultMsg.setReCode(IFResultCode.IFMsgCode.SZEC_SAVE_OK.getCode());
                resultMsg.setReMsg(IFResultCode.IFMsgCode.SZEC_SAVE_OK.getValue());
            } else {
                resultMsg.setFlag(false);
                resultMsg.setReCode(IFResultCode.IFMsgCode.SZEC_SIGN_05.getCode());
                resultMsg.setReMsg(IFResultCode.IFMsgCode.SZEC_SIGN_05.getValue());
            }
            //不接收附件
        } else {
            resultMsg.setFlag(true);
            resultMsg.setReCode(IFResultCode.IFMsgCode.SZEC_SAVE_OK.getCode());
            resultMsg.setReMsg(IFResultCode.IFMsgCode.SZEC_SAVE_OK.getValue());
        }
    }

    /**
     * 预签收项目
     *
     * @param signDto
     * @return
     */
    @Override
    @Transactional
    public ResultMsg pushPreProject(SignDto signDto, boolean isGetFiles) {
        if (signDto == null) {
            return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SIGN_01.getCode(), IFResultCode.IFMsgCode.SZEC_SIGN_01.getValue());
        }
        if (!Validate.isString(signDto.getFilecode())) {
            return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SIGN_02.getCode(), IFResultCode.IFMsgCode.SZEC_SIGN_02.getValue());
        }
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
           /* Sign sign = signRepo.findByFilecode(signDto.getFilecode(), Constant.EnumState.DELETE.getValue());
            boolean isExistPro = false, isLoginUser = Validate.isString(SessionUtil.getUserId());
            Date now = new Date();
            if (!Validate.isObject(sign) || !Validate.isString(sign.getSignid())) {
                sign = new Sign();
                BeanCopierUtils.copyProperties(signDto, sign);
                sign.setSignid(UUID.randomUUID().toString());
                sign.setIsLightUp(Constant.signEnumState.NOLIGHT.getValue());
                sign.setCreatedDate(now);
                sign.setCreatedBy(isLoginUser ? SessionUtil.getUserId() : SUPER_USER);
                //这里的送件人，默认为流程发起人，而不是委里项目的送件人
                sign.setSendusersign(isLoginUser ? SessionUtil.getDisplayName() : SUPER_USER);
                //预签收
                sign.setIspresign(Constant.EnumState.NO.getValue());
                sign.setSignState(Constant.EnumState.NORMAL.getValue());
                //预签收日期
                sign.setPresignDate(now);
                //初始化办理部门信息
                signService.initSignDeptInfo(sign);
                //送来时间
                sign.setReceivedate(now);
            } else {
                isExistPro = true;
                BeanCopierUtils.copyPropertiesIgnoreNull(signDto, sign);
            }
            sign.setModifiedBy(isLoginUser ? SessionUtil.getDisplayName() : SUPER_USER);
            sign.setModifiedDate(now);
            signRepo.save(sign);*/
            resultMsg = signService.reserveAddSign(signDto);
            if(resultMsg.isFlag()){
                boolean isLoginUser = Validate.isString(SessionUtil.getUserId());
                Sign sign = (Sign) resultMsg.getReObj();
                checkDownLoadFile(resultMsg, isGetFiles, sign.getSignid(), signDto.getSysFileDtoList(), isLoginUser ? SessionUtil.getUserId() : SUPER_USER, Constant.SysFileType.SIGN.getValue(), Constant.SysFileType.FGW_FILE.getValue());
            }
        } catch (Exception e) {
            resultMsg = new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SAVE_ERROR.getCode(), IFResultCode.IFMsgCode.SZEC_SAVE_ERROR.getValue() + e.getMessage());
        } finally {

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
    public ResultMsg setToFGW(SignDto sign, WorkProgramDto mainWP, DispatchDocDto dispatchDoc, String fgwUrl, Map<String, CommentDto> commentDtoMap) {
        Map<String, String> params = new HashMap<>();
        try {
            //1、评审意见对象
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("swbh", sign.getFilecode());// 收文编号
            //2、办理意见
            /**
             * 评审过程办理环节接口对照码
             * 1：收文
             * 2：分办
             * 3：评审方案
             * 4：发文
             */
            ArrayList<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
            //2.1 收文的办理意见(项目签收人意见，默认去综合部意见信息)
            HashMap<String, Object> psgcMap = new HashMap<String, Object>();
            psgcMap.put("blhj", "1");// 办理环节
            psgcMap.put("psblyj", sign.getComprehensivehandlesug());// 办理意见
            psgcMap.put("blr", sign.getSendusersign());// 办理人
            psgcMap.put("blsj", sign.getSigndate().getTime());// 办理时间
            dataList.add(psgcMap);

            //2.1 分办意见（分管领导分办和部门分办）
            setBlyj(commentDtoMap,FLOW_SIGN_FGLD_FB,dataList);
            setBlyj(commentDtoMap,FLOW_SIGN_BMFB1,dataList);
            setBlyj(commentDtoMap,FLOW_SIGN_BMFB2,dataList);
            setBlyj(commentDtoMap,FLOW_SIGN_BMFB3,dataList);
            setBlyj(commentDtoMap,FLOW_SIGN_BMFB4,dataList);

            /**
             * 2018-01-12
             * 送审日期和送审结束时间不用传了。
             * 送审日期我默认为调用你们接口成功的时间，结束时间默认为你调用我们接口成功的时间
             */
            //dataMap.put("sssj", (new Date()).getTime());// 送审日期
            //dataMap.put("psjssj", (new Date()).getTime());// 评审结束时间
            boolean isHaveWP = (mainWP == null) ? false : true;
            if (isHaveWP) {
                dataMap.put("psfs", IFResultCode.PSFS.getCodeByValue(mainWP.getReviewType()));// 评审方式
                //2.2工作方案环节处理意见(主工作方案分管领导意见)
                psgcMap = new HashMap<String, Object>();
                psgcMap.put("blhj", "3");// 办理环节
                psgcMap.put("psblyj", mainWP.getLeaderSuggesttion());// 办理意见
                psgcMap.put("blr", mainWP.getLeaderName());// 办理人
                psgcMap.put("blsj", mainWP.getLeaderDate().getTime());// 办理时间
                dataList.add(psgcMap);
            } else {
                // 不做工作方案，评审方式属于自评
                dataMap.put("psfs", "2");
            }

            if (!Validate.isObject(dispatchDoc)) {
                return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SFGW_03.getCode(), IFResultCode.IFMsgCode.SZEC_SFGW_03.getValue());
            }
            dataMap.put("xmpsyd", dispatchDoc.getReviewAbstract());// 项目评审要点
            dataMap.put("sb", dispatchDoc.getDeclareValue() == null ? null : (dispatchDoc.getDeclareValue()).setScale(2, BigDecimal.ROUND_HALF_UP));// 申报投资额（万元）
            dataMap.put("sd", dispatchDoc.getAuthorizeValue() == null ? null : (dispatchDoc.getAuthorizeValue()).setScale(2, BigDecimal.ROUND_HALF_UP));// 审定投资额（万元）
            dataMap.put("hjz", dispatchDoc.getExtraValue() == null ? null : (dispatchDoc.getExtraValue()).setScale(2, BigDecimal.ROUND_HALF_UP));// 核减（增）投资额（万元）
            dataMap.put("hjzl", dispatchDoc.getExtraRate() == null ? null : (dispatchDoc.getExtraRate()).setScale(2, BigDecimal.ROUND_HALF_UP));// 核减（增）率
            dataMap.put("psbz", dispatchDoc.getRemark());// 备注
            dataMap.put("xmjsbyx", dispatchDoc.getProjectBuildNecess());// 项目建设必要性
            dataMap.put("jsnrjgm", dispatchDoc.getBuildSizeContent());// 建设内容及规模
            dataMap.put("tzksjzjly", dispatchDoc.getFundTotalOrigin());// 投资估算及资金来源
            dataMap.put("xyjdgzyq", dispatchDoc.getNextWorkPlan());// 下一阶段工作要求

            //2.3 发文环节处理意见(主任意见)
            psgcMap = new HashMap<String, Object>();
            psgcMap.put("blhj", "4");// 办理环节
            psgcMap.put("psblyj", dispatchDoc.getDirectorSuggesttion());// 办理意见
            psgcMap.put("blr", dispatchDoc.getDirectorName());// 办理人
            psgcMap.put("blsj", dispatchDoc.getDirectorDate().getTime());// 办理时间
            dataList.add(psgcMap);

            //上传评审报告附件
            ArrayList<HashMap<String, Object>> fjList = new ArrayList<HashMap<String, Object>>();
            List<SysFile> fileList = sysFileRepo.queryFileList(sign.getSignid(), "评审报告");
            if (Validate.isList(fileList)) {
                for (SysFile sf : fileList) {
                    HashMap<String, Object> fjMap = new HashMap<String, Object>();
                    fjMap.put("url", sysFileService.getLocalUrl() + "/file/remoteDownload/" + sf.getSysFileId());
                    fjMap.put("filename", sf.getShowName());
                    fjMap.put("tempName", sf.getCreatedBy());
                    fjList.add(fjMap);
                }
                dataMap.put("psbg", fjList);
            }
            params.put("dataMap", JSON.toJSONString(dataMap));
            params.put("dataList", JSON.toJSONString(dataList));

            HttpResult hst = httpClientOperate.doPost(fgwUrl, params);
            FGWResponse fGWResponse = JSON.toJavaObject(JSON.parseObject(hst.getContent()), FGWResponse.class);
            //成功
            if (Constant.EnumState.PROCESS.getValue().equals(fGWResponse.getRestate())) {
                return new ResultMsg(true, IFResultCode.IFMsgCode.SZEC_SEND_OK.getCode(), "项目【" + sign.getProjectname() + "(" + sign.getFilecode() + ")】回传数据给发改委成功！");
            } else {
                return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SEND_ERROR.getCode(),
                        "项目【" + sign.getProjectname() + "(" + sign.getFilecode() + ")】回传数据给发改委失败！" + fGWResponse.getRedes() + "<br>");
            }
        } catch (Exception e) {
            return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_DEAL_ERROR.getCode(),
                    "项目【" + sign.getProjectname() + "(" + sign.getFilecode() + ")】回传数据给发改委异常！" + e.getMessage());
        }
    }

    /**
     * 封装办理意见
     * @param commentDtoMap
     * @param nodeKey
     * @param dataList
     */
    private void setBlyj(Map<String, CommentDto> commentDtoMap, String nodeKey, ArrayList<HashMap<String, Object>> dataList) {
        if(commentDtoMap.get(nodeKey) != null){
            CommentDto commentDto = commentDtoMap.get(nodeKey);
            HashMap<String, Object> fgldMap = new HashMap<String, Object>();
            fgldMap.put("blhj", "2");// 办理环节
            fgldMap.put("psblyj", commentDto.getComments());// 办理意见
            fgldMap.put("blr", commentDto.getUserName());// 办理人
            fgldMap.put("blsj", commentDto.getCommentDate().getTime());// 办理时间
            dataList.add(fgldMap);
        }
    }

    /**
     * 获取回传给委里的接口地址
     *
     * @return
     */
    @Override
    public String getReturnUrl() {
        String returnUrl = "";
        SysConfigDto sysConfigDto = sysConfigService.findByKey(RETURN_FGW_URL.getValue());
        if (sysConfigDto != null) {
            returnUrl = sysConfigDto.getConfigValue();
        } else {
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            returnUrl = propertyUtil.readProperty(IFResultCode.FGW_PROJECT_IFS);
        }
        return returnUrl;
    }

    /**
     * 获取回预签收地址
     *
     * @return
     */
    @Override
    public String getPreReturnUrl() {
        String returnUrl = "";
        SysConfigDto sysConfigDto = sysConfigService.findByKey(FGW_PRE_PROJECT_IFS.getValue());
        if (sysConfigDto != null) {
            returnUrl = sysConfigDto.getConfigValue();
        } else {
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            returnUrl = propertyUtil.readProperty(IFResultCode.FGW_PRE_PROJECT_IFS);
        }
        return returnUrl;
    }


    private ArrayList<HashMap<String, Object>> checkFile(List<SysFile> fileList, List<String> checkNameArr, String loaclUrl) {
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
