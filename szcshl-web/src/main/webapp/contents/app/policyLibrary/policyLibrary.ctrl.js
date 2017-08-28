(function(){
    'use strict';
    angular.module('app').controller('policyLibraryCtrl',policyLibrary);

    policyLibrary.$inject=['$scope','$state','$location','policyLibrarySvc'];

    function policyLibrary($scope,$state,$location,policyLibrarySvc){
        var vm = this;
        // vm.title="";
        vm.parentId = $state.params.parentId;
        vm.fileId = $state.params.fileId;
        vm.fileLibrary={};
        activate();
        function activate(){
            policyLibrarySvc.initPolicyFolder(function(data){
                var zTreeObj;
                var setting = {
                    callback:{
                        onClick : zTreeOnClick
                    },
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: "id",
                            pIdKey: "pId"
                        }
                    }
                };
                function zTreeOnClick(event, treeId, treeNode) {
                    $state.go('policyLibrary.policyList',{parentId : treeNode.id,fileId : ''});
                };

                var zNodes = $linq(data).select(
                    function(x){
                        var isParent = false;
                        var pId =null;
                        if(x.parentFileId){
                            pId = x.parentFileId;
                        }
                        return {
                            id : x.fileId,
                            name : x.fileName,
                            pId : pId,
                        };
                    }).toArray();
                zTreeObj = $.fn.zTree.init($("#zTree"),setting,zNodes);
                vm.folderTree = zTreeObj;
            });
        }

        /**
         * 新建文件夹弹出窗
         * */
        vm.addFolderWindow=function(){
            $("#addRootFolder").kendoWindow({
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
        vm.saveRootFolder = function(){
            policyLibrarySvc.saveRootFolder(vm);
        }


    }
})();