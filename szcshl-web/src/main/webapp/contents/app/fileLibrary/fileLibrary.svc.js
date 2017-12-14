(function () {
    'use strict';
    angular.module('app').factory('fileLibrarySvc', fileLibrary);

    fileLibrary.$inject = ['$http', '$state', '$location', 'sysfileSvc'];

    function fileLibrary($http, $state, $location, sysfileSvc) {
        var service = {
            saveRootFolder: saveRootFolder,//新建根目录文件夹
            saveChildFolder: saveChildFolder,//新建子目录
            initFileFolder: initFileFolder,//初始化文件夹
            initFileList: initFileList,//初始化文件夹下所有文件
            saveFile: saveFile,//保存文件
            findFileById: findFileById,//通过id查询文件
            updateFile: updateFile,//更新文件
            deleteFile: deleteFile,//删除文件
            deleteRootDirectory: deleteRootDirectory,//删除根目录
            folderById: folderById, //通过id查询文件夹
            queryUser: queryUser,//模糊查询
            getFileUrlById: getFileUrlById,//获取路径
        }

        return service;
        //begin getFileUrlById
        function getFileUrlById(vm, fileId) {
            var httpOptions = {
                method: "get",
                url: rootPath + "/fileLibrary/getFileUrlById",
                params: {fileId: fileId}
            }
            var httpSuccess = function success(response) {
                vm.fileUrl = response.data.fileUrl;
                vm.title = response.data.fileUrl;
                vm.initFileUpload();
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }

        //end getFileUrlById
        //查询
        function queryUser(vm) {
            vm.gridOptions.dataSource.read();
        }

        //begin deleteRootDirectory
        function deleteRootDirectory(treeId , callBack) {
            var httpOptions = {
                method: "delete",
                url: rootPath + "/fileLibrary/deleteRootDirectory",
                params: {parentFileId: treeId}
            }

            var httpSuccess = function success(response) {
                if(callBack != undefined && typeof callBack == 'function'){
                    callBack(response.data);

                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //end deleteRootDirectory


        //begin deleteFile
        function deleteFile(vm, fileId) {
            var httpOptions = {
                method: "delete",
                url: rootPath + "/fileLibrary/deleteFile",
                params: {fileId: fileId}
            }

            var httpSuccess = function success(response) {
                vm.gridOptions.dataSource.read();
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //end deleteFile

        //begin updateFile
        function updateFile(vm , callBack) {
            var httpOptions = {
                method: "put",
                url: rootPath + "/fileLibrary/updateFile",
                data: vm.fileLibrary
            }

            var httpSuccess = function success(response) {
                if(callBack != undefined && typeof  callBack == 'function'){
                    callBack(response.data);
                }
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //end updateFile

        //begin fodlerById
        function folderById(vm, fileId) {
            var httpOptions = {
                method: "get",
                url: rootPath + "/fileLibrary/findFileById",
                params: {fileId: fileId}
            }
            var httpSuccess = function success(response) {
                vm.fileLibrary = response.data;
                vm.addFolderWindow();
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }

        //end fodlerById

        //begin findFileById
        function findFileById(vm, fileId) {
            var httpOptions = {
                method: "get",
                url: rootPath + "/fileLibrary/findFileById",
                params: {fileId: fileId}
            }
            var httpSuccess = function success(response) {
                vm.fileLibrary = response.data;
                vm.fileUrl = vm.fileLibrary.fileUrl;
                vm.fileName = vm.fileLibrary.fileName;
                vm.initFileUpload();
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }

        //end findFileById

        //begin saveFile
        function saveFile(vm , callBack) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/fileLibrary/saveFile",
                    data: vm.fileLibrary
                }
                var httpSuccess = function success(response) {
                    if(callBack != undefined && typeof  callBack=="function"){
                        callBack(response.data);
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
        function initFileFolder(vm,$scope,callBack) {
            var httpOptions = {
                method: "get",
                url: rootPath + "/fileLibrary/initFileFolder",
                params : {fileType : vm.fileLibrary.fileType}
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }//end initFolder


        //begin saveRootFolder
        function saveRootFolder(fileLibrary, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/fileLibrary/addFileFolder",
                data: fileLibrary
            };
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }//end saveRootFolder

        //begin saveChildFolder
        function saveChildFolder(fileLibrary, callBack) {
            if (fileLibrary.fileName != undefined) {
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/fileLibrary/addFileFolder",
                    data: fileLibrary
                };
                var httpSuccess = function success(response) {
                    if (callBack != undefined && typeof callBack == 'function') {
                        callBack(response.data);
                    }
                };
                common.http({
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            } else {
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
        function initFileList(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/fileLibrary/initFileList?fileId=" + vm.parentId, $("#" + vm.formId)),
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
                    template: function (item) {
                        if (item.sysFileDtoList.length > 0) {
                            var sysFileDtoList = "";
                            for (var i = 0, l = item.sysFileDtoList.length; i < l; i++) {
                                sysFileDtoList += "<li>" + item.sysFileDtoList[i].showName + "</li>"
                            }
                            return sysFileDtoList;
                        } else {
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