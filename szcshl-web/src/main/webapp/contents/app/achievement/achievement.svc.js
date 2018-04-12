(function () {
    'use strict';

    angular.module('app').factory('achievementSvc', achievement);

    achievement.$inject = ['$http'];

    function achievement($http) {
        var service = {
            achievementSum: achievementSum,
        };
        return service;

        //S_业绩汇总
        function achievementSum(vm,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/getAchievementSum",
                data: vm.model
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
        }//E_专家评审费用统计

    }
})();