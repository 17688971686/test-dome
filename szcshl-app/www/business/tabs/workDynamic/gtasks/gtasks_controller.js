angular.module('gtasks.controller', ['gtasks.service', 'common.service', 'global_variable'])

	.controller('gtasksCtrl', ['$rootScope','$scope', '$ionicPopup', '$state', '$timeout','APP_EVENTS','$ionicHistory','gtasksService','workDynamicService',
		function($rootScope,$scope, $ionicPopup, $state, $timeout,APP_EVENTS,$ionicHistory,gtasksService,workDynamicService) {

			//返回
			$scope.back = function() {
				workDynamicService.myCountInfo($rootScope.userInfo.id).then(function(response){
         			$rootScope.signCounts = response.data.DO_SIGN_COUNT;//待办项目
         			$rootScope.taskCounts = response.data.DO_TASK_COUNT;//待办任务
         			$state.go('tab.workDynamic');
         		},function(response){
         			console.log('初始化失败');
         			
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});

			}
			activate();
		
			function activate(){
				gtasksService.gtasks($rootScope.userInfo.id).then(function(response){
					$scope.gtaksList=response.data.value;
   
         		},function(response){
         			console.log('初始化失败');
         		
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});

			}
	   
	
		}
	])

