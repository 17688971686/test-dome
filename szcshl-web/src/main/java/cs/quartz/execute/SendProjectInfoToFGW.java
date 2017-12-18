package cs.quartz.execute;

import com.alibaba.fastjson.JSON;
import cs.ahelper.HttpClientOperate;
import cs.ahelper.HttpResult;
import cs.common.Constant;
import cs.common.FGWResponse;
import cs.common.IFResultCode;
import cs.common.ResultMsg;
import cs.common.utils.PropertyUtil;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.project.SignDispaWork;
import cs.domain.project.Sign_;
import cs.domain.sys.Log;
import cs.service.project.SignDispaWorkService;
import cs.service.project.SignService;
import cs.service.sys.LogService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 发送信息项目信息给委里
 * Created by ldm on 2017/12/18.
 */
public class SendProjectInfoToFGW implements Job {

    private static final String FGW_PROJECT_IFS = "FGW_PROJECT_IFS";
    @Autowired
    private SignDispaWorkService signDispaWorkService;
    @Autowired
    private SignService signService;
    @Autowired
    private LogService logService;
    @Autowired
    private HttpClientOperate httpClientOperate;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //1、查询还未发送给发改委的项目信息（在办或者已办结，未发送的项目）
        List<SignDispaWork> unSendList = signDispaWorkService.findUnSendFGWList();
        if (Validate.isList(unSendList)) {
            //部分参数
            int sucessCount = 0, errorCount = 0, totalCount = unSendList.size();
            StringBuffer errorBuffer = new StringBuffer();
            List<String> sucessIdList = new ArrayList<>();
            ResultMsg resultMsg = null;

            // 接口地址
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            String endpoint = propertyUtil.readProperty(FGW_PROJECT_IFS);

            if (Validate.isString(endpoint)) {
                for (int i = 0, l = totalCount; i < l; i++) {
                    SignDispaWork signDispaWork = unSendList.get(i);
                    try {
                        //1、评审意见对象
                        Map<String, Object> dataMap = new HashMap<>();
                        // dataMap.put("xmmc", "HLT备案11201644");// 项目名称
                        // dataMap.put("jsdw", "测试建设单位");// 建设单位
                        // dataMap.put("swrq", sdf.parse("2017/11/20").getTime());// 收文日期
                        // dataMap.put("xmbm", "S-2017-A01-500046-11-01");// 项目编码

                        dataMap.put("swbh", signDispaWork.getFilecode());// 收文编号
                        dataMap.put("psfs", IFResultCode.PSFS.getCodeByValue(signDispaWork.getReviewType()));// 评审方式
                        dataMap.put("sssj", (new Date()).getTime());// 送审日期
                        dataMap.put("psjssj", (new Date()).getTime());// 评审结束时间
                        dataMap.put("xmpsyd", "项目评审要点项目评审要点项目评审要点项目评审要点项目评审要点项目评审要点项目评审要点");// 项目评审要点
                        dataMap.put("sb", 1.2);// 申报投资额（万元）
                        dataMap.put("sd", 2.1);// 审定投资额（万元）
                        dataMap.put("hjz", 6.2);// 核减（增）投资额（万元）
                        dataMap.put("hjzl", 58.62);// 核减（增）率
                        dataMap.put("psbz", "备注备注备注备注备注备注备注");// 备注
                        dataMap.put("xmjsbyx", "项目建设必要性项目建设必要性项目建设必要性项目建设必要性项目建设必要性");// 项目建设必要性
                        dataMap.put("jsnrjgm", "建设内容及规模建设内容及规模建设内容及规模建设内容及规模建设内容及规模建设内容及规模建设内容及规模");// 建设内容及规模
                        dataMap.put("tzksjzjly", "投资估算及资金来源投资估算及资金来源投资估算及资金来源投资估算及资金来源投资估算及资金来源投资估算及资金来源投资估算及资金来源");// 投资估算及资金来源
                        dataMap.put("xyjdgzyq", "下一阶段工作要求下一阶段工作要求下一阶段工作要求");// 下一阶段工作要求

                        //2、附件列表
                        ArrayList<HashMap<String, Object>> fjList = new ArrayList<>();
                        HashMap<String, Object> fjMap = new HashMap<String, Object>();
                        fjMap.put("url", "http://localhost:8089/FGWPM/LEAP/Download/default/2017/12/10/pdftest.pdf");
                        fjMap.put("filename", "评审报告pdf");
                        fjMap.put("tempName", "黄凌涛");
                        fjList.add(fjMap);
                        fjMap = new HashMap<String, Object>();
                        fjMap.put("url", "http://localhost:8089/FGWPM/LEAP/Download/default/2017/12/10/wordtest.docx");
                        fjMap.put("filename", "评审报告word");
                        fjMap.put("tempName", "黄凌涛");
                        fjList.add(fjMap);
                        dataMap.put("psbg", fjList);// 评审报告（需上传pdf和word文档）

                        fjList = new ArrayList<HashMap<String, Object>>();
                        fjMap = new HashMap<String, Object>();
                        fjMap.put("url", "http://localhost:8089/FGWPM/LEAP/Download/default/2017/12/10/pdftest.pdf");
                        fjMap.put("filename", "投资估算审核表pdf");
                        fjMap.put("tempName", "黄凌涛");
                        fjList.add(fjMap);
                        fjMap = new HashMap<String, Object>();
                        fjMap.put("url", "http://localhost:8089/FGWPM/LEAP/Download/default/2017/12/10/wordtest.docx");
                        fjMap.put("filename", "投资估算审核表word");
                        fjMap.put("tempName", "黄凌涛");
                        fjList.add(fjMap);
                        dataMap.put("tzgsshb", fjList);// 投资估算审核表（需上传pdf和word文档）
                        fjList = new ArrayList<HashMap<String, Object>>();
                        dataMap.put("zjpsyj", fjList);// 专家评审意见
                        fjList = new ArrayList<HashMap<String, Object>>();
                        dataMap.put("zjpsmd", fjList);// 专家评审名单
                        fjList = new ArrayList<HashMap<String, Object>>();
                        dataMap.put("qtfj", fjList);// 其它

                        //3、办理意见
                        ArrayList<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
                        HashMap<String, Object> psgcMap = new HashMap<String, Object>();
                        psgcMap.put("blhj", IFResultCode.PSGCBLHJ.getCodeByValue("124"));// 办理环节
                        psgcMap.put("psblyj", "办理意见办理意见办理意见办理意见办理意见");// 办理意见
                        psgcMap.put("blr", "办理人");// 办理人
                        psgcMap.put("blsj", (new Date()).getTime());// 办理时间
                        fjList = new ArrayList<HashMap<String, Object>>();
                        fjMap = new HashMap<String, Object>();
                        fjMap.put("url", "http://localhost:8089/FGWPM/LEAP/Download/default/2017/12/10/pdftest.pdf");
                        fjMap.put("filename", "投资估算审核表pdf");
                        fjMap.put("tempName", "黄凌涛");
                        fjList.add(fjMap);
                        psgcMap.put("cl", fjList);// 材料（附件）
                        dataList.add(psgcMap);

                        Map<String, String> params = new HashMap<>();
                        params.put("dataMap", JSON.toJSONString(dataMap));
                        params.put("dataList", JSON.toJSONString(dataList));

                        HttpResult hst = httpClientOperate.doPost(endpoint, params);
                        FGWResponse fGWResponse = JSON.toJavaObject(JSON.parseObject(hst.getContent()), FGWResponse.class);
                        //成功
                        if(Constant.EnumState.PROCESS.getValue().equals(fGWResponse.getRestate())){
                            sucessCount ++ ;
                            sucessIdList.add(signDispaWork.getSignid());
                        }else{
                            errorCount ++;
                            errorBuffer.append("项目【"+signDispaWork.getSignid()+"】回传数据给发改委失败！"+fGWResponse.getRedes()+"\r\n");
                        }
                    } catch (Exception e) {
                        errorCount ++;
                        errorBuffer.append("项目【"+signDispaWork.getSignid()+"】回传数据给发改委异常！"+"\r\n");
                    }
                }
                if(sucessCount == 0){
                    resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), errorBuffer.toString());
                }else if(errorCount == 0){
                    resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(), "本次回传数据成功，总共回传【"+sucessCount+"】个项目数据！");
                }else {
                    String msg = "本次总共回传【"+totalCount+"】，成功【"+sucessCount+"】个，失败"+errorCount+"】个；"+errorBuffer.toString();
                    resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(), msg);
                }
                //更改成功发送的项目状态
                if(Validate.isList(sucessIdList)){
                    signService.updateSignState(StringUtils.join(sucessIdList,","), Sign_.isSendFGW.getName(), Constant.EnumState.YES.getValue());
                }
            } else {
                resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "数据回传失败，获取不到接口地址！");
            }

            //添加日记记录
            Log log = new Log();
            log.setCreatedDate(new Date());
            log.setLogCode(resultMsg.getReCode());
            log.setMessage(resultMsg.getReMsg());
            log.setModule(Constant.LOG_MODULE.INTERFACE.getValue() + "【项目回调接口】");
            log.setResult(resultMsg.isFlag() ? Constant.EnumState.YES.getValue() : Constant.EnumState.NO.getValue());
            log.setLogger(this.getClass().getName() + ".execute");
            //优先级别高
            log.setLogLevel(Constant.EnumState.PROCESS.getValue());
            logService.save(log);
        }

    }
}
