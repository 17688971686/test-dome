
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;


public class SignDto  {

    private String filecode;

    private String projectcode;

    private BigDecimal declaration;

    private String reviewstage;

    private String ispresign;

    private String projectname;

    private String ischangeEstimate;

    private String maindepetid;

    private String maindeptName;

    private String mainDeptUserName;

    private String maindepetcontactuserid;

    private String assistdeptid;

    private String assistdeptName;

    private String assistDeptUserName;

    private String assistdeptcontactuserid;

    private String designcompanyid;

    private String designcompanyName;

    private String builtcompanyid;

    private String builtcompanyName;

    private String urgencydegree;

    private String yearplantype;

    private String secrectlevel;

    private String sendusersign;


    //建议书项目处理表份数
    private Integer sugProDealCount;
    //建议书项目处理表是否有原件
    private String sugProDealOriginal;
    //建议书项目处理表是否有复印件
    private String sugProDealCopy;

    //建议书文件处理表份数
    private Integer sugFileDealCount;

    //建议书文件处理表是否有原件
    private String sugFileDealOriginal;

    //建议书文件处理表是否有复印件
    private String sugFileDealCopy;


    //建议书项目单位申报表份数
    private Integer sugOrgApplyCount;

    //建议书项目单位申报表是否有原件
    private String sugOrgApplyOriginal;

    //建议书项目单位申报表是否有复印件
    private String sugOrgApplyCopy;


    //建议书项目单位请示报告份数
    private Integer sugOrgReqCount;
    //建议书项目单位请示报告是否有原件
    private String sugOrgReqOriginal;

    //建议书项目单位请示报告是否有复印件
    private String sugOrgReqCopy;

    //资金申请报告份数
    private Integer capitalAppReportCount;

    //资金申请报告是否有原件
    private String capitalAppReportOriginal;

    //项目建议书份数
    private Integer sugProAdviseCount;

    //项目建议书是否有原件
    private String sugProAdviseOriginal;

    //项目建议书是否有复印件
    private String sugProAdviseCopy;

    //项目建议书电子文档份数
    private Integer proSugEledocCount;

    //项目建议书电子文档是否有原件
    private String proSugEledocOriginal;

    //项目建议书电子文档是否有复印件
    private String proSugEledocCopy;

    //申报投资
    private BigDecimal appalyInvestment;

    //建议书相关会议纪要份数
    private Integer sugMeetCount;

    //建议书相关会议纪要是否有原件
    private String sugMeetOriginal;

    //建议书相关会议纪要是否有复印件
    private String sugMeetCopy;

    //第二阶段：项目可研究性阶段

    //1.可研项目处理表份数
    private Integer studyProDealCount;

    //可研项目处理表是否有原件
    private String studyPealOriginal;

    //可研项目处理表是否有复印件
    private String studyProDealCopy;


    //2.可研文件处理表份数
    private Integer studyFileDealCount;

    //可研文件处理表是否有原件
    private String studyFileDealOriginal;

    //可研文件处理表是否有复印件
    private String studyFileDealCopy;

    //3.可研项目单位申报表份数
    private Integer studyOrgApplyCount;

    //可研项目单位申报表是否有原件
    private String studyOrgApplyOriginal;

    //可研项目单位申报表是否有复印件
    private String studyOrgApplyCopy;

    //4.可研项目单位请示报告份数
    private Integer studyOrgReqCount;

    //可研项目单位请示报告是否有原件
    private String studyOrgReqOriginal;

    //可研项目单位请示报告是否有复印件
    private String studyOrgReqCopy;

    //5.可研项目建议书批复
    private Integer studyProSugCount;

    //可研项目建议书批复是否有原件
    private String studyProSugOriginal;

    //可研项目建议书批复是否有复印件
    private String studyProSugCopy;

    //6.可研相关会议纪要份数
    private Integer studyMeetCount;

    //可研相关会议是否有原件
    private String studyMeetOriginal;

    //可研相关会议是否有复印件
    private String studyMeetCopy;

    //第二阶段 、右边
    //1.可研环保批复文件份数
    private Integer envproReplyCount;

    //可研环保批复文件是否有原件
    private String envproReplyOriginal;

    //可研环保批复文件是否有复印件
    private String envproReplyCopy;

    //2.可研规划选址批文份数
    private Integer planAddrCount;

    //可研规划选址批文是否有原件
    private String planAddrOriginal;

    //可研规划选址批文是否有复印件
    private String planAddrCopy;

    //3.可研报告份数
    private Integer reportCount;

    //可研报告是否有原件
    private String reportOrigin;

    //可研报告是否有复印件
    private String reportCopy;

    //4可研报告电子文档份数
    private Integer eledocCount;

    //可研报告电子文档是否有原件
    private String eledocOriginal;

    //可研报告电子文档是否有复印件
    private String eledocCopy;

    //5.可研节能报告份数
    private Integer energyCount;

    //可研节能报告是否有原件
    private String energyOriginal;

    //可研节能报告是否有复印件
    private String energyCopy;

    //S (进口设备) 政府采购进口产品申报份数
    private Integer governmentPurchasCount;

    //政府采购进口产品申报是否有原件
    private String governmentPurchasOriginal;

    // 政府采购进口产品申报是否有复印件
    private String governmentPurchasCopy;

    // 专家论证意见份数
    private Integer expertArgumentCount;

    //专家论证意见是否有原件
    private String expertArgumentOriginal;

    //专家论证意见是否有复印件
    private String expertArgumentCopy;

    //发改委意见份数
    private Integer developmentCount;

    //发改委意见是否有原件
    private String developementOriginal;

    //发改委意见是否复印件
    private String developementCopy;

    //文件处理表份数
    private Integer officialDisposeCount;

    //文件处理表是否有原件
    private String officialDisposeOriginal;

    //文件处理表是否复印件
    private String officialDisposeCopy;

    //主管部门意见分数
    private Integer managerDeptCount;

    //主管部门意见是否有原件
    private String managerDeptOriginal;

    //主管部门意见是否复印件
    private String managerDeptCopy;

    //进口产品目录分数
    private Integer importProductCount;

    //进口产品目录是否有原件
    private String importProductOriginal;

    //进口产品目录是否复印件
    private String importProductCopy;

    //其他项目资料分数
    private Integer ortherProjectCount;

    //其他项目资料是否有原件
    private String ortherProjectOriginal;

    //其他项目资料是否复印件
    private String ortherProjectCopy;

    //设备说明书分数
    private Integer sprcialDevicesCount;

    //设备说明书是否有原件
    private String sprcialDevicesOriginal;

    //设备说明书是否有复印件
    private String sprcialDevicesCopy;

    //E (进口设备)

    //S (设备清单(国产))

    //项目申报表是否有分数
    private Integer projectDeclareCount;

    //项目申报表是否有原件
    private String projectDeclareOriginal;

    //项目申报表是否有复印件
    private String projectDeclareCopy;

    //项目申请报告分数
    private Integer projectApplyReportCount;

    //项目申请报告是否有原件
    private String projectApplyReportOriginal;

    //项目申请报告是否有复印件
    private String projectApplyReportCopy;

    //采购设备清单分数
    private Integer purchasDeviceCount;

    //采购设备清单是否有原件
    private String purchasDeviceOriginal;

    //采购设备清单是否有复印件
    private String purchasDeviceCopy;

    //电子文档分数
    private Integer electronicDocumentCount;

    //电子文档是否有原件
    private String electronicDocumentOriginal;

    //电子文档是否有复印件
    private String electronicDocumentCopy;

    //采购合同书分数
    private Integer purchasDevicePactCount;

    //采购合同书是否有原件
    private String purchasDevicePactOriginal;

    //采购合同书是否有复印件
    private String purchasDevicePactCopy;

    //项目核准文件份数
    private Integer projectApproveFileCount;

    //项目核准文件是否有原件
    private String projectApproveFileOriginal;

    //项目核准文件是否有复印件
    private String projectApproveFileCopy;

    //营业执照分数
    private Integer businessLicenseCount;

    //营业执照是否有原件
    private String businessLicenseOriginal;

    //营业执照是否有复印件
    private String businessLicenseCopy;

    //法人身份证分数
    private Integer legalPersoncCardCount;

    //法人身份证是否有原件
    private String legalPersoncCardOriginal;

    //法人身份证复印件
    private String legalPersoncCardCopy;

    //E (设备清单(国产))

    //S (项目概算)

    //环境影响评价报告分数
    private Integer environmentalEffectCount;

    //环境影响评价报告是否有原件
    private String environmentalEffectOriginal;

    //环境影响评价报告是否有复印件
    private String environmentalEffectCopy;

    //用地规划许可证分数
    private Integer landPlanningLicenseCount;

    //用地规划许可证是否有原件
    private String landPlanningLicenseOriginal;

    //用地规划许可证是否有复印件
    private String landPlanningLicenseCopy;

    //地质勘察报告分数
    private Integer geologicalSurveyCount;

    //地质勘察报告是否有原件
    private String geologicalSurveyOriginal;

    //地质勘察报告是否有复印件
    private String geologicalSurveyCopy;

    //设计说明分数
    private Integer descriptionDesignCount;

    //设计说明是否有原件
    private String descriptionDesignOriginal;

    //设计说明是否有复印件
    private String descriptionDesignCopy;

    //工程概算书分数
    private Integer projectBudgetCount;

    //工程概算书是否有原件
    private String projectBudgetOriginal;

    //工程概算书是否有复印件
    private String projectBudgetCopy;

    //初步设计图纸分数
    private Integer preliminaryDrawingCount;

    //初步设计图纸是否有原件
    private String preliminaryDrawingOriginal;

    //初步设计图纸是否有复印件
    private String preliminaryDrawingCopy;

    //施工设计图纸分数
    private Integer constructionDrawingsCount;

    //施工设计图纸是否有原件
    private String constructionDrawingsOriginal;

    //施工设计图纸是否有复印件
    private String constructionDrawingsCopy;
    //E (项目概算)


}