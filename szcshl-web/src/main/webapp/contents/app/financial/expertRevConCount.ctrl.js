(function () {
    'use strict';

    angular.module('app').controller('expertRevConCountCtrl', expertRevConCount);

    expertRevConCount.$inject = ['$location', 'expertRevConCountSvc','$state','$http'];

    function expertRevConCount($location, expertRevConCountSvc,$state,$http) {
        var vm = this;
        vm.title = '专家评审基本情况统计';
        vm.model={};

        vm.expertRevConCount = function () {
            expertRevConCountSvc.expertRevConCount(vm,function(data){
                vm.expertReviewConDtoList = data.reObj.expertReviewConDtoList;
            });
        }



        //重置查询表单
        vm.formReset = function(){
            vm.model = {};
        }
        activate();
        function activate() {
            expertRevConCountSvc.expertRevConCount(vm,function(data){
                vm.expertReviewConDtoList = data.reObj.expertReviewConDtoList;
            });
        }
    }
})();
