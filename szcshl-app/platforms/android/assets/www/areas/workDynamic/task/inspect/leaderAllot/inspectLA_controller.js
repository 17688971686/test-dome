angular.module('inspectLA.controller', ['common.service', 'ngCordova', 'angularBMap'])

	//待办工作列表控制器
	.controller('inspectLACtrl', ["$scope", "$state", "inspectProcessService", 'GlobalVariable', 'HttpUtil', 'fileSvc', 'Message',
		function($scope, $state, inspectProcessService, GlobalVariable, HttpUtil, fileSvc, Message) {

			$scope.processName = "投诉报修";
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
					operationType: 'inspectFeedBack',
					dealSuggestion: "指派巡查人员",
					businessId: $scope.model.inspectMaintenanceId,
					userIds:[userName]
				}
				inspectProcessService.submitProc(JSON.stringify(workJson));
			}
			$scope.goProccessRecords = function(number) {
				HttpUtil.getProccessRecords($scope.model.processInstanceId, $scope);
			}
			//返回
			$scope.back = function() {
				$state.go('task');
			};
			init(); //初始化
			function init() {
				inspectProcessService.get($scope);
				HttpUtil.getUserRole('', function(data) {
					$scope.list = data;
				});
			};
		}
	])