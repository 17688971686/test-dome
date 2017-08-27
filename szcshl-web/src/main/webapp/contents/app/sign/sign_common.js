(function () {
    'use strict';

    /**
     * 项目业务类型
     */
    var signBusinessType = {
        PD : "PD",      //评估
        GD : "GD",      //概算
    }

    /**
     * 默认办理人员
     */
    var signDealUser = {
        ZHANG_YF : "张一帆",      //评估
        LI_J : "李军",      //概算
    }

    /**
     * 流程参数
     */
    var signFlowParams = {
        godispatch : "godispatch",          //直接发文
        principaluser : "principaluser",    //项目负责人
        seconduser : "seconduser",           //第二负责人审核
    }

    var service = {
        getBusinessType: getBusinessType,               // 获取项目类型
        getDefaultLeader : getDefaultLeader,            // 获取默认办理的分管主任
        getDefaultZHBYJ:getDefaultZHBYJ,                // 获取默认综合部审批意见
        getFlowParams : getFlowParams,                  // 获取流程参数
        getReviewStage : function(){
            return {
                STAGE_SUG:"项目建议书",
                STAGE_STUDY:"可行性研究报告",
                STAGE_BUDGET:"项目概算",
                APPLY_REPORT :"资金申请报告",
                OTHERS:"其它",
                DEVICE_BILL_HOMELAND:"设备清单（国产）",
                DEVICE_BILL_IMPORT:"设备清单（进口）",
                IMPORT_DEVICE:"进口设备"
            }
        }

    };
    window.signcommon = service;

    //S_获取项目类型
    function getBusinessType() {
        return signBusinessType;
    }//E_getBusinessType

    //S_获取默认办理的分管主任
    function getDefaultLeader(businessType){
        var resultName = "";
        switch(businessType){
            case signBusinessType.PD:
                resultName = signDealUser.ZHANG_YF
                break;
            case signBusinessType.GD:
                resultName = signDealUser.LI_J
                break;
            default:
                ;
        }
        return resultName;
    }//E_getDefaultLeader

    //S_获取默认综合部审批意见
    function getDefaultZHBYJ(businessType){
        var resultOption = "";
        switch(businessType){
            case signBusinessType.PD:
                resultOption = "请张主任阅示。";
                break;
            case signBusinessType.GD:
                resultOption = "请李主任阅示。";
                break;
            default:
                ;
        }
        return resultOption;
    }//E_getDefaultZHBYJ

    //S_获取流程参数
    function getFlowParams() {
        return signFlowParams;
    }//E_getFlowParams

})();

