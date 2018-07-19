(function () {
    'use strict';

    angular.module('myApp').config(governmentInvestProjectEditConfig);

    governmentInvestProjectEditConfig.$inject = ["$stateProvider"];

    function governmentInvestProjectEditConfig($stateProvider) {
        $stateProvider.state('projectManageEdit', {
            url: "/projectManageEdit/:id/:flag",
            controllerAs: "vm",
            templateUrl: util.formatUrl('project/html/edit'),
            controller: projectManagerEditCtrl
        });
    }

    projectManagerEditCtrl.$inject = ["$scope", "projectManagerSvc","$state","bsWin"];

    function projectManagerEditCtrl($scope,projectManagerSvc,$state,bsWin) {
        $scope.csHide("cjgl");
        var vm = this;
         vm.model = {};
         vm.model.id = $state.params.id;
         vm.flag = $state.params.flag;
         vm.attachments = [];

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
            });
        }else{
             projectManagerSvc.createUUID(vm);
         }


        /**
         * 删除附件
         */
        vm.removeFile = function (fileId, index) {
            bsWin.confirm("是否移除附件", function () {
                projectManagerSvc.deleteFileById(fileId, vm);
                vm.attachments.splice(index, 1);
            })
        };
        /**
         * 日期比较
         */
        function compareDate (d1,d2) {
            return ((new Date(d1.replace(/-/g,"\/"))) > (new Date(d2.replace(/-/g,"\/"))));
        }


        vm.checkLength = function (obj, max, id) {
            util.checkLength(obj, max, id);
        };


        //新增与编辑
        vm.save = function () {
            util.initJqValidation();
            var isValid = $('form').valid();
            if(isValid){
                if (vm.model.id) {
                    projectManagerSvc.updateGovernmentInvestProject(vm);
                } else {
                    vm.model.id = vm.UUID;
                    vm.model.status = '1';
                    projectManagerSvc.createGovernmentInvestProject(vm);
                }
            }

        };


    }
})();