//个人中心功能路由
angular.module('route.maintenanceProcess', ['maintenanceProcess.controller', 'newMaintenanceProce.controller', 'listMaintenanceProce.controller', 'maintenanceLA.controller','maintenanceLC.controller','maintenanceCC.controller','maintenanceCT.controller','maintenanceLAC.controller'])
	.config(function($stateProvider, $urlRouterProvider) {

		$stateProvider
			//文体设施报修
			.state('maintenanceProcess/doRepair', {
				url: '/office/maintenanceProcess/doRepair/:id/:taskId',
				cache:'false',
				templateUrl: 'areas/workDynamic/task/maintenance/maintenanceProcess.html',
				controller: "maintenanceProcessCtrl"
			})
			//领导分办
			.state('maintenanceProcess/leaderAllot', {
				url: '/office/maintenanceProcess/leaderAllot/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/maintenance/leaderAllot/maintenanceLA.html',
				controller: "maintenanceLACtrl"
			})
			//维修费用审核
			.state('maintenanceProcess/checkCost',{
				url: '/office/maintenanceProcess/checkCost/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/maintenance/checkCost/maintenanceCT.html',
				controller: "maintenanceCTCtrl"
			})
			//文体局确认
			.state('maintenanceProcess/leaderCheck',{
				url: '/office/maintenanceProcess/leaderCheck/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/maintenance/leaderCheck/maintenanceLC.html',
				controller: "maintenanceLCCtrl"
			})
			//社区确认
			.state('maintenanceProcess/checkComplete',{
				url: '/office/maintenanceProcess/checkComplete/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/maintenance/checkComplete/maintenanceCC.html',
				controller: "maintenanceCCCtrl"
			})
			//文体局再次确认
			.state('maintenanceProcess/leaderAgainCheck',{
				url: '/office/maintenanceProcess/leaderAgainCheck/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/maintenance/leaderAgainCheck/maintenanceLAC.html',
				controller: "maintenanceLACCtrl"
			})
			//已办结的文体设施报修流程
			.state('end_maintenanceProcess', {
				url: '/office/end_maintenanceProcess/:id/:taskId',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/maintenance/end_maintenanceProcess.html',
				controller: "end_maintenanceProcessCtrl"
			})
			//设施维护暂存列表
			.state('listMaintenanceProce', {
				url: '/office/listMaintenanceProce',
				cache:'false',
				templateUrl: 'areas/workDynamic/task/maintenance/list.html',
				controller: "listMaintenanceProceCtrl"
			})
			//新增文体设施维护流程
			.state('newMaintenanceProce', {
				url: '/office/newMaintenanceProce/:id',
				cache:'true',
				templateUrl: 'areas/workDynamic/task/maintenance/newMaintenanceProce.html',
				controller: "newMaintenanceProceCtrl"
			})
	});
			