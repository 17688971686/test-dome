//项目统计功能路由
angular.module('route.proStatistics', [
	'proStatistics.controller',
	'route.staQuery'//统计查询
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('tab.proStatistics', {
				url: '/proStatistics',
				views: {
					'tab-proStatistics': {
						templateUrl: 'business/tabs/proStatistics/proStatisticsIndex.html',
						controller: 'proStatisticsCtrl'
					}
				}

			})
	});
			