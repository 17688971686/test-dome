(function () {
    'use strict';

    angular.module('myApp').factory("organSvc", organSvc);

    organSvc.$inject = ["$http", "bsWin"];

    function organSvc($http, bsWin) {
        var organ_url = util.formatUrl("sys/organ");

        return {
            getOrganList: function (vm, fn) {
                $http.get(organ_url + "?$orderby=itemOrder asc").success(function (data) {
                    vm.organList = data.value || [];
                    fn && fn(vm.organList);
                });
            },
            /**
             * 机构列表（用于业主单位列表）
             * @param vm
             * @param fn
             */
            findOrganList: function (vm, fn) {
                $http.get(organ_url + "/findOrganList").then(function (response) {
                    if (fn) {
                        fn(response.data)
                    } else {
                        vm.organList = response.data;
                    }
                })
            },

            createOrgan: function (vm, fn) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.post(organ_url, vm.model).then(function () {
                        vm.isSubmit = false;
                        bsWin.success("创建成功", function () {
                            fn && fn();
                        });
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            //begin#updateOrgan
            findOrganById: function (vm, fn) {
                $http.get(organ_url + "/" + (vm.organId || "")).success(function (data) {
                    data = data || {};
                    if(!fn) {
                        vm.model = data;
                    } else {
                        fn(data);
                    }
                });
            },
            updateOrgan: function (vm, fn) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.put(organ_url, vm.model).then(function () {
                        vm.isSubmit = false;
                        bsWin.success("更新成功", function () {
                            fn && fn();
                        });
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            //End:updateOrgan

            deleteOrgan: function (vm, fn) {
                // console.log(vm.organ.id);
                $http['delete'](organ_url, {params: {"organId": vm.organId || ""}}).then(function () {
                    bsWin.success("删除成功", function () {
                        fn && fn();
                    });
                }, function () {
                    vm.isSubmit = false;
                });
            },
            authorization: function (vm) {
                if (!vm.organId || !vm.model) {
                    bsWin.warning("缺少参数");
                    return;
                }
                vm.isSubmit = true;
                $http.put(organ_url + "/authorization?organId=" + vm.organId, vm.model.resources || []).then(function () {
                    vm.isSubmit = false;
                    bsWin.success("权限更新成功");
                }, function () {
                    vm.isSubmit = false;
                });
            }
        };


    }

})();