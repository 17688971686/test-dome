(function () {
    'use strict';

    angular.module('app').controller('fileRecordEditCtrl', fileRecord);

    fileRecord.$inject = ['$location','fileRecordSvc','$state',"$http"]; 

    function fileRecord($location, fileRecordSvc,$state,$http) {     
        var vm = this;
        vm.title = '项目归档编辑';

        vm.fileRecord = {};
        vm.fileRecord.signId = $state.params.signid;
        vm.signId = $state.params.signid;
        
        $("input").click(function(){
        	
        })
        //文件下载
        	
        vm.commonDownloadSysFile = function(id){
        	common.commonDownloadFile(vm,id);
        }
        //删除系统文件
        vm.commonDelSysFile = function(id){
        	common.commonDelSysFile(vm,id,$http);
        }
        
        //查看附件
        vm.fileRecordJquery = function(){
        	common.initcommonQueryWin(vm);
        	vm.sysSignId=vm.fileRecord.fileRecordId;
        	common.commonSysFilelist(vm,$http);
        }
        
        //上传附件弹窗
        vm.fileRecordUpload = function(options){
        	common.initcommonUploadWin({businessId:vm.fileRecord.fileRecordId});
        }
        
        activate();
        function activate(){
        	fileRecordSvc.initFileRecordData(vm);
        }
                
        vm.create = function(){
        	fileRecordSvc.saveFileRecord(vm);
        }
    }
})();
