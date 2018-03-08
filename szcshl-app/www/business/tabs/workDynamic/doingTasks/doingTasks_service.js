angular.module('doingTasks.service', ['common.service', 'global_variable'])
	.service('doingTasksService', function(global,$http) {
		return {
			doingTasks: function(id) {
				var url = global.SERVER_PATH + "/api/workDynamic/doingtasks?id="+id;
				return $http({
					method: 'GET',
					url: url,
				});
		
			},
		
		
		}

	});