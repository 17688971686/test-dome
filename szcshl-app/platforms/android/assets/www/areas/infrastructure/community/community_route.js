//社区路由
angular.module('route.community', ['community.controller'])
	.config(function($stateProvider) {

		$stateProvider
			//待办工作
			.state('infrastructure/community', {
				url: '/infrastructure/community/:id',
				cache: true,
				templateUrl: 'areas/infrastructure/community/community.html',
				controller: "communityCtrl"
			})

	});