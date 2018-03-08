angular.module('map.service', []).service('mapSvc', function($ionicModal, $interval, $timeout) {
		var mapSvcObj = {};//地图服务对象
		mapSvcObj.position = {};//业务位置数据
		mapSvcObj.setCurrentPosition = function(){ setCurrentPosition(); }
		mapSvcObj.defaultPoint = new BMap.Point(114.254291, 22.726161);//默认中心点
		var icon = new BMap.Icon("img/pin.png", new BMap.Size(20, 41), { anchor: new BMap.Size(10, 41) }); //icon_url为自己的图片路径
		//添加标注
		function addMarker(longitude, latitude){
			if( !isNaN(longitude) && !isNaN(latitude)){
				var new_point = new BMap.Point(longitude, latitude);//创建坐标对象
				var marker = new BMap.Marker(new_point,{icon: icon});  // 创建标注
				mapSvcObj.map.addOverlay(marker); // 将标注添加到地图中
			}
		}
		//清空标注
		function clearMarker(){
			mapSvcObj.map.clearOverlays(); //清理标注
		}
		//设置显示中心
		function setCentre(longitude, latitude){
			if( !isNaN(longitude) && !isNaN(latitude)){
				var new_point = new BMap.Point(longitude, latitude);//创建坐标对象
				$timeout(function(){
				    mapSvcObj.map.panTo(new_point);   //切换显示到坐标位置  
				},500);   //该函数延迟执行
			}
		}
		//设置地图位置
		function setPosition(longitude, latitude){
			mapSvcObj.longitude = longitude;
			mapSvcObj.latitude = latitude;
			mapSvcObj.map.clearOverlays(); //清理标注
			var new_point = new BMap.Point(mapSvcObj.longitude, mapSvcObj.latitude);//创建坐标对象
			$timeout(function(){
			     mapSvcObj.map.panTo(new_point);   //切换显示到坐标位置  
			},500);   //该函数延迟执行
			var marker = new BMap.Marker(new_point,{icon: icon});  // 创建标注
			mapSvcObj.map.addOverlay(marker); // 将标注添加到地图中
			var geoc = new BMap.Geocoder();//具体地址信息
		    geoc.getLocation(new_point, function(rs){
		        mapSvcObj.addrmsg=rs.address;//详细地址string
		       /* var infoWindow = new BMap.InfoWindow("地址:"+mapSvcObj.addrmsg, { width : 200,height: 40});  //创建信息窗口对象 
		        mapSvcObj.map.openInfoWindow(infoWindow,new_point); //开启信息窗口*/
		    });
//		    mapSvcObj.map.setCurrentCity('');//显示城市
		};
		//定位到当前位置
		function setCurrentPosition(){
			getCurrentPosition(function(result){
				setPosition(result.longitude, result.latitude);
			});
		};
		//获取当前位置数据
		function getCurrentPosition(callBack){
			if(callBack && typeof callBack === 'function'){
				if(ionic.Platform.platform()=='win32'){
					$timeout(function(){
						callBack({  longitude: (108.288342+parseInt(Math.random()*10)), latitude: (22.864816+parseInt(Math.random()*10))  });//电脑测试代码
					},1000);   //模拟定位延迟
					return;
				}
				if(baidumap_location){//只能在安卓系统下跑，浏览器模拟会报错
					baidumap_location.getCurrentPosition(callBack, function (error) {
						alert('获取位置失败');
						console.log(error);
					});
				}else{
					console.log('百度地图定位插件未定义');
				}
			}
		};
		//初始化地图
		function init (id){
			// 百度地图API功能
			mapSvcObj.map = new BMap.Map(id || 'baiduMap');//地图对象
			mapSvcObj.map.centerAndZoom(mapSvcObj.defaultPoint, 15);
			if(mapSvcObj.position.longitude && mapSvcObj.position.latitude){
				setPosition(mapSvcObj.position.longitude, mapSvcObj.position.latitude);//定位到业务数据坐标
			}else{
				setCurrentPosition();//定位当前位置
			}
//			mapSvcObj.map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
			mapSvcObj.map.addControl( new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL})); //右上角，仅包含平移和缩放按钮
			mapSvcObj.map.addControl(new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT}));//比例尺
		};
		return {
			//设置业务数据位置
			setBusinessPosition: function(longitude, latitude){
				mapSvcObj.position.longitude = longitude;
				mapSvcObj.position.latitude = latitude;
			},
			mapSvcObj: mapSvcObj,
			//初始化地图
			init: init,
			//设置位置和标志
			setPosition: setPosition,
			//添加标注
			addMarker: addMarker,
			//清理标注
			clearMarker: clearMarker,
			//设置显示中心
			setCentre: setCentre,
			//定位到当前位置
			setCurrentPosition: setCurrentPosition,
			//获取当前位置数据
			getCurrentPosition: getCurrentPosition,
			//获取map对象
			getMap: function(){ return mapSvcObj.map; },
			//设置map对象
			setMap: function(mapObj){
				if(typeof mapObj === 'string'){
					mapSvcObj.map = new BMap.Map(mapObj);//地图对象
					return ;
				}
				mapSvcObj.map = mapObj; 
			},
			//设置默认中心点
			setDefaultPoint: function(longitude, latitude){
				if(latitude && longitude){
					mapSvcObj.defaultPoint = new BMap.Point(longitude, latitude);//坐标方式
				}else if(longitude && typeof longitude === 'object' && longitude.lng && longitude.lat){
						mapSvcObj.defaultPoint = longitude;//对象方式
				}
			},
			//弹窗显示地图
			showMap: function($scope,templateUrl) {
				if(!(templateUrl && typeof templateUrl === 'string')){
					templateUrl = 'areas/workDynamic/Map/showMap.html';//默认地图页
				}
				$ionicModal.fromTemplateUrl(templateUrl, {
					scope: $scope
				}).then(function(modal) {
					modal.show();
					$scope.modal = modal;
					init();
					mapSvcObj.remove = function(){ modal.remove(); }
					$scope.mapSvcObj = mapSvcObj;
					var timer = $interval(function(){//启动定时器
					    if(modal && !modal.isShown()){
					    	modal.remove();
					    	$interval.cancel(timer);//取消定时器
					    }
					},1000);
				});
			},
		}
	})