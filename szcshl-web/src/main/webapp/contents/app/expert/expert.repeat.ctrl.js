(function () {
    'use strict';

    angular.module('app').controller('expertRepeatCtrl', expert);

    expert.$inject = ['$location', 'expertSvc' , 'bsWin'];

    function expert($location, expertSvc , bsWin) {
    	var vm = this;
    	
        activate();
        function activate() {
        	expertSvc.repeatGrid(vm);
        }

        //S 查看专家详细
        vm.findExportDetail = function (id) {
            expertSvc.getExpertById(id, function (data) {
                vm.model = data;
                $("#reExportDetail").kendoWindow({
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
         * 删除专家
         * @param expertId
         */
        vm.del = function(expertId){
            bsWin.confirm("删除数据不可恢复，确认删除数据吗？" , function(){
                expertSvc.deleteExpertData(vm, expertId , function(data){
                    bsWin.alert("操作成功", function(){
                        vm.repeatGridOptions.dataSource.read();
                    });
                });
            })
        }


        /**
         * 批量删除
         */
        vm.dels = function(){
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                bsWin.alert("请选择数据");
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.del(idStr);
            }
        }
    }
})();
