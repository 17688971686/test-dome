angular.module('dtasks.controller', ['dtasks.service', 'common.service', 'global_variable'])

	.controller('dtasksCtrl', ['$rootScope','$scope', '$ionicPopup', '$state', '$timeout','APP_EVENTS','$ionicHistory','dtasksService',
		function($rootScope,$scope, $ionicPopup, $state, $timeout,APP_EVENTS,$ionicHistory,dtasksService) {

			//返回
			$scope.back = function() {
				$state.go('tab.workDynamic');
			}
			activate();
		
			function activate(){
				dtasksService.dtasks().then(function(response){
					$scope.dtaksList=response.data.value;
   
         		},function(response){
         			console.log('初始化失败');
         		
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});

			}
	   
	
		}
	])

