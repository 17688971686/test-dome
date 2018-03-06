angular.module('agendaTasks.controller', ['agendaTasks.service', 'common.service', 'global_variable'])

	.controller('agendaTasksCtrl', ['$rootScope','$scope', '$ionicPopup', '$state', '$timeout','APP_EVENTS','$ionicHistory','agendaTasksService',
		function($rootScope,$scope, $ionicPopup, $state, $timeout,APP_EVENTS,$ionicHistory,agendaTasksService) {

			//返回
			$scope.back = function() {
				$state.go('tab.workDynamic');
			}
			
			//控制不同的任务跳转
			$scope.flowDeal=function(processKey,businessKey,taskId,processInstanceId){
				
				switch (processKey) {
				//图书流程
                case flowcommon.getFlowDefinedKey().BOOKS_BUY_FLOW:
                		$state.go('bookBuy',{id:businessKey,processInstanceId:processInstanceId,taskId:taskId});
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

