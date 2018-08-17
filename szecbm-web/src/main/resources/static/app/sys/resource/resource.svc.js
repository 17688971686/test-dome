(function () {
    'use strict';

    var app = angular.module('myApp');

    app.factory("resourceSvc", function ($http, bsWin) {
        var resource_url = util.formatUrl("sys/resource");
        return {
            /**
             * 获取系统资源数据
             * @param vm
             * @param fn    操作成功的回调函数
             */
            getResourceData: function (vm, fn) {
                $http.get(resource_url + "?$orderby=itemOrder asc").success(function (data) {
                    vm.resources = data;
                    fn && fn(data);
                })
            },
            /**
             * 通过主键查找系统资源
             * @param vm
             * @param fn    操作成功的回调函数
             */
            findResourceById: function (vm, fn) {
                $http.get(resource_url + "/" + (vm.resId || "")).success(function (data) {
                    vm.model = data;
                    fn && fn(data);
                });
            },
            /**
             * 创建系统资源
             * @param vm
             * @param fn    操作成功的回调函数
             */
            createResource: function (vm, fn) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.post(resource_url, vm.model).then(function () {
                        vm.isSubmit = false;
                        bsWin.alert("创建成功");
                        fn && fn();
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            /**
             * 更新系统资源
             * @param vm
             * @param fn    操作成功的回调函数
             */
            updateResource: function (vm, fn) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.put(resource_url, vm.model).then(function () {
                        vm.isSubmit = false;
                        bsWin.alert("更新成功");
                        fn && fn();
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            /**
             * 删除系统资源
             * @param vm
             * @param fn    操作成功的回调函数
             */
            deleteResource: function (vm, fn) {
                vm.isSubmit = true;
                $http['delete'](resource_url, {data: vm.resId || ""}).then(function () {
                    bsWin.alert("删除成功");
                    vm.isSubmit = false;
                    fn && fn();
                }, function () {
                    vm.isSubmit = false;
                });
            },
            /**
             * 重置系统资源
             * @param vm
             * @param fn    操作成功的回调函数
             */
            resetResource: function (vm, fn) {
                vm.isSubmit = true;
                $http.post(resource_url + "/reset").then(function () {
                    vm.isSubmit = false;
                    bsWin.alert("系统资源已成功重置");
                    fn && fn();
                }, function () {
                    vm.isSubmit = false;
                });
            }

        };


    });

})();