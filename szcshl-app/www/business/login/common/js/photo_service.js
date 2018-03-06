/**
 * 图片服务，包括拍照，上传图片等
 * @author lqs
 * */
angular.module('photo.service', ['global_variable'])
	.service('photoSvc', function($http, $q, global, $cordovaCamera, $ionicPopup, $ionicModal, $cordovaFileTransfer) {
		var rootPath = global.SERVER_PATH;
		var service = {
			config: config,//用于配置获取手机相片的参数
			camera: camera, //获取照片
			takePhoto: takePhoto, //拍照
			merge: merge, //本地照片和服务器进行更新
			showImageSlide: showImageSlide, //查看图片
			saveToLocal: saveToLocal, //保存图片到本地存储
			resetItem: resetItem, //重置本地图片数据
			deletePhoto: deletePhoto, //删除图片
			upload: upload, //上传
		};
		
		//获取相片参数配置
		var config = {
			//这些参数可能要配合使用，如选择了sourcetype是0，destinationtype要相应的设置为1：【返回文件的URI(content://media/external/images/media/2 for Android)】
			destinationType: 0, //返回类型：DATA_URL= 0，返回作为 base64 編碼字串。 FILE_URI=1，返回影像档的 URI。NATIVE_URI=2，返回图像本机URI 
			sourceType: sourceType, //从哪里选择图片：PHOTOLIBRARY=0（从设备相册选择图片）,相机拍照=1,SAVEDPHOTOALBUM=2,0和1其实都是本地图库
			allowEdit: false, //在选择之前允许修改截图
			encodingType: 0, //保存的图片格式： JPEG = 0, PNG = 1
			quality: 50, //相片质量0-100
//			targetWidth: 1800, //照片宽度，不设则默认原图像素
//			targetHeight: 2400, //照片高度
			mediaType: 0, //可选媒体类型：圖片=0,默认值,只允许选择图片將返回指定DestinationType的参数。 視頻格式=1，允许选择视频，最终返回 FILE_URI(网址)。ALLMEDIA= 2，允许所有媒体类型的选择。
			cameraDirection: 0, //选择摄像头类型(前置摄像头或者后面的摄像头)：Back= 0(后置),Front-facing = 1(前置)
			saveToPhotoAlbum: true //是否保存进手机相册
//			popoverOptions: CameraPopoverOptions,//CameraPopoverOptions,iOS特供,从iPad的系统相册选择图片,指定popover的定位元素的位置箭头方向和参数
		};

		//启用手机拍照
		function camera() {
			var r = $cordovaCamera.getPicture(config)
			return r;
		}

		function takePhoto(callback) {
			camera().then(function(imageData) {
				var photo = {};
				var dateString = new Date().Format('yyyyMMddhhmmssS');
				var fileName = dateString + '.jpg';
				var base64Data = 'data:image/jpeg;base64,' + imageData;
				photo.ts = dateString;

				saveToLocal(fileName, base64Data).then(function(fileEntry) {
					var fileUrl = fileEntry.toURL();
					photo.smallImageUrl = fileUrl;
					photo.bigImageUrl = fileUrl;
					photo.showName = fileName;
					callback(photo);
				});
			}, function(err) {
				//alert('拍照功能错误');
			});
		}
		
		//
		function getBase64Image(img) {
			var canvas = document.createElement("canvas");
			canvas.width = img.width;
			canvas.height = img.height;
			var ctx = canvas.getContext("2d");
			ctx.drawImage(img, 0, 0);
			var dataURL = canvas.toDataURL();
			return dataURL.replace(/^data:image\/(png|jpg);base64,/, "");
		}
		//base64数据转Blob对象数据
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

		//文件创建失败回调
		function onErrorCreateFile(error) {
			alert("文件创建失败！" + error.code);
		}

		//FileSystem加载失败回调
		function onErrorLoadFs(error) {
			alert("文件系统加载失败！");
		}

		/**
		 * 合并本地照片和服务端图片数据
		 * @param photoKey 存储图片的key
		 * @param remotePhotos 服务端返回的图片数据
		 * */
		function merge(photoKey, remotePhotos) {

			var deferred = $q.defer();
			var rootPath = global.SERVER_PATH; //根路径
			//获取该业务ID在本地的图片
			localforage.getItem(photoKey).then(function(localPhoto) {
				if(!localPhoto) {
					//如果本地图片不存在，初始化一个空数组
					console.log('local photo is empty');
					localPhoto = [];
				}

				//处理
				var promises = [];
				//遍历检查服务端照片在本地是否已存在
				angular.forEach(remotePhotos, function(x, index) {
					var existsPhotos = $linq(localPhoto).where(function(photo) {
						return photo.sysFileId && photo.sysFileId == x.sysFileId;
					}).toArray();

					//如果不存在，发起请求去读
					if(!existsPhotos || existsPhotos.length == 0) {
						var deffer = $q.defer();
						//alert('photo ' + x.sysFileId + ' does not exists,do http request to get it');
						//alert(JSON.stringify(x));
						var photoFromRemote = {};
						photoFromRemote.ts = new Date().Format('yyyyMMddhhmmssS');
						photoFromRemote.sysFileId = x.sysFileId;
						photoFromRemote.fileUrl = x.fileUrl.replace(/\\/g, '/');
						photoFromRemote.fileType = x.fileType;
						var promise = $q(function(resolve, reject) {
							var url = encodeURI(rootPath + '/downLoad' + photoFromRemote.fileUrl + '?type=small');
							var filename = photoFromRemote.fileUrl.split("/").pop();
							var targetPath = cordova.file.dataDirectory + photoFromRemote.sysFileId + (new Date().Format('yyyyMMddhhmmssS')) + '.jpg';
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
								}, function(progress) {
									//$timeout(function () { $scope.downloadProgress = (progress.loaded / progress.total) * 100; })
								});

						});

						promises.push(promise);

					}

				});

				$q.all(promises).then(function(results) {
					resetItem(photoKey, localPhoto).then(function() {
						console.log('保存图片到本地成功');
						deferred.resolve(localPhoto);
					});
					
				});
			});

			return deferred.promise;
		}

		/**
		 * 保存图片到手机本地存储
		 * @param saveUrl 本地路径
		 * @param base64Data 图片base64数据
		 * */
		function saveToLocal(saveUrl, base64Data) {
			var deferred = $q.defer();
			window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, function(fs) {
				fs.root.getFile(saveUrl, {
					create: true,
					exclusive: false
				}, function(fileEntry) {
					var blobData = dataURItoBlob(base64Data);
					// Create a FileWriter object for our FileEntry (log.txt).
					fileEntry.createWriter(function(fileWriter) {
						fileWriter.onwriteend = function() {
							deferred.resolve(fileEntry);
						};

						fileWriter.onerror = function(e) {
							alert("Failed file write: " + e.toString());
							deferred.reject(e);
						};

						fileWriter.write(blobData);
					});
				}, onErrorCreateFile);

			}, onErrorLoadFs);

			return deferred.promise;
		}

		function showImageSlide($scope, imgs, index, deletes) {
			var deferred = $q.defer();

			if(imgs.length < 1) {
				return;
			}
			if(index >= imgs.length){
				index = imgs.length - 1 ;
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
				//判断是否需要删除功能
				$scope.modal.show_photo_delete = false; //是否显示删除图片按钮
				if(deletes) {
					$scope.modal.show_photo_delete = true; //是否显示删除图片按钮
					if(deletes != imgs) {
						$scope.modal.deletePhoto = function(index) {
							var img = imgs[index];
							imgs.splice(index, 1); //删除数组里的图片
							deletePhoto(deletes, img);
//							$scope.$applyAsync();
							$scope.modal.remove();
							showImageSlide($scope, imgs, index, deletes);
						}
					}else{
						$scope.modal.deletePhoto = function(index) {
							var img = imgs[index];
							deletePhoto(deletes, img);
//							$scope.$applyAsync();
							$scope.modal.remove();
							showImageSlide($scope, imgs, index, deletes);
						}
					}
				}
			});
			
			var promises = [];
			angular.forEach(imgs, function(img) {
				//alert(JSON.stringify(img));
				
				if(img.bigImageUrl == undefined || img.bigImageUrl == null) {

					var promise = $q(function(resolve, reject) {
						var fileUrl = img.fileUrl.replace(/\\/g, '/');//替换路径上的“\”为“/”
						var url = encodeURI(rootPath + '/downLoad' + fileUrl);
						var filename = fileUrl.split("/").pop();
						var targetPath = cordova.file.dataDirectory + img.sysFileId + (new Date().Format('yyyyMMddhhmmssS')) + '.jpg';
						var trustHosts = true
						var options = {}
						$cordovaFileTransfer.download(url, targetPath, options, trustHosts)
							.then(function(result) {
								img.bigImagedownloadProgress = undefined;
								img.bigImageUrl = result.nativeURL;
								resolve(img);
							}, function(error) {
								alert(JSON.stringify(error));
								reject();
							}, function(progress) {
								//$timeout(function() {
									img.downloadProgress = progress.loaded;
								//})
							});
					});
					
					promises.push(promise);
				}
			});
			
			$q.all(promises).then(function(results) {
				deferred.resolve();
			}, function() {
				deferred.reject();
			});
		}

		/**
		 * 重置图片数据
		 * @param string key
		 * @param {Object} value
		 */
		function resetItem(key, value) {
			return localforage.setItem(key, value);
		}

		//删除
		function deletePhoto(imgs, img) {
			var imageKey = 'photo_' + img.businessId;
			if(img.sysFileId != undefined) {
				var url = rootPath + "/file/delete?sysFileId=" + img.sysFileId;
				$http({
					method: 'POST',
					url: url,
				}).success(function(response) {}).error(function() {});
			}
			imgs.splice(imgs.indexOf(img), 1); //删除数组里的图片
			resetItem(imageKey, imgs)

		}
		
		//上传
		function upload(sysFileDtos) {
			var deferred = $q.defer();
			var promises = [];
			var popup = $ionicPopup.show({
				title: '正在上传图片<i class="icon ion-load-a"></i>',
			});
			angular.forEach(sysFileDtos, function(sysFileDto) {
				var promise = $q(function(resolve, reject) {
					var file_url = rootPath + '/file?businessId=' + sysFileDto.businessId + '&module=' + sysFileDto.model + "&fileType=" + sysFileDto.fileType;
					var options = new FileUploadOptions();
					options.fileKey = "file";
					options.fileName = sysFileDto.bigImageUrl.substring(sysFileDto.bigImageUrl.lastIndexOf('/') + 1);
					options.mimeType = "text/plain";
					var params = {};
					options.params = params;
					var ft = new FileTransfer();
					ft.upload(sysFileDto.bigImageUrl, encodeURI(file_url), function(r) {
						var file = JSON.parse(r.response);
						sysFileDto.sysFileId = file.sysFileId;
						sysFileDto.fileUrl = file.fileUrl;
						sysFileDto.showName = file.showName;
						resolve(r.response);
					}, function(error) {
						alert("An error has occurred: " + JSON.stringify(error));
						reject(error);
					}, options);

				});

				promises.push(promise);
			});

			$q.all(promises).then(function(results) {
				popup.close();
				deferred.resolve();
			}, function() {
				alert('上传失败');
				popup.close();
			});

			return deferred.promise;
		}

		return service;
	});