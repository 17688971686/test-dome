(function(){
    'use strict';
    angular.module('app').controller('policyCtrl',policy);

    policy.$inject=['$scope','$state','$location','fileLibrarySvc' , 'bsWin'];

    function policy($scope,$state,$location,fileLibrarySvc , bsWin){
        var vm = this;
        // vm.title="";
        vm.parentId = $state.params.parentId;
        vm.fileId = $state.params.fileId;
        vm.fileLibrary={};
        vm.fileLibrary.fileType = "POLICY";//初始化文件库类型 - 政策标准文件库
        activate();
        function activate(){
            fileLibrarySvc.initFileFolder(vm ,function(data){
                var zTreeObj;
                var setting = {
                    check: {
                        enable: true,
                        chkStyle: "radio",
                        chkboxType: { "Y": "", "N": "" }
                    },
                    edit:{
                        enable :true,
                        // editNameSelectAll: true,//设置编辑时
                        showRenameBtn : false, //不显示编辑按钮
                        showRemoveBtn : false,//显示删除按钮
                        showAddBtn : true ,
                        removeTitle : "删除节点",//设置删除按钮标题
                    },
                    view: {
                        addHoverDom: addHoverDom, // 添加节点方法
                        removeHoverDom: removeHoverDom, // 删除节点方法
                        dblClickExpand: true , //双击节点 自动展开子节点

                    },
                    callback:{
                        onClick : zTreeOnClick,
                        beforeRemove : zTreeOnRemove,
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
                    $state.go('policyLibrary.policyList',{parentId : treeNode.id});
                };

                //删除节点
                function zTreeOnRemove(event, treeId, treeNode ){
                    var zTree = $.fn.zTree.getZTreeObj("zTree");
                    var treeNode = zTree.getSelectedNodes();
                    console.log(treeNode);
                    vm.onRemove(treeNode.id);
                }

                //添加节点
                function addHoverDom(treeId,treeNode){
                    //判断，如果是文件夹，才有新增按钮
                    if(treeNode.fileNature == 'FOLDER') {
                        var sObj = $("#" + treeNode.tId + "_span");
                        if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
                        var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
                            + "' title='添加子节点' onfocus='this.blur();'></span>";
                        sObj.after(addStr);
                        var btn = $("#addBtn_" + treeNode.tId);
                        if (btn) btn.bind("click", function () {
                            var zTree = $.fn.zTree.getZTreeObj("zTree");
                            vm.addFolderWindow(treeNode.id);
                        });
                    }
                }

                //删除节点
                function removeHoverDom(treeId,treeNode){
                    $("#addBtn_"+treeNode.tId).unbind().remove();
                }
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
                            fileNature : x.fileNature
                        };
                    }).toArray();
                zTreeObj = $.fn.zTree.init($("#zTree"),setting,zNodes);
                vm.folderTree = zTreeObj;
            });
        }

        /**
         * 新建文件夹弹出窗
         * */
        vm.addFolderWindow=function(fileId){
            vm.fileLibrary = {};
            vm.fileLibrary.fileType = "POLICY";
            if(fileId != undefined){
                vm.fileLibrary.parentFileId = fileId;
            }
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
            if (vm.fileLibrary.fileName != undefined) {
                fileLibrarySvc.saveRootFolder(vm.fileLibrary,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        bsWin.alert("保存成功！",function(){
                            window.parent.$("#addRootFolder").data("kendoWindow").close();
                            $state.go('policyLibrary',{},{reload:true});
                        });
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("文件名不能为空!");
            }
        }

        /**
         * 删除文件夹
         */
        vm.deleteFolder = function(){
            var zTree = $.fn.zTree.getZTreeObj("zTree");
            var nodes = zTree.getCheckedNodes();
            if(nodes != undefined && nodes.length >0){
                // var ids = [];
                var idStr = nodes[0].id;
                //判断是不是选择的是父节点，如果有选择子节点，则默认传递子节点的id进行删除
                // if (nodes.length == 1 ){
                //     idStr = nodes[0].id;
                // }else{
                //     for(var i=1 ; i<nodes.length ; i++){
                //         ids.push(nodes[i].id);
                //     }
                //     idStr = ids.join(',');
                // }

                bsWin.confirm("删除的文件将无法恢复，确认删除？" , function(){
                    fileLibrarySvc.deleteRootDirectory(idStr , function(data){
                        if(data.flag || data.reCode == 'ok'){
                            bsWin.success("操作成功！");
                            $state.go('policyLibrary',{},{reload:true});
                        }else{
                            bsWin.error(data.reMsg);
                        }

                    });
                });
            }
        }



    }
})();