(function () {
    'use strict';

    angular.module('app').controller('addRegisterFileCtrl', addRegisterFile);

    addRegisterFile.$inject = ['bsWin', 'addRegisterFileSvc', '$state'];

    function addRegisterFile(bsWin, addRegisterFileSvc, $state) {
        var vm = this;
        vm.title = '登记补充资料';
        vm.sign = {};//收文对象
        vm.addRegisters = [];//登记补充材料集合
        vm.businessId = $state.params.businessId;

        //新建登记补充材料
        vm.addRegisterFile = function () {
            vm.addRegister = {};
            vm.addRegister.businessId = vm.businessId;
            vm.addRegister.id = common.uuid();
            vm.addRegisters.push(vm.addRegister);
        }

        //保存登记补充材料
        vm.saveRegisterFile = function () {
            addRegisterFileSvc.saveRegisterFile(vm.addRegisters,function(data){
                if(data.flag || data.reCode=='ok'){
                    bsWin.alert("操作成功");
                    vm.addRegisters = data.reObj;
                }else{
                    bsWin.alert(data.reMsg);
                }
            });
        }
        //删除登记补充资料
        vm.deleteRegisterFile = function () {
            var isCheked = $("#addRegistersTable input[name='addRegistersCheck']:checked")
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
                if(ids.length > 0){
                    addRegisterFileSvc.deleteByIds(ids.join(","), function (data) {
                        bsWin.alert("删除成功！");
                    });
                }
            }
        }

        //份数数字校验
        vm.inputIntegerValue = function (checkValue, idSort) {
            if (addRegisterFileSvc.isUnsignedInteger(checkValue)) {
                $("#errorsUnmber" + idSort).html("");
            } else {
                $("#errorsUnmber" + idSort).html("只能输入数字");
            }
        }

        activate();
        function activate() {
            if($state.params.businessId){
                addRegisterFileSvc.initAddRegisterFile($state.params.businessId,function(data){
                    if(data){
                        vm.addRegisters = data;
                    }
                });
            }

        }
    }
})();
