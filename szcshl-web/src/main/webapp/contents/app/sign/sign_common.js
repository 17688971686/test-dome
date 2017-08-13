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
        initSuppData : initSuppData,                    // 初始化项目资料补充函
        saveSuppletter : saveSuppletter,                // 保存项目资料补充函
        registerFilePrint : registerFilePrint,          // 转到登记资料打印页面
        initPrintData : initPrintData,                  // 初始化登记资料打印页面数据
        kendoGridinlineConfig : kendoGridinlineConfig,  // 行内编辑方法
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

    //S_判断评审阶段
    function isCommonStage(reviewstage){
        if(reviewstage=='项目建议书' || reviewstage=='可行性研究报告' ||reviewstage=='提前介入' || reviewstage=='项目概算' || reviewstage=='其它' || reviewstage=='资金申请报告'){
            return true;
        }else{
            return false;
        }
    }//E_isCommonStage

    // 初始化项目补充资料函
    function initSuppData(vm, options) {
        // options.$state.go('addSupp', {signid:
        // vm.model.signid,id:vm.model.suppletterid })
        // options.url;
        options.$http({
            method : 'get',
            url : rootPath + "/addSuppLetter",
            headers : {
                "contentType" : "application/json;charset=utf-8" // 设置请求头信息
            },
            dataType : "json",
            params : {
                signid : options.$state.params.signid,
                id : options.$state.params.id
            }
        }).then(function(response) {
            vm.suppletter = response.data;
        });

        vm.saveSuppletter = function() {
            if (vm.suppletter.id) {// 更新
                updateSuppletter(vm, options);
            } else {
                saveSuppletter(vm, options);// 保存
            }
        }
        if (vm.suppletter.id) {
            getAddsuppletterbyId(vm, options);// 根据id获取数据
        }
        /*
         * $("#suppWin").kendoWindow({ width: "50%", height: "80%", title:
         * "意见选择", visible: false, modal: true, closable: true, actions: ["Pin",
         * "Minimize", "Maximize", "close"]
         * }).data("kendoWindow").center().open();
         */
    }

    function saveSuppletter(vm, options) {
        options.$http({
            method : 'post',
            url : rootPath + "/addSuppLetter/add",
            headers : {
                "contentType" : "application/json;charset=utf-8" // 设置请求头信息
            },
            dataType : "json",
            data : vm.suppletter
        }).then(function(response) {
            alert("保存成功！");
        });
    }// end

    function updateSuppletter(vm, options) {
        options.$http({
            method : 'post',
            url : rootPath + "/addSuppLetter/update",
            headers : {
                "contentType" : "application/json;charset=utf-8" // 设置请求头信息
            },
            dataType : "json",
            data : vm.suppletter
        }).then(function(response) {
            alert("更新成功！");
        });
    }// end

    function getAddsuppletterbyId(vm, options) {
        options.$http({
            method : 'get',
            url : rootPath + "/addSuppLetter/getbyId",
            headers : {
                "contentType" : "application/json;charset=utf-8" // 设置请求头信息
            },
            dataType : "json",
            params : {id:vm.suppletter.id}
        }).then(function(response) {
            vm.suppletter = response.data;
        });
    }

    function registerFilePrint(vm, options) {
        vm.showPrint = true;
        var registerWin = $("#printWindow");
        registerWin.kendoWindow({
            width : "50%",
            height : "60%",
            title : "打印登记补充资料",
            visible : false,
            modal : true,
            closable : true,
            actions : ["Pin", "Minimize", "Maximize", "close"]
        }).data("kendoWindow").center().open();

        initPrintData(vm, options);
    }

    function initPrintData(vm, options) {
        options.$http({
            method : 'post',
            url : rootPath + "/addRegisterFile/initprintdata",
            headers : {
                "contentType" : "application/json;charset=utf-8" // 设置请求头信息
            },
            dataType : "json",
            params : {
                signid : vm.model.signid
            }
        }).then(function(response) {
            vm.registerFileList = response.data.AddRegisterFileDtoList;
            vm.signdto = response.data.signdto;
            vm.printDate = response.data.printDate;

        });
    }

    function kendoGridinlineConfig() {
        return {
            filterable : {
                extra : false,
                // mode: "row", 将过滤条件假如title下,如果不要直接与title并排
                operators : {
                    string : {
                        "contains" : "包含",
                        "eq" : "等于"
                        // "neq": "不等于",
                        // "doesnotcontain": "不包含"
                    },
                    number : {
                        "eq" : "等于",
                        "neq" : "不等于",
                        gt : "大于",
                        lt : "小于"
                    },
                    date : {
                        gt : "大于",
                        lt : "小于"
                    }
                }
            },
            pageable : {
                pageSize : 10,
                previousNext : true,
                buttonCount : 5,
                refresh : true,
                pageSizes : true
            },
            schema : function(model) {
                return {
                    data : "value",
                    total : function(data) {
                        return data['count'];
                    },
                    model : model
                };
            },
            transport : function(vm, url, paramObj) {
                return {
                    read : {
                        url : url.readUrl,
                        dataType : "json",
                        type : "post",
                        beforeSend : function(req) {
                            req.setRequestHeader('Token', service.getToken());
                        }
                    },
                    update : {
                        url : url.updateUrl,
                        dataType : "json",
                        type : "post"
                    },
                    destroy : {
                        url : url.destroyUrl,
                        dataType : "json",
                        type : "post",
                        contentType : "application/json"
                    },
                    create : {
                        url : url.createUrl,
                        dataType : "json",
                        type : "post",
                        contentType : "application/json"
                    },
                    parameterMap : function(options, operation) {
                        if (operation == "update"|| operation == "destroy" ||operation == "create") {
                            return kendo.stringify(options.models);
                        } else if (operation == "read") {
                            return {
                                "$filter" : paramObj.filter,
                                "$skip" : options.skip,
                                "$top" : options.take,
                                "$inlinecount" : "allpages"
                            }
                        }
                    }
                }
            },
            noRecordMessage : {
                template : '暂时没有数据.'
            }
        }
    }

})();

