(function () {
    'use strict';

    /**
     * 流程通用基础类
     */
    var signNode = {
        SIGN_ZR: "SIGN_ZR",                //填报
        SIGN_QS: "SIGN_QS",                //签收
        SIGN_ZHB: "SIGN_ZHB",              //综合部审批
        SIGN_FGLD_FB: "SIGN_FGLD_FB",      //分管副主任审批
        SIGN_BMFB1: "SIGN_BMFB1",          //部门分办1
        SIGN_BMFB2: "SIGN_BMFB2",          //部门分办2
        SIGN_BMFB3: "SIGN_BMFB3",          //部门分办3
        SIGN_BMFB4: "SIGN_BMFB4",          //部门分办4
        SIGN_XMFZR1: "SIGN_XMFZR1",        //项目负责人办理1
        SIGN_XMFZR2: "SIGN_XMFZR2",        //项目负责人办理2
        SIGN_XMFZR3: "SIGN_XMFZR3",        //项目负责人办理3
        SIGN_XMFZR4: "SIGN_XMFZR4",        //项目负责人办理4
        SIGN_BMLD_SPW1: "SIGN_BMLD_SPW1",  //部长审批1
        SIGN_BMLD_SPW2: "SIGN_BMLD_SPW2",  //部长审批2
        SIGN_BMLD_SPW3: "SIGN_BMLD_SPW3",  //部长审批3
        SIGN_BMLD_SPW4: "SIGN_BMLD_SPW4",  //部长审批4
        SIGN_FGLD_SPW1: "SIGN_FGLD_SPW1",  //分管副主任审批1
        SIGN_FGLD_SPW2: "SIGN_FGLD_SPW2",  //分管副主任审批2
        SIGN_FGLD_SPW3: "SIGN_FGLD_SPW3",  //分管副主任审批3
        SIGN_FGLD_SPW4: "SIGN_FGLD_SPW4",  //分管副主任审批4
        SIGN_FW: "SIGN_FW",                //发文申请
        SIGN_QRFW: "SIGN_QRFW",            //项目负责人确认发文
        SIGN_BMLD_QRFW: "SIGN_BMLD_QRFW",  //部长审批发文
        SIGN_FGLD_QRFW: "SIGN_FGLD_QRFW",  //分管领导审批发文
        SIGN_ZR_QRFW: "FLOW_SIGN_ZR_QRFW", //主任审批发文
        SIGN_FWBH: "SIGN_FWBH",            //生成发文编号
        SIGN_CWBL: "SIGN_CWBL",            //财务办理
        SIGN_GD: "SIGN_GD",                //归档
        SIGN_DSFZR_QRGD: "SIGN_DSFZR_QRGD",//第二负责人确认
        SIGN_QRGD: "SIGN_QRGD"              //最终归档
    };

    var service = {
        signFlowNode: signFlowNode,               //项目签收流程环节
    };
    window.flowcommon = service;

    function signFlowNode() {
        return signNode;
    }

})();
