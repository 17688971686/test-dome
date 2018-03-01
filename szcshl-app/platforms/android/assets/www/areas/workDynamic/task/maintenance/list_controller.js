angular.module('listMaintenanceProce.controller', ['maintenanceProcess.service', 'common.service', 'basics.service',
		'ngCordova', 'facilitybasicdata.service', 'repairregister.service', 'file.service', 'map.service', 'siteInfo.service'
	])

	//待办工作列表控制器
	.controller('listMaintenanceProceCtrl', ["$scope", "$state", "maintenanceProcessService", 'GlobalVariable', 'facilitybasicdataService', 'Message',
		'basicsSvc', 'siteInfoService', 'fileSvc',
		function($scope, $state, maintenanceProcessService, GlobalVariable, facilitybasicdataService, Message, basicsSvc, siteInfoService, fileSvc) {

			///处理兼容性
			$scope.viewMainClass = "";
			if(localStorage.getItem("isIOS") == "true") {
				$scope.viewMainClass = "iosViewMain";
			}
			
			$scope.doRefresh = function(){
				maintenanceProcessService.getList($scope, function(data) {
					// 停止广播ion-refresher
					$scope.$broadcast('scroll.refreshComplete');
					$scope.list = data.value;
					$scope.count = data.count;
				});
			}

			//返回
			$scope.back = function() {
				$state.go('tab.workDynamic');
				//				GlobalVariable.goBack($state);
			};
			init(); //初始化
			function init() {
				maintenanceProcessService.getList($scope, function(data) {
					$scope.list = data.value;
					$scope.count = data.count;
				});
			};
		}
	])