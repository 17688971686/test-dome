(function(){
    'use strict';
    angular.module('app').controller('qualityCtrl', quality);

    quality.$inject=['bsWin','$state','$location','fileLibrarySvc' ,'$scope' , '$interval' , 'sysfileSvc'];

    function quality(bsWin,$state,$location,fileLibrarySvc , $scope , $interval , sysfileSvc){
        var vm = this;
        // vm.title="";
        vm.parentId = $state.params.parentId;
        vm.fileLibrary={};
        vm.fileLibrary.fileType = "QUALITY";//初始化文件库类型 - 质量管理文件库
        vm.fileType = "QUALITY";
        vm.qualityList = [];
        activate();
        function activate(){
            fileLibrarySvc.initFileFolder(vm, $scope , function(data){
                // vm.qualityList = data;

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
                        addHoverDom: addHoverDom, // 鼠标经过时，有添加按钮
                        removeHoverDom: removeHoverDom, // 鼠标移除，删除添加按钮
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
                            pIdKey: "pId",
                        },
                    }
                };
                function zTreeOnClick(event, treeId, treeNode ) {
                    // $state.go('fileLibrary.fileList',{parentId : treeNode.id});
                    vm.parentFileId = treeNode.fileId;
                    if(treeNode.fileNature == "FOLDER" ){
                        vm.qualityList = [];
                        if(treeNode.children){
                            vm.qualityList = treeNode.children;
                        }
                        $scope.$apply();
                    }

                };
                //删除节点
                function zTreeOnRemove(event, treeId, treeNode ){
                    // var zTree = $.fn.zTree.getZTreeObj("zTree");
                    // var treeNode = zTree.getSelectedNodes();
                    vm.onRemove(treeNode.id);
                }
                //添加节点
                function addHoverDom(treeId,treeNode){
                    //判断，如果是文件夹，才有新增按钮
                    if(treeNode.fileNature == 'FOLDER'){
                        var sObj = $("#" + treeNode.tId + "_span");
                        if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
                        var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
                            + "' title='添加子节点' onfocus='this.blur();'></span>";
                        sObj.after(addStr);
                        var btn = $("#addBtn_"+treeNode.tId);
                        if (btn) btn.bind("click", function(){
                            var zTree = $.fn.zTree.getZTreeObj("zTree");
                            vm.addFolderWindow(treeNode.id);
                        });
                    }

                }

                //删除节点
                function removeHoverDom(treeId,treeNode){
                    $("#addBtn_"+treeNode.tId).unbind().remove();
                }

                vm.zNodes = $linq(data).select(
                    function(x){
                        var pId = null;
                        var returnObj = x;
                        returnObj = x;
                        returnObj.id = x.fileId;
                        returnObj.name = x.fileName;
                        returnObj.pId = x.parentFileId;

                        if(x.fileNature == 'FOLDER'){
                            returnObj.icon = rootPath+"/contents/libs/zTree/css/zTreeStyle/img/diy/7.png";
                        }
                        return returnObj;
                    }).toArray();

                vm.initFileTreeSucess = true;
                // zTreeObj = $.fn.zTree.init($("#zTree"),setting,  zNodes );
                // vm.folderTree = zTreeObj;

                //监听值改变
                $scope.$watch("vm.initFileTreeSucess", function (newValue, oldValue) {
                    if (newValue == true) {
                        var timer = $interval(function () {
                            var s = document.getElementById("zTree");
                            //当有ztree的id时开始赋值
                            if (s != null) {
                                zTreeObj = $.fn.zTree.init($("#zTree"), setting, vm.zNodes);
                                $interval.cancel(timer);//停止定时器
                            }
                        }, 500);   //间隔0.5秒定时执行
                    }
                });
            });
        }

        /**
         * 新建文件夹弹出窗
         * */
        vm.addFolderWindow=function(fileId){
            vm.fileLibrary = {};
            vm.fileLibrary.fileType = "QUALITY";
            if(fileId != undefined){
                vm.fileLibrary.parentFileId = fileId;
            }
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

        /**
         * 删除文件夹 -停用
         * @param treeId
         * @param $scope
         */
        $scope.onRemove = function(treeId ){
            bsWin.confirm("删除的文件将无法恢复，确认删除？" , function(){
                fileLibrarySvc.deleteRootDirectory(treeId , function(data){
                    if(data.flag || data.reCode == 'ok'){
                        bsWin.success("操作成功！");
                        return true;
                    }else{
                        bsWin.error(data.reMsg);
                        return false;
                    }

                });
            });
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
                            $state.go('fileLibrary',{},{reload:true});
                        }else{
                            bsWin.error(data.reMsg);
                        }

                    });
                });
            }
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
                        fileLibrarySvc.initFileList(vm.parentFileId , vm.fileType, function(data){
                            vm.qualityList = data.reObj;
                        })
                    });
                }
            });
        }

        /**
         * 新建文件 或 更新文件 操作
         */
        vm.fileEdit = function(fileId ){
            vm.isUpdate = false;
            vm.fileId = fileId;
            vm.fileLibrary = {};
            vm.fileLibrary.fileType = "QUALITY";
            if(vm.fileId){
                vm.isUpdate=true;
                fileLibrarySvc.findFileById(vm , vm.fileId);
                sysfileSvc.findByBusinessId(vm.fileId,function(data){
                    vm.sysFilelists = data;
                });
            }
            $("#qualityEdit").kendoWindow({
                width: "800px",
                height: "500px",
                title: "文件编辑",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "close"]
            }).data("kendoWindow").center().open();
        }

        /**
         * 保存新建文件
         */
        vm.createFile=function(){
            vm.fileLibrary.parentFileId = vm.parentFileId;
            common.initJqValidation();
            if (vm.fileLibrary.fileName) {
                fileLibrarySvc.saveFile(vm, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        bsWin.alert("保存成功！", function () {
                            vm.qualityList.push(data.reObj);
                            vm.fileId = data.reObj.fileId;
                            fileLibrarySvc.getFileUrlById(vm, vm.fileId);
                            vm.initFileUpload();
                        });
                    } else {
                        bsWin.error(data.reMsg);
                    }
                });
            }
        }

        /**
         * 更新文件
         */
        vm.updateFile = function (){
            fileLibrarySvc.updateFile(vm , function(data){
                if(data.flag || data.reCode == 'ok'){
                    bsWin.alert("更新成功！", function(){
                        vm.initFileUpload();
                        window.parent.$("#qualityEdit").data("kendoWindow").close();
                    });
                }else{
                    bsWin.error(data.reMsg);
                }
            });
        }

        /**
         * 删除文件
         * @param fileId
         */
        vm.del = function(fileId){
            bsWin.confirm("删除的数据将无法恢复，确认删除？" , function(){
                fileLibrarySvc.deleteFile(fileId,function(){
                    vm.qualityList.forEach(function(quality , number){
                        if(quality.fileId == fileId){
                            vm.qualityList.splice(number , 1);
                        }
                    });
                });
            })
        }


        /**
         *  模糊查询
         */
       /* vm.queryUser=function(){
            fileLibrarySvc.queryUser(vm);
        };*/

        /**
         * 重置按钮
         */
        /*vm.resetUser = function(){
            var tab = $("#fileForm").find('input');
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }*/

        /**
         * 附件下载
         */

        vm.commonDownloadSysFile = function(sysFileId){
            sysfileSvc.downloadFile(sysFileId);
        }
        /**
         * 重写 删除附件
         * @param fileId
         */
        vm.delFile = function(fileId){
            sysfileSvc.delSysFile(fileId, function () {
                $.each(vm.sysFilelists, function (i, sf) {
                    if (sf.sysFileId == fileId) {
                        vm.sysFilelists.splice(i, 1);
                    }
                });
                fileLibrarySvc.initFileList(vm.parentFileId , vm.fileType , function(data){
                    vm.qualityList = data.reObj;
                })
            });
        }
    }
})();