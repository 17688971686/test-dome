angular.module('personal.controller', ['personal.service', 'common.service', 'global_variable'])

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

