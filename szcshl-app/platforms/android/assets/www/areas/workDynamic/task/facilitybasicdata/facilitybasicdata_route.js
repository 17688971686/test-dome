angular.module('route.facilitybasicdata', ['facilitybasicdata.controller'])
	.config(function($stateProvider) {

		$stateProvider
			.state('facilitybasicdata/addOrEdit', {
				url: '/office/facilitybasicdata/addOrEdit',
				cache:'false',
				templateUrl: 'areas/workDynamic/task/facilitybasicdata/addOrEdit.html',
				controller: "facilitybasicdataCtrl"
			})
	});
		