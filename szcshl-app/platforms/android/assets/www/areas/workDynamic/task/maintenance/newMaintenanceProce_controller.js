angular.module('newMaintenanceProce.controller', ['maintenanceProcess.service', 'common.service', 'ngCordova'])

	//待办工作列表控制器
	.controller('newMaintenanceProceCtrl', ["$scope", "$state", "maintenanceProcessService", 'GlobalVariable', 'facilitybasicdataService', 'Message',
				'basicsSvc', 'siteInfoService', 'fileSvc', '$filter',
		function($scope, $state, maintenanceProcessService, GlobalVariable, facilitybasicdataService, Message, basicsSvc, siteInfoService, fileSvc,
				$filter) {
			
			$scope.id = $state.params.id;
            
            ///处理兼容性
			$scope.viewMainClass="";
			
			if(localStorage.getItem("isIOS")=="true")
			{
				$scope.viewMainClass="iosViewMain";
			}
			//添加图片
			$scope.addImage = function(){
				fileSvc.addPic(function(sysFileDto){
					sysFileDto.businessId = $scope.model.repairRegisterId;
					sysFileDto.fileType = 1;
					sysFileDto.model = 'repairRegister';
	//				sysFileDto.showName = 'hdfgiu.png';
					if($scope.model.files == undefined) {
						$scope.model.files = [];
					}
					$scope.model.files.push(sysFileDto);
				});
			}
			//删除突图片
			$scope.deleteImage = function(sysFileDto){
				fileSvc.deletePic($scope.model.files, sysFileDto, $scope);
			}
			//全屏查看图片
			$scope.showImg = function(imgs, index){
				fileSvc.showImage($scope,imgs,index);
			}
			//添加维修设施
			$scope.addFacility = function(){
				if($scope.model.siteInfo.siteId){
					facilitybasicdataService.addFacility($scope, $scope.model.siteInfo.siteId,function(obj){
						$scope.model.repairFacilitiDtos.push(obj);
					});
				}else{
					Message.show('未选择场地');
				}
			}
			//街道改变
			$scope.streetChange = function(id){
				$scope.communitys = [];
				$scope.siteInfos = [];
				if(id){
					basicsSvc.getCommunitys(id, function(data){
						$scope.communitys = data;
					});
				}
			}
			//社区改变
			$scope.communityChange = function(id){
				$scope.siteInfos = [];
				if(id){
					siteInfoService.getSiteInfos(id, function(data){
						$scope.siteInfos = data;
					});
				}
			}
			//场地改变
			$scope.siteInfoChange = function(id){
				for(x in $scope.siteInfos){
					var o = $scope.siteInfos[x];
					if(o.siteId == id){
						$scope.model.feedBackAddress = o.address;
						var today = new Date();
						var dateF = $filter('date')(today, 'yyyyMMdd');
						$scope.model.repairTitle = "[设施报修]" + o.siteName + dateF;
						break;
					}
				}
			}
			//保存
			$scope.save = function(){
				if($scope.model.repairRegisterId){
					maintenanceProcessService.setTemp($scope.model);//保存缓存
					//执行图片删除--删除服务器上的图片
					fileSvc.deleteFiles($scope.delete_files);
					//上传图片
					fileSvc.uploadFiles($scope.model.files, function(){
						maintenanceProcessService.save($scope);//保存更新
					});
				}else{
					maintenanceProcessService.create($scope, function(data){
						var files = $scope.model.files;
						$scope.model = data;
						$scope.model.files = files;
						for(x in files){
							var sysFileDto = files[x];
							sysFileDto.businessId = $scope.model.repairRegisterId;
						}
						fileSvc.uploadFiles(files, function(){
							Message.show('操作成功' ,function() {});
						});//上传图片
					});
				}
			}
			//提交
			$scope.submit = function(){
				if($scope.model.repairRegisterId){
					maintenanceProcessService.setTemp($scope.model);//保存缓存
					//执行图片删除--删除服务器上的图片
					fileSvc.deleteFiles($scope.delete_files);
					//上传图片
					fileSvc.uploadFiles($scope.model.files, function(){
						maintenanceProcessService.startProc($scope);//发起流程
					});
				}else{
					maintenanceProcessService.create($scope, function(data){
						var files = $scope.model.files;
						$scope.model = data;
						$scope.model.files = files;
						for(x in files){
							var sysFileDto = files[x];
							sysFileDto.businessId = $scope.model.repairRegisterId;
						}
						fileSvc.uploadFiles(files, function(){
							maintenanceProcessService.startProc($scope);//发起流程
						});
					});
				}
			}
			//返回
			$scope.back = function() {
				$state.go('listMaintenanceProce');
//				GlobalVariable.goBack($state);
			};
			init(); //初始化
			function init() {
				//加载街道
				basicsSvc.getStreets(undefined, function(data){
					$scope.streets = data;
				},false);
				//判断更新和新增
				if($scope.id){
					//加数据
					maintenanceProcessService.get($scope, function(data){
						$scope.title = $scope.title || '更新设施维护';
						//加载社区列表
						basicsSvc.getCommunitys($scope.model.street.streetId, function(data){
							$scope.communitys = data;
						});
						//加载场地列表
						siteInfoService.getSiteInfos($scope.model.community.communityId, function(data){
							$scope.siteInfos = data;
						});
					});
					
				}else{
					$scope.model = {
						repairFacilitiDtos: [],
						street: {streetId: ''},
						community: {communityId: ''},
						siteInfo: {siteId: ''}
					};
				}
			};
		}
	])
