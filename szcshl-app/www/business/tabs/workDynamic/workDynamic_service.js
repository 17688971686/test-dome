angular.module('workDynamic.service', ['common.service', 'global_variable'])
	.service('workDynamicService', function(global,$http) {
		return {
			myCountInfo: function(id) {
				var url = global.SERVER_PATH + "/api/workDynamic/myCountInfo?id="+id;
				return $http({
					method: 'GET',
					url: url,
				});
		
			},
		
			
		}

	});