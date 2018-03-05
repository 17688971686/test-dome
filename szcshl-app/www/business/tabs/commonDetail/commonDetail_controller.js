angular.module('commonDetail.controller', ['signFlowDeal.service' , 'common.service', 'global_variable' ])

	.controller('commonDetailCtrl', ['$rootScope','$scope', '$ionicPopup', '$state', '$timeout','APP_EVENTS','$ionicHistory','signFlowDealService',
		function($rootScope,$scope, $ionicPopup, $state, $timeout,APP_EVENTS,$ionicHistory , signFlowDealService) {

			$scope.$on('$ionicView.beforeEnter', function() {
				$scope.loginName = localStorage.getItem("loginName");
			});
			$scope.title = "项目详情信息表";
			$scope.signid = $state.params.signId;
			
			//返回
			$scope.back = function() {
				$state.go('staQuery');
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

