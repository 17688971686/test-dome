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
        vm.isControl=$state.params.isControl;		//按钮显示
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
                    //重新加载页面
                    vm.fileRecord.fileRecordId = data.reObj;
                    bsWin.alert("修改成功！");
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
            if(!vm.addRegisters){
                vm.addRegisters = [];
            }
            if (!businessId) {
                bsWin.alert("请先保存数据！");
            } else {
                if(businessType == "2"){
                    vm.addRegisters = vm.drawingFile;
                    vm.showFilePage = true;
                    vm.showFileOther = false;
                    vm.showSignOther = false;
                }else{
                    vm.addRegisters = vm.otherFile;
                    vm.showFileOther = true;
                    vm.showFilePage = false;
                    vm.showSignOther = false;
                }
                vm.businessId = businessId;
                vm.businessType = businessType;

                $("#addOtherFile").kendoWindow({
                    width: "840px",
                    height: "480px",
                    title: "补充资料编辑",
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
                    vm.addRegisters = data.reObj;
                    bsWin.alert("操作成功");
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

        vm.updateFileCode = function(){
            $("#fileCodeWin").kendoWindow({
                width: "660px",
                height: "400px",
                title: "归档文号编辑",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Close"]
            }).data("kendoWindow").center().open();
        }

        vm.resetCancelBox = function (temp) {
            var cheboxObj = $("input[name='"+temp+"']:checked");
           if(cheboxObj.length==0){
               vm.fileRecord[temp] = '0';
            }
        }

        //S_初始化input框的值
        vm.initInputValue = function($event,defaultValue,temp){
            var checkbox = $event.target;
            var checked = checkbox.checked;
            if (checked) {
                if((!defaultValue)){
                    return 1;
                }else{
                    return defaultValue;
                }

            }else{
                vm.fileRecord[temp] = '0';
                return defaultValue;
            }
        }//E_initInputValue

        /**
         * 打印其它资料时，先手动勾选打印数据
         */
        vm.otherFileWindow = function(signId){
            vm.signId = signId;
            $("#otherFileWindow").kendoWindow({
                width: "660px",
                height: "400px",
                title: "归档资料",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Close"]
            }).data("kendoWindow").center().open();
            // printFile(vm.model.signid,'FILERECOED_OTHERFILE' , 'OTHER_FILE')
        }

        /**
         * 确认打印
         */
        vm.otherFilePrint = function(){
            var isCheck = $("#otherFileTable input[name='epConditionSort']:checked");
            if (isCheck.length == 0) {
                bsWin.alert("请选择数据");
            } else {
                window.parent.$("#otherFileWindow").data("kendoWindow").close();
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    ids.push(isCheck[i].value);
                }
                var idStr = ids.join(',');
                fileRecordSvc.otherFilePrint(vm.signId , idStr);
            }
        }
    }

})();
