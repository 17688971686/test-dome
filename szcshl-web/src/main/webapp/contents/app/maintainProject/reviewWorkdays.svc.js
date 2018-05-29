(function(){
    'use strict';
    angular.module('app').factory("reviewWorkdaysSvc" , reviewWorkdays);
    reviewWorkdays.$inject = ['$http'];

    function reviewWorkdays($http){

        var service = {
            initReviewWorkDays : initReviewWorkDays ,//初始化评审工作日维护
            saveReview : saveReview , //保存维护的信息
            countWeekDays : countWeekDays , //计算剩余工作日
        };

        return service ;

        function countWeekDays(oldSignDate , signDate , callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/signView/admin/countWeekDays",
                params : {oldSignDate : oldSignDate , signDate : signDate}
            }

            var httpSuccess = function success(response){

                if(callBack != undefined && typeof  callBack == 'function'){
                    return callBack(response.data);
                }
            }
            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }

        function initReviewWorkDays(vm , callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/maintainProject/initReviewDays",
                params : {signId : vm.signId}
            }

            var httpSuccess = function success(response){

                if(callBack != undefined && typeof  callBack == 'function'){
                    return callBack(response.data);
                }
            }
            common.http({
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }

        function saveReview(vm , callBack){
            var httpOptions  = {
                method : 'put',
                url : rootPath + "/maintainProject/saveReview",
                data : vm.sign
            }

            var httpSuccess = function(response){
                if(callBack != undefined && typeof callBack == 'function'){
                    callBack(response.data);
                }
            }
            common.http({
                $http : $http ,
                httpOptions : httpOptions,
                success : httpSuccess
            });
        }

    }
})();