//个人中心功能路由
angular.module('route.signFlowDeal', [
	'signFlowDeal.controller',
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('signFlowDeal/:signid/:processInstanceId', {
				url: '/workDynamic/gtasks/signFlowDeal/:signid/:processInstanceId/:taskId',
				cache:'false',
				templateUrl: 'business/tabs/workDynamic/gtasks/signFlowDeal/signFlowDeal.html',
				controller: "signFlowDealCtrl"

			})
			
	});
			