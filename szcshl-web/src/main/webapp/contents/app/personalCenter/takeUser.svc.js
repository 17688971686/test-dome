(function(){
    'use strict';
    angular.module('app').factory("takeUserSvc" ,takeUser);

    takeUser.$inject=['$http'];
    function takeUser($http){

        var service={
            initAllUser : initAllUser ,//初始化所有用户
            saveTakeUser : saveTakeUser ,//保存代办人
            getUser : getUser ,//通过id查询个人信息
            cancelTakeUser : cancelTakeUser ,//取消代办人
            initZtreeClient:initZtreeClient,
            updateZtree : updateZtree,

        }
        return service;

        // begin#initZtreeClient
        function initZtreeClient(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/role/findAllRoles"
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        var zTreeObj;
                        var setting = {
                            check: {
                                chkboxType: {
                                    "Y": "ps",
                                    "N": "ps"
                                },
                                enable: true
                            }
                        };
                        var zNodes = $linq(response.data).select(
                            function (x) {
                                return {
                                    id: x.id,
                                    name: x.roleName
                                };
                            }).toArray();
                        var rootNode = {
                            id: '',
                            name: '角色集合',
                            children: zNodes
                        };
                        zTreeObj = $.fn.zTree.init($("#zTree"), setting, rootNode);
                        updateZtree(vm);
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //begin initAllUser
        function  initAllUser(callBack){
            var httpOptions={
                method : "post",
                url : rootPath + "/user/getAllUserDisplayName",
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess
            });
        }
        //end initAllUser

        //begin saveTakeUser
        function saveTakeUser(takeUserId,callBack){
            var httpOptions = {
                method: "post",
                url: rootPath + "/user/saveTakeUser",
                params: {
                    takeUserId:takeUserId
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
        //end saveTakeUser

        //begin getUser
        function getUser(callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/user/findCurrentUser",
            }
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
        }//end getUser

        //begin cancelTakeUser
        function cancelTakeUser(callBack){
            var httpOptions={
                method : "get",
                url : rootPath + "/user/cancelTakeUser"
            }

            var httpSuccess = function success(response){
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
        //end cancelTakeUser

        function updateZtree(vm) {
            var treeObj = $.fn.zTree.getZTreeObj("zTree");
            var checkedNodes = $linq(vm.model.roleDtoList).select(function (x) {
                return x.roleName;
            }).toArray();
            var allNodes = treeObj.getNodesByParam("level", 1, null);

            var nodes = $linq(allNodes).where(function (x) {
                return $linq(checkedNodes).contains(x.name);
            }).toArray();

            for (var i = 0, l = nodes.length; i < l; i++) {
                treeObj.checkNode(nodes[i], true, true);
            }
        }
    }
})();