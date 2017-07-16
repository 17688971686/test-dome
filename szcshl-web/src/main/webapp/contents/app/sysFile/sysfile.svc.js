(function () {
    'use strict';

    angular.module('app').factory('sysfileSvc', sysfile);

    sysfile.$inject = ['$http'];
    function sysfile($http) {
        var service = {
            initUploadOptions: initUploadOptions,       // 初始化上传附件控件
            commonDelSysFile: commonDelSysFile,         // 删除系统文件
            commonDownloadFile: commonDownloadFile,     // 系统文件下载
        };
        return service;


        // 系统文件下载
        function commonDownloadFile(vm, id) {
            var sysfileId = id;
            window.open(rootPath + "/file/fileDownload?sysfileId=" + id);
        }

        // S 删除系统文件
        function commonDelSysFile(vm, id) {
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/file/deleteSysFile",
                params: {
                    id: id
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        window.parent.$("#commonQueryWindow").data("kendoWindow").close();
                        common.alert({
                            vm: vm,
                            msg: "删除成功",
                            closeDialog : true
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
        // E 删除系统文件

        // S 初始化上传附件控件
        /**
         * options 属性
         *  businessId: 业务ID    （必须）
         *  sysSignId: 收文ID,
         *  sysfileType: 模块,
         *  sysMinType: 文件类型,
         *  uploadBt : 上传按钮
         *  detailBt : 查看按钮
         * @param options
         */
        function initUploadOptions(options) {
            var sysFileDefaults = {
                width: "800px",
                height: "600px",
                sysMinType: "",
                sysfileType: "",
                sysSignId: "",
                uploadBt : "upload_file_bt",
                detailBt : "detail_file_bt",
                vm  : options.vm,
            };
            if(options.businessId){
                sysFileDefaults.businessId = options.businessId;
            }
            if (options.width) {
                sysFileDefaults.width = options.width;
            }
            if (options.height) {
                sysFileDefaults.height = options.height;
            }
            if (options.sysSignId) {
                sysFileDefaults.sysSignId = options.sysSignId;
            }
            if (options.sysfileType) {
                sysFileDefaults.sysfileType = options.sysfileType;
            }
            if (options.sysMinType) {
                sysFileDefaults.sysMinType = options.sysMinType;
            }
            //附件下载方法
            sysFileDefaults.vm.downloadSysFile = function(id){
                window.open(rootPath + "/file/fileDownload?sysfileId=" + id);
            }
            //附件删除方法
            sysFileDefaults.vm.delSysFile = function(id){
                var httpOptions = {
                    method: 'delete',
                    url: rootPath + "/file/deleteSysFile",
                    params: {
                        id: id
                    }
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: sysFileDefaults.vm,
                        response: response,
                        fn: function () {
                            window.parent.$("#commonQueryWindow").data("kendoWindow").close();
                            common.alert({
                                vm: sysFileDefaults.vm,
                                msg: "删除成功",
                                closeDialog : true
                            })
                        }
                    });
                }
                common.http({
                    vm: sysFileDefaults.vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }

            if (options.uploadBt) {
                sysFileDefaults.uploadBt = options.uploadBt;
                $("#"+sysFileDefaults.uploadBt).click(function(){
                	if(angular.isUndefined(options.businessId)){
                		common.alert({
                            vm: sysFileDefaults.vm,
                            msg: "请先保存业务数据！",
                            closeDialog : true
                        })
                	}else{
                		$("#commonuploadWindow").kendoWindow({
                            width: sysFileDefaults.width,
                            height: sysFileDefaults.height,
                            title: "附件上传",
                            visible: false,
                            modal: true,
                            closable: true,
                            actions: ["Pin", "Minimize", "Maximize", "Close"]
                        }).data("kendoWindow").center().open();
                	}            
                });
            }
            if (options.detailBt) {
                sysFileDefaults.detailBt = options.detailBt;
                $("#"+sysFileDefaults.detailBt).click(function(){
                	if(angular.isUndefined(options.businessId)){
                		common.alert({
                            vm: sysFileDefaults.vm,
                            msg: "请先保存业务数据！",
                            closeDialog : true
                        })
                	}else{
                		var httpOptions = {
                            method: 'get',
                            url: rootPath + "/file/findByBusinessId",
                            params: {
                                businessId: sysFileDefaults.businessId
                            }
                        }
                        var httpSuccess = function success(response) {
                            sysFileDefaults.vm.sysFilelists = response.data;
                            $("#commonQueryWindow").kendoWindow({
                                width: "800px",
                                height: "400px",
                                title: "附件上传列表",
                                visible: false,
                                modal: true,
                                closable: true,
                                actions: ["Pin", "Minimize", "Maximize", "Close"]
                            }).data("kendoWindow").center().open();
                        }
                        common.http({
                            vm: sysFileDefaults.vm,
                            $http: $http,
                            httpOptions: httpOptions,
                            success: httpSuccess
                        });
                	}                   
                });
            }
            //有业务数据才能初始化
            if(sysFileDefaults.businessId){
                var projectfileoptions = {
                    language: 'zh',
                    allowedPreviewTypes: ['image'],
                    allowedFileExtensions: ['jpg', 'png', 'gif', "xlsx", "docx", "doc", "xls", "pdf","ppt","zip","rar"],
                    maxFileSize: 2000,
                    showRemove: false,
                    uploadUrl: rootPath + "/file/fileUpload",
                    uploadExtraData: {
                        businessId: sysFileDefaults.businessId,
                        sysSignId: sysFileDefaults.sysSignId,
                        sysfileType: sysFileDefaults.sysfileType,
                        sysMinType: sysFileDefaults.sysMinType
                    }
                };
                $("#sysfileinput").fileinput(projectfileoptions)
                    .on("filebatchselected", function (event, files) {

                    }).on("fileuploaded", function (event, data) {
                });
            }

        }
        // E 初始化上传附件控件
    }
})();