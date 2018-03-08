//场地路由
angular.module('route.site', ['site.controller'])
	.config(function($stateProvider) {

		$stateProvider
			//待办工作
			.state('infrastructure/site', {
				url: '/infrastructure/site/:id',
				cache: true,
				templateUrl: 'areas/infrastructure/site/site.html',
				controller: "siteCtrl"
			})

	});