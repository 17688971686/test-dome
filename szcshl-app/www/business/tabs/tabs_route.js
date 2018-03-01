// tab功能路由
angular.module('route.tabs', [
	'tabs.controller',//控制器
	'route.personal',//个人中心
	'route.workDynamic',//工作动态
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
