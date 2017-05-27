(function () {
    'use strict';

    angular.module('app').controller('expertSelectCtrl', expertReview);

    expertReview.$inject = ['$location', 'expertReviewSvc','expertConditionSvc','$state'];

    function expertReview($location, expertReviewSvc,expertConditionSvc,$state) {
        var vm = this;
        vm.title = '选择专家';
        vm.isAutoSelectExpert = true;       //专家是否已经抽取
        vm.selExpertConfirm = false;        //抽取的专家是否已经确认
        vm.conditionIndex = 1;              //条件号
        vm.conditions = new Array();        //条件对象
        vm.autoExpertMap = {};              //随机抽取专家
        vm.expertReview = {};
        vm.workProgram = {};
        vm.expertReview.workProgramId = $state.params.workProgramId;		//这个是收文ID
        
        activate();
        function activate() {
            expertConditionSvc.initConditionAndWP(vm);
        	expertReviewSvc.initExpertGrid(vm);
            expertReviewSvc.initSelect(vm);
        }

        //弹出自选专家框
        vm.showSelfExpertGrid = function(){
        	expertReviewSvc.initSelfExpert(vm);
        }
        
        //保存自选的专家
        vm.saveSelfExpert = function(){
        	expertReviewSvc.saveSelfExpert(vm);
        }
        
        //删除自选专家
        vm.delertSelfExpert = function(){
        	var isCheck = $("input[name='seletedEp']:checked");
			if (isCheck.length < 1) {
				common.alert({
					vm : vm,
					msg : "请选择操作对象"					
				})
			}else{
				 var ids=[];
                 for (var i = 0; i < isCheck.length; i++) {
                 	ids.push(isCheck[i].value);
   			  	 }  
                 var idStr=ids.join(',');                				
				expertReviewSvc.delertExpert(vm,idStr);
			}         	
        }

        //境外专家
        vm.showOutExpertGrid = function(){
            expertReviewSvc.showOutExpertGrid(vm);
        }
        //删除选择的境外专家
        vm.delertOutSelfExpert = function () {
            var isCheck = $("input[name='seletedOutEp']:checked");
            if (isCheck.length < 1) {
                common.alert({
                    vm : vm,
                    msg : "请选择操作对象"
                })
            }else{
                var ids=[];
                for (var i = 0; i < isCheck.length; i++) {
                    ids.push(isCheck[i].value);
                }
                var idStr=ids.join(',');
                expertReviewSvc.delertExpert(vm,idStr);
            }
        }
        //保存选择的境外专家
        vm.saveOutExpert = function(){
            expertReviewSvc.saveOutExpert(vm);
        }

        //计算符合条件的专家，并缓存数据
        vm.countMatchExperts = function(sortIndex){
            expertReviewSvc.countMatchExperts(vm,sortIndex);
        }

        vm.checkIntegerValue = function(checkValue,idStr,idSort){
            if(expertConditionSvc.isUnsignedInteger(checkValue)){
                $("#"+idStr+idSort).val(checkValue);
                $("#errorsOfficialNum"+idSort).html("");
                $("#errorsAlternativeNum"+idSort).html("");
            }else{
                $("#errorsOfficialNum"+idSort).html("只能填写数字");
                $("#errorsAlternativeNum"+idSort).html("只能填写数字");
            }
        }

        //添加随机抽取条件
        vm.addCondition = function(){
            if(vm.isAutoSelectExpert){
                common.alert({
                    vm : vm,
                    msg : "当前项目已经进行整体专家方案的抽取，不能再修改方案！"
                })
            }else{
                vm.condition = {};
                vm.condition.sort = vm.conditionIndex;
                vm.condition.workProgramId = vm.expertReview.workProgramId;
                vm.conditions.push(vm.condition);
                vm.conditionIndex++;
            }
        }

        //删除专家抽取条件
        vm.removeCondition = function(){
            if(vm.isAutoSelectExpert){
                common.alert({
                    vm : vm,
                    msg : "当前项目已经进行整体专家方案的抽取，不能再修改方案！"
                })
            }else{
                var isCheck = $("#conditionTable input[name='epConditionSort']:checked");
                if(isCheck.length >= 1){
                    common.confirm({
                        vm:vm,
                        title:"温馨提示",
                        msg:"删除数据不可恢复，确定删除么？",
                        fn:function () {
                            $('.confirmDialog').modal('hide');
                            var delIds = [];
                            for (var i = 0; i < isCheck.length; i++) {
                                vm.conditions.forEach(function (t, number) {
                                    if(isCheck[i].value == t.sort){
                                        vm.conditions.splice(number,1);
                                        //未抽取专家的可以删除
                                        if(t.id && (angular.isUndefined(t.isSelete) || t.isSelete==0)){
                                            delIds.push(t.id);
                                        }
                                    }
                                });
                            }
                            if(delIds.length > 0){
                                expertConditionSvc.deleteSelConditions(vm,delIds.join(","));
                            }
                        }
                    })
                }
            }
        }

        //保存专家抽取条件
        vm.saveCondition = function(){
            if(vm.isAutoSelectExpert){
                common.alert({
                    vm : vm,
                    msg : "当前项目已经进行整体专家方案的抽取，不能再修改方案！"
                })
            }else{
                expertConditionSvc.saveCondition(vm);
            }
        }

        //显示随机抽取页面
        vm.showAutoExpertWin = function(){
            var pass = true;
            //遍历抽取条件，如果有未保存的，提示先保存
            vm.conditions.forEach(function (t, number) {
                if(angular.isUndefined(t.id) || t.id == ""){
                    pass = false;
                }
            });
            if(pass){
                common.alert({
                    vm : vm,
                    msg : "功能正在开发中！"
                })
            }else{
                common.alert({
                    vm : vm,
                    msg : "请先保存编辑的抽取方案！"
                })
            }
        }

        //再次抽取专家
        vm.resetAutoExpert = function(){
            common.alert({
                vm : vm,
                msg : "功能待开发！"
            })
        }

        //确认已选的专家
        vm.affirmExpert = function(){
            var isCheck = $("input[name='seletedEp']:checked");
            if (isCheck.length < 1) {
                common.alert({
                    vm : vm,
                    msg : "请选择操作对象"
                })
            }else{
                var ids=[];
                for (var i = 0; i < isCheck.length; i++) {
                    ids.push(isCheck[i].value);
                }
                var idStr=ids.join(',');
                expertReviewSvc.updateExpertState(vm,idStr,"9");
            }
        }
    }
})();
