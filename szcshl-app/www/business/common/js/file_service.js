angular.module('file.service', ['global_variable'])
	.service('fileSvc', function() {
//		var requestFileSystem = window.requestFileSystem || window.webkitRequestFileSystem;//获取对象
		var requestFileSystem = window.webkitRequestFileSystem || window.requestFileSystem;//获取对象
		//获取文件操作权限
		function getPermission(){
			window.webkitStorageInfo.requestQuota(window.PERSISTENT, 1024*1024, function(grantedBytes) {
				console.log('信息：', grantedBytes);
			}, function(e) {
				console.log('错误：', e);
			});
		}
		//初始化
		var fileSystem;
		function initFs(){
			/**
			 * 第一个参数：window.PERSISTENT：持久     window.TEMPORARY:临时
			 * 第二个参数：大小
			 * 第三个参数：成功回调
			 * 第四个参数：错误回调
			 */
			requestFileSystem(window.PERSISTENT, 50000, function(fs){
				fileSystem = fs;
			},  function(err){
				console.error(err);
			});//开始
		}
		//创建目录
		function getDirectory(url) {
			if(!fileSystem){
				initFs();
			}
			var fr = fileSystem.root;
			var urls = url.split('/');
			url = urls[0];
			for(var i=0; i<urls.length; i++){
				fr.getDirectory(url,  {
					create: true,
					exclusive: false
				}, function(dirEntry){
					console.log(dirEntry);
				}, function(err){
					console.log('错误：', err);
				});
			}
		}
		//创建文件
		function getDirectory(fileName) {
			if(!fileSystem){
				initFs();
			}
			var fr = fileSystem.root;
			var urls = fileName.split('/');
			fuleName = urls.pop();
			for(var i=0; i<urls.length; i++){
				fr.getDirectory(urls[i],  {
					create: true,
					exclusive: false
				}, function(dirEntry){
					console.log(dirEntry);
				}, function(err){
					console.log('错误：', err);
				});
			}
			
			
			
			for(var i=0; i<fileNames.length; i++){
				fr.getDirectory(fileNames[i],  {
					create: true,
					exclusive: false
				}, function(dirEntry){
					console.log(dirEntry);
				}, function(err){
					console.log('错误：', err);
				});
			}
		}
		
		
//		function initFs(fs) {
//			fs.root.getFile("123.txt", {
//				create: true,
//				exclusive: false
//			}, function(fileEntry) {
//				console.log(fileEntry)
//			}, function(err){
//				console.error(err);
//				getPermission();
//			});
//			
//			fs.root.getDirectory('lgx',  {
//				create: true,
//				exclusive: false
//			}, function(dirEntry){
//				console.log(dirEntry);
//			}, function(err){
//				console.log('错误：', err);
//			});
//		}
		//返回服务
		return{
			getPermission: getPermission,
			initFs: initFs,
			getDirectory: getDirectory,
		}
		
	})