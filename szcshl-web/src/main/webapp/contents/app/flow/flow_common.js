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
        SIGN_ZR_QRFW: "SIGN_ZR_QRFW", //主任审批发文
        SIGN_FWBH: "SIGN_FWBH",            //生成发文编号
        SIGN_CWBL: "SIGN_CWBL",            //财务办理
        SIGN_GD: "SIGN_GD",                //归档
        SIGN_DSFZR_QRGD: "SIGN_DSFZR_QRGD",//第二负责人确认
        SIGN_QRGD: "SIGN_QRGD"              //最终归档
    };

    /**
     * 课题研究流程环节参数
     */
    var topicNode ={
        TOPIC_JHTC : "TOPIC_JHTC",                //计划提出
        TOPIC_BZSH_JH : "TOPIC_BZSH_JH",          //部长审核
        TOPIC_FGLD_JH : "TOPIC_FGLD_JH",          //副主任审核
        TOPIC_ZRSH_JH : "TOPIC_ZRSH_JH",          //主任审定
        TOPIC_BFGW : "TOPIC_BFGW",                //报发改委审批
        TOPIC_LXDW : "TOPIC_LXDW",                //联系合作单位
        TOPIC_QDHT : "TOPIC_QDHT",                //签订合同
        TOPIC_YJSS : "TOPIC_YJSS",                //课题研究实施
        TOPIC_NBCS : "TOPIC_NBCS",                //内部初审
        TOPIC_GZFA : "TOPIC_GZFA",                //提出成果鉴定会（或论证会）方案
        TOPIC_BZSH_FA : "TOPIC_BZSH_FA",          //部长审核
        TOPIC_FGLD_FA : "TOPIC_FGLD_FA",          //副主任审核
        TOPIC_ZRSH_FA : "TOPIC_ZRSH_FA",          //主任审定
        TOPIC_CGJD : "TOPIC_CGJD",                //召开成果鉴定会
        TOPIC_KTBG : "TOPIC_KTBG",                //完成课题报告
        TOPIC_BZSH_BG : "TOPIC_BZSH_BG",          //部长审核
        TOPIC_FGLD_BG : "TOPIC_FGLD_BG",          //副主任审核
        TOPIC_ZRSH_BG : "TOPIC_ZRSH_BG",          //主任审定
        TOPIC_KTJT : "TOPIC_KTJT",                //课题结题
        TOPIC_BZSH_JT : "TOPIC_BZSH_JT",          //部长审核
        TOPIC_FGLD_JT : "TOPIC_FGLD_JT",          //副主任审核
        TOPIC_ZRSH_JT : "TOPIC_ZRSH_JT",          //主任审定
        TOPIC_YFZL : "TOPIC_YFZL",                //印发资料
        TOPIC_ZLGD : "TOPIC_ZLGD",                //资料归档
    }

    /**
     * 图书采购流程环节参数
     */
    var booksBuyNode ={
        BOOK_LEADER_CGQQ : "BOOK_LEADER_CGQQ",    //各项目负责人/部门提出购买图书请求
        BOOK_BZSP : "BOOK_BZSP",                 //部长审核
        BOOK_FGFZRSP : "BOOK_FGFZRSP",          //分管副主任审批
        BOOK_ZXZRSP : "BOOK_ZXZRSP",            //中心主任审批
        BOOK_YSRK : "BOOK_YSRK"                 //中心主任审批
    }

    //流程定义值
    var flowDefinedKey = {
        TOPIC_FLOW : "TOPIC_FLOW",                 //课题研究流程
        BOOKS_BUY_FLOW:"BOOKS_BUY_FLOW"            //图书采购流程
    }
    var service = {
        getFlowDefinedKey : function(){
            return flowDefinedKey;               //流程定义值
        },
        getSignFlowNode: function(){
            return signNode;
        },                                        //项目签收流程环节
        getTopicFlowNode: function(){
            return topicNode;
        },                                        //课题研究流程
        getBooksBuyFlowNode: function(){
            return booksBuyNode;                 //图书采购流程环节
        }
    };
    window.flowcommon = service;

})();
