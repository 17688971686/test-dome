//设施路由
angular.module('route.facility', ['facility.controller'])
	.config(function($stateProvider) {

		$stateProvider
			//待办工作
			.state('infrastructure/facility', {
				url: '/infrastructure/facility/:id',
				cache: true,
				templateUrl: 'areas/infrastructure/facilitybasicdata/facility.html',
				controller: "facilityCtrl"
			})

	});