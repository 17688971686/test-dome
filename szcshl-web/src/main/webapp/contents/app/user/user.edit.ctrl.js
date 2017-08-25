(function () {
    'use strict';

    angular.module('app').controller('userEditCtrl', user);

    user.$inject = ['$location', 'userSvc', '$state','bsWin'];

    function user($location, userSvc, $state,bsWin) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '新增用户';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '编辑用户';
        }

        activate();
        function activate() {
            if (vm.isUpdate) {
                userSvc.getUserById(vm);
            } else {
                userSvc.initZtreeClient(vm);
            }
            userSvc.getOrg(function(data){
                vm.org = {};
                vm.org = data;
            });
        }

        vm.create = function () {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                var nodes = userSvc.getZtreeChecked();
                var nodes_roles = $linq(nodes).where(function (x) {
                    return x.isParent == false;
                }).select(function (x) {
                    return {
                        id: x.id,
                        roleName: x.name
                    };
                }).toArray();
                vm.model.roleDtoList = nodes_roles;
                userSvc.createUser(vm.model, vm.isSubmit,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        if(!vm.model.id){
                            vm.model.id = data.idCode;
                        }
                        bsWin.success("操作成功！");
                    }else{
                        bsWin.error(data.reMsg);
                    }

                });
            }
        };

        vm.update = function () {
            userSvc.updateUser(vm);
        };


    }
})();
