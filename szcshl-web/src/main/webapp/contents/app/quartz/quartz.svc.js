(function () {
    'use strict';

    angular.module('app').factory('quartzSvc', quartz);

    quartz.$inject = ['$http'];

    function quartz($http) {
        var url_quartz = rootPath + "/quartz", url_back = '#/quartz';
        var service = {
            grid: grid,
            getQuartzById: getQuartzById,
            saveQuartz: saveQuartz,
            deleteQuartz: deleteQuartz,
            updateQuartz: updateQuartz,
            quartzExecute : quartzExecute,	//执行定时器
            quartzStop : quartzStop	//停止执行定时器
        };

        return service;
        
        //begin quartzExecute
        function quartzExecute(vm,id){
        	var httpOptions={
        		method: "put",
        		url:url_quartz+"/quartzExecute",
        		params:{quartzId : id}
        		
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
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                    vm.gridOptions.dataSource.read();
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

        }//end quartzExecute
        
        //begin quartzStop
        function quartzStop(vm,id){
        	var httpOptions={
        		method: "put",
        		url:url_quartz+"/quartzStop",
        		params:{quartzId : id}
        		
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
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                    vm.gridOptions.dataSource.read();
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
        }//end quartzStop

        // begin#updateQuartz
        function updateQuartz(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.quartz.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_quartz+"/updateQuartz",
                    data: vm.quartz
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
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                    window.parent.$("#quartz_edit_div").data("kendoWindow").close();
                                    vm.gridOptions.dataSource.read();
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

        // begin#deleteQuartz
        function deleteQuartz(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_quartz,
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
                            closeDialog: true,
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

        // begin#createQuartz
        function saveQuartz(vm) {
            common.initJqValidation($("#quartz_form"));
            var isValid = $("#quartz_form").valid();
            if (isValid) {
                vm.isSubmit = true;
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/quartz",
                    data: vm.quartz
                };

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                closeDialog: true,
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                    window.parent.$("#quartz_edit_div").data("kendoWindow").close();
                                    vm.gridOptions.dataSource.read();
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

        // begin#getQuartzById
        function getQuartzById(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/quartz/html/findById",
                params: {id: vm.id}
            };
            var httpSuccess = function success(response) {
                vm.quartz = response.data;
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
                transport: common.kendoGridConfig().transport(rootPath + "/quartz/findByOData"),
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
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "quartzName",
                    title: "定时器名称",
                    width: 100,
                    filterable: true
                },
                {
                    field: "className",
                    title: "类名",
                    width: 100,
                    filterable: true
                },
                {
                    field: "cronExpression",
                    title: "表达式",
                    width: 100,
                    filterable: true
                },
                {
                    field: "",
                    title: "执行状态",
                    width: 80,
                    filterable: true,
                    template :function (item){
                    	if(item.curState){
                    		if(item.curState=="9"){
                    			return "在执行";
                    		}
                    		if(item.curState=="0"){
                    			return "未执行";
                    		}
                    	}else{
                    		return "";
                    	}
                    }
                },
                {
                    field: "",
                    title: "是否可用",
                    width: 80,
                    filterable: true,
                    template :function (item){
                    	if(item.isEnable){
                    		if(item.isEnable=="0"){
	                    		return "停用";
                    		}
                    		if(item.isEnable=="9"){
                    			return "在用";
                    		}
                    	}else {
                    		return"";
                    	}
                    }
                },
                {
                    field: "descInfo",
                    title: "描述",
                    width: 180,
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.del('" + item.id + "')",  "vm.edit('" + item.id + "')",item.isEnable,"vm.execute('"+item.id+"')",item.curState,"vm.stop('"+item.id+"')");
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
                resizable: true
            };
        }// end fun grid

    }
})();