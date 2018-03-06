//个人中心功能路由
angular.module('route.workDynamic', [
	'workDynamic.controller',
	'route.gtasks',//待办项目
	'route.dtasks',//在办项目
	'route.agendaTasks',//在办任务
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('tab.workDynamic', {
				url: '/workDynamic',
				views: {
					'tab-workDynamic': {
						templateUrl: 'business/tabs/workDynamic/workDynamic.html',
						controller: 'workDynamicCtrl'
					}
				}

			})
	});
			