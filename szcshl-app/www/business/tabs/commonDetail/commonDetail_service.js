angular.module('commonDetail.service', ['common.service', 'global_variable'])
	.service('commonDetailService', function(global,$http) {
		return {
			myCountInfo: function(id) {
				var url = global.SERVER_PATH + "/api/commonDetail/myCountInfo?id="+id;
				return $http({
					method: 'GET',
					url: url,
				});
		
			},
		
			
		}

	});