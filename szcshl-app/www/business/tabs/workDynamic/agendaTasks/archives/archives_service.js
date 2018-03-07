angular.module('archives.service', ['common.service', 'global_variable'])
	.service('archivesService', function(global,$http) {
		return {
			initFlowPageData: function(id) {
				var url = global.SERVER_PATH + "/api/pendingTask/archives?id="+id;
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