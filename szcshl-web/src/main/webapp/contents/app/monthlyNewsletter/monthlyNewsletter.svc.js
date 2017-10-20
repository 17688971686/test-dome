(function () {
    'use strict';

    angular.module('app').factory('monthlyNewsletterSvc', monthlyNewsletter);

    monthlyNewsletter.$inject = ['$http'];

    function monthlyNewsletter($http) {
        var url_monthlyNewsletter = rootPath + "/monthlyNewsletter", url_back = '#/monthlyNewsletterList';
        var service = {
            monthlyNewsletterGrid: monthlyNewsletterGrid,//月报简报管理列表
            monthlyDeleteGrid:monthlyDeleteGrid,//已删除月报简报列表
            theMonthGrid:theMonthGrid,//月报简报列表
            createMonthlyNewsletter: createMonthlyNewsletter,//保存月报简报
            updateMonthlyNewsletter: updateMonthlyNewsletter, //月报简报编辑
            deleteMonthlyNewsletter: deleteMonthlyNewsletter,//删除月报简报记录
            getMonthlyNewsletterById: getMonthlyNewsletterById,
        };

        return service;

        // begin#updateMonthlyNewsletter
        function updateMonthlyNewsletter(vm) {
        	console.log(vm.monthly);
        	 common.initJqValidation();
             var isValid = $('form').valid();
             if (isValid) {
                 vm.isSubmit = true;
                 vm.monthly.id = vm.id;// id
                 var httpOptions = {
                     method: 'put',
                     url: url_monthlyNewsletter+"/monthlyEdit",
                     data: vm.monthly
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

        // begin#deleteMonthlyNewsletter
        function deleteMonthlyNewsletter(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_monthlyNewsletter+"/deleteMonthlyData",
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

        // begin#保存月报简报
        function createMonthlyNewsletter(monthly,callBack) {
                var httpOptions = {
                    method: 'post',
                    url: url_monthlyNewsletter+"/savaMonthlyNewsletter",
                    data: monthly
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
        //end#保存月报简报
        
        // begin#getMonthlyNewsletterById
        function getMonthlyNewsletterById(vm) {
      
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/monthlyNewsletter/html/findById",
                params:{id:vm.id}
            };
            var httpSuccess = function success(response) {
                vm.monthly = response.data;
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });                       
        }

        // begin#月报简报管理列表
        function monthlyNewsletterGrid(vm) {

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_monthlyNewsletter+"/getMonthlyList"),
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
                    field: "reportMultiyear",
                    title: "报告年度",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "theMonths",
                    title: "报告月份",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "remark",
                    title: "报告说明",
                    width: 100,
                    filterable: false
                },
                {
                    field: "authorizedUser",
                    title: "编制人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "authorizedTime",
                    title: "编制时间",
                    width: 100,
                    filterable: false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                             item.id,"vm.del('" + item.id + "')");
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
        
        // begin#删除月报简报列表
        function monthlyDeleteGrid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_monthlyNewsletter+"/deleteMonthlyList"),
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
                    field: "reportMultiyear",
                    title: "报告年度",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "theMonths",
                    title: "报告月份",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "remark",
                    title: "报告说明",
                    width: 100,
                    filterable: false
                },
                {
                    field: "authorizedUser",
                    title: "编制人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "authorizedTime",
                    title: "编制时间",
                    width: 100,
                    filterable: false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
               
            ];
            // End:column

            vm.monthlyDeleteGridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                dataBound:dataBound,
                columns: columns,
                resizable: true
            };

        }// end fun 删除月报简报列表

        // begin#月报简报列表
        function theMonthGrid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_monthlyNewsletter+"/getMonthlyList"),
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
                    field: "monthlyNewsletterName",
                    title: "名称",
                    width: 100,
                    filterable: false,
                    template:function(item){
                    	return '<a href="#/monthlyFindByMultiyear/'+item.id+'" >'+item.monthlyNewsletterName+'</a>';
                    }
                },
                {
                    field: "reportMultiyear",
                    title: "年份",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "remark",
                    title: "备注",
                    width: 100,
                    filterable: false
                },
                {
                    field: "authorizedTime",
                    title: "添加时间",
                    width: 100,
                    filterable: false
                },
                
            ];
            // End:column

            vm.theMonthGridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                dataBound:dataBound,
                columns: columns,
                resizable: true
            };

        }// end fun 月报简报列表
        
    }
})();