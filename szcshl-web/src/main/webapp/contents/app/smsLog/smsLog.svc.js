(function () {
    'use strict';

    angular.module('app').factory('smslogSvc', smslog);

    smslog.$inject = ['$http', '$compile'];
    function smslog($http, $compile) {

        var service = {
            msgGrid: msgGrid,
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


        function msgGrid(vm) {
            var form = $("#smsLogform");
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
                 {
                     field: "",
                     title: "序号",
                     width: 50,
                     filterable: false,
                     template: "<span class='row-number'></span>"
                 },
                {

                    field: "",
                    title: "应发人",
                    width: 150,
                    filterable: false,
                    template:function(item){
                        return "<textarea rows='4' style='width:100%;'>"+item.userName+"</textarea>";
                    },
                },
                {

                    field: "smsUserName",
                    title: "实发人",
                    width: 150,
                    filterable: false,
                    template:function(item){
                        var nValue = "";
                        if(item.smsUserName){
                            nValue = item.smsUserName
                        }
                        return "<textarea rows='4' style='width:100%;'>"+nValue+"</textarea>";
                    },
                },
                {
                    field: "",
                    title: "接收手机号码",
                    width: 150,
                    filterable: false,
                    template:function(item){
                        var nValue = "";
                        if(item.smsUserPhone){
                            nValue = item.smsUserPhone;
                        }
                        return "<textarea rows='4' style='width:100%;'>"+nValue+"</textarea>";
                    },
                },
                {
                    field: "",
                    title: "短信内容",
                    filterable: false,
                    width: 220,
                    template:function(item){
                        return "<textarea rows='4' style='width:100%;'>"+item.message+"</textarea>";
                    },
                },
                {
                    field: "",
                    title: "返回信息",
                    filterable: false,
                    width: 220,
                    template:function(item){
                        return "<textarea rows='4' style='width:100%;'>"+item.resultCode +'['+item.customMessage+']'+"</textarea>";
                    },
                },
                {
                    field: "createdDate",
                    title: "创建时间",
                    width: 100,
                    filterable: false,
                    format: "{0:yyyy/MM/dd HH:mm:ss}"
                }
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