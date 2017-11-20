(function () {
    'use strict';

    angular.module('app').controller('signReserveAddCtrl', sign);

    sign.$inject = ['$location', 'reserveSignSvc', '$state', 'bsWin'];

    function sign($location, reserveSignSvc, $state, bsWin) {
        var vm = this;
        vm.model = {};						//创建一个form对象
        vm.title = '新增预签收收文';        		//标题
        vm.reserveAdd = function () {
            common.initJqValidation($('#reserveform'));
            var isValid = $('#reserveform').valid();
            if (isValid) {
                reserveSignSvc.reserveAdd(vm.model, function (data) {
                    if (data.flag || data.reCode == "ok") {
                        bsWin.success("操作成功，请继续填写项目审核登记表", function () {
                            $state.go('fillSign', {signid: data.reObj.signid}, {reload: true});
                        });
                    } else {
                        bsWin.error(data.reMsg);
                    }
                });
            } else {
                bsWin.alert("操作失败，有红色*号的选项为必填项，请按要求填写！");
            }
        };
    }
})();
