(function () {
    'use strict';

    angular.module('app').factory('signworkSvc', signwork);

    signwork.$inject = ['$http', '$state','bsWin'];

    function signwork($http, $state,bsWin) {
        var service = {
            signWorkList: signWorkList,
        };
        return service;

        function signWorkList(vm,callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signwork/fingSignWorkList",
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

    }
})();