angular.module('proStatistics.controller', ['common.service', 'global_variable'])

	.controller('proStatisticsCtrl', ['$rootScope','$scope', '$ionicPopup', '$state', '$timeout','APP_EVENTS','$ionicHistory',
		function($rootScope,$scope, $ionicPopup, $state, $timeout,APP_EVENTS,$ionicHistory) {

			$scope.$on('$ionicView.beforeEnter', function() {
				$scope.loginName = localStorage.getItem("loginName");
			});

		}
	])

