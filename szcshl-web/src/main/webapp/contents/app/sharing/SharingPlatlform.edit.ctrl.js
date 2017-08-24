(function () {
    'use strict';

    angular.module('app').controller('sharingPlatlformEditCtrl', sharingPlatlform);

    sharingPlatlform.$inject = ['sharingPlatlformSvc', '$state','sysfileSvc','$scope'];

    function sharingPlatlform(sharingPlatlformSvc, $state,sysfileSvc,$scope) {
        var vm = this;
        vm.title = '新增共享资料';
        vm.model = {};                   //共享平台对象
        vm.businessFlag ={
            isUpdate : false,           //是否为更改
            isLoadModel : false,        //是否已经加载对象
            isLoadOrgUser : false,      //是否已经加载部门和用户
        }

        vm.model.sharId = $state.params.sharId;
        if (vm.model.sharId) {
            vm.businessFlag.isUpdate = true;
            vm.title = '更改共享资料';
        }
        //初始化附件上传控件
        vm.initFileUpload = function(){
            if(!vm.model.sharId){
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.model.sharId",function (newValue, oldValue) {
                    if(newValue && newValue != oldValue && !vm.initUploadOptionSuccess){
                        vm.initFileUpload();
                    }
                });
            }

            //创建附件对象
            vm.sysFile = {
                businessId : vm.model.sharId,
                mainId : vm.model.sharId,
                mainType : sysfileSvc.mainTypeValue().SHARE,
            };
            sysfileSvc.initUploadOptions({
                inputId:"sysfileinput",
                vm:vm,
                uploadSuccess:function(){
                    sysfileSvc.findByBusinessId(vm.model.sharId,function(data){
                        vm.sysFilelists = data;
                    });
                }
            });
        }

        activate();
        function activate() {
            if (vm.model.sharId) {
                sharingPlatlformSvc.getSharingPlatlformById(vm);
                //初始化附件列表
                sysfileSvc.findByBusinessId(vm.model.sharId,function(data){
                    vm.sysFilelists = data;
                });
            }else{
                vm.initFileUpload();
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

        /**
         * 保存发布信息
         */
        vm.create = function () {
            sharingPlatlformSvc.createSharingPlatlform(vm);
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
