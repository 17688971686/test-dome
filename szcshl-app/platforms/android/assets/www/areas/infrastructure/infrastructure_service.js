angular.module('infrastructure.service', [])

.service('infrastructureSvc', function($http, GlobalVariable, myCache) {
		var loginName = localStorage.getItem("loginName");
		var rootPath = GlobalVariable.SERVER_PATH + "/mobile";
		var url;
		
		function load(data,callback,error){
			if(!(typeof callback === 'function')){
				console.log("未定义回调函数")
				return;
			}
			$http({
			    method: 'GET',
			    data:data,
			    url: rootPath + url
			}).then(callback, error
			);
		}
		return {
			//获取场地列表
			getSites: function(data,callback) {
				url = "/siteInfo/get?$top="+data.pageSize+"&$skip="+((data.pageNum - 1) * data.pageSize)+"&$filter=siteState eq 0"+data.condition;
//				console.log(url)
				load(data,callback,function(response){
//					$scope.$broadcast('scroll.infiniteScrollComplete');//停止上拉广播
//					$scope.$broadcast('scroll.refreshComplete'); //停止下拉广播
					console.log("获取数据失败");
				});
			},
			//获取社区列表
			getCommunitys: function(data,callback) {
				url = "/community?$top="+data.pageSize+"&$skip="+((data.pageNum - 1) * data.pageSize)+"&$filter=communityState eq 0"+data.condition;
//				console.log(url)
				load(data,callback,function(response){
//					$scope.$broadcast('scroll.infiniteScrollComplete');//停止上拉广播
//					$scope.$broadcast('scroll.refreshComplete'); //停止下拉广播
					console.log("获取数据失败");
				});
			},
			//获取设施列表
			getFacilitys: function(data,callback) {
				url = "/facilityBasicData/get?$top="+data.pageSize+"&$skip="+((data.pageNum - 1) * data.pageSize)+"&$filter=facilityFileStatus eq 0"+data.condition;
//				console.log(url)
				load(data,callback,function(response){
//					$scope.$broadcast('scroll.infiniteScrollComplete');//停止上拉广播
//					$scope.$broadcast('scroll.refreshComplete'); //停止下拉广播
					console.log("获取数据失败");
				});
			},
		}

});