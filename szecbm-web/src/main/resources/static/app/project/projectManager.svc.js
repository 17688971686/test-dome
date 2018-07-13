(function () {
    'use strict';

    angular.module('myApp').factory("projectManagerSvc", projectManagerSvc);

    projectManagerSvc.$inject = ["$http", "bsWin", "$state"];

    function projectManagerSvc($http, bsWin, $state) {
        var url_management = util.formatUrl("project");
        return {

            bsTableControlForManagement: function (vm, searchUrl, filter) {
                vm.bsTableControlForManagement = {
                    options: util.getTableFilterOption({
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
                            filterControl: 'input'
                        },{
                            field: 'projectName',
                            title: '项目名称',
                            width: 200,
                            sortable: false,
                            filterControl: "input",
                            filterOperator: "like",
                            formatter: '<a href="#/smallProjectManageFillDetails/{{row.id}}" style="color:blue">{{row.projectname}}</a>'
                        }, {
                            field: 'reviewStage',
                            title: '评审阶段',
                            width: 100,
                            filterControl: 'input'
                        }, {
                            field: 'proUnit',
                            title: '项目单位',
                            width: 90,
                            filterControl: 'input'
                        }, {
                            field: 'reviewDept',
                            title: '评审部门',
                            width: 90,
                            filterControl: 'input'
                        }, {
                            field: 'mUserId',
                            title: '项目负责人',
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
                            filterControl: 'input'
                        }, {
                            field: 'fileDate',
                            title: '存档日期',
                            width: 90,
                            filterControl: 'input'
                        },{
                            field: 'fileNo',
                            title: '存档号',
                            width: 90,
                            filterControl: 'input'
                        },{
                            field: 'remark',
                            title: '备注',
                            width: 90,
                            filterControl: 'input'
                        },{
                            field: 'id',
                            title: '操作',
                            width: 240,
                            formatter: $("#columnBtns").html()
                        }]
                    })
                }
            },

            bsTableCancelManagement: function (vm, searchUrl, filter) {
                vm.bsTableCancelManagement = {
                    options: util.getTableFilterOption({
                        url: url_management + ("/cancelInfo" || ""),
                        defaultFilters: filter,
                        columns: [{
                            title: '序号',
                            switchable: false,
                            align: "center",
                            width: 50,
                            formatter: function (value, row, index) {
                                var state = vm.bsTableCancelManagement.state;
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
                            filterControl: 'input'
                        },{
                            field: 'projectName',
                            title: '项目名称',
                            width: 200,
                            sortable: false,
                            filterControl: "input",
                            filterOperator: "like",
                            formatter: '<a href="#/smallProjectManageFillDetails/{{row.id}}" style="color:blue">{{row.projectname}}</a>'
                        }, {
                            field: 'reviewStage',
                            title: '评审阶段',
                            width: 100,
                            filterControl: 'input'
                        }, {
                            field: 'proUnit',
                            title: '项目单位',
                            width: 90,
                            filterControl: 'input'
                        }, {
                            field: 'reviewDept',
                            title: '评审部门',
                            width: 90,
                            filterControl: 'input'
                        }, {
                            field: 'mUserId',
                            title: '项目负责人',
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
                            filterControl: 'input'
                        }, {
                            field: 'fileDate',
                            title: '存档日期',
                            width: 90,
                            filterControl: 'input'
                        },{
                            field: 'fileNo',
                            title: '存档号',
                            width: 90,
                            filterControl: 'input'
                        },{
                            field: 'remark',
                            title: '备注',
                            width: 90,
                            filterControl: 'input'
                        },{
                            field: 'id',
                            title: '操作',
                            width: 240,
                            formatter: $("#columnBtns").html()
                        }]
                    })
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
                    if(vm.model.status == 1){
                        bsWin.success("提交成功");
                        $state.go("projectManage");
                    }else{
                        bsWin.success("创建成功");
                        $state.go("projectManage");
                    }
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
                    if(vm.model.status == '2' || vm.model.status == '1'){
                        if(vm.model.status == '2'){
                            bsWin.success("作废成功");
                            $state.go("projectManage");
                        }else{
                            bsWin.success("恢复成功");
                            $state.go("projectManageCancel");
                        }
                    }else{
                        bsWin.success("更新成功");
                    }
                });
            },

        }
    }

})();