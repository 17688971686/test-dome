angular.module('task.controller', ['task.service', 'common.service', 'dateUtil.service','global'])

	//待办工作列表控制器
	.controller('TaskCtrl', ['$scope', '$state', '$stateParams','$q','taskservice', 'GlobalVariable', 'HttpUtil','$ionicPlatform',
	'dateUtil','$window','$rootScope','$ionicHistory','APP_EVENTS',
		function($scope, $state, $stateParams,$q,taskservice, GlobalVariable, HttpUtil ,$ionicPlatform,dateUtil,$window,$rootScope,$ionicHistory,APP_EVENTS) {
			//task列表数组
			$scope.task = [];
			
			//是否有更多数据加载
			$scope.moreDataCanBeLoaded = true;
			
			///处理兼容性
			$scope.viewMainClass="";
			
			if(localStorage.getItem("isIOS")=="true")
			{
				$scope.viewMainClass="iosViewMain";
			}
			
			//分页查询对象
			$scope.pageInfo = {
				pageNum: 1, //当前页码
				pageSize: 15, //每次查看多少条
			};
			
			//$scope.$on('$stateChangeSuccess', $scope.doRefresh);
			//加载系统流程数据（流程筛选）
			HttpUtil.getProcType($scope);
			//加载接到数据（街道筛选）
			HttpUtil.getStreet($scope,undefined);

			$scope.setEndTime = function(){
				dateUtil.openDatePicker(function(val){
					$scope.temp.endTime = val;
				}, $scope.temp.endTime);
			}
    		$scope.setStartTime = function(){
				dateUtil.openDatePicker(function(val){
					$scope.temp.startTime = val;
				}, $scope.temp.startTime);
			}
    		//切换页面
		    $scope.handoverPage = function(){
		    	$scope.show_list = !$scope.show_list;
		    }
			//拼接筛选条件并查询
			$scope.condition = {search:''};
			$scope.temp = {proc:{},street:{},startTime:'',endTime:''};
			$scope.query = function(search){
				$scope.condition.search = search == undefined ? '' : search;
				$scope.doRefresh();
			}
			//选择流程
			$scope.selectProc = function(i, index){
				$scope.selectProcIndex = index;
				$scope.temp.proc = i;
			}
			//选择街道
			$scope.selectStreet = function(x, index){
				$scope.selectStreetIndex = index;
				$scope.temp.street = x;
			}
			//确定筛选    必须按下确定筛选条件才有效
			$scope.screen = function(){
				$scope.procName_show = $scope.temp.proc.NAME_;
				$scope.streetName_show = $scope.temp.street.streetName;
				$scope.condition.procKey = $scope.temp.proc.KEY_ == undefined ? '' : $scope.temp.proc.KEY_;//流程条件
				$scope.condition.street = $scope.temp.street.streetId == undefined ? '' : $scope.temp.street.streetId;//街道条件
				$scope.condition.startTime = $scope.temp.startTime == undefined ? '' : $scope.temp.startTime;//时间下限
				$scope.condition.endTime = $scope.temp.endTime == undefined ? '' : $scope.temp.endTime;//时间上限
				$scope.query($scope.condition.search);
			}
			//筛选重置
			$scope.reset = function(){
				$scope.selectProcIndex = -1;
				$scope.selectStreetIndex = -1;
				$scope.temp = {proc:{},street:{},startTime:'',endTime:''};
			}
			//刷新获取最新数据
			$scope.doRefresh = function() {
				//刷新时将当前页面设为第一页
				$scope.pageInfo.pageNum = 1;
				//task列表
				var promise = taskservice.getList($scope);
				promise.then(
					//成功回调函数
					function(data) {
						$scope.task = data.value;
						$scope.taskCount = data.count;
						// 如果数据不为空，我们将数据挂载到$scope.task数组中
						if(data.value.length != 0) {
							//页码+1
							$scope.pageInfo.pageNum++;
							/*$ionicLoading.hide();*/
						} else {
							$scope.moreDataCanBeLoaded = false;
						}
					},
					//失败回调函数
					function(reason) {
						console.log("刷新失败");
					}
				).finally(function() {
					// 停止广播ion-refresher
					$scope.$broadcast('scroll.refreshComplete');
				});
			};

			//加载数据
			$scope.loadData = function() {
				var deferred = $q.defer();
				var promise = taskservice.getList($scope);
				promise.then(
					//成功回调函数
					function(data) {
//						console.log("加载更多数据");
						// 如果数据不为空，我们将数据挂载到$scope.task数组中
						if(data != null && data.value.length != 0) {
							$scope.task = $scope.task.concat(data.value);
							$scope.taskCount = data.count;
							//页码+1
							$scope.pageInfo.pageNum++;
							/*$ionicLoading.hide();*/
						} else {
							$scope.moreDataCanBeLoaded = false;
						}
						
						deferred.resolve(data);
					},
					//失败回调函数
					function(reason) {
						deferred.reject(reason);
						console.log("加载失败");
					}
				).finally(function() {
					// 停止广播infinite-scroll
					$scope.$broadcast('scroll.infiniteScrollComplete');
				});
				
				return deferred.promise;
			};

			//返回
			$scope.back = function() {
				if($scope.show_list){
					$scope.show_list = false;
				}else{
					$state.go('tab.workDynamic');
				}
			}


			//init();/*初始化*/
			function init(){
			}
		}
	])
//===============办结工作列表控制器=========办结工作列表控制器==============办结工作列表控制器=================办结工作列表控制===========================
	//办结工作列表控制器
	.controller('EndTaskCtrl', ['$scope', '$state', 'taskservice', 'GlobalVariable', 'HttpUtil', 'dateUtil',
		function($scope, $state, taskservice, GlobalVariable, HttpUtil, dateUtil) {

			//task列表数组
			$scope.endtask = [];

			//是否有更多数据加载
			$scope.moreDataCanBeLoaded = true;
            ///处理兼容性
			$scope.viewMainClass="";
			
			if(localStorage.getItem("isIOS")=="true")
			{
				$scope.viewMainClass="iosViewMain";
			}
			//分页查询对象
			$scope.pageInfo = {
				pageNum: 1, //当前页码
				pageSize: 15, //每次查看多少条
			};
			//加载系统流程数据（流程筛选）
			HttpUtil.getProcType($scope);
			//加载接到数据（街道筛选）
			HttpUtil.getStreet($scope,undefined);
			$scope.setEndTime = function(){
				dateUtil.openDatePicker(function(val){
					$scope.temp.endTime = val;
				}, $scope.temp.endTime);
			}
    		$scope.setStartTime = function(){
				dateUtil.openDatePicker(function(val){
					$scope.temp.startTime = val;
				}, $scope.temp.startTime);
			}
    		//切换页面
		    $scope.handoverPage = function(){
		    	$scope.show_list = !$scope.show_list;
		    }
			//拼接筛选条件并查询
			$scope.condition = {search:''};
			$scope.temp = {proc:{},street:{},startTime:'',endTime:''};
			$scope.query = function(search){
				$scope.condition.search = search == undefined ? '' : search;
				$scope.doRefresh();
			}
			//选择流程
			$scope.selectProc = function(i, index){
				$scope.selectProcIndex = index;
				$scope.temp.proc = i;
			}
			//选择街道
			$scope.selectStreet = function(x, index){
				$scope.selectStreetIndex = index;
				$scope.temp.street = x;
			}
			//确定筛选    必须按下确定筛选条件才有效
			$scope.screen = function(){
				$scope.procName_show = $scope.temp.proc.NAME_;
				$scope.streetName_show = $scope.temp.street.streetName;
				$scope.condition.procKey = $scope.temp.proc.KEY_ == undefined ? '' : $scope.temp.proc.KEY_;//流程条件
				$scope.condition.street = $scope.temp.street.streetId == undefined ? '' : $scope.temp.street.streetId;//街道条件
				$scope.condition.startTime = $scope.temp.startTime == undefined ? '' : $scope.temp.startTime;//时间下限
				$scope.condition.endTime = $scope.temp.endTime == undefined ? '' : $scope.temp.endTime;//时间上限
				$scope.query($scope.condition.search);
			}
			//筛选重置
			$scope.reset = function(){
				$scope.selectProcIndex = -1;
				$scope.selectStreetIndex = -1;
				$scope.temp = {proc:{},street:{},startTime:'',endTime:''};
			}


			//刷新获取最新数据
			$scope.doRefresh = function() {
				
				//刷新时将当前页面设为第一页
				$scope.pageInfo.pageNum = 1;
				//endtask列表
				var promise = taskservice.getEndList($scope);
				promise.then(
					//成功回调函数
					function(data) {
						// 如果数据不为空，我们将数据挂载到$scope.endtask数组中
						if(data != null) {
							$scope.endtask = data.value;
							$scope.endtaskCount = data.count;
						} else {
							$scope.moreDataCanBeLoaded = false;
						}
					},
					//失败回调函数
					function(reason) {
						console.log("刷新失败");
					}
				).finally(function() {
					// 停止广播ion-refresher
					$scope.$broadcast('scroll.refreshComplete');
				});
			};

			//加载数据
			$scope.loadData = function() {
				var promise = taskservice.getEndList($scope);
				promise.then(
					//成功回调函数
					function(data) {
						// 如果数据不为空，我们将数据挂载到$scope.endtask数组中
						if(data != null && data.value.length != 0) {
							$scope.endtask = $scope.endtask.concat(data.value);
							$scope.endtaskCount = data.count;
							//页码+1
							$scope.pageInfo.pageNum++;
						} else {
							$scope.moreDataCanBeLoaded = false;
						}
					},
					//失败回调函数
					function(reason) {
						console.log("加载失败");
					}
				).finally(function() {
					// 停止广播infinite-scroll
					$scope.$broadcast('scroll.infiniteScrollComplete');
				});
			};

			//审核记录
			$scope.taskRecord = function(number) {
				var promise = taskservice.getTaskRecord(number);
				promise.then(
					//成功回调函数
					function(data) {
						var data = JSON.parse(data);

						//有disID则为发文
						if(data.disID != null) {
							console.log("这是发文类型")
							var disType = data.disType;
							if(disType == "普通发文") {
								data = JSON.stringify(data);
								$state.go('dispatch_record', { data: data })
							} else if(disType == "会议纪要") {
								data = JSON.stringify(data);
								$state.go('disproxy_record', { data: data })
							} else if(disType == "征求事项") {
								data = JSON.stringify(data);
								$state.go('disseek_record', { data: data })
							}
						}
						//有recID则为收文
						else if(data.recID != null) {
							console.log('这是收文类型');
							data = JSON.stringify(data);
							$state.go('receipt_record', { data: data })
						}
					},
					//失败回调函数
					function(reason) {
						console.log("失败");
					}
				)
			};

			//返回
			$scope.back = function() {
				$state.go('tab.workDynamic');/*后退动作*/
			}

		}
	])