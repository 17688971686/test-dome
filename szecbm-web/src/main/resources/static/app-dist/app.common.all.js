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
        /*$templateCache.put(sysTopMenusTpl,
            '<span class="top-tab" ng-repeat="x in MAIN_MENU">\
                <a ng-click="!!x.children && csHide(x.resId)" id="menu_{{x.resId}}" ng-href="{{x.resUri||\'javascript:;\'}}" target="{{x.target}}">\
                    <i class="t-z-green-hover {{x.resIcon||\'fa fa-bars\'}}"></i>\
                    <span>{{x.resName}}</span>\
                </a>\
            </span>');*/
        // 系统左菜单模板
        $templateCache.put(sysLeftMenusTpl,
            '<ul class="sidebar-menu">\
                <li class="active treeview">\
                    <a href="#/"><i class="fa fa-home"></i><span>主页</span></a>\
                </li>\
                <li class="treeview active" ng-repeat="y in MAIN_MENU" >\
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
                    fn && fn($templateCache.get(sysLeftMenusTpl));
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
/**
 * 自定义弹窗窗口（在对应的方法中注入bsWin对象）
 * 提示窗口使用方法：
 *  bsWin.alert("消息提示", "操作成功！", function () { console.log("This's close!"); } );
 * 或者
 *  bsWin.alert({
 *      title: "消息提示",
 *      message: "操作成功！",
 *      onClose: function () { console.log("This's close!"); }
 *  });
 * 询问窗口使用方法：
 *  bsWin.confirm("询问提示", "是否进行该操作！", function () { console.log("This's ok!"); }, function () { console.log("This's close!"); } );
 * 或者
 *  bsWin.confirm({
 *      title: "询问提示",
 *      message: "是否进行该操作！",
 *      onOk: function () { console.log("This's ok!"); },
 *      onClose: function () { console.log("This's close!"); }
 *  });
 */
(function () {
    'use strict';

    if (typeof angular === 'undefined') {
        return;
    }

    var app = angular.module('sn.common'),
        _body = $("body"),
        alertTplPath = "template/dialog/alert.html",
        confirmTplPath = "template/dialog/confirm.html",
        reloginTplPath = "template/dialog/relogin.html",
        traceImgTplPath = "template/dialog/traceImg.html";

    app.factory('bsWin', ["$rootScope", "$injector", "$templateCache", "bsWinConfig",
        function ($rootScope, $injector, $templateCache, bsWinConfig) {
            var index = 0;
            var traceUrl = util.formatUrl("workflow/process/trace/");

            $rootScope.reloginWin = false;

            return {
                success: function (message, onClose) {
                    return this.alert({
                        message: message,
                        type: "success",
                        onClose: onClose
                    });
                },
                warning: function (message, onClose) {
                    return this.alert({
                        message: message,
                        type: "warning",
                        onClose: onClose
                    });
                },
                error: function (message, onClose) {
                    return this.alert({
                        message: message,
                        type: "error",
                        onClose: onClose
                    });
                },
                alert: function (options, message, onClose) {
                    if (!angular.isObject(options)) {
                        var title;
                        if (!message || angular.isFunction(message)) {
                            onClose = message;
                            message = options;
                        } else {
                            title = options;
                        }

                        options = {
                            message: message,
                            onClose: onClose || function () {
                            }
                        };
                        if (title) {
                            options.title = title;
                        }
                    }
                    options.templateUrl = alertTplPath;
                    return initWin(options);
                },
                confirm: function (options, message, onOk, onClose) {
                    if (!angular.isObject(options)) {
                        var title;
                        if (!message || angular.isFunction(message)) {
                            onClose = onOk || function () {
                                };
                            onOk = message || function () {
                                };
                            message = options;
                        } else {
                            title = options;
                        }

                        options = {
                            message: message,
                            onOk: onOk,
                            onClose: onClose
                        };
                        if (title) {
                            options.title = title;
                        }
                    }
                    options.templateUrl = confirmTplPath;
                    return initWin(options);
                },
                relogin: function (message) {
                    // 避免 登录窗口 重复打开
                    if ($rootScope.reloginWin) {
                        return false;
                    }
                    var bsWin = this;
                    var options = {
                        message: message,
                        onOk: function () {
                            var me = this, $http = $injector.get("$http"),
                                encrypt = new JSEncrypt();
                            // get publicKey
                            $http.get(util.formatUrl("rsaKey")).success(function (data) {

                                me.scope.model.username = me.scope.model.username.replace(/\s+/g, "");

                                encrypt.setPublicKey(data);
                                me.scope.model.password = encrypt.encrypt(me.scope.model.password);
                                // to login
                                $http({
                                    method: 'POST',
                                    url: util.loginUrl,
                                    data: $.param(me.scope.model),
                                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
                                }).then(function (response) {
                                    var data = response.data;
                                    if (data.status == 'SUCCESS') {
                                        me.el.modal("hide");
                                        bsWin.success("登陆成功！");
                                    } else {
                                        bsWin.error(data.message);
                                    }
                                });
                            })

                            return false;
                        },
                        onClose: function () {
                            $rootScope.reloginWin = false;
                        }
                    };
                    options.templateUrl = reloginTplPath;

                    var reloginWin = initWin(options);
                    reloginWin.scope.loginCaptcha = loginCaptcha || false;
                    reloginWin.scope.captchaImagePath = util.formatUrl("captchaImage");
                    reloginWin.scope.reloadVerify = function () {
                        reloginWin.scope.captchaImagePath = util.formatUrl("captchaImage?" + Math.random());
                    }
                    // 避免 登录窗口 重复打开
                    $rootScope.reloginWin = true;
                    return reloginWin;
                },
                traceImg: function (processInstanceId) {
                    var options = {
                        message: "",
                        onClose: function () {
                        }
                    };
                    options.templateUrl = traceImgTplPath;
                    var myWin = initWin(options);
                    myWin.scope.imgUrl = !processInstanceId ? "#" : (traceUrl + processInstanceId);
                    return myWin;
                }
            };

            function initWin(opt) {
                if (opt.msg) {
                    opt.message = opt.msg;
                }
                var options = angular.extend({}, bsWinConfig, opt);

                var newWin = {
                    winId: index++,
                    scope: $rootScope.$new()
                };

                createScope(newWin, options);

                if (options.onOk) {
                    newWin.scope.ok = function () {
                        if (options.onOk && options.onOk.apply(newWin) == false) {
                            return false;
                        }
                        newWin.el.modal("hide");
                    }
                }

                newWin.el = createWinEl(newWin.scope, options.templateUrl)
                    .on('hidden.bs.modal', function (e) {
                        if (options.onClose && options.onClose.apply(newWin, e) == false) {
                            return false;
                        }
                        destroy(newWin);
                    }).modal('show');  // 打开
                return newWin;
            }

            function createScope(win, options) {
                // console.log("bsWin createScope", options);
                var _scope = win.scope;
                _scope.title = options.title;
                _scope.message = options.message;

                _scope.winId = win.winId;
                _scope.type = options.type;
                _scope.extraData = options.extraData;

                _scope.onKeyEnter = function (e) {
                    if (e.which === 13) {
                        _scope.ok ? _scope.ok() : win.el.modal("hide");
                    } else if (e.which == 27) {
                        win.el.modal("hide");
                    }
                }

                win.scope.options = {
                    onClose: options.onClose
                };
            }

            function createWinEl(scope, tpl) {
                // debugger;
                var $compile = $injector.get("$compile"),
                    elHtml = $compile($templateCache.get(tpl))(scope);
                return $(elHtml).appendTo("body");
            }

            /**
             * 销毁窗口
             * @param el
             */
            function destroy(win) {
                win.scope.$destroy();
                win.el.unbind();
                win.el.remove();
                win.el = null;
                win = null;
                _body.css('padding-right', "");
            }

        }]);


    /**
     * 窗口默认配置
     */
    app.constant('bsWinConfig', {
        title: "提示窗口",
        message: "",
        type: "info",
        timeout: 0,
        templateUrl: alertTplPath,
        onOk: null,
        onClose: function (e) {
        }
    });

    app.run(["$templateCache", function ($templateCache) {
        // 提示窗口模板
        $templateCache.put(alertTplPath,
            '<div class="alertDialog modal fade" tabindex="-1" role="dialog" style="z-index: {{10000 + winId}};" ng-keypress="onKeyEnter($event)">\
                <div class="modal-dialog" role="document" style="margin:80px auto;width:80%;max-width:400px;">\
                    <div class="modal-content">\
                        <div class="modal-header">\
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>\
                            <h4 class="modal-title text-info">{{title || \'消息提醒\'}}</h4>\
                        </div>\
                        <div class="modal-body text-{{type==\'error\'? \'danger\' : type}}">\
                            <p class="alertDialogMessage">\
                                <i class="fa fa-{{type == \'success\' ? \'check\' : (type == \'error\' ? \'ban\' : type)}}" aria-hidden="true"></i> \
                                {{message}}\
                            </p>\
                        </div>\
                        <div class="modal-footer">\
                            <button type="button"  class="btn btn-default" data-dismiss="modal">关闭</button>\
                        </div>\
                    </div>\
                </div>\
            </div>');

        // 询问窗口模板
        $templateCache.put(confirmTplPath,
            '<div class="confirmDialog modal fade" tabindex="-1" role="dialog" style="z-index: {{10000 + winId}};" ng-keypress="onKeyEnter($event)">\
                <div class="modal-dialog" role="document" style="margin:80px auto;width:80%;max-width:400px;">\
                    <div class="modal-content">\
                        <div class="modal-header">\
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>\
                            <h4 class="modal-title text-info">{{title||\'提示\'}}</h4>\
                        </div>\
                        <div class="modal-body text-primary"><p><i class="fa fa-question-circle" aria-hidden="true"></i> {{message}}</p></div>\
                        <div class="modal-footer">\
                            <button type="button" ng-click="ok()" class="btn btn-primary" >确认</button>\
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>\
                        </div>\
                    </div>\
                </div>\
            </div>');

        // 重新登录窗口
        $templateCache.put(reloginTplPath,
            '<div class="confirmDialog modal fade" tabindex="-1" role="dialog" style="z-index: {{10000 + winId}};" ng-keypress="onKeyEnter($event)">\
                <div class="modal-dialog" role="document" style="margin:80px auto;width:80%;max-width:400px;">\
                    <div class="modal-content">\
                        <div class="modal-header">\
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>\
                            <h4 class="modal-title text-info">登录窗口</h4>\
                        </div>\
                        <div class="modal-body text-primary">\
                            <p><i class="fa fa-warning" aria-hidden="true"></i> {{message}}</p>\
                            <div><span ng-show="vm.message" class="errors" ng-bind="vm.message"></span></div>\
                            <div><span data-valmsg-for="username" data-valmsg-replace="true" class="errors"></span></div>\
                            <div class="form-group has-feedback">\
                                <input type="text" class="form-control" placeholder="用户名" maxlength="100" name="username" id="username" ng-model="model.username" data-val="true" data-val-required="用户名必填">\
                                <span class="glyphicon glyphicon-envelope form-control-feedback"></span>\
                            </div>\
                            <div><span data-valmsg-for="password" data-valmsg-replace="true" class="errors"></span></div>\
                            <div class="form-group has-feedback">\
                                <input type="password" class="form-control" placeholder="密码" maxlength="100" name="password" id="password" ng-model="model.password" data-val="true" data-val-required="密码必填">\
                                <span class="glyphicon glyphicon-lock form-control-feedback"></span>\
                            </div>\
                            <div ng-if="loginCaptcha"><span data-valmsg-for="captcha" data-valmsg-replace="true" class="errors"></span></div>\
                            <div ng-if="loginCaptcha" class="form-group has-feedback">\
                                <input type="text" class="form-control" placeholder="验证码" maxlength="10" name="captcha" id="captcha" ng-model="model.captcha" data-val="true" data-val-required="验证码必填"/>\
                                <span>\
                                    <label class="code_word" ng-click="reloadVerify()">看不清，换一张</label>\
                                    <img id="verify" width="100" height="50" ng-click="reloadVerify()" ng-src="{{captchaImagePath}}" />\
                                </span>\
                            </div>\
                        </div>\
                        <div class="modal-footer">\
                            <div class="pull-right col-sm-12">\
                                <button type="button" ng-click="ok()" class="btn btn-primary btn-block btn-flat l-icon">登 录</button>\
                            </div>\
                        </div>\
                    </div>\
                </div>\
            </div>');

        // 查看流程图窗口
        $templateCache.put(traceImgTplPath,
            '<div class="traceImgDialog modal fade" tabindex="-1" role="dialog" style="z-index: {{10000 + winId}};" ng-keypress="onKeyEnter($event)">\
                <div class="modal-dialog modal-lg" role="document">\
                    <div class="modal-content">\
                        <div class="modal-header">\
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>\
                            <h4 class="modal-title text-info">流程图</h4>\
                        </div>\
                        <div class="modal-body" style="overflow: auto;min-height: 500px;">\
                            <img id="traceImg" ng-src="{{imgUrl}}">\
                        </div>\
                    </div>\
                </div>\
            </div>')

    }]);
})();
/**
 * 封装 datetimepicker 的 angular 指令
 * 例子：
 * <div class="input-group date" sn-datetimepicker format="yyyy-mm-dd" style="width: 200px;">
 *    <input class="form-control" size="16" type="text" name="beginDate" ng-model="vm.model.beginDate" readonly>
 *    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
 *    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
 * </div>
 *
 */
(function () {
    'use strict';

    if (typeof angular === 'undefined') {
        return;
    }

    angular.module('sn.common').directive('snDatetimepicker', function () {

        var defaultAttr = {
            language: 'zh-CN',
            autoclose: true,
            minView: 2,
            pickerPosition: "bottom-right",
            format: 'yyyy-mm-dd',
            todayBtn: true,
            todayHighlight: true
        }

        return {
            restrict: 'A',
            require: '?ngModel',
            scope: {
                ngModel: '='
            },
            link: function (scope, element, attr, ngModel) {

                element.datetimepicker($.extend(defaultAttr, attr));

            }
        }
    });

})();
/**
 * 全局angular $http拦截器
 */
(function () {
    'use strict';

    if (typeof angular === 'undefined') {
        return;
    }

    var app = angular.module('sn.common');

    app.factory("commonHttpInterceptor", ["$injector", "bsWin", commonHttpInterceptor]);

    function commonHttpInterceptor($q, bsWin) {  //  anguler $http全局请求拦截器
        return {
            request: function (config) {
                // config.headers["Token"] = common.getToken();
                config.headers["X-Requested-With"] = "XMLHttpRequest";  // 用于后台ajax请求的判断
                return config;
            },
            // requestError: function (request) {
            //     return $q.reject(request);
            // },
            // response: function (response) {
            //     console.log("response", response);
            //     return response;
            // },
            responseError: function (response) {
                // console.log("responseError", response);
                errorHandle(bsWin, response.status, response.data);
                return $q.reject(response);
            }
        };

    }

    /**
     * 统一错误状态处理
     * @param bsWin     提示窗口对象
     * @param status    响应状态码
     * @param data      响应数据
     */
    function errorHandle(bsWin, status, data) {
        // debugger;
        switch (status) {
            case 500:
                bsWin.error(data.message || "系统内部错误");
                break;
            case 400:
                bsWin.warning(data.message || "操作失败");
                break;
            case 401:
                bsWin.warning("缺少操作权限");
                break;
            case 403:
                bsWin.warning("无权限执行此操作");
                break;
            case 404:
                bsWin.error("未找到相应的操作");
                break;
            case 408:
                bsWin.relogin("登录超时，请重新登录系统");
                break;
            case 412:
            case 415:
                bsWin.warning(data.message || "操作失败");
                break;
            default:
                bsWin.error("操作失败");
        }
    }

    app.config(['$httpProvider', function ($httpProvider) { // 添加拦截器
        // Initialize get if not there
        if (!$httpProvider.defaults.headers.get) {
            $httpProvider.defaults.headers.get = {};
        }

        //禁用IE对ajax的缓存
        $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
        $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';

        $httpProvider.interceptors.push(commonHttpInterceptor);
    }]);

    if (typeof jQuery === 'undefined') {
        return;
    }
    // 设置jQuery的ajax全局默认配置
    jQuery(document).ajaxSend(function (event, request, settings) {
        // request.setRequestHeader('Token', common.getToken());
    }).ajaxError(function (event, jqXHR, settings, thrownError) {
        var _body = angular.element("body"),
            scope = _body.scope(),
            bsWin = _body.injector().get("bsWin"),
            data = angular.isString(jqXHR.responseText) ? JSON.parse(jqXHR.responseText || "{}") : jqXHR.responseText;

        scope.$apply(function () {
            errorHandle(bsWin, jqXHR.status, data || {});
        });
    });
})();
/**
 * 封装multipleSelect的angular指令
 */
(function () {
    'use strict';

    if (typeof angular === 'undefined') {
        return;
    }

    var config = {
        placeholder: "请选择单位",
        filter: true,
        noMatchesFound: '未查询到单位',
        single: true
    }

    angular.module('sn.common').directive('snMultipleSelect', function ($timeout) {

        return {
            restrict: 'EA',
            require: '?ngModel',
            scope: {
                snMultipleSelect: '=',
                ngModel: '='
            },
            link: function (scope, element, attrs, ngModel) {
                element.multipleSelect($.extend({}, config, attrs));

                var isLoad = false, isNewValue = false;
                scope.$watch('snMultipleSelect', function (value) {
                    if (!value) {
                        return;
                    }

                    var tags = [];
                    $.each(value, function (index, v) {
                        tags.push(util.format('<option value="{0}">{1}</option>', v.organId, v.organName));
                    });
                    element.html(tags.join(""));

                    $timeout(function () {
                        element.multipleSelect("refresh", true);
                        setElValue();
                        isLoad = true;
                    }, 100)
                });

                scope.$watch(function(){
                    return ngModel.$modelValue;
                }, function(newValue){
                    isNewValue = true;
                    if (isLoad) {
                        $timeout(setElValue);
                    }
                })

                function setElValue() {
                    if (isNewValue) {
                        element.multipleSelect("setSelects", [ngModel.$modelValue]);
                        isNewValue = false;
                    }
                }
            }
        }
    });

})();
/**
 * 参照kendo.data.odata.js，主要处理中文乱码参数的问题
 * 源码地址：https://github.com/telerik/kendo-ui-core/blob/f68dbc2b3a4b51958c77be30234a44b39b9efbc1/src/kendo.data.odata.js
 * var params = {
 *  filter: { field: "name", operator: "contains", value: "测试" },
 *  sort: { field: "itemOrder", dir: "desc" },
 *  take: 10
 * };
 * $.toOdata(params);
 */
(function ($, undefined) {
    if (typeof jQuery === 'undefined') {
        return;
    }

    var odataFilters = {
            eq: "eq",
            neq: "ne",
            gt: "gt",
            gte: "ge",
            lt: "lt",
            lte: "le",
            ni:"ni",
            in: "in",
            like: "substringof",
            nolike: "substringof",
            contains: "substringof",
            doesnotcontain: "substringof",
            endswith: "endswith",
            startswith: "startswith",
            isnull: "eq",
            isnotnull: "ne",
            isempty: "eq",
            isnotempty: "ne"
        },
        mappers = {
            pageSize: $.noop,
            page: $.noop,
            filter: function (params, filter, useVersionFour) {
                if (filter) {
                    filter = toOdataFilter(filter, useVersionFour);
                    if (filter) {
                        params.$filter = filter;
                    }
                }
            },
            sort: function (params, orderby) {
                if (!$.isArray(orderby)) {
                    orderby = [orderby];
                }
                var expr = $.map(orderby, function (value) {
                    var order = value.field.replace(/\./g, "/");

                    if (value.dir === "desc") {
                        order += " desc";
                    }

                    return order;
                }).join(",");

                if (expr) {
                    params.$orderby = expr;
                }
            },
            skip: function (params, skip) {
                if (skip) {
                    params.$skip = skip;
                }
            },
            take: function (params, take) {
                if (take) {
                    params.$top = take;
                }
            }
        };

    function toOdataFilter(filter, useOdataFour) {
        var result = [],
            logic = filter.logic || "and",
            idx,
            length,
            field,
            type,
            format,
            operator,
            value,
            ignoreCase,
            filters;

        if (filter.field) {
            filters = [filter];
        } else {
            filters = filter.filters || [];
        }

        for (idx = 0, length = filters.length; idx < length; idx++) {
            filter = filters[idx];
            field = filter.field;
            value = filter.value;
            operator = filter.operator;

            if (filter.filters) {
                filter = toOdataFilter(filter, useOdataFour);
            } else {
                ignoreCase = filter.ignoreCase;
                field = field.replace(/\./g, "/");
                filter = odataFilters[operator];
                if (useOdataFour) {
                    filter = odataFilters[operator];
                }

                if (operator === "isnull" || operator === "isnotnull") {
                    filter = util.format("{0} {1} null", field, filter);
                } else if (operator === "isempty" || operator === "isnotempty") {
                    filter = util.format("{0} {1} ''", field, filter);
                } else if (filter && value !== undefined) {
                    type = $.type(value);
                    if (type === "string") {
                        format = "'{1}'";
                        value = value.replace(/'/g, "''");

                        if (ignoreCase === true) {
                            field = "tolower(" + field + ")";
                        }
                        value = encodeURIComponent(value);

                    } else if (type === "date") {
                        format = "datetime'{1:yyyy-MM-dd HH:mm:ss}'";
                    } else {
                        format = "{1}";
                    }

                    if (filter.length > 3) {
                        if (filter !== "substringof") {
                            format = "{0}({2}," + format + ")";
                        } else {
                            format = "{0}(" + format + ",{2})";
                            if (operator === "doesnotcontain") {
                                if (useOdataFour) {
                                    format = "{0}({2},'{1}') eq -1";
                                    filter = "indexof";
                                } else {
                                    format += " eq false";
                                }
                            }
                        }
                    } else {
                        format = "{2} {0} " + format;
                    }

                    filter = util.format(format, filter, value, field);
                }
            }

            filter && result.push(filter);
        }

        filter = result.join(" " + logic + " ");

        if (result.length > 1) {
            filter = "(" + filter + ")";
        }

        return filter;
    }

    $.extend({
        toOdata: function (options) {
            options = options || {};

            var params = {
                $inlinecount: "allpages"
            };

            for (var option in options) {
                if (mappers[option]) {
                    mappers[option](params, options[option], true);
                } else {
                    params[option] = options[option];
                }
            }
            return params;
        },
        toOdataFilter: function (filters) {
            return toOdataFilter(filters, true)
        }
    })

})(jQuery);
/**
 *************************************************
 ******      JS工具类，定义常用的方法           ******
 *************************************************
 */
(function (window) {
    'use strict';

    var util = {
        loginUrl: formatUrl("login"),
        initJqValidation: initJqValidation,
        /**
         *请求加上ContextPath
         *
         */
        formatUrl: formatUrl,
        checkLength: checkLength,
        /**
         * 字符串格式，第一个参数为需要格式的字符串，以一对花括号和数字{0}获取后面的参数，第二个参数开始为字符串提供格式的数据
         * 如：util.format("{0} {1} !", "Hello", "World");  浏览器的控制台的输出结果为Hello World !
         * @returns {*}
         */
        format: format,
        /**
         * 根据表单根据odata格式数据   TODO 还需要处理or
         * @param from
         * @param filters
         * @returns {string|object}
         */
        buildOdataFilter: buildOdataFilter,
        getTableOption: getTableOption,
        getTableFilterOption: getTableFilterOption,
        initUpload: initUpload,
        /**
         * 高频执行事件/方法的防抖
         * @param func      延时调用函数
         * @param wait      延迟多长时间
         * @param immediate 是否立刻执行
         * @returns {Function}
         */
        debounce: function (func, wait, immediate) {
            var timeout, result;

            var debounced = function () {
                var context = this;
                var args = Array.prototype.slice.call(arguments);

                if (timeout) clearTimeout(timeout);
                if (immediate) {
                    // 如果已经执行过，不再执行
                    var callNow = !timeout;
                    timeout = setTimeout(function () {
                        timeout = null;
                    }, wait);
                    if (callNow) {
                        result = func.apply(context, args)
                    }
                }
                else {
                    timeout = setTimeout(function () {
                        result = func.apply(context, args)
                    }, wait);
                }
                return result;
            };

            debounced.cancel = function () {
                clearTimeout(timeout);
                timeout = null;
            };

            return debounced;
        },
        UUID: function () {
            var d = new Date().getTime();
            var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = (d + Math.random() * 16) % 16 | 0;
                d = Math.floor(d / 16);
                return (c == 'x' ? r : (r & 0x7 | 0x8)).toString(16);
            });
            return uuid;
        }
    };

    window.util = util;

    function initJqValidation(selector) {
        selector = selector || "form";
        $(selector).removeData("validator").removeData("unobtrusiveValidation");
        $.validator.unobtrusive.parse(selector);
    }

    function formatUrl(url) {
        url = url || "";
        // 如果链接带有协议头，则返回原始链接
        if (url.indexOf("http://") == 0 || url.indexOf("https://") == 0
            || typeof contextPath == "undefined" || url.indexOf('javascript:;') > -1) {
            return url;
        }
        if (url.indexOf("/") != 0) {
            url = '/' + url;
        }
        return contextPath + format(url, Array.prototype.slice.call(arguments, 1));
    }

    var formatReg = /\{(\w+)(:[^\}]+)?(\.[^\}]+)?\}/g;
    function format() {
        var str = arguments[0] || "", _d;
        if (!str) return false;
        var data = Array.prototype.slice.call(arguments, 1);
        if (data.length == 1 && (Object.prototype.toString.call(data[0]) == "[object Object]")
            || Object.prototype.toString.call(data[0]) == "[object Array]") {
            data = data[0];
        }
        return str.replace(formatReg, function (m, i, f) {
            _d = data[i];
            if (typeof _d == "undefined" || _d == null) {
                return "";
            } else if (Object.prototype.toString.call(_d) == "[object Date]") {
                return _d.format(f.substr(1));
            } else if (typeof _d == "object") {
                return JSON.stringify(_d);
            } else {
                if (f && f.length > 1) {
                    _d = new Date(_d).format(f.substr(1));
                }
                return _d;
            }
        });
    }

    function checkLength(obj, max, id) {
        if (!id) {
            return false;
        }
        var n = 0;
        max = max || 0;
        if (obj) {
            var length = obj.length;
            if (length <= max) {
                n = max - length;
            } else {
                n = 0;
            }
        } else {
            n = max;
        }
        $("#" + id).html("<font size='5'>" + n + "</font>");
    }

    // 封装bs table的默认值
    var tableDefaultOption = {
        sidePagination: "server",
        cache: false,
        striped: true,
        pagination: true,
        pageSize: 10,
        pageList: [10, 25, 50, 100],
        showColumns: true,
        showRefresh: true,
        minimumCountColumns: 2,
        clickToSelect: true,
        maintainSelected: true,
        filterForm:null,
        totalField: "count",            // 修改bs table默认统计字段
        dataField: "value",             // 修改bs table默认数据字段
        defaultSort: "createdDate desc",
        defaultFilters: null
    };

    function buildOdataFilter(from, filters) {
        filters = filters || [];
        if (!angular.isArray(filters)) {
            filters = [filters];
        }
        $(from).find('input,select,textarea').each(function (index, obj) {
            var $me = $(this), val = obj.value, name = obj.name, operator;
            if (!name || !val) return;
            if ($me.hasClass("date-input")) {
                if (val.indexOf(" ") == -1) {
                    val += " 00:00:00";
                }
                val = new Date(val);
            }
            if (name.indexOf("filter_") == 0) {
                var tmp = name.split("_");
                if (tmp.length == 3) {
                    operator = tmp[1];
                    name = tmp[2] || "";
                } else if (tmp.length == 2) {
                    operator = "like";
                    name = tmp[1] || "";
                } else {
                    return;
                }
            } else if (val) {
                operator = $me.attr("operator") || "eq";
            }
            var names = name.split("|");
            if (names.length > 1) {
                var orLogic = [];
                for (var n in names) {
                    orLogic.push({
                        field: names[n],
                        operator: operator,
                        value: val
                    });
                }
                filters.push({
                    logic: "or",
                    filters: orLogic
                });
            } else {
                filters.push({
                    field: name,
                    operator: operator,
                    value: val
                });
            }
        });

        return filters.length == 0 ? "" : $.toOdataFilter({
            logic: "and",
            filters: filters
        });
    }

    /**
     * 获取bs table的配置项（默认封装，带分页）
     * @param option    自定义配置项
     */
    function getTableOption(option) {
        option = option || {};
        return $.extend({}, tableDefaultOption, {
            queryParams: function (params) {
                var me = this,
                    _params = {
                        "$skip": params.offset,
                        "$top": params.limit,
                        "$orderby": !params.sort ? me.defaultSort : (params.sort + " " + params.order),
                        "$filter": buildOdataFilter(me.filterForm || me.toolbar || "#toolbar", me.defaultFilters)
                    };
                if (me.pagination) {
                    _params["$inlinecount"] = "allpages";
                }
                return _params;
            }
        }, option);
    }

    function getTableFilterOption(option) {
        option = option || {};
        return $.extend({}, tableDefaultOption, {
            filterControl: true,
            filterShowClear: true
        }, option);
    }

    function initUpload(selector, options, bsWin) {
        if (!selector) {
            return false;
        }
        if (!bsWin) {
            alert("缺少参数，上传控件无法初始化");
            return false;
        }
        var $scope = angular.element("body").scope();
        options = $.extend({}, {
            uploader: formatUrl("sys/attachment/upload"),
            swf: formatUrl('libs/uploadify/uploadify.swf'),
            cancelImage: formatUrl('libs/uploadify/uploadify-cancel.png'),
            multi: false,
            fileObjName: 'files',       // 上传参数名称
            fileSizeLimit: "40MB",      // 上传文件大小限制
            buttonText: '<span style="font-size: 12px">选择文件</span>',
            fileExt: '*.pdf;*.txt;*.png;*.jpg;*.doc;*.docx;*.wps',
            fileTypeExts: '*.pdf;*.txt;*.png;*.jpg;*.doc;*.docx;*.wps',
            fileTypeDesc: "请选择*.pdf;*.txt;*.png;*.jpg;*.doc;*.docx;*.wps文件",     // 文件说明
            removeCompleted: true,     // 设置已完成上传的文件是否从队列中移除，默认为true
            // 'onSelectError' : function(file) {
            //     alert('The file ' + file.name + ' returned an error and was not added to the queue.');
            // },
            onUploadError: function (file, errorCode, errorMsg, errorString) {
                $scope.$apply(function () {
                    switch (errorCode) {
                        case -100:
                            bsWin.error("上传的文件数量已经超出系统限制的" + myUploadify.uploadify('settings', 'queueSizeLimit') + "个文件！");
                            break;
                        case -110:
                            bsWin.error("文件 [" + file.name + "] 大小超出系统限制的" + myUploadify.uploadify('settings', 'fileSizeLimit') + "大小！");
                            break;
                        case -120:
                            bsWin.error("文件 [" + file.name + "] 大小异常！");
                            break;
                        case -130:
                            bsWin.error("文件 [" + file.name + "] 类型不正确！");
                            break;
                        default:
                            bsWin.error("上传失败");
                            break;
                    }
                })
            },
            onCancel: function (file) {
                $scope.$apply(function () {
                    bsWin.confirm("询问提示", "确认删除该文件吗！");
                })
            },
            onFallback: function () {
                setTimeout(function () {
                    $scope.$apply(function () {
                        bsWin.warning("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。"); //检测FLASH失败调用
                    })
                }, 100)
            },
            onQueueComplete: function () {
                $('.upload_load').remove();
            }
        }, options || {});

        var myUploadify = $(selector).uploadify(options);
        return myUploadify;
    }

    // 对Date的扩展，将 Date 转化为指定格式的String
    // 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
    // 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
    // 例子：
    // (new Date()).format("yyyy-MM-dd HH:mm:ss.S") ==> 2006-07-02 08:09:04.423
    // (new Date()).format("yyyy-M-d H:m:s.S")      ==> 2006-7-2 8:9:4.18
    if (!Date.prototype.format) {
        Date.prototype.format = function (fmt) {
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "H+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds() //毫秒
            };
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt))
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }
    }

    // 判断是否引入 bootstrapTable 插件包
    if ($.fn.bootstrapTable && $.fn.bootstrapTable.defaults.filterTemplate) {
        var BootstrapTable = $.fn.bootstrapTable.Constructor,
            _initColumn = BootstrapTable.prototype.initColumn;

        // 根据 filterControl 的配置，设置数据的格式化，主要针对数据字典
        BootstrapTable.prototype.initColumn = function (column) {
            var filterControl = column.filterControl;
            if (typeof filterControl == "string" && filterControl.toLowerCase() == "dict") {
                var filterData = column.filterData;
                if (typeof filterData == "string") {
                    var filterDataName = column.filterDataName || "dictName";
                    if (!column.formatter) {
                        column.formatter = util.format('{{{0}.dicts[row.{1}].{2}}}',
                            filterData, column.field, filterDataName
                        );
                    }
                }
            }

            return $.extend({}, _initColumn.apply(this, Array.prototype.slice.apply(arguments)), column);
        }

        // 扩展表头 filter
        $.extend($.fn.bootstrapTable.defaults.filterTemplate, {
            // 针对数据字典的处理（单选）
            dict: function (that, field, isVisible, operator, placeholder, column) {
                var filterData = column.filterData || "DICT",
                    filterDataKey = column.filterDataKey || "key",
                    filterDataName = column.filterDataName || "value";
                if (typeof filterData == "string") {
                    if (filterDataKey == "key") {
                        filterDataKey = "dictKey";
                    }
                    if (!filterDataName || filterDataName == "value") {
                        filterDataName = "dictName";
                    }
                    // if (!column.formatter) {
                    //     column.formatter = util.format("{{{0}.dicts[row.{1}].dictName}}", filterData, field);
                    // }

                    var tag = util.format('<select class="form-control odata-filter select-filter-control bootstrap-table-filter-control-{0}" name="{0}" style="width: 100%; visibility: {1}">' +
                        '<option value="">请选择……</option><option ng-repeat="x in {2}.dictList" value="\{\{{3}\}\}">{{{4}}}</option></select>',
                        field, isVisible, filterData, "x." + filterDataKey, "x." + filterDataName
                    );
                    return tag;
                } else if ($.isArray(filterData)) {
                    var tags = [util.format('<select class="form-control odata-filter select-filter-control bootstrap-table-filter-control-{0}" name="{0}" style="width: 100%; visibility: {1}">', field, isVisible)];
                    tags.push('<option value="">请选择……</option>');
                    $.each(filterData, function (i, o) {
                        var key = o[filterDataKey], name = o[filterDataName];
                        if (key && name) {
                            tags.push(util.format('<option value="{0}">{1}</option>', key, name));
                        }
                    });
                    tags.push('</select>');
                    return tags.join("");
                } else {
                    return "";
                }
            }
        });

    }

})(window);