(function () {
    'use strict';

    angular.module('app').controller('fileRecordEditCtrl', fileRecord);

    fileRecord.$inject = ['$location','fileRecordSvc','$state']; 

    function fileRecord($location, fileRecordSvc,$state) {     
        var vm = this;
        vm.title = '项目归档编辑';

        vm.fileRecord = {};
        vm.fileRecord.signId = $state.params.signid;
        vm.signId = $state.params.signid;
        
        $("input").click(function(){
        	
        })
        //文件下载
        vm.fileDownload = function(id){
        	fileRecordSvc.fileDownload(vm,id);
        }
        //删除系统文件
        vm.delfileSysFile = function(id){
        	fileRecordSvc.delfileSysFile(vm,id);
        }
        //查看附件
        vm.fileRecordJquery = function(){
        	 $("#filequeryWin").kendoWindow({
                 width : "800px",
                 height : "400px",
                 title : "附件上传",
                 visible : false,
                 modal : true,
                 closable : true,
                 actions : [ "Pin", "Minimize", "Maximize", "Close" ]
             }).data("kendoWindow").center().open();
        	 fileRecordSvc.initFileRecordData(vm);
        }
        
        //上传附件弹窗
        vm.fileRecordUpload = function(){
        	 $("#fileRecordUploadWin").kendoWindow({
                 width : "660px",
                 height : "400px",
                 title : "附件上传",
                 visible : false,
                 modal : true,
                 closable : true,
                 actions : [ "Pin", "Minimize", "Maximize", "Close" ]
             }).data("kendoWindow").center().open();
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
