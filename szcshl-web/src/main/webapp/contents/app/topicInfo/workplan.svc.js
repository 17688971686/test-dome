(function () {
    'use strict';

    angular.module('app').factory('workPlanSvc', workPlan);

    workPlan.$inject = ['$http','bsWin'];

    function workPlan($http,bsWin) {
        var service = {
            findByTopicId: findByTopicId,                   //根据他课题研究ID查询工作计划
            save    :   save,                               //保存工作方案
        };

        return service;

        //S_根据他课题研究ID查询工作计划
        function findByTopicId(topicId,callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/workPlan/initByTopicId",
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
        function save(workPlan,callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/workPlan",
                data:workPlan
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