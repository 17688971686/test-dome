(function(){
    'use strict';
    angular.module('app').controller('fileLibraryCtrl',fileLibrary);

    fileLibrary.$inject=['$scope','$state','$location','fileLibrarySvc'];

    function fileLibrary($scope,$state,$location,fileLibrarySvc){
        var vm = this;
        vm.title="";
        vm.fileLibrary={};
        vm.parentId = $state.params.parentId;
        vm.fileId = $state.params.fileId;
        activate();
        function activate(){
            fileLibrarySvc.initFolder(vm);
            fileLibrarySvc.initFileList(vm);
        }

        /**
         * 新建文件夹弹出窗
         * */
        vm.addFolderWindow=function(target){
            vm.fileLibrary={};
            vm.target = target;
            if(target == "addChildFolder"){
                vm.fileLibrary.parentFileId = vm.parentId;
            }
            $("#"+target).kendoWindow({
                width: "500px",
                height: "300px",
                title: "新建文件夹",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "close"]
            }).data("kendoWindow").center().open();
        }

        /**
         * 保存新建文件夹
         */
        vm.saveFolder = function(){
            fileLibrarySvc.saveFolder(vm);
        }

        vm.addFile = function(){
            $state.go('fileLibrary.fileEdit',{parentId : vm.parentId,fileId : ''});
        }

        vm.update  = function(fileId){
            $state.go('fileLibrary.fileEdit',{parentId :vm.parentId ,fileId : fileId});
        }

        vm.del = function(fileId){
            common.alert({
                vm : vm ,
                msg : "删除的数据将无法恢复，确定要删除？",
                fn : function (){
                    $('.alertDialog').modal('hide');
                    $('.modal-backdrop').remove();
                    fileLibrarySvc.deleteFile(vm,fileId);
                }
            });
        }

        /**
         * 删除根目录
         */
        vm.deleteRootDirectory=function(){
            fileLibrarySvc.deleteRootDirectory(vm);

        }

        /**
         * 更新根目录
         */
        vm.updateRootDirectory = function(){
            // vm.isSubmit=true;
            // fileLibrarySvc.folderById(vm,vm.parentId);

        }

    }
})();