package cs.service.restService;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import cs.ahelper.HttpClientOperate;
import cs.ahelper.HttpResult;
import cs.common.FGWResponse;
import cs.common.IFResultCode;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.constants.ProjectConstant;
import cs.common.constants.SysConstants;
import cs.common.sysprop.BusinessProperties;
import cs.common.utils.SessionUtil;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.project.DispatchDoc;
import cs.domain.project.Sign;
import cs.domain.sys.Log;
import cs.domain.sys.SysFile;
import cs.model.project.CommentDto;
import cs.model.project.DispatchDocDto;
import cs.model.project.SignDto;
import cs.model.project.WorkProgramDto;
import cs.model.sys.SysConfigDto;
import cs.model.sys.SysFileDto;
import cs.repository.repositoryImpl.project.DispatchDocRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.service.project.DispatchDocService;
import cs.service.project.SignService;
import cs.service.sys.LogService;
import cs.service.sys.SysConfigService;
import cs.service.sys.SysFileService;
import cs.spring.SpringContextUtil;
import cs.threadtask.FileDownLoadThread;
import cs.threadtask.MsgThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static cs.common.constants.FlowConstant.*;
import static cs.common.constants.SysConstants.SUPER_ACCOUNT;
import static cs.common.constants.SysConstants.SYS_BUSI_PROP_BEAN;

/**
 * 项目接口实现类
 * @author ldm on 2017/12/20.
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
    private SysFileService sysFileService;
    @Autowired
    private DispatchDocService dispatchDocService;
    @Autowired
    private DispatchDocRepo dispatchDocRepo;
    @Autowired
    private LogService logService;
    /**
     * 委里推送项目
     *
     * @param signDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg pushProject(SignDto signDto, boolean isGetFiles,Log log) {
        if (signDto == null) {
            if(log != null){
                log.setResult(Constant.EnumState.NO.getValue());
                log.setLogCode(IFResultCode.IFMsgCode.SZEC_SIGN_01.getCode());
                log.setMessage(IFResultCode.IFMsgCode.SZEC_SIGN_01.getValue());
                logService.save(log);
            }
            return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SIGN_01.getCode(), IFResultCode.IFMsgCode.SZEC_SIGN_01.getValue());
        }
        if (!Validate.isString(signDto.getFilecode())) {
            if(log != null){
                log.setResult(Constant.EnumState.NO.getValue());
                log.setLogCode(IFResultCode.IFMsgCode.SZEC_SIGN_02.getCode());
                log.setMessage(IFResultCode.IFMsgCode.SZEC_SIGN_02.getValue());
                logService.save(log);
            }
            return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SIGN_02.getCode(), IFResultCode.IFMsgCode.SZEC_SIGN_02.getValue());
        }
        //1、项目评审阶段判断
        if (!Validate.isString(signDto.getReviewstage())) {
            if(log != null){
                log.setResult(Constant.EnumState.NO.getValue());
                log.setLogCode(IFResultCode.IFMsgCode.SZEC_SIGN_03.getCode());
                log.setMessage(IFResultCode.IFMsgCode.SZEC_SIGN_03.getValue());
                logService.save(log);
            }
            return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SIGN_03.getCode(), IFResultCode.IFMsgCode.SZEC_SIGN_03.getValue());
        }
        String stageCode = signDto.getReviewstage();
        ProjectConstant.REVIEW_STATE_ENUM reviewStateEnum = ProjectConstant.REVIEW_STATE_ENUM.getByEnCode(stageCode);
        if (!Validate.isObject(reviewStateEnum)) {
            if(log != null){
                log.setResult(Constant.EnumState.NO.getValue());
                log.setLogCode(IFResultCode.IFMsgCode.SZEC_SIGN_04.getCode());
                log.setMessage(IFResultCode.IFMsgCode.SZEC_SIGN_04.getValue());
                logService.save(log);
            }
            return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SIGN_04.getCode(), IFResultCode.IFMsgCode.SZEC_SIGN_04.getValue());
        }
        //对应系统的阶段名称
        signDto.setReviewstage(reviewStateEnum.getZhCode());
        //是否是项目概算流程
        if (ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.getEnCode().equals(stageCode) || Validate.isString(signDto.getIschangeEstimate())) {
            signDto.setIsassistflow(Constant.EnumState.YES.getValue());
        } else {
            signDto.setIsassistflow(Constant.EnumState.NO.getValue());
        }
        //申报金额调整(由于委里定义的接口，Declaration这个字段为申报金额字段，所以这里要调整下)；
        signDto.setAppalyInvestment(signDto.getDeclaration());

        //如果是概算项目，并且是调概项目，则在项目名称后加“(调概项目)”
        if(ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.equals(signDto.getReviewstage())
                && Constant.EnumState.YES.getValue().equals(signDto.getIschangeEstimate())){
            signDto.setProjectname(signDto.getProjectname() + "(调概项目)");
        }

        //定义返回对象
        ResultMsg resultMsg = null;
        try {
            resultMsg = signService.createSign(signDto);
            if (resultMsg.isFlag()) {
                boolean isLoginUser = Validate.isString(SessionUtil.getUserId());
                Sign sign = (Sign) resultMsg.getReObj();
                checkDownLoadFile(resultMsg, isGetFiles, sign.getSignid(), signDto.getSysFileDtoList(), isLoginUser ? SessionUtil.getUserId() : SUPER_ACCOUNT,
                        Constant.SysFileType.SIGN.getValue(), Constant.SysFileType.FGW_FILE.getValue(),log);
            }
        } catch (Exception e) {
            resultMsg = new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SAVE_ERROR.getCode(), IFResultCode.IFMsgCode.SZEC_SAVE_ERROR.getValue() + e.getMessage());
        } finally {

        }
        return resultMsg;
    }

    public void checkDownLoadFile(ResultMsg resultMsg, boolean isGetFiles, String businessId, List<SysFileDto> sysFileDtoList,
                                  String userId, String mainType, String busiType,Log log){
        //如果获取附件
        if (isGetFiles && Validate.isList(sysFileDtoList)) {
            //获取传送过来的附件
            ConcurrentLinkedQueue<SysFileDto> fileQueue = new ConcurrentLinkedQueue(sysFileDtoList);
            //手动创建线程池
            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("附件下载线程：thread-filedownload-runner-%d").build();
            ExecutorService threadPool = new ThreadPoolExecutor(1,5,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(),namedThreadFactory);
            threadPool.execute(new FileDownLoadThread(sysFileService,fileQueue,businessId,userId,mainType,busiType,log));
            threadPool.shutdown();
        }
        resultMsg.setFlag(true);
        resultMsg.setReCode(IFResultCode.IFMsgCode.SZEC_SAVE_OK.getCode());
        resultMsg.setReMsg(IFResultCode.IFMsgCode.SZEC_SAVE_OK.getValue());
    }


    /**
     * 预签收项目
     * @param signDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg pushPreProject(SignDto signDto, boolean isGetFiles) {
        if (signDto == null) {
            return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SIGN_01.getCode(), IFResultCode.IFMsgCode.SZEC_SIGN_01.getValue());
        }
        if (!Validate.isString(signDto.getFilecode())) {

            return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SIGN_02.getCode(), IFResultCode.IFMsgCode.SZEC_SIGN_02.getValue());
        }
        String stageCode = signDto.getReviewstage();
        ProjectConstant.REVIEW_STATE_ENUM reviewStateEnum = ProjectConstant.REVIEW_STATE_ENUM.getByEnCode(stageCode);
        if (!Validate.isObject(reviewStateEnum)) {

            return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SIGN_04.getCode(), IFResultCode.IFMsgCode.SZEC_SIGN_04.getValue());
        }
        //对应系统的阶段名称
        signDto.setReviewstage(reviewStateEnum.getZhCode());
        //是否是项目概算流程
        if (ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.getEnCode().equals(stageCode) || Validate.isString(signDto.getIschangeEstimate())) {
            signDto.setIsassistflow(Constant.EnumState.YES.getValue());
        } else {
            signDto.setIsassistflow(Constant.EnumState.NO.getValue());
        }
        //申报金额调整(由于委里定义的接口，Declaration这个字段为申报金额字段，所以这里要调整下)；
        signDto.setAppalyInvestment(signDto.getDeclaration());
        //如果是概算项目，并且是调概项目，则在项目名称后加“(调概项目)”
        if(ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.equals(signDto.getReviewstage())
                && Constant.EnumState.YES.getValue().equals(signDto.getIschangeEstimate())){
            signDto.setProjectname(signDto.getProjectname() + "(调概项目)");
        }
        ResultMsg resultMsg = null;
        try {
            resultMsg = signService.reserveAddSign(signDto);
            if (resultMsg.isFlag()) {
                boolean isLoginUser = Validate.isString(SessionUtil.getUserId());
                Sign sign = (Sign) resultMsg.getReObj();
                checkDownLoadFile(resultMsg, isGetFiles, sign.getSignid(), signDto.getSysFileDtoList(), isLoginUser ? SessionUtil.getUserId() : SUPER_ACCOUNT,
                        Constant.SysFileType.SIGN.getValue(), Constant.SysFileType.FGW_FILE.getValue(),null);
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
    public ResultMsg setToFGW(SignDto sign, WorkProgramDto mainWP, DispatchDocDto dispatchDoc, String fgwUrl,String localUrl, Map<String, CommentDto> commentDtoMap,List<SysFileDto> sysFileDtoList) {
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
            setBlyj(commentDtoMap, FLOW_SIGN_FGLD_FB, dataList);
            setBlyj(commentDtoMap, FLOW_SIGN_BMFB1, dataList);
            setBlyj(commentDtoMap, FLOW_SIGN_BMFB2, dataList);
            setBlyj(commentDtoMap, FLOW_SIGN_BMFB3, dataList);
            setBlyj(commentDtoMap, FLOW_SIGN_BMFB4, dataList);


            boolean isHaveWP = Validate.isObject(mainWP) ? true : false;
            if (isHaveWP && Validate.isString(mainWP.getLeaderSuggesttion()) && Validate.isString(mainWP.getLeaderName()) && Validate.isObject(mainWP.getLeaderDate())) {
                dataMap.put("psfs", IFResultCode.PSFS.getCodeByValue(mainWP.getReviewType()));// 评审方式
                //2.2工作方案环节处理意见(主工作方案分管领导意见)
                psgcMap = new HashMap<>();
                psgcMap.put("blhj", "3");// 办理环节
                psgcMap.put("psblyj", mainWP.getLeaderSuggesttion());// 办理意见
                psgcMap.put("blr", mainWP.getLeaderName());// 办理人
                psgcMap.put("blsj", mainWP.getLeaderDate().getTime());// 办理时间
                dataList.add(psgcMap);
            } else {
                // 不做工作方案，评审方式属于自
                dataMap.put("psfs", "2");
            }

            if (!Validate.isObject(dispatchDoc)) {
                return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SFGW_03.getCode(), IFResultCode.IFMsgCode.SZEC_SFGW_03.getValue());
            }
            dataMap.put("xmpsyd", StringUtil.getDefaultValue(dispatchDoc.getReviewAbstract(), "无"));// 项目评审要点
            dataMap.put("sb", dispatchDoc.getDeclareValue() == null ? null : (dispatchDoc.getDeclareValue()).setScale(2, BigDecimal.ROUND_HALF_UP));// 申报投资额（万元）
            dataMap.put("sd", dispatchDoc.getAuthorizeValue() == null ? null : (dispatchDoc.getAuthorizeValue()).setScale(2, BigDecimal.ROUND_HALF_UP));// 审定投资额（万元）
            dataMap.put("hjz", dispatchDoc.getExtraValue() == null ? null : (dispatchDoc.getExtraValue()).setScale(2, BigDecimal.ROUND_HALF_UP));// 核减（增）投资额（万元）
            dataMap.put("hjzl", dispatchDoc.getExtraRate() == null ? null : (dispatchDoc.getExtraRate()).setScale(2, BigDecimal.ROUND_HALF_UP));// 核减（增）率
            dataMap.put("psbz", StringUtil.getDefaultValue(dispatchDoc.getRemark(), "无"));// 备注
            dataMap.put("xmjsbyx", StringUtil.getDefaultValue(dispatchDoc.getProjectBuildNecess(), "无"));// 项目建设必要性
            dataMap.put("jsnrjgm", StringUtil.getDefaultValue(dispatchDoc.getBuildSizeContent(), "无"));// 建设内容及规模
            dataMap.put("tzksjzjly", StringUtil.getDefaultValue(dispatchDoc.getFundTotalOrigin(), "无"));// 投资估算及资金来源
            dataMap.put("xyjdgzyq", StringUtil.getDefaultValue(dispatchDoc.getNextWorkPlan(), "无"));// 下一阶段工作要求
            boolean isTw = "项目退文".equals(dispatchDoc.getDispatchType());
            dataMap.put("istw", isTw ? "1" : "0");// 是否项目退文

            //2.3 发文环节处理意见(主任意见)
            psgcMap = new HashMap<>();
            psgcMap.put("blhj", "4");// 办理环节
            psgcMap.put("psblyj", dispatchDoc.getDirectorSuggesttion());// 办理意见
            psgcMap.put("blr", dispatchDoc.getDirectorName());// 办理人
            psgcMap.put("blsj", dispatchDoc.getDirectorDate().getTime());// 办理时间
            dataList.add(psgcMap);

            //上传评审报告附件
            ArrayList<HashMap<String, Object>> fjList = new ArrayList<HashMap<String, Object>>();
            if (Validate.isList(sysFileDtoList)) {
                for (SysFileDto sfDto : sysFileDtoList) {
                    HashMap<String, Object> fileMap = new HashMap<String, Object>();
                    fileMap.put("url", localUrl + "/file/remoteDownload/" + sfDto.getSysFileId());
                    fileMap.put("filename", sfDto.getShowName());
                    fileMap.put("tempName", sfDto.getCreatedBy());
                    fjList.add(fileMap);
                }
                dataMap.put("psbg", fjList);
            }
            params.put("dataMap", JSON.toJSONString(dataMap));
            params.put("dataList", JSON.toJSONString(dataList));
            HttpResult hst = httpClientOperate.doPost(fgwUrl, params,true);
            //调用接口成功才开始解析
            if (Validate.isObject(hst) && (200 == hst.getStatusCode())) {
                FGWResponse fGWResponse = JSON.toJavaObject(JSON.parseObject(hst.getContent()), FGWResponse.class);
                //成功
                if (Constant.EnumState.PROCESS.getValue().equals(fGWResponse.getRestate())) {
                    return new ResultMsg(true, IFResultCode.IFMsgCode.SZEC_SEND_OK.getCode(), sign.getProjectname() + "[" + sign.getFilecode() + "]回传成功！\n");
                } else {
                    return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SEND_ERROR.getCode(),
                            sign.getProjectname() + "[" + sign.getFilecode() + "]回传失败！" + fGWResponse.getRedes()+ "\n" );
                }
            } else {
                return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_SEND_ERROR.getCode(),
                        sign.getProjectname() + "[" + sign.getFilecode() + "]回传失败！返回码为：" + hst.getStatusCode() + "!返回内容：" + hst.getContent()+ "\n" );
            }
        } catch (Exception e) {
            return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_DEAL_ERROR.getCode(),
                    sign.getProjectname() + "[" + sign.getFilecode() + "]回传异常！" + e.getMessage()+ "\n" );
        }
    }

    /**
     * 项目批复金额
     * @param signDto
     * @param isGetFiles
     * @return
     */
    @Override
    public ResultMsg signProjAppr(SignDto signDto, boolean isGetFiles) throws MissingServletRequestParameterException {
        ResultMsg resultMsg = new ResultMsg();
        Sign sign = signRepo.findByFilecode(signDto.getFilecode(), Constant.EnumState.DELETE.getValue());
        if (!Validate.isObject(sign) || !Validate.isString(sign.getSignid())) {
            resultMsg.setFlag(false);
            resultMsg.setReCode(IFResultCode.IFMsgCode.SZEC_SIGN_01.getCode());
            resultMsg.setReMsg(IFResultCode.IFMsgCode.SZEC_SIGN_01.getValue());
            return resultMsg;
        }
        //发文跟收文是1V1
        DispatchDoc dispatchDoc = dispatchDocRepo.findById("signid", sign.getSignid());
        if (!Validate.isObject(dispatchDoc) || !Validate.isString(dispatchDoc.getId())) {
            resultMsg.setFlag(false);
            resultMsg.setReCode(IFResultCode.IFMsgCode.DISPATHCH_DOC_DTO_1.getCode());
            resultMsg.setReMsg(IFResultCode.IFMsgCode.DISPATHCH_DOC_DTO_1.getValue());
            return resultMsg;
        }
        dispatchDocService.updateDisApprValue(dispatchDoc.getId(), signDto.getDeclaration());
        boolean isLoginUser = Validate.isString(SessionUtil.getUserId());
        //开始下载pdf
        checkDownLoadFile(resultMsg, isGetFiles, sign.getSignid(), signDto.getSysFileDtoList(), isLoginUser ? SessionUtil.getUserId() : SUPER_ACCOUNT,
                Constant.SysFileType.SIGN.getValue(), "委批复文件",null);
        return resultMsg;
    }

    /**
     * 封装办理意见
     *
     * @param commentDtoMap
     * @param nodeKey
     * @param dataList
     */
    private void setBlyj(Map<String, CommentDto> commentDtoMap, String nodeKey, ArrayList<HashMap<String, Object>> dataList) {
        if (commentDtoMap.get(nodeKey) != null) {
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
       /* SysConfigDto sysConfigDto = sysConfigService.findByKey(RETURN_FGW_URL.getValue());*/
        SysConfigDto sysConfigDto = sysConfigService.findByKey(SysConstants.SYS_CONFIG_ENUM.RETURN_FGW_URL.toString());
        if (Validate.isObject(sysConfigDto)) {
            return sysConfigDto.getConfigValue();
        } else {
            BusinessProperties businessProperties = SpringContextUtil.getBean(SYS_BUSI_PROP_BEAN);
            return businessProperties.getFgwProjIfs();
        }
    }

    @Override
    public String getLocalUrl() {
        String localUrl = "";
        SysConfigDto sysConfigDto = sysConfigService.findByKey(SysConstants.SYS_CONFIG_ENUM.LOCAL_URL.toString());
        if(sysConfigDto != null) {
            localUrl = sysConfigDto.getConfigValue();
        }
        if (Validate.isString(localUrl) && localUrl.endsWith("/")) {
            localUrl = localUrl.substring(0, localUrl.length() - 1);
        }
        return localUrl;
    }

    /**
     * 获取回预签收地址
     *
     * @return
     */
    @Override
    public String getPreReturnUrl() {
        /*SysConfigDto sysConfigDto = sysConfigService.findByKey(FGW_PRE_PROJECT_IFS.getValue());*/
        SysConfigDto sysConfigDto = sysConfigService.findByKey(SysConstants.SYS_CONFIG_ENUM.FGW_PRE_PROJECT_IFS.toString());
        if (sysConfigDto != null) {
            return sysConfigDto.getConfigValue();
        } else {
            BusinessProperties businessProperties = SpringContextUtil.getBean(SYS_BUSI_PROP_BEAN);
            return businessProperties.getFgwPreProjIfs();
        }
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
