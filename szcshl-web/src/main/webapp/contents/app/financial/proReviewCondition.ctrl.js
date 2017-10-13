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
            if(vm.model.beginTime != null){
                var timeArr =  vm.model.beginTime.split("-");
                vm.year = timeArr[0];
                vm.begMonth = timeArr[1];
                vm.begDay = timeArr[2];
            }
            if(vm.model.endTime != null){
                var timeArr =  vm.model.endTime.split("-");
                vm.endMonth = timeArr[1];
                vm.endDay = timeArr[2];
            }
            proReviewConditionSvc.proReviewConCount(vm,function(data){
                vm.protReviewConditionList = data.reObj.protReviewConditionList;
            });
        }

        //重置查询表单
        vm.formReset = function(){
            vm.model = {};
        }

        activate();
        function activate() {
                var date=new Date;
                var year=date.getFullYear();
                var month=date.getMonth()+1;
                var day = date.getDate();
                month =(month<10 ? "0"+month:month);
                vm.model.beginTime = year.toString()+"-"+"01"+"-31";
                vm.model.endTime = year.toString()+"-"+month.toString()+"-"+day.toString();
                vm.year = year;
                vm.begMonth = "1";
                vm.endMonth = month;
                vm.begDay = "31";
                vm.endDay = day;
                proReviewConditionSvc.proReviewConCount(vm,function(data){
                vm.protReviewConditionList = data.reObj.protReviewConditionList;
                vm.total.projectcount = 0;
                vm.total.declarevalue = 0;
                vm.total.authorizevalue = 0;
                vm.total.ljhj = 0;
                vm.total.hjl = "";
                vm.protReviewConditionList.forEach(function(p ,index){
                       if(p.proCount != undefined){
                           vm.total.projectcount += p.proCount;
                       }
                        if(p.declareValue != undefined){
                            vm.total.declarevalue += p.declareValue;
                        }
                        if(p.authorizeValue != undefined){
                            vm.total.authorizevalue += p.authorizeValue;
                        }
                        if(p.ljhj != undefined){
                            vm.total.ljhj += p.ljhj;
                        }
                    });


            });
        }
    }
})();
