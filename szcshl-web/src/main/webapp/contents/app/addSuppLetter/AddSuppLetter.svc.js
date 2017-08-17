(function () {
    'use strict';

    angular.module('app').factory('addSuppLetterSvc', addSuppLetter);

    addSuppLetter.$inject = ['$http'];

    function addSuppLetter($http) {
        var url_addSuppLetter = rootPath + "/addSuppLetter", url_back = '#/addSuppLetterList';
        var service = {
            grid: grid,
            getAddSuppLetterById: getAddSuppLetterById,
            createAddSuppLetter: createAddSuppLetter,
            deleteAddSuppLetter: deleteAddSuppLetter,
            updateAddSuppLetter: updateAddSuppLetter,
            initSuppLetter:initSuppLetter,//初始化补充资料函
            createFilenum:createFilenum,//生成文件字号
        };

        return service;
        
        //S 生成文件字号 
        function createFilenum(vm){
        	var httpOptions = {
                    method: 'post',
                    url: url_addSuppLetter + "/createFileNum",
                    params:{id:vm.suppletter.id}
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
                                     myrefresh();
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
      //E 生成文件字号 
        
      //刷新页面
        function myrefresh(){
        	 window.location.reload();
        }
        
        //S 初始化补充资料函
        function initSuppLetter(vm){
        	var httpOptions = {
                    method: 'get',
                    url: url_addSuppLetter + "/initaddSuppLetterData",
                    params:{
                    	signid:vm.suppletter.signid,
                    	id:vm.suppletter.id
                    }
                };
                var httpSuccess = function success(response) {
                    vm.suppletter = response.data.suppletterDto;
                    
                };
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });     
        }
        //E 初始化补充资料函
        
        // begin#updateAddSuppLetter
        function updateAddSuppLetter(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_addSuppLetter,
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

        // begin#deleteAddSuppLetter
        function deleteAddSuppLetter(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_addSuppLetter,
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

        // begin#createAddSuppLetter
        function createAddSuppLetter(vm) {
            common.initJqValidation($('#suppletter_form'));
            var isValid = $('#suppletter_form').valid();
            if (isValid) {
                vm.isSubmit = true;
                var httpOptions = {
                    method: 'post',
                    url: url_addSuppLetter +"/add",
                    data: vm.suppletter
                };
                var httpSuccess = function success(response) {
                	
                	console.log(response.data);
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
                                    myrefresh();
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

        // begin#getAddSuppLetterById
        function getAddSuppLetterById(vm) {
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/addSuppLetter/html/findById",
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
                transport: common.kendoGridConfig().transport(url_addSuppLetter),
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
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "id",
                    title: "id",
                    width: 100,
                    filterable: true
                },
                {
                    field: "orgName",
                    title: "orgName",
                    width: 100,
                    filterable: true
                },
                {
                    field: "userName",
                    title: "userName",
                    width: 100,
                    filterable: true
                },
                {
                    field: "suppLetterTime",
                    title: "suppLetterTime",
                    width: 100,
                    filterable: true,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "disapDate",
                    title: "disapDate",
                    width: 100,
                    filterable: true,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "secretLevel",
                    title: "secretLevel",
                    width: 100,
                    filterable: true
                },
                {
                    field: "mergencyLevel",
                    title: "mergencyLevel",
                    width: 100,
                    filterable: true
                },
                {
                    field: "filenum",
                    title: "filenum",
                    width: 100,
                    filterable: true
                },
                {
                    field: "title",
                    title: "title",
                    width: 100,
                    filterable: true
                },
                {
                    field: "dispaRange",
                    title: "dispaRange",
                    width: 100,
                    filterable: true
                },
                {
                    field: "suppleterSuggest",
                    title: "suppleterSuggest",
                    width: 100,
                    filterable: true
                },
                {
                    field: "meetingSuggest",
                    title: "meetingSuggest",
                    width: 100,
                    filterable: true
                },
                {
                    field: "leaderSuggest",
                    title: "leaderSuggest",
                    width: 100,
                    filterable: true
                },
                {
                    field: "printnum",
                    title: "printnum",
                    width: 100,
                    filterable: true
                },
                {
                    field: "signid",
                    title: "signid",
                    width: 100,
                    filterable: true
                },
                {
                    field: "fileSeq",
                    title: "fileSeq",
                    width: 100,
                    filterable: true
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
                columns: columns,
                resizable: true
            };

        }// end fun grid

    }
})();