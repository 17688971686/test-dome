// tab功能路由
angular.module('route.tabs', ['tabs.controller'])
  .config(function($stateProvider, $urlRouterProvider) {

    $stateProvider
      .state('tab', {
        url: '/tab',
        abstract: true,
        templateUrl: 'areas/tabs/tabs.html',
        controller:'TabsCtrl'
      })


  });
