angular.module('dtasks.service', ['common.service', 'global_variable'])
	.service('dtasksService', function(global,$http) {
		return {
			dtasks: function(id) {
				var url = global.SERVER_PATH + "/api/workDynamic/doingtasks?id="+id;
				return $http({
					method: 'GET',
					url: url,
				});
		
			},
		
		
		}

	});