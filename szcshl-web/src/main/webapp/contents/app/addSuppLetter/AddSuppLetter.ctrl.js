(function () {
    'use strict';

    angular.module('app').controller('addSuppLetterCtrl', addSuppLetter);
    addSuppLetter.$inject = ['$location', 'addSuppLetterSvc', '$state', 'bsWin'];

    function addSuppLetter($location, addSuppLetterSvc, $state, bsWin) {
        var vm = this;
        vm.suppletter = {}; //补充资料对象$state
        vm.suppletter.businessId = $state.params.businessId;        //业务ID
        vm.suppletter.businessType = $state.params.businessType;    //业务类型
        vm.title = '登记补充资料';

        activate();
        function activate() {
            addSuppLetterSvc.initSuppLetter(vm.suppletter.businessId,vm.suppletter.businessType,function(data){
                vm.suppletter = data;
            });
        }

        //保存补充资料函
        vm.saveAddSuppletter = function () {
            bsWin.confirm({
                title: "询问提示",
                message: "确认为最终保存数据吗？",
                onOk: function () {
                    common.initJqValidation($('#suppletter_form'));
                    var isValid = $('#suppletter_form').valid();
                    if (isValid) {
                        addSuppLetterSvc.createAddSuppLetter(vm.suppletter,vm.isSubmit,function(data){
                            vm.isSubmit = false;
                            bsWin.alert("保存成功！");
                        })
                    }
                }
            });
        }


        //拟补充资料函正文
        vm.addSuppContent = function () {
            vm.showsupp = true;
            var ideaEditWindow = $("#addsuppContent");
            ideaEditWindow.kendoWindow({
                width: "50%",
                height: "80%",
                title: "拟补资料函正文",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "close"]
            }).data("kendoWindow").center().open();
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
