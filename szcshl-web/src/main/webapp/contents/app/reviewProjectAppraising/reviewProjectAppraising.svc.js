(function () {
    'use strict';

    angular.module('app').factory('reviewProjectAppraisingSvc', reviewProjectAppraising);

    reviewProjectAppraising.$inject = ['$http'];

    function reviewProjectAppraising($http) {
        var service = {

            endProjectGrid : endProjectGrid ,//查询办结项目

            appraisingProjectGrid : appraisingProjectGrid , //查询优秀评审项目

            monthlyExcellentGrid: monthlyExcellentGrid,//优秀评审报告列表



        }

        return service;

        //begin endProjectGrid
        function endProjectGrid(vm){

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/reviewProjectAppraising/findEndProject"),
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
                        }else{
                            return common.format($('#columnBtns').html(),
                                "vm.appraisingWindow('" + item.id + "')");
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
                transport: common.kendoGridConfig().transport(rootPath + "/reviewProjectAppraising/findAppraisingProject"),
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