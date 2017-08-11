(function () {
    'use strict';
    angular.module('app', [
        // Angular modules 
        "ui.router",
        "kendo.directives",
        'angular-loading-bar',
        'ngAnimate',
        'froala'
        ]).filter('trust2Html', ['$sce',function($sce) {
            return function(val) {
                return $sce.trustAsHtml(val);
            };
        }])
        .config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
            cfpLoadingBarProvider.parentSelector = '#loading-bar-container';
            cfpLoadingBarProvider.spinnerTemplate = '<div style="position:fixed;width:100%;height:100%;left:0;top:0; z-index:99;background:rgba(0, 0, 0, 0.3);overflow: hidden;"><div style="position: absolute;top:30%; width: 400px;height:40px;left:50%;"><i class="fa fa-spinner fa-pulse fa-1x fa-fw"></i>数据加载中...</div></div>';
        }])
        .config(["$stateProvider", "$urlRouterProvider", function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise('/welcome');
        $stateProvider
            .state('welcome', {
                url: '/welcome',
                templateUrl: rootPath + '/admin/welcome.html',
                controller: 'adminWelComeCtrl',
                controllerAs: 'vm'
            })
            .state('gtasks', {
                url: '/gtasks',
                templateUrl: rootPath + '/admin/gtasks.html',
                controller: 'adminCtrl',
                controllerAs: 'vm'
            })
             //begin#addSuppletter
            .state('addSupp', {
                url: '/addSupp/:signid',
                templateUrl: rootPath + '/addSuppLetter/edit.html',
                controller: 'suppletterCtrl',
                controllerAs: 'vm'
            })//end#addSuppletter
             //begin#registerFile
            .state('registerFile', {
                url: '/registerFile/:signid/:id',
                templateUrl: rootPath + '/addRegisterFile/list.html',
                controller: 'registerFileCtrl',
                controllerAs: 'vm'
            }) //end#registerFile
            .state('dtasks', {
                url: '/dtasks',
                templateUrl: rootPath + '/admin/dtasks.html',
                controller: 'adminDoingCtrl',
                controllerAs: 'vm'
            })
            .state('etasks', {
                url: '/etasks',
                templateUrl: rootPath + '/admin/etasks.html',
                controller: 'adminEndCtrl',
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
            }).state('userEdit', {
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
            }).state('orgEdit', {
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

            //begin#config
            .state('config', {
                url: '/config',
                templateUrl: rootPath + '/sysConfig/html/list.html',
                controller: 'sysConfigCtrl',
                controllerAs: 'vm'
            })
            //end#config
            
            //begin#upload
        	.state('upload', {
        		url: '/upload/:uploadid',
        		templateUrl: rootPath + '/upload/html/edit.html',
        		controller: 'uploadEditCtrl',
        		controllerAs: 'vm'
        	})
        	//end#upload
        	
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
                url: '/room/:workProgramId',
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
            //begin#demo
            .state('demo', {
                url: '/demo',
                templateUrl: rootPath + '/demo/html/list.html',
                controller: 'demoCtrl',
                controllerAs: 'vm'
            })
            //end#demo
            //begin Dict
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
            //end Dict
            
            //begin expert
            .state('expert', {
            	url: '/expert',
                templateUrl: rootPath + '/expert/html/queryAllList.html',
                controller: 'expertCtrl',
                controllerAs: 'vm'
            })            
            .state('expertAudit', {
            	url: '/expertAudit',
                templateUrl: rootPath + '/expert/html/audit.html',
                controller: 'expertAuditCtrl',
                controllerAs: 'vm'
            })
            .state('expertRepeat', {
            	url: '/expertRepeat',
                templateUrl: rootPath + '/expert/html/repeat.html',
                controller: 'expertRepeatCtrl',
                controllerAs: 'vm'
            })            
            .state('expertEdit', {
                url: '/expertEdit/:expertID',
                templateUrl: rootPath + '/expert/html/edit.html',
                controller: 'expertEditCtrl',
                controllerAs: 'vm'
            })
            .state('expertReviewEdit', {
                url: '/expertReview/:workProgramId',
                templateUrl: rootPath + '/expertReview/html/selectExpert.html',
                controller: 'expertSelectCtrl',
                controllerAs: 'vm'
            })
            .state('expertReviewList',{
                url: '/expertReviewList',
                templateUrl: rootPath + '/expert/html/reviewList.html',
                controller: 'expertReviewListCtrl',
                controllerAs: 'vm'
            })
            //end expert
            
            //begin#sign
            .state('addSign', {
                url: '/addSign',
                templateUrl: rootPath + '/sign/html/add.html',
                controller: 'signCreateCtrl',
                controllerAs: 'vm'
            }).state('fillSign', {
                url: '/fillSign/:signid',
                cache:'false',
                templateUrl: rootPath + '/sign/html/fillin.html',
                controller: 'signFillinCtrl',
                controllerAs: 'vm'
            }).state('listSign', {
                url: '/listSign',
                templateUrl: rootPath + '/sign/html/list.html',
                controller: 'signCtrl',
                controllerAs: 'vm'
            }).state('signFlowDeal', {
                url: '/signFlowDeal/:signid/:taskId/:processInstanceId',
                templateUrl: rootPath + '/sign/html/flowDeal.html',
                controller: 'signFlowDealCtrl',
                controllerAs: 'vm'
            }).state('signFlowDetail', {
                url: '/signFlowDetail/:signid/:taskId/:processInstanceId',
                templateUrl: rootPath + '/sign/html/signFlowDetail.html',
                controller: 'signFlowDetailCtrl',
                controllerAs: 'vm'
            }).state('signDetails', {//详细信息
                url: '/signDetails/:signid/:processInstanceId',
                templateUrl: rootPath + '/sign/html/signDetails.html',
                controller: 'signDetailsCtrl',
                controllerAs: 'vm'
            }).state('endSignDetail', { //已经办结的详情信息
                url: '/endSignDetail/:signid/:processInstanceId',
                templateUrl: rootPath + '/sign/html/signEndDetails.html',
                controller: 'signEndCtrl',
                controllerAs: 'vm'
            }).state('signList', { //项目查询统计
                url: '/signList',
                templateUrl: rootPath + '/sign/html/signList.html',
                controller: 'adminSignListCtrl',
                controllerAs: 'vm'
            })//end#signList
            .state('pauseProject', { //项目暂停审批
                url: '/pauseProject',
                templateUrl: rootPath + '/sign/html/pauseProjectList.html',
                controller: 'pauseProjectCtrl',
                controllerAs: 'vm'
            })
            .state('reserveAdd', {	//新增预签收
                url: '/reserveAdd',
                templateUrl: rootPath + '/sign/html/reserveAdd.html',
                controller: 'signReserveAddCtrl',
                controllerAs: 'vm'
            }).state('reserveList', {	//预签收列表
                url: '/reserveList',
                templateUrl: rootPath + '/sign/html/reserveList.html',
                controller: 'signReserveCtrl',
                controllerAs: 'vm'
            }).state('reserveEdit', {	//预签收审批登记表
                url: '/reserveEdit',
                templateUrl: rootPath + '/sign/html/reserveList.html',
                controller: 'signReserveCtrl',
                controllerAs: 'vm'
            })
            //end#signList

            //begin#workprogram
            .state('workprogramEdit', {
                url: '/workprogramEdit/:signid',
                templateUrl: rootPath + '/workprogram/html/edit.html',
                controller: 'workprogramEditCtrl',
                controllerAs: 'vm'
            })
            //end#workprogram

            //begin#dispatch
            .state('dispatchEdit', {
                url: '/dispatchEdit/:signid',
                templateUrl: rootPath + '/dispatch/html/edit.html',
                controller: 'dispatchEditCtrl',
                controllerAs: 'vm'
            })//end#dispatch

            //begin#fileRecord
        	.state('fileRecordEdit', {
            	url: '/fileRecordEdit/:signid',
            	templateUrl: rootPath + '/fileRecord/html/edit.html',
            	controller: 'fileRecordEditCtrl',
            	controllerAs: 'vm'
        	})
        	//end#fileRecord
            //begin#assistMng
            .state('assistPlan', {
                url: '/assistPlan',
                templateUrl: rootPath + '/assistPlan/html/manager.html',
                controller: 'assistPlanCtrl',
                controllerAs: 'vm'
            })
            //begin#assistMng

        	//start #officeUser
        	.state('officeUserList', {
                url: '/officeUserList',
                templateUrl: rootPath + '/officeUser/html/list.html',
                controller: 'officeUserCtrl',
                controllerAs: 'vm'
            })
        	.state('officeUserEdit', {
            	url: '/officeUserEdit/:officeID',
            	templateUrl: rootPath + '/officeUser/html/edit.html',
            	controller: 'officeUserEditCtrl',
            	controllerAs: 'vm'
        	})
        	// end #officeUser
        	
        	//begin#dept
        	.state('listDept', {
                url: '/listDept',
                templateUrl: rootPath + '/dept/html/list.html',
                controller: 'deptCtrl',
                controllerAs: 'vm'
            })
        	.state('deptEdit', {
            	url: '/deptEdit/:deptId',
            	templateUrl: rootPath + '/dept/html/edit.html',
            	controller: 'deptEditCtrl',
            	controllerAs: 'vm'
        	}).state('deptOfficeUser', {
                url: '/deptOfficeUser/:deptId',
                templateUrl: rootPath + '/dept/html/listOfficeUser.html',
                controller: 'deptOfficeUserCtrl',
                controllerAs: 'vm'
            })
        	//end#dept
            //begin#assistUnit
            .state('assistUnit', {
                url: '/assistUnit',
                templateUrl: rootPath + '/assistUnit/html/assistUnitList.html',
                controller: 'assistUnitCtrl',
                controllerAs: 'vm'
            }).state('assistUnitEdit', {
                url: '/assistUnitEdit/:id',
                templateUrl: rootPath + '/assistUnit/html/assistUnitEdit.html',
                controller: 'assistUnitEditCtrl',
                controllerAs: 'vm'
            })
            //end#assistUnit
            //begin#assistUnit
            .state('quartz', {
                url: '/quartz',
                templateUrl: rootPath + '/quartz/html/list.html',
                controller: 'quartzCtrl',
                controllerAs: 'vm'
            })
            //begin workday
             .state('workday', {
                url: '/workday',
                templateUrl: rootPath + '/workday/html/list.html',
                controller: 'workdayCtrl',
                controllerAs: 'vm'
            })
             .state('workdayEdit', {
                url: '/workdayEdit/:id',
                templateUrl: rootPath + '/workday/html/edit.html',
                controller: 'workdayEditCtrl',
                controllerAs: 'vm'
            })
            //通过公告
            .state('annountment', {
                url: '/annountment',
                templateUrl: rootPath + '/annountment/html/list.html',
                controller: 'annountmentCtrl',
                controllerAs: 'vm'
            })
            .state('annountmentEdit', {
                url: '/annountmentEdit/:id',
                templateUrl: rootPath + '/annountment/html/edit.html',
                controller: 'annountmentEditCtrl',
                controllerAs: 'vm'
            })
             //通知公告详情页
             .state('annountmentDetail', {
                url: '/annountmentDetail/:id',
                templateUrl: rootPath + '/annountment/html/detail.html',
                controller: 'annountmentDetailCtrl',
                controllerAs: 'vm'
            })
             //通过公告
            .state('annountmentYet', {
                url: '/annountmentYet',
                templateUrl: rootPath + '/annountment/html/yetList.html',
                controller: 'annountmentYetCtrl',
                controllerAs: 'vm'
            })
            //begin#sharing
            .state('sharingPlatlform', {
                url: '/sharingPlatlform',
                templateUrl: rootPath + '/sharingPlatlform/html/list.html',
                controller: 'sharingPlatlformCtrl',
                controllerAs: 'vm'
            }).state('sharingPlatlformEdit', {
                url: '/sharingPlatlformEdit/:sharId',
                templateUrl: rootPath + '/sharingPlatlform/html/edit.html',
                controller: 'sharingPlatlformEditCtrl',
                controllerAs: 'vm'
            })
             //资料共享详情页
             .state('sharingDetil', {
                url: '/sharingDetil/:sharId',
                templateUrl: rootPath + '/sharingPlatlform/html/detail.html',
                controller: 'sharingDetailCtrl',
                controllerAs: 'vm'
            })
             .state('sharingPlatlformYet', {
                url: '/sharingPlatlformYet',
                templateUrl: rootPath + '/sharingPlatlform/html/yetList.html',
                controller: 'sharingPlatlformYetCtrl',
                controllerAs: 'vm'
            })
            //end#sharing
             //S 财务管理
             .state('financialManager', {
                url: '/financialManager/:signid',
                templateUrl: rootPath + '/financialManager/html/add.html',
                controller: 'financialManagerCtrl',
                controllerAs: 'vm'
            })
              //begin#dispatch
           /* .state('financialEdit', {
                url: '/financialEdit/:signid',
                templateUrl: rootPath + '/financialManager/html/addFinancial.html',
                controller: 'financialManagerEditCtrl',
                controllerAs: 'vm'
            })*///end#财务管理
            //end#financial
            //系统安装包管理
            .state('pluginfile',{
                url: '/pluginfile',
                templateUrl: rootPath + '/file/html/pluginfile.html',
                controller: 'pluginfileCtrl',
                controllerAs: 'vm'
            });
    }]).run(function ($rootScope, $http, $state, $stateParams) {
        //获取表头名称
        $rootScope.getTBHeadName = function(stageName,isAdvanced,type){
            //项目建议书、可行性  提前介入称为评估论证
            if(isAdvanced && isAdvanced == '9' && (stageName == '项目建议书' || stageName == '可行性研究报告')){
                return "评估论证" + type;
            }else{
                if(stageName){
                    if(stageName == '项目概算'){
                        return "概算审核"+type;
                    }
                    return stageName + type;
                }else{
                    return type;
                }

            }
        }

        //实现返回的函数
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;
        $rootScope.$on("$stateChangeSuccess", function (event, toState, toParams, fromState, fromParams) {
            $rootScope.previousState_name = fromState.name;
            $rootScope.previousState_params = fromParams;
        });

        $rootScope.back = function () {
        	if($rootScope.previousState_name ){
        		$state.go($rootScope.previousState_name, $rootScope.previousState_params);
        	}else{
        		window.history.back();
        	}           
        };

        //kendo 语言
    	kendo.culture("zh-CN");
    	
        $rootScope.topSelectChange = function (dictKey, dicts , type) {
        	if(dicts !=undefined){       		
	            for (var i = 0; i < dicts.length; i++) {
	            	//根据code查询
	            	if(type && type == "code"){
	            		
	            		if (dicts[i].dictCode == dictKey) {
	                        return dicts[i].dicts;
	                    }
	            	//默认根据name查询	
	            	}else{
	            		if (dicts[i].dictName == dictKey) {
	                        return dicts[i].dicts;
	                    }
	            	}               
	            }
        	}
        }      

        common.getTaskCount({$http: $http});
    	common.initDictData({$http: $http, scope: $rootScope});
    });

})();