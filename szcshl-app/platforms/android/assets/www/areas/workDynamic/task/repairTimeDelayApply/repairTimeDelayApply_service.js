angular.module('repairTimeDelayApply.service', ['global','common.service'])

	.service('repairTimeDelayApplyService', function($http, $state, HttpUtil, Message, $window,myCache) {
		var loginName = sessionStorage.getItem("loginName");
		var rootPath = HttpUtil.rootPath;
		var timeout = HttpUtil.timeout;
		var temp_key = "repairTimeDelayApply/";
		var modelId;
		function setTemp(obj){
			modelId = obj.id;
			myCache.put(temp_key+modelId, obj); //保存最新数据
		}
		function update($scope, callBack, error) {
			var url = rootPath + "/mobile/repairTimeDelayApply/update";
			var data = {
				loginName: loginName,
				jsonText: JSON.stringify($scope.tiemApply) //变动的
			};
			$http({
				method: 'POST',
				timeout: timeout,
				url: url,
				data: data
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
				var url = rootPath + "/mobile/repairTimeDelayApply/get?$filter=id eq '" + $scope.id + "'";
				HttpUtil.ajax({
					method: 'get', //String
					url: url, //String
					data: "", //Object
					dataType: 'Notjson', //String
					success: function(response) {
						myCache.put(temp_key+$scope.id, response.value[0]); //保存最新数据
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
			getList: function($scope,callback) {
				var url = rootPath + "/mobile/repairTimeDelayApply/get?$filter=repairRegisterId eq '" + $scope.id + "' &$orderby=createdDate desc";
				HttpUtil.ajax({
					method: 'get', //String
					url: url, //String
					data: "", //Object
					dataType: 'Notjson', //String
					success: function(response) {
						myCache.put(temp_key+$scope.id, response.value[0]); //保存最新数据
						if(typeof callback === 'function'){
							callback(response);
						}
						$scope.$broadcast('scroll.refreshComplete'); //停止刷新广播
					}, //function
					error: function(error) {
						console.log("获取最新数据失败");
						$scope.model = myCache.get(temp_key+$scope.id); //读取缓存数据
					} //function
				});
			},
			//提交流程处理
			save: function($scope) {
				update($scope, function() {
					Message.show('操作成功' ,function() {
						//$window.location.reload();
					});
				}, function(error) {
				});
			},
			update: update,
			//提交流程处理
			submitProc: function(work) {
				var url = rootPath + "/mobile/repairTimeDelayApply";
				var data = {
					loginName: loginName,
					workJson: work,
				};
				$http({
					method: 'POST',
					timeout: timeout,
					url: url,
					data: data
				}).then(function successCallback(response) {
					myCache.remove(temp_key+modelId); //移除缓存数据
					Message.show('操作成功' ,function() {
						$state.go('task',{new: Math.random()});
					});
				}, function errorCallback(error) {
				});
			},
		}

	});