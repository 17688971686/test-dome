angular.module('agendaTasks.controller', ['agendaTasks.service', 'common.service', 'global_variable'])

	.controller('agendaTasksCtrl', ['$rootScope','$scope', '$ionicPopup', '$state', '$timeout','APP_EVENTS','$ionicHistory','agendaTasksService','workDynamicService',
		function($rootScope,$scope, $ionicPopup, $state, $timeout,APP_EVENTS,$ionicHistory,agendaTasksService,workDynamicService) {

			//返回
			$scope.back = function() {
				workDynamicService.myCountInfo($rootScope.userInfo.id).then(function(response){
         			$rootScope.signCounts = response.data.DO_SIGN_COUNT;//待办项目
         			$rootScope.taskCounts = response.data.DO_TASK_COUNT;//待办任务
         			$state.go('tab.workDynamic');
         		},function(response){
         			console.log('初始化失败');
         			
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});

			}
			
			//控制不同的任务跳转
			$scope.flowDeal=function(processKey,businessKey,taskId,processInstanceId){
				
				switch (processKey) {
				//图书流程
                case flowcommon.getFlowDefinedKey().BOOKS_BUY_FLOW:
                		$state.go('bookBuy',{id:businessKey,processInstanceId:processInstanceId,taskId:taskId,backType:"agendaTasks"});
                    break;
					
				
				//月报流程
                case flowcommon.getFlowDefinedKey().MONTHLY_BULLETIN_FLOW:
                		$state.go('monthly',{id:businessKey,processInstanceId:processInstanceId,taskId:taskId,backType:"agendaTasks"});
                    break;
					
				
				//通知公告流程
                case flowcommon.getFlowDefinedKey().ANNOUNT_MENT_FLOW:
                		$state.go('annount',{id:businessKey,processInstanceId:processInstanceId,taskId:taskId,backType:"agendaTasks"});
                    break;
					
				
				//拟补充资料函流程		
			    case flowcommon.getFlowDefinedKey().FLOW_SUPP_LETTER:
                		$state.go('suppletter',{id:businessKey,processInstanceId:processInstanceId,taskId:taskId,backType:"agendaTasks"});
                    break;
					
				
				//优秀评审报告申报流程		
			    case flowcommon.getFlowDefinedKey().FLOW_APPRAISE_REPORT:
                		$state.go('appraise',{id:businessKey,processInstanceId:processInstanceId,taskId:taskId,backType:"agendaTasks"});
                    break;
					
				
				//项目暂停流程		
			    case flowcommon.getFlowDefinedKey().PROJECT_STOP_FLOW:
                		$state.go('projectStop',{id:businessKey,processInstanceId:processInstanceId,taskId:taskId,backType:"agendaTasks"});
                    break;
					
				
				//课题研究流程		
			    case flowcommon.getFlowDefinedKey().TOPIC_FLOW:
                		$state.go('topic',{id:businessKey,processInstanceId:processInstanceId,taskId:taskId,backType:"agendaTasks"});
                    break;
					
				
				//资产入库流程		
			    case flowcommon.getFlowDefinedKey().ASSERT_STORAGE_FLOW:
                		$state.go('assertStorage',{id:businessKey,processInstanceId:processInstanceId,taskId:taskId,backType:"agendaTasks"});
                    break;
              
				
				//档案借阅流程		
			    case flowcommon.getFlowDefinedKey().FLOW_ARCHIVES:
                		$state.go('archives',{id:businessKey,processInstanceId:processInstanceId,taskId:taskId,backType:"agendaTasks"});
                    break;
                     default:
                    ;
					
				}
				
				
			}
			
			activate();
		
			function activate(){
				agendaTasksService.agendaTasks($rootScope.userInfo.id).then(function(response){
					$scope.agendaTasksList=response.data.value;
   
         		},function(response){
         			console.log('初始化失败');
         		
         		}).finally(function(){
         			console.log('refresh complete event...');
         		});

			}

	
		}
	])

