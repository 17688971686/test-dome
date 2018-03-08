angular.module('file.service', ['global'])
	.service('fileSvc', function($http, $q, GlobalVariable, $cordovaCamera, $ionicPopup,$ionicModal) {
		var rootPath = GlobalVariable.SERVER_PATH;
		//获取图片
		var camera = function(callback, sourceType) {
			var options = {
				//这些参数可能要配合使用，如选择了sourcetype是0，destinationtype要相应的设置为1：【返回文件的URI(content://media/external/images/media/2 for Android)】
				destinationType: 0, //返回类型：DATA_URL= 0，返回作为 base64 編碼字串。 FILE_URI=1，返回影像档的 URI。NATIVE_URI=2，返回图像本机URI 
				sourceType: sourceType, //从哪里选择图片：PHOTOLIBRARY=0（从设备相册选择图片）,相机拍照=1,SAVEDPHOTOALBUM=2,0和1其实都是本地图库
				allowEdit: false, //在选择之前允许修改截图
				encodingType: 0, //保存的图片格式： JPEG = 0, PNG = 1
				quality: 50, //相片质量0-100
//				targetWidth: 1800, //照片宽度，不设则默认原图像素
//				targetHeight: 2400, //照片高度
				mediaType: 0, //可选媒体类型：圖片=0,默认值,只允许选择图片將返回指定DestinationType的参数。 視頻格式=1，允许选择视频，最终返回 FILE_URI(网址)。ALLMEDIA= 2，允许所有媒体类型的选择。
				cameraDirection: 0, //选择摄像头类型(前置摄像头或者后面的摄像头)：Back= 0(后置),Front-facing = 1(前置)
				saveToPhotoAlbum: true //是否保存进手机相册
//			    popoverOptions: CameraPopoverOptions,//CameraPopoverOptions,iOS特供,从iPad的系统相册选择图片,指定popover的定位元素的位置箭头方向和参数
			};
			$cordovaCamera.getPicture(options).then(function(imageData) {
				var sysFileDto = {};
				if(imageData){
					sysFileDto.fileSize = parseInt(imageData.length*3/4/1024);//KB
					if(sysFileDto.fileSize < 100){//100kB=100*1024
						showMsg('图片过小('+sysFileDto.fileSize+')KB',sysFileDto);
						return;
					}else if(sysFileDto.fileSize > 5120){//5MB=5*1024*1024
						showMsg('图片过大('+parseInt(sysFileDto.fileSize/1024)+')MB',sysFileDto);
						return;
					}
				}else{
					imageData = 'iVBORw0KGgoAAAANSUhEUgAAADYAAAA/CAYAAACrSjsVAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAJ/SURBVGhD7ZjRkcIgEEDTpFaQLvKrDZgS1Aq0AidfahOONnFzzuwJgWRZFgSjUTw+3sydWZZ9YSGZFD+/V/hGslhqZLHUsMXOa5gUBRSa6RpONGZMHqwni1lxY5HFTAyx3RwlcDKHHRojOK2mTFwBk9XFiDNo5uyYopjC8tzGPFqPYJiYszgMmZiuAEsr9x6xoAIV80bN0cCMu27xZDFJUE9fYDlFMTdmDbpuSav2oivM5ZYxfTs+ZY9JQhLRmG5FeujdluJM63r3oWBUsaC9ZSIFaG4GY+UFY4q5TkEfemWCxuI537piTCv6uX+IdC06qhiNcZxMFrcbwu8pRlTfrJeJ4ROqwz4VXXJt69FT0c5ptahLjK3Hxha72yZKIOoAoWL36Q+RwHoIjNi9DY4SBRcaJ0bbNbgeBCsmsVrAk8hXsLUnuDZW+A6hmHpuuMUSJ4ulRhZLjSyWGlksNbJYamSx1MhiqZHFUuMfih1qKOuj8du+LmFxQDEfjCEmCi/LCIh4yxEW6nq15b4hPofTtlJ11LBnrr9gxYRYBRv97e+8hcoxeRRcHk/ugD0mCo0p7FPFZDBqNxZfoVisb8sSyxq/k1yiU6wxjjzRYtUWfQsUSfHgC2yqULEb1uRifN/Scq9085GxQlJf4yQGixn/DxSzisHx4m/HPh4iFn0qGsKaEDGaC8UrOX2tkxwixiFPwrqOeH7FrpgHud9U7DPFhJR+Fu1rfFd9cGJ4rLnHjOJorCVGaogV0w8/c5Xagvj2wxAxPc7Ih9uNFMueigImT6iY3mPeNwaZTE3sfPMgxb6K2BUbxleLBaz8QB5/V0ycLJYaWSw1slhqZLG0uMIfzhy/QVDQXJMAAAAASUVORK5CYII=';
				}
				sysFileDto.fileUrl = "data:image/jpeg;base64," + imageData;
				callback(sysFileDto);
			}, function(err) { //相机取消
			});
		};
		//提示图片大小限制
		var showMsg = function(msg){
			$ionicPopup.show({
				title: msg,
				template:'只能上传100KB ~ 5MB的图片',
				buttons: [{
						text: '我知道了',
						type: 'button-positive',
						onTap: function(e) {
								//
						}
					}
				]
			});
		};
		//base64转Blob对象
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
		//图片上传
		var doUploadFile = function(sysFileDto, callback) {
			var blob = dataURItoBlob(sysFileDto.fileUrl); // 转换
			var fd = new FormData(document.forms[0]); //创建form表单数据
			//添加数据  inputName  fileDataata  fileName
			fd.append("file", blob, sysFileDto.showName || Date.parse(new Date()) + '.' + blob.type.split('/')[1]);
			//上传文件
			$.ajax({
				url: rootPath + '/file?businessId=' + sysFileDto.businessId + '&module=' + sysFileDto.model + "&fileType=" + sysFileDto.fileType,
				method: 'POST',
				processData: false, // 必须
				contentType: false, // 必须
				dataType: 'json',
				data: fd,
//				async:false,//是否异步
//				processData : false,
// 				contentType : false ,
				/*//文件上传百分比
				 xhr: function(){
				    var xhr = $.ajaxSettings.xhr();
				    if(onprogress && xhr.upload) {
				    	xhr.upload.addEventListener("progress" , onprogress, false);
				    	return xhr;
				    }
				},*/
				success:function(data) {
					sysFileDto.sysFileId = data.sysFileId;
					sysFileDto.showName = data.showName;
					sysFileDto.fileUrl = data.fileUrl;
					callback();
				}
			});
			
			/*function onprogress(evt){
				var loaded = evt.loaded;     //已经上传大小情况
				var tot = evt.total;      //附件总大小
				var per = Math.floor(100*loaded/tot);  //已经上传的百分比
				console.log(per);
			}*/
		};
		//图片数组上传完成回调函数
        var uploadsCB = function(){
        	alert('默认回调--所有文件上传完成');
        };
        //图片数组上传递归函数
		var uploads = function(files) {
			if(files != undefined && files.length != 0) {
				doUploadFile(files.pop(),function(){
					uploads(files);
				});
			}else{
				uploadsCB();
			}
		};
		return {
			//全屏查看图片
			showImage: function($scope,imgs,index) {
				$ionicModal.fromTemplateUrl('areas/common/html/image/imageView.html', {
					scope: $scope
				}).then(function(modal) {
					$scope.modal = modal;
					$scope.modal.imgs = imgs;
					$scope.modal.index = index;
					$scope.modal.show();
				});
			},
			camera: function(callback){
				if(!(typeof callback === 'function')){
					console.log("回调函数错误");
					return;
				}
				camera(callback, 1); //0图库，1相机
			},
			//添加图片
			addPic: function(callback) {
				if(!(typeof callback === 'function')){
					console.log("回调函数错误");
					return;
				}
				// 选择图片来源
				$ionicPopup.show({
					title: '请选择图片源',
					buttons: [{
							text: '<i class="icon ion-images"></i><br/>图库',
							type: 'button-positive',
							onTap: function(e) {
								camera(callback, 0); //0图库，1相机
							}
						},
						{
							text: '<i class="icon ion-camera"></i><br/>相机',
							type: 'button-positive',
							onTap: function(e) {
								camera(callback, 1); //0图库，1相机
							}
						}
					]
				});
			},
			//删除图片--页面
			deletePic: function(files, sysFileDto, $scope) {
				if(sysFileDto.fileUrl.length <= 750) {
					if($scope.delete_files == undefined) {
						$scope.delete_files = [];
					}
					$scope.delete_files.push(sysFileDto);
				}
				files.splice(files.indexOf(sysFileDto), 1); //删除数组里的图片
			},
			//=========================文件上传操作==========================================================
			//上传文件--后端
			uploadFile: function(sysFileDto,callback){
				if(sysFileDto.fileUrl.length < 750 ) { return; }
				if(typeof callback === 'function'){
					doUploadFile(sysFileDto,callback);	
				}else{
					doUploadFile(sysFileDto,function(){
						console.log("文件上传成功回调 --- 默认");
					});
				}
			},
			//文件多项上传--后端
			uploadFiles: function(files,callback){
				var array = [];
				for(x in files){
					obj = files[x];
					if(obj.fileUrl != undefined && obj.fileUrl.length > 750){
						array.push(obj);
					}
				}
				//所有图片上传成功回调函数
				if(typeof callback === 'function'){
					uploadsCB = function(){ callback(); }
				}
				uploads(array);
			},
			//修改文件--后端
			updateFile: function() {},
			//删除文件--后端
			deleteFile: function(fileObj) {
				if(!fileObj || !fileObj.fileUrl || fileObj.fileUrl.length > 750) {
					return;
				}
				var url = rootPath + "/file/delete?sysFileId=" + fileObj.sysFileId;
				$http({
					method: 'POST',
					url: url,
				}).success(function(response) {}).error(function() {});
			},
			//文件多项删除--后端
			deleteFiles: function(files) {
				//执行图片删除			
				if(files == undefined || files.length == 0) {
					return;
				}
				for(x in files) {
					this.deleteFile(files[x]);
				}
			},
			//通过业务ID获取文件信息
			getFilesByBusiness:function(businessId){
				var deferred = $q.defer();
				var url = rootPath + "/file/business/" + businessId;
				$http({
					method: 'GET',
					url: url,
				}).success(function(response) {
					deferred.resolve(response);
				}).error(function(err) {
					deferred.reject(err);
				});
				
				return deferred.promise;
			}
		}
	})