//个人中心功能路由
angular.module('route.staQuery', [
	'staQuery.controller',
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('staQuery', {
				url: '/proStatistics/staQuery',
				cache:'false',
				templateUrl: 'business/tabs/proStatistics/staQuery/queryList.html',
				controller: "staQueryCtrl"

			})
			
	});
			