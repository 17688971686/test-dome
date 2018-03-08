angular.module('facility.controller', ['facility.service'])

	.controller('facilityCtrl', ['$scope', '$rootScope', '$state', 'facilitySvc', 'GlobalVariable', '$window','basicsSvc','facilitybasicdataService','mapSvc',

		function($scope, $rootScope, $state, facilitySvc, GlobalVariable, $window,basicsSvc,facilitybasicdataService,mapSvc) {
	
			$scope.id = $state.params.id;
			
			///处理兼容性
			$scope.viewMainClass = "";

			if(localStorage.getItem("isIOS") == "true") {
				$scope.viewMainClass = "iosViewMain";
			}
			
			//刷新
			$scope.doRefresh = function(){
				facilitySvc.get($scope.id,function(data){
					$scope.model = data;
					$scope.$broadcast('scroll.refreshComplete'); //停止刷新广播
				});
			}
			//返回
			$scope.back = function(){
				$state.go('tab.infrastructure')
			}
			init();
			function init(){
				if($rootScope.infrastructure__param && $rootScope.infrastructure__param.facility){
					$scope.model = $rootScope.infrastructure__param.facility;//获取场地
				}else{
					facilitySvc.get($scope.id,function(data){
						$scope.model = data;
					});
				}
			}
		}
	]);