(function () {
    'use strict';

    angular.module('app').controller('userAssertDetailAddCtrl', userAssertDetail);

    userAssertDetail.$inject = ['$location', 'userAssertDetailSvc', '$state'];

    function userAssertDetail($location, userAssertDetailSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.model = {};
        vm.conMaxIndex = 0;                   //条件号
        vm.conditions = new Array();         //条件列表
        vm.title = '添加用户资产明细';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新用户资产明细';
        }

        vm.create = function () {
            userAssertDetailSvc.createUserAssertDetail(vm);
        };
        vm.update = function () {
            userAssertDetailSvc.updateUserAssertDetail(vm);
        };

        vm.addCondition = function () {
            vm.condition = {};
            vm.condition.sort = vm.conMaxIndex+1;
            vm.conditions.push(vm.condition);
            vm.conMaxIndex++;
         /*   if(vm.showFlag.addBooksDeatail){
                vm.condition.sort = vm.conMaxIndex+1;
            }else{
                vm.conMaxIndex = vm.conditions.length;
                vm.condition.sort = vm.conditions.length+1;
            }
            vm.conditions.push(vm.condition);
            vm.conMaxIndex++;*/
        }

        activate();
        function activate() {
            if (vm.isUpdate) {
                userAssertDetailSvc.getUserAssertDetailById(vm);
            }else{
                userAssertDetailSvc.initFillData(function(data){
                    vm.goodsDetailDtoList = data.reObj.goodsDetailDtoList
                });
            }
        }
    }
})();
