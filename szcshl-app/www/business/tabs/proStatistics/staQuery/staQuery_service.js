angular.module('staQuery.service', ['common.service', 'global_variable'])
	.service('staQueryService', function(global,$http , $q ) {
		return {
			staQuery: function($scope) {
				var url = global.SERVER_PATH + "/api/proStatistics/getSignList";
				url += "?pageNum=" + $scope.pageInfo.pageNum;
				url += "&pageSize=" + $scope.pageInfo.pageSize;
				url += "&search= " + $scope.condition.search;
				var deferred = $q.defer(); //生成异步对象
				
				var req = {
					method: 'POST',
					url: url,
				};
				
				var temp_key = "/api/proStatistics/getSignList";
				$http(req).success(function(data){
					//执行到这里时，改变deferred状态为执行成功，返回data为从后台取到的数据，可以继续执行then,done
					deferred.resolve(data);
//					if($scope.pageInfo.pageNum == 1){
//						//保存数据到缓存中
//						myCache.put(temp_key , data);
//					}
				}).error(function(error){
//					if($scope.pageInfo.pageNum == 1){
//						//获取缓存的数据
//						deferred.resolve(myCache.get(temp_key));
//					}
					//执行到这里时，改变deferred状态为执行失败，返回error为报错，可以继续执行fail
					deferred.reject(error);
				})
				//起到保护作用，不允许函数外部改变函数内的deferred状态
				return deferred.promise;
		
			},
		
		
		}

	});