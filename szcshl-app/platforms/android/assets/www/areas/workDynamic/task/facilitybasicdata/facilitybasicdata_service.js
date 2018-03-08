angular.module('facilitybasicdata.service', ['global','common.service'])

.service('facilitybasicdataService', function($http, $ionicModal, HttpUtil,Message,myCache) {
	var loginName = sessionStorage.getItem("loginName");
	var rootPath = HttpUtil.rootPath;
	var CACHE_ = {};
	var facilitySvcObj = {};
	facilitySvcObj.equipMentChange = function($scope, index){
		$scope.facilitybasicdata.equipMentDto = {};
		if(index && index >= 0){
			var equipMentDto = $scope.equipMents[index];
			$scope.facilitybasicdata.equipMentBrand = equipMentDto.equipMentBrand;
		    $scope.facilitybasicdata.equipMentDto.equipMentId = equipMentDto.equipMentId;
		    $scope.facilitybasicdata.facilityName = equipMentDto.equipMentName;
		    $scope.facilitybasicdata.facilityNumber = equipMentDto.equipMentNumber;
			return;					
		}
	    $scope.facilitybasicdata.facilityName = '';
		$scope.facilitybasicdata.facilityNumber = "";
		$scope.facilitybasicdata.equipMentDto.equipMentId = 1;
	}
	facilitySvcObj.create = function(obj){
		if(obj.street){
			obj.facilitybasicdata.streetDto = {streetId: obj.street.streetId};
		}
		if(obj.community){
			obj.facilitybasicdata.communityDto = {communityId: obj.community.communityId};
		}
		obj.facilitybasicdata.siteInfoDto = {siteId: obj.siteInfo.siteId};
		var url = rootPath + "/mobile/facilityBasicData";
		$http({
			method: 'post',
			url: url, 
			data:{
				facilityBasicDataDtoJson: JSON.stringify(obj.facilitybasicdata),
				loginName: loginName
			}
		}).success(function(response) {
			CACHE_["/mobile/facilityBasicData/get/" + obj.siteInfo.siteId] = undefined;
			facilitySvcObj.get(obj, obj.siteInfo.siteId);
			Message.show("操作成功", function(){
				obj.hide();
			});
		}).error(function(error) {
					
		});
	}
	//获取场地设施
	facilitySvcObj.get = function(obj,site,callback){
				var temp_key = "/mobile/facilityBasicData/get/" + site;
				if(CACHE_[temp_key]){
					obj.facilitybasicdata = CACHE_[temp_key];
					if(typeof callback === 'function'){
						callback(CACHE_[temp_key]);
					}
					return;
				}
				var url = rootPath + "/mobile/facilityBasicData/get?$filter=facilityFileStatus eq 0";
				if(site){
					url += " and siteInfo.siteId eq '" + site + "'";
				}
				$http({
					method: 'get',
					url: url,
				}).success(function(response) {
					obj.facilitybasicdata = response.value;
					CACHE_[temp_key] = obj.facilitybasicdata;
					myCache.put(temp_key, obj.facilitybasicdata); //保存最新数据
					if(typeof callback === 'function'){
						callback(response.value);
					}
				}).error(function(error) {
					obj.facilitybasicdata = myCache.get(temp_key); //读取本地数据
				});
	}
	//添加维修设施
	facilitySvcObj.addFacility = function($scope, siteId, callback){
		$ionicModal.fromTemplateUrl('areas/workDynamic/task/facilitybasicdata/addFacilityModal.html', {
			scope: $scope
		}).then(function(modal) {
			facilitySvcObj.get(modal, siteId);//读取场地设施
			modal.show();
			modal.change = function(x){
				if(x){
					modal.facility = modal.facilitybasicdata[x];
				}else{
					modal.facility = {}
				}
			}
			modal.affirm = function(obj){
				if(typeof callback === 'function'){
					obj.repairItemDto = [];
					callback(obj);
				}
				modal.remove();
			}
			$scope.modal = modal;
		});
	}
	return {
		show: function($scope, siteInfo, community, street){
			$ionicModal.fromTemplateUrl('areas/workDynamic/task/facilitybasicdata/addOrEdit.html', {
					scope: $scope
				}).then(function(modal) {
					$scope.facilityModal = modal;
					HttpUtil.getEquipMent($scope.facilityModal);//加载设备列表
					$scope.facilityModal.title = '';
					$scope.facilityModal.facilitybasicdata = {};
					$scope.facilityModal.siteInfo = siteInfo;
					$scope.facilityModal.community = community;
					$scope.facilityModal.street = street;
					$scope.facilityModal.equipMentChange = function(index){
						facilitySvcObj.equipMentChange($scope.facilityModal, index);
					}
					$scope.facilityModal.submit = function(){
						facilitySvcObj.create($scope.facilityModal)
					};
					$scope.facilityModal.show();
				});
		},
		//获取场地设施
		get: facilitySvcObj.get,
		//新增场地设施
		create: facilitySvcObj.create,
		//添加维修设施
		addFacility: facilitySvcObj.addFacility,
	}
});