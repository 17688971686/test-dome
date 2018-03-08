//待办工作，办结工作路由
angular.module('route.inspectProcess', ['inspectProcess.controller', 'inspectLA.controller'])
	.config(function($stateProvider, $urlRouterProvider) {

		$stateProvider
			//巡查填报
			.state('inspectProcess/inspectFeedBack', {
				url: '/office/inspectProcess/inspectFeedBack/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/inspect/inspectProcess.html',
				controller: "inspectProcessCtrl"
			})
			//领导分办
			.state('inspectProcess/leaderAllot', {
				url: '/office/inspectProcess/leaderAllot/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/inspect/leaderAllot/inspectLA.html',
				controller: "inspectLACtrl"
			})
			//办结工作
			.state('end_inspectProcess', {
				url: '/office/end_inspectProcess/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/inspect/end_inspectProcess.html',
				controller: "end_inspectProcessCtrl"
			})
	});