angular.module('detali.service', ['common.service', 'global_variable'])
	.service('detaliService', function(global,$http) {
		return {
			detali: function(id) {
				var url = global.SERVER_PATH + "/api/workDynamic/detali?id="+id;
				return $http({
					method: 'GET',
					url: url,
				});
		
			},
		
		
		}

	});