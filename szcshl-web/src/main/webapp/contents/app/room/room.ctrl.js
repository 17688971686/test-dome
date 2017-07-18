(function () {
    'use strict';

    angular.module('app').controller('roomCtrl', room);

    room.$inject = ['$location','roomSvc','$scope','$state']; 

    function room($location, roomSvc,$scope,$state) {
        /* jshint validthis:true */
    	var vm = this;
        vm.title = '会议室预定列表';
        vm.id = $state.params.id;
        vm.workProgramId = $state.params.workProgramId;     //工作方案ID
        vm.startDateTime = new Date("2006/6/1 08:00");
        vm.endDateTime = new Date("2030/6/1 21:00");
vm.currentDate="";
       
       
        //预定会议编辑
       vm.editRoom = function(){
        	roomSvc.editRoom(vm);
        }
        //预定会议室添加
        vm.addRoom = function(){
        	roomSvc.addRoom(vm);
        }

        //导出本周评审会议安排
        vm.exportThisWeekStage = function(){
        	vm.currentDate=$('.k-sm-date-format').html();
        	vm.rbType="0";//表示评审会
        	roomSvc.exportThisWeekStage(vm);
        }
        //导出本周全部会议安排
        vm.exportThisWeek = function(){
        	vm.currentDate=$('.k-sm-date-format').html();
        	vm.rbType="1";//表示全部
        	roomSvc.exportThisWeekStage(vm);
        }
        //导出下周全部会议安排
        vm.exportNextWeek = function(){
        	console.log(123);
        	var currentDate=$('.k-sm-date-format').html();
        	var str=currentDate.split("-")[0].split("/");
        	var year=str[0];
        	var month=str[1].length==2? str[1] : ("0"+str[1]);
        	var day=str[2].length>=2? str[2].substr(0,2) : ("0"+str[2].substr(0,1));
        	var startDate=new Date(month+"/"+day+"/"+year);
        	var endDate=new Date(month+"/"+day+"/"+year);
        	startDate.setDate(startDate.getDate()+ 8 -startDate.getDay());
        	endDate.setDate(endDate.getDate() + 15- endDate.getDay());
        	var start=new Date(startDate);
        	var end=new Date(endDate);
        	vm.currentDate=start.getFullYear()+"/"+(start.getMonth()+1)+"/"+ start.getDate()+"-" +end.getFullYear()+"/"+(end.getMonth()+1)+"/"+ end.getDate();
        	vm.rbType="1";//表示全部
        	roomSvc.exportThisWeekStage(vm);
        }
        //导出下周评审会议安排
        vm.exportNextWeekStage = function(){
        	var currentDate=$('.k-sm-date-format').html();
        	var str=currentDate.split("-")[0].split("/");
        	var year=str[0];
        	var month=str[1].length==2? str[1] : ("0"+str[1]);
        	var day=str[2].length>=2? str[2].substr(0,2) : ("0"+str[2].substr(0,1));
        	var startDate=new Date(month+"/"+day+"/"+year);
        	var endDate=new Date(month+"/"+day+"/"+year);
        	startDate.setDate(startDate.getDate()+ 8 -startDate.getDay());
        	endDate.setDate(endDate.getDate() + 15- endDate.getDay());
        	var start=new Date(startDate);
        	var end=new Date(endDate);
        	vm.currentDate=start.getFullYear()+"/"+(start.getMonth()+1)+"/"+ start.getDate()+"-" +end.getFullYear()+"/"+(end.getMonth()+1)+"/"+ end.getDate();
        	vm.rbType="0";//表示评审会
        	roomSvc.exportThisWeekStage(vm);
        }
        //会议室查询
        vm.findMeeting = function(){
        	roomSvc.findMeeting(vm);
        }
        
        //kendo导出  未实现
          vm.getPDF =function(selector) {
   		   kendo.drawing.drawDOM($(selector)).then(function (group) {
           kendo.drawing.pdf.saveAs(group, "会议室安排表.pdf");
            
       });
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
           roomSvc.showMeeting(vm);
            roomSvc.initRoom(vm);
            roomSvc.initWorkProgram(vm);
            roomSvc.initFindUserName(vm);
        }
    }
})();
