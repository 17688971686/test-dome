//总路由
angular.module('route', [
    'route.workDynamic',//工作状态
	'route.login',//登录
	'route.tabs',//选项卡
	'route.gtasks',//待办项目
	'route.signFlowDeal',//待办项目处理
	])

    .config(function($urlRouterProvider) {
        // 路径不匹配，默认跳转登录页面
        $urlRouterProvider.otherwise('login');
    });
