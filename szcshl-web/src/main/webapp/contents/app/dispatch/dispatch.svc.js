(function () {
    'dispatch strict';

    angular.module('app').factory('dispatchSvc', dispatch);

    dispatch.$inject = ['sysfileSvc', '$http','$state','bsWin'];

    function dispatch(sysfileSvc,$http,$state,bsWin) {
        var service = {
            initDispatchData: initDispatchData, // 初始化流程数据
            saveDispatch: saveDispatch,         // 保存
            initMergeInfo: initMergeInfo,       // 打开合并发文页面
            unMergeDISSign: unMergeDISSign,     // 显示待选项目
            getMergeDISSign: getMergeDISSign,   // 已选合并发文项目
            chooseProject: chooseProject,       // 选择合并发文项目
            cancelProject: cancelProject,       // 取消选择
            deleteAllMerge : deleteAllMerge,    // 取消所有的合并项目
        };
        return service;

        // begin#gotoWPage
        function initMergeInfo(vm,signId) {
            unMergeDISSign(signId,function (data) {
                vm.unMergeSign = [];
                vm.unMergeSign = data;
            });//待选
            getMergeDISSign(signId,function (data) {
                vm.mergeSign = [];
                vm.mergeSign = data;
            });//初始化已选项目
        }

        // end#gotoWPage

        // S_初始化
        function initDispatchData(vm) {
            vm.busiFlag.signleToMerge = false;
            var httpOptions = {
                method: 'get',
                url: rootPath + "/dispatch/initData",
                params: {
                    signid: vm.dispatchDoc.signId
                }
            }

            var httpSuccess = function success(response) {
                var data = response.data;
                vm.sign = data.sign;
                vm.dispatchDoc = data.dispatch;     //可编辑的发文对象

                vm.dispatchDoc.authorizeValue =0;//默认审定金额为0

                vm.dispatchDoc.signId = $state.params.signid;
                if(vm.dispatchDoc.dispatchWay && vm.dispatchDoc.dispatchWay == 2){
                    vm.busiFlag.isMerge = true;     //合并发文
                }
                if(vm.dispatchDoc.isMainProject && vm.dispatchDoc.isMainProject == 9){
                    vm.busiFlag.isMain = true;     //主项目
                }
                vm.associateDispatchs = data.associateDispatchs;
                vm.proofread = data.mainUserList;   //校对人

                //合并发文次项目
                if(vm.dispatchDoc.dispatchWay == 2 && vm.dispatchDoc.isMainProject==0){
                    vm.showFlag.isReveiwDS = true;
                }

                //初始化附件上传
                vm.initFileUpload();

                //监听是否关联选项
                vm.watchIsRelated();
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }// E_初始化

        // S_保存
        function saveDispatch(vm) {
            vm.isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/dispatch",
                data: vm.dispatchDoc
            }
            var httpSuccess = function success(response) {
                vm.isCommit = false;
                if (response.data.flag || response.data.reCode == "ok") {
                    if(vm.dispatchDoc.dispatchWay && vm.dispatchDoc.dispatchWay == 2){
                        vm.busiFlag.isMerge = true;     //合并发文
                    }
                    if(vm.dispatchDoc.isMainProject && vm.dispatchDoc.isMainProject == 9){
                        vm.busiFlag.isMain = true;     //主项目
                    }

                    if(!vm.dispatchDoc.id){
                        vm.dispatchDoc.id = response.data.reObj.id;
                    }
                }
                bsWin.alert(response.data.reMsg);
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError:function () {
                    vm.isCommit = false;
                }
            });
        }// E_保存

        // begin##chooseProject
        function chooseProject(signId,mergeIds,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/mergeSign",
                params: {
                    signId:signId ,
                    mergeIds: mergeIds,
                    mergeType:"2"
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

        // end##chooseProject

        // begin##chooseProject
        function cancelProject(signId,cancelIds,callBack) {
            var paramObj = {
                signId: signId,
                mergeType:"2"
            }
            if(cancelIds){
                paramObj.cancelIds = cancelIds;
            }
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/cancelMergeSign",
                params: paramObj
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
        // end##chooseProject

        //S_删除所有合并评审工作方案
        function deleteAllMerge(signId,callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/deleteAllMerge",
                params: {
                    signId: signId,
                    mergeType:"2"
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
        }//E_deleteAllMerge

        // begin##getSeleSignBysId
        function getMergeDISSign(signId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/getMergeDISSign",
                params: {
                    signId: signId
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
        // end##getSeleSignBysId

        // begin##getSignForMerge
        function unMergeDISSign(signId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/signView/unMergeDISSign",
                params: {
                    signId: signId
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
                success: httpSuccess,
            });
        }// end##getSignForMerge

    }
})();