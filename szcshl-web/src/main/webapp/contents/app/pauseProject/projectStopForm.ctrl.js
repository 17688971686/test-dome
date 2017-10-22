(function(){
    'use strict';
    angular.module('app').controller('projectStopFormCtrl' , projectStopForm);
    projectStopForm.$inject = ['$state' , 'pauseProjectSvc','bsWin'];
    function projectStopForm($state, pauseProjectSvc,bsWin){
        var vm = this;
        vm.sign = {};
        var signId = $state.params.signId;
        vm.projectStop = {};
        vm.projectStop.signid = signId;
        activate();
        function activate(){
            pauseProjectSvc.initProject(signId,function(data){
                vm.sign = data;
                if(vm.sign.reviewstage == '可行性研究报告' || vm.sign.reviewstage == '项目概算'){
                    vm.sign.countUsedWorkday = 15-vm.sign.surplusdays;
                }else{
                    vm.sign.countUsedWorkday = 12-vm.sign.surplusdays;
                }
            });
        }

        vm.Checked = function($event,isHasFile){
            var checkbox = $event.target;
            var checked = checkbox.checked;
            if(isHasFile){
                if(checked == 9 || checked == '9'){
                    vm.noFile = false;
                }else{
                    vm.noFile = true;
                }
            }else{
                if(vm.noFile){
                    vm.projectStop.isSupplementMaterial = 0;
                    vm.projectStop.isPuaseApprove = 0;
                }
            }
        }

        /**
         *更新暂停项目信息
         */
        vm.commitProjectStop = function () {
            common.initJqValidation();
            var isValid = $('#form').valid();
            if (isValid) {
                vm.projectStop.processName = "《"+vm.sign.projectname+"》暂停申请";
                if(!vm.projectStop.userDays){
                    vm.projectStop.userDays = vm.sign.countUsedWorkday;
                }
                pauseProjectSvc.pauseProject(vm.projectStop,function(data){
                    if(data.flag || data.reCode=="ok"){
                        bsWin.alert("操作成功！",function(){$state.go("personDtasks");})
                    }else{
                        bsWin.alert(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("项目暂停表填写不符合要求！");
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

    }
})();