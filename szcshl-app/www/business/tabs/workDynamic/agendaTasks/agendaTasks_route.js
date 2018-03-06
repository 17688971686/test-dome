//个人中心功能路由
angular.module('route.agendaTasks', [
	'agendaTasks.controller',
	'route.bookBuy',//图书任务处理
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('agendaTasks', {
				url: '/workDynamic/agendaTasks',
				cache:'false',
				templateUrl: 'business/tabs/workDynamic/agendaTasks/agendaTasks.html',
				controller: "agendaTasksCtrl"

			})
			
	});
			