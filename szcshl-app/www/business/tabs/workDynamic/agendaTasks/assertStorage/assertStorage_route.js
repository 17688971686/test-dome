//个人中心功能路由
angular.module('route.assertStorage', [
	'assertStorage.controller',
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('assertStorage', {
				url: '/workDynamic/agendaTasks/assertStorage/:id/:processInstanceId/:taskId/:backType',
				cache:'false',
				templateUrl: 'business/tabs/workDynamic/agendaTasks/assertStorage/assertStorage.html',
				controller: "assertStorageCtrl"

			})
			
	});
			