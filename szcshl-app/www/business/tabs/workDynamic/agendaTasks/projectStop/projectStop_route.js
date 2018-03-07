//个人中心功能路由
angular.module('route.projectStop', [
	'projectStop.controller',
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('projectStop', {
				url: '/workDynamic/agendaTasks/projectStop/:id/:processInstanceId/:taskId',
				cache:'false',
				templateUrl: 'business/tabs/workDynamic/agendaTasks/projectStop/projectStop.html',
				controller: "projectStopCtrl"

			})
			
	});
			