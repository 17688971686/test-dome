(function(){
    'use strict';
    angular.module('app').factory("takeUserSvc" ,takeUser);

    takeUser.$inject=['$http'];
    function takeUser($http){

        var service={
            initAllUser : initAllUser ,//初始化所有用户
            saveTakeUser : saveTakeUser ,//保存代办人
            getUser : getUser ,//通过id查询个人信息
            cancelTakeUser : cancelTakeUser ,//取消代办人

        }
        return service;

        //begin initAllUser
        function  initAllUser(vm){
            var httpOptions={
                method : "get",
                url : rootPath + "/user/getAllUserDisplayName",
            }

            var httpSuccess = function(response){
                vm.users=response.data;
            }

            common.http({
                vm : vm ,
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess
            });

        }
        //end initAllUser

        //begin saveTakeUser
        function saveTakeUser(takeUserId,callBack){
            var httpOptions = {
                method: "post",
                url: rootPath + "/user/saveTakeUser",
                params: {
                    takeUserId:takeUserId
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //end saveTakeUser

        //begin getUser
        function getUser(callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/user/findCurrentUser",
            }
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
        }//end getUser

        //begin cancelTakeUser
        function cancelTakeUser(vm){
            var httpOptions={
                method : "get",
                url : rootPath + "/user/cancelTakeUser"
            }

            var httpSuccess = function success(response){
                vm.takeUser = "";
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //end cancelTakeUser
    }
})();