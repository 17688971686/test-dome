//个人中心功能路由
angular.module('route.agendaTasks', [
	'agendaTasks.controller',
	'route.bookBuy',//图书任务处理
	'route.monthly',//月报任务处理
	'route.annount',//通知公告任务处理
	'route.suppletter',//拟补充资料任务处理
	'route.appraise',//优秀评审报告任务处理
	'route.projectStop',//项目暂停任务处理
	'route.topic',//课题研究任务处理
	'route.assertStorage',//资产入库任务处理
	'route.archives',//档案借阅任务处理
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('agendaTasks', {
				url: '/workDynamic/agendaTasks',
				cache:'false',
				templateUrl: 'business/tabs/workDynamic/agendaTasks/agendaTasks.html',
				controller: "agendaTasksCtrl"

			})
			
	});
			