(function () {
    'use strict';

    angular.module('app').factory('signworkSvc', signwork);

    signwork.$inject = ['$http', '$state','bsWin'];

    function signwork($http, $state,bsWin) {
        var service = {
            signWorkList: signWorkList,
        };
        return service;

        function signWorkList(callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signwork/fingSignWorkList",
                headers:{ 'Content-Type': 'application/x-www-form-urlencoded'},
                data: {$filter:common.buildOdataFilter($("#signworkform"))},
                transformRequest: function(obj) {
                    var str = [];
                    for(var p in obj){
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    }
                    return str.join("&");
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

    }
})();