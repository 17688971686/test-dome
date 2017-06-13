(function () {
    'use strict';

    angular.module('app').factory('assistUnitUserSvc', assistUnitUser);

    assistUnitUser.$inject = ['$http'];

    function assistUnitUser($http) {
        var url_assistUnitUser = rootPath + "/assistUnitUser", url_back = '#/assistUnitUser';
        var service = {
            grid: grid,
            getAssistUnitUserById: getAssistUnitUserById,
            createAssistUnitUser: createAssistUnitUser,
            deleteAssistUnitUser: deleteAssistUnitUser,
            updateAssistUnitUser: updateAssistUnitUser,
            getAssistUnit: getAssistUnit,		//获取协审单位
            queryAssistUnitUser:queryAssistUnitUser		//模糊查询
        };

        return service;

        // begin#updateAssistUnitUser
        function updateAssistUnitUser(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid && vm.isUserExist==false) {
                vm.isSubmit = true;
                vm.assistUnitUser.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_assistUnitUser,
                    data: vm.assistUnitUser
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
                                    location.href=url_back;
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

        // begin#deleteAssistUnitUser
        function deleteAssistUnitUser(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_assistUnitUser,
                data: id
            };

            var httpSuccess = function success(response) {
            	   common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.isSubmit = false;
                        vm.gridOptions.dataSource.read();
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

        // begin#createAssistUnitUser
        function createAssistUnitUser(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid && vm.isUserExist==false) {
                vm.isSubmit = true;

                var httpOptions = {
                    method: 'post',
                    url: url_assistUnitUser,
                    data: vm.assistUnitUser
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

        // begin#getAssistUnitUserById
        function getAssistUnitUserById(vm) {
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/assistUnitUser/html/findById",
                params:{id:vm.id}
            };
            var httpSuccess = function success(response) {
                vm.assistUnitUser = response.data;
                vm.assistUnitDto=vm.assistUnitUser.assistUnitDto;
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });                       
        }
        
        function getAssistUnit(vm){
        	var httpOptions={
        		method: 'post',
        		url: rootPath+"/assistUnitUser/getAssistUnit"
        	}
        	var httpSuccess=function success(response){
	        	vm.assistUnit={},
	        	vm.assistUnit=response.data;
        	
        	}
        	
        	common.http({
        		vm:vm,
	        	$http:$http,
	        	httpOptions:httpOptions,
	        	success:httpSuccess
        	});
        }

             // begin#grid
        function grid(vm) {

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_assistUnitUser+"/findByOData",$("#assistUnitUserform")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        },
                        modifiedDate: {
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
            
            //序号
            var dataBound=function(){
            var rows=this.items();
            var page=this.pager.page() - 1;
            var pagesize=this.pager.pageSize();
            $(rows).each(function(){
            var index=$(this).index() + 1 + page * pagesize;
            var rowLabel=$(this).find(".row-number");
            $(rowLabel).html(index);
            });
            
            }//end 序号

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.id)
                    },
                    filterable: false,
                    width: 10,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "rowNumber",
                    title: "序号",
                    width: 20,
                    filterable: false,
                    template:"<span class='row-number'></span>"
                },
                {
                    field: "userName",
                    title: "名称",
                    width: 100,
                    filterable: false
                },
                {
                    field: "phoneNum",
                    title: "电话号码",
                    width: 100,
                    filterable: false
                },
                {
                    field: "position",
                    title: "职位",
                    width: 100,
                    filterable: false
                },
                {
                    field: "assistUnit.unitName",
                    title: "协审单位",
                    width: 100,
                    filterable: false
                },
                
                {
                    field: "",
                    title: "操作",
                    width: 140,
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
        
        function queryAssistUnitUser(vm){
        	 vm.gridOptions.dataSource.read();
        }
    }
})();