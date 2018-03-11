//个人中心功能路由
angular.module('route.topic', [
	'topic.controller',
]).config(function($stateProvider, $urlRouterProvider) {
		$stateProvider

			.state('topic', {
				url: '/workDynamic/agendaTasks/topic/:id/:processInstanceId/:taskId/:backType',
				cache:'false',
				templateUrl: 'business/tabs/workDynamic/agendaTasks/topic/topic.html',
				controller: "topicCtrl"

			})
			
	});
			