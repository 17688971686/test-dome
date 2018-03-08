angular.module('community.controller', ['community.service'])

	.controller('communityCtrl', ['$scope', '$rootScope', '$state', 'communitySvc',

		function($scope, $rootScope, $state, communitySvc) {
	
			$scope.id = $state.params.id;
			///处理兼容性
			$scope.viewMainClass = "";

			if(localStorage.getItem("isIOS") == "true") {
				$scope.viewMainClass = "iosViewMain";
			}
			//显示场地
			$scope.show_siteList = function(){
				if($scope.model.sites){ return; }
				communitySvc.getSites($scope.id,function(data){
					$scope.model.sites = data;
				});
			}
			//刷新
			$scope.doRefresh = function(){
				communitySvc.get($scope.id,function(data){
					$scope.model = data;
					$scope.$broadcast('scroll.refreshComplete'); //停止刷新广播
				});
			}
			//返回
			$scope.back = function(){
				$state.go('tab.infrastructure')
			}
			init();
			function init(){
				if($rootScope.infrastructure__param && $rootScope.infrastructure__param.community){
					$scope.model = $rootScope.infrastructure__param.community;//获取社区
				}else{
					communitySvc.get($scope.id,function(data){
						$scope.model = data;
					});
				}
			}
		}
	]);