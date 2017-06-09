(function () {
    'use strict';

    angular.module('app').controller('assistPlanCtrl', assistPlan);

    assistPlan.$inject = ['$location','$state','assistSvc'];

    function assistPlan($location,$state,assistSvc) {
        var vm = this;
        vm.model = {};							//创建一个form对象
        vm.filterModel = {};                    //filter对象
        vm.title = '协审计划管理';        		//标题
        vm.splitNumArr = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15];
        vm.planSign = {};                       //计划收文关联对象
        vm.assistSign = new Array();

        active();
        function active(){
            assistSvc.initPlanPage(vm);

        }

        //数据过滤
        vm.filterSign = function(item){
            var isMatch = true;
            if(!angular.isUndefined(item)){
                if(!angular.isUndefined(vm.filterModel.filterFilecode)){
                     if((item.filecode).indexOf(vm.filterModel.filterFilecode) == -1){
                         isMatch = false;
                     }
                }
                if(isMatch){
                    if(!angular.isUndefined(vm.filterModel.filterProjectCode)){
                        if((item.projectcode).indexOf(vm.filterModel.filterProjectCode) == -1){
                            isMatch = false;
                        }
                    }
                }
                if(isMatch){
                    if(!angular.isUndefined(vm.filterModel.filterProjectName)){
                        if((item.projectname).indexOf(vm.filterModel.filterProjectName) == -1){
                            isMatch = false;
                        }
                    }
                }
                if(isMatch){
                    if(!angular.isUndefined(vm.filterModel.filterBuiltName)){
                        if(angular.isUndefined(item.builtcompanyName)){
                            isMatch = false;
                        }
                        if(isMatch && (item.builtcompanyName).indexOf(vm.filterModel.filterBuiltName) == -1){
                            isMatch = false;
                        }
                    }
                }
                if(isMatch){
                    return item;
                }
            }
        }

        //重置拆分值
        vm.initSplit = function(typeName){
            if(vm.planSign.assistType == typeName){
                if(!angular.isUndefined(vm.planSign.spliNum)){
                    vm.planSign.spliNum = 0;
                }
            }
        }

        //挑选项目
        vm.affirmSign = function () {
            var isCheckSign = $("input[name='selASTSign']:checked");
            if (isCheckSign.length < 1) {
                common.alert({
                    vm : vm,
                    msg : "请选择要挑选的项目"
                })
            }else{
                if(isCheckSign.length > 1){
                    if(vm.planSign.assistType == '合并项目'){
                        common.alert({
                            vm : vm,
                            msg : "合并项目要先挑选一个主项目，再挑选次项目！"
                        })
                    }else{
                        common.alert({
                            vm : vm,
                            msg : "独立项目，每次只能选择一个！"
                        })
                    }
                    return ;
                }else{
                    vm.model.signId = isCheckSign[0].value;
                    vm.model.assistType = vm.planSign.assistType;
                    vm.model.isSignle = vm.planSign.assistType == '合并项目'?false:true;
                    vm.model.splitNum = vm.planSign.spliNum;
                    vm.assistSign.forEach(function (st,index) {
                        if(st.signid == vm.model.signId){
                            vm.model.projectName = st.projectname;
                        }
                    });
                    assistSvc.saveAssistPlan(vm);
                }
            }
        }

        //删除操作
        vm.doDelete  = function(){
            alert("功能在开发中");
        }
    }
})();
