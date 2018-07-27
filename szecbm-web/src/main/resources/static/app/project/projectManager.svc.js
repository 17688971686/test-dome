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
                            filterControl: 'dict',
                            filterData: 'DICT.REVIEWSTAGE.dicts.PRO_STAGE'
                        }, {
                            field: 'proUnit',
                            title: '项目单位',
                            width: 90,
                            filterControl: 'input',
                            filterOperator: "like",
                        }, {
                            field: 'reviewDept',
                            title: '评审部门',
                            width: 90,
                            filterControl: 'dict',
                            filterData: 'DICT.DEPT.dicts.TRANSACT_DEPARTMENT'
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
                 /*       queryParams: function (params) {
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
                        },*/
                        url: url_management + ("/proInfo" || ""),
                        defaultSort: "createdDate desc",
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
                            filterControl: 'dict',
                            filterData: 'DICT.REVIEWSTAGE.dicts.PRO_STAGE',
                            width: 100,
                        }, {
                            field: 'proUnit',
                            title: '项目单位',
                            width: 90,
                        }, {
                            field: 'reviewDept',
                            title: '评审部门',
                            width: 90,
                            filterControl: 'dict',
                            filterData: 'DICT.DEPT.dicts.TRANSACT_DEPARTMENT'
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
                            }, {
                                field: 'reviewDept',
                                title: '评审部门',
                                width: 90,
                                filterControl: 'dict',
                                filterData: 'DICT.DEPT.dicts.TRANSACT_DEPARTMENT'
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
            }
        }
    }

})();