(function () {
    'expert strict';

    angular.module('app').controller('expertCtrl', expert);

    expert.$inject = ['$location', 'expertSvc', '$state'];

    function expert($location, expertSvc, $state) {
        var vm = this;
        vm.data = {};
        vm.title = '专家列表';
        vm.expertId = "";
        vm.headerType = "专家类型";
        vm.fileName = "专家信息";
        vm.expert = {};

        activate();
        function activate() {
            expertSvc.grid(vm);
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

        //S 查看专家详细
        vm.findExportDetail = function (id) {
            expertSvc.getExpertById(id, function (data) {
                vm.model = data;
                $("#queryExportDetail").kendoWindow({
                    width: "80%",
                    height: "auto",
                    title: "专家详细信息",
                    visible: false,
                    modal: true,
                    open:function(){
                        $("#expertPhotoSrc").attr("src", rootPath + "/expert/transportImg?expertId=" + vm.model.expertID + "&t=" + Math.random());
                    },
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            });
        }
        //S 查看专家详细


        /**
         * 导出execl功能
         */
        vm.exportToExcel = function () {
            expertSvc.exportToExcel(vm);
        }
    }
})();
