(function () {
    'use strict';
    if (typeof angular === 'undefined') {
        return;
    }

    var sysTopMenusTpl = "template/sys/topMenu.html",
        sysLeftMenusTpl = "template/sys/leftMenu.html";

    angular.module('sn.common', []).filter('formatUrl', function () {
        return function (input) {
            return util.formatUrl(input);
        };
    }).run(["$templateCache", function ($templateCache) {
        // 系统菜单模板
        $templateCache.put(sysTopMenusTpl,
            '<span class="top-tab" ng-repeat="x in MAIN_MENU">\
                <a ng-click="!!x.children && csHide(x.resId)" id="menu_{{x.resId}}" ng-href="{{x.resUri||\'javascript:;\'}}" target="{{x.target}}">\
                    <i class="t-z-green-hover {{x.resIcon||\'fa fa-bars\'}}"></i>\
                    <span>{{x.resName}}</span>\
                </a>\
            </span>');
        // 系统左菜单模板
        $templateCache.put(sysLeftMenusTpl,
            '<ul class="sidebar-menu" ng-repeat="x in MAIN_MENU"  ng-if="menuBoxResId==x.resId">\
                <li class="active treeview">\
                    <a href="#/"><i class="fa fa-home"></i><span>主页</span></a>\
                </li>\
                <li class="treeview">\
                    <a class="t-z-blue-dep" href="javascript:;">\
                        <i class="{{x.resIcon||\'fa fa-bars\'}}"></i>\
                        <span> &nbsp; {{x.resName}}</span>\
                    </a>\
                </li>\
                <li class="treeview active" ng-repeat="y in x.children" ng-if="!!x.children">\
                    <a class="" ng-href="{{y.resUri||\'javascript:;\'}}" target="{{y.target}}">\
                        <i class="{{y.resIcon||\'fa fa-bars\'}}"></i>\
                        <span>{{y.resName}}</span>\
                        <span class="pull-right-container" ng-if="!!y.children">\
                            <i class="fa fa-angle-left pull-right"></i>\
                        </span>\
                    </a>\
                    <ul class="treeview-menu" ng-if="!!y.children">\
                        <li ng-repeat="z in y.children">\
                            <a ng-href="{{z.resUri||\'javascript:;\'}}" target="{{z.target}}">\
                                <i class="{{z.resIcon||\'fa fa-circle-o\'}}"></i> {{z.resName}}\
                            </a>\
                        </li>\
                    </ul>\
                </li>\
            </ul>');
    }]).factory("snBaseUtils", ["$http", "$templateCache", function ($http, $templateCache) {

        return {
            /**
             * 初始化数据字典
             * @param vm
             */
            initDicts: function (vm) {
                $http.get(util.formatUrl('sys/dict/all')).success(function (data) {
                    var dictData = data || [];
                    vm.dictMetaData = dictData;
                    vm.DICT = reduceDict(dictData).dicts;
                });
            },
            /**
             * 初始化左菜单
             * @param vm
             * @param leftMenuBox
             * @param sys
             */
            initMenus: function (vm, fn) {
                $http.get(util.formatUrl("sys/user/resources?status=1")).success(function (data) {
                    vm.MAIN_MENU = initMenus(data);
                    fn && fn($templateCache.get(sysTopMenusTpl), $templateCache.get(sysLeftMenusTpl));
                });
            }
        };

        /**
         * 组装字段结构
         * @param dictData
         * @param dId
         * @returns {*}
         */
        function reduceDict(dictData, dId) {
            if (!dictData) {
                return {};
            }
            var dictMap = {}, dictList = [], _d;
            for (var i = 0, len = dictData.length; i < len; i++) {
                _d = dictData[i];
                if ((!dId && !_d.parentId) || (!!dId && dId == _d.parentId)) {
                    var children = reduceDict(dictData, _d.dictId);
                    var dd = {
                        dictKey: _d.dictKey,
                        dictName: _d.dictName,
                        remark: _d.remark,
                        dict: _d,
                        dicts: children.dicts,
                        dictList: children.dictList
                    };
                    dictMap[_d.dictKey] = dd;
                    dictList.push(dd);
                }
            }

            return {dicts: dictMap, dictList: dictList};
        }

        /**
         * 组织菜单 tree 结构
         * @param menuData
         * @param parentId
         * @returns {Array}
         */
        function initMenus(menuData, parentId) {
            menuData = menuData || [];
            var menus = [], m, children;
            for (var i = 0, len = menuData.length; i < len; i++) {
                m = menuData[i];
                if ((!parentId && !m.parentId) || m.parentId == parentId) {
                    children = initMenus(menuData, m.resId);
                    if (children.length > 0) {
                        m.children = children;
                    }
                    if (!!m.resUri && m.resUri.indexOf("#") != 0) {
                        m.resUri = util.formatUrl(m.resUri);
                    }
                    menus.push(m);
                }
            }
            return menus;
        }

    }]);

})();