angular.module('inspectProcess.service', [])

.service('inspectProcessService', function($q,$http, HttpUtil, Message, $window, $state, myCache) {
	var loginName = sessionStorage.getItem("loginName");
	var rootPath = HttpUtil.rootPath;
	var timeout = HttpUtil.timeout;
	var temp_key = "/inspectMaintenance/get/";
	var modelId;
	function setTemp(obj){
		modelId = obj.inspectMaintenanceId;
		myCache.put(temp_key+modelId, obj); //保存最新数据
	}
	function update($scope,callback,error){
		var url = rootPath + "/mobile/inspectMaintenance/update";
				var data = {
					loginName: loginName,
					jsonText: JSON.stringify($scope.model)
				};
				$http({
					method: 'POST',
					timeout: timeout,
					url: url,
					data:data
				}).then(function successCallback(response) {
					if(typeof callback === 'function'){
						callback();
					}
				}, function errorCallback(response) {
				});
	}
	return {
		setTemp: setTemp,
		//获取基本数据 -- 初始化数据
			get: function($scope) {
				var deferred = $q.defer();
				var url = rootPath + "/mobile/inspectMaintenance/get";
				 + $scope.id;
				$http.get(url+"?$filter=inspectMaintenanceId eq '" + $scope.id + "'&needTask=0")
  				.success(function (response) {
  					$scope.model = response.value[0];
					if($scope.model.inspectResultContent){
						$scope.model.isNeedRepair += '';
					}else{
						$scope.model.isNeedRepair = '';
					}
					deferred.resolve(response);
					//HttpUtil.getFileByBusinessId($scope.model, $scope.model.inspectMaintenanceId);
					myCache.put(temp_key+$scope.id, $scope.model); //保存最新数据
					$scope.$broadcast('scroll.refreshComplete'); //停止刷新广播
  				})
  				.error(function(error) {
  					deferred.reject(error);
					Message.show("获取数据失败，读取本地数据");
					$scope.model = myCache.get(temp_key+$scope.id);//读取缓存数据
				});
				
				return deferred.promise;
			},
			//保存
			save: function($scope){
				update($scope,function(){
					Message.show( '操作成功',function(){
//						$window.location.reload();
					});
				},function(){});
			},
			update: update,
			//提交巡查
			submitProc: function(workJson){
				var url = rootPath + "/mobile/inspectMaintenance";
				var data = {
					loginName: loginName,
					workJson: workJson,
				};
				$http({
					method: 'POST',
					timeout: timeout,
					url: url,
					data:data
				}).then(function successCallback(response) {
					myCache.remove(temp_key+modelId); //移除缓存数据
					Message.show( '操作成功',function(){//操作成功提示
						$state.go('task',{new: Math.random()});
					});
				},function(){});
			},
		
	}
});