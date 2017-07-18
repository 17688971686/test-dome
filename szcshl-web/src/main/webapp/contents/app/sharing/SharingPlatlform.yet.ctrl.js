(function () {
    'use strict';

    angular.module('app').controller('sharingPlatlformYetCtrl', sharingPlatlformYet);

    sharingPlatlformYet.$inject = ['$location', '$state','$http','sharingPlatlformYetSvc'];

    function sharingPlatlformYet($location,$state, $http,sharingPlatlformYetSvc) {
        var vm = this;
        
        vm.title = '共享资料';
        
        vm.model = {};
        
        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    sharingPlatlformYetSvc.deletesharingPlatlformYet(vm, id);
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
        vm.findSharing = function(){
        	vm.gridOptions.dataSource.read();
        }
        //重置
        vm.resetShared = function(){
        	var tab = $("#formSharingPub").find('select');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
        }
       //确认发布
        vm.publish = function(id){
        	vm.model.sharId=id;
        	 common.confirm({
                 vm: vm,
                 title: "",
                 msg: "您确认发布这条数据吗？",
                 fn: function () {
                     $('.confirmDialog').modal('hide');
                     sharingPlatlformYetSvc.publish(vm,id);
                 }
             });
        	
        }
        //取消发布
        vm.closePublish = function(id){
        	vm.model.sharId=id;
        	 common.confirm({
                 vm: vm,
                 title: "",
                 msg: "您取消发布这条数据吗？",
                 fn: function () {
                     $('.confirmDialog').modal('hide');
                     sharingPlatlformYetSvc.publish(vm,id);
                 }
             });
        }

        activate();
        function activate() {
            sharingPlatlformYetSvc.grid(vm);
            sharingPlatlformYetSvc.findAllOrglist(vm);
            sharingPlatlformYetSvc.findAllUsers(vm);
            
        }
    }
})();
