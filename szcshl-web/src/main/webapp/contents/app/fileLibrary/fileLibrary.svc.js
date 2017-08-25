(function(){
    'use strict';
    angular.module('app').factory('fileLibrarySvc',fileLibrary);

    fileLibrary.$inject=['$http','$state','$location'];

    function fileLibrary($http,$state,$location){
        var service={
            saveFolder : saveFolder,//新建文件夹
            initFolder : initFolder ,//初始化文件夹
            initFileList : initFileList,//初始化文件夹下所有文件
            saveFile : saveFile,//保存文件
            findFileById : findFileById ,//通过id查询文件
            updateFile : updateFile,//更新文件
            deleteFile : deleteFile,//删除文件
            initFileOption: initFileOption,             //初始化附件参数
            findFileList: findFileList,                 //查询附件列表
            deleteRootDirectory : deleteRootDirectory ,//删除根目录
            folderById : folderById , //通过id查询文件夹

        }

        return service;


        //begin deleteRootDirectory
        function deleteRootDirectory(vm){
            var httpOptions = {
                method : "delete",
                url : rootPath +"/fileLibrary/deleteRootDirectory",
                params : {parentFileId : vm.parentId}
            }

            var httpSuccess = function success(response){
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {

                        common.alert({
                            vm: vm,
                            msg: "操作成功",
                            fn: function () {
                                vm.isSubmit = false;
                                $('.alertDialog').modal('hide');
                                $('.modal-backdrop').remove();
                                initFolder(vm);
                                location.href = rootPath + "/admin/index#/fileLibrary";
                            }
                        })
                    }

                });
            }
            common.http({
                vm : vm ,
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end deleteRootDirectory


        //begin deleteFile
        function deleteFile(vm,fileId){
            var httpOptions = {
                method : "delete",
                url : rootPath + "/fileLibrary/deleteFile",
                params : {fileId : fileId}
            }

            var httpSuccess = function success(response){
                vm.gridOptions.dataSource.read();
            }
            common.http({
                vm : vm ,
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end deleteFile

        //begin updateFile
        function updateFile(vm){
            var httpOptions = {
                method : "put",
                url : rootPath + "/fileLibrary/updateFile",
                data : vm.fileLibrary
            }

            var httpSuccess = function success(response){
                common.alert({
                    vm : vm,
                    msg : "修改成功",
                    fn : function() {
                        vm.isSubmit = false;
                        $('.alertDialog').modal('hide');
                        $('.modal-backdrop').remove();
                        $state.go('fileLibrary.fileList',{parentId : vm.parentId,fileId : ''});
                    }
                })
            }

            common.http({
                vm : vm ,
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end updateFile

        //begin fodlerById
        function  folderById(vm ,fileId){
            var httpOptions = {
                method : "get",
                url : rootPath + "/fileLibrary/findFileById",
                params : {fileId : fileId}
            }
            var httpSuccess = function success(response){
                vm.fileLibrary = response.data;
                vm.addFolderWindow();
            }

            common.http({
                vm : vm ,
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });

        }
        //end fodlerById

        //begin findFileById
        function findFileById(vm,fileId){
            var httpOptions = {
                method : "get",
                url : rootPath + "/fileLibrary/findFileById",
                params : {fileId : fileId}
            }
            var httpSuccess = function success(response){
                vm.fileLibrary = response.data;

                //初始化附件上传
                if (vm.businessFlag.isInitFileOption == false) {
                    initFileOption({
                        businessId: vm.fileLibrary.fileId,
                        sysfileType: "文件库附件",
                        uploadBt: "upload_file_bt",
                        sysMinType:$("#sysMinType").val(),
                        vm: vm
                    });
                }
            }

            common.http({
                vm : vm ,
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });

        }
        //end findFileById

        //begin saveFile
        function saveFile(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/fileLibrary/saveFile",
                    data: vm.fileLibrary
                }
                var httpSuccess = function success(response) {

                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {

                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                    $state.go('fileLibrary.fileList', {parentId: vm.parentId, fileId: ''});
                                }
                            })
                        }

                    });
                    vm.fileLibrary.fileId = response.data.fileId;
                    //初始化附件上传
                    if (vm.businessFlag.isInitFileOption == false) {
                        initFileOption({
                            businessId: vm.fileLibrary.fileId,
                            sysfileType: "文件库附件",
                            uploadBt: "upload_file_bt",
                            sysMinType: $("#sysMinType").val(),
                            vm: vm
                        });
                    }

                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }
        //end saveFile

        //begin initFolder
        function initFolder(vm){
            var httpOptions={
                method : "get",
                url : rootPath + "/fileLibrary/initFolder"
            }
            var httpSuccess = function success(response){
                common.requestSuccess({
                    vm :vm ,
                    response : response,
                    fn : function(){
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
                            vm.title = treeNode.fileUrl;
                            console.log(treeNode.fileUrl);
                            $state.go('fileLibrary.fileList',{parentId : treeNode.id,fileId : ''});
                        };
                        var zNodes = $linq(response.data).select(
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
                                    fileUrl : x.fileUrl
                                };
                            }).toArray();
                        var rootNode = {
                            id : '0',
                            name : '文件库',
                            'chkDisabled':true,
                            children : zNodes
                        };
                        zTreeObj = $.fn.zTree.init($("#fileTree"),setting,zNodes);
                        vm.folderTree = zTreeObj;
                    }
                });
            }
            common.http({
                vm : vm,
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess
            });

        }//end initFolder

        //begin saveFolder
        function saveFolder(vm){
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid && vm.fileLibrary.fileName !=undefined) {
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/fileLibrary/addFolder",
                    data: vm.fileLibrary
                };
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                    window.parent.$("#"+vm.target).data("kendoWindow").close();
                                    initFolder(vm);
                                }
                            })
                        }

                    });
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }//end saveFolder

        //begin initFileList
        function initFileList(vm){
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/fileLibrary/initFileList?fileId="+vm.parentId),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        },
                        modifiedDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: [
                    {
                        field: "issue",
                        dir: "asc"
                    }
                ]
            });
            // End:dataSource
            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.anId)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "fileName",
                    title: "文件名",
                    width: 100,
                    filterable: false
                },
                {
                    field: "",
                    title: "附件",
                    width: 300,
                    filterable: false,
                    template : function(item){
                        if(item.sysFileDtoList.length>0){
                            var sysFileDtoList = "";
                            for (var i = 0, l = item.sysFileDtoList.length; i < l; i++) {
                                sysFileDtoList += "<li>"+item.sysFileDtoList[i].showName+"</li>"
                            }
                            return sysFileDtoList;
                        }else{
                            return "";
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 150,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.del('" + item.fileId + "')", "vm.update('" + item.fileId + "')");
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true
            };
        }//end initFileList

        //S_initFileOption
        function initFileOption(option) {
            if (option.uploadBt) {
                $("#" + option.uploadBt).click(function () {
                    if (!option.businessId) {
                        common.alert({
                            vm: option.vm,
                            msg: "请先保存数据！",
                            closeDialog: true
                        })
                    } else {
                        $("#commonuploadWindow").kendoWindow({
                            width: "800px",
                            height: "500px",
                            title: "附件上传",
                            visible: false,
                            modal: true,
                            closable: true,
                            actions: ["Pin", "Minimize", "Maximize", "Close"]
                        }).data("kendoWindow").center().open();
                    }
                });
            }
            if (option.businessId) {
                //附件下载方法
                option.vm.downloadSysFile = function (id) {
                    window.open(rootPath + "/file/fileDownload?sysfileId=" + id);
                }
                //附件删除方法
                option.vm.delSysFile = function (id) {
                    var httpOptions = {
                        method: 'delete',
                        url: rootPath + "/file/deleteSysFile",
                        params: {
                            id: id
                        }
                    }
                    var httpSuccess = function success(response) {
                        common.requestSuccess({
                            vm: option.vm,
                            response: response,
                            fn: function () {
                                findFileList(option.vm);
                                common.alert({
                                    vm: option.vm,
                                    msg: "删除成功",
                                    closeDialog: true
                                })
                            }
                        });
                    }
                    common.http({
                        vm: option.vm,
                        $http: $http,
                        httpOptions: httpOptions,
                        success: httpSuccess
                    });
                }
                var projectfileoptions = {
                    language: 'zh',
                    allowedPreviewTypes: ['image'],
                    allowedFileExtensions: ['jpg', 'png', 'gif', "xlsx", "docx", "doc", "xls", "pdf", "ppt", "zip", "rar"],
                    maxFileSize: 2000,
                    showRemove: false,
                    uploadUrl: rootPath + "/file/fileUpload",
                    uploadExtraData: function(previewId, index) {
                        var result={};
                        result.businessId=option.businessId;
                        result.sysSignId="文件库附件";
                        result.sysfileType=angular.isUndefined(option.sysfileType) ? "文件库附件" : option.sysfileType;
                        result.sysMinType=$("#sysMinType option:selected").val();
                        return result;
                        // businessId: option.businessId,
                        // sysSignId:"通知公告",
                        // sysfileType: angular.isUndefined(option.sysfileType) ? "通知公告" : option.sysfileType,
                        // sysMinType :$("#sysMinType option:selected").val()
                    }
                };

                var filesCount = 0;
                $("#sysfileinput").fileinput(projectfileoptions)
                    .on("filebatchselected", function (event, files) {
                        filesCount = files.length;
                    }).on("fileuploaded", function (event, data, previewId, index) {
                    if (filesCount == (index + 1)) {
                        findFileList(option.vm);
                    }
                });
                option.vm.businessFlag.isInitFileOption = true;
            }

        }//E_initFileOption

        //S_findFileList
        function findFileList(vm,fileId) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/file/findByBusinessId",
                params: {
                    businessId: fileId
                }
            }
            var httpSuccess = function success(response) {
                vm.sysFilelists = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_findFileList
    }

})();