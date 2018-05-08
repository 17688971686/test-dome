/**
 * Created by Administrator on 2018/5/8 0008.
 */
(function () {
    'use strict';

    angular.module('app').controller('msgCtrl', msgFc);

    msgFc.$inject = ['$location','msgSvc','$state','$http'];

    function msgFc($location, msgSvc,$state,$http) {
        var vm = this;
        vm.title = '短信列表';
        vm.model={};
        activate();
        function activate() {

        }
    }
})();
