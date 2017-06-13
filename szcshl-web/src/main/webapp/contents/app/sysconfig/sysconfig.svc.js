(function () {
    'use strict';

    angular.module('app').factory('sysConfigSvc', sysConfig);

    sysConfig.$inject = ['$http'];

    function sysConfig($http) {
        var service = {
            queryList : queryList,			        //初始化表格
            deleteConfig : deleteConfig,            //删除参数
            saveConfig : saveConfig,                //保存系统参数

        };
        return service;

        //S_queryList
        function queryList(vm) {
            var httpOptions = {
                method : 'get',
                url : rootPath+"/sysConfig/queryList",
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm:vm,
                    response:response,
                    fn:function() {
                        vm.configList = new Array();
                        vm.configList = response.data;
                    }
                });
            }
            common.http({
                vm:vm,
                $http:$http,
                httpOptions:httpOptions,
                success:httpSuccess
            });
        }//E_queryList

        //S_deleteConfig
        function deleteConfig(vm,ids){
            var httpOptions = {
                method : 'delete',
                url : rootPath+"/sysConfig",
                params :{id:ids}
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm:vm,
                    response:response,
                    fn:function() {
                        common.alert({
                            vm:vm,
                            msg:"操作成功",
                            fn:function(){
                                $('.alertDialog').modal('hide');
                                $('.modal-backdrop').remove();
                                vm.isSubmit=false;
                                queryList(vm);

                            }
                        })
                    }
                });
            }
            common.http({
                vm:vm,
                $http:$http,
                httpOptions:httpOptions,
                success:httpSuccess
            });
        }//E_deleteConfig

        //S_saveConfig
        function saveConfig(vm){
            var httpOptions = {
                method : 'post',
                url : rootPath+"/sysConfig",
                data :vm.model
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm:vm,
                    response:response,
                    fn:function() {
                        common.alert({
                            vm:vm,
                            msg:"操作成功",
                            fn:function(){
                                $('.alertDialog').modal('hide');
                                $('.modal-backdrop').remove();
                                vm.isSubmit=false;
                                queryList(vm);
                            }
                        })
                    }
                });
            }
            common.http({
                vm:vm,
                $http:$http,
                httpOptions:httpOptions,
                success:httpSuccess
            });
        }//E_saveConfig

    }//E_sysConfig

})();