//个人中心功能路由
angular.module('route.appraise', [
	'appraise.controller',
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('appraise', {
				url: '/workDynamic/agendaTasks/appraise/:id/:processInstanceId/:taskId/:backType',
				cache:'false',
				templateUrl: 'business/tabs/workDynamic/agendaTasks/appraise/appraise.html',
				controller: "appraiseCtrl"

			})
			
	});
			