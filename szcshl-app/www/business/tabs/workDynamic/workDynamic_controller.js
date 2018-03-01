angular.module('workDynamic.controller', ['workDynamic.service', 'common.service', 'global_variable'])

	.controller('workDynamicCtrl', ['$rootScope','$scope', '$ionicPopup', '$state', '$timeout','APP_EVENTS','$ionicHistory','workDynamicService',
		function($rootScope,$scope, $ionicPopup, $state, $timeout,APP_EVENTS,$ionicHistory,workDynamicService) {

			$scope.$on('$ionicView.beforeEnter', function() {
				$scope.loginName = localStorage.getItem("loginName");
			});
			

			activate();
		
			function activate(){
				workDynamicService.myCountInfo($rootScope.userInfo.id).then(function(response){
         			$scope.signCounts = response.data.DO_SIGN_COUNT;//待办项目
         			$scope.taskCounts = response.data.DO_TASK_COUNT;//待办任务
         
         		},function(response){
         			console.log('初始化失败');
         			
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});

			}

		}
	])

