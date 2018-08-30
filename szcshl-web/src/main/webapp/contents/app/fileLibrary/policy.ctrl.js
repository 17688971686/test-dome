(function(){
    'use strict';
    angular.module('app').controller('policyCtrl',policy);

    policy.$inject=['$scope','$state','$location','fileLibrarySvc' , 'bsWin' , '$interval' , 'sysfileSvc' , 'policySvc'];

    function policy($scope,$state,$location,fileLibrarySvc , bsWin , $interval , sysfileSvc , policySvc){
        var vm = this;
        // vm.title="";
        vm.parentId = $state.params.parentId;
        vm.fileId = $state.params.fileId;
        vm.fileLibrary={};
        vm.fileLibrary.fileType = "POLICY";//初始化文件库类型 - 政策标准文件库
        vm.fileType = "POLICY";
        vm.showPolicyList = true;
        vm.search;

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
                mainId : vm.fileId,
                mainType : sysfileSvc.mainTypeValue().POLICYLIBRARY,
                sysBusiType :vm.fileUrl == undefined ? "" : vm.fileUrl.substring(vm.fileUrl.lastIndexOf(sysfileSvc.mainTypeValue().POLICYLIBRARY),vm.fileUrl.lastIndexOf(vm.fileName)),
                showBusiType : false,
            };
            sysfileSvc.initUploadOptions({
                inputId : "sysfileinput",
                vm :vm ,
                uploadSuccess : function(){
                    sysfileSvc.findByBusinessId(vm.fileId,function(data){
                        vm.sysFilelists = data;
                        fileLibrarySvc.initFileList(vm.parentFileId , vm.fileType , function(data){
                            vm.policyList = data.reObj;
                        })
                    });
                }
            });
        }

        /**
         * 初始化文件列表
         * @param fileId
         */
        vm.initFileList = function(){
            vm.showAddPolicyLibrary = false;
            vm.showPolicyList = true;
            vm.showPolicyDetail = false;
            if(!vm.exist){
                vm.page = lgx.page.init({
                    id: "demo5", get: function (o) {
                        var skip ;
                        vm.price ={};

                        //oracle的分页不一样。
                        if (o.skip != 0) {
                            skip = o.skip + 1
                        } else {
                            skip = o.skip
                        }

                        vm.price.skip = skip;//页码
                        vm.price.size = o.size + o.skip;//页数
                        policySvc.findFileByIdGrid(vm, function (data) {
                            vm.fileList = [];
                            if(vm.fileList){
                                vm.fileList = data.value;
                                vm.page.callback(data.count);//请求回调时传入总记录数

                            }

                        });
                    }
                });
                vm.exist = true;
            }else{
                vm.page.selPage(1);
            }
        }

        /**
         * 初始化树
         */
        vm.initZtree = function(){
            policySvc.initFileFolder($scope,function(data){
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
                        showAddBtn : false ,
                        removeTitle : "删除节点",//设置删除按钮标题
                    },
                    view: {
                        // addHoverDom: addHoverDom, // 添加节点方法
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
                        },
                        showTitle:true, //是否显示节点title信息提示 默认为true
                        key: {
                            title:"standardName" //设置title提示信息对应的属性名称 也就是节点相关的某个属性
                        }
                    }
                };
                function zTreeOnClick(event, treeId, treeNode) {
                    vm.parentFileId = treeNode.id;
                    vm.showAddFile = true;
                    vm.standardId = vm.parentFileId;
                    vm.initFileList();
                };

                //删除节点
                function zTreeOnRemove(event, treeId, treeNode ){
                    var zTree = $.fn.zTree.getZTreeObj("zTree");
                    var treeNode = zTree.getSelectedNodes();
                    vm.onRemove(treeNode.id);
                }

                //添加节点
                function addHoverDom(treeId,treeNode){
                    //判断，如果是文件夹，才有新增按钮,只能到四级
                    if(treeNode.stardandType == 'FOLDER'  && treeNode.level < 2 ) {
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
                    }else{
                        return ;
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
                        returnObj.id = x.id;
                        returnObj.name = x.standardName;
                        returnObj.pId = x.standardPId;

                        if(x.stardandType == 'FOLDER'){
                            returnObj.icon = rootPath+"/contents/libs/zTree/css/zTreeStyle/img/diy/7.png";
                        }
                        return returnObj;
                    }).toArray();

                vm.initFileTreeSucess = true;
                //监听值改变
                $scope.$watch("vm.initFileTreeSucess", function (newValue, oldValue) {
                    if (newValue == true) {
                        var timer = $interval(function () {
                            var s = document.getElementById("zTree");
                            for(var i=0;i<vm.zNodes.length;i++){//控制文件名，多的用点来显示
                                if(vm.zNodes[i].name.length>7){
                                    var ss=vm.zNodes[i].name.substring(0,7);
                                    vm.zNodes[i].name=ss+"...";
                                }
                            }
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


        activate();
        function activate(){
            vm.initFileList();
            vm.initZtree();
            vm.initFileUpload();
        }

        /**
         * 政策指标库文件夹添加操作
         * */
        vm.addFolderWindow=function(fileId){
            vm.fileLibrary = {};
            //初设化是文件夹类型
            vm.fileLibrary.stardandType = "FOLDER";
            if(fileId != undefined){
                vm.fileLibrary.standardPId = fileId;
            }
            vm.showAddPolicyLibrary = true;
            vm.showPolicyList = false;
            vm.showPolicyDetail = false;
        }

        /**
         * 保存新建文件夹 - 作废
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
         * 删除政策指标库
         */
        vm.deleteFolder = function(){
            var zTree = $.fn.zTree.getZTreeObj("zTree");
            var nodes = zTree.getCheckedNodes();
            if(nodes != undefined && nodes.length >0){
                var idStr = nodes[0].id;
                bsWin.confirm("所有子文件将被删除，确认删除？" , function(){
                    policySvc.deletePolicy(idStr , function(data){
                        bsWin.alert("删除成功!");
                        activate();
                    });
                });
            }else{
                bsWin.alert("请选择删除数据!");
            }
        }

        /**
         *新增（更新）政策指标库文件操作
         */
        vm.fileEdit = function(fileId ){
            vm.isUpdate = false;
            vm.fileId = fileId;
            vm.fileLibrary = {};
            vm.sysFilelists = {};
            vm.fileLibrary.stardandType = "FILE"; //初始化政策指标库类型
            vm.fileLibrary.standardPId  = vm.parentFileId; //初始化父Id
            vm.showAddPolicyLibrary = true;
            vm.showPolicyList = false;
            vm.showPolicyDetail = false;
            if(vm.fileId){
                vm.isUpdate=true;
                policySvc.findFileById(vm.fileId , function(data){
                    vm.fileLibrary = data;
                    vm.fileUrl = vm.fileLibrary.fileUrl;
                    vm.fileName = vm.fileLibrary.fileName;
                    vm.initFileUpload();
                });
                sysfileSvc.findByBusinessId(vm.fileId,function(data){
                    vm.sysFilelists = data;
                });
            }
        }

        /**
         * 保存新建文件 -作废
         */
        vm.createFile=function(){
            vm.fileLibrary.parentFileId = vm.parentFileId;
            common.initJqValidation();
            if (vm.fileLibrary.fileName) {

                fileLibrarySvc.saveFile(vm, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        bsWin.alert("保存成功！", function () {
                            /* window.parent.$("#qualityEdit").data("kendoWindow").close();*/
                            vm.isUpdate=true;
                            activate();
                            vm.policyList.push(data.reObj);
                            vm.fileId = data.reObj.fileId;
                            fileLibrarySvc.getFileUrlById(vm, vm.fileId);
                            vm.initFileUpload();

                        });
                    } else {
                        bsWin.error(data.reMsg);
                    }
                    // vm.fileLibrary = {};
                });
            }
        }

        /**
         * 更新文件 -作废
         */
        vm.updateFile = function (){
            fileLibrarySvc.updateFile(vm , function(data){
                if(data.flag || data.reCode == 'ok'){
                    bsWin.alert("更新成功！", function(){
                        vm.initFileUpload();
                        window.parent.$("#qualityEdit").data("kendoWindow").close();
                        activate();
                    });
                }else{
                    bsWin.error(data.reMsg);
                }
            });
        }

        /**
         * 删除文件 - 作废
         * @param fileId
         */
        vm.del = function(fileId){
            bsWin.confirm("删除的数据将无法恢复，确认删除？" , function(){

                //手动删除树节点
                var tree = $.fn.zTree.getZTreeObj("zTree");
                var nodes = tree.getNodeByParam("id",fileId,null) ;
                tree.removeNode(nodes);

                fileLibrarySvc.deleteFile(fileId,function(){
                    vm.policyList.forEach(function(quality , number){
                        if(quality.fileId == fileId){
                            vm.policyList.splice(number , 1);
                        }
                    });
                });
            })
        }

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
                    vm.policyList = data.reObj;
                })
            });
        }

        /**
         * 返回列表页
         */
        vm.goBack = function(){
                vm.showAddPolicyLibrary = false;
                vm.showPolicyList = true;
                vm.showPolicyDetail = false;
            activate();
        }

        /**
         * 保存政策指标库
         */
        vm.create = function(){
            common.initJqValidation($('#policyform'));
            var isValid = $('#policyform').valid();
            if (isValid) {
                policySvc.createPolicy(vm.fileLibrary, function (data) {
                    if (data.flag || data.reCode == 'ok') {

                        //1、先记录当前文件类型,以及父Id 2、清空对象 3、初始化文件类型,以及父Id
                        // vm.stardandType = vm.fileLibrary.stardandType;
                        // vm.standardPId = vm.fileLibrary.standardPId;
                        // vm.fileLibrary = {};
                        // vm.fileLibrary.stardandType = vm.stardandType;
                        // vm.fileLibrary.standardPId = vm.standardPId;
                        bsWin.alert("操作成功！" , function(){
                            vm.fileId = data.reObj.id;
                            vm.fileLibrary.id =  data.reObj.id;
                            vm.update = true;
                            vm.initFileUpload();
                            vm.initZtree();
                        });
                        // activate();
                    } else {
                        bsWin.alert(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("页面未填报完整或者为正确，请检查！");
            }
        }

        /**
         * 查看详情
         * @param fileId
         */
        vm.policyLibraryEdit = function(fileId){
            vm.showPolicyList = false;
            vm.showPolicyDetail = true;
            vm.fileId = fileId;
            policySvc.findByPolicyId(vm , function(data){
                vm.policy = data;
            });

        }


        /**
         * 更新政策指标库，跳转页面
         */
        vm.updatePolicy = function(){
            var zTree = $.fn.zTree.getZTreeObj("zTree");
            var nodes = zTree.getCheckedNodes();
            if(nodes != undefined && nodes.length >0){
                var idStr = nodes[0].id;
                vm.fileId = idStr;
                vm.update = true;
                vm.showAddPolicyLibrary = true;
                vm.showPolicyList = false;
                vm.showPolicyDetail = false;
                policySvc.findByPolicyId(vm , function(data){

                    vm.fileLibrary = data;
                });

            }else{
                bsWin.alert("请选择要更新的数据!");
            }
        }

        /**
         * 保存，更新政策指标库
         */
        vm.updatePolicySave = function(){
            common.initJqValidation($('#policyform'));
            var isValid = $('#policyform').valid();
            if (isValid) {
                policySvc.createPolicy(vm.fileLibrary, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        bsWin.alert("操作成功！" , function(){
                            vm.fileLibrary.id =  data.reObj.id;
                            vm.update = true;
                            // activate();
                            vm.initZtree();
                        });

                    } else {
                        bsWin.alert(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("页面未填报完整或者为正确，请检查！");
            }
        }

        /**
         * 查询
         */
        vm.queryPolicy = function(){
            vm.initFileList();
        }

        /**
         * 更新指标库文件，跳转页面
         * @param policyId
         */
        vm.updatePolicyFile = function(policyId){
            vm.showAddPolicyLibrary = true;
            vm.showPolicyList = false;
            vm.showPolicyDetail = false;
            vm.fileId = policyId;
            policySvc.findByPolicyId(vm , function(data){

                vm.fileLibrary = data;
            });
        }

        /**
         * 删除指标库文件
         * @param policyId
         */
        vm.deletePolicyFile = function(policyId){
            bsWin.confirm("删除的数据无法恢复，确定删除？" , function(){
                policySvc.deletePolicy(policyId , function(data){
                    bsWin.alert("删除成功!");
                    activate();
                });
            })

        }

    }
})();