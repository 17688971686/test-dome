(function () {
    'dispatch strict';

    angular.module('app').factory('dispatchSvc', dispatch);

    dispatch.$inject = ['sysfileSvc', '$http','$state'];

    function dispatch(sysfileSvc,$http,$state) {
        var service = {
            initDispatchData: initDispatchData, // 初始化流程数据
            saveDispatch: saveDispatch,         // 保存
            gotoMergePage: gotoMergePage,       // 打开合并发文页面
            chooseProject: chooseProject,       // 选择合并发文项目
            getSignForMerge: getSignForMerge,   // 显示待选项目
            cancelProject: cancelProject,       // 取消选择
            getSeleSignBysId: getSeleSignBysId,
            deleteAllMerge: deleteAllMerge,      // 删除所有关联信息
        };
        return service;


        // begin#gotoWPage
        function gotoMergePage(vm) {
            //查询待选择的项目信息
            getSignForMerge(vm);
            //查询已选择的项目信息
            getSeleSignBysId(vm);

            $("#mwindow").kendoWindow({
                width: "1200px",
                height: "630px",
                title: "合并发文",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();


        }

        // end#gotoWPage

        // S_初始化
        function initDispatchData(vm) {
            vm.isCommit = true;
            vm.busiFlag.signleToMerge = false;
            var httpOptions = {
                method: 'get',
                url: rootPath + "/dispatch/initData",
                params: {
                    signid: vm.dispatchDoc.signId
                }
            }

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.isCommit = false;
                        var data = response.data;
                        vm.sign = data.sign;
                        vm.dispatchDoc = data.dispatch;     //可编辑的发文对象
                        vm.dispatchDoc.signId = $state.params.signid;
                        if(vm.dispatchDoc.dispatchWay && vm.dispatchDoc.dispatchWay == 2){
                            vm.busiFlag.isMerge = true;     //合并发文
                        }
                        if(vm.dispatchDoc.isMainProject && vm.dispatchDoc.isMainProject == 9){
                            vm.busiFlag.isMain = true;     //主项目
                        }
                        vm.associateDispatchs = data.associateDispatchs;
                        vm.proofread = data.mainUserList;   //校对人

                        //初始化附件上传
                        if (vm.dispatchDoc.id) {
                            vm.showFlag.buttSysFile = true;
                            sysfileSvc.initUploadOptions({
                                businessId: vm.dispatchDoc.id,
                                sysSignId: vm.dispatchDoc.signId,
                                sysfileType: "发文",
                                uploadBt: "upload_file_bt",
                                detailBt: "detail_file_bt",
                                vm: vm
                            });
                        }
                    }
                })
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
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.isCommit = false;
                        common.alert({
                            vm: vm,
                            msg: response.data.reMsg,
                            closeDialog: true,
                            fn: function () {
                                if (response.data.reCode == "error") {
                                    vm.isCommit = false;
                                } else {
                                    if(!vm.dispatchDoc.id){
                                        vm.dispatchDoc.id = response.data.reObj.id;
                                        //初始化附件上传
                                        vm.showFlag.buttSysFile = true;
                                        sysfileSvc.initUploadOptions({
                                            businessId: vm.dispatchDoc.id,
                                            sysSignId: vm.dispatchDoc.signId,
                                            sysfileType: "发文",
                                            uploadBt: "upload_file_bt",
                                            detailBt: "detail_file_bt",
                                            vm: vm
                                        });
                                    }
                                }
                            }
                        })
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError:function () {
                    vm.isCommit = false;
                }
            });

            //}
        }// E_保存

        // begin##chooseProject
        function chooseProject(vm) {
            var linkSignId = $("input[name='checksign']:checked");
            if (linkSignId.length > 0) {
                var ids = [];
                $.each(linkSignId, function (i, obj) {
                    ids.push(obj.value);
                });
                var httpOptions = {
                    method: 'get',
                    url: rootPath + "/dispatch/mergeDispa",
                    params: {
                        mainBusinessId: vm.dispatchDoc.id,
                        signId: vm.dispatchDoc.signId,
                        linkSignId: ids.join(",")
                    }
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功！",
                                closeDialog: true
                            });
                            //查询待选择的项目信息
                            getSignForMerge(vm);
                            //查询已选择的项目信息
                            getSeleSignBysId(vm);
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
        }

        // end##chooseProject

        // begin##chooseProject
        function cancelProject(vm) {
            var linkSignId = $("input[name='checkss']:checked");
            if (linkSignId.length > 0) {
                var ids = [];
                $.each(linkSignId, function (i, obj) {
                    ids.push(obj.value);
                });
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/dispatch/deleteMerge",
                    params: {
                        mainBusinessId: vm.dispatchDoc.id,
                        removeSignIds: ids.join(",")
                    }
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功！",
                                closeDialog: true
                            });
                            //查询待选择的项目信息
                            getSignForMerge(vm);
                            //查询已选择的项目信息
                            getSeleSignBysId(vm);
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
        }

        // end##chooseProject

        // begin##getSeleSignBysId
        function getSeleSignBysId(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/dispatch/getSignByBusinessId",
                params: {
                    mainBussnessId: vm.dispatchDoc.id
                }
            }
            var httpSuccess = function success(response) {
                vm.selectedSign = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // end##getSeleSignBysId

        // begin##getSignForMerge
        function getSignForMerge(vm) {
            vm.serarchSign.signid = vm.dispatchDoc.signId;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/dispatch/getSignForMerge",
                data: vm.serarchSign,
                params: {
                    dispatchId: vm.dispatchDoc.id
                }
            }
            var httpSuccess = function success(response) {
                vm.signs = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                }
            });
        }// end##getSignForMerge

        // begin##deletemerge
        function deleteAllMerge(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/dispatch/deleteMerge",
                params: {
                    mainBusinessId: vm.dispatchDoc.id,
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: "操作成功！",
                            closeDialog: true
                        });
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                }
            });
        }// end##deletemerge

    }
})();