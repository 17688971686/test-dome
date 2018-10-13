(function () {
    'use strict';

    angular.module('app').controller('signCreateCtrl', sign);

    sign.$inject = ['$location','signSvc','$state','bsWin'];

    function sign($location, signSvc,$state,bsWin) {
        var vm = this;
        vm.model = {};						//创建一个form对象
        vm.title = '新增收文';        		//标题

        vm.create = function () {
            common.initJqValidation();
            var isValid = $('form').valid();
            if(isValid){
                signSvc.createSign(vm.model,function(data){
                    if (data.flag || data.reCode == "ok") {
                        //如果已经发起流程，则不允许再修改
                        if(data.reObj.processInstanceId){
                            bsWin.alert("操作成功！");
                        } else{
                            bsWin.success("操作成功，请继续填写项目审核登记表",function(){
                                $state.go('fillSign', {signid: data.reObj.signid}, {reload: true});
                            });
                        }
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }

        };

        /**
         * 提前介入复选框
         * @constructor
         */
        vm.TQJRCheckbox = function(){
            var flag = $("#isAdvanced").is(':checked');
            if(flag){
                if(vm.model.projectname){
                    vm.model.projectname += "(提前介入)";
                }else{
                    vm.model.projectname = "(提前介入)";
                }
            }else{
                if(vm.model.projectname){
                    vm.model.projectname = vm.model.projectname.replace("(提前介入)" , "");
                }
            }
        }

        /**
         * 是否调概复选框
         * @constructor
         */
        vm.SFTGCheckbox = function(){
            var flag = $("#ischangeEstimate").is(':checked');
            if(flag){
                if(vm.model.projectname){
                    vm.model.projectname += "(是否调概)";
                }else{
                    vm.model.projectname = "(是否调概)";
                }
            }else{
                if(vm.model.projectname){
                    vm.model.projectname = vm.model.projectname.replace("(是否调概)" , "");
                }
            }
        }

        /**
         * 评审阶段为“登记赋码”时，在项目名称后自动添加（登记赋码）
         */
        vm.reviewstageSelect = function(){
            if(vm.model.reviewstage && vm.model.reviewstage == "登记赋码"){
                if(vm.model.projectname){
                    vm.model.projectname += "(赋码阶段)";
                }else{
                    vm.model.projectname = "(赋码阶段)";
                }
            }else{
                if(vm.model.projectname){
                    vm.model.projectname = vm.model.projectname.replace("(赋码阶段)" , "");
                }
            }
        }

    }
})();
