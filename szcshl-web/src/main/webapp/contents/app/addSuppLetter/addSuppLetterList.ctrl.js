(function () {
    'use strict';

    angular.module('app').controller('addSuppLetterListCtrl', addSuppLetter);
    addSuppLetter.$inject = ['$location', 'addSuppLetterSvc', '$state', 'bsWin'];

    function addSuppLetter($location, addSuppLetterSvc, $state, bsWin) {
        var vm = this;
        vm.suppletter = {}; //补充资料对象$state
        vm.suppletter.businessId = $state.params.businessId;        //业务ID
        vm.title = '登记补充资料';
        activate();
        function activate() {
            addSuppLetterSvc.initSuppListDate(vm.suppletter.businessId,function(data){
                vm.suppletterlist = data;
            });
        }
        //生成发文字号
        vm.getFilenum = function (id) {
            addSuppLetterSvc.createFilenum(id,function(data){
                if(data.flag || data.reCode == 'ok'){
                    bsWin.alert("操作成功！");
                    $.each(vm.suppletterlist,function (i,sl) {
                        if(sl.id == data.reObj.id){
                            sl.filenum = data.reObj.filenum;
                        }
                    })
                }else{
                    bsWin.alert(data.reMsg);
                }
            });
        }
        //跳转到编辑页面
        vm.findByIdAddSuppLetter = function (id) {
        	$state.go('addSuppLetterEdit', {id: id});
        }

        vm.removeAddSuppLetter = function ( index , id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    vm.suppletterlist.splice(index , 1);
                    addSuppLetterSvc.deleteById(vm, id);
                }
            });
        }
    }
})();
