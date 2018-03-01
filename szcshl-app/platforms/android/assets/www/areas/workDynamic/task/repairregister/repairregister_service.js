angular.module('repairregister.service', ['global','common.service'])

	.service('repairregisterService', function($http, $state, HttpUtil, Message, $window,myCache) {
		var loginName = sessionStorage.getItem("loginName");
		var rootPath = HttpUtil.rootPath;
		var timeout = HttpUtil.timeout;
		var temp_key = "repairregister/";
		var modelId;
		function setTemp(obj){
			modelId = obj.repairRegisterId;
			myCache.put(temp_key+modelId, obj); //保存最新数据
		}
		function update($scope, callBack, error) {
			var url = rootPath + "/mobile/repairregister/update";
			var data = {
				loginName: loginName,
				jsonText: JSON.stringify($scope.model) //变动的
			};
			$http({
				method: 'POST',
				url: url,
				data: data,
				timeout: timeout
			}).then(function successCallback(response) {
				if(typeof callBack === 'function') {
					callBack();
				}
			}, function errorCallback(response) {
				if(typeof error === 'function') {
					error(response);
				}
			});
		}
		return {
			setTemp: setTemp,
			get: function($scope,callback) {
				var url = rootPath + "/mobile/repairregister/get?$filter=id eq '" + $scope.id + "'";
				HttpUtil.ajax({
					method: 'get', //String
					url: url, //String
					data: "", //Object
					dataType: 'Notjson', //String
					success: function(response) {
						$scope.model = response.value[0];
						HttpUtil.getFileByBusinessId($scope.model, $scope.model.repairRegisterId);
						$scope.title = $scope.model.repairTitle; //页面标题
						myCache.put(temp_key+$scope.id, $scope.model); //保存最新数据
						if(typeof callback === 'function'){
							callback(response.value[0]);
						}
						$scope.$broadcast('scroll.refreshComplete'); //停止刷新广播
					}, //function
					error: function(error) {
						console.log("获取最新数据失败");
						$scope.model = myCache.get(temp_key+$scope.id); //读取缓存数据
					} //function
				});
			},
			//获取处理剩余时间
			getSurplusTime: function($scope, callBack) {
				if(!(typeof callBack === 'function')) {
					console.log(未定义回调函数);
					return;
				}
				var url = rootPath + "/mobile/repairregister/SurplusTime?repairRegisterId=" + $scope.id;
				$http({
					method: 'GET',
					url: url,
				}).then(function successCallback(response) {
					callBack(response.data);
				}, function errorCallback(response) {
				});
			},
			//提交流程处理
			save: function($scope) {
				update($scope, function() {
					Message.show('操作成功' ,function() {
						//						$window.location.reload();
					});
				}, function(error) {
				});
			},
			update: update,
			//提交流程处理
			submitProc: function(work) {
				var url = rootPath + "/mobile/repairregister";
				var data = {
					loginName: loginName,
					workJson: work,
				};
				$http({
					method: 'POST',
					url: url,
					data: data,
					timeout: timeout
				}).then(function (response) {
					myCache.remove(temp_key+modelId); //移除缓存数据
					Message.show('操作成功' ,function() {
						$state.go('task',{new: Math.random()});
					});
				}, function (error) {
				});
			},
			//提交延长维修
			repairTimeDelayApply: function(data) {
				data.loginName = loginName;
				var url = rootPath + "/mobile/repairTimeDelayApply/proc";
				$http({
					method: 'POST',
					url: url,
					timeout: timeout,
					data: {
						jsonText: JSON.stringify(data),
						loginName: loginName
					}
				}).then(function successCallback(response) {
					Message.show('操作成功' ,function() {
						data.reason = '';
						data.duration = '';
					});
				}, function errorCallback(error) {
				});
			},

		}

	});