(function () {
    'use strict';

    angular.module('app').controller('deptOfficeUserCtrl', deptOfficeUser);

    deptOfficeUser.$inject = ['$location','$state','deptSvc','deptOfficeUserSvc'];

    function deptOfficeUser($location,$state,deptSvc, deptOfficeUserSvc) {
        var vm = this;
        vm.title = '办事处人员列表';
        vm.id=$state.params.deptId;
        vm.showAddUserDialog = function (){
        	$('.addUser').modal({
                backdrop: 'static',
                keyboard:false
            });
        	 vm.deptOfficeGrid.dataSource.read();
        }
        vm.closeAddUserDialog=function(){
        	$('.addUser').modal('hide');		
        	
        }
        vm.addOfficeUser = function(officeId){
        	deptOfficeUserSvc.addOfficeUser(vm,officeId);
        }
        
        vm.remove = function (officeId) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    deptOfficeUserSvc.deletedeptOfficeUser(vm, officeId);
                }
            });
        }
        vm.removes = function () {
            var selectIds = common.getKendoCheckId('.deptOfficeGrid');
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
                vm.remove(idStr);
            }
        };
        
      
        activate();
        function activate() {
            deptOfficeUserSvc.grid(vm);
            deptOfficeUserSvc.deptOfficeGrid(vm);
        }
    }
})();
