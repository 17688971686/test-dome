//个人中心功能路由
angular.module('route.personal', ['personal.controller'])
	.config(function($stateProvider, $urlRouterProvider) {

		$stateProvider

			.state('tab.personal', {
				url: '/personal',
				views: {
					'tab-personal': {
						templateUrl: 'areas/personal/personal.html',
						controller: 'personalCtrl'
					}
				}

			})
			//个人设置
			.state('settings', {
				url: '/personal/settings',
				cache:'false',
				templateUrl: 'areas/personal/settings.html',
				controller: "settingsCtrl"

			})
			//修改密码
			.state('modifyPwd', {
				url: '/personal/settings/modifyPwd',
				cache:'false',
				templateUrl: 'areas/personal/modifyPwd.html',
				controller: "modifyPwdCtrl"

			})
			//版本检测
			.state('version', {
				url: '/personal/settings/version',
				cache:'false',
				templateUrl: 'areas/personal/version.html',
				controller: "versionCtrl"

			})
	});
			