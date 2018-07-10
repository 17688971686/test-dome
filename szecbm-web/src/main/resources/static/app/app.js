(function () {
    'use strict';

    angular.module('myApp', ['ui.router', 'angular-loading-bar', "bsTable", "sn.common"]).config(appConfig).run(appRun).controller('indexCtrl', indexCtrl);

    indexCtrl.$inject = ['$scope', '$state', '$http', '$compile', "snBaseUtils"];

    // 系统主控制器
    function indexCtrl($scope, $state, $http, $compile, snBaseUtils) {
        var topMenuBox = $("#topMenuBox"),
            leftMenuBox = $("#leftMenuBox");

        // 状态切换开始时触发事件，销毁uploadify对象
        $scope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
            if (event) {
                $('.uploadify').each(function () {
                    $(this).uploadify('destroy');
                })
            }
        });
        $scope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
            // console.log("$stateChangeSuccess", toState);
            // $scope.currentState_name = toState.name;
            // $scope.currentState_url = toState.url;
            // $scope.currentState_params = toParams;
            $scope.previousState_name = fromState.name;
            $scope.previousState_params = fromParams;
        });
        //实现返回前一页的函数
        $scope.backPrevPage = function (toState) {
            toState = toState || "welcome";
            $state.go($scope.previousState_name || toState, $scope.previousState_params);
        };

        $scope.csHide = function (param) {
            if (param && param != $scope.menuBoxResId) {
                if ($("#menu_" + param).length > 0) {
                    $scope.menuBoxResId = param;
                } else {
                    $scope.menuBoxResId = "pm";
                }
            }
        }

        //  初始化系统菜单
        snBaseUtils.initMenus($scope, function (sysTopMenusTpl, sysLeftMenusTpl) {
            if (!$scope.menuBoxResId) {
                $scope.menuBoxResId = "pm";
            }
            topMenuBox.append($compile(sysTopMenusTpl)($scope));
            leftMenuBox.append($compile(sysLeftMenusTpl)($scope));
        });


        // 初始化数据字典
        snBaseUtils.initDicts($scope);


    }

    appConfig.$inject = ['$urlRouterProvider', 'cfpLoadingBarProvider', '$compileProvider'];

    // 基础配置
    function appConfig($urlRouterProvider, cfpLoadingBarProvider, $compileProvider) {
        cfpLoadingBarProvider.parentSelector = '#loading-bar-container';
        cfpLoadingBarProvider.spinnerTemplate = '<div style="position:fixed;width:100%;height:100%;left:0;top:0; z-index:99;background:rgba(0, 0, 0, 0.3);overflow: hidden;"><div style="position: absolute;top:30%; width: 400px;height:40px;left:50%;"><i class="fa fa-spinner fa-pulse fa-1x fa-fw"></i>程序处理中...</div></div>';

        $urlRouterProvider.otherwise("/welcome");

        // 用于解决a标签动态href出现unsafe的问题
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|file|javascript):/);
    }

    appRun.$inject = ['$rootScope'];

    // 系统执行方法
    function appRun($rootScope) {
        $rootScope.$on('$viewContentLoaded', function (event) {
            if (jQuery.AdminLTE.layout) {  // 避免AdminLTE未初始化完成报错
                // 解决angular加载模块页面时，影响AdminLTE布局的问题
                jQuery.AdminLTE.layout.fix();
                jQuery.AdminLTE.layout.fixSidebar();
            }
        });
    }

    //===============回到顶部的按钮=============================
    var slideToTop = $('<div><i class="fa fa-chevron-up"></i></div>')
        .css({
            position: 'fixed',
            bottom: '20px',
            right: '25px',
            width: '40px',
            height: '40px',
            color: '#eee',
            'font-size': '',
            'line-height': '40px',
            'text-align': 'center',
            'background-color': '#222d32',
            cursor: 'pointer',
            'border-radius': '5px',
            'z-index': '99999',
            opacity: '.7',
            'display': 'none'
        }).on({
            'mouseenter': function () {
                slideToTop.css('opacity', '1');
            },
            'mouseout': function () {
                slideToTop.css('opacity', '.7');
            },
            "click": function () {
                $("html,body").animate({"scrollTop": top}, 500)
            }
        });
    $('.wrapper').append(slideToTop);
    $(window).scroll(function () {
        if ($(window).scrollTop() >= 150) {
            if (!slideToTop.is(':visible')) {
                slideToTop.fadeIn(500);
            }
        } else {
            slideToTop.fadeOut(500);
        }
    });
})();
