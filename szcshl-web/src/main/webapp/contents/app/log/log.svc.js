(function () {
    'use strict';

    angular.module('app').factory('logSvc', log);

    log.$inject = ['$http', '$compile'];
    function log($http, $compile) {
        var service = {
            grid: grid
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
                    width: 100,
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
                    field: "message",
                    title: "日志内容",
                    filterable: false
                }, {
                    field: "module",
                    title: "所属模块",
                    filterable: false
                }, {
                    field: "logger",
                    title: "所在方法",
                    filterable: false
                }, {
                    field: "userName",
                    title: "操作者",
                    width: 80,
                    filterable: false
                }, {
                    field: "createdDate",
                    title: "操作时间",
                    width: 180,
                    filterable: false,
                    format: "{0:yyyy/MM/dd HH:mm:ss}"
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