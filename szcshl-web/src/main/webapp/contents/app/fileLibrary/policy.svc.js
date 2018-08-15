(function(){
    'ust strict';
    angular.module('app').factory('policySvc' , policy);
    policy.$inject = ['$http'];
    function policy($http){
        var service = {
            initFileFolder : initFileFolder , //初始化政策指标库所有的文件夹
            createPolicy : createPolicy, //创建政策指标库
            findFileById : findFileById , // 通过id查询文件
        }

        return service;

        //begin initFileFolder
        function initFileFolder($scope,callBack){

            var httpOptions = {
                method: 'post',
                url: rootPath + "/policy/initFileFolder",
            };
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError : function(){}
            });
        }
        //end initFileFolder


        //S_创建政策指标库
        function createPolicy(topicModel,callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/policy",
                data : topicModel
            };
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError : function(){}
            });
        }

        //begin findFileById
        function findFileById( fileId , callBack) {
            var httpOptions = {
                method: "post",
                url: rootPath + "/fileLibrary/findFileById",
                params: {fileId: fileId}
            }
            var httpSuccess = function success(response) {
                if( callBack != undefined && typeof  callBack == 'function'){
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