(function(){
    'ust strict';
    angular.module('app').factory('policySvc' , policy);
    policy.$inject = ['$http'];
    function policy($http){
        var service = {
            initFileFolder : initFileFolder , //初始化政策指标库所有的文件夹
            createPolicy : createPolicy, //创建政策指标库
            findFileByIdGrid : findFileByIdGrid , // 通过id查询文件
            deletePolicy : deletePolicy , //删除政策指标库
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
        //end createPolicy

        //begin findFileByIdGrid
        function findFileByIdGrid( vm , callBack) {
            $http({
                method: 'post',
                url: rootPath + "/policy/findFileById",
                params: {
                    fileId: vm.standardId,
                    skip: vm.price.skip,
                    size: vm.price.size,
                },
            }).then(function (r) {
                if (typeof callBack == 'function') {
                    callBack(r.data);
                }
            });
        }
        //end findFileByIdGrid

        //begin deletePolicy
        function deletePolicy(idStr , callBack){
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/policy/deletePolicy",
                params : {idStr : idStr}
            };
            console.log(idStr);
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
        //end deletePolicy

    }
})();