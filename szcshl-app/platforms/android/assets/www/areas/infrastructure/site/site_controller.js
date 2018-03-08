angular.module('site.controller', ['site.service'])

	.controller('siteCtrl', ['$scope', '$rootScope', '$state', 'siteSvc', 'GlobalVariable', '$window','basicsSvc','facilitybasicdataService','mapSvc',

		function($scope, $rootScope, $state, siteSvc, GlobalVariable, $window,basicsSvc,facilitybasicdataService,mapSvc) {
	
			$scope.id = $state.params.id;
			
			///处理兼容性
			$scope.viewMainClass = "";

			if(localStorage.getItem("isIOS") == "true") {
				$scope.viewMainClass = "iosViewMain";
			}
			
			//查看场地坐标
			$scope.showMap = function() {
				mapSvc.mapSvcObj.title = "查看场地坐标";//地图标题
				mapSvc.setBusinessPosition($scope.model.siteCoord_X, $scope.model.siteCoord_Y);//设置业务位置数据
				mapSvc.showMap($scope,'areas/infrastructure/site/siteMap.html');
			};
			//显示场地设施
			$scope.show_facilitybasicdata = function(){
				facilitybasicdataService.get($scope.model,$scope.id,function(data){
					$scope.model.facilityBasicData = data;
				});
			}
			
			//刷新
			$scope.doRefresh = function(){
				siteSvc.get($scope.id,function(data){
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
				if($rootScope.infrastructure__param && $rootScope.infrastructure__param.site){
					$scope.model = $rootScope.infrastructure__param.site;//获取场地
				}else{
					siteSvc.get($scope.id,function(data){
						$scope.model = data;
					});
				}
			}
		}
	]);