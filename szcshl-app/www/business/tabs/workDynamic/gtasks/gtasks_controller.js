angular.module('gtasks.controller', ['gtasks.service', 'common.service', 'global_variable'])

	.controller('gtasksCtrl', ['$rootScope','$scope', '$ionicPopup', '$state', '$timeout','APP_EVENTS','$ionicHistory','gtasksService',
		function($rootScope,$scope, $ionicPopup, $state, $timeout,APP_EVENTS,$ionicHistory,gtasksService) {

			//返回
			$scope.back = function() {
				$state.go('tab.workDynamic');
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

