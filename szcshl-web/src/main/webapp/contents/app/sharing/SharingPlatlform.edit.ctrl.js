(function () {
    'use strict';

    angular.module('app').controller('sharingPlatlformEditCtrl', sharingPlatlform);

    sharingPlatlform.$inject = ['$location', 'sharingPlatlformSvc', '$state'];

    function sharingPlatlform($location, sharingPlatlformSvc, $state) {
        var vm = this;
        vm.title = '新增共享资料';
        vm.model = {};                   //共享平台对象
        vm.businessFlag ={
            isInitFileOption : false,   //是否已经初始化附件上传控件
            isUpdate : false,           //是否为更改
            isInitSeled : false,        //是否已经初始化选择了
        }

        vm.model.sharId = $state.params.sharId;
        if (vm.model.sharId) {
            vm.businessFlag.isUpdate = true;
            vm.title = '更改共享资料';
        }
        activate();
        function activate() {
            if (vm.businessFlag.isUpdate) {
                sharingPlatlformSvc.getSharingPlatlformById(vm);
                //初始化附件列表
                sharingPlatlformSvc.findFileList(vm);
            }else{
                //附件初始化
                sharingPlatlformSvc.initFileOption({
                    sysfileType: "共享平台",
                    uploadBt: "upload_file_bt",
                    vm: vm
                })
            }
            //初始化部门和用户
            sharingPlatlformSvc.initOrgAndUser(vm);
        }

        //重置
        vm.resetSharing = function(){
        	var tab = $("#formSharing").find('input,select');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
        }
        
        vm.create = function () {
            sharingPlatlformSvc.createSharingPlatlform(vm);
        };
        vm.update = function () {
            sharingPlatlformSvc.updateSharingPlatlform(vm);
        };

        // 业务判断
        vm.checkBox = function ($event) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            var checkboxValue = checkbox.value;
            if (checked) {
                $('input[tit=\"' + checkboxValue + '\"]').each(function () {
                    $(this).attr("disabled", "disabled");
                    $(this).removeAttr("checked");
                });
            } else {
                $('input[tit=\"' + checkboxValue + '\"]').each(function () {
                    $(this).removeAttr("disabled");
                });
            }
        }
        

    }
})();
