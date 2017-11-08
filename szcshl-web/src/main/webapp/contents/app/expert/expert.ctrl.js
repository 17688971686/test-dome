(function () {
    'expert strict';

    angular.module('app').controller('expertCtrl', expert);

    expert.$inject = ['$location', 'expertSvc' , '$state'];

    function expert($location, expertSvc , $state) {
        var vm = this;
        vm.data = {};
        vm.title = '专家列表';
        vm.expertId = "";
        vm.headerType = "专家类型";
        vm.fileName = "专家信息";
        vm.expert = {};

        //S 查看专家详细
        vm.findExportDetail = function(id){
              $("#exportDetail").kendoWindow({
             width: "80%",
             height: "800px",
             title: "专家详细信息",
             visible: false,
             modal: true,
             closable: true,
             actions: ["Pin", "Minimize", "Maximize", "Close"]
             }).data("kendoWindow").center().open();
             if (id) {
             expertSvc.getExpertById(id, function (data) {
             vm.model = data;
             vm.showSS = false;
             vm.showSC = true;
             vm.showWS = false;
             vm.showWC = true;
             // initUpload(vm);
             $("#expertPhotoSrc").attr("src", rootPath + "/expert/transportImg?expertId=" + vm.model.expertID + "&t=" + Math.random());
             });
             }
        }
        //S 查看专家详细
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

        /**
         * 导出execl功能
         */
        vm.exportToExcel = function () {
            expertSvc.exportToExcel(vm);
        }
    }
})();
