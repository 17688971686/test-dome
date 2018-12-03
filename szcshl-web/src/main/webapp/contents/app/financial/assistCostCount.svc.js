(function () {
    'use strict';

    angular.module('app').factory('assistCostCountSvc', assistCostCount);

    assistCostCount.$inject = ['$http'];

    function assistCostCount($http) {
        var url_assistCostCount = rootPath + "/financialManager", url_back = '#/assistCostCountList';
        var service = {
            grid: grid,
            deleteassistCostCount: deleteassistCostCount,			//删除协审费用记录
            saveAssistCost: saveAssistCost,							//协审费用保存
            sumAssistCount: sumAssistCount,							//统计评审费用总和
            initAssistlProject: initAssistlProject,					//初始化协审费用关联的项目
            isUnsignedInteger: isUnsignedInteger,					//数字校验
            assistExportExcel: assistExportExcel,					//专家协申费用导出
            findSingAssistCostCount: findSingAssistCostCount,		//协审费用统计列表
            findSingAssistCostList: findSingAssistCostList,			//协审费录入列表
            findSignCostBySignId : findSignCostBySignId,            //根据项目ID获取项目的协审费用
        };

        return service;

        //S_根据项目ID获取项目的协审费用
        function findSignCostBySignId(signId,callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/financialManager/findSignCostBySignId",
                params: {
                    signId : signId
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
        }//E_根据项目ID获取项目的协审费用

        //S 协审费用统计列表
        function findSingAssistCostCount(singAssist, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/financialManager/findSingAssistCostCount",
                data: singAssist
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
        }//E 协审费用统计列表

        //S 协审费录入列表
        function findSingAssistCostList(singAssist, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/financialManager/findSingAssistCostList",
                data: singAssist
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
        }

        // E 协审费录入列表

        //S 专家协申费用导出
        function assistExportExcel(vm, businessId, fileName) {
            var fileName = escape(encodeURIComponent(fileName));
            window.open(rootPath + '/financialManager/exportExcel?fileName=' + fileName + '&businessId=' + businessId);
        }

        // E 专家协申费用导出


        //检查是否为正整数
        function isUnsignedInteger(value) {
            if ((/^(\+|-)?\d+$/.test(value)) && value > 0) {
                return true;
            } else {
                return false;
            }
        }

        //S 初始化协审费用关联的项目
        function initAssistlProject(businessId, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/financialManager/initfinancial",
                params: {
                    businessId: businessId,
                }
            };
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof  callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // E 初始化协审费用关联的项目

        //S 统计评审费用总和
        function sumAssistCount(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/financialManager/html/sumfinancial",
                params: {
                    businessId: vm.financial.businessId
                }
            };
            var httpSuccess = function success(response) {
                vm.financial.stageCount = 0;
                if(response.data){
                    vm.financial.stageCount = response.data;
                }
                $("#financialCount").html(common.htmlEscape(vm.financial.stageCount));
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //E 统计评审费用总和

        //S 保存报销记录
        function saveAssistCost(financials,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/financialManager",
                headers: {
                    "contentType": "application/json;charset=utf-8"  //设置请求头信息
                },
                traditional: true,
                dataType: "json",
                data: angular.toJson(financials),//将Json对象序列化成Json字符串，JSON.stringify()原生态方法
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
        }

        //E 保存报销记录
        //刷新页面
        function myrefresh() {
            window.location.reload();
        }

        // begin#删除协审费用记录
        function deleteassistCostCount(id, callBack) {
            var httpOptions = {
                method: 'delete',
                url: url_assistCostCount,
                data: id
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

        // end#删除协审费用记录

        //S_初始化grid
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/expertSelected/assistCostList", $("#searchform")),
                schema: {
                    data: "value",
                    model: {
                        id: "id",
                        fields: {
                            createdDate: {
                                type: "date"
                            }
                        }
                    }
                },
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
                    title: "",
                    width: 30,
                    template: function (item) {
                        switch (item.lightState) {
                            case "4":          //暂停
                                return $('#span1').html();
                                break;
                            case "8":         	//存档超期
                                return $('#span5').html();
                                break;
                            case "7":           //超过25个工作日未存档
                                return $('#span4').html();
                                break;
                            case "6":          	//发文超期
                                return $('#span3').html();
                                break;
                            case "5":          //少于3个工作日
                                return $('#span2').html();
                                break;
                            case "1":          //在办
                                return "";
                                break;
                            case "2":           //已发文
                                return "";
                                break;
                            case "3":           //已发送存档
                                return "";
                                break;
                            default:
                                return "";
                                ;
                        }
                    }
                },
                /* {
                 field: "",
                 title: "序号",
                 template: "<span class='row-number'></span>",
                 width: 50
                 },*/
                {
                    field: "projectname",
                    title: "项目名称",
                    width: 100,
                    filterable: false
                },

                {
                    field: "builtcompanyname",
                    title: "协审单位",
                    width: 100,
                    filterable: false,
                },

                {
                    field: "principal",
                    title: "项目负责人",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "signNum",
                    title: "协审登记号",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "totalCost",
                    title: "计划协审费用",
                    width: 120,
                    filterable: false,
                },
                {
                    field: "totalCost",
                    title: "实付协审费用",
                    width: 120,
                    filterable: false,
                },
                {
                    field: "payDate",
                    title: "付款日期",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "declareValue",
                    title: "申报金额",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "",
                    title: "操作",
                    width: 100,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.addCost('" + item.businessId + "')"
                        );
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
        }//E_初始化grid

    }
})();