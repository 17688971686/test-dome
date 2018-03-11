angular.module('detali.controller', ['detali.service', 'common.service', 'global_variable'])

	.controller('detaliCtrl', ['$rootScope','$scope', '$ionicPopup', '$state', '$timeout','APP_EVENTS','$ionicHistory','detaliService','signFlowDealService',
		function($rootScope,$scope, $ionicPopup, $state, $timeout,APP_EVENTS,$ionicHistory,detaliService,signFlowDealService) {

			$scope.title = "项目详情信息表";
			console.log($state.params.signId);
			$scope.signid = $state.params.signId;
			//返回
			$scope.back = function() {
				$state.go('dtasks');
			}
			activate();
		
			function activate(){
				signFlowDealService.initFlowPageData($scope.signid).then(function(response){
					$scope.sign=response.data
					$scope.fileRecord=$scope.sign.fileRecord;
					$scope.dispatchDoc=$scope.sign.dispatchDocDto;
					$scope.workProgramList = $scope.sign.workProgramDtoList;
   
         		},function(response){
         			console.log('初始化失败');
         		
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});
			}
	   
	
		}
	])

