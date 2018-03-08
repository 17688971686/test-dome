angular.module("staQuery.controller" , [
'staQuery.service',
'common.service', 
'global_variable'
]).controller('staQueryCtrl', ['$rootScope','$scope','$q' ,'$ionicPopup', '$state', '$timeout','APP_EVENTS','$ionicHistory','staQueryService',
		function($rootScope,$scope, $q , $ionicPopup, $state, $timeout,APP_EVENTS,$ionicHistory , staQueryService) {

			//返回
			$scope.back = function() {
				$state.go('tab.proStatistics');
			}
			
			//是否有更多数据加载
			$scope.moreDataCanBeLoaded = true;
			$scope.projectList = []; //定义返回的数据
			//分页查询对象
			$scope.pageInfo = {
				pageNum: 1, //当前页码
				pageSize: 10 //每次查看多少条
			};
			
			
			$scope.condition  = {search : ""};
			
			//模糊搜索
			$scope.query = function(){
				$scope.doRefresh();
			}
			
			//刷新获取最新数据
			$scope.doRefresh = function() {//刷新时将当前页面设为第一页
				$scope.pageInfo.pageNum = 1;
				$scope.projectList = [];
				var deferred = $q.defer();
				var promise = staQueryService.staQuery($scope);
				promise.then(
					//成功回调函数
					function(data) {
						// 如果数据不为空，我们将数据挂载到$scope.task数组中
						if(data != null && data.length != 0) {
							$scope.projectList = $scope.projectList.concat(data);
							//页码+1
							$scope.pageInfo.pageNum++;
						} else {
							$scope.moreDataCanBeLoaded = false;
						}
						
						deferred.resolve(data);
					},
					//失败回调函数
					function(reason) {
						deferred.reject(reason);
						console.log("加载失败");
					}
				).finally(function() {
					// 停止广播infinite-scroll
					$scope.$broadcast('scroll.infiniteScrollComplete');
				});
				
				return deferred.promise;
			};

			
			//加载数据
			$scope.loadData = function() {
				var deferred = $q.defer();
				var promise = staQueryService.staQuery($scope);
				promise.then(
					//成功回调函数
					function(data) {
						// 如果数据不为空，我们将数据挂载到$scope.task数组中
						if(data != null && data.length != 0) {
							$scope.projectList = $scope.projectList.concat(data);
							//页码+1
							$scope.pageInfo.pageNum++;
						} else {
							$scope.moreDataCanBeLoaded = false;
						}
						
						deferred.resolve(data);
					},
					//失败回调函数
					function(reason) {
						deferred.reject(reason);
						console.log("加载失败");
					}
				).finally(function() {
					// 停止广播infinite-scroll
					$scope.$broadcast('scroll.infiniteScrollComplete');
				});
				
				return deferred.promise;
			};

		}
	])
