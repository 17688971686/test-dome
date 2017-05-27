(function () {
    'use strict';

    angular
        .module('app')
        .controller('roomCtrl', room);

    room.$inject = ['$location','roomSvc','$scope','$state']; 

    function room($location, roomSvc,$scope,$state) {
        /* jshint validthis:true */
    	var vm = this;
        vm.title = '会议室预定列表';
        vm.id = $state.params.id;
      
        //预定会议编辑
       vm.editRoom = function(){
        	roomSvc.editRoom(vm);
        }
        //预定会议室添加
        vm.addRoom = function(){
        	roomSvc.addRoom(vm);
        }
        //结束时间不能小开始时间
        vm.startEnd = function(){
        	roomSvc.startEnd(vm);
        }
       
        vm.onWClose = function(){
        	//window.parent.$("#customEditorTemplate").data("kendoWindow").close();
        }
        //导出本周评审会议安排
        vm.exportWeek = function(){
        	roomSvc.exportWeek();
        }
        //导出本周全部会议安排
        vm.exportThisWeek = function(){
        	
        	roomSvc.exportThisWeek();
        }
        //导出下周全部会议安排
        vm.exportNextWeek = function(){
        	
        	roomSvc.exportNextWeek();
        }
        //导出下周评审会议安排
        vm.stageNextWeek = function(){
        	
        	roomSvc.stageNextWeek();
        }
        //会议室查询
        vm.findMeeting = function(){
        	roomSvc.findMeeting(vm);
        }
        
        vm.del = function (id) {        	
        	
             common.confirm({
            	 vm:vm,
            	 title:"",
            	 msg:"确认删除数据吗？",
            	 fn:function () {
                  	$('.confirmDialog').modal('hide');             	
                    roomSvc.deleteRoom(vm,id);
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
        	if(vm.isUpdate){
        		//roomSvc.editRoom(vm);
        	}
        	//调用room.svc.js的初始化方法
           roomSvc.initRoom(vm);
           roomSvc.showMeeting(vm);
           //roomSvc.findUser(vm);
        }
    }
})();
