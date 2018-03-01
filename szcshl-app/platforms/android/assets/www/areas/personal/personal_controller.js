angular.module('personal.controller', ['personal.service', 'common.service','global'])

	.controller('personalCtrl', ['$rootScope','$scope', '$ionicPopup', '$state', '$timeout','APP_EVENTS','$ionicHistory',
		function($rootScope,$scope, $ionicPopup, $state, $timeout,APP_EVENTS,$ionicHistory) {

			$scope.$on('$ionicView.beforeEnter', function() {
				$scope.loginName = localStorage.getItem("loginName");
			});

			/*//弹出信息窗口
				$scope.showMesPopup = function() {
					var myPopup = $ionicPopup.show({
						title: '<b>' + "待开发" + '</b>'
					});
					$timeout(function() {
						myPopup.close(); // 1秒后关闭
					}, 1000);
				};*/
			//退出确认框
			$scope.logout = function() {
				var confirmPopup = $ionicPopup.confirm({
					title: '是否退出登录',
					template: '你是否确定退出登录？',
					okText: '退出',
					cancelText: '取消'
				});
				confirmPopup.then(function(res) {
					if(res) {
						//退出删除session的用户名
						localStorage.removeItem("loginName");
						localStorage.removeItem("password");
						sessionStorage.removeItem("loginName");
						
						$ionicHistory.clearCache();
						
						$state.go('login');
						//发布退出事件
						$rootScope.$broadcast(APP_EVENTS.loggedOut);
						console.log('成功退出');
					} else {
						console.log('取消退出');
					}
				});
			};
		}
	])
	//个人设置controller
	.controller('settingsCtrl', ['$rootScope', '$scope', '$ionicPopup', '$state', '$timeout', 'myCache', 'Message','GlobalVariable',
		function($rootScope, $scope, $ionicPopup, $state, $timeout, myCache, Message, GlobalVariable) {
			//返回
			$scope.back = function() {
				$state.go('tab.personal');
			}
			//版本判断
			$scope.newVersion = localStorage.getItem("VERSION_NEW");//新版本
			$scope.version = GlobalVariable.VERSION;//当前版本
			$scope.isNew = $scope.newVersion != $scope.version;//是否需要更新
			//跳到版本检测页面
			$scope.goVersion = function(){
				$state.go('version');
			}
			//清理缓存
			$scope.clearLocal = function() {
				myCache.removeAll();
				Message.show("清理完成");
			};
			///处理兼容性
			$scope.viewMainClass = "";

			if(localStorage.getItem("isIOS") == "true") {
				$scope.viewMainClass = "iosViewMain";
			}
		}
	])
	//修改密码controller
	.controller('modifyPwdCtrl', ['$scope', '$ionicPopup', '$state', '$timeout', 'personalService',
		function($scope, $ionicPopup, $state, $timeout, personalService) {

			//返回
			$scope.back = function() {
				$state.go('settings');
			}

			///处理兼容性
			$scope.viewMainClass = "";

			if(localStorage.getItem("isIOS") == "true") {
				$scope.viewMainClass = "iosViewMain";
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
				personalService.doModifyPwd({
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
	//版本检测controller
	.controller('versionCtrl', ['$rootScope', '$scope', '$state', 'GlobalVariable', 'Message',
		function($rootScope, $scope, $state, GlobalVariable, Message) {
			var info = localStorage.getItem('VERSION_INFO');
			$scope.versionInfo = JSON.parse( info || "" );//获取最新版本信息
			$scope.version = GlobalVariable.VERSION;//当前版本
			$scope.platform = ionic.Platform.platform();//获取平台
			$scope.isNew = $scope.version != $scope.versionInfo.version;//是否需要更新
			$scope.versionCheck = function(){
				GlobalVariable.versionCheck(function(versionInfo){
					$scope.versionInfo = versionInfo;
					$scope.isNew = $scope.version != $scope.versionInfo.version;//是否需要更新
					if(!$scope.isNew){
						Message.show("已经是最新版本");
					}
				});
			}
			//返回
			$scope.back = function() {
				$state.go('settings');
			}
			
		}
	]);