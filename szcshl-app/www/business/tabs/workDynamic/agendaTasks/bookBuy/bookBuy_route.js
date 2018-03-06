//个人中心功能路由
angular.module('route.bookBuy', [
	'bookBuy.controller',
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('bookBuy', {
				url: '/workDynamic/agendaTasks/bookBuy/:id/:processInstanceId/:taskId',
				cache:'false',
				templateUrl: 'business/tabs/workDynamic/agendaTasks/bookBuy/bookBuy.html',
				controller: "bookBuyCtrl"

			})
			
	});
			