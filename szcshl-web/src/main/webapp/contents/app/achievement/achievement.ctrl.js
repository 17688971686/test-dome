(function () {
    'use strict';

    angular.module('app').controller('achievementListCtrl', achievementList);

    achievementList.$inject = ['$location', 'achievementSvc','$state','$http','bsWin'];

    function achievementList($location, achievementSvc,$state,$http,bsWin) {
        var vm = this;
        vm.title = '工作业绩统计表';
        vm.model={};
        vm.mainDoc = {};
        vm.assistDoc = {};
        vm.model.year = $state.params.year;
        vm.model.quarter = $state.params.quarter;
        activate();
        function activate() {
            achievementSvc.achievementSum(vm,function (data) {
                if(data.flag || data.reCode == 'ok'){
                    vm.achievementSumList = data.reObj.achievementSumList;
                    if(vm.achievementSumList.length > 0){
                        vm.assistDoc = vm.achievementSumList[0];
                        vm.mainDoc = vm.achievementSumList[1];
                    }
                    vm.achievementMainList =  data.reObj.achievementMainList;
                    vm.achievementAssistList =  data.reObj.achievementAssistList;
                }
            })
        }

        /**
         * 主办人评审项目一览表
         */
        vm.showMainDocDetail = function () {
            $("#mainDocDetail").kendoWindow({
                width: "80%",
                height: "680px",
                title: "主办人评审项目一览表",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        /**
         * 协办人评审项目一览表
         */
        vm.showAssistDocDetail = function () {
            $("#assistDocDetail").kendoWindow({
                width: "80%",
                height: "680px",
                title: "协办人评审项目一览表",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        vm.countAchievementDetail = function () {
            achievementSvc.achievementSum(vm,function (data) {
                if(data.flag || data.reCode == 'ok'){
                    vm.achievementSumList = data.reObj.achievementSumList;
                    if(vm.achievementSumList.length > 0){
                        vm.assistDoc = vm.achievementSumList[0];
                        vm.mainDoc = vm.achievementSumList[1];
                    }
                    vm.achievementMainList =  data.reObj.achievementMainList;
                    vm.achievementAssistList =  data.reObj.achievementAssistList;
                }
            })
        }
    }
})();
