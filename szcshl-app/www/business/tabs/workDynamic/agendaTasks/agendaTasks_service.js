angular.module('agendaTasks.service', ['common.service', 'global_variable'])
	.service('agendaTasksService', function(global,$http) {
		return {
			agendaTasks: function(id) {
				var url = global.SERVER_PATH + "/api/workDynamic/agendaTasks?id="+id;
				return $http({
					method: 'GET',
					url: url,
				});
		
			},
			    
		
		}

	});