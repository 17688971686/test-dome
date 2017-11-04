(function () {
    'use strict';

    angular.module('app').controller('addSuppLetterCtrl', addSuppLetter);
    addSuppLetter.$inject = ['$location', 'addSuppLetterSvc', 'sysfileSvc', '$state', 'bsWin', '$scope'];

    function addSuppLetter($location, addSuppLetterSvc, sysfileSvc, $state, bsWin, $scope) {
        var vm = this;
        vm.suppletter = {}; //补充资料对象$state
        vm.suppletter.businessId = $state.params.businessId;        //业务ID
        vm.suppletter.businessType = $state.params.businessType;    //业务类型
        vm.suppletter.id = $state.params.id;

        vm.title = '拟补充资料函';
        vm.businessFlag = {
            isInitFileOption: false,   //是否已经初始化附件上传控件
        }

        activate();
        function activate() {
            addSuppLetterSvc.initSuppLetter(vm.suppletter.businessId, vm.suppletter.businessType, function (data) {
                vm.suppletter = data;
                vm.initFileUpload();
            });
        }

        //初始化附件上传控件
        vm.initFileUpload = function () {
            if (!vm.suppletter.id) {
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.suppletter.id", function (newValue, oldValue) {
                    if (newValue && newValue != oldValue && !vm.initUploadOptionSuccess) {
                        vm.initFileUpload();
                    }
                });
            }
            vm.sysFile = {
                businessId: vm.suppletter.id,
                mainId: vm.suppletter.id,
                mainType: sysfileSvc.mainTypeValue().SIGN,
                sysfileType: sysfileSvc.mainTypeValue().AADSUPP_FILE,
                sysBusiType: sysfileSvc.mainTypeValue().AADSUPP_FILE,
            };
            sysfileSvc.initUploadOptions({
                inputId: "sysfileinput",
                vm: vm
            });
        }
        //保存补充资料函
        vm.saveAddSuppletter = function () {
            common.initJqValidation($("#suppletter_form"));
            var isValid = $("#suppletter_form").valid();
            if (isValid) {
                addSuppLetterSvc.saveSuppLetter(vm.suppletter,vm.isCommit,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        vm.suppletter = data.reObj;
                        bsWin.success("操作成功！");
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            } else {
                bsWin.alert("表格填写不正确，请检查相应的必填项信息！");
            }
        }

        /**
         * 发起流程
         */
        vm.startSignSupperFlow = function(){
            common.initJqValidation($("#suppletter_form"));
            var isValid = $("#suppletter_form").valid();
            if (isValid) {
                bsWin.confirm({
                    title: "询问提示",
                    message: "确认已经完成填写，并且发起流程么？",
                    onOk: function () {
                        addSuppLetterSvc.saveSuppLetter(vm.suppletter,vm.isCommit,function(data){
                            vm.isCommit = false;
                            if(data.flag || data.reCode == 'ok'){
                                vm.suppletter = data.reObj;
                                //保存成功，则发起流程
                                addSuppLetterSvc.startSignSupperFlow(vm.suppletter.id,function(data){
                                    if(data.flag || data.reCode == 'ok'){
                                        bsWin.success("操作成功！");

                                    }else{
                                        bsWin.error(data.reMsg);
                                    }
                                })
                            }else{
                                bsWin.error(data.reMsg);
                            }
                        });
                    }
               });
            } else {
                bsWin.alert("表格填写不正确，请检查相应的必填项信息！");
            }
        }

        //拟补充资料函查看附件
        vm.addSuppContent = function () {
            if (vm.suppletter.id) {
                vm.showsupp = true;
                var ideaEditWindow = $("#addsuppletter");
                ideaEditWindow.kendoWindow({
                    width: "60%",
                    height: "90%",
                    title: "拟补资料函正文",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "close"]
                }).data("kendoWindow").center().open();
                addSuppLetterSvc.findByBusinessId(vm);
            } else {
                bsWin.alert("请先保存业务数据");
            }
        }

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    addSuppLetterSvc.deleteAddSuppLetter(vm, id);
                }
            });
        }
        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: '请选择数据'
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.del(idStr);
            }
        };

    }
})();
