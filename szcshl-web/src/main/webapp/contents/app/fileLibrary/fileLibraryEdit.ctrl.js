(function(){
    'use strict';
    angular.module('app').controller('fileLibraryEditCtrl',fileLibraryEdit);
    fileLibraryEdit.$inject=['$state','fileLibrarySvc'];
    function fileLibraryEdit($state,fileLibrarySvc){
        var vm = this;
        vm.parentId = $state.params.parentId;
        vm.fileLibrary={};
        vm.fileLibrary.parentFileId = vm.parentId;
        vm.fileId = $state.params.fileId;

        vm.businessFlag ={
            isInitFileOption : false,   //是否已经初始化附件上传控件
        }

        activate();
        function activate(){
            if(vm.fileId){
                vm.isUpdate=true;
                fileLibrarySvc.findFileById(vm , vm.fileId);
                fileLibrarySvc.findFileList(vm,vm.fileId);
            }else{
                //附件初始化
                fileLibrarySvc.initFileOption({
                    sysfileType: "文件库附件",
                    uploadBt: "upload_file_bt",
                    vm: vm
                })
            }
        }

        vm.createFile=function(){
            fileLibrarySvc.saveFile(vm);
        }

        vm.updateFile = function (){
            fileLibrarySvc.updateFile(vm);
        }

    }

})();