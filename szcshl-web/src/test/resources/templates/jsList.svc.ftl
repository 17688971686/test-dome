<#assign idField = "id">
(function () {
    'use strict';

    angular.module('app').factory('${info.beanName?uncap_first}Svc', ${info.beanName?uncap_first});

    ${info.beanName?uncap_first}.$inject = ['$http'];

    function ${info.beanName?uncap_first}($http) {
        var url_${info.beanName?uncap_first} = rootPath + "/${info.beanName?uncap_first}", url_back = '#/${info.beanName?uncap_first}List';
        var service = {
            grid: grid,
            get${info.beanName!''}ById: get${info.beanName!''}ById,
            create${info.beanName!''}: create${info.beanName!''},
            delete${info.beanName!''}: delete${info.beanName!''},
            update${info.beanName!''}: update${info.beanName!''}
        };

        return service;

        // begin#update${info.beanName!''}
        function update${info.beanName!''}(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.${idField!'id'} = vm.${idField!'id'};// id

                var httpOptions = {
                    method: 'put',
                    url: url_${info.beanName?uncap_first},
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

        // begin#delete${info.beanName!''}
        function delete${info.beanName!''}(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_${info.beanName?uncap_first},
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

        // begin#create${info.beanName!''}
        function create${info.beanName!''}(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;

                var httpOptions = {
                    method: 'post',
                    url: url_${info.beanName?uncap_first},
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

        // begin#get${info.beanName!''}ById
        function get${info.beanName!''}ById(vm) {
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/${info.beanName?uncap_first}/html/findById",
                params:{${idField}:vm.${idField}}
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
                transport: common.kendoGridConfig().transport(url_${info.beanName?uncap_first}),
                schema: common.kendoGridConfig().schema({
                    id: "${idField!'id'}",
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
                            item.${idField!'id'})
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
<#list info.fields as f><#if f.isId=true><#assign idField = f.name></#if>
                {
                    field: "${f.name!''}",
                    title: "<#if f.comment??>${f.comment}<#else>${f.name!''}</#if>",
                    width: 100,
                    filterable: true<#if f.type='Date'>,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"</#if>
                },
</#list>
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.del('" + item.${idField!'id'} + "')", item.${idField!'id'});
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