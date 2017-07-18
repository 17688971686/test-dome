(function () {
    'use strict';

    angular.module('app').controller('sharingPlatlformEditCtrl', sharingPlatlform);

    sharingPlatlform.$inject = ['$location', 'sharingPlatlformSvc', '$state'];

    function sharingPlatlform($location, sharingPlatlformSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加共享平台';
        vm.model = {};//共享平台对象
        vm.isuserExist = false;
        vm.model.sharId = $state.params.sharId;
        if (vm.model.sharId) {
            vm.isUpdate = true;
            vm.title = '更新共享平台';
        }
        //重置
        vm.resetSharing = function(){
        	
        	var tab = $("#formSharing").find('input,select');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
        }
      //  vm.contentHide = true;
        //发布部门
        vm.coloseHide = function(){
        	 if(vm.model.pubDept){
            	  vm.publishOrg = true
            }else{
            	 vm.publishOrg = false
            }
        }
     
        //个人发布
        vm.postPerson = function(){
        	var username = "admin";
        	 if(vm.model.postResume){
        		 vm.postResume = true
              }else{
            	  /*common.alert({
                      vm: vm,
                      msg: "个人发布必须为本人",
                      closeDialog :true,
                  })*/
                  vm.postResume = false
              }
        }
       
        
        vm.create = function () {
            sharingPlatlformSvc.createSharingPlatlform(vm);
        };
        vm.update = function () {
            sharingPlatlformSvc.updateSharingPlatlform(vm);
        }; 
        vm.businessFlag ={
            isInitFileOption : false,   //是否已经初始化附件上传控件
        }

        activate();
        function activate() {
        
           if (vm.isUpdate) {
                sharingPlatlformSvc.getSharingPlatlformById(vm);
            }else{
            //附件初始化
              sharingPlatlformSvc.initFileOption({
                    sysfileType: "共享平台",
                     uploadBt: "upload_file_bt",
                       vm: vm
               })
            }
           sharingPlatlformSvc.findAllOrglist(vm);
           sharingPlatlformSvc.findAllUsers(vm);
           sharingPlatlformSvc.findFileList(vm);
           
        }
    }
})();
