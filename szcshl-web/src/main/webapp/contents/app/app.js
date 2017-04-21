(function () {
    'use strict';

    angular.module('app', [
        // Angular modules 
       "ui.router",
       "kendo.directives"
       
        // Custom modules 

        // 3rd Party Modules

    ]).config(["$stateProvider", "$urlRouterProvider", function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise('/');
        $stateProvider
             .state('index', {
                 url: '/',
                 templateUrl:  rootPath + '/admin/welcome.html',
                 controller: 'roleCtrl',
                 controllerAs: 'vm'
             })
             //begin#role
            .state('role', {
                url: '/role',
                templateUrl: rootPath + '/role/html/list.html',
                controller: 'roleCtrl',
                controllerAs: 'vm'
            })
            .state('roleEdit', {
                url: '/roleEdit/:id',
                templateUrl: rootPath + '/role/html/edit.html',
                controller: 'roleEditCtrl',
                controllerAs: 'vm'
            })
        	//end#role
        	
        	//begin#user
	        .state('user', {
	            url: '/user',
	            templateUrl: rootPath + '/user/html/list.html',
	            controller: 'userCtrl',
	            controllerAs: 'vm'
	        }) .state('userEdit', {
	            url: '/userEdit/:id',
	            templateUrl: rootPath + '/user/html/edit.html',
	            controller: 'userEditCtrl',
	            controllerAs: 'vm'
	        })
        	//end#user
	        
	        //begin#org
	        .state('org', {
	            url: '/org',
	            templateUrl: rootPath + '/org/html/list.html',
	            controller: 'orgCtrl',
	            controllerAs: 'vm'
	        }) .state('orgEdit', {
	            url: '/orgEdit/:id',
	            templateUrl: rootPath + '/org/html/edit.html',
	            controller: 'orgEditCtrl',
	            controllerAs: 'vm'
	        }).state('orgUser', {
	            url: '/orgUser/:id',
	            templateUrl: rootPath + '/org/html/orgUser.html',
	            controller: 'orgUserCtrl',
	            controllerAs: 'vm'
	        })
	        //end#org
	        
	        //begin#log
	        .state('log', {
	            url: '/log',
	            templateUrl: rootPath + '/log/html/list.html',
	            controller: 'logCtrl',
	            controllerAs: 'vm'
	        }) 
	        //end#log
	        
	        //begin#meeting
	        .state('meeting', {
	            url: '/meeting',
	            templateUrl: rootPath + '/meeting/html/list.html',
	            controller: 'meetingCtrl',
	            controllerAs: 'vm'
	        }).state('meetingEdit', {
	            url: '/meetingEdit/:id',
	            templateUrl: rootPath + '/meeting/html/edit.html',
	            controller: 'meetingEditCtrl',
	            controllerAs: 'vm'
	        }) 
	        //end#meeting
	        
	        //begin#room
	        .state('room', {
	            url: '/room',
	            templateUrl: rootPath + '/room/html/roomlist.html',
	            controller: 'roomCtrl',
	            controllerAs: 'vm'
	        }).state('roomCount', {
	            url: '/roomCount/:id',
	            templateUrl: rootPath + '/room/html/countlist.html',
	            controller: 'roomCountCtrl',
	            controllerAs: 'vm'
	        }) 
	        //end#room
	        
	        //begin#company
	        .state('company', {
	            url: '/company',
	            templateUrl: rootPath + '/company/html/list.html',
	            controller: 'companyCtrl',
	            controllerAs: 'vm'
	        }).state('companyEdit', {
	            url: '/companyEdit/:id',
	            templateUrl: rootPath + '/company/html/edit.html',
	            controller: 'companyEditCtrl',
	            controllerAs: 'vm'
	        }) 
	        //end#company
	        
            //begin#home
	        .state('accountPwd', {
	            url: '/accountPwd',
	            templateUrl: rootPath + '/account/html/changePwd.html',
	            controller: 'homeCtrl',
	            controllerAs: 'vm'
	        }) 
	        //end#home
	         //begin#home
	        .state('demo', {
	            url: '/demo',
	            templateUrl: rootPath + '/demo/html/list.html',
	            controller: 'demoCtrl',
	            controllerAs: 'vm'
	        }) 
	          //end#home
	        //beginDict
	        .state('dict', {
	            url: '/dict',
	            templateUrl: rootPath + '/dict/html/list.html',
	            controller: 'dictCtrl',
	            controllerAs: 'vm'
	        })
	        .state('dict.edit', {
	            url: '/edit/:id',
	            templateUrl: rootPath + '/dict/html/edit.html',
	            controller: 'dictEditCtrl',
	            controllerAs: 'vm'
	        })
        	//endDict
	        
	        //begin#sign
	        .state('addSign', {
	            url: '/addSign',
	            templateUrl: rootPath + '/sign/html/add.html',
	            controller: 'signCreateCtrl',
	            controllerAs: 'vm'
	        }).state('fillSign', {
	        	url: '/fillSign/:signid',
	            templateUrl: rootPath + '/sign/html/fillin.html',
	            controller: 'signFillinCtrl',
	            controllerAs: 'vm'
	        }).state('listSign', {
	            url: '/listSign',
	            templateUrl: rootPath + '/sign/html/list.html',
	            controller: 'signCtrl',
	            controllerAs: 'vm'
	        }).state('flowSign', {
	            url: '/flowSign',
	            templateUrl: rootPath + '/sign/html/flow.html',
	            controller: 'signFlowCtrl',
	            controllerAs: 'vm'
	        }).state('flowDeal', {
                url: '/flowDeal/:signid/:taskId/:processInstanceId',
                templateUrl: rootPath + '/sign/html/flowDeal.html',
                controller: 'signFlowDealCtrl',
                controllerAs: 'vm'
            });
        	//end#sign
    }]);
    
})();