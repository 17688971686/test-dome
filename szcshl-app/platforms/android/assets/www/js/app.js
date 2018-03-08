// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.services' is found in services.js
// 'starter.controllers' is found in controllers.js
angular.module('starter', ['ionic', 'route', 'config', 'global', 'DIYfilter', /*'ng-fusioncharts',*/ 'ngCordova', 'ionic-datepicker'])

	.run(function($ionicPlatform, GlobalVariable, $rootScope, $http, $state, $ionicPopup) {
		//获取用户名并保存到全局域中
		$rootScope.loginName = localStorage.getItem("loginName");
		//全局根目录
		$rootScope.rootPath = GlobalVariable.SERVER_PATH;
		//获取屏幕宽高
		$rootScope.HEIGHT = window.screen.height;
		$rootScope.WIDTH = window.screen.width;
		//		console.log(ionic.Platform.isIOS());
		localStorage.setItem("isIOS", ionic.Platform.isIOS());


		//版本检测
		GlobalVariable.versionCheck = function(callback) {
			$http({
				method: 'get',
				url: GlobalVariable.SERVER_PATH + '/mobile/version/versionCheck?platform='+ionic.Platform.platform()
			}).then(function(response) {
				var versionInfo = response.data;
				if(versionInfo &&  versionInfo.version){
					var date = new Date();
					var version_date = date.getFullYear() + "/" + date.getMonth() + "/" + date.getDate();
					localStorage.setItem('VERSION_DATE', version_date); //保存版本检测日期
					localStorage.setItem("VERSION_INFO", JSON.stringify(versionInfo)); //保存版本信息
					localStorage.setItem("VERSION_NEW", versionInfo.version); //保存新版本号
					if(versionInfo.version != GlobalVariable.VERSION && (typeof callback === 'function')) {
						callback(versionInfo);
					}
				}
			}, function(response) {
				// 请求失败执行代码
			});
		}

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
			//自动版本检测
			var date = new Date();
			var version_date = date.getFullYear() + "/" + date.getMonth() + "/" + date.getDate();
			var VERSION_DATE = localStorage.getItem('VERSION_DATE'); //获取上次版本检测日期
			if(VERSION_DATE != version_date) {
				GlobalVariable.versionCheck(function(versionInfo) {
					$ionicPopup.confirm({
						title: '有新版本可用 v' + versionInfo.version,
						//template: versionInfo.info,
						cancelText: '下次再说',
						okText: '现在更新'
					}).then(function(res) {
						if(res) {
							window.location.href = $rootScope.rootPath + "/contents/assets/" + data.platform + "/文体设施管养.apk";
						} else {

						}
					});
				});
			}
			
			Date.prototype.Format = function (fmt) { //author: meizz 
			    var o = {
			        "M+": this.getMonth() + 1, //月份 
			        "d+": this.getDate(), //日 
			        "h+": this.getHours(), //小时 
			        "m+": this.getMinutes(), //分 
			        "s+": this.getSeconds(), //秒 
			        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
			        "S": this.getMilliseconds() //毫秒 
			    };
			    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
			    for (var k in o)
			    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
			    return fmt;
			}
		});

		//		/*注册返回事件*/
		//		$ionicPlatform.registerBackButtonAction(function(e) {
		//
		////			alert("sucess");
		//			ionic.Platform.exitApp();
		//			/*根据首页的路由判断是否提示退出*/
		//			if($location.path() == "/login") {
		//				if(!$rootScope.popup.isPopup) {
		//					showConfirm();
		//				}
		//			}
		//			e.preventDefault();
		//			return false;
		//		});

		//自定义数组删除元素方法
		Array.prototype.removeByValue = function(val) {
			for(var i = 0; i < this.length; i++) {
				if(this[i] == val) {
					this.splice(i, 1);
					break;
				}
			}
		}

	});