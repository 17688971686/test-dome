//个人中心功能路由
angular.module('route.suppletter', [
	'suppletter.controller',
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('suppletter', {
				url: '/workDynamic/agendaTasks/suppletter/:id/:processInstanceId/:taskId',
				cache:'false',
				templateUrl: 'business/tabs/workDynamic/agendaTasks/suppletter/suppletter.html',
				controller: "suppletterCtrl"

			})
			
	});
			