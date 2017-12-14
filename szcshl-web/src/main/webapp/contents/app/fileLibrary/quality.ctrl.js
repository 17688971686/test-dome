(function(){
    'use strict';
    angular.module('app').controller('qualityCtrl', quality);

    quality.$inject=['bsWin','$state','$location','fileLibrarySvc' ,'$scope' , '$interval'];

    function quality(bsWin,$state,$location,fileLibrarySvc , $scope , $interval){
        var vm = this;
        // vm.title="";
        vm.parentId = $state.params.parentId;
        vm.fileLibrary={};
        vm.fileLibrary.fileType = "QUALITY";//初始化文件库类型 - 质量管理文件库
        vm.qualityList = [];
        activate();
        function activate(){
            fileLibrarySvc.initFileFolder(vm, $scope , function(data){
                vm.qualityList = data;
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
                    if(treeNode.check_Child_State == 0 ){
                        vm.qualityList = [];
                        if(treeNode.children){
                            vm.qualityList = treeNode.children;
                        }
                        $scope.$apply();
                        // console.log(vm.qualityList);
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
                        var returnObj = {
                            id : x.fileId,
                            name : x.fileName,
                            pId : x.parentFileId,
                            fileNature : x.fileNature,
                            sysFileDtoList : x.sysFileDtoList,
                        }

                        if(x.fileNature == 'FOLDER'){
                            returnObj.icon = rootPath+"/contents/libs/zTree/css/zTreeStyle/img/diy/7.png";
                        }

                        /*return {
                            id : x.fileId,
                            name : x.fileName,
                            pId : pId,
                            fileNature : x.fileNature,
                            icon : rootPath+"/contents/libs/zTree/css/zTreeStyle/img/diy/7.png"
                        };*/
                        return returnObj;
                    }).toArray();
                // zTreeObj = $.fn.zTree.init($("#zTree"),setting,  zNodes );
                // vm.folderTree = zTreeObj;

                var array = vm.qualityList;
                vm.zNodes = [];
                vm.initFileTreeSucess = false;
                var data = [];
                //循环数据取出父类和相对应的子类
                for(var i=0 , l = array.length ; i < l ; i++){
                    var s = new Object();
                    s = array[i];
                    s.name = array[i].fileName;
                    s.id = array[i].fileId ;
                    //如果父id为空，说明是第一级文件夹,直接以第一级的文件名作为key
                    if(!array[i].parentFileId  ) {
                        var node = new Object();//定义父类的对象
                        node.id = (new Date()).getTime();
                        node.name = array[i].fileName;
                        vm.zNodes.push(node);
                        var arr = [];
                        arr.push(s);
                        data[array[i].fileName] = arr;
                    }else {
                        for(var j=0 , h = array.length ; j<h ; j++){
                            if(array[i].parentFileId == array[j].fileId){
                                data[array[j].fileName].push(s)
                            }
                        }
                    }
                }
                for(var i=0,l=vm.zNodes.length;i<l;i++){
                    for(var key in data){
                        if(vm.zNodes[i].name == key){
                            vm.zNodes[i].children = data[key];
                        }
                    }
                    if(i==(l-1)){
                        vm.initFileTreeSucess = true;
                    }
                }


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
        vm.update  = function(fileId,parentFileId){
            $state.go('fileLibrary.fileEdit',{parentId :parentFileId ,fileId : fileId});
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