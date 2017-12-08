(function () {
    'use strict';

    angular.module('app').factory('signSvc', sign);

    sign.$inject = ['$http', '$state','bsWin','sysfileSvc','templatePrintSvc'];

    function sign($http, $state,bsWin,sysfileSvc,templatePrintSvc) {
        var service = {
            signGrid: signGrid,				//初始化项目列表
            createSign: createSign,			//新增
            initFillData: initFillData,		//初始化表单填写页面（可编辑）
            initDetailData: initDetailData,	//初始化详情页（不可编辑）
            updateFillin: updateFillin,		//申报编辑
            deleteSign: deleteSign,			//删除收文
            findOfficeUsersByDeptName: findOfficeUsersByDeptName,//根据协办部门名称查询用户
            initFlowPageData: initFlowPageData, //初始化流程收文信息
            removeWP: removeWP,             //删除工作方案
            associateGrid: associateGrid,   //项目关联列表
            getAssociateSign : getAssociateSign, //获取项目关联阶段信息
            saveAssociateSign: saveAssociateSign,//保存项目关联
            initAssociateSigns: initAssociateSigns,//初始化项目关联信息
            meetingDoc: meetingDoc,             //生成会前准备材
            createDispatchFileNum:createDispatchFileNum,    //生成发文字号
            realSign : realSign ,               //正式签收
            createDispatchTemplate : createDispatchTemplate ,//生成发文模板
            signGetBackGrid :signGetBackGrid,                //项目取回列表
            getBack:getBack,                            //项目取回
            editTemplatePrint:editTemplatePrint,      //编辑模板打印
            workProgramPrint:workProgramPrint        //工作方案模板打印

        };
        return service;

        //negin createDispatchTemplate
        function createDispatchTemplate(vm , callBack){
            var httpOptions = {
                method : "post" ,
                url : rootPath + "/dispatch/createDispatchTemplate",
                params : {signId : vm.model.signid }
            }

            var httpSuccess = function success(response){
                if(callBack != undefined && typeof callBack == 'function'){
                    callBack(response.data);
                }
            }

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }
        //end createDispatchTemplate

        //E 上传附件列表

        //S_初始化grid(过滤已签收和已经完成的项目)
        function signGrid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/sign/findBySignUser", $("#searchform")),
                schema: common.kendoGridConfig().schema({
                    id: "signid",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            // End:dataSource
            //S_序号
            var  dataBound=function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            }
            //S_序号
            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />", item.signid)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"

                },
                {
				    field: "rowNumber",
				    title: "序号",
				    width: 50,
				    filterable : false,
				    template: "<span class='row-number'></span>"
				 },
                {
                    field: "",
                    title: "项目名称",
                    width: 120,
                    filterable: false,
                    template: function (item) {
                        return '<a href="#/signDetails/' + item.signid +'/' + item.processInstanceId + '" >' + item.projectname + '</a>';
                    }
                },
                {
                    field: "filecode",
                    title: "收文编号",
                    width: 80,
                    filterable: false,
                },
                {
                    field: "designcompanyName",
                    title: "项目单位",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "reviewstage",
                    title: "项目阶段",
                    width: 80,
                    filterable: false,
                },
                {
                    field: "projectcode",
                    title: "项目代码",
                    width: 80,
                    filterable: false,
                },
                {
                    field: "signdate",
                    title: "签收日期",
                    width: 100,
                    filterable: false,
                    format: "{0:yyyy/MM/dd HH:mm:ss}"
                },
                {
                    field: "",
                    title: "流程状态",
                    width: 80,
                    filterable: false,
                    template: function (item) {
                        if (item.signState) {
                            if(item.signState == 0){
                                return '<span style="color:red">预签收</span>';
                            }
                            else if (item.signState == 1) {
                                return '<span style="color:green;">进行中</span>';
                            } else if (item.signState == 2) {
                                return '<span style="color:orange;">已暂停</span>';
                            } else if (item.signState == 8) {
                                return '<span style="color:red;">强制结束</span>';
                            } else if (item.signState == 9) {
                                return '<span style="color:blue;">已完成</span>';
                            }else if (item.signState == 5) {
                                return '未发起';
                            }else{
                                return "";
                            }
                        } else {
                            return "未发起"
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 180,
                    template: function (item) {
                        var isStartFlow = angular.isString(item.processInstanceId);  //如果已经发起流程，则不能编辑
                        var isRealSign = (item.issign && item.issign == 9)?true:false;

                        //如果已经发起流程，则只能查看
                        return common.format($('#columnBtns').html(), item.signid, isStartFlow,
                            item.signid + "/" + item.processInstanceId,
                            "vm.del('" + item.signid + "')", isStartFlow,
                            "vm.startNewFlow('" + item.signid + "')", isStartFlow,
                            "vm.realSign('" + item.signid + "')", isRealSign);
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound:dataBound,
                resizable: true
            };
        }//E_初始化grid


        //S_创建收文
        function createSign(model,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign",
                data: model
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_创建收文

        //start  根据协办部门查询用户
        function findOfficeUsersByDeptName(param, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/officeUser/findOfficeUsersByDeptName",
                data: param
            };

            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //end  根据协办部门查询用户

        //Start 申报登记编辑
        function updateFillin(signObj,callBack) {
            var httpOptions = {
                method: 'put',
                url: rootPath + "/sign",
                data: signObj,
            }
            var httpSuccess = function success(response) {
                //关闭项目关联窗口
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //End 申报登记编辑

        //Start 删除收文
        function deleteSign(signid,callBack) {
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/sign",
                params: {
                	signid:signid
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //End 删除收文

        //S_初始化填报页面数据
        function initFillData(signid,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/html/initFillPageData",
                params: {
                    signid: signid
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_初始化填报页面数据

        //S_初始化详情数据
        function initDetailData(signid,callBack) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/sign/html/initDetailPageData",
                params: {
                    signid:signid
                }
            }

            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_初始化详情数据

        //S_初始化流程页面
        function initFlowPageData(signId,callBack) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/sign/initFlowPageData",
                params: {
                    signid: signId,
                    queryAll: true
                }
            }

            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_初始化流程页面

        //S_removeWP
        function removeWP(vm) {
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/workprogram/deleteBySignId",
                params: {
                    signId: vm.model.signid
                }
            }
            var httpSuccess = function success(response) {
                vm.isSubmit = false;
                if ( response.data.flag || response.data.reCode == "ok") {
                    //更改状态
                    vm.businessFlag.isFinishWP = false;
                    bsWin.success("操作成功！");
                } else {
                    bsWin.error(response.data.reMsg);
                }
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_removeWP

        //associateGrid（停用，2017-08-27，改用List的方式）
        function associateGrid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/sign/fingByOData", $("#searchAssociateform"),{filter: "isAssociate eq 0"}),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            // End:dataSource

            // Begin:column
            var columns = [
                {
                    field: "projectname",
                    title: "项目名称",
                    width: 160,
                    filterable: false
                },
                {
                    field: "projectcode",
                    title: "收文编号",
                    width: 140,
                    filterable: false,
                },
                {
                    field: "designcompanyName",
                    title: "项目单位",
                    width: 200,
                    filterable: false,
                },
                {
                    field: "reviewstage",
                    title: "项目阶段",
                    width: 160,
                    filterable: false,
                },
                {
                    field: "projectcode",
                    title: "项目代码",
                    width: 140,
                    filterable: false,
                }
            ];
            // End:column
            vm.associateGridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true
            };
        }//E_初始化associateGrid

        //S_获取关联项目
        function getAssociateSign(signModel,callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/findAssociateSign",
                data:signModel
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_getAssociateSign

        //start saveAssociateSign
        //如果associateSignId为空，解除关联
        function saveAssociateSign(signId, associateSignId, callBack) {
            associateSignId = associateSignId == 'undefined' ? null : associateSignId;
            var httpOptions = {
                method: 'post',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                url: rootPath + "/sign/associate",
                data: $.param({signId: signId, associateId: associateSignId}, true),

            }
            var httpSuccess = function success(response) {
                //关闭项目关联窗口
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack();
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //end saveAssociateSign

        //显示关联信息
        //start initAssociateSigns
        function initAssociateSigns(vm, singid) {
            var httpOptions = {
                method: 'get',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                url: rootPath + "/sign/associate?signId=" + singid,
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        if (response.data != undefined) {
                            vm.associateSign = response.data;
                            var signs = response.data;

                            var html_ = '';
                            for (var i = (signs.length - 1); i >= 0; i--) {
                                var s = signs[i];
                                var signdate = s.signdate || '';
                                html_ += '<div class="intro-list">' +
                                    '<div class="intro-list-left">' +
                                    '项目阶段：' + s.reviewstage + "<br/>签收时间：" + signdate +
                                    '</div>' +
                                    '<div class="intro-list-right">' +
                                    '<span></span>' +
                                    '<div class="intro-list-content">' +
                                    '名称：<span style="color:red;">' + s.projectname + '</span><br/>' +
                                    '送件人：<span style="color:red;">' + s.sendusersign + '</span><br/>' +
                                    '</div>' +
                                    '</div>' +
                                    '</div>';

                            }
                            $('#introFlow').html(html_);
                            var step = $("#myStep").step({
                                animate: true,
                                initStep: 1,
                                speed: 1000
                            });

                        }
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //end initAssociateSigns

        //S_meetingDoc
        function meetingDoc(vm,callBack) {
                var httpOptions = {
                    method: 'get',
                    url: rootPath + "/workprogram/createMeetingDoc",
                    params: {
                        signId: vm.model.signid,
                        // workprogramId: wpId
                    }
                }
                var httpSuccess = function success(response) {
                    if(callBack !=undefined && typeof  callBack=="function"){
                        callBack(response.data);
                    }
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
        }//E_meetingDoc


        //S_createDispatchFileNum
        function createDispatchFileNum(signId,dispatchId,callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/dispatch/createFileNum",
                params: {
                    signId : signId,
                    dispatchId: dispatchId
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_createDispatchFileNum

        //S_项目正式签收
        function realSign(signid,callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/realSign",
                params:{
                    signid : signid
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
            });
        }//E_realSign


        //signGetBackGrid
        function signGetBackGrid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/sign/fingByGetBack", $("#signBackform")),
                schema: common.kendoGridConfig().schema({
                    id: "signid",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            // End:dataSource

            // Begin:column
            var columns = [
                {
                    field: "",
                    title: "序号",
                    template: "<span class='row-number'></span>",
                    width: 50
                },
                {
                    field: "projectName",
                    title: "项目名称",
                    width: "25%",
                    filterable: false
                },
                {
                    field: "nodeName",
                    title: "当前环节",
                    width: "10",
                    filterable: false,
                },
                {
                    field: "displayName",
                    title: "处理人",
                    width: "10",
                    filterable: false,
                },
                {
                    field: "",
                    title: "合并评审",
                    width: "15%",
                    filterable: false,
                    template: function (item) {
                        if (item.reviewType) {
                            if (item.reviewType == 9 || item.reviewType == '9') {
                                return "合并评审[主项目]";
                            } else {
                                return "合并评审[次项目]";
                            }
                        } else {
                            return "否";
                        }
                    }
                },
                {
                    field: "reviewStage",
                    title: "项目阶段",
                    width: "15%",
                    filterable: false,
                },
                {
                    field: "signDate",
                    title: "签收时间",
                    width: "10%",
                    filterable: false,
                },
                {
                    field: "",
                    title: "操作",
                    width: "10%",
                    template: function (item) {
                        return common.format($('#columnBtns').html(),"signFlowDetail", item.businessKey, item.taskId, item.processInstanceId,"vm.getBack");
                    }
                }

            ];
            // End:column
            vm.signGetBackGrid = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound: function () {
                    var rows = this.items();
                    var page = this.pager.page() - 1;
                    var pagesize = this.pager.pageSize();
                    $(rows).each(function () {
                        var index = $(this).index() + 1 + page * pagesize;
                        var rowLabel = $(this).find(".row-number");
                        $(rowLabel).html(index);
                    });
                },
                resizable: true
            };
        }//E_初始化signGetBackGrid

        //S_项目取回
        function getBack(taskId,businessKey,callBack){
            //var activityId= "SIGN_FGLD_FB";根据角色判断回退到哪个环节
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/getBack",
                params:{
                    taskId : taskId,
                    businessKey:businessKey
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
            });
        }//getBack

        //编辑模板打印
        function editTemplatePrint(vm){
            if(vm.model.reviewstage=='项目建议书'|| vm.model.reviewstage=='可行性研究报告'|| vm.model.reviewstage=='其它'){
                templatePrintSvc.templatePrint("sign_fill_xmjys_templ");
            }else if(vm.model.reviewstage=='资金申请报告'){
                templatePrintSvc.templatePrint("sign_fill_zjsq_templ");
            }else if(vm.model.reviewstage=='进口设备'){
                templatePrintSvc.templatePrint("sign_fill_jksb_templ");
            }else if(vm.model.reviewstage=='设备清单（国产）' || vm.model.reviewstage=='设备清单（进口）'){
                templatePrintSvc.templatePrint("sign_fill_sbqd_templ");
            }else if(vm.model.reviewstage=='项目概算'){
                templatePrintSvc.templatePrint("sign_fill_xmgs_templ");
            }
        }

        //工作方案详细打印
        function workProgramPrint(id){
            var tempStr1;
            var tempStr2;
            if(id.indexOf("wpMain")>-1){
                if(id=='wpMain'){
                    tempStr1 = "wp1";
                    tempStr2 = "wp2";
                }else{
                    tempStr1 = "wpEdit1";
                    tempStr2 = "wpEdit2";
                }
                var LODOP = getLodop();
                var strStylePath = rootPath +"/contents/shared/templatePrint.css";
                var strStyleCSS="<link href="+strStylePath+" type='text/css' rel='stylesheet'>";
                var strFormHtml1="<head>"+strStyleCSS+"</head><body>"+$("#"+tempStr1).html()+"</body>";
                LODOP.PRINT_INIT("");
                LODOP.ADD_PRINT_HTML(10,20,"100%","100%",strFormHtml1);
                LODOP.NewPage();
                var strFormHtml2="<head>"+strStyleCSS+"</head><body>"+$("#"+tempStr2).html()+"</body>";
                LODOP.ADD_PRINT_HTML(10,20,"100%","100%",strFormHtml2);
                LODOP.PREVIEW();
            }else if(id.indexOf("wpAssist") > -1 ){
                var strArr  = id.split("_");
                var LODOP = getLodop();
                var strStylePath = rootPath +"/contents/shared/templatePrint.css";
                var strStyleCSS="<link href="+strStylePath+" type='text/css' rel='stylesheet'>";
                var strFormHtml1="<head>"+strStyleCSS+"</head><body>"+$("#wpAssistTempl1"+strArr[1]).html()+"</body>";
                LODOP.PRINT_INIT("");
                LODOP.ADD_PRINT_HTML(10,20,"100%","100%",strFormHtml1);
                LODOP.NewPage();
                var strFormHtml2="<head>"+strStyleCSS+"</head><body>"+$("#wpAssistTempl2"+strArr[1]).html()+"</body>";
                LODOP.ADD_PRINT_HTML(10,20,"100%","100%",strFormHtml2);
                LODOP.PREVIEW();
            }
        }
    }
})();