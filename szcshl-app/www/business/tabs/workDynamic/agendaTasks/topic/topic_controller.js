angular.module('topic.controller', ['topic.service', 'common.service', 'global_variable'])

	.controller('topicCtrl', ['$rootScope','$scope', '$ionicPopup', '$state', '$timeout','APP_EVENTS','$ionicHistory','topicService','Message',
		function($rootScope,$scope, $ionicPopup, $state, $timeout,APP_EVENTS,$ionicHistory,topicService,Message) {

	    $scope.id = $state.params.id;
		$scope.processInstanceId = $state.params.processInstanceId;
		$scope.taskId = $state.params.taskId;
		var parameter={};
		parameter.processInstanceId=$scope.processInstanceId;
		parameter.taskId=$scope.taskId;
		parameter.userid=$rootScope.userInfo.id;
	
			//返回
			$scope.back = function() {
				$state.go($state.params.backType);
			}
		//流程提交	
	$scope.submitProc=function(){
          	//提交时先进行表单检查
			if ($scope.flow.dealOption) {
				topicService.commit($scope.flow,$rootScope.userInfo.loginName).then(function(response){
					
					if(response.data.flag){
						Message.show(response.data.reMsg,function(){
						$state.go('agendaTasks');		
			        });
					}else{
					    Message.show(response.data.reMsg);
					}
   
         		},function(response){
         			console.log('初始化失败');
         		
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});		
         		
         	}else{
         		  Message.show("请输入处理意见");
         	}
			
			
		}
			activate();
		
			function activate(){
				//详细信息
				topicService.initFlowPageData($scope.id).then(function(response){
					$scope.topicList=response.data;
					$scope.filingDto=$scope.topicList.filingDto;
					$scope.workPlanDto=$scope.topicList.workPlanDto;
					//标题
					$scope.title=$scope.topicList.topicName
         		},function(response){
         			console.log('初始化失败');
         		
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});
         		
  /*       		//流程信息
         		topicService.flowNodeInfo(parameter).then(function(response){
         		     $scope.flow=response.data;
         		},function(response){
         			console.log('初始化失败');
         		
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});*/

			}
		
	   
	
		}
	])

