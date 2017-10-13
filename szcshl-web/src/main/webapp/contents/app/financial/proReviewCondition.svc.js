(function () {
    'use strict';

    angular.module('app').factory('proReviewConditionSvc', proReviewCondition);

    proReviewCondition.$inject = ['$http'];

    function proReviewCondition($http) {
        var service = {
            proReviewConCount:proReviewConCount                  //项目评审情况统计
        };
        return service;

        //项目评审情况统计
        function proReviewConCount(vm,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertSelected/proReviewConditionCount",
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
        }//E_项目评审情况统计

    }
})();