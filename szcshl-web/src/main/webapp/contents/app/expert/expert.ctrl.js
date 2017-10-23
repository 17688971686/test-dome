(function () {
    'expert strict';

    angular.module('app').controller('expertCtrl', expert);

    expert.$inject = ['$location', 'expertSvc'];

    function expert($location, expertSvc) {
        var vm = this;
        vm.data = {};
        vm.title = '专家列表';
        vm.expertId = "";
        activate();
        function activate() {
            expertSvc.grid(vm);
            expertSvc.reviewProjectGrid(vm);
        }

        vm.search = function () {
            expertSvc.searchMuti(vm);
        };

        vm.searchAudit = function () {
            expertSvc.searchMAudit(vm);
        };

        vm.formReset = function () {
            expertSvc.formReset(vm);
        }

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    expertSvc.deleteExpert(vm, id);
                }
            })
        };

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

        /**
         * 导出execl功能
         */
        vm.exportToExcel = function () {
            expertSvc.exportToExcel();
        }

        /**
         * 查看专家评审过的项目列表
         * @param expertId
         */
        vm.selReviewProject = function (expertId) {
            vm.expertId = expertId;
            vm.reviewProjectOptions.dataSource.read({"expertId" : expertId});
            $("#reviewProject").kendoWindow({
                width: "70%",
                height: "60%",
                title: "评审项目列表",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "close"]
            }).data("kendoWindow").center().open();

        }
    }
})();
