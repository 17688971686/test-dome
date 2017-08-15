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
        function saveTakeUser(vm){
            common.initJqValidation();
            var isValid = $('#form').valid();
            if (isValid) {
                var httpOptions = {
                    method: "post",
                    url: rootPath + "/user/saveTakeUser",
                    params: {takeUserId: vm.takeUserId}
                }
                var httpSuccess = function (response) {
                    vm.chooseTakeUser = false;
                    common.confirm({
                        vm: vm,
                        title: "",
                        msg: "保存成功！",
                        fn: function () {
                            $('.confirmDialog').modal('hide');
                            window.parent.$("#chooseTakeUserWindow").data("kendoWindow").close();
                            getUser(vm);
                        }

                    });
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }
        //end saveTakeUser

        //begin getUser
        function getUser(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/user/getUserByLoginName",
            }
            var httpSuccess = function success(response) {
                vm.takeUser = response.data.displayName;
            }

            common.http({
                vm: vm,
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
                getUser(vm);
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