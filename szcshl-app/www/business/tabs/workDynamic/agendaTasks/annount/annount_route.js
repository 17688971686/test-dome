//个人中心功能路由
angular.module('route.annount', [
	'annount.controller',
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('annount', {
				url: '/workDynamic/agendaTasks/annount/:id/:processInstanceId/:taskId/:backType',
				cache:'false',
				templateUrl: 'business/tabs/workDynamic/agendaTasks/annount/annount.html',
				controller: "annountCtrl"

			})
			
	});
			