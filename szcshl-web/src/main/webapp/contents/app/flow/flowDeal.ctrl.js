/**
 * Created by Administrator on 2017/9/5 0005.
 */
(function () {
    'use strict';

    angular.module('app').controller('flowDealCtrl', flowDeal);

    flowDeal.$inject = ['ideaSvc','bsWin'];

    function flowDeal(ideaSvc, bsWin) {
        var vm = this;
        vm.title = '待办任务处理';
        activate();
        function activate() {

        }

        /***************  S_个人意见  ***************/
        vm.ideaEdit = function (options) {
            if(!angular.isObject(options)){
                options = {};
            }
            ideaSvc.initIdeaData(vm,options);
        }
        vm.selectedIdea = function(){
            vm.flow.dealOption = vm.chooseIdea;
        }
        /***************  E_个人意见  ***************/

        /***************  S_流程处理  ***************/
        vm.commitTopicFlow = function () {
            alert("功能未开发");
        }

        //S_流程回退
        vm.backTopicFlow = function () {
            alert("功能未开发");
        }
        /***************  E_流程处理  ***************/
    }
})();

