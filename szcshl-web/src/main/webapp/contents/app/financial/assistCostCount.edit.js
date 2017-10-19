(function () {
    'use strict';

    angular.module('app').controller('assistCostCountEditCtrl', assistCostCount);

    assistCostCount.$inject = ['$location', 'assistCostCountSvc', '$state','$http'];

    function assistCostCount($location, assistCostCountSvc, $state,$http) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '协审费录入';
        vm.signAssistCost = {};

        activate();
        function activate() {
            assistCostCountSvc.findSingAssistCostList(vm.signAssistCost,function (data) {
                vm.signAssistCostList = data;
            });
        }

        //查询
        vm.queryAssistCost = function(){
        	assistCostCountSvc.assistCostCountList(vm ,function(data){
        		 vm.projectReviewCostDtoList = data.reObj.projectReviewCostDtoList;
        	});
        }
        //重置
        vm.assistCostReset = function(){
        	vm.model = {};
        }
        
        vm.lightState = function(lightState){
            switch (lightState) {
                case "4":          //暂停
                    return $('#span1').html();
                    break;
                case "8":         	//存档超期
                    return $('#span5').html();
                    break;
                case "7":           //超过25个工作日未存档
                    return $('#span4').html();
                    break;
                case "6":          	//发文超期
                    return $('#span3').html();
                    break;
                case "5":          //少于3个工作日
                    return $('#span2').html();
                    break;
                case "1":          //在办
                    return "";
                    break;
                case "2":           //已发文
                    return "";
                    break;
                case "3":           //已发送存档
                    return "";
                    break;
                default:
                    return "";
                    ;
            }
        }

    }
})();
