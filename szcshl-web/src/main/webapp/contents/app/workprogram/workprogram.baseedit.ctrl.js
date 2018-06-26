(function () {
    'use strict';

    angular.module('app').controller('wpBaseCtrl', workprogram);

    workprogram.$inject = ['bsWin','workprogramSvc','$state',"$rootScope"];

    function workprogram(bsWin,workprogramSvc,$state,$rootScope) {
        var vm = this;
    	vm.work = {};						//创建一个form对象
        vm.title = '项目基本信息';        	//标题
        vm.signId = $state.params.signid;
        vm.isadmin = $state.params.isadmin; //是否是从项目维护跳转过来

        activate();
        function activate() {
        	workprogramSvc.initBaseInfo(vm.signId,function(data){
                vm.work = data;
                if(vm.work.projectType){
                    vm.projectTypes = $rootScope.topSelectChange(vm.work.projectType,$rootScope.DICT.PROJECTTYPE.dicts)
                }
            });
        }

        vm.goBack = function(){
            if(vm.isadmin){
                $state.go('MaintainProjectEdit', {signid: $state.params.signid,processInstanceId:null});
            }else{
                $rootScope.back();
            }
        }
        vm.create = function () {
            common.initJqValidation($("#work_program_form"));
            var isValid = $("#work_program_form").valid();
            if (isValid) {
                vm.iscommit = true;
                workprogramSvc.saveBaseInfo(vm.work,vm.iscommit,function(data){
                    vm.iscommit = false;
                    if (data.flag || data.reCode == "ok") {
                        vm.work.id = data.idCode;
                        bsWin.success("操作成功！");
                    } else {
                        bsWin.error(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("操作失败，有红色*号的选项为必填项，请按要求填写！");
            }
        };  

    }
})();
