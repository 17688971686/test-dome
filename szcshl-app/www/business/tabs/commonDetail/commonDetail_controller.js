angular.module('commonDetail.controller', ['common.service', 'global_variable'])

	.controller('commonDetailCtrl', ['$rootScope','$scope', '$ionicPopup', '$state', '$timeout','APP_EVENTS','$ionicHistory',
		function($rootScope,$scope, $ionicPopup, $state, $timeout,APP_EVENTS,$ionicHistory) {

			$scope.$on('$ionicView.beforeEnter', function() {
				$scope.loginName = localStorage.getItem("loginName");
			});
			$scope.title = "审批登记表";

		}
	])

