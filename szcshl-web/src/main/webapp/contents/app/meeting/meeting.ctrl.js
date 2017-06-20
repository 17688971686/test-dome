(function () {
    'use strict';

    angular.module('app').controller('meetingCtrl', meeting);

    meeting.$inject = ['$location','meetingSvc']; 

    function meeting($location, meetingSvc) {
        /* jshint validthis:true */
    	var vm = this;
        vm.title = '会议室列表';
        vm.model={};
        vm.del = function (id) {
             common.confirm({
            	 vm:vm,
            	 title:"",
            	 msg:"确认删除数据吗？",
            	 fn:function () {
                  	$('.confirmDialog').modal('hide');             	
                    meetingSvc.deleteMeeting(vm,id);
                 }
             })
        }
        vm.used=function(id){
        	vm.model.id=id;
        	vm.model.mrStatus="1";
        	meetingSvc.roomUseState(vm);
        	//vm.isUse=false;
        }

        //停用会议室
        vm.stoped=function(id){
            common.confirm({
                vm:vm,
                title:"",
                msg:"确定停用会议室么？",
                fn:function () {
                    $('.confirmDialog').modal('hide');
                    vm.model.id=id;
                    vm.model.mrStatus="0";
                    meetingSvc.roomUseState(vm);;
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
        //会议室查询
        vm.queryMeeting = function(){
        	meetingSvc.queryMeeting(vm);
        }

        activate();
        function activate() {
            meetingSvc.grid(vm);
        }
    }
})();
