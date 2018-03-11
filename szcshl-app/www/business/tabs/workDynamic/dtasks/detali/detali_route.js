//个人中心功能路由
angular.module('route.detali', [
	'detali.controller',
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('detali', {
				url: '/dtasks/detali/:signId',
				cache:'false',
				templateUrl: 'business/tabs/workDynamic/dtasks/detali/detali.html',
				controller: "detaliCtrl"

			})
			
	});
			