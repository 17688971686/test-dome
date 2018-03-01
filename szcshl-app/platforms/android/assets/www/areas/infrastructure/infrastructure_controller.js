angular.module('infrastructure.controller', ['infrastructure.service','common.service'])

	.controller('infrastructureCtrl', ['$scope', '$rootScope', '$state', 'infrastructureSvc', 'GlobalVariable', '$window', 'basicsSvc', "$ionicScrollDelegate",

		function($scope, $rootScope, $state, infrastructureSvc, GlobalVariable, $window, basicsSvc, $ionicScrollDelegate) {
			$rootScope.infrastructure__param = {}; //基础设施参数传递
			$scope.infinite_scroll = true;
			$scope.page_index = 1; //默认显示    社区0  场地1  设施2
			
			$scope.viewMainClass = "";

			if(localStorage.getItem("isIOS") == "true") {
				$scope.viewMainClass = "iosViewMain";
			}
			
			
			//刷新
			/*$scope.doRefresh = function(index) {
				$scope.infinite_scroll = false;
				if(index == 1) {} else if(index == 2) {} else {}
			}*/

			/**************************************************************************************
			 * ************************************************************************************
			 ***********************************设施***********************************************
			 * ************************************************************************************
			 * **************************以下是设施列表功能*****************************************/
			$scope.facility_data = {
				pageNum: 1,
				pageSize: 15
			};
			$scope.facility_data.loadMore = true;
			//点击设施
			$scope.facilityClick = function(facility) {
				$rootScope.infrastructure__param.facility = facility; //传递参数
				$state.go('infrastructure/facility', {
					id: facility.facilityId
				});
			}
			//刷新设施
			$scope.doRefreshFacility = function() {
				var data = $scope.facility_data;
				data.pageNum = 1;
				infrastructureSvc.getFacilitys(data, function(resp) {
					$scope.$broadcast('scroll.refreshComplete'); //停止下拉广播
					if(resp && resp.data && resp.data.value) {
						var d = resp.data.value;
						if(d.length == data.pageSize) {
							data.pageNum += 1;
							$scope.facility_data.loadMore = true;
						} else {
							$scope.facility_data.loadMore = false;
						}
						$scope.facilityList = d;
					}
				});
			};
			//加载设施
			$scope.facilityList = [];
			$scope.loadFacility = function(flg) {
				if(flg){
					$scope.$broadcast('scroll.infiniteScrollComplete'); //停止上拉广播
					return;
				}
				var data = $scope.facility_data;
				//				console.log(data)
				infrastructureSvc.getFacilitys(data, function(resp) {
					$scope.$broadcast('scroll.infiniteScrollComplete'); //停止上拉广播
					if(resp && resp.data && resp.data.value) {
						var d = resp.data.value;
						if(d.length == data.pageSize) {
							data.pageNum += 1;
							$scope.facility_data.loadMore = true;
						} else {
							$scope.facility_data.loadMore = false;
						}
						$scope.facilityList = $scope.facilityList.concat(d);
					}
				});
			};
			//获取设施列表社区条件
			$scope.faCommunitys = [];
			function getFaCommunitys(streetId) {
				var data = {
					pageNum: 1,
					pageSize: 999999999,
					condition: " and street.streetId eq '" + streetId + "'"
				}
				infrastructureSvc.getCommunitys(data, function(resp) {
					if(resp && resp.data && resp.data.value) {
						$scope.faCommunitys = resp.data.value;
					}
				})
			}
			//获取设施列表场地条件
			$scope.faSites = [];
			$scope.facility_data.site = '';
			function getFaSites(communityId) {
				var data = {
					pageNum: 1,
					pageSize: 999999999,
					condition: " and community.communityId eq '" + communityId + "'"
				}
				infrastructureSvc.getSites(data, function(resp) {
					if(resp && resp.data && resp.data.value) {
						$scope.faSites = resp.data.value;
					}
				})
			}
			//设施列表街道条件改变
			$scope.facility_data.street = '';
			$scope.faStreetChange = function(street) {
				$scope.faCommunitys = [];
				$scope.facility_data.community = '';
				if(street) {
					getFaCommunitys(street.streetId);
				}
			}
			//设施列表社区条件改变
			$scope.facility_data.community = '';
			$scope.faCommunityChange = function(community) {
				$scope.faSites = [];
				$scope.facility_data.site = '';
				if(community) {
					getFaSites(community.communityId);
				}
			}
			//查询设施
			$scope.facility_data.condition = "";
			$scope.queryFacility = function(string) {
				$scope.facility_data.condition = ''; //清空条件
				var site = $scope.facility_data.site;
				if(site) { //筛选场地
					$scope.facility_data.condition += " and siteInfo.siteId eq '" + site + "'";
				}
				var community = $scope.facility_data.community;
				if(!site && community) { //筛选社区
					$scope.facility_data.condition += " and siteInfo.community.communityId eq '" + community + "'";
				}
				var street = $scope.facility_data.street;
				if(!site && !community && street) { //筛选场地
					$scope.facility_data.condition += " and siteInfo.community.street.streetId eq '" + street + "'";
				}
				if(string) { //模糊查询
					$scope.facility_data.condition += string;
				}
				$scope.facility_data.pageNum = 1;
				$scope.facilityList = [];
				$scope.loadFacility();
			}
			//模糊查询
			$scope.dimFacility = function() {
				var string = '';
				var data = $scope.facility_data
				if(data.search) {
					string += " and facilityName like '" + data.search + "'";
					//					string += " and facilityNumber like '"+data.search+"'";
					//					string += " and principal like '"+data.search+"'";
				}
				$scope.queryFacility(string);
			}
			//精确查询设施
			$scope.preciseFacility = function() {
				var string = '';
				var data = $scope.facility_data;
				if(data.facilityState) { //使用状态
					string += " and facilityState like '" + data.facilityState + "'";
				}
				if(data.name) { //设施名称
					string += " and facilityName like '" + data.name + "'";
				}
				if(data.number) { //设施编号
					string += " and facilityNumber like '" + data.number + "'";
				}
				if(data.principal) { //设施负责人
					string += " and principal like '" + data.principal + "'";
				}
				$scope.queryFacility(string);
			}
			/****************************以上是设施列表功能*****************************************
			 * ************************************************************************************
			 ***********************************场地***********************************************
			 * ************************************************************************************
			 * **************************以下是场地列表功能*****************************************/

			$scope.site_data = {
				pageNum: 1,
				pageSize: 15
			};
			$scope.site_data.loadMore = true;
			//点击选择场地
			$scope.siteClick = function(site) {
				$rootScope.infrastructure__param.site = site; //传递参数
				$state.go('infrastructure/site', {
					id: site.siteId
				});
			}
			//刷新场地
			$scope.doRefreshSite = function() {
				var data = $scope.site_data;
				data.pageNum = 1;
				infrastructureSvc.getSites(data, function(resp) {
					$scope.$broadcast('scroll.refreshComplete'); //停止下拉广播
					if(resp && resp.data && resp.data.value) {
						var d = resp.data.value;
						if(d.length == data.pageSize) {
							data.pageNum += 1;
							$scope.site_data.loadMore = true;
						} else {
							$scope.site_data.loadMore = false;
						}
						$scope.siteList = d;
					}
				});
			};
			//加载场地数据
			$scope.siteList = [];
			$scope.loadSite = function(flg) {
				if(flg){
					$scope.$broadcast('scroll.infiniteScrollComplete'); //停止上拉广播
					return;
				}
				var data = $scope.site_data;
				//				console.log(data)
				infrastructureSvc.getSites(data, function(resp) {
					$scope.$broadcast('scroll.infiniteScrollComplete'); //停止上拉广播
					if(resp && resp.data && resp.data.value) {
						var d = resp.data.value;
						if(d.length == data.pageSize) {
							data.pageNum += 1;
							$scope.site_data.loadMore = true;
						} else {
							$scope.site_data.loadMore = false;
						}
						$scope.siteList = $scope.siteList.concat(d);
					}
				});
			};
			//街道改变
			$scope.site_data.street = '';
			$scope.streetChange = function(street) {
				$scope.communitys = [];
				$scope.site_data.community = '';
				if(street) {
					getCommunitys(street.streetId);
				}
			}
			//获取社区
			$scope.communitys = [];

			function getCommunitys(streetId) {
				var data = {
					pageNum: 1,
					pageSize: 999999999,
					condition: " and street.streetId eq '" + streetId + "'"
				}
				infrastructureSvc.getCommunitys(data, function(resp) {
					if(resp && resp.data && resp.data.value) {
						$scope.communitys = resp.data.value;
					}
				})
			}
			//社区改变
			$scope.site_data.community = '';
			$scope.communityChange = function(community) {

			}
			//查询场地
			$scope.site_data.condition = "";
			$scope.querySite = function(string) {
				$scope.site_data.condition = '';
				var community = $scope.site_data.community;
				if(community) { //筛选社区
					$scope.site_data.condition += " and community.communityId eq '" + community + "'";
				}
				var street = $scope.site_data.street;
				if(!community && street) { //筛选场地
					$scope.site_data.condition += " and community.street.streetId eq '" + street + "'";
				}
				if(string) { //模糊查询
					$scope.site_data.condition += " and siteName like '" + string + "'";
				}
				$scope.site_data.pageNum = 1;
				$scope.siteList = [];
				$scope.loadSite();
			}

			/****************************以上是设场地表功能*****************************************
			 * ************************************************************************************
			 ***********************************社区***********************************************
			 * ************************************************************************************
			 * **************************以下是社区列表功能*****************************************/
			$scope.community_data = {
				pageNum: 1,
				pageSize: 15
			};
			$scope.community_data.loadMore = true;
			//社区点击事件
			$scope.communityClick = function(community) {
				$rootScope.infrastructure__param.community = community; //传递参数
				$state.go('infrastructure/community', {
					id: community.communityId
				});
			}
			//刷新社区
			$scope.doRefreshCommunity = function() {
				var data = $scope.community_data;
				data.pageNum = 1;
				infrastructureSvc.getCommunitys(data, function(resp) {
					$scope.$broadcast('scroll.refreshComplete'); //停止下拉广播
					if(resp && resp.data && resp.data.value) {
						var d = resp.data.value;
						if(d.length == data.pageSize) {
							data.pageNum += 1;
							data.loadMore = true;
						} else {
							data.loadMore = false;
						}
						$scope.communityList = d;
					}
				});
			};
			//加载社区
			$scope.communityList = [];
			$scope.loadCommunity = function(flg) {
				if(flg){
					$scope.$broadcast('scroll.infiniteScrollComplete'); //停止上拉广播
					return;
				}
				var data = $scope.community_data;
				//				console.log(data)
				infrastructureSvc.getCommunitys(data, function(resp) {
					$scope.$broadcast('scroll.infiniteScrollComplete'); //停止上拉广播
					if(resp && resp.data && resp.data.value) {
						var d = resp.data.value;
						if(d.length == data.pageSize) {
							data.pageNum += 1;
							data.loadMore = true;
						} else {
							data.loadMore = false;
						}
						$scope.communityList = $scope.communityList.concat(d);
					}
				});
			};
			//街道改变
			$scope.community_data.street = '';
			//查询场地
			$scope.community_data.condition = "";
			$scope.queryCommunity = function(string) {
				$scope.community_data.condition = '';
				var street = $scope.community_data.street;
				if(street) { //筛选场地
					$scope.community_data.condition += " and street.streetId eq '" + street + "'";
				}
				if(string) { //模糊查询
					$scope.community_data.condition += " and communityName like '" + string + "'";
				}
				$scope.community_data.pageNum = 1;
				$scope.communityList = [];
				$scope.loadCommunity();
			}
			/****************************以上是社区列表功能*****************************************
			 * ************************************************************************************
			 ***********************************初始化*********************************************
			 * ************************************************************************************
			 * **************************以下是初始化功能*******************************************/
			init();

			function init() {
				$scope.loadFacility();//加载设施数据
				$scope.loadCommunity();//加载社区数据
				//加载街道数据
				basicsSvc.getStreets('', function(data) {
					$scope.streets = data;
				});
			}
		}
	]);