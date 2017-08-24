(function () {
    'use strict';

    angular.module('app').controller('meetingEditCtrl', meeting);

    meeting.$inject = ['$location','meetingSvc','$state']; 

    function meeting($location, meetingSvc,$state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加会议室';
        vm.isuserExist=false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新会议室';
        }
        
        vm.create = function () {
        	//alert("sss");
        	meetingSvc.createMeeting(vm);
        };
        vm.update = function () {
        	meetingSvc.updateMeeting(vm);
        };      

        activate();
        function activate() {
        	if (vm.isUpdate) {
        		meetingSvc.getMeetingById(vm);
            } else {
            	
            }
        }
    }
})();
