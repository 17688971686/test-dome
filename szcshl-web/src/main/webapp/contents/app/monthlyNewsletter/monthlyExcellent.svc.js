(function () {
    'use strict';

    angular.module('app').factory('monthlyExcellentSvc', monthlyExcellent);

    monthlyExcellent.$inject = ['$http'];

    function monthlyExcellent($http) {
        var url_monthlyExcellent = rootPath + "/monthlyNewsletter", url_back = '#/monthlyNewsletterList';
        var service = {
            monthlyExcellentGrid: monthlyExcellentGrid,//优秀评审报告列表
            
            createmonthlyExcellent: createmonthlyExcellent,//添加月报简报历史数据
            deletemonthlyExcellent: deletemonthlyExcellent,//删除月报简报记录
            getmonthlyExcellentById: getmonthlyExcellentById,
            updatemonthlyExcellent: updatemonthlyExcellent
        };

        return service;

        // begin#updatemonthlyExcellent
        function updatemonthlyExcellent(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_monthlyExcellent,
                    data: vm.model
                }

                var httpSuccess = function success(response) {

                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {

                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                }
                            })
                        }

                    })
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            } else {
                // common.alert({
                // vm:vm,
                // msg:"您填写的信息不正确,请核对后提交!"
                // })
            }

        }

        // begin#删除月报简报记录
        function deletemonthlyExcellent(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_monthlyExcellent+"/deleteHistory",
                data: id
            };

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                    	common.alert({
                            vm: vm,
                            msg: "操作成功",
                            closeDialog :true,
                            fn: function () {
                            	vm.isSubmit = false;
                                vm.gridOptions.dataSource.read();
                            }
                        })
                    }
                });
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#添加月报简报历史数据
        function createmonthlyExcellent(monthly,callBack) {
                var httpOptions = {
                    method: 'post',
                    url: url_monthlyExcellent+"/savaHistory",
                    data:monthly
                };
                var httpSuccess = function success(response) {
                	 if (callBack != undefined && typeof callBack == 'function') {
                         callBack(response.data);
                     }
                };

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

        }
      //end#添加月报简报历史数据

        // begin#getmonthlyExcellentById
        function getmonthlyExcellentById(vm) {
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/monthlyExcellent/html/findById",
                params:{id:vm.id}
            };
            var httpSuccess = function success(response) {
                vm.model = response.data;
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });                       
        }

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