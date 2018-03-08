//个人中心功能路由
angular.module('route.doingTasks', [
	'doingTasks.controller',
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('doingTasks', {
				url: '/workDynamic/doingTasks',
				cache:'false',
				templateUrl: 'business/tabs/workDynamic/doingTasks/doingTasks.html',
				controller: "doingTasksCtrl"

			})
			
	});
			