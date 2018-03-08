// 配置模块，控制不平平台的兼容性
angular.module('config', ['interceptor', 'global_variable'])
	.config(function($ionicConfigProvider, $httpProvider, global) {
 
		// 不同平台样式兼容性配置
		$ionicConfigProvider.platform.ios.tabs.style('standard');
		$ionicConfigProvider.platform.ios.tabs.position('bottom');
		$ionicConfigProvider.platform.android.tabs.style('standard');
		$ionicConfigProvider.platform.android.tabs.position('standard');
		$ionicConfigProvider.platform.ios.navBar.alignTitle('center');
		$ionicConfigProvider.platform.android.navBar.alignTitle('center');
 		$httpProvider.interceptors.push('MessageInterceptor');
		//$ionicConfigProvider.platform.ios.tabs.style('standard');
		//$ionicConfigProvider.platform.ios.tabs.position('bottom');
		//$ionicConfigProvider.platform.android.tabs.style('standard');
		//$ionicConfigProvider.platform.android.tabs.position('bottom');
		//
		//$ionicConfigProvider.platform.ios.navBar.alignTitle('center');
		//$ionicConfigProvider.platform.android.navBar.alignTitle('center');
		//
		//$ionicConfigProvider.platform.ios.backButton.previousTitleText('').icon('ion-ios-arrow-thin-left');
		//$ionicConfigProvider.platform.android.backButton.previousTitleText('').icon('ion-android-arrow-back');
		//
		//$ionicConfigProvider.platform.ios.views.transition('ios');
		//$ionicConfigProvider.platform.android.views.transition('android');
		//默认转换请求数据的格式
		$httpProvider.defaults.transformRequest = function(obj) {
			var str = [];
			for(var p in obj) {
				str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
			}
			str.push(encodeURIComponent("callKey") + "=" + global.SERVER_CALLKEY);
			return str.join("&");
		}
		$httpProvider.defaults.headers.post = {
			'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
		}
		
		//  缓存信息配置
		//$ionicConfigProvider.views.forwardCache(true);

	})

