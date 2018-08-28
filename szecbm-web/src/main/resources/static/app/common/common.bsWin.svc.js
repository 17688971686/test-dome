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
            '<div class="alertDialog modal fade" tabindex="-1" role="dialog" style="z-index: {{10000000000000000009 + winId}};" ng-keypress="onKeyEnter($event)">\
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
            '<div class="confirmDialog modal fade" tabindex="-1" role="dialog" style="z-index: {{100000000000000000009 + winId}};" ng-keypress="onKeyEnter($event)">\
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
            '<div class="confirmDialog modal fade" tabindex="-1" role="dialog" style="z-index: {{1000000 + winId}};" ng-keypress="onKeyEnter($event)">\
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
            '<div class="traceImgDialog modal fade" tabindex="-1" role="dialog" style="z-index: {{1000000 + winId}};" ng-keypress="onKeyEnter($event)">\
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