//总路由
angular.module('route', [

	'route.login',//登录
	'route.personal',//个人中心
	'route.tabs',//选项卡
	'route.infrtureDibut',//地图应用
	'route.infrastructure',//基础设施
	'route.workDynamic',//工作动态
	'route.task',//任务
	'route.repairregister',//投诉报修
	'route.maintenanceProcess',//文体设施报修
	'route.facilityBasicProcess',//文体设施备注修改
	'route.inspectProcess',//巡查流程
	'route.facilitybasicdata',
	'route.repairTimeDelayApply'//维修延长
	])

    .config(function($urlRouterProvider) {
        // 路径不匹配，默认跳转登录页面
        $urlRouterProvider.otherwise('login');
    });
