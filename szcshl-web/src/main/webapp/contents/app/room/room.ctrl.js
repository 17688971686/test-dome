(function () {
    'use strict';

    angular.module('app').controller('roomCtrl', room);

    room.$inject = ['bsWin', 'roomSvc', '$rootScope', '$state'];

    function room(bsWin, roomSvc, $rootScope, $state) {
        var vm = this;
        vm.title = '会议室预定列表';
        vm.initSuccess = false;
        vm.model = {
            businessId : $state.params.businessId,
            businessType : $state.params.businessType,
        };
        vm.startDateTime = new Date("2006/6/1 08:00");
        vm.endDateTime = new Date("2030/6/1 21:00");
        vm.currentDate = "";
        vm.reportName="会议室安排表";//默认会议室导出文件名
        vm.mrID = "";//会议室ID

        activate();
        function activate() {
            roomSvc.initRoom(vm);
            roomSvc.showMeeting(vm,function(data){
                vm.meetings = {};
                vm.meetings = data;
                roomSvc.initDefaultValue(vm.model.businessId,vm.model.businessType,function(data){
                    vm.model = data;
                    if(vm.meetings && vm.meetings.length > 0){
                        vm.mrID = vm.meetings[0].id;
                        vm.model.mrID = vm.mrID;
                    }
                    roomSvc.updateDataSource(vm);
                })
            });


        }

        //预定会议编辑
        vm.editRoom = function () {
            roomSvc.editRoom(vm);
        }

        //导出本周评审会议安排
        vm.exportThisWeekStage = function () {
            vm.currentDate = $('.k-sm-date-format').html();
            vm.rbType = "0";//表示评审会
            vm.reportName = "本周评审会会议安排表";
            roomSvc.exportThisWeekStage(vm);
        }
        //导出本周全部会议安排
        vm.exportThisWeek = function () {
            vm.currentDate = $('.k-sm-date-format').html();
            vm.rbType = "1";//表示全部
            vm.reportName = "本周全部会议安排表";
            roomSvc.exportThisWeekStage(vm);
        }
        //导出下周全部会议安排
        vm.exportNextWeek = function () {
            var currentDate = $('.k-sm-date-format').html();
            var str = currentDate.split("-")[0].split("/");
            var year = str[0];
            var month = str[1].length == 2 ? str[1] : ("0" + str[1]);
            var day = str[2].length >= 2 ? str[2].substr(0, 2) : ("0" + str[2].substr(0, 1));
            var startDate = new Date(month + "/" + day + "/" + year);
            var endDate = new Date(month + "/" + day + "/" + year);
            startDate.setDate(startDate.getDate() + 8 - startDate.getDay());
            endDate.setDate(endDate.getDate() + 15 - endDate.getDay());
            var start = new Date(startDate);
            var end = new Date(endDate);
            vm.currentDate = start.getFullYear() + "/" + (start.getMonth() + 1) + "/" + start.getDate() + "-" + end.getFullYear() + "/" + (end.getMonth() + 1) + "/" + end.getDate();
            vm.rbType = "1";//表示全部
            vm.reportName = "下周全部会议安排表";
            roomSvc.exportThisWeekStage(vm);
        }
        //导出下周评审会议安排
        vm.exportNextWeekStage = function () {
            var currentDate = $('.k-sm-date-format').html();
            var str = currentDate.split("-")[0].split("/");
            var year = str[0];
            var month = str[1].length == 2 ? str[1] : ("0" + str[1]);
            var day = str[2].length >= 2 ? str[2].substr(0, 2) : ("0" + str[2].substr(0, 1));
            var startDate = new Date(month + "/" + day + "/" + year);
            var endDate = new Date(month + "/" + day + "/" + year);
            startDate.setDate(startDate.getDate() + 8 - startDate.getDay());
            endDate.setDate(endDate.getDate() + 15 - endDate.getDay());
            var start = new Date(startDate);
            var end = new Date(endDate);
            vm.currentDate = start.getFullYear() + "/" + (start.getMonth() + 1) + "/" + start.getDate() + "-" + end.getFullYear() + "/" + (end.getMonth() + 1) + "/" + end.getDate();
            vm.rbType = "0";//表示评审会
            vm.reportName = "下周评审会会议安排表";
            roomSvc.exportThisWeekStage(vm);
        }
        //会议室查询
        vm.findMeeting = function () {
            roomSvc.findMeeting(vm);
        }

        //kendo导出  未实现
        vm.getPDF = function (selector) {
            kendo.drawing.drawDOM($(selector)).then(function (group) {
                kendo.drawing.pdf.saveAs(group, "会议室安排表.pdf");
            });
        }

    }
})();
