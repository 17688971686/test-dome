(function () {
    'use strict';

    angular.module('app').controller('expertSelectCtrl', expertReview);

    expertReview.$inject = ['$location', 'expertReviewSvc','$state'];

    function expertReview($location, expertReviewSvc,$state) {
        var vm = this;
        vm.title = '选择专家';
        vm.expertReview = {};
        vm.expertReview.workProgramId = $state.params.workProgramId;		//这个是收文ID
        
        activate();
        function activate() {
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
