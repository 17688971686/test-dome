angular.module('login.service', ['common.service', 'global_variable'])
	.service('loginservice', function($q,global,$http) {
		return {
			login: function(user) {
				var deferred = $q.defer();
				var url = global.SERVER_PATH + "/api/user/login";
				
				$http({
					method: 'POST',
					url: url,
					params: user,
					isIgnoreLoading:true
				}).success(function(data){
					deferred.resolve(data);
				}).error(function(res,status){
					deferred.reject(status);
				});
				
				return deferred.promise;
			},
			getUserInfo: function(loginName) {
				var deferred = $q.defer();
				var url = global.SERVER_PATH + "/api/user/getUserInfo?loginName="+loginName;
				
				$http({
					method: 'GET',
					url: url,
				}).success(function(data){
					deferred.resolve(data);
				}).error(function(res,status){
					deferred.reject(status);
				});
				
				return deferred.promise;
			}
		}

	});