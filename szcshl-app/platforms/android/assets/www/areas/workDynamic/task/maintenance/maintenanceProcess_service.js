angular.module('maintenanceProcess.service', [])

	.service('maintenanceProcessService', function($http, $q, $state, HttpUtil, Message, $window, myCache) {
		
		var rootPath = HttpUtil.rootPath;
		var timeout = HttpUtil.timeout;
		var temp_key = "maintenance/get/";
		var modelId;
		function setTemp(obj){
			modelId = obj.repairRegisterId;
			myCache.put(temp_key+obj.repairRegisterId, obj); //保存最新数据
		}
		function update($scope, callBack,error){
			var loginName = sessionStorage.getItem("loginName");
			var url = rootPath + "/mobile/maintenance/update";
				var data = {
					loginName: loginName,
					jsonText: JSON.stringify($scope.model) //变动的
				};
				$http({
					method: 'POST',
					timeout: timeout,
					url: url,
					data:data
				}).then(function successCallback(response) {
					if(typeof callBack === 'function'){
						callBack();
					}
				}, function errorCallback(response) {
					if(typeof error === 'function'){
						error(response);
					}
				});
		}
		return {
			setTemp: setTemp,
			//获取设施报修记录
			getList: function($scope, callback){
				var loginName = sessionStorage.getItem("loginName");
				var url = rootPath + "/mobile/maintenance/get?$filter=state eq 1 and createdBy eq '"+loginName+"'&$inlinecount=allpages&loginName="+loginName;
				$http.get(url
				).then(function successCallback(response) {
					myCache.put(temp_key+"list", response.data); //保存最新数据
					$scope.$broadcast('scroll.refreshComplete'); //停止刷新广播
					if(typeof callback === 'function'){
						callback(response.data);
					}
				}, function errorCallback(response) {
					Message.show("获取最新数据失败");
					if(typeof callback === 'function'){
						callback(myCache.get(temp_key+"list")); //读取缓存
					}
				});
			},
			//获取基本数据 -- 初始化数据
			get: function($scope, callback) {
				var loginName = sessionStorage.getItem("loginName");
				var url = rootPath + "/mobile/repairregister/get?$filter=repairRegisterId eq '"+ $scope.id +"'";
				HttpUtil.ajax({
					method: 'get', //String
					url: url, //String
					success: function(response) {
						$scope.model = response.value[0];
					
						$scope.title = $scope.model.repairTitle;//页面标题
						//获取附件，附件放在$scope.model.files
						HttpUtil.getFileByBusinessId($scope.model, $scope.model.repairRegisterId);
						myCache.put(temp_key+$scope.id, $scope.model); //保存最新数据
						$scope.$broadcast('scroll.refreshComplete'); //停止刷新广播
						if(typeof callback === 'function'){
							callback(response.value[0]);
						}
					},
					error: function(error) {
						Message.show("获取最新数据失败");
						$scope.model = myCache.get(temp_key+$scope.id);//读取缓存
					} 
				});
			},
			//获取处理剩余时间
		getSurplusTime: function($scope, callBack){
			var loginName = sessionStorage.getItem("loginName");
			if(!(typeof callBack === 'function')){
				console.log(未定义回调函数);
				return;
			}
			var url = rootPath + "/mobile/repairregister/SurplusTime?repairRegisterId="+ $scope.id;
			$http({
				method: 'GET',
				url: url,
			}).then(function successCallback(response) {
				callBack(response.data);
			}, function errorCallback(response) {
			});
		},
		//创建设施报修
			create: function($scope, callback) {
				var loginName = sessionStorage.getItem("loginName");
				var url = rootPath + "/mobile/maintenance/create";
				var data = {
					loginName: loginName,
					jsonText: JSON.stringify($scope.model) //变动的
				};
				$http({
					method: 'POST',
					timeout: timeout,
					url: url,
					data:data
				}).then(function successCallback(response) {
					if(typeof callback === 'function'){
						myCache.put(temp_key+response.data.repairRegisterId, response.data); //保存最新数据
						callback(response.data);//保存图片
					}
				}, function errorCallback(error) {
				});
			},
		//更新流程数据
			save: function($scope) {
				update($scope,function(){
					Message.show( '操作成功',function(){
//						$window.location.reload();
					});
				},function(error){
				});
			},
			//更新
			update: update,
		//发起流程
			startProc: function($scope) {
				var loginName = sessionStorage.getItem("loginName");
				var url = rootPath + "/mobile/maintenance/start";
				$scope.model.stateValue = 'doRepair';
				var data = {
					loginName: loginName,
					jsonText: JSON.stringify($scope.model) //变动的
				};
				$http({
					method: 'POST',
					timeout: timeout,
					url: url,
					data:data
				}).success(function(response) {
					myCache.remove(temp_key+modelId); //移除数据
					Message.show( '操作成功',function(){
						$state.go('listMaintenanceProce');
					});
				}).error(function(error) {
				});
			},
		//提交流程处理--下一环节
			submitProc: function(work) {
				var loginName = sessionStorage.getItem("loginName");
				var url = rootPath + "/mobile/maintenance/submit";
					var data = {
						loginName: loginName,
						workJson: work,
					};
					$http({
						method: 'POST',
						timeout: timeout,
						url: url,
						data:data
					}).then(function successCallback(response) {
						myCache.remove(temp_key+modelId); //移除数据
						Message.show( '操作成功',function(){
							$state.go('task',{new: Math.random()});
						});
					}, function errorCallback(error) {
					});
			},
			//提交延长维修
		repairTimeDelayApply: function(data) {
			var loginName = sessionStorage.getItem("loginName");
			data.loginName = loginName;
			var url = rootPath + "/mobile/repairTimeDelayApply/proc";
			$http({
				method: 'POST',
				timeout: timeout,
				url: url,
				data:{
					jsonText: JSON.stringify(data),
					loginName: loginName
				}
			}).then(function successCallback(response) {
				Message.show( '操作成功',function(){
					data.reason = '';
					data.duration = '';
				});
			}, function errorCallback(error) {
			});
		},

		}
	})