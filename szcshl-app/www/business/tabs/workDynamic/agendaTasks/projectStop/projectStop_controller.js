angular.module('projectStop.controller', ['projectStop.service', 'common.service', 'global_variable'])

	.controller('projectStopCtrl', ['$rootScope','$scope', '$ionicPopup', '$state', '$timeout','APP_EVENTS','$ionicHistory','projectStopService','Message',
		function($rootScope,$scope, $ionicPopup, $state, $timeout,APP_EVENTS,$ionicHistory,projectStopService,Message) {

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
				projectStopService.commit($scope.flow,$rootScope.userInfo.loginName).then(function(response){
					
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
				projectStopService.initFlowPageData($scope.id).then(function(response){
					
					$scope.projectStopList=response.data;
					$scope.signDispaWork=$scope.projectStopList.signDispaWork;
					//标题
					$scope.title=$scope.signDispaWork.projectname;
					  //评审天数-剩余工作日
                     if($scope.signDispaWork.reviewstage == '可行性研究报告' || $scope.signDispaWorkreviewstage == '项目概算'){
                           $scope.signDispaWork.countUsedWorkday = 15-$scope.signDispaWork.surplusdays;
                        }else{
                            $scope.signDispaWork.countUsedWorkday = 12-$scope.signDispaWork.surplusdays;
                        }
         		},function(response){
         			console.log('初始化失败');
         		
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});
         		
/*         		//流程信息
         		projectStopService.flowNodeInfo(parameter).then(function(response){
         		     $scope.flow=response.data;
         		},function(response){
         			console.log('初始化失败');
         		
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});*/

			}
		
	   
	
		}
	])

