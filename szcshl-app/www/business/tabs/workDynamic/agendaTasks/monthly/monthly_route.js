//个人中心功能路由
angular.module('route.monthly', [
	'monthly.controller',
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('monthly', {
				url: '/workDynamic/agendaTasks/monthly/:id/:processInstanceId/:taskId/:backType',
				cache:'false',
				templateUrl: 'business/tabs/workDynamic/agendaTasks/monthly/monthly.html',
				controller: "monthlyCtrl"

			})
			
	});
			