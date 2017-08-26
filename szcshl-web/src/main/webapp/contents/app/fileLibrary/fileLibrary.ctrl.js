(function(){
    'use strict';
    angular.module('app').controller('fileLibraryCtrl',fileLibrary);

    fileLibrary.$inject=['$scope','$state','$location','fileLibrarySvc'];

    function fileLibrary($scope,$state,$location,fileLibrarySvc){
        var vm = this;
        // vm.title="";
        vm.parentId = $state.params.parentId;
        vm.fileId = $state.params.fileId;
        vm.fileLibrary={};
        activate();
        function activate(){
            fileLibrarySvc.initFolder(function(data){
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
                    $state.go('fileLibrary.fileList',{parentId : treeNode.id,fileId : ''});
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
            fileLibrarySvc.saveRootFolder(vm);
        }


    }
})();