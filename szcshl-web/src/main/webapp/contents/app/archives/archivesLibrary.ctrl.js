(function () {
    'use strict';

    angular.module('app').controller('archivesLibraryCtrl', archivesLibrary);

    archivesLibrary.$inject = ['archivesLibrarySvc', 'bsWin', '$state'];

    function archivesLibrary(archivesLibrarySvc, bsWin, $state) {
        var vm = this;
        vm.model = {};
        vm.title = '项目档案借阅管理';
        vm.id = $state.params.id;

        activate();
        function activate() {
            if ($state.params.id) {
                archivesLibrarySvc.initArchivesLibrary($state.params.id, function (data) {
                    vm.model = data;
                });
            }
        }

        //保存中心档案借阅
        vm.createLibrary = function () {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                archivesLibrarySvc.createArchivesLibrary(vm.model, function (data) {
                    if (data.flag || data.reCode == "ok") {
                        vm.model = data.reObj;
                        bsWin.alert("操作成功！");
                    } else {
                        bsWin.error(data.reMsg);
                    }
                });
            }
        };

        vm.startFlow = function () {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                archivesLibrarySvc.createArchivesLibrary(vm.model, function (data) {
                    if (data.flag || data.reCode == "ok") {
                        vm.model = data.reObj;
                        archivesLibrarySvc.startFlow(vm.model.id, function (data) {
                            if (data.flag || data.reCode == "ok") {
                                bsWin.alert("操作成功！", function () {
                                    $state.go("archivesLibraryList");
                                });
                            } else {
                                bsWin.error(data.reMsg);
                            }
                        })
                    } else {
                        bsWin.error(data.reMsg);
                    }
                });
            }
        }

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    archivesLibrarySvc.deleteArchivesLibrary(vm, id);
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
