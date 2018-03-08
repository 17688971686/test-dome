//个人中心功能路由
angular.module('route.repairTimeDelayApply', ['repairTimeDelayApply.controller','reapply.controller'])
	.config(function($stateProvider, $urlRouterProvider) {

		$stateProvider

			//维修延长的审核
			.state('repairTimeDelayApplyProcess/leadercheck', {
				url: '/office/repairTimeDelayApplyProcess/leadercheck/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/repairTimeDelayApply/repairTimeDelayApply.html',
				controller: "repairTimeDelayApplyCtrl"
			})
			//重新申请
			.state('repairTimeDelayApplyProcess/reapply', {
				url: '/office/repairTimeDelayApplyProcess/reapply/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/repairTimeDelayApply/reapply/reapply.html',
				controller: "reapplyCtrl"
			})
	});
			