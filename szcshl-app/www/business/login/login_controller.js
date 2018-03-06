angular.module('login.controller', ['login.service','common.service', 'global_variable'])

	.controller('LoginCtrl', ['$rootScope','$scope', '$ionicPopup', '$timeout', '$state', 'loginservice','Message','$window','APP_EVENTS',
		function($rootScope,$scope, $ionicPopup, $timeout, $state,loginservice,Message,$window,APP_EVENTS) {

			$scope.formUser = {};
			$scope.formUser.loginName = localStorage.getItem("loginName");
			$scope.formUser.password = localStorage.getItem("password");
			if($scope.formUser.loginName != null && $scope.formUser.password != null) {
				//获取用户名并保存到全局域中
				$rootScope.loginName = $scope.formUser.loginName;//用户名
				$rootScope.userInfo = JSON.parse(localStorage.getItem('userInfo'));//用户信息
				if(!$rootScope.userInfo){
					//读取用户信息
					loginservice.getUserInfo($rootScope.loginName).then(function(data){
						$rootScope.userInfo = data;
						localStorage.setItem("userInfo", JSON.stringify(data));//保存用户信息
					});
				}
				$state.go("tab.personal");//去到初始页面
			}

			//执行用户登录操作
			$scope.doLogin = function(user) {

				user = $scope.formUser;
				var myPopup = $ionicPopup.show({
					title: '<b>正在登录请稍后</b>',
					template: '<p style="text-align: center"><ion-spinner icon="android" class="spinner-positive"></ion-spinner></p>'
				});

				//调用service方法发送数据给后台
				loginservice.login(user).then(function(data){
					if(data.flag) {
						//获取用户信息
						loginservice.getUserInfo(user.loginName).then(function(data){
							myPopup.close();
							$rootScope.userInfo = data;
							localStorage.setItem("userInfo", JSON.stringify(data));//保存用户信息
							//成功登录时默认记住账号和密码
							$rootScope.loginName = user.loginName;
							localStorage.setItem("loginName", user.loginName);
							localStorage.setItem("password", user.password);
							//保存用户名到session级别
							sessionStorage.setItem("userName", user.loginName);
							sessionStorage.setItem("loginName", user.loginName);
							$rootScope.$broadcast(APP_EVENTS.loggedIn);
							$state.go("tab.personal");
//							$window.location.reload();//刷新页面，清除缓存，解决切换账号后不能及时拿到新账号对应数据
							//$scope.showSuccessMesPopup("正在登录请稍后");
						}, function errorCallback(response) {
							myPopup.close();
							$scope.showErrorMesPopup('无法获取用户信息');
						});
					} else {
						myPopup.close();
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