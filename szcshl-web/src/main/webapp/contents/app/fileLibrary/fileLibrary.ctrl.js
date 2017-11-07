(function(){
    'use strict';
    angular.module('app').controller('fileLibraryCtrl',fileLibrary);

    fileLibrary.$inject=['bsWin','$state','$location','fileLibrarySvc'];

    function fileLibrary(bsWin,$state,$location,fileLibrarySvc){
        var vm = this;
        // vm.title="";
        vm.parentId = $state.params.parentId;
        vm.fileId = $state.params.fileId;
        vm.fileLibrary={};
        activate();
        function activate(){
            fileLibrarySvc.initFileFolder(function(data){
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
                width: "600px",
                height: "300px",
                title: "创建文件夹",
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
            if (vm.fileLibrary.fileName != undefined) {
                fileLibrarySvc.saveRootFolder(vm.fileLibrary,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        bsWin.alert("保存成功！",function(){
                            window.parent.$("#addRootFolder").data("kendoWindow").close();
                            $state.go('fileLibrary',{},{reload:true});
                        });
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("文件名不能为空!");
            }
        }


    }
})();