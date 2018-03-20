(function () {
    'use strict';

    angular.module('app').controller('roomCountCtrl', roomCount);

    roomCount.$inject = ['adminSvc', 'roomCountSvc'];

    function roomCount(adminSvc, roomCountSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '预定会议统计列表';

        vm.queryRoomCount = function () {
            roomCountSvc.queryRoomCount(vm);
        }

        vm.ResetRoomCount = function () {
            roomCountSvc.cleanValue();
        }

        activate();
        function activate() {
            roomCountSvc.grid(vm);
            //会议室
            roomCountSvc.roomList(vm);
            //用户
            roomCountSvc.findAllUsers(vm,function (data) {
                vm.userlist = {};
                vm.userlist = data;
            });
            //部门
            adminSvc.initSignList(function(data){
                if(data.flag || data.reCode == 'ok'){
                    vm.orgDeptList = data.reObj;
                }
            });
        }
    }
})();
