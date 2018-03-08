//基础设施路由
angular.module('route.infrastructure', [
	'infrastructure.controller',//基础设施控制器
	'route.community',//社区路由
	'route.site',//场地路由
	'route.facility',//设施路由
	])
	.config(function($stateProvider, $urlRouterProvider) {

		$stateProvider

			.state('tab.infrastructure', {
			url: '/infrastructure',
			cache:true,
			views: {
				'tab-infrastructure': {
					templateUrl: 'areas/infrastructure/infrastructure.html',
					controller: "infrastructureCtrl"
				}
			}
		})
	});