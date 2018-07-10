(function () {
    'use strict';

    var app = angular.module('myApp');
    app.factory("dictSvc", function ($http, bsWin, $state) {
        var dict_url = util.formatUrl("sys/dict");
        return {
            //list#zTree#Begin
            initDictTree: initDictTree,
            //list#zTree#End
            //edit#zTree#Begin
            /**
             * 初始化数据字典树
             * @param vm    作用域
             */
            initpZtreeClient: function (vm) {
                vm.dictsTree && vm.dictsTree.destroy();
                $http.get(dict_url + "?$orderby=itemOrder asc").success(function (data) {
                    //vm.dict = data;
                    var setting = {
                        check: {enable: true, chkStyle: "radio", radioType: "all"},
                        data: {
                            key: {
                                name: "dictName"
                            },
                            simpleData: {
                                enable: true,
                                idKey: "dictId",
                                pIdKey: "parentId",
                                rootPId: 0
                            }
                        }
                    };
                    vm.dictsTree = $.fn.zTree.init($("#pzTree"), setting, data || []);
                });
            },
            //edit#zTree#End
            /**
             * 创建数据字典
             * @param vm    作用域
             */
            createDict: function (vm) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.post(dict_url, vm.dict).then(function () {
                        bsWin.success("创建成功");
                        vm.isSubmit = false;
                        initDictTree(vm);
                    }, function () {
                        vm.isSubmit = false;
                    });
                }
            },
            /**
             * 通过主键查找数据字典数据
             * @param vm    作用域
             */
            findDictById: function (vm) {
                if (!vm.dictId) return false;
                $http.get(dict_url + "/" + vm.dictId).success(function (data) {
                    vm.dict = data;
                });
            },

            updateDict: function (vm) {
                util.initJqValidation();
                var isValid = $('form').valid();
                if (isValid) {
                    vm.isSubmit = true;
                    $http.put(dict_url, vm.dict).then(function () {
                        bsWin.success("更新成功");
                        vm.isSubmit = false;
                        initDictTree(vm);
                    }, function () {
                        vm.isSubmit = false;
                    })
                }
            },

            deleteDict: function (vm) {
                vm.isSubmit = true;
                $http['delete'](dict_url, {params: {"dictId": vm.dict.dictId || ""}}).then(function () {
                    bsWin.success("删除成功");
                    vm.isSubmit = false;
                    initDictTree(vm);
                }, function () {
                    vm.isSubmit = false;
                });
            },
            /**
             *
             * @param vm
             */
            resetDict: function (vm) {
                vm.isSubmit = true;
                $http.post(dict_url + "/reset", {}).then(function () {
                    bsWin.success("操作成功");
                    vm.isSubmit = false;
                    initDictTree(vm);
                }, function () {
                    vm.isSubmit = false;
                });
            }
        };

        function initDictTree(vm) {
            vm.dictsTree && vm.dictsTree.destroy();
            $http.get(dict_url + "?$orderby=itemOrder asc").success(function (data) {
                // vm.dict = data;

                vm.dictsTree = $.fn.zTree.init($("#zTree"), {
                    callback: {
                        onClick: zTreeOnClick
                    },
                    data: {
                        key: {
                            name: "dictName"
                        },
                        simpleData: {
                            enable: true,
                            idKey: "dictId",
                            pIdKey: "parentId",
                            rootPId: 0
                        }
                    }
                }, data || []);
                function zTreeOnClick(event, treeId, treeNode) {
                    $state.go('dict.edit', {dictId: treeNode.dictId});
                }

                // 初始化模糊搜索方法
                window.fuzzySearch("zTree", '#dictTreeKey', null, true);
            });
        }
    });

})();