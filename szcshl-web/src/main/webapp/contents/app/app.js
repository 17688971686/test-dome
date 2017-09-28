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
            .state('agendaTasks', {
                url: '/agendaTasks',
                templateUrl: rootPath + '/admin/agendaTasks.html',
                controller: 'adminAgendaCtrl',
                controllerAs: 'vm'
            })
            .state('doingTasks', {
                url: '/doingTasks',
                templateUrl: rootPath + '/admin/doingTasks.html',
                controller: 'adminDoTaskCtrl',
                controllerAs: 'vm'
            })
            //begin#流程公共页面
            .state('flowDeal', {
                url: '/flowDeal/:businessKey/:processKey/:taskId/:instanceId',
                templateUrl: function($routeParams){return rootPath + '/flow/flowDeal/'+$routeParams.processKey+'.html';},
                controller: 'flowDealCtrl',
                controllerAs: 'vm'
            })
            .state('flowDetail', {
                url: '/flowDetail/:businessKey/:processKey/:taskId/:instanceId',
                templateUrl: function($routeParams){return rootPath + '/flow/flowDetail/'+$routeParams.processKey+'.html';},
                controller: 'flowDetailCtrl',
                controllerAs: 'vm'
            })
            .state('flowEnd', {
                url: '/flowEnd/:businessKey/:processKey/:instanceId',
                templateUrl: function($routeParams){return rootPath + '/flow/flowEnd/'+$routeParams.processKey+'.html';},
                controller: 'flowEndCtrl',
                controllerAs: 'vm'
            })
            //end#流程公共页面
            
             //S 拟补充资料函管理 
            
            //begin#添加拟补充资料函
            .state('addSupp', {
                url: '/addSupp/:businessId/:businessType',
                templateUrl: rootPath + '/addSuppLetter/edit.html',
                controller: 'addSuppLetterCtrl',
                controllerAs: 'vm'
            })//end#添加拟补充资料函
            
             //begin#拟补充资料函列表
            .state('addSuppletterList', {
                url: '/addSuppletterList/:businessId',
                templateUrl: rootPath + '/addSuppLetter/list.html',
                controller: 'addSuppLetterListCtrl',
                controllerAs: 'vm'
            })//end#拟补充资料函列表
            
            //begin#拟补充资料函查看流程详细页面
            .state('addSuppLetterDetail', {
                url: '/addSuppLetterDetail',
                templateUrl: rootPath + '/addSuppLetter/detail.html',
                controller: 'addSuppLetterEditCtrl',
                controllerAs: 'vm'
            })//end#拟补充资料函查看流程详细页面
            
            
            //begin#拟补充资料函查询
            .state('suppletterList', {
                url: '/suppletterList',
                templateUrl: rootPath + '/addSuppLetter/suppLetterList.html',
                controller: 'addSuppLetterQueryCtrl',
                controllerAs: 'vm'
            })//end#拟补充资料函查询
            
           
             //begin#拟补充资料函详细信息
            .state('querySuppLetterDetail', {
                url: '/querySuppLetterDetail/:id',
                templateUrl: rootPath + '/addSuppLetter/detail.html',
                controller: 'addSuppLetterQueryEditCtrl',
                controllerAs: 'vm'
            })//end#拟补充资料函详细信息
             
             //begin#拟补充资料函审批
            .state('suppLetterApproveList', {
                url: '/suppLetterApproveList',
                templateUrl: rootPath + '/addSuppLetter/suppLetterApproveList.html',
                controller: 'addSuppLetterQueryCtrl',
                controllerAs: 'vm'
            })//end#拟补充资料函审批
            
           
            //begin#拟补充资料函审批处理页面
            .state('suppLetterApproveEdit', {
                url: '/suppLetterApproveEdit/:id',
                templateUrl: rootPath + '/addSuppLetter/suppLetterApproveEdit.html',
                controller: 'addSuppLetterQueryEditCtrl',
                controllerAs: 'vm'
            })//end#拟补充资料函审批处理页面
            
            //E 拟补充资料函管理
            
             //begin#registerFile
            .state('registerFile', {
                url: '/registerFile/:businessId/',
                templateUrl: rootPath + '/addRegisterFile/list.html',
                controller: 'addRegisterFileCtrl',
                controllerAs: 'vm'
            }) //end#registerFile
            .state('dtasks', {
                url: '/dtasks',
                templateUrl: rootPath + '/admin/dtasks.html',
                controller: 'adminDoingCtrl',
                controllerAs: 'vm'
            })
            .state('personDtasks', {
                url: '/personDtasks',
                templateUrl: rootPath + '/admin/personDtasks.html',
                controller: 'adminPersonDoingCtrl',
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

            //begin#sysdept
            .state('sysdept', {
                url: '/sysdept',
                templateUrl: rootPath + '/sysdept/html/list.html',
                controller: 'sysdeptCtrl',
                controllerAs: 'vm'
            }).state('sysdeptEdit', {
                url: '/sysdeptEdit/:id',
                templateUrl: rootPath + '/sysdept/html/edit.html',
                controller: 'sysdeptEditCtrl',
                controllerAs: 'vm'
            }).state('sysdeptUser', {
                url: '/sysdeptUser/:id',
                templateUrl: rootPath + '/sysdept/html/sysdeptUser.html',
                controller: 'sysdeptUserCtrl',
                controllerAs: 'vm'
            })
            //end#sysdept

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
                url: '/room/:businessId/:businessType',
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
                url: '/expertReview/:businessId/:minBusinessId/:businessType',
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
                templateUrl: rootPath + '/signView/html/signList.html',
                controller: 'adminSignListCtrl',
                controllerAs: 'vm'
            })//end#signList
            .state('selectHeader',{
                url : '/selectHeader',
                templateUrl : rootPath + '/sign/html/selectHeader.html',
                controller : 'selectHeaderCtrl',
                controllerAs : 'vm'

            })
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
                templateUrl: rootPath + '/annountment/html/flowDetail.html',
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
                templateUrl: rootPath + '/sharingPlatlform/html/flowDetail.html',
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
             //S 项目费用管理
            //评审费录入页面
             .state('financialManager', {
                url: '/financialManager/:signid',
                templateUrl: rootPath + '/financialManager/html/add.html',
                controller: 'financialManagerCtrl',
                controllerAs: 'vm'
            })
            //查看评审费发放表
            .state('findStageCostTable', {
                url: '/findStageCostTable/:signid',
                templateUrl: rootPath + '/financialManager/html/stageCostTable.html',
                controller: 'financialManagerEditCtrl',
                controllerAs: 'vm'
            })
            //查看协审费发放表
            .state('findAssistCostTable', {
                url: '/findAssistCostTable/:signid',
                templateUrl: rootPath + '/financialManager/html/stageCostTable.html',
                controller: 'assistCostCountCtrl',
                controllerAs: 'vm'
            })
            //协审费录入页面
            .state('assistCostAdd', {
                url: '/assistCostAdd/:signid',
                templateUrl: rootPath + '/financialManager/html/assistCostAdd.html',
                controller: 'assistCostCountCtrl',
                controllerAs: 'vm'
            })
            //协审费统计列表
             .state('assistCostCountList', {
                url: '/assistCostCountList',
                templateUrl: rootPath + '/financialManager/html/assistCostCount.html',
                controller: 'assistCostCountSvcEditCtrl',
                controllerAs: 'vm'
            })
            //评审费统计列表
             .state('financialManagerList', {
                url: '/financialManagerList',
                templateUrl: rootPath + '/financialManager/html/list.html',
                controller: 'financialManagerEditCtrl',
                controllerAs: 'vm'
            })
            
            //专家缴费统计列表
            .state('expertPaymentCountList', {
                url: '/expertPaymentCountList/:beginTime',
                templateUrl: rootPath + '/financialManager/html/expertPaymentCount.html',
                controller: 'expertPaymentCountCtrl',
                controllerAs: 'vm'
            })
            //专家缴费明细统计列表
            .state('expertPaymentDetailCountList', {
                url: '/expertPaymentDetailCountList/:beginTime',
                templateUrl: rootPath + '/financialManager/html/expertPaymentDetailCount.html',
                controller: 'expertPaymentDetailCountCtrl',
                controllerAs: 'vm'
            })
            //专家费统计列表
             .state('exportCountList', {
                url: '/exportCountList',
                templateUrl: rootPath + '/financialManager/html/expertCount.html',
                controller: 'exportCountCtrl',
                controllerAs: 'vm'
            })
             //E 项目费用管理

             //S 月报简报管理
             //月报简报管理列表
             .state('monthlyNewsletterList', {
                url: '/monthlyNewsletterList',
                templateUrl: rootPath + '/monthlyNewsletter/html/monthlyNewsletterList.html',
                controller: 'monthlyNewsletterCtrl',
                controllerAs: 'vm'
            })
            //新建月报简报管理
            .state('monthlyEdit', {
                url: '/monthlyEdit/:id',
                templateUrl: rootPath + '/monthlyNewsletter/html/monthlyNewsletterEdit.html',
                controller: 'monthlyNewsletterEditCtrl',
                controllerAs: 'vm'
            })

             //月报管理列表
             .state('theMonthsList', {
                url: '/theMonthsList',
                templateUrl: rootPath + '/monthlyNewsletter/html/theMonthsList.html',
                controller: 'monthlyNewsletterCtrl',
                controllerAs: 'vm'
            })
             //根据年度查找列表页面
             .state('monthlyFindByMultiyear', {
                url: '/monthlyFindByMultiyear/:reportMultiyear',
                templateUrl: rootPath + '/monthlyNewsletter/html/monthlyMultiyearList.html',
                controller: 'monthlyMultiyearCtrl',
                controllerAs: 'vm'
            })
            //新建年度月报简报页面
             .state('monthlyMultiyearEdit', {
                url: '/monthlyMultiyearEdit/:id',
                templateUrl: rootPath + '/monthlyNewsletter/html/monthlyMultiyearAdd.html',
                controller: 'monthlyMultiyearEditCtrl',
                controllerAs: 'vm'
            })
             //上传附件页面
             .state('uploadMonthly', {
                url: '/uploadMonthly/:id',
                templateUrl: rootPath + '/monthlyNewsletter/html/monthlyUpload.html',
                controller: 'monthlyMultiyearEditCtrl',
                controllerAs: 'vm'
            })
            

            //月报简报历史数据列表
             .state('monthlyHistoryList', {
                url: '/monthlyHistoryList',
                templateUrl: rootPath + '/monthlyNewsletter/html/monthlyHistoryList.html',
                controller: 'monthlyHistoryCtrl',
                controllerAs: 'vm'
            })
             //新建月报简报历史数据
            .state('monthlyHistoryEdit', {
                url: '/monthlyHistoryEdit/:id',
                templateUrl: rootPath + '/monthlyNewsletter/html/monthlyHistoryAdd.html',
                controller: 'monthlyHistoryEditCtrl',
                controllerAs: 'vm'
            })
            //优秀评审报告列表
            .state('monthlyExcellentList', {
                url: '/monthlyExcellentList/:id',
                templateUrl: rootPath + '/monthlyNewsletter/html/monthlyExcellentList.html',
                controller: 'monthlyExcellentCtrl',
                controllerAs: 'vm'
            })
            
            //E 月报简报管理

            //S 档案借阅管理

            //中心档案录入页面
             .state('libraryAdd', {
                url: '/libraryAdd/:id',
                templateUrl: rootPath + '/archivesLibrary/html/archivesLibraryAdd.html',
                controller: 'archivesLibraryCtrl',
                controllerAs: 'vm'
            })

            //市档案借阅录入页面
             .state('archivesCityEdit', {
                url: '/archivesCityEdit/:id',
                templateUrl: rootPath + '/archivesLibrary/html/archivesCityEdit.html',
                controller: 'archivesLibraryCtrl',
                controllerAs: 'vm'
            })
            //项目查阅审批列表
            .state('archivesProjectRead', {
                url: '/archivesProjectRead/:id',
                templateUrl: rootPath + '/archivesLibrary/html/archivesProjectList.html',
                controller: 'archivesLibraryCtrl',
                controllerAs: 'vm'
            })
            //部长审批处理页面
            .state('archivesLibraryProjectEdit', {
                url: '/archivesLibraryProjectEdit/:id',
                templateUrl: rootPath + '/archivesLibrary/html/archivesLibraryEdit.html',
                controller: 'archivesLibraryEditCtrl',
                controllerAs: 'vm'
            })


            //中心档案查询
            .state('archivesLibraryList', {
                url: '/archivesLibraryList/:id',
                templateUrl: rootPath + '/archivesLibrary/html/archivesLibraryList.html',
                controller: 'archivesLibraryCtrl',
                controllerAs: 'vm'
            })
            //查看中心档案详细
            .state('archivesLibraryDetail', {
                url: '/archivesLibraryDetail/:id',
                templateUrl: rootPath + '/archivesLibrary/html/archivesLibraryDetail.html',
                controller: 'archivesLibraryEditCtrl',
                controllerAs: 'vm'
            })

            //市档案查询
            .state('archivesCityList', {
                url: '/archivesCityList/:id',
                templateUrl: rootPath + '/archivesLibrary/html/archivesCityList.html',
                controller: 'archivesLibraryCtrl',
                controllerAs: 'vm'
            })


            //E 档案借阅管理
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
            })
            //个人中心
            .state('takeUser',{
                url:'/takeUser',
                templateUrl: rootPath + '/personalCenter/html/takeUser.html',
                controller: 'takeUserCtrl',
                controllerAs: 'vm'
            })
            //质量管理文件库
            .state('fileLibrary',{
                url : '/fileLibrary',
                templateUrl : rootPath + '/fileLibrary/html/fileLibrary.html',
                controller : 'fileLibraryCtrl',
                controllerAs : 'vm'
            })
            .state('fileLibrary.fileList',{ //文件列表
                url : '/fileList/:parentId/:fileId',
                templateUrl : rootPath + '/fileLibrary/html/fileList.html',
                controller : 'fileLibraryEditCtrl',
                controllerAs : 'vm'
            })
            .state('fileLibrary.fileEdit',{//新建文件
                url : '/fileEdit/:parentId/:fileId',
                templateUrl : rootPath + '/fileLibrary/html/fileEdit.html',
                controller : 'fileLibraryEditCtrl',
                controllerAs : 'vm'
            })
            //政策标准库
            .state('policyLibrary',{
                url : '/policyLibrary',
                templateUrl : rootPath + '/fileLibrary/html/policyLibrary.html',
                controller : 'policyLibraryCtrl',
                controllerAs : 'vm'
            })
            .state('policyLibrary.policyList',{ //文件列表
                url : '/policyList/:parentId/:fileId',
                templateUrl : rootPath + '/fileLibrary/html/policyList.html',
                controller : 'policyLibraryEditCtrl',
                controllerAs : 'vm'
            })
            .state('policyLibrary.policyEdit',{//新建文件
                url : '/policyEdit/:parentId/:fileId',
                templateUrl : rootPath + '/fileLibrary/html/policyEdit.html',
                controller : 'policyLibraryEditCtrl',
                controllerAs : 'vm'
            })
            //图书采购流程
            .state('bookBuyBusinessEdit', {
                url: '/bookBuyBusinessEdit/:businessId',
                templateUrl: rootPath + '/bookBuyBusiness/html/bookBuyBusinessEdit.html',
                controller: 'bookBuyBusinessEditCtrl',
                controllerAs: 'vm'
            })
            .state('myBookBuyBusiness',{
                url : '/myBookBuyBusiness',
                templateUrl : rootPath + '/bookBuyBusiness/html/bookBuyBusinessList.html',
                controller : 'bookBuyBusinessCtrl',
                controllerAs : 'vm'
            })
            //固定资产申购流程
            .state('assertStorageBusinessEdit', {
                url: '/assertStorageBusinessEdit/:businessId',
                templateUrl: rootPath + '/assertStorageBusiness/html/assertStorageBusinessEdit.html',
                controller: 'assertStorageBusinessEditCtrl',
                controllerAs: 'vm'
            })
            .state('myAssertStorageBusiness',{
            url : '/myAssertStorageBusiness',
            templateUrl : rootPath + '/assertStorageBusiness/html/assertStorageBusinessList.html',
            controller : 'assertStorageBusinessCtrl',
            controllerAs : 'vm'
        })
            .state('assertApplyUse',{
                url : '/assertApplyUse',
                templateUrl : rootPath + '/userAssertDetail/html/userAssertDetailAdd.html',
                controller : 'userAssertDetailAddCtrl',
                controllerAs : 'vm'
            })
            //课题研究流程
            .state('addTopic',{
                url : '/topicInfo/:id',
                templateUrl : rootPath + '/topicInfo/html/add.html',
                controller : 'topicAddCtrl',
                controllerAs : 'vm'
            })
            .state('myTopic',{
                url : '/myTopic',
                templateUrl : rootPath + '/topicInfo/html/myList.html',
                controller : 'myTopicCtrl',
                controllerAs : 'vm'
            })
                //表头设置
            .state('header',{
                url : '/header',
                templateUrl : rootPath + '/header/html/list.html',
                controller : 'headerCtrl',
                controllerAs: 'vm'
            })
            .state('headerEdit',{
                url : '/headerEdit/:headerType',
                templateUrl : rootPath + '/header/html/selectHeader.html',
                controller : 'headerEditCtrl',
                controllerAs: 'vm'
            })
            .state('statisticalList',{
                url : '/statisticalList/:headerType',
                templateUrl : rootPath + '/header/html/statisticalList.html',
                controller : 'statisticalListCtrl',
                controllerAs: 'vm'
            })
        //统计图表
            .state('statistical',{
                url : '/statistical',
                templateUrl : rootPath + "/statistical/html/list.html",
                controller : 'statisticalCtrl',
                controllerAs : 'vm'
            })
            .state('editWorkPlan',{
                url : '/editWorkPlan/:topicId',
                templateUrl : rootPath + '/workPlan/html/edit.html',
                controller : 'workPlanEditCtrl',
                controllerAs : 'vm'
            })
            .state('editFiling',{
                url : '/editFiling/:topicId',
                templateUrl : rootPath + '/filing/html/edit.html',
                controller : 'filingEditCtrl',
                controllerAs : 'vm'
            })

        ;
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
            if(fromState.name == 'signFlowDeal' || fromState.name == 'flowDeal'){
                $rootScope.$flowUrl = fromState.name;
                $rootScope.$flowParams = fromParams;
            }
        });

        $rootScope.back = function () {
        	if($rootScope.previousState_name ){
        		$state.go($rootScope.previousState_name, $rootScope.previousState_params);
        	}else{
                $state.go('welcome');
        	}           
        };
        $rootScope.backtoflow = function () {
            if($rootScope.$flowUrl ){
                $state.go($rootScope.$flowUrl, $rootScope.$flowParams);
            }else{
                $state.go('gtasks');
            }
        };
    	
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

        //S_初始化input框的值
        $rootScope.initInputValue = function($event,defaultValue){
            var checkbox = $event.target;
            var checked = checkbox.checked;
            if (checked && !defaultValue) {
                return 1;
            }else{
                return defaultValue;
            }
        }//E_initInputValue

        //用于循环数字用
        $rootScope.range = function(n) {
            if(n){
                return new Array(n);
            }
        }

        //kendo 语言
        kendo.culture("zh-CN");
        common.getTaskCount({$http: $http});
    	common.initDictData({$http: $http, scope: $rootScope});
    });

})();
