(function () {
    'use strict';

    angular.module('app').controller('monthlyNewsletterCtrl', monthlyNewsletter);

    monthlyNewsletter.$inject = ['bsWin', 'monthlyNewsletterSvc'];

    function monthlyNewsletter(bsWin, monthlyNewsletterSvc) {
        var vm = this;
        vm.title = '月报简报列表';

        activate();
        function activate() {
            monthlyNewsletterSvc.monthlyNewsletterGrid(vm);
            monthlyNewsletterSvc.monthlyDeleteGrid(vm);
        }
      
        vm.del = function (id) {
            bsWin.confirm({
                title: "询问提示",
                message: "确认删除数据吗？",
                onOk: function () {
                    monthlyNewsletterSvc.deleteMonthlyNewsletter(id,function(data){
                        if(data.flag || data.reCode=='ok'){
                            bsWin.alert("操作成功！",function(){
                                vm.gridOptions.dataSource.read();
                                vm.monthlyDeleteGridOptions.dataSource.read();
                            })
                        }
                    });
                }
            });
        }

        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                bsWin.alert("请选择要删除的数据！");
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.del(idStr);
            }
        };

    }
})();
