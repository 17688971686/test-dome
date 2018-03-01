angular.module('maintenanceLA.controller', ['maintenanceProcess.service', 'common.service', 'ngCordova', 'angularBMap','file.service'])

	//待办工作列表控制器
	.controller('maintenanceLACtrl', ["$scope", "$state", "maintenanceProcessService", 'GlobalVariable', 'HttpUtil', 'fileSvc', 'Message',
		function($scope, $state, maintenanceProcessService, GlobalVariable, HttpUtil, fileSvc, Message) {

			$scope.processName = "设施报修";
			$scope.taskId = $state.params.taskId;
			$scope.id = $state.params.id;
			$scope.model = {}; //后台查询出的数据
			$scope.data = {}; //页面传到后台的参数

			//查看图片
			$scope.showImg = function(imgs, index) {
				fileSvc.showImage($scope, imgs, index);
			}
			//提交
			$scope.submitProc = function(userName) {
				var workJson = {
					taskId: $scope.taskId,
					operationType: 'leaderAllot',
					dealSuggestion: "指派维修人员",
					businessId: $scope.model.repairRegisterId,
					userIds: [userName]
				}
				maintenanceProcessService.submitProc(JSON.stringify(workJson));
			}
			$scope.getRecords = function(number) {
				HttpUtil.getProccessRecords($scope.model.processInstanceId, $scope);
			}
			//返回
			$scope.back = function() {
				$state.go('task');
				//				GlobalVariable.goBack($state);
			};
			init(); //初始化
			function init() {
				/*$scope.model = GlobalVariable.setBack($state, "get" + $scope.id); //
				if($scope.model == undefined) {
					maintenanceProcessService.get($scope);
				} else {
					$scope.title = $scope.model.repairTitle; //页面标题
				}*/
				maintenanceProcessService.get($scope);
				HttpUtil.getUserRole('', function(data) {
					$scope.list = data;
				});
			};
		}
	])