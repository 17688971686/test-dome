(function () {
    'use strict';

    angular.module('app').factory('monthlyMultiyearSvc', monthlyMultiyear);

    monthlyMultiyear.$inject = ['$http', 'bsWin'];

    function monthlyMultiyear($http, bsWin) {
        var url_monthlyMultiyear = rootPath + "/monthlyNewsletter", url_back = '#/monthlyNewsletterList';
        var url_user = rootPath + "/user";
        var service = {
            createmonthlyMultiyear: createmonthlyMultiyear, //添加中心文件稿纸
            initMonthlyMultiyear: initMonthlyMultiyear,     //初始化中心文件稿纸
            monthlyMultiyearGrid: monthlyMultiyearGrid,     //月报简报文件查询列表
            monthlyYearGrid: monthlyYearGrid,  			    //月报简报详情列表（跟拟补充资料函一个表）
            getmonthlyMultiyearById: getmonthlyMultiyearById,//根据ID查找中心文件稿纸
            updatemonthlyMultiyear: updatemonthlyMultiyear,	//更新中心文件稿纸
            //updateApprove: updateApprove,					//领导审批中心文件
            addSuppQuery: addSuppQuery,						//查询
            findAllOrg: findAllOrg,							//查询部门
            findAllUser: findAllUser,  						//查询用户
            deletemonthlyMultiyear: deletemonthlyMultiyear, //删除年度（中心）月报简报记录
            startFlow: startFlow,                           //启动流程
            initFlowDeal: initFlowDeal                      //初始化流程数据
        };

        return service;

        //S_查询用户
        function findAllUser(vm) {
            var httpOptions = {
                method: 'get',
                url: common.format(url_user + "/findAllUsers")
            }
            var httpSuccess = function success(response) {
                vm.userlist = {};
                vm.userlist = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //E_查询用户

        //S_查询部门列表
        function findAllOrg(vm) {
            var httpOptions = {
                method: 'get',
                url: common.format(url_user + "/getOrg")
            }
            var httpSuccess = function success(response) {
                vm.orglist = {};
                vm.orglist = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //E_查询部门列表

        //查询
        function addSuppQuery(vm) {
            vm.monthlyYearGrid.dataSource._skip=0;
            vm.monthlyYearGrid.dataSource.read();
        }

        //S 领导审批中心文件处理
       /* function updateApprove(vm) {
            var httpOptions = {
                method: 'post',
                url: url_monthlyMultiyear + "/updateApprove",
                data: vm.suppletter
            };
            var httpSuccess = function success(response) {
                bsWin.success("操作成功！")
            };
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }*/
        //E 领导审批中心文件处理

        //S 初始化中心文件稿纸
        function initMonthlyMultiyear(callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/monthlyNewsletter/initMonthlyMultiyear",
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
        //E 初始化中心文件稿纸

        // begin#updatemonthlyMultiyear
        function updatemonthlyMultiyear(suppletter, callBack) {
            var httpOptions = {
                method: 'post',
                url: url_monthlyMultiyear + "/saveMonthlyMultiyear",
                data: suppletter
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

        // begin#删除年度(中心)月报简报记录
        function deletemonthlyMultiyear(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_monthlyMultiyear + "/deleteMutiyear",
                data: id
            };

            var httpSuccess = function success(response) {
                bsWin.alert("操作成功",function () {
                    vm.isSubmit = false;
                    vm.monthlyYearGrid.dataSource.read();
                })

            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#添加中心文件稿纸
        function createmonthlyMultiyear(suppletter, callBack) {
            var httpOptions = {
                method: 'post',
                url: url_monthlyMultiyear + "/saveMonthlyMultiyear",
                data: suppletter
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

        //end#添加月报简报历史数据

        // begin#getmonthlyMultiyearById
        function getmonthlyMultiyearById(id, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/addSuppLetter/findById",
                params: {id: id}
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

        //S 年度月报简报列表
        function monthlyYearGrid(vm) {
     /*       var dataSource = new kendo.data.DataSource({
                type : 'odata',
                transport : common.kendoGridConfig().transport(rootPath + "/addSuppLetter/monthlyMultiyearList", $("#form_monthly"), {filter:"fileYear eq '"+vm.suppletter.fileYear+"' and fileType eq '2' and monthlyType eq '月报简报'"}),
                schema : common.kendoGridConfig().schema({
                    id : "id",
                    fields : {
                        createdDate : {
                            type : "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: false,
                pageSize: 10,
                sort : {
                    field : "createdDate",
                    dir : "desc"
                }
            });*/
            var dataSource = common.kendoGridDataSource(rootPath + "/addSuppLetter/monthlyMultiyearList",$("#form_monthly"),vm.queryParams.page,vm.queryParams.pageSize,vm.gridParams);
        /*    //S_序号
            var dataBound = function () {
                var rows = this.items();
                $(rows).each(function () {
                    var index = $(this).index() + 1;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            }
            //S_序号*/
            // Begin:column
            var columns = [
                {
                    field: "rowNumber",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },{
                    field: "title",
                    title: "文件标题",
                    width: 180,
                    filterable: false,
                    template: function (item) {
                        if(!item.processInstanceId){
                            return '<a ng-click="vm.saveView()" href="#/monthlyMultiyearEdit/'+vm.suppletter.fileYear+'/' + item.id + '" >' + item.title + '</a>';
                        }else{
                            return '<a ng-click="vm.saveView()" href="#/monthlyMultiyView/' + item.id + '" >' + item.title + '</a>';
                        }

                    }
                },
                {
                    field: "orgName",
                    title: "拟办部门",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "userName",
                    title: "拟稿人",
                    width: 120,
                    filterable: false
                },

                {
                    field: "suppLetterTime",
                    title: "拟稿时间",
                    width: 100,
                    filterable: false
                },
                {
                    field: "secretLevel",
                    title: "秘密等级",
                    width: 100,
                    filterable: false
                },

                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        var isStartFlow = true;
                        if(angular.isUndefined(item.processInstanceId) || item.processInstanceId == ''){
                            isStartFlow = false;
                        }
                        return common.format($('#columnBtns').html(),vm.suppletter.fileYear, item.id, isStartFlow,item.createdBy,item.id,"vm.del('" + item.id + "')");
                    }
                }
            ];
            // End:column
            vm.monthlyYearGrid = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
              /*  pageable: common.kendoGridConfig().pageable,*/
                noRecords: common.kendoGridConfig().noRecordMessage,
                pageable : common.kendoGridConfig(vm.queryParams).pageable,
                dataBound:common.kendoGridConfig(vm.queryParams).dataBound,
                columns: columns,
                resizable: true
            };
        }
        //E 年度月报简报列表

        // begin#中心文件查询列表
        function monthlyMultiyearGrid(vm) {

            var dataSource = common.kendoGridDataSource(rootPath + "/addSuppLetter/monthlyMultiyearList",$("#form"),vm.queryParams.page,vm.queryParams.pageSize,vm.gridParams);
            // End:dataSource


            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "rowNumber",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "title",
                    title: "文件标题",
                    width: "30%",
                    filterable: false,
                    template: function (item) {
                        return '<a ng-click="vm.saveView()" href="#/monthlyMultiyView/' + item.id + '" >' + item.title + '</a>';
                    }
                },
                {
                    field: "orgName",
                    title: "拟办部门",
                    width: "10%",
                    filterable: false,
                },
                {
                    field: "userName",
                    title: "拟稿人",
                    width: "10%",
                    filterable: false
                },

                {
                    field: "suppLetterTime",
                    title: "拟稿时间",
                    width: "10%",
                    filterable: false
                },
                {
                    field: "secretLevel",
                    title: "秘密等级",
                    width: "10%",
                    filterable: false
                },

                {
                    field: "",
                    title: "操作",
                    width: 80,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),item.id, item.appoveStatus);
                    }
                }
            ];
            // End:column
            vm.multiyearGrid = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                pageable : common.kendoGridConfig(vm.queryParams).pageable,
                dataBound:common.kendoGridConfig(vm.queryParams).dataBound,
                columns: columns,
                resizable: true
            };
        }// end#中心文件查询列表


        //S_startFlow
        function startFlow(id, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/monthlyNewsletter/startFlow",
                params: {
                    id: id
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
        }//E_startFlow

        //S_初始化流程数据
        function initFlowDeal(vm) {
            getmonthlyMultiyearById(vm.businessKey, function (data) {
                vm.suppletter = data;
            })
        }//E_initFlowDeal

    }
})();