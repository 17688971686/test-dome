/**
 * 全局angular $http拦截器
 */
(function () {
    'use strict';

    var app = angular.module('app');

    app.factory("commonHttpInterceptor", ["$injector", "bsWin", commonHttpInterceptor]);

    function commonHttpInterceptor($q, bsWin) {  //  anguler $http全局请求拦截器

        return {
            request: function (config) {
                config.headers["Token"] = common.getToken();
                config.headers["X-Requested-With"] = "XMLHttpRequest";  // 用于后台ajax请求的判断
                return config;
            },
            responseError: function (response) {
                // console.log("responseError", response);
                if (!response.config.headers.commonHttp) { // 过滤掉用common.http发起的请求
                    errorHandle(bsWin, response.status, response.data);
                }
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
                bsWin.error("系统内部错误");
                break;
            case 401:
                // TODO 可改为用户登录弹出窗口，不需要跳转到登录界面
                bsWin.warning("登录信息失效或您没有权限,请重新登录!");
                break;
            case 403:
                bsWin.warning("无权限执行此操作");
                break;
            case 404:
                bsWin.error("未找到相应的操作");
                break;
            case 408:
                bsWin.warning("请求超时");
                break;
            case 412:
                bsWin.warning(data.message || "操作失败");
                break;
            default:
                bsWin.error("操作失败");
        }
    }

    app.config(['$httpProvider', function ($httpProvider) { // 添加拦截器
        $httpProvider.interceptors.push(commonHttpInterceptor);
    }]);

    if (jQuery) {  // 设置jQuery的ajax全局默认配置
        jQuery(document).ajaxSend(function (event, request, settings) {
            request.setRequestHeader('Token', common.getToken());
        }).ajaxError(function (event, jqXHR, settings, thrownError) {
            var _body = angular.element("body"),
                scope = _body.scope(),
                bsWin = _body.injector().get("bsWin"),
                data = angular.isString(jqXHR.responseText) ? JSON.parse(jqXHR.responseText || "{}") : jqXHR.responseText;

            scope.$apply(function () {
                errorHandle(bsWin, jqXHR.status, data || {});
            });
        });
    }
})();