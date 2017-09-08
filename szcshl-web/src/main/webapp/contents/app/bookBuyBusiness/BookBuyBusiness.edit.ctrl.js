(function () {
    'use strict';

    angular.module('app').controller('bookBuyBusinessEditCtrl', bookBuyBusiness);

    bookBuyBusiness.$inject = ['$location', 'bookBuyBusinessSvc', '$state','bsWin'];

    function bookBuyBusiness($location, bookBuyBusinessSvc, $state,bsWin) {
        /* jshint validthis:true */
        var vm = this;
        vm.conMaxIndex = 0;                   //条件号
        vm.conditions = new Array();         //条件列表
        vm.title = '添加图书采购申请业务信息';
        vm.isuserExist = false;
        vm.businessId = $state.params.businessId;
        if (vm.businessId) {
            vm.isUpdate = true;
            vm.title = '更新图书采购申请业务信息';
        }

        vm.create = function () {
            bookBuyBusinessSvc.createBookBuyBusiness(vm);
        };
        vm.update = function () {
            bookBuyBusinessSvc.updateBookBuyBusiness(vm);
        };
        //添加图书详细信息
        vm.addCondition = function () {
            vm.condition = {};
            vm.condition.sort = vm.conMaxIndex+1;
            vm.conditions.push(vm.condition);
            vm.conMaxIndex++;
        }
        //删除图书详细信息
        vm.removeCondition = function () {
            var isCheck = $("#conditionTable input[name='epConditionSort']:checked");
            if (isCheck.length > 0) {
                bsWin.confirm({
                    title: "询问提示",
                    message: "删除数据不可恢复，确定删除么？",
                    onOk: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < isCheck.length; i++) {
                            $.each(vm.conditions,function(c,con){
                                if (isCheck[i].value == con.sort) {
                                    if (con.id) {
                                        ids.push(con.id);
                                    }else{
                                        vm.conditions.splice(c, 1);
                                    }
                                }
                            })
                        }
                        if(ids.length > 0){
                            expertConditionSvc.deleteSelConditions(ids.join(","),vm.isCommit,function(data){
                                if(data.flag || data.reCode == 'ok'){
                                    bsWin.success("操作成功！");
                                    $.each(ids,function(i,id){
                                        $.each(vm.conditions,function(c,con){
                                            if (id == con.sort) {
                                                vm.conditions.splice(c, 1);     //没有保存抽取条件的直接删除
                                            }
                                        })
                                    })
                                }else{
                                    bsWin.error(data.reMsg);
                                }
                            });
                        }else{
                            bsWin.success("操作成功！");
                        }
                    },
                });
            }else{
                bsWin.alert("请选择要删除的抽取条件！");
            }

        }

        vm.saveCondition = function () {
                if (buildCondition(false)) {
                    bookBuyBusinessSvc.saveBookBuyBusinessDetail(vm.conditions,function(data){
                        if(data.flag || data.reCode == 'ok'){
                            bsWin.success("保存成功！");
                        }else{
                            bsWin.error(data.reMsg);
                        }
                    });
                } else {
                    bsWin.error("请添加后再保存");
                }

        }

        function buildCondition(checkId) {
            //TODO:表单参数校验待核实
            if (vm.conditions.length > 0) {
                var validateResult = true;
                vm.conditions.forEach(function (p, number) {
                    p.booksName = $("#booksName" + p.sort).val();
                    p.booksType = $("#booksType" + p.sort).val();
                    p.publishingCompany = $("#publishingCompany" + p.sort).val();
                    p.bookNo = $("#bookNo" + p.sort).val();
                    p.author = $("#author" + p.sort).val();
                    p.bookNumber = $("#bookNumber" + p.sort).val();
                    p.storeConfirm = $("#storeConfirm" + p.sort).val();
                    p.booksPrice = $("#booksPrice" + p.sort).val();
                });
                return validateResult;
            } else {
                return false;
            }
        }


        activate();
        function activate() {
            if (vm.isUpdate) {
                bookBuyBusinessSvc.getBookBuyBusinessById(vm);
            }
        }
    }
})();
