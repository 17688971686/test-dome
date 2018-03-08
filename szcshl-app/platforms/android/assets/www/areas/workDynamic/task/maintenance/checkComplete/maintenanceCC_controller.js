angular.module('maintenanceCC.controller', ['maintenanceProcess.service','common.service', 'ngCordova', 'angularBMap','file.service'])

	//待办工作列表控制器
	.controller('maintenanceCCCtrl', ["$scope", "$state", "maintenanceProcessService", 'GlobalVariable', 'HttpUtil', 'fileSvc', 'Message',
		function($scope, $state, maintenanceProcessService, GlobalVariable, HttpUtil, fileSvc, Message) {
			
			$scope.processName = "设施报修";
			$scope.taskId = $state.params.taskId;
			$scope.id = $state.params.id;
			$scope.model = {}; //后台查询出的数据
			$scope.data = {}; //页面传到后台的参数
            $scope._TEMP_ = {};//缓存，仅在页面临时使用
			//查看图片
			$scope.showImg = function(imgs, index) {
				fileSvc.showImage($scope, imgs, index);
			}
			//返回
			$scope.back = function() {
				switch($scope.facility_page) {
					case 0: $state.go('task');break;
					case 1: $scope.facility_page = 5;break;
					case 2: $scope.facility_page = 1;break;
					case 3: $scope.facility_page = 5;break;
					case 5: $scope.facility_page = 0;break;
					default:$state.go('task');break;	
				}
			};
			//处理页面
			$scope.getRecords = function(number) {
				HttpUtil.getProccessRecords($scope.model.processInstanceId, $scope);
			}	
			//维修设施页面切换
			$scope.facility_page = 0;//显示界面编号
			$scope.swichFacilityPage = function(num){
				$scope.facility_page = num;
			};
			//提交流程
			$scope.submitProc = function(){
				maintenanceProcessService.update($scope, function() {
					var workJson = {
						taskId: $scope.taskId,
						operationType: 'checkComplete',
						dealSuggestion: $scope.model.checkCompleteMsg,
						businessId: $scope.model.repairRegisterId
					};
					maintenanceProcessService.submitProc(JSON.stringify(workJson)); //提交流程
				}, function(error) {
				});
			}

			init(); //初始化
			function init() {
				maintenanceProcessService.get($scope,function(data){
					if( $scope.model.isCheckPass==0){
						$scope.model.checkCompleteMsg="社区确认通过";
					}else{
						$scope.model.checkCompleteMsg="社区确认不通过，需文体再次确认";
					}
				});
			};
		}
	])