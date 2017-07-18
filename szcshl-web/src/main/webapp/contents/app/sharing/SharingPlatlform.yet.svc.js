(function () {
    'use strict';

    angular.module('app').factory('sharingPlatlformYetSvc', sharingPlatlformYet);

    sharingPlatlformYet.$inject = ['$http'];

    function sharingPlatlformYet($http) {
        var url_sharingPlatlformYet = rootPath + "/sharingPlatlform", url_back = '#/sharingPlatlformYetList';
        var url_user=rootPath +'/user';
        var service = {
            grid: grid,
            getsharingPlatlformYetById: getsharingPlatlformYetById,
            createsharingPlatlformYet: createsharingPlatlformYet,
            deletesharingPlatlformYet: deletesharingPlatlformYet,
            updatesharingPlatlformYet: updatesharingPlatlformYet,
            publish:publish,//发布
            findAllOrglist:findAllOrglist,//查询部门列表
            findAllUsers:findAllUsers,//所有用户
        };

        return service;
        
        //S 所有用户
        function findAllUsers(vm){
        	
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
        //E 所有用户
      //S_查询部门列表
		function findAllOrglist(vm){
			
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
        function publish(vm,id){
        	var httpOptions = {
                    method: 'put',
                    url: rootPath + "/sharingPlatlform/editPubStatus",
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
                                fn: function () {
                                	  $('.alertDialog').modal('hide');
                                      $('.modal-backdrop').remove();
                                      vm.gridOptions.dataSource.read();
                                }
                            })
                        }

                    })
                };

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
        }

        // begin#updatesharingPlatlformYet
        function updatesharingPlatlformYet(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_sharingPlatlformYet,
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

        // begin#deletesharingPlatlformYet
        function deletesharingPlatlformYet(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_sharingPlatlformYet,
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

        // begin#createsharingPlatlformYet
        function createsharingPlatlformYet(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;

                var httpOptions = {
                    method: 'post',
                    url: url_sharingPlatlformYet,
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

        // begin#getsharingPlatlformYetById
        function getsharingPlatlformYetById(vm) {
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/sharingPlatlformYet/html/findById",
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
        function grid(vm) {
        	
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_sharingPlatlformYet + "/findByODataYet",$("#formSharingPub")),
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
            var dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            };
            //S_序号

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.sharId)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "unitSort",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                
                {
                    field: "theme",
                    title: "发布主题",
                    width: 100,
                    filterable: true
                },
                {
                    field: "pubDept",
                    title: "发布部门",
                    width: 100,
                    filterable: true
                },
                {
                    field: "publishUsername",
                    title: "发布人",
                    width: 100,
                    filterable: true
                },
                {
                    field: "publishDate",
                    title: "发布时间",
                    width: 100,
                    filterable: true
                },
                {
                    field: "remark",
                    title: "备注",
                    width: 100,
                    filterable: true
                },
                {
                    field: "isPublish",
                    title: "发布状态",
                    width: 100,
                    filterable: true,
                    template: function(item){
                    	
                    	if(item.isPublish){
                    		if(item.isPublish == '9'){
                    			 return '<span style="color:green;">已发布</span>';
                    		}else if(item.isPublish == '0'){
                    			return '<span style="color:red;">未发布</span>';
                    		}
                    	}else{
                    		return '<span style="color:red;">未发布</span>';
                    	}
                    }
                },
               
               
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                    	var ispublish = false;
                    	if(item.isPublish == '9'){
                    		 ispublish = true;
                    	}
                    	else if(item.isPublish == '0'){
                    		 ispublish = false;
                    	}
                        return common.format($('#columnBtns').html(),
                        		"vm.publish('" +item.sharId + "')", ispublish ,
                        		 "vm.closePublish('" +item.sharId + "')",ispublish );
                    	
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