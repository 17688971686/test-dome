angular.module('seting.controller', ['seting.service', 'common.service', 'global_variable', 'cache.service', 'file.service'])
	//个人设置controller
	.controller('setingCtrl', ['$rootScope', '$scope', '$ionicPopup', '$state', '$timeout', 'cacheSvc', 'Message','global', 'fileSvc',
		function($rootScope, $scope, $ionicPopup, $state, $timeout, cacheSvc, Message, global, fileSvc) {
			
			$scope.version = global.VERSION;//当前版本
			//获取服务端版本信息
//			global.versionCheck(function(versionInfo){
////				$scope.versionInfo = versionInfo;
//				$scope.newVersion = versionInfo.version;
//				$scope.isNew = true;
//			},function(versionInfo){//已经是最新版本
//				$scope.isNew = false;
//			});
//缓存测试
//cacheSvc.put('abhg',{arr:[1,2,3]});//保存缓存
//cacheSvc.remove('abhg');//清除缓存
//cacheSvc.get('abhg',function(data){//获取缓存
//	console.log(data)
//});


//fileSvc.readEntries();
fileSvc.init();
			//返回
			$scope.back = function() {
				$state.go('tab.personal');
			}
			//跳到版本检测页面
			$scope.goVersion = function(){
				$state.go('version');
			}
			//清理缓存
			$scope.clearLocal = function() {
				cacheSvc.removeAll();
				Message.show("清理完成");
			};
		}
	])

	//版本检测controller
	.controller('versionCtrl', ['$rootScope', '$scope', '$state', 'global', 'Message', '$ionicPopup',
		function($rootScope, $scope, $state, global, Message, $ionicPopup) {
			$scope.isIOS = ionic.Platform.isIOS();//判断苹果平台
			$scope.platform = ionic.Platform.platform();//获取平台
			$scope.version = global.VERSION;//当前版本
			$scope.isNew = localStorage.getItem('VERSION_NEW');//是否需要更新
			if($scope.isNew){
				var info = localStorage.getItem('VERSION_INFO');//获取新版本本地信息
				$scope.versionInfo = JSON.parse( info || "" );
			}
			$scope.versionCheck = function(){
				global.versionCheck(function(versionInfo) {
					$scope.versionInfo = versionInfo;
					$scope.isNew = true;
					$ionicPopup.confirm({
						title: '有新版本可用 v' + versionInfo.version,
						//template: versionInfo.info,
						cancelText: '下次再说',
						okText: '现在更新'
					}).then(function(res) {
						if(res) {
							if(isIOS){
								window.open(global.SERVER_PATH+'/contents/assets/ios/ios_link.html','_blank');
							}else{
								window.location.href = global.SERVER_PATH + "/contents/assets/" + $scope.platform + "/文体设施管养.apk";
							}
						} else {

						}
					});
				},function(versionInfo){
					$scope.isNew = false;
					Message.show('当前已经是最新版本');
				});
			}
			//返回
			$scope.back = function() {
				$state.go('settings');
			}
			
		}
	]);