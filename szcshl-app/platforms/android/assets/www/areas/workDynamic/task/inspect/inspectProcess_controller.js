angular.module('inspectProcess.controller', ['inspectProcess.service', 'common.service','photo.service'])

	//待办工作列表控制器
	.controller('inspectProcessCtrl', ['$scope', '$rootScope', '$state', 'GlobalVariable', 'inspectProcessService',
		'HttpUtil', 'fileSvc', 'mapSvc', 'facilitybasicdataService', 'Message', 'siteInfoService','$ionicTabsDelegate','photoSvc',
		function($scope, $rootScope, $state, GlobalVariable, inspectProcessService, HttpUtil, fileSvc, mapSvc,
			facilitybasicdataService, Message, siteInfoService,$ionicTabsDelegate,photoSvc) {

			$scope.id = $state.params.id;
			$scope.taskId = $state.params.taskId;
			$scope.show_video = true;
			$scope.data = {};
			$scope._TEMP_ = {};
			$scope._TEMP_.page = 0;
			$scope.inspectResult_flag = '0';
			$scope.allow_submit = false;
			$scope.pageIndex = 0;//页面索引
			var imageKey = 'photo_'+$scope.id;
			//新增场地设施
			$scope.addFacilitybasicdata = function() {
				facilitybasicdataService.show($scope, $scope.model.siteInfo, $scope.model.community, $scope.model.street);
			};
			//巡查结果选择
			$scope.inspectResultChange = function(value) {
				//情况1 未选择
				if(value == 0) {
					$scope.allow_submit = false;
					return;
				}
				$scope.allow_submit = true;
				//情况2 需要报修
				if(value == 1) {
					$scope.model.isNeedRepair = 1;
					if($scope.model.inspectResultContent == '不需要维修') {
						$scope.model.inspectResultContent = '需要维修';
					}
					return;
				}
				//情况3 不需要报修
				$scope.model.isNeedRepair = 0;
				$scope.model.inspectResultContent = '不需要维修';

			}
			//刷新流程处理记录页面
			$scope.refreshProccessRecords = function() {
				HttpUtil.getProccessRecords($scope.model.processInstanceId, $scope);
			}
			//切换页面
			$scope.switchPageM = function(index){
				$ionicTabsDelegate.select(index);
				$scope.pageIndex = index;
				if(index==1){
					$scope.proccessRecords || HttpUtil.getProccessRecords($scope.model.processInstanceId, $scope);
				}
			}
			//查看图片
			$scope.showImg = function(index) {				
				//查看图片
				photoSvc.showImageSlide($scope, $scope.model.files, index);
				//fileSvc.showImage($scope, imgs, index);
			}
			//添加图片
			$scope.addPic = function(type) {
				photoSvc.takePhoto().then(function(imageData){
					$scope.model.files = $scope.model.files||[];
				    var photo = {};
				    photo.ts = new Date().Format('yyyyMMddhhmmssS');
			        // photoFromRemote.sysFileId = x.sysFileI
				    var fileName = new Date().Format('yyyyMMddhhmmssS') + '.jpg';
				    var base64Data = 'data:image/png;base64,' + imageData;
				    photoSvc.saveToLocal(fileName,base64Data).then(function(fileEntry){
				    	var fileUrl = fileEntry.toURL();
				    	
				    	photo.smallImageUrl = fileUrl;
				    	photo.bigImageUrl = fileUrl;
				    	photo.businessId = $scope.model.inspectMaintenanceId;
						photo.fileType = 1;
						photo.model = 'inspectMaintenance';
				    	$scope.model.files.push(photo);
				    });
				},function(err){
					alert('拍照功能错误');

				});					
				
			}
			//删除照片
			$scope.deletePic = function(obj) {
				fileSvc.deletePic($scope.model.files, obj, $scope);
			}
			//添加报修记录
			$scope.addRecords = function(obj) {
				$scope.model.repairRegisters.push(obj);
			}
			//添加维修设施
			$scope.addFacility = function(item) {
				if($scope.model.siteInfo.siteId) {
					facilitybasicdataService.addFacility($scope, $scope.model.siteInfo.siteId, function(obj) {
						if(item.repairFacilities == undefined) {
							item.repairFacilities = [];
						}
						item.repairFacilities.push(obj);
					});
				} else {
					Message.show('未选择场地');
				}
			}
			//保存信息
			$scope.save = function() {
				inspectProcessService.setTemp($scope.model);//保存缓存								
				//提交流程
				inspectProcessService.save($scope);
			}
			//提交流程
			$scope.submitProc = function() {								
				if($scope.model.inspectLng && $scope.model.inspectLat) {
					inspectProcessService.setTemp($scope.model);//保存缓存
					//执行图片删除
					//fileSvc.deleteFiles($scope.delete_files);
					//上传
					var needToUploadPhotos = $linq($scope.model.files).where(function (photo) {
		                return photo.sysFileId == undefined||photo.sysFileId == null;
		            }).toArray();
		            		       
					photoSvc.upload(needToUploadPhotos).then(function(){
						
						photoSvc.resetItem(imageKey,$scope.model.files).then(function(){
							inspectProcessService.update($scope, function() {
								var workJson = {
									taskId: $scope.taskId,
									operationType: 'inspectFeedBack',
									dealSuggestion: $scope.model.inspectResultContent,
									businessId: $scope.model.inspectMaintenanceId
								}
								inspectProcessService.submitProc(JSON.stringify(workJson)); //提交流程
							},function(){});	
						});
						
		            	
		           });
				} else {
					Message.show('未设置巡查地点，不能提交数据！', function() {
						$scope.showMap();
					});
				}
				
			}
			//设置巡查地点
			$scope.showMap = function() {
				mapSvc.mapSvcObj.title = "设置巡查地点"; //地图标题
				mapSvc.setBusinessPosition($scope.model.inspectLng, $scope.model.inspectLat);//设置业务位置数据
				mapSvc.showMap($scope, 'areas/workDynamic/task/inspect/inspectMap.html');
			};
			//场地信息
			$scope.setSite = function() {
				Message.showModal($scope, 'areas/workDynamic/siteInfo/siteInfoMap.html', function() {
					//读取场地数据
					siteInfoService.get($scope.model.siteInfo.siteId, function() {
						var site = siteInfoService.siteInfoSvcObj.siteInfo;
						//设置地图中心点
						mapSvc.setBusinessPosition(site.siteCoord_X, site.siteCoord_Y); //设置业务位置数据
						mapSvc.init(); //初始化地图
						$scope.mapSvcObj = mapSvc.mapSvcObj;
						$scope.siteInfoSvcObj = siteInfoService.siteInfoSvcObj;
					});
				});
			};
			//返回
			$scope.back = function() {
				switch($scope._TEMP_.page) {
					case 0:
						photoSvc.resetItem(imageKey,$scope.model.files).then(function(){
							console.log('保存图片到本地成功');
							$state.go('task');
						});
						break;
					case 1:
						$scope._TEMP_.page = 0;
						break;
					default:
						photoSvc.resetItem(imageKey,$scope.model.files).then(function(){
							console.log('保存图片到本地成功');
							$state.go('task');
						});
				}
			};
			init(); //初始化
			function init() {
				console.log($scope.id);
				inspectProcessService.get($scope).then(function(data){
					var remotePhotos = [];
					fileSvc.getFilesByBusiness($scope.id).then(function(res){
						remotePhotos = $linq(res.value).where(function (photo) {
			                return photo.fileType&&photo.fileType == '1';
			            }).toArray();
						
					}).finally(function(){
						photoSvc.merge(imageKey,remotePhotos).then(function(mergedPhotos){
							$scope.model.files = mergedPhotos;
							//$scope.$applyAsync();
							
						});
					});
				});
			};
		}
	])

	.controller('end_inspectProcessCtrl', ['$scope', '$state', 'inspectProcessService',
		'HttpUtil', 'fileSvc', 'mapSvc',
		function($scope, $state, inspectProcessService, HttpUtil, fileSvc, mapSvc) {

			$scope.id = $state.params.id;
			$scope.taskId = $state.params.taskId;
			$scope.show_video = true;
			$scope.data = {};
			$scope._TEMP_ = {};
			$scope._TEMP_.page = 0;
			$scope.inspectResult_flag = '0';
			$scope.allow_submit = false;
			//进入流程处理记录页面
			$scope.goProccessRecords = function() {
				if($scope.proccessRecords == undefined) {
					HttpUtil.getProccessRecords($scope.model.processInstanceId, $scope);
				}
			}
			//退出流程处理记录页面
			$scope.exitProccessRecords = function() {}
			//刷新流程处理记录页面
			$scope.refreshProccessRecords = function() {
				HttpUtil.getProccessRecords($scope.model.processInstanceId, $scope);
			}
			//查看图片
			$scope.showImg = function(index) {
				fileSvc.showImage($scope, $scope.id, index);
			}
			//返回
			$scope.back = function() {
				$state.go('endtask');
			};
			init(); //初始化
			function init() {
				inspectProcessService.get($scope);
			};
		}
	])