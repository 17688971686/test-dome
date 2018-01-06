(function () {
    'use strict';

    angular.module('app').controller('fileRecordEditCtrl', fileRecord);

    fileRecord.$inject = ['fileRecordSvc','$state','sysfileSvc', 'bsWin','$scope' , 'templatePrintSvc' , 'addRegisterFileSvc'];

    function fileRecord(fileRecordSvc,$state,sysfileSvc,bsWin,$scope , templatePrintSvc , addRegisterFileSvc) {
        var vm = this;
        vm.title = '项目归档编辑';

        vm.fileRecord = {};
        vm.fileRecord.signId = $state.params.signid;
        vm.fileRecord.fileRecordId = "";
        vm.signId = $state.params.signid;
        //是否协审归档（默认不是）
        vm.isassistproc = false;
        vm.drawingFile = [];//图纸资料
        vm.otherFile = [];//其它资料
        vm.declareFile = [];//建议书申报资料
        //初始化附件上传控件
        vm.initFileUpload = function () {
            if (!vm.fileRecord.fileRecordId) {
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.fileRecord.fileRecordId", function (newValue, oldValue) {
                    if (newValue && newValue != oldValue && !vm.initUploadOptionSuccess) {
                        vm.initFileUpload();
                    }
                });
            }
            vm.sysFile = {
                businessId: vm.fileRecord.fileRecordId,
                mainId: vm.signId,
                mainType: sysfileSvc.mainTypeValue().SIGN,
                sysfileType: sysfileSvc.mainTypeValue().DOFILE,
                sysBusiType: sysfileSvc.mainTypeValue().DOFILE,
            };
            sysfileSvc.initUploadOptions({
                inputId: "sysfileinput",
                vm: vm
            });
        }

        activate();
        function activate() {
            fileRecordSvc.initFileRecordData(vm);

        }

        vm.create = function () {
            fileRecordSvc.saveFileRecord(vm,function(data){
                vm.isCommit = false;
                if(data.flag || data.reCode == 'ok'){
                    vm.fileRecord = data.reObj;
                    vm.fileRecord.signId = vm.signId;
                    bsWin.success("操作成功！");
                }else{
                    bsWin.error(response.data.reMsg);
                }
            });
        }

        /**
         * 打印功能 -分页
         */
        vm.templatePage = function (id) {
            templatePrintSvc.templatePage(id);
        }

        /******以下是其它资料添加*****/

        vm.addOtherFile = function (businessId, businessType) {
            if(businessType == "2"){
                vm.addRegisters = vm.drawingFile;
            }else{
                vm.addRegisters = vm.otherFile;
            }
            /*if(businessType == "XMJYS_DECLARE_FILE"){
                vm.addRegisters = vm.declareFile;
            }*/
            if(!vm.addRegisters){
                vm.addRegisters = [];
            }
            if (!businessId) {
                bsWin.alert("请先保存数据！");
            } else {
                vm.businessId = businessId;
                vm.businessType = businessType;

                $("#addOtherFile").kendoWindow({
                    width: "50%",
                    height: "600px",
                    title: "其它资料编辑页",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            }

        }

        //新建其它资料
        vm.addRegisterFile = function () {
            vm.addRegister = {};
            vm.addRegister.businessId = vm.businessId;
            vm.addRegister.id = common.uuid();
            vm.addRegisters.push(vm.addRegister);
        }

        //保存其它资料
        vm.saveRegisterFile = function () {
            addRegisterFileSvc.saveRegisterFile(vm.addRegisters, function (data) {
                if (data.flag || data.reCode == 'ok') {
                    bsWin.alert("操作成功");
                    vm.addRegisters = data.reObj;
                } else {
                    bsWin.alert(data.reMsg);
                }
            });
        }
        //删除其它资料
        vm.deleteRegisterFile = function () {
            var isCheked = $("#addOtherFile input[name='addRegistersCheck']:checked")
            if (isCheked.length < 1) {
                bsWin.alert("请选择要删除的记录！");
            } else {
                var ids = [];
                for (var i = 0; i < isCheked.length; i++) {
                    vm.addRegisters.forEach(function (f, number) {
                        if (f.id && isCheked[i].value == f.id) {
                            ids.push(isCheked[i].value);
                            vm.addRegisters.splice(number, 1);
                        }
                    });
                }
                if (ids.length > 0) {
                    addRegisterFileSvc.deleteByIds(ids.join(","), function (data) {
                        bsWin.alert("删除成功！");
                    });
                }
            }
        }

    }
})();
