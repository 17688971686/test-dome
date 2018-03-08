angular.module('office.controller', ['workDynamic.service','global'])

	.controller('workDynamicCtrl', ['$rootScope','$scope', '$state', 'workDynamicservice', 'GlobalVariable', '$window','APP_EVENTS','myCache',

		function($rootScope,$scope, $state, workDynamicservice, GlobalVariable, $window,APP_EVENTS,myCache) {
			var temp_key = "/api/work/count";
			//刷新
			$scope.doRefresh = function(){
				workDynamicservice.getCount($scope).then(function(response){
         			$scope.counts = response.data;
         			myCache.put(temp_key,$scope.counts);
         			$scope.$broadcast('scroll.refreshComplete');
         		},function(response){
         			$scope.counts = myCache.get(temp_key);
         		}).finally(function(){
         			console.log('refresh complete event...');
         			$scope.$broadcast('scroll.refreshComplete');
         		});
         		
			}
			
			init();
			
			function init(){
				workDynamicservice.getCount($scope).then(function(response){
         			$scope.counts = response.data;
         			myCache.put(temp_key,$scope.counts);
         		},function(response){
         			console.log('初始化失败');
         			$scope.counts = myCache.get(temp_key);
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});

			}
			
		}
	]);