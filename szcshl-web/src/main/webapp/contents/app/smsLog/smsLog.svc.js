(function () {
    'use strict';

    angular.module('app').factory('smslogSvc', smslog);

    smslog.$inject = ['$http', '$compile'];
    function smslog($http, $compile) {

        var service = {
            grid: grid,
            select: select,
        };
        return service;
        // var url_sms = rootPath + "/smslog";


        // begin#deleteUser
        //获取下拉框 数据
        function select(vm) {
            var httpOptions = {
                method: 'get',
                url: '/szcshl-web/smslog/querySMSLogType',
                params: {type: "all"}
            }
            var httpSuccess = function success(response) {
                vm.select = response.data;
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }




        function grid(vm) {
            // Begin:dataSource
            var form = $("#smsLogform");
            debugger;
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath +"/smslog/fingByOData",form),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        },
                        smsLogType:"smsLogType"
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
                // {
                //     field: "",
                //     title: "序号",
                //     width: 50,
                //     filterable: false,
                //     template: "<span class='row-number'></span>"
                // },
                 {
                    field: "userName",
                    title: "短信接收者",
                    width: 100,
                    filterable: false
                }
                ,
                {
                    field: "smsUserPhone",
                    title: "接收手机号码",
                    width: 180,
                    filterable: false
                }
                , {
                    field: "createdDate",
                    title: "创建时间",
                    width: 100,
                    filterable: false,
                    format: "{0:yyyy/MM/dd HH:mm:ss}"
                }
                // , {
                //     field: "customMessage",
                //     title: "自定义短信内容",
                //     filterable: false,
                //     width: 120,
                //     attributes: {style: "white-space:nowrap;text-overflow:ellipsis;"},
                // }
                , {
                    field: "",
                    title: "结果",
                    filterable: false,
                    width: 50,
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
                    field: "message",
                    title: "短信内容",
                    filterable: false,
                    width: 600,
                    attributes: {style: "white-space:nowrap;text-overflow:ellipsis;"},
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