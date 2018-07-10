(function () {
    'use strict';

    angular.module('app').factory('logSvc', log);

    log.$inject = ['$http', '$compile'];
    function log($http, $compile) {
        var service = {
            grid: grid,
            gridFgw:gridFgw
        };
        return service;

        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath +"/log/fingByOData",$("#logform")),
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

            var dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            }

            // Begin:column
            var columns = [
                {
                    field: "",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                }, {
                    field: "",
                    title: "级别",
                    width: 60,
                    filterable: false,
                    template:function(item){
                        if(item.logLevel){
                            if(item.logLevel == "1"){
                                return "高"
                            }else if(item.logLevel == "2"){
                                return "中"
                            }else if(item.logLevel == "3"){
                                return "低"
                            }
                        }else{
                            return "低";
                        }
                    },
                }, {
                    field: "userName",
                    title: "操作者",
                    width: 80,
                    filterable: false
                }, {
                    field: "createdDate",
                    title: "操作时间",
                    width: 170,
                    filterable: false,
                    format: "{0:yyyy/MM/dd HH:mm:ss}"
                } ,{
                    field: "ipAdd",
                    title: "IP地址",
                    filterable: false,
                    width: 130,
                }, {
                    field: "browserInfo",
                    title: "浏览器",
                    filterable: false,
                    width: 120,
                }, {
                    field: "module",
                    title: "操作描述",
                    filterable: false,
                    width: 200,
                }, {
                    field: "",
                    title: "结果",
                    filterable: false,
                    width: 80,
                    template:function(item){
                        if(item.result){
                            if(item.result == "9"){
                                return '<span class="label label-success">成功</span>';
                            }else {
                                return '<span class="label label-danger">失败</span>';
                            }
                        }else{
                            return "";
                        }
                    },
                },{
                    field: "",
                    title: "日志内容",
                    filterable: false,
                    width: 400,
                    template:function(item){
                        return "<textarea rows='4' style='width:100%;'>"+item.message+"</textarea>";
                    },
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound: dataBound,
                resizable: true
            };

        }// end fun grid

        function gridFgw(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath +"/log/findFgwSignLog",$("#logform")),
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

            var dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            }

            // Begin:column
            var columns = [
                {
                    field: "",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                }, {
                    field: "",
                    title: "级别",
                    width: 60,
                    filterable: false,
                    template:function(item){
                        if(item.logLevel){
                            if(item.logLevel == "1"){
                                return "高"
                            }else if(item.logLevel == "2"){
                                return "中"
                            }else if(item.logLevel == "3"){
                                return "低"
                            }
                        }else{
                            return "低";
                        }
                    },
                }, {
                    field: "userName",
                    title: "操作者",
                    width: 80,
                    filterable: false
                }, {
                    field: "createdDate",
                    title: "操作时间",
                    width: 170,
                    filterable: false,
                    format: "{0:yyyy/MM/dd HH:mm:ss}"
                } ,{
                    field: "ipAdd",
                    title: "IP地址",
                    filterable: false,
                    width: 130,
                }, {
                    field: "browserInfo",
                    title: "浏览器",
                    filterable: false,
                    width: 120,
                }, {
                    field: "module",
                    title: "操作描述",
                    filterable: false,
                    width: 200,
                }, {
                    field: "",
                    title: "结果",
                    filterable: false,
                    width: 80,
                    template:function(item){
                        if(item.result){
                            if(item.result == "9"){
                                return '<span class="label label-success">成功</span>';
                            }else {
                                return '<span class="label label-danger">失败</span>';
                            }
                        }else{
                            return "";
                        }
                    },
                },{
                    field: "",
                    title: "日志内容",
                    filterable: false,
                    width: 400,
                    template:function(item){
                        return "<textarea rows='4' style='width:100%;'>"+item.message+"</textarea>";
                    },
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound: dataBound,
                resizable: true
            };

        }// end fun grid

        function gridFgw(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath +"/log/findFgwSignLog",$("#logform")),
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

            var dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            }

            // Begin:column
            var columns = [
                {
                    field: "",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                }, {
                    field: "",
                    title: "级别",
                    width: 60,
                    filterable: false,
                    template:function(item){
                        if(item.logLevel){
                            if(item.logLevel == "1"){
                                return "高"
                            }else if(item.logLevel == "2"){
                                return "中"
                            }else if(item.logLevel == "3"){
                                return "低"
                            }
                        }else{
                            return "低";
                        }
                    },
                }, {
                    field: "userName",
                    title: "操作者",
                    width: 80,
                    filterable: false
                }, {
                    field: "createdDate",
                    title: "操作时间",
                    width: 170,
                    filterable: false,
                    format: "{0:yyyy/MM/dd HH:mm:ss}"
                } ,{
                    field: "ipAdd",
                    title: "IP地址",
                    filterable: false,
                    width: 130,
                }, {
                    field: "browserInfo",
                    title: "浏览器",
                    filterable: false,
                    width: 120,
                }, {
                    field: "module",
                    title: "操作描述",
                    filterable: false,
                    width: 200,
                }, {
                    field: "",
                    title: "结果",
                    filterable: false,
                    width: 80,
                    template:function(item){
                        if(item.result){
                            if(item.result == "9"){
                                return '<span class="label label-success">成功</span>';
                            }else {
                                return '<span class="label label-danger">失败</span>';
                            }
                        }else{
                            return "";
                        }
                    },
                },{
                    field: "",
                    title: "日志内容",
                    filterable: false,
                    width: 400,
                    template:function(item){
                        return "<textarea rows='4' style='width:100%;'>"+item.message+"</textarea>";
                    },
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound: dataBound,
                resizable: true
            };

        }// end fun grid


    }


})();