
/**
 * 图片服务，包括拍照，上传图片等
 * @author lqs
 * */
angular.module('photo.service', ['global'])
	.service('photoSvc', function($http, $q, GlobalVariable, $cordovaCamera, $ionicPopup,$ionicModal,$cordovaFileTransfer) {
		var rootPath = GlobalVariable.SERVER_PATH;
		var service = {
			takePhoto:takePhoto,//拍照
			calSize:calSize,//计算图片大小
			merge:merge,//本地照片和服务器进行更新
			upload:upload,//保存照片
			showImageSlide:showImageSlide,//查看图片
			saveToLocal:saveToLocal,//保存图片到本地存储
			resetItem:resetItem//重置本地图片数据
		};
		
		function takePhoto(opts){
			var deferred = $q.defer();
			
			var options = {  
		      quality: 50,  
		      destinationType: 0,//返回类型：DATA_URL= 0，返回作为 base64 編碼字串。 FILE_URI=1，返回影像档的 URI。NATIVE_URI=2，返回图像本机URI 
		      sourceType: 1, //从哪里选择图片：PHOTOLIBRARY=0（从设备相册选择图片）,相机拍照=1,SAVEDPHOTOALBUM=2,0和1其实都是本地图库 
		      allowEdit: false,  
		      encodingType: 0,  //保存的图片格式： JPEG = 0, PNG = 1
		      //targetWidth: 100,  
		     // targetHeight: 100,  
		     // popoverOptions: CameraPopoverOptions,  
		      saveToPhotoAlbum: false  
		    };  
			
			$cordovaCamera.getPicture(options).then(function(imageData) {  
				deferred.resolve(imageData);
		    }, function(err) {  
		    	deferred.reject(err);
		    	alert(JSON.stringify(err));
		    }); 
			
			return deferred.promise;
		}
		
		function calSize(imageData){
			
		}
		
		
		function getBase64Image(img) {
		    var canvas = document.createElement("canvas");
		    canvas.width = img.width;
		    canvas.height = img.height;
			console.log(img.width);
		    var ctx = canvas.getContext("2d");
		    ctx.drawImage(img, 0, 0);
			
		    var dataURL = canvas.toDataURL();
		
		    return dataURL.replace(/^data:image\/(png|jpg);base64,/, "");
		}
		
		//文件创建失败回调
	    function  onErrorCreateFile(error){
	       alert("文件创建失败！"+error.code);
	    }
	 
	    //FileSystem加载失败回调
	    function  onErrorLoadFs(error){
	       alert("文件系统加载失败！");
	    }
		
		/**
		 * 合并本地照片和服务端图片数据
		 * @param businessId 业务ID
		 * @param photos 服务端返回的图片数据
		 * */
		function merge(photoKey,remotePhotos){
			console.log('begin to merge...');
			console.log($cordovaFileTransfer);
			var deferred = $q.defer();
			var rootPath = GlobalVariable.SERVER_PATH;//根路径
			//获取该业务ID在本地的图片
			localforage.getItem(photoKey).then(function(localPhoto){
				if(!localPhoto){
					//如果本地图片不存在，初始化一个空数组
					console.log('local photo is empty');
					localPhoto = [];
				}
			
				//处理
				var promises = [];
				//遍历检查服务端照片在本地是否已存在
				angular.forEach(remotePhotos,function(x,index){
					var existsPhotos = $linq(localPhoto).where(function (photo) {
		                return photo.sysFileId&&photo.sysFileId == x.sysFileId;
		            }).toArray();
					
			 		
					//如果不存在，发起请求去读
					if(!existsPhotos||existsPhotos.length == 0){
						var deffer = $q.defer();
						console.log('photo '+x.sysFileId+' does not exists,do http request to get it');
						var photoFromRemote = {};
						photoFromRemote.ts = new Date().Format('yyyyMMddhhmmssS');
				        photoFromRemote.sysFileId = x.sysFileId;
				        photoFromRemote.fileUrl = x.fileUrl.replace(/\\/g,'/');;
						var promise = $q(function(resolve,reject) {
							var url = encodeURI(rootPath+'/downLoad'+  photoFromRemote.fileUrl + '?type=small');
							var filename = photoFromRemote.fileUrl.split("/").pop();
							var targetPath = cordova.file.dataDirectory + (new Date().Format('yyyyMMddhhmmssS')) + '.jpg';
							var trustHosts = true
           					var options = {}
							$cordovaFileTransfer.download(url, targetPath, options, trustHosts) 
							.then(function(result) { 
									// Success!
									//alert(JSON.stringify(result)); 
									photoFromRemote.smallImageUrl = result.nativeURL;
									//alert('save photo to local success URL:'+result.nativeURL);
									localPhoto.push(photoFromRemote);
									resolve();
								}, function(error) {
									alert(JSON.stringify(error));
									reject();
								},function (progress) { 
									//$timeout(function () { $scope.downloadProgress = (progress.loaded / progress.total) * 100; }) 
								});
							
						});
						
						promises.push(promise);	
				       													
					}
				
				
				
				});
			
				$q.all(promises).then(function (results) {
		            deferred.resolve(localPhoto);
		        });
			});
		
			return deferred.promise;
		}
		
		
		
		/**
		 * 保存图片到手机本地存储
		 * @param saveUrl 本地路径
		 * @param base64Data 图片base64数据
		 * */
		function saveToLocal(saveUrl,base64Data){
			var deferred = $q.defer();
			window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, function (fs) {
		    	  fs.root.getFile(saveUrl, { create: true, exclusive: false }, function (fileEntry) {
		            var blobData = dataURItoBlob(base64Data);
					// Create a FileWriter object for our FileEntry (log.txt).
				    fileEntry.createWriter(function (fileWriter) {
				        fileWriter.onwriteend = function() {
				            deferred.resolve(fileEntry);
				        };
				
				        fileWriter.onerror = function (e) {
				            alert("Failed file write: " + e.toString());
				            deferred.reject(e);
				        };
				
				        fileWriter.write(blobData);
				    });
			    }, onErrorCreateFile);
			    				    				    				   			   
		    },onErrorLoadFs);
		    
		    return deferred.promise;
		}
		
		var dataURItoBlob = function(base64Data) {
			var byteString;
			//判断是否是base64数据
			if(base64Data.split(',')[0].indexOf('base64') >= 0) {
				byteString = atob(base64Data.split(',')[1]);
			} else {
				byteString = unescape(base64Data.split(',')[1]);
			}
			//获取文件类型
			var mimeString = base64Data.split(',')[0].split(':')[1].split(';')[0];
			//创建8位数据数组
			var ia = new Uint8Array(byteString.length);
			//将base64数据存入数组
			for(var i = 0; i < byteString.length; i++) {
				ia[i] = byteString.charCodeAt(i);
			}
			//创建Blob对象
			return new Blob([ia], {
				type: mimeString
			});
		};
		
		function upload(sysFileDtos){
			var deferred = $q.defer();
			var promises = [];
			var popup = $ionicPopup.show({
				title: '正在上传图片<i class="icon ion-load-a"></i>',
			});
			angular.forEach(sysFileDtos,function(sysFileDto){
				var promise = $q(function(resolve,reject) {	
					var file_url = rootPath + '/file?businessId=' + sysFileDto.businessId + '&module=' + sysFileDto.model + "&fileType=" + sysFileDto.fileType;
					var options = new FileUploadOptions();
					options.fileKey = "file";
					options.fileName = sysFileDto.bigImageUrl.substring(sysFileDto.bigImageUrl.lastIndexOf('/') + 1);
					options.mimeType = "text/plain";
					var params = {};					
					options.params = params;					
					var ft = new FileTransfer();
					ft.upload(sysFileDto.bigImageUrl, encodeURI(file_url),  function (r) {
					   resolve(r.response);
					}, function (error) {
					   alert("An error has occurred: " + JSON.stringify(error));
					   reject(error);
					}, options);
					
					
				});
				
				promises.push(promise);
			});
			
			$q.all(promises).then(function (results) {
				popup.close();
	            deferred.resolve();
	        },function(){
	        	alert('上传失败');
	        	popup.close();
	        });
									
			return deferred.promise;
		}
		
		
		function showImageSlide($scope,imgs,index){
			var deferred = $q.defer();
			
			if(imgs.length < index-1){
				return ;
			}
							
			var activeImg = imgs[index];
			var promises = [];
			
			//全屏查看图片
			$ionicModal.fromTemplateUrl('areas/common/html/image/imageSlideView.html', {
				scope: $scope
			}).then(function(modal) {
				$scope.modal = modal;
				$scope.modal.imgs = imgs;
				$scope.modal.index = index;
				$scope.modal.show();
			});
			
			
			angular.forEach(imgs,function(img){
				//alert(JSON.stringify(img));
				if(img.bigImageUrl == undefined||img.bigImageUrl == null){
					
					var url = encodeURI(rootPath+'/downLoad'+  img.fileUrl);
					var filename = img.fileUrl.split("/").pop();
					var targetPath = cordova.file.dataDirectory + (new Date().Format('yyyyMMddhhmmssS')) + '.jpg';
					var trustHosts = true
   					var options = {}
   					//alert('bigImage begin download');
					$cordovaFileTransfer.download(url, targetPath, options, trustHosts) 
					.then(function(result) { 
							img.bigImageUrl = result.nativeURL;
						}, function(error) {
							alert(JSON.stringify(error));
							reject();
						},function (progress) { 
							$timeout(function () { img.bigImagedownloadProgress = (progress.loaded / progress.total) * 100; }) 
						});
				}
			});
		}
		
		function resetItem(key,value){
			var deferred = $q.defer();
			localforage.removeItem(key).then(function(){
				localforage.setItem(key,value).then(function(){
					deferred.resolve();
				});
			}).catch(function(err){
				console.log(err);
				deferred.reject();
			});
			
			return deferred.promise;
		}
		
		return service;
	});