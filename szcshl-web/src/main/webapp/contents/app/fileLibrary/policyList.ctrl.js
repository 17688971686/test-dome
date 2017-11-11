(function(){
    'use strict';
    angular.module('app').controller('policyListCtrl',policyList);
    policyList.$inject=['$state','fileLibrarySvc', 'bsWin'];
    function policyList($state,fileLibrarySvc, bsWin){
        var vm = this;
        vm.parentId = $state.params.parentId;
        vm.fileLibrary={};
        vm.fileLibrary.parentFileId = vm.parentId;
        vm.fileLibrary.fileType = "POLICY";//初始化文件库类型 - 政策标准文件库
        vm.formId = "policyForm";

        activate();
        function activate(){
            fileLibrarySvc.initFileList(vm);
        }

        /**
         * 新建文件跳转页
         */
        vm.addFile = function(){
            $state.go('policyLibrary.policyEdit',{parentId : vm.parentId,fileId : ''});
        }

        /**
         * 跳转更新文件页面
         * @param fileId
         */
        vm.update  = function(fileId){
            $state.go('policyLibrary.policyEdit',{parentId :vm.parentId ,fileId : fileId});
        }

        /**
         * 删除文件
         * @param fileId
         */
        vm.del = function(fileId){
            bsWin.confirm("删除的数据将无法恢复，确认删除？", function(){
                fileLibrarySvc.deleteFile(vm,fileId);
            })
        }


        /**
         *  模糊查询
         */
        vm.queryPolicy=function(){
            console.log(24654);
            fileLibrarySvc.queryUser(vm);
        };

        /**
         * 重置按钮
         */
        vm.resetPolicy = function(){
            console.log(4546);
            var tab = $("#policyForm").find('input');
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }

    }

})();