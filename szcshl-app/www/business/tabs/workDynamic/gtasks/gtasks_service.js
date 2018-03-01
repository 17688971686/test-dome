angular.module('gtasks.service', ['common.service', 'global_variable'])
	.service('gtasksService', function(global,$http) {
		return {
			gtasks: function(id) {
				var url = global.SERVER_PATH + "/api/workDynamic/gtasks?id="+id;
				return $http({
					method: 'GET',
					url: url,
				});
		
			},
		
		
		}

	});