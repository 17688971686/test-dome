(function () {
    'use strict';

    angular.module('app').factory('monthlyHistorySvc', monthlyHistory);

    monthlyHistory.$inject = ['$http'];

    function monthlyHistory($http) {
        var url_monthlyHistory = rootPath + "/monthlyNewsletter", url_back = '#/monthlyNewsletterList';
        var service = {
            monthlyHistoryGrid: monthlyHistoryGrid,//月报简报管理列表
            deleteGridOptions:deleteGridOptions,//已删除月报简报列表
            createmonthlyHistory: createmonthlyHistory,//添加月报简报历史数据
            deletemonthlyHistory: deletemonthlyHistory,//删除月报简报记录
            getmonthlyHistoryById: getmonthlyHistoryById,
            updatemonthlyHistory: updatemonthlyHistory
        };

        return service;

        // begin#updatemonthlyHistory
        function updatemonthlyHistory(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_monthlyHistory,
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
        function deletemonthlyHistory(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_monthlyHistory+"/deleteHistory",
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
        function createmonthlyHistory(monthly,callBack) {
                var httpOptions = {
                    method: 'post',
                    url: url_monthlyHistory+"/savaHistory",
                    data:monthly
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

        // begin#getmonthlyHistoryById
        function getmonthlyHistoryById(vm) {
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/monthlyHistory/html/findById",
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
        function monthlyHistoryGrid(vm) {

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_monthlyHistory+"/mothlyHistoryList"),
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
                pageSize: 5,
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
				    filterable : false,
				    template: "<span class='row-number'></span>"
				 },
                {
                    field: "startDay",
                    title: "开始时间",
                    width: 100,
                    filterable: false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "startDay",
                    title: "结束时间",
                    width: 100,
                    filterable: false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "stageMunber",
                    title: "已评审项目数",
                    width: 120,
                    filterable: false
                },
                
                {
                    field: "declarationSum",
                    title: "报审总投资",
                    width: 100,
                    filterable: false
                },
                {
                    field: "assessorSum",
                    title: "审核总投资",
                    width: 100,
                    filterable: false
                },
            
                {
                    field: "authorizedUser",
                    title: "录入人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "authorizedTime",
                    title: "录入时间",
                    width: 100,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 100,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.del('" + item.id + "')", item.id);
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                dataBound:dataBound,
                columns: columns,
                resizable: true
            };

        }// end fun grid
        
        // begin#grid
        function deleteGridOptions(vm) {
            /// Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_monthlyHistory+"/deleteHistoryList"),
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
                pageSize: 5,
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
				    filterable : false,
				    template: "<span class='row-number'></span>"
				 },
                {
                    field: "startDay",
                    title: "开始时间",
                    width: 100,
                    filterable: false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "startDay",
                    title: "结束时间",
                    width: 100,
                    filterable: false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "stageMunber",
                    title: "已评审项目数",
                    width: 120,
                    filterable: false
                },
                
                {
                    field: "declarationSum",
                    title: "报审总投资",
                    width: 100,
                    filterable: false
                },
                {
                    field: "assessorSum",
                    title: "审核总投资",
                    width: 100,
                    filterable: false
                },
            
                {
                    field: "authorizedUser",
                    title: "录入人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "authorizedTime",
                    title: "录入时间",
                    width: 100,
                    filterable: false
                },
                
            ];
            // End:column

            vm.deleteGridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                dataBound:dataBound,
                columns: columns,
                resizable: true
            };

        }// end fun grid

        
    }
})();