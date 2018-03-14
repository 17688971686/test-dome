(function () {
    'use strict';

    angular.module('app').controller('partyMeetingCtrl', partyMeeting);

    partyMeeting.$inject = ['partySvc' , 'bsWin' , 'sharingPlatlformSvc'];

    function partyMeeting(partySvc , bsWin , sharingPlatlformSvc) {
        var vm = this;
        vm.title = '党员会议添加';        		//标题
        vm.party = {};

        active();
        function active(){
            sharingPlatlformSvc.initOrgAndUser(vm);
        }

    }
})();
