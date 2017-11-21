(function () {
    'use strict';

    angular.module('app').controller('roomCtrl', room);

    room.$inject = ['bsWin', 'roomSvc', '$state'];

    function room(bsWin, roomSvc, $state) {
        var vm = this;
        vm.title = '会议室预定列表';
        vm.initSuccess = false;
        vm.model = {
            businessId: $state.params.businessId,
            businessType: $state.params.businessType,
        };
        vm.startDateTime = new Date("2005/6/1 08:00");
        vm.startRbDate = new Date();
        vm.endDateTime = new Date("2030/6/1 21:00");
        vm.currentDate = "";
        vm.reportName = "会议室安排表";//默认会议室导出文件名
        vm.search = {};     //查询对象
        vm.loadScheduler = false;

        activate();
        function activate() {
            //加载会议室
            roomSvc.showMeeting(vm, function (data) {
                //初始化行程控件
                roomSvc.initScheduler(vm);
                vm.meetings = {};
                vm.meetings = data;
                roomSvc.initDefaultValue(vm.model.businessId, vm.model.businessType, function (data2) {
                    vm.model = data2;
                    if (vm.meetings && vm.meetings.length > 0) {
                        vm.search.mrID = vm.meetings[0].id;
                        vm.model.mrID =  vm.search.mrID;
                        //查询预订会议数据
                        vm.findMeeting();
                    }
                })
            });
        }

        /**
         * 切换日期时查询
         */
        vm.reQueryDate = function (e) {
            var now = e.date;
            var weekStartDate, weekEndDate;
            var nowDayOfWeek = now.getDay(); //今天本周的第几天
            //星期天
            if (nowDayOfWeek == 0) {
                weekStartDate = now.Days(-6);
                weekEndDate = now.Format("yyyy-MM-dd");
                //星期一
            } else if (nowDayOfWeek == 1) {
                weekStartDate = now.Format("yyyy-MM-dd");
                ;
                weekEndDate = now.Days(6);
            } else {
                weekStartDate = now.Days(-(nowDayOfWeek - 1));
                weekEndDate = now.Days(7 - nowDayOfWeek);
            }
            vm.search.beginTimeStr = weekStartDate;
            vm.search.endTimeStr = weekEndDate;
            //查询预订会议数据
            vm.findMeeting();
        }

        //会议室查询
        vm.findMeeting = function () {
            //查询预订会议数据
            roomSvc.queryBookRoom(vm.search, function (data) {
                roomSvc.setSCDataSource(vm,data);      //设置数据源
                /*vm.model.mrID =  vm.search.mrID;*/
            });
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


    }
})();
