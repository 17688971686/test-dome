(function () {
    'use strict';

    angular.module('app').factory('topicSvc', topicService);

    topicService.$inject = ['$http','bsWin'];

    function topicService($http,bsWin) {
        var service = {
            findOrgUser: findOrgUser,                   //查询当前用户所在部门的所有用户信息
            createTopic : createTopic,                  //创建课题研究信息
            startFlow : startFlow,                      //发起课题研究流程
        };

        return service;

        //S_查询当前用户所在部门的所有用户信息
        function findOrgUser(callBack){
            var httpOptions = {
                method: 'get',
                url: rootPath + "/user/findChargeUsers",
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
        }//E_findOrgUser

        //S_创建课题研究信息
        function createTopic(topicModel,isCommit,callBack){
            isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/topicInfo",
                data : topicModel
            };
            var httpSuccess = function success(response) {
                isCommit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError : function(){isCommit = false;}
            });
        }//E_createTopic

        //S_启动流程
        function startFlow(topicModel,isCommit,callBack){
            isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/topicInfo/startFlow",
                data : topicModel
            };
            var httpSuccess = function success(response) {
                isCommit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError : function(){isCommit = false;}
            });
        }//E_startFlow
    }
})();