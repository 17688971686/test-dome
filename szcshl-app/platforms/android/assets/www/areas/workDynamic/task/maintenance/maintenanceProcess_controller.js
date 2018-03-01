angular.module('maintenanceProcess.controller', ['maintenanceProcess.service', 'common.service'])

	//待办工作列表控制器
	.controller('maintenanceProcessCtrl', ["$scope", "$state", "maintenanceProcessService", 'GlobalVariable',
		'HttpUtil', 'fileSvc', 'facilitybasicdataService', 'mapSvc', 'Message', '$interval','$ionicTabsDelegate','repairTimeDelayApplyService',
		function($scope, $state, maintenanceProcessService, GlobalVariable, HttpUtil, fileSvc, facilitybasicdataService, mapSvc, Message, $interval,$ionicTabsDelegate,repairTimeDelayApplyService) {

			$scope.processName = "设施报修";
			$scope.taskId = $state.params.taskId;
			$scope.id = $state.params.id;
			$scope.model = {}; //后台查询出的数据
			$scope.data = {}; //页面传到后台的参数
			$scope._TEMP_ = {}; //缓存，仅在页面临时使用
			$scope.pageIndex = 0;//页面索引

			$scope.imgBasicUrl = GlobalVariable.imgBasicUrl;
             ///处理兼容性
			$scope.viewMainClass="";

			if(localStorage.getItem("isIOS")=="true")
			{
				$scope.viewMainClass="iosViewMain";
			}
			//计算剩余处理时间
			$scope.remind = function() {
				$scope.remindTime = '正在获取数据。。。';
				maintenanceProcessService.getSurplusTime($scope, function(data) {
					if(data && data[0] && data[0].dueDate){
						/*date = new Date(data[0].dueDate).getTime();
						$interval(function() {
							$scope.remindTime = date - new Date().getTime();
						}, 1000);*/
						$scope.remindTime = data[0].dueDate;
					}else{
						$scope.remindTime = "任务未计时";
					}
				});
			}
			//新增场地设施
			$scope.addFacilitybasicdata = function() {
				facilitybasicdataService.show($scope, $scope.model.siteInfo, $scope.model.community, $scope.model.street);
			};
			//查看图片
			$scope.showImg = function(imgs, index) {
				fileSvc.showImage($scope, imgs, index);
			}
			//添加维修图片
			$scope.addPic = function(type) {
				fileSvc.addPic(function(sysFileDto){
					sysFileDto.businessId = $scope.model.repairRegisterId;
					sysFileDto.fileType = type;
					sysFileDto.model = 'repairRegister';
					//				sysFileDto.showName = 'hdfgiu.png';
					if($scope.model.files == undefined) {
						$scope.model.files = [];
					}
					$scope.model.files.push(sysFileDto);
				}); //0图库，1相机
			}
			//删除维修照片
			$scope.deletePic = function(sysFileDto) {
				fileSvc.deletePic($scope.model.files, sysFileDto, $scope);
			}
			//添加维修细项图片
			$scope.addRepairItemPic = function(type, obj) {
				fileSvc.addPic(function(sysFileDto){
					sysFileDto.businessId = obj.repairItemId;
					sysFileDto.fileType = type;
					sysFileDto.model = 'repairItem';
					//				sysFileDto.showName = 'hdfgiu';
					if(obj.fileList == undefined) {
						obj.fileList = [];
					}
					obj.fileList.push(sysFileDto);
				});
			}
			//删除维修细项照片
			$scope.deleteRepairItemPic = function(sysFileDto, obj) {
				fileSvc.deletePic(obj.fileList, sysFileDto, $scope);
			}
			//============================页面切换===begin==============================================================
			//切换页面
			$scope.switchPageM = function(index){
				$ionicTabsDelegate.select(index);
				$scope.pageIndex = index;
				if(index==3){
					$scope.proccessRecords || HttpUtil.getProccessRecords($scope.model.processInstanceId, $scope);
				}
			}
			//刷新页面数据
			$scope.doRefresh = function(index) {
				switch(index || $scope.pageIndex) {
					case 3: //查看流程处理记录
						HttpUtil.getProccessRecords($scope.model.processInstanceId, $scope);
						break;
					default:maintenanceProcessService.get($scope);
				}
			};
			//============================页面切换===end====================================================================
			//添加维修设施
			$scope.addFacility = function() {
				if($scope.model.siteInfo.siteId) {
					facilitybasicdataService.addFacility($scope, $scope.model.siteInfo.siteId, function(obj) {
						$scope.model.repairFacilitiDtos.push(obj);
					});
				} else {
					Message.show('未选择场地');
				}
			}
			//维修设施页面切换
			$scope.facility_page = 0; //显示界面编号
			$scope.swichFacilityPage = function(num) {
				$scope.facility_page = num;
				$scope.countCost();
				switch(num) {
					case 0:
					case 2: //编辑维修细项
					case 3: //新增维修细项
						HttpUtil.getDICTByCode($scope._TEMP_, "DICT_MPLTYPE");
						break;
				}

			};
			//总费用计算
			$scope.countCost = function() {
				$scope.cost = 0;
				if($scope.model && $scope.model.repairFacilitiDtos) {
					for(var x = 0; x < $scope.model.repairFacilitiDtos.length; x++) {
						var rfd = $scope.model.repairFacilitiDtos[x];
						rfd.cost = 0;
						if(rfd.repairItemDto) {
							for(var i = 0; i < rfd.repairItemDto.length; i++) {
								var rid = rfd.repairItemDto[i];
								rfd.cost += rid.repairUnitPrice * rid.repairNumber;
							}
							$scope.cost += rfd.cost;
						}
					}
				}
			}
			//==========================维修细项===begin=====================================================================
			//维修细项类型改变
			$scope.changeMplType = function(type) {
				$scope._TEMP_.repairItemDto.repairMplName = undefined;
				if(type == undefined) {
					return;
				}
				HttpUtil.getMaintenancePrice($scope._TEMP_, type);
			}
			//维修细项改变
			$scope.changeMplName = function(name) {
				$scope._TEMP_.repairItemDto.repairNumber = undefined;
				if(name == undefined) {
					return;
				}
				var mpls = $scope._TEMP_.mpls;
				for(var index in mpls) {
					var o = mpls[index];
					if(o.mplName == name) {
						$scope._TEMP_.repairItemDto.repairMplId = o.mplId;
						$scope._TEMP_.repairItemDto.repairMplName = o.mplName;
						$scope._TEMP_.repairItemDto.repairUnitPrice = o.mplUnitPrice;
						$scope._TEMP_.repairItemDto.repairUnit = o.mplUnit;
						return;
					}
				}
			}
			//克隆维修细项 -- 编辑前克隆
			$scope.cloneRepairItems = function(obj) {
				HttpUtil.getMaintenancePrice($scope._TEMP_, obj.repairType);
				$scope._TEMP_.repairItemDto = obj;
				$scope.clone_temp = JSON.stringify(obj);
			}
			//还原维修细项 -- 取消编辑时还原
			$scope.restoreRepairItems = function(obj) {
				obj = JSON.parse($scope.clone_temp);
				$scope._TEMP_.repairItemDto = obj;
			}
			//==========================维修细项===end=====================================================================
			//保存
			$scope.save = function(submit) {
				maintenanceProcessService.setTemp($scope.model);//保存缓存
				//将所有图片放在一个数组
				var files = [];
				if($scope.model.files) {
					files = $scope.model.files.concat();
				}
				for(rfx in $scope.model.repairFacilitiDtos) {
					var rf = $scope.model.repairFacilitiDtos[rfx];
					for(rix in rf.repairItemDto) {
						var ri = rf.repairItemDto[rix];
						if(ri.fileList) {
							files = files.concat(ri.fileList);
						}
					}
				}
				//先上传图片，全部上传完成再提交流程数据
				fileSvc.uploadFiles(files, function() {
					//执行图片删除--删除服务器上的图片
					fileSvc.deleteFiles($scope.delete_files);
					if(typeof submit === 'function') {
						submit(); //提交流程
					} else {
						maintenanceProcessService.save($scope); //保存流程
					}
				});
			};
			//提交流程处理
			$scope.submitProc = function() {
				if(!($scope.model.repairLng && $scope.model.repairLat)) {
					Message.show('未设置维修地点，不能提交数据！', function() {
						$scope.showMap();
					});
					return;
				}
				if($scope.cost > 10000) {
					Message.confirm('费用超出系统要求范围，请先提交审核再维修！确定继续提交吗？', function() {
						$scope.save(function() {
							sbm();
						});
					});
					return
				}
				$scope.save(function() {
					sbm();
				});
			};

			function sbm() {
				maintenanceProcessService.update($scope, function() {
					var workJson = {
						taskId: $scope.taskId,
						operationType: 'doRepair',
						dealSuggestion: $scope.model.repairRemark,
						businessId: $scope.model.repairRegisterId
					};
					maintenanceProcessService.submitProc(JSON.stringify(workJson)); //提交流程
				}, function(error) {
				});
			}
			//提延时报修
			$scope.data.repairApply = {};
			$scope.repairTimeDelayApply = function() {
				$scope.data.repairApply.repairRegisterId = $scope.model.repairRegisterId;
				$scope.data.repairApply.taskId = $scope.taskId;
				$scope.data.repairApply.repairTitle = $scope.model.repairTitle;
				maintenanceProcessService.repairTimeDelayApply($scope.data.repairApply);
			};
			//设置维修地点
			$scope.showMap = function() {
				mapSvc.mapSvcObj.title = "设置维修地点"; //地图标题
				mapSvc.setBusinessPosition($scope.model.repairLng, $scope.model.repairLat); //设置业务位置数据
				mapSvc.showMap($scope, 'areas/workDynamic/task/maintenance/maintenanceMap.html');
			};
			//返回
			$scope.back = function() {
				$scope.countCost();
				switch($scope.facility_page) {
					case 0:
						$state.go('task');
						break;
					case 1:
						$scope.facility_page = 5;
						break;
					case 2:
						$scope.facility_page = 1;
						break;
					case 3:
						$scope.facility_page = 5;
						break;
					case 5:
						$scope.facility_page = 0;
						break;
					default:
						$state.go('task');
						break;
				}
				//				GlobalVariable.goBack($state);
			};
			init(); //初始化
			function init() {

				maintenanceProcessService.get($scope,function(data){
				$scope.id=data.repairRegisterId;
				 repairTimeDelayApplyService.getList($scope,function(data){
	           	 $scope.tiemApply=data.value; 
	           	  if($scope.tiemApply.length==0){
	           	      $scope.isSubmit = false;
	           	  }else{
                       if($scope.tiemApply[0].isActivation==1){
                             $scope.isSubmit = true;
                        }else{
                                 $scope.isSubmit = false;
                             }
	           	  }
	               });
				});
				$scope.remind();
			};
		}
	])

	.controller('end_maintenanceProcessCtrl', ["$scope", "$state", "maintenanceProcessService",
		'HttpUtil', 'fileSvc', 'facilitybasicdataService', 'mapSvc',
		function($scope, $state, maintenanceProcessService, HttpUtil, fileSvc, facilitybasicdataService, mapSvc) {

			$scope.taskId = $state.params.taskId;
			$scope.id = $state.params.id;
			$scope.model = {}; //后台查询出的数据
			$scope.data = {}; //页面传到后台的参数
			$scope._TEMP_ = {}; //缓存，仅在页面临时使用
            ///处理兼容性
			$scope.viewMainClass="";

			if(localStorage.getItem("isIOS")=="true")
			{
				$scope.viewMainClass="iosViewMain";
			}
			//查看图片
			$scope.showImg = function(imgs, index) {
				fileSvc.showImage($scope, imgs, index);
			}
			//============================页面切换===begin==============================================================
			$scope.page_number = 0; //页面编号
			//进入其他操作页面
			$scope.goElse = function() {
				$scope.page_number = 2; //默认进入延长维修页面
				$scope.title = "处理记录";
				if($scope.proccessRecords == undefined) {
					HttpUtil.getProccessRecords($scope.model.processInstanceId, $scope);
				}
			};
			//退出其他操作页面
			$scope.exitElse = function() {
				$scope.page_number = 0;
				$scope.title = $scope.model.repairTitle;
			};
			//切换其他操作页面
			$scope.switchPage = function(number) {
				$scope.page_number = number;
				switch(number) {
					case 2: //查看流程处理记录
						$scope.title = "处理记录";
						if($scope.proccessRecords == undefined) {
							HttpUtil.getProccessRecords($scope.model.processInstanceId, $scope);
						}
						break;
					case 3: //查看流程图
						$scope.title = "流程图";
						if($scope.imageUrl == undefined) {
							$scope.imageUrl = HttpUtil.rootPath + '/workflow/proccessInstance/img/' + $scope.model.processInstanceId;
						}
						break;
				}
			}
			//刷新页面数据
			$scope.doRefresh = function() {
				switch($scope.page_number) {
					case 0: //设施报修
					case 1: //延长维修
						maintenanceProcessService.get($scope);
						break;
					case 2: //查看流程处理记录
						HttpUtil.getProccessRecords($scope.model.processInstanceId, $scope);
						break;
					case 3: //查看流程图
						$scope.imageUrl = HttpUtil.rootPath + '/workflow/proccessInstance/img/' + $scope.model.processInstanceId;
						$scope.$broadcast('scroll.refreshComplete'); /*停止刷新广播*/
						break;
				}
			};
			//维修设施页面切换
			$scope.facility_page = false;
			$scope.facility_pageChange = function() {
				$scope.facility_page = !$scope.facility_page;
			}
			//返回
			$scope.back = function() {
				if($scope.facility_page){
					$scope.facility_page = false;
				}else{
					$state.go('endtask');
				}
			};
			init(); //初始化
			function init() {
				maintenanceProcessService.get($scope);
			};
		}
	])
