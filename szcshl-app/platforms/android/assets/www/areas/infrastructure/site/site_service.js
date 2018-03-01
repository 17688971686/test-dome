angular.module('site.service', ['global','common.service'])

.service('siteSvc', function($http, GlobalVariable, Message, $state) {
	var loginName = sessionStorage.getItem("loginName");
	var rootPath = GlobalVariable.SERVER_PATH;
	var CACHE_ = {};
	var siteInfoSvcObj = {};
	//获取场地信息
	siteInfoSvcObj.get = function(siteId, callback){
		if(!(typeof siteId === 'string')){
			alert('场地ID未定义');
			return;
		}
		var url = rootPath + "/mobile/siteInfo/get";
		$http.get(url+"?$filter=siteId eq '" + siteId + "' and siteState eq 0"
		).success(function (response) {
  			siteInfoSvcObj.siteInfo = response.value[0];
  			if(typeof callback === 'function'){
  				callback(response.value[0]);
  			}
  		}).error(function(error) {
			Message.show("读取数据失败",function(){
			});
		});
	};
	siteInfoSvcObj.getSiteInfos = function(communityId, callback, reset){
		if(!(typeof callback === 'function')){
			console.log('回调函数未定义');
			return;
		}
		var temp_key = "getSiteInfos" + communityId;
		if(!reset && CACHE_[temp_key]){
			callback(CACHE_[temp_key]);
			return;
		}
		var url = rootPath + "/mobile/siteInfo/get?$filter=siteState eq 0";
		if(communityId != undefined) {
			url += " and community.communityId eq '" + communityId +"'";
		}
		$http.get(url
		).success(function (response) {
  			callback(response.value);
  			CACHE_[temp_key] = response.value;
  		}).error(function(error) {
			Message.show("读取数据失败",function(){
			});
		});
	};
	//更新场地信息
	siteInfoSvcObj.updateSite = function(){
		var url = rootPath + "/mobile/siteInfo";
		var data = {
			loginName: loginName,
			jsonText: JSON.stringify(siteInfoSvcObj.siteInfo)
		};
		$http({
			method: 'POST',
			url: url,
			data:data
		}).then(function successCallback(response) {
			Message.show("操作成功");
		}, function errorCallback(response) {
			Message.show("操作失败");
		});
	};
	//设置场地坐标
	siteInfoSvcObj.setPosition = function(x,y){
		siteInfoSvcObj.siteInfo.siteCoord_X = x;
		siteInfoSvcObj.siteInfo.siteCoord_Y = y;
		siteInfoSvcObj.updateSite();
	};
	
	return {
		siteInfoSvcObj: siteInfoSvcObj,
		get: siteInfoSvcObj.get,
		updateSite: siteInfoSvcObj.updateSite,
		getSiteInfos: siteInfoSvcObj.getSiteInfos,
	}
});