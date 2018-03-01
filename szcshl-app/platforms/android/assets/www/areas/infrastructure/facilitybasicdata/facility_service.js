angular.module('facility.service', ['global','common.service'])

.service('facilitySvc', function($http, GlobalVariable, Message, $state) {
	var loginName = sessionStorage.getItem("loginName");
	var rootPath = GlobalVariable.SERVER_PATH + "/mobile";
	function get(facilityId, callback) {
		var url = "/facilityBasicData/get?$filter=facilityId eq '"+facilityId+"'";
//		console.log(url)
		$http({
		    method: 'GET',
		    url: rootPath + url
		}).then(function(resp){
			if(typeof callback === 'function'){
				callback(resp.data.value[0]);
				return;
			}
			console.log('回调函数未定义');
		}, function(error){
				
		}).finally(function() {
			// 停止广播infinite-scroll
			$scope.$broadcast('scroll.infiniteScrollComplete');
		});
	}
	return{
		//获取设施列表
		get: get,
	}
});