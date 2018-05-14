/**
 * Created by Administrator on 2018/5/8 0008.
 */
(function() {
    'use strict';

    angular.module('app').factory('msgSvc', msgSvcFc);

    msgSvcFc.$inject = ['$rootScope', '$http'];

    function msgSvcFc($rootScope, $http) {

        return {
            initGrid : initGrid,                        //短信发送表格
            initOrgAndUser : initOrgAndUser,            //初始化部门和用户列表
            saveMsgInfo : saveMsgInfo,                  //保存用户信息
        };

        function initGrid(){

        }

        /**
         * 初始化部门和用户信息
         * @param callBack
         */
        function initOrgAndUser(callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sharingPlatlform/initOrgAndUser",
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
        }

        /**
         * 保存用户信息
         * @param vm
         * @param callBack
         */
        function saveMsgInfo(vm,callBack){

        }

    }
})();
