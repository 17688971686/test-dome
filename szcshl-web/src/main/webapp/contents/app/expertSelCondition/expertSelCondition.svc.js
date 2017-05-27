(function () {
    'use strict';

    angular.module('app').factory('expertConditionSvc', expertCondition);

    expertCondition.$inject = ['$http'];

    function expertCondition($http) {
        var service = {
            initConditionAndWP : initConditionAndWP,      //初始化选择条件和评审方案信息
        	saveCondition:saveCondition,	    //保存抽取条件
            deleteSelConditions:deleteSelConditions,    //删除抽取条件
        };
        return service;

        //S_initCondition
        function initConditionAndWP(vm){
            var httpOptions = {
                method : 'get',
                url : rootPath + "/expertSelCondition/html/findByWorkProId",
                params:{
                    workProId : vm.expertReview.workProgramId
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm : vm,
                    response : response,
                    fn : function() {
                        if(response.data.conditionList){
                            vm.conditions = response.data.conditionList;
                            if(vm.conditions.length > 0){
                                vm.conditions.forEach(function (t, number) {
                                    if(t.sort > vm.conditionIndex){
                                        vm.conditionIndex = t.sort;
                                    }
                                });
                                vm.conditionIndex++;
                            }
                        }
                        if(response.data.workProgram){
                            vm.workProgram = response.data.workProgram;
                            if(vm.workProgram.isSelete && vm.workProgram.isSelete == 9){
                                vm.isAutoSelectExpert = true;
                            }else{
                                vm.isAutoSelectExpert = false;
                            }
                            if(vm.workProgram.isComfireResult && vm.workProgram.isComfireResult == 9){
                                vm.isComfireResult = true;
                            }else{
                                vm.isComfireResult = false;
                            }
                        }
                    }
                });
            }
            common.http({
                vm : vm,
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess
            });
        }//E_initCondition

        //S_saveCondition
		function saveCondition(vm) {
            var httpOptions = {
                method : 'post',
                url : rootPath + "/expertSelCondition/saveConditionList",
                headers:{
                    "contentType":"application/json;charset=utf-8"  //设置请求头信息
                },
                traditional: true,
                dataType : "json",
                data : angular.toJson(vm.conditions),//将Json对象序列化成Json字符串，JSON.stringify()原生态方法
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm : vm,
                    response : response,
                    fn : function() {
                        vm.conditions = response.data;
                        vm.conditions.forEach(function (t, number) {
                            t.workProgramId = vm.expertReview.workProgramId;
                        });
                        common.alert({
                            vm: vm,
                            msg: "操作成功！",
                            closeDialog: true
                        })
                    }
                });
            }
            common.http({
                vm : vm,
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess
            });
        }//E_saveCondition

        //S_deleteSelConditions
        function deleteSelConditions(vm,delIds){
            vm.iscommit = true;
            var httpOptions = {
                method : 'delete',
                url : rootPath + "/expertSelCondition",
                params:{
                    id : delIds,
                    workProId : vm.expertReview.workProgramId,
                    deleteEP : false
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm : vm,
                    response : response,
                    fn : function() {
                        vm.iscommit = false;
                        common.alert({
                            vm: vm,
                            msg: "操作成功！",
                            closeDialog: true
                        })
                    }
                });
            }
            common.http({
                vm : vm,
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess,
                onError: function(response){vm.iscommit = false;}
            });
        }//E_deleteSelConditions
    }
})();