/**
 *应用的拦截器定义
 *@author lqs
 *@date 2017/9/6
 */
(function() {
	'use strict';
	var app = angular.module('interceptor', ['global']);

	app.factory('MessageInterceptor', ["$q", "$timeout", "$injector", "GlobalVariable", function($q, $timeout, $injector, GlobalVariable) {
		var popup = undefined;
		var alertInstance = undefined;

		function showMsg(msg) {
			var $ionicPopup = $injector.get('$ionicPopup');
			popup = $ionicPopup.show({
				title: msg + '<i class="icon ion-load-a"></i>',
			});
		}

		function closeMsg() {
			if(popup) {
				popup.close();
				popup = undefined;
			}
		}

		function alertMsg(msg) {
			var deferred = $q.defer();
			var $ionicPopup = $injector.get('$ionicPopup');
			alertInstance = $ionicPopup.alert({
				title: '提示',
				template: "<center>" + msg + "</center>",
				okText: '确定'
			}).then(function() {
				alertInstance = undefined;
				deferred.resolve();
			});

			return deferred.promise;
		}
		return {
			request: function(config) {
				if(config.url.indexOf('.html') == -1) {
					config.params = config.params || {};
					config.params.callKey = GlobalVariable.SERVER_CALLKEY;
				}
				if(config.method != 'GET'&&!config.isIgnoreLoading) {
					if(!popup) {
						showMsg('正在处理');
					}

				}
				return config || $q.when(config);
			},
			requestError: function(response) {
				closeMsg();
				if(response.config.method != 'GET' && !alertInstance) {
					alertMsg('请求错误');
				}

				return $q.reject(response);
			},
			// 可选，拦截成功的响应
			response: function(response) {
				if(response.config.method != 'GET') {
					closeMsg();
				}

				return response || $q.when(reponse);
			},
			responseError: function(response) {
				closeMsg();
				if(response.config.method != 'GET' && !alertInstance&&!response.config.isIgnoreLoading) {
					// 提示操作失败
					if(response.status == 401) {
						alertMsg('没有操作权限');
					} else if(response.status == -1) {
						alertMsg('与服务器已断开连接');
					} else if(response.status == 500) {
						alertMsg('服务器内部错误');
					}
				}

				return $q.reject(response);
			}
		};

	}]);
})();