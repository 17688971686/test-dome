(function () {
    'use strict';

    angular.module('app').controller('bookBorrowCtrl', bookBorrow);

    bookBorrow.$inject = ['$location', 'bookBuySvc','bsWin'];

    function bookBorrow($location, bookBuySvc,bsWin) {
        var vm = this;
        vm.title = '借书列表';
        vm.model = {};
        vm.searchModel = {};
        activate();
        function activate() {
            bookBuySvc.bookBorrowGrid(vm,function(data){
                vm.bookBorrowDetailList = data.reObj.bookBorrowDetailList;
                vm.bookBorrowSumDtoList = data.reObj.bookBorrowSumDtoList;
            });
        }
        //表单查询
        vm.searchForm = function(){
           bookBuySvc.bookBorrowGrid(vm,function(data){
             vm.bookBorrowDetailList = data.reObj.bookBorrowDetailList;
             vm.bookBorrowSumDtoList = data.reObj.bookBorrowSumDtoList;

             });
        }
        //重置查询表单
        vm.formReset = function(){
            vm.searchModel = {};
        }

        //还书
        vm.returnBook = function(id,bookNo,booksName,bookBorrower,borrowNum,returnDate){
            vm.model.id = id;
            vm.model.bookNo = bookNo;
            vm.model.booksName = booksName;
            vm.model.bookBorrower = bookBorrower;
            vm.model.returnDate = new Date(returnDate).Format("yyyy-MM-dd");
            vm.model.returnBorrower = $("#curName").val();
            $("#returnBookWindow").kendoWindow({
                width: "38%",
                height: "300px",
                title: "图书借还",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        /**
         * 保存还书信息
         */
        vm.saveReturnBookDetail = function() {
            bookBuySvc.saveReturnBookDetail(vm,function(data){
                if(data.flag || data.reCode == 'ok'){
                    //保存成功重新跳转，要不然路径不对
                    bsWin.alert("保存成功！",function(){
                        window.parent.$("#returnBookWindow").data("kendoWindow").close();
                        $state.go("bookBorrowList");
                    });
                }else{
                    bsWin.alert(data.reMsg);
                }
            });
        }

        /**
         * 返回还书信息列表
         */
        vm.returnBookList = function () {
            window.parent.$("#returnBookWindow").data("kendoWindow").close();
            $state.go('bookBorrowList');
        }
    }
})();
