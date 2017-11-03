(function(){
    'use strict';
    angular.module('app').controller('projectStopFormEditCtrl' , projectStopFormEdit);
    projectStopFormEdit.$inject = ['$state' , 'pauseProjectSvc','bsWin'];
    function projectStopFormEdit($state, pauseProjectSvc,bsWin){
        var vm = this;
        vm.stopId = $state.params.stopId;
        activate();
        function activate(){
            pauseProjectSvc.getProjectStopByStopId(vm.stopId,function(data){
                vm.projectStop = data;
                vm.sign=vm.projectStop.signDispaWork;
                if(vm.sign.reviewstage == '可行性研究报告' || vm.sign.reviewstage == '项目概算'){
                    vm.sign.countUsedWorkday = 15-vm.sign.surplusdays;
                }else{
                    vm.sign.countUsedWorkday = 12-vm.sign.surplusdays;
                }

            });
        }

        /**
         *保存更新暂停项目信息
         */
        vm.saveProjectStop = function () {
            common.initJqValidation();
            var isValid = $('#form').valid();
            if (isValid) {
                pauseProjectSvc.pauseProject(vm.projectStop,function(data){
                    if(data.flag || data.reCode=="ok"){
                        bsWin.alert("操作成功！")
                    }else{
                        bsWin.alert(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("项目暂停表填写不符合要求！");
            }
        }


    }
})();