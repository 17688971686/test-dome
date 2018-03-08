//项目详情页首页的路由
angular.module('route.commonDetail', [
	'commonDetail.controller'
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('commonDetail', {
				url: '/commonDetail/:signId',
				cache:'false',
				templateUrl: 'business/tabs/commonDetail/commonIndex.html',
				controller: "commonDetailCtrl"

			})
			
	});
			