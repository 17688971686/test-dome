/**
 * Created by Administrator on 2018/5/8 0008.
 */
(function () {
    'use strict';

    angular.module('app').controller('msgEditCtrl', msgEditFc);

    msgEditFc.$inject = ['$location','msgSvc','sharingPlatlformSvc','$http'];

    function msgEditFc($location, msgSvc,sharingPlatlformSvc,$http) {
        var vm = this;
        vm.title = '短信编辑';
        vm.model={};
        vm.businessFlag = {
            isLoadOrgUser : false
        };

        activate();
        function activate() {
            msgSvc.initOrgAndUser(function(data){
                vm.shareOrgList = data.orgDtoList;
                vm.noOrgUsetList = data.noOrgUserList;
                vm.businessFlag.isLoadOrgUser = true;
            });
        }

        /**
         * 保存发布信息
         */
        vm.create = function () {
            msgSvc.saveMsgInfo(vm);
        };
    }
})();
