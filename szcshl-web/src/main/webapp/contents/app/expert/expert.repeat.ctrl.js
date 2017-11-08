(function () {
    'use strict';

    angular.module('app').controller('expertRepeatCtrl', expert);

    expert.$inject = ['$location', 'expertSvc'];

    function expert($location, expertSvc) { 
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
    }
})();
