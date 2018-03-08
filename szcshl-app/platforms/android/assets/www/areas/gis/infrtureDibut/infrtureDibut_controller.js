angular.module('infrtureDibut.controller', ['infrtureDibut.service', 'map.service'])

	.controller('infrtureDibutCtrl', ['$scope', '$rootScope', '$state', 'basicsSvc', 'infrastructureSvc', 'Message',

		function($scope, $rootScope, $state, basicsSvc, infrastructureSvc, Message) {
			//初始化====begin
			$scope.site_data = {
				pageNum: 1,
				pageSize: 30,
				message: '请搜索数据'//列表提示信息
			};
			
			///处理兼容性
			$scope.viewMainClass = "";

			if(localStorage.getItem("isIOS") == "true") {
				$scope.viewMainClass = "iosViewMain";
			}
			
			var map = new BMap.Map('baiduMap_infrtureDibut'); //地图对象
			map.centerAndZoom(new BMap.Point(114.254291, 22.726161), 15);
			map.addControl(new BMap.NavigationControl({
				type: BMAP_NAVIGATION_CONTROL_SMALL
			})); //仅包含平移和缩放按钮
			map.addControl(new BMap.ScaleControl()); //比例尺
			//加载街道数据
			basicsSvc.getStreets('', function(data) {
				$scope.streets = data;
			});
			//初始化==end
			//获取地图放大倍数
			function getZoom(right, left, bottom, top) {
				var zoom = [20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000, 25000, 50000, 100000, 200000, 500000, 1000000, 2000000] //级别18到3。  
				var point_right = new BMap.Point(right.x, right.y);
				var point_left = new BMap.Point(left.x, left.y);
				var point_bottom = new BMap.Point(bottom.x, bottom.y);
				var point_top = new BMap.Point(top.x, top.y);
				var x = map.getDistance(point_right, point_left).toFixed(1); //获取两点距离,保留小数点后两位
				var y = map.getDistance(point_bottom, point_top).toFixed(1); //获取两点距离,保留小数点后两位
				if(x > y) {
					y = x;
				}
				for(var i = 0, zoomLen = zoom.length; i < zoomLen; i++) {
					if(zoom[i] > y) {
						return 19 - i + 2; //之所以会多3，是因为地图范围常常是比例尺距离的10倍以上。所以级别会增加3。  
					}
				};
			}
			//标注添加点击弹窗事件
			function addClickHandler(content, marker) {
				marker.addEventListener("click", function(e) {
					openInfo(content, e)
				});
			}
			//打开弹窗
			function openInfo(content, e) {
				var p = e.target;
				var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
				var infoWindow = new BMap.InfoWindow("地址：" + content.address, {
					width: 250, // 信息窗口宽度
					height: 80, // 信息窗口高度
					title: content.title, // 信息窗口标题
					enableMessage: true //设置允许信息窗发送短息
				}); // 创建信息窗口对象
				map.openInfoWindow(infoWindow, point); //开启信息窗口
			}

			//加载场地数据
			//$scope.siteList = [];
			$scope.loadSite = function(pageNum) {
				$scope.site_data.message = '正在查询数据';
				$scope.siteList = [];
				if(pageNum){
					$scope.site_data.pageNum = pageNum;
				}
				var data = $scope.site_data;
				infrastructureSvc.getSites(data, function(resp) {
					if(resp && resp.data && resp.data.value) {
						$scope.siteList = resp.data.value;
						if($scope.siteList.length == $scope.site_data.pageSize){
							$scope.site_data.message = '';
						}else if($scope.siteList.length == 0){
							$scope.site_data.message = '没有查到相关数据';
						}else{
							$scope.site_data.message = '没有更多数据';
						}
						if(map) {
							map.clearOverlays();
							var top = {
								y: 0
							};
							var bottom = {
								y: 9999
							};
							var left = {
								x: 0
							};
							var right = {
								x: 9999
							};
							var data_info = [];
							for(var i = 0; i < $scope.siteList.length; i++) {
								var site = $scope.siteList[i];
								if(!isNaN(site.siteCoord_X) && !isNaN(site.siteCoord_Y)) {
									var marker = new BMap.Marker(new BMap.Point(site.siteCoord_X, site.siteCoord_Y)); // 创建标注
									var content = {};
									content.address = site.address;
									content.title = site.siteName;
									map.addOverlay(marker); // 将标注添加到地图中
									addClickHandler(content, marker);
									if(site.siteCoord_X < right.x) {
										right.x = site.siteCoord_X;
										right.y = site.siteCoord_Y;
									}
									if(site.siteCoord_X > left.x) {
										left.x = site.siteCoord_X;
										left.y = site.siteCoord_Y;
									}
									if(site.siteCoord_Y > top.y) {
										top.x = site.siteCoord_X;
										top.y = site.siteCoord_Y;
									}
									if(site.siteCoord_Y < bottom.y) {
										bottom.x = site.siteCoord_X;
										bottom.y = site.siteCoord_Y;
									}
								}
							}
							var cenLng = (parseFloat(left.x) + parseFloat(right.x)) / 2;
							var cenLat = (parseFloat(top.y) + parseFloat(bottom.y)) / 2;

							var zoom = getZoom(right, left, bottom, top);
							map.centerAndZoom(new BMap.Point(cenLng, cenLat), zoom);
						}
					}else{
						$scope.siteList = [];
						$scope.site_data.message = '没有查到相关数据';
					}
				});
			};
			//场地列表点击事件
			$scope.siteClick = function(site) {
				if(!isNaN(site.siteCoord_X) && !isNaN(site.siteCoord_Y)) {
					var point = new BMap.Point(site.siteCoord_X, site.siteCoord_Y);
					
					var infoWindow = new BMap.InfoWindow("地址：" + site.address, {
						width: 250, // 信息窗口宽度
						height: 80, // 信息窗口高度
						title: site.siteName, // 信息窗口标题
						enableMessage: true //设置允许信息窗发送短息
					}); // 创建信息窗口对象
					map.openInfoWindow(infoWindow, point); //开启信息窗口
					point.lat += 0.0028;//转移位置偏地图下方显示
					map.centerAndZoom(point, 17);
					return;
				}
				Message.show('该场地未定位');
				map.closeInfoWindow(); //开启信息窗口
			}
			//街道改变
			$scope.site_data.street = '';
			$scope.streetChange = function(street) {
				$scope.communitys = [];
				$scope.site_data.street = '';
				if(street) {
					getCommunitys(street.streetId);
					$scope.site_data.street = street.streetId;
				}
			}
			//获取社区
			$scope.communitys = [];

			function getCommunitys(streetId) {
				var data = {
					pageNum: 1,
					pageSize: 9999,
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
				$scope.site_data.community = '';
				if(community) {
					$scope.site_data.community = community.communityId;
				}
			}
			//查询场地
			$scope.site_data.condition = "";
			$scope.query = function(string) {
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
				$scope.loadSite();
			}
			//刷新
			/*$scope.doRefresh = function(index) {
			}*/
		}
	]);