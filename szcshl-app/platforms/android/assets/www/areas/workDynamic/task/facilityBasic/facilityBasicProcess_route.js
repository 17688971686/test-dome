//个人中心功能路由
angular.module('route.facilityBasicProcess', ['facilityBasicProcess.controller'])
	.config(function($stateProvider, $urlRouterProvider) {

		$stateProvider

			//文体设施备注修改
			.state('facilityBasicProcess/doRepair', {
				url: '/office/facilityBasicProcess/doRepair/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/facilityBasic/facilityBasicProcess.html',
				controller: "facilityBasicProcessCtrl"
			})
	});
			