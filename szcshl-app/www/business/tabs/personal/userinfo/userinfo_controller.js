angular.module('userinfo.controller', ['userinfo.service', 'common.service', 'global_variable'])

	.controller('userinfoCtrl', ['$rootScope','$scope', '$ionicPopup', '$state', '$timeout','APP_EVENTS','$ionicHistory',
		function($rootScope,$scope, $ionicPopup, $state, $timeout,APP_EVENTS,$ionicHistory) {
			//返回
			$scope.back = function() {
				$state.go('tab.personal');
			}
		}
	])
	//修改密码controller
	.controller('modifyPwdCtrl', ['$scope', '$ionicPopup', '$state', '$timeout', 'userinfoSvc',
		function($scope, $ionicPopup, $state, $timeout, userinfoSvc) {

			//返回
			$scope.back = function() {
				$state.go('userinfo');
			}

			$scope.doModifyPwd = function(user) {

				var loginName = localStorage.getItem("loginName");
				if(!loginName) {
					var myPopup = $ionicPopup.show({
						title: '<b>未登录，请登录 </b>',
						template: '<p style="text-align: center"><ion-spinner icon="android" class="spinner-positive"></ion-spinner></p>'
					});
					$timeout(function() {
						myPopup.close(); // 2秒后关闭
						$state.go('login');
					}, 2000);
					return;
				}
				userinfoSvc.doModifyPwd({
					loginName: loginName,
					oldPwd: user.oldPwd,
					password: user.newPwd
				}, function(data) {
					if(data.isSuccess) {
						//修改成功，跳转到个人设置

						var myPopup = $ionicPopup.show({
							title: '<b>修改密码成功，请重新登录 </b>',
							template: '<p style="text-align: center"><ion-spinner icon="android" class="spinner-positive"></ion-spinner></p>'
						});
						$timeout(function() {
							myPopup.close(); // 2秒后关闭
							localStorage.removeItem("loginName");
							localStorage.removeItem("password");
							sessionStorage.removeItem("loginName");
							$state.go('login');
						}, 2000);

					} else {
						var myPopup = $ionicPopup.show({
							title: '<b> 修改密码失败 ' + data.message + '</b>',
							template: '<p style="text-align: center"><ion-spinner icon="android" class="spinner-positive"></ion-spinner></p>'
						});

						$timeout(function() {
							myPopup.close(); // 2秒后关闭
						}, 2000);
					}

				});
			}
		}
	])