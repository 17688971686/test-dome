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
 *  bsWin.confirm("询问提示", "是否进行该操作！", function () { console.log("This's ok!"); }, function () { console.log("This's close!"); },function(){console.log("This's onCancel!");} );
 * 或者
 *  bsWin.confirm({
 *      title: "询问提示",
 *      message: "是否进行该操作！",
 *      onOk: function () { console.log("This's ok!"); },
 *      onClose: function () { console.log("This's close!"); },
 *      onCancel:function(){console.log("This's onCancel!");}
 *  });
 */
(function () {
    'use strict';

    var app = angular.module('app'),
        alertTplPath = "template/dialog/alert.html",
        confirmTplPath = "template/dialog/confirm.html",
        reloginTplPath = "template/dialog/relogin.html";

    app.factory('bsWin', ["$rootScope", "$injector", "$templateCache", "bsWinConfig",
        function ($rootScope, $injector, $templateCache, bsWinConfig) {
            var index = 0;

            return {
                success: function (message, onClose) {
                    return this.alert({
                        message: message,
                        type: "success",
                        onClose: onClose
                    });
                },
                warning:  function (message, onClose) {
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
                            onClose: onClose || function () { }
                        };
                        if (title) {
                            options.title = title;
                        }
                    }
                    options.templateUrl = alertTplPath;
                    return initWin(options);
                },
                confirm: function (options, message, onOk, onClose,onCancel) {
                    if (!angular.isObject(options)) {
                        var title;
                        if (!message || angular.isFunction(message)) {
                            onCancel = onClose || function(){
                                };
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
                            onClose: onClose,
                            onCancel: onCancel
                        };
                        if (title) {
                            options.title = title;
                        }
                    }
                    options.templateUrl = confirmTplPath;
                    return initWin(options);
                },
                relogin: function (message) {
                    var bsWin = this;
                    var  options = {
                        message: message,
                        onOk: function () {
                            var me = this;
                            $injector.get("$http")({
                                method: 'POST',
                                url: common.loginUrl,
                                params: me.scope.model
                            }).then(function (response) {
                                var data = response.data;
                                if (data.status == 'SUCCESS') {
                                    bsWin.success(data.message || "登錄成功！", function () {
                                        me.el.modal("hide");
                                    });
                                } else {
                                    bsWin.error(data.message);
                                }
                            });
                            return false;
                        }
                    };
                    options.templateUrl = reloginTplPath;
                    return initWin(options);
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
                        if (options.onOk && options.onOk.apply(this) == false) {
                            return false;
                        }
                        newWin.el.modal("hide");
                    }
                }
                if(options.onCancel){
                    newWin.scope.onCancel = function () {
                        if (options.onCancel && options.onCancel.apply(this) == false) {
                            return false;
                        }
                        newWin.el.modal("hide");
                    }
                }
                newWin.el = createWinEl(newWin.scope, options.templateUrl)
                    .on('hidden.bs.modal', function (e) {
                        if (options.onClose && options.onClose.apply(this, e) == false) {
                            return false;
                        }
                        destroy(newWin);
                    }).modal('show');  // 打开
                return newWin;
            }

            function createScope(win, options) {
                // console.log("bsWin createScope", options);
                win.scope.title = options.title;
                win.scope.message = options.message;

                win.scope.winId = win.winId;
                win.scope.type = options.type;
                win.scope.extraData = options.extraData;

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
                // console.log("bsWin destroy", win);
                win.scope.$destroy();
                win.el.unbind();
                win.el.remove();
                win.el = null;
                win = null;
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
            '<div class="alertDialog modal fade" tabindex="-1" role="dialog">\
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
                            <button type="button"  class="btn btn-info" data-dismiss="modal">关闭</button>\
                        </div>\
                    </div>\
                </div>\
            </div>');

        // 询问窗口模板
        $templateCache.put(confirmTplPath,
            '<div class="confirmDialog modal fade" tabindex="-1" role="dialog" style="z-index: 10000;">\
                <div class="modal-dialog" role="document" style="margin:80px auto;width:80%;max-width:400px;">\
                    <div class="modal-content">\
                        <div class="modal-header">\
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>\
                            <h4 class="modal-title text-info">{{title||\'提示\'}}</h4>\
                        </div>\
                        <div class="modal-body text-primary"><p><i class="fa fa-question-circle" aria-hidden="true"></i> {{message}}</p></div>\
                        <div class="modal-footer">\
                            <button type="button" ng-click="ok()" class="btn btn-info" >确认</button>\
                            <button type="button" ng-click="onCancel()" class="btn btn-info" data-dismiss="modal">取消</button>\
                        </div>\
                    </div>\
                </div>\
            </div>');

        // 重新登录窗口
        $templateCache.put(reloginTplPath,
            '<div class="confirmDialog modal fade" tabindex="-1" role="dialog" style="z-index: {{10000 + winId}};">\
                <div class="modal-dialog" role="document" style="margin:80px auto;width:80%;max-width:400px;">\
                    <div class="modal-content">\
                        <div class="modal-header">\
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>\
                            <h4 class="modal-title text-info">登录窗口</h4>\
                        </div>\
                        <div class="modal-body text-primary">\
                            <p><i class="fa fa-warning" aria-hidden="true"></i> {{message}}</p>\
                            <div><span ng-show="vm.message" class="errors" ng-bind="vm.message"></span></div>\
                            <div><span data-valmsg-for="loginName" data-valmsg-replace="true" class="errors"></span></div>\
                            <div class="form-group has-feedback">\
                                <input type="text" class="form-control" placeholder="用户名" maxlength="100" name="loginName" id="loginName" ng-model="model.loginName" data-val="true" data-val-required="用户名必填">\
                                <span class="glyphicon glyphicon-envelope form-control-feedback"></span>\
                            </div>\
                            <div><span data-valmsg-for="password" data-valmsg-replace="true" class="errors"></span></div>\
                            <div class="form-group has-feedback">\
                                <input type="password" class="form-control" placeholder="密码" maxlength="100" name="password" id="password" ng-model="model.password" data-val="true" data-val-required="密码必填">\
                                <span class="glyphicon glyphicon-lock form-control-feedback"></span>\
                            </div>\
                        </div>\
                        <div class="modal-footer">\
                            <div class="pull-right col-sm-12">\
                                <button type="button" ng-click="ok()"  class="btn btn-primary btn-block btn-flat l-icon">登 录</button>\
                            </div>\
                        </div>\
                    </div>\
                </div>\
            </div>');
    }]);
})();