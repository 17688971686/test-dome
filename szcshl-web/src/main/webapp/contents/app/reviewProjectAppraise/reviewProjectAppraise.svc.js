(function () {
    'use strict';

    angular.module('app').factory('reviewProjectAppraiseSvc', reviewProjectAppraise);

    reviewProjectAppraise.$inject = ['$http', 'bsWin' , '$state'];

    function reviewProjectAppraise($http , bsWin , $state) {
        var service = {

            endProjectGrid : endProjectGrid ,//查询办结项目

            appraisingProjectGrid : appraisingProjectGrid , //查询优秀评审项目

            monthlyExcellentGrid: monthlyExcellentGrid,//优秀评审报告列表(停用)

            appraiseWindow : appraiseWindow , //评优弹出窗
            initProposer : initProposer ,//初始化申请人
            saveApply : saveApply,//保存评优申请
            approveListGrid : approveListGrid,//优秀评审报告审批列表
            getAppraiseById : getAppraiseById , //通过Id查询信息
            saveApprove : saveApprove,//保存审批意见



        }
        return service;


        //being saveApprove
        function saveApprove(vm) {
            common.initJqValidation();
            var isValid = $('#form').valid();
            if (isValid && vm.appraise.ministerOpinion != undefined ) {
                var httpOptions = {
                    method: 'put',
                    url: rootPath + '/reviewProjectAppraise/saveApprove',
                    data: vm.appraise
                }
                var httpSuccess = function success(response) {
                    bsWin.success("操作成功！" , function (){
                        window.parent.$("#appraiseWindow").data("kendoWindow").close();
                        location.reload();
                        // $state.go('approveList');
                    });


                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }
        //end saveApprove


        //begin getAppraiseById
        function getAppraiseById(id,callBack){
            var httpOptions = {
                method : 'get' ,
                url : rootPath + '/reviewProjectAppraise/getAppraiseById',
                params : {id : id}
            }

            var httpSuccess = function success(response){
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end getAppraiseById

        //begin initProposer
        function initProposer(callBack){
            var httpOptions = {
                method : 'get',
                url : rootPath + "/reviewProjectAppraise/initProposer"
            }

            var httpSuccess = function success(response){
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }

            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });

        }
        //end

        //begin appraiseWindow
        function appraiseWindow(vm){
            $("#appraiseWindow").kendoWindow({
                width: "800px",
                height: "400px",
                title: "评审报告评优申请",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }
        //end appraiseWindow

        //begin saveApply
        function saveApply(vm){
            common.initJqValidation();
            var isValid = $('#form').valid();
            if (isValid && vm.appraise.appraiseReportName !=undefined &&
                vm.appraise.proposerTime != undefined && vm.appraise.appraiseCause !=undefined) {
                var httpOptions = {
                    method: 'post',
                    url: rootPath + '/reviewProjectAppraise/saveApply',
                    data: vm.appraise
                }

                var httpSuccess = function success(response) {
                    bsWin.success("保存成功！");
                    window.parent.$("#appraiseWindow").data("kendoWindow").close();
                    vm.gridOptions.dataSource.read();
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }
        //end saveApply



        //begin endProjectGrid
        function endProjectGrid(vm){

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/reviewProjectAppraise/findEndProject"),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        // createdDate: {
                        //     type: "date"
                        // }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                // sort: {
                //     field: "createdDate",
                //     dir: "desc"
                // }
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
                    field: "",
                    title: "项目名称",
                    width: 160,
                    filterable: false,
                    template: function (item) {
                        if(item.processInstanceId){
                            return '<a href="#/signDetails/'+item.signid+'/'+item.processInstanceId+'" >'+item.projectname+'</a>';
                        }else{
                            return '<a href="#/signDetails/'+item.signid+'/" >'+item.projectname+'</a>';
                        }

                    }
                },
                {
                    field: "reviewstage",
                    title: "评审阶段",
                    width: 140,
                    filterable: false
                },
                {
                    field: "signdate",
                    title: "收文日期",
                    width: 200,
                    filterable: false
                },
                {
                    field: "dispatchtype",
                    title: "发文日期",
                    width: 160,
                    filterable: false
                },
                {
                    field: "reviewOrgName",
                    title: "评审部门",
                    width: 140,
                    filterable: false
                },
                {
                    field: "aUserName",
                    title: "项目负责人",
                    width: 140,
                    filterable: false
                },
                {
                    field: "ffilenum",
                    title: "归档编号",
                    width: 140,
                    filterable: false
                },
                {
                    field: "dfilenum",
                    title: "文件字号",
                    width: 140,
                    filterable: false
                },
                {
                    field: "appalyinvestment",
                    title: "申报投资",
                    width: 140,
                    filterable: false
                },
                {
                    field: "authorizevalue",
                    title: "审定投资",
                    width: 140,
                    filterable: false
                },
                {
                    field: "extravalue",
                    title: "核减（增）投资",
                    width: 140,
                    filterable: false
                },
                {
                    field: "extrarate",
                    title: "核减率",
                    width: 140,
                    filterable: false
                },
                {
                    field: "approvevalue",
                    title: "批复金额",
                    width: 140,
                    filterable: false
                },
                {
                    field: "receivedate",
                    title: "批复来文时间",
                    width: 140,
                    filterable: false
                },
                {
                    field: "dispatchtype",
                    title: "发文类型",
                    width: 140,
                    filterable: false
                },
                {
                    field: "ffiledate",
                    title: "归档日期",
                    width: 140,
                    filterable: false
                },
                {
                    field: "builtcompanyName",
                    title: "建设单位",
                    width: 140,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 100,
                    template: function (item) {
                        if(item.isAppraising == 9){
                            return "优秀评审项目";
                        }else if(item.isAppraising == 8){
                            return "申请未通过";
                        }else if(item.isAppraising == 1){
                            return "审核中";
                        }else{
                            return common.format($('#columnBtns').html(),
                                "vm.appraisingWindow('" + item.signid + "','"+item.projectname+"')");
                        }
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
                resizable: true,
                dataBound: function () {
                    var rows = this.items();
                    var page = this.pager.page() - 1;
                    var pagesize = this.pager.pageSize();
                    $(rows).each(function () {
                        var index = $(this).index() + 1 + page * pagesize;
                        var rowLabel = $(this).find(".row-number");
                        $(rowLabel).html(index);
                    });
                }
            };

        }
        //end endProjectGrid


        //beign appraisingProjectGrid
        function appraisingProjectGrid(vm){

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/reviewProjectAppraise/findAppraisingProject"),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        // createdDate: {
                        //     type: "date"
                        // }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                // sort: {
                //     field: "createdDate",
                //     dir: "desc"
                // }
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
                    field: "",
                    title: "项目名称",
                    width: 160,
                    filterable: false,
                    template: function (item) {
                        if(item.processInstanceId){
                            return '<a href="#/signDetails/'+item.signid+'/'+item.processInstanceId+'" >'+item.projectname+'</a>';
                        }else{
                            return '<a href="#/signDetails/'+item.signid+'/" >'+item.projectname+'</a>';
                        }

                    }
                },
                {
                    field: "reviewstage",
                    title: "评审阶段",
                    width: 140,
                    filterable: false
                },
                {
                    field: "signdate",
                    title: "收文日期",
                    width: 200,
                    filterable: false
                },
                {
                    field: "dispatchtype",
                    title: "发文日期",
                    width: 160,
                    filterable: false
                },
                {
                    field: "reviewOrgName",
                    title: "评审部门",
                    width: 140,
                    filterable: false
                },
                {
                    field: "aUserName",
                    title: "项目负责人",
                    width: 140,
                    filterable: false
                },
                {
                    field: "ffilenum",
                    title: "归档编号",
                    width: 140,
                    filterable: false
                },
                {
                    field: "dfilenum",
                    title: "文件字号",
                    width: 140,
                    filterable: false
                },
                {
                    field: "appalyinvestment",
                    title: "申报投资",
                    width: 140,
                    filterable: false
                },
                {
                    field: "authorizevalue",
                    title: "审定投资",
                    width: 140,
                    filterable: false
                },
                {
                    field: "extravalue",
                    title: "核减（增）投资",
                    width: 140,
                    filterable: false
                },
                {
                    field: "extrarate",
                    title: "核减率",
                    width: 140,
                    filterable: false
                },
                {
                    field: "approvevalue",
                    title: "批复金额",
                    width: 140,
                    filterable: false
                },
                {
                    field: "receivedate",
                    title: "批复来文时间",
                    width: 140,
                    filterable: false
                },
                {
                    field: "dispatchtype",
                    title: "发文类型",
                    width: 140,
                    filterable: false
                },
                {
                    field: "ffiledate",
                    title: "归档日期",
                    width: 140,
                    filterable: false
                },
                {
                    field: "builtcompanyName",
                    title: "建设单位",
                    width: 140,
                    filterable: false
                }
            ];

            // End:column
            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                dataBound: function () {
                    var rows = this.items();
                    var page = this.pager.page() - 1;
                    var pagesize = this.pager.pageSize();
                    $(rows).each(function () {
                        var index = $(this).index() + 1 + page * pagesize;
                        var rowLabel = $(this).find(".row-number");
                        $(rowLabel).html(index);
                    });
                }
            };

        }
        //end appraisingProjectGrid


        //begin approveListGrid
        function approveListGrid(vm){

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/reviewProjectAppraise/getAppraiseReport"),
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
                    field: "",
                    title: "序号",
                    template: "<span class='row-number'></span>",
                    width: 50
                },
                {
                    field: "",
                    title: "项目名称",
                    width: 160,
                    filterable: false,
                    template: function (item) {
                        if(item.processInstanceId){
                            return '<a href="#/signDetails/'+item.signid+'/'+item.processInstanceId+'" >'+item.projectName+'</a>';
                        }else{
                            return '<a href="#/signDetails/'+item.signid+'/" >'+item.projectName+'</a>';
                        }

                    }
                },
                {
                    field: "appraiseReportName",
                    title: "评审报告名称",
                    width: 160,
                    filterable: false,

                },
                {
                    field: "proposerName",
                    title: "申请人",
                    width: 160,
                    filterable: false,

                },
                {
                    field: "proposerTime",
                    title: "申请时间",
                    width: 160,
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"

                },
                {
                    field: "",
                    title: "操作",
                    width: 100,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.dealWindow('" + item.id + "')");
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
                resizable: true,
                dataBound: function () {
                    var rows = this.items();
                    var page = this.pager.page() - 1;
                    var pagesize = this.pager.pageSize();
                    $(rows).each(function () {
                        var index = $(this).index() + 1 + page * pagesize;
                        var rowLabel = $(this).find(".row-number");
                        $(rowLabel).html(index);
                    });
                }
            };

        }
        //end approveListGrid

        // begin#grid
        function monthlyExcellentGrid(vm) {

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/signView/getSignList", $("#searchform")),
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
                    field: "",
                    title: "序号",
                    template: "<span class='row-number'></span>",
                    width: 50
                },
                {
                    field: "",
                    title: "项目名称",
                    width: 160,
                    filterable: false,
                    template: function (item) {
                        if(item.processInstanceId){
                            return '<a href="#/signDetails/'+item.signid+'/'+item.processInstanceId+'" >'+item.projectname+'</a>';
                        }else{
                            return '<a href="#/signDetails/'+item.signid+'/" >'+item.projectname+'</a>';
                        }

                    }
                },
                {
                    field: "reviewstage",
                    title: "评审阶段",
                    width: 140,
                    filterable: false
                },
                {
                    field: "signdate",
                    title: "收文日期",
                    width: 200,
                    filterable: false
                },
                {
                    field: "dispatchtype",
                    title: "发文日期",
                    width: 160,
                    filterable: false
                },
                {
                    field: "reviewdays",
                    title: "评审天数",
                    width: 140,
                    filterable: false
                },
                {
                    field: "surplusdays",
                    title: "剩余工作日",
                    width: 140,
                    filterable: false
                },
                {
                    field: "reviewOrgName",
                    title: "评审部门",
                    width: 140,
                    filterable: false
                },
                {
                    field: "aUserName",
                    title: "项目负责人",
                    width: 140,
                    filterable: false
                },
                {
                    field: "ffilenum",
                    title: "归档编号",
                    width: 140,
                    filterable: false
                },
                {
                    field: "dfilenum",
                    title: "文件字号",
                    width: 140,
                    filterable: false
                },
                {
                    field: "appalyinvestment",
                    title: "申报投资",
                    width: 140,
                    filterable: false
                },
                {
                    field: "authorizevalue",
                    title: "审定投资",
                    width: 140,
                    filterable: false
                },
                {
                    field: "extravalue",
                    title: "核减（增）投资",
                    width: 140,
                    filterable: false
                },
                {
                    field: "extrarate",
                    title: "核减率",
                    width: 140,
                    filterable: false
                },
                {
                    field: "approvevalue",
                    title: "批复金额",
                    width: 140,
                    filterable: false
                },
                {
                    field: "receivedate",
                    title: "批复来文时间",
                    width: 140,
                    filterable: false
                },
                {
                    field: "dispatchtype",
                    title: "发文类型",
                    width: 140,
                    filterable: false
                },
                {
                    field: "ffiledate",
                    title: "归档日期",
                    width: 140,
                    filterable: false
                },
                {
                    field: "builtcompanyName",
                    title: "建设单位",
                    width: 140,
                    filterable: false
                },
                {
                    field: "isassistproc",
                    title: "是否协审",
                    width: 140,
                    filterable: false,
                    template: function (item) {
                        if (item.sisassistproc == 9) {
                            return "是";
                        } else {
                            return "否";
                        }
                    }
                },
                {
                    field: "daysafterdispatch",
                    title: "发文后工作日",
                    width: 140,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 100,
                    template: function (item) {
                        if (item.isAssociate == 0) {
                            return '<button class="btn btn-xs btn-success" ng-click="vm.showAssociate(\''+item.signid+'\')" ng-disabled="vm.isSubmit">  <span class="glyphicon glyphicon-resize-small"></span>关联项目</button>';
                        } else {
                            return '<button class="btn btn-xs btn-warning" ng-click="vm.disAssociateSign(\''+item.signid+'\')" ng-disabled="vm.isSubmit">  <span class="glyphicon glyphicon-resize-small"></span>取消关联</button>';;
                        }
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
                resizable: true,
                dataBound: function () {
                    var rows = this.items();
                    var page = this.pager.page() - 1;
                    var pagesize = this.pager.pageSize();
                    $(rows).each(function () {
                        var index = $(this).index() + 1 + page * pagesize;
                        var rowLabel = $(this).find(".row-number");
                        $(rowLabel).html(index);
                    });
                }
            };

        }// end fun grid



    }
})();