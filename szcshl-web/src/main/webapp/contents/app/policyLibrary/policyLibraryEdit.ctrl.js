(function(){
    'use strict';
    angular.module('app').controller('policyLibraryEditCtrl',policyLibraryEdit);
    policyLibraryEdit.$inject=['$state','policyLibrarySvc','sysfileSvc','$scope'];
    function policyLibraryEdit($state,policyLibrarySvc,sysfileSvc,$scope){
        var vm = this;
        vm.parentId = $state.params.parentId;
        vm.fileLibrary={};
        vm.fileLibrary.parentFileId = vm.parentId;
        vm.fileId = $state.params.fileId;
        vm.fileUrl = "";
        vm.fileName="";

        vm.businessFlag ={
            isInitFileOption : false,   //是否已经初始化附件上传控件
        }

        //初始化附件上传控件
        vm.initFileUpload = function(){
            if(!vm.fileId){
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.fileId",function (newValue , oldValue){
                    if(newValue && newValue != oldValue && !vm.initUploadOptionSuccess){
                        vm.initFileUpload();
                    }
                });
            }
            //创建附件对象
            vm.sysFile = {
                businessId : vm.fileId,
                mainId : '',
                mainType : sysfileSvc.mainTypeValue().POLICYLIBRARY,
                sysBusiType :vm.fileUrl.substring(vm.fileUrl.lastIndexOf(sysfileSvc.mainTypeValue().POLICYLIBRARY),vm.fileUrl.lastIndexOf(vm.fileName))
            };
            sysfileSvc.initUploadOptions({
                inputId : "sysfileinput",
                vm :vm ,
                uploadSuccess : function(){
                    sysfileSvc.findByBusinessId(vm.fileId,function(data){
                        vm.sysFilelists = data;
                    });
                }
            });
        }


        activate();
        function activate(){
            if(vm.parentId){
                policyLibrarySvc.getFileUrlById(vm,vm.parentId);
                policyLibrarySvc.initFileList(vm);
            }
            if(vm.fileId){
                vm.isUpdate=true;
                policyLibrarySvc.findFileById(vm , vm.fileId);
                sysfileSvc.findByBusinessId(vm.fileId,function(data){
                    vm.sysFilelists = data;
                });
            }

        }


        /**
         * 新建文件夹弹出窗
         * */
        vm.addFolderWindow=function(){
            vm.fileLibrary.parentFileId = vm.parentId;
            $("#addChildFolder").kendoWindow({
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
        vm.saveChildFolder = function(){
            policyLibrarySvc.saveChildFolder(vm);
        }

        /**
         * 新建文件跳转页
         */
        vm.addFile = function(){
            $state.go('policyLibrary.policyEdit',{parentId : vm.parentId,fileId : ''});
        }

        /**
         * 保存新建文件
         */
        vm.createFile=function(){
            policyLibrarySvc.saveFile(vm);
        }

        /**
         * 跳转更新文件页面
         * @param fileId
         */
        vm.update  = function(fileId){
            $state.go('policyLibrary.policyEdit',{parentId :vm.parentId ,fileId : fileId});
        }

        /**
         * 更新文件
         */
        vm.updateFile = function (){
            policyLibrarySvc.updateFile(vm);
        }


        /**
         * 删除文件
         * @param fileId
         */
        vm.del = function(fileId){
            common.alert({
                vm : vm ,
                msg : "删除的数据将无法恢复，确定要删除？",
                fn : function (){
                    $('.alertDialog').modal('hide');
                    $('.modal-backdrop').remove();
                    policyLibrarySvc.deleteFile(vm,fileId);
                }
            });
        }


        /**
         * 删除文件根目录
         */
        vm.deleteRootDirectory=function(){
            policyLibrarySvc.deleteRootDirectory(vm);

        }


        //模糊查询
        vm.queryUser=function(){
            policyLibrarySvc.queryUser(vm);
        };


    }

})();