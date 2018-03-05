// tab功能路由
angular.module('route.tabs', [
	'tabs.controller',//控制器
	'route.personal',//个人中心
	'route.workDynamic',//工作动态
	'route.proStatistics',//项目统计
	'route.commonDetail' , //项目详情页首页
	]).config(function($stateProvider, $urlRouterProvider) {

    $stateProvider
      .state('tab', {
        url: '/tab',
        abstract: true,
        cache: true,
        templateUrl: 'business/tabs/tabs.html',
        controller:'TabsCtrl'
      })


  });
