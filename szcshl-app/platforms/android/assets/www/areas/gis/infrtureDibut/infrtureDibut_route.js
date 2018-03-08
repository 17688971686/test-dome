//基础设施路由
angular.module('route.infrtureDibut', ['infrtureDibut.controller'])
	.config(function($stateProvider) {

		$stateProvider

			.state('tab.infrtureDibut', {
				url: '/infrtureDibut',
				cache:true,
				views: {
					'tab-infrtureDibut': {
						templateUrl: 'areas/gis/infrtureDibut/infrtureDibut.html',
						controller: "infrtureDibutCtrl"
					}
				}
			})
	});