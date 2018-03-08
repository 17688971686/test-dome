//个人中心功能路由
angular.module('route.archives', [
	'archives.controller',
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('archives', {
				url: '/workDynamic/agendaTasks/archives/:id/:processInstanceId/:taskId',
				cache:'false',
				templateUrl: 'business/tabs/workDynamic/agendaTasks/archives/archives.html',
				controller: "archivesCtrl"

			})
			
	});
			