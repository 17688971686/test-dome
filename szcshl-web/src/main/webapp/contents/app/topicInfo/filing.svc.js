(function () {
    'use strict';

    angular.module('app').factory('filingSvc', filingService);

    filingService.$inject = ['$http','bsWin'];

    function filingService($http,bsWin) {
        var service = {
            findByTopicId: findByTopicId,                   //根据他课题研究ID查询存档
            save    :   save,                               //保存存档
        };

        return service;

        //S_根据他课题研究ID查询工作计划
        function findByTopicId(topicId,callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/filing/initByTopicId",
                params:{
                    topicId:topicId
                }
            };
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
        }//E_findPlanByTopicId


        //S_保存工作方案
        function save(filing,callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/filing",
                data:filing
            };
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
        }//E_save
    }
})();