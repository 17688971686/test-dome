angular.module('repairTimeDelayApply.controller', ['repairTimeDelayApply.service', 'common.service'])

	//待办工作列表控制器
	.controller('repairTimeDelayApplyCtrl', ["repairTimeDelayApplyService","repairregisterService","$scope",'$state','GlobalVariable', 'Message',
		function(repairTimeDelayApplyService,repairregisterService,$scope,$state,GlobalVariable, Message) {
			
			var vm = this;
			var taskId = $state.params.taskId;
			$scope.id = $state.params.id;
			$scope.data = {};
            $scope.taskId=taskId;
            $scope.tiemApply={};
			$scope.doRefresh = function(){
				$scope.get();
			};

			//提交流程处理
			$scope.submitProc = function(){
				repairTimeDelayApplyService.update($scope, function() {
					var workJson = {
						taskId: $scope.taskId,
						operationType: 'leadercheck',
						dealSuggestion: $scope.tiemApply.approvalOpinion,
						operation:$scope.tiemApply.operation,
						businessId: $scope.tiemApply.id
					};
					repairTimeDelayApplyService.submitProc(JSON.stringify(workJson)); //提交流程
				}, function(error) {
				});
			}				
			//返回
			$scope.back = function() {
				$state.go('task');
//				GlobalVariable.goBack($state);
			}
			init();
			function init(){
	           repairTimeDelayApplyService.get($scope,function(data){
	           	$scope.tiemApply=data;
	           	$scope.tiemApply.approvalOpinion="";
	           	$scope.id=data.repairRegisterId;
	           		repairregisterService.get($scope);
	           					
	           });
			}
		}
	])