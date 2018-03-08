angular.module('task.service', [])

.service('taskservice', function($http, $q, GlobalVariable,$state,myCache) {
	
	var rootPath = GlobalVariable.SERVER_PATH;
	return {

		//获取待办工作列表
		getList: function($scope) {
			var loginName = sessionStorage.getItem("loginName");
			var deferred = $q.defer();
			var url = rootPath + "/mobile/api/work/tasks";
			url += "?loginName="+loginName;
			url += "&pageNum="+$scope.pageInfo.pageNum;
			url += "&pageSize="+$scope.pageInfo.pageSize;
			if($scope.condition.search){url += "&search="+$scope.condition.search;}
			if($scope.condition.procKey){url += "&procKey="+$scope.condition.procKey;}
			if($scope.condition.endTime){url += "&endTime="+$scope.condition.endTime;}
			if($scope.condition.startTime){url += "&startTime="+$scope.condition.startTime;}
			if($scope.condition.street){url += "&street="+$scope.condition.street;}
			
			var req = {
				method: 'GET',
				url: url
			};
			var temp_key = "/mobile/api/work/tasks";
			$http(req).success(function(data) {
				deferred.resolve(data);
				if($scope.pageInfo.pageNum == 1){ myCache.put(temp_key,data); }//保存数据到缓存
			}).error(function(error) {
				if($scope.pageInfo.pageNum == 1){ deferred.resolve(myCache.get(temp_key)); };//获取缓存数据
				deferred.reject(error);
			});
			return deferred.promise;
		},
		//获取办结工作列表
		getEndList: function($scope) {
			var deferred = $q.defer();
			var loginName = sessionStorage.getItem("loginName");
			var url = rootPath + "/mobile/api/work/endTasks";
			url += "?loginName="+loginName;
			url += "&pageNum="+$scope.pageInfo.pageNum;
			url += "&pageSize="+$scope.pageInfo.pageSize;
			if($scope.condition.search){url += "&search="+$scope.condition.search;}
			if($scope.condition.procKey){url += "&procKey="+$scope.condition.procKey;}
			if($scope.condition.endTime){url += "&endTime="+$scope.condition.endTime;}
			if($scope.condition.startTime){url += "&startTime="+$scope.condition.startTime;}
			if($scope.condition.street){url += "&street="+$scope.condition.street;}
			var req = {
				method: 'GET',
				url: url,
			};
			var temp_key = "/mobile/api/work/endTasks";
			$http(req).success(function(data) {
				deferred.resolve(data);
				if($scope.pageInfo.pageNum == 1){ myCache.put(temp_key,data); }//保存数据到缓存
			}).error(function(error) {
				if($scope.pageInfo.pageNum == 1){ deferred.resolve(myCache.get(temp_key)); };//获取缓存数据
				deferred.reject(error)
			});
			return deferred.promise;
		},
	}

});