(function () {
    'use strict';

    angular.module('app').factory('assertStorageBusinessSvc', assertStorageBusiness);

    assertStorageBusiness.$inject = ['$http'];

    function assertStorageBusiness($http) {
        var url_assertStorageBusiness = rootPath + "/assertStorageBusiness", url_back = '#/assertStorageBusinessList';
        var service = {
            grid: grid,
            getAssertStorageBusinessById: getAssertStorageBusinessById,
            createAssertStorageBusiness: createAssertStorageBusiness,
            deleteAssertStorageBusiness: deleteAssertStorageBusiness,
            updateAssertStorageBusiness: updateAssertStorageBusiness,
            saveGoodsDetailBusinessDetail:saveGoodsDetailBusinessDetail,
            startFlow:startFlow,
            initFlowDeal:initFlowDeal
        };

        return service;

        // begin#updateAssertStorageBusiness
        function updateAssertStorageBusiness(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_assertStorageBusiness,
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

        // begin#deleteAssertStorageBusiness
        function deleteAssertStorageBusiness(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_assertStorageBusiness,
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

        // begin#createAssertStorageBusiness
        function createAssertStorageBusiness(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;

                var httpOptions = {
                    method: 'post',
                    url: url_assertStorageBusiness,
                    data: vm.model
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
                                    location.href = url_back;
                                }
                            });
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
        }

        function saveGoodsDetailBusinessDetail(conditions,callBack){
            console.log(conditions);
            var httpOptions = {
                method : 'post',
                url : rootPath + "/assertStorageBusiness/saveGoodsDetailList",
                headers:{
                    "contentType":"application/json;charset=utf-8"  //设置请求头信息
                },
                traditional: true,
                dataType : "json",
                data : angular.toJson(conditions)//将Json对象序列化成Json字符串，JSON.stringify()原生态方法
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess
            });
        }

        //S_启动流程
        function startFlow(conditions,isCommit,callBack){
            isCommit = true;
            var httpOptions = {
                method : 'post',
                url : rootPath + "/assertStorageBusiness/startFlow",
                headers:{
                    "contentType":"application/json;charset=utf-8"  //设置请求头信息
                },
                traditional: true,
                dataType : "json",
                data : angular.toJson(conditions)//将Json对象序列化成Json字符串，JSON.stringify()原生态方法
            }
            var httpSuccess = function success(response) {
                isCommit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError : function(){isCommit = false;}
            });
        }//E_startFlow

        //S_初始化资产采购流程信息
        function initFlowDeal(vm){
            //vm.businessKey,vm.taskId,vm.instanceId
            var httpOptions = {
                method: 'get',
                url: rootPath + "/assertStorageBusiness/html/findById",
                params : {
                    id:vm.businessKey
                }
            };
            var httpSuccess = function success(response) {
                vm.model = response.data;
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError : function(){}
            });
        }//E_initFlowDeal

        // begin#getAssertStorageBusinessById
        function getAssertStorageBusinessById(vm) {
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/assertStorageBusiness/html/findById",
                params:{id:vm.businessId}
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
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/assertStorageBusiness/findByOData", $("#myTopicForm"),{filter: "createdBy eq ${CURRENT_USER.id}"}),
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
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />", item.id)
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
                    field: "businessName",
                    title: "业务流程名称",
                    width: "20%",
                    filterable: false
                },
                {
                    field: "createdDate",
                    title: "创建日期",
                    width: "15%",
                    filterable: false,
                    format: "{0:yyyy/MM/dd HH:mm:ss}"
                },
                {
                    field: "",
                    title: "已发起流程",
                    width: "15%",
                    filterable: false,
                    template: function (item) {
                        if(item.processInstanceId){
                            return "是";
                        }else{
                            return "否";
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        var isStartFlow = item.processInstanceId?true:false;
                        return common.format($('#columnBtns').html(), item.businessId, isStartFlow);
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
                dataBound:dataBound,
                resizable: true
            };
        }// end fun grid

    }
})();