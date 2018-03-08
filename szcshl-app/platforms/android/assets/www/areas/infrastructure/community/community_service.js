angular.module('community.service', ['global','common.service'])

.service('communitySvc', function($http, GlobalVariable, Message, $state) {
	var loginName = sessionStorage.getItem("loginName");
	var rootPath = GlobalVariable.SERVER_PATH + "/mobile";
	function get(communityId, callback) {
		var url = "/community?$filter=communityId eq '"+communityId+"'";
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
				
		})
	};
	//获取场地列表
	function getSites(communityId, callback) {
		var url = "/siteInfo/get?$filter=siteState eq 0 and community.communityId eq '"+communityId + "'";
//		console.log(url)
		$http({
		    method: 'GET',
		    url: rootPath + url
		}).then(function(resp){
			if(typeof callback === 'function'){
				callback(resp.data.value);
				return;
			}
			console.log('回调函数未定义');
		}, function(error){
				
		})
	}
	return{
		//获取设施列表
		get: get,
		getSites: getSites,
	}
});