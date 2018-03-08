//设置
angular.module('route.seting', ['seting.controller'])
	.config(function($stateProvider, $urlRouterProvider) {

		$stateProvider

			.state('settings', {
				url: '/personal/settings',
				cache:'false',
				templateUrl: 'business/tabs/personal/seting/seting.html',
				controller: "setingCtrl"

			})
			//版本检测
			.state('version', {
				url: '/personal/settings/version',
				cache:'false',
				templateUrl: 'business/tabs/personal/seting/version.html',
				controller: "versionCtrl"

			})
	});