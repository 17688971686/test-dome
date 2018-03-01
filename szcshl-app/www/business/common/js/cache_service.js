angular.module('cache.service', [])
	//缓存
	.service('cacheSvc', function($cacheFactory) {
		var put; 
		var get; 
		var remove; 
		var removeAll; 
		var clear; //定义方法名
		//创建缓存对象--容器
		var modelCache = $cacheFactory('modelCache');
		//用户名
		var loginName = localStorage.getItem("loginName");
		//存储key的key
		var clearModelKey = loginName + "_CLEAR_MODEL_KEY";
		//获取key
		function getModelKey(key) {
			return loginName + "_" + key;
		}
		//判断使用的缓存方式（优先选择localforage，否则选择localStorage）
		if(typeof localforage === 'object') {
			//保存数据到缓存
			put = function(key, value) {
				key = getModelKey(key); //获取key
				//console.log("缓存数据 key："+key);
				localforage.getItem(clearModelKey, function(clearKey){
					if(clearKey == undefined) {
						clearKey = [];
					} //创建缓存key数组
					if(clearKey.indexOf(key) == -1) {
						clearKey.push(key);
					} //判断key不存在则保存key
					localforage.setItem(clearModelKey, clearKey); //保存key值数组到本地
					modelCache.put(key, value); //数据放入缓存
					localforage.setItem(key, value).then(function(value){
//						console.log(value);
					}).catch(function(err) {
					    console.log(err);
					});
				}); 
			}
			//获取缓存数据
			get = function(key, callback) {
				key = getModelKey(key); //获取key
				console.log("读取缓存数据 key：" + key);
				var value = modelCache.get(key);
				if(value) {
					callback(value);
				}
				localforage.getItem(key, function(err,value){
					modelCache.put(key, value);
					callback(value);
				}); //缓存中没有数据则读取本地
			}
			//移除指定缓存
			remove = function(key) {
				key = getModelKey(key); //获取key
				modelCache.remove(key); //移除缓存
				localforage.getItem(clearModelKey, function(err,clearKey){
					if(clearKey == undefined || clearKey.indexOf(key) == -1) {return;};
					clearKey.slice(clearKey.indexOf(key), 1); //移除key
					localforage.setItem(clearModelKey, clearKey); //保存key值数组到本地
					localforage.removeItem(key).then(function() {
				    	console.log('清空缓存：'+key);
					}).catch(function(err) {
					    console.log(err);
					});
				}); //获取本地记录的key值数组
			}
			//移除当前用户所有缓存
			removeAll = function() {
				modelCache.removeAll(); //移除缓存
				var clearKey = localforage.getItem(clearModelKey,function(err,clearKey){
					if(clearKey && clearKey.length>0) {
						for(var i = 0; i < clearKey.length; i++) {
							localforage.removeItem(clearKey[i]); //清理缓存
						}
					}
					localforage.removeItem(clearModelKey); //清理key
				}); //获取需要清理的缓存key
			}
			//清除所有缓存
			clear = function() {
				modelCache.removeAll(); //移除缓存
				localforage.clear().then(function() {
				    console.log('清空所有缓存');
				}).catch(function(err) {
				    console.log(err);
				});
			}
		} else {
			//保存数据到缓存
			put = function(key, value) {
				key = getModelKey(key); //获取key
				//			console.log("缓存数据 key："+key);
				var clearKey = JSON.parse(localStorage.getItem(clearModelKey)); //获取本地记录的key值数组
				if(clearKey == undefined) {
					clearKey = [];
				} //创建缓存key数组
				if(clearKey.indexOf(key) == -1) {
					clearKey.push(key);
				} //判断key不存在则保存key
				localStorage.setItem(clearModelKey, JSON.stringify(clearKey)); //保存key值数组到本地
				modelCache.put(key, value); //数据放入缓存
				localStorage.setItem(key, JSON.stringify(value)); //保存数据到本地	
			}
			//获取缓存数据
			get = function(key, callback) {
				key = getModelKey(key); //获取key
				console.log("读取缓存数据 key：" + key);
				var data = modelCache.get(key);
				if(data) {
					return callback(data);
				}
				data = JSON.parse(localStorage.getItem(key)); //缓存中没有数据则读取本地
				if(data) {
					modelCache.put(key, data);
				}; //数据放入缓存
				return callback(data);
			}
			//移除指定缓存
			remove = function(key) {
				var data = get(key);
				if(data) {
					key = getModelKey(key); //获取key
					modelCache.remove(key); //移除缓存
					var clearKey = JSON.parse(localStorage.getItem(clearModelKey)); //获取本地记录的key值数组
					if(clearKey == undefined || clearKey.indexOf(key) == -1) {
						return data;
					};
					clearKey.slice(clearKey.indexOf(key), 1); //移除key
					localStorage.setItem(clearModelKey, JSON.stringify(clearKey)); //保存key值数组到本地
					localStorage.removeItem(key); //移除本地数据
					return data;
				}
			}
			//移除当前用户所有缓存
			removeAll = function() {
				modelCache.removeAll(); //移除缓存
				var clearKey = JSON.parse(localStorage.getItem(clearModelKey)); //获取需要清理的缓存key
				if(clearKey && clearKey.length) {
					for(var i = 0; i < clearKey.length; i++) {
						localStorage.removeItem(clearKey[i]); //清理缓存
					}
				}
				localStorage.removeItem(clearModelKey); //清理key
			}
			//清除所有缓存
			clear = function() {
				modelCache.removeAll(); //移除缓存
				localStorage.removeAll();
			}
		}

		return {
			put: put,
			get: get,
			remove: remove,
			removeAll: removeAll,
			clear: clear,
		}
	})