angular.module('workDynamic.service', [])

.service('workDynamicservice', ['$q','$http', 'GlobalVariable', 'myCache',function($q,$http, GlobalVariable, myCache) {
		var rootPath = GlobalVariable.SERVER_PATH;
		return {
			getCount: function(){
				var deferred = $q.defer();
				var loginName = localStorage.getItem("loginName");			
				var url = rootPath + "/mobile/api/work/count";	
				$http({
					method: 'GET',
					url: rootPath + "/mobile/api/work/count?loginName=" + loginName
				}).then(function successCallback(response) {
					deferred.resolve(response);
				}, function errorCallback(response) {
					deferred.reject(response);
				}).finally(function() {

				});
				
				return deferred.promise;
			}
		}

}]);