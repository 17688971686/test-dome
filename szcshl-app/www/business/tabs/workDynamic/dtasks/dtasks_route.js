//个人中心功能路由
angular.module('route.dtasks', [
	'dtasks.controller',
	'route.detali', //详细页
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('dtasks', {
				url: '/workDynamic/dtasks',
				cache:'false',
				templateUrl: 'business/tabs/workDynamic/dtasks/dtasks.html',
				controller: "dtasksCtrl"

			})
			
	});
			