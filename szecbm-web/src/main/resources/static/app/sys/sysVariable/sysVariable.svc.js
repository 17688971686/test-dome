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