(function () {
    'use strict';

    angular.module('app').controller('archivesLibraryEditCtrl', archivesLibrary);

    archivesLibrary.$inject = ['$location', 'archivesLibrarySvc', '$state','bsWin'];

    function archivesLibrary($location, archivesLibrarySvc, $state,bsWin) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加档案借阅管理';
        vm.isuserExist = false;
        vm.model = {};
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新档案借阅管理';
        }
        
        //部长审批借阅档案
        vm.updateLibrary = function(){
           common.initJqValidation();
            var isValid = $('form').valid();
           if(isValid){
        	   archivesLibrarySvc.updateArchivesLibrary(vm);
        	 /*  archivesLibrarySvc.updateArchivesLibrary(vm.model,function(data){
        		   vm.isSubmit = false;
                   bsWin.alert("操作成功！");
                });*/
            }else{
            	bsWin.alert("请填写审批意见，再提交！");
            }
        	 
        }
        

        activate();
        function activate() {
            if (vm.isUpdate) {
                archivesLibrarySvc.getArchivesLibraryById(vm);
                //获取归档员
                archivesLibrarySvc.getArchivesUserName(vm);
            }
        }
    }
})();
