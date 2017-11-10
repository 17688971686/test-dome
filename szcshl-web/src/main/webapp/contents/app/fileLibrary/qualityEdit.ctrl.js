(function(){
    'use strict';
    angular.module('app').controller('qualityEditCtrl',qualityEdit);
    qualityEdit.$inject=['$state','fileLibrarySvc','sysfileSvc','$scope' , 'bsWin'];
    function qualityEdit($state,fileLibrarySvc,sysfileSvc,$scope , bsWin){
        var vm = this;
        vm.parentId = $state.params.parentId;
        vm.fileLibrary={};
        vm.fileLibrary.parentFileId = vm.parentId;
        vm.fileLibrary.fileType = "QUALITY";//初始化文件库类型 - 质量管理文件库
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
                mainType : sysfileSvc.mainTypeValue().FILELIBRARY,
                sysBusiType :vm.fileUrl.substring(vm.fileUrl.lastIndexOf(sysfileSvc.mainTypeValue().FILELIBRARY),vm.fileUrl.lastIndexOf(vm.fileName))
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
            if(vm.fileId){
                vm.isUpdate=true;
                fileLibrarySvc.findFileById(vm , vm.fileId);
                sysfileSvc.findByBusinessId(vm.fileId,function(data){
                    vm.sysFilelists = data;
                });
            }

            if(vm.parentId){
                fileLibrarySvc.getFileUrlById(vm,vm.parentId);
                fileLibrarySvc.initFileList(vm);
            }
        }

        /**
         * 保存新建文件
         */
        vm.createFile=function(){
            fileLibrarySvc.saveFile(vm , function(data){
                if(data.flag || data.reCode == 'ok'){
                    bsWin.alert("保存成功！", function(){
                        vm.fileId = data.reObj.fileId;
                        vm.initFileUpload();
                    });


                }else{
                    bsWin.error(data.reMsg);
                }
            });
        }

        /**
         * 更新文件
         */
        vm.updateFile = function (){
            fileLibrarySvc.updateFile(vm , function(data){
                if(data.flag || data.reCode == 'ok'){
                    bsWin.alert("更新成功！", function(){
                        vm.initFileUpload();
                    });
                }else{
                    bsWin.error(data.reMsg);
                }
            });
        }

    }

})();