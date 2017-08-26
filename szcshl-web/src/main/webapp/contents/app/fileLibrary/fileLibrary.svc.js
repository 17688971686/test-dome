(function(){
    'use strict';
    angular.module('app').factory('fileLibrarySvc',fileLibrary);

    fileLibrary.$inject=['$http','$state','$location','sysfileSvc'];

    function fileLibrary($http,$state,$location,sysfileSvc){
        var service={
            saveRootFolder : saveRootFolder,//新建根目录文件夹
            saveChildFolder : saveChildFolder,//新建子目录
            initFolder : initFolder ,//初始化文件夹
            initFileList : initFileList,//初始化文件夹下所有文件
            saveFile : saveFile,//保存文件
            findFileById : findFileById ,//通过id查询文件
            updateFile : updateFile,//更新文件
            deleteFile : deleteFile,//删除文件
            deleteRootDirectory : deleteRootDirectory ,//删除根目录
            folderById : folderById , //通过id查询文件夹
            queryUser : queryUser,//模糊查询
            getFileUrlById : getFileUrlById ,//获取路径
        }

        return service;
        //begin getFileUrlById
        function getFileUrlById(vm,fileId){
            var httpOptions = {
                method : "get",
                url : rootPath + "/fileLibrary/getFileUrlById",
                params : {fileId : fileId}
            }
            var httpSuccess = function success(response){
                vm.title = response.data.fileUrl;
            }

            common.http({
                vm : vm ,
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });

        }
        //end getFileUrlById
        //查询
        function queryUser(vm) {
            vm.gridOptions.dataSource.read();
        }

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
                                // initFolder(vm);
                                $state.go('fileLibrary',{},{reload:true});
                                // location.href = rootPath + "/admin/index#/fileLibrary";
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
                        $state.go('fileLibrary.fileList',{parentId : vm.parentId,fileId : vm.fileLibrary.fileId});
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
                vm.fileUrl = vm.fileLibrary.fileUrl;
                vm.fileName= vm.fileLibrary.fileName;
                vm.initFileUpload();
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
                    vm.fileId = response.data.fileId;
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
                                    // $state.go('fileLibrary.fileList', {parentId: vm.parentId, fileId: ''});
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
        }
        //end saveFile

        //begin initFolder
        function initFolder(callBack){
            var httpOptions={
                method : "get",
                url : rootPath + "/fileLibrary/initFolder"
            }
            var httpSuccess = function success(response){
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess
            });

        }//end initFolder

        //begin saveRootFolder
        function saveRootFolder(vm){
            if (vm.fileLibrary.fileName !=undefined) {
            // vm.fileLibrary.fileName !=undefined
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/fileLibrary/addFolder",
                    data: vm.fileLibrary
                };
                console.log(vm.fileLibrary);
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
                                    window.parent.$("#addRootFolder").data("kendoWindow").close();
                                    $state.go('fileLibrary',{},{reload:true});
                                    // initFolder(vm);
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
            }else{
                common.alert({
                    vm: vm,
                    msg: "文件名不能为空",
                    fn: function () {
                        $('.alertDialog').modal('hide');
                        $('.modal-backdrop').remove();
                    }
                })
            }
        }//end saveRootFolder

        //begin saveChildFolder
        function saveChildFolder(vm){
            // common.initJqValidation();
            // var isValid = $('#form').valid();
            // if (isValid) {
            if (vm.fileLibrary.fileName !=undefined) {
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
                                    window.parent.$("#addChildFolder").data("kendoWindow").close();
                                    $state.go('fileLibrary',{},{reload:true});
                                    // initFolder(vm);
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
            }else{
                common.alert({
                    vm: vm,
                    msg: "文件名不能为空",
                    fn: function () {
                        $('.alertDialog').modal('hide');
                        $('.modal-backdrop').remove();
                    }
                })
            }
        }//end saveChildFolder

        //begin initFileList
        function initFileList(vm){
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/fileLibrary/initFileList?fileId="+vm.parentId ,$("#fileLibraryForm")),
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


    }

})();