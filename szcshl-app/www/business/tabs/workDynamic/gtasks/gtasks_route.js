//个人中心功能路由
angular.module('route.gtasks', [
	'gtasks.controller',
	'route.signFlowDeal',//待办项目处理
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('gtasks', {
				url: '/workDynamic/gtasks',
				cache:'false',
				templateUrl: 'business/tabs/workDynamic/gtasks/gtasks.html',
				controller: "gtasksCtrl"

			})
			
	});
			