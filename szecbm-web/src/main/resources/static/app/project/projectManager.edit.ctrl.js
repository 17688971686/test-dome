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

    projectManagerEditCtrl.$inject = ["$scope", "projectManagerSvc","$state"];

    function projectManagerEditCtrl($scope,projectManagerSvc,$state) {
        $scope.csHide("cjgl");
        var vm = this;
         vm.model = {};
         vm.model.id = $state.params.id;
         vm.flag = $state.params.flag;
         if (vm.model.id) {
            projectManagerSvc.findGovernmentInvestProjectById(vm, function () {
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
        }


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
                    vm.model.id = vm.id;    //赋值附件id
                    vm.model.status = '1';
                    projectManagerSvc.createGovernmentInvestProject(vm);
                }
            }

        };


    }
})();