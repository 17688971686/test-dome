(function(){
    'use strict';
    angular.module('app').controller('projectStopFormCtrl' , projectStopForm);
    projectStopForm.$inject = ['$state' , 'pauseProjectSvc'];
    function projectStopForm($state , pauseProjectSvc){
        var vm = this;
        vm.sign = {};
        var signId = $state.params.signId;
        var stopid = $state.params.stopId;
        vm.projectStop = {};
        vm.projectStop.signid = signId;
        activate();
        function activate(){
            pauseProjectSvc.initProject(vm, signId);
            pauseProjectSvc.countUsedWorkday(vm, signId);
            if (stopid != ""){
                vm.showIdea = true ;
                pauseProjectSvc.getProjectStopByStopId(vm,stopid , function(data){
                    vm.projectStop = data;
                    if( vm.projectStop.directorIdeaContent == undefined){
                        vm.projectStop.directorIdeaContent="";
                    }
                    if(vm.projectStop.leaderIdeaContent == undefined){
                        vm.projectStop.leaderIdeaContent="";
                    }
                });
            }

        }

        /**
         *更新暂停项目信息
         */
        vm.commitProjectStop = function () {
            if(stopid != ""){
                pauseProjectSvc.updateProjectStop(vm);
            }else{
                pauseProjectSvc.pauseProject(vm);
            }

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

        vm.Checked=function(){
            if($("#fileNo").is(":checked")){
                $("#file1").prop("checked",false);
                $("#file2").prop("checked",false);
            }
        }
        vm.Checked2=function(){
            $("#fileNo").prop("checked",false);
        }

    }
})();