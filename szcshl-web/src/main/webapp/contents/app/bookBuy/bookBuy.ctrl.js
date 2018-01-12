(function () {
    'use strict';

    angular.module('app').controller('bookBuyCtrl', bookBuy);

    bookBuy.$inject = ['$location', 'bookBuySvc','$state','bsWin'];

    function bookBuy($location, bookBuySvc,$state,bsWin) {
        var vm = this;
        vm.title = '图书查询';
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
            vm.model.borrowNum = "";
            vm.model.borrowDate = "";
            vm.model.returnDate = "";
            vm.model.bookBorrower =$("#curName").val();
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
            bookBuySvc.saveBorrowBookDetail(vm,function(data){
                if(data.flag || data.reCode == 'ok'){
                    //保存成功重新跳转，要不然路径不对
                    bsWin.alert("保存成功！",function(){
                        window.parent.$("#borrowBookWindow").data("kendoWindow").close();
                        vm.searchForm();
                    });
                }else{
                    bsWin.alert(data.reMsg);
                }
            });
        }

        /**
         * 返回图书信息列表
         */
        vm.returnBookList = function () {
            window.parent.$("#borrowBookWindow").data("kendoWindow").close();
             vm.searchForm();
           // $state.go("bookDetailList");
        }
        /**
         *限制借书的数量
         */
        vm.comparisonSize=function () {
            if (vm.model.borrowNum> vm.model.storeConfirm) {
                vm.model.borrowNum = vm.model.storeConfirm;
            }

        }
    }
})();
