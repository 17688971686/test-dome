angular.module('facilityBasicProcess.controller', ['facilityBasicProcess.service', 'common.service'])

	//待办工作列表控制器
	.controller('facilityBasicProcessCtrl', ["facilityBasicProcessService","$scope",'$state','GlobalVariable', 'Message',
		function(facilityBasicProcessService,$scope,$state,GlobalVariable, Message) {
			
			var vm = this;
			var taskId = $state.params.taskId;
			$scope.id = $state.params.id;
			$scope.data = {};

			$scope.doRefresh = function(){
				$scope.get();
			};
			$scope.get = function(){
				
            	$scope.data.taskId = taskId;
				facilityBasicProcessService.get($scope);
				
			}

			$scope.proceSubmit = function (op){
				Message.showProcessing();//显示正在处理
				$scope.model.data.operation = op;
            	$scope.model.data.businessId = $scope.id;
            	$scope.model.data.taskId = taskId;
            	
            	facilityBasicProcessService.proceSubmit($scope);
			}

			//返回
			$scope.back = function() {
				$state.go('task');
//				GlobalVariable.goBack($state);
			}
			
			init();
			function init(){
				$scope.model = GlobalVariable.setBack($state);
				if($scope.model == undefined){
					$scope.model = {};
					$scope.get();
				}
			}
		}
	])