(function () {
    'use strict';

    angular.module('myApp', ['ui.router', 'kendo.directives','angular-loading-bar', "bsTable", "sn.common"]).config(appConfig).run(appRun).controller('indexCtrl', indexCtrl);

    indexCtrl.$inject = ['$scope', '$state', '$http', '$compile', "snBaseUtils"];

    // 系统主控制器
    function indexCtrl($scope, $state, $http, $compile, snBaseUtils) {
        var topMenuBox = $("#topMenuBox"),
            leftMenuBox = $("#leftMenuBox");

        // 状态切换开始时触发事件，销毁uploadify对象
        $scope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
            if (event) {
                $('.uploadify').each(function () {
                    $(this).uploadify('destroy');
                })
            }
        });
        $scope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
            $scope.previousState_name = fromState.name;
            $scope.previousState_params = fromParams;
        });
        //实现返回前一页的函数
        $scope.backPrevPage = function (toState) {
            toState = toState || "welcome";
            $state.go($scope.previousState_name || toState, $scope.previousState_params);
        };

        $scope.csHide = function (param) {
            if (param && param != $scope.menuBoxResId) {
                if ($("#menu_" + param).length > 0) {
                    $scope.menuBoxResId = param;
                } else {
                    $scope.menuBoxResId = "pm";
                }
            }
        }

        //  初始化系统菜单
        snBaseUtils.initMenus($scope, function (sysLeftMenusTpl) {
            if (!$scope.menuBoxResId) {
                $scope.menuBoxResId = "pm";
            }
            //topMenuBox.append($compile(sysTopMenusTpl)($scope));
            leftMenuBox.append($compile(sysLeftMenusTpl)($scope));
        });

        // 初始化数据字典
        snBaseUtils.initDicts($scope);
    }

    appConfig.$inject = ['$urlRouterProvider', 'cfpLoadingBarProvider', '$compileProvider'];

    // 基础配置
    function appConfig($urlRouterProvider, cfpLoadingBarProvider, $compileProvider) {
        cfpLoadingBarProvider.parentSelector = '#loading-bar-container';
        cfpLoadingBarProvider.spinnerTemplate = '<div style="position:fixed;width:100%;height:100%;left:0;top:0; z-index:99;background:rgba(0, 0, 0, 0.3);overflow: hidden;"><div style="position: absolute;top:30%; width: 400px;height:40px;left:50%;"><i class="fa fa-spinner fa-pulse fa-1x fa-fw"></i>程序处理中...</div></div>';

        $urlRouterProvider.otherwise("/welcome");

        // 用于解决a标签动态href出现unsafe的问题
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|file|javascript):/);
    }

    appRun.$inject = ['$rootScope'];

    // 系统执行方法
    function appRun($rootScope) {
        $rootScope.$on('$viewContentLoaded', function (event) {
            if (jQuery.AdminLTE.layout) {  // 避免AdminLTE未初始化完成报错
                // 解决angular加载模块页面时，影响AdminLTE布局的问题
                jQuery.AdminLTE.layout.fix();
                jQuery.AdminLTE.layout.fixSidebar();
            }
        });
    }

    //===============回到顶部的按钮=============================
    var slideToTop = $('<div><i class="fa fa-chevron-up"></i></div>')
        .css({
            position: 'fixed',
            bottom: '20px',
            right: '25px',
            width: '40px',
            height: '40px',
            color: '#eee',
            'font-size': '',
            'line-height': '40px',
            'text-align': 'center',
            'background-color': '#222d32',
            cursor: 'pointer',
            'border-radius': '5px',
            'z-index': '99999',
            opacity: '.7',
            'display': 'none'
        }).on({
            'mouseenter': function () {
                slideToTop.css('opacity', '1');
            },
            'mouseout': function () {
                slideToTop.css('opacity', '.7');
            },
            "click": function () {
                $("html,body").animate({"scrollTop": top}, 500)
            }
        });
    $('.wrapper').append(slideToTop);
    $(window).scroll(function () {
        if ($(window).scrollTop() >= 150) {
            if (!slideToTop.is(':visible')) {
                slideToTop.fadeIn(500);
            }
        } else {
            slideToTop.fadeOut(500);
        }
    });
})();

(function () {
    'use strict';

    angular.module('myApp').config(proCancelConfig);

    proCancelConfig.$inject = ["$stateProvider"];

    function proCancelConfig($stateProvider) {
        //项目管理列表页
        $stateProvider.state('projectManageCancel', {
            url: "/projectManageCancel",
            controllerAs: "vm",
            templateUrl: util.formatUrl('project/html/cancel'),
            controller: projectManagerCancelCtrl
        });
    }

    projectManagerCancelCtrl.$inject = ["$scope","projectManagerSvc","bsWin","$state"];

    function projectManagerCancelCtrl($scope,projectManagerSvc,bsWin,$state) {
        $scope.csHide("cjgl");
        var vm = this;
        vm.model = {};
        vm.tableParams = {};

        //获取项目列表
        projectManagerSvc.bsTableCancelManagement(vm);

        vm.filterSearch = function(){
            $('#listTable').bootstrapTable('refresh');
        }

        //导出项目信息
        vm.expProinfo = function () {
            vm.status = '2';
            vm.tableParams.$filter =  util.buildOdataFilter("#toolbar",null);
            vm.tableParams.$orderby = "createdDate desc";
            projectManagerSvc.createProReport(vm);
        }

        vm.restore = function (pro) {
            projectManagerSvc.checkProPriUser(pro.id,function(data){
                if(data.flag){
                    vm.model = pro;
                    vm.model.status = '1';
                    bsWin.confirm("是否恢复项目", function () {
                        projectManagerSvc.restoreInvestProject(vm);
                    })
                }else{
                    bsWin.alert("您无权进行编辑操作！");
                }
            });
        }

        vm.delete = function (id) {
            projectManagerSvc.checkProPriUser(id,function(data){
                if(data.flag){
                    bsWin.confirm("是否删除项目，删除后数据不可恢复", function () {
                        projectManagerSvc.deleteGovernmentInvestProject(id);
                    })
                }else{
                    bsWin.alert("您无权进行编辑操作！");
                }
            });
        }
    }

})();
(function () {
    'use strict';

    angular.module('myApp').config(governmentInvestProjectEditConfig);

    governmentInvestProjectEditConfig.$inject = ["$stateProvider"];

    function governmentInvestProjectEditConfig($stateProvider) {
        $stateProvider.state('projectManageEdit', {
            url: "/projectManageEdit/:id/:flag",
            controllerAs: "vm",
            templateUrl: util.formatUrl('project/html/edit'),
            controller: projectManagerEditCtrl
        });
    }

    projectManagerEditCtrl.$inject = ["$scope", "projectManagerSvc", "$state", "bsWin"];

    function projectManagerEditCtrl($scope, projectManagerSvc, $state, bsWin) {
        $scope.csHide("cjgl");
        var vm = this;
        vm.model = {};
        vm.model.id = $state.params.id;
        vm.flag = $state.params.flag;
        vm.attachments = [];


        /*第一负责人也是有部门的人员，而不是当前用户所在部门的部门人员（修改2018-11-18）
        projectManagerSvc.findOrgUser(function (data) {
            vm.principalUsers = data;
            vm.initFileUpload();
        });
        */
        projectManagerSvc.findOrganUser(function (data) {
            vm.orgUsers = data;
            vm.principalUsers = data;
            vm.initFileUpload();
        });

        projectManagerSvc.findAllOrgDelt(function (data) {
            vm.orgDeptList = data;
        });

        if (vm.model.id) {
            projectManagerSvc.findGovernmentInvestProjectById(vm, function () {
                /**
                 * 查询附件列表
                 */
         /*       projectManagerSvc.getAttachments(vm, {
                    "businessId": vm.model.id
                }, function (data) {
                    angular.forEach(vm.attachments.concat(data), function (o, i) {
                        vm.attachments = vm.attachments.concat(o);
                    });
                });*/
            });
        } else {
            projectManagerSvc.createUUID(vm);
        }

        //初始化附件上传控件
        vm.initFileUpload = function(){
            if (!vm.model.id) {
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.model.id", function (newValue, oldValue) {
                    if (newValue && newValue != oldValue && !vm.initUploadOptionSuccess) {
                        vm.initFileUpload();
                    }
                });
            }
            vm.sysFile = {
                businessId: vm.model.id,
                mainId: "",
                mainType: "",
                sysfileType: "",
                sysBusiType: "",
                detailBt: "detail_file_bt",
            };
            projectManagerSvc.initUploadOptions({
                inputId: "sysfileinput",
                vm: vm
            });
        }


        /**
         * 删除附件
         */
        vm.removeFile = function (fileId, index) {
            bsWin.confirm("是否移除附件", function () {
                projectManagerSvc.deleteFileById(fileId, vm);
                vm.attachments.splice(index, 1);
            })
        };
        /**
         * 日期比较
         */
        function compareDate(d1, d2) {
            return ((new Date(d1.replace(/-/g, "\/"))) > (new Date(d2.replace(/-/g, "\/"))));
        }


        vm.checkLength = function (obj, max, id) {
            util.checkLength(obj, max, id);
        };


        //新增与编辑
        vm.save = function () {
            util.initJqValidation();
            var isSelectOrg = false;
            $('.seleteTable input[selectType="main"]:checked').each(function () {
                vm.model.mainOrgId = $(this).val();
                vm.model.mainOrgName = $(this).attr("tit");
                isSelectOrg = true;
            });
            if(isSelectOrg){
                var assistOrgArr = [];
                var assistOrgNameArr = []
                $('.seleteTable input[selectType="assist"]:checked').each(function () {
                    assistOrgArr.push($(this).val());
                    assistOrgNameArr.push($(this).attr("tit"));
                });
                if(assistOrgArr.length > 0){
                    vm.model.assistOrgId = assistOrgArr.join(",");
                    vm.model.assistOrgName = assistOrgNameArr.join(",");
                }
            }else{
                bsWin.alert("您还没选择评审部门！");
                return false;
            }

            var isValid = $('#form').valid();
            if (isValid) {
                var selUser = []
                var selUserName = []
                $('#principalUser_ul input[selectType="assistUser"]:checked').each(function () {
                    selUser.push($(this).attr("value"));
                    selUserName.push($(this).attr("tit"));
                });
                vm.model.mainUserName = $.trim($("#mainUser").find("option:selected").text());
                vm.model.assistUser = selUser.join(",");
                vm.model.assistUserName = selUserName.join(",");
                if (vm.model.id) {
                    projectManagerSvc.updateGovernmentInvestProject(vm);
                } else {
                    vm.model.id = vm.UUID;
                    vm.model.status = '1';
                    projectManagerSvc.createGovernmentInvestProject(vm);
                }
            }
        };

        //检查项目负责人
        vm.checkPrincipal = function () {
            var selUserId = $("#mainUser").val();
            if (selUserId) {
                $('#principalUser_ul input[selectType="assistUser"]').each(
                    function () {
                        var value = $(this).attr("value");
                        if (value == selUserId) {
                            $(this).removeAttr("checked");
                            $(this).attr("disabled", "disabled");
                        } else {
                            $(this).removeAttr("disabled");
                        }
                    }
                );
            }
        }

        // 业务判断
        vm.mainOrg = function ($event) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            var checkboxValue = checkbox.value;
            if (checked) {
                $('.seleteTable input[selectType="main"]').each(
                    function () {
                        var value = $(this).attr("value");
                        if (value != checkboxValue) {
                            $(this).removeAttr("checked");
                            $("#assist_" + value).removeAttr("disabled");
                        } else {
                            $("#assist_" + checkboxValue).removeAttr("checked");
                            $("#assist_" + checkboxValue).attr("disabled", "disabled");
                        }
                    });

            } else {
                $("#assist_" + checkboxValue).removeAttr("disabled");
            }
        }
    }
})();
(function () {
    'use strict';

    angular.module('myApp').config(projectConfig);

    projectConfig.$inject = ["$stateProvider"];

    function projectConfig($stateProvider) {
        //项目管理列表页
        $stateProvider.state('projectManage', {
            url: "/projectManage",
            controllerAs: "vm",
            templateUrl: util.formatUrl('project/html/list'),
            controller: projectManagerCtrl
        });
    }

    projectManagerCtrl.$inject = ["$state","projectManagerSvc","bsWin"];

    function projectManagerCtrl($state,projectManagerSvc,bsWin) {
        var vm = this;
        vm.model = {};
        vm.tableParams = {};
   /*     projectManagerSvc.findOrgUser(function(data){
            vm.principalUsers = data;
        });*/
        //获取项目列表
        projectManagerSvc.rsTableControl(vm);

        vm.filterSearch = function(){
            $('#listTable').bootstrapTable('refresh');
        }

        //导出项目信息
        vm.expProinfo = function () {
            vm.status = '1';
            vm.tableParams.$filter =  util.buildOdataFilter("#toolbar",null);
            vm.tableParams.$orderby = "createdDate desc";
           projectManagerSvc.createProReport(vm);

        }

        //作废项目
        vm.cancel = function (pro) {
            projectManagerSvc.checkProPriUser(pro.id,function(data){
                if(data.flag){
                    vm.model = pro;
                    vm.model.status = '2';
                    bsWin.confirm("是否作废项目", function () {
                        projectManagerSvc.cancelInvestProject(vm);
                    })
                }else{
                    bsWin.alert("您无权进行编辑操作！");
                }
            });
        }

        vm.editProj = function(projId){
            projectManagerSvc.checkProPriUser(projId,function(data){
                if(data.flag){
                    $state.go("projectManageEdit",{id:projId,flag:"edit"});
                }else{
                    bsWin.alert("您无权进行编辑操作！");
                }
            });
        }

    }
})();
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
                                width: "75%",
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
(function () {
    'use strict';

    angular.module('myApp').config(governmentInvestProjectViewConfig);

    governmentInvestProjectViewConfig.$inject = ["$stateProvider"];

    function governmentInvestProjectViewConfig($stateProvider) {
        $stateProvider.state('projectManageView', {
            url: "/projectManageView/:id/:flag",
            controllerAs: "vm",
            templateUrl: util.formatUrl('project/html/view'),
            controller: projectManagerViewCtrl
        });
    }

    projectManagerViewCtrl.$inject = ["$scope", "projectManagerSvc","$state","bsWin","attachmentSvc"];

    function projectManagerViewCtrl($scope,projectManagerSvc,$state,bsWin,attachmentSvc) {
        $scope.csHide("cjgl");
        var vm = this;
         vm.model = {};
         vm.model.id = $state.params.id;
         vm.flag = $state.params.flag;
         vm.attachments = [];

        /*第一负责人也是有部门的人员，而不是当前用户所在部门的部门人员（修改2018-11-18）
         projectManagerSvc.findOrgUser(function (data) {
         vm.principalUsers = data;
         vm.initFileUpload();
         });
         */
        projectManagerSvc.findOrganUser(function (data) {
            vm.orgUsers = data;
            vm.principalUsers = data;
            vm.initFileUpload();
        });

        projectManagerSvc.findAllOrgDelt(function(data){
            vm.orgDeptList = data;
        });

  /*      /!**
         * 初始化附件上传
         *!/
        projectManagerSvc.initUploadConfig(vm,"relateAttach",function (data) {
            vm.attachments = vm.attachments.concat(JSON.parse(data));
        });*/
         if (vm.model.id) {

            projectManagerSvc.findGovernmentInvestProjectById(vm, function () {
        /*        /!**
                 * 查询附件列表
                 *!/
               projectManagerSvc.getAttachments(vm, {
                    "businessId": vm.model.id
                }, function (data) {
                    angular.forEach(vm.attachments.concat(data), function (o, i) {
                        vm.attachments = vm.attachments.concat(o);
                    });
                });*/
                if(vm.flag == 'cancel' || vm.flag == 'normal'){
                    if(vm.flag == 'cancel'){
                        vm.model.status = '2';
                    }else{
                        vm.model.status = '1';
                    }
                    projectManagerSvc.updateGovernmentInvestProject(vm);
                }else if(vm.flag == 'delete'){
                    projectManagerSvc.deleteGovernmentInvestProject(vm.model.id);
                }

            });
        }else{
             projectManagerSvc.createUUID(vm);
         }

        /**
         * 日期比较
         */
        function compareDate (d1,d2) {
            return ((new Date(d1.replace(/-/g,"\/"))) > (new Date(d2.replace(/-/g,"\/"))));
        }

        //初始化附件上传控件
        vm.initFileUpload = function(){
            if (!vm.model.id) {
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.model.id", function (newValue, oldValue) {
                    if (newValue && newValue != oldValue && !vm.initUploadOptionSuccess) {
                        vm.initFileUpload();
                    }
                });
            }
            vm.sysFile = {
                businessId: vm.model.id,
                mainId: "",
                mainType: "",
                sysfileType: "",
                sysBusiType: "",
                detailBt: "detail_file_bt",
            };
            projectManagerSvc.initUploadOptions({
                inputId: "sysfileinput",
                vm: vm
            });
        }

    }
})();
(function () {
    'use strict';

    angular.module('myApp').config(function ($stateProvider) {
        var userInfo_url = util.formatUrl('admin/userInfo');

        $stateProvider.state('userInfo', {
            url: "/userInfo",
            controllerAs: "vm",
            templateUrl: userInfo_url,
            controller: function ($scope, $http, bsWin) {
                var vm = this;
                vm.isSubmit = false;
                vm.toUpdate = function () {
                    util.initJqValidation();
                    var isValid = $('form').valid();
                    if (isValid) {
                        var encrypt = new JSEncrypt();
                        $http.get(util.formatUrl("rsaKey")).success(function (data) {
                            encrypt.setPublicKey(data || "");
                            if (vm.model.oldPassword) {
                                vm.model.oldPassword = encrypt.encrypt(vm.model.oldPassword);
                                vm.model.newPassword = encrypt.encrypt(vm.model.newPassword);
                                vm.model.verifyPassword = encrypt.encrypt(vm.model.verifyPassword);
                            }

                            vm.isSubmit = true;
                            $http.put(userInfo_url, vm.model).then(function () {
                                vm.isSubmit = false;
                                bsWin.success("修改成功");
                            }, function () {
                                vm.isSubmit = false;
                            })
                        })
                    }
                }
            }
        });
    });

})();
(function () {
    'use strict';

    angular.module('myApp').config(welcomeConfig);

    welcomeConfig.$inject = ["$stateProvider"];

    function welcomeConfig($stateProvider) {
        $stateProvider.state('welcome', {
            url: "/welcome",
            templateUrl: util.formatUrl('admin/welcome'),
            controllerAs: "vm",
            controller: welcomeCtrl
        });
    }

    welcomeCtrl.$inject = ["$scope", "operatorLogSvc"];

    function welcomeCtrl($scope, operatorLogSvc) {
        var vm = this;

        // 用户操作日志
        vm.getOperatorLogList = function () {
            operatorLogSvc.getOperatorLogList(vm, {
                "$orderby": "createdDate desc",
                "$top": 5
            });
        }

    }

})();
(function () {
    'use strict';

    angular.module('myApp').factory("welcomeSvc", function ($http, bsWin) {
        var welcome_url = util.formatUrl("admin");

        return {};

    });
})();
(function () {
    'use strict';

    angular.module('myApp').config(function ($stateProvider) {
        $stateProvider.state('attachment', {
            url: "/attachment",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/attachment/html/list'),
            controller: function ($scope, attachmentSvc, bsWin) {
                $scope.csHide("bm");
                var vm = this;
                vm.attachment = {};

                vm.del = function (fileId) {
                    bsWin.confirm("确认删除数据吗？", function () {
                        attachmentSvc.deleteById(vm, fileId, function () {
                            vm.bsTableControl.refresh();
                        });
                    });
                }

                vm.dels = function () {
                    var rows = $('#editTable').bootstrapTable('getSelections');//返回的是所有选中的行对象

                    if (rows.length == 0) {
                        bsWin.warning("请选择要删除的数据");
                        return;
                    }
                    var ids = [];
                    $.each(rows, function (i, row) {
                        ids.push(row.id);
                    })
                    vm.del(ids.join(","));
                }

                attachmentSvc.bsTableControl(vm);

            }
        });
    });

})();
(function () {
    'use strict';

    angular.module('myApp').config(function ($stateProvider) {
        $stateProvider.state('attachmentEdit', {
            url: "/attachmentEdit/:id",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/attachment/html/edit'),
            controller: function ($scope, attachmentSvc, $state, bsWin) {
                $scope.csHide("bm");
                var vm = this;
                vm.attachment = {};
                vm.attachmentId = $state.params.id;
                vm.attachment.publicAtt = true;
                if (vm.attachmentId) {
                    attachmentSvc.findDocById(vm.attachmentId, vm);
                }

                vm.save = function () {
                    if (vm.attachmentId) {
                        vm.attachment.id = vm.attachmentId;
                        attachmentSvc.update(vm);//更新
                    } else {
                        if (!vm.attachment.id) {
                            bsWin.alert("请先上传文档！");
                            return;
                        }
                        attachmentSvc.create(vm);//创建
                    }
                }

                //初始化上传控件
                $('#orginalFiles').uploadify({
                    uploader: util.formatUrl('sys/attachment/upload'),
                    swf: util.formatUrl("libs/uploadify/uploadify.swf"),
                    method: 'post',
                    multi: true,
                    auto: true,//自动上传
                    fileObjName: 'files',// 上传参数名称
                    fileSizeLimit: "10MB",//上传文件大小限制
                    buttonText: '选择文档',
                    fileExt: '*.pdf;*.txt;*.png;*.doc',
                    fileTypeExts: '*.pdf;*.txt;*.png;*.doc;*png;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.ceb',
                    fileTypeDesc: "请选择*.pdf;*.txt;*.png;*.doc文件",     // 文件说明
                    removeCompleted: true,   //设置已完成上传的文件是否从队列中移除，默认为true
                    onUploadStart: function (file) {
                        if (!vm.attachment.tableKey) {
                            bsWin.error("参数错误！");
                            return false;
                        }
                        //var filters=[];
                        if (vm.attachment.id) {
                            $('#orginalFiles').uploadify("settings", "formData", {"attId": vm.attachment.id});//传参到后台
                        }
                        $('#orginalFiles').uploadify("settings", "formData", {"tableKey": vm.attachment.tableKey});//传参到后台
                    },
                    onUploadSuccess: function (file, data, response) {
                        angular.element("body").scope().$apply(function () {
                            vm.attachment.originalName = (file.name).substring(0, (file.name).indexOf("."));
                            bsWin.alert("上传成功");
                        })
                    },
                    onUploadError: function (file, errorCode, errorMsg, errorString) {
                        angular.element("body").scope().$apply(function () {
                            bsWin.error("上传失败");
                        })
                    },
                });


                vm.backPrevPage = function (backUrl) {
                    $scope.backPrevPage(backUrl);
                };

            }
        });
    });

})();
(function () {
    'use strict';

    angular.module('myApp').factory("attachmentSvc", function ($http, bsWin) {
        var attachment_url = util.formatUrl("sys/attachment");
        return {
            bsTableControl: function (vm) {
                vm.bsTableControl = {
                    options: util.getTableFilterOption({
                        url: attachment_url,
                        defaultSort: "itemOrder desc",
                        columns: [{
                            title: '行号',
                            width: 50,
                            switchable: false,
                            formatter: function (value, row, index) {
                                var state = vm.bsTableControl.state;
                                if (state.pageNumber && state.pageSize) {
                                    return index + 1 + (state.pageNumber - 1) * state.pageSize;
                                } else {
                                    return index + 1
                                }
                            }
                        }, {
                            checkbox: true
                        },{
                            field: 'originalName',
                            title: '文档名称',
                            sortable: true,
                            filterControl: "input",
                            filterOperator: "like",
                            formatter: function (value, row, index) {
                                return '<a href="' + attachment_url + '/download/' + row.id + '" target="_blank">' + value + '</a>';
                            }
                        }, {
                            field: 'docCategory',
                            title: '文档分类',
                            sortable: true,
                            filterControl: "input",
                            filterOperator: "like",
                            formatter: "{{DICT.ATTACHMENT.dicts.CATEGORY.dicts[row.docCategory].dictName}}"
                        }, {
                            field: 'attMonth',
                            title: '所属月份',
                            width: 100,
                            sortable: true,
                            filterControl: "input",
                            filterOperator: "like"
                        }, {
                            field: 'publicAtt',
                            title: '是否公开给业主',
                            width: 130,
                            sortable: false,
                            align: "center",
                            filterControl: "input",
                            filterOperator: "like",
                            formatter:function(value){
                               if(value){
                                    return"是";
                                }else{
                                    return"否";
                                }
                            }
                        }, {
                            field: 'createdDate',
                            title: '建档时间',
                            width: 200,
                            sortable: true,
                            filterControl: "datepicker",
                            filterOperator: "gt"
                        }, {
                            field: 'modifiedDate',
                            title: '最后修改时间',
                            width: 200,
                            sortable: false,
                            filterControl: "datepicker",
                            filterOperator: "gt"
                        }, {
                            field: 'createdBy',
                            title: '建档人',
                            width: 100,
                            sortable: false,
                            filterControl: "input",
                            filterOperator: "like"
                        },{
                            field: '',
                            title: '操作',
                            width: 100,
                            formatter: $("#columnBtns").html()
                        }]
                    })
                }
            },
            create: function (vm) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.put(attachment_url, vm.attachment).then(function () {
                        bsWin.success("添加成功");
                        vm.backPrevPage();
                        vm.isSubmit = false;
                    }).then(function () {
                        vm.isSubmit = false;
                    });
                }
            },
            /**
             * 通过主键查找数据
             * @param fileId
             * @param vm
             */
            findDocById: function (vm) {
                $http.get(attachment_url + "/findById",{params:{"id":vm.attachment.id}}).then(function (response) {
                    vm.attachment = response.data;
                });
            },
            update: function (vm) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.put(attachment_url, vm.attachment).success(function () {
                        vm.isSubmit = false;
                        bsWin.success("更新成功");
                        vm.backPrevPage("attachment");
                    }).then(function () {
                        vm.isSubmit = false;
                    });
                }
            },
            deleteById: function (vm, fileId, fn) {
                vm.isSubmit = true;
                $http['delete'](attachment_url, {params: {"id": fileId || ""}}).then(function () {
                    bsWin.success("删除成功");
                    // $("#editTable").bootstrapTable('refresh', "");//刷新表格數據
                    angular.isFunction(fn) && fn();
                    vm.isSubmit = false;
                }).then(function () {
                    vm.isSubmit = false;
                });
            },

        }
    });

})();
(function () {
    'use strict';

    angular.module('myApp').config(function ($stateProvider) {
        $stateProvider.state('dict', {
            url: "/dict",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/dict/html/list'),
            controller: function ($scope, dictSvc, bsWin) {
                $scope.csHide("bm");
                var vm = this;
                vm.dict = {};
                //deleteDict#Begin
                vm.dels = function () {
                    var nodes = vm.dictsTree.getSelectedNodes();
                    if (nodes && nodes.length > 0) {
                        vm.del(nodes[0].dictId)
                    } else {
                        bsWin.confirm("请选择数据")
                    }
                };
                vm.del = function (dictId) {
                    vm.dict.dictId = dictId;
                    bsWin.confirm("删除字典将会连下级字典一起删除，确认删除数据吗？", function () {
                        dictSvc.deleteDict(vm);
                    });
                };
                //deleteDict#End

                vm.resetDict = function () {
                    bsWin.confirm("您确定要重置数据字典吗？", function () {
                        dictSvc.resetDict(vm);
                    });
                }

                dictSvc.initDictTree(vm);
            }

        });
    });

})();
(function () {
    'use strict';

    angular.module('myApp').config(function ($stateProvider) {
        $stateProvider.state('dict.edit', {
            url: "/dictEdit/:dictId",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/dict/html/edit'),
            controller: function ($scope, dictSvc, $state) {
                $scope.csHide("bm");
                var vm = this;
                vm.dict = {};
                vm.dictId = $state.params.dictId;
                if (vm.dictId) {
                    vm.isUpdate = true;
                }
                if (vm.isUpdate) {
                    dictSvc.findDictById(vm);
                } else {
                    dictSvc.initpZtreeClient(vm);
                }
                vm.create = function () {
                    if (vm.dictsTree) {
                        var pNode = vm.dictsTree.getCheckedNodes(true);
                        if (pNode && pNode.length != 0) {
                            vm.dict.parentId = pNode[0].dictId;
                        }
                    }
                    dictSvc.createDict(vm);
                };

                vm.update = function () {
                    dictSvc.updateDict(vm);
                };

                vm.dictTypeChange = function () {
                    if (vm.dict.dictType) {
                        vm.dict.dictKey = '';
                    }
                };

            }
        });
    });

})();
(function () {
    'use strict';

    var app = angular.module('myApp');
    app.factory("dictSvc", function ($http, bsWin, $state) {
        var dict_url = util.formatUrl("sys/dict");
        return {
            //list#zTree#Begin
            initDictTree: initDictTree,
            //list#zTree#End
            //edit#zTree#Begin
            /**
             * 初始化数据字典树
             * @param vm    作用域
             */
            initpZtreeClient: function (vm) {
                vm.dictsTree && vm.dictsTree.destroy();
                $http.get(dict_url + "?$orderby=itemOrder asc").success(function (data) {
                    //vm.dict = data;
                    var setting = {
                        check: {enable: true, chkStyle: "radio", radioType: "all"},
                        data: {
                            key: {
                                name: "dictName"
                            },
                            simpleData: {
                                enable: true,
                                idKey: "dictId",
                                pIdKey: "parentId",
                                rootPId: 0
                            }
                        }
                    };
                    vm.dictsTree = $.fn.zTree.init($("#pzTree"), setting, data || []);
                });
            },
            //edit#zTree#End
            /**
             * 创建数据字典
             * @param vm    作用域
             */
            createDict: function (vm) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.post(dict_url, vm.dict).then(function () {
                        bsWin.success("创建成功");
                        vm.isSubmit = false;
                        initDictTree(vm);
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            /**
             * 通过主键查找数据字典数据
             * @param vm    作用域
             */
            findDictById: function (vm) {
                if (!vm.dictId) return false;
                $http.get(dict_url + "/" + vm.dictId).success(function (data) {
                    vm.dict = data;
                });
            },

            updateDict: function (vm) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.put(dict_url, vm.dict).then(function () {
                        bsWin.success("更新成功");
                        vm.isSubmit = false;
                        initDictTree(vm);
                    }, function () {
                        vm.isSubmit = false;
                    })
                }
            },

            deleteDict: function (vm) {
                vm.isSubmit = true;
                $http['delete'](dict_url, {params: {"dictId": vm.dict.dictId || ""}}).then(function () {
                    bsWin.success("删除成功");
                    vm.isSubmit = false;
                    initDictTree(vm);
                }, function () {
                    vm.isSubmit = false;
                });
            },
            /**
             *
             * @param vm
             */
            resetDict: function (vm) {
                vm.isSubmit = true;
                $http.post(dict_url + "/reset", {}).then(function () {
                    bsWin.success("操作成功");
                    vm.isSubmit = false;
                    initDictTree(vm);
                }, function () {
                    vm.isSubmit = false;
                });
            }
        };

        function initDictTree(vm) {
            vm.dictsTree && vm.dictsTree.destroy();
            $http.get(dict_url + "?$orderby=itemOrder asc").success(function (data) {
                // vm.dict = data;

                vm.dictsTree = $.fn.zTree.init($("#zTree"), {
                    callback: {
                        onClick: zTreeOnClick
                    },
                    data: {
                        key: {
                            name: "dictName"
                        },
                        simpleData: {
                            enable: true,
                            idKey: "dictId",
                            pIdKey: "parentId",
                            rootPId: 0
                        }
                    }
                }, data || []);
                function zTreeOnClick(event, treeId, treeNode) {
                    $state.go('dict.edit', {dictId: treeNode.dictId});
                }

                // 初始化模糊搜索方法
                window.fuzzySearch("zTree", '#dictTreeKey', null, true);
            });
        }
    });

})();
(function () {
    'use strict';

    var app = angular.module('myApp');

    app.config(function ($stateProvider) {
        $stateProvider
            .state('operatorLog', {
                url: "/operatorLog",
                controllerAs: "vm",
                templateUrl: util.formatUrl('sys/operatorLog/html/list'),
                controller: function ($scope, operatorLogSvc, bsWin) {
                    $scope.csHide("bm");

                    var vm = this;
                    vm.model = {};
                    vm.delLog = function(days){
                        operatorLogSvc.deleteLog(vm,days);
                    }

                    // vm.del = function (id) {
                    //     vm.model.id = id;
                    //     bsWin.confirm("确认删除数据吗？", function () {
                    //         operatorLogSvc.deleteLog(vm);
                    //     })
                    // };
                    //
                    // vm.dels = function () {
                    //     var rows = $('#editTable').bootstrapTable('getSelections');//返回的是所有选中的行对象
                    //     if (rows.length == 0) {
                    //         bsWin.alert("请选择要删除的数据");
                    //         return;
                    //     }
                    //     var ids = [];
                    //     $.each(rows, function (i, row) {
                    //         ids.push(row.id)
                    //     });
                    //     vm.del(ids.join(","));
                    // };

                    // 初始化列表
                    operatorLogSvc.bsTableControl($scope);
                }

            });
    });

})();
(function () {
    'use strict';

    angular.module('myApp').factory("operatorLogSvc", function ($http, bsWin) {
        var operatorLog_url = util.formatUrl("sys/operatorLog");
        return {
            /**
             * 构建操作日志登录列表配置项
             * @param vm    作用域
             */
            bsTableControl: function (vm) {
                vm.bsTableControl = {
                    options: util.getTableFilterOption({
                        url: operatorLog_url,
                        columns: [{
                            title: '行号',
                            switchable: false,
                            width: 50,
                            align: "left",
                            formatter: function (value, row, index) {
                                var state = vm.bsTableControl.state;
                                if (state.pageNumber && state.pageSize) {
                                    return index + 1 + (state.pageNumber - 1) * state.pageSize;
                                } else {
                                    return index + 1
                                }
                            }
                        }, {
                            field: 'createdDate',
                            title: '操作时间',
                            width: 100,
                            sortable: true,
                            filterControl: "datepicker",
                            filterOperator: "lte"
                        }, {
                            field: 'createdBy',
                            title: '操作人',
                            width: 100,
                            sortable: true,
                            filterControl: "input",
                            filterOperator: "like"
                        }, {
                            field: 'businessType',
                            title: '操作对象',
                            width: 100,
                            sortable: true,
                            filterControl: "input",
                            filterOperator: "like",
                            formatter: function (value, row, index) {
                                return row.businessType+"["+row.operateType+"]";
                            }
                        }, {
                            field: 'operateTime',
                            title: '耗时',
                            width: 80,
                            sortable: true,
                            align: "right",
                            filterControl: "number"
                        }, {
                            field: 'ipAddress',
                            title: 'IP地址',
                            width: 100,
                            align: "center",
                            filterControl: "input",
                            filterOperator: "like"
                        },  {
                            field: 'sucessFlag',
                            title: '结果',
                            width: 60,
                            filterControl: "input",
                            filterOperator: "like",
                            formatter: "<span ng-show='row.sucessFlag==1' class='bg-green'>成功</span><span ng-show='row.sucessFlag!=1' class='bg-red'>失败</span>"
                        }, {
                            field: 'message',
                            title: '结果消息',
                            width: 120
                        },{
                            field: 'oldInfo',
                            title: '原数据',
                            width: 200,
                            formatter: function (value, row, index) {
                                if(value){
                                    return "<textarea style='width: 100%' rows='4'>"+value+"</textarea>";
                                }else{
                                    return "<textarea style='width: 100%' rows='4'> </textarea>";
                                }
                            }
                        },{
                            field: 'newInfo',
                            title: '更新数据',
                            width: 200,
                            formatter: function (value, row, index) {
                                if(value){
                                    return "<textarea style='width: 100%' rows='4'>"+value+"</textarea>";
                                }else{
                                    return "<textarea style='width: 100%' rows='4'> </textarea>";
                                }
                            }
                        },{
                            field: 'updateInfo',
                            title: '更新信息',
                            width: 200,
                            formatter: function (value, row, index) {
                                if(value){
                                    return "<textarea style='width: 100%' rows='4'>"+value+"</textarea>";
                                }else{
                                    return "<textarea style='width: 100%' rows='4'> </textarea>";
                                }
                            }
                        }]
                    })
                };
            },
            /**
             * 获取操作日志数据
             * @param vm        作用域
             * @param params    查询参数
             */
            getOperatorLogList: function (vm, params) {
                $http.get(operatorLog_url, {
                    params: params
                }).success(function (data) {
                    vm.operatorLogList = data || {};
                });
            },
            /**
             * 删除操作日志数据
             * @param vm    作用域
             * @param days  删除指定天数之前的数据
             */
            deleteLog: function (vm, days) {
                vm.isSubmit = true;
                $http['delete'](operatorLog_url, {params: {"days": days || ""}}).then(function () {
                    bsWin.alert("删除成功");
                    vm.isSubmit = false;
                    $("#editTable").bootstrapTable('refresh', "");//刷新表格数据
                });
            }

        };


    });

})();
(function () {
    'use strict';

    angular.module('myApp').config(organAddConfig);

    organAddConfig.$inject = ["$stateProvider"];

    function organAddConfig($stateProvider) {
        $stateProvider
            .state('organ.add', {
                url: "/organAdd/:id",
                controllerAs: "vm",
                templateUrl: util.formatUrl('sys/organ/html/add'),
                controller: organAddCtrl
            });
    }

    organAddCtrl.$inject = ["$scope", "organSvc", "$state"];

    function organAddCtrl($scope, organSvc, $state) {
        $scope.organId = $state.params.id;
        $scope.isSubmit = false;
        $scope.hasParent = false;

        if ($scope.organId) {
            $scope.hasParent = true;
            organSvc.findOrganById($scope, function (data) {
                $scope.parentOrgan = data;
                $scope.model = {
                    parentId: data.organId
                };
            });
        } else {
            var parentOrganTree;
            $scope.$watch("organList", function (organList) {
                parentOrganTree && parentOrganTree.destroy();

                if (!organList || organList.length == 0) {
                    $scope.hasParent = true;
                } else {
                    $scope.hasParent = false;
                    parentOrganTree = $.fn.zTree.init($("#parentOrganTree"), {
                        treeId: "organId",
                        data: {
                            key: {
                                name: "organName"
                            },
                            simpleData: {
                                enable: true,
                                idKey: "organId",
                                pIdKey: "parentId"
                            }
                        },
                        check: {
                            enable: true,
                            chkStyle: "radio"
                        },
                        callback: {
                            onCheck: function (event, treeId, treeNode) {
                                if (treeNode.checked) {
                                    $scope.$apply(function () {
                                        if(!$scope.model) $scope.model = {};
                                        $scope.model.parentId = treeNode.organId;
                                    })
                                }
                            }
                        }
                    }, organList);
                }
            })
        }

        $scope.saveOrgan = function () {
            organSvc.createOrgan($scope, function () {
                $state.go("organ", {}, {reload: true});
            });
        };

    }

})();
(function () {
    'use strict';

    angular.module('myApp').config(organConfig);

    organConfig.$inject = ["$stateProvider"];

    function organConfig($stateProvider) {
        $stateProvider.state('organ', {
            url: "/organ",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/organ/html/list'),
            controller: organCtrl
        });
    }

    organCtrl.$inject = ["$scope", "$state", "organSvc", "bsWin"];

    function organCtrl($scope, $state, organSvc, bsWin) {
        $scope.csHide("bm");

        $scope.deleteOrgan = function (organId) {
            $scope.organId = organId;
            bsWin.confirm("确认删除数据吗？", function () {
                organSvc.deleteOrgan(vm);
            })
        };

        var treeAddBtn = $("#treeAddBtn"),
            organTreeDom = $("#organTree"),
            organTreeDelete = organTreeDom.attr("delete");

        // 初始化机构树
        var organTree;
        initOrganTree();
        function initOrganTree() {
            organTree && organTree.destroy();

            organSvc.getOrganList($scope, function (data) {
                organTree = $.fn.zTree.init(organTreeDom, {
                    treeId: "organId",
                    data: {
                        key: {
                            name: "organName"
                        },
                        simpleData: {
                            enable: true,
                            idKey: "organId",
                            pIdKey: "parentId"
                        }
                    },
                    edit: {
                        enable: true,
                        removeTitle: "删除机构",
                        showRemoveBtn: function (treeId, treeNode) {
                            if (!organTreeDelete) {
                                return false;
                            }
                            return treeNode.organDataType > 0 ? !treeNode.isParent : false;
                        },
                        showRenameBtn: false
                    },
                    view: {
                        addHoverDom: function (treeId, treeNode) {
                            if (treeNode.organType < 2) {
                                var sObj = $("#" + treeNode.tId + "_span");
                                if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
                                if (treeAddBtn.length > 0) {
                                    var addStr = util.format(treeAddBtn.html(), treeNode.tId);
                                    sObj.after(addStr);
                                    var btn = $("#addBtn_" + treeNode.tId);
                                    if (btn) btn.bind("click", function () {
                                        $state.go("organ.add", {id: treeNode.organId});
                                        return false;
                                    });
                                }
                            }
                        },
                        removeHoverDom: function (treeId, treeNode) {
                            $("#addBtn_" + treeNode.tId).unbind().remove();
                        },
                        selectedMulti: false
                    },
                    callback: {
                        onClick: function (event, treeId, treeNode) {
                            // if (treeNode.organDataType == 0 && !treeNode.isParent) {
                            //     $state.go('organ.user', {id: treeNode.organId});
                            // } else {
                            // if (treeNode.organType == 0) {
                            $state.go('organ.edit', {id: treeNode.organId});
                            // } else {
                            //     $state.go('organ.user', {id: treeNode.organId});
                            // }
                            // }
                        },
                        beforeRemove: function (treeId, treeNode) {
                            $scope.$apply(function () {
                                $scope.organId = treeNode.organId;
                                organSvc.deleteOrgan($scope, function () {
                                    $scope.organId = null;
                                    $state.go('organ', {}, {reload: true});
                                });
                            });
                            return false;
                        },
                        beforeDrag: function () {
                            return false;
                        }
                    }
                }, data);

                var rootNode = organTree.getNodeByParam("parentId", null);
                organTree.expandNode(rootNode, true);

                // 初始化模糊搜索方法
                window.fuzzySearch("organTree", '#organKey', null, true);
            })
        }

        $scope.initOrganTree = initOrganTree;
    }

})();
(function () {
    'use strict';

    angular.module('myApp').config(myConfig);

    myConfig.$inject = ["$stateProvider"];

    function myConfig($stateProvider) {
        $stateProvider.state('organ.edit', {
            url: "/organEdit/:id",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/organ/html/edit'),
            controller: myCtrl
        });
    }

    myCtrl.$inject = ["$scope", "$state", "organSvc", "resourceSvc"];

    function myCtrl($scope, $state, organSvc, resourceSvc) {
        $scope.organId = $state.params.id;
        $scope.isSubmit = false;

        if ($scope.organId) {
            organSvc.findOrganById($scope, function (data) {
                $scope.model = data;
                if (data.parentId) {
                    organSvc.findOrganById({organId: data.parentId}, function (parentOrgan) {
                        $scope.parentOrgan = parentOrgan;
                    });
                }
            });
        }

        $scope.saveOrgan = function () {
            organSvc.updateOrgan($scope, function () {
                $scope.initOrganTree && $scope.initOrganTree();
            })
        };

        $scope.authorization = function () {
            initResourceTree();
            $("#organAuthorizationWin").modal('show');
        }

        $scope.toAuthorization = function () {
            if (resourceTree) {
                $scope.model.resources = resourceTree.getCheckedNodes(true);
                organSvc.authorization($scope);
            }
        }

        var resourceTree;

        function initResourceTree() {
            if (!resourceTree) {
                resourceSvc.getResourceData($scope, function (data) {
                    data = data.value || [];

                    resourceTree = $.fn.zTree.init($("#resourceTree"), {
                        treeId: "resId",
                        check: {
                            enable: true,
                            chkboxType: {"Y": "p", "N": "s"}
                        },
                        data: {
                            key: {
                                name: "resName"
                            },
                            simpleData: {
                                enable: true,
                                idKey: "resId",
                                pIdKey: "parentId"
                            }
                        }
                    }, data);

                    checkNodes();
                });
            } else {
                checkNodes();
            }

            function checkNodes() {
                resourceTree.checkAllNodes(false);
                $.each($scope.model.resources, function (k, v) {
                    resourceTree.checkNode(resourceTree.getNodeByParam("resId", v.resId), true, true);
                })
            }
        }
    }

})();

(function () {
    'use strict';

    angular.module('myApp').factory("organSvc", organSvc);

    organSvc.$inject = ["$http", "bsWin"];

    function organSvc($http, bsWin) {
        var organ_url = util.formatUrl("sys/organ");

        return {
            getOrganList: function (vm, fn) {
                $http.get(organ_url + "?$orderby=itemOrder asc").success(function (data) {
                    vm.organList = data.value || [];
                    fn && fn(vm.organList);
                });
            },
            /**
             * 机构列表（用于业主单位列表）
             * @param vm
             * @param fn
             */
            findOrganList: function (vm, fn) {
                $http.get(organ_url + "/findOrganList").then(function (response) {
                    if (fn) {
                        fn(response.data)
                    } else {
                        vm.organList = response.data;
                    }
                })
            },

            createOrgan: function (vm, fn) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.post(organ_url, vm.model).then(function () {
                        vm.isSubmit = false;
                        bsWin.success("创建成功", function () {
                            fn && fn();
                        });
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            //begin#updateOrgan
            findOrganById: function (vm, fn) {
                $http.get(organ_url + "/" + (vm.organId || "")).success(function (data) {
                    data = data || {};
                    if(!fn) {
                        vm.model = data;
                    } else {
                        fn(data);
                    }
                });
            },
            updateOrgan: function (vm, fn) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.put(organ_url, vm.model).then(function () {
                        vm.isSubmit = false;
                        bsWin.success("更新成功", function () {
                            fn && fn();
                        });
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            //End:updateOrgan

            deleteOrgan: function (vm, fn) {
                // console.log(vm.organ.id);
                $http['delete'](organ_url, {params: {"organId": vm.organId || ""}}).then(function () {
                    bsWin.success("删除成功", function () {
                        fn && fn();
                    });
                }, function () {
                    vm.isSubmit = false;
                });
            },
            authorization: function (vm) {
                if (!vm.organId || !vm.model) {
                    bsWin.warning("缺少参数");
                    return;
                }
                vm.isSubmit = true;
                $http.put(organ_url + "/authorization?organId=" + vm.organId, vm.model.resources || []).then(function () {
                    vm.isSubmit = false;
                    bsWin.success("权限更新成功");
                }, function () {
                    vm.isSubmit = false;
                });
            }
        };


    }

})();
(function () {
    'use strict';

    angular.module('myApp').config(myConfig);

    myConfig.$inject = ["$stateProvider"]

    function myConfig($stateProvider) {
        $stateProvider.state('organ.user', {
            url: "/organUser/:id",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/organ/html/user'),
            controller: myCtrl
        });
    }

    myCtrl.$inject = ["$scope", "$state", "organSvc", "userSvc", "bsWin"];

    function myCtrl($scope, $state, organSvc, userSvc, bsWin) {
        $scope.organId = $state.params.id || '';
        $scope.isSubmit = false;
        $scope.isUpdate = false;
        $scope.openUserEditWin = function (userId) {
            userEditWin.modal("show");
            if (userId) {
                $scope.isUpdate = true;
                $scope.userId = userId;
                userSvc.findUserById($scope, function (data) {
                    $scope.model = data;
                });
            } else {
                $scope.isUpdate = false;
                $scope.model = {
                    useState: 1,
                    organ: {
                        organId: $scope.organId
                    }
                };
            }
        }

        var userEditWin = $("#organUserEditModel").on('hidden.bs.modal', function (e) {
            $scope.$apply(function () {
                $scope.model = null;
            })
        });

        $scope.saveUser = function () {
            if ($scope.isUpdate) {
                userSvc.updateUser($scope, function () {
                    userEditWin.modal("hide");
                    $("#editTable").bootstrapTable('refresh');//刷新表格数据
                });
            } else {
                userSvc.createUser($scope, function () {
                    userEditWin.modal("hide");
                    $("#editTable").bootstrapTable('refresh');//刷新表格数据
                });
            }
        }

        $scope.delUser = function (userId) {
            $scope.userId = userId;
            bsWin.confirm("确认删除数据吗？", function () {
                userSvc.deleteUser($scope);
            })
        };

        $scope.delUsers = function () {
            var rows = $('#editTable').bootstrapTable('getSelections');//返回的是所有选中的行对象
            if (rows.length == 0) {
                bsWin.alert("请选择要删除的数据");
                return;
            }
            var ids = [];
            $.each(rows, function (i, row) {
                ids.push(row.userId)
            });
            $scope.delUser(ids.join(","));
        };

        $scope.start = function (row) {
            $scope.userId = row.userId;
            userSvc.enableUser($scope);
        }

        $scope.stop = function (row) {
            $scope.userId = row.userId;
            userSvc.disableUser($scope);
        }

        organSvc.findOrganById($scope, function (data) {
            $scope.organ = data;
        });
        userSvc.bsTableControl($scope, {field: "organ.organId", operator: "eq", value: $scope.organId});
        userSvc.reloadBsTable($scope);
    }

})();
(function () {
    'use strict';

    angular.module('myApp').config(function ($stateProvider) {
        $stateProvider.state('resource', {
            url: "/resource",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/resource/html/list'),
            controller: function ($scope, resourceSvc, bsWin) {
                $scope.csHide("bm");

                $scope.editTitle = "新增系统资源";
                $scope.model = {status: 1};
                $scope.isUpdate = false;
                $scope.isSubmit = false;
                $scope.createResource = createResource;

                function createResource(resId) {
                    if (!resId) {
                        $scope.model = {status: 1};
                        $scope.isUpdate = false;
                        $scope.editTitle = "新增系统资源";
                        var nodes = parentResourceTree.getCheckedNodes(true);
                        for (var i = 0, l = nodes.length; i < l; i++) {
                            parentResourceTree.checkNode(nodes[i], false, true);
                        }
                    } else {
                        $scope.isUpdate = true;
                        $scope.editTitle = "编辑系统资源";
                        if (!$scope.model) $scope.model = {};
                        $scope.resId = resId;
                        resourceSvc.findResourceById($scope, function (data) {
                            $scope.parentId = data.parentId;
                            var node = parentResourceTree.getNodeByParam("resId", data.parentId);
                            node && parentResourceTree.checkNode(node, true, true);
                        });
                    }
                }

                $scope.createSave = function () {
                    var nodes = parentResourceTree.getCheckedNodes(true);
                    if (nodes.length > 0) {
                        $scope.model.parentId = nodes[0].resId;
                    }

                    resourceSvc.createResource($scope, function () {
                        initResourceTree()
                    });
                }

                $scope.updateSave = function () {
                    util.initJqValidation();
                    var isValid = $('form').valid();
                    if (isValid) {
                        var nodes = parentResourceTree.getCheckedNodes(true);
                        if (nodes.length > 0) {
                            $scope.model.parentId = nodes[0].resId;
                        }

                        resourceSvc.updateResource($scope, function () {
                            initResourceTree()
                        });
                    }
                }

                // 重置系统资源
                $scope.resetResource = function () {
                    resourceSvc.resetResource($scope, function () {
                        initResourceTree();
                    });
                }

                // 初始化资源树
                var resourceTree, parentResourceTree;
                initResourceTree();
                function initResourceTree() {
                    resourceTree && resourceTree.destroy();
                    parentResourceTree && parentResourceTree.destroy();

                    resourceSvc.getResourceData($scope, function (data) {
                        data = data.value || [];
                        var setting = {
                            treeId: "resId",
                            data: {
                                key: {
                                    name: "resName"
                                },
                                simpleData: {
                                    enable: true,
                                    idKey: "resId",
                                    pIdKey: "parentId"
                                }
                            }
                        };

                        resourceTree = $.fn.zTree.init($("#resourceTree"), $.extend({
                            edit: {
                                enable: true,
                                removeTitle: "删除资源",
                                showRemoveBtn: function (treeId, treeNode) {
                                    return !treeNode.isParent;
                                },
                                showRenameBtn: false
                            },
                            callback: {
                                onClick: function (event, treeId, treeNode) {
                                    // console.log(treeId, treeNode);
                                    $scope.$apply(function () {
                                        createResource(treeNode.resId);
                                    })
                                },
                                beforeRemove: function (treeId, treeNode) {
                                    $scope.$apply(function () {
                                        $scope.resId = treeNode.resId;
                                        resourceSvc.deleteResource($scope, function () {
                                            $scope.resId = null;
                                            $scope.parentId = null;
                                            createResource();
                                            initResourceTree();
                                        });
                                    });
                                    return false;
                                },
                                beforeDrag: function () {
                                    return false;
                                }
                            }
                        }, setting), data);

                        if ($scope.parentId) {
                            var node = resourceTree.getNodeByParam("resId", $scope.parentId);
                            resourceTree.expandNode(node, true, false, true);
                        }

                        parentResourceTree = $.fn.zTree.init($("#parentResourceTree"), $.extend({
                            check: {
                                enable: true,
                                chkStyle: "radio"
                            }
                        }, setting), data);

                        // 初始化模糊搜索方法
                        window.fuzzySearch("resourceTree", '#resourceKey', null, true);
                    });
                }

            }

        });
    });

})();
(function () {
    'use strict';

    var app = angular.module('myApp');

    app.factory("resourceSvc", function ($http, bsWin) {
        var resource_url = util.formatUrl("sys/resource");
        return {
            /**
             * 获取系统资源数据
             * @param vm
             * @param fn    操作成功的回调函数
             */
            getResourceData: function (vm, fn) {
                $http.get(resource_url + "?$orderby=itemOrder asc").success(function (data) {
                    vm.resources = data;
                    fn && fn(data);
                })
            },
            /**
             * 通过主键查找系统资源
             * @param vm
             * @param fn    操作成功的回调函数
             */
            findResourceById: function (vm, fn) {
                $http.get(resource_url + "/" + (vm.resId || "")).success(function (data) {
                    vm.model = data;
                    fn && fn(data);
                });
            },
            /**
             * 创建系统资源
             * @param vm
             * @param fn    操作成功的回调函数
             */
            createResource: function (vm, fn) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.post(resource_url, vm.model).then(function () {
                        vm.isSubmit = false;
                        bsWin.alert("创建成功");
                        fn && fn();
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            /**
             * 更新系统资源
             * @param vm
             * @param fn    操作成功的回调函数
             */
            updateResource: function (vm, fn) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.put(resource_url, vm.model).then(function () {
                        vm.isSubmit = false;
                        bsWin.alert("更新成功");
                        fn && fn();
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            /**
             * 删除系统资源
             * @param vm
             * @param fn    操作成功的回调函数
             */
            deleteResource: function (vm, fn) {
                vm.isSubmit = true;
                $http['delete'](resource_url, {data: vm.resId || ""}).then(function () {
                    bsWin.alert("删除成功");
                    vm.isSubmit = false;
                    fn && fn();
                }, function () {
                    vm.isSubmit = false;
                });
            },
            /**
             * 重置系统资源
             * @param vm
             * @param fn    操作成功的回调函数
             */
            resetResource: function (vm, fn) {
                vm.isSubmit = true;
                $http.post(resource_url + "/reset").then(function () {
                    vm.isSubmit = false;
                    bsWin.alert("系统资源已成功重置");
                    fn && fn();
                }, function () {
                    vm.isSubmit = false;
                });
            }

        };


    });

})();
(function () {
    'use strict';

    angular.module('myApp').config(function ($stateProvider) {
        $stateProvider.state('role', {
            url: "/role",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/role/html/list'),
            controller: function ($scope, roleSvc, resourceSvc, bsWin) {
                $scope.csHide("bm");
                var vm = this;
                vm.role = {};

                vm.del = function (roleId) {
                    bsWin.confirm("确认删除数据吗？", function () {
                        vm.roleId = roleId;
                        roleSvc.deleteById(vm);
                    });
                };

                vm.dels = function () {
                    var rows = $('#editTable').bootstrapTable('getSelections');//返回的是所有选中的行对象
                    if (rows.length == 0) {
                        bsWin.warning("请选择要删除的数据");
                        return;
                    }
                    var ids = [];
                    $.each(rows, function (i, row) {
                        ids.push(row.roleId);
                    });
                    vm.del(ids.join(","));
                };

                /**
                 * 打开授权窗口
                 * @param role  角色数据
                 */
                vm.authorization = function (role) {
                    vm.roleId = role.roleId;
                    vm.displayName = role.displayName;
                    initResourceTree(role);
                    $("#roleAuthorizationWin").modal('show');
                }

                vm.toAuthorization = function () {
                    if (resourceTree) {
                        vm.resources = resourceTree.getCheckedNodes(true);
                        roleSvc.authorization(vm);
                    }
                }

                // vm.setUserData = function (row) {
                // }

                vm.start = function (roleId) {
                    roleSvc.stateEnable(roleId, vm);
                }

                vm.stop = function (roleId) {
                    roleSvc.stateDisable(roleId, vm);
                }

                var resourceTree;

                function initResourceTree(role) {
                    if (!resourceTree) {
                        resourceSvc.getResourceData($scope, function (data) {
                            data = data.value || [];

                            resourceTree = $.fn.zTree.init($("#resourceTree"), {
                                treeId: "resId",
                                check: {
                                    enable: true,
                                    chkboxType: {"Y": "p", "N": "s"}
                                },
                                data: {
                                    key: {
                                        name: "resName"
                                    },
                                    simpleData: {
                                        enable: true,
                                        idKey: "resId",
                                        pIdKey: "parentId"
                                    }
                                }
                            }, data);

                            checkNodes();
                        });
                    } else {
                        checkNodes();
                    }

                    function checkNodes() {
                        resourceTree.checkAllNodes(false);
                        vm.roleId = role.roleId;
                        roleSvc.findRoleById(vm, function (data) {
                            $.each(data.resources, function (k, v) {
                                resourceTree.checkNode(resourceTree.getNodeByParam("resId", v.resId), true, true);
                            })
                        })
                    }
                }

                // 初始化角色列表
                roleSvc.bsTableControl(vm);
            }
        });
    });

})();
(function () {
    'use strict';

    angular.module('myApp').config(roleConfig);

    roleConfig.$inject = ["$stateProvider"];

    function roleConfig($stateProvider) {
        $stateProvider.state('roleEdit', {
            url: "/roleEdit/:id",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/role/html/edit'),
            controller: roleCtrl
        });
    }

    roleCtrl.$inject = ["$scope", "$state", "roleSvc"];

    function roleCtrl($scope, $state, roleSvc) {
        var vm = this;
        vm.roleId = $state.params.id;
        vm.role = {
            roleState: "1"
        };

        if (vm.roleId) {
            roleSvc.findRoleById(vm);
        }

        vm.save = function () {
            if (vm.roleId) {
                roleSvc.update(vm);
            } else {
                roleSvc.create(vm);
            }
        }
    }

})();
(function () {
    'use strict';

    angular.module('myApp').factory("roleSvc", function ($http, bsWin, $state) {
        var role_url = util.formatUrl("sys/role");

        return {
            bsTableControl: function (vm) {
                vm.bsTableControl = {
                    options: util.getTableFilterOption({
                        url: role_url,
                        columns: [{
                            title: '行号',
                            width: 50,
                            switchable: false,
                            formatter: function (value, row, index) {
                                var state = vm.bsTableControl.state;
                                if (state.pageNumber && state.pageSize) {
                                    return index + 1 + (state.pageNumber - 1) * state.pageSize;
                                } else {
                                    return index + 1
                                }
                            }
                        }, {
                            checkbox: true
                        }, {
                            field: 'roleName',
                            title: '角色名',
                            sortable: true,
                            filterControl: "input",
                            filterOperator: "like"
                        }, {
                            field: 'displayName',
                            title: '角色显示名',
                            sortable: false,
                            filterControl: "input",
                            filterOperator: "like"
                        }, {
                            field: 'roleState',
                            title: '状态',
                            width: 100,
                            align: "center",
                            filterControl: "input",
                            filterOperator: "like",
                            formatter: '<span class="bg-green" ng-if="row.roleState == 1">启用</span><span class="bg-red" ng-if="row.roleState != 1">禁用</span>'
                        }, {
                            title: '操作',
                            width: 350,
                            align: "center",
                            formatter: $("#columnBtns").html()
                        }]
                    })
                }
            },
            create: function (vm) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.post(role_url, vm.role).then(function () {
                        vm.isSubmit = false;
                        bsWin.success("创建成功", function () {
                            $state.go("role");
                        });
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            findRoleById: function (vm, fn) {
                $http.get(role_url + "/" + (vm.roleId || "")).success(function (data) {
                    if(!fn) {
                        vm.role = data;
                    } else {
                        fn(data);
                    }
                });
            },
            update: function (vm) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.put(role_url, vm.role).then(function () {
                        vm.isSubmit = false;
                        bsWin.success("更新成功");
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            deleteById: function (vm) {
                vm.isSubmit = true;
                $http['delete'](role_url, {params: {"roleId": vm.roleId || ""}}).then(function () {
                    vm.isSubmit = false;
                    bsWin.success("删除成功");
                    //刷新表格数据
                    vm.bsTableControl.refresh();
                }, function () {
                    vm.isSubmit = false;
                });
            },
            authorization: function (vm) {
                if (!vm.roleId) {
                    bsWin.warning("缺少参数");
                    return;
                }
                vm.isSubmit = true;
                $http.put(role_url + "/authorization?roleId=" + vm.roleId, vm.resources).then(function () {
                    vm.isSubmit = false;
                    bsWin.success("权限更新成功");
                }, function () {
                    vm.isSubmit = false;
                });
            },
            stateEnable: function (roleId, vm) {
                //状态启用
                $http.put(role_url + "/enable?id=" + roleId).then(function () {
                    vm.isSubmit = false;
                    bsWin.success("状态已启用");
                    //刷新表格数据
                    vm.bsTableControl.refresh();
                }, function () {
                    vm.isSubmit = false;
                });
            },
            stateDisable: function (roleId, vm) {
                //状态停用
                $http.put(role_url + "/disable?id=" + roleId).then(function () {
                    vm.isSubmit = false;
                    bsWin.success("状态已停用");
                    //刷新表格数据
                    vm.bsTableControl.refresh();
                }, function () {
                    vm.isSubmit = false;
                });
            },
            findRoles: function (vm, params, fn) {
                if (angular.isFunction(params)) {
                    fn = params;
                    params = "";
                } else {
                    params = params || ""
                }
                $http.get(role_url, {
                    params: params
                }).success(function (data) {
                    if(!fn) {
                        vm.roles = data;
                    } else {
                        fn(data);
                    }
                });
            },
            findUserRoles: function (vm, userId, fn) {
                if (angular.isFunction(userId)) {
                    fn = userId;
                    userId = "";
                } else {
                    userId = userId || ""
                }
                $http.get(role_url + "/userRoles", {
                    params: {
                        userId: userId
                    }
                }).success(function (data) {
                    if(!fn) {
                        vm.roles = data;
                    } else {
                        fn(data);
                    }
                });
            }
        }
    });

})();
(function () {
    'use strict';

    angular.module('myApp').config(variableConfig);

    variableConfig.$inject = ["$stateProvider"];

    function variableConfig($stateProvider) {
        $stateProvider.state('sysVariable', {
            url: "/sysVariable",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/variable/html/list'),
            controller: variableCtrl
        });
    }

    variableCtrl.$inject = ["$scope", "sysVariableSvc", "bsWin"];

    function variableCtrl($scope, sysVariableSvc, bsWin) {
        $scope.csHide("bm");
        var vm = this;

        vm.del = function (varId) {
            bsWin.confirm("确认删除数据吗？", function () {
                vm.varId = varId;
                sysVariableSvc.deleteById(vm);
            });
        };

        vm.dels = function () {
            var rows = $('#editTable').bootstrapTable('getSelections');//返回的是所有选中的行对象
            if (rows.length == 0) {
                bsWin.warning("请选择要删除的数据");
                return;
            }
            var ids = [];
            $.each(rows, function (i, row) {
                ids.push(row.varId);
            });
            vm.del(ids.join(","));
        };

        // 初始化系统变量列表
        sysVariableSvc.bsTableControl(vm);
    }

})();
(function () {
    'use strict';

    angular.module('myApp').config(ariableConfig);

    ariableConfig.$inject = ["$stateProvider"];

    function ariableConfig($stateProvider) {
        $stateProvider.state('sysVariableEdit', {
            url: "/sysVariableEdit/:id",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/variable/html/edit'),
            controller: variableCtrl
        });
    }

    variableCtrl.$inject = ["$state", "sysVariableSvc"];

    function variableCtrl($state, sysVariableSvc) {
        var vm = this;
        vm.varId = $state.params.id;

        if (vm.varId) {
            sysVariableSvc.findById(vm);
        }

        vm.save = function () {
            if (vm.varId) {
                sysVariableSvc.update(vm);
            } else {
                sysVariableSvc.create(vm);
            }
        }
    }

})();
(function () {
    'use strict';

    angular.module('myApp').factory("sysVariableSvc", sysVariableSvc);

    sysVariableSvc.$inject = ["$http", "bsWin", "$state"];

    function sysVariableSvc($http, bsWin, $state) {
        var var_url = util.formatUrl("sys/variable");

        return {
            bsTableControl: function (vm) {
                vm.bsTableControl = {
                    options: util.getTableFilterOption({
                        url: var_url,
                        columns: [{
                            title: '行号',
                            width: 50,
                            switchable: false,
                            formatter: function (value, row, index) {
                                var state = vm.bsTableControl.state;
                                if (state.pageNumber && state.pageSize) {
                                    return index + 1 + (state.pageNumber - 1) * state.pageSize;
                                } else {
                                    return index + 1
                                }
                            }
                        }, {
                            checkbox: true
                        }, {
                            field: 'varCode',
                            title: '系统变量编码',
                            sortable: true,
                            width: 150,
                            filterControl: "input",
                            filterOperator: "like"
                        }, {
                            field: 'varName',
                            title: '系统变量名',
                            sortable: false,
                            width: 150,
                            filterControl: "input",
                            filterOperator: "like"
                        }, {
                            field: 'typeName',
                            title: '系统变量类型',
                            width: 150,
                            sortable: false,
                            filterControl: "input",
                            filterOperator: "like"
                        }, {
                            field: 'varValue',
                            title: '系统变量值',
                            width: 200,
                            sortable: false,
                            filterControl: "input",
                            filterOperator: "like"
                        }, {
                            field: 'remark',
                            title: '备注',
                            sortable: false,
                            filterControl: "input",
                            filterOperator: "like"
                        }, {
                            title: '操作',
                            width: 150,
                            align: "center",
                            formatter: $("#columnBtns").html()
                        }]
                    })
                }
            },
            create: function (vm) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.post(var_url, vm.model).then(function () {
                        vm.isSubmit = false;
                        bsWin.success("创建成功", function () {
                            $state.go("role");
                        });
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            findById: function (vm, fn) {
                $http.get(var_url + "/" + (vm.varId || "")).success(function (data) {
                    if (!fn) {
                        vm.model = data;
                    } else {
                        fn(data);
                    }
                });
            },
            update: function (vm) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.put(var_url, vm.model).then(function () {
                        vm.isSubmit = false;
                        bsWin.success("更新成功");
                        $("#editTable").bootstrapTable('refresh');//刷新表格数据
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            deleteById: function (vm) {
                vm.isSubmit = true;
                $http['delete'](var_url, {params: {"varId": vm.varId || ""}}).then(function () {
                    vm.isSubmit = false;
                    bsWin.success("删除成功");
                    $("#editTable").bootstrapTable('refresh');//刷新表格数据
                }, function () {
                    vm.isSubmit = false;
                });
            }
        }
    }

})();
(function () {
    'use strict';

    angular.module('myApp').config(userConfig);

    userConfig.$inject = ["$stateProvider"];

    function userConfig($stateProvider) {
        $stateProvider.state('user', {
            url: "/user",
            controllerAs: "vm",
            templateUrl: util.formatUrl('sys/user/html/list'),
            controller: userCtrl
        });
    }

    userCtrl.$inject = ["$scope", "userSvc", "organSvc", "roleSvc", "bsWin"];

    function userCtrl($scope, userSvc, organSvc, roleSvc, bsWin) {
        $scope.csHide("bm");

        var vm = this;

        vm.orgList = [];
        // 初始化列表
        var userFilter = {
            field: "organ.organId",
            operator: "eq",
            value: "|"
        };

        userSvc.bsTableControl(vm, userFilter);

        function reloadBsTable(treeNode) {
           /* vm.organName = treeNode.oldname || treeNode.organName || "-";
            vm.organId = treeNode.organId;*/
            userFilter.value = treeNode.organId;
            userSvc.reloadBsTable(vm, userFilter);
        }

        vm.resetPwd = function (userId) {
            vm.userId = userId;
            userSvc.resetPwd(vm);
        }
        
        // 删除操作
        vm.del = function (userId) {
            vm.userId = userId;
            bsWin.confirm("确认删除数据吗？", function () {
                userSvc.deleteUser(vm);
            })
        };

        // 批量删除操作
        vm.dels = function () {
            // 返回的是所有选中的行对象
            var rows = vm.bsTableControl.getSelections();
            if (rows.length == 0) {
                bsWin.warning("请选择要删除的数据");
                return;
            }
            var ids = [];
            $.each(rows, function (i, row) {
                ids.push(row.userId)
            });
            vm.del(ids.join(","));
        };

        /**
         * 启用操作
         * @param row
         */
        vm.start = function (row) {
            vm.userId = row.userId;
            userSvc.enableUser(vm);
        }

        /**
         * 禁用操作
         * @param row
         */
        vm.stop = function (row) {
            vm.userId = row.userId;
            userSvc.disableUser(vm);
        }

        // 初始化机构树
        var organTree;
        initOrganTree();

        function initOrganTree() {
            organTree && organTree.destroy();
            vm.orgList = [];
            organSvc.getOrganList(vm, function (data) {
                vm.orgList = data;
                organTree = $.fn.zTree.init($("#organTree"), {
                    treeId: "organId",
                    data: {
                        key: {
                            name: "organName"
                        },
                        simpleData: {
                            enable: true,
                            idKey: "organId",
                            pIdKey: "parentId"
                        }
                    },
                    callback: {
                        onClick: function (event, treeId, treeNode) {
                            $scope.$apply(function () {
                                reloadBsTable(treeNode);
                            })
                        }
                    }
                }, data);

                var rootNode = organTree.getNodeByParam("parentId", null);
                organTree.expandNode(rootNode, true);
                reloadBsTable(rootNode);

                // 初始化模糊搜索方法
                window.fuzzySearch("organTree", '#organKey', null, true);
            })
        }

        vm.initOrganTree = initOrganTree;

        //===============用户编辑 begin===============
        vm.openUserEditWin = function (userId) {
            userEditWin.modal("show");
            if (userId) {
                vm.isUpdate = true;
                vm.userId = userId;
                userSvc.findUserById(vm, function (data) {
                    vm.model = data;
                });
            } else {
                vm.isUpdate = false;
                vm.model = {
                    useState: 1,
                    organ: {
                        organId: vm.organId
                    }
                };
            }
        }

        var userEditWin = $("#organUserEditModel").on('hidden.bs.modal', function (e) {
            $scope.$apply(function () {
                vm.model = null;
            })
        });

        vm.saveUser = function () {
            if (vm.isUpdate) {
                userSvc.updateUser(vm, function () {
                    userEditWin.modal("hide");
                    $("#editTable").bootstrapTable('refresh'); //刷新表格数据
                });
            } else {
                userSvc.createUser(vm, function () {
                    userEditWin.modal("hide");
                    $("#editTable").bootstrapTable('refresh'); //刷新表格数据
                });
            }
        }
        //===============用户编辑 end===============

        var userRolesModel = $("#userRolesModel");
        vm.setRoles = function (user) {
            vm.userId = user.userId;
            userRolesModel.modal("show");

            roleSvc.findUserRoles(vm, vm.userId, function (data) {
                vm.roles = data || [];
            });
        }

        vm.toSetRoles = function() {
            var roleIds = [];
            userRolesModel.find("input[name='roleId']:checked").each(function (i, o) {
                if(!this.disabled) {
                    roleIds.push(this.value);
                }
            });
            // console.log(roleIds);
            // return false;
            userSvc.toSetRoles(vm, roleIds.join(","))
        }

    }

})();
(function () {
    'use strict';

    var app = angular.module('myApp');

    app.config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('userEdit', {
                url: "/userEdit/:id",
                controllerAs: "vm",
                templateUrl: util.formatUrl('sys/user/html/edit'),
                controller: function ($scope, userSvc, $state) {
                    $scope.csHide("基础管理");
                    var vm = this;
                    vm.userId = $state.params.id;

                    vm.isUpdate = false;
                    if (vm.userId) {
                        vm.isUpdate = true;
                        userSvc.findUserById(vm);
                    } else {
                        vm.model = {
                            useState: 1
                        };
                    }

                    vm.save = function () {
                        if (vm.isUpdate) {
                            userSvc.updateUser(vm);
                        } else {
                            userSvc.createUser(vm, function () {
                                $state.go("user");
                            });
                        }
                    };
                }
            });
    });

})();
(function () {
    'use strict';

    angular.module('myApp').factory("userSvc", userSvc);

    userSvc.$inject = ["$http", "bsWin"];

    function userSvc($http, bsWin) {
        var user_url = util.formatUrl("sys/user"), rsaKey_url = util.formatUrl("rsaKey");

        return {
            reloadBsTable: function (vm, filter) {
                var options = vm.bsTableControl.options;
                options.url = user_url;
                if (filter) {
                    options.defaultFilters = filter;
                }
            },
            /**
             * 构建用户列表配置
             * @param vm
             * @param filter
             */
            bsTableControl: function (vm, filter) {
                vm.bsTableControl = {
                    options: util.getTableFilterOption({
                        // url: user_url,
                        defaultFilters: filter,
                        defaultSort: "itemOrder asc,createdDate desc",
                        columns: [{
                            title: '行号',
                            switchable: false,
                            align: "center",
                            width: 50,
                            formatter: function (value, row, index) {
                                var state = vm.bsTableControl.state;
                                if (state.pageNumber && state.pageSize) {
                                    return index + 1 + (state.pageNumber - 1) * state.pageSize;
                                } else {
                                    return index + 1
                                }
                            }
                        }, {
                            checkbox: true,
                            align: "center"
                        }, {
                            field: 'username',
                            title: '用户名',
                            width: 150,
                            filterControl: "input",
                            filterOperator: "like"
                        }, {
                            field: 'displayName',
                            title: '姓名',
                            width: 150,
                            sortable: true,
                            filterControl: "input",
                            filterOperator: "like"
                        }, {
                            field: 'organ.organName',
                            title: '所属机构',
                            sortable: false,
                            width: 200,
                            filterControl: "input",
                            filterOperator: "like"
                        },  {
                            field: 'lastLoginDate',
                            title: '最后登录时间',
                            width: 120,
                            sortable: true,
                            filterControl: "datepicker",
                            filterOperator: "gt"
                        }, {
                            field: 'useState',
                            title: '状态',
                            width: 60,
                            align: "center",
                            filterControl: "input",
                            filterOperator: "like",
                            formatter: '<span ng-if="row.useState == 1" class="bg-green">启用</span><span ng-if="row.useState != 1"  class="bg-red">禁用</span>'
                        },{
                            field: '',
                            title: '操作',
                            width: 200,
                            formatter: $("#userColumnBtns").html()
                        }]
                    })
                }
            },
            /**
             * 创建用户
             * @param vm
             * @param fn    操作成功的回调函数
             */
            createUser: function (vm, fn) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;

                    if (vm.model.password) {
                        $.get(rsaKey_url).success(function (data) {
                            var encrypt = new JSEncrypt();
                            encrypt.setPublicKey(data || "");
                            vm.model.password = encrypt.encrypt(vm.model.password);
                            vm.model.verifyPassword = encrypt.encrypt(vm.model.verifyPassword);
                            toCreate();
                        })
                    } else {
                        toCreate();
                    }

                }
                function toCreate() {
                    $http.post(user_url, vm.model).then(function () {
                        vm.isSubmit = false;
                        bsWin.success("创建成功");
                        fn && fn();
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            /**
             * 通过用户ID查找用户信息
             * @param vm
             * @param fn    操作成功的回调函数
             * @returns {boolean}
             */
            findUserById: function (vm, fn) {
                if (!vm.userId) return false;
                $http.get(user_url + "/" + vm.userId).success(function (data) {
                    data = data || {};
                    if (!fn) {
                        vm.model = data;
                    } else {
                        fn(data)
                    }
                });
            },
            /**
             * 更新用户信息
             * @param vm
             * @param fn    操作成功的回调函数
             */
            updateUser: function (vm, fn) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.put(user_url, vm.model).then(function () {
                        vm.isSubmit = false;
                        bsWin.success("更新成功");
                        fn && fn();
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            /**
             * 用户启用
             * @param vm
             * @param fn    操作成功的回调函数
             * @returns {boolean}
             */
            enableUser: function (vm, fn) {
                if (!vm.userId) return false;
                vm.isSubmit = true;
                $http.put(user_url + "/enable?id=" + vm.userId).then(function () {
                    vm.isSubmit = false;
                    bsWin.success("启用成功");
                    $("#editTable").bootstrapTable('refresh');//刷新表格数据
                    fn && fn();
                }, function () {
                    vm.isSubmit = false;
                });
            },
            /**
             * 用户禁用
             * @param vm
             * @param fn    操作成功的回调函数
             */
            disableUser: function (vm, fn) {
                vm.isSubmit = true;
                $http.put(user_url + "/disable?id=" + vm.userId).then(function () {
                    vm.isSubmit = false;
                    bsWin.success("禁用成功");
                    $("#editTable").bootstrapTable('refresh');//刷新表格数据
                    fn && fn();
                }, function () {
                    vm.isSubmit = false;
                });
            },
            /**
             * 删除用户
             * @param vm
             * @returns {boolean}
             */
            deleteUser: function (vm) {
                if (!vm.userId) return false;
                vm.isSubmit = true;
                $http['delete'](user_url + "?userId=" + vm.userId).then(function () {
                    bsWin.success("删除成功");
                    vm.bsTableControl.refresh();//刷新表格数据
                    vm.isSubmit = false;
                }, function () {
                    vm.isSubmit = false;
                });
            },
            /**
             * 重置密码
             * @param vm
             * @param userId
             * @returns {boolean}
             */
            resetPwd: function (vm, userId) {
                userId = userId || vm.userId;
                if (!userId) {
                    bsWin.warning("缺少参数");
                    return false;
                }
                bsWin.confirm("您是否要重置用户密码？", function () {
                    vm.isSubmit = true;
                    $http.put(user_url + "/resetPwd", userId).then(function () {
                        vm.isSubmit = false;
                        bsWin.success("用户密码已重置成功");
                    }, function () {
                        vm.isSubmit = false;
                    });
                });
            },
            userResources: function (vm, params, fn) {
                $http.get(user_url + "/resources", {
                    params: params
                }).success(function (data) {
                    if (!fn) {
                        vm.menus = data;
                    } else {
                        fn(data);
                    }
                });
            },
            toSetRoles: function (vm, roleIds) {
                if (!vm.userId) {
                    bsWin.warning("缺少参数");
                    return false;
                }

                vm.isSubmit = true;
                $http.post(user_url + "/setRoles", {}, {
                    params: {
                        userId: vm.userId,
                        roleIds: roleIds
                    }
                }).then(function () {
                    vm.isSubmit = false;
                    bsWin.success("用户角色设置成功");
                }, function () {
                    vm.isSubmit = false;
                });
            }

        };

    }

})();