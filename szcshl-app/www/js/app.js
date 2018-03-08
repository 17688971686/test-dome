
angular.module('starter', ['ionic', 'route', 'config', 'global_variable', 'ngCordova', 'ionic-datepicker'])

	.run(function($ionicPlatform, global, $rootScope, $http, $state, $ionicPopup, $location, $timeout, $ionicHistory) {
		//获取屏幕宽高
		$rootScope.HEIGHT = window.screen.height;
		$rootScope.WIDTH = window.screen.width;
		//$rootScope.body_padding_top = ionic.Platform.isIOS() ? 20 : 0;//body内部距顶部像素值
		$rootScope.userInfo=JSON.parse(localStorage.getItem('userInfo'));
		$rootScope.viewMainClass = ionic.Platform.isIOS() ? 'iosViewMain' : '';
		//生成年份数组最小2016年，最大当前年份加1
		var max = global.CurrentYear + 1;
		for(var i = 2016; i <= max; i++) {
			global.Years.push({
				id: i - 2016,
				name: i + '年',
				value: i
			});
		}
		//系统就绪
		$ionicPlatform.ready(function() {
			// Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
			// for form inputs)
			if(window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
				cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
				cordova.plugins.Keyboard.disableScroll(true);
			}
			if(window.StatusBar) {
				// org.apache.cordova.statusbar required
				StatusBar.styleLightContent();
			}
		});

		//退出系统
		var exitApp = function() {
			//双击退出方式
			/*if($rootScope.backButtonPressedOnceToExit) {
				ionic.Platform.exitApp();//退出app
			} else {
				$rootScope.backButtonPressedOnceToExit = true;
				$cordovaToast.showShortBottom('再按一次退出系统');//需安装插件
				setTimeout(function() {
					$rootScope.backButtonPressedOnceToExit = false;
				}, 2000);
			}*/
			//询问框退出方式
			$ionicPopup.confirm({
				title: '确定退出系统?',
				//template: '',
				cancelText: '取消',
				okText: '退出'
			}).then(function(res) {
				if(res) {
					ionic.Platform.exitApp(); //退出app
				}
			});
		}
		//注册返回事件  注册101为退出系统事件
		$ionicPlatform.registerBackButtonAction(function(e) {

			var path = $location.path();
			if(path == '/tab/workDynamic' || path == '/tab/infrtureDibut' || path == '/tab/infrastructure' || path == '/tab/personal' || path == '/login') {
				exitApp(); //首页返回-退出系统
			} else if((path.substring(0, 13) == '/office/task/' || path.substring(0, 28) == '/office/listMaintenanceProce') && typeof $rootScope.back_function === 'function') {
				$rootScope.back_function();
			} else if($ionicHistory.backView()) {
				$ionicHistory.goBack(); //返回上一界面
			} else {
				exitApp(); //无法确定返回动作-退出系统
			}
			e.preventDefault();
			return false;
		}, 101);

	});