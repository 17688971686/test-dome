//待办工作，办结工作路由
angular.module('route.task', ['task.controller'])
	.config(function($stateProvider, $urlRouterProvider) {

		$stateProvider
			//待办工作
			.state('task', {
				url: '/office/task/:new',
				cache: true,
				templateUrl: 'areas/workDynamic/task/task.html',
				controller: "TaskCtrl"
			})
			//办结工作
			.state('endtask', {
				url: '/office/endtask',
				cache: true,
				templateUrl: 'areas/workDynamic/task/endtask.html',
				controller: "EndTaskCtrl"
			})

	});