angular.module('login.controller', ['login.service','common.service','global'])

	.controller('LoginCtrl', ['$rootScope','$scope', '$ionicPopup', '$timeout', '$state', 'loginservice','Message','$window','APP_EVENTS',
		function($rootScope,$scope, $ionicPopup, $timeout, $state,loginservice,Message,$window,APP_EVENTS) {

			$scope.formUser = {};
			$scope.formUser.loginName = localStorage.getItem("loginName");
			$scope.formUser.password = localStorage.getItem("password");
			if($scope.formUser.loginName != null && $scope.formUser.password != null) {
				sessionStorage.setItem("loginName", $scope.formUser.loginName);
				$state.go("tab.workDynamic");
			}

			//执行用户登录操作
			$scope.doLogin = function(user) {
				
				$scope.formUser.loginName = '陈得芳';
				$scope.formUser.password = '123456';
				user = $scope.formUser;
				var myPopup = $ionicPopup.show({
					title: '<b>正在登录请稍后</b>',
					template: '<p style="text-align: center"><ion-spinner icon="android" class="spinner-positive"></ion-spinner></p>'
				});
				
				//调用service方法发送数据给后台
				loginservice.login(user).then(function(data){
					myPopup.close();
					if(data.isSuccess) {
						//成功登录时默认记住账号和密码
						localStorage.setItem("loginName", user.loginName);
						localStorage.setItem("password", user.password);
						//保存用户名到session级别
						sessionStorage.setItem("userName", user.loginName);
						sessionStorage.setItem("loginName", user.loginName);
						$rootScope.$broadcast(APP_EVENTS.loggedIn);
						//$location.path('/workDynamic');
						$state.go("tab.workDynamic");
						//$window.location.reload();//刷新页面，清除缓存，解决切换账号后不能及时拿到新账号对应数据
						//$scope.showSuccessMesPopup("正在登录请稍后");
						
					} else {
						$scope.showErrorMesPopup(data.message);
					}
				},function(status){
					myPopup.close();
					var errMsg = '';
					if(-1 == status){
						errMsg = '服务器无响应';
					}
					$scope.showErrorMesPopup(errMsg);
				});
				/*loginservice.login(user,function(data){
					if(data.isSuccess) {
						//成功登录时默认记住账号和密码
						localStorage.setItem("loginName", user.loginName);
						localStorage.setItem("password", user.password);
						//保存用户名到session级别
						sessionStorage.setItem("userName", user.loginName);
						sessionStorage.setItem("loginName", user.loginName);
						$rootScope.$broadcast(APP_EVENTS.loggedIn);
						//$location.path('/workDynamic');
						$state.go("tab.workDynamic");
						//$window.location.reload();//刷新页面，清除缓存，解决切换账号后不能及时拿到新账号对应数据
						//$scope.showSuccessMesPopup("正在登录请稍后");
					} else {
						$scope.showErrorMesPopup(data.message);
					}
				},function(status){
					var errMsg = '';
					if(-1 == status){
						errMsg = '服务器无响应';
					}
					$scope.showErrorMesPopup(errMsg);
				});*//*.then(function(data) {
					if(data.isSuccess) {
						//成功登录时默认记住账号和密码
						localStorage.setItem("loginName", user.loginName);
						localStorage.setItem("password", user.password);
						//保存用户名到session级别
						sessionStorage.setItem("userName", data.message);
						sessionStorage.setItem("loginName", user.loginName);
						$scope.showSuccessMesPopup("正在登录请稍后");
					} else {
						$scope.showErrorMesPopup(data.message);
					}
				}, function(error) {
					$scope.showErrorMesPopup(error);
				});*/

			};

			//弹出错误信息窗口
			$scope.showErrorMesPopup = function(title) {
				var myPopup = $ionicPopup.show({
					title: '<b>' + title + '</b>'
				});
				$timeout(function() {
					myPopup.close(); // 1秒后关闭
				}, 1000);
			};

			//弹出成功信息窗口
			$scope.showSuccessMesPopup = function(title) {
				var myPopup = $ionicPopup.show({
					title: '<b>' + title + '</b>',
					template: '<p style="text-align: center"><ion-spinner icon="android" class="spinner-positive"></ion-spinner></p>'
				});
				return myPopup;
				/*$timeout(function() {
					myPopup.close(); // 2秒后关闭
					$state.go("tab.workDynamic");
				}, 2000);*/
			};
		}
	]);