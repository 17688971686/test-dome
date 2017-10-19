(function () {
    'use strict';

    angular.module('app').controller('expertRevConCountCtrl', expertRevConCount);

    expertRevConCount.$inject = ['$location', 'expertRevConCountSvc','$state','$http','bsWin'];

    function expertRevConCount($location, expertRevConCountSvc,$state,$http,bsWin) {
        var vm = this;
        vm.title = '专家评审基本情况统计';
        vm.model={};

        vm.expertRevConCount = function () {
            if(vm.model.reportType == 3){
                if(vm.model.beginTime==undefined || vm.model.beginTime=='' || vm.model.endTime==undefined || vm.model.endTime==''){
                    bsWin.alert("会议起止时间不能为空");
                    return;
                }
            }
            expertRevConCountSvc.expertRevConCount(vm,function(data){
                if(vm.model.reportType == 1){
                    vm.expertReviewConDtoList = data.reObj.expertReviewConDtoList;
                }else if(vm.model.reportType == 2){
                    vm.expertRevConSimDtoList = data.reObj.expertRevConSimDtoList;
                }else if(vm.model.reportType == 3){
                    vm.expertRevConCompDtoList = data.reObj.expertRevConCompDtoList;
                }
            });
        }

        vm.dropSlelectedCount = function () {
            if(vm.model.reportType == 1 || vm.model.reportType==2){
                var date=new Date;
                var year=date.getFullYear();
                var month=date.getMonth()+1;
                var day = date.getDate();
                month =(month<10 ? "0"+month:month);
                vm.model.beginTime = year.toString()+"-01-01";
                vm.model.endTime = year.toString()+"-"+month.toString()+"-"+day;
            }else if(vm.model.reportType == 3){
                vm.model.beginTime = getQuarterStartDate();
                vm.model.endTime = getQuarterEndDate();
            }
            expertRevConCountSvc.expertRevConCount(vm,function(data){
                if(vm.model.reportType == 1){
                    vm.expertReviewConDtoList = data.reObj.expertReviewConDtoList;
                }else if(vm.model.reportType == 2){
                    vm.expertRevConSimDtoList = data.reObj.expertRevConSimDtoList;
                }else if(vm.model.reportType == 3){
                    vm.expertRevConCompDtoList = data.reObj.expertRevConCompDtoList;
                }
            });
        }
        //获得本季度的开始日期
        function getQuarterStartDate() {
            var now = new Date(); //当前日期
            var nowYear = now.getFullYear(); //当前年
            var quarterStartDate = new Date(nowYear, getQuarterStartMonth(), 1);
            return formatDate(quarterStartDate);
        }
        //或的本季度的结束日期
        function getQuarterEndDate() {
            var now = new Date(); //当前日期
            var nowYear = now.getFullYear(); //当前年
            var quarterEndMonth = getQuarterStartMonth() + 2;
            var quarterStartDate = new Date(nowYear, quarterEndMonth,
                getMonthDays(quarterEndMonth));
            return formatDate(quarterStartDate);
        }

        //获得某月的天数
        function getMonthDays(myMonth) {
            var now = new Date(); //当前日期
            var nowYear = now.getYear(); //当前年
            var monthStartDate = new Date(nowYear, myMonth, 1);
            var monthEndDate = new Date(nowYear, myMonth + 1, 1);
            var days = (monthEndDate - monthStartDate) / (1000 * 60 * 60 * 24);
            return days;
        }

        //获得本季度的开始月份
        function getQuarterStartMonth() {
            var now = new Date(); //当前日期
            var nowMonth = now.getMonth(); //当前月
            var quarterStartMonth = 0;
            if (nowMonth < 3) {
                quarterStartMonth = 0;
            }
            if (2 < nowMonth && nowMonth < 6) {
                quarterStartMonth = 3;
            }
            if (5 < nowMonth && nowMonth < 9) {
                quarterStartMonth = 6;
            }
            if (nowMonth > 8) {
                quarterStartMonth = 9;
            }
            return quarterStartMonth;
        }

        //格式化日期：yyyy-MM-dd
        function formatDate(date) {
            var myyear = date.getFullYear();
            var mymonth = date.getMonth() + 1;
            var myweekday = date.getDate();
            if (mymonth < 10) {
                mymonth = "0" + mymonth;
            }
            if (myweekday < 10) {
                myweekday = "0" + myweekday;
            }
            return (myyear.toString() + "-" + mymonth.toString() + "-" + myweekday.toString());
        }


        //重置查询表单
        vm.formReset = function(){
            var reportType = vm.model.reportType;
            vm.model = {};
            vm.model.reportType = reportType;
        }
        activate();
        function activate() {
                var date=new Date;
                var year=date.getFullYear();
                var month=date.getMonth()+1;
                var day = date.getDate();
                month =(month<10 ? "0"+month:month);
                vm.model.beginTime = year.toString()+"-01-01";
                vm.model.endTime = year.toString()+"-"+month.toString()+"-"+day;
                vm.model.reportType=1;
                expertRevConCountSvc.expertRevConCount(vm,function(data){
                vm.expertReviewConDtoList = data.reObj.expertReviewConDtoList;
            });
        }
    }
})();
