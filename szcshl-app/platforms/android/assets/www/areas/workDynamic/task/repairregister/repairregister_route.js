//个人中心功能路由
angular.module('route.repairregister', ['repairregister.controller','repairregisterLA.controller','repairregisterLC.controller','repairregisterCC.controller','repairregisterCT.controller','repairregisterVB.controller','repairregisterLAC.controller'])
	.config(function($stateProvider, $urlRouterProvider) {

		$stateProvider
			//维修处理
			.state('repairRegisterProcess/doRepair', {
				url: '/office/repairRegisterProcess/doRepair/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/repairregister/repairregister.html',
				controller: "repairregisterCtrl"
			})
			//领导分办
			.state('repairRegisterProcess/leaderAllot', {
				url: '/office/repairRegisterProcess/leaderAllot/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/repairregister/leaderAllot/repairregisterLA.html',
				controller: "repairregisterLACtrl"
			})
			//维修费用审核
			.state('repairRegisterProcess/checkCost',{
				url: '/office/repairRegisterProcess/checkCost/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/repairregister/checkCost/repairregisterCT.html',
				controller: "repairregisterCTCtrl"
			})
			//文体局确认
			.state('repairRegisterProcess/leaderCheck',{
				url: '/office/repairRegisterProcess/leaderCheck/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/repairregister/leaderCheck/repairregisterLC.html',
				controller: "repairregisterLCCtrl"
			})
	       //社区确认
			.state('repairRegisterProcess/checkComplete',{
				url: '/office/repairRegisterProcess/checkComplete/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/repairregister/checkComplete/repairregisterCC.html',
				controller: "repairregisterCCCtrl"
			})
			//文体局再次确认
			.state('repairRegisterProcess/leaderAgainCheck',{
				url: '/office/repairRegisterProcess/leaderAgainCheck/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/repairregister/leaderAgainCheck/repairregisterLAC.html',
				controller: "repairregisterLACCtrl"
			})
			//维修费用审核
			.state('repairRegisterProcess/telVisitBack',{
				url: '/office/repairRegisterProcess/telVisitBack/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/repairregister/telVisitBack/repairregisterVB.html',
				controller: "repairregisterVBCtrl"
			})
			//办结工作
			.state('end_repairRegisterProcess', {
				url: '/office/end_repairRegisterProcess/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/repairregister/end_repairregister.html',
				controller: "end_repairregisterCtrl"
			})
	});
			