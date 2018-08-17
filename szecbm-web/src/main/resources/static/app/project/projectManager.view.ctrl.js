(function () {
    'use strict';

    angular.module('myApp').config(governmentInvestProjectViewConfig);

    governmentInvestProjectViewConfig.$inject = ["$stateProvider"];

    function governmentInvestProjectViewConfig($stateProvider) {
        $stateProvider.state('projectManageView', {
            url: "/projectManageView/:id/:flag",
            controllerAs: "vm",
            templateUrl: util.formatUrl('project/html/view'),
            controller: projectManagerViewCtrl
        });
    }

    projectManagerViewCtrl.$inject = ["$scope", "projectManagerSvc","$state","bsWin","attachmentSvc"];

    function projectManagerViewCtrl($scope,projectManagerSvc,$state,bsWin,attachmentSvc) {
        $scope.csHide("cjgl");
        var vm = this;
         vm.model = {};
         vm.model.id = $state.params.id;
         vm.flag = $state.params.flag;
         vm.attachments = [];

        projectManagerSvc.findOrgUser(function(data){
            vm.principalUsers = data;
        });
        /**
         * 初始化附件上传
         */
        projectManagerSvc.initUploadConfig(vm,"relateAttach",function (data) {
            vm.attachments = vm.attachments.concat(JSON.parse(data));
        });
         if (vm.model.id) {

            projectManagerSvc.findGovernmentInvestProjectById(vm, function () {
                /**
                 * 查询附件列表
                 */
               projectManagerSvc.getAttachments(vm, {
                    "businessId": vm.model.id
                }, function (data) {
                    angular.forEach(vm.attachments.concat(data), function (o, i) {
                        vm.attachments = vm.attachments.concat(o);
                    });
                });
                if(vm.flag == 'cancel' || vm.flag == 'normal'){
                    if(vm.flag == 'cancel'){
                        vm.model.status = '2';
                    }else{
                        vm.model.status = '1';
                    }
                    projectManagerSvc.updateGovernmentInvestProject(vm);
                }else if(vm.flag == 'delete'){
                    projectManagerSvc.deleteGovernmentInvestProject(vm.model.id);
                }

            });
        }else{
             projectManagerSvc.createUUID(vm);
         }

        /**
         * 日期比较
         */
        function compareDate (d1,d2) {
            return ((new Date(d1.replace(/-/g,"\/"))) > (new Date(d2.replace(/-/g,"\/"))));
        }

    }
})();