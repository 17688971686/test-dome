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
    }
})();
