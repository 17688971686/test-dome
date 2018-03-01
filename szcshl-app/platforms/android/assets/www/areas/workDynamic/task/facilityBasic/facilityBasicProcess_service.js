angular.module('facilityBasicProcess.service', [])

.service('facilityBasicProcessService', function($http, $q, HttpUtil,$state,Message) {
	var loginName = sessionStorage.getItem("loginName");
	var rootPath = HttpUtil.rootPath;
	var timeout = HttpUtil.timeout;
	return {
		//任务办理
		proceSubmit: function($scope) {

			$scope.model.data.loginName = loginName;
			var url = rootPath + "/mobile/facilityBasicData/procSubmit";

			$http({
				method:'post',
				timeout: timeout,
				url:url,
				data:$scope.model.data,
			}).success(function(response) {
				Message.showSuccess( function() {
					$state.go('task',{new: Math.random()});
				});
			}).error(function(error) {
				Message.showError(error);//操作失败提示
			});

		},
		
		get: function ($scope) {

			$scope.data.loginName = loginName;
			var deferred = $q.defer();
			var url = rootPath + "/mobile/facilityBasicData/";
			var suc = function(response){
				$scope.model.map = response;
				$http({
					method:'get',
					url:url + "get?$filter=id eq '"+ $scope.id +"'",
				}).success(function(response) {
					$scope.model.facility = response.value[0];
					$scope.$broadcast('scroll.refreshComplete');
				}).error(function(error) {
					Message.showError(error);//操作失败提示
				});
			};
			
			$http({
				method:'post',
				url:url + "getProc",
				data:$scope.data
			}).success(suc).error(function(error){
				Message.showError(error);//操作失败提示
				});
        
        },
		
		
		
		
	}
});