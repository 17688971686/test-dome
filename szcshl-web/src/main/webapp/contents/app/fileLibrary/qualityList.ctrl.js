(function(){
    'use strict';
    angular.module('app').controller('qualityListCtrl',qualityList);
    qualityList.$inject=['$state','fileLibrarySvc','sysfileSvc','$scope' , 'bsWin'];
    function qualityList($state,fileLibrarySvc,sysfileSvc,$scope , bsWin){
        var vm = this;
        vm.parentId = $state.params.parentId;
        vm.fileLibrary={};
        vm.fileLibrary.parentFileId = vm.parentId;
        vm.fileLibrary.fileType = "QUALITY";//初始化文件库类型 - 质量管理文件库
        vm.formId = "fileForm";

        activate();
        function activate(){
            fileLibrarySvc.initFileList(vm);
        }
        /**
         * 新建文件跳转页
         */
        vm.addFile = function(){
            $state.go('fileLibrary.fileEdit',{parentId : vm.parentId,fileId : ''});
        }

        /**
         * 跳转更新文件页面
         * @param fileId
         */
        vm.update  = function(fileId){
            $state.go('fileLibrary.fileEdit',{parentId :vm.parentId ,fileId : fileId});
        }

        /**
         * 删除文件
         * @param fileId
         */
        vm.del = function(fileId){
            bsWin.confirm("删除的数据将无法恢复，确认删除？" , function(){
                fileLibrarySvc.deleteFile(vm,fileId);
            })
        }


        /**
         *  模糊查询
         */
        vm.queryUser=function(){
            fileLibrarySvc.queryUser(vm);
        };

        /**
         * 重置按钮
         */
        vm.resetUser = function(){
            var tab = $("#fileForm").find('input');
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }



    }

})();