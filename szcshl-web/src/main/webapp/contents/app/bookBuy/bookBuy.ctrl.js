(function () {
    'use strict';

    angular.module('app').controller('bookBuyCtrl', bookBuy);

    bookBuy.$inject = ['$location', 'bookBuySvc','$state'];

    function bookBuy($location, bookBuySvc,$state) {
        var vm = this;
        vm.title = '图书管理';
        vm.model = {};
        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    bookBuySvc.deleteBookBuy(vm, id);
                }
            });
        }
        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: '请选择数据'
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.del(idStr);
            }
        };

        activate();
        function activate() {
            bookBuySvc.grid(vm);
        }

        //表单查询
        vm.searchForm = function(){
            vm.gridOptions.dataSource.read();
        }

        //重置查询表单
        vm.formReset = function(){
            vm.searchModel = {};
        }

        /**
         * 借书
         */
        vm.borrowBook = function() {
            var grid = $("#bookListGrid").data("kendoGrid");
            // 获取行对象
            var data = grid.dataItem(grid.select());
            vm.model = data;
           $("#borrowBookWindow").kendoWindow({
                width: "38%",
                height: "300px",
                title: "图书借阅",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        /**
         * 保存借书信息
         */
        vm.saveBooksDetail = function() {
            bookBuySvc.saveBorrowBookDetail(vm);
        }

        /**
         * 返回图书信息列表
         */
        vm.returnBookList = function () {
            window.parent.$("#borrowBookWindow").data("kendoWindow").close()
            $state.go("bookDetailList");
        }
    }
})();
