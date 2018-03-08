/***
 * angularjs 使用指令封装百度地图
 * Created by xc on 2015/6/13.
 */
(function (window, angular) {
  'use strict';
  var angularMapModule = angular.module('angularBMap', []);
  angularMapModule.provider('angularBMap', mapService);//定位服务
  angularMapModule.directive('baiduMap', mapDirective);//定位指令
  /*
   * 定位相关服务
   */
  function mapService() {
    //基础配置
    this.default_position = new BMap.Point(114.26084528571428, 22.729227714285717);//地图默认中心点
    /**
     * 设置地图默认中心点
     * @param lng
     * @param lat
     * @returns {mapService}
     */
    this.setDefaultPosition = function (lng, lat) {
      this.default_position = new BMap.Point(lng, lat);
      return this;
    };

    //返回的服务
    this.$get = BMapService;
    BMapService.$inject = ['$q', '$timeout'];
    function BMapService($q, $timeout) {
      var map,//全局可用的map对象
        default_position = this.default_position,//默认中心点
        globalMarker;//标注点
      return {
        initMap: initMap,//初始化地图
        getMap: getMap,//返回当前地图对象
        geoLocation: geoLocation,//获取当前位置
        geoLocationAndCenter: geoLocationAndCenter,//获取当前位置，并将地图移动到当前位置
        drawMarkers: drawMarkers,//添加兴趣点
        drawMarkersAndSetOnclick: drawMarkersAndSetOnclick,//添加兴趣点同时添加点击事件
        getCurrentMakers:getCurrentMakers,//获取当前标注点
        setCenterPosition:setCenterPosition//设置中心点
      };
      /**
       * 获取map对象
       * @alias getMap
       */
      function getMap() {
        if (!map) {
          map = new BMap.Map('bMap');//地图对象
        }
        return map;
      }

       /**
       * 设置地图中心点
       * @alias setCenterPosition
       */
      function setCenterPosition(point){
         if(!point){
            default_position = point;
         }
         var defer = $q.defer();
         var map = getMap();
         map.centerAndZoom(default_position, 15);  // 初始化地图,设置中心点坐标和地图级别
         map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
         map.enableScrollWheelZoom(true);
         defer.resolve();
         return defer.promise;
      }
      /**
       * 初始化地图
       * @constructor
       */
      function initMap() {
        var defer = $q.defer();
        $timeout(function () {
          getMap().centerAndZoom(default_position, 14);
          defer.resolve();
        });
        return defer.promise;
      }

      /**
       * 调用百度地图获取当前位置
       * @constructor
       */
      function geoLocation() {
        var defer = $q.defer(), location = new BMap.Geolocation();//百度地图定位实例
        location.getCurrentPosition(function (result) {
          if (this.getStatus() === BMAP_STATUS_SUCCESS) {
            //定位成功,返回定位地点和精度
            defer.resolve(result);
          } else {
            defer.reject('不能获取位置');
          }
        }, function (err) {
          defer.reject('定位失败');
        });
        return defer.promise;
      }

      /**
       * 获取当前位置，并将地图移动到当前位置
       * @constructor
       */
      function geoLocationAndCenter() {
        var defer = $q.defer();
        geoLocation().then(function (result) {

            var map = getMap();
            map.centerAndZoom(result.point, 15);  // 初始化地图,设置中心点坐标和地图级别
            map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
            map.setCurrentCity(result.address.city);          // 设置地图显示的城市 此项是必须设置的
            map.enableScrollWheelZoom(true);


            var marker = new BMap.Marker(result.point);  // 创建标注
            map.addOverlay(marker);               // 将标注添加到地图中
            marker.enableDragging();
            globalMarker = marker;

            defer.resolve(result);
        }, function (err) {
          //定位失败
          getMap().panTo(default_position);
          var marker = new BMap.Marker(default_position);
          getMap().addOverlay(marker);
          defer.reject('定位失败');
        });
        return defer.promise;
      }

      /**
      *
      *
      */
      function getCurrentMakers(){
          var _this  = this;
          return globalMarker;
      }
      /**
       * 向地图添加兴趣点（marker）
       * @param markers
       */
      function drawMarkers(markers) {
        var _markers = [],//待添加的兴趣点列表
          defer = $q.defer(),
          point,//当前添加的坐标点
          _length,//数组长度
          _progress;//当前正在添加的点的索引
        $timeout(function () {
          //判断是否含有定位点
          if (!markers) {
            defer.reject('没有传入兴趣点');
            return;
          }
          //传入了参数
          if (!angular.isArray(markers)) {
            //传入的不是array
            if (markers.loc) {
              _markers.push(markers);
            } else {
              defer.reject('获取不到loc对象信息');
            }
          } else {
            if (markers[0].loc) {
              _markers = markers;
            } else {
              defer.reject('获取不到loc对象信息');
            }
          }
          _length = _markers.length - 1;
          angular.forEach(_markers, function (obj, index) {
            _progress = index;
            if (angular.isObject(obj.loc)) {
              point = new BMap.Point(obj.loc.lng, obj.loc.lat);
            } else if (angular.isString(obj.loc)) {
              point = new BMap.Point(obj.loc.split(',')[0], obj.loc.split(',')[1]);
            } else {
              _progress = '第' + index + '个兴趣点loc对象不存在或格式错误，只支持object和string';
            }
            var marker = new BMap.Marker(point);
            getMap().addOverlay(marker);
            defer.notify(_progress);
            if (index === _length) {
              defer.resolve();
            }
          });
        });
        return defer.promise;
      }

      /**
       * 默认点击事件
       * @param obj
       */
      function markerClick() {
          getMap().panTo(this.point);
          alert(1);
      }

      /**
       * 向地图添加兴趣点同时添加点击事件
       * @param markers
       * @param onClick
       * @returns {*}
       */
      function drawMarkersAndSetOnclick(markers, onClick) {
        var _markers = [],//待添加的兴趣点列表
          defer = $q.defer(),
          point,//当前添加的坐标点
          _length,//数组长度
          _progress,//当前正在添加的点的索引
          _onClick;//点击事件函数
        if (onClick) {
          _onClick = onClick;
        } else {
          _onClick = markerClick;
        }
        $timeout(function () {
          //判断是否含有定位点
          if (!markers) {
            defer.reject('没有传入兴趣点');
            return;
          }
          //传入了参数
          if (!angular.isArray(markers)) {
            //传入的不是array
            if (markers.loc) {
              _markers.push(markers);
            } else {
              defer.reject('获取不到loc对象信息');
            }
          } else {
            if (markers[0].loc) {
              _markers = markers;
            } else {
              defer.reject('获取不到loc对象信息');
            }
          }
          _length = _markers.length - 1;
          angular.forEach(_markers, function (obj, index) {
            _progress = index;
            if (angular.isObject(obj.loc)) {
              point = new BMap.Point(obj.loc.lng, obj.loc.lat);
            } else if (angular.isString(obj.loc)) {
              point = new BMap.Point(obj.loc.split(',')[0], obj.loc.split(',')[1]);
            } else {
              _progress = '第' + index + '个兴趣点loc对象不存在或格式错误，只支持object和string';
            }
            var marker = new BMap.Marker(point);
            marker.obj = obj;
            marker.addEventListener('click', _onClick);
            getMap().addOverlay(marker);
            defer.notify(_progress);
            if (index === _length) {
              defer.resolve();
            }
          });
        });
        return defer.promise;
      }
    }
  }

  /***
   * 地图指令
   */
  mapDirective.$inject = ['angularBMap'];
  function mapDirective(angularBMap) {
    return {
      restrict: 'EAC',
      replace: true,
      scope: true,
      template: '<div id="bMap" style="width: 100%;height:100%"></div>',
      link: mapLink,
      controller: mapController
    };

    function mapLink($scope, element, attr, ctrl) {
      var options = $scope.$eval(attr.baiduMap);
      var bMap = ctrl.map;
      angular.extend(options,bMap);
      $scope.options = options;
      bMap.setCenterPosition(options.centerPoint).then(function(){
        if(options.onMapShow&&typeof options.onMapShow === 'function'){
            options.onMapShow();
        }
      });
    }

    function mapController() {
      this.geoLocation = angularBMap.geoLocation;//定位
      this.initMap = angularBMap.initMap;//初始化
      this.geoLocationAndCenter = angularBMap.geoLocationAndCenter;//获取当前定位并移动到地图中心
      this.drawMarkers = angularBMap.drawMarkers;//添加兴趣点
      this.drawMarkersAndSetOnclick = angularBMap.drawMarkersAndSetOnclick;//添加兴趣点同时添加点击事件
      this.map = angularBMap;
    }
  }
})(window, window.angular);