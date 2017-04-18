(function () {
    'use strict';

    angular
        .module('app')
        .controller('roomCtrl', room);

    room.$inject = ['$location','roomSvc','$scope']; 

    function room($location, roomSvc,$scope) {
        /* jshint validthis:true */
    	var vm = this;
        vm.title = '会议室预定列表';
        

        vm.del = function (id) {        	
        	
             common.confirm({
            	 vm:vm,
            	 title:"",
            	 msg:"确认删除数据吗？",
            	 fn:function () {
                  	$('.confirmDialog').modal('hide');             	
                    roomSvc.deleteroom(vm,id);
                 }
             })
        }
        vm.dels = function () {     
        	var selectIds = common.getKendoCheckId('.grid');
        	
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
                vm.del(idStr);
            }   
       }
        activate();
        function activate() {
        	
        	//调用room.svc.js的初始化方法
           roomSvc.initRoom(vm);
        }
    }
})();
