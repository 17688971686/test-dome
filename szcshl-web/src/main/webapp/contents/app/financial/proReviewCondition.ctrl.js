(function () {
    'use strict';

    angular.module('app').controller('proReviewConditionCtrl', proReviewCondition);

    proReviewCondition.$inject = ['$location', 'proReviewConditionSvc','$state','$http'];

    function proReviewCondition($location, proReviewConditionSvc,$state,$http) {
        var vm = this;
        vm.title = '项目评审情况统计';
        vm.model={};
        vm.total={};
         //项目评审情况统计
        vm.proReviewConCount = function () {
            //console.log(vm.model);
            if(vm.model.beginTime != null && vm.model.beginTime!=""){
                var timeArr =  vm.model.beginTime.split("-");
                vm.year = timeArr[0];
                vm.begMonth = timeArr[1];
                if(vm.begMonth.charAt(0)=='0'){
                    vm.begMonth = vm.begMonth.charAt(1);
                }
            }
            if(vm.model.endTime != null && vm.model.endTime != ""){
                var timeArr =  vm.model.endTime.split("-");
                vm.year = timeArr[0];
                vm.endMonth = timeArr[1];
                if(vm.endMonth.charAt(0)=='0'){
                    vm.endMonth = vm.endMonth.charAt(1);
                }
            }
            proReviewConditionSvc.proReviewConCount(vm,function(data){
                vm.protReviewConditionList = data.reObj.protReviewConditionList;
                calTotal();
            });
        }

        //重置查询表单
        vm.formReset = function(){
            vm.model = {};
        }

        //自定义加法运算
        function addNum (num1, num2) {
            var sq1,sq2,m;
            try {
                sq1 = num1.toString().split(".")[1].length;
            }
            catch (e) {
                sq1 = 0;
            }
            try {
                sq2 = num2.toString().split(".")[1].length;
            }
            catch (e) {
                sq2 = 0;
            }
            m = Math.pow(10,Math.max(sq1, sq2));
            return (num1 * m + num2 * m) / m;
        }

        function calTotal(){
            vm.total.projectcount = 0;
            vm.total.declarevalue = 0;
            vm.total.authorizevalue = 0;
            vm.total.ljhj = 0;
            vm.total.hjl = 0;
            vm.protReviewConditionList.forEach(function(p ,index){
                if(p.proCount != undefined){
                    vm.total.projectcount += p.proCount;
                }
                if(p.declareValue != undefined){
                    vm.total.declarevalue = addNum(vm.total.declarevalue ,p.declareValue);
                }
                if(p.authorizeValue != undefined){
                    vm.total.authorizevalue = addNum(vm.total.authorizevalue ,p.authorizeValue);
                }
                if(p.ljhj != undefined){
                    vm.total.ljhj = addNum(vm.total.ljhj ,p.ljhj);
                }
            });
            vm.total.hjl =   Math.round(vm.total.ljhj/vm.total.declarevalue  * 10000) / 10000*100;
        }
        activate();
        function activate() {
                var date=new Date;
                var year=date.getFullYear();
                var month=date.getMonth()+1;
                var day = date.getDate();
                month =(month<10 ? "0"+month:month);
                vm.model.beginTime = year.toString()+"-"+"01";
                vm.model.endTime = year.toString()+"-"+month.toString();
                vm.year = year;
                vm.begMonth = "1";
                vm.endMonth = month;
                vm.begDay = "31";
                vm.endDay = day;
                proReviewConditionSvc.proReviewConCount(vm,function(data){
                vm.protReviewConditionList = data.reObj.protReviewConditionList;
                calTotal();
            });
        }
    }
})();
