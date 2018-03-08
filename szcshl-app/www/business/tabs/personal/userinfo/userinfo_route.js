//账号信息
angular.module('route.userinfo', ['userinfo.controller'])
	.config(function($stateProvider, $urlRouterProvider) {

		$stateProvider

			.state('userinfo', {
				url: '/personal/userinfo',
				cache:'false',
				templateUrl: 'business/tabs/personal/userinfo/userinfo.html',
				controller: "userinfoCtrl"

			})
			//修改密码
			.state('modifyPwd', {
				url: '/personal/userinfo/modifyPwd',
				cache:'false',
				templateUrl: 'business/tabs/personal/userinfo/modifyPwd.html',
				controller: "modifyPwdCtrl"

			})
	});