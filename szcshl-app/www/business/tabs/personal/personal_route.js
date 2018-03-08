//个人中心功能路由
angular.module('route.personal', [
	'personal.controller',
	'route.seting',//设置
	'route.userinfo'//账号信息
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('tab.personal', {
				url: '/personal',
				views: {
					'tab-personal': {
						templateUrl: 'business/tabs/personal/personal.html',
						controller: 'personalCtrl'
					}
				}

			})
	});
			