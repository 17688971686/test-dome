(function () {
    'use strict';

    angular.module('app').controller('sharingPlatlformCtrl', sharingPlatlform);

    sharingPlatlform.$inject = ['$location', '$state','$http','sharingPlatlformSvc'];

    function sharingPlatlform($location,$state, $http,sharingPlatlformSvc) {
        var vm = this;
        vm.title = '共享资料管理';

        activate();
        function activate() {
            sharingPlatlformSvc.grid(vm);
        }

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    sharingPlatlformSvc.deleteSharingPlatlform(vm, id);
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
        
        //查询
        vm.querySharing = function(){
        	vm.gridOptions.dataSource.read();
        }

    }
})();
