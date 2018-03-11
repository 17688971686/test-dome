angular.module('bookBuy.controller', ['bookBuy.service', 'common.service', 'global_variable'])

	.controller('bookBuyCtrl', ['$rootScope','$scope', '$ionicPopup', '$state', '$timeout','APP_EVENTS','$ionicHistory','bookBuyService','Message',
		function($rootScope,$scope, $ionicPopup, $state, $timeout,APP_EVENTS,$ionicHistory,bookBuyService,Message) {

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
				bookBuyService.commit($scope.flow,$rootScope.userInfo.loginName).then(function(response){
					
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
				bookBuyService.initFlowPageData($scope.id).then(function(response){
					$scope.bookList=response.data;
					//标题
					$scope.title=$scope.bookList.businessName
         		},function(response){
         			console.log('初始化失败');
         		
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});
         		
    /*     		//流程信息
         		bookBuyService.flowNodeInfo(parameter).then(function(response){
         		     $scope.flow=response.data;
         		},function(response){
         			console.log('初始化失败');
         		
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});*/

			}
		
	   
	
		}
	])

