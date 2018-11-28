(function () {
    'use strict';

    angular.module('myApp').factory("projectManagerSvc", projectManagerSvc);

    projectManagerSvc.$inject = ["$http", "bsWin", "$state"];

    function projectManagerSvc($http, bsWin, $state) {
        var url_management = util.formatUrl("project");
        var url_user = util.formatUrl("sys/user");
        var attachments_url = util.formatUrl("sys/sysfile");

        return {
            bsTableControlForManagement: function (vm, searchUrl, filter) {
                vm.bsTableControlForManagement = {
                    options: util.getTableFilterOption({
                        queryParams: function (params) {
                           var filters = params.filter;
                            var me = this,
                                _params = {
                                    "$skip": params.offset,
                                    "$top": params.limit,
                                    "$orderby": !params.sort ? me.defaultSort : (params.sort + " " + params.order),
                                    "$filter": filters.length ==0 ? "" :$.toOdataFilter({
                                        logic:"and",
                                        filters:filters
                                 })
                                };
                            if (me.pagination) {
                                _params["$inlinecount"] = "allpages";
                            }
                            vm.tableParams = _params;
                            return _params;
                        },
                        url: url_management + ("/proInfo" || ""),
                        defaultFilters: filter,
                        columns: [{
                            title: '序号',
                            switchable: false,
                            align: "center",
                            width: 50,
                            formatter: function (value, row, index) {
                                var state = vm.bsTableControlForManagement.state;
                                if (state.pageNumber && state.pageSize) {
                                    return index + 1 + (state.pageNumber - 1) * state.pageSize;
                                } else {
                                    return index + 1;
                                }
                            }
                        },{
                            field: 'fileCode',
                            title: '收文编号',
                            width: 100,
                            filterControl: 'input',
                            filterOperator: "like"
                        },{
                            field: 'projectName',
                            title: '项目名称',
                            width: 200,
                            sortable: false,
                            filterControl: "input",
                            filterOperator: "like",
                            formatter: '<a href="#/projectManageView/{{row.id}}/view" style="color:blue">{{row.projectName}}</a>'
                        }, {
                            field: 'reviewStage',
                            title: '评审阶段',
                            width: 100,
                            filterControl: 'input',
                        }, {
                            field: 'proUnit',
                            title: '项目单位',
                            width: 90,
                            filterControl: 'input',
                            filterOperator: "like",
                        }, {
                            field: 'mainOrgName',
                            title: '主办部门',
                            width: 90,
                            filterControl: 'input',
                        }, , {
                            field: 'assistOrgName',
                            title: '协办部门',
                            width: 220,
                            filterControl: 'input',
                        }, {
                            field: 'mainUserName',
                            title: '第一负责人',
                            width: 90,
                            filterControl: 'input'
                        }, {
                            field: 'assistUserName',
                            title: '其他负责人',
                            width: 90,
                            filterControl: 'input'
                        }, {
                            field: 'dispatchDate',
                            title: '发文日期',
                            width: 90,
                            filterControl: 'input'
                        }, {
                            field: 'fileNum',
                            title: '发文号',
                            width: 90,
                            filterControl: 'input',
                            filterOperator: "like",
                        }, {
                            field: 'fileDate',
                            title: '存档日期',
                            width: 90,
                            filterControl: 'input'
                        },{
                            field: 'fileNo',
                            title: '存档号',
                            width: 90,
                            filterControl: 'input',
                            filterOperator: "like",
                        },{
                            field: 'remark',
                            title: '备注',
                            width: 90,
                            filterControl: 'input',
                            filterOperator: "like"
                        },{
                            field: 'id',
                            title: '操作',
                            width: 240,
                            formatter: $("#columnBtns").html()
                        }]
                    })
                }
            },
            rsTableControl : function rsTableControl(vm) {
                vm.rsTableControl = {
                    options: util.getTableOption({
                        url: url_management + ("/proInfo" || ""),
                        defaultSort: "createdDate desc",
                        filterForm:"#filterForm",
                        columns: [{
                            title: '序号',
                            switchable: false,
                            width: 50,
                            formatter: function (value, row, index) {
                                var state = vm.rsTableControl.state;
                                if (state.pageNumber && state.pageSize) {
                                    return index + 1 + (state.pageNumber - 1) * state.pageSize;
                                } else {
                                    return index + 1
                                }
                            }
                        },{
                            field: 'fileCode',
                            title: '收文编号',
                            width: 100,
                        },{
                            field: 'projectName',
                            title: '项目名称',
                            width: 200,
                            sortable: false,
                            formatter: '<a href="#/projectManageView/{{row.id}}/view" style="color:blue">{{row.projectName}}</a>'
                        }, {
                            field: 'reviewStage',
                            title: '评审阶段',
                            width: 100,
                        }, {
                            field: 'proUnit',
                            title: '项目单位',
                            width: 90,
                        }, {
                            field: 'mainOrgName',
                            title: '主办部门',
                            width: 90,
                            filterControl: 'input',
                        }, {
                            field: 'assistOrgName',
                            title: '协办部门',
                            width: 220,
                            filterControl: 'input',
                        }, {
                            field: 'mainUserName',
                            title: '第一负责人',
                            width: 90,
                            filterControl: 'input'
                        }, {
                            field: 'assistUserName',
                            title: '其他负责人',
                            width: 90,
                            filterControl: 'input'
                        },{
                            field: 'dispatchDate',
                            title: '发文日期',
                            width: 90,
                        }, {
                            field: 'fileNum',
                            title: '发文号',
                            width: 90,

                        }, {
                            field: 'fileDate',
                            title: '存档日期',
                            width: 90,

                        },{
                            field: 'fileNo',
                            title: '存档号',
                            width: 90,

                        },{
                            field: 'remark',
                            title: '备注',
                            width: 90,

                        },{
                            field: 'id',
                            title: '操作',
                            width: 240,
                            formatter: $("#columnBtns").html()
                        }
                            ]
                    })
                };
            },

            bsTableCancelManagement: function (vm, searchUrl, filter) {
                {
                    vm.bsTableCancelManagement = {
                        options: util.getTableOption({
                            url: url_management + ("/cancelInfo" || ""),
                            defaultSort: "createdDate desc",
                            filterForm:"#filterForm",
                            columns: [{
                                title: '序号',
                                switchable: false,
                                width: 50,
                                formatter: function (value, row, index) {
                                    var state = vm.bsTableCancelManagement.state;
                                    if (state.pageNumber && state.pageSize) {
                                        return index + 1 + (state.pageNumber - 1) * state.pageSize;
                                    } else {
                                        return index + 1
                                    }
                                }
                            },{
                                field: 'fileCode',
                                title: '收文编号',
                                width: 100,
                            },{
                                field: 'projectName',
                                title: '项目名称',
                                width: 200,
                                sortable: false,
                                formatter: '<a href="#/projectManageView/{{row.id}}/view" style="color:blue">{{row.projectName}}</a>'
                            }, {
                                field: 'reviewStage',
                                title: '评审阶段',
                                filterControl: 'dict',
                                filterData: 'DICT.REVIEWSTAGE.dicts.PRO_STAGE',
                                width: 100,
                            }, {
                                field: 'proUnit',
                                title: '项目单位',
                                width: 90,
                            },{
                                field: 'mainOrgName',
                                title: '主办部门',
                                width: 90,
                                filterControl: 'input',
                            }, {
                                field: 'assistOrgName',
                                title: '协办部门',
                                width: 220,
                                filterControl: 'input',
                            }, {
                                field: 'mainUserName',
                                title: '第一负责人',
                                width: 90,
                                filterControl: 'input'
                            }, {
                                field: 'assistUserName',
                                title: '其他负责人',
                                width: 90,
                                filterControl: 'input'
                            }, {
                                field: 'dispatchDate',
                                title: '发文日期',
                                width: 90,
                            }, {
                                field: 'fileNum',
                                title: '发文号',
                                width: 90,

                            }, {
                                field: 'fileDate',
                                title: '存档日期',
                                width: 90,

                            },{
                                field: 'fileNo',
                                title: '存档号',
                                width: 90,

                            },{
                                field: 'remark',
                                title: '备注',
                                width: 90,

                            },{
                                field: 'id',
                                title: '操作',
                                width: 240,
                                formatter: $("#columnBtns").html()
                            }
                            ]
                        })
                    };
                }
            },
            //根据id查找小型投资项目
            findGovernmentInvestProjectById: function (vm, fn) {
                $http.get(url_management + "/findById", {params: {"id": vm.model.id || ""}}).then(function (response) {
                    vm.model = response.data;
                    if (fn) {
                        fn();
                    }
                });
            },
            //创建政府投资项目
            createGovernmentInvestProject: function (vm) {
                $http.post(url_management, vm.model).then(function () {
                        bsWin.success("创建成功");
                        $state.go("projectManage");

                });

            },
            //根据id删除项目
            deleteGovernmentInvestProject: function (id) {
                $http["delete"](url_management, {params: {"id": id || ""}}).then(function () {
                    bsWin.success("删除成功");
                    $("#listTable").bootstrapTable('refresh');//刷新表格数据
                });
            },
            //更新项目
            updateGovernmentInvestProject: function (vm) {
                $http.put(url_management, vm.model).then(function () {
                    bsWin.success("更新成功");
                });
            },
            //恢复项目
            restoreInvestProject: function (vm) {
                $http.post(url_management + "/restore", vm.model).then(function () {
                    bsWin.success("项目恢复成功");
                    $("#listTable").bootstrapTable('refresh');//刷新表格数据
                });

            },
            //作废项目
            cancelInvestProject: function (vm) {
                $http.post(url_management + "/cancel", vm.model).then(function () {
                    bsWin.success("项目作废成功");
                    $("#listTable").bootstrapTable('refresh');//刷新表格数据
                });

            },
            createProReport: function(vm) {
                window.open(url_management + "/exportPro2?$filter=" + vm.tableParams.$filter +"and status eq '"+vm.status+"' &$orderby="+ vm.tableParams.$orderby);
        },

            /**
             * 查询附件列表
             * @param vm
             * @param params
             * @param fn
             */
            getAttachments: function (vm, params, fn) {
                $http.get(attachments_url + "/findByBusinessId", {params: params}).success(function (data) {
                    fn(data);
                });
            },
            /**
             * 初始化附件上传
             * @param vm
             * @param id
             * @param fn
             */
            initUploadConfig: function (vm, id, fn) {
                $("#" + id).uploadify({
                    uploader: util.formatUrl('sys/sysfile/fileUpload'),
                    swf: util.formatUrl("libs/uploadify/uploadify.swf"),
                    buttonText: '相关附件',
                    method: 'post',
                    multi: true,
                    auto: true,//自动上传
                    fileObjName: 'files',// 上传参数名称
                    fileSizeLimit: "40MB",//上传文件大小限制
                    fileExt: '*.pdf;*.txt;*.png;*.jpg;*.doc;*.docx;*.wps',
                    fileTypeExts: '*.pdf;*.txt;*.png;*.jpg;*.doc;*.docx;*.wps',
                    fileTypeDesc: "请选择*.pdf;*.txt;*.png;*.jpg;*.doc;*.docx;*.wps文件",     // 文件说明
                    removeCompleted: true,
                    onUploadStart: function (file) {
                        $('#relateAttach').uploadify("settings", "formData", {
                            "businessId": vm.model.id ? vm.model.id : vm.UUID,
                            "mainType" : 'ProAttachment'
                        });
                    },
                    onUploadSuccess: function (file, data, response) {
                        fn(data);
                        angular.element("body").scope().$apply(function () {
                            bsWin.success("上传成功")
                        });
                    },
                    onCancel: function (file) {
                        bsWin.confirm("询问提示", "确认删除该文件吗？", function () {
                        });
                    },
                    onUploadError: function (file, errorCode, errorMsg, errorString) {
                        angular.element("body").scope().$apply(function () {
                            switch (errorCode) {
                                case -100:
                                    bsWin.error("上传的文件数量已经超出系统限制的文件！");
                                    break;
                                case -110:
                                    bsWin.error("文件 [" + file.name + "] 大小超出系统限制的大小！");
                                    break;
                                case -120:
                                    bsWin.error("文件 [" + file.name + "] 大小异常！");
                                    break;
                                case -130:
                                    bsWin.error("文件 [" + file.name + "] 类型不正确！");
                                    break;
                                default:
                                    bsWin.error("上传失败");
                                    break;
                            }
                        });
                    }
                });
            },
            // S 初始化上传附件控件
            /**
             * options 属性 options.vm.sysFile 一定要有，这个是附件对象
             *  uploadBt : 上传按钮
             *  detailBt : 查看按钮
             *  inputId : "sysfileinput",
             *  mainType : 主要业务模块，业务的根目录
             * @param options
             */
            initUploadOptions: function (options) {
            options.vm.initUploadOptionSuccess = false;
            //options.vm.sysFile 为定义好的附件对象
            var sysFileDefaults = {
                width: "70%",
                height: "460px",
                uploadBt: "upload_file_bt",
                detailBt: "detail_file_bt",
                inputId: "sysfileinput",
                mainType: "ProAttachment",
                sysBusiType: "",
                showBusiType: true,
            };
            if (!options.vm.sysFile) {
                bsWin.alert("初始化附件控件失败，请先定义附件对象！");
                return;
            }
            if (options.sysBusiType) {
                sysFileDefaults.sysBusiType = options.sysBusiType;
            }
            if (options.width) {
                sysFileDefaults.width = options.width;
            }
            if (options.height) {
                sysFileDefaults.height = options.height;
            }

            //是否显示业务下来框
            if (angular.isUndefined(options.vm.sysFile.showBusiType)) {
                options.vm.sysFile.showBusiType = sysFileDefaults.showBusiType;
            }

                //附件下载方法
                options.vm.downloadSysFile = function (id) {
                    downloadFile(id);
                }
                //附件删除方法
                options.vm.delSysFile = function (id) {
                    bsWin.confirm({
                        title: "询问提示",
                        message: "确认删除么？",
                        onOk: function () {
                            delSysFile(id, function (data) {
                                bsWin.alert(data.reMsg || "删除成功！");
                                $.each(options.vm.sysFilelists, function (i, sf) {
                                    if (!angular.isUndefined(sf) && sf.sysFileId == id) {
                                        options.vm.sysFilelists.splice(i, 1);
                                    }
                                })
                            });
                        }
                    });
                }

            options.vm.clickUploadBt = function () {
                if (!options.vm.sysFile.businessId) {
                    bsWin.alert("请先保存业务数据！");
                } else {
                    //B、清空上一次的上传文件的预览窗口
                    options.vm.sysFile.sysBusiType="";
                    //E、清空上一次的上传文件的预览窗口
                    angular.element('#sysfileinput').fileinput('clear');
                    $("#commonUploadWindow").kendoWindow({
                        width: sysFileDefaults.width,
                        height: sysFileDefaults.height,
                        title: "附件上传",
                        visible: false,
                        modal: true,
                        closable: true,
                    }).data("kendoWindow").center().open();
                }
            }

                options.vm.clickDetailBt = function () {
                    if (!options.vm.sysFile.businessId) {
                        bsWin.alert("请先保存业务数据！");
                        return;
                    } else {
                        findByBusinessId(options.vm.sysFile.businessId, function (data) {
                            options.vm.sysFilelists = [];
                            options.vm.sysFilelists = data;
                            $("#commonQueryWindow").kendoWindow({
                                width: "50%",
                                height: "500px",
                                title: "附件上传列表",
                                visible: false,
                                modal: true,
                                closable: true,
                                actions: ["Pin", "Minimize", "Maximize", "Close"]
                            }).data("kendoWindow").center().open();
                        });
                    }
                }

                //有业务数据才能初始化
                if (options.vm.sysFile.businessId) {
                    var projectfileoptions = {
                        language: 'zh',
                        allowedPreviewTypes: ['image'],
                        allowedFileExtensions: ['sql', 'exe', 'lnk'],//修改过，改为了不支持了。比如不支持.sql的
                        maxFileSize: 0,     //文件大小不做限制
                        showRemove: false,
                        previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
                        uploadAsync: false, //同步上传
                        enctype : 'multipart/form-data',
                        uploadUrl: attachments_url+"/fileUpload",// 默认上传ftp服务器 /file/fileUploadLocal 为上传到本地服务
                        previewFileIconSettings: {
                            'doc': '<i class="fa fa-file-word-o text-primary"></i>',
                            'xls': '<i class="fa fa-file-excel-o text-success"></i>',
                            'ppt': '<i class="fa fa-file-powerpoint-o text-danger"></i>',
                            'docx': '<i class="fa fa-file-word-o text-primary"></i>',
                            'xlsx': '<i class="fa fa-file-excel-o text-success"></i>',
                            'pptx': '<i class="fa fa-file-powerpoint-o text-danger"></i>',
                            'pdf': '<i class="fa fa-file-pdf-o text-danger"></i>',
                            'zip': '<i class="fa fa-file-archive-o text-muted"></i>',
                        },
                        uploadExtraData: function (previewId, index) {
                            var result = {};
                            result.businessId = options.vm.sysFile.businessId;
                            result.mainId = options.vm.sysFile.mainId || "";
                            result.mainType = options.vm.sysFile.mainType || sysFileDefaults.mainType;
                            result.sysfileType = options.vm.sysFile.sysfileType || "";
                            result.sysBusiType = options.vm.sysFile.sysBusiType || sysFileDefaults.sysBusiType;
                            return result;
                        }
                    };

                    var filesCount = 0;
                    $("#" + options.inputId || sysFileDefaults.inputId).fileinput(projectfileoptions)
                    //附件选择
                        .on("filebatchselected", function (event, files) {
                            filesCount = files.length;
                            //console.log("附件选择:" + filesCount);
                        })
                        //上传前
                        .on('filepreupload', function (event, data, previewId, index) {
                            var form = data.form, files = data.files, extra = data.extra,
                                response = data.response, reader = data.reader;
                            //console.log("附件上传前:" + files);
                        })
                        /*//异步上传返回结果处理
                         .on("fileuploaded", function (event, data, previewId, index) {
                         projectfileoptions.sysBusiType = options.vm.sysFile.sysBusiType;
                         if (filesCount == (index + 1)) {
                         if (options.uploadSuccess != undefined && typeof options.uploadSuccess == 'function') {
                         options.uploadSuccess(event, data, previewId, index);
                         }
                         }
                         })*/
                        //同步上传错误处理
                        .on('filebatchuploaderror', function(event, data, msg) {
                            console.log("同步上传错误");
                            // get message
                            //alert(msg);
                        })
                        //同步上传返回结果处理
                        .on("filebatchuploadsuccess", function (event, data, previewId, index) {
                            if (options.uploadSuccess != undefined && typeof options.uploadSuccess == 'function') {
                                options.uploadSuccess(event, data, previewId, index);
                            }
                        });

                    //表示初始化控件成功
                    options.vm.initUploadOptionSuccess = true;
                }


        },
        // E 初始化上传附件控件
            /**
             * 获取UUID:附件上传id
             * @param vm
             */
            createUUID: function (vm) {
                $http.get(url_management + "/createUUID").success(function (data) {
                    vm.UUID = data || [];
                });
            },
            deleteFileById: function (fileId, vm, fn) {
                $http['delete'](attachments_url, {params: {"sysFileId": fileId || ""}}).then(function () {
                    bsWin.success("删除成功");
                    angular.isFunction(fn) && fn();
                }).then(function () {
                });
            },
            findOrgUser: function (fn) {
                $http.get(url_user + "/findUsersByOrgId").success(function (data) {
                    fn(data)
                });
            },
            findOrganUser: function (fn) {
                $http.get(url_user + "/findOrgUser").success(function (data) {
                    fn(data)
                });
            },
            findAllOrgDelt : function(fn){
                $http.get("sys/organ/findAllOrgDept").success(function (data) {
                    fn(data)
                });
            },
            //验证是否是项目负责人
            checkProPriUser:function(projId, callBack) {
            $http.get(url_management + "/checkProPriUser?id="+projId).success(function (data) {
                callBack(data);
            });
        }
        }

        //根据主业务获取所有的附件信息
        function findByBusinessId(businessId, callBack) {
            $http.get(attachments_url + "/findByBusinessId?businessId="+businessId).success(function (data) {
                callBack(data);
            });
        }

        // 系统文件下载
        function downloadFile(id) {
            $http.get(attachments_url + "/fileSysCheck?sysFileId="+id).success(function (response) {
                var downForm = $("#szecSysFileDownLoadForm");
                downForm.attr("target","");
                downForm.attr("method","get");
                if (response.flag || response.reCode == 'ok') {
                    downForm.attr("action",attachments_url + "/fileDownload");
                    downForm.find("input[name='sysfileId']").val(id);
                    downForm.submit();//表单提交
                } else {
                    downForm.attr("action","");
                    downForm.find("input[name='sysfileId']").val("");
                    bsWin.error(response.reMsg);
                }
            });

        }


        // S 删除系统文件,自己实现回调方法
        function delSysFile(sysFileId, callBack) {
            $http.get(attachments_url + "/deleteSysFile?sysFileId="+sysFileId).success(function (data) {
                callBack(data);
            });
        }

    }

})();