angular.module('basics.service', ['global'])
.service('basicsSvc', function($http, GlobalVariable) {
	var rootPath = GlobalVariable.SERVER_PATH;
	var CACHE_ = {};

		function load(temp_key, url, callback){
			$http({
				method: 'get',
				url: url,
			}).success(function(response) {
				callback(response.value);
				CACHE_[temp_key] = response.value;
			}).error(function(error) {
				console.log('获取数据失败，读取本地数据');
			});
		}
		return {
			//获取街道数据
			getStreets: function(id, callback, reset) {
				if(!(typeof callback === 'function')){
					console.log('回调函数未定义');
					return;
				}
				var temp_key = "getStreet" + id;
				if(!reset && CACHE_[temp_key]){
					callback(CACHE_[temp_key]);
					return;
				}
				var url = rootPath + "/mobile/street?$filter=streetState eq 0";
				load(temp_key, url, callback);
			},
			//获取社区数据
			getCommunitys: function(streetId, callback, reset) {
				if(!(typeof callback === 'function')){
					console.log('回调函数未定义');
					return;
				}
				var temp_key = "getCommunity" + streetId;
				if(!reset && CACHE_[temp_key]){
					callback(CACHE_[temp_key]);
					return;
				}
				var url = rootPath + "/mobile/community?$filter=communityState eq 0";
				if(streetId != undefined) {
					url += " and street.streetId eq '" + streetId +"'";
				}
				load(temp_key, url, callback);
			},
		}
	})