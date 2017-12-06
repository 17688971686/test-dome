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
                vm.sign = vm.projectStop.signDispaWork;
                //评审天数-剩余工作日
                vm.sign.countUsedWorkday = vm.sign.reviewdays-vm.sign.surplusdays;
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