//login路由
angular.module('route.login', ['login.controller'])

    .config(function($stateProvider, $urlRouterProvider,$ionicConfigProvider) {

        $stateProvider

            .state('login',{
                url:'/login',
                templateUrl:'business/login/login.html',
                controller:'LoginCtrl'
            })

          
    });
