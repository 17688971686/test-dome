(function () {
    'use strict';

    angular.module('app').factory('sharingPlatlformSvc', sharingPlatlform);

    sharingPlatlform.$inject = ['$http'];

    function sharingPlatlform($http) {
        var url_sharingPlatlform = rootPath + "/sharingPlatlform", url_back = '#/sharingPlatlform';

        var service = {
            grid: grid,
            getSharingPlatlformById: getSharingPlatlformById,
            createSharingPlatlform: createSharingPlatlform,
            deleteSharingPlatlform: deleteSharingPlatlform,
            initFileOption: initFileOption,					//初始化上传控件
            findFileList: findFileList,					    //系统附件列表
            getSharingDetailById: getSharingDetailById,	    //获取详情页
            downloadSysfile: downloadSysfile,		        //下载
            initOrgAndUser: initOrgAndUser,                 //初始化部门和用户
            initSeleObj : initSeleObj,                      //初始化选择的用户
            updatePublish : updatePublish,                  //批量更改发布状态
        };

        return service;

        //S_初始化选择的用户
        function initSeleObj(vm){
            if((vm.shareOrgList && vm.shareOrgList.length > 0)){
                //1、先计算选择的部门
                if(vm.model.privilegeDtoList && vm.model.privilegeDtoList.length > 0){
                    vm.shareOrgList.forEach(function (so,i){
                        vm.model.privilegeDtoList.forEach(function (p,index) {
                            if(p.businessType == 1 || p.businessType == "1"){
                                if(p.businessId == so.id){
                                    so.isChecked = true;
                                    so.userDtos.forEach(function (u,k){
                                        u.isDisabled = true;
                                    });
                                }
                            }else if(p.businessType == 2 || p.businessType == "2"){
                                so.userDtos.forEach(function (u,k){
                                    if(p.businessId == u.id){
                                        u.isChecked = true;
                                    }
                                });
                            }
                        })

                    });
                }

                if(vm.noOrgUsetList && vm.noOrgUsetList.length > 0){
                    vm.noOrgUsetList.forEach(function (nu,i){
                        vm.model.privilegeDtoList.forEach(function (p,index) {
                             if( (p.businessType == 2 || p.businessType == "2") && p.businessId == nu.id){
                                 nu.isChecked = true;
                            }
                        })
                    });
                }
                vm.businessFlag.isInitSeled = true;
            }
        }//E_initSeleObj

        //S_初始化部门和用户
        function initOrgAndUser(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sharingPlatlform/initOrgAndUser",
            };
            var httpSuccess = function success(response) {
                vm.shareOrgList = response.data.orgDtoList;
                vm.noOrgUsetList = response.data.noOrgUserList;
                if(vm.businessFlag.isUpdate && !vm.businessFlag.isInitSeled){
                    initSeleObj(vm);
                }
            };
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_initOrgAndUser

        //下载
        function downloadSysfile(id) {
            if (id) {
                window.open(rootPath + "/file/fileDownload?sysfileId=" + id);
            }
        }

        //S 详情页面
        function getSharingDetailById(vm, id) {
            var httpOptions = {
                method: 'get',
                url: url_sharingPlatlform + "/html/sharingDeatilById",
                params: {id: id}
            };
            var httpSuccess = function success(response) {
                vm.model = response.data;
            };
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //E 详情页面


        //S_findFileList
        function findFileList(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/file/findByBusinessId",
                params: {
                    businessId: vm.model.sharId
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
                            width: 700,
                            height: 400,
                            title: "附件上传",
                            visible: false,
                            modal: true,
                            closable: true,
                            actions: ["Pin", "Minimize", "Maximize", "Close"]
                        }).data("kendoWindow").center().open();
                    }
                });

                if (option.businessId) {
                    //附件下载方法
                    option.vm.downloadSysFile = function (id) {
                        window.open(rootPath + "/file/fileDownload?sysfileId=" + id);
                    }
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
                    allowedFileExtensions: ['jpg', 'png', 'gif', "xlsx", "docx", "doc", "xls", "pdf", "ppt", "zip", "rar","txt"],
                    maxFileSize: 2000,
                    showRemove: false,
                    uploadUrl: rootPath + "/file/fileUpload",
                    uploadExtraData: {
                        businessId: option.businessId,
                        sysSignId:"共享平台",
                        sysfileType: angular.isUndefined(option.sysfileType) ? "共享平台" : option.sysfileType,
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

        // begin#deleteSharingPlatlform
        function deleteSharingPlatlform(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_sharingPlatlform + "/sharingDelete",
                data: id
            };

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: "操作成功",
                            closeDialog: true,
                            fn: function () {
                                vm.isSubmit = false;
                                vm.gridOptions.dataSource.read();
                            }
                        })
                    }
                });
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#createSharingPlatlform
        function createSharingPlatlform(vm) {
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                //如果不是全员可看，则要获取选择的部门和用户
                if(vm.model.isNoPermission != 9){
                    vm.model.privilegeDtoList = [];
                    //1、先计算选择的部门
                    var oCheck = $("input[name='shareOrg']:checked");
                    if (oCheck.length > 0) {
                        for (var i = 0; i < oCheck.length; i++) {
                            var shareOrg = {};
                            shareOrg.businessId = oCheck[i].value;
                            shareOrg.businessType = "1";
                            vm.model.privilegeDtoList.push(shareOrg);
                        }
                    }
                    //2、再选择人员
                    var uCheck = $("input[name='shareUser']:not(:disabled):checked");
                    if (uCheck.length > 0) {
                        for (var i = 0; i < uCheck.length; i++) {
                            var shareUser = {};
                            shareUser.businessId = uCheck[i].value;
                            shareUser.businessType = "2";
                            vm.model.privilegeDtoList.push(shareUser);
                        }
                    }

                }
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/sharingPlatlform/saveSharing",
                    data: vm.model
                };

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            vm.isSubmit = false;
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                closeDialog: true
                            })
                        }
                    });
                    vm.model.sharId = response.data.sharId;
                    //初始化附件上传
                    initFileOption({
                        businessId: vm.model.sharId,
                        sysfileType: "共享平台",
                        uploadBt: "upload_file_bt",
                        vm: vm
                    });
                };

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess,
                    onError:function () {
                        vm.isSubmit = false;
                    }
                });

            }
        }

        // begin#getSharingPlatlformById
        function getSharingPlatlformById(vm) {
            var httpOptions = {
                method: 'get',
                url: url_sharingPlatlform + "/html/findById",
                params: {id: vm.model.sharId}
            };
            var httpSuccess = function success(response) {
                vm.model = response.data;
                if(!vm.businessFlag.isInitSeled){
                    initSeleObj(vm);
                }
                initFileOption({
                    businessId: vm.model.sharId,
                    sysfileType: "共享平台",
                    uploadBt: "upload_file_bt",
                    vm: vm
                });
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#grid
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_sharingPlatlform + "/findByCurUser", $("#formSharing")),
                schema: common.kendoGridConfig().schema({
                    id: "sharId",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            // End:dataSource
            //S_序号
            var dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            };
            //S_序号
            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.sharId)
                    },
                    filterable: false,
                    width: 30,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "unitSort",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "theme",
                    title: "主题",
                    width: 180,
                    filterable: false
                },
                {
                    field: "publishUsername",
                    title: "发布人",
                    width: 120,
                    filterable: false
                },
                {
                    field: "publishDate",
                    title: "发布时间",
                    format: "{0:yyyy-MM-dd hh24:mm:ss}",
                    width: 180,
                    filterable: false
                },
                {
                    field: "isPublish",
                    title: "发布状态",
                    width: 100,
                    template: function (item) {
                       if(item.isPublish && item.isPublish == 9){
                           return "已发布";
                       }else{
                           return "未发布";
                       }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 180,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            item.sharId, item.sharId, "vm.del('" + item.sharId + "')");
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                dataBound: dataBound,
                columns: columns,
                resizable: true
            };

        }// end fun grid


        //S_批量发布
        function updatePublish(vm,isUpdate){
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: "请选择要批量发布的数据"
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/sharingPlatlform/updatePublish",
                    params: {
                        ids: ids.join(','),
                        status: (isUpdate == true)?'9':'0'
                    }
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            vm.isSubmit = false;
                            vm.gridOptions.dataSource.read();
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                closeDialog: true
                            })
                        }
                    })
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }//E_bathPublish

    }
})();