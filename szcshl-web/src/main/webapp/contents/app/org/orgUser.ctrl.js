(function () {
    'use strict';

    angular
        .module('app')
        .controller('orgUserCtrl', org);

    org.$inject = ['$location','$state','orgSvc','orgUserSvc']; 

    function org($location,$state, orgSvc,orgUserSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.id = $state.params.id;

        vm.showAddUserDialog = function () {
        	$('.addUser').modal({
                backdrop: 'static',
                keyboard:false
            });
        	 vm.orgUserGrid.dataSource.read();
        };
        vm.closeAddUserDialog=function(){
        	$('.addUser').modal('hide');		
        	
        }
        vm.add = function (userId) {
        	orgUserSvc.add(vm,userId);
        };
        vm.remove = function (userId) {
        	orgUserSvc.remove(vm,userId);
        };
        vm.removes = function () {     
        	var selectIds = common.getKendoCheckId('.gird');
            if (selectIds.length == 0) {
                common.alert({
                	vm:vm,
                	msg:'请选择数据'
                	
                });
            } else {
            	var ids=[];
                for (var i = 0; i < selectIds.length; i++) {
                	ids.push(selectIds[i].value);
				}  
                var idStr=ids.join(',');
                vm.remove(idStr);
            }   
       }
       //查询
       vm.query=function () {
           vm.gridOptions.dataSource._skip= 0;
           vm.gridOptions.dataSource.read();
       }
       //重置
        vm.formReset=function () {
            var tab = $("#form").find('input,select').not(":submit, :reset, :image, :disabled,:hidden");
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }

        activate();
        function activate() {
            /**
             * 部门用户查询
             */
        	orgUserSvc.orgUserGrid(vm);
            /**
             * 未分配部门用户查询
             */
        	orgUserSvc.allUserGrid(vm);
        }
    }
})();
