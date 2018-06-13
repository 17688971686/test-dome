(function () {
    'use strict';
    angular.module('app', [
        // Angular modules
        'ui.router',
        'kendo.directives',
        'angular-loading-bar',
        'ngAnimate',
        'ngFileSaver',
        'angular-toArrayFilter',
    ]).filter('trust2Html', ['$sce', function ($sce) {
        return function (val) {
            return $sce.trustAsHtml(val);
        };
    }]).filter('myFilter', function () {
        return function (collection, keyname, value) {
            var output = [];
            var valueArr = [];
            if (value instanceof Array) {
                valueArr = value;
            } else {
                valueArr.push(value);
            }
            angular.forEach(collection, function (item) {
                angular.forEach(valueArr, function (checkValue) {
                    //过滤数组中值与指定值相同的元素
                    if (item[keyname] == checkValue) {
                        output.push(item);
                    }
                })
            });
            return output;
        }
    }).filter('unique', function () {
        return function (collection, keyname) {
            var output = [],
                keys = [];
            angular.forEach(collection, function (item) {
                var key = item[keyname];
                if (keys.indexOf(key) === -1) {
                    keys.push(key);
                    output.push(item);
                }
            });
            return output;
        };
    }).filter('FormatStrDate', function () {
        return function (input) {
            var date = new Date(input);
            var monthValue = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1);
            var dayValue = (date.getDate()) < 10 ? "0" + (date.getDate()) : (date.getDate());
            var formatDate = date.getFullYear() + "/" + monthValue + "/" + dayValue;
            return formatDate;
        }
    })
        .config(["$stateProvider", "$locationProvider", "$urlRouterProvider", 'cfpLoadingBarProvider', function ($stateProvider, $locationProvider, $urlRouterProvider, cfpLoadingBarProvider) {
            $locationProvider.hashPrefix(''); // 1.6.x版本使用路由功能需加上这句
            cfpLoadingBarProvider.parentSelector = '#loading-bar-container';
            cfpLoadingBarProvider.spinnerTemplate = '<div style="position:fixed;width:100%;height:100%;left:0;top:0; z-index:10001;background:rgba(0, 0, 0, 0.3);overflow: hidden;"><div style="position: absolute;top:30%; width: 400px;height:40px;left:50%;"><i class="fa fa-spinner fa-pulse fa-1x fa-fw"></i>数据加载中...</div></div>';
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
                    templateUrl: function ($routeParams) {
                        return rootPath + '/flow/flowDeal/' + $routeParams.processKey + '.html';
                    },
                    controller: 'flowDealCtrl',
                    controllerAs: 'vm'
                })
                .state('flowDetail', {
                    url: '/flowDetail/:businessKey/:processKey/:taskId/:instanceId',
                    templateUrl: function ($routeParams) {
                        return rootPath + '/flow/flowDetail/' + $routeParams.processKey + '.html';
                    },
                    controller: 'flowDetailCtrl',
                    controllerAs: 'vm'
                })
                .state('flowEnd', {
                    url: '/flowEnd/:businessKey/:processKey/:instanceId',
                    templateUrl: function ($routeParams) {
                        return rootPath + '/flow/flowEnd/' + $routeParams.processKey + '.html';
                    },
                    controller: 'flowEndCtrl',
                    controllerAs: 'vm'
                })
                //end#流程公共页面

                //S 拟补充资料函管理

                //begin#添加拟补充资料函
                .state('addSupp', {
                    url: '/addSupp/:businessId/:businessType/:isControl',
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
                .state('addSuppLetterEdit', {
                    url: '/addSuppLetterEdit/:id',
                    templateUrl: rootPath + '/addSuppLetter/editUpload.html',
                    controller: 'addSuppLetterEditCtrl',
                    controllerAs: 'vm'
                })//end#拟补充资料函查看流程详细页面

                //begin#拟补充资料函查看流程详细页面
                .state('suppLetterView', {
                    url: '/suppLetterView/:id',
                    templateUrl: rootPath + '/addSuppLetter/view.html',
                    controller: 'suppLetterViewCtrl',
                    controllerAs: 'vm'
                })//end#拟补充资料函查看流程详细页面


                //begin#拟补充资料函查询
                .state('suppletterList', {
                    url: '/suppletterList',
                    templateUrl: rootPath + '/addSuppLetter/html/suppLetterList.html',
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
                    url: '/registerFile/:businessId',
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
                    templateUrl: rootPath + '/sign/personDtasks.html',
                    controller: 'adminPersonDoingCtrl',
                    controllerAs: 'vm'
                })
                .state('etasks', {
                    url: '/etasks',
                    templateUrl: rootPath + '/sign/etasks.html',
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
                    url: '/room',
                    params:{'businessId' : '' , 'businessType' : ''},
                    templateUrl: rootPath + '/room/html/roomlist.html',
                    controller: 'roomCtrl',
                    controllerAs: 'vm'
                }).state('roomCount', {
                    url: '/roomCount',
                    params:{'id' : ''},
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
                    url: '/expertEdit/:id',
                    templateUrl: rootPath + '/expert/html/edit.html',
                    controller: 'expertEditCtrl',
                    controllerAs: 'vm'
                })
                .state('expertReviewEdit', {
                    url: '/expertReview/:businessId/:minBusinessId/:businessType/:reviewType/:isback/:processInstanceId/:taskId',
                    templateUrl: rootPath + '/expertReview/html/selectExpert.html',
                    controller: 'expertSelectCtrl',
                    controllerAs: 'vm'
                })

                .state('expertScore', {
                    url: '/expertScore',
                    templateUrl: rootPath + '/expert/html/scoreList.html',
                    controller: 'expertScoreCtrl',
                    controllerAs: 'vm'
                })
                .state('expertSelectHis', {
                    url: '/expertSelectHis',
                    templateUrl: rootPath + '/expert/html/selectHisList.html',
                    controller: 'expertSelectHisCtrl',
                    controllerAs: 'vm'
                })
                //end expert
                //begin#sign
                .state('addSign', {
                    url: '/addSign',
                    templateUrl: rootPath + '/sign/html/add.html',
                    controller: 'signCreateCtrl',
                    controllerAs: 'vm'
                }).state('fillSign', {//isControl用来控制按钮的显示
                    url: '/fillSign/:signid/:isControl',
                    cache: 'false',
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
                .state('projectStopInfo', { //项目暂停表单（多个）
                    url: '/projectStopInfo/:signId',
                    templateUrl: rootPath + '/projectStop/html/projectStopInfo.html',
                    controller: 'projectStopInfoCtrl',
                    controllerAs: 'vm'
                })//end#signList
                .state('selectHeader', {
                    url: '/selectHeader',
                    templateUrl: rootPath + '/sign/html/selectHeader.html',
                    controller: 'selectHeaderCtrl',
                    controllerAs: 'vm'

                })
                .state('signGetBack', {//项目取回
                    url: '/signGetBack',
                    templateUrl: rootPath + '/sign/html/signGetBack.html',
                    controller: 'signGetBackCtrl',
                    controllerAs: 'vm'
                })
                .state('pauseProject', { //项目暂停审批
                    url: '/pauseProject',
                    templateUrl: rootPath + '/projectStop/html/pauseProjectList.html',
                    controller: 'pauseProjectCtrl',
                    controllerAs: 'vm'
                })
                .state('projectStopForm', { //项目暂停表单
                    url: '/projectStopForm/:signId/:stopId',
                    templateUrl: rootPath + '/projectStop/html/projectStopForm.html',
                    controller: 'projectStopFormCtrl',
                    controllerAs: 'vm'
                })
                .state('projectStopFormEdit', { //编辑项目暂停表单
                    url: '/projectStopFormEdit/:stopId',
                    templateUrl: rootPath + '/projectStop/html/projectStopForm.html',
                    controller: 'projectStopFormEditCtrl',
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
                }).state('deletList', {	//作废项目列表
                    url: '/deletList',
                    templateUrl: rootPath + '/maintainProject/html/deletList.html',
                    controller: 'signDeletCtrl',
                    controllerAs: 'vm'
                })
                //end#signList

                //begin#workprogram
                /*.state('workprogramEdit', {
                    url: '/workprogramEdit/:signid/:isControl/:minBusinessId/:businessType',
                    templateUrl: rootPath + '/workprogram/html/edit.html',
                    controller: 'workprogramEditCtrl',
                    controllerAs: 'vm'
                })*/
                //流程处理中工作方案填写(新方法)
                .state('flowWPEdit', {
                    url: '/flowWPEdit/:signid/:taskid',
                    templateUrl: rootPath + '/workprogram/html/edit.html',
                    controller: 'flowWPEditCtrl',
                    controllerAs: 'vm'
                })
                //end#workprogram

                //begin#maintWorkprogram
                .state('maintWorkprogramEdit', {
                    url: '/maintWorkprogramEdit/:signid',
                    templateUrl: rootPath + '/maintainProject/html/workprogramEdit.html',
                    controller: 'maintWorkprogramEditCtrl',
                    controllerAs: 'vm'
                })
                //end#maintWorkprogram

                //begin#dispatch
                .state('dispatchEdit', {
                    url: '/dispatchEdit/:signid/:isControl',
                    templateUrl: rootPath + '/dispatch/html/edit.html',
                    controller: 'dispatchEditCtrl',
                    controllerAs: 'vm'
                })//end#dispatch

                //begin#fileRecord
                .state('fileRecordEdit', {
                    url: '/fileRecordEdit/:signid/:isControl',
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
                //S 项目费用管理
                //评审费录入页面
                .state('financialManager', {
                    url: '/financialManager/:businessId',
                    templateUrl: rootPath + '/financialManager/html/add.html',
                    controller: 'financialManagerCtrl',
                    controllerAs: 'vm'
                })
                //协审费录入
                .state('financialAssistManager', {
                    url: '/financialAssistManager/:businessId',
                    templateUrl: rootPath + '/financialManager/html/assistCostAdd.html',
                    controller: 'assistCostEditCtrl',
                    controllerAs: 'vm'
                })
                //协审费录入列表页面
                .state('assistCostlist', {
                    url: '/assistCostlist/:costType',
                    templateUrl: rootPath + '/financialManager/html/assistCostList.html',
                    controller: 'assistCostCountEditCtrl',
                    controllerAs: 'vm'
                })
                //协审费统计列表
                .state('assistCostCountList', {
                    url: '/assistCostCountList',
                    templateUrl: rootPath + '/financialManager/html/assistCostCount.html',
                    controller: 'assistCostCountListCtrl',
                    controllerAs: 'vm'
                })
                //评审费录入列表
                .state('financialManagerList', {
                    url: '/financialManagerList/:costType',
                    templateUrl: rootPath + '/financialManager/html/list.html',
                    controller: 'financialManagerEditCtrl',
                    controllerAs: 'vm'
                })

                //专家缴费统计列表
                .state('expertPaymentCountList', {
                    url: '/expertPaymentCountList',
                    templateUrl: rootPath + '/financialManager/html/expertPaymentCount.html',
                    params: {'year': (new Date()).getFullYear()+'','month':((new Date()).getMonth() + 1)+''},//参数在这边声明
                    controller: 'expertPaymentCountCtrl',
                    controllerAs: 'vm'
                })
                //专家缴费明细统计列表
                .state('expertPaymentDetailCountList', {
                    url: '/expertPaymentDetailCountList/:year/:month',
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
                //专家评审情况统计列表
                .state('expertRevCondCountList', {
                    url: '/expertRevCondCountList',
                    templateUrl: rootPath + '/signView/html/expertReviewCondCount.html',
                    controller: 'expertRevConCountCtrl',
                    controllerAs: 'vm'
                })
                //项目评审费统计列表
                .state('projectCostCountList', {
                    url: '/projectCostCountList',
                    templateUrl: rootPath + '/financialManager/html/projectCostCount.html',
                    controller: 'projectCostCountCtrl',
                    controllerAs: 'vm'
                })
                //项目评审费分类统计列表
                .state('proCostClassifyCountList', {
                    url: '/proCostClassifyCountList',
                    templateUrl: rootPath + '/financialManager/html/proCostClassifyCount.html',
                    controller: 'proCostClassifyCountCtrl',
                    controllerAs: 'vm'
                })
                //项目评审情况统计
                .state('proReviewConCountList', {
                    url: '/proReviewConCountList',
                    templateUrl: rootPath + '/signView/html/proReviewConCount.html',
                    controller: 'proReviewConditionCtrl',
                    controllerAs: 'vm'
                })
                //业绩统计表
                .state('achievementList', {
                    url: '/achievementList',
                    templateUrl: rootPath + '/signView/html/achievement.html',
                    params: {'year': (new Date()).getFullYear()+'','quarter':'0'},
                    controller: 'achievementListCtrl',
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

                //年度月报简报
                .state('theMonthsList', {
                    url: '/theMonthsList',
                    templateUrl: rootPath + '/monthlyNewsletter/html/theMonthsList.html',
                    controller: 'yearMonthlyNewsletterCtrl',
                    controllerAs: 'vm'
                })

                //年度月报简报列表理页面
                .state('monthlyFindByMultiyear', {
                    url: '/monthlyFindByMultiyear/:year',
                    templateUrl: rootPath + '/monthlyNewsletter/html/monthlyMultiyearList.html',
                    controller: 'monthlyMultiyearCtrl',
                    controllerAs: 'vm'
                })

                //编辑新建年度月报简报页面
                .state('monthlyMultiyearEdit', {
                    url: '/monthlyMultiyearEdit/:year/:id',
                    templateUrl: rootPath + '/monthlyNewsletter/html/monthlyMultiyearAdd.html',
                    controller: 'monthlyMultiyearEditCtrl',
                    controllerAs: 'vm'
                })

                //月报简报查询页面
                .state('monthlyMultiyFileList', {
                    url: '/monthlyMultiyFileList',
                    templateUrl: rootPath + '/monthlyNewsletter/html/monthlyMultiyFileList.html',
                    controller: 'monthlyMultiFileCtrl',
                    controllerAs: 'vm'
                })

                //年度(中心文件)月报简报详细页面
                .state('monthlyMultiyView', {
                    url: '/monthlyMultiyView/:id',
                    templateUrl: rootPath + '/monthlyNewsletter/html/monthlyMultiyView.html',
                    controller: 'monthlyMultiyViewCtrl',
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
                //优秀评审报告填报
                .state('approveWindow', {
                    url: '/approveWindow/:signId',
                    templateUrl: rootPath + '/reviewProjectAppraise/html/approveWindow.html',
                    controller: 'approveWindowCtrl',
                    controllerAs: 'vm'
                })
                //优秀评审报告编辑
                .state('approveWindowEdit', {
                    url: '/approveWindowEdit/:id',
                    templateUrl: rootPath + '/reviewProjectAppraise/html/approveWindow.html',
                    controller: 'approveWindowEditCtrl',
                    controllerAs: 'vm'
                })

                //优秀评审报告列表
                .state('reviewProjectAppraiseList', {
                    url: '/reviewProjectAppraiseList',
                    params:{'id' : ''},
                    templateUrl: rootPath + '/signView/html/list.html',
                    controller: 'reviewProjectAppraiseCtrl',
                    controllerAs: 'vm'
                })
                //评审项目评优列表
                .state('reviewProjectAppraiseEdit', {
                    url: '/reviewProjectAppraiseEdit',
                    templateUrl: rootPath + '/reviewProjectAppraise/html/edit.html',
                    controller: 'reviewProjectAppraiseEditCtrl',
                    controllerAs: 'vm'
                })
                //优秀评审报告审批列表
                .state('approveList', {
                    url: '/approveList',
                    templateUrl: rootPath + '/reviewProjectAppraise/html/approveList.html',
                    controller: 'approveListCtrl',
                    controllerAs: 'vm'
                })
                //E 月报简报管理

                //S 档案借阅管理
                //项目档案借阅录入页面
                .state('libraryAdd', {
                    url: '/libraryAdd',
                    params:{'id' : ''},
                    templateUrl: rootPath + '/archivesLibrary/html/archivesLibraryAdd.html',
                    controller: 'archivesLibraryCtrl',
                    controllerAs: 'vm'
                })
                //档案借阅查询
                .state('archivesLibraryList', {
                    url: '/archivesLibraryList',
                    params:{'id' : ''},
                    templateUrl: rootPath + '/archivesLibrary/html/archivesLibraryList.html',
                    controller: 'archivesLibraryListCtrl',
                    controllerAs: 'vm'
                })
                //档案借阅查看详情页
                .state('archivesLibraryView', {
                    url: '/archivesLibraryView/:id',
                    templateUrl: rootPath + '/archivesLibrary/html/archivesLibraryView.html',
                    controller: 'archivesLibraryViewCtrl',
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
                .state('pluginfile', {
                    url: '/pluginfile',
                    templateUrl: rootPath + '/file/html/pluginfile.html',
                    controller: 'pluginfileCtrl',
                    controllerAs: 'vm'
                })
                //个人中心
                .state('takeUser', {
                    url: '/takeUser',
                    templateUrl: rootPath + '/personalCenter/html/takeUser.html',
                    controller: 'takeUserCtrl',
                    controllerAs: 'vm'
                })
                //质量管理文件库
                .state('fileLibrary', {
                    url: '/fileLibrary',
                    templateUrl: rootPath + '/fileLibrary/html/fileLibrary.html',
                    controller: 'qualityCtrl',
                    controllerAs: 'vm'
                })
                .state('fileLibrary.fileList', { //文件列表
                    url: '/fileList/:parentId',
                    templateUrl: rootPath + '/fileLibrary/html/fileList.html',
                    controller: 'qualityListCtrl',
                    controllerAs: 'vm'
                })
                .state('fileLibrary.fileEdit', {//新建文件
                    url: '/fileEdit/:parentId/:fileId',
                    templateUrl: rootPath + '/fileLibrary/html/fileEdit.html',
                    controller: 'qualityEditCtrl',
                    controllerAs: 'vm'
                })
                //政策标准库
                .state('policyLibrary', {
                    url: '/policyLibrary',
                    templateUrl: rootPath + '/fileLibrary/html/policyLibrary.html',
                    controller: 'policyCtrl',
                    controllerAs: 'vm'
                })
                .state('policyLibrary.policyList', { //文件列表
                    url: '/policyList/:parentId',
                    templateUrl: rootPath + '/fileLibrary/html/policyList.html',
                    controller: 'policyListCtrl',
                    controllerAs: 'vm'
                })
                .state('policyLibrary.policyEdit', {//新建文件
                    url: '/policyEdit/:parentId/:fileId',
                    templateUrl: rootPath + '/fileLibrary/html/policyEdit.html',
                    controller: 'policyEditCtrl',
                    controllerAs: 'vm'
                })
                //图书采购流程
                .state('bookBuyBusinessEdit', {
                    url: '/bookBuyBusinessEdit',
                    params:{'businessId' : '' , 'viewDetail' : ''},
                    templateUrl: rootPath + '/bookBuyBusiness/html/bookBuyBusinessEdit.html',
                    controller: 'bookBuyBusinessEditCtrl',
                    controllerAs: 'vm'
                })
                .state('myBookBuyBusiness', {
                    url: '/myBookBuyBusiness',
                    templateUrl: rootPath + '/bookBuyBusiness/html/bookBuyBusinessList.html',
                    controller: 'bookBuyBusinessCtrl',
                    controllerAs: 'vm'
                })
                //图书查询
                .state('bookDetailList', {
                    url: '/bookDetailList',
                    templateUrl: rootPath + '/bookBuyBusiness/html/bookBuyList.html',
                    controller: 'bookBuyCtrl',
                    controllerAs: 'vm'
                })
                //图书详情页
                .state('bookBuyBusinessDetail', {
                    url: '/bookBuyBusinessDetail/:businessId/:viewDetail',
                    templateUrl: rootPath + '/bookBuyBusiness/html/bookBuyBusinessDetail.html',
                    controller: 'bookBuyBusinessDetailCtrl',
                    controllerAs: 'vm'
                })
                //借书查询
                .state('bookBorrowList', {
                    url: '/bookBorrowList',
                    templateUrl: rootPath + '/bookBuyBusiness/html/bookBorrowList.html',
                    controller: 'bookBorrowCtrl',
                    controllerAs: 'vm'
                })
                //固定资产申购流程
                .state('assertStorageBusinessEdit', {
                    url: '/assertStorageBusinessEdit/:businessId',
                    templateUrl: rootPath + '/assertStorageBusiness/html/assertStorageBusinessEdit.html',
                    controller: 'assertStorageBusinessEditCtrl',
                    controllerAs: 'vm'
                })
                .state('myAssertStorageBusiness', {
                    url: '/myAssertStorageBusiness',
                    templateUrl: rootPath + '/assertStorageBusiness/html/assertStorageBusinessList.html',
                    controller: 'assertStorageBusinessCtrl',
                    controllerAs: 'vm'
                })
                .state('assertApplyUse', {
                    url: '/assertApplyUse',
                    templateUrl: rootPath + '/userAssertDetail/html/userAssertDetailAdd.html',
                    controller: 'userAssertDetailAddCtrl',
                    controllerAs: 'vm'
                })
                //课题研究流程
                .state('addTopic', {
                    url: '/topicInfo/:id',
                    templateUrl: rootPath + '/topicInfo/html/add.html',
                    controller: 'topicAddCtrl',
                    controllerAs: 'vm'
                })
                .state('myTopic', {
                    url: '/myTopic',
                    templateUrl: rootPath + '/topicInfo/html/myList.html',
                    controller: 'myTopicCtrl',
                    controllerAs: 'vm'
                })
                .state('queryTopic', {
                    url: '/queryTopic',
                    templateUrl: rootPath + '/topicInfo/html/queryTopic.html',
                    controller: 'queryTopicCtrl',
                    controllerAs: 'vm'
                })
                .state('topicDetail', {
                    url: '/topicDetail/:businessId/:processInstanceId',
                    templateUrl: rootPath + '/topicInfo/html/topicDetail.html',
                    controller: 'topicDetailCtrl',
                    controllerAs: 'vm'
                })
                //表头设置
                .state('header', {
                    url: '/header',
                    templateUrl: rootPath + '/header/html/list.html',
                    controller: 'headerCtrl',
                    controllerAs: 'vm'
                })
                .state('headerEdit', {
                    url: '/headerEdit/:headerType',
                    templateUrl: rootPath + '/header/html/selectHeader.html',
                    controller: 'headerEditCtrl',
                    controllerAs: 'vm'
                })
                .state('statisticalList', {
                    url: '/statisticalList/:headerType',
                    templateUrl: rootPath + '/header/html/statisticalList.html',
                    controller: 'statisticalListCtrl',
                    controllerAs: 'vm'
                })
                //统计图表
                .state('statistical', {
                    url: '/statistical',
                    templateUrl: rootPath + "/statistical/html/list.html",
                    controller: 'statisticalCtrl',
                    controllerAs: 'vm'
                })
                .state('editWorkPlan', {
                    url: '/editWorkPlan/:topicId',
                    templateUrl: rootPath + '/workPlan/html/edit.html',
                    controller: 'workPlanEditCtrl',
                    controllerAs: 'vm'
                })
                .state('editFiling', {
                    url: '/editFiling/:topicId',
                    templateUrl: rootPath + '/filing/html/edit.html',
                    controller: 'filingEditCtrl',
                    controllerAs: 'vm'
                })
                //系统管理员业务
                //评审费发放
                .state('reviewFee', {
                    url: '/reviewFee',
                    templateUrl: rootPath + "/reviewFee/html/list.html",
                    controller: 'reviewFeeCtrl',
                    controllerAs: 'vm'
                })
                //项目专家抽取修改
                .state('signwork', {
                    url: '/signwork/:signid',
                    templateUrl: rootPath + "/signwork/html/list.html",
                    controller: 'signworkCtrl',
                    controllerAs: 'vm'
                })
                //待办的附件右边列表页
                .state('signFlowDeal.fileList', { //文件列表
                    url: '/fileList/:id/:type',
                    templateUrl: rootPath + '/file/html/rightList.html',
                    controller: 'fileListCtrl',
                    controllerAs: 'vm'
                })
                //在办的附件右边列表页
                .state('signFlowDetail.fileList', { //文件列表
                    url: '/fileList/:id/:type',
                    templateUrl: rootPath + '/file/html/rightList.html',
                    controller: 'fileListCtrl',
                    controllerAs: 'vm'
                })
                //已办结的附件右边列表页
                .state('endSignDetail.fileList', { //文件列表
                    url: '/fileList/:id/:type',
                    templateUrl: rootPath + '/file/html/rightList.html',
                    controller: 'fileListCtrl',
                    controllerAs: 'vm'
                })
                //详细信息的附件右边列表页
                .state('signDetails.fileList', { //文件列表
                    url: '/fileList/:id/:type',
                    templateUrl: rootPath + '/file/html/rightList.html',
                    controller: 'fileListCtrl',
                    controllerAs: 'vm'
                })

                //课题研究的附件右边列表页
                .state('flowDeal.fileList', { //文件列表
                    url: '/flowDeal/:id/:type',
                    templateUrl: rootPath + '/file/html/rightList.html',
                    controller: 'fileListCtrl',
                    controllerAs: 'vm'
                })
                //维护项目附件右边列表页
                .state('MaintainProjectEdit.fileList', { //文件列表
                    url: '/MaintainProjectEdit/:id/:type',
                    templateUrl: rootPath + '/file/html/rightList.html',
                    controller: 'fileListCtrl',
                    controllerAs: 'vm'
                })

                //项目查询统计图表分析
                .state('signChart', {
                    url: '/signChart',
                    templateUrl: rootPath + "/signView/html/signChart.html",
                    controller: 'signChartCtrl',
                    controllerAs: 'vm'
                })
                /********************以下是项目维护***************************/
                .state('MaintainProjectList', {	//维护项目列表
                    url: '/MaintainProjectList',
                    templateUrl: rootPath + '/maintainProject/html/MaintainProjectList.html',
                    controller: 'MaintainProjectCtrl',
                    controllerAs: 'vm'
                })
                .state('MaintainProjectEdit', {	//维护项目的编辑
                    url: '/MaintainProjectEdit/:signid/:processInstanceId',
                    templateUrl: rootPath + '/maintainProject/html/MaintainProjectEdit.html',
                    controller: 'MaintainProjectEditCtrl',
                    controllerAs: 'vm'
                })
                .state('reviewWorkday', {	//评审工作日维护
                    url: '/reviewWorkday/:signid',
                    templateUrl: rootPath + '/maintainProject/html/reviewWorkday.html',
                    controller: 'reviewWorkdayCtrl',
                    controllerAs: 'vm'
                })
                .state('reviewOpinion', {//修改意见
                    url: '/reviewOpinion/:signid/:processInstanceId',
                    templateUrl: rootPath + "/maintainProject/html/reviewOpinion.html",
                    controller: 'reviewOpinionCtrl',
                    controllerAs: 'vm'
                })
                .state('maintainExpertScore', {//专家评分
                    url: '/maintainExpertScore/:signid',
                    templateUrl: rootPath + "/maintainProject/html/maintainExpertScore.html",
                    controller: 'maintainExpertScoreCtrl',
                    controllerAs: 'vm'
                })
                .state('maintainExpertPayment', {//评审费发放
                    url: '/maintainExpertPayment/:signid',
                    templateUrl: rootPath + "/maintainProject/html/maintainExpertPayment.html",
                    controller: 'maintainExpertPaymentCtrl',
                    controllerAs: 'vm'
                })
                .state('maintainExpertConfirm', {//修改确定的专家
                    url: '/maintainExpertConfirm/:signid',
                    templateUrl: rootPath + "/maintainProject/html/maintainExpertConfirm.html",
                    controller: 'maintainExpertConfirmCtrl',
                    controllerAs: 'vm'
                })

            // begin 党务管理
                .state('partyEdit', {//党员信息录入
                    url: '/partyEdit/:id',
                    templateUrl: rootPath + "/partyManager/html/partyEdit.html",
                    controller: 'partyEditCtrl',
                    controllerAs: 'vm'
                })
                .state('partyList', {//党员信息查询列
                    url: '/partyList',
                    templateUrl: rootPath + "/partyManager/html/partyList.html",
                    controller: 'partyListCtrl',
                    controllerAs: 'vm'
                })
                .state('partyMeetList', {//党员会议列表
                    url: '/partyMeetList',
                    templateUrl: rootPath + "/partyMeet/html/partyMeetList.html",
                    controller: 'partyMeetingCtrl',
                    controllerAs: 'vm'
                })
                .state('addPartyMeet', {//党员会议添加编辑页
                    url: '/addPartyMeet/:id',
                    templateUrl: rootPath + "/partyMeet/html/addPartyMeeting.html",
                    controller: 'partyMeetingCtrl',
                    controllerAs: 'vm'
                })
                .state('partyMeetDetail', {//党员会议添加编辑页
                    url: '/partyMeetDetail/:id',
                    templateUrl: rootPath + "/partyMeet/html/partyMeetDetail.html",
                    controller: 'partyMeetingCtrl',
                    controllerAs: 'vm'
                })
                // end 党务管理

                //begin 短信模块
                .state('msgEdit', {
                    url: '/msgEdit',
                    templateUrl: rootPath + "/message/html/edit.html",
                    controller: 'msgEditCtrl',
                    controllerAs: 'vm'
                })

                //end 短信编辑
        }]).run(function ($rootScope, $http, $state, $stateParams, bsWin) {
        $rootScope.rootPath = rootPath;
        $rootScope.DICT = DICTOBJ;
        //kendo 语言
        kendo.culture("zh-CN");
        common.getTaskCount({$http: $http});


        //获取表头名称
        $rootScope.getTBHeadName = function (stageName, isAdvanced, type) {
            //项目建议书、可行性  提前介入称为评估论证
            if (isAdvanced && isAdvanced == '9' && (stageName == '项目建议书' || stageName == '可行性研究报告')) {
                return "评估论证" + type;
            } else {
                if (stageName) {
                    if (stageName == '项目概算') {
                        return "概算审核" + type;
                    }
                    return stageName + type;
                } else {
                    return type;
                }
            }
        }

        //实现返回的函数
        $rootScope.$on("$stateChangeSuccess", function (event, toState, toParams, fromState, fromParams) {
            $rootScope.previousState_name = fromState.name;
            $rootScope.previousState_params = fromParams;
            if (fromState.name == 'signFlowDeal' || fromState.name == 'flowDeal') {
                $rootScope.$flowUrl = fromState.name;
                $rootScope.$flowParams = fromParams;
            }
        });
        $rootScope.back = function () {
            if ($rootScope.previousState_name) {
                $state.go($rootScope.previousState_name, $rootScope.previousState_params);
            } else {
                $state.go('welcome');
            }
        };
        $rootScope.backtoflow = function () {
            if ($rootScope.$flowUrl) {
                $state.go($rootScope.$flowUrl, $rootScope.$flowParams);
            } else {
                $state.go('gtasks');
            }
        };

        $rootScope.topSelectChange = function (dictKey, dicts, type) {
            if (dicts != undefined) {
                for (var i = 0; i < dicts.length; i++) {
                    //根据code查询
                    if (type && type == "code") {
                        if (dicts[i].dictCode == dictKey) {
                            return dicts[i].dicts;
                        }
                        //默认根据name查询
                    } else {
                        if (dicts[i].dictName == dictKey) {
                            return dicts[i].dicts;
                        }
                    }
                }
            }
        }

        //S_初始化input框的值
        $rootScope.initInputValue = function ($event, defaultValue) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            if (checked && !defaultValue) {
                return 1;
            } else {
                return defaultValue;
            }
        }//E_initInputValue

        //用于循环数字用
        $rootScope.range = function (n) {
            if (n) {
                return new Array(n);
            }
        }

        /**
         * 控制textarea输入长度限制
         * @param key
         * @param maxnumber
         * @param msgId
         * @returns {*}
         */
        $rootScope.countCharacter = function (key, maxnumber, msgId) {
            var writeNum = key ? key.length : 0;
            var resultMsg = "<span style='font-size:12px;'>（最多允许输入" + maxnumber + "个字";
            if (writeNum > 0) {
                var lessNum = (maxnumber - writeNum) > 0 ? (maxnumber - writeNum) : 0;
                resultMsg += ",还能输入<font color='red'>" + lessNum + "</font>个字";
                if (lessNum == 0) {
                    key = key.substr(0, maxnumber);
                }
            }
            resultMsg += "</span>)";
            $("#" + msgId).html(resultMsg);
            return key;
        }

        //文件预览
        $rootScope.previewFile = function (sysFileId, fileType) {
            if (sysFileId) {
                var url, width, height;
                if ("pdf" == fileType) {
                    url = rootPath + "/contents/libs/pdfjs-dist/web/viewer.html?file=" + rootPath + "/file/preview/" + sysFileId + "&version=" + (new Date()).getTime() + "";
                } else if ("image" == fileType) {
                    url = rootPath + "/file/preview/" + sysFileId + "?version=" + (new Date()).getTime() + "";
                }
                if (url) {
                    var httpOptions = {
                        method: 'post',
                        url: rootPath + "/file/fileSysCheck",
                        params: {
                            sysFileId: sysFileId
                        }
                    }
                    var httpSuccess = function success(response) {
                        $("#iframePreview").attr("src", "");
                        if (response.data.flag || response.data.reCode == 'ok') {
                            $("#iframePreview").attr("src", url);
                            $("#previewModal").kendoWindow({
                                width: "1050px",
                                height: "730px",
                                title: "",
                                visible: false,
                                modal: true,
                                closable: true,
                                actions: ["Pin", "Minimize", "Maximize", "Close"]
                            }).data("kendoWindow").center().open();
                        } else {
                            bsWin.error(response.data.reMsg);
                        }
                    };
                    common.http({
                        $http: $http,
                        httpOptions: httpOptions,
                        success: httpSuccess
                    });
                } else {
                    bsWin.alert("该文件不支持在线预览和在线编辑");
                }
            } else {
                bsWin.alert("参数不正确，无法在线预览");
            }
        }

        //文件在线编辑
        $rootScope.editFile = function (sysFileId, fileType) {
            var url = rootPath + "/file/editFile?sysFileId=" + sysFileId + "&fileType=" + fileType + "&version=" + (new Date()).getTime();
            window.open(url, "_blank");
        }

        //打印预览，生成word模板直接预览
        $rootScope.printFile = function (businessId, businessType, stageType) {
            if (!businessId || !businessType || !stageType) {
                bsWin.alert("没有项目阶段，找不到对应的打印模板，打印预览失败！");
            } else {
                var url = rootPath + "/contents/libs/pdfjs-dist/web/viewer.html?version=" + (new Date()).getTime() + "&file=" + rootPath + "/file/printPreview/" + businessId + "/" + businessType + "/" + stageType;
                $("#iframePreview").attr("src", url);
                $("#previewModal").kendoWindow({
                    width: "80%",
                    height: "730px",
                    title: "",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            }
        }
        //评审费打印。判断开户行和银行账户信息完不完整
        $rootScope.isBankCard=function (expertSelectedDtoList,signid , payData) {
            var flag  = false;
            if(payData == undefined){
                flag = true ;
                bsWin.alert("评审费未发放，不能进行打印操作！");
            }else{
                for(var i=0;i<expertSelectedDtoList.length;i++){
                    //必须是确认参与的专家
                    if(expertSelectedDtoList[i].isConfrim == "9"
                        && expertSelectedDtoList[i].isJoin == "9"){


                        if(expertSelectedDtoList[i].expertDto.bankAccount ==undefined
                            || expertSelectedDtoList[i].expertDto.openingBank ==undefined ){

                            flag = true;
                            bsWin.alert("专家的开户行和银行账户信息不全，请填写完整！");
                            break;
                        }

                        if(expertSelectedDtoList[i].reviewTaxes == undefined){
                            flag = true;
                            bsWin.alert("评审费未计纳税额，不能进行打印操作！");
                            break;
                        }

                    }

                }
            }


            if(!flag){
                $rootScope.printFile(signid,'SIGN_EXPERT' , 'SIGN_EXPERT_PAY');
            }
        }

        /**
         * 导出功能
         */
        $rootScope.exportInfo = function(businessId, businessType, stageType , fileName){
            if (!businessId || !businessType || !stageType) {
                bsWin.alert("没有项目阶段，找不到对应的导出模板，导出失败！");
            } else {
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/file/exportInfo",
                    params: {
                        "businessId" : businessId ,
                        "businessType" : businessType ,
                        "stageType" : stageType ,
                        "fileName" : fileName
                    }
                }
                var httpSuccess = function success(response) {
                    fileName =fileName + ".doc";
                    var fileType ="msword";
                    common.downloadReport(response.data , fileName , fileType);
                };
                common.http({
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }

        /**
         * 返回时列表状态不改变。
         * @type {{}}
         */
        //状态
        $rootScope.view = {};
        //保存查询条件
        $rootScope.storeView = function (storeName, params) {
            $rootScope.view[storeName] = params;
        }
    });

})();
