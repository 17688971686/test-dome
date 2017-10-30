(function(){
    'use strict';
    angular.module("app").factory('signChartSvc' , signChart);

    signChart.$inject = ['$http'];
    function signChart($http){
        var service = {
            findByTime : findByTime , //通过时间段获取项目信息
            pieData : pieData , //通过申报金额统计项目信息

        }
        return service;

        //begin findByTime
        function findByTime(vm, callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + '/signView/findByTime',
                params : {startTime : vm.startTime , endTime : vm.endTime}
            }

            var httpSuccess = function(response){
                if(callBack != undefined && typeof  callBack=='function'){
                    callBack(response.data);
                }
            }
            common.http({
                vm : vm,
                httpOptions : httpOptions ,
                $http : $http ,
                success : httpSuccess
            });
        }
        //end findByTime

        //begin pieData
        function pieData(vm , callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + '/signView/pieDate',
                params : {startTime : vm.startTime , endTime : vm.endTime}
            }

            var httpSuccess = function(response){
                if(callBack != undefined && typeof  callBack=='function'){
                    callBack(response.data);
                }
            }
            common.http({
                vm : vm,
                httpOptions : httpOptions ,
                $http : $http ,
                success : httpSuccess
            });
        }
        //end pieData
    }
})();