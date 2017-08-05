(function () {
    'use strict';

    angular.module('app').controller('pauseProjectCtrl', pauseProject);

    pauseProject.$inject = ['$location','pauseProjectSvc','$state',"adminSvc","ideaSvc"];

    function pauseProject($location, pauseProjectSvc,$state,adminSvc,ideaSvc) {
        var vm = this;
        vm.title = '项目暂停审批';        		//标题

        active();
        function active(){
            pauseProjectSvc.grid(vm);
            ideaSvc.initIdea(vm);	//初始化个人常用意见
        }

        /**
         * 暂停项目弹出框
         */
        vm.pauseProjectWindow=function(signid,stopid){
            pauseProjectSvc.pauseProjectWindow(vm,signid,stopid);
        }

        /**
         *更新暂停项目审批信息
         */
        vm.commitProjectStop = function () {
            pauseProjectSvc.updateProjectStop(vm);
        }

        /**
         * 取消
         */
        vm.closewin=function(){
            window.parent.$("#spwindow").data("kendoWindow").close()
        }

        /**
         * 选择部长意见
         */
        vm.selectMinisterIdea=function(){

            vm.projectStop.directorIdeaContent += vm.directorIdea;
        }

        /**
         * 选择分管副主任意见
         */
        vm.selectDirectorIdea=function(){
            vm.projectStop.leaderIdeaContent += vm.leaderIdea;
        }
    }
})();
