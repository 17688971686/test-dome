(function () {
    'use strict';

    angular.module('app').controller('annountmentEditCtrl', annountmentEdit);

    annountmentEdit.$inject = ['$state', 'annountmentSvc', 'sysfileSvc', '$scope', 'bsWin'];

    function annountmentEdit($state, annountmentSvc, sysfileSvc, $scope, bsWin) {
        var vm = this;
        vm.title = "通知公告编辑";
        vm.annountment = {};        //通知公告对象
        vm.annountment.anId = $state.params.id;
        vm.editor = undefined;
        vm.businessFlag = {
            isInitFileOption: false,   //是否已经初始化附件上传控件
        }
        //初始化附件上传控件
        vm.initFileUpload = function () {
            if (!vm.annountment.anId) {
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.annountment.anId", function (newValue, oldValue) {
                    if (newValue && newValue != oldValue && !vm.initUploadOptionSuccess) {
                        vm.initFileUpload();
                    }
                });
            }

            //创建附件对象
            vm.sysFile = {
                businessId: vm.annountment.anId,
                mainId: vm.annountment.anId,
                mainType: "通知公告",
                sysBusiType:"通知公告",
                showBusiType:false,
            };
            sysfileSvc.initUploadOptions({
                inputId: "sysfileinput",
                vm: vm,
                uploadSuccess: function () {
                    sysfileSvc.findByBusinessId(vm.annountment.anId, function (data) {
                        vm.sysFilelists = data;
                    });
                }
            });
        }
        active();
        function active() {
            vm.isNeed = "1";//是否需要审批，默认不需要
            /*  UE.delEditor("editor");//可能是缓存问题导致的，因此先删除缓存中已有的富文本，
             vm.editor= UE.getEditor("editor");//再重新渲染*/
            //渲染百度Ueditor的编辑器
            vm.editor = new UE.ui.Editor();
            vm.editor.render('editor');
            if (vm.annountment.anId) {
                vm.isUpdate = true;
                annountmentSvc.findAnnountmentById(vm, function (data) {
                    vm.annountment = data;
                    vm.initFileUpload();
                    vm.editor.ready(function () {//在初始化后，填充编辑器的值
                        if (vm.annountment.anContent != undefined) {
                            vm.editor.setContent(vm.annountment.anContent);
                        }
                    })
                });
                sysfileSvc.findByBusinessId(vm.annountment.anId, function (data) {
                    vm.sysFilelists = data;
                });
            } else {
                vm.initFileUpload();
            }
        }


        //新增通知公告
        vm.create = function () {
            annountmentSvc.createAnnountment(vm, function (data) {
                vm.isSubmit = false;
                vm.annountment.anId = data.anId;
                bsWin.alert("保存成功！");
            });
        }

        //编辑通知公告
        vm.update = function () {
            annountmentSvc.updateAnnountment(vm, function (data) {
                vm.isSubmit = false;
                bsWin.alert("操作成功！");
            });
        }

        //************************** S 以下是新流程处理js **************************//
        vm.startNewFlow = function () {
            bsWin.confirm({
                title: "询问提示",
                message: "确认已经完成填写，并且发起流程么？",
                onOk: function () {
                    //当是更新提交时，先更新在提交
                    if (vm.annountment.anId) {
                        annountmentSvc.updateAnnountment(vm, function (data) {//提交时先更新在提交
                            annountmentSvc.startFlow(vm.annountment.anId, function (data) {//更新的提交
                                if (data.flag || data.reCode == 'ok') {
                                    bsWin.success("操作成功！", function () {
                                        $state.go('annountment');
                                    });
                                } else {
                                    bsWin.error(data.reMsg);
                                }
                            });
                        });
                    } else {
                        //当是保存时提交就先保存
                        annountmentSvc.createAnnountment(vm, function (data) {
                            vm.id = data.anId;                            //保存后取得id,流程发起需要
                            annountmentSvc.startFlow(vm.id, function (data) {//更新的提交
                                if (data.flag || data.reCode == 'ok') {
                                    bsWin.success("操作成功！", function () {
                                        $state.go('annountment');
                                    });
                                } else {
                                    bsWin.error(data.reMsg);
                                }
                            });
                        });
                    }
                }
            });
        }

    }
})();