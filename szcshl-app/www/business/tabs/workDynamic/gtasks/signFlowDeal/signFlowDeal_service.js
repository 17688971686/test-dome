angular.module('signFlowDeal.service', ['common.service', 'global_variable'])
	.service('signFlowDealService', function(global,$http) {
		return {
			initFlowPageData: function(signid) {
				var url = global.SERVER_PATH + "/api/workDynamic/initFlowPageData?signid="+signid;
				return $http({
					method: 'GET',
					url: url,
				});
		
			},
			flowNodeInfo: function(parameter) {
				var url = global.SERVER_PATH + "/api/flowApp/flowNodeInfo";
				return $http({
					method: 'POST',
					url: url,
					params: parameter,
					isIgnoreLoading:true
				});
		
			},
			commit: function(flowObj,userName) {
				var url = global.SERVER_PATH + "/api/flowApp/commit";
				return $http({
					method: 'POST',
					url: url,
					data:{
				            flowObj: JSON.stringify(flowObj),
				            userName: userName
			            }
				});
		
			},
			
			
			
		
		
		}

	});