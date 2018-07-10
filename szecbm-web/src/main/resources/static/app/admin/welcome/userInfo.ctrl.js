(function () {
    'use strict';

    angular.module('myApp').config(function ($stateProvider) {
        var userInfo_url = util.formatUrl('admin/userInfo');

        $stateProvider.state('userInfo', {
            url: "/userInfo",
            controllerAs: "vm",
            templateUrl: userInfo_url,
            controller: function ($scope, $http, bsWin) {
                var vm = this;
                vm.isSubmit = false;
                vm.toUpdate = function () {
                    util.initJqValidation();
                    var isValid = $('form').valid();
                    if (isValid) {
                        var encrypt = new JSEncrypt();
                        $http.get(util.formatUrl("rsaKey")).success(function (data) {
                            encrypt.setPublicKey(data || "");
                            if (vm.model.oldPassword) {
                                vm.model.oldPassword = encrypt.encrypt(vm.model.oldPassword);
                                vm.model.newPassword = encrypt.encrypt(vm.model.newPassword);
                                vm.model.verifyPassword = encrypt.encrypt(vm.model.verifyPassword);
                            }

                            vm.isSubmit = true;
                            $http.put(userInfo_url, vm.model).then(function () {
                                vm.isSubmit = false;
                                bsWin.success("修改成功");
                            }, function () {
                                vm.isSubmit = false;
                            })
                        })
                    }
                }
            }
        });
    });

})();