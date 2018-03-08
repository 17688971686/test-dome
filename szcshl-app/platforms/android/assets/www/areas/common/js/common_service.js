angular.module('common.service', [])
	//缓存
	.service('myCache', function($cacheFactory) {
		//缓存对象--容器
		var modelCache = $cacheFactory('modelCache');
		//用户名
		var loginName = localStorage.getItem("loginName");
		//保存
		var clearModelKey = "CLEAR_MODEL_KEY";
		//获取key
		function getModelKey(key){
			return loginName+"_"+key;
		}
		//保存数据到缓存
		function put(key, value){
			key = getModelKey(key);//获取key
//			console.log("缓存数据 key："+key);
			var clearKey = JSON.parse(localStorage.getItem(clearModelKey));//获取本地记录的key值数组
			if(clearKey == undefined ){ clearKey = []; }//创建缓存key数组
			if(clearKey.indexOf(key) == -1){ clearKey.push(key); }//判断key不存在则保存key
			localStorage.setItem(clearModelKey, JSON.stringify(clearKey));//保存key值数组到本地
			modelCache.put(key,value);//数据放入缓存
			localStorage.setItem(key,JSON.stringify(value));//保存数据到本地	
		}
		//获取缓存数据
		function get(key){
			key = getModelKey(key);//获取key
			console.log("读取缓存数据 key："+key);
			var data = modelCache.get(key);
			if(data){ return data; }
			data = JSON.parse(localStorage.getItem(key));//缓存中没有数据则读取本地
			if(data){ modelCache.put(key, data); };//数据放入缓存
			return data;//返回数据
		}
		//移除缓存
		function remove(key){
			var data = get(key);
			if(data){
				key = getModelKey(key);//获取key
				modelCache.remove(key);//移除缓存
				var clearKey = JSON.parse(localStorage.getItem(clearModelKey));//获取本地记录的key值数组
				if(clearKey == undefined || clearKey.indexOf(key) == -1){ return data; };
				clearKey.slice(clearKey.indexOf(key),1);//移除key
				localStorage.setItem(clearModelKey, JSON.stringify(clearKey));//保存key值数组到本地
				localStorage.removeItem(key);//移除本地数据
				return data;
			}
		}
		//移除所有缓存
		function removeAll(){
			modelCache.removeAll();//移除缓存
			var clearKey = JSON.parse(localStorage.getItem(clearModelKey));//获取需要清理的缓存key
			if( clearKey && clearKey.length){
				for(var i =0 ; i < clearKey.length; i++){
					localStorage.removeItem(clearKey[i]);//清理缓存
				}
			}
			localStorage.removeItem(clearModelKey);//清理key
		}
		
		return {
			put: put,
			get: get,
			remove: remove,
			removeAll: removeAll,
			
		}
	})

//弹窗
.service('Message', function($ionicPopup, $ionicModal,$ionicBackdrop,$timeout,GlobalVariable) {
		var template = '<ion-popover-view><ion-content> Hello! </ion-content></ion-popover-view>';
		var await;
		function show(msg,callback) {
			$ionicPopup.alert({
				title: '提示',
				template: "<center>"+msg+"</center>",
               	okText: '确定'
			}).then(function(res) {
		           if(typeof callback === 'function'){
		        	if(res) {callback();} 
		        }
            });
		}
		function closeProcessing(){
			await.close();
			await=undefined;
//			$ionicBackdrop.release();//取消遮罩
		}
		return {
			show: show,
			confirm: function(msg,okFun,canFun) {
	            $ionicPopup.confirm({
		            title: '请确认您的操作',
		            template: msg,
		            cancelText: '取消',
		            okText: '确定'
	            }).then(function(res) {
	             	if(res) {
	             		if(typeof okFun === 'function'){okFun();}
		            } else {
		               	if(typeof canFun === 'function'){canFun();}
		            }
	            });
           	},
           	showModal: function($scope, templateUrl, showFun) {
				if(!(templateUrl && typeof templateUrl === 'string')){
					console.log('未设置弹窗模板');
				}
				$ionicModal.fromTemplateUrl(templateUrl, {
					scope: $scope
				}).then(function(modal) {
					$scope.modal = modal;
					modal.show();
					if(typeof showFun === 'function'){
						showFun();
					}
				});
			},
		}
	})

	//http post json
	.service('HttpUtil', function($http, $q, GlobalVariable,myCache) {
		var rootPath = GlobalVariable.SERVER_PATH;//根路径
		var timeout = GlobalVariable.DefaultTimeOut;//请求超时
		var CACHE_ = {};//缓存数据
		//Http失败处理
		var defaultErrFn = function(status) {
			//TODO:Http请求发生失败的处理
			switch(status) {
				case -1:
					console.log('服务器无响应');
					break;
				case 401:
					console.log('没权限');
					break;
			}
		}
		return {
			//根路径
			rootPath: rootPath,
			timeout: timeout,
			//
			ajax: function(paramater) {
				$http({
					method: paramater.method,
					url: paramater.url,
					params: paramater.data,
				}).success(paramater.success).error(paramater.error || defaultErrFn);
			},
			//获取流程处理记录
			getProccessRecords: function(processInstanceId, $scope) {
				var url = rootPath + '/workflow/proccessInstance/hisTask/' + processInstanceId;
				$http({
					method: 'get',
					url: url,
				}).success(function(response) {
					$scope.proccessRecords = response;
					$scope.$broadcast('scroll.refreshComplete'); //停止刷新广播
				}).error(defaultErrFn);
			},
			//获取流程图
			getProccessImage: function(processInstanceId, httpSuccess) {
				var url = rootPath + '/workflow/proccessInstance/img/' + processInstanceId;
				doHttp('get', url).then(httpSuccess, defaultErrFn);
			},
			//获取文体设施
			getEquipMent: function(obj) {
				var temp_key = "/mobile/equipMent/get";
				if(CACHE_[temp_key]) {
					obj.equipMents = CACHE_[temp_key];
					return;
				}
				var url = rootPath + "/mobile/equipMent?$filter=equipMentState eq 0";
				$http({
					method: 'get',
					url: url,
				}).success(function(response) {
					obj.equipMents = response.value;
					CACHE_[temp_key] = obj.equipMents;
					myCache.put(temp_key, obj.equipMents); //保存最新数据
				}).error(function(error) {
					obj.equipMents = myCache.get(temp_key); //读取本地数据
				});
			},
			//获取维修内容价格清单
			getMaintenancePrice: function(obj, type) {
				var temp_key = "/mobile/maintenancePrice" + type;
				if(CACHE_[temp_key]) {
					obj.mpls = CACHE_[temp_key];
					return;
				}
				var url = rootPath + "/mobile/maintenancePrice?$filter=mplType eq '" + type + "'";
				$http({
					method: 'get',
					url: url,
				}).success(function(response) {
					obj.mpls = response.value;
					CACHE_[temp_key] = obj.mpls;
					myCache.put(temp_key, obj.mpls); //保存最新数据
				}).error(function(error) {
					obj.mpls = myCache.get(temp_key); //读取本地数据
				});
			},
			//获取数据字典 --- 维修细项类型
			getDICTByCode: function(obj, code) {
				var temp_key = "/sys/dict/dictItems" + code;
				if(CACHE_[temp_key]) {
					obj.DICT = CACHE_[temp_key];
					return;
				}
				var url = rootPath + "/mobile/dict/dictItems?dictCode=" + code;
				$http({
					method: 'get',
					url: url,
				}).success(function(response) {
					obj.DICT = response;
					CACHE_[temp_key] = obj.DICT;
					myCache.put(temp_key, obj.DICT); //保存最新数据
				}).error(function(error) {
					obj.DICT = myCache.get(temp_key); //读取本地数据
				});
			},
			//获取街道,没有Id是获取全部
			getStreet: function(obj, streetId) {
				var temp_key = "/infrastructure/street" + streetId;
				if(CACHE_[temp_key]) {
					obj.streetDtos = CACHE_[temp_key];
					return;
				}
				var url = rootPath + "/mobile/street?$filter=streetState eq 0";
				if(streetId != undefined) {
					url += "and streetId eq " + streetId;
				}
				$http({
					method: 'get',
					url: url,
				}).success(function(response) {
					obj.streetDtos = response.value;
					CACHE_[temp_key] = obj.streetDtos;
					myCache.put(temp_key, obj.streetDtos); //保存最新数据
				}).error(function(error) {
					obj.streetDtos = myCache.get(temp_key); //读取本地数据
				});
			},
			//获取流程类型（筛选）
			getProcType: function(obj) {
				var temp_key = "/mobile/api/work/proc";
				if(CACHE_[temp_key]) {
					obj.procList = CACHE_[temp_key];
					return;
				}
				$http({
					method: 'get',
					url: rootPath + "/mobile/api/work/proc",
				}).then(function successCallback(response) {
					obj.procList = response.data;
					CACHE_[temp_key] = obj.procList;
					myCache.put(temp_key, obj.procList); //保存最新数据
				}, function errorCallback(response) {
					obj.procList = myCache.get(temp_key); //读取本地数据
				});
			},
			//根据类型获取用户
			getUserRole: function(options, callBack) {
				if(typeof callBack === 'function'){
					$http({
						method: 'get',
						url: rootPath + "/mobile/user/userRole?userType=2&roleName=维修员&departOrCompanyId=undefined",
					}).then(function successCallback(response) {
						callBack(response.data.value);
					}, function errorCallback(response) {
						console.log("读取数据失败")
					});
				}
			},
			//根据业务Id获取文件--后端
			getFileByBusinessId: function(obj, businessId) {
				var url = rootPath + "/file/business/" + businessId;
				$http({
					method: 'GET',
					url: url,
				}).success(function(response) {
					obj.files = response.value;
				}).error(function() {});
			},
			//获取系统参数配置
			getSYSConfig: function(obj, name) {
				var temp_key = "/mobile/sysConfig" + name;
				if(CACHE_[temp_key]) {
					obj.sysConfig = CACHE_[temp_key];
					return;
				}
				var url = rootPath + "/mobile/sysConfig";
				var data = {};
				if(name != undefined) {
					url += "/findByName"
					data.name = name;
				}
				$http({
					method: 'get',
					url: url,
					data: data
				}).then(function successCallback(response) {
					obj.sysConfig = response.sysConfigValue;
					CACHE_[temp_key] = obj.sysConfig;
					myCache.put(temp_key, obj.sysConfig); //保存最新数据
				}, function errorCallback(response) {
					obj.sysConfig = myCache.get(temp_key); //读取本地数据
				});
			},
		}
	})
