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