//工作动态功能路由
angular.module('route.workDynamic', [
	'office.controller',//工作动态控制器
	
	])
	.config(function($stateProvider, $urlRouterProvider) {

		$stateProvider

			.state('tab.workDynamic', {
			url: '/workDynamic',
			cache:false,
			views: {
				'tab-workDynamic': {
					templateUrl: 'areas/workDynamic/workDynamic.html',
					controller: "workDynamicCtrl"
				}
			}
		})
	});