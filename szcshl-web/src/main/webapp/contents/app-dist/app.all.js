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
                url: '/addSupp/:signid/:id',
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
            if(fromState.name == 'signFlowDeal'){
                $rootScope.$flowUrl = fromState.name;
                $rootScope.$flowParams = fromParams;
            }
        });

        $rootScope.back = function () {
        	if($rootScope.previousState_name ){
        		$state.go($rootScope.previousState_name, $rootScope.previousState_params);
        	}else{
        		window.history.back();
        	}           
        };
        $rootScope.backtoflow = function () {
            if($rootScope.$flowUrl ){
                $state.go($rootScope.$flowUrl, $rootScope.$flowParams);
            }else{
                $state.go('gtasks');
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
(function () {
    'use strict';

    angular.module('app').controller('adminCtrl', admin);

    admin.$inject = ['$location','adminSvc'];

    function admin($location, adminSvc) {
        var vm = this;
        vm.title = '待办事项';
        activate();
        function activate() {
        	adminSvc.gtasksGrid(vm);
        }
        vm.countWorkday=function(){
        	adminSvc.countWorakday(vm);
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('adminDoingCtrl', admin);

    admin.$inject = ['$location', 'adminSvc', 'flowSvc','pauseProjectSvc'];

    function admin($location, adminSvc, flowSvc,pauseProjectSvc) {
        var vm = this;
        vm.title = '在办任务';
        vm.model = {};
        activate();
        function activate() {
            vm.showwin = false;
            adminSvc.dtasksGrid(vm);
        }

        /**
         * 项目暂停弹窗
         */
        vm.pauseProject = function (signid) {
            pauseProjectSvc.findPausingProject(vm,signid,"");
            // pauseProjectSvc.pauseProjectWindow(vm,signid,"");
        }

        vm.Checked=function(){
            if($("#fileNo").is(":checked")){
                $("#file1").prop("checked",false);
                $("#file2").prop("checked",false);
            }
        }
        vm.Checked2=function(){
            $("#fileNo").prop("checked",false);
        }


        /**
         * 保存项目暂停
         */
        vm.commitProjectStop = function () {
            pauseProjectSvc.pauseProject(vm);
        }
        /**
         * 取消项目暂停窗口
         */
        vm.closewin = function () {
            window.parent.$("#spwindow").data("kendoWindow").close()
        }

        /**
         * 流程激活
         * @param signid
         */
        vm.startProject = function (signid) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认激活吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    flowSvc.activeFlow(vm, signid);
                }
            })
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('adminEndCtrl', admin);

    admin.$inject = ['$location','adminSvc']; 

    function admin($location, adminSvc) {
        var vm = this;
        vm.title = '办结事项';
             
        activate();
        function activate() {
        	adminSvc.etasksGrid(vm);
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('adminSignListCtrl', admin);

    admin.$inject = ['signSvc', 'adminSvc'];

    function admin(signSvc, adminSvc) {
        var vm = this;
        vm.title = '项目查询统计';
        activate();
        function activate() {
            //初始化查询参数
            adminSvc.initSignList(function(data){
                if(data.flag || data.reCode == 'ok'){
                    vm.orgsList = data.reObj;
                }
            });
            adminSvc.getSignList(vm);
            //项目关联初始化
            //signSvc.associateGrid(vm);
        }

        //重置
        vm.formReset = function () {
            var tab = $("#searchform").find('input,select');
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }

        //項目查詢統計
        vm.searchSignList = function () {
            vm.signListOptions.dataSource.read();
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').factory('adminSvc', admin);

    admin.$inject = ['$rootScope', '$http'];

    function admin($rootScope, $http) {

        var service = {
            gtasksGrid: gtasksGrid,		//个人待办
            etasksGrid: etasksGrid,		//个人办结
            dtasksGrid: dtasksGrid,        //在办任务
            countWorakday: countWorakday,	//计算工作日

            initFile: initFile,	        //初始化附件
            upload: upload,	            //	下载附件
            getSignList: getSignList,   //项目查询统计
            initSignList: initSignList, //初始化項目查詢統計
           // <!-- 以下是首页方法-->
            initAnnountment: initAnnountment,	    //初始化通知公告栏
            findendTasks: findendTasks,             //已办项目列表
            findtasks: findtasks,                   //待办项目列表
            findHomePluginFile :findHomePluginFile, //获取首页安装文件
        }
        return service;


        //begin countWorakday
        function countWorakday(vm) {
            var httpOptions = {
                method: "get",
                url: rootPath + "/workday/countWorkday"
            }

            var httpSuccess = function success(response) {
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }//end countWorakday

        //begin initAnnountment
        function initAnnountment(vm) {
            vm.i = 1;
            var httpOptions = {
                method: "get",
                url: rootPath + "/annountment/getHomePageAnnountment"
            }

            var httpSuccess = function success(response) {
                vm.annountmentList = response.data;
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }//end initAnnountment

        //S_获取本地安装包
        function findHomePluginFile(vm){
            var httpOptions = {
                method: "post",
                url: rootPath + "/file/listHomeFile"
            }
            var httpSuccess = function success(response) {
                vm.pluginFileList = {};
                vm.pluginFileList = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_findHomePluginFile

        //查找待办
        function findtasks(vm) {
            var httpOptions = {
                method: "post",
                url: rootPath + "/flow/getMyHomeTasks"
            }
            var httpSuccess = function success(response) {
                vm.tasksList = {};
                vm.tasksList = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //查找已办
        function findendTasks(vm) {
            vm.endTasksList = {};
            var httpOptions = {
                method: "post",
                url: rootPath + "/flow/getMyHomeEndTask"
            }

            var httpSuccess = function success(response) {
                vm.endTasksList = response.data;
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //begin initFile
        function initFile(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/file/findByBusinessId",
                params: {businessId: vm.anId}
            }

            var httpSuccess = function success(response) {
                vm.file = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//end initFile

        //begin upload
        function upload(vm, sysFileId) {
            window.open(rootPath + "/file/fileDownload?sysfileId=" + sysFileId);
        }//end upload

        //S_gtasksGrid
        function gtasksGrid(vm) {
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/flow/html/tasks"),
                schema: {
                    data: "value",
                    total: function (data) {
                        if (data['count']) {
                            $('#GtasksCount').html(data['count']);
                        } else {
                            $('#GtasksCount').html(0);
                        }
                        return data['count'];
                    },
                    model: {
                        id: "id",
                        fields: {
                            createdDate: {
                                type: "date"
                            }
                        }
                    }
                },
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            var columns = [
                {
                    field: "",
                    title: "",
                    width: 30,
                    template: function (item) {
                        switch (item.lightState) {
                            case "4":          //暂停
                                return $('#span1').html();
                                break;
                            case "8":         	//存档超期
                                return $('#span5').html();
                                break;
                            case "7":           //超过25个工作日未存档
                                return $('#span4').html();
                                break;
                            case "6":          	//发文超期
                                return $('#span3').html();
                                break;
                            case "5":          //少于3个工作日
                                return $('#span2').html();
                                break;
                            case "1":          //在办
                                return "";
                                break;
                            case "2":           //已发文
                                return "";
                                break;
                            case "3":           //已发送存档
                                return "";
                                break;
                            default:
                                return "";
                                ;
                        }
                    }
                },
                {
                    field: "",
                    title: "序号",
                    template: "<span class='row-number'></span>",
                    width: 50
                },
                {
                    field: "projectName",
                    title: "项目名称",
                    filterable: false,
                    width: "10%"
                },
                {
                    field: "reviewStage",
                    title: "项目阶段",
                    filterable: false,
                    width: "10%"
                },
                {
                    field: "nodeName",
                    title: "当前环节",
                    width: 120,
                    filterable: false
                },
                {
                    field: "preSignDate",
                    title: "预签收时间",
                    width: 120,
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "signDate",
                    title: "正式签收时间",
                    width: 120,
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "surplusDays",
                    title: "剩余工作日",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "displayName",
                    title: "处理人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "",
                    title: "流程状态",
                    width: 80,
                    filterable: false,
                    template: function (item) {
                        if (item.processState && item.processState == 2) {
                            return '<span style="color:orange;">已暂停</span>';
                        } else {
                            return '<span style="color:green;">进行中</span>';
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 80,
                    template: function (item) {
                        //项目签收流程，则跳转到项目签收流程处理野人
                        if (item.processKey == "FINAL_SIGN_FLOW" || item.processKey == "SIGN_XS_FLOW") {
                            if(item.processState == 2){
                                return common.format($('#detailBtns').html(), "signFlowDetail", item.businessKey, item.taskId, item.processInstanceId);
                            }else{
                                return common.format($('#columnBtns').html(), "signFlowDeal", item.businessKey, item.taskId, item.processInstanceId);
                            }

                        } else {
                            return "<a class='btn btn-xs btn-danger' >流程已停用</a>";
                        }
                    }
                }
            ];
            // End:column
            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                dataBound: function () {
                    var rows = this.items();
                    var page = this.pager.page() - 1;
                    var pagesize = this.pager.pageSize();
                    $(rows).each(function () {
                        var index = $(this).index() + 1 + page * pagesize;
                        var rowLabel = $(this).find(".row-number");
                        $(rowLabel).html(index);
                    });
                }
            };
        }//E_gtasksGrid

        //S_etasksGrid
        function etasksGrid(vm) {
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/flow/html/endTasks"),
                schema: {
                    data: "value",
                    total: function (data) {
                        return data['count'];
                    },
                    model: {
                        id: "id",
                        fields: {
                            createdDate: {
                                type: "date"
                            }
                        }
                    }
                },
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            var columns = [
                {
                    field: "",
                    title: "序号",
                    template: "<span class='row-number'></span>",
                    width: 40
                },
                {
                    field: "businessName",
                    title: "任务名称",
                    filterable: false,
                    width: 180
                },
                {
                    field: "createDate",
                    title: "开始时间",
                    width: 150,
                    filterable: false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "endDate",
                    title: "结束时间",
                    width: 150,
                    filterable: false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "",
                    title: "用时",
                    width: 180,
                    filterable: false,
                    template: function (item) {
                        if (item.durationTime) {
                            return item.durationTime;
                        } else {
                            return '<span style="color:orangered;">已办结</span>';
                        }
                    }
                },
                {
                    field: "",
                    title: "流程状态",
                    width: 120,
                    filterable: false,
                    template: function (item) {
                        return '<span style="color:orangered;">已办结</span>';
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 80,
                    template: function (item) {
                        if ((item.processDefinitionId).indexOf("FINAL_SIGN_FLOW") >= 0 || (item.processDefinitionId).indexOf("SIGN_XS_FLOW") >= 0) {
                            return common.format($('#columnBtns').html(), "endSignDetail", item.businessKey, item.processInstanceId);
                        } else {
                            return '';
                        }
                    }
                }
            ];
            // End:column
            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                dataBound: function () {
                    var rows = this.items();
                    var page = this.pager.page() - 1;
                    var pagesize = this.pager.pageSize();
                    $(rows).each(function () {
                        var index = $(this).index() + 1 + page * pagesize;
                        var rowLabel = $(this).find(".row-number");
                        $(rowLabel).html(index);
                    });
                }
            };
        }//E_etasksGrid

        //S_dtasksGrid
        function dtasksGrid(vm) {
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/flow/html/doingtasks"),
                schema: {
                    data: "value",
                    total: function (data) {
                        return data['count'];
                    },
                    model: {
                        id: "id"
                    }
                },
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            var columns = [
                {
                    field: "",
                    title: "",
                    width: 30,
                    template: function (item) {
                        switch (item.lightState) {
                            case "4":          //暂停
                                return $('#span1').html();
                                break;
                            case "8":         	//存档超期
                                return $('#span5').html();
                                break;
                            case "7":           //超过25个工作日未存档
                                return $('#span4').html();
                                break;
                            case "6":          	//发文超期
                                return $('#span3').html();
                                break;
                            case "5":          //少于3个工作日
                                return $('#span2').html();
                                break;
                            case "1":          //在办
                                return "";
                                break;
                            case "2":           //已发文
                                return "";
                                break;
                            case "3":           //已发送存档
                                return "";
                                break;
                            default:
                                return "";
                                ;
                        }
                    }
                },
                {
                    field: "",
                    title: "序号",
                    template: "<span class='row-number'></span>",
                    width: 50
                },
                {
                    field: "projectName",
                    title: "项目名称",
                    filterable: false,
                    width: 150
                },
                {
                    field: "reviewStage",
                    title: "项目阶段",
                    filterable: false,
                    width: 150
                },
                {
                    field: "nodeName",
                    title: "当前环节",
                    width: 120,
                    filterable: false
                },
                {
                    field: "preSignDate",
                    title: "预签收时间",
                    width: 120,
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "signDate",
                    title: "正式签收时间",
                    width: 120,
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "surplusDays",
                    title: "剩余工作日",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "",
                    title: "处理人",
                    width: 100,
                    filterable: false,
                    template: function (item) {
                        if (item.assignee) {
                            return item.assignee;
                        } else if (item.userName) {
                            return item.userName;
                        }
                    }
                },
                {
                    field: "",
                    title: "流程状态",
                    width: 80,
                    filterable: false,
                    template: function (item) {
                        if (item.processState && item.processState == 2) {
                            return '<span style="color:orange;">已暂停</span>';
                        } else {
                            return '<span style="color:green;">进行中</span>';
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 150,
                    template: function (item) {
                    	 var isstart = false;
                        if (item.processState == "2") {
                            isstart = true;//显示已暂停，提示启动
                        } else {
                            isstart = false;//显示暂停
                        }
                        //项目签收流程，则跳转到项目签收流程处理野人
                        if (item.processKey == "FINAL_SIGN_FLOW" || item.processKey == "SIGN_XS_FLOW") {
                            return common.format($('#columnBtns').html(), "signFlowDetail", item.businessKey, item.taskId, item.processInstanceId,
                                "vm.pauseProject('"+item.businessKey+"')",isstart,"vm.startProject('"+item.businessKey+"')",isstart);
                        } else {
                            return '<a class="btn btn-xs btn-danger" >流程已停用</a>';
                        }
                    }
                }
            ];
            // End:column
            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                dataBound: function () {
                    var rows = this.items();
                    var page = this.pager.page() - 1;
                    var pagesize = this.pager.pageSize();
                    $(rows).each(function () {
                        var index = $(this).index() + 1 + page * pagesize;
                        var rowLabel = $(this).find(".row-number");
                        $(rowLabel).html(index);
                    });
                }
                
            };
        }//E_dtasksGrid
        
        function on(arg){
        	var row = this.select();
            var data = this.dataItem(row);
            var name = data.name;
            alert('单击事件【name：' + name + '】');
        }

        //begin_getSignList
        function getSignList(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/sign/getSignList", $("#searchform")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            // End:dataSource

            // Begin:column
            var columns = [
                {
                    field: "",
                    title: "序号",
                    template: "<span class='row-number'></span>",
                    width: 50
                },
                {
                    field: "projectname",
                    title: "项目名称",
                    width: 160,
                    filterable: false
                },
                {
                    field: "reviewstage",
                    title: "评审阶段",
                    width: 140,
                    filterable: false
                },
                {
                    field: "signdate",
                    title: "收文日期",
                    width: 200,
                    filterable: false
                },
                {
                    field: "dispatchtype",
                    title: "发文日期",
                    width: 160,
                    filterable: false
                },
                {
                    field: "reviewdays",
                    title: "评审天数",
                    width: 140,
                    filterable: false
                },
                {
                    field: "surplusdays",
                    title: "剩余工作日",
                    width: 140,
                    filterable: false
                },
                {
                    field: "reviewOrgName",
                    title: "评审部门",
                    width: 140,
                    filterable: false
                },
                {
                    field: "aUserName",
                    title: "项目负责人",
                    width: 140,
                    filterable: false
                },
                {
                    field: "ffilenum",
                    title: "归档编号",
                    width: 140,
                    filterable: false
                },
                {
                    field: "dfilenum",
                    title: "文件字号",
                    width: 140,
                    filterable: false
                },
                {
                    field: "appalyinvestment",
                    title: "申报投资",
                    width: 140,
                    filterable: false
                },
                {
                    field: "authorizevalue",
                    title: "审定投资",
                    width: 140,
                    filterable: false
                },
                {
                    field: "extravalue",
                    title: "核减（增）投资",
                    width: 140,
                    filterable: false
                },
                {
                    field: "extrarate",
                    title: "核减率",
                    width: 140,
                    filterable: false
                },
                {
                    field: "approvevalue",
                    title: "批复金额",
                    width: 140,
                    filterable: false
                },
                {
                    field: "receivedate",
                    title: "批复来文时间",
                    width: 140,
                    filterable: false
                },
                {
                    field: "dispatchtype",
                    title: "发文类型",
                    width: 140,
                    filterable: false
                },
                {
                    field: "ffiledate",
                    title: "归档日期",
                    width: 140,
                    filterable: false
                },
                {
                    field: "builtcompanyName",
                    title: "建设单位",
                    width: 140,
                    filterable: false
                },
                {
                    field: "isassistproc",
                    title: "是否协审",
                    width: 140,
                    filterable: false,
                    template: function (item) {
                        if (item.sisassistproc == 9) {
                            return "是";
                        } else {
                            return "否";
                        }
                    }
                },
                {
                    field: "daysafterdispatch",
                    title: "发文后工作日",
                    width: 140,
                    filterable: false
                }
                
                /* {
                 field: "",
                 title: "操作",
                 width: 60,
                 template: $('#columnBtns').html()
                 }*/
            ];
            
            // End:column
            vm.signListOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                selectable: "multiple cell",
                resizable: true,
                dataBound: function () {
                    var rows = this.items();
                    var page = this.pager.page() - 1;
                    var pagesize = this.pager.pageSize();
                    $(rows).each(function () {
                        var index = $(this).index() + 1 + page * pagesize;
                        var rowLabel = $(this).find(".row-number");
                        $(rowLabel).html(index);
                    });
                }
            };
        }//end_getSignList

        //begin_initSignList
        function initSignList(callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/initSignList"
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        } //end_initSignList

    }
})();
(function () {
    'use strict';

    angular.module('app').controller('adminWelComeCtrl', adminWelCome).filter('FormatStrDate', function() {
        return function(input) {
            var date = new Date(input);
            var monthValue = (date.getMonth()+1) < 10 ?"0"+(date.getMonth()+1):(date.getMonth()+1);
            var dayValue = (date.getDate()) < 10 ?"0"+(date.getDate()):(date.getDate());
            var formatDate=date.getFullYear()+"/"+monthValue+"/"+dayValue;
            return formatDate
        }
    });

    adminWelCome.$inject = ['$location','adminSvc'];

    function adminWelCome($location, adminSvc) {
        var vm = this;
        vm.title = '主页';
        activate();
        function activate() {
        	adminSvc.initAnnountment(vm);
            adminSvc.findtasks(vm);
            adminSvc.findendTasks(vm);
            adminSvc.findHomePluginFile(vm);
        }

    }
})();

(function () {
    'use strict';

    angular.module('app').controller('annountmentCtrl', annountment);

    annountment.$inject = ['$location', '$state', '$http', 'annountmentSvc', 'sysfileSvc'];

    function annountment($location, $state, $http, annountmentSvc, sysfileSvc) {
        var vm = this;
        vm.title = "通知公告管理";

        active();
        function active() {
            annountmentSvc.grid(vm);
        }

        //批量发布
        vm.bathIssue = function(){
            annountmentSvc.updateIssueState(vm,"9");
        }

        //取消发布
        vm.bathUnissue = function () {
            annountmentSvc.updateIssueState(vm,"0");
        }

        vm.del = function (anId) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    annountmentSvc.deleteAnnountment(vm, anId);
                }
            })
        }

        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: "请选择数据"
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.del(idStr);
            }
        }

        //查看详情
        vm.detail = function(id){
            if(id){
                $state.go('annountmentDetail', {id: id});
            }
        }

        //查询
        vm.queryAnnountment = function(){
            vm.gridOptions.dataSource.read();
        }

        //重置
        vm.resetAnnountment=function(){
        	var tab=$("#annountmentform").find('input,select');
        	$.each(tab,function(i,obj){
        		obj.value="";
        	});
        }
    }
})();
(function () {
    'use strict';

    angular.module('app').controller('annountmentDetailCtrl',annountmentDetail);

    annountmentDetail.$inject = ['$location','$state','annountmentSvc'];

    function annountmentDetail($location, $state,annountmentSvc) {
        var vm = this;
        vm.title = '通知公告详情页';
        vm.annountment = {};    //通知公告对象
        vm.annountment.anId = $state.params.id;
        activate();
        function activate() {
            annountmentSvc.findDetailById(vm,vm.annountment.anId);
        }
        
        vm.post=function(id){
            annountmentSvc.findDetailById(vm,id);
        }
        
        vm.next=function(id){
            annountmentSvc.findDetailById(vm,id);
        }


        vm.alertwd=function(){
            $("section").addClass("cont-alert");
        }
        vm.closed=function(){
            $("section").removeClass("cont-alert");
        }
        //打印
        vm.prints=function(){
            var html='<div  style="width: 80%;height:80%; text-align: center; margin: auto; font-family: \'Microsoft YaHei\'">'
                +$("#context-body").html()
                +"</div>";
            var newWindow;
            var ht=$(window).height();
            var wt=$(window).width();
            newWindow=window.open('','','width='+wt+',height='+ht);
            newWindow.document.body.innerHTML=html;
            newWindow.print();
        }


    }





})();


(function () {
    'use strict';

    angular.module('app').controller('annountmentEditCtrl', annountmentEdit);

    annountmentEdit.$inject = ['$state', 'annountmentSvc'];

    function annountmentEdit($state, annountmentSvc) {
        var vm = this;
        vm.title = "通知公告编辑";
        vm.annountment = {};        //通知公告对象
        vm.annountment.anId = $state.params.id;
        vm.businessFlag ={
            isInitFileOption : false,   //是否已经初始化附件上传控件
        }
        active();
        function active() {
           $('#froalaEditor') .froalaEditor({
                language: 'zh_cn',
                inlineMode: false,
                placeholderText:'请输入内容' ,
                imageUploadURL: rootPath +"/froala/uploadImg",
                imageUploadParams:{rootPath:rootPath},//接口其他传参,默认为空对象{},
                height: '260px', //高度
                enter: $.FroalaEditor.ENTER_BR,
                toolbarButtons: [
                    'bold', 'italic', 'underline','strikeThrough','fontFamily', 'paragraphFormat', 'align','color','fontSize','outdent',
                    'indent','insertImage','insertTable','undo', 'redo','insertLink','fullscreen'
                ]
            });

            if (vm.annountment.anId) {
            	vm.isUpdate=true;
                annountmentSvc.findAnnountmentById(vm);
                annountmentSvc.findFileList(vm)
            }else{
                //附件初始化
                annountmentSvc.initFileOption({
                    sysfileType: "通知公告",
                    uploadBt: "upload_file_bt",
                    vm: vm
                })
            }
            
          
           
        }

        //新增通知公告
        vm.create = function () {
            annountmentSvc.createAnnountment(vm);
        }

        //编辑通知公告
        vm.update = function () {
            annountmentSvc.updateAnnountment(vm);
        }
    }
})();
(function () {
    'use strict';

    angular.module('app').factory('annountmentSvc', annountment);

    annountment.$inject = ['$http', 'sysfileSvc'];

    function annountment($http, sysfileSvc) {

        var url_annountment = rootPath + "/annountment";
        var url_back = "#/annountment";
        var service = {
            grid: grid,		                            //初始化列表
            createAnnountment: createAnnountment,	    //新增通知公告
            initAnOrg: initAnOrg,		                //初始化发布单位
            findAnnountmentById: findAnnountmentById,	//获取通知公告信息
            updateIssueState: updateIssueState,         //更改通知公告的发布状态
            updateAnnountment: updateAnnountment,	    //更新通知公告
            deleteAnnountment: deleteAnnountment,	    //删除通知公告
            initFileOption: initFileOption,             //初始化附件参数
            findFileList: findFileList,                 //查询附件列表
            findDetailById: findDetailById,	            //通过id获取通过公告
            postArticle: postArticle,	                //访问上一篇文章
            nextArticle: nextArticle,	                //访问下一篇文章

        };

        return service;

        //begin initAnOrg
        function initAnOrg(vm) {
            var httpOptions = {
                method: "get",
                url: url_annountment + "/initAnOrg"
            }

            var httpSuccess = function success(response) {
                vm.annountment.anOrg = "";
                vm.annountment.anOrg = response.data.substring(1, response.data.length - 1);
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }

        //end initAnOrg

        //begin findAnnountmentById
        function findAnnountmentById(vm) {
            var httpOptions = {
                method: "get",
                url: url_annountment + "/findAnnountmentById",
                params: {anId: vm.annountment.anId}
            }

            var httpSuccess = function success(response) {
                vm.annountment = response.data;
                $("#froalaEditor").froalaEditor('html.set', vm.annountment.anContent);
                //初始化附件上传
                if (vm.businessFlag.isInitFileOption == false) {
                    initFileOption({
                        businessId: vm.annountment.anId,
                        sysfileType: "通知公告",
                        uploadBt: "upload_file_bt",
                        sysMinType:$("#sysMinType").val(),
                        vm: vm
                    });
                }
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//end findAnnountmentById

        //begin createAnnountment
        function createAnnountment(vm) {
        	vm.annountment.anContent=$("#froalaEditor").val();
            common.initJqValidation();
            var isValid = $('#form').valid();
            if (isValid) {
                var httpOptions = {
                    method: "post",
                    url: url_annountment,
                    data: vm.annountment
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            vm.isSubmit = false;
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                closeDialog: true
                            })
                        }
                    })
                    vm.annountment.anId = response.data.anId;
                    //初始化附件上传
                    if (vm.businessFlag.isInitFileOption == false) {
                        initFileOption({
                            businessId: vm.annountment.anId,
                            sysfileType: "通知公告",
                            uploadBt: "upload_file_bt",
                            sysMinType:$("#sysMinType").val(),
                            vm: vm
                        });
                    }
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }

        }//end createAnnountment

        //begin updateAnnountment
        function updateAnnountment(vm) {
        	vm.annountment.anContent=$("#froalaEditor").val();
            var httpOptions = {
                method: "put",
                url: url_annountment,
                data: vm.annountment
            }

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: "操作成功",
                            fn: function () {
                                vm.isSubmit = false;
                                $('.alertDialog').modal('hide');
                                $('.modal-backdrop').remove();
                            }
                        })
                    }
                })
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }//end updateAnnountment


        //begin deleteAnnountment
        function deleteAnnountment(vm, anId) {
            var httpOptions = {
                method: "delete",
                url: url_annountment,
                data: anId
            }

            var httpSuccess = function success(response) {
                vm.gridOptions.dataSource.read();
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }

        //end deleteAnnountment

        // begin#grid
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/annountment/fingByCurUser",$("#annountmentform")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        },
                        modifiedDate: {
                            type: "date"
                        }

                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: [
                {
                    field: "issue",
                    dir: "asc"
                }
                ]
                	

            });
            // End:dataSource

            //S_序号
            var dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            };
            //S_序号

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.anId)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "unitSort",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "anTitle",
                    title: "标题",
                    width: 300,
                    filterable: false
                },
                {
                    field: "issueDate",
                    title: "发布时间",
                    format: "{0:yyyy-MM-dd hh24:mm:ss}",
                    width: 160,
                    filterable: false
                },
                {
                    field: "issueUser",
                    title: "发布人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "",
                    title: "发布状态",
                    width: 100,
                    template: function (item) {
                        if (item.issue && item.issue == '9') {
                            return "已发布";
                        } else {
                            return "未发布";
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 150,
                    template: function (item) {
                    	
                        return common.format($('#columnBtns').html(),
                            "vm.detail('" + item.anId + "')", item.anId, "vm.del('" + item.anId + "')");
                    	
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                dataBound: dataBound,
                columns: columns,
                resizable: true
            };

        }// end fun grid


        //S_initFileOption
        function initFileOption(option) {
            if (option.uploadBt) {
                $("#" + option.uploadBt).click(function () {
                    if (!option.businessId) {
                        common.alert({
                            vm: option.vm,
                            msg: "请先保存数据！",
                            closeDialog: true
                        })
                    } else {
                        $("#commonuploadWindow").kendoWindow({
                            width: "800px",
                            height: "500px",
                            title: "附件上传",
                            visible: false,
                            modal: true,
                            closable: true,
                            actions: ["Pin", "Minimize", "Maximize", "Close"]
                        }).data("kendoWindow").center().open();
                    }
                });
            }
            if (option.businessId) {
                //附件下载方法
                option.vm.downloadSysFile = function (id) {
                    window.open(rootPath + "/file/fileDownload?sysfileId=" + id);
                }
                //附件删除方法
                option.vm.delSysFile = function (id) {
                    var httpOptions = {
                        method: 'delete',
                        url: rootPath + "/file/deleteSysFile",
                        params: {
                            id: id
                        }
                    }
                    var httpSuccess = function success(response) {
                        common.requestSuccess({
                            vm: option.vm,
                            response: response,
                            fn: function () {
                                findFileList(option.vm);
                                common.alert({
                                    vm: option.vm,
                                    msg: "删除成功",
                                    closeDialog: true
                                })
                            }
                        });
                    }
                    common.http({
                        vm: option.vm,
                        $http: $http,
                        httpOptions: httpOptions,
                        success: httpSuccess
                    });
                }
                var projectfileoptions = {
                    language: 'zh',
                    allowedPreviewTypes: ['image'],
                    allowedFileExtensions: ['jpg', 'png', 'gif', "xlsx", "docx", "doc", "xls", "pdf", "ppt", "zip", "rar"],
                    maxFileSize: 2000,
                    showRemove: false,
                    uploadUrl: rootPath + "/file/fileUpload",
                    uploadExtraData: function(previewId, index) {
                        var result={};
                        result.businessId=option.businessId;
                        result.sysSignId="通知公告";
                        result.sysfileType=angular.isUndefined(option.sysfileType) ? "通知公告" : option.sysfileType;
                        result.sysMinType=$("#sysMinType option:selected").val();
                        return result;
                        // businessId: option.businessId,
                        // sysSignId:"通知公告",
                        // sysfileType: angular.isUndefined(option.sysfileType) ? "通知公告" : option.sysfileType,
                        // sysMinType :$("#sysMinType option:selected").val()
                    }
                };

                var filesCount = 0;
                $("#sysfileinput").fileinput(projectfileoptions)
                    .on("filebatchselected", function (event, files) {
                        filesCount = files.length;
                    }).on("fileuploaded", function (event, data, previewId, index) {
                    if (filesCount == (index + 1)) {
                        findFileList(option.vm);
                    }
                });
                option.vm.businessFlag.isInitFileOption = true;
            }

        }//E_initFileOption

        //S_findFileList
        function findFileList(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/file/findByBusinessId",
                params: {
                    businessId: vm.annountment.anId
                }
            }
            var httpSuccess = function success(response) {
                vm.sysFilelists = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_findFileList


        //S_updateIssueState
        function updateIssueState(vm, state) {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: "请选择数据"
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/annountment/updateIssueState",
                    params: {
                        ids: ids.join(','),
                        issueState: state
                    }
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            vm.isSubmit = false;
                            vm.gridOptions.dataSource.read();
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                closeDialog: true
                            })
                        }
                    })
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }

        }//E_updateIssueState

        function findDetailById(vm,id) {
            var httpOptions = {
                method: "get",
                url: url_annountment + "/findAnnountmentById",
                params: {
                    anId: id
                }
            }
            var httpSuccess = function success(response) {
                vm.annountment = response.data;
                findFileList(vm)
                postArticle(vm, id);
                nextArticle(vm, id);
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//end findAnnountmentById

        //begin postArticle
        function postArticle(vm, id) {
            var httpOptions = {
                method: "get",
                url: rootPath + "/annountment/postArticle",
                params: {
                    anId: id
                }
            }

            var httpSuccess = function success(response) {
                vm.annountmentPost = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//end postArticle


        //begin nextArticle
        function nextArticle(vm, id) {
            var httpOptions = {
                method: "get",
                url: rootPath + "/annountment/nextArticle",
                params: {
                    anId: id
                }
            }
            var httpSuccess = function success(response) {
                vm.annountmentNext = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//end nextArticle


    }
})();
(function () {
    'use strict';

    angular.module('app').controller('annountmentYetCtrl', annountmentYet);

    annountmentYet.$inject = ['$location', '$state', '$http', 'annountmentYetSvc'];

    function annountmentYet($location, $state, $http, annountmentYetSvc) {
        var vm = this;
        vm.title = "通知公告列表";
        active();
        function active() {
            annountmentYetSvc.grid(vm);
        }

        //查看详情
        vm.detail = function(id){
            if(id){
                $state.go('annountmentDetail', {id: id});
            }
        }

        //查询
        vm.findAnnountment = function(){
        	vm.gridOptions.dataSource.read();
        }

        
         //重置
        vm.resetAnnountment=function(){
        	var tab=$("#annountmentYetform").find('input,select');
        	$.each(tab,function(i,obj){
        		obj.value="";
        	});
        }
    }
})();
(function () {
    'use strict';

    angular.module('app').factory('annountmentYetSvc', annountmentYet);

    annountmentYet.$inject = ['$http'];

    function annountmentYet($http) {
    	
    	var service={
    		grid : grid
    	};
    	
    	return service;
    	
    	 // begin#grid
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/annountment/findByIssue",$("#annountmentYetform")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        },
                        modifiedDate: {
                            type: "date"
                        }

                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: [
                {
                    field: "createdDate",
                    dir: "desc"
                }
                ]
                	

            });
            // End:dataSource

            //S_序号
            var dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            };
            //S_序号

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.anId)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "unitSort",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "anTitle",
                    title: "标题",
                    width: 300,
                    filterable: false
                },
                {
                    field: "issueDate",
                    title: "发布时间",
                    format: "{0:yyyy-MM-dd hh24:mm:ss}",
                    width: 160,
                    filterable: false
                },
                {
                    field: "issueUser",
                    title: "发布人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 150,
                    template: function (item) {
                    	
                        return common.format($('#columnBtns').html(),"vm.detail('" + item.anId + "')");
                    	
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                dataBound: dataBound,
                columns: columns,
                resizable: true
            };

        }// end fun grid
    	
    	
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('assistPlanCtrl', assistPlan);

    assistPlan.$inject = ['$location','$state','assistSvc','$http','$interval'];

    function assistPlan($location,$state,assistSvc,$http,$interval) {
        var vm = this;
        vm.model = {};							//创建一个form对象
        vm.filterModel = {};                    //filter对象
        vm.filterLow = {};
        vm.title = '协审计划管理';        		//标题
        vm.splitNumArr = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15];
        vm.plan = {};                           //添加的协审对象
        vm.planList = new Array();              //在办协审计划列表
        vm.showPlan = {};                       //显示协审计划信息

        vm.assistSign = new Array();            //待选项目列表
        vm.pickSign = new Array();              //协审计划已选的项目列表
        vm.pickMainSign = new Array();          //主项目对象
        vm.lowerSign = new Array();             //次项目对象
        vm.selectPlanId = "";                   //选择显示的协审计划ID
        vm.selectMainSignId = "";               //查看的主项目ID
        vm.initPickLowSign = false;             //是否初始化选择的次项目信息
        vm.drawType="";

        active();
        function active(){
            assistSvc.initPlanPage(vm);
            assistSvc.initPlanGrid(vm);
            $('#planInfo li').click(function (e) {
                var aObj = $("a",this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#"+showDiv).addClass("active").addClass("in").show(500);
            })
        }

        //待选择过来器
        vm.filterSign = function(item){
            var isMatch = true;
            if(!angular.isUndefined(item)){
                if(!angular.isUndefined(vm.filterModel.filterFilecode)){
                     if((item.filecode).indexOf(vm.filterModel.filterFilecode) == -1){
                         isMatch = false;
                     }
                }
                if(isMatch){
                    if(!angular.isUndefined(vm.filterModel.filterProjectCode)){
                        if((item.projectcode).indexOf(vm.filterModel.filterProjectCode) == -1){
                            isMatch = false;
                        }
                    }
                }
                if(isMatch){
                    if(!angular.isUndefined(vm.filterModel.filterProjectName)){
                        if((item.projectname).indexOf(vm.filterModel.filterProjectName) == -1){
                            isMatch = false;
                        }
                    }
                }
                if(isMatch){
                    if(!angular.isUndefined(vm.filterModel.filterBuiltName)){
                        if(angular.isUndefined(item.builtcompanyName)){
                            isMatch = false;
                        }
                        if(isMatch && (item.builtcompanyName).indexOf(vm.filterModel.filterBuiltName) == -1){
                            isMatch = false;
                        }
                    }
                }
                if(isMatch){
                    return item;
                }
            }
        }

        //次项目待选择器
        vm.filterLowSign = function(item){
            var isMatch = true;
            if(!angular.isUndefined(item)){
                if(!angular.isUndefined(vm.filterLow.filterFilecode)){
                    if((item.filecode).indexOf(vm.filterLow.filterFilecode) == -1){
                        isMatch = false;
                    }
                }
                if(isMatch){
                    if(!angular.isUndefined(vm.filterLow.filterProjectCode)){
                        if((item.projectcode).indexOf(vm.filterLow.filterProjectCode) == -1){
                            isMatch = false;
                        }
                    }
                }
                if(isMatch){
                    if(!angular.isUndefined(vm.filterLow.filterProjectName)){
                        if((item.projectname).indexOf(vm.filterLow.filterProjectName) == -1){
                            isMatch = false;
                        }
                    }
                }
                if(isMatch){
                    if(!angular.isUndefined(vm.filterLow.filterBuiltName)){
                        if(angular.isUndefined(item.builtcompanyName)){
                            isMatch = false;
                        }
                        if(isMatch && (item.builtcompanyName).indexOf(vm.filterLow.filterBuiltName) == -1){
                            isMatch = false;
                        }
                    }
                }
                if(isMatch){
                    return item;
                }
            }
        }


        //重置拆分值
        vm.initSplit = function(typeName){
            if(vm.plan.assistType == typeName){
                if(!angular.isUndefined(vm.plan.spliNum)){
                    vm.plan.spliNum = 0;
                }
            }
        }

        //挑选项目
        vm.affirmSign = function () {
            var isCheckSign = $("input[name='selASTSign']:checked");
            if (isCheckSign.length < 1) {
                common.alert({
                    vm : vm,
                    msg : "请选择要挑选的项目"
                })
            }else{
                if(isCheckSign.length > 1){
                    if(vm.plan.assistType == '合并项目'){
                        common.alert({
                            vm : vm,
                            msg : "合并项目要先挑选一个主项目，再挑选次项目！"
                        })
                    }else{
                        common.alert({
                            vm : vm,
                            msg : "独立项目，每次只能选择一个！"
                        })
                    }
                    return ;
                }else{
                    vm.model.signId = isCheckSign[0].value;
                    vm.model.assistType = vm.plan.assistType;
                    vm.model.single = vm.plan.assistType == '合并项目'?false:true;
                    vm.model.splitNum = vm.plan.spliNum;
                    vm.model.id = vm.selectPlanId;
                    vm.assistSign.forEach(function (st,index) {
                        if(st.signid == vm.model.signId){
                            vm.model.projectName = st.projectname;
                        }
                    });
                    assistSvc.saveAssistPlan(vm);
                }
            }
        }

        //取消
        vm.cancelSign = function(){
            var isCheckSign = $("input[name='checkASTSign']:checked");
            if (isCheckSign.length < 1) {
                common.alert({
                    vm : vm,
                    msg : "请选择取消的项目"
                })
            }else{
                common.confirm({
                    vm: vm,
                    title: "",
                    msg: "确认取消挑选项目吗?",
                    fn: function () {
                        $('.confirmDialog').modal('hide');
                        var ids=[];
                        for (var i = 0; i < isCheckSign.length; i++) {
                            ids.push(isCheckSign[i].value);
                        }
                        assistSvc.cancelPlanSign(vm,ids.join(','));
                    }
                });
            }
        }

        //初始化选择的协审计划信息
        vm.initSelPlan = function(){
            assistSvc.initSelPlan(vm);
        }

        //删除操作
        vm.doDelete  = function(){
           if(vm.showPlan.id){
               common.confirm({
                   vm: vm,
                   title: "",
                   msg: "确认删除数据吗?",
                   fn: function () {
                       $('.confirmDialog').modal('hide');
                       assistSvc.deletePlan(vm);
                   }
               });
           }else{
               common.alert({
                   vm : vm,
                   msg : "请选择要删除的数据"
               })
           }
        }

        //显示次项目信息
        vm.showPickLowSign = function(mainSignId){
            vm.selectMainSignId = mainSignId;
            assistSvc.showPickLowSign(vm);
            //显示次项目窗口
            $("#lowerSignWin").kendoWindow({
                width : "1024px",
                height : "600px",
                title : "次项目信息",
                visible : false,
                modal : true,
                closable : true,
                actions : [ "Pin", "Minimize", "Maximize", "Close" ]
            }).data("kendoWindow").center().open();
        }

        //挑选次项目
        vm.affirmLowerSign = function(){
            var checkSign = $("input[name='selLowSign']:checked");
            if (checkSign.length < 1) {
                common.alert({
                    vm : vm,
                    msg : "请选择要挑选的次项目"
                })
            }else{
                var ids = [];
                for (var i = 0; i < checkSign.length; i++) {
                    ids.push(checkSign[i].value);
                }
                assistSvc.saveLowPlanSign(vm,ids);
            }
        }

        //取消次项目
        vm.cancelLowerSign = function(){
            var checkSign = $("input[name='checkLowSign']:checked");
            if (checkSign.length < 1) {
                common.alert({
                    vm : vm,
                    msg : "请选择要挑选的次项目"
                })
            }else{
                var ids = [];
                for (var i = 0; i < checkSign.length; i++) {
                    ids.push(checkSign[i].value);
                }
                assistSvc.cancelLowPlanSign(vm,ids.join(","));
            }
        }

        //查询协审计划信息
        vm.queryPlan = function () {
            assistSvc.queryPlan(vm);
        }
        
         
        var assistPlanId='';//协审计划Id
        vm.planId=''; //
       
        //查看协审计划的详情信息
        vm.showPlanDetail = function(planId){
            $("#planInfo").kendoWindow({
                width : "1024px",
                height : "600px",
                title : "协审项目清单",
                visible : false,
                modal : true,
                closable : true,
                actions : [ "Pin", "Minimize", "Maximize", "Close" ]
            }).data("kendoWindow").center().open();
           	vm.signNum=0;//抽取单位个数
            assistPlanId=planId;
            vm.planId=planId;
            assistSvc.getPlanSignByPlanId(vm,planId);
        }
        
        vm.ministerOpinionEdit=function (ministerOpinion){	//部长意见
        	common.initIdeaData(vm,$http,ministerOpinion);
        }
        
        vm.viceDirectorOpinionEdit=function(viceDirectorOpinion){	//副主任意见
        	common.initIdeaData(vm,$http,viceDirectorOpinion);
        }
        
        vm.directorOpinionEdit=function (directorOpinion){	//主任意见
        	common.initIdeaData(vm,$http,directorOpinion);
        }
        
        vm.assistPlan={};
        vm.savePlanSign=function(){//保存协审项目信息
	       	assistSvc.savePlanSign(vm);
	        vm.assistPlan.id=assistPlanId;
	       	assistSvc.savePlan(vm);
        }
        
        
       vm.checked='option1';
        vm.chooseAssistUnit=function(){
        	vm.number=0;
        	vm.drawType="";
        	if(vm.checked=='option1'){
        		vm.drawType="1";
        		vm.number=vm.assistPlanSign.length+1;
        	}
        	if(vm.checked=='option2'){
        		vm.drawType="0";
        		vm.number=vm.assistPlanSign.length;
        	}
        	assistSvc.chooseAssistUnit(vm);
        
        }
        
         vm.againChooleAssistUnit=function(){
        	$("#againChooleAssistUnit").kendoWindow({
	        	title:"选择参加协审单位",
	        	width:"600px",
    			height:"500px",
	        	visible : false,
	            modal : true,
	            closable : true,
	            actions : [ "Pin", "Minimize", "Maximize", "Close" ]
            }).data("kendoWindow").center().open();
            
            assistSvc.getAllUnit(vm);
            vm.num=0;
        	if(vm.showPlan.drawType=="0"){
        		vm.num=vm.assistPlanSign.length;
        	}else {
        		console.log(123);
        		vm.num=vm.assistPlanSign.length+1;
        	}
        }
        
        vm.saveAddChooleUnit=function(unitObject){
        	assistSvc.saveAddChooleUnit(vm,unitObject);
        
        }

        //协审项目抽签
        vm.drawAssistUnit = function(){
        	console.log(vm.showPlan.drawType);
            if(vm.assistPlanSign != undefined&&vm.assistPlanSign.length>0){
                vm.assistPlanSign.forEach(function(t,n){
                    t.assistUnit = null;
                });
            }else{
                return ;
            }
            //待被抽取的协审单位
            vm.drawAssistUnits = vm.unitList.slice(0);
            
            //判断协审单位个数是否不少于协审计划个数，若少则先手动选择参与的协审单位，不少则可以直接抽签 drawType
            if(vm.drawType=="1"? (vm.drawAssistUnits.length>vm.assistPlanSign.length):(vm.drawAssistUnits.length>=vm.assistPlanSign.length)){
            
//            var drawAssistPlanSign
            var drawPlanSignIndex = 0;
            var signIndex=-1;//记录被抽取的协审单位下标
            //先让上次轮空的协审单位进行抽取项目
	            for(var i=0;i<vm.drawAssistUnits.length;i++){ //遍历协审单位，判断是否为空，9表示为空，如果为空，则进行抽签协审计划，分配协审单位
	            	if(vm.drawAssistUnits[i].isLastUnSelected=='9'){
	            		var selscope = Math.floor(Math.random()*(vm.assistPlanSign.length));//产生随机数
	            		signIndex=selscope;
	            		vm.assistPlanSign[selscope].assistUnit=vm.drawAssistUnits[i];//将协审单位分配给协审计划
	            		vm.drawPlanSign = vm.assistPlanSign[selscope];
	            		vm.drawAssistUnits.splice(i,1);//将上轮轮空的协审单位移除
	            	}
	            	
	            }
            
            //当前抽取第一个项目的协审单位
            vm.drawPlanSign = vm.assistPlanSign[drawPlanSignIndex];
            var timeCount = 0;
            vm.isStartDraw = true;
            vm.isDrawDone = false;
            vm.t = $interval(function() {
                vm.drawPlanSign = vm.assistPlanSign[drawPlanSignIndex];
                var selscope = Math.floor(Math.random()*(vm.drawAssistUnits.length));
              	var selAssistUnit = vm.drawAssistUnits[selscope];
                vm.showAssitUnitName = selAssistUnit.unitName;
                timeCount++;
                //一秒后，选中协审单位
                if(timeCount % 20 == 0){
                    //选中协审单位
                	if(drawPlanSignIndex!=signIndex){
                    	vm.assistPlanSign[drawPlanSignIndex].assistUnit = selAssistUnit;
                	}else{
                		if(drawPlanSignIndex!=vm.assistPlanSign.length-1){
	                		vm.assistPlanSign[++drawPlanSignIndex].assistUnit = selAssistUnit;
                		}
                	}
                    drawPlanSignIndex ++;
                    if(drawPlanSignIndex==signIndex && signIndex==vm.assistPlanSign.length-1){ //判断轮空抽签的是不是最后一个，并且协审计划轮抽到最后一个时，停止抽签
                    	$interval.cancel(vm.t);
                        vm.isDrawDone = true;
                    }
                    if(drawPlanSignIndex == vm.assistPlanSign.length){
                        //抽签完毕
                        $interval.cancel(vm.t);
                        vm.isDrawDone = true;
                    }

                    vm.drawAssistUnits.forEach(function (t,n){
                        if(t.id == selAssistUnit.id){
                            vm.drawAssistUnits.splice(n,1);
                        }
                    });
                    
            	}
            }, 50);
        }else{
        	common.alert({
        		vm:vm,
        		msg:"当前协审单位少于项目计划数目，不能抽签！请先到项目计划表中选择参加的协审单位后再进行抽签！"
        	});
        }
        }


        vm.saveDrawAssistUnit = function(){
            assistSvc.saveDrawAssistUnit(vm);
        }

    }
})();

(function () {
    'use strict';

    angular.module('app').controller('assistUnitCtrl', assistUnit);

    assistUnit.$inject = ['$location', 'assistUnitSvc'];

    function assistUnit($location, assistUnitSvc) {
        var vm = this;
        vm.title = '协审单位';
        vm.resource = {};

        vm.del = function (id) {
            vm.id = id;
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认要删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    vm.resource = {};
                    assistUnitSvc.deleteAssistUnit(vm, id);
                }
            });
        }

        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: "请选择数据"
                });
            } else {
                common.confirm({
                    vm: vm,
                    title: "",
                    msg: "确认要删除数据吗？",
                    fn: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < selectIds.length; i++) {
                            ids.push(selectIds[i].value);
                        }
                        var idStr = ids.join(",");
                        assistUnitSvc.deleteAssistUnit(vm, idStr);
                    }
                });
            }
        }

        vm.queryAssistUnit = function () {
            assistUnitSvc.queryAssistUnit(vm);
        }

        activate();
        function activate() {
            assistUnitSvc.grid(vm);
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').factory('assistUnitSvc', assistUnit);

    assistUnit.$inject = ['$http'];

    function assistUnit($http) {
        var url_assistUnit = rootPath + "/assistUnit";
        var url_back = '#/assistUnit';
        var service = {
            grid: grid,
            deleteAssistUnit : deleteAssistUnit,			//删除协审单位
            createAssistUnit : createAssistUnit,		//新增协审单位
            updateAssistUnit : updateAssistUnit,		//更新协审单位
            getAssistUnitById : getAssistUnitById,		//通过id查询协审单位
            queryAssistUnit : queryAssistUnit			//模糊查询
            
        };

        return service;
        
        function createAssistUnit(vm){
         	common.initJqValidation();
            var isValid = $('form').valid();
            if(isValid  && vm.isUnitExist==false){
        	var httpOptions={
        		method:"post",
        		url:url_assistUnit,
        		data:vm.assistUnit
        	}
        	var httpSuccess=function success(response){
        		common.requestSuccess({
        			vm:vm,
        			response:response,
        			fn:function(){
        				common.alert({
	        				vm:vm,
	        				msg:"操作成功",
	        				fn:function(){
	        					vm.isSubmit=false;
	        					$('.alertDialog').modal('hide');
	        					$('.modal-backdrop').remove();
	        					location.href=url_back;
	        					
	        				}
        				})
        			}
        		});
        	}
        	
        	common.http({
        		vm:vm,
        		$http:$http,
        		httpOptions:httpOptions,
        		success:httpSuccess
        	});
        
            }
        }
        
        function deleteAssistUnit(vm,id){
        	 vm.isSubmint=true;
        	var httpOptions={
        		method: 'delete',
        		url: url_assistUnit,
        		data: id
        	};
        	
        	var httpSuccess=function success(response){
        		common.requestSuccess({
        			vm:vm,
        			response:response,
        			fn:function(){
        			 common.alert({
        			 	vm:vm,
	        				msg:"操作成功",
	        				fn:function(){
			        			 vm.isSubmit=false;
			        			 $('.alertDialog').modal('hide');
	        					$('.modal-backdrop').remove();
			        			 vm.gridOptions.dataSource.read();
	        				}
        			 });
        			}
        		});
        	
        	};
        	
        	common.http({
        		vm:vm,
        		$http:$http,
        		httpOptions:httpOptions,
        		success:httpSuccess
        	
        	});
        
        }//end create
        
        function updateAssistUnit(vm){
        	 common.initJqValidation();
            var isValid = $('form').valid();
            if(isValid && vm.isUnitExist==false){
        	var httpOptions={
        		method:"put",
        		url:url_assistUnit,
        		headers:{
                 "contentType":"application/json;charset=utf-8"  //设置请求头信息
              },
			  dataType : "json",
			  data:angular.toJson(vm.assistUnit)
//        		data:vm.assistUnit
        	}
        	
        	var httpSuccess=function success(response){
        		common.requestSuccess({
        			vm:vm,
        			response:response,
        			fn:function(){
        				common.alert({
	        				vm:vm,
	        				msg:"操作成功",
	        				fn:function(){
	        					vm.isSubmit=false;
	        					$('.alertDialog').modal('hide');
	        					$('.modal-backdrop').remove();
	        					location.href=url_back;
	        					
	        				}
        				})
        			}
        		});
        	}
        	common.http({
        		vm:vm,
        		$http:$http,
        		httpOptions:httpOptions,
        		success:httpSuccess
        	});
            }
        }//end 
        
        function getAssistUnitById(vm){
        	
        	var httpOptions={
        		method:'get',
        		url: url_assistUnit+'/getAssistUnitById',
        		params:{id:vm.id}
        	}
        	
        	var httpSuccess=function success(response){
        		vm.assistUnit=response.data;
        	}
        	
        	common.http({
        		vm:vm,
        		$http:$http,
        		httpOptions:httpOptions,
        		success:httpSuccess
        	});
        }//end
        
        function queryAssistUnit(vm){
        	 vm.gridOptions.dataSource.read();
        }

     
        // begin#grid
        function grid(vm) {

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_assistUnit+"/fingByOData",$("#assistUnitform")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        },
                        modifiedDate: {
                        	type: "date"
                        },
                        isUse:{
                        },
                        unitSort:{
                        }
                        
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: [
               /* {
                	 field: "isUse",
	                    dir: "desc"
                },*/
                 {
	                    field: "unitSort",
	                    dir: "asc"
	                }
                ]
	               
            });

            // End:dataSource

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "unitSort",
                    title: "序号",
                    width: 50,
                    filterable: false
                },
                {
                    field: "unitName",
                    title: "单位名称",
                    width: 100,
                    filterable: true
                },
                {
                    field: "unitShortName",
                    title: "单位简称",
                    width: 100,
                    filterable: true
                },
               /* {
                    field: "phoneNum",
                    title: "电话号码",
                    width: 100,
                    filterable: false
                },*/
               
                {
                    field: "principalName",
                    title: "负责人名称",
                    width: 100,
                    filterable: false
                },
                {
                    field: "principalPhone",
                    title: "负责人电话",
                    width: 100,
                    filterable: false
                },
                {
                    field: "fax",
                    title: "负责人传真",
                    width: 100,
                    filterable: false
                },
               
                {
                    field: "contactName",
                    title: "联系人名称",
                    width: 100,
                    filterable: false
                },
                {
                    field: "contactTell",
                    title: "联系人手机号",
                    width: 100,
                    filterable: false
                },
                {
                    field: "address",
                    title: "企业地址",
                    width: 100,
                    filterable: true
                },
                {
                    field: "isUse",
                    title: "状态",
                    width: 100,
                    filterable: false,
                    template:function(item){
                    	if(item.isUse){
                    		if(item.isUse=="0"){
                    		
                    			return "已停用";
                    		}
                    		if(item.isUse=="1"){
                    		
                    			return "在用";
                    		}
                    	}else{
                    		return "";
                    	}
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.del('" + item.id + "')", item.id,item.isUse);
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true
            };

        }// end fun grid

    }
})();
(function () {
    'use strict';

    angular.module('app').controller('assistUnitEditCtrl', assistUnitEdit);

    assistUnitEdit.$inject = ['$location', 'assistUnitSvc','$state'];

    function assistUnitEdit($location, assistUnitSvc,$state) {
        var vm = this;
        vm.title = '新增协审单位';
        vm.id=$state.params.id;
        vm.isUnitExist=false;
        if(vm.id){
        	vm.isUpdate=true;
        	vm.title='更新协审单位';
        }
        
        vm.create=function(){
          assistUnitSvc.createAssistUnit(vm);
        }
        
        vm.update=function(){
        	assistUnitSvc.updateAssistUnit(vm);
        }
        

        activate();
        function activate() {
        	if(vm.isUpdate){
        		assistUnitSvc.getAssistUnitById(vm);
        	}
        	
        }
    }
})();

(function() {
	'use strict';
	
	angular.module('app').factory('assistSvc', assist);

    assist.$inject = ['$http','$state'];

	function assist($http,$state) {
		var service = {
            initPlanPage : initPlanPage,						//初始化计划方案表
            initPlanGrid : initPlanGrid,                        //舒适化表格
            saveAssistPlan : saveAssistPlan,                    //保存协审计划
            deletePlan : deletePlan,                            //删除协审计划包
            findPlanSign : findPlanSign,                        //根据计划ID查找收文选择的收文信息
            cancelPlanSign : cancelPlanSign,                    //取消挑选项目
            saveLowPlanSign : saveLowPlanSign,                  //保存挑选的次项目
            cancelLowPlanSign : cancelLowPlanSign,              //取消次项目
            initSelPlan : initSelPlan,                          //初始化选择的计划信息
            showPickLowSign : showPickLowSign,                  //初始化选择的次项目信息
            queryPlan : queryPlan,                              //查询协审计划信息
            getPlanSignByPlanId : getPlanSignByPlanId,			//通过协审计划id或取协审项目信息
            savePlanSign : savePlanSign,						//保存协审项目信息
            savePlan : savePlan,								//保存协审计划
            initPlanByPlanId : initPlanByPlanId,				//初始化协审计划
            chooseAssistUnit : chooseAssistUnit,				//选择协审单位
            saveDrawAssistUnit:saveDrawAssistUnit,              //保存协审计划抽签
            getUnitUser : getUnitUser,
            getAllUnit : getAllUnit,			                    //获取所有的协审单位
            saveAddChooleUnit : saveAddChooleUnit,		//保存手动选择的协审单位
            initAssistUnitByPlanId : initAssistUnitByPlanId	//初始化计划项目的协审单位
            
		};
		return service;

        function getPlanColumns(){
            var columns = [
                {
                    field: "rowNumber",
                    title: "序号",
                    width: 50,
                    template: "<span class='row-number'></span>"
                },
                {
                    field : "planName",
                    title : "协审计划名称",
                    width : 100,
                    filterable : false
                },
                {
                    field : "reportTime",
                    title : "报审时间",
                    width : 50,
                    filterable : false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field : "drawTime",
                    title : "抽签时间",
                    width : 100,
                    filterable : false
                },
                {
                    field : "createdBy",
                    title : "创建人员",
                    width : 100,
                    filterable : false
                },
                {
                    field : "createdDate",
                    title : "记录生成时间",
                    width : 100,
                    filterable : false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field : "",
                    title : "操作",
                    width : 100,
                    filterable : false,
                    template : function(item) {
                        return '<button class="btn btn-xs btn-primary"  ng-click="vm.showPlanDetail(\''+item.id+'\')"><span class="glyphicon glyphicon-edit"></span>详情</button>';
                    }
                }
            ];
            return columns;
        }

        //S_initPlanGrid
        function initPlanGrid(vm){
            //2、初始化grid
            var  dataSource = common.kendoGridDataSource(rootPath+"/assistPlan/findByOData",$("#searchform"));
            var  dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            }

            // End:column
            vm.gridOptions = {
                dataSource : common.gridDataSource(dataSource),
                filterable : common.kendoGridConfig().filterable,
                pageable : common.kendoGridConfig().pageable,
                noRecords : common.kendoGridConfig().noRecordMessage,
                columns : getPlanColumns(),
                dataBound:dataBound,
                resizable : true
            };

        }//E_initPlanGrid

		//S_initPlanPage
		function initPlanPage(vm){
            //1、查找正在办理的项目概算流程
            var httpOptions = {
                method : 'get',
                url : rootPath+"/assistPlan/initPlanManager",
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm:vm,
                    response:response,
                    fn:function() {
                        vm.planList = new Array();
                        if(response.data.signList){
                            vm.assistSign = response.data.signList;
                        }
                        if(response.data.planList && response.data.planList.length > 0){
                            vm.planList = response.data.planList;
                            //如果之前有选择，则默认显示选择的协审计划，否则默认显示第一个
                            if(!vm.selectPlanId){
                                vm.showPlan = response.data.planList[0];
                                vm.selectPlanId = vm.showPlan.id;
                            }
                            //初始化显示的协审计划信息
                            initSelPlan(vm);
                        }
                    }
                });
            }
            common.http({
                vm:vm,
                $http:$http,
                httpOptions:httpOptions,
                success:httpSuccess
            });
		}//E_initPlanPage

        //S_saveAssistPlan
        function saveAssistPlan(vm){
        	vm.model.isDrawed="0";
            var url = rootPath+"/assistPlan";
            var httpOptions = {
                method : 'post',
                url : url,
                data : vm.model
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm:vm,
                    response:response,
                    fn:function() {
                        vm.iscommit = false;
                        //如果是新增，则重新刷新列表
                        if(!vm.showPlan.id){
                            vm.gridOptions.dataSource.read();
                        }
                        vm.showPlan = response.data;
                        initPlanPage(vm);

                        //如果是合并对象，则选择次项目
                        if(vm.plan.assistType == '合并项目'){
                            vm.showPickLowSign(vm.model.signId);
                        }else{
                            common.alert({
                                vm:vm,
                                msg:"操作成功！",
                                closeDialog:true
                            })
                        }
                    }
                });
            }
            common.http({
                vm:vm,
                $http:$http,
                httpOptions:httpOptions,
                success:httpSuccess
            });
        }//E_saveAssistPlan

        //S_deletePlan
        function deletePlan(vm){
            var httpOptions = {
                method : 'delete',
                url : rootPath+"/assistPlan",
                data : vm.showPlan.id,
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm:vm,
                    response:response,
                    fn:function() {
                        vm.iscommit = false;
                        common.alert({
                            vm:vm,
                            msg:"操作成功！",
                            fn : function() {
                                $('.alertDialog').modal('hide');
                                initPlanPage(vm);
                                //刷新列表信息
                                vm.gridOptions.dataSource.read();
                            }
                        })
                    }
                });
            }
            common.http({
                vm:vm,
                $http:$http,
                httpOptions:httpOptions,
                success:httpSuccess,
                onError:function(){vm.iscommit = false;}
            });
        }//E_deletePlan

        //S_findPlanSign
        function findPlanSign(vm,planId){
            var httpOptions = {
                method : 'get',
                url : rootPath+"/sign/findByPlanId",
                params : {planId : planId},
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm:vm,
                    response:response,
                    fn:function() {
                        vm.pickSign = response.data;             //已选项目列表
                        vm.pickMainSign = new Array();          //主项目对象全部清空
                        vm.lowerSign = new Array();             //次项目对象

                        //挑选主项目
                        if(vm.showPlan.assistPlanSignDtoList){
                            vm.pickSign.forEach(function(ps,index) {
                                vm.showPlan.assistPlanSignDtoList.forEach(function (apsl, number) {
                                    if (apsl.isMain == '9' && apsl.signId == ps.signid) {
                                        //添加评审类型属性
                                        ps.assistType = apsl.assistType;
                                        vm.pickMainSign.push(ps);
                                    }
                                });
                            });
                        }

                        if(vm.initPickLowSign == true){
                            showPickLowSign(vm);
                        }
                    }
                });
            }
            common.http({
                vm:vm,
                $http:$http,
                httpOptions:httpOptions,
                success:httpSuccess,
                onError:function(){vm.iscommit = false;}
            });
        }//E_findPlanSign

        //S_cancelPlanSign
        function cancelPlanSign(vm,signIds){
            var httpOptions = {
                method : 'delete',
                url : rootPath+"/assistPlan/cancelPlanSign",
                params : {
                    planId : vm.selectPlanId,
                    signIds : signIds
                },
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm:vm,
                    response:response,
                    fn:function() {
                        initPlanPage(vm);
                        common.alert({
                            vm:vm,
                            msg:"操作成功！",
                            closeDialog:true
                        })
                    }
                });
            }
            common.http({
                vm:vm,
                $http:$http,
                httpOptions:httpOptions,
                success:httpSuccess,
                onError:function(){vm.iscommit = false;}
            });
        }//E_cancelPlanSign

        //S_saveLowPlanSign
        function saveLowPlanSign(vm,signIdArr){
           var saveLowSignArr = new Array();
           vm.assistSign.forEach(function(asts,index){
               for(var i=0,l=signIdArr.length;i<l;i++){
                   if(asts.signid == signIdArr[i]){
                       var LowSign = {};
                       LowSign.signId = asts.signid;
                       LowSign.projectName = asts.projectname;
                       LowSign.assistType = '合并项目';
                       LowSign.isMain = '0';
                       LowSign.mainSignId = vm.selectMainSignId;
                       saveLowSignArr.push(LowSign);
                   }
               }
           });

           vm.model = vm.showPlan;
           vm.model.assistPlanSignDtoList = saveLowSignArr;
           vm.iscommit = true;
           var httpOptions = {
                method : 'post',
                url : rootPath+"/assistPlan/saveLowPlanSign",
                data : vm.model,
           }
           var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm:vm,
                    response:response,
                    fn:function() {
                        vm.iscommit = false;
                        vm.initPickLowSign = true;
                        initPlanPage(vm);
                        common.alert({
                            vm:vm,
                            msg:"操作成功！",
                            closeDialog:true
                        })
                    }
                });
            }
            common.http({
                vm:vm,
                $http:$http,
                httpOptions:httpOptions,
                success:httpSuccess,
                onError:function(){vm.iscommit = false;}
            });
        }//E_saveLowPlanSign

        //S_cancelLowPlanSign
        function cancelLowPlanSign(vm,signIds){
            vm.iscommit = true;
            var httpOptions = {
                method : 'delete',
                url : rootPath+"/assistPlan/cancelLowPlanSign",
                params : {
                    planId : vm.showPlan.id,
                    signIds : signIds
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm:vm,
                    response:response,
                    fn:function() {
                        vm.iscommit = false;
                        vm.initPickLowSign = true;
                        initPlanPage(vm);
                        common.alert({
                            vm:vm,
                            msg:"操作成功！",
                            closeDialog:true
                        })
                    }
                });
            }
            common.http({
                vm:vm,
                $http:$http,
                httpOptions:httpOptions,
                success:httpSuccess,
                onError:function(){vm.iscommit = false;}
            });
        }//E_cancelLowPlanSign

        //S_initSelPlan
        function initSelPlan(vm){
            if(vm.selectPlanId){
                vm.planList.forEach(function(ps,number){
                    if(ps.id == vm.selectPlanId){
                        vm.showPlan = ps;
                        vm.drawType=vm.showPlan.drawType;
                        console.log(vm.drawType);
                    }
                });
                findPlanSign(vm,vm.selectPlanId);
            }else{
                //全部初始化
                vm.showPlan = {};                       //显示协审计划信息
                vm.pickSign = new Array();              //协审计划已选的项目列表
                vm.pickMainSign = new Array();          //主项目对象
                vm.lowerSign = new Array();             //次项目对象
                vm.selectMainSignId = "";               //查看的主项目ID
                vm.initPickLowSign = false;             //是否初始化选择的次项目信息
            }
        }//E_initSelPlan

        //S_showPickLowSign
        function showPickLowSign(vm){
            vm.lowerSign = new Array();
            vm.pickSign.forEach(function(ps,number){
                vm.showPlan.assistPlanSignDtoList.forEach(function(lps,index){
                    if(lps.isMain == '0'  && lps.mainSignId == vm.selectMainSignId && lps.signId == ps.signid){
                        vm.lowerSign.push(ps);
                    }
                });
            });
        }//E_showPickLowSign

        //S_queryPlan
        function queryPlan(vm){
            vm.gridOptions.dataSource.read();
        }//E_queryPlan
        
        //begin getPlanSignByPlan
        function getPlanSignByPlanId(vm,planId){
        	vm.reviewNum=''; //几个评审单位
	        var httpOptions={
	        	method:'get',
	        	url:rootPath+'/assistPlanSign/getPlanSignByPlanId',
	        	params:{planId:planId}
	        }
	        var httpSuccess=function success(response){
	        	vm.assistPlanSign=response.data;
	        	vm.reviewNum=vm.assistPlanSign.length;
	        	 if(vm.assistPlanSign.length > 0){
			           initPlanByPlanId(vm,planId);//初始化协审计划
			           initAssistUnitByPlanId(vm,planId);//初始化协审单位
//			           getUnitUser(vm);
           		}
	        }
	        
	        common.http({
	        	vm:vm,
	        	$http:$http,
	        	httpOptions:httpOptions,
	        	success:httpSuccess
	        });
        	
        }//end 
        
        //begin savePlanSign
        function savePlanSign(vm){
        	vm.assistPlan.ministerOpinion=$("#ministerOpinion").val();
        	vm.assistPlan.viceDirectorOpinion=$("#viceDirectorOpinion").val();
        	vm.assistPlan.directorOpinion=$("#directorOpinion").val();
        	var httpOptions={
        		method:"put",
        		url: rootPath +"/assistPlanSign/savePlanSign",
        		headers:{
                 "contentType":"application/json;charset=utf-8"  //设置请求头信息
              },
			  dataType : "json",
			  data:angular.toJson(vm.assistPlanSign)
        	}
        	
        	 var httpSuccess=function success(response){
        	 	}
	        
	        common.http({
	        	vm:vm,
	        	$http:$http,
	        	httpOptions:httpOptions,
	        	success:httpSuccess
	        });
        }
        //end savePlanSign
        
        //begin savePlan
        function savePlan(vm){
        var httpOptions={
        		method:"put",
        		url: rootPath +"/assistPlan",
        		data:vm.assistPlan
        	}
        	 var httpSuccess=function success(response){
        	 	alert("保存成功！");
        	 	 window.parent.$("#planInfo").data("kendoWindow").close();
	        }
	        
	        common.http({
	        	vm:vm,
	        	$http:$http,
	        	httpOptions:httpOptions,
	        	success:httpSuccess
	        });
        	
        }
        //end savePlan
        
        //begin initPlanByPlanId
        function initPlanByPlanId(vm,planId){
        	var httpOptions={
        		method:"get",
        		url:rootPath+'/assistPlan/html/findById',
        		params:{id:planId}
        	}
        	
        	var httpSuccess=function success(response){
        		vm.assistPlan=response.data;
        	}
        	
        	common.http({
        		vm: vm,
        		$http: $http,
        		httpOptions: httpOptions,
        		success: httpSuccess
        	});
        	
        }//end initPlanByPlanId

        //begin chooseAssistUnit
        function chooseAssistUnit(vm){
        	var httpOptions={
        		method:"get",
        		url:rootPath+'/assistUnit/chooseAssistUnit',
        		params:{planId:vm.planId,number:vm.number,drawType:vm.drawType}
        	}
        	
        	var httpSuccess=function success(response){
        		vm.unitList=response.data;
        		vm.signNum=vm.unitList.length;
        		vm.isChoose=true;
        	}
        	
        	common.http({
        		vm: vm,
        		$http: $http,
        		httpOptions: httpOptions,
        		success: httpSuccess
        	});
        }//end chooseAssistUnit
        
        
         // begin  getUnitUser
        function getUnitUser(vm){
        	var httpOptions={
        		method:"post",
        		url:rootPath+'/assistUnitUser/findByOData'
        	}
        	
        	var httpSuccess=function success(response){
        		vm.unitUserList=response.data.value;
        		
        	}
        	
        	common.http({
        		vm: vm,
        		$http: $http,
        		httpOptions: httpOptions,
        		success: httpSuccess
        	});
        	
        }
        //end getUnitUser
        
        //begin getAllUnit
        function getAllUnit(vm){
        	var httpOptions={
        		method:"post",
        		url:rootPath+'/assistUnit/fingByOData'
        	}
        	
        	var httpSuccess=function success(response){
        		vm.allUnitList=response.data.value;
        		
        	}
        	
        	common.http({
        		vm: vm,
        		$http: $http,
        		httpOptions: httpOptions,
        		success: httpSuccess
        	});
        }
        //end  getAllUnit

        //begin saveDrawAssistUnit
        function saveDrawAssistUnit(vm){
            var ids = '';
            var length = vm.assistPlanSign.length;
            vm.assistPlanSign.forEach(function(t,n){
                //格式,AssistPlanSign.id|AssistUnit.id,,,
                ids += (t.id+'|'+t.assistUnit.id);
                if(n != (length-1)){
                    ids += ',';
                }
            });

            var unSelectedIds = '';
            if(vm.drawAssistUnits.length>0){
                var dauLength = vm.drawAssistUnits.length;
                vm.drawAssistUnits.forEach(function(t,n){
                    //格式,AssistPlanSign.id|AssistUnit.id,,,
                    unSelectedIds += t.id;
                    if(n != (dauLength-1)){
                        unSelectedIds += ',';
                    }
                });
            }

            vm.iscommit = true;
            var httpOptions = {
                method : 'put',
                url : rootPath+"/assistPlan/saveDrawAssistUnit",
                params : {planId:vm.planId,drawAssitUnitIds:ids,unSelectedIds:unSelectedIds}
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm:vm,
                    response:response,
                    fn:function() {
                        vm.iscommit = false;
                        vm.isCommited = true;
                        common.alert({
                            closeDialog:true,
                            vm:vm,
                            msg:"操作成功！"
                        })
                    }
                });
            }
            common.http({
                vm:vm,
                $http:$http,
                httpOptions:httpOptions,
                success:httpSuccess,
                onError: function(response){vm.iscommit = false;}
            });
        }
        //end saveDrawAssistUnit
        
        //begin saveAddChooleUnit
        function saveAddChooleUnit(vm,unitObject){
        	if(vm.unitList.length<vm.num){
	        	var i=0;
	        	vm.unitList.forEach(function(x){
	        		if(unitObject.id == x.id){
	        			i=-1;
	        			common.alert({
	        				vm:vm,
	        				msg:"该协审单位已被选中！"
	        			});
	        			return;
	        		}
	        	});
	        	if(i!=-1){
	        		var httpOptions={
	        			method:"post",
	        			url:rootPath+"/assistPlan/saveChooleUnit",
	        			params:{planId:vm.showPlan.id,unitId:unitObject.id}
	        		}
	        		var httpSuccess=function success(response){
		        		common.alert({
		        			vm:vm,
	        				msg:"添加成功！"
	        			});
	        		}
	        		
	        		common.http({
		                vm:vm,
		                $http:$http,
		                httpOptions:httpOptions,
		                success:httpSuccess
	           		});
	        	}
        	}else{
        		common.alert({
        				vm:vm,
        				msg:"当前只能"+vm.num+"家单位参与抽签"
        			});
        	}
        }
        //end saveAddChooleUnit
        
        //begin initAssistUnitByPlanId
        function initAssistUnitByPlanId(vm){
        	var httpOptions={
        		method : "get",
        		url : rootPath+"/assistPlan/initAssistUnit",
        		params:{planId : vm.showPlan.id }
        	}
        	
        	var httpSuccess=function success(response){
		        	vm.unitList=response.data;	
		        	vm.signNum=vm.unitList.length;
		        	if(vm.signNum>0){
			        	vm.isChoose=true;
		        	}
	        }
	        		
	        common.http({
		          vm:vm,
		          $http:$http,
		          httpOptions:httpOptions,
		          success:httpSuccess
	         });
        	
        }
        //end  initAssistUnitByPlanId

	}		
})();
/**
 * 自定义弹窗窗口（在对应的方法中注入bsWin对象）
 * 提示窗口使用方法：
 *  bsWin.alert("消息提示", "操作成功！", function () { console.log("This's close!"); } );
 * 或者
 *  bsWin.alert({
 *      title: "消息提示",
 *      message: "操作成功！",
 *      onClose: function () { console.log("This's close!"); }
 *  });
 * 询问窗口使用方法：
 *  bsWin.confirm("询问提示", "是否进行该操作！", function () { console.log("This's ok!"); }, function () { console.log("This's close!"); },function(){console.log("This's onCancel!");} );
 * 或者
 *  bsWin.confirm({
 *      title: "询问提示",
 *      message: "是否进行该操作！",
 *      onOk: function () { console.log("This's ok!"); },
 *      onClose: function () { console.log("This's close!"); },
 *      onCancel:function(){console.log("This's onCancel!");}
 *  });
 */
(function () {
    'use strict';

    var app = angular.module('app'), alertTplPath = "template/dialog/alert.html",
        confirmTplPath = "template/dialog/confirm.html";

    app.factory('bsWin', ["$rootScope", "$injector", "$templateCache", "bsWinConfig",
        function ($rootScope, $injector, $templateCache, bsWinConfig) {
            var index = 0;

            return {
                success: function (message, onClose) {
                    return this.alert({
                        message: message,
                        type: "success",
                        onClose: onClose
                    });
                },
                warning:  function (message, onClose) {
                    return this.alert({
                        message: message,
                        type: "warning",
                        onClose: onClose
                    });
                },
                error: function (message, onClose) {
                    return this.alert({
                        message: message,
                        type: "error",
                        onClose: onClose
                    });
                },
                alert: function (options, message, onClose) {
                    if (!angular.isObject(options)) {
                        var title;
                        if (!message || angular.isFunction(message)) {
                            onClose = message;
                            message = options;
                        } else {
                            title = options;
                        }

                        options = {
                            message: message,
                            onClose: onClose || function () { }
                        };
                        if (title) {
                            options.title = title;
                        }
                    }
                    options.templateUrl = alertTplPath;
                    return initWin(options);
                },
                confirm: function (options, message, onOk, onClose,onCancel) {
                    if (!angular.isObject(options)) {
                        var title;
                        if (!message || angular.isFunction(message)) {
                            onCancel = onClose || function(){
                                };
                            onClose = onOk || function () {
                                };
                            onOk = message || function () {
                                };
                            message = options;
                        } else {
                            title = options;
                        }

                        options = {
                            message: message,
                            onOk: onOk,
                            onClose: onClose,
                            onCancel: onCancel
                        };
                        if (title) {
                            options.title = title;
                        }
                    }
                    options.templateUrl = confirmTplPath;
                    return initWin(options);
                }
            };

            function initWin(opt) {
                if (opt.msg) {
                    opt.message = opt.msg;
                }
                var options = angular.extend({}, bsWinConfig, opt);

                var newWin = {
                    winId: index++,
                    scope: $rootScope.$new()
                };

                createScope(newWin, options);

                if (options.onOk) {
                    newWin.scope.ok = function () {
                        if (options.onOk && options.onOk.apply(this) == false) {
                            return false;
                        }
                        newWin.el.modal("hide");
                    }
                }
                if(options.onCancel){
                    newWin.scope.onCancel = function () {
                        if (options.onCancel && options.onCancel.apply(this) == false) {
                            return false;
                        }
                        newWin.el.modal("hide");
                    }
                }
                newWin.el = createWinEl(newWin.scope, options.templateUrl)
                    .on('hidden.bs.modal', function (e) {
                        if (options.onClose && options.onClose.apply(this, e) == false) {
                            return false;
                        }
                        destroy(newWin);
                    }).modal('show');  // 打开
                return newWin;
            }

            function createScope(win, options) {
                // console.log("bsWin createScope", options);
                win.scope.title = options.title;
                win.scope.message = options.message;

                win.scope.winId = win.winId;
                win.scope.type = options.type;
                win.scope.extraData = options.extraData;

                win.scope.options = {
                    onClose: options.onClose
                };
            }

            function createWinEl(scope, tpl) {
                // debugger;
                var $compile = $injector.get("$compile"),
                    elHtml = $compile($templateCache.get(tpl))(scope);
                return $(elHtml).appendTo("body");
            }

            /**
             * 销毁窗口
             * @param el
             */
            function destroy(win) {
                // console.log("bsWin destroy", win);
                win.scope.$destroy();
                win.el.unbind();
                win.el.remove();
                win.el = null;
                win = null;
            }

        }]);


    /**
     * 窗口默认配置
     */
    app.constant('bsWinConfig', {
        title: "提示窗口",
        message: "",
        type: "info",
        timeout: 0,
        templateUrl: alertTplPath,
        onOk: null,
        onClose: function (e) {
        }
    });

    app.run(["$templateCache", function ($templateCache) {
        // 提示窗口模板
        $templateCache.put(alertTplPath,
            '<div class="alertDialog modal fade" tabindex="-1" role="dialog">\
                <div class="modal-dialog" role="document" style="margin:80px auto;width:80%;max-width:400px;">\
                    <div class="modal-content">\
                        <div class="modal-header">\
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>\
                            <h4 class="modal-title text-info">{{title || \'消息提醒\'}}</h4>\
                        </div>\
                        <div class="modal-body text-{{type==\'error\'? \'danger\' : type}}">\
                            <p class="alertDialogMessage">\
                                <i class="fa fa-{{type == \'success\' ? \'check\' : (type == \'error\' ? \'ban\' : type)}}" aria-hidden="true"></i> \
                                {{message}}\
                            </p>\
                        </div>\
                        <div class="modal-footer">\
                            <button type="button"  class="btn btn-info" data-dismiss="modal">关闭</button>\
                        </div>\
                    </div>\
                </div>\
            </div>');

        // 询问窗口模板
        $templateCache.put(confirmTplPath,
            '<div class="confirmDialog modal fade" tabindex="-1" role="dialog" style="z-index: 10000;">\
                <div class="modal-dialog" role="document" style="margin:80px auto;width:80%;max-width:400px;">\
                    <div class="modal-content">\
                        <div class="modal-header">\
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>\
                            <h4 class="modal-title text-info">{{title||\'提示\'}}</h4>\
                        </div>\
                        <div class="modal-body text-primary"><p><i class="fa fa-question-circle" aria-hidden="true"></i> {{message}}</p></div>\
                        <div class="modal-footer">\
                            <button type="button" ng-click="ok()" class="btn btn-info" >确认</button>\
                            <button type="button" ng-click="onCancel()" class="btn btn-info" data-dismiss="modal">取消</button>\
                        </div>\
                    </div>\
                </div>\
            </div>');
    }]);
})();
(function () {
    'use strict';
    var DICT_ITEMS;    //数字字典
    var service = {
        initJqValidation: initJqValidation,         // 重置form验证
        requestSuccess: requestSuccess,             // 请求成功时执行
        format: format,                             // string格式化
        blockNonNumber: blockNonNumber,             // 只允许输入数字
        floatNumberInput: floatNumberInput,
        adminContentHeight: adminContentHeight,     // 当前Content高度
        alert: alertDialog,                         // 显示alert窗口
        confirm: confirmDialog,                     // 显示Confirm窗口
        getQuerystring: getQuerystring,             // 取得Url参数
        kendoGridConfig: kendoGridConfig,           // kendo grid配置
        getKendoCheckId: getKendoCheckId,           // 获得kendo grid的第一列checkId
        cookie: cookie,                             // cookie操作
        getToken: getToken,                         // 获得令牌
        appPath: "",                                // app路径
        http: http,                                 // http请求
        gridDataSource: gridDataSource,             // gridDataSource
        loginUrl: window.rootPath + '/home/login',
        buildOdataFilter: buildOdataFilter,         // 创建多条件查询的filter
        initDictData: initDictData,                 // 初始化数字字典
        kendoGridDataSource: kendoGridDataSource,   // 获取gridDataSource
        initUploadOption: initUploadOption,         // 附件上传参数(已停用)
        getTaskCount: getTaskCount,                 // 用户待办总数
        getPauseProjectCount:getPauseProjectCount,  // 待办暂停项目个数
        initIdeaData: initIdeaData,                 // 初始化选择意见窗口数据
        deleteCommonIdea: deleteCommonIdea,         // 删除常用意见
        addCommonIdea: addCommonIdea,               // 添加常用意见
        saveCommonIdea: saveCommonIdea,             // 保存常用意见
        addCorrentIdea: addCorrentIdea,             // 添加当前意见
        saveCurrentIdea: saveCurrentIdea,           // 绑定当前意见
        initDictItems: function (dictList) {
            DICT_ITEMS = dictList;
        }
    };
    window.common = service;

    function initJqValidation(formObj) {
        if (formObj) {
            formObj.removeData("validator");
            formObj.removeData("unobtrusiveValidation");
            $.validator.unobtrusive.parse(formObj);
        } else {
            $("form").removeData("validator");
            $("form").removeData("unobtrusiveValidation");
            $.validator.unobtrusive.parse("form");
        }
    }//end

    function requestSuccess(options) {
        var showError = function (msg) {
            service.alert({
                vm: options.vm,
                msg: msg,
                fn: function () {
                    options.vm.isSubmit = false;
                    $('.alertDialog').modal('hide');
                }
            });
        };
        if (options.response.status > 400) {
            showError("发生错误！");
        } else {
            var data = options.response.data;
            if (data && data.status == 555) {
                showError(data.message);
            } else if (options.fn) {
                options.fn(data);
            }
        }
    }//end

    function format() {
        var theString = arguments[0];
        for (var i = 1; i < arguments.length; i++) {
            var regEx = new RegExp("\\{" + (i - 1) + "\\}", "gm");
            theString = theString.replace(regEx, arguments[i]);
        }
        return theString;
    }//end

    function blockNonNumber(val) {
        var str = val.toString().replace(/[^0-9]/g, '');
        return parseInt(str, 10);
    }//end

    function floatNumberInput(val) {
        return isNaN(parseFloat(val, 10)) ? 0 : parseFloat(val, 10);
    }//end

    function adminContentHeight() {
        return $(window).height() - 180;
    }//end

    function alertDialog(options) {
        options.vm.alertDialogMessage = options.msg;
        options.vm.alertDialogFn = function () {
            if (options.closeDialog && options.closeDialog == true) {
                $('.alertDialog').modal('hide');
                $('.modal-backdrop').remove();
            }
            if (options.fn) {
                options.fn();
            } else {
                $('.alertDialog').modal('hide');
            }
        };
        $('.alertDialog').modal({
            backdrop: 'static',
            keyboard: false
        });
    }//end

    function confirmDialog(options) {
        options.vm.dialogConfirmTitle = options.title;
        options.vm.dialogConfirmMessage = options.msg;
        $('.confirmDialog').modal({
            backdrop: 'static'
        });
        options.vm.dialogConfirmSubmit = options.fn;
        if (options.cancel) {
            options.vm.dialogConfirmCancel = options.cancel;
        } else {
            options.vm.dialogConfirmCancel = function () {
                $('.confirmDialog').modal('hide');
            }
        }
    }//end

    function getQuerystring(key, default_) {
        if (default_ == null)
            default_ = "";
        key = key.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
        var regex = new RegExp("[\\?&]" + key + "=([^&#]*)");
        var qs = regex.exec(window.location.href);
        if (qs == null)
            return default_;
        else
            return qs[1];
    }//end

    function kendoGridDataSource(url, searchForm) {
        var dataSource = new kendo.data.DataSource({
            type: 'odata',
            transport: kendoGridConfig().transport(url, searchForm),
            schema: kendoGridConfig().schema({
                id: "id",
                fields: {
                    createdDate: {
                        type: "date"
                    }
                }
            }),
            serverPaging: true,
            serverSorting: true,
            serverFiltering: true,
            pageSize: 10,
            sort: {
                field: "createdDate",
                dir: "desc"
            }
        });
        return dataSource;
    }//end

    function kendoGridConfig() {
        return {
            filterable: {
                extra: false,
                operators: {
                    string: {
                        "contains": "包含",
                        "eq": "等于"
                    },
                    number: {
                        "eq": "等于",
                        "neq": "不等于",
                        gt: "大于",
                        lt: "小于"
                    },
                    date: {
                        gt: "大于",
                        lt: "小于"
                    }
                }
            },
            pageable: {
                pageSize: 10,
                previousNext: true,
                buttonCount: 5,
                refresh: true,
                pageSizes: true
            },
            schema: function (model) {
                return {
                    data: "value",
                    total: function (data) {
                        return data['count'];
                    },
                    model: model
                };
            },
            transport: function (url, form, paramObj) {
                return {
                    read: {
                        url: url,
                        dataType: "json",
                        type: "post",
                        beforeSend: function (req) {
                            req.setRequestHeader('Token', service.getToken());
                        },
                        data: function () {
                            if (form) {
                                var filterParam = common.buildOdataFilter(form);
                                if (filterParam) {
                                    if (paramObj && paramObj.filter) {
                                        return {
                                            "$filter": filterParam + " and "
                                            + paramObj.filter
                                        };
                                    } else {
                                        return {
                                            "$filter": filterParam
                                        };
                                    }
                                } else {
                                    if (paramObj && paramObj.filter) {
                                        return {
                                            "$filter": paramObj.filter
                                        };
                                    } else {
                                        return {};
                                    }
                                }
                            } else {
                                return {};
                            }
                        }
                    }
                }
            },
            noRecordMessage: {
                template: '暂时没有数据.'
            }
        }
    }//end

    function getKendoCheckId($id) {
        var checkbox = $($id).find('tr td:nth-child(1)').find('input:checked')
        var data = [];
        checkbox.each(function () {
            var id = $(this).attr('relId');
            data.push({
                name: 'id',
                value: id
            });
        });
        return data;
    }//end

    function http(options) {
        options.headers = {
            Token: service.getToken()
        };
        options.$http(options.httpOptions).then(options.success,
            function (response) {
                if (options.onError) {
                    options.onError(response);
                }
            });
    }//end

    // begin:cookie
    function cookie() {
        var cookieUtil = {
            get: function (name, subName) {
                var subCookies = this.getAll(name);
                if (subCookies) {
                    return subCookies[subName];
                } else {
                    return null;
                }
            },
            getAll: function (name) {
                var cookieName = encodeURIComponent(name) + "=", cookieStart = document.cookie
                    .indexOf(cookieName), cookieValue = null, result = {};
                if (cookieStart > -1) {
                    var cookieEnd = document.cookie.indexOf(";", cookieStart)
                    if (cookieEnd == -1) {
                        cookieEnd = document.cookie.length;
                    }
                    cookieValue = document.cookie.substring(cookieStart
                        + cookieName.length, cookieEnd);
                    if (cookieValue.length > 0) {
                        var subCookies = cookieValue.split("&");
                        for (var i = 0, len = subCookies.length; i < len; i++) {
                            var parts = subCookies[i].split("=");
                            result[decodeURIComponent(parts[0])] = decodeURIComponent(parts[1]);
                        }
                        return result;
                    }
                }
                return null;
            },
            set: function (name, subName, value, expires, path, domain, secure) {
                var subcookies = this.getAll(name) || {};
                subcookies[subName] = value;
                this.setAll(name, subcookies, expires, path, domain, secure);
            },
            setAll: function (name, subcookies, expires, path, domain, secure) {
                var cookieText = encodeURIComponent(name) + "=";
                var subcookieParts = new Array();
                for (var subName in subcookies) {
                    if (subName.length > 0
                        && subcookies.hasOwnProperty(subName)) {
                        subcookieParts.push(encodeURIComponent(subName) + "="
                            + encodeURIComponent(subcookies[subName]));
                    }
                }
                if (subcookieParts.length > 0) {

                    cookieText += subcookieParts.join("&");
                    if (expires instanceof Date) {

                        cookieText += ";expires=" + expires.toGMTString();
                    }
                    if (path) {
                        cookieText += ";path=" + path;
                    }
                    if (domain) {
                        cookieText += ";domain=" + domain;
                    }
                    if (secure) {
                        cookieText += ";secure";
                    }
                } else {

                    cookieText += ";expires=" + (new Date(0)).toGMTString();
                }
                document.cookie = cookieText;
            },
            unset: function (name, subName, path, domain, secure) {
                var subcookies = this.getAll(name);
                if (subcookies) {
                    delete subcookies[subName];
                    this.setAll(name, subcookies, null, path, domain, secure);
                }
            },
            unsetAll: function (name, path, domain, secure) {
                this.setAll(name, null, new Date(0), path, domain, secure);
            }
        };
        return cookieUtil;
    }

    // end:cookie

    function getToken() {
        var data = cookie().getAll("data");
        return data != null ? data.token : "";
    }//end

    function gridDataSource(dataSource) {
        dataSource.error = function (e) {
            if (e.status == 401) {
                location.href = service.loginUrl;
            } else {

            }
        };
        return dataSource;
    }//end

    // S_封装filer的参数
    function buildOdataFilter(from) {
        /*
         * var t = new Array(); var arrIndex = 0;
         * $(from).find('input,radio,select,textarea').each(function(index,obj){
         * if(obj.name && obj.value){ var param = {};
         * if($(this).attr('operator')){ param.operator =
         * $(this).attr('operator'); }else{ param.operator = 'eq'; } param.name =
         * $.trim(obj.name); param.value = $.trim(obj.value); t[arrIndex] =
         * param; arrIndex++; } });
         *
         * var i = 0; var filterStr = ""; $.each(t, function() { if(this.value){
         * if(i > 0){ filterStr += " and "; } filterStr += this.name + " " +
         * this.operator + " '"+ this.value +"'"; i++; } }); return filterStr;
         */

        var manipulation_rcheckableType = /^(?:checkbox|radio)$/i,
            rsubmitterTypes = /^(?:submit|button|image|reset|file)$/i,
            rsubmittable = /^(?:input|select|textarea|keygen)/i;

        return $(from).map(function () {
            var elements = jQuery.prop(this, "elements");
            return elements ? jQuery.makeArray(elements) : this;
        }).filter(
            function () {
                var type = this.type;
                return this.value
                    && this.name
                    && !$(this).is(":disabled")
                    && rsubmittable.test(this.nodeName)
                    && !rsubmitterTypes.test(type)
                    && (this.checked || !manipulation_rcheckableType
                        .test(type));
            }).map(
            function (i, elem) {
                var $me = $(this), val = $me.val();
                if (!val) {
                    return false;
                }
                var dataType = $me.attr("data-type") || "String";
                if (!("Integer" == dataType)) {
                    val = "'" + val + "'";
                }
                var operator = $me.attr("operator") || "eq",
                    dataRole = $me.attr("data-role") || ""; // data-role="datepicker"
                if (dataRole == "datepicker") {
                    val = "date" + val;
                } else if (dataRole == "datetimepicker") {
                    val = "datetime" + val;
                }

                return operator == "like" ? ("substringof(" + val + ", "
                + elem.name + ")") : (elem.name + " " + operator
                + " " + val);
            }).get().join(" and ");
    }// E_封装filer的参数

    function initDictData(options) {
        if (!DICT_ITEMS) {
            options.$http({
                method: 'get',
                url: rootPath + '/dict/dictItems'
            }).then(function (response) {
                options.scope.dictMetaData = response.data;
                var dictsObj = {};
                reduceDict(dictsObj, response.data);
                options.scope.DICT = dictsObj;
            }, function (response) {
                alert('初始化数据字典失败');
            });
        } else {
            options.scope.dictMetaData = DICT_ITEMS;
            var dictsObj = {};
            reduceDict(dictsObj, options.scope.dictMetaData);
            options.scope.DICT = dictsObj;
        }
    }//end

    function reduceDict(dictsObj, dicts, parentId) {
        if (!dicts || dicts.length == 0) {
            return;
        }
        if (!parentId) {
            for (var i = 0; i < dicts.length; i++) {
                var dict = dicts[i];
                if (!dict.parentId) {
                    dictsObj[dict.dictCode] = {};
                    dictsObj[dict.dictCode].dictId = dict.dictId;
                    dictsObj[dict.dictCode].dictCode = dict.dictCode;
                    dictsObj[dict.dictCode].dictName = dict.dictName;
                    dictsObj[dict.dictCode].dictKey = dict.dictKey;
                    dictsObj[dict.dictCode].dictSort = dict.dictSort;
                    reduceDict(dictsObj[dict.dictCode], dicts, dict.dictId);
                }
            }
        } else {
            for (var i = 0; i < dicts.length; i++) {
                var dict = dicts[i];
                if (dict.parentId && dict.parentId == parentId) {
                    if (!dictsObj.dicts) {
                        dictsObj.dicts = new Array();
                    }
                    var subDict = {};
                    subDict.dictId = dict.dictId;
                    subDict.dictName = dict.dictName;
                    subDict.dictCode = dict.dictCode;
                    subDict.dictKey = dict.dictKey;
                    subDict.dictSort = dict.dictSort;
                    dictsObj.dicts.push(subDict);
                    reduceDict(subDict, dicts, dict.dictId);
                }
            }
        }
    }//end

    // S_附件上传参数初始化
    function initUploadOption(options) {
        return {
            async: {
                saveUrl: rootPath + "/file",
                removeUrl: rootPath + "/file/delete",
                autoUpload: false
            },
            select: function (e) {
                if (options.onSelect) {
                    options.onSelect(e)
                } else {
                    $.each(e.files, function (index, value) {
                        console.log("Name: " + value.name + "Size: "
                            + value.size + " bytes" + "Extension: "
                            + value.extension);
                    });
                }
            },
            upload: function (e) {
                if (options.onUpload) {
                    options.onUpload(e)
                } else {
                    var files = e.files;
                    console.log(e.response)
                }
            },
            success: function (e) {
                if (options.onSuccess) {
                    options.onSuccess(e)
                } else {
                    var files = e.files;
                    if (e.operation == "upload") {
                        files[0].sysFileId = e.response.sysFileId;
                    }
                }
            },
            remove: function (e) {
                if (options.onRemove) {
                    options.onRemove(e)
                } else {
                    var files = e.files;
                    e.data = {
                        'sysFileId': files[0].sysFileId
                    };
                }
            }
        }
    }// E_附件上传参数初始化

    // S_获取待办总数
    function getTaskCount(options) {
        options.$http({
            method: 'get',
            url: rootPath + '/flow/html/tasksCount'
        }).then(function (response) {
            $('#GtasksCount').html(response.data);
        });
    }// E_获取待办总数

    // 初始化常用意见
    function initIdeaData(vm, $http, options) {
        vm.ideaContent = '';// 初始化当前意见
        vm.$http = $http;
        vm.i = 1;

        var ideaEditWindow = $("#ideaWindow");
        ideaEditWindow.kendoWindow({
            width: "50%",
            height: "80%",
            title: "意见选择",
            visible: false,
            modal: true,
            closable: true,
            actions: ["Pin", "Minimize", "Maximize", "close"]
        }).data("kendoWindow").center().open();

        vm.$http({
            method: 'get',
            url: rootPath + "/idea"
        }).then(function (response) {
            vm.commonIdeas = response.data;

            vm.deleteCommonIdea = function () {// 删除常用意见
                deleteCommonIdea(vm);
            };

            vm.addCorrentIdea = function (ideaContent) {// 添加当前意见
                addCorrentIdea(vm, ideaContent);
            };

            vm.addCommonIdea = function () {// 添加常用意见
                addCommonIdea(vm);
            }

            vm.saveCommonIdea = function () {// 保存常用意见

                saveCommonIdea(vm);
            }

            vm.saveCurrentIdea = function () {
                saveCurrentIdea(vm, options);
            }
        });
    }//end

    function deleteCommonIdea(options) {
        var isCheck = $("#commonIdeaTable input[name='ideaCheck']:checked");
        if (isCheck.length < 1) {
            alert("请选择要删除的意见！");
        } else {
            var ids = [];
            for (var i = 0; i < isCheck.length; i++) {
                options.commonIdeas.forEach(function (c, number) {
                    if (isCheck[i].value == c.ideaID || c.ideaID == undefined) {
                        options.commonIdeas.splice(number, 1);
                    }
                    ids.push(isCheck[i].value);
                });
            }
            var idsStr = ids.join(",");

            options.$http({
                method: 'delete',
                url: rootPath + '/idea',
                params: {
                    ideas: idsStr
                }
            })

        }
    }// end

    function addCorrentIdea(options, ideaContent) {
        options.ideaContent = options.ideaContent + ideaContent;
    }// end

    function addCommonIdea(options) {
        options.commonIdea = {};
        options.commonIdea.ideaType = "个人常用意见";
        options.commonIdeas.push(options.commonIdea);
        options.i++;
    }// end

    function saveCommonIdea(options) {
        options.$http({
            method: 'post',
            url: rootPath + "/idea",
            headers: {
                "contentType": "application/json;charset=utf-8" // 设置请求头信息
            },
            dataType: "json",
            data: angular.toJson(options.commonIdeas)
        }).then(function (response) {
            alert("保存成功！");
        });
    }// end

    function saveCurrentIdea(vm, options) {
        var targetObj = $("#" + options.targetId);
        targetObj.val(targetObj.val() + vm.ideaContent);
        window.parent.$("#ideaWindow").data("kendoWindow").close();
        targetObj.focus();
    }// end

    // begin 获取项目暂停待办个数
    function getPauseProjectCount(options) {
        options.$http({
            method : "get",
            url : rootPath + "/projectStop/pauseProjectCount"
        }).then(function(response) {
            $("#pauseCount").html(response.data);
        });
    }// end 获取项目暂停待办个数

    // init
    init();
    function init() {
        // 全选
        $(document).on(
            'click',
            '#checkboxAll',
            function () {
                var isSelected = $(this).is(':checked');
                $('.grid').find('tr td:nth-child(1)').find('input:checkbox').prop('checked',isSelected);
            });
        // 点击行，改变背景
        $('body').on('click', '.grid tr', function (e) {
            $(this).parent().find('tr').removeClass('selected');
            $(this).addClass('selected');
        })
    }

})();

/**
 * 全局angular $http拦截器
 */
(function () {
    'use strict';

    var app = angular.module('app');

    app.factory("commonHttpInterceptor", ["$injector", "bsWin", commonHttpInterceptor]);

    function commonHttpInterceptor($q, bsWin) {  //  anguler $http全局请求拦截器

        return {
            request: function (config) {
                config.headers["Token"] = common.getToken();
                config.headers["X-Requested-With"] = "XMLHttpRequest";  // 用于后台ajax请求的判断
                return config;
            },
            responseError: function (response) {
                // console.log("responseError", response);
                if (!response.config.headers.commonHttp) { // 过滤掉用common.http发起的请求
                    errorHandle(bsWin, response.status, response.data);
                }
                return $q.reject(response);
            }
        };

    }

    /**
     * 统一错误状态处理
     * @param bsWin     提示窗口对象
     * @param status    响应状态码
     * @param data      响应数据
     */
    function errorHandle(bsWin, status, data) {
        // debugger;
        switch (status) {
            case 500:
                bsWin.error("系统内部错误");
                break;
            case 401:
                // TODO 可改为用户登录弹出窗口，不需要跳转到登录界面
                bsWin.warning("登录信息失效或您没有权限,请重新登录!");
                break;
            case 403:
                bsWin.warning("无权限执行此操作");
                break;
            case 404:
                bsWin.error("未找到相应的操作");
                break;
            case 408:
                bsWin.warning("请求超时");
                break;
            case 412:
                bsWin.warning(data.message || "操作失败");
                break;
            case 499:
                bsWin.warning(data.message || "您的账号已在别的地方登录，请确认密码是否被修改！");
                break;
            default:
                bsWin.error("发生错误,系统已记录,我们会尽快处理！");
        }
    }

    app.config(['$httpProvider', function ($httpProvider) { // 添加拦截器
        $httpProvider.interceptors.push(commonHttpInterceptor);
    }]);

    if (jQuery) {  // 设置jQuery的ajax全局默认配置
        jQuery(document).ajaxSend(function (event, request, settings) {
            request.setRequestHeader('Token', common.getToken());
        }).ajaxError(function (event, jqXHR, settings, thrownError) {
            var _body = angular.element("body"),
                scope = _body.scope(),
                bsWin = _body.injector().get("bsWin"),
                data = angular.isString(jqXHR.responseText) ? JSON.parse(jqXHR.responseText || "{}") : jqXHR.responseText;

            scope.$apply(function () {
                errorHandle(bsWin, jqXHR.status, data || {});
            });
        });
    }
})();
(function () {
    'use strict';

    angular
        .module('app')
        .controller('companyCtrl', company);

    company.$inject = ['$location','companySvc']; 

    function company($location, companySvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '单位列表';
       
        vm.del = function (id) {        	
        	
             common.confirm({
            	 vm:vm,
            	 title:"",
            	 msg:"确认删除数据吗？",
            	 fn:function () {
                  	$('.confirmDialog').modal('hide');             	
                    companySvc.deletecompany(vm,id);
                 }
             })
        }
        vm.dels = function () {     
        	var selectIds = common.getKendoCheckId('.grid');
        	//alert(selectIds.length);
            if (selectIds.length == 0) {
            	common.alert({
                	vm:vm,
                	msg:'请选择数据'
                	
                });
            } else {
            	var ids=[];
                for (var i = 0; i < selectIds.length; i++) {
                	ids.push(selectIds[i].value);
				}  
                var idStr=ids.join(',');
                vm.del(idStr);
            }   
       }
        vm.queryConpany =function(){
        	companySvc.queryConpany(vm)
        }
        activate();
        function activate() {
            companySvc.grid(vm);
        }
    }
})();

(function () {
    'use strict';

    angular
        .module('app')
        .controller('companyEditCtrl', company);

    company.$inject = ['$location','companySvc','$state']; 

    function company($location, companySvc,$state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '新增单位';
        vm.iscompanyExist=false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新单位';
        }
        
        vm.create = function () {
        	companySvc.createcompany(vm);
        };
        vm.update = function () {
        	companySvc.updatecompany(vm);
        };

        activate();
        function activate() {
        	if (vm.isUpdate) {
        		companySvc.getcompanyById(vm);
            } 
        }
    }
})();

(function() {
	'use strict';

	angular.module('app').factory('companySvc', company);

	company.$inject = [ '$http','$compile' ];	
	function company($http,$compile) {	
		var url_company = rootPath +"/company";
		var url_back = '#/company';
		var url_user=rootPath +'/user';
			
		var service = {
			grid : grid,
			createcompany : createcompany,			
			getcompanyById : getcompanyById,
			updatecompany:updatecompany,
			deletecompany:deletecompany	,
			queryConpany : queryConpany,//模糊查询
		};		
		return service;	
		
		function grid(vm) {

			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_company+"/fingByOData",$("#form")),
				schema : common.kendoGridConfig().schema({
					id : "id",
					fields : {
						createdDate : {
							type : "date"
						}
					}
				}),
				serverPaging : true,
				serverSorting : true,
				serverFiltering : true,			
				pageSize: 10,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});

			// End:dataSource
			
			  //S_序号
            var  dataBound=function () {  
                var rows = this.items();  
                var page = this.pager.page() - 1;  
                var pagesize = this.pager.pageSize();  
                $(rows).each(function () {  
                    var index = $(this).index() + 1 + page * pagesize;  
                    var rowLabel = $(this).find(".row-number");  
                    $(rowLabel).html(index);  
                });  
            } 
            //S_序号

			// Begin:column
			var columns = [
					{
						template : function(item) {
							return kendo
									.format(
											"<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
											item.id)
						},
						filterable : false,
						width : 40,
						title : "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
						
					},  
					 {  
					    field: "rowNumber",  
					    title: "序号",  
					    width: 50,
					    filterable : false,
					    template: "<span class='row-number'></span>"  
					 }
					,
					{
						field : "coName",
						title : "单位名称",
						width : 220,						
						filterable : false
					},
					{
						field : "coPhone",
						title : "单位电话",
						width : 100,						
						filterable : false
					},
					{
						field : "coPC",
						title : "邮编",
						width : 100,						
						filterable : false
					},
					{
						field : "coAddress",
						title : "地址",
						width : 160,						
						filterable : false
					},			
					{
						field : "coType",
						title : "单位类型",
						width : 160,						
						filterable : false
					},
					 
					{
						field : "",
						title : "操作",
						width : 200,
						template:function(item){							
							return common.format($('#columnBtns').html(),"vm.del('"+item.id+"')",item.id);
							
						}
						

					}

			];
			// End:column
		
			vm.gridOptions={
					dataSource : common.gridDataSource(dataSource),
					filterable : common.kendoGridConfig().filterable,
					pageable : common.kendoGridConfig().pageable,
					noRecords:common.kendoGridConfig().noRecordMessage,
					columns : columns,
					dataBound:dataBound,
					resizable: true
				};
			
		}// end fun grid
		
		//Start 模糊查询
		function queryConpany(vm){
			vm.gridOptions.dataSource.read();	
		}
		// end 模糊查询
		
		function createcompany(vm) {
			common.initJqValidation();
			var isValid = $('form').valid();
			if (isValid && vm.iscompanyExist == false) {
				vm.isSubmit = true;
				var httpOptions = {
					method : 'post',
					url : url_company,
					data : vm.model
				}
				
				var httpSuccess = function success(response) {				
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function() {							
							
							common.alert({
								vm:vm,
								msg:"操作成功",
								fn:function() {
									vm.isSubmit = false;
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
									location.href = url_back;
								}
							})
						}
						
					});

				}

				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess
				});

			} else {				
//				common.alert({
//					vm:vm,
//					msg:"您填写的信息不正确,请核对后提交!"
//				})
			}
		}// end fun createcompany
		
		//start  getcompanyById
		function getcompanyById(vm) {
			var httpOptions = {
				method : 'get',
				url : url_company +"/html/findByIdCompany",
				params:{id:vm.id}
			}
			var httpSuccess = function success(response) {
				vm.model=response.data;
				console.log(vm.model);
			}
			
			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}// end  getcompanyById
		
		function updatecompany(vm){
			common.initJqValidation();			
			var isValid = $('form').valid();
			if (isValid && vm.iscompanyExist == false) {
				vm.isSubmit = true;
				vm.model.id=vm.id;// id
				var httpOptions = {
					method : 'put',
					url : url_company,
					data : vm.model
				}

				var httpSuccess = function success(response) {
					
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function() {
							
							common.alert({
								vm:vm,
								msg:"操作成功",
								fn:function() {
									vm.isSubmit = false;
									$('.alertDialog').modal('hide');							
								}
							})
						}
						
					})
				}

				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess
				});

			} else {
//				common.alert({
//				vm:vm,
//				msg:"您填写的信息不正确,请核对后提交!"
//			})
			}
		}// end fun updatecompany
		
		function deletecompany(vm,id) {
		
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url:url_company,
                data:id
                
            }
            var httpSuccess = function success(response) {
                
                common.requestSuccess({
					vm:vm,
					response:response,
					fn:function () {
	                    vm.isSubmit = false;
	                    vm.gridOptions.dataSource.read();
	                }
					
				});

            }
            common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
        }// end fun deletecompany
		
		
		
		

	}
	
	
	
})();
(function () {
    'use strict';

    angular.module('app').controller('deptCtrl', dept);

    dept.$inject = ['$location', 'deptSvc'];

    function dept($location, deptSvc) {
        var vm = this;
        vm.title = '处室管理';

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    deptSvc.deleteDept(vm, id);
                }
            });
        }
        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: '请选择数据'
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.del(idStr);
            }
        };
        
        vm.queryDept = function(){
        	deptSvc.queryDept(vm);
        }
        activate();
        function activate() {
            deptSvc.grid(vm);
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('deptEditCtrl', dept);

    dept.$inject = ['$location', 'deptSvc', '$state'];

    function dept($location, deptSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '编辑处室';
        vm.isuserExist = false;
        vm.deptId = $state.params.deptId;
        if (vm.deptId) {
            vm.isUpdate = true;
            vm.title = '更新处室';
        }

        vm.create = function () {
            deptSvc.createDept(vm);
        };
        vm.update = function () {
            deptSvc.updateDept(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                deptSvc.getDeptById(vm);
            }
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').factory('deptSvc', dept);

    dept.$inject = ['$http'];

    function dept($http) {
        var url_dept = rootPath + "/dept", url_back = '#/listDept';
        var service = {
            grid: grid,
            getDeptById: getDeptById,
            createDept: createDept,
            deleteDept: deleteDept,
            updateDept: updateDept,
            queryDept:queryDept,
        };

        return service;
        function queryDept(vm){
        	vm.gridOptions.dataSource.read();	
        }
        // begin#updateDept
        function updateDept(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                var httpOptions = {
                    method: 'put',
                    url: url_dept,
                    data: vm.model
                }

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                closeDialog :true,
                                fn: function () {
                                    vm.isSubmit = false;
                                }
                            })
                        }

                    })
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            } 
        }

        // begin#deleteDept
        function deleteDept(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_dept,
                data: id
            };

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                    	common.alert({
                            vm: vm,
                            msg: "操作成功",
                            closeDialog :true,
                            fn: function () {
                            	vm.isSubmit = false;
                                vm.gridOptions.dataSource.read();
                            }
                        })
                    }
                });
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#createDept
        function createDept(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;

                var httpOptions = {
                    method: 'post',
                    url: url_dept,
                    data: vm.model
                };

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                closeDialog :true,
                                fn: function () {
                                    vm.isSubmit = false;
                                    location.href = url_back;
                                }
                            });
                        }
                    });
                };

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            }
        }

        // begin#getDeptById
        function getDeptById(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/dept/html/findById",
                params:{deptId:vm.deptId}
            };
            var httpSuccess = function success(response) {
                if(response.data.offices){
                	vm.offices ={};
                	vm.offiecs = response.data.offices;
                }
                vm.model = response.data;
               // console.log(vm.model);
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        
        
        // begin#grid
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_dept+"/fingByOData",$("#formDept")),
                schema: common.kendoGridConfig().schema({
                    id: "deptId",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            // End:dataSource
            
            //S_序号
            var  dataBound=function () {  
                var rows = this.items();  
                var page = this.pager.page() - 1;  
                var pagesize = this.pager.pageSize();  
                $(rows).each(function () {  
                    var index = $(this).index() + 1 + page * pagesize;  
                    var rowLabel = $(this).find(".row-number");  
                    $(rowLabel).html(index);  
                });  
            } 
            //S_序号
            
            // Begin:column
            var columns = [
            	
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.deptId)
                    },
                    filterable: false,
                    width: 20,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {  
 				    field: "rowNumber",  
 				    title: "序号",  
 				    width: 30,
 				    filterable : false,
 				    template: "<span class='row-number'></span>"  
 				 }
 				,
                {
                    field: "deptName",
                    title: "处室名称",
                    width: 100,
                    filterable: false
                },
                {
                    field: "deptUserName",
                    title: "联系人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "address",
                    title: "地址",
                    width: 100,
                    filterable: false
                },
                {
                    field: "deptType",
                    title: "类型",
                    width: 100,
                    filterable: false
                },
                {
                    field: "",
                    title: "状态",
                    width: 100,
                    filterable: false,
                    template : function(item) {
						if(item.status){
							if(item.status == 5){
								return "正常"
							}else if(item.status == 7){
								return "已删除"
							}
						}else{
							return " "
						}
					}
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.del('" + item.deptId + "')", item.deptId,item.deptId);
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
            	dataBound :dataBound,
                resizable: true
            };

        }// end fun grid

    }
})();
(function () {
    'use strict';

    angular.module('app').controller('deptOfficeUserCtrl', deptOfficeUser);

    deptOfficeUser.$inject = ['$location','$state','deptSvc','deptOfficeUserSvc'];

    function deptOfficeUser($location,$state,deptSvc, deptOfficeUserSvc) {
        var vm = this;
        vm.title = '办事处人员列表';
        vm.id=$state.params.deptId;
        vm.showAddUserDialog = function (){
        	$('.addUser').modal({
                backdrop: 'static',
                keyboard:false
            });
        	 vm.deptOfficeGrid.dataSource.read();
        }
        vm.closeAddUserDialog=function(){
        	$('.addUser').modal('hide');		
        	
        }
        vm.addOfficeUser = function(officeId){
        	deptOfficeUserSvc.addOfficeUser(vm,officeId);
        }
        
        vm.remove = function (officeId) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    deptOfficeUserSvc.deletedeptOfficeUser(vm, officeId);
                }
            });
        }
        vm.removes = function () {
            var selectIds = common.getKendoCheckId('.deptOfficeGrid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: '请选择数据'
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.remove(idStr);
            }
        };
        
      
        activate();
        function activate() {
            deptOfficeUserSvc.grid(vm);
            deptOfficeUserSvc.deptOfficeGrid(vm);
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').factory('deptOfficeUserSvc', dept);

    dept.$inject = ['$http'];

    function dept($http) {
        var url_dept = rootPath + "/dept", url_back = '#/deptList';
        var url_deptOfficeUsers =rootPath+"/dept/getDeptOfficeUsers";		//在办事处成员
        var url_deptNotInOfficeUser =rootPath+"/dept/NotInoDeptfficeUsers";	//不在办事处成员
        var service = {
            grid: grid,									//办事处列表
            getdeptById: getdeptById,
            createdept: createdept,
            deletedeptOfficeUser: deletedeptOfficeUser,  //移除成员
            updatedept: updatedept,
            getDepts:getDepts,						     //获取所有办事处
            deptOfficeGrid :deptOfficeGrid,              //办事处人员列表
            addOfficeUser:addOfficeUser,					     //添加成员
        };

        return service;
        //start# 继续添加成员
        function addOfficeUser(vm,officeId){
        	 var httpOptions = {
                     method: 'post',
                     url:rootPath+"/dept/addOfficeUser",
                     params:{
                     	deptId:vm.id,
                     	officeId: officeId
                     }                
                 }
                 
                 var httpSuccess = function success(response) {              
                     common.requestSuccess({
     					vm:vm,
     					response:response,
     					fn:function () {
     						vm.deptOfficeGrid.dataSource.read();
     	                    vm.gridOptions.dataSource.read();	                   
     	                }					
     				});
                 }
                 common.http({
     				vm:vm,
     				$http:$http,
     				httpOptions:httpOptions,
     				success:httpSuccess
     			});
        }
        //end# 添加成员
        
        //start 获取所有办事处
        function getDepts(vm){
        	var httpOptions = {
                    method: 'get',
                    url: common.format(url_dept + "/getDepts")
                }
                var httpSuccess = function success(response) {
                    vm.depts = {};
                    vm.depts = response.data;
//                    console.log(vm.depts);
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
        	
        }
        //end 获取所有办事处
        
        // begin#updatedept
        function updatedept(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_dept,
                    data: vm.model
                }

                var httpSuccess = function success(response) {

                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {

                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                }
                            })
                        }

                    })
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            } else {
                // common.alert({
                // vm:vm,
                // msg:"您填写的信息不正确,请核对后提交!"
                // })
            }

        }

        // begin#deletedeptOfficeUser
        function deletedeptOfficeUser(vm, officeId) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url:rootPath+"/dept/deleteOfficeUsers",
               params:{
            	   deptId:vm.id,
            	   officeId:officeId
            
               }
            };

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                    	common.alert({
                            vm: vm,
                            msg: "操作成功",
                            closeDialog :true,
                            fn: function () {
                            	vm.isSubmit = false;
                                vm.gridOptions.dataSource.read();
                            }
                        })
                    }
                });
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#createdept
        function createdept(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;

                var httpOptions = {
                    method: 'post',
                    url: url_dept,
                    data: vm.model
                };

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                closeDialog :true,
                                fn: function () {
                                    vm.isSubmit = false;
                                    location.href = url_back;
                                }
                            });
                        }
                    });
                };

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            }
        }

        // begin#getdeptById
        function getdeptById(vm) {
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/dept/html/findById",
                params:{officeID:vm.officeID}
            };
            var httpSuccess = function success(response) {
            	if(response.data.dept){
					vm.depts = {}
					//vm.depts = response.data.depts;
					//console.log(vm.depts);
					
				}
                vm.model = response.data;
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });                       
        }

        // begin#查看人员列表
        function deptOfficeGrid(vm) {
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_deptNotInOfficeUser+"?deptId="+vm.id),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            // End:dataSource

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.officeID)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                
                {
                    field: "officeUserName",
                    title: "办事处联系人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "officePhone",
                    title: "电话",
                    width: 100,
                    filterable: false
                },
               
                {
                    field: "officeDesc",
                    title: "描述",
                    width: 100,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#allUserGridBtns').html(),
                            "vm.addOfficeUser('" + item.officeID + "')", item.officeID);
                    }
                }
            ];
            // End:column

            vm.deptOfficeGrid = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true
            };

        }// end fun 查看人员列表
        
        //start 办事处列表
        function grid(vm) {
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_deptOfficeUsers+"?deptId="+vm.id),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            // End:dataSource

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.officeID)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
               
                {
                    field: "officeUserName",
                    title: "办事处联系人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "officePhone",
                    title: "电话",
                    width: 100,
                    filterable: false
                },
               
                {
                    field: "officeDesc",
                    title: "描述",
                    width: 100,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.remove('" + item.officeID + "')", item.officeID);
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true
            };

        }// end 办事处列表

    }
})();
(function () {
    'use strict';

    angular
        .module('app')
        .controller('dictCtrl', dict);

    dict.$inject = ['$location','dictSvc','$scope']; 

    function dict($location, dictSvc,$scope) {
    	  /* jshint validthis:true */
    	var vm = this;
        vm.title = '字典';
        
        vm.model = {};
        vm.del = function (id) {        	
        	 
             common.confirm({
            	 vm:vm,
            	 title:"",
            	 msg:"删除字典将会连下级字典一起删除，确认删除数据吗？",
            	 fn:function () {
                  	$('.confirmDialog').modal('hide');             	
                  	dictSvc.deleteDict(vm,id);
                 }
             })
        }
        vm.dels = function () {     

            var nodes = vm.dictsTree.getSelectedNodes();
            
            if (nodes&&nodes.length >0) {
            	 vm.del(nodes[0].id)
            } else {
            	common.alert({
                	vm:vm,
                	msg:'请选择数据'
                	
                });
            }   
       }
        
        activate();
        function activate() {
            dictSvc.initDictTree(vm);
        }
               
        
    }
    
   
})();

(function () {
    'use strict';

    angular
        .module('app')
        .controller('dictEditCtrl', dict);

    dict.$inject = ['$scope','$location','dictSvc','$state']; 
    function dict($scope,$location, dictSvc,$state) {

    	var vm = this;
        vm.title = '增加字典';
        vm.model = {};
      
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '编辑字典';
        }
        vm.model.dictSort=0;//默认排序序号为0
 
        vm.createDict = function(){
        	dictSvc.createDict(vm);
        };
        
        vm.updateDict = function(){
        	dictSvc.updateDict(vm);
        }
    	vm.dictTypeChange = function(){
    		if(vm.model.dictType){
    			vm.model.dictKey = '';
    		}
    		
    	};
    	
    	vm.apply = function(){
    		$scope.$apply();
    	}
    	
        activate();
        function activate() {
        	
        	if (vm.isUpdate) {
        		dictSvc.getDictById(vm)
            } else {
            	vm.model.dictCode = '';
            	dictSvc.initpZtreeClient(vm);
            }
        }
          
    }
    
    
    
})();

(function() {
	'use strict';

	angular.module('app').factory('dictSvc', dict);

	dict.$inject = [ '$http' ,'$state','$location'];

	function dict($http,$state,$location) {
		var url_back = '#/dict';
		var url_dictgroup = rootPath + "/dict";
		var url_dictitems = rootPath + "/dict/dictNameData";
		var service = {
			initDictTree:initDictTree,
			createDict:createDict,
			getDictById:getDictById,
			updateDict:updateDict,
			deleteDict:deleteDict,
			initpZtreeClient:initpZtreeClient,
			getTreeData:getTreeData,
			getdictItems:getdictItems
		};

		return service;

		function getdictItems(vm){
			var dictCode = 'DICT_SEX';
			
			
			var httpOptions = {
					method : 'get',
					url : common.format(url_dictitems + "?dictCode={0}", dictCode)
			};
			
			var httpSuccess = function success(response) {
			
				
			}

			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
			
		}
		
		function getTreeData(vm){
			
			var httpOptions = {
					method : 'get',
					url : url_dictgroup
			};
			
			var httpSuccess = function success(response) {

				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						
						vm.treeData = {};
						vm.treeData = response.data.value;
					
						if(vm.isUpdate&&vm.treeData&&vm.model.parentId){
							for(var i = 0;i<vm.treeData.length;i++){
								if(vm.treeData[i].dictId == vm.model.parentId){
									vm.model.parentDictName = vm.treeData[i].dictName;
									break;
								}
							}
						}
						
					}

				});

			};
			
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
			
		}
		
		

		
		//beginDeleteGroup
		function deleteDict(vm,id){
		
           
			vm.isSubmit = true;
			var httpOptions = {
				method : 'delete',
				url : url_dictgroup,
				data : id

			}
			var httpSuccess = function success(response) {

				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.isSubmit = false;	
						location.href = url_back;
						common.alert({
							vm : vm,
							msg : "操作成功",
							fn : function() {	
								$('.alertDialog').modal('hide');
								$('.modal-backdrop').remove();
								$state.go('dict',{},{reload:true});
							}
						});
						
						
					}

				});

			}
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		
		}

		//begin#createDict
		function createDict(vm){
			common.initJqValidation();
		
			var isValid = $('form').valid();
			
		
			var nodes = vm.zpTree.getCheckedNodes(true);
			if(nodes&&nodes.length>0){
				vm.model.parentId = nodes[0].id;
			}
			
			if(isValid){
				vm.isSubmit = true;
				
				var httpOptions = {
					method : 'post',
					url : url_dictgroup,
					data : vm.model
				}
				
				
				var httpSuccess = function success(response) {

					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {

							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {	
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
									$state.go('dict',{},{reload:true});
								}
							});
						}

					});

				}

				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});
				
				
			}
			
			
				
		}
		
		//updateDict
		function updateDict(vm){
			common.initJqValidation();
			
			var isValid = $('form').valid();
			
			if(isValid){
				vm.isSubmit = true;
		
				var httpOptions = {
					method : 'put',
					url : url_dictgroup,
					data : vm.model
				}

				var httpSuccess = function success(response) {

					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {

							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {	
									vm.isSubmit = false;
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();	
									$state.go('dict.edit', { id: vm.model.dictId},{reload:true});
								}
							})
						}

					});

				}

				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});
				
				
			}
			
		}
		
		// begin#initZtreeClient
		function initDictTree(vm) {
			var httpOptions = {
				method : 'get',
				url : url_dictgroup + "?$orderby=dictSort"
			}

			var httpSuccess = function success(response) {

				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						var zTreeObj;
						var setting = {
							/*check : {
								chkboxType : {
									"Y" : "s",
									"N" : "s"
								},
								enable : true
							},*/
							callback: {
								onClick: zTreeOnClick
								//onCheck: zTreeOnCheck
							},
							data: {
								simpleData: {
									enable: true,
									idKey: "id",
									pIdKey: "pId"
								}
							}
						};
						
						function zTreeOnClick(event, treeId, treeNode) {
						   $state.go('dict.edit', { id: treeNode.id});
						};
						function zTreeOnCheck(event, treeId, treeNode) {
							var selId = treeNode.id;
							if(!vm.model.dels){
								vm.model.dels = [];
							}
							var delIds = vm.model.dels;
							if(treeNode.checked){
								delIds.push(selId);
							}else{
								for(var i =0;i<delIds.length;i++){
									if(delIds[i] == selId){
										delIds.splice(i);
										break;
									}
								}
							}
							
						};
						var zNodes = $linq(response.data.value).select(
								function(x) {
									var isParent = false;
									var pId = null;
									if(x.parentId){										
										pId = x.parentId;
									}
									return {
										id : x.dictId,
										name : x.dictName,
										pId:pId
									};
									
								}).toArray();
						var rootNode = {
							id : '0',
							name : '字典集合',
							'chkDisabled':true,
							children : zNodes
						};
								
						zTreeObj = $.fn.zTree.init($("#zTree"), setting,zNodes);
						vm.dictsTree = zTreeObj;
			
					}

				});

			}
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}

		
		
		// begin#initpZtreeClient
		function initpZtreeClient(vm) {
			var httpOptions = {
				method : 'get',
				url : url_dictgroup + "?$orderby=dictSort"
			}
			var httpSuccess = function success(response) {

				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						
						var zpTreeObj;
						var setting = {
							check: {enable: true,chkStyle: "radio",radioType: "all"},
							callback: {
								//onCheck: zTreeOnCheck,
								//onClick: zTreeOnClick
							},
							data: {
								simpleData: {
									enable: true,
									idKey: "id",
									pIdKey: "pId"//,
									//rootPId: 0
								}
							}
						};
						
						
						function zTreeOnCheck(event, treeId, treeNode) {
							
						
						};
						
						function zTreeOnClick(event, treeId, treeNode,clickFlag) {
							
						};
						var zNodes = $linq(response.data.value).select(
								function(x) {
									var pId;
									if(x.parentId){										
										pId = x.parentId;
									}
									return {
										id : x.dictId,
										name : x.dictName,
										pId:pId
									};
									
								}).toArray();
						var rootNode = {
								id : '',
								name : '字典集合',
								'chkDisabled':true,
								children : zNodes
							};
						vm.zpTree = $.fn.zTree.init($("#pzTree"), setting,zNodes);						
					}

				});

			}
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}

		
		
		//begin#getDictGroupByCode
		function getDictById(vm){
			var httpOptions = {
					method : 'get',
					url : common.format(url_dictgroup + "?$filter=dictId eq '{0}'", vm.id)
			};
			
			var httpSuccess = function success(response) {
				vm.model = response.data.value[0];
				getTreeData(vm);
			}

			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}
		
	
	}
})();
(function () {
    'dispatch strict';

    angular.module('app').controller('dispatchEditCtrl', dispatch);

    dispatch.$inject = ['$location', 'dispatchSvc', '$state', 'bsWin'];

    function dispatch($location, dispatchSvc, $state, bsWin) {
        var vm = this;
        vm.title = '项目发文编辑';
        vm.sign = {};
        vm.searchSign = {};        //发文查询对象
        vm.dispatchDoc = {};       //发文对象
        vm.dispatchDoc.signId = $state.params.signid;

        vm.showFlag = {
            buttSysFile : false,        //显示附件按钮
        }
        vm.busiFlag = {
            isMerge : false,            //是否合并发文
            isMain : false,             //是否合并发文主项目
        }

        activate();
        function activate() {
            dispatchSvc.initDispatchData(vm);
        }

        //发文方式改变事件
        vm.sigleProject = function () {
            //1、由合并发文主项目改为单个发文
            if(vm.dispatchDoc.dispatchWay == "1" ){
                if(vm.busiFlag.isMerge && vm.busiFlag.isMain){
                    bsWin.confirm({
                        title: "询问提示",
                        message: "该项目已经设为合并发文，并且已经有关联项目，如果现在要取消合并发文，以前的关联信息将被删除，您确定要取消合并发文么?",
                        onOk: function () {
                            $('.confirmDialog').modal('hide');
                            dispatchSvc.cancelProject(vm.dispatchDoc.signId,null,function (data) {
                                if (data.flag || data.reCode == "ok") {
                                    vm.dispatchDoc.isMainProject = "0";
                                    vm.busiFlag.isMerge=false;
                                    vm.busiFlag.isMain=false;
                                }
                                bsWin.alert(data.reMsg);
                            });
                        },
                        onClose : function(){
                        },
                        onCancel : function () {
                            vm.dispatchDoc.dispatchWay = "2";
                            $('.confirmDialog').modal('hide');
                        }
                    });
                }
            //2、由单个发文改为合并发文
            }else if(vm.dispatchDoc.dispatchWay == "2" ){
                if(!vm.busiFlag.isMerge){
                	vm.busiFlag.isMerge=true;
                    vm.busiFlag.isMain=(vm.dispatchDoc.isMainProject=="9")?true:false;//判断是否为主项目
                }
            }
        }

        // 创建发文
        vm.create = function () {
            dispatchSvc.saveDispatch(vm);
        }
        // 核减（增）/核减率（增）计算
        vm.count = function () {
            var pt = /^(-)?(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,4})?$/;    //保留4个小数点
            if(!pt.test(vm.dispatchDoc.declareValue)){
                vm.dispatchDoc.declareValue = 0;
                $("span[data-valmsg-for='declareValue']").html("金额只能输入数字！");
                return ;
            }
            if(!pt.test(vm.dispatchDoc.authorizeValue)){
                vm.dispatchDoc.authorizeValue = 0;
                $("span[data-valmsg-for='authorizeValue']").html("金额只能输入数字！");
                return ;
            }
            $("span[data-valmsg-for='declareValue']").html("");
            $("span[data-valmsg-for='authorizeValue']").html("");

            var dvalue = (parseFloat(vm.dispatchDoc.declareValue) - parseFloat(vm.dispatchDoc.authorizeValue)).toFixed(2);
            var extraRate = parseFloat((dvalue/vm.dispatchDoc.declareValue * 10000)/100.00).toFixed(2);
            vm.dispatchDoc.extraRate = extraRate;
            vm.dispatchDoc.extraValue = dvalue;
        }

        // 打开合并页面
        vm.gotoMergePage = function () {
        	 vm.busiFlag.isMain=(vm.dispatchDoc.isMainProject=="9")?true:false;//判断是否为主项目
             //没保存或者单个发文改成合并发文主项目时候要先进行保存
            if( vm.dispatchDoc.isMainProject == 9 || !vm.dispatchDoc.id){
                bsWin.alert("请先保存！");
            }else{
                //初始化合并评审信息
                dispatchSvc.initMergeInfo(vm,vm.dispatchDoc.signId);
                $("#mwindow").kendoWindow({
                    width: "1200px",
                    height: "630px",
                    title: "合并发文",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            }
        }

        // 选择合并发文项目
        vm.chooseProject = function () {
            var selIds = $("input[name='checksign']:checked");
            if(selIds.length == 0){
                bsWin.alert("请选择要合并发文的项目！");
            }else{
                var signIdArr = [];
                $.each(selIds, function (i, obj) {
                    signIdArr.push(obj.value);
                });
                dispatchSvc.chooseProject(vm.dispatchDoc.signId,signIdArr.join(","),function (data) {
                    if (data.flag || data.reCode == "ok") {
                        dispatchSvc.initMergeInfo(vm,vm.dispatchDoc.signId);
                    }
                    bsWin.alert(data.reMsg);
                });
            }
        }

        // 取消选择
        vm.cancelProject = function () {
            var linkSignId = $("input[name='checkss']:checked");
            if (linkSignId.length < 1){
                bsWin.alert("请选择要取消合并发文的项目！");
            }else{
                var ids = [];
                $.each(linkSignId, function (i, obj) {
                    ids.push(obj.value);
                });
                dispatchSvc.cancelProject(vm.dispatchDoc.signId,ids.join(","),function (data) {
                    if (data.flag || data.reCode == "ok") {
                        dispatchSvc.initMergeInfo(vm,vm.dispatchDoc.signId);
                    }
                    bsWin.alert(data.reMsg);
                });
            }

        }

        // 关闭窗口
        vm.onClose = function () {
            window.parent.$("#mwindow").data("kendoWindow").close();
        }

        //合并发文待选过滤器
        vm.filterMergeSign = function(item){
            var isMatch = true;
            if(vm.searchSign.projectname && (item.projectname).indexOf(vm.searchSign.projectname) == -1){
                isMatch = false;
            }
            if(vm.searchSign.reviewstage && (item.reviewstage != vm.searchSign.reviewstage)){
                isMatch = false;
            }
            if(vm.searchSign.builtcompanyName && (item.builtcompanyName).indexOf(vm.searchSign.builtcompanyName) == -1){
                isMatch = false;
            }
            if(isMatch){
                return item;
            }
        }

        //重置合并发文
        vm.formReset = function () {
            vm.searchSign = {};
        }

    }
})();

(function () {
    'dispatch strict';

    angular.module('app').factory('dispatchSvc', dispatch);

    dispatch.$inject = ['sysfileSvc', '$http','$state','bsWin'];

    function dispatch(sysfileSvc,$http,$state,bsWin) {
        var service = {
            initDispatchData: initDispatchData, // 初始化流程数据
            saveDispatch: saveDispatch,         // 保存
            initMergeInfo: initMergeInfo,       // 打开合并发文页面
            unMergeDISSign: unMergeDISSign,     // 显示待选项目
            getMergeDISSign: getMergeDISSign,   // 已选合并发文项目
            chooseProject: chooseProject,       // 选择合并发文项目
            cancelProject: cancelProject,       // 取消选择
        };
        return service;

        // begin#gotoWPage
        function initMergeInfo(vm,signId) {
            unMergeDISSign(signId,function (data) {
                vm.signs = [];
                vm.signs = data;
            });//待选
            getMergeDISSign(signId,function (data) {
                vm.selectedSign = [];
                vm.selectedSign = data;
            });//初始化已选项目
        }

        // end#gotoWPage

        // S_初始化
        function initDispatchData(vm) {
            vm.busiFlag.signleToMerge = false;
            var httpOptions = {
                method: 'get',
                url: rootPath + "/dispatch/initData",
                params: {
                    signid: vm.dispatchDoc.signId
                }
            }

            var httpSuccess = function success(response) {
                var data = response.data;
                vm.sign = data.sign;
                vm.dispatchDoc = data.dispatch;     //可编辑的发文对象
                vm.dispatchDoc.signId = $state.params.signid;
                if(vm.dispatchDoc.dispatchWay && vm.dispatchDoc.dispatchWay == 2){
                    vm.busiFlag.isMerge = true;     //合并发文
                }
                if(vm.dispatchDoc.isMainProject && vm.dispatchDoc.isMainProject == 9){
                    vm.busiFlag.isMain = true;     //主项目
                }
                vm.associateDispatchs = data.associateDispatchs;
                vm.proofread = data.mainUserList;   //校对人

                //初始化附件上传
                if (vm.dispatchDoc.id) {
                    vm.showFlag.buttSysFile = true;
                    sysfileSvc.initUploadOptions({
                        businessId: vm.dispatchDoc.id,
                        sysSignId: vm.dispatchDoc.signId,
                        sysfileType: "发文",
                        uploadBt: "upload_file_bt",
                        detailBt: "detail_file_bt",
                        vm: vm
                    });
                }
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }// E_初始化

        // S_保存
        function saveDispatch(vm) {
            vm.isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/dispatch",
                data: vm.dispatchDoc
            }
            var httpSuccess = function success(response) {
                vm.isCommit = false;
                if (response.data.flag || response.data.reCode == "ok") {
                    if(vm.dispatchDoc.dispatchWay && vm.dispatchDoc.dispatchWay == 2){
                        vm.busiFlag.isMerge = true;     //合并发文
                    }
                    if(vm.dispatchDoc.isMainProject && vm.dispatchDoc.isMainProject == 9){
                        vm.busiFlag.isMain = true;     //主项目
                    }

                    if(!vm.dispatchDoc.id){
                        vm.dispatchDoc.id = response.data.reObj.id;
                        //初始化附件上传
                        vm.showFlag.buttSysFile = true;
                        sysfileSvc.initUploadOptions({
                            businessId: vm.dispatchDoc.id,
                            sysSignId: vm.dispatchDoc.signId,
                            sysfileType: "发文",
                            uploadBt: "upload_file_bt",
                            detailBt: "detail_file_bt",
                            vm: vm
                        });
                    }
                }
                bsWin.alert(response.data.reMsg);
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError:function () {
                    vm.isCommit = false;
                }
            });
        }// E_保存

        // begin##chooseProject
        function chooseProject(signId,mergeIds,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/mergeSign",
                params: {
                    signId:signId ,
                    mergeIds: mergeIds,
                    mergeType:"2"
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // end##chooseProject

        // begin##chooseProject
        function cancelProject(signId,cancelIds,callBack) {
            var paramObj = {
                signId: signId,
                mergeType:"2"
            }
            if(cancelIds){
                paramObj.cancelIds = cancelIds;
            }
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/cancelMergeSign",
                params: paramObj
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        // end##chooseProject

        // begin##getSeleSignBysId
        function getMergeDISSign(signId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/getMergeDISSign",
                params: {
                    signId: signId
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        // end##getSeleSignBysId

        // begin##getSignForMerge
        function unMergeDISSign(signId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/unMergeDISSign",
                params: {
                    signId: signId
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
            });
        }// end##getSignForMerge

    }
})();
(function () {
    'use strict';

    angular.module('app').controller('expertAuditCtrl', expert);

    expert.$inject = ['$location', 'expertSvc'];

    function expert($location, expertSvc) {   
    	var vm = this;
    	
    	vm.searchAudit = function(){
    		expertSvc.searchAudit(vm);
    	}
    	
    	//审核状态去到各状态
        vm.auditToOfficial = function() {
     	  expertSvc.auditTo(vm,2);
	    };
	    
	    vm.auditToAlternative=function() {
	      	expertSvc.auditTo(vm,3);
	    };
	    
	    vm.auditToStop=function() {
	      	expertSvc.auditTo(vm,4);
	    };
	    
	    vm.auditToRemove=function(){
	      	expertSvc.auditTo(vm,5);
	    };
	    
	    //各状态回到审核状态
	    vm.officialToAudit=function(){
	      	expertSvc.toAudit(vm,2);
	    };
	    
	    vm.alternativeToAudit=function(){
	      	expertSvc.toAudit(vm,3);
	    };
	    
	    vm.stopToAudit=function(){
	      	expertSvc.toAudit(vm,4);
	    };
	    
	    vm.temoveToAudit=function(){
	      	expertSvc.toAudit(vm,5);
	    };
          
        activate();
        function activate() {
        	expertSvc.auditGrid(vm);
        }
    }
})();

(function () {
    'expert strict';

    angular.module('app').controller('expertCtrl', expert);
    
    expert.$inject = ['$location','expertSvc']; 
    
    function expert($location, expertSvc) {
    	var vm = this;
    	vm.data={};
    	vm.title = '专家列表';

        activate();
        function activate() {
            expertSvc.grid(vm);
        }

        vm.search = function () {
        	expertSvc.searchMuti(vm);
        };
        
        vm.searchAudit = function () {
        	expertSvc.searchMAudit(vm);
        };
        
        vm.formReset=function(){
        	expertSvc.formReset(vm);
        }
        
        vm.del = function (id) {        	
             common.confirm({
            	 vm:vm,
            	 title:"",
            	 msg:"确认删除数据吗？",
            	 fn:function () {
                  	$('.confirmDialog').modal('hide');  
                  	expertSvc.deleteExpert(vm,id);
                 }
             })
        };
        
        vm.dels = function () {     
        	var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
            	common.alert({
                	vm:vm,
                	msg:'请选择数据'
                	
                });
            } else {
            	var ids=[];
                for (var i = 0; i < selectIds.length; i++) {
                	ids.push(selectIds[i].value);
				}  
                var idStr=ids.join(',');
                vm.del(idStr);
            }   
       };

    }
})();

(function () {
    'expert strict';

    angular.module('app').controller('expertEditCtrl', expert);

    expert.$inject = ['$location','projectExpeSvc','workExpeSvc','expertSvc','expertOfferSvc','expertTypeSvc','$state'];

    function expert($location,projectExpeSvc,workExpeSvc,expertSvc,expertOfferSvc,expertTypeSvc,$state) {
        var vm = this;
        vm.model = {};
        vm.title = '专家信息录入';
        vm.isuserExist=false;
        vm.isHide=true;
        vm.isUpdate=false;
        vm.expertID = $state.params.expertID;
        vm.expertOfferList = {};    //专家聘书列表
        vm.expertOffer = {};        //专家聘书

        activice();
        function activice(){
        	vm.showSS=true;
        	vm.showSC=false;
        	vm.showWS=true;
        	vm.showWC=false;
        	vm.MW=false;
        	vm.MS=false;
        	projectExpeSvc.initProjectType(vm);
            if (vm.expertID) {
                vm.title = '更新专家';
                vm.isHide=false;
                vm.isUpdate=true;
                expertSvc.getExpertById(vm);
            }
        }

        vm.showUploadWin = function(){
            if(vm.model.expertID){
                $("#uploadWin").kendoWindow({
                    width : "660px",
                    height : "360px",
                    title : "上传头像",
                    visible : false,
                    modal : true,
                    closable : true,
                    actions : [ "Pin", "Minimize", "Maximize", "Close" ]
                }).data("kendoWindow").center().open();
            }else{
                common.alert({
                    vm : vm,
                    msg : "先保存数据在执行操作！"
                })
            }

        }
        
  		vm.create=function(){
  			expertSvc.createExpert(vm);
  		}
        
        vm.update=function(){
        	expertSvc.updateExpert(vm);
        }
        
        vm.gotoWPage=function(){
        	vm.createWork=true;
        	workExpeSvc.gotoWPage(vm);
        }
        
        vm.updateWorkPage=function(){
        	vm.createWork=false;
        	workExpeSvc.updateWorkPage(vm);
        }
        
        vm.createWorks=function(){
        	vm.createWork=true;
        	vm.work.expertID=vm.model.expertID;
        	workExpeSvc.createWork(vm);
        }
        
        vm.updateWork=function(){
        	workExpeSvc.updateWork(vm);
        }
        
        vm.deleteWork=function(){
        	workExpeSvc.deleteWork(vm);
        }
        
        vm.onWClose=function(){
        	workExpeSvc.cleanValue();
        	window.parent.$("#wrwindow").data("kendoWindow").close();
        }
        
        vm.onPClose=function(){
        	projectExpeSvc.cleanValue();
        	window.parent.$("#pjwindow").data("kendoWindow").close();
        }
        
        vm.gotoJPage=function(){
        	vm.createProject=true;
        	projectExpeSvc.gotoJPage(vm);
        }
        
        vm.updateProject=function(){
        	projectExpeSvc.updateProject(vm);
        }
        
        vm.createProjects=function(){
        	vm.createProject=true;
        	vm.project.expertID=vm.model.expertID;
        	projectExpeSvc.createProject(vm);
        }
        
        vm.updateProjectPage=function(){
        	vm.createProject=false;
        	projectExpeSvc.updateProjectPage(vm);
        }
        
        vm.delertProject=function(){
        	projectExpeSvc.delertProject(vm);
        }

//专家类型-业务处理
        vm.gotoExpertType=function(expertID){
        	vm.isUpdate=false;
        	expertTypeSvc.gotoExpertType(vm);
        }
        
         vm.onETlose=function(){
        	expertTypeSvc.cleanValue();
        	window.parent.$("#addExpertType").data("kendoWindow").close();
        }
        
        vm.createExpertType=function(){
        	vm.createExpertType=true;
        	vm.expertType.expertID=vm.model.expertID;
        	expertTypeSvc.createExpertType(vm)
        }
        
        vm.updateProjectType=function(){
        	vm.isUpdate=true;
        	vm.createExpertType=false;
        	expertTypeSvc.updateExpertType(vm);
        }
        
        vm.saveUpdateExpertType=function(){
        	expertTypeSvc.saveUpdate(vm);
        }
        
        vm.delertProjectType=function(){
        	expertTypeSvc.deleteExpertType(vm);
        }

        //专家聘书弹窗
        vm.gotoOfferPage = function(){
            $("#ep_offer_div").kendoWindow({
                width : "800px",
                height : "600px",
                title : "专家聘书",
                visible : false,
                modal : true,
                closable : true,
                actions : [ "Pin", "Minimize", "Maximize", "Close" ]
            }).data("kendoWindow").center().open();
        }

        //保存聘书信息
        vm.saveOffer = function () {
            if(vm.model.expertID){
                expertOfferSvc.saveOffer(vm);
            }else{
                common.alert({
                    vm : vm,
                    msg : "先保存专家信息！"
                })
            }
        }
        //关闭窗口信息
        vm.closeOffer=function(){
            window.parent.$("#ep_offer_div").data("kendoWindow").close();
        }
        //查看专家聘书
        vm.showOffer = function(id){
            vm.expertOffer = {};        //专家聘书
            vm.expertOfferList.forEach(function(o,index){
                if(o.id == id){
                   vm.expertOffer = o;
                   return ;
               }
            });
            vm.gotoOfferPage();
        }
        
        vm.chooseMW=function(){
        	vm.MW=true;
        	vm.showWS=true;
        	vm.showWC=false;
        }
        vm.cancelMW=function(){
        	vm.showWS=false;
        	vm.showWC=true;
        }
        vm.sureMW=function(){
        	if(!vm.majorWork){
        		common.alert({
							vm : vm,
							msg : "您未选择专业，请选择！",
							fn : function() {
								$('.alertDialog').modal('hide');
								$('.modal-backdrop').remove();
							}
						})
        	}else{
	        	vm.model.majorWork=vm.majorWork;
	        	vm.showWS=false;
	        	vm.showWC=true;
        	}
        }
        vm.chooseMS=function(){
        	vm.MS=true;
        	vm.showSS=true;
        	vm.showSC=false;
        }
        vm.cancelMS=function(){
        	vm.showSS=false;
        	vm.showSC=true;
        }
       vm.sureMS=function(){
       	if(!vm.majorStudy){
        		common.alert({
							vm : vm,
							msg : "您未选择专业，请选择！",
							fn : function() {
								$('.alertDialog').modal('hide');
								$('.modal-backdrop').remove();
							}
						})
        	}else{
	       		vm.model.majorStudy=vm.majorStudy;
	        	vm.showSS=false;
	        	vm.showSC=true;
        	}
       }
        
        
        
        
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('expertRepeatCtrl', expert);

    expert.$inject = ['$location', 'expertSvc'];

    function expert($location, expertSvc) { 
    	var vm = this;
    	
        activate();
        function activate() {
        	expertSvc.repeatGrid(vm);
        }
    }
})();

(function() {
	'expert strict';

	angular.module('app').factory('expertSvc', expert);

	expert.$inject = [ '$http'];
	
	function expert($http) {
		var url_expert = rootPath + "/expert";
		var service = {
			grid : grid,						//初始化综合查询grid
			auditGrid : auditGrid,				//初始化审核页面的所有grid
			getExpertById : getExpertById,		//通过ID查询专家信息详情
			createExpert : createExpert,        //创建专家信息
			deleteExpert : deleteExpert,        //删除专家信息
			updateExpert : updateExpert,        //更新专辑信息
			searchMuti : searchMuti,		    //综合查询
			searchAudit : searchAudit,		    //审核查询
			repeatGrid : repeatGrid,		    //重复专家查询
			updateAudit : updateAudit,		    //专家评审
			toAudit : toAudit,				    //由个状态回到审核状态
			auditTo : auditTo,				    //由审核状态去到各个状态
            initUpload : initUpload,             //初始化附件上传
            formReset : formReset,		//重置页面
            initUpload : initUpload,            //初始化附件上传
            initUpload : initUpload            //初始化附件上传
		};
		return service;	
		
		//begin formReset
		function formReset(vm){
			$("#searchform")[0].reset();
			vm.gridOptions.dataSource.read();
			
		}
		//end formReset
		
		// begin#updateExpert
		function updateExpert(vm) {
			common.initJqValidation();
			var isValid = $('form').valid();
			if (true) {
				vm.isSubmit = true;
				vm.model.expertID = vm.expertID;// id
				vm.model.birthDay= $("#birthDay").val();
				vm.model.createDate= $("#createDate").val();

				var httpOptions = {
					method : 'put',
					url : url_expert,
					data : vm.model
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.isSubmit = false;
							common.alert({
								vm : vm,
								msg : "操作成功"
							})
						}

					})
				}
				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});
			}
		}
		
		// begin#deleteUser
		function deleteExpert(vm, id) {
			vm.isSubmit = true;
			var httpOptions = {
				method : 'delete',
				url : url_expert,
				data : id

			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.isSubmit = false;
						vm.gridOptions.dataSource.read();
					}
				});
			}
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}
		// end#deleteUser
		
		// begin#search
		function searchMuti(vm) {
			vm.gridOptions.dataSource.read();	
		}
		// end#searchMuti									
				
		// begin#createExpert		
		function createExpert(vm) {	
			common.initJqValidation();
			var isValid = $('form').valid();
			if(isValid){				
				vm.isSubmit = true;												
				vm.model.birthDay=$('#birthDay').val();
				vm.model.graduateDate=$('#graduateDate').val();
				
				var httpOptions = {
					method : 'post',
					url : url_expert,
					data : vm.model
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.model.expertID = response.data.expertID;
							vm.isUpdate=true;
							vm.showBt=true;	
							vm.isSubmit = false;	
							common.alert({
								vm : vm,
								msg : "操作成功"
							})							
						}

					});
				}
				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});
			}
		}
		// end#createExpert
						
		// begin#getExpertById
		function getExpertById(vm,expert) {
			var httpOptions = {
				method : 'get',
				url : url_expert+"/findById",
				params:{
					id:vm.expertID
				}
			}
			var httpSuccess = function success(response) {
				vm.showBt = true;
				vm.model = response.data;

				 if(vm.model.majorWork){
            		vm.showWS=false;
        			vm.showWC=true;
				 }
        		
	            if(vm.model.majorStudy){
	        		vm.showSC=true;
	            	vm.showSS=false;
	            }
				
				//工作简历
				if(response.data.workDto && response.data.workDto.length > 0){
					vm.showWorkHistory = true;
					vm.workList=response.data.workDto;
					
				}
				//项目经验
				if(response.data.projectDto && response.data.projectDto.length > 0){
					vm.projectkHistory = true;
					vm.projectList=response.data.projectDto;					
				}
				
				if(response.data.expertTypeDtoList && response.data.expertTypeDtoList.length > 0){
					vm.expertTypeList = true;
					vm.expertTypeList=response.data.expertTypeDtoList;		
				}
				//专家聘书
				if(response.data.expertOfferDtoList && response.data.expertOfferDtoList.length > 0){
                    vm.showExpertOffer = true;
                    vm.expertOfferList = response.data.expertOfferDtoList;
                }
                initUpload(vm);
                $("#expertPhotoSrc").attr("src",rootPath+"/expert/transportImg?expertId="+vm.model.expertID+"&t="+Math.random());
			} 
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}//end#getExpertById								
		
		// begin#grid
		function grid(vm) {
			var  dataSource = common.kendoGridDataSource(rootPath+"/expert/findByOData",$("#searchform"));
			var  dataBound = function () {  
                var rows = this.items();  
                var page = this.pager.page() - 1;  
                var pagesize = this.pager.pageSize();  
                $(rows).each(function () {  
                    var index = $(this).index() + 1 + page * pagesize;  
                    var rowLabel = $(this).find(".row-number");  
                    $(rowLabel).html(index);  
                });  
            } 
						
			// End:column
			vm.gridOptions = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridConfig().filterable,
				pageable : common.kendoGridConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : getExpertColumns(),
				dataBound:dataBound,
				resizable : true
			};
		}// end fun grid
		
		function getExpertColumns(){
			var columns = [
				{
					template : function(item) {
						return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",item.expertID)
					},
					filterable : false,
					width : 40,
					title : "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
				},
				{  
				    field: "rowNumber",  
				    title: "序号",  
				    width: 50,
				    template: "<span class='row-number'></span>"  
			    },
				{
					field : "name",
					title : "姓名",
					width : 80,
					filterable : true
				},
				{
					field : "sex",
					title : "性别",
					width : 50,
					filterable : true
				},
				{
					field : "degRee",
					title : "学位",
					width : 50,
					filterable : true
				},
				{
					field : "userPhone",
					title : "手机号码",
					width : 100,
					filterable : true
				},
				{
					field : "comPany",
					title : "工作单位",
					width : 100,
					filterable : true
				},
				{
					field : "degRee",
					title : "职称",
					width : 100,
					filterable : true
				},				
				{
					field : "majorWork",
					title : "现从事专业",
					width : 100,
					filterable : true
				},
				{
					field : "acaDemy",
					title : "毕业院校",
					width : 100,
					filterable : true
				},
				{
					field : "expertSort",
					title : "专家类别",
					width : 100,
					filterable : true
				},
				{
					field : "",
					title : "操作",
					width : 100,
					template : function(item) {
						return common.format($('#columnBtns').html(), "vm.del('" + item.expertID + "')", item.expertID);
					}
				}
			];			
			return columns;
		}
		
		function getMinColumns(){
			var columns = [
				{
					template : function(item) {
						return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",item.expertID)
					},
					filterable : false,
					width : 25,
					title : "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
				},
				{
					field : "name",
					title : "姓名",
					width : 100,
					filterable : false
				},
				{
					field : "degRee",
					title : "学位",
					width : 100,
					filterable : false
				},
				{
					field : "sex",
					title : "性别",
					width : 50,
					filterable : true
				},
				{
					field : "comPany",
					title : "工作单位",
					width : 100,
					filterable : false
				},
				{
					field : "degRee",
					title : "职务",
					width : 100,
					filterable : false
				},{
					field : "",
					title : "专家类别",
					width : 100,
					filterable : false,
					template:function(item){
						if(item.expertTypeDtoList){
							var resultStr='';
							for(var i=0;i<item.expertTypeDtoList.length;i++){
								if(i==0){
									resultStr += item.expertTypeDtoList[i].expertType;
								}else{
									
									resultStr += "、"+item.expertTypeDtoList[i].expertType;
								}
							}
							return resultStr;
						}else{
						 return "";
						}
					}
				}
			];
			
			return columns;
		}
				
		//S_auditGrid
		function auditGrid(vm){
			var dataSource1 = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '1' and unable ne '1'"}),
				schema : common.kendoGridConfig().schema({
					id : "id",
					fields : {
						createdDate : {
							type : "date"
						}
					}
				}),
				serverPaging : true,
				serverSorting : true,
				serverFiltering : true,
				pageSize : 25,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});
			
			var dataSource2 = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '2' and unable ne '1'"}),
				schema : common.kendoGridConfig().schema({
					id : "id",
					fields : {
						createdDate : {
							type : "date"
						}
					}
				}),
				serverPaging : true,
				serverSorting : true,
				serverFiltering : true,
				pageSize : 5,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});						
			
			var dataSource3 = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '3' and unable ne '1'"}),
				schema : common.kendoGridConfig().schema({
					id : "id",
					fields : {
						createdDate : {
							type : "date"
						}
					}
				}),
				serverPaging : true,
				serverSorting : true,
				serverFiltering : true,
				pageSize : 5,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});
			
			var dataSource4 = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '4' and unable ne '1'"}),
				schema : common.kendoGridConfig().schema({
					id : "id",
					fields : {
						createdDate : {
							type : "date"
						}
					}
				}),
				serverPaging : true,
				serverSorting : true,
				serverFiltering : true,
				pageSize : 5,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});
			
			var dataSource5 = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '5' and unable ne '1'"}),
				schema : common.kendoGridConfig().schema({
					id : "id",
					fields : {
						createdDate : {
							type : "date"
						}
					}
				}),
				serverPaging : true,
				serverSorting : true,
				serverFiltering : true,
				pageSize : 5,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});
			
			var  dataBound = function () {  
                var rows = this.items();  
                var page = this.pager.page() - 1;  
                var pagesize = this.pager.pageSize();  
                $(rows).each(function () {  
                    var index = $(this).index() + 1 + page * pagesize;  
                    var rowLabel = $(this).find(".row-number");  
                    $(rowLabel).html(index);  
                });  
            } 
			
			vm.gridOptions1 = {
				dataSource : common.gridDataSource(dataSource1),
				filterable : common.kendoGridConfig().filterable,
				pageable : common.kendoGridConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : getMinColumns(),
				dataBound:dataBound,
				resizable : true
			};
			
			vm.gridOptions2 = {
				dataSource : common.gridDataSource(dataSource2),
				filterable : common.kendoGridConfig().filterable,
				pageable : common.kendoGridConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : getMinColumns(),
				dataBound:dataBound,
				resizable : true
			};
			
			vm.gridOptions3 = {
				dataSource : common.gridDataSource(dataSource3),
				filterable : common.kendoGridConfig().filterable,
				pageable : common.kendoGridConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : getMinColumns(),
				dataBound:dataBound,
				resizable : true
			};
			
			vm.gridOptions4 = {
					dataSource : common.gridDataSource(dataSource4),
					filterable : common.kendoGridConfig().filterable,
					pageable : common.kendoGridConfig().pageable,
					noRecords : common.kendoGridConfig().noRecordMessage,
					columns : getMinColumns(),
					dataBound:dataBound,
					resizable : true
				};
			
			vm.gridOptions5 = {
				dataSource : common.gridDataSource(dataSource5),
				filterable : common.kendoGridConfig().filterable,
				pageable : common.kendoGridConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : getMinColumns(),
				dataBound:dataBound,
				resizable : true
			};
		}//E_auditGrid				
		
		//S_searchAudit
		function searchAudit(vm){
			vm.gridOptions1.dataSource.read();	
			vm.gridOptions2.dataSource.read();
			vm.gridOptions3.dataSource.read();
			vm.gridOptions4.dataSource.read();
			vm.gridOptions5.dataSource.read();
		}//S_endAudit
		
		//S_repeatGrid
		function repeatGrid(vm){
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findRepeatByOData"),
				schema : common.kendoGridConfig().schema({
					id : "id"
				}),
				rowNumber: true,  
	            headerCenter: true
			});
			
			var  dataBound = function () {  
                var rows = this.items();   
                $(rows).each(function (i) {                    	
                     $(this).find(".row-number").html(i+1);                   
                });  
            } 
						
			// End:column
			vm.repeatGridOptions = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridConfig().filterable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : getExpertColumns(),
				dataBound:dataBound,
				resizable : true
			};
		}//E_repeatGrid
		
		//S_toAudit
		function toAudit(vm,flag){
        	var selectIds = common.getKendoCheckId('#grid'+flag);
        	if (selectIds.length == 0) {
        		common.alert({
        			vm:vm,
        			msg:'请选择数据'
        		});
        	}else{
        		var ids=[];
        		for (var i = 0; i < selectIds.length; i++) {
        			ids.push(selectIds[i].value);
        		}  
        		var idStr=ids.join(',');
        		updateAudit(vm,idStr,1);
        	}
        }//E_toAudit
		 
		//S_auditTo
		function auditTo(vm,flag){
			var selectIds = common.getKendoCheckId('#grid1');
	       if (selectIds.length == 0) {
	       	common.alert({
	           	vm:vm,
	           	msg:'请选择数据'
	           });
	       }else{
	       	var ids=[];
	           for (var i = 0; i < selectIds.length; i++) {
	           	ids.push(selectIds[i].value);
				}  
	           var idStr=ids.join(',');
	           updateAudit(vm,idStr,flag);
	       }
	   }//E_auditTo
		
		//begin updateAudit
		function updateAudit(vm,ids,flag){
			vm.isSubmit = true;
			var httpOptions = {
				method : 'post',
				url : url_expert+"/updateAudit",
				params:{
					ids:ids,
					flag:flag
				}		
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.isSubmit = false;
						vm.gridOptions1.dataSource.read();
						vm.gridOptions2.dataSource.read();
						vm.gridOptions3.dataSource.read();
						vm.gridOptions4.dataSource.read();
						vm.gridOptions5.dataSource.read();
						common.alert({
							vm : vm,
							msg : "操作成功"
						})	
					}
	
				});
	
			}
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}//end updateAudit
		
		
        //S_initUpload
        function initUpload(vm){
            var projectfileoptions = {
                language : 'zh',
                allowedPreviewTypes : ['image'],
                allowedFileExtensions : [ 'jpg', 'png', 'gif' ],
                maxFileSize : 2000,
                showRemove: false,
                uploadUrl:rootPath + "/expert/uploadPhoto",
                uploadExtraData:{expertId:vm.model.expertID}
            };
            $("#expertphotofile").fileinput(projectfileoptions).on("filebatchselected", function(event, files){

            }).on("fileuploaded", function(event, data) {
                $("#expertPhotoSrc").removeAttr("src");
                $("#expertPhotoSrc").attr("src",rootPath+"/expert/transportImg?expertId="+vm.model.expertID+"&t="+Math.random());
            });
        }//E_initUpload


	}
})();
(function () {
    'expert strict';

    angular.module('app').controller('expertReviewListCtrl', expertReviewList);

    expertReviewList.$inject = ['$location','expertSvc'];

    function expertReviewList($location, expertSvc) {
    	var vm = this;
    	vm.data={};
    	vm.title = '专家评分一览表';

        activate();
        function activate() {
        }


    }
})();

(function() {
	'expertType strict';

	angular.module('app').factory('expertTypeSvc', expertType);

	expertType.$inject = [ '$http','expertSvc' ,'$rootScope'];

	function expertType($http,expertSvc,$rootScope) {
		var service = {
			gotoExpertType : gotoExpertType,
			createExpertType : createExpertType,	//添加专家类型
			cleanValue : cleanValue,
			getExpertType : getExpertType,	//通过专家id获取专家类型
			updateExpertType : updateExpertType,	//进入更新专家类型页面
			getExpertTypeById : getExpertTypeById,	//	通过专家类型ID获取专家类型
			saveUpdate : saveUpdate,	//保存更新数据
			deleteExpertType : deleteExpertType	//删除专家类型
			
			

		};

		return service;
		
		// 清空页面数据
		// begin#cleanValue
		function cleanValue() {
			var tab = $("#addExpertType").find('input');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
		}
		
		//begin getExpertTypeByExpertId
		function getExpertType(vm){
			var httpOptions = {
				method : 'GET',
				url : rootPath + "/expertType/getExpertType?$filter=expert.expertID eq '"+vm.model.expertID+"'"
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.expertTypeList = response.data;
					}
				});
			}
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
			
			
		}
		
		//begin gotoExpertType
		function gotoExpertType(vm){
			var WorkeWindow = $("#addExpertType");
			WorkeWindow.kendoWindow({
				width : "690px",
				height : "400px",
				title : "添加专家类型",
				visible : false,
				modal : true,
				closable : true,
				actions : [ "Pin", "Minimize", "Maximize", "Close" ]
			}).data("kendoWindow").center().open();
		}//end gotoExpertType
		
		
		//begin getExpertTypeById
		function getExpertTypeById(vm){
			var httpOptions={
				method : "get",
				url : rootPath +"/expertType/getExpertTypeById",
				params:{expertTypeId: vm.expertTypeId}
			}
			
			var httpSuccess=function success(response){			
				vm.expertType=response.data;			
				vm.expertType.majobSmallDicts = $rootScope.topSelectChange(vm.expertType.maJorBig,$rootScope.DICT.MAJOR.dicts)
			}
			
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});

		}
		//end getExpertTypeById
		
		//begin createExpertType
		function createExpertType(vm){
			common.initJqValidation($('#expertTypeForm'));
			var isValid = $('#expertTypeForm').valid();
			if (isValid) {
				
				var httpOptions = {
					method : 'post',
					url : rootPath + "/expertType",
					data : vm.expertType
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							window.parent.$("#addExpertType").data("kendoWindow").close();
							cleanValue();
//							getExpertType(vm);
							expertSvc.getExpertById(vm);
							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									vm.projectTypeList = true;
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
								}
							})
						}

					});

				}
				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});

			}
		
		}//end createExpertType
		
		//begin updateExpertType
		function updateExpertType(vm){
			var isCheck=$("input[name='checkEType']:checked");
			if(isCheck.length<1){
				common.alert({
					vm : vm,
					msg : "请选择操作对象",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				});
			}else if(isCheck.length>1){
				common.alert({
					vm : vm,
					msg : "无法同时操作多条数据",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				});
			}else{
				vm.expertTypeId=isCheck.val();
				getExpertTypeById(vm);
				vm.expertID = vm.model.expertID;
				gotoExpertType(vm);
			}
			
		}//end
		
		//begin
		function saveUpdate(vm){
			common.initJqValidation($('#expertTypeForm'));
			var isValid = $('#expertTypeForm').valid();
			if (isValid) {
//			vm.model.id=vm.id;
			vm.expertType.expertID = vm.expertID;
			var httpOptions={
				method : "put",
				url : rootPath + "/expertType",
				data : vm.expertType
			}
			var httpSuccess=function success(response){
				
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						window.parent.$("#addExpertType").data("kendoWindow").close();
//						getExpertType(vm);
						expertSvc.getExpertById(vm);
						cleanValue();
						common.alert({
							vm : vm,
							msg : "操作成功",
							fn : function() {
								vm.isSubmit = false;
								$('.alertDialog').modal('hide');
							}
						})
					}
				
				})
			}
			
			common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});
		}
	}//end
	
	function deleteExpertType(vm){
		var isCheck=$("input[name='checkEType']:checked");
		if(isCheck.length<1){
			common.alert({
					vm : vm,
					msg : "请选择操作对象",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				});
		
		}else{
			var ids="";
			$.each(isCheck,function(i,obj){
				ids += obj.value+",";
			});
			
			vm.isSubmit=true;
			var httpOptions={
				method :"delete",
				url : rootPath + "/expertType",
				data : ids
			}
		var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.isSubmit = false;
							common.alert({
							vm : vm,
							msg : "操作成功",
							fn : function() {
								vm.isSubmit = false;
								$('.alertDialog').modal('hide');
							}
						});
						expertSvc.getExpertById(vm);
//							getExpertType(vm);
						}
					
					});
					
				}
				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});
		
		}
	
	}
	
	}

})();
(function() {
	'projectExpe strict';

	angular.module('app').factory('projectExpeSvc', projectExpe);

	projectExpe.$inject = [ '$http'];
	
	function projectExpe($http) {
		var service = {
				getProject : getProject,
				createProject : createProject,
				updateProject : updateProject,
				updateProjectPage : updateProjectPage,
				getProjectById : getProjectById,
				delertProject : delertProject,
				gotoJPage : gotoJPage,
				cleanValue : cleanValue,
				initProjectType : initProjectType
		};
		return service;
		
		//begin  initProjectType
		function initProjectType(vm){
			var code="PROJECTTYPE";
			var httpOptions={
				method: "get",
				url: rootPath +"/dict/getAllDictByCode",
				params:{dictCode :code}
			}
			var httpSuccess=function success(response){
				vm.projectTypes=response.data;
			}
			
			common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});
		}//end initProjectType
		
		//begin#delertProject
		function delertProject(vm){
			common.initJqValidation();
			var isCheck=$("input[name='checkpj']:checked");
			if(isCheck.length<1){
				common.alert({
					vm : vm,
					msg : "请选择操作对象",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				})
			}else{
				var ids="";
				$.each(isCheck,function(i,obj){
					ids+=obj.value+",";
				});
				
				/*for(var i=0;i<isCheck.length;i++){
					ids+=isCheck[i].val()+",";
				}*/
				vm.isSubmit = true;
				var httpOptions = {
						method : 'delete',
						url : rootPath + "/projectExpe/deleteProject",
						data : ids
						
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.isSubmit = false;
							getProject(vm);
						}
					
					});
					
				}
				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});
			}
		}
		////end#delertProject
		
		
		//begin#getProjectById
		function getProjectById(vm){
			var httpOptions = {
					method : 'get',
					url : common.format( rootPath + "/projectExpe/getProject?$filter=peID eq '{0}'", vm.peID)
			}
			var httpSuccess = function success(response) {
				//vm.model = response.data[0];
				vm.project={};
				vm.project.projectName=response.data[0].projectName;
				vm.project.projectType=response.data[0].projectType;
				vm.project.projectbeginTime=response.data[0].projectbeginTime;
				vm.project.projectendTime=response.data[0].projectendTime;
				
				//$('#projectbeginTime').val(response.data[0].projectbeginTime);
				//$('#projectendTime').val(response.data[0].projectendTime);
				if (vm.isUpdate) {
					//initZtreeClient(vm);
				}
			}
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
			
			
		}
		
		
		//begin#updateProject
		function updateProject(vm){
			common.initJqValidation($('#ProjectForm'));
			var isValid = $('#ProjectForm').valid();
			if (isValid) {
			vm.isSubmit = true;
			vm.project.peID=vm.peID;
			vm.project.expertID = vm.expertID;
			vm.project.projectbeginTime=$('#projectbeginTime').val();
			vm.project.projectendTime=$('#projectendTime').val();
			//alert(vm.model.projectendTime);
			var httpOptions = {
					method : 'put',
					url : rootPath + "/projectExpe/updateProject",
					data : vm.project
			}
			
			var httpSuccess = function success(response) {
				
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						window.parent.$("#pjwindow").data("kendoWindow").close();
						getProject(vm);
						cleanValue();
						common.alert({
							vm : vm,
							msg : "操作成功",
							fn : function() {
								vm.isSubmit = false;
								$('.alertDialog').modal('hide');
							}
						})
					}
				
				})
			}
			}
			
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
			
			//} else {
			// common.alert({
			// vm:vm,
			// msg:"您填写的信息不正确,请核对后提交!"
			// })
			//}
			
	}
		
		
		
		//begin#getProject
		function getProject(vm){
			var httpOptions = {
					method : 'GET',
					url :  rootPath + "/projectExpe/getProject?$filter=expert.expertID eq '"+vm.model.expertID+"'"
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.projectList=response.data;
					}
				
				});
			}
			
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
			
		}
		
		//begin#updateProject
		function updateProjectPage(vm){
			var isCheck=$("input[name='checkpj']:checked");
			if(isCheck.length<1){
				common.alert({
					vm : vm,
					msg : "请选择操作对象",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				})
			}else if(isCheck.length>1){
				common.alert({
					vm : vm,
					msg : "无法同时操作多条数据",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				})
			}else{
				vm.peID=isCheck.val();
				getProjectById(vm);
				vm.expertID = vm.model.expertID;
				gotoJPage(vm);																
			}
		}
		
		// begin#gotoJPage
		function gotoJPage(vm){
			/*var onClose=function(){
				window.location.href="#/expertEdit";
				
			};*/
			var WorkeWindow = $("#pjwindow");
			 WorkeWindow.kendoWindow({
	                width: "690px",
	                height: "330px",
	                title: "添加项目经验",
	                visible: false,
	                modal: true,
	                closable: true,
	                actions: [
	                    "Pin",
	                    "Minimize",
	                    "Maximize",
	                    "Close"
	                ]             
	            }).data("kendoWindow").center().open();
		}
		// end#gotoJPage
		
		//清空页面数据
		//begin#cleanValue
		function cleanValue(){
			var tab=$("#pjwindow").find('input');
			 $.each(tab,function(i,obj){
				 obj.value="";
			 });
		}
		// begin#createProject
		function createProject(vm){
			common.initJqValidation($('#ProjectForm'));
			var isValid = $('#ProjectForm').valid();
			if (isValid) {
				vm.project.projectbeginTime=$('#projectbeginTime').val();
				vm.project.projectendTime=$('#projectendTime').val();
				var httpOptions = {
					method : 'post',
					url : rootPath + "/projectExpe/projectExpe",
					data : vm.project
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							window.parent.$("#pjwindow").data("kendoWindow").close();
							cleanValue();
							getProject(vm);
							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									vm.projectkHistory = true;
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
									
								}
							})
						}

					});

				}
				
				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});

		}
		}
	}
})();
(function() {
	'workExpe strict';

	angular.module('app').factory('workExpeSvc', workExpe);

	workExpe.$inject = [ '$http','expertSvc' ];

	function workExpe($http,expertSvc) {
		var service = {
			createWork : createWork,
			updateWork : updateWork,
			deleteWork : deleteWork,
			updateWorkPage : updateWorkPage,
			gotoWPage : gotoWPage,
			getWorkById : getWorkById,
			getWork : getWork,
			cleanValue : cleanValue
		};

		return service;
		// begin#getWork
		function getWork(vm) {
			var httpOptions = {
				method : 'GET',
				url : rootPath + "/workExpe/getWork?$filter=expert.expertID eq '" + vm.model.expertID + "'"
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.workList = response.data;
//						console.log(vm.work);
					}
				});
			}
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}

		// begin#deleteWork
		function deleteWork(vm) {
			var isCheck = $("input[name='checkwr']:checked");
			if (isCheck.length < 1) {
				common.alert({
					vm : vm,
					msg : "请选择操作对象",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				})
			} else {
				var ids = "";
				$.each(isCheck, function(i, obj) {
					ids += obj.value + ",";
				});

				vm.isSubmit = true;
				var httpOptions = {
					method : 'delete',
					url : rootPath + "/workExpe/deleteWork",
					data : ids
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.isSubmit = false;
//							expertSvc.getExpertById(vm);
							getWork(vm);
						}
					});
				}
				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});
			}
		}
		// end#delertWork

		// begin#updateWork
		function updateWorkPage(vm) {
			var isCheck = $("input[name='checkwr']:checked");
			if (isCheck.length < 1) {
				common.alert({
					vm : vm,
					msg : "请选择操作对象",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				})
			} else if (isCheck.length > 1) {
				common.alert({
					vm : vm,
					msg : "无法同时操作多条数据",
					fn : function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
						return;
					}
				})
			} else {				
				vm.weID = isCheck.val();
				getWorkById(vm);
				gotoWPage(vm);
				vm.expertID = vm.model.expertID;

			}
		}

		// begin#getWorkById
		function getWorkById(vm) {
			var httpOptions = {
				method : 'get',
				url : common.format(rootPath + "/workExpe/getWork?$filter=weID eq '{0}'", vm.weID)
			}
			var httpSuccess = function success(response) {
				vm.work={};
				vm.work.companyName = response.data[0].companyName;
				vm.work.workJob = response.data[0].workJob;
				vm.work.beginTime = response.data[0].beginTime;
				vm.work.endTime = response.data[0].endTime;
			}
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});

		}

		// 清空页面数据
		// begin#cleanValue
		function cleanValue() {
			var tab = $("#wrwindow").find('input');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
		}

		// begin#gotoWPage
		function gotoWPage(vm) {
			var WorkeWindow = $("#wrwindow");
			WorkeWindow.kendoWindow({
				width : "690px",
				height : "330px",
				title : "添加工作简历",
				visible : false,
				modal : true,
				closable : true,
				actions : [ "Pin", "Minimize", "Maximize", "Close" ]
			}).data("kendoWindow").center().open();
		}
		// end#gotoWPage

		// begin#getWork
		/*function getWork(vm) {
			var httpOptions = {
				method : 'GET',
				url : rootPath+ "/workExpe/getWork?$filter=expert.expertID eq '"+ vm.model.expertID + "'"
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.work = response.data;
					}

				});
			}

			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});

		}*/
		// begin#createWork
		function createWork(vm) {
			common.initJqValidation($('#workForm'));
			var isValid = $('#workForm').valid();
			if (isValid) {
				/*vm.work.beginTime = $('#beginTime').val();
				vm.work.endTime = $('#endTime').val();*/
				
				var httpOptions = {
					method : 'post',
					url : rootPath + "/workExpe/workExpe",
					data : vm.work
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							cleanValue();
							window.parent.$("#wrwindow").data("kendoWindow").close();
							getWork(vm);
							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									vm.showWorkHistory = true;
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
								}
							})
						}

					});

				}
				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});

			}
		}

		// begin#updateWork
		function updateWork(vm) {
			common.initJqValidation();
			var isValid = $('form').valid();
			if (isValid) {
				vm.work.weID = vm.weID;
				vm.work.expertID = vm.expertID;
				vm.work.beginTime = $('#beginTime').val();
				vm.work.endTime = $('#endTime').val();

				var httpOptions = {
					method : 'put',
					url : rootPath + "/workExpe/updateWork",
					data : vm.work
				}

				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							window.parent.$("#wrwindow").data("kendoWindow").close();
							getWork(vm);
							cleanValue();
							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									vm.showWorkHistory = true;
									$('.alertDialog').modal('hide');
								}
							})
						}

					})
				}

				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});
			}
		}
	}

})();
(function () {
    'use strict';

    angular.module('app').factory('expertOfferSvc', expertOffer);

    expertOffer.$inject = ['$http','expertSvc'];

    function expertOffer($http,expertSvc) {
        var service = {
            saveOffer: saveOffer,	            //保存专家聘书

        };
        return service;

        //S_saveOffer
        function saveOffer(vm) {
            common.initJqValidation($("#expert_offer_form"));
            var isValid = $('#expert_offer_form').valid();
            if (isValid) {
                vm.expertOffer.expertId = vm.model.expertID
                var httpOptions = {
                    method : 'post',
                    url : rootPath + "/expertOffer",
                    data : vm.expertOffer
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm : vm,
                        response : response,
                        fn : function() {
                            expertSvc.getExpertById(vm);
                            common.alert({
                                vm : vm,
                                msg : "操作成功"
                            })
                        }

                    });
                }
                common.http({
                    vm : vm,
                    $http : $http,
                    httpOptions : httpOptions,
                    success : httpSuccess
                });
            }
        }//E_saveOffer

    }
})();
(function () {
    'use strict';

    angular.module('app').controller('expertReviewCtrl', expertReview);

    expertReview.$inject = ['$location', 'expertReviewSvc'];

    function expertReview($location, expertReviewSvc) {
        var vm = this;
        vm.title = '专家列表';

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    expertReviewSvc.deleteExpertReview(vm, id);
                }
            });
        }
        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: '请选择数据'
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.del(idStr);
            }
        };

        activate();
        function activate() {
            expertReviewSvc.initExpertGrid(vm);
            expertReviewSvc.getReviewList(vm);
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('expertReviewEditCtrl', expertReview);

    expertReview.$inject = ['$location', 'expertReviewSvc', '$state'];

    function expertReview($location, expertReviewSvc, $state) {
        var vm = this;
        vm.title = '添加附件';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新附件';
        }

        vm.create = function () {
            expertReviewSvc.createExpertReview(vm);
        };
        vm.update = function () {
            expertReviewSvc.updateExpertReview(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                expertReviewSvc.getExpertReviewById(vm);
            }
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('expertSelectCtrl', expertReview);

    expertReview.$inject = [ 'expertReviewSvc', 'expertConditionSvc', '$state','bsWin'];

    function expertReview(expertReviewSvc, expertConditionSvc, $state,bsWin) {
        var vm = this;
        vm.title = '选择专家';
        vm.conMaxIndex = 1;                   //条件号
        vm.conditions = new Array();         //条件列表
        vm.customCondition = new Array();
        vm.expertReview = {};                 //评审方案对象
        vm.confirmEPList = [];                //拟聘请专家列表（已经经过确认的专家）
        vm.matchEPMap = {};                  //保存符合条件的专家信息
        vm.selectIds = [],                    //已经抽取的专家信息ID（用于排除查询）
        vm.autoSelectedEPList = [];           //抽取结果列表，抽取方法在后面封装
        vm.workProgramId = $state.params.workProgramId;

        //刷新已经选择的专家信息
        vm.reFleshSelEPInfo = function(explist) {
            $.each(explist,function(i, obj){
                vm.confirmEPList.push(obj);
                vm.selectIds.push(obj.expertDto.expertID);
            })
            vm.excludeIds = vm.selectIds.join(',');
        }

        //删除后刷新
        vm.reFleshAfterRemove = function(ids){
            $.each(ids,function(i, obj){
                //1、删除已确认的专家
                $.each(vm.confirmEPList,function(index, epObj){
                    if(obj == epObj.id){
                        vm.confirmEPList.splice(index, 1);
                    }
                })
            })
        }

        //更新参加未参加状态
        vm.reFleshJoinState = function(ids,state){
            $.each(ids,function(i, obj){
                //1、删除已确认的专家
                $.each(vm.confirmEPList,function(index, epObj){
                    if(obj == epObj.id){
                        epObj.isJoin = state;
                    }
                })
            })
        }

        //更新是否确认状态
        vm.reFleshJoinState = function(ids,state){
            $.each(ids,function(i, obj){
                //1、删除已确认的专家
                $.each(vm.confirmEPList,function(index, epObj){
                    if(obj == epObj.id){
                        epObj.isConfrim = state;
                    }
                })
            })
        }

        //更新抽取条件的抽取次数
        vm.updateSelectedIndex = function(sort){
            if(sort){
                $.each(vm.conditions,function(i,con){
                    if(con.sort == sort){
                        con.selectIndex = (!con.selectIndex)?1:con.selectIndex+1;
                    }
                })
            }else{
                $.each(vm.conditions,function(i,con){
                    con.selectIndex = (!con.selectIndex)?1:con.selectIndex+1;
                })
            }
        }

        vm.init = function(workProgramId){
            expertReviewSvc.initReview(workProgramId,function(data){
                vm.expertReview = data;
                //专家抽取条件
                if (!angular.isUndefined(vm.expertReview.expertSelConditionDtoList) && angular.isArray(vm.expertReview.expertSelConditionDtoList)) {
                    vm.conditions = vm.expertReview.expertSelConditionDtoList;
                    $.each(vm.conditions,function(i,scdObj){
                        if(scdObj.sort > vm.conMaxIndex){
                            vm.conMaxIndex = scdObj.sort;
                        }
                    });
                }
                //获取已经抽取的专家
                if (!angular.isUndefined(vm.expertReview.expertSelectedDtoList) && angular.isArray(vm.expertReview.expertSelectedDtoList)) {
                    $.each(vm.expertReview.expertSelectedDtoList,function(i, sep){
                        vm.selectIds.push(sep.expertDto.expertID);
                        vm.confirmEPList.push(sep);
                    })
                    if (vm.selectIds.length > 0) {
                        vm.excludeIds = vm.selectIds.join(',');
                    } else {
                        vm.excludeIds = '';
                    }
                }

            });
        }

        activate();
        function activate() {
            expertReviewSvc.initExpertGrid(vm);
            vm.init(vm.workProgramId);
        }

        //弹出自选专家框
        vm.showSelfExpertGrid = function () {
            vm.selfExpertOptions.dataSource.read();
            $("#selfExpertDiv").kendoWindow({
                width: "860px",
                height: "500px",
                title: "自选评审专家",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //保存自选的专家
        vm.saveSelfExpert = function () {
            var selectIds = common.getKendoCheckId('#selfExpertGrid');
            if (selectIds.length == 0) {
                bsWin.alert("请先选择专家！");
            } else if (selectIds.length > 1) {
                bsWin.alert("自选专家最多只能选择一个！");
            }else{
                expertReviewSvc.saveSelfExpert(vm.workProgramId,selectIds[0].value, vm.expertReview.id,vm.isCommit,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        if(!vm.expertReview.id){
                            vm.expertReview.id = data.idCode;
                        }
                        //刷新
                        vm.reFleshSelEPInfo(data.reObj);
                        bsWin.success("操作成功！",function(){
                            window.parent.$("#selfExpertDiv").data("kendoWindow").close();
                        });
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }
        }

        //删除自选专家
        vm.delertSelfExpert = function () {
            var isCheck = $("input[name='seletedEp']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择要删除的专家");
            } else {
                bsWin.confirm({
                    title: "询问提示",
                    message: "删除数据不可恢复，确定删除么？",
                    onOk: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < isCheck.length; i++) {
                            ids.push(isCheck[i].value);
                        }
                        expertReviewSvc.delSelectedExpert(vm.expertReview.id, ids.join(','),vm.isCommit,function(data){
                            if(data.flag || data.reCode == 'ok'){
                                vm.reFleshAfterRemove(ids);
                                bsWin.success("操作成功！");
                            }else{
                                bsWin.error(data.reMsg);
                            }
                        });
                    },
                });
            }
        }

        //境外专家
        vm.showOutExpertGrid = function () {
            vm.outExpertOptions.dataSource.read();
            $("#outExpertDiv").kendoWindow({
                width: "860px",
                height: "500px",
                title: "自选新专家、市外、境外专家",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //删除选择的境外专家
        vm.delertOutSelfExpert = function () {
            var isCheck = $("input[name='seletedOutEp']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择要删除的专家");
            } else {
                bsWin.confirm({
                    title: "询问提示",
                    message: "删除数据不可恢复，确定删除么？",
                    onOk: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < isCheck.length; i++) {
                            ids.push(isCheck[i].value);
                        }
                        expertReviewSvc.delSelectedExpert(vm.expertReview.id, ids.join(','),vm.isCommit,function(data){
                            if(data.flag || data.reCode == 'ok'){
                                vm.reFleshAfterRemove(ids);
                                bsWin.success("操作成功！");
                            }else{
                                bsWin.error(data.reMsg);
                            }
                        });
                    },
                });
            }
        }

        //保存选择的境外专家
        vm.saveOutExpert = function () {
            var selectIds = common.getKendoCheckId('#outExpertGrid');
            if (selectIds.length == 0) {
                bsWin.alert("请先选择专家！");
                //$("#outExpertError").html("请选择一条专家数据才能保存！");
            } else {
                var selExpertIdArr = [];
                $.each(selectIds, function (i, obj) {
                    selExpertIdArr.push(obj.value);
                });
                expertReviewSvc.saveOutExpert(vm.workProgramId,selExpertIdArr.join(","), vm.expertReview.id, vm.isCommit, function (data) {
                    if(data.flag || data.reCode == 'ok'){
                        if(!vm.expertReview.id){
                            vm.expertReview.id = data.idCode;
                        }
                        vm.reFleshSelEPInfo(data.reObj);

                        bsWin.success("操作成功！",function(){
                            window.parent.$("#outExpertDiv").data("kendoWindow").close();
                        });
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }
        }

        //计算符合条件的专家
        vm.countMatchExperts = function (sortIndex) {
            if (vm.expertReview.id) {
                var postData = {};
                vm.conditions.forEach(function (t, number) {
                    if (t.sort == sortIndex) {
                        postData = t;
                        postData.maJorBig = $("#maJorBig" + t.sort).val();
                        postData.maJorSmall = $("#maJorSmall" + t.sort).val();
                        postData.expeRttype = $("#expeRttype" + t.sort).val();
                    }
                });
                postData.expertReviewDto = {};
                postData.expertReviewDto.id = vm.expertReview.id;   //抽取方案ID

                expertReviewSvc.countMatchExperts(postData,vm.workProgramId,vm.expertReview.id,function(data){
                    vm.matchEPMap[sortIndex] = data;
                    $("#expertCount" + sortIndex).html(data.length);
                });
            } else {
                bsWin.alert("请保存整体抽取方案再计算");
            }
        }

        //查看符合条件的专家信息
        vm.showMatchExperts = function(sortIndex){
            vm.matchExpertList = [];
            vm.matchExpertList = vm.matchEPMap[sortIndex];
            $("#matchExpertDiv").kendoWindow({
                width: "800px",
                height: "500px",
                title: "专家信息",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        vm.checkIntegerValue = function (checkValue, idStr, idSort) {
            if (expertConditionSvc.isUnsignedInteger(checkValue)) {
                $("#" + idStr + idSort).val(checkValue);
                $("#errorsOfficialNum" + idSort).html("");
                $("#errorsAlternativeNum" + idSort).html("");
            } else {
                $("#errorsOfficialNum" + idSort).html("只能填写数字");
                $("#errorsAlternativeNum" + idSort).html("只能填写数字");
            }
        }

        //添加随机抽取条件
        vm.addCondition = function () {
            if (vm.expertReview.isComfireResult == '9' || vm.expertReview.isComfireResult == 9) {
                bsWin.alert("当前项目已经进行整体专家方案的抽取，不能再修改方案！");
            } else {
                vm.condition = {};
                vm.condition.sort = vm.conMaxIndex+1;
                if (vm.expertReview.id) {
                    vm.condition.expertReviewDto = {};
                    vm.condition.expertReviewDto.id = vm.expertReview.id;   //抽取方案ID
                }
                vm.condition.selectType = "1";    //选择类型，这个一定不能少
                vm.conditions.push(vm.condition);
                vm.conMaxIndex++;
            }
        }

        //删除专家抽取条件
        vm.removeCondition = function () {
            if (vm.expertReview.isComfireResult == 9 || vm.expertReview.isComfireResult == '9' || vm.expertReview.selCount > 0 ) {
                bsWin.alert("当前项目已经进行整体专家方案的抽取，不能再修改方案！");
            } else {
                var isCheck = $("#conditionTable input[name='epConditionSort']:checked");
                if (isCheck.length > 0) {
                    bsWin.confirm({
                        title: "询问提示",
                        message: "删除数据不可恢复，确定删除么？",
                        onOk: function () {
                            $('.confirmDialog').modal('hide');
                            var ids = [];
                            for (var i = 0; i < isCheck.length; i++) {
                                $.each(vm.conditions,function(c,con){
                                    if (isCheck[i].value == con.sort) {
                                        if (con.id) {
                                            ids.push(con.id);
                                        }else{
                                            vm.conditions.splice(c, 1);     //没有保存抽取条件的直接删除
                                        }
                                    }
                                })
                            }
                            if(ids.length > 0){
                                expertConditionSvc.deleteSelConditions(ids.join(","),vm.isCommit,function(data){
                                    if(data.flag || data.reCode == 'ok'){
                                        bsWin.success("操作成功！");
                                        $.each(ids,function(i,id){
                                            $.each(vm.conditions,function(c,con){
                                                if (id == con.sort) {
                                                    vm.conditions.splice(c, 1);     //没有保存抽取条件的直接删除
                                                }
                                            })
                                        })
                                    }else{
                                        bsWin.error(data.reMsg);
                                    }
                                });
                            }else{
                                bsWin.success("操作成功！");
                            }
                        },
                    });
                }else{
                    bsWin.alert("请选择要删除的抽取条件！");
                }
            }
        }

        //检查是否为正整数
        function isUnsignedInteger(value) {
            if ((/^(\+|-)?\d+$/.test(value)) && value > 0) {
                return true;
            } else {
                return false;
            }
        }

        /******************************  以下是专家抽取方法 ***********************************/
        //封装专家抽取条件信息
        function buildCondition(checkId) {
            if (vm.conditions.length > 0) {
                var validateResult = true;
                vm.conditions.forEach(function (t, number) {
                    if (checkId) {
                        if (angular.isUndefined(t.id) || t.id == "") {
                            validateResult = false;
                        }
                    }
                    if (vm.expertReview.id) {
                        t.expertReviewDto = {};
                        t.expertReviewDto.id = vm.expertReview.id;   //抽取方案ID
                    }
                    t.workProgramId = vm.expertReview.workProgramId;
                    t.maJorBig = $("#maJorBig" + t.sort).val();
                    t.maJorSmall = $("#maJorSmall" + t.sort).val();
                    t.expeRttype = $("#expeRttype" + t.sort).val();
                    if ($("#officialNum" + t.sort).val() && isUnsignedInteger($("#officialNum" + t.sort).val())) {
                        t.officialNum = $("#officialNum" + t.sort).val();
                    } else {
                        $("#errorsOfficialNum" + t.sort).html("必填，且为数字");
                        validateResult = false;
                    }
                    if ($("#alternativeNum" + t.sort).val() && isUnsignedInteger($("#alternativeNum" + t.sort).val())) {
                        t.alternativeNum = $("#alternativeNum" + t.sort).val();
                    } else {
                        $("#errorsAlternativeNum" + t.sort).html("必填，且为数字");
                        validateResult = false;
                    }
                    if (validateResult) {
                        $("#errorsOfficialNum" + t.sort).html("");
                        $("#errorsAlternativeNum" + t.sort).html("");
                    }
                });
                return validateResult;
            } else {
                return false;
            }
        }

        //保存专家抽取条件
        vm.saveCondition = function () {
            if (vm.expertReview.isComfireResult == '9') {
                bsWin.alert("当前项目已经进行整体专家方案的抽取，不能再修改方案！");
            }else {
                if (buildCondition(false)) {
                    expertConditionSvc.saveCondition(vm.workProgramId,vm.conditions,function(data){
                        if(data.flag || data.reCode == 'ok'){
                            vm.conditions = data.reObj;
                            if(!vm.expertReview.id){
                                vm.expertReview.id = vm.conditions[0].expertReviewId;
                            }
                            //抽取方案ID
                            $.each(vm.conditions, function (i, obj) {
                                obj.expertReviewDto = {};
                                obj.expertReviewDto.id = vm.expertReview.id;
                            });
                            bsWin.success("保存成功！");
                        }else{
                            bsWin.error(data.reMsg);
                        }
                    });
                } else {
                    bsWin.alert("专家抽取条件设置不完整！");
                }
            }
        }

        //（整体方案抽取）开始随机抽取
        vm.startAutoExpertWin = function () {
            if (buildCondition(true)) {
                if(vm.expertReview.selCount > 0){
                    bsWin.alert("您已经进行整体专家抽取，不能再进行整体方案的抽取！");
                    return ;
                }
                if (vm.expertReview.isComfireResult == 9 || vm.expertReview.isComfireResult == '9' || vm.expertReview.selCount > 0) {
                    bsWin.alert("该方案已经进行整体专家方案的抽取，不能在继续抽取！");
                } else {
                    expertReviewSvc.queryAutoExpert(vm.conditions,vm.workProgramId,vm.expertReview.id,function(data){
                        if(data.flag || data.reCode == 'ok'){
                            //刷新页面抽取的专家
                            vm.reFleshSelEPInfo(data.reObj.autoEPList);
                            //抽取次数加一
                            vm.expertReview.selCount = data.reObj.selCount;
                            //抽取结果数组
                            vm.autoSelectedEPList = [];
                            vm.autoSelectedEPList = data.reObj.autoEPList;
                            //刷新抽取次数
                            vm.updateSelectedIndex();
                            //弹框
                            vm.showAutoExpertWin();
                            //显示抽取效果
                            expertReviewSvc.validateAutoExpert(data.reObj.allEPList,vm);
                        }else{
                            bsWin.error(data.reMsg);
                        }
                    });
                }
            } else {
                bsWin.alert("请先保存编辑的抽取方案！");
            }
        }

        //显示随机抽取框
        vm.showAutoExpertWin = function () {
            $("#aotuExpertDiv").kendoWindow({
                width: "1024px",
                height: "600px",
                title: "专家抽取",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //显示随机抽取结果
        vm.showAutoMatchResultWin = function () {
            $("#aotuMatchResultDiv").kendoWindow({
                width: "1024px",
                height: "500px",
                title: "专家抽取结果",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //再次抽取专家
        vm.repeatAutoExpert = function(conSort) {
            var condition = [];
            $.each(vm.conditions,function(i,con){
                if(con.sort == conSort){
                    condition.push(con);
                }
            })
            if(condition[0].selectIndex > 3){
                bsWin.alert("该条件已经进行了3次抽取，不能再继续抽取！");
                return ;
            }
            expertReviewSvc.queryAutoExpert(condition,vm.workProgramId,vm.expertReview.id,function(data){
                if(data.flag || data.reCode == 'ok'){
                    //刷新页面抽取的专家
                    vm.reFleshSelEPInfo(data.reObj.autoEPList);
                    //抽取次数加一
                    vm.expertReview.selCount = data.reObj.selCount;
                    //抽取结果数组
                    vm.autoSelectedEPList = [];
                    vm.autoSelectedEPList = data.reObj.autoEPList;
                    //刷新抽取次数
                    vm.updateSelectedIndex(conSort);
                    //弹框
                    vm.showAutoExpertWin();
                    //显示抽取效果
                    expertReviewSvc.validateAutoExpert(data.reObj.allEPList,vm);
                }else{
                    bsWin.error(data.reMsg);
                }
            });
        }

        //确认已抽取的专家
        vm.affirmAutoExpert = function () {
            var isCheck = $("#allAutoEPTable input[name='autoEPCheck']:checked");
            if(isCheck.length < 1){
                bsWin.alert("请选择要确认的抽取专家！");
                return ;
            }
            var ids = [];
            for (var i = 0; i < isCheck.length; i++) {
                ids.push(isCheck[i].value);
            }
            expertReviewSvc.affirmAutoExpert(vm.expertReview.id,ids.join(","),'9',function(data){
                bsWin.success("操作成功");
                vm.reFleshJoinState(ids,"9");
            })

        }

        //确定实际参加会议的专家
        vm.affirmJoinExpert = function () {
            $("#confirmJoinExpert").kendoWindow({
                width: "960px",
                height: "600px",
                title: "参加评审会专家确认",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //未参加改为参加
        vm.updateToJoin = function () {
            var isCheck = $("#notJoinExpertTable input[name='notJoinExpert']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择要改为参加会议的专家");
            } else {
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    ids.push(isCheck[i].value);
                }
                expertReviewSvc.updateJoinState( ids.join(','), '9',vm.isCommit,function(data){
                    bsWin.success("操作成功！");
                    vm.reFleshJoinState(ids,'9');
                });
            }
        }

        //参加改为未参加
        vm.updateToNotJoin = function () {
            var isCheck = $("#joinExpertTable input[name='joinExpert']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择未参加会议的专家");
            } else {
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    ids.push(isCheck[i].value);
                }
                expertReviewSvc.updateJoinState( ids.join(','), '0',vm.isCommit,function(data){
                    bsWin.success("操作成功！");
                    vm.reFleshJoinState(ids,'0');
                });
            }
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').factory('expertReviewSvc', expertReview);

    expertReview.$inject = ['$http', '$interval'];

    function expertReview($http, $interval) {
        var service = {
            initExpertGrid: initExpertGrid,	            //初始化待抽取专家列表
            saveSelfExpert: saveSelfExpert,		        //保存自选专家
            saveOutExpert: saveOutExpert,               //保存选择的境外专家
            countMatchExperts: countMatchExperts,       //计算符合条件的专家
            getReviewList: getReviewList,               //查询专家评分

            //以下为新方法
            initReview: initReview,                      //初始化评审方案信息
            delSelectedExpert: delSelectedExpert,        //删除已选专家信息
            queryAutoExpert: queryAutoExpert,            //查询符合抽取条件的专家
            validateAutoExpert: validateAutoExpert,      //显示抽取专家效果(抽取方法已在后台封装)
            affirmAutoExpert: affirmAutoExpert,	         //确认已经抽取的专家
            updateJoinState: updateJoinState,            //更改是否参加状态
        };
        return service;

        //S_initReview
        function initReview(workProgramId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/initByWPId",
                params: {
                    workProgramId:workProgramId
                }
            };
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_initReview

        function getMinColumns() {
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />", item.expertID)
                    },
                    filterable: false,
                    width: 25,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "name",
                    title: "姓名",
                    width: 100,
                    filterable: false
                },
                {
                    field: "degRee",
                    title: "学位",
                    width: 100,
                    filterable: false
                },
                {
                    field: "sex",
                    title: "性别",
                    width: 50,
                    filterable: true
                },
                {
                    field: "comPany",
                    title: "工作单位",
                    width: 100,
                    filterable: false
                },
                {
                    field: "degRee",
                    title: "职务",
                    width: 100,
                    filterable: false
                }, {
                    field: "expeRttype",
                    title: "专家类别",
                    width: 100,
                    filterable: false
                }
            ];
            return columns;
        }

        function initExpertGrid(vm) {
            var dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            }

            //S_专家自选
            var dataSource2 = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/expert/findByOData", $("#selfSelExpertForm")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            vm.selfExpertOptions = {
                dataSource: common.gridDataSource(dataSource2),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: getMinColumns(),
                dataBound: dataBound,
                resizable: true
            };//E_专家自选


            //S_市外专家
            var dataSource3 = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/expert/findByOData", $("#outSelExpertForm"), {filter: "state eq '3'"}),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            vm.outExpertOptions = {
                dataSource: common.gridDataSource(dataSource3),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: getMinColumns(),
                dataBound: dataBound,
                resizable: true
            };//E_市外专家
        }

        //S_saveSelfExpert
        function saveSelfExpert(workProgramId,expertId,expertReviewId,isCommit,callBack) {
            isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/saveExpertReview",
                params: {
                    workProgramId : workProgramId,
                    reviewId: angular.isUndefined(expertReviewId)?"":expertReviewId,
                    expertIds: expertId,
                    selectType: "2"
                }
            }
            var httpSuccess = function success(response) {
                isCommit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                    isCommit = false;
                }
            });
        }//E_saveSelfExpert

        //S_保存境外专家
        function saveOutExpert(workProgramId,selExpertIds,expertReviewId,isCommit,callBack) {
            isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/saveExpertReview",
                params: {
                    workProgramId:workProgramId,
                    reviewId: angular.isUndefined(expertReviewId)?"":expertReviewId,
                    expertIds: selExpertIds,
                    selectType: "3"
                }
            }
            var httpSuccess = function success(response) {
                isCommit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                    isCommit = false;
                }
            });
        }//E_saveOutExpert

        //S_countMatchExperts
        function countMatchExperts(postData,workProgramId,expertReviewId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expert/countReviewExpert",
                data: postData,
                params: {
                    workprogramId:workProgramId,
                    reviewId: expertReviewId
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_countMatchExperts

        //begin##getReviewList
        function getReviewList(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/expertReview/html/getReviewList"
            }
            var httpSuccess = function success(response) {
                vm.reviewList = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                    vm.isCommit = false;
                }
            });
        }//end##getReviewList

        //S_queryAutoExpert
        function queryAutoExpert(conditionArr,workProgramId,expertReviewId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expert/autoExpertReview",
                headers: {
                    "contentType": "application/json;charset=utf-8"  //设置请求头信息
                },
                traditional: true,
                dataType: "json",
                data: angular.toJson(conditionArr),//将Json对象序列化成Json字符串，JSON.stringify()原生态方法
                params: {
                    workprogramId: workProgramId,
                    reviewId: expertReviewId
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_queryAutoExpert


        //S_validateAutoExpert
        function validateAutoExpert(allEPList,vm) {
            //随机抽取
            var timeCount = 0,totalExpertCount = vm.autoSelectedEPList.length,index = 0;
            var interValVar = $interval(function () {
                if (totalExpertCount == 0) {
                    $interval.cancel(interValVar);
                }else{
                    var selscope = Math.floor(Math.random() * (allEPList.length));
                    vm.showAutoExpertName = allEPList[selscope].name;
                    timeCount++;
                    if (timeCount % 10 == 0) {
                        vm.autoSelectedEPList[index].show = true;
                        vm.autoSelectedEPList[index+1].show = true;
                        index = index + 2;
                        totalExpertCount = totalExpertCount-2;
                    }
                }
            }, 200);



        }//E_validateAutoExpert

        //S_updateJoinState
        function updateJoinState(ids, joinState,isCommit,callBack) {
            isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/updateJoinState",
                params: {
                    expertSelId: ids,
                    state: joinState
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                    isCommit = false;
                }
            });
        }//E_updateJoinState

        //S_affirmAutoExpert(确认抽取专家)
        function affirmAutoExpert(reviewId,seletedIds,joinState,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/affirmAutoExpert",
                params: {
                    reviewId: reviewId,
                    expertSelId:seletedIds,
                    state: joinState
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                }
            });
        }//E_affirmAutoExpert

        //S_delSelectedExpert(删除已选专家)
        function delSelectedExpert(expertReviewId, delIds,isCommit,callBack) {
            isCommit = true;
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/expertSelected",
                params: {
                    reviewId: expertReviewId,
                    id: delIds,
                    deleteAll: false
                }
            }
            var httpSuccess = function success(response) {
                isCommit = false;
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                    isCommit = false;
                }
            });
        }//E_delSelectedExpert

    }
})();
(function () {
    'use strict';

    angular.module('app').factory('expertConditionSvc', expertCondition);

    expertCondition.$inject = ['$http'];

    function expertCondition($http) {
        var service = {
        	saveCondition:saveCondition,	    //保存抽取条件
            deleteSelConditions:deleteSelConditions,    //删除抽取条件
            isUnsignedInteger : isUnsignedInteger,  //验证是否是正整数
        };
        return service;

        //S_saveCondition
		function saveCondition(workProgramId,conditions,callBack) {
            var httpOptions = {
                method : 'post',
                url : rootPath + "/expertSelCondition/saveConditionList",
                headers:{
                    "contentType":"application/json;charset=utf-8"  //设置请求头信息
                },
                traditional: true,
                dataType : "json",
                data : angular.toJson(conditions),//将Json对象序列化成Json字符串，JSON.stringify()原生态方法
                params:{
                    workProgramId:workProgramId
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess
            });
        }//E_saveCondition

        //检查是否为正整数
        function isUnsignedInteger(value){
            if((/^(\+|-)?\d+$/.test(value)) && value>0 ){
                return true;
            }else{
                return false;
            }
        }

        //S_deleteSelConditions
        function deleteSelConditions(delIds,isCommit,callBack){
            isCommit = true;
            var httpOptions = {
                method : 'delete',
                url : rootPath + "/expertSelCondition",
                params:{
                    ids : delIds
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess,
                onError: function(response){isCommit = false;}
            });
        }//E_deleteSelConditions
    }
})();
(function () {
    'use strict';

    angular.module('app').controller('fileRecordEditCtrl', fileRecord);

    fileRecord.$inject = ['fileRecordSvc','$state'];

    function fileRecord(fileRecordSvc,$state) {
        var vm = this;
        vm.title = '项目归档编辑';

        vm.fileRecord = {};
        vm.fileRecord.signId = $state.params.signid;
        vm.signId = $state.params.signid;

        activate();
        function activate(){
            fileRecordSvc.initFileRecordData(vm);
        }

        vm.create = function(){
            fileRecordSvc.saveFileRecord(vm);
        }

    }
})();

(function () {
    'use strict';

    angular.module('app').factory('fileRecordSvc', fileRecord);

    fileRecord.$inject = ['sysfileSvc', '$http'];

    function fileRecord(sysfileSvc, $http) {
        var service = {
            initFileRecordData: initFileRecordData,		//初始化流程数据
            saveFileRecord: saveFileRecord,				//保存

        };
        return service;

        //S_初始化
        function initFileRecordData(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/fileRecord/html/initFillPage",
                params: {signId: vm.fileRecord.signId}
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        if (response.data != null && response.data != "") {
                            vm.fileRecord = response.data.file_record;
                            
                            vm.fileRecord.signId = vm.signId;
                            vm.signUserList = response.data.sign_user_List;

                            //初始化附件上传
                            if(vm.fileRecord.fileRecordId){
	                            sysfileSvc.initUploadOptions({
	                                businessId: vm.fileRecord.fileRecordId,
	                                sysSignId: vm.fileRecord.signId,
	                                sysfileType: "归档",
	                                uploadBt: "upload_file_bt",
	                                detailBt: "detail_file_bt",
	                                vm: vm
	                            });
                            }
                        }
                    }

                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_初始化

        //S_保存
        function saveFileRecord(vm) {
            common.initJqValidation($("#fileRecord_form"));
            var isValid = $("#fileRecord_form").valid();
            if (isValid) {
                vm.signUserList.forEach(function(su,index){
                    if(vm.fileRecord.signUserid == su.id){
                        vm.fileRecord.signUserName = su.displayName;
                        return;
                    }
                })

                vm.isCommit = true;
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/fileRecord",
                    data: vm.fileRecord
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            vm.isCommit = false;
                            common.alert({
                                vm: vm,
                                msg: response.data.reMsg,
                                closeDialog: true,
                                fn: function () {
                                    if (response.data.reCode == "error") {
                                        vm.isCommit = false;
                                    } else {
                                        if(!vm.fileRecord.fileRecordId){
                                            vm.fileRecord = response.data.reObj;
                                            vm.fileRecord.signId = vm.signId;
                                            //初始化附件上传
                                            sysfileSvc.initUploadOptions({
                                                businessId: vm.fileRecord.fileRecordId,
                                                sysSignId: vm.fileRecord.signId,
                                                sysfileType: "归档",
                                                uploadBt: "upload_file_bt",
                                                detailBt: "detail_file_bt",
                                                vm: vm
                                            });
                                        }
                                    }
                                }
                            })
                        }
                    });
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess,
                    onError: function (response) {
                        vm.isCommit = false;
                    }
                });
            }
        }//E_保存

    }
})();
(function () {
    'use strict';

    angular.module('app').controller('financialManagerCtrl', financialManager);

    financialManager.$inject = ['$location', 'financialManagerSvc','$state','$http'];

    function financialManager($location, financialManagerSvc,$state,$http) {
        var vm = this;
        vm.title = '财务管理';
        vm.financials = new Array;
        vm.sign = {}; //收文对象
        vm.financial = {};//财务对象
        vm.financial.signid = $state.params.signid;
        
        //S 输入数字校验
        vm.inputIntegerValue = function(checkValue,idSort){
        	if(financialManagerSvc.isUnsignedInteger(checkValue)){
        		$("#errorsUnmber" + idSort).html("");
        	}else{
        		$("#errorsUnmber" + idSort).html("只能输入数字");
        	}
        }
        //E 输入数字校验
        
        //检查是否为正整数
        function isUnsignedInteger(value) {
            if ((/^(\+|-)?\d+$/.test(value)) && value > 0) {
                return true;
            } else {
                return false;
            }
        }
        
        //添加报销记录
       vm.addFinancial =  function () {
    	   	var projectName = $("#projectName").val();
    	    var signid =vm.financial.signid;
        	vm.financial = {};
        	vm.financial.chargeType ="评审项目"; 
        	vm.financial.signid = signid ;
        	vm.financial.projectName= projectName;
            vm.financials.push(vm.financial);
            vm.i++;
        }// end
       
       //保存报销记录
       vm.saveFinancial = function (){
    	   financialManagerSvc.savefinancial(vm);
       }
       //删除报销记录
       vm.deleteFinancial = function(){
    	   var isChecked = $("#financialsTable input[name='financialsCheck']:checked");
    	   if(isChecked.length < 1){
    		   common.alert({
                   vm:vm,
                   msg:"请选择要删除的记录！"
               })
    	   }else{
    		   var ids = [];
    		   for(var i = 0; i <isChecked.length ;i++){
    			   vm.financials.forEach(function( f , number){
    				   if(isChecked[i].value == f.id || f.id == undefined){
    					   vm.financials.splice(number,1);
    				   }
    				   ids.push(isChecked[i].value);
    			   });
    				var idsStr = ids.join(",");
    				 financialManagerSvc.deleteFinancialManager(vm,idsStr);
    		   }
    	   }
       }
    
        activate();
        function activate() {
            financialManagerSvc.grid(vm);
            financialManagerSvc.sumFinancial(vm);
            financialManagerSvc.initFinancialProject(vm);
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('financialManagerEditCtrl', financialManager);

    financialManager.$inject = ['$location', 'financialManagerSvc', '$state'];

    function financialManager($location, financialManagerSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加财务管理';
        vm.sign = {}; //收文对象
        vm.financial = {};//财务对象
        vm.isuserExist = false;
        vm.id = $state.params.id;
        vm.financial.signid = $state.params.signid;
     
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新财务管理';
        }

        vm.create = function () {
        	
            financialManagerSvc.createFinancialManager(vm);
        };
        vm.update = function () {
            financialManagerSvc.updateFinancialManager(vm);
        };
    
        activate();
        function activate() {
            if (vm.isUpdate) {
                financialManagerSvc.getFinancialManagerById(vm);
            }
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').factory('financialManagerSvc', financialManager);

    financialManager.$inject = ['$http'];

    function financialManager($http) {
        var url_financialManager = rootPath + "/financialManager", url_back = '#/financialManagerList';
        var service = {
            grid: grid,
            deleteFinancialManager: deleteFinancialManager,
            savefinancial:savefinancial,//保存报销记录
            sumFinancial:sumFinancial,//统计评审费用总和
            initFinancialProject:initFinancialProject,//初始化关联项目评审费
            isUnsignedInteger:isUnsignedInteger,	//	数字校验
        };

        return service;
      //检查是否为正整数
        function isUnsignedInteger(value){
            if((/^(\+|-)?\d+$/.test(value)) && value>0 ){
                return true;
            }else{
                return false;
            }
        }
       //S 初始化关联项目评审费
        function initFinancialProject(vm){
        	var httpOptions = {
                    method: 'get',
                    url: rootPath + "/financialManager/initfinancial",
                    params:{
                    	signid: vm.financial.signid
                    }
                };
                var httpSuccess = function success(response) {
                    vm.model = response.data.financialDto;
                    vm.financials = response.data.financiallist;
                    
                };
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });            
        }
       // E 初始化关联项目评审费
        
        //S 统计评审费用总和
       function  sumFinancial(vm){
    		var httpOptions = {
                    method: 'get',
                    url: rootPath + "/financialManager/html/sumfinancial",
                    params:{
                    	signId: vm.financial.signid
                    }
                };
                var httpSuccess = function success(response) {
                	vm.financial.stageCount = response.data;
                   $("#financialCount").html(vm.financial.stageCount);
                   
                };

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
               });   
       }
     //E 统计评审费用总和
       
       //S 保存报销记录
       function savefinancial(vm){
    		   var httpOptions = {
    				   method : 'post',
    				   url : rootPath + "/financialManager",
    				   headers:{
    					   "contentType":"application/json;charset=utf-8"  //设置请求头信息
    				   },
    				   traditional: true,
    				   dataType : "json",
    				   data : angular.toJson(vm.financials),//将Json对象序列化成Json字符串，JSON.stringify()原生态方法
    		   }
    		   var httpSuccess = function success(response) {
    			   common.requestSuccess({
    				   vm : vm,
    				   response : response,
    				   fn : function() {
    					   common.alert({
    						   vm: vm,
    						   msg: "操作成功",
    						   fn: function () {
    							   myrefresh();
    						   }
    					   })
    				   }
    			   });
    		 
    	   }

    	   common.http({
    		   vm : vm,
    		   $http : $http,
    		   httpOptions : httpOptions,
    		   success : httpSuccess
    	   });
       }
       //E 保存报销记录
        //刷新页面
        function myrefresh(){
        	 window.location.reload();
        }

        // begin#deleteFinancialManager
        function deleteFinancialManager(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_financialManager,
                data: id
            };

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                    	common.alert({
                            vm: vm,
                            msg: "操作成功",
                            closeDialog :true,
                            fn: function () {
                            	vm.isSubmit = false;
                            	myrefresh();
                            }
                        })
                    }
                });
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        // end#deleteFinancialManager

        // begin#grid
        function grid(vm) {

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_financialManager),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            // End:dataSource

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "id",
                    title: "id",
                    width: 100,
                    filterable: true
                },
                {
                    field: "chargeName",
                    title: "chargeName",
                    width: 100,
                    filterable: true
                },
                {
                    field: "charge",
                    title: "charge",
                    width: 100,
                    filterable: true
                },
                {
                    field: "remarke",
                    title: "remarke",
                    width: 100,
                    filterable: true
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.del('" + item.id + "')", item.id);
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true
            };

        }// end fun grid

    }
})();
(function () {
    'use strict';

    angular.module('app').factory('flowSvc', flow);

    flow.$inject = ['$http', '$state', 'signFlowSvc'];

    function flow($http, $state, signFlowSvc) {
        var service = {
            initFlowData: initFlowData, // 初始化流程数据
            getFlowInfo: getFlowInfo, // 获取流程信息
            commit: commit, // 提交
            rollBackToLast: rollBackToLast, // 回退到上一环节
            rollBack: rollBack, // 回退到选定环节
            initBackNode: initBackNode, // 初始化回退环节信息
            suspendFlow: suspendFlow, // 流程挂起
            activeFlow: activeFlow, // 重启流程
            deleteFlow: deleteFlow, // 流程终止
            saveMark: saveMark, // 保存专家评分
            savePayment: savePayment, // 保存专家费用
            countTaxes: countTaxes, // 计算应纳税额
            gotopayment: gotopayment,
        };
        return service;

        // S_初始化流程数据
        function initFlowData(vm) {
            var processInstanceId = vm.flow.processInstanceId;
            if (angular.isUndefined(vm.flow.hideFlowImg)|| vm.flow.hideFlowImg == false) {
                vm.picture = rootPath + "/flow/processInstance/img/"+ processInstanceId;
            }
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath+ "/flow/processInstance/history/" + processInstanceId),
                schema: common.kendoGridConfig().schema({
                    id: "id"
                }),
                rowNumber: true,
                headerCenter: true
            });

            var columns = [{
                field: "",
                title: "序号",
                template: "<span class='row-number'></span>",
                width: 40
            }, {
                field: "nodeName",
                title: "环节名称",
                width: 120,
                filterable: false
            }, {
                field: "displayName",
                title: "处理人",
                width: 80,
                filterable: false
            }, {
                field: "startTime",
                title: "开始时间",
                width: 120,
                filterable: false,
                format: "{0: yyyy-MM-dd HH:mm:ss}"
            }, {
                field: "endTime",
                title: "结束时间",
                width: 120,
                filterable: false,
                format: "{0: yyyy-MM-dd HH:mm:ss}"
            }, {
                field: "durationStr",
                title: "处理时长",
                width: 120,
                filterable: false
            }, {
                field: "message",
                title: "处理信息",
                width: 300,
                filterable: false
            }];
            // End:column
            vm.historygrid = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                dataBound: function () {
                    var rows = this.items();
                    $(rows).each(function (i) {
                        if (i == rows.length - 1) {
                            initBackNode(vm);
                        }
                        $(this).find(".row-number").html(i + 1);
                    });
                }
            };
        }// E_初始化流程数据

        // S_getFlowInfo
        function getFlowInfo(taskId,processInstanceId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/flow/processInstance/flowNodeInfo",
                params: {
                    taskId: taskId,
                    processInstanceId:processInstanceId
                }
            }

            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }// E_getFlowInfo

        // S_提交下一步
        function commit(isCommit,flowObj,callBack) {
            common.initJqValidation($("#flow_form"));
            var isValid = $("#flow_form").valid();
            if (isValid) {
                isCommit = true;
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/flow/commit",
                    data: flowObj
                }
                var httpSuccess = function success(response) {
                    isCommit = false;
                    if (callBack != undefined && typeof callBack == 'function') {
                        callBack(response.data);
                    }
                }
                common.http({
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess,
                    onError: function (response) {
                        isCommit = false;
                    }
                });
            }
        }// E_提交下一步

        // S_回退到上一步
        function rollBackToLast(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/flow/rollbacklast",
                data: vm.flow
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: response.data.reMsg,
                            closeDialog: true,
                            fn: function () {
                                if (response.data.reCode == "error") {
                                    vm.isCommit = false;
                                } else {
                                    $state.go('gtasks');
                                }
                            }
                        })
                    }

                })
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                    vm.isCommit = false;
                }
            });
        }// E_回退到上一步

        // S_回退到指定环节
        function rollBack(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/flow/rollbacklast",
                data: vm.flow
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: response.data.reMsg
                        })
                    }
                })
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }// E_回退到指定环节

        // S_初始化回退环节信息
        function initBackNode(vm) {
            vm.backNode = [];
            // 初始化可回退环节
            var datas = vm.historygrid.dataSource.data()
            var totalNumber = datas.length;
            for (var i = 0; i < totalNumber; i++) {
                if (datas[i].assignee && datas[i].endTime) {
                    vm.backNode.push({
                        "activitiId": datas[i].activityId,
                        "activitiName": datas[i].activityName,
                        "assignee": datas[i].assignee
                    });
                }
            }
        }// E_初始化回退环节信息

        // S_流程挂起
        function suspendFlow(vm, businessKey) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/flow/suspend/" + businessKey,
                data : vm.projectStop
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: response.data.reMsg,
                            closeDialog: true,
                            fn: function () {
                                if (response.data.reCode == "error") {
                                    vm.isCommit = false;
                                } else {
                                    window.parent.$("#spwindow").data("kendoWindow").close();
                                    vm.gridOptions.dataSource.read();
                                }
                            }
                        })
                    }
                })
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError : function () {
                    vm.isCommit = false;
                }
            });
        }// E_流程挂起

        // S_流程激活
        function activeFlow(vm, businessKey) {
            vm.isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/flow/active/" + businessKey
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: response.data.reMsg,
                            closeDialog: true,
                            fn: function () {
                                if (response.data.reCode == "error") {
                                    vm.isCommit = false;
                                } else {
                                    vm.gridOptions.dataSource.read();
                                }
                            }
                        })
                    }
                })
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError : function () {
                    vm.isCommit = false;
                }
            });
        }// E_流程激活

        // S_终止流程
        function deleteFlow(vm) {
            if (vm.flow.dealOption == null || vm.flow.dealOption == "") {
                common.alert({
                    vm: vm,
                    msg: "请填写处理信息！"
                })
                return;
            }
            var httpOptions = {
                method: 'post',
                url: rootPath + "/flow/deleteFLow",
                data: vm.flow
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: "操作成功！"
                        })
                    }
                })
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }// E_终止流程

        // S_saveMark
        function saveMark(vm,callBack) {
            common.initJqValidation($('#markform'));
            var isValid = $('#markform').valid();
            if (isValid) {
                var httpOptions = {
                    method: 'put',
                    url: rootPath + "/expertSelected",
                    data: vm.businessFlag.expertScore,
                }

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            window.parent.$("#expertmark") .data("kendoWindow").close();
                            vm.isSubmit = false;
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                closeDialog: true,
                                fn: function () {
                                    //重新初始化评审费发放数据
                                    if (callBack != undefined && typeof callBack == "function") {
                                        callBack();
                                    }
                                }
                            });
                        }
                    });
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }// E_saveMark

        // S_savePayment
        function savePayment(vm,callBack) {
            common.initJqValidation($('#payform'));
            var isValid = $('#payform').valid();
            if (isValid) {
                if (!validateNum(vm, vm.businessFlag.expertReviews)) {
                    common.alert({
                        vm: vm,
                        msg: "应纳税额计算错误,保存失败！",
                        fn: function () {
                            $('.alertDialog').modal('hide');
                            $('.modal-backdrop').remove();
                        }
                    });
                    vm.isCommit = false;
                    return;
                }
                vm.isCommit = true;
                var httpOptions = {
                    method: 'post',
                    //headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    url: rootPath + "/expertReview/html/saveExpertReviewCost",
                    data: JSON.stringify(vm.businessFlag.expertReviews)
                }


                var httpSuccess = function success(response) {
                    vm.isCommit = false;
                    common.alert({
                        vm: vm,
                        msg: "操作成功",
                        closeDialog: true,
                        fn: function () {
                            //重新初始化评审费发放数据
                            if (callBack != undefined && typeof callBack == "function") {
                                callBack();
                            }
                        }
                    });
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }// E_savePayment

        // S_countNum
        function countNum(reviewCost) {
            reviewCost = reviewCost == undefined ? 0 : reviewCost;
            // console.log('评审费：'+reviewCost);
            //var XSum = vm.expertReview.reviewCost;
            var reviewTaxes = 0;
            if (reviewCost > 800 && reviewCost <= 4000) {
                reviewTaxes = (reviewCost - 800) * 0.2;
            } else if (reviewCost > 4000 && reviewCost <= 20000) {
                reviewTaxes = reviewCost * (1 - 0.2) * 0.2
            } else if (reviewCost > 20000 && reviewCost <= 50000) {
                reviewTaxes = reviewCost * (1 - 0.2) * 0.3 - 2000;
            } else if (reviewCost > 50000) {
                //待确认
            }
            return reviewTaxes;
        }// E_countNum

        // S_countTaxes
        function countTaxes(vm, expertReview) {
            if (expertReview.expertSelectedDtoList == undefined || expertReview.expertSelectedDtoList.length == 0) {
                return;
            }

            var expertSelectedDtoList = expertReview.expertSelectedDtoList;
            var len = expertReview.expertSelectedDtoList.length, ids = '', month;
            expertReview.expertSelectedDtoList.forEach(function (v, i) {
                ids += "'" + v.id + "'";
                if (i != (len - 1)) {
                    ids += ",";
                }
            });
            var payDate = expertReview.payDate

            month = payDate.substring(0, payDate.lastIndexOf('-'));
            var url = rootPath + "/expertReview/html/getExpertReviewCost?expertIds={0}&month={1}";

            //取得该评审方案评审专家在这个月的所有评审费用
            var httpOptions = {
                method: 'get',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                url: common.format(url, ids, month)
            }

            var httpSuccess = function success(response) {
                var allExpertCost = response.data;
                expertReview.reviewCost = 0;
                expertReview.reviewTaxes = 0;
                expertReview.totalCost = 0;

                expertSelectedDtoList.forEach(function (v, i) {

                    var expertDto = v.expertDto;
                    var expertId = v.EXPERTID;
                    var expertSelectedId = v.id;
                    var totalCost = 0;
                    //console.log("计算专家:"+expertDto.name);
                    if (allExpertCost != undefined && allExpertCost.length > 0) {

                        //累加专家改月的评审费用
                        allExpertCost.forEach(function (v, i) {
                            if (v.EXPERTID == expertId && v.ESID != expertSelectedId) {
                                v.REVIEWCOST = v.REVIEWCOST == undefined ? 0 : v.REVIEWCOST;
                                v.REVIEWCOST = parseFloat(v.REVIEWCOST);
                                totalCost = parseFloat(totalCost) + v.REVIEWCOST;
                            }
                        });
                    }

                    //console.log("专家当月累加:" + totalCost);

                    //计算评审费用
                    v.reviewCost = v.reviewCost == undefined ? 0 : v.reviewCost;
                    var reviewTaxesTotal = totalCost + parseFloat(v.reviewCost);
                    //console.log("专家当月累加加上本次:" + reviewTaxesTotal);


                    v.reviewTaxes = countNum(reviewTaxesTotal).toFixed(2);
                    v.totalCost = (parseFloat(v.reviewCost) + parseFloat(v.reviewTaxes)).toFixed(2);
                    expertReview.reviewCost = (parseFloat(expertReview.reviewCost) + parseFloat(v.reviewCost)).toFixed(2);
                    expertReview.reviewTaxes = (parseFloat(expertReview.reviewTaxes) + parseFloat(v.reviewTaxes)).toFixed(2);
                    expertReview.totalCost = (parseFloat(expertReview.reviewCost) + parseFloat(expertReview.reviewTaxes)).toFixed(2);
                });

                //console.log(expertReview);

            }


            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });


        }// E_countTaxes

        // S_validateNum
        function validateNum(vm, expertReviews) {
            var isVilad = true;
            //计算每个评审的评审费是否正确
            if (expertReviews != undefined && expertReviews.length > 0) {
                expertReviews.forEach(function (v, i) {
                    if (v.payDate == undefined) {
                        v.errorMsg = "请选择发放日期";
                        isVilad = false;
                        return;
                    }
                    v.errorMsg = "";
                    //总评审费
                    var totalReviewCost = v.reviewCost == undefined ? 0 : v.reviewCost;
                    //总税额
                    var totalReviwTaxes = v.reviewTaxes == undefined ? 0 : v.reviewTaxes;
                    //总合计
                    var totalCost = v.totalCost == undefined ? 0 : v.totalCost;

                    //计算每个专家
                    if (v.expertSelectedDtoList != undefined && v.expertSelectedDtoList.length > 0) {

                        var tempTotalReviewCost = 0;
                        var tempTotalReviwTaxes = 0;
                        var tempTotalCost = 0;

                        v.expertSelectedDtoList.forEach(function (expertSelected, i) {
                            //评审费用
                            var reviewCost = expertSelected.reviewCost == undefined ? 0 : expertSelected.reviewCost;
                            //税额
                            var reviewTaxes = expertSelected.reviewTaxes == undefined ? 0 : expertSelected.reviewTaxes;
                            //合计
                            var totalCost = expertSelected.totalCost == undefined ? 0 : expertSelected.totalCost;
                            var tempCost = parseFloat(reviewCost) + parseFloat(reviewTaxes);
                            if (tempCost.toFixed(2) != parseFloat(totalCost).toFixed(2)) {
                                isVilad = false;
                                return;
                            }
                            tempTotalReviewCost = parseFloat(tempTotalReviewCost) + parseFloat(reviewCost);
                            tempTotalReviwTaxes = parseFloat(tempTotalReviwTaxes) + parseFloat(reviewTaxes);
                            tempTotalCost = parseFloat(tempTotalCost) + parseFloat(totalCost);
                        });

                        if (parseFloat(tempTotalReviewCost).toFixed(2) != parseFloat(totalReviewCost).toFixed(2)) {
                            isVilad = false;
                            return;
                        }
                        if (parseFloat(tempTotalReviwTaxes).toFixed(2) != parseFloat(totalReviwTaxes).toFixed(2)) {
                            isVilad = false;
                            return;
                        }
                        if (parseFloat(tempTotalCost).toFixed(2) != parseFloat(totalCost).toFixed(2)) {
                            isVilad = false;
                            return;
                        }
                    }
                });
            }

            return isVilad;
        }// E_validateNum

        // S_gotopayment
        function gotopayment(vm) {
            vm.showExpertpayment = true;
            var WorkeWindow = $("#payment");
            WorkeWindow.kendoWindow({
                width: "1000px",
                height: "630px",
                title: "编辑-专家费用",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();

        }// E_gotopayment

    }
})();

(function () {
    'use strict';

    /**
     * 流程通用基础类
     */
    var signNode = {
        SIGN_ZR: "SIGN_ZR",                //填报
        SIGN_QS: "SIGN_QS",                //签收
        SIGN_ZHB: "SIGN_ZHB",              //综合部审批
        SIGN_FGLD_FB: "SIGN_FGLD_FB",      //分管副主任审批
        SIGN_BMFB1: "SIGN_BMFB1",          //部门分办1
        SIGN_BMFB2: "SIGN_BMFB2",          //部门分办2
        SIGN_BMFB3: "SIGN_BMFB3",          //部门分办3
        SIGN_BMFB4: "SIGN_BMFB4",          //部门分办4
        SIGN_XMFZR1: "SIGN_XMFZR1",        //项目负责人办理1
        SIGN_XMFZR2: "SIGN_XMFZR2",        //项目负责人办理2
        SIGN_XMFZR3: "SIGN_XMFZR3",        //项目负责人办理3
        SIGN_XMFZR4: "SIGN_XMFZR4",        //项目负责人办理4
        SIGN_BMLD_SPW1: "SIGN_BMLD_SPW1",  //部长审批1
        SIGN_BMLD_SPW2: "SIGN_BMLD_SPW2",  //部长审批2
        SIGN_BMLD_SPW3: "SIGN_BMLD_SPW3",  //部长审批3
        SIGN_BMLD_SPW4: "SIGN_BMLD_SPW4",  //部长审批4
        SIGN_FGLD_SPW1: "SIGN_FGLD_SPW1",  //分管副主任审批1
        SIGN_FGLD_SPW2: "SIGN_FGLD_SPW2",  //分管副主任审批2
        SIGN_FGLD_SPW3: "SIGN_FGLD_SPW3",  //分管副主任审批3
        SIGN_FGLD_SPW4: "SIGN_FGLD_SPW4",  //分管副主任审批4
        SIGN_FW: "SIGN_FW",                //发文申请
        SIGN_QRFW: "SIGN_QRFW",            //项目负责人确认发文
        SIGN_BMLD_QRFW: "SIGN_BMLD_QRFW",  //部长审批发文
        SIGN_FGLD_QRFW: "SIGN_FGLD_QRFW",  //分管领导审批发文
        SIGN_ZR_QRFW: "SIGN_ZR_QRFW", //主任审批发文
        SIGN_FWBH: "SIGN_FWBH",            //生成发文编号
        SIGN_CWBL: "SIGN_CWBL",            //财务办理
        SIGN_GD: "SIGN_GD",                //归档
        SIGN_DSFZR_QRGD: "SIGN_DSFZR_QRGD",//第二负责人确认
        SIGN_QRGD: "SIGN_QRGD"              //最终归档
    };

    var service = {
        getSignFlowNode: getSignFlowNode,               //项目签收流程环节
    };
    window.flowcommon = service;

    function getSignFlowNode() {
        return signNode;
    }

})();

(function () {
    'use strict';

    angular
        .module('app')
        .controller('homeCtrl', home);

    home.$inject = ['$location','homeSvc']; 

    function home($location, homeSvc) {
        /* jshint validthis:true */
    	var vm = this;
        vm.title = '';
        

        vm.changePwd = function () {        	
        	 homeSvc.changePwd(vm);
          
        }
       
        activate();
        function activate() {
        }
    }
})();

(function() {
	'use strict';

	angular.module('app').factory('homeSvc', home);

	home.$inject = [ '$http' ];

	function home($http) {
		var url_account_password = "/account/password";
		
		var service = {			
			changePwd : changePwd
		};

		return service;

		// begin#updatehome
		function changePwd(vm) {
			common.initJqValidation();
			var isValid = $('form').valid();
			if (isValid) {
				vm.isSubmit = true;
				

				var httpOptions = {
					method : 'put',
					url : url_account_password,
					data : vm.model.password
				}

				var httpSuccess = function success(response) {

					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {

							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									vm.isSubmit = false;
									$('.alertDialog').modal('hide');
								}
							})
						}

					})
				}

				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});

			} else {
				// common.alert({
				// vm:vm,
				// msg:"您填写的信息不正确,请核对后提交!"
				// })
			}

		}

	}
})();
(function () {
    'use strict';

    angular
        .module('app')
        .controller('logCtrl', log);

    log.$inject = ['$location','logSvc']; 

    function log($location, logSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '日志列表';
        

       
        activate();
        function activate() {
            logSvc.grid(vm);
        }
    }
})();

(function() {
	'use strict';

	angular.module('app').factory('logSvc', log);

	log.$inject = [ '$http','$compile' ];	
	function log($http,$compile) {	
		var url_log = rootPath +"/log";
		var url_back = rootPath +'#/log';
			
		var service = {
			grid : grid			
		};		
		return service;	
		
		function grid(vm) {

			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_log+"/fingByOData"),
				schema : common.kendoGridConfig().schema({
					id : "id",
					fields : {
						createdDate : {
							type : "date"
						}
					}
				}),
				serverPaging : true,
				serverSorting : true,
				serverFiltering : true,			
				pageSize: 10,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});

			// End:dataSource

			// Begin:column
			var columns = [
					  {
						field : "id",
						title : "ID",
						width : 80,						
						filterable : false
					},{
						field : "level",
						title : "级别",
						width : 100,
						filterable : true
					} ,{
						field : "message",
						title : "日志内容",
						filterable : false
					},{
						field : "userId",
						title : "操作者",
						width : 150,
						filterable : true
					}, {
						field : "createdDate",
						title : "操作时间",
						width : 180,
						filterable : false,
						format : "{0:yyyy/MM/dd HH:mm:ss}"

					}

			];
			// End:column
		
			vm.gridOptions={
					dataSource : common.gridDataSource(dataSource),
					filterable : common.kendoGridConfig().filterable,
					pageable : common.kendoGridConfig().pageable,
					noRecords:common.kendoGridConfig().noRecordMessage,
					columns : columns,
					resizable: true
				};
			
		}// end fun grid

		
		
		

	}
	
	
	
})();
(function () {
    'use strict';

    angular.module('app').controller('meetingCtrl', meeting);

    meeting.$inject = ['$location','meetingSvc']; 

    function meeting($location, meetingSvc) {
        /* jshint validthis:true */
    	var vm = this;
        vm.title = '会议室列表';
        vm.model={};
        vm.del = function (id) {
             common.confirm({
            	 vm:vm,
            	 title:"",
            	 msg:"确认删除数据吗？",
            	 fn:function () {
                  	$('.confirmDialog').modal('hide');             	
                    meetingSvc.deleteMeeting(vm,id);
                 }
             })
        }
        vm.used=function(id){
        	vm.model.id=id;
        	vm.model.mrStatus="1";//显示停用标志
        	meetingSvc.roomUseState(vm);
        	//vm.isUse=false;
        }

        //停用会议室
        vm.stoped=function(id){
            common.confirm({
                vm:vm,
                title:"",
                msg:"确定停用会议室么？",
                fn:function () {
                    $('.confirmDialog').modal('hide');
                    vm.model.id=id;
                    vm.model.mrStatus="2";//显示启用标志
                    meetingSvc.roomUseState(vm);
                }
            })
        }

        vm.dels = function () {     
        	var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
            	common.alert({
                	vm:vm,
                	msg:'请选择数据'
                	
                });
            } else {
            	var ids=[];
                for (var i = 0; i < selectIds.length; i++) {
                	ids.push(selectIds[i].value);
				}  
                var idStr=ids.join(',');
                vm.del(idStr);
            }   
       }
        //会议室查询
        vm.queryMeeting = function(){
        	meetingSvc.queryMeeting(vm);
        }

        activate();
        function activate() {
            meetingSvc.grid(vm);
        }
    }
})();

(function () {
    'use strict';

    angular
        .module('app')
        .controller('meetingEditCtrl', meeting);

    meeting.$inject = ['$location','meetingSvc','$state']; 

    function meeting($location, meetingSvc,$state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加会议室';
        vm.isuserExist=false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新会议室';
        }
        
        vm.create = function () {
        	//alert("sss");
        	meetingSvc.createMeeting(vm);
        };
        vm.update = function () {
        	meetingSvc.updateMeeting(vm);
        };      

        activate();
        function activate() {
        	if (vm.isUpdate) {
        		meetingSvc.getMeetingById(vm);
            } else {
            	
            }
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').factory('meetingSvc', meeting);

    meeting.$inject = ['$http'];

    function meeting($http) {
        var url_meeting = rootPath + "/meeting";
        var url_back = '#/meeting';
        var service = {
            grid: grid,
            getMeetingById: getMeetingById,
            createMeeting: createMeeting,
            deleteMeeting: deleteMeeting,
            updateMeeting: updateMeeting,
            queryMeeting: queryMeeting,		//会议室查询
            roomUseState: roomUseState
        };

        return service;

        //会议室查询
        function queryMeeting(vm) {
            vm.gridOptions.dataSource.read();
        }

        // begin#updateUser
        function updateMeeting(vm) {

            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id
                var httpOptions = {
                    method: 'put',
                    url: url_meeting,
                    data: vm.model
                }

                var httpSuccess = function success(response) {

                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                }
                            })
                        }

                    })
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            } else {
                // common.alert({
                // vm:vm,
                // msg:"您填写的信息不正确,请核对后提交!"
                // })
            }

        }

        // begin#deleteUser
        function deleteMeeting(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_meeting,
                data: id

            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.isSubmit = false;
                        vm.gridOptions.dataSource.read();
                    }

                });

            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        function roomUseState(vm) {
            var httpOptions = {
                method: 'put',
                url: url_meeting + "/roomUseState",
                data: vm.model
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.gridOptions.dataSource.read();
                    }

                });

            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#createUser
        function createMeeting(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                var httpOptions = {
                    method: 'post',
                    url: url_meeting,
                    data: vm.model
                }

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                    location.href = url_back;
                                }
                            })
                        }
                    });

                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess,
                    onError: function (response) {
                        vm.iscommit = false;
                    }
                });

            }
        }

        // begin#getUserById
        function getMeetingById(vm) {
            var httpOptions = {
                method: 'get',
                url: url_meeting + "/html/findByIdMeeting",
                params: {id: vm.id}
            }
            var httpSuccess = function success(response) {
                vm.model = response.data;
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#grid
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_meeting + "/fingByOData", $("#meetingFrom")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            // End:dataSourc

            //S_序号
            var dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            }
            //S_序号

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />", item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "rowNumber",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                }
                ,
                {
                    field: "num",
                    title: "编号",
                    width: 100,
                    filterable: false
                },
                {
                    field: "mrName",
                    title: "会议室名称",
                    width: 150,
                    filterable: false
                },
                {
                    field: "mrType",
                    title: "类型",
                    width: 100,
                    filterable: false
                },
                {
                    field: "addr",
                    title: "会议室地点",
                    width: 200,
                    filterable: false
                },
                {
                    field: "capacity",
                    title: "容量",
                    width: 80,
                    filterable: false
                },
                {
                    field: "userName",
                    title: "负责人",
                    width: 70,
                    filterable: false
                },
                {
                    field: "userPhone",
                    title: "负责人电话",
                    width: 120,
                    filterable: false
                },
                {
                    field : "",
                    title : "状态",
                    width : 50,
                    template: function (item) {
                       if(item.mrStatus == "2"){
                            return "停用";
                       }else{
                           return "正常";
                       }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 100,
                    template: function (item) {
                        var isUse = false;
                        if (item.mrStatus == "2") {
                            isUse = true;//会议室可用
                        } else {
                            isUse = false;//会议室不可用
                        }
                        //return common.format($('#columnBtns').html(),"vm.stoped('" + item.id + "')",isUse,"vm.used('" + item.id + "')",isUse, item.id,"vm.del('" + item.id + "')");
                        return common.format($('#columnBtns').html(), "vm.stoped('" + item.id + "')", isUse, "vm.used('" + item.id + "')", isUse, item.id);
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound: dataBound,
                resizable: true
            };
        }// end fun grid


    }
})();
(function () {
    'use strict';

    angular.module('app').controller('officeUserCtrl', officeUser);

    officeUser.$inject = ['$location', 'officeUserSvc'];

    function officeUser($location, officeUserSvc) {
        var vm = this;
        vm.title = '处室人员管理';

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    officeUserSvc.deleteOfficeUser(vm, id);
                }
            });
        }
        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: '请选择数据'
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.del(idStr);
            }
        };
        
        vm.queryOffice = function(){
        	officeUserSvc.queryOffice(vm);
        }
        activate();
        function activate() {
            officeUserSvc.grid(vm);
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('officeUserEditCtrl', officeUser);

    officeUser.$inject = ['$location', 'officeUserSvc', '$state'];

    function officeUser($location, officeUserSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加人员';
        vm.isuserExist = false;
        vm.officeID = $state.params.officeID;
        if (vm.officeID) {
            vm.isUpdate = true;
            vm.title = '更新人员';
        }

        vm.create = function () {
            officeUserSvc.createOfficeUser(vm);
        };
        vm.update = function () {
            officeUserSvc.updateOfficeUser(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                officeUserSvc.getOfficeUserById(vm);
            }
            officeUserSvc.getDepts(vm);//获取所有办事处
           
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').factory('officeUserSvc', officeUser);

    officeUser.$inject = ['$http'];

    function officeUser($http) {
        var url_officeUser = rootPath + "/officeUser", url_back = '#/officeUserList';
        var service = {
            grid: grid,
            getOfficeUserById: getOfficeUserById,
            createOfficeUser: createOfficeUser,
            deleteOfficeUser: deleteOfficeUser,
            updateOfficeUser: updateOfficeUser,
            getDepts:getDepts,						//获取所有办事处
            queryOffice:queryOffice,
        };

        return service;
        
        function queryOffice(vm){
        	vm.gridOptions.dataSource.read();	
        }
        //start 获取所有办事处
        function getDepts(vm){
        	var httpOptions = {
                    method: 'get',
                    url: common.format(url_officeUser + "/getDepts")
                }
                var httpSuccess = function success(response) {
                    vm.depts = {};
                    vm.depts = response.data;
                    
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
        	
        }
        //end 获取所有办事处
        
        // begin#updateOfficeUser
        function updateOfficeUser(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id
                var httpOptions = {
                    method: 'put',
                    url: url_officeUser,
                    data: vm.model
                }

                var httpSuccess = function success(response) {

                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {

                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                }
                            })
                        }

                    })
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            } else {
                // common.alert({
                // vm:vm,
                // msg:"您填写的信息不正确,请核对后提交!"
                // })
            }

        }

        // begin#deleteOfficeUser
        function deleteOfficeUser(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_officeUser,
                data: id
            };

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                    	common.alert({
                            vm: vm,
                            msg: "操作成功",
                            closeDialog :true,
                            fn: function () {
                            	vm.isSubmit = false;
                                vm.gridOptions.dataSource.read();
                            }
                        })
                    }
                });
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#createOfficeUser
        function createOfficeUser(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;

                var httpOptions = {
                    method: 'post',
                    url: url_officeUser,
                    data: vm.model
                };

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                closeDialog :true,
                                fn: function () {
                                    vm.isSubmit = false;
                                    location.href = url_back;
                                }
                            });
                        }
                    });
                };

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            }
        }

        // begin#getOfficeUserById
        function getOfficeUserById(vm) {
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/officeUser/html/findById",
                params:{officeID:vm.officeID}
            };
            var httpSuccess = function success(response) {
            	if(response.data.dept){
					//vm.depts = {}
					//vm.depts = response.data.depts;
					//console.log(vm.depts);
					
				}
                vm.model = response.data;
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });                       
        }

        // begin#grid
        function grid(vm) {

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_officeUser+"/fingByOData",$("#formOffice")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            // End:dataSource
            
          //S_序号
            var  dataBound=function () {  
                var rows = this.items();  
                var page = this.pager.page() - 1;  
                var pagesize = this.pager.pageSize();  
                $(rows).each(function () {  
                    var index = $(this).index() + 1 + page * pagesize;  
                    var rowLabel = $(this).find(".row-number");  
                    $(rowLabel).html(index);  
                });  
            } 
            //S_序号

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.officeID)
                    },
                    filterable: false,
                    width: 20,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {  
 				    field: "rowNumber",  
 				    title: "序号",  
 				    width: 30,
 				    filterable : false,
 				    template: "<span class='row-number'></span>"  
 				 }
 				,
                {
                    field: "officeUserName",
                    title: "办事处联系人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "deptName",
                    title: "所在办事处",
                    width: 100,
                    filterable: false
                },
               
                {
                    field: "officePhone",
                    title: "电话",
                    width: 100,
                    filterable: false
                },
                {
                    field: "officeEmail",
                    title: "邮件",
                    width: 100,
                    filterable: false
                },
                {
                    field: "officeDesc",
                    title: "描述",
                    width: 100,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.del('" + item.officeID + "')", item.officeID);
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound:dataBound,
                resizable: true
            };

        }// end fun grid

    }
})();
(function () {
    'use strict';

    angular.module('app').controller('quartzCtrl', quartz);

    quartz.$inject = ['$location', 'quartzSvc'];

    function quartz($location, quartzSvc) {
        var vm = this;
        vm.title = '定时器配置';

        activate();
        function activate() {
            quartzSvc.grid(vm);
        }

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    quartzSvc.deleteQuartz(vm, id);
                }
            });
        }
        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: '请选择数据'
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.del(idStr);
            }
        };

        //新增定时器
        vm.addQuartz = function () {
        	vm.quartz={};
            $("#quartz_edit_div").kendoWindow({
                width : "600px",
                height : "400px",
                title : "定时器编辑",
                visible : false,
                modal : true,
                closable : true,
                actions : [ "Pin", "Minimize", "Maximize", "Close" ]
            }).data("kendoWindow").center().open();
        }

          //修改定时器
        vm.edit = function (id) {
        	vm.id=id;
            $("#quartz_edit_div").kendoWindow({
                width : "600px",
                height : "400px",
                title : "定时器修改",
                visible : false,
                modal : true,
                closable : true,
                actions : [ "Pin", "Minimize", "Maximize", "Close" ]
            }).data("kendoWindow").center().open();
            quartzSvc.getQuartzById(vm);
            
        }
        
        
        //关闭弹窗
        vm.colseQuartz = function(){
            window.parent.$("#quartz_edit_div").data("kendoWindow").close();
        }

        //保存定时器
        vm.saveQuartz = function(){
            quartzSvc.saveQuartz(vm);
        }
        
        vm.execute=function (id){
        	quartzSvc.quartzExecute(vm,id);
        }
        
        vm.stop=function (id){
        	quartzSvc.quartzStop(vm,id);
        }

    }
})();

(function () {
    'use strict';

    angular.module('app').controller('quartzEditCtrl', quartz);

    quartz.$inject = ['$location', 'quartzSvc', '$state'];

    function quartz($location, quartzSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加定时器配置';
        vm.isuserExist = false;
        vm.id = $state.params.id;

        activate();
        function activate() {
        }

        vm.create = function () {
            quartzSvc.createQuartz(vm);
        };
        vm.update = function () {
            quartzSvc.updateQuartz(vm);
        };
        
            

    }
})();

(function () {
    'use strict';

    angular.module('app').factory('quartzSvc', quartz);

    quartz.$inject = ['$http'];

    function quartz($http) {
        var url_quartz = rootPath + "/quartz", url_back = '#/quartz';
        var service = {
            grid: grid,
            getQuartzById: getQuartzById,
            saveQuartz: saveQuartz,
            deleteQuartz: deleteQuartz,
            updateQuartz: updateQuartz,
            quartzExecute : quartzExecute,	//执行定时器
            quartzStop : quartzStop	//停止执行定时器
        };

        return service;
        
        //begin quartzExecute
        function quartzExecute(vm,id){
        	var httpOptions={
        		method: "put",
        		url:url_quartz+"/quartzExecute",
        		params:{quartzId : id}
        		
        	}
        	   var httpSuccess = function success(response) {
        	   	var msg="操作成功";
        	   	var str=response.data;
        	   	if("defeated"==str.substring(1,response.data.length-1)){
                    msg="该定时器未存在！"
        	   	}
        	   	common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {

                            common.alert({
                                vm: vm,
                                msg: msg,
                                fn: function () {
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                    vm.gridOptions.dataSource.read();
                                }
                            })
                        }

                    })
        	   	
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

        }//end quartzExecute
        
        //begin quartzStop
        function quartzStop(vm,id){
        	var httpOptions={
        		method: "put",
        		url:url_quartz+"/quartzStop",
        		params:{quartzId : id}
        		
        	}
        	   var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {

                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                    vm.gridOptions.dataSource.read();
                                }
                            })
                        }

                    })
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
        }//end quartzStop

        // begin#updateQuartz
        function updateQuartz(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.quartz.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_quartz+"/updateQuartz",
                    data: vm.quartz
                }

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {

                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                    window.parent.$("#quartz_edit_div").data("kendoWindow").close();
                                    vm.gridOptions.dataSource.read();
                                }
                            })
                        }

                    })
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            } else {
                // common.alert({
                // vm:vm,
                // msg:"您填写的信息不正确,请核对后提交!"
                // })
            }

        }

        // begin#deleteQuartz
        function deleteQuartz(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_quartz,
                data: id
            };

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: "操作成功",
                            closeDialog: true,
                            fn: function () {
                                vm.isSubmit = false;
                                vm.gridOptions.dataSource.read();
                            }
                        })
                    }
                });
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#createQuartz
        function saveQuartz(vm) {
            common.initJqValidation($("#quartz_form"));
            var isValid = $("#quartz_form").valid();
            if (isValid) {
                vm.isSubmit = true;
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/quartz",
                    data: vm.quartz
                };

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                closeDialog: true,
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                    window.parent.$("#quartz_edit_div").data("kendoWindow").close();
                                    vm.gridOptions.dataSource.read();
                                }
                            });
                        }
                    });
                };
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            }
        }

        // begin#getQuartzById
        function getQuartzById(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/quartz/html/findById",
                params: {id: vm.id}
            };
            var httpSuccess = function success(response) {
                vm.quartz = response.data;
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#grid
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/quartz/findByOData"),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            // End:dataSource

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "quartzName",
                    title: "定时器名称",
                    width: 100,
                    filterable: true
                },
                {
                    field: "className",
                    title: "类名",
                    width: 100,
                    filterable: true
                },
                {
                    field: "cronExpression",
                    title: "表达式",
                    width: 100,
                    filterable: true
                },
                {
                    field: "",
                    title: "执行状态",
                    width: 80,
                    filterable: true,
                    template :function (item){
                    	if(item.curState){
                    		if(item.curState=="9"){
                    			return "在执行";
                    		}
                    		if(item.curState=="0"){
                    			return "未执行";
                    		}
                    	}else{
                    		return "";
                    	}
                    }
                },
                {
                    field: "",
                    title: "是否可用",
                    width: 80,
                    filterable: true,
                    template :function (item){
                    	if(item.isEnable){
                    		if(item.isEnable=="0"){
	                    		return "停用";
                    		}
                    		if(item.isEnable=="9"){
                    			return "在用";
                    		}
                    	}else {
                    		return"";
                    	}
                    }
                },
                {
                    field: "descInfo",
                    title: "描述",
                    width: 180,
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.del('" + item.id + "')",  "vm.edit('" + item.id + "')",item.isEnable,"vm.execute('"+item.id+"')",item.curState,"vm.stop('"+item.id+"')");
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true
            };
        }// end fun grid

    }
})();
(function () {
    'use strict';

    angular
        .module('app')
        .controller('roleCtrl', role);

    role.$inject = ['$location','roleSvc']; 

    function role($location, roleSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '角色列表';
        

        vm.del = function (id) {        	
        	 
             common.confirm({
            	 vm:vm,
            	 title:"",
            	 msg:"确认删除数据吗？",
            	 fn:function () {
                  	$('.confirmDialog').modal('hide');             	
                    roleSvc.deleteRole(vm,id);
                 }
             })
        }
        vm.dels = function () {     
        	var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
            	common.alert({
                	vm:vm,
                	msg:'请选择数据'
                	
                });
            } else {
            	var ids=[];
                for (var i = 0; i < selectIds.length; i++) {
                	ids.push(selectIds[i].value);
				}  
                var idStr=ids.join(',');
                vm.del(idStr);
            }   
       }
        activate();
        function activate() {
            roleSvc.grid(vm);
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('roleEditCtrl', role);

    role.$inject = ['$location','roleSvc','$state']; 

    function role($location, roleSvc,$state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加角色';
        vm.isRoleExist=false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新角色';
        }
        
        vm.create = function () {
        	roleSvc.createRole(vm);
        };
        vm.update = function () {
        	roleSvc.updateRole(vm);
        };
        vm.checkRole = function () {
        	roleSvc.checkRole(vm);
        };

        activate();
        function activate() {
        	if (vm.isUpdate) {
        		roleSvc.getRoleById(vm);
            } else {
            	roleSvc.initZtreeClient(vm);
            }
        }
    }
})();

(function() {
	'use strict';

	angular.module('app').factory('roleSvc', role);

	role.$inject = [ '$http','$compile' ];	
	function role($http,$compile) {	
		var url_role =rootPath + "/role";
		var url_back = '#/role';
		var url_resource=rootPath +"/sys/resource"
			
		var service = {
			grid : grid,
			createRole : createRole,
			checkRole : checkRole,
			getRoleById : getRoleById,
			updateRole:updateRole,
			deleteRole:deleteRole,
			initZtreeClient:initZtreeClient
		};		
		return service;	
		
		// begin common fun
		function getZtreeChecked() {
            var treeObj = $.fn.zTree.getZTreeObj("zTree");
            var nodes = treeObj.getCheckedNodes(true);
            return nodes;
        }
		
		function updateZtree(vm) {
            var treeObj = $.fn.zTree.getZTreeObj("zTree");
            var checkedNodes = $linq(vm.model.resources).select(function (x) { return x.path; }).toArray();
            var allNodes = treeObj.getNodesByParam("level", 1, null);

            var nodes = $linq(allNodes).where(function (x) { return $linq(checkedNodes).contains(x.path); }).toArray();
            
            for (var i = 0, l = nodes.length; i < l; i++) {
                treeObj.checkNode(nodes[i], true, true);
            }
        }
		// end common fun
		
		function grid(vm) {

			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_role+"/fingByOData"),
				schema : common.kendoGridConfig().schema({
					id : "id",
					fields : {
						createdDate : {
							type : "date"
						}
					}
				}),
				serverPaging : true,
				serverSorting : true,
				serverFiltering : true,			
				pageSize: 10,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});

			// End:dataSource
			
			 //S_序号
            var  dataBound=function () {  
                var rows = this.items();  
                var page = this.pager.page() - 1;  
                var pagesize = this.pager.pageSize();  
                $(rows).each(function () {  
                    var index = $(this).index() + 1 + page * pagesize;  
                    var rowLabel = $(this).find(".row-number");  
                    $(rowLabel).html(index);  
                });  
            } 
            //S_序号

			// Begin:column
			var columns = [
					{
						template : function(item) {
							return kendo
									.format(
											"<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
											item.id)
						},
						filterable : false,
						width : 40,
						title : "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
						
					}, 
					 {  
					    field: "rowNumber",  
					    title: "序号",  
					    width: 70,
					    filterable : false,
					    template: "<span class='row-number'></span>"  
					 }
					,
					{
						field : "roleName",
						title : "角色名称",
						width : 200,						
						filterable : false
					}, {
						field : "remark",
						title : "描述",
						filterable : false
					}, {
						field : "createdDate",
						title : "创建时间",
						width : 180,
						filterable : false,
						format : "{0:yyyy-MM-dd HH:mm:ss}"

					},  {
						field : "",
						title : "操作",
						width : 180,
						template:function(item){							
							return common.format($('#columnBtns').html(),"vm.del('"+item.id+"')",item.id);
						}
					}
			];
			// End:column
		
			vm.gridOptions={
					dataSource : common.gridDataSource(dataSource),
					filterable : common.kendoGridConfig().filterable,
					pageable : common.kendoGridConfig().pageable,
					noRecords:common.kendoGridConfig().noRecordMessage,
					columns : columns,
					dataBound:dataBound,
					resizable: true
				};
			
		}// end fun grid

		function createRole(vm) {
			common.initJqValidation();
			var isValid = $('form').valid();
			if (isValid && vm.isRoleExist == false) {
				vm.isSubmit = true;
				
				// zTree
				var nodes = getZtreeChecked();
               var nodes_role = $linq(nodes).where(function (x) { return x.isParent == false; }).select(function (x) { return { id: x.id, name: x.name,path:x.path,method:x.method }; }).toArray();
               vm.model.resources = nodes_role;   
	               
				var httpOptions = {
					method : 'post',
					url : url_role,
					data : vm.model
				}

				var httpSuccess = function success(response) {				
					
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function() {
							common.alert({
								vm:vm,
								msg:"操作成功",
								fn:function() {
									vm.isSubmit = false;
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
									location.href = url_back;
								}
							})
						}
						
					});

				}

				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess
				});

			} else {				
//				common.alert({
//					vm:vm,
//					msg:"您填写的信息不正确,请核对后提交!"
//				})
			}
		}// end fun createRole

		function checkRole(vm) {

		}// end fun checkRole

		//begin getRoleById
		function getRoleById(vm) {
			var httpOptions = {
				method : 'post',
				url : rootPath + "/role/findById",
				params:{
					roleId:vm.id
				}
			}
			var httpSuccess = function success(response) {
				vm.model = response.data;
				if (vm.isUpdate) {
					initZtreeClient(vm);
				}
			}
			
			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}//end  getRoleById
		
		function updateRole(vm){
			common.initJqValidation();			
			var isValid = $('form').valid();
			if (isValid && vm.isRoleExist == false) {
				vm.isSubmit = true;
				vm.model.id=vm.id;// id
				//console.log(vm.model);
				//return ;
				// zTree
				var nodes = getZtreeChecked();
               var nodes_role = $linq(nodes).where(function (x) { return x.isParent == false; }).select(function (x) { return { id: x.id, name: x.name,path:x.path,method:x.method }; }).toArray();
               vm.model.resources = nodes_role; 
               vm.model.createdDate = "2017-04-07 12:00:00";
				var httpOptions = {
					method : 'put',
					url : url_role,
					data : vm.model
				}

				var httpSuccess = function success(response) {
					
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function() {
							
							common.alert({
								vm:vm,
								msg:"操作成功",
								fn:function() {
									vm.isSubmit = false;
									$('.alertDialog').modal('hide');							
								}
							})
						}
						
					})
				}

				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess
				});

			} else {
//				common.alert({
//				vm:vm,
//				msg:"您填写的信息不正确,请核对后提交!"
//			})
			}
		}// end fun updateRole
		
		function deleteRole(vm,id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url:url_role,
                data:id
                
            }
            var httpSuccess = function success(response) {
                
                common.requestSuccess({
					vm:vm,
					response:response,
					fn:function () {
	                    vm.isSubmit = false;
	                    vm.gridOptions.dataSource.read();
	                }
					
				});

            }
            common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
        }// end fun deleteRole
		
		function initZtreeClient(vm){
			var httpOptions = {
	                method: 'get',
	                url: url_resource
	            }
	            var httpSuccess = function success(response) {
	              
	                
	                common.requestSuccess({
						vm:vm,
						response:response,
						fn:function () {
		                    var zTreeObj;
		                    var setting = {
		                        check: {
		                            chkboxType: { "Y": "ps", "N": "ps" },
		                            enable: true
		                        }
		                    };
		                    var zNodes = response.data;
		                    
		                    zTreeObj = $.fn.zTree.init($("#zTree"), setting, zNodes);
		                    if (vm.isUpdate) {
		                         updateZtree(vm);

		                    }
		                }
						
					});
	                

	            }
			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}// end fun initZtreeClient
		
		

	}
	
	
	
})();
(function () {
    'use strict';

    angular
        .module('app')
        .controller('roomCountCtrl', roomCount);

    roomCount.$inject = ['$location','roomCountSvc']; 

    function roomCount($location, roomCountSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '预定会议统计列表';
        
        vm.queryRoomCount = function(){
        	roomCountSvc.queryRoomCount(vm);
        }
        vm.del = function (id) {        	
             common.confirm({
            	 vm:vm,
            	 title:"",
            	 msg:"确认删除数据吗？",
            	 fn:function () {
                  	$('.confirmDialog').modal('hide');             	
                    roomCountSvc.deleteroomCount(vm,id);
                 }
             })
        }
        vm.ResetRoomCount=function(){
        	roomCountSvc.cleanValue();
        }
        vm.dels = function () {     
        	var selectIds = common.getKendoCheckId('.grid');
        	//alert(selectIds.length);
            if (selectIds.length == 0) {
            	common.alert({
                	vm:vm,
                	msg:'请选择数据'
                	
                });
            } else {
            	var ids=[];
                for (var i = 0; i < selectIds.length; i++) {
                	ids.push(selectIds[i].value);
				}  
                var idStr=ids.join(',');
                vm.del(idStr);
            }   
       }
        activate();
        function activate() {
            roomCountSvc.grid(vm);
            roomCountSvc.roomList(vm);
            roomCountSvc.findAllOrg(vm);
            roomCountSvc.findAllRoom(vm);
        }
    }
})();

(function() {
	'use strict';

	angular.module('app').factory('roomCountSvc', roomCount);

	roomCount.$inject = [ '$http','$compile' ];	
	function roomCount($http,$compile) {	
		var url_roomCount = rootPath +"/roomCount";
		var url_room = rootPath + "/room";
		var url_user = rootPath + "/user";
		var url_back = '#/roomCount';
		var url_user=rootPath +'/user';
			
		var service = {
			grid : grid,
			queryRoomCount:queryRoomCount,//查询
			roomList : roomList,
			findAllOrg:findAllOrg,//查询部门列表
			cleanValue : cleanValue,
			findAllRoom:findAllRoom
		};		
		return service;	
		
		function cleanValue() {
			var tab = $("#roomCountform").find('input,select');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
		}
		
		function queryRoomCount(vm){
			vm.gridOptions.dataSource.read();
		}
		
		//S_查询预定人
		function findAllRoom(vm){
			var httpOptions = {
					method: 'get',
					url: common.format(url_user + "/findAllUsers")
			}
			var httpSuccess = function success(response) {
				vm.userlist = {};
				vm.userlist = response.data;
			}
			common.http({
				vm: vm,
				$http: $http,
				httpOptions: httpOptions,
				success: httpSuccess
			});
		}
		//E_查询预定人
		
		//S_查询部门列表
		function findAllOrg(vm){
			var httpOptions = {
					method: 'get',
					url: common.format(url_user + "/getOrg")
			}
			var httpSuccess = function success(response) {
				vm.orglist = {};
				vm.orglist = response.data;
			}
			common.http({
				vm: vm,
				$http: $http,
				httpOptions: httpOptions,
				success: httpSuccess
			});
		}
		//E_查询部门列表
		
		//S_查询所有会议室名称
		function roomList(vm){
			
			var httpOptions = {
					method: 'get',
					url: common.format(url_room + "/meeting")
			}
			var httpSuccess = function success(response) {
				vm.roomlists = {};
				vm.roomlists = response.data;
			}
			common.http({
				vm: vm,
				$http: $http,
				httpOptions: httpOptions,
				success: httpSuccess
			});
			
		}
		//E_查询所有会议室名称
		
		//S_giid
		function grid(vm) {
			
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_room+"/fingByOData",$("#roomCountform")),
				schema : common.kendoGridConfig().schema({
					id : "id",
					fields : {
						createdDate : {
							type : "date"
						}
					},
					
				}),
				serverPaging : true,
				serverSorting : true,
				serverFiltering : true,			
				pageSize: 10,
				sort : {
					field : "createdDate",
					dir : "desc"
				},
			
			});

			// End:dataSource
			var  dataBound=function () {  
                var rows = this.items();  
                var page = this.pager.page() - 1;  
                var pagesize = this.pager.pageSize();  
                $(rows).each(function () {  
                    var index = $(this).index() + 1 + page * pagesize;  
                    var rowLabel = $(this).find(".row-number");  
                    $(rowLabel).html(index);  
                });  
            } 
			// Begin:column
			var columns = [
					
					{  
					    field: "rowNumber",  
					    title: "序号",  
					    width: 40,
					    filterable : false,
					    template: "<span class='row-number'></span>"  
					 }
					,
					{
						field : "rbName",
						title : "会议名称",
						width : 120,						
						filterable : false
					},
					{
						field : "addressName",
						title : "会议地点",
						width : 120,						
						filterable : false
					},
					{
						field : "rbDay",
						title : "会议日期",
						width : 160,						
						filterable : false
					},
					{
						field : "beginTimeStr",
						title : "会议开始时间",
						width : 160,						
						filterable : false,
					},
					{
						field : "endTimeStr",
						title : "会议结束时间",
						type : "date",
						width : 160,						
						filterable : false,
					},
					{
						field : "rbType",
						title : "会议类型",
						width : 100,						
						filterable : false
					},
					{
						field : "dueToPeople",
						title : "会议预定人",
						width : 100,						
						filterable : false
					},

			];
			// End:column
		
			vm.gridOptions={
					dataSource : common.gridDataSource(dataSource),
					filterable : common.kendoGridConfig().filterable,
					pageable : common.kendoGridConfig().pageable,
					noRecords:common.kendoGridConfig().noRecordMessage,
					columns : columns,
					dataBound :dataBound,
					resizable: true
				};
			
		}// end fun grid

	}
	
	
})();
(function () {
    'use strict';

    angular.module('app').controller('roomCtrl', room);

    room.$inject = ['$location','roomSvc','$scope','$state']; 

    function room($location, roomSvc,$scope,$state) {
        /* jshint validthis:true */
    	var vm = this;
        vm.title = '会议室预定列表';
        vm.id = $state.params.id;
        vm.workProgramId = $state.params.workProgramId;     //工作方案ID
        vm.startDateTime = new Date("2006/6/1 08:00");
        vm.endDateTime = new Date("2030/6/1 21:00");
        vm.currentDate="";
       
       
        //预定会议编辑
       vm.editRoom = function(){
        	roomSvc.editRoom(vm);
        }
        //预定会议室添加
        vm.addRoom = function(){
        	roomSvc.addRoom(vm);
        }

        //导出本周评审会议安排
        vm.exportThisWeekStage = function(){
        	vm.currentDate=$('.k-sm-date-format').html();
        	vm.rbType="0";//表示评审会
        	roomSvc.exportThisWeekStage(vm);
        }
        //导出本周全部会议安排
        vm.exportThisWeek = function(){
        	vm.currentDate=$('.k-sm-date-format').html();
        	vm.rbType="1";//表示全部
        	roomSvc.exportThisWeekStage(vm);
        }
        //导出下周全部会议安排
        vm.exportNextWeek = function(){
        	console.log(123);
        	var currentDate=$('.k-sm-date-format').html();
        	var str=currentDate.split("-")[0].split("/");
        	var year=str[0];
        	var month=str[1].length==2? str[1] : ("0"+str[1]);
        	var day=str[2].length>=2? str[2].substr(0,2) : ("0"+str[2].substr(0,1));
        	var startDate=new Date(month+"/"+day+"/"+year);
        	var endDate=new Date(month+"/"+day+"/"+year);
        	startDate.setDate(startDate.getDate()+ 8 -startDate.getDay());
        	endDate.setDate(endDate.getDate() + 15- endDate.getDay());
        	var start=new Date(startDate);
        	var end=new Date(endDate);
        	vm.currentDate=start.getFullYear()+"/"+(start.getMonth()+1)+"/"+ start.getDate()+"-" +end.getFullYear()+"/"+(end.getMonth()+1)+"/"+ end.getDate();
        	vm.rbType="1";//表示全部
        	roomSvc.exportThisWeekStage(vm);
        }
        //导出下周评审会议安排
        vm.exportNextWeekStage = function(){
        	var currentDate=$('.k-sm-date-format').html();
        	var str=currentDate.split("-")[0].split("/");
        	var year=str[0];
        	var month=str[1].length==2? str[1] : ("0"+str[1]);
        	var day=str[2].length>=2? str[2].substr(0,2) : ("0"+str[2].substr(0,1));
        	var startDate=new Date(month+"/"+day+"/"+year);
        	var endDate=new Date(month+"/"+day+"/"+year);
        	startDate.setDate(startDate.getDate()+ 8 -startDate.getDay());
        	endDate.setDate(endDate.getDate() + 15- endDate.getDay());
        	var start=new Date(startDate);
        	var end=new Date(endDate);
        	vm.currentDate=start.getFullYear()+"/"+(start.getMonth()+1)+"/"+ start.getDate()+"-" +end.getFullYear()+"/"+(end.getMonth()+1)+"/"+ end.getDate();
        	vm.rbType="0";//表示评审会
        	roomSvc.exportThisWeekStage(vm);
        }
        //会议室查询
        vm.findMeeting = function(){
        	roomSvc.findMeeting(vm);
        }
        
        //kendo导出  未实现
          vm.getPDF =function(selector) {
   		   kendo.drawing.drawDOM($(selector)).then(function (group) {
           kendo.drawing.pdf.saveAs(group, "会议室安排表.pdf");
            
       });
   }
        vm.del = function (id) {
             common.confirm({
            	 vm:vm,
            	 title:"",
            	 msg:"确认删除数据吗？",
            	 fn:function () {
                  	$('.confirmDialog').modal('hide');             	
                    roomSvc.deleteRoom(vm,id);
                 }
             })
        }
        
        vm.dels = function () {     
        	var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
            	common.alert({
                	vm:vm,
                	msg:'请选择数据'
                	
                });
            } else {
            	var ids=[];
                for (var i = 0; i < selectIds.length; i++) {
                	ids.push(selectIds[i].value);
				}  
                var idStr=ids.join(',');
                vm.del(idStr);
            }   
       }

        activate();
        function activate() {
           roomSvc.showMeeting(vm);
            roomSvc.initRoom(vm);
            roomSvc.initWorkProgram(vm);
            roomSvc.initFindUserName(vm);
        }
    }
})();

(function () {
    'use strict';

    angular
        .module('app')
        .controller('roomEditCtrl', room);

    room.$inject = ['$location','roomSvc','$state']; 

    function room($location, roomSvc,$state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加会议室预定';
        vm.isuserExist=false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新会议室';
        }
        
        activate();
        function activate() {
        	if (vm.isUpdate) {
        		roomSvc.getroomById(vm);
            } else {
            	//roomSvc.initZtreeClient(vm);
            }
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').factory('roomSvc', room);

    room.$inject = ['$http'];

    function room($http) {
        var url_room = rootPath + "/room";
        var url_back = '#/room';

        var service = {
            initRoom: initRoom,
            showMeeting: showMeeting,
            findMeeting: findMeeting,
            exportThisWeekStage: exportThisWeekStage,//导出本周评审会会议安排
            exportNextWeekStage: exportNextWeekStage,//下周评审会议安排
            exportThisWeek: exportThisWeek,
            addRoom: addRoom,  //添加会议室预定
            editRoom: editRoom,//编辑
            initFindUserName:initFindUserName,//初始化会议预订人
            initWorkProgram:initWorkProgram,//初始化工作方案
        };

        return service;
        
        //S 初始化工作方案
        function initWorkProgram(vm){
        	 //console.log();
        	if(!vm.workProgramId){
        		return;
        	}else{
        		var httpOptions = {
            			method: 'get',
            			url: rootPath + "/workprogram/html/initWorkProgramById",
            			params:{workId:vm.workProgramId}
            	}
            	var httpSuccess = function success(response) {
            		vm.model = {}
            		vm.model = response.data;
            		vm.model.rbName = vm.model.projectName;
                	vm.model.stageOrgName = vm.model.reviewOrgName;
                	console.log(vm.model.projectName);
                	console.log(vm.model.stageOrgName);
            	}
            	common.http({
            		vm: vm,
            		$http: $http,
            		httpOptions: httpOptions,
            		success: httpSuccess
            	});
        	}
        
        }
        //E 初始化工作方案
        
        //S 初始化预订人
        function initFindUserName(vm){
        	
            var httpOptions = {
                    method: 'get',
                    url: rootPath + "/room/findUser",
                    data: vm.model
                }
                var httpSuccess = function success(response) {
	            	vm.model = {}
	            	vm.model = response.data;
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
        }
        //E 初始化预订人
        
        //S_会议预定编辑
        function editRoom(vm) {
            vm.model.id = $("#id").val();
            vm.model.rbName = $("#rbName").val();
            vm.model.mrID = $("#mrID").val();
            vm.model.rbType = $("#rbType").val();
            vm.model.host = $("#host").val();
            vm.model.dueToPeople = $("#dueToPeople").val();
            vm.model.rbDay = $("#rbDay").val();
            vm.model.beginTime = $("#beginTime").val();
            vm.model.endTime = $("#endTime").val();
            vm.model.content = $("#content").val();
            vm.model.content = $("#remark").val();
            common.initJqValidation($('#formRoom'));
            var isValid = $('#formRoom').valid();
            if (isValid) {
                vm.iscommit = true;
                var httpOptions = {
                    method: 'put',
                    url: rootPath + "/room/updateRoom",
                    data: vm.model
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                }
                            })
                        }
                    })
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }

        //E_会议预定编辑

        // 清空页面数据
        // begin#cleanValue
        function cleanValue() {
            var tab = $("#stageWindow").find('input');
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }

        // end#cleanValue

        //S_添加会议室预定(停用)
        function addRoom(vm) {
            common.initJqValidation($('#formRoom'));
            var isValid = $('#formRoom').valid();
            if (isValid) {
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/room/addRoom",
                    data: vm.model
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                }
                            })
                        }

                    });

                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            }
        }

        //E_添加会议室预定

        //start 初始化会议预定页面
        function initRoom(vm) {
            var dataSource = new kendo.data.SchedulerDataSource({
                batch: true,
                sync: function () {
                    this.read();
                },
                transport: {
                    read: function (options) {
                        var mrID = options.data.mrID;
                        var url = rootPath + "/room";
                        if (mrID) {
                            url += "?" + mrID;
                        }
                        $http.get(
                            url
                        ).success(function (data) {
                            options.success(data.value);
                        }).error(function (data) {
                            console.log("查询数据失败！");
                        });
                    },
                    create: function (options) {
                        common.initJqValidation($('#formRoom'));
                        var isValid = $('#formRoom').valid();
                        if (isValid) {
                            var model = options.data.models[0];
                            model.rbName = $("#rbName").val();
                            model.stageOrgName = $("#stageOrgName").val();
                            model.rbDay = $("#rbDay").val();
                            model.beginTimeStr = $("#beginTime").val();
                            model.endTimeStr = $("#endTime").val();
                            model.beginTime = $("#rbDay").val() + " " + $("#beginTime").val() + ":00";
                            model.endTime = $("#rbDay").val() + " " + $("#endTime").val() + ":00";
                            console.log(model);
                            if (vm.workProgramId) {
                                model.workProgramId = vm.workProgramId;
                            }
                            if (new Date(model.endTime) < new Date(model.beginTime)) {
                                $("#errorTime").html("开始时间不能大于结束时间!");
                                return;
                            }
                            var httpOptions = {
                                method: 'post',
                                url: rootPath + "/room/addRoom",
                                data: model
                            }
                            var httpSuccess = function success(response) {
                                common.requestSuccess({
                                    vm: vm,
                                    response: response,
                                    fn: function () {
                                        common.alert({
                                            vm: vm,
                                            msg: "操作成功",
                                            fn: function () {
                                                findMeeting(vm);
                                                $('.alertDialog').modal('hide');
                                                $('.modal-backdrop').remove();
                                                //vm.schedulerOptions.cancelEvent();
                                            }
                                        })
                                    }
                                });
                            }
                            common.http({
                                vm: vm,
                                $http: $http,
                                httpOptions: httpOptions,
                                success: httpSuccess
                            });
                        }
                    },
                    update: function (options) {
                        common.initJqValidation($('#formRoom'));
                        var isValid = $('#formRoom').valid();
                        if (isValid) {
                            var model = options.data.models[0];
                           
                            model.rbDay = $("#rbDay").val();
                            model.beginTimeStr = $("#beginTime").val();
                            model.endTimeStr = $("#endTime").val();
                            model.beginTime = $("#rbDay").val() + " " + $("#beginTime").val() + ":00";
                            model.endTime = $("#rbDay").val() + " " + $("#endTime").val() + ":00";
                            if (new Date(model.endTime) < new Date(model.beginTime)) {
                                $("#errorTime").html("开始时间不能大于结束时间!");
                                return;
                            }
                            var httpOptions = {
                                method: 'put',
                                url: rootPath + "/room/updateRoom",
                                data: model
                            }
                            var httpSuccess = function success(response) {
                                common.requestSuccess({
                                    vm: vm,
                                    response: response,
                                    fn: function () {
                                        common.alert({
                                            vm: vm,
                                            msg: "操作成功",
                                            fn: function () {
                                                findMeeting(vm);
                                                $('.alertDialog').modal('hide');
                                                $('.modal-backdrop').remove();
                                                vm.schedulerOptions.cancelEvent();
                                            }
                                        })
                                    }
                                });
                            }
                            common.http({
                                vm: vm,
                                $http: $http,
                                httpOptions: httpOptions,
                                success: httpSuccess
                            });
                        }
                    },
                    destroy: function (options) {
                        var id = options.data.models[0].id;
                        var httpOptions = {
                            method: 'delete',
                            url: url_room,
                            data: id
                        }
                        var httpSuccess = function success(response) {
                            common.requestSuccess({
                                vm: vm,
                                response: response,
                                fn: function () {
                                    common.alert({
                                        vm: vm,
                                        msg: "删除成功",
                                        closeDialog: true
                                    })
                                }

                            });

                        }
                        common.http({
                            vm: vm,
                            $http: $http,
                            httpOptions: httpOptions,
                            success: httpSuccess
                        });
                    },
                    parameterMap: function (options, operation) {
                        console.log(operation);
                        if (operation !== "read" && options.models) {
                            return {models: kendo.stringify(options.models)};
                        }
                    }
                },
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                schema: {
                    model: {
                        id: "taskId",
                        fields: {
                            taskId: {from: "id"},
                            title: {from: "addressName", defaultValue: "会议室"},
                            start: {type: "date", from: "beginTime"},
                            end: {type: "date", from: "endTime"}
                        }
                    }
                },
            });

            vm.schedulerOptions = {
            	 toolbar: [ "pdf" ],
            pdf: {
                fileName: "会议室一览表.pdf",
                proxyURL: "https://demos.telerik.com/kendo-ui/service/export"
            },
                date: new Date(),
                startTime: vm.startDateTime,
                endTime: vm.endDateTime,
                height: 600,
                views: [
                    "day",
                    "workWeek",
                    {type: "week", selected: true},
                    "month",
                    "agenda",
                ],
                editable: {
                    template: $("#customEditorTemplate").html(),
                },
                eventTemplate: $("#event-template").html(),
                timezone: "Etc/UTC",
                dataSource: dataSource,
                footer: false,
            };
        }

        //end 初始化会议预定页面

        //start#会议室地点查询
        function showMeeting(vm) {
            $http.get(
                url_room + "/meeting"
            ).success(function (data) {
                vm.meetings = {};
                vm.meetings = data;
                if(vm.meetings && vm.meetings.length > 0){
                    vm.mrID = vm.meetings[0].id;
                    findMeeting(vm)
                }
            }).error(function (data) {
                //alert("查询会议室失败");
            });
        }

        //end #会议室地点查询

        //查询会议室
        function findMeeting(vm) {
            if (vm.mrID) {
                vm.schedulerOptions.dataSource.read({"mrID": common.format("$filter=mrID eq '{0}'", vm.mrID)});
            } else {
                vm.schedulerOptions.dataSource.read();
            }
        }

        //start#deleteRoom
        function deleteRoom(vm) {
            var model = vm.data.models[0];
            var id = model.id;
            var httpOptions = {
                method: 'delete',
                url: url_room,
                data: id
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: "删除成功",
                            fn: function () {
                                $('.alertDialog').modal('show');
                                $('.modal-backdrop').remove();
                            }
                        })
                    }

                });

            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }

        //end#deleteRoom

        //start#exportWeek
        //本周评审会议
        function exportThisWeekStage(vm) {
             var httpOptions = {
                 method: 'get',
                 url: url_room+"/exportThisWeekStage",
                 params:{currentDate : vm.currentDate,rbType : vm.rbType, mrId : vm.mrID}
                
             }
             var httpSuccess = function success(response) {
                 common.requestSuccess({
                     vm: vm,
                     response: response,
                     fn: function () {
                    	 common.alert({
                             vm: vm,
                             msg: "操作成功",
                             fn: function () {
                                 $('.alertDialog').modal('hide');
                                 $('.modal-backdrop').remove();
                             }
                         })
                     }

                 });

             }
             common.http({
                 vm: vm,
                 $http: $http,
                 httpOptions: httpOptions,
                 success: httpSuccess
             });
        }

        //S 下周评审会议
        function exportNextWeekStage(vm) {
        
            var httpOptions = {
                    method: 'get',
                 url: url_room+"/exportThisWeekStage",
                 params:{currentDate : vm.currentDate}
                   
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                       	 common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                }
                            })
                        }

                    });

                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
        }
        //S 下周评审会议
        
        //本周全部会议
        function exportThisWeek() {
            window.open(url_room + "/exportWeek");
        }

        //下周全部会议
        function exportNextWeek() {
            window.open(url_room + "/exportNextWeek");
        }

     
        //end#exportWeek

    }
})();


(function () {
    'use strict';

    angular.module('app').controller('sharingDetailCtrl', sharingPlatlform);

    sharingPlatlform.$inject = ['$location', '$state', '$http', 'sharingPlatlformSvc'];

    function sharingPlatlform($location, $state, $http, sharingPlatlformSvc) {
        var vm = this;
        vm.title = '资料共享详情页';
        vm.model = {}; //创建资料共享对象
        vm.model.sharId = $state.params.sharId;

        //下载
        vm.downloadSysFile = function (id) {
            sharingPlatlformSvc.downloadSysfile(id);
        }

        activate();
        function activate() {
            sharingPlatlformSvc.getSharingDetailById(vm, vm.model.sharId);
            sharingPlatlformSvc.findFileList(vm);  //附件查询
        }
    }
})();
(function () {
    'use strict';

    angular.module('app').controller('sharingPlatlformCtrl', sharingPlatlform);

    sharingPlatlform.$inject = ['$location', '$state', '$http', 'sharingPlatlformSvc'];

    function sharingPlatlform($location, $state, $http, sharingPlatlformSvc) {
        var vm = this;
        vm.title = '共享资料管理';

        activate();
        function activate() {
            sharingPlatlformSvc.grid(vm);
        }

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    sharingPlatlformSvc.deleteSharingPlatlform(vm, id);
                }
            });
        }
        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: '请选择要删除的数据'
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');

                vm.del(idStr);
            }
        };

        /**
         * 批量发布
         */
        vm.bathPublish = function () {
            sharingPlatlformSvc.updatePublish(vm, true);
        }

        /**
         * 批量取消发布
         */
        vm.bathDown = function () {
            sharingPlatlformSvc.updatePublish(vm, false);
        }

        //查询
        vm.querySharing = function () {
            vm.gridOptions.dataSource.read();
        }

    }
})();

(function () {
    'use strict';

    angular.module('app').controller('sharingPlatlformEditCtrl', sharingPlatlform);

    sharingPlatlform.$inject = ['$location', 'sharingPlatlformSvc', '$state'];

    function sharingPlatlform($location, sharingPlatlformSvc, $state) {
        var vm = this;
        vm.title = '新增共享资料';
        vm.model = {};                   //共享平台对象
        vm.businessFlag ={
            isInitFileOption : false,   //是否已经初始化附件上传控件
            isUpdate : false,           //是否为更改
            isInitSeled : false,        //是否已经初始化选择了
        }

        vm.model.sharId = $state.params.sharId;
        if (vm.model.sharId) {
            vm.businessFlag.isUpdate = true;
            vm.title = '更改共享资料';
        }
        activate();
        function activate() {
            if (vm.businessFlag.isUpdate) {
                sharingPlatlformSvc.getSharingPlatlformById(vm);
                //初始化附件列表
                sharingPlatlformSvc.findFileList(vm);
            }else{
                //附件初始化
                sharingPlatlformSvc.initFileOption({
                    sysfileType: "共享平台",
                    uploadBt: "upload_file_bt",
                    vm: vm
                })
            }
            //初始化部门和用户
            sharingPlatlformSvc.initOrgAndUser(vm);
        }

        //重置
        vm.resetSharing = function(){
        	var tab = $("#formSharing").find('input,select');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
        }

        /**
         * 保存发布信息
         */
        vm.create = function () {
            sharingPlatlformSvc.createSharingPlatlform(vm);
        };

        // 业务判断
        vm.checkBox = function ($event) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            var checkboxValue = checkbox.value;
            if (checked) {
                $('input[tit=\"' + checkboxValue + '\"]').each(function () {
                    $(this).attr("disabled", "disabled");
                    $(this).removeAttr("checked");
                });
            } else {
                $('input[tit=\"' + checkboxValue + '\"]').each(function () {
                    $(this).removeAttr("disabled");
                });
            }
        }
        

    }
})();

(function () {
    'use strict';

    angular.module('app').factory('sharingPlatlformSvc', sharingPlatlform);

    sharingPlatlform.$inject = ['$http'];

    function sharingPlatlform($http) {
        var url_sharingPlatlform = rootPath + "/sharingPlatlform", url_back = '#/sharingPlatlform';

        var service = {
            grid: grid,
            getSharingPlatlformById: getSharingPlatlformById,
            createSharingPlatlform: createSharingPlatlform,
            deleteSharingPlatlform: deleteSharingPlatlform,
            initFileOption: initFileOption,					//初始化上传控件
            findFileList: findFileList,					    //系统附件列表
            getSharingDetailById: getSharingDetailById,	    //获取详情页
            downloadSysfile: downloadSysfile,		        //下载
            initOrgAndUser: initOrgAndUser,                 //初始化部门和用户
            initSeleObj : initSeleObj,                      //初始化选择的用户
            updatePublish : updatePublish,                  //批量更改发布状态
        };

        return service;

        //S_初始化选择的用户
        function initSeleObj(vm){
            if((vm.shareOrgList && vm.shareOrgList.length > 0)){
                //1、先计算选择的部门
                if(vm.model.privilegeDtoList && vm.model.privilegeDtoList.length > 0){
                    vm.shareOrgList.forEach(function (so,i){
                        vm.model.privilegeDtoList.forEach(function (p,index) {
                            if(p.businessType == 1 || p.businessType == "1"){
                                if(p.businessId == so.id){
                                    so.isChecked = true;
                                    so.userDtos.forEach(function (u,k){
                                        u.isDisabled = true;
                                    });
                                }
                            }else if(p.businessType == 2 || p.businessType == "2"){
                                so.userDtos.forEach(function (u,k){
                                    if(p.businessId == u.id){
                                        u.isChecked = true;
                                    }
                                });
                            }
                        })

                    });
                }

                if(vm.noOrgUsetList && vm.noOrgUsetList.length > 0){
                    vm.noOrgUsetList.forEach(function (nu,i){
                        vm.model.privilegeDtoList.forEach(function (p,index) {
                             if( (p.businessType == 2 || p.businessType == "2") && p.businessId == nu.id){
                                 nu.isChecked = true;
                            }
                        })
                    });
                }
                vm.businessFlag.isInitSeled = true;
            }
        }//E_initSeleObj

        //S_初始化部门和用户
        function initOrgAndUser(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sharingPlatlform/initOrgAndUser",
            };
            var httpSuccess = function success(response) {
                vm.shareOrgList = response.data.orgDtoList;
                vm.noOrgUsetList = response.data.noOrgUserList;
                if(vm.businessFlag.isUpdate && !vm.businessFlag.isInitSeled){
                    initSeleObj(vm);
                }
            };
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_initOrgAndUser

        //下载
        function downloadSysfile(id) {
            if (id) {
                window.open(rootPath + "/file/fileDownload?sysfileId=" + id);
            }
        }

        //S 详情页面
        function getSharingDetailById(vm, id) {
            var httpOptions = {
                method: 'get',
                url: url_sharingPlatlform + "/html/sharingDeatilById",
                params: {id: id}
            };
            var httpSuccess = function success(response) {
                vm.model = response.data;
            };
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //E 详情页面


        //S_findFileList
        function findFileList(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/file/findByBusinessId",
                params: {
                    businessId: vm.model.sharId
                }
            }
            var httpSuccess = function success(response) {
                vm.sysFilelists = response.data;

            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_findFileList

        //S_initFileOption
        function initFileOption(option) {
            if (option.uploadBt) {
                $("#" + option.uploadBt).click(function () {
                    if (!option.businessId) {
                        common.alert({
                            vm: option.vm,
                            msg: "请先保存数据！",
                            closeDialog: true
                        })
                    } else {
                        $("#commonuploadWindow").kendoWindow({
                            width: 700,
                            height: 400,
                            title: "附件上传",
                            visible: false,
                            modal: true,
                            closable: true,
                            actions: ["Pin", "Minimize", "Maximize", "Close"]
                        }).data("kendoWindow").center().open();
                    }
                });

                if (option.businessId) {
                    //附件下载方法
                    option.vm.downloadSysFile = function (id) {
                        window.open(rootPath + "/file/fileDownload?sysfileId=" + id);
                    }
                }
                //附件删除方法
                option.vm.delSysFile = function (id) {
                    var httpOptions = {
                        method: 'delete',
                        url: rootPath + "/file/deleteSysFile",
                        params: {
                            id: id
                        }
                    }
                    var httpSuccess = function success(response) {
                        common.requestSuccess({
                            vm: option.vm,
                            response: response,
                            fn: function () {
                                findFileList(option.vm);
                                common.alert({
                                    vm: option.vm,
                                    msg: "删除成功",
                                    closeDialog: true
                                })
                            }
                        });
                    }
                    common.http({
                        vm: option.vm,
                        $http: $http,
                        httpOptions: httpOptions,
                        success: httpSuccess
                    });
                }

                var projectfileoptions = {
                    language: 'zh',
                    allowedPreviewTypes: ['image'],
                    allowedFileExtensions: ['jpg', 'png', 'gif', "xlsx", "docx", "doc", "xls", "pdf", "ppt", "zip", "rar","txt"],
                    maxFileSize: 2000,
                    showRemove: false,
                    uploadUrl: rootPath + "/file/fileUpload",
                    uploadExtraData: function(previewId, index) {
                        var result={};
                        result.businessId=option.businessId;
                        result.sysSignId="共享平台";
                        result.sysfileType=angular.isUndefined(option.sysfileType) ? "共享平台" : option.sysfileType;
                        result.sysMinType=$("#sysMinType option:selected").val();
                        return result;
                        // businessId: option.businessId,
                        // sysSignId:"共享平台",
                        // sysfileType: angular.isUndefined(option.sysfileType) ? "共享平台" : option.sysfileType,
                    }
                };

                var filesCount = 0;
                $("#sysfileinput").fileinput(projectfileoptions)
                    .on("filebatchselected", function (event, files) {
                        filesCount = files.length;
                    }).on("fileuploaded", function (event, data, previewId, index) {
                    if (filesCount == (index + 1)) {
                        findFileList(option.vm);
                    }
                });
                option.vm.businessFlag.isInitFileOption = true;
            }

        }//E_initFileOption

        // begin#deleteSharingPlatlform
        function deleteSharingPlatlform(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_sharingPlatlform + "/sharingDelete",
                data: id
            };

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: "操作成功",
                            closeDialog: true,
                            fn: function () {
                                vm.isSubmit = false;
                                vm.gridOptions.dataSource.read();
                            }
                        })
                    }
                });
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#createSharingPlatlform
        function createSharingPlatlform(vm) {
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                //如果不是全员可看，则要获取选择的部门和用户
                if(vm.model.isNoPermission != 9){
                    vm.model.privilegeDtoList = [];
                    //1、先计算选择的部门
                    var oCheck = $("input[name='shareOrg']:checked");
                    if (oCheck.length > 0) {
                        for (var i = 0; i < oCheck.length; i++) {
                            var shareOrg = {};
                            shareOrg.businessId = oCheck[i].value;
                            shareOrg.businessType = "1";
                            vm.model.privilegeDtoList.push(shareOrg);
                        }
                    }
                    //2、再选择人员
                    var uCheck = $("input[name='shareUser']:not(:disabled):checked");
                    if (uCheck.length > 0) {
                        for (var i = 0; i < uCheck.length; i++) {
                            var shareUser = {};
                            shareUser.businessId = uCheck[i].value;
                            shareUser.businessType = "2";
                            vm.model.privilegeDtoList.push(shareUser);
                        }
                    }

                }
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/sharingPlatlform/saveSharing",
                    data: vm.model
                };

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            vm.isSubmit = false;
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                closeDialog: true
                            })
                        }
                    });
                    vm.model.sharId = response.data.sharId;
                    //初始化附件上传
                    initFileOption({
                        businessId: vm.model.sharId,
                        sysfileType: "共享平台",
                        uploadBt: "upload_file_bt",
                        vm: vm
                    });
                };

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess,
                    onError:function () {
                        vm.isSubmit = false;
                    }
                });

            }
        }

        // begin#getSharingPlatlformById
        function getSharingPlatlformById(vm) {
            var httpOptions = {
                method: 'get',
                url: url_sharingPlatlform + "/html/findById",
                params: {id: vm.model.sharId}
            };
            var httpSuccess = function success(response) {
                vm.model = response.data;
                if(!vm.businessFlag.isInitSeled){
                    initSeleObj(vm);
                }
                initFileOption({
                    businessId: vm.model.sharId,
                    sysfileType: "共享平台",
                    uploadBt: "upload_file_bt",
                    vm: vm
                });
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#grid
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_sharingPlatlform + "/findByCurUser", $("#formSharing")),
                schema: common.kendoGridConfig().schema({
                    id: "sharId",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            // End:dataSource
            //S_序号
            var dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            };
            //S_序号
            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.sharId)
                    },
                    filterable: false,
                    width: 30,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "unitSort",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "theme",
                    title: "主题",
                    width: 180,
                    filterable: false
                },
                {
                    field: "publishUsername",
                    title: "发布人",
                    width: 120,
                    filterable: false
                },
                {
                    field: "publishDate",
                    title: "发布时间",
                    format: "{0:yyyy-MM-dd hh24:mm:ss}",
                    width: 180,
                    filterable: false
                },
                {
                    field: "isPublish",
                    title: "发布状态",
                    width: 100,
                    template: function (item) {
                       if(item.isPublish && item.isPublish == 9){
                           return "已发布";
                       }else{
                           return "未发布";
                       }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 180,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            item.sharId, item.sharId, "vm.del('" + item.sharId + "')");
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                dataBound: dataBound,
                columns: columns,
                resizable: true
            };

        }// end fun grid


        //S_批量发布
        function updatePublish(vm,isUpdate){
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: "请选择要批量发布的数据"
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/sharingPlatlform/updatePublish",
                    params: {
                        ids: ids.join(','),
                        status: (isUpdate == true)?'9':'0'
                    }
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            vm.isSubmit = false;
                            vm.gridOptions.dataSource.read();
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                closeDialog: true
                            })
                        }
                    })
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }//E_bathPublish

    }
})();
(function () {
    'use strict';

    angular.module('app').controller('sharingPlatlformYetCtrl', sharingPlatlformYet);

    sharingPlatlformYet.$inject = ['$location', '$state','$http','sharingPlatlformYetSvc'];

    function sharingPlatlformYet($location,$state, $http,sharingPlatlformYetSvc) {
        var vm = this;
        vm.title = '共享管理';
        vm.model = {};

        activate();
        function activate() {
            sharingPlatlformYetSvc.grid(vm);
        }
        
        //查询
        vm.findSharing = function(){
        	vm.gridOptions.dataSource.read();
        }
        //重置
        vm.resetShared = function(){
        	var tab = $("#formSharingPub").find('select');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
        }
    }
})();

(function () {
    'use strict';
    angular.module('app').factory('sharingPlatlformYetSvc', sharingPlatlformYet);
    sharingPlatlformYet.$inject = ['$http'];

    function sharingPlatlformYet($http) {
        var service = {
            grid: grid,
        };

        return service;

        // begin#grid
        function grid(vm) {
        	
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/sharingPlatlform/findByReception",$("#formSharingPub")),
                schema: common.kendoGridConfig().schema({
                    id: "sharId",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            // End:dataSource
            //S_序号
            var dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            };
            //S_序号

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.sharId)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "unitSort",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                
                {
                    field: "theme",
                    title: "共享主题",
                    width: 180,
                    filterable: true
                },
                {
                    field: "publishUsername",
                    title: "发布人",
                    width: 100,
                    filterable: true
                },
                {
                    field: "publishDate",
                    title: "发布时间",
                    width: 100,
                    filterable: true
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),item.sharId);
                    	
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound:dataBound,
                resizable: true
            };

        }// end fun grid

    }
})();
(function () {
    'use strict';

    angular.module('app').factory('ideaSvc', idea);

    idea.$inject = ['$http', '$state'];

    function idea($http, $state) {
        var service = {
        	initIdea : initIdea //初始化个人常用意见
        };
        return service;
        
        
        function initIdea(vm){
        
        	var httpOptions={
        		method: 'get',
           		url: rootPath + "/idea"
        	}
        	
        	var httpSuccess=function success(response){
        		vm.ideas=response.data;
        	}
        	
        	 common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
     }
})();
(function () {
    'use strict';

    angular.module('app').controller('signCreateCtrl', sign);

    sign.$inject = ['$location','signSvc','$state','bsWin'];

    function sign($location, signSvc,$state,bsWin) {
        var vm = this;
    	vm.model = {};						//创建一个form对象
        vm.title = '新增收文';        		//标题

        vm.create = function () {
            common.initJqValidation();
            var isValid = $('form').valid();
            if(isValid){
                signSvc.createSign(vm.model,function(data){
                    if (data.flag || data.reCode == "ok") {
                        bsWin.success("操作成功，请继续填写项目审核登记表",function(){
                            $state.go('fillSign', {signid: data.reObj.signid}, {reload: true});
                        });
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }

        };       
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('signCtrl', sign);

    sign.$inject = ['$location','signSvc','$state','flowSvc','signFlowSvc','bsWin'];

    function sign($location,signSvc,$state,flowSvc,signFlowSvc,bsWin) {
        var vm = this;
        vm.title = "收文列表";

        active();
        function active() {
            signSvc.grid(vm);
        }

        //收文查询
        vm.querySign = function(){
        	signSvc.querySign(vm);
        }
        vm.check=function(){
      	 		vm.isAssociate = vm.ischeck?9:0;
      	 }
        
        //start 收文删除
        vm.del = function (signid) {       	   
             common.confirm({
              	 vm:vm,
              	 title:"",
              	 msg:"确认删除数据吗？",
              	 fn:function () {
                    	$('.confirmDialog').modal('hide');             	
                    	signSvc.deleteSign(vm,signid);
                 }
              })
         }//end 收文删除
        
         //start 收文删除
         vm.dels = function () {         	 
        	 var selectIds = common.getKendoCheckId('.grid');         	
             if (selectIds.length == 0) {
              	common.alert({
                  	vm:vm,
                  	msg:'请选择数据'                 	
                  });
              } else {
            	  var ids=[];
                  for (var i = 0; i < selectIds.length; i++) {
                  	ids.push(selectIds[i].value);
    			  }  
                  var idStr=ids.join(',');
                  vm.del(idStr);
              }   
         }//end 收文删除
         
         //************************** S 以下是新流程处理js **************************//
         vm.startNewFlow = function(signid){
             bsWin.confirm({
                 title: "询问提示",
                 message: "确认已经完成填写，并且发起流程么？",
                 onOk: function () {
                     $('.confirmDialog').modal('hide');
                     signFlowSvc.startFlow(signid,function(data){
                         if(data.flag || data.reCode == 'ok'){
                             vm.gridOptions.dataSource.read();
                             bsWin.success("操作成功！");
                         }else{
                             bsWin.error(data.reMsg);
                         }
                     });
                 }
             });
         }

        /**
         * 正式签收收文
         * @param signId
         */
        vm.realSign = function(signid){
            common.confirm({
                vm:vm,
                title:"",
                msg:"确认正式签收了么？",
                fn:function () {
                    $('.confirmDialog').modal('hide');
                    signSvc.realSign(vm,signid);
                }
            })
        }
         //************************** S 以下是新流程处理js **************************//

    }
})();

(function () {
    'use strict';

    angular.module('app').controller('signFillinCtrl', sign);

    sign.$inject = ['signSvc', 'sysfileSvc','$state', '$http','bsWin'];

    function sign(signSvc,sysfileSvc, $state, $http,bsWin) {
        var vm = this;
        vm.model = {};		//创建一个form对象
        vm.title = '填写报审登记表';        		//标题
        vm.model.signid = $state.params.signid;	//收文ID
        vm.flowDeal = false;		//是否是流程处理标记

        vm.busiObj ={};             //业务对象，用于记录页面操作对象等信息

        active();
        function  active(){
            signSvc.initFillData(vm.model.signid,function(data){
                vm.model = data.reObj.sign;
                vm.deptlist = data.reObj.deptlist

                if (data.reObj.mainOfficeList) {
                    vm.mainOfficeList = data.reObj.mainOfficeList;
                }
                if (data.reObj.assistOfficeList) {
                    vm.assistOfficeList = data.reObj.assistOfficeList;
                }
                //建设单位和编制单位
                vm.companyList = data.reObj.companyList;
                //分管领导信息
                vm.busiObj.leaderList = data.reObj.leaderList;

                //初始化附件上传
                if(vm.model.signid){
                    sysfileSvc.initUploadOptions({
                        businessId:vm.model.signid,
                        sysSignId :vm.model.signid,
                        sysfileType:"审批登记表",
                        uploadBt:"upload_file_bt",
                        detailBt:"detail_file_bt",
                        vm:vm
                    });
                }
            });
        }

        //选择默认办理部门
        vm.checkOrgType = function($event){
            var checkbox = $event.target;
            var checked = checkbox.checked;
            var checkboxValue = checkbox.value;
            if(checked){
                vm.model.leaderName = signcommon.getDefaultLeader(checkboxValue);
                vm.model.comprehensivehandlesug = signcommon.getDefaultZHBYJ(checkboxValue);
                vm.model.comprehensiveName = '综合部';
                //设置综合部和分管领导ID
                $.each(vm.busiObj.leaderList,function(i,leader){
                    console.log(leader.mngOrgType+"---"+checkboxValue)
                    if(leader.mngOrgType == checkboxValue){
                        vm.model.leaderId = leader.id;
                        vm.model.leaderName = leader.displayName;
                        vm.model.comprehensivehandlesug = "请"+(leader.displayName).substring(0,2)+"主任阅示。";
                    }
                })
                if(!vm.model.leaderId){
                    bsWin.alert("选择的默认办理部门没有合适的分管领导，请先设置分管领导角色用户！");
                }
                var date = new Date();
                var monthValue = (date.getMonth()+1) < 10 ?"0"+(date.getMonth()+1):(date.getMonth()+1);
                var dayValue = (date.getDate()) < 10 ?"0"+(date.getDate()):(date.getDate());
                vm.model.comprehensiveDate = (date.getFullYear()+"-"+monthValue+"-"+dayValue);
            }
        }

        //发起流程
        vm.startNewFlow = function(){
            bsWin.confirm({
                title: "询问提示",
                message: "确定发起流程么",
                onOk: function () {
                    $('.confirmDialog').modal('hide');
                    var httpOptions = {
                        method : 'post',
                        url : rootPath+"/sign/startNewFlow",
                        params : {
                            signid:vm.model.signid
                        }
                    }
                    var httpSuccess = function success(response) {
                        if(response.data.reCode == "ok"){
                            bsWin.success("操作成功！");
                        }else{
                            bsWin.error(response.data.reMsg);
                        }
                    }
                    common.http({
                        vm : vm,
                        $http : $http,
                        httpOptions : httpOptions,
                        success : httpSuccess
                    });
                }
            });
        }

        //打印预览
        vm.signPreview = function (oper) {

            var htmlBody=$(".well").parents("body");
            var htmlsidebar=htmlBody.find(".main-sidebar");
            var htmlhedaer=htmlBody.find(".main-header");
            var htmlContentwrapper=htmlBody.find(".content-wrapper");
            //隐藏不需打印的区域;
             htmlsidebar.hide();
             htmlhedaer.hide();
             $(".toolbar").hide();

            //修改打印显示样式

                 //添加替换input的显示内容，打印后自动删除
                     $(".well input").each(function(){
                         var inptTpye=$(this).attr("type");
                         if(inptTpye=="text"){
                                 $(this).before('<span class="printmesge" data="text" style="white-space : nowrap;">'+$(this).val()+'</span>');
                         };
                         if(inptTpye=="checkbox"){
                             if($(this).is(':checked')){
                                 $(this).before('<span class="printmesge" data="text">有</span>');
                             }else{
                                 $(this).before('<span class="printmesge" data="text">无</span>');
                             }
                         }
                     });
                   $(".printmesge").show();
                   $(".well input[type=text]").hide();
                   $(".well input[type=checkbox]").hide();
                   $(".well button").hide();
                   htmlContentwrapper.find("td div select").hide();
                   htmlContentwrapper.find("td div span").css("margin","0");

               /*自定义表格样式*/
             $(".well").addClass("printbody");
             $(".well .table-bordered").addClass("tableBOX");
             htmlContentwrapper.find("input").addClass("noborder");
             htmlContentwrapper.addClass("nomargin");

             window.print();

         // 恢复原有
            htmlsidebar.show();
            htmlhedaer.show();
            $(".toolbar").show();
            $(".printmesge").hide();
            $(".well input[type=text]").show();
            $(".well input[type=checkbox]").show();
            $(".well button").show();
            htmlContentwrapper.find("td div select").show();
            htmlContentwrapper.find("td div span").css("margin-left","100px");
            $(".well").removeClass("printbody");
            $(".well .table-bordered").removeClass("tableBOX");
            $("[data=text]").remove();//删除临时添加的内容

            htmlContentwrapper.find("input").removeClass("noborder");
            htmlContentwrapper.removeClass("nomargin");


          /* if (oper < 5) {
               /!* var bdhtml = window.document.table.innerHTML;//获取当前页的html代码
                var sprnstr = "<!--startprint" + oper + "   ";//设置打印开始区域
                var eprnstr = "<!--endprint" + oper + "-->";//设置打印结束区域
                var prnhtml = bdhtml.substring(bdhtml.indexOf(sprnstr) + 10); //从开始代码向后取html
                var prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));//从结束代码向前取html
                window.document.table.innerHTML = prnhtml;
                window.print();
                window.document.table.innerHTML = bdhtml;*!/

            } else {
                window.print();
            }*/
        }

        //申报登记编辑
        vm.updateFillin = function () {
            vm.isSubmit = true;
            signSvc.updateFillin(vm.model,function (data) {
                vm.isSubmit = false;
                bsWin.alert("操作成功！");
            });
        }

        //根据协办部门查询用户
        vm.findOfficeUsersByDeptName = function (status) {
            var param = {};
            if ("main" == status) {
                param.maindeptName = vm.model.maindeptName;
            } else {
                param.assistdeptName = vm.model.assistdeptName;
            }
            signSvc.findOfficeUsersByDeptName(param,function(data){
                if ("main" == status) {
                    vm.mainOfficeList = {};
                    vm.mainOfficeList = data;
                } else {
                    vm.assistOfficeList = {};
                    vm.assistOfficeList = data;
                }
            });
        }

    }
})();

(function () {
    'use strict';

    angular.module('app').controller('signEndCtrl', sign);

    sign.$inject = ['$location','signSvc','$state','flowSvc'];

    function sign($location,signSvc,$state,flowSvc) {
        var vm = this;
        vm.title = "已办结项目详情";
        vm.model = {};
        vm.flow = {};
        vm.model.signid = $state.params.signid;   //业务ID
        vm.flow.processInstanceId = $state.params.processInstanceId;	//流程实例ID
        vm.flow.hideFlowImg = true;

        //按钮显示控制，全部归为这个对象控制
        vm.showFlag = {
            tabWorkProgram:false,       // 显示工作方案标签tab
            tabBaseWP:false,            // 项目基本信息tab
            tabDispatch:false,          // 发文信息tab
            tabFilerecord:false,        // 归档信息tab
            tabExpert:false,            // 专家信息tab
            tabSysFile:false,           // 附件信息tab
            tabAssociateSigns:false,    // 关联项目tab
        };

        //业务控制对象
        vm.businessFlag = {

        }
        active();
        function active(){
            $('#myTab li').click(function (e) {
                var aObj = $("a",this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#"+showDiv).addClass("active").addClass("in").show(500);
            })

            flowSvc.initFlowData(vm);
            signSvc.initFlowPageData(vm);
        }
        //获取专家评星
        vm.getExpertStar = function(id ,score){
            var returnStr = "";
            if (score != undefined) {
                for (var i = 0; i <score; i++) {
                    returnStr += "<span style='color:gold;font-size:20px;'><i class='fa fa-star' aria-hidden='true'></i></span>";
                }
            }
            $("#"+id+"_starhtml").html(returnStr);
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').factory('signFlowSvc', signFlow);

    signFlow.$inject = ['$http', '$state', 'bsWin'];

    function signFlow($http, $state, bsWin) {
        var service = {
            startFlow: startFlow,			            //启动流程
            initBusinessParams: initBusinessParams,	    //初始化业务参数
            checkBusinessFill: checkBusinessFill,	    //检查相应的表单填写
            getChargeDispatch: getChargeDispatch,		//获取发文
            getChargeFilerecord: getChargeFilerecord,	//获取归档信息
            endSignDetail: endSignDetail,                 //已办结的签收信息
        };
        return service;

        //S_startFlow
        function startFlow(signid,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/startNewFlow",
                params: {
                    signid: signid
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_startFlow

        //S_initBusinessParams
        function initBusinessParams(vm) {
            switch (vm.flow.curNode.activitiId) {
                //项目签收环节
                case flowcommon.getSignFlowNode().SIGN_QS:
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeSign = true;
                    break;
                //综合部办理
                case flowcommon.getSignFlowNode().SIGN_ZHB:
                    vm.showFlag.buttBack = true;
                    vm.showFlag.businessNext = true;
                    vm.showFlag.nodeSelViceMgr = true;
                    if (vm.flow.businessMap) {
                        vm.viceDirectors = vm.flow.businessMap.viceDirectors;
                    }
                    break;
                //分管领导审批工作方案
                case flowcommon.getSignFlowNode().SIGN_FGLD_FB:
                    vm.showFlag.buttBack = true;    //可回退
                    vm.showFlag.businessNext = true;
                    vm.showFlag.nodeSelOrgs = true;
                    if (vm.flow.businessMap) {
                        vm.orgs = vm.flow.businessMap.orgs;
                    }
                    break;
                //部门分办
                case flowcommon.getSignFlowNode().SIGN_BMFB1:
                    vm.businessFlag.isMainBranch = true;
                case flowcommon.getSignFlowNode().SIGN_BMFB2:
                case flowcommon.getSignFlowNode().SIGN_BMFB3:
                case flowcommon.getSignFlowNode().SIGN_BMFB4:
                    vm.showFlag.businessNext = true;
                    vm.showFlag.nodeSelPrincipal = true;
                    if (vm.flow.businessMap) {
                        vm.users = vm.flow.businessMap.users;
                    }
                    break;
                //项目负责人办理
                case flowcommon.getSignFlowNode().SIGN_XMFZR1:
                    vm.businessFlag.isMainBranch = true;
                case flowcommon.getSignFlowNode().SIGN_XMFZR2:
                case flowcommon.getSignFlowNode().SIGN_XMFZR3:
                case flowcommon.getSignFlowNode().SIGN_XMFZR4:
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeWorkProgram = true;
                    vm.businessFlag.isFinishWP = vm.flow.businessMap.isFinishWP;
                    //已经在做工作方案，则显示
                    if(vm.model.processState >= 2){
                        vm.showFlag.tabWorkProgram = true;
                        $("#show_workprogram_a").click();
                    }
                    break;
                //发文
                case flowcommon.getSignFlowNode().SIGN_FW:
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeDispatch = true;
                    if(vm.flow.businessMap.prilUserList){
                        vm.businessFlag.principalUsers = vm.flow.businessMap.prilUserList;
                        vm.showFlag.businessNext = true;
                    }
                    //已经填报的发文信息，则显示
                    if(vm.model.processState >= 4){
                        vm.showFlag.tabDispatch = true;
                        $("#show_dispatch_a").click();
                    }
                    break;
                //项目负责人确认发文
                case flowcommon.getSignFlowNode().SIGN_QRFW:
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeConfirmDis = true;
                    vm.businessFlag.passDis = '9';  //默认通过
                    vm.showFlag.tabDispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                //部长审批发文
                case flowcommon.getSignFlowNode().SIGN_BMLD_QRFW:
                //分管领导审批发文
                case flowcommon.getSignFlowNode().SIGN_FGLD_QRFW:
                //主任审批发文
                case flowcommon.getSignFlowNode().SIGN_ZR_QRFW:
                //生成发文编号
                case flowcommon.getSignFlowNode().SIGN_FWBH:
                    vm.showFlag.tabDispatch = true;
                    $("#show_dispatch_a").click();
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeCreateDisNum = true;
                    break;
                //财务办理
                case flowcommon.getSignFlowNode().SIGN_CWBL:
                	vm.showFlag.businessTr = true;
                	vm.showFlag.financialCode = true;
                    break;
                //归档
                case flowcommon.getSignFlowNode().SIGN_GD:
                    //有第二负责人确认
                    if(vm.flow.businessMap.checkFileUser){
                        vm.showFlag.businessNext = true;
                    }
                    vm.showFlag.businessTr = true;
                    vm.showFlag.nodeFileRecord = true;
                    if(vm.model.processState > 6){
                        vm.showFlag.tabFilerecord = true;
                        $("#show_filerecord_a").click();
                    }
                    break;
                //第二负责人确认归档
                case flowcommon.getSignFlowNode().SIGN_DSFZR_QRGD:
                    vm.showFlag.tabFilerecord = true;
                    $("#show_filerecord_a").click();
                    break;
                //最终归档
                case flowcommon.getSignFlowNode().SIGN_QRGD:
                    vm.showFlag.tabFilerecord = true;
                    $("#show_filerecord_a").click();

                    vm.showFlag.nodeNext = false;
                    break;
                default:
                    ;
            }
        }//E_initBusinessParams

        //S_checkBusinessFill
        function checkBusinessFill(vm) {
            if(!vm.flow.businessMap){
                vm.flow.businessMap = {};
            }

            //默认通过
            var resultObj = {};
            resultObj.resultTag = true;

            switch (vm.flow.curNode.activitiId) {
                //综合部拟办
                case  flowcommon.getSignFlowNode().SIGN_ZHB:
                    if ($("#viceDirector").val()) {
                        vm.flow.businessMap.FGLD = $("#viceDirector").val();
                    } else {
                        resultObj.resultTag = false;
                    }
                    break;
                //分管领导审批，要选择主办部门
                case flowcommon.getSignFlowNode().SIGN_FGLD_FB:
                    resultObj.resultTag = false;
                    $('.seleteTable input[selectType="main"]:checked').each(function () {
                        vm.flow.businessMap.MAIN_ORG = $(this).val();
                        resultObj.resultTag = true;
                    });
                    if(resultObj.resultTag){
                        var assistOrgArr = [];
                        $('.seleteTable input[selectType="assist"]:checked').each(function () {
                            assistOrgArr.push($(this).val())
                        });
                        if(assistOrgArr.length > 3){
                            resultObj.resultTag = false;
                            resultObj.resultMsg = "协办部门最多只能选择3个！";
                        }else{
                            vm.flow.businessMap.ASSIST_ORG = assistOrgArr.join(',');
                        }
                    }else{
                        resultObj.resultMsg = "主办部门不为空！";
                    }
                    break;
                //部门分办，要选择办理人员
                case flowcommon.getSignFlowNode().SIGN_BMFB1:
                    //主办才有第一负责人，协办的全是第二负责人
                    var selUserId = $("#selPrincipalMainUser").val();
                    if (!selUserId) {
                        resultObj.resultTag = false;
                        resultObj.resultMsg = "必须要选择一个第一负责人！";
                        break;
                    }
                    resultObj.resultTag = true;
                    vm.flow.businessMap.M_USER_ID = selUserId;
                    //判断选择第二负责人
                    var assistIdArr = [];
                    $('#principalAssistUser input[selectType="assistUser"]:checked').each(function () {
                        assistIdArr.push($(this).val());
                    });
                    if (assistIdArr.length > 0) {
                        vm.flow.businessMap.A_USER_ID = assistIdArr.join(',');
                    }
                    break;
                case flowcommon.getSignFlowNode().SIGN_BMFB2:
                case flowcommon.getSignFlowNode().SIGN_BMFB3:
                case flowcommon.getSignFlowNode().SIGN_BMFB4:
                    var assistIdArr = [];
                    $('#principalAssistUser input[selectType="assistUser"]:checked').each(function () {
                        assistIdArr.push($(this).val());
                    });
                    if (assistIdArr.length > 0) {
                        vm.flow.businessMap.A_USER_ID = assistIdArr.join(',');
                    }else{
                        resultObj.resultTag = false;
                        resultObj.resultMsg = "必须要选择负责人！";
                    }
                    break;
                case flowcommon.getSignFlowNode().SIGN_XMFZR1:
                case flowcommon.getSignFlowNode().SIGN_XMFZR2:
                case flowcommon.getSignFlowNode().SIGN_XMFZR3:
                case flowcommon.getSignFlowNode().SIGN_XMFZR4:
                    if(vm.businessFlag.isNeedWP == 9 && vm.businessFlag.isFinishWP == false){
                        resultObj.resultTag = false;
                        resultObj.resultMsg = "您还没有完成工作方案，不能进行下一步操作！";
                    }
                    vm.flow.businessMap.IS_NEED_WP = vm.businessFlag.isNeedWP;
                    break;
                //部长审核工作方案
                case flowcommon.getSignFlowNode().SIGN_BMLD_SPW1:
                case flowcommon.getSignFlowNode().SIGN_BMLD_SPW2:
                case flowcommon.getSignFlowNode().SIGN_BMLD_SPW3:
                case flowcommon.getSignFlowNode().SIGN_BMLD_SPW4:
                   break;
                //分管领导审核工作方案
                case flowcommon.getSignFlowNode().SIGN_FGLD_SPW1:
                case flowcommon.getSignFlowNode().SIGN_FGLD_SPW2:
                case flowcommon.getSignFlowNode().SIGN_FGLD_SPW3:
                case flowcommon.getSignFlowNode().SIGN_FGLD_SPW4:
                    break;
                //发文申请
                case flowcommon.getSignFlowNode().SIGN_FW:
                    if(vm.model.processState < 4){
                        resultObj.resultTag = false;
                        resultObj.resultMsg = "您还没完成发文操作，不能进行下一步操作！";
                    }
                    break;
                //项目负责人确认发文
                case flowcommon.getSignFlowNode().SIGN_QRFW:
                    if(vm.businessFlag.passDis == '9' || vm.businessFlag.passDis == 9){
                        vm.flow.businessMap.AGREE = '9';
                    }else{
                        vm.flow.businessMap.AGREE = '0';
                    }
                    break;
                //部长审批发文
                case flowcommon.getSignFlowNode().SIGN_BMLD_QRFW:
                //分管领导审批发文
                case flowcommon.getSignFlowNode().SIGN_FGLD_QRFW:
                //主任审批发文
                case flowcommon.getSignFlowNode().SIGN_ZR_QRFW:
                    vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id;
                    break;
                //生成发文编号
                case flowcommon.getSignFlowNode().SIGN_FWBH:
                    vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id;
                    break;
                //财务办理
                case flowcommon.getSignFlowNode().SIGN_CWBL:
                    break;
                //归档
                case flowcommon.getSignFlowNode().SIGN_GD:
                    if(vm.model.processState < 7 ){
                        resultObj.resultTag = false;
                        resultObj.resultMsg = "您还没完成归档操作，不能进行下一步操作！";
                    }
                    break;
                //第二负责人确认归档
                case flowcommon.getSignFlowNode().SIGN_DSFZR_QRGD:
                    break;
                //最终归档
                case flowcommon.getSignFlowNode().SIGN_QRGD:
                    vm.flow.businessMap.GD_ID = vm.fileRecord.fileRecordId;
                    break;
                default:
                    ;
            }

            return resultObj;
        }//E_checkBusinessFill

        //S_getChargeDispatch
        function getChargeDispatch(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/dispatch/html/initDispatchBySignId",
                params: {signId: vm.model.signid}
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.dispatchDoc = response.data;
                        $("#show_dispatch_a").click();
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_getChargeDispatch

        //S_getChargeFilerecord
        function getChargeFilerecord(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/fileRecord/html/initBySignId",
                params: {signId: vm.model.signid}
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.fileRecord = response.data;
                        $("#show_filerecord_a").click();
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_getChargeFilerecord

        //S_endSignDetail
        function endSignDetail(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/sign/html/initDetailPageData",
                params: {signid: vm.model.signid, queryAll: true}
            }

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.model = response.data;
                        if (vm.model.workProgramDtoList && vm.model.workProgramDtoList.length > 0) {
                            vm.show_workprogram = true;
                            vm.model.workProgramDtoList.forEach(function (w, index) {
                                if (w.isMain == 9) {
                                    vm.showMainwork = true;
                                    vm.mainwork = {};
                                    vm.mainwork = w;
                                } else if (w.isMain == 0) {
                                    vm.showAssistwork = true;
                                    vm.assistwork = {};
                                    vm.assistwork = w;
                                }
                            });
                        }
                        if (vm.model.dispatchDocDto) {
                            vm.show_dispatch = true;
                            vm.dispatchDoc = vm.model.dispatchDocDto;
                        }
                        if (vm.model.fileRecordDto) {
                            vm.show_filerecord = true;
                            vm.fileRecord = vm.model.fileRecordDto;
                        }
                    }
                })
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_endSignDetail

    }//E_signFlow
})();
(function () {
    'use strict';

    angular.module('app').controller('signFlowDealCtrl', sign);

    sign.$inject = ['sysfileSvc', 'signSvc', '$state', 'flowSvc', 'signFlowSvc','ideaSvc',
        '$http','bsWin'];

    function sign(sysfileSvc, signSvc, $state, flowSvc, signFlowSvc,ideaSvc, $http,bsWin) {
        var vm = this;
        vm.title = "项目流程处理";
        vm.model = {};          //收文对象
        vm.flow = {};           //流程对象
        vm.mainwork = {};       //主工作方案
        vm.assistwork = {};     //协工作方案
        vm.dispatchDoc = {};    //发文
        vm.fileRecord = {};     //归档
        vm.expertReview = {};   //评审方案

        //按钮显示控制，全部归为这个对象控制
        vm.showFlag = {
            businessTr:false,          //显示业务办理tr
            businessDis:false,         //显示直接发文复选框
            businessNext:false,        //显示下一环节处理人或者部门

            nodeNext : true,           //下一环节名称
            nodeSelViceMgr:false,      // 选择分管副主任环节
            nodeSelOrgs:false,         // 选择分管部门
            nodeSelPrincipal:false,    // 选择项目负责人
            isMainBranch:false,        // 选择第一负责人
            nodeSign:false,            // 项目签收
            nodeWorkProgram:false,     // 工作方案
            nodeDispatch:false,        // 发文
            nodeConfirmDis:false,      // 确认发文
            nodeCreateDisNum:false,    // 生成发文编号
            nodeFileRecord:false,      // 归档
            nodeSelXSOrg:false,        // 协审选择分管部门
            nodeSelXSPri:false,        // 选择负责人
            nodeXSWorkProgram:false,   // 协审工作方案

            tabWorkProgram:false,       // 显示工作方案标签tab
            tabBaseWP:false,            // 项目基本信息tab
            tabDispatch:false,          // 发文信息tab
            tabFilerecord:false,        // 归档信息tab
            tabExpert:false,            // 专家信息tab
            tabSysFile:false,           // 附件信息tab

            buttBack:false,             // 回退按钮
            expertRemark:false,         // 专家评分弹窗内容显示
            expertpayment:false,        // 专家费用弹窗内容显示
            expertEdit:true,            // 专家评分费用编辑权限
        };

        //业务控制对象
        vm.businessFlag = {
            isLoadSign : false,         // 是否加载收文信息
            isLoadFlow : false,         // 是否加载流程信息
            isGotoDis : false,          // 是否直接发文
            isMakeDisNum : false,       // 是否生成发文编号
            principalUsers : [],         // 负责人列表
            isSelMainPriUser:false,     // 是否已经设置主要负责人
            expertReviews : [],          // 专家评审方案
            editExpertSC : false,       // 编辑专家评审费和评分,只有专家评审方案环节才能编辑
            expertScore:{},              // 专家评分对象
            isNeedWP : 9,                // 是否需要工作方案
            isMainBranch : false,       // 是否是主分支流程
            isFinishWP : false,         // 是否完成了工作方案
            passDis:false,              // 发文是否通过
        }

        vm.model.signid = $state.params.signid;
        vm.flow.taskId = $state.params.taskId; // 流程任务ID
        vm.flow.processInstanceId = $state.params.processInstanceId; // 流程实例ID

        active();
        function active() {
            $('#myTab li').click(function (e) {
                var aObj = $("a", this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#" + showDiv).addClass("active").addClass("in").show(500);
            })
            // 初始化业务信息
            signSvc.initFlowPageData(vm.model.signid,function(data){
                vm.model = data;
                //有关联，则显示项目
                if(vm.model.isAssociate && vm.model.isAssociate == 1){
                    vm.showFlag.tabAssociateSigns = true;
                    signSvc.initAssociateSigns(vm,vm.model.signid);
                    //没有则初始化关联表格
                }
                //发文
                if (vm.model.dispatchDocDto) {
                    vm.showFlag.tabDispatch = true;
                    vm.dispatchDoc = vm.model.dispatchDocDto;
                    //如果是合并发文次项目，则不用生成发文编号
                    if((vm.dispatchDoc.dispatchWay == 2 && vm.dispatchDoc.isMainProject == 0)
                        || vm.dispatchDoc.fileNum){
                        vm.businessFlag.isCreateDisFileNum = true;
                    }else{
                        vm.showFlag.buttDisFileNum = true;
                    }
                }
                //归档
                if (vm.model.fileRecordDto) {
                    vm.showFlag.tabFilerecord = true;
                    vm.fileRecord = vm.model.fileRecordDto;
                }
                //初始化专家评分
                if (vm.model.processState > 2) {
                    signSvc.paymentGrid(vm);
                }
                //更改状态,并初始化业务参数
                vm.businessFlag.isLoadSign = true;
                if(vm.businessFlag.isLoadSign && vm.businessFlag.isLoadFlow){
                    signFlowSvc.initBusinessParams(vm);
                }
            });
            // 初始化流程数据
            flowSvc.getFlowInfo(vm.flow.taskId,vm.flow.processInstanceId,function(data){
                vm.flow = data;
                //如果是结束环节，则不显示下一环节信息
                if (vm.flow.end) {
                    vm.showFlag.nodeNext = false;
                }
                vm.businessFlag.isLoadFlow = true;
                //更改状态,并初始化业务参数
                if(vm.businessFlag.isLoadSign && vm.businessFlag.isLoadFlow){
                    signFlowSvc.initBusinessParams(vm);
                }
            });
            // 初始化办理信息
            flowSvc.initFlowData(vm);
            // 初始化上传附件
            signSvc.uploadFilelist(vm);

            ideaSvc.initIdea(vm);	//初始化个人常用意见
        }

        //检查项目负责人
        vm.checkPrincipal = function(){
            var selUserId = $("#selPrincipalMainUser").val();
            if(selUserId){
                $('#principalAssistUser input[selectType="assistUser"]').each(
                    function () {
                        var value = $(this).attr("value");
                        if (value == selUserId) {
                            $(this).removeAttr("checked");
                            $(this).attr("disabled", "disabled");
                        } else {
                            $(this).removeAttr("disabled");
                        }
                    }
                );
            }
        }

        // 编辑专家评分
        vm.editSelectExpert = function (id) {
            vm.businessFlag.expertScore = {};
            vm.businessFlag.expertReviews.forEach(function(epRw,index){
               epRw.expertSelectedDtoList.forEach(function(epSel,i){
                   if(epSel.id == id){
                       vm.businessFlag.expertScore = epSel;
                       return ;
                   }
               })
            });
            $("#star").raty({
                score: function () {
                    $(this).attr("data-num", angular.isUndefined(vm.businessFlag.expertScore.score)?0:vm.businessFlag.expertScore.score);
                    return $(this).attr("data-num");
                },
                starOn: '../contents/libs/raty/lib/images/star-on.png',
                starOff: '../contents/libs/raty/lib/images/star-off.png',
                starHalf: '../contents/libs/raty/lib/images/star-half.png',
                readOnly: false,
                halfShow: true,
                size: 34,
                click: function (score, evt) {
                    vm.businessFlag.expertScore.score = score;
                }
            });

            $("#expertmark").kendoWindow({
                width: "820px",
                height: "365px",
                title: "编辑-专家星级",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }
        // 关闭专家评分
        vm.closeEditMark = function () {
            window.parent.$("#expertmark").data("kendoWindow").close();
        }

        // 保存专家评分
        vm.saveMark = function () {
            flowSvc.saveMark(vm,function(){

            });
        }
        //获取专家评星
        vm.getExpertStar = function(id ,score){
            var returnStr = "";
            if (score != undefined) {
                for (var i = 0; i <score; i++) {
                    returnStr += "<span style='color:gold;font-size:20px;'><i class='fa fa-star' aria-hidden='true'></i></span>";
                }
            }
            $("#"+id+"_starhtml").html(returnStr);
        }

        vm.editpayment = function (id) {
            vm.expertReview.expertId = id;
            flowSvc.gotopayment(vm);
        }

        // 计算应纳税额
        vm.countTaxes = function (expertReview) {
            if(expertReview == undefined){
                return ;
            }
            if(expertReview.payDate == undefined){
                expertReview.errorMsg = "请选择发放日期";
                return ;
            }
            var reg = /^(\d{4}-\d{1,2}-\d{1,2})$/;
            if(!reg.exec(expertReview.payDate)){
                 expertReview.errorMsg = "请输入正确的日期格式";
                 return ;
            }
            expertReview.errorMsg = "";
            common.initJqValidation($('#payform'));
            var isValid = $('#payform').valid();
            if(isValid){
                 flowSvc.countTaxes(vm,expertReview);
            }


        }
        // 关闭专家费用
        vm.closeEditPay = function () {
            window.parent.$("#payment").data("kendoWindow").close();
        }
        // 保存专家费用
        vm.savePayment = function () {
            flowSvc.savePayment(vm,function(){
                 signSvc.paymentGrid(vm);
            });
        }

        // begin 管理个人意见
        vm.ideaEdit = function (options) {
            common.initIdeaData(vm, $http, options);
        }
      
        //选择个人意见触发事件
        vm.selectIdea=function(){
        	vm.flow.dealOption = vm.idea;
        }

        //流程提交
        vm.commitNextStep = function () {
            if(vm.flow.isSuspended){
                bsWin.error("该流程目前为暂停状态，不能进行流转操作！");
                return ;
            }
            var checkResult = signFlowSvc.checkBusinessFill(vm);
            if (checkResult.resultTag) {
                flowSvc.commit(vm.isCommit,vm.flow,function(data){
                    if (data.flag || data.reCode == "ok") {
                        bsWin.success("操作成功！",function(){
                            $state.go('gtasks');
                        })
                    }
                });
            } else {
                bsWin.alert(checkResult.resultMsg);
            }
        }

        vm.commitBack = function () {

            common.initJqValidation($("#flow_form"));
            var isValid = $("#flow_form").valid();
            if(isValid){
                bsWin.confirm({
                    title: "询问提示",
                    message: "确认回退吗？",
                    onOk: function () {
                        //有几个环节要传递业务参数
                        switch (vm.flow.curNode.activitiId) {
                            /************   以下是签收流程  **************/
                            case "FGLD_SP_GZFA1":
                                vm.flow.businessMap.M_WP_ID = vm.mainwork.id;       //部门负责人审批工作方案
                                break;
                            case "FGLD_SP_GZFA2":
                                vm.flow.businessMap.A_WP_ID = vm.assistwork.id;
                                break;
                            case "FGLD_SP_FW":                                      //分管领导审批发文
                                vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id;
                                break;
                            case "ZR_SP_FW":                                        //主任审批发文
                                vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id;
                                break;
                            case "BMLD_QR_GD":                                       //确认归档
                                vm.flow.businessMap.ExclusiveGateWay = true;         //注明是排他网关
                                break;
                            default:
                                ;
                        }
                        flowSvc.rollBackToLast(vm); // 回退到上一个环节
                    }
                });
            }
        }

        vm.deleteFlow = function () {
            bsWin.confirm({
                title: "询问提示",
                message: "终止流程将无法恢复，确认删除么？？",
                onOk: function () {
                    $('.confirmDialog').modal('hide');
                    flowSvc.deleteFlow(vm);
                }
            });
        }

        //编辑审批登记表
        vm.editSign = function(){
            $state.go('fillSign', {signid: vm.model.signid });
        }
        // S_跳转到 工作方案 编辑页面
        vm.addWorkProgram = function () {  
            $state.go('workprogramEdit', {signid: vm.model.signid});
        }// E_跳转到 工作方案 编辑页面
        
        //S_链接到拟补充资料函
        vm.addSuppLetter = function () {
        	console.log("id:"+vm.model.suppletterid);
        	$state.go('addSupp', {signid: vm.model.signid,id:vm.model.suppletterid });
        }// E_跳转到 拟补充资料函 编辑页面
        
        //S_链接到登记表补充资料
        vm.addRegisterFile = function () {
        	$state.go('registerFile', {signid: vm.model.signid});
        }// E_链接到登记表补充资料
        
        //S_跳转到 工作方案 基本信息
        vm.addBaseWP = function(){
            $state.go('workprogramBaseEdit', {signid: vm.model.signid });
        }

        // S_跳转到 发文 编辑页面
        vm.addDisPatch = function () {
            $state.go('dispatchEdit', {
                signid: vm.model.signid
            });
        }// E_跳转到 发文 编辑页面
        
        // S 财务报销
        vm.addFinancialApply = function(){
        	  $state.go('financialManager', {
                  signid: vm.model.signid
              });
        }
        // E 财务报销
        
        vm.addDoFile = function () {
            $state.go('fileRecordEdit', {
                signid: vm.model.signid
            });
        }

        // 业务判断
        vm.mainOrg = function ($event) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            var checkboxValue = checkbox.value;
            if (checked) {
                $('.seleteTable input[selectType="main"]').each(
                    function () {
                        var value = $(this).attr("value");
                        if(value != checkboxValue){
                            $(this).removeAttr("checked");
                            $("#assist_" + value).removeAttr("disabled");
                        }else{
                            $("#assist_" + checkboxValue).removeAttr("checked");
                            $("#assist_" + checkboxValue).attr("disabled", "disabled");
                        }
                    });

            } else {
                $("#assist_" + checkboxValue).removeAttr("disabled");
            }
            vm.initOption();
        }

        vm.initOption = function(){
            var selOrg = [];
            $('.seleteTable input[selectType="main"]:checked').each(function () {
                selOrg.push($(this).attr("tit"));
            });
            $('.seleteTable input[selectType="assist"]:checked').each(function () {
                selOrg.push($(this).attr("tit"));
            });
            if(selOrg.length > 0){
                vm.flow.dealOption = "请（"+selOrg.join('，')+"）组织评审";
            }
        }

        // checkbox 单选
        vm.checkBoxSingle = function ($event, type) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            var checkboxValue = checkbox.value;
            if (checked) {
                $('.seleteTable input[selectType=\"' + type + '\"]').each(function () {
                    var id = $(this).attr("id");
                    var value = $(this).attr("value");
                    if (id != (type + "_" + checkboxValue)) {
                        $("#" + disabletype + "_" + value).removeAttr("disabled");
                        $(this).removeAttr("checked");
                    } else {
                        $("#" + disabletype + "_" + checkboxValue).attr("disabled", "disabled");
                    }
                });
            } else {
                $("#" + disabletype + "_" + checkboxValue).removeAttr("disabled");
            }
        }

        //checkbox 单选
        vm.checkBoxSingle = function ($event, type) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            var checkboxValue = checkbox.value;
            if (checked) {
                $('#xs_table input[selectType=\"' + type + '\"]').each(function () {
                    var id = $(this).attr("id");
                    var value = $(this).attr("value");
                    if (value != checkboxValue) {
                        $(this).removeAttr("checked");
                    }
                });
            }
        }

        //项目关联弹窗
        vm.showAssociate = function(){
            vm.currentAssociateSign = vm.model;
            //选中要关联的项目
            var signAssociateWindow = $("#associateWindow");
            signAssociateWindow.kendoWindow({
                width: "60%",
                height: "625px",
                title: "项目关联",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "close"]
            }).data("kendoWindow").center().open();
            //初始化associateGrid
        }

        //start 保存项目关联
        vm.saveAssociateSign = function(associateSignId){
            if(vm.model.signid == associateSignId){
                bsWin.alert("不能关联自身项目");
                return ;
            }
            signSvc.saveAssociateSign(vm,vm.model.signid,associateSignId,function(){
                bsWin.alert(associateSignId != undefined ? "项目关联成功" : "项目解除关联成功");
                //回调
                $state.reload();
            });
        }
        //end 保存项目关联

        //start 查询功能
        vm.associateQuerySign = function(){
            vm.associateGridOptions.dataSource.read();
        }//end 查询功能

        //start 解除项目关联
        vm.disAssociateSign = function(){
            signSvc.saveAssociateSign(vm,vm.model.signid,null,function(){
                 //回调
                 $state.reload();
            });
        }
        //end 解除项目关联
        
        //选择负责人
        vm.addPriUser = function () {
            var isCheck = $("#xs_bmfb input[name='unSelPriUser']:checked");
            if (isCheck.length < 1) {
                common.alert({
                    vm: vm,
                    msg: "请选择负责人"
                })
            }else{
                if(vm.isMainPriUser == 9 && isCheck.length > 1){
                    common.alert({
                        vm: vm,
                        msg: "总负责人只能选一个"
                    })
                    return ;
                }
                if(vm.businessFlag.isSelMainPriUser == false && (angular.isUndefined(vm.isMainPriUser) || vm.isMainPriUser == 0)){
                    common.alert({
                        vm: vm,
                        msg: "请先选择总负责人！"
                    })
                    return ;
                }
                if(vm.businessFlag.isSelMainPriUser == true && vm.isMainPriUser == 9){
                    common.alert({
                        vm: vm,
                        msg: "你已经选择了一个总负责人，不能再次选择负责人"
                    })
                    return ;
                }
                if(vm.businessFlag.principalUsers && (vm.businessFlag.principalUsers.length + isCheck.length) > 3){
                    common.alert({
                        vm: vm,
                        msg: "最多只能选择3个负责人，请重新选择！"
                    })
                    return ;
                }

                for (var i = 0; i < isCheck.length; i++) {
                    var priUser = {};
                    priUser.userId = isCheck[i].value;
                    priUser.userType = $("#userType").val();
                    if(vm.isMainPriUser == 9){
                        vm.businessFlag.isSelMainPriUser = true;
                        priUser.isMainUser = 9;
                        vm.isMainPriUser == 0;
                    }else{
                        priUser.isMainUser = 0;
                    }
                    vm.xsusers.forEach(function(u,index){
                       if(u.id == isCheck[i].value){
                           u.isSelected = true;
                           priUser.userName = u.loginName;
                       }
                    });
                    vm.businessFlag.principalUsers.push(priUser);
                }
            }

        }

        //删除负责人
        vm.delPriUser = function () {
            var isCheck = $("#xs_bmfb input[name='selPriUser']:checked");
            if (isCheck.length < 1) {
                common.alert({
                    vm: vm,
                    msg: "请选择取消的负责人"
                })
            }else{
                for (var i = 0; i < isCheck.length; i++) {
                    vm.xsusers.forEach(function(u,index){
                        if(u.id == isCheck[i].value){
                            u.isSelected = false;
                        }
                    });
                    vm.businessFlag.principalUsers.forEach(function(pu,index){
                        if(pu.userId == isCheck[i].value){
                            if(pu.isMainUser == 9){
                                vm.businessFlag.isSelMainPriUser = false;
                            }
                            vm.businessFlag.principalUsers.splice(index,1);
                        }
                    });
                }
            }
        }//E_删除负责人

        //S_判断是否需要工作方案
        vm.checkNeedWP = function($event){
            var checkbox = $event.target;
            var checked = checkbox.checked;
            if(checked){
                vm.businessFlag.isNeedWP = 9;
            }else{
                //如果已经完成了工作方案，则询问是否要删除
                if(vm.businessFlag.isFinishWP){
                    bsWin.confirm({
                        title: "询问提示",
                        message: "取消工作方案操作将会对您之前的做的工作方案进行清除，数据清除不可恢复，确定不做工作么？",
                        onOk: function () {
                            $('.confirmDialog').modal('hide');
                            vm.businessFlag.isNeedWP = 0;
                            signSvc.removeWP(vm);
                        },
                        onClose:function(){
                            checkbox.checked = !checked;
                            vm.businessFlag.isNeedWP = 9;
                        }
                    });
                }else{
                    vm.businessFlag.isNeedWP = 0;
                }
            }
        }//E_判断是否需要工作方案

        //生产会前准备材料
        vm.meetingDoc = function(){
            common.confirm({
                vm:vm,
                title:"",
                msg:"如果之前已经生成会前准备材料，则本次生成的文档会覆盖之前产生的文档，确定执行操作么？",
                fn:function () {
                    $('.confirmDialog').modal('hide');
                    signSvc.meetingDoc(vm);
                }
            })
        }

        //附件下载
        vm.commonDownloadSysFile = function(sysFileId){
            sysfileSvc.commonDownloadFile(vm,sysFileId);
        }

        //生成发文字号
        vm.createDispatchFileNum = function(){
            signSvc.createDispatchFileNum(vm.model.signid,vm.dispatchDoc.id,function(data){
                if (data.flag || data.reCode == "ok") {
                    vm.dispatchDoc.fileNum = data.reObj;
                }
                bsWin.alert(data.reMsg);
            });
        }

    }
})();

(function () {
    'use strict';

    angular.module('app').controller('signFlowDetailCtrl', sign);

    sign.$inject = ['$location','signSvc','$state','flowSvc','signFlowSvc']; 

    function sign($location,signSvc,$state,flowSvc,signFlowSvc) {        
        var vm = this;
        vm.title = "项目流程信息";
        vm.model = {};
        vm.flow = {};					
        vm.work = {};
        vm.dispatchDoc = {};
        vm.fileRecord = {};
        
        vm.model.signid = $state.params.signid;	
        vm.flow.taskId = $state.params.taskId;			//流程任务ID
        vm.flow.processInstanceId = $state.params.processInstanceId;	//流程实例ID

        //按钮显示控制，全部归为这个对象控制
        vm.showFlag = {
            tabWorkProgram:false,       // 显示工作方案标签tab
            tabBaseWP:false,            // 项目基本信息tab
            tabDispatch:false,          // 发文信息tab
            tabFilerecord:false,        // 归档信息tab
            tabExpert:false,            // 专家信息tab
            tabSysFile:false,           // 附件信息tab
            tabAssociateSigns:false,    // 关联项目tab
        };

        //业务控制对象
        vm.businessFlag = {

        }

        active();
        function active(){
        	$('#myTab li').click(function (e) {
        		var aObj = $("a",this);        		
        		e.preventDefault();       		  
        		aObj.tab('show');      		
        		var showDiv = aObj.attr("for-div");   		
        		$(".tab-pane").removeClass("active").removeClass("in");
        		$("#"+showDiv).addClass("active").addClass("in").show(500);
        	})

        	//初始化流程信息        
        	flowSvc.initFlowData(vm);
        	//初始化业务信息
        	signSvc.initFlowPageData(vm);
        }

        //获取专家评星
        vm.getExpertStar = function(id ,score){
            var returnStr = "";
            if (score != undefined) {
                for (var i = 0; i <score; i++) {
                    returnStr += "<span style='color:gold;font-size:20px;'><i class='fa fa-star' aria-hidden='true'></i></span>";
                }
            }
            $("#"+id+"_starhtml").html(returnStr);
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('signReserveAddCtrl', sign);

    sign.$inject = ['$location','reserveSignSvc','$state']; 

    function sign($location, reserveSignSvc,$state) {        
        var vm = this;
    	vm.model = {};						//创建一个form对象
        vm.title = '新增预签收收文';        		//标题
        vm.reserveAdd = function () {
        	reserveSignSvc.reserveAdd(vm);
        };       
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('signReserveCtrl', sign);

    sign.$inject = ['$location','reserveSignSvc','$state']; 

    function sign($location, reserveSignSvc,$state) {        
        var vm = this;
    	vm.model = {};						//创建一个form对象
        vm.title = '预签收列表';        		//标题  
        
       vm.del = function(id){
    	   common.confirm({
          	 vm:vm,
          	 title:"",
          	 msg:"确认删除数据吗？",
          	 fn:function () {
                	$('.confirmDialog').modal('hide');             	
                	reserveSignSvc.deleteReserveSign(vm,id);
               }
           })
       }
       //查询
       vm.querySign = function (){
    	   reserveSignSvc.querySign(vm);
       }
       
        active();
        function active(){
        	reserveSignSvc.grid(vm);
        }
       
    }
})();

(function () {
    'use strict';

    angular.module('app').factory('reserveSignSvc', sign);

    sign.$inject = ['$http','$state'];

    function sign($http,$state) {
        var url_sign = rootPath + "/sign";
        var url_back = '#/sign';
        var service = {
            grid: grid,
            getsignById: getsignById,
            reserveAdd: reserveAdd,
            deleteReserveSign: deleteReserveSign,
            updatesign: updatesign,
            querySign: querySign,		//会议室查询
            roomUseState: roomUseState
        };

        return service;

        //S_查询grid
        function querySign(vm) {
            vm.gridOptions.dataSource.read();
        }//E_查询grid

        // begin#updateUser
        function updatesign(vm) {

            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id
                var httpOptions = {
                    method: 'put',
                    url: url_sign,
                    data: vm.model
                }

                var httpSuccess = function success(response) {

                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                }
                            })
                        }

                    })
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            } else {
                // common.alert({
                // vm:vm,
                // msg:"您填写的信息不正确,请核对后提交!"
                // })
            }

        }

        // begin#deleteUser
        function deleteReserveSign(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_sign +"/deleteReserve",
                params:{signid :id}

            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.isSubmit = false;
                        vm.gridOptions.dataSource.read();
                    }

                });

            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        function roomUseState(vm) {
            var httpOptions = {
                method: 'put',
                url: url_sign + "/roomUseState",
                data: vm.model
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.gridOptions.dataSource.read();
                    }

                });

            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#reserveAdd
        function reserveAdd(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                var httpOptions = {
                    method: 'post',
                    url: url_sign+"/html/reserveAddPost",
                    data: vm.model
                }

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                        	 common.alert({
                                 vm: vm,
                                 msg: "操作成功,请继续填写报审登记表！",
                                 closeDialog: true,
                                 fn: function () {
                                     //跳转并刷新页面
                                     $state.go('fillSign', {signid: response.data.signid}, {reload: true});
                                 }
                             })
                        }
                    });

                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess,
                    onError: function (response) {
                        vm.iscommit = false;
                    }
                });

            }
        }
        //end reserveAdd
        
        // begin#getUserById
        function getsignById(vm) {
            var httpOptions = {
                method: 'get',
                url: url_sign + "/html/findByIdsign",
                params: {id: vm.id}
            }
            var httpSuccess = function success(response) {
                vm.model = response.data;
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#grid
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_sign + "/reserveListSign", $("#reserveFrom")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            // End:dataSourc

            //S_序号
            var dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            }
            //S_序号

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />", item.signid)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"

                },
                {  
				    field: "rowNumber",  
				    title: "序号",  
				    width: 50,
				    filterable : false,
				    template: "<span class='row-number'></span>"  
				 },
                {
                    field: "projectname",
                    title: "项目名称",
                    width: 160,
                    filterable: false
                },
                {
                    field: "projectcode",
                    title: "收文编号",
                    width: 120,
                    filterable: false,
                },
                {
                    field: "designcompanyName",
                    title: "项目单位",
                    width: 150,
                    filterable: false,
                },
                {
                    field: "reviewstage",
                    title: "项目阶段",
                    width: 80,
                    filterable: false,
                },
                {
                    field: "projectcode",
                    title: "项目代码",
                    width: 120,
                    filterable: false,
                },
                {
                    field: "receivedate",
                    title: "收文时间",
                    width: 160,
                    filterable: false,
                    format: "{0:yyyy/MM/dd HH:mm:ss}"

                },
                {
                    field: "",
                    title: "流程状态",
                    width: 80,
                    filterable: false,
                    template: function (item) {
                        if (item.ispresign) {
                            if (item.ispresign == 0) {
                                return '<span style="color:green;">预签收</span>';
                            }
                        } else {
                            return " "
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 180,
                    template: function (item) {
                        return common.format($('#columnBtns').html(), item.signid,
                            "vm.del('" + item.signid + "')");
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound: dataBound,
                resizable: true
            };
        }// end fun grid


    }
})();
(function () {
    'use strict';

    angular.module('app').controller('signDetailsCtrl', sign);

    sign.$inject = ['$location','signSvc','$state','flowSvc'];

    function sign($location, signSvc,$state,flowSvc) {
        var vm = this;
    	vm.model = {};							//创建一个form对象   	
        vm.title = '查看详情信息';        			//标题
        vm.model.signid = $state.params.signid;	//收文ID
        vm.show_flow_info = false;				//显示流程图信息
       
        active();
        function active(){
            $('#myTab li').click(function (e) {
                var aObj = $("a",this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#"+showDiv).addClass("active").addClass("in").show(500);
            })

            signSvc.initFlowPageData(vm);

            signSvc.initAssociateSigns(vm,vm.model.signid);
            
            if(!angular.isUndefined($state.params.processInstanceId) && $state.params.processInstanceId != 'undefined'){           	
                vm.flow = {}
                vm.flow.processInstanceId = $state.params.processInstanceId;	//流程实例ID
                //判断是否加载流程图
                flowSvc.initFlowData(vm);
                vm.show_flow_info = true;
            }
        }

    }
})();

(function () {
    'use strict';

    angular.module('app').factory('signSvc', sign);

    sign.$inject = ['$http', '$state','bsWin'];

    function sign($http, $state,bsWin) {
        var service = {
            grid: grid,						//初始化项目列表
            querySign: querySign,			//查询
            createSign: createSign,			//新增
            initFillData: initFillData,		//初始化表单填写页面（可编辑）
            initDetailData: initDetailData,	//初始化详情页（不可编辑）
            updateFillin: updateFillin,		//申报编辑
            deleteSign: deleteSign,			//删除收文
            findOfficeUsersByDeptName: findOfficeUsersByDeptName,//根据协办部门名称查询用户
            initFlowPageData: initFlowPageData, //初始化流程收文信息
            removeWP: removeWP,             //删除工作方案
            associateGrid: associateGrid,//项目关联列表
            saveAssociateSign: saveAssociateSign,//保存项目关联
            initAssociateSigns: initAssociateSigns,//初始化项目关联信息
            paymentGrid: paymentGrid,           //专家评审费
            uploadFilelist: uploadFilelist,		//上传附件列表
            meetingDoc: meetingDoc,             //生成会前准备材
            createDispatchFileNum:createDispatchFileNum,    //生成发文字号
            realSign : realSign ,               //正式签收
        };
        return service;

        //S 上传附件列表
        function uploadFilelist(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/file/findBySysFileSignId",
                params: {
                    signid: vm.model.signid
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        if (response.data && response.data.length > 0) {
                            vm.sysFileDtoList = response.data;
                            vm.showFlag.tabSysFile = true;
                        }
                    }
                })
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //E 上传附件列表
        
        //S_初始化grid(过滤掉已经签收的项目)
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/sign/fingByOData", {filter: "issign eq (isNull,0)"},$("#searchform")),
                schema: common.kendoGridConfig().schema({
                    id: "signid",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            // End:dataSource
            //S_序号
            var  dataBound=function () {  
                var rows = this.items();  
                var page = this.pager.page() - 1;  
                var pagesize = this.pager.pageSize();  
                $(rows).each(function () {  
                    var index = $(this).index() + 1 + page * pagesize;  
                    var rowLabel = $(this).find(".row-number");  
                    $(rowLabel).html(index);  
                });  
            } 
            //S_序号
            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />", item.signid)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"

                },
                {  
				    field: "rowNumber",  
				    title: "序号",  
				    width: 50,
				    filterable : false,
				    template: "<span class='row-number'></span>"  
				 },
                {
                    field: "projectname",
                    title: "项目名称",
                    width: 160,
                    filterable: false
                },
                {
                    field: "projectcode",
                    title: "收文编号",
                    width: 120,
                    filterable: false,
                },
                {
                    field: "designcompanyName",
                    title: "项目单位",
                    width: 150,
                    filterable: false,
                },
                {
                    field: "reviewstage",
                    title: "项目阶段",
                    width: 80,
                    filterable: false,
                },
                {
                    field: "projectcode",
                    title: "项目代码",
                    width: 120,
                    filterable: false,
                },
                {
                    field: "receivedate",
                    title: "收文时间",
                    width: 160,
                    filterable: false,
                    format: "{0:yyyy/MM/dd HH:mm:ss}"

                },
                {
                    field: "",
                    title: "流程状态",
                    width: 80,
                    filterable: false,
                    template: function (item) {
                        if (item.signState) {
                            if (item.signState == 1) {
                                return '<span style="color:green;">进行中</span>';
                            } else if (item.signState == 2) {
                                return '<span style="color:orange;">已暂停</span>';
                            } else if (item.signState == 8) {
                                return '<span style="color:red;">强制结束</span>';
                            } else if (item.signState == 9) {
                                return '<span style="color:blue;">已完成</span>';
                            }else if (item.signState == 5) {
                                return '未发起';
                            }else{
                                return "";
                            }
                        } else {
                            return "未发起"
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 180,
                    template: function (item) {
                        var isStartFlow = angular.isString(item.processInstanceId);
                        var isRealSign = (item.issign && item.issign == 9)?true:false;

                        //如果已经发起流程，则只能查看
                        return common.format($('#columnBtns').html(), item.signid, false,
                            item.signid + "/" + item.processInstanceId, 
                            "vm.del('" + item.signid + "')", isStartFlow,
                            "vm.startNewFlow('" + item.signid + "')", isStartFlow,
                            "vm.realSign('" + item.signid + "')", isRealSign);
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound:dataBound,
                resizable: true
            };
        }//E_初始化grid

        //S_查询grid
        function querySign(vm) {
            vm.gridOptions.dataSource.read();
        }//E_查询grid

        //S_创建收文
        function createSign(model,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign",
                data: model
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_创建收文

        //start  根据协办部门查询用户
        function findOfficeUsersByDeptName(param, callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/officeUser/findOfficeUsersByDeptName",
                data: param
            };

            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //end  根据协办部门查询用户

        //Start 申报登记编辑
        function updateFillin(signObj,callBack) {
            common.initJqValidation($('#sign_fill_form'));
            var isValid = $('#sign_fill_form').valid();
            if (isValid) {
                var httpOptions = {
                    method: 'put',
                    url: rootPath + "/sign",
                    data: signObj,
                }
                var httpSuccess = function success(response) {
                    //关闭项目关联窗口
                    if (callBack != undefined && typeof callBack == 'function') {
                        callBack(response.data);
                    }
                }
                common.http({
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }
        //End 申报登记编辑

        //Start 删除收文
        function deleteSign(vm, signid) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/sign",
                params: {
                	signid:signid
                }
            }

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.isSubmit = false;
                        vm.gridOptions.dataSource.read();
                        common.alert({
                            vm: vm,
                            msg: "操作成功",                            
                        })
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //End 删除收文

        //S_初始化填报页面数据
        function initFillData(signid,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/html/initFillPageData",
                params: {
                    signid: signid
                }
            }
            var httpSuccess = function success(response) {
                //关闭项目关联窗口
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_初始化填报页面数据

        //S_初始化详情数据
        function initDetailData(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/sign/html/initDetailPageData",
                params: {signid: vm.model.signid}
            }

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.model = response.data;
                    }
                })
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_初始化详情数据

        //S_初始化流程页面
        function initFlowPageData(signId,callBack) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/sign/initFlowPageData",
                params: {
                    signid: signId,
                    queryAll: true
                }
            }

            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_初始化流程页面

        //S_removeWP
        function removeWP(vm) {
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/workprogram/deleteBySignId",
                params: {
                    signId: vm.model.signid
                }
            }
            var httpSuccess = function success(response) {
                vm.isSubmit = false;
                if ( response.data.flag || response.data.reCode == "ok") {
                    //更改状态
                    vm.businessFlag.isFinishWP = false;
                    bsWin.success("操作成功！");
                } else {
                    bsWin.error(response.data.reMsg);
                }
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_removeWP

        //associateGrid
        function associateGrid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/sign/fingByOData", {filter: "signid ne '"+vm.model.signid+"' and isAssociate eq 0 "}, $("#searchform")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            // End:dataSource

            // Begin:column
            var columns = [
                {
                    field: "projectname",
                    title: "项目名称",
                    width: 160,
                    filterable: false
                },
                {
                    field: "projectcode",
                    title: "收文编号",
                    width: 140,
                    filterable: false,
                },
                {
                    field: "designcompanyName",
                    title: "项目单位",
                    width: 200,
                    filterable: false,
                },
                {
                    field: "reviewstage",
                    title: "项目阶段",
                    width: 160,
                    filterable: false,
                },
                {
                    field: "projectcode",
                    title: "项目代码",
                    width: 140,
                    filterable: false,
                },
                {
                    field: "",
                    title: "操作",
                    width: 60,
                    template: function (item) {
                        return common.format($('#associateColumnBtns').html(), "vm.saveAssociateSign('" + item.signid + "')");
                    }
                }
            ];
            // End:column
            vm.associateGridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true
            };
            /*$("#associateGrid").kendoGrid({
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true
            });*/
            vm.associateGridOptions.dataSource.read();
        }//E_初始化associateGrid

        //start saveAssociateSign
        //如果associateSignId为空，解除关联
        function saveAssociateSign(vm, signId, associateSignId, callBack) {
            associateSignId = associateSignId == 'undefined' ? null : associateSignId;
            var httpOptions = {
                method: 'post',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                url: rootPath + "/sign/associate",
                data: $.param({signId: signId, associateId: associateSignId}, true),

            }
            var httpSuccess = function success(response) {
                //关闭项目关联窗口
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack();
                }
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //end saveAssociateSign

        //显示关联信息
        //start initAssociateSigns
        function initAssociateSigns(vm, singid) {

            var httpOptions = {
                method: 'get',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                url: rootPath + "/sign/associate?signId=" + singid,

            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        if (response.data != undefined) {
                            vm.associateSign = response.data;
                            var signs = response.data;
                            // console.log(signs);
                            var steps = [];
                            var html_ = '';
                            for (var i = (signs.length - 1); i >= 0; i--) {
                                var s = signs[i];
                                var signdate = s.signdate || '';
                                html_ += '<div class="intro-list">' +
                                    '<div class="intro-list-left">' +
                                    '项目阶段：' + s.reviewstage + "<br/>签收时间：" + signdate +
                                    '</div>' +
                                    '<div class="intro-list-right">' +
                                    '<span></span>' +
                                    '<div class="intro-list-content">' +
                                    '名称：<span style="color:red;">' + s.projectname + '</span><br/>' +
                                    '送件人：<span style="color:red;">' + s.sendusersign + '</span><br/>' +
                                    '</div>' +
                                    '</div>' +
                                    '</div>';

                            }
                            $('#introFlow').html(html_);
                            var step = $("#myStep").step({
                                animate: true,
                                initStep: 1,
                                speed: 1000
                            });

                        }
                    }
                });
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //end initAssociateSigns

        // begin#remarkGrid
        function paymentGrid(vm) {
            var signId = vm.model.signid;
            var url = rootPath + "/expertReview/html/getBySignId/" + signId;
            var httpOptions = {
                method: 'post',
                url: url
            }

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.businessFlag.expertReviews = response.data.value;
                        if (vm.businessFlag.expertReviews != undefined && vm.businessFlag.expertReviews.length > 0) {
                            vm.showFlag.tabExpert = true;   //显示专家信息tab
                        }
                    }
                })
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }// end fun grid

        //S_meetingDoc
        function meetingDoc(vm) {
            var wpId = "";
            switch (vm.flow.curNode.activitiId) {
                case "XMFZR_SP_GZFA1":
                    if (!angular.isUndefined(vm.mainwork) && !angular.isUndefined(vm.mainwork.id) && vm.mainwork.id) {
                        wpId = vm.mainwork.id;
                    }
                    break;
                case "XMFZR_SP_GZFA2":
                    if (!angular.isUndefined(vm.assistwork) && !angular.isUndefined(vm.assistwork.id) && vm.assistwork.id) {
                        wpId = vm.assistwork.id;
                    }
                    break;
                    
                case "XS_XMFZR_GZFA":
                    if (!angular.isUndefined(vm.mainwork) && !angular.isUndefined(vm.mainwork.id) && vm.mainwork.id) {
                        wpId = vm.mainwork.id;
                    }
            }
            if (wpId) {
                var httpOptions = {
                    method: 'get',
                    url: rootPath + "/workprogram/createMeetingDoc",
                    params: {
                        signId: vm.model.signid,
                        workprogramId: wpId
                    }
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: response.data.reMsg,
                                closeDialog: true,
                                fn: function () {
                                    if (response.data.reCode == "error") {
                                        vm.isCommit = false;
                                    } else {
                                        uploadFilelist(vm);
                                    }
                                }
                            })
                        }
                    })
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            } else {
                common.alert({
                    vm: vm,
                    msg: "请先填写工作方案信息！"
                })
            }
        }//E_meetingDoc
        
      
  //S_createDispatchFileNum
        function createDispatchFileNum(signId,dispatchId,callBack){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/dispatch/createFileNum",
                params: {
                    signId : signId,
                    dispatchId: dispatchId
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_createDispatchFileNum

        //S_项目正式签收
        function realSign(vm,signid){
            vm.isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/realSign",
                params:{
                    signid : signid
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.isCommit = false;
                        common.alert({
                            vm: vm,
                            msg: response.data.reMsg,
                            closeDialog: true,
                            fn: function () {
                                if (response.data.reCode == "error") {

                                } else {
                                    vm.gridOptions.dataSource.read();
                                }
                            }
                        })
                    }
                })
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError : function () {
                    vm.isCommit = false;
                }
            });
        }//E_realSign


    }
})();
(function () {
    'use strict';

    /**
     * 项目业务类型
     */
    var signBusinessType = {
        PD : "PD",      //评估
        GD : "GD",      //概算
    }

    /**
     * 默认办理人员
     */
    var signDealUser = {
        ZHANG_YF : "张一帆",      //评估
        LI_J : "李军",      //概算
    }

    /**
     * 流程参数
     */
    var signFlowParams = {
        godispatch : "godispatch",          //直接发文
        principaluser : "principaluser",    //项目负责人
        seconduser : "seconduser",           //第二负责人审核
    }

    var service = {
        getBusinessType: getBusinessType,               // 获取项目类型
        getDefaultLeader : getDefaultLeader,            // 获取默认办理的分管主任
        getDefaultZHBYJ:getDefaultZHBYJ,                // 获取默认综合部审批意见
        getFlowParams : getFlowParams,                  // 获取流程参数
        initSuppData : initSuppData,                    // 初始化项目资料补充函
        saveSuppletter : saveSuppletter,                // 保存项目资料补充函
        registerFilePrint : registerFilePrint,          // 转到登记资料打印页面
        initPrintData : initPrintData,                  // 初始化登记资料打印页面数据
        kendoGridinlineConfig : kendoGridinlineConfig,  // 行内编辑方法
    };
    window.signcommon = service;

    //S_获取项目类型
    function getBusinessType() {
        return signBusinessType;
    }//E_getBusinessType

    //S_获取默认办理的分管主任
    function getDefaultLeader(businessType){
        var resultName = "";
        switch(businessType){
            case signBusinessType.PD:
                resultName = signDealUser.ZHANG_YF
                break;
            case signBusinessType.GD:
                resultName = signDealUser.LI_J
                break;
            default:
                ;
        }
        return resultName;
    }//E_getDefaultLeader

    //S_获取默认综合部审批意见
    function getDefaultZHBYJ(businessType){
        var resultOption = "";
        switch(businessType){
            case signBusinessType.PD:
                resultOption = "请张主任阅示。";
                break;
            case signBusinessType.GD:
                resultOption = "请李主任阅示。";
                break;
            default:
                ;
        }
        return resultOption;
    }//E_getDefaultZHBYJ

    //S_获取流程参数
    function getFlowParams() {
        return signFlowParams;
    }//E_getFlowParams

    //S_判断评审阶段
    function isCommonStage(reviewstage){
        if(reviewstage=='项目建议书' || reviewstage=='可行性研究报告' ||reviewstage=='提前介入' || reviewstage=='项目概算' || reviewstage=='其它' || reviewstage=='资金申请报告'){
            return true;
        }else{
            return false;
        }
    }//E_isCommonStage

    // 初始化项目补充资料函
    function initSuppData(vm, options) {
        // options.$state.go('addSupp', {signid:
        // vm.model.signid,id:vm.model.suppletterid })
        // options.url;
        options.$http({
            method : 'get',
            url : rootPath + "/addSuppLetter",
            headers : {
                "contentType" : "application/json;charset=utf-8" // 设置请求头信息
            },
            dataType : "json",
            params : {
                signid : options.$state.params.signid,
                id : options.$state.params.id
            }
        }).then(function(response) {
            vm.suppletter = response.data;
        });

        vm.saveSuppletter = function() {
            if (vm.suppletter.id) {// 更新
                updateSuppletter(vm, options);
            } else {
                saveSuppletter(vm, options);// 保存
            }
        }
        if (vm.suppletter.id) {
            getAddsuppletterbyId(vm, options);// 根据id获取数据
        }
        /*
         * $("#suppWin").kendoWindow({ width: "50%", height: "80%", title:
         * "意见选择", visible: false, modal: true, closable: true, actions: ["Pin",
         * "Minimize", "Maximize", "close"]
         * }).data("kendoWindow").center().open();
         */
    }

    function saveSuppletter(vm, options) {
        options.$http({
            method : 'post',
            url : rootPath + "/addSuppLetter/add",
            headers : {
                "contentType" : "application/json;charset=utf-8" // 设置请求头信息
            },
            dataType : "json",
            data : vm.suppletter
        }).then(function(response) {
            alert("保存成功！");
        });
    }// end

    function updateSuppletter(vm, options) {
        options.$http({
            method : 'post',
            url : rootPath + "/addSuppLetter/update",
            headers : {
                "contentType" : "application/json;charset=utf-8" // 设置请求头信息
            },
            dataType : "json",
            data : vm.suppletter
        }).then(function(response) {
            alert("更新成功！");
        });
    }// end

    function getAddsuppletterbyId(vm, options) {
        options.$http({
            method : 'get',
            url : rootPath + "/addSuppLetter/getbyId",
            headers : {
                "contentType" : "application/json;charset=utf-8" // 设置请求头信息
            },
            dataType : "json",
            params : {id:vm.suppletter.id}
        }).then(function(response) {
            vm.suppletter = response.data;
        });
    }

    function registerFilePrint(vm, options) {
        vm.showPrint = true;
        var registerWin = $("#printWindow");
        registerWin.kendoWindow({
            width : "50%",
            height : "60%",
            title : "打印登记补充资料",
            visible : false,
            modal : true,
            closable : true,
            actions : ["Pin", "Minimize", "Maximize", "close"]
        }).data("kendoWindow").center().open();

        initPrintData(vm, options);
    }

    function initPrintData(vm, options) {
        options.$http({
            method : 'post',
            url : rootPath + "/addRegisterFile/initprintdata",
            headers : {
                "contentType" : "application/json;charset=utf-8" // 设置请求头信息
            },
            dataType : "json",
            params : {
                signid : vm.model.signid
            }
        }).then(function(response) {
            vm.registerFileList = response.data.AddRegisterFileDtoList;
            vm.signdto = response.data.signdto;
            vm.printDate = response.data.printDate;

        });
    }

    function kendoGridinlineConfig() {
        return {
            filterable : {
                extra : false,
                // mode: "row", 将过滤条件假如title下,如果不要直接与title并排
                operators : {
                    string : {
                        "contains" : "包含",
                        "eq" : "等于"
                        // "neq": "不等于",
                        // "doesnotcontain": "不包含"
                    },
                    number : {
                        "eq" : "等于",
                        "neq" : "不等于",
                        gt : "大于",
                        lt : "小于"
                    },
                    date : {
                        gt : "大于",
                        lt : "小于"
                    }
                }
            },
            pageable : {
                pageSize : 10,
                previousNext : true,
                buttonCount : 5,
                refresh : true,
                pageSizes : true
            },
            schema : function(model) {
                return {
                    data : "value",
                    total : function(data) {
                        return data['count'];
                    },
                    model : model
                };
            },
            transport : function(vm, url, paramObj) {
                return {
                    read : {
                        url : url.readUrl,
                        dataType : "json",
                        type : "post",
                        beforeSend : function(req) {
                            req.setRequestHeader('Token', service.getToken());
                        }
                    },
                    update : {
                        url : url.updateUrl,
                        dataType : "json",
                        type : "post"
                    },
                    destroy : {
                        url : url.destroyUrl,
                        dataType : "json",
                        type : "post",
                        contentType : "application/json"
                    },
                    create : {
                        url : url.createUrl,
                        dataType : "json",
                        type : "post",
                        contentType : "application/json"
                    },
                    parameterMap : function(options, operation) {
                        if (operation == "update"|| operation == "destroy" ||operation == "create") {
                            return kendo.stringify(options.models);
                        } else if (operation == "read") {
                            return {
                                "$filter" : paramObj.filter,
                                "$skip" : options.skip,
                                "$top" : options.take,
                                "$inlinecount" : "allpages"
                            }
                        }
                    }
                }
            },
            noRecordMessage : {
                template : '暂时没有数据.'
            }
        }
    }

})();


(function () {
    'use strict';

    angular.module('app').controller('sysConfigCtrl', sysConfig);

    sysConfig.$inject = ['$location', 'sysConfigSvc'];

    function sysConfig($location, sysConfigSvc) {
        var vm = this;
        vm.model = {};      // 参数对象
        vm.title = '系统配置';

        activate();
        function activate() {
            sysConfigSvc.queryList(vm);
        }

        //新增参数
        vm.addConfig = function () {
            vm.model = {};
            //显示次项目窗口
            $("#configdiv").kendoWindow({
                width: "700px",
                height: "440px",
                title: "参数编辑",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //关闭窗口
        vm.closeWin = function () {
            window.parent.$("#configdiv").data("kendoWindow").close();
        }

        //保存参数
        vm.doCommit = function () {
            common.initJqValidation();
            var isValid = $('#configForm').valid();
            if (isValid) {
                sysConfigSvc.saveConfig(vm);
            }
        }

        //编辑参数
        vm.editConfig = function (id) {
            vm.configList.forEach(function (c, index) {
                if (c.id == id) {
                    vm.model = c;
                }
            });
            //显示次项目窗口
            $("#configdiv").kendoWindow({
                width: "700px",
                height: "440px",
                title: "参数编辑",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //删除参数
        vm.del = function (ids) {
            var checkSign = $("input[name='configid']:checked");
            if (checkSign.length < 1) {
                common.alert({
                    vm: vm,
                    msg: "请选择删除的参数"
                })
            } else {
                common.confirm({
                    vm: vm,
                    title: "",
                    msg: "确认删除数据吗？",
                    fn: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < checkSign.length; i++) {
                            ids.push(checkSign[i].value);
                        }
                        sysConfigSvc.deleteConfig(vm, ids.join(","));
                    }
                })
            }
        }

    }//E_sysConfig
})();

(function () {
    'use strict';

    angular.module('app').factory('sysConfigSvc', sysConfig);

    sysConfig.$inject = ['$http'];

    function sysConfig($http) {
        var service = {
            queryList : queryList,			        //初始化表格
            deleteConfig : deleteConfig,            //删除参数
            saveConfig : saveConfig,                //保存系统参数

        };
        return service;

        //S_queryList
        function queryList(vm) {
            var httpOptions = {
                method : 'get',
                url : rootPath+"/sysConfig/queryList",
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm:vm,
                    response:response,
                    fn:function() {
                        vm.configList = new Array();
                        vm.configList = response.data;
                    }
                });
            }
            common.http({
                vm:vm,
                $http:$http,
                httpOptions:httpOptions,
                success:httpSuccess
            });
        }//E_queryList

        //S_deleteConfig
        function deleteConfig(vm,ids){
            var httpOptions = {
                method : 'delete',
                url : rootPath+"/sysConfig",
                params :{id:ids}
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm:vm,
                    response:response,
                    fn:function() {
                        common.alert({
                            vm:vm,
                            msg:"操作成功",
                            fn:function(){
                                $('.alertDialog').modal('hide');
                                $('.modal-backdrop').remove();
                                vm.isSubmit=false;
                                queryList(vm);

                            }
                        })
                    }
                });
            }
            common.http({
                vm:vm,
                $http:$http,
                httpOptions:httpOptions,
                success:httpSuccess
            });
        }//E_deleteConfig

        //S_saveConfig
        function saveConfig(vm){
            var httpOptions = {
                method : 'post',
                url : rootPath+"/sysConfig",
                data :vm.model
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm:vm,
                    response:response,
                    fn:function() {
                        common.alert({
                            vm:vm,
                            msg:"操作成功",
                            fn:function(){
                                $('.alertDialog').modal('hide');
                                $('.modal-backdrop').remove();
                                vm.isSubmit=false;
                                queryList(vm);
                            }
                        })
                    }
                });
            }
            common.http({
                vm:vm,
                $http:$http,
                httpOptions:httpOptions,
                success:httpSuccess
            });
        }//E_saveConfig

    }//E_sysConfig

})();
(function () {
    'use strict';

    angular.module('app').controller('pluginfileCtrl', pluginfile);

    pluginfile.$inject = ['$location', 'sysfileSvc'];

    function pluginfile($location, sysfileSvc) {
        var vm = this;
        vm.title = '系统安装包列表';

        activate();
        function activate() {
            sysfileSvc.queryPluginfile(vm);
        }


    }//E_sysConfig
})();

(function () {
    'use strict';

    angular.module('app').factory('sysfileSvc', sysfile);

    sysfile.$inject = ['$http'];
    function sysfile($http) {
        var service = {
            initUploadOptions: initUploadOptions,       // 初始化上传附件控件
            commonDelSysFile: commonDelSysFile,         // 删除系统文件
            commonDownloadFile: commonDownloadFile,     // 系统文件下载
            queryPluginfile :queryPluginfile,           // 查询系统安装包
        };
        return service;


        // 系统文件下载
        function commonDownloadFile(vm, id) {
            var sysfileId = id;
            window.open(rootPath + "/file/fileDownload?sysfileId=" + id);
        }

        // S 删除系统文件
        function commonDelSysFile(vm, id) {
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/file/deleteSysFile",
                params: {
                    id: id
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        window.parent.$("#commonQueryWindow").data("kendoWindow").close();
                        common.alert({
                            vm: vm,
                            msg: "删除成功",
                            closeDialog : true
                        })
                    }

                });

            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        // E 删除系统文件

        // S 初始化上传附件控件
        /**
         * options 属性
         *  businessId: 业务ID    （必须）
         *  sysSignId: 收文ID,
         *  sysfileType: 模块,
         *  sysMinType: 文件类型,
         *  uploadBt : 上传按钮
         *  detailBt : 查看按钮
         * @param options
         */
        function initUploadOptions(options) {
            var sysFileDefaults = {
                width: "800px",
                height: "600px",
                sysMinType: "",
                sysfileType: "",
                sysSignId: "",
                uploadBt : "upload_file_bt",
                detailBt : "detail_file_bt",
                vm  : options.vm,
            };
            if(options.businessId){
                sysFileDefaults.businessId = options.businessId;
            }
            if (options.width) {
                sysFileDefaults.width = options.width;
            }
            if (options.height) {
                sysFileDefaults.height = options.height;
            }
            if (options.sysSignId) {
                sysFileDefaults.sysSignId = options.sysSignId;
            }
            if (options.sysfileType) {
                sysFileDefaults.sysfileType = options.sysfileType;
            }
            if (options.sysMinType) {
                sysFileDefaults.sysMinType = options.sysMinType;
            }
            //附件下载方法
            sysFileDefaults.vm.downloadSysFile = function(id){
                window.open(rootPath + "/file/fileDownload?sysfileId=" + id);
            }
            //附件删除方法
            sysFileDefaults.vm.delSysFile = function(id){
                var httpOptions = {
                    method: 'delete',
                    url: rootPath + "/file/deleteSysFile",
                    params: {
                        id: id
                    }
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: sysFileDefaults.vm,
                        response: response,
                        fn: function () {
                            window.parent.$("#commonQueryWindow").data("kendoWindow").close();
                            common.alert({
                                vm: sysFileDefaults.vm,
                                msg: "删除成功",
                                closeDialog : true
                            })
                        }
                    });
                }
                common.http({
                    vm: sysFileDefaults.vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }

            if (options.uploadBt) {
                sysFileDefaults.uploadBt = options.uploadBt;
                $("#"+sysFileDefaults.uploadBt).click(function(){
                	if(angular.isUndefined(options.businessId)){
                		common.alert({
                            vm: sysFileDefaults.vm,
                            msg: "请先保存业务数据！",
                            closeDialog : true
                        })
                	}else{
                		$("#commonuploadWindow").kendoWindow({
                            width: sysFileDefaults.width,
                            height: sysFileDefaults.height,
                            title: "附件上传",
                            visible: false,
                            modal: true,
                            closable: true,
                            actions: ["Pin", "Minimize", "Maximize", "Close"]
                        }).data("kendoWindow").center().open();
                	}            
                });
            }
            if (options.detailBt) {
                sysFileDefaults.detailBt = options.detailBt;
                $("#"+sysFileDefaults.detailBt).click(function(){
                	if(angular.isUndefined(options.businessId)){
                		common.alert({
                            vm: sysFileDefaults.vm,
                            msg: "请先保存业务数据！",
                            closeDialog : true
                        })
                	}else{
                		var httpOptions = {
                            method: 'get',
                            url: rootPath + "/file/findByBusinessId",
                            params: {
                                businessId: sysFileDefaults.businessId
                            }
                        }
                        var httpSuccess = function success(response) {
                            sysFileDefaults.vm.sysFilelists = response.data;
                            $("#commonQueryWindow").kendoWindow({
                                width: "800px",
                                height: "500px",
                                title: "附件上传列表",
                                visible: false,
                                modal: true,
                                closable: true,
                                actions: ["Pin", "Minimize", "Maximize", "Close"]
                            }).data("kendoWindow").center().open();
                        }
                        common.http({
                            vm: sysFileDefaults.vm,
                            $http: $http,
                            httpOptions: httpOptions,
                            success: httpSuccess
                        });
                	}                   
                });
            }
            //有业务数据才能初始化
            if(sysFileDefaults.businessId){
                var projectfileoptions = {
                    language: 'zh',
                    allowedPreviewTypes: ['image'],
                    allowedFileExtensions: ['jpg', 'png', 'gif', "xlsx", "docx", "doc", "xls", "pdf","ppt","zip","rar"],
                    maxFileSize: 2000,
                    showRemove: false,
                    uploadUrl: rootPath + "/file/fileUpload",
                    uploadExtraData: function(previewId, index) {
                        var result={};
                        result.businessId=sysFileDefaults.businessId;
                        result.sysSignId=sysFileDefaults.sysSignId;
                        result.sysfileType=sysFileDefaults.sysfileType;
                        result.sysMinType=$("#sysMinType option:selected").val();
                        return result;
                        // businessId: sysFileDefaults.businessId,
                        // sysSignId: sysFileDefaults.sysSignId,
                        // sysfileType: sysFileDefaults.sysfileType,
                        // sysMinType: sysFileDefaults.sysMinType
                    }
                };
                $("#sysfileinput").fileinput(projectfileoptions)
                    .on("filebatchselected", function (event, files) {

                    }).on("fileuploaded", function (event, data) {
                        console.log(555);
                    projectfileoptions.sysMinType=$("#sysMinType").val();
                });
            }

        }
        // E 初始化上传附件控件

        // S 系统安装包管理
        function queryPluginfile(vm){
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/file/getPluginFile"),
                schema: common.kendoGridConfig().schema(),
                serverPaging: false,
                serverSorting: true,
                serverFiltering: true,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            // End:dataSource

            // Begin:column
            var columns = [
                {
                    field: "fileName",
                    title: "名称",
                    filterable : false
                },
                {
                    field: "fileLength",
                    title: "大小",
                    width: 160,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 150,
                    template: function (item) {
                        return common.format($('#columnBtns').html(), rootPath+"/"+item.relativePath);
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true
            };
        }// E queryPluginfile

    }
})();
(function () {
    'use strict';

    angular.module('app').controller('userCtrl', user);

    user.$inject = ['$location', 'userSvc'];

    function user($location, userSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '用户列表';

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    userSvc.deleteUser(vm, id);
                }
            });
        }
        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: '请选择数据'
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.del(idStr);
            }
        };
        //查询
        vm.queryUser = function(){
        	userSvc.queryUser(vm);
        }
        activate();
        function activate() {
            userSvc.grid(vm);
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('userEditCtrl', user);

    user.$inject = ['$location', 'userSvc', '$state'];

    function user($location, userSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '新增用户';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '编辑用户';
        }

        vm.create = function () {
            userSvc.createUser(vm);
        };

        vm.update = function () {
            userSvc.updateUser(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                userSvc.getUserById(vm);
            } else {
                userSvc.initZtreeClient(vm);
                userSvc.initUserNo(vm);
            }
            userSvc.getOrg(vm);
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').factory('userSvc', user);

    user.$inject = ['$http'];

    function user($http) {
        var url_user = rootPath + "/user", url_back = '#/user', url_role = rootPath + "/role/fingByOData",
            url_dictgroup = rootPath + "/dict";
        var service = {
            grid: grid,
            getUserById: getUserById,
            initZtreeClient: initZtreeClient,
            createUser: createUser,
            deleteUser: deleteUser,
            updateUser: updateUser,
            getOrg: getOrg,
            queryUser: queryUser,
            initUserNo : initUserNo//初始化 员工工号
        };

        return service;
        
        //begin initUserNo
        function initUserNo(vm){
        
        	var httpOptions={
        		method : "get",
        		url : url_user +"/createUserNo"
        	}
        	
        	var httpSuccess=function success(response){
        	
        		vm.model={};
        		var userNo=response.data;
        		vm.model.userNo=userNo.substring(1,userNo.length-1);
        	}
         common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
        	
        }//end initUserNo

        // begin#updateUser
        function updateUser(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

                // zTree
                var nodes = getZtreeChecked();
                var nodes_role = $linq(nodes).where(function (x) {
                    return x.isParent == false;
                }).select(function (x) {
                    return {
                        id: x.id,
                        roleName: x.name
                    };
                }).toArray();
                vm.model.roleDtoList = nodes_role;

                var httpOptions = {
                    method: 'put',
                    url: url_user,
                    data: vm.model
                }

                var httpSuccess = function success(response) {

                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {

                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                }
                            })
                        }

                    })
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            } else {
                // common.alert({
                // vm:vm,
                // msg:"您填写的信息不正确,请核对后提交!"
                // })
            }

        }

        // begin#deleteUser
        function deleteUser(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_user,
                data: id

            }
            var httpSuccess = function success(response) {

                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.isSubmit = false;
                        vm.gridOptions.dataSource.read();
                    }

                });

            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#createUser
        function createUser(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;

                // zTree
                var nodes = getZtreeChecked();
                var nodes_roles = $linq(nodes).where(function (x) {
                    return x.isParent == false;
                }).select(function (x) {
                    return {
                        id: x.id,
                        roleName: x.name
                    };
                }).toArray();
                vm.model.roleDtoList = nodes_roles;

                var httpOptions = {
                    method: 'post',
                    url: url_user,
                    data: vm.model
                }

                var httpSuccess = function success(response) {

                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {

                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                    location.href = url_back;
                                }
                            })
                        }

                    });

                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

            }
        }

        //获取部门信息
        function getOrg(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/org/listAll",
            }
            var httpSuccess = function success(response) {
                vm.org = {};
                vm.org = response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#initZtreeClient
        function initZtreeClient(vm) {
            var httpOptions = {
                method: 'post',
                url: url_role = rootPath + "/role/findAllRoles"
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        var zTreeObj;
                        var setting = {
                            check: {
                                chkboxType: {
                                    "Y": "ps",
                                    "N": "ps"
                                },
                                enable: true
                            }
                        };
                        var zNodes = $linq(response.data).select(
                            function (x) {
                                return {
                                    id: x.id,
                                    name: x.roleName
                                };
                            }).toArray();
                        var rootNode = {
                            id: '',
                            name: '角色集合',
                            children: zNodes
                        };
                        zTreeObj = $.fn.zTree.init($("#zTree"), setting, rootNode);
                        if (vm.isUpdate) {
                            updateZtree(vm);
                        }
                    }

                });

            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#getUserById
        function getUserById(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/user/findUserById",
                params: {
                    userId: vm.id
                }
            }
            var httpSuccess = function success(response) {
                vm.model = response.data;
                if (vm.isUpdate) {
                    initZtreeClient(vm);
                }
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        // begin#grid
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_user + "/fingByOData?$orderby=userSort", $("#usersform")),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                        createdDate: {
                            type: "date"
                        },
                        modifiedDate: {
                            type: "date"
                        }
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            // End:dataSource

            //S_序号
            var dataBound = function () {
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
            }
            //S_序号
            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo
                            .format(
                                "<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                                item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"

                },
                {
                    field: "rowNumber",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                }
                ,
                {
                    field: "loginName",
                    title: "登录名",
                    width: 100,
                    filterable: false
                },
                {
                    field: "displayName",
                    title: "显示名",
                    width: 100,
                    filterable: false
                },
                {
                    field: "orgDto.name",
                    title: "所属部门",
                    width: 100,
                    filterable: false
                },
                {
                    field: "userIP",
                    title: "登录IP",
                    width: 160,
                    filterable: false
                },
                {
                    field: "lastLogin",
                    title: "最后登录时间",
                    width: 160,
                    filterable: false
                },
                {
                    field: "",
                    title: "所属角色",
                    width: 160,
                    filterable: false,
                    template: function (item) {
                        if (item.roleDtoList) {
                            var resultStr = "";
                            for (var i = 0, l = item.roleDtoList.length; i < l; i++) {
                                if (i == 0) {
                                    resultStr += item.roleDtoList[i].roleName
                                } else {
                                    resultStr += ", " + item.roleDtoList[i].roleName;
                                }
                            }
                            return resultStr;
                        }
                        else {
                            return " ";
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.del('" + item.id + "')", item.id);

                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound: dataBound,
                resizable: true
            };

        }// end fun grid

        //查询
        function queryUser(vm) {
            vm.gridOptions.dataSource.read();
        }

        // begin common fun
        function getZtreeChecked() {
            var treeObj = $.fn.zTree.getZTreeObj("zTree");
            var nodes = treeObj.getCheckedNodes(true);
            return nodes;
        }

        function updateZtree(vm) {
            var treeObj = $.fn.zTree.getZTreeObj("zTree");
            var checkedNodes = $linq(vm.model.roleDtoList).select(function (x) {
                return x.roleName;
            }).toArray();
            var allNodes = treeObj.getNodesByParam("level", 1, null);

            var nodes = $linq(allNodes).where(function (x) {
                return $linq(checkedNodes).contains(x.name);
            }).toArray();

            for (var i = 0, l = nodes.length; i < l; i++) {
                treeObj.checkNode(nodes[i], true, true);
            }
        }

        // end common fun
    }
})();
(function () {
    'use strict';

    angular.module('app').controller('workdayCtrl', workday);

    workday.$inject = ['$location', 'workdaySvc'];

    function workday($location, workdaySvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '工作日列表';
        
        activate();
        function activate() {
        	workdaySvc.grid(vm);
        }
        
        vm.del=function(id){
        	common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    workdaySvc.deleteWorkday(vm, id);
                }
            });
        }
        
         vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: '请选择数据'
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.del(idStr);
            }
        };
        
        
        vm.queryWorkday=function(){
        	workdaySvc.queryWorkday(vm);
        }
        
        vm.ResetWorkday=function(){
        	workdaySvc.clearValue(vm);
        }
    }
})();

(function() {
	'use strict';
	
	angular.module('app').factory('workdaySvc', workday);
	
	workday.$inject = ['$http','$state'];

	function workday($http,$state) {
		
		var url_workday=rootPath+'/workday';
		var url_back="#/workday"
		var service = {
			grid : grid,	//初始化数据
			createWorkday : createWorkday,	//新增工作日
			getWorkdayById : getWorkdayById,	//通过id查找该对象信息
			updateWorkday : updateWorkday,		//更新
			deleteWorkday : deleteWorkday,		//删除
			queryWorkday : queryWorkday,		//模糊查询
			clearValue : clearValue		//重置
			
			
		}
		
		return service;	
		
		//begin clearValue
		function clearValue(vm){
		var tab = $("#workdayForm").find('input,select');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
			
		vm.gridOptions.dataSource.read();
		}
		
		//begin getWorkdayById
		function getWorkdayById(vm){
		
			var httpOptions={
				method:'get',
				url :url_workday+"/getWorkdayById",
				params:{id:vm.id}
			}
			
			var httpSuccess=function success(response){
				vm.workday=response.data;
			}
			
			 common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
		}//end getWorkdayById
		
		function queryWorkday(vm){
			vm.gridOptions.dataSource.read();
		}
		
		//begin createWorkday
		function createWorkday(vm){
			var httpOptions={
				method :'post',
				url : url_workday+"/createWorkday",
				data : vm.workday
			}
			var httpSuccess=function success(response){
				 common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                    location.href = url_back;
                                }
                            })
                        }
                    });
			}
			 common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
		
		
		}//end createWorkday
		
		//begin updateWorkday
		function updateWorkday(vm){
			var httpOptions={
				method: "put",
				url : url_workday,
				data : vm.workday
			}
			
			var httpSuccess=function success(response){
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function(){
						common.alert({
							vm: vm,
							msg:"操作成功",
							fn:function(){
								vm.isSubmit = false;
                                $('.alertDialog').modal('hide');
                                $('.modal-backdrop').remove();
                                location.href = url_back;
							}
						});
					}
				});
			}
			common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
		}
		//end updateWorkday
		
		
		//begin deleteWorkday
		function deleteWorkday(vm,id){
			var httpOptions={
				method:'delete',
				url:url_workday,
				data:id
			}
			
			var httpSuccess=function success(response){
				 vm.gridOptions.dataSource.read();
			}
			common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
			
		}
		//end deleteWorkday
		
		function grid(vm){
			//begin dataSource
			var dataSource=new kendo.data.DataSource({
				type:'odata',
				transport:common.kendoGridConfig().transport(url_workday+'/findByOdataObj',$("#workdayForm")),
				schema:common.kendoGridConfig().schema({
					id:'id',
					fields:{
						createdDate:{
							type:"date"
						},
						modifiedDate:{
							type:"date"
						}
					}
				
				}),
				serverPaging:true,
				serverSorting:true,
				serverFiltering:true,
				pageSize :10,
				sort:{
					field:"dates",
					dir:"desc"
				}
				
			});//end dataSource
			
			//S_序号
            var  dataBound=function () {  
                var rows = this.items();  
                var page = this.pager.page() - 1;  
                var pagesize = this.pager.pageSize();  
                $(rows).each(function () {  
                    var index = $(this).index() + 1 + page * pagesize;  
                    var rowLabel = $(this).find(".row-number");  
                    $(rowLabel).html(index);  
                });  
            }; 
            //S_序号
			
			var columns=[
				{
				 template: function (item) {
                        return kendo
                            .format(
                                "<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                                item.id)
                    },
                    filterable: false,
                    width: 40,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"

                },
                {  
				    field: "rowNumber",  
				    title: "序号",  
				    width: 50,
				    filterable : false,
				    template: "<span class='row-number'></span>"  
				 },
                {  
				    field: "dates",  
				    title: "时间",  
				    width: 200,
				    format:"{0:yyyy-MM-dd}",
				    filterable : false
				 },
                {  
				    field: "",  
				    title: "状态",  
				    width: 100,
				    filterable : false,
				    template:function(item){
				    	if(item.status){
					    	if(item.status=="1"){
					    		return "调休";
					    	}
					    	if(item.status=="2"){
					    		return "加班";
					    	}
				    	}else{
				    		return "";
				    	}
				    }
				 } ,
                {  
				    field: "remark",  
				    title: "备注",  
				    width: 140,
				    filterable : false
				 }
				 ,
                {  
				    field: "",  
				    title: "操作",  
				    width: 140,
				    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.del('" + item.id + "')", item.id);

                    }
				 }
			
			];
			vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                columns: columns,
                dataBound:dataBound,
                resizable: true
            };
		
		}//end grid
		}
		
	})();
(function () {
    'use strict';

    angular.module('app').controller('workdayEditCtrl', workdayEdit);

    workdayEdit.$inject = ['$location', 'workdaySvc','$state'];

    function workdayEdit($location, workdaySvc,$state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加工作日';
        vm.id=$state.params.id;
        vm.workday={};
        vm.workday.status="1";//初始化状态
        if(vm.id){
        	vm.isUpdate=true;
        	vm.title="更新工作日";
        }
        
        
        activate();
        function activate() {
        	if(vm.isUpdate){
        		workdaySvc.getWorkdayById(vm);
        	}
        }
        
        vm.create=function(){
        	workdaySvc.createWorkday(vm);
        }
        
        vm.update=function(){
        	workdaySvc.updateWorkday(vm);
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('workprogramBaseEditCtrl', workprogram);

    workprogram.$inject = ['$location','workprogramSvc','$state',"$http"]; 

    function workprogram($location,workprogramSvc,$state,$http) {        
        var vm = this;
    	vm.work = {};						//创建一个form对象
        vm.title = '项目基本信息';        	//标题
        vm.work.signId = $state.params.signid;		//这个是收文ID

        activate();
        function activate() {
        	workprogramSvc.initPage(vm);
        }

        vm.create = function () {  
        	workprogramSvc.createWP(vm,false);
        };  

    }
})();


(function () {
    'use strict';

    angular.module('app').controller('workprogramEditCtrl', workprogram);

    workprogram.$inject = ['workprogramSvc','$state','bsWin'];

    function workprogram(workprogramSvc,$state,bsWin) {
        var vm = this;
    	vm.work = {};						//创建一个form对象
        vm.model = {};                      //项目对象
        vm.title = '评审方案编辑';        	//标题
        vm.startDateTime = new Date("2006/6/1 08:00");
        vm.endDateTime = new Date("2030/6/1 21:00"); 
        vm.work.signId = $state.params.signid;		//收文ID

        vm.sign = {};						//创建收文对象
        vm.unSeledWork = {};                //未选择的工作方案
        vm.serchSign = {};                  //用于过滤

        vm.businessFlag={
            isSelfReview : false,          //是否自评
            isSingleReview : true,         //是否单个评审
            isMainWorkProj: false,         //是否是合并评审主项目
            isLoadMeetRoom: false,         //是否已经加载了会议室
        }

        //页面初始化
        activate();
        function activate() {
        	workprogramSvc.initPage(vm);
            
            $('#wpTab li').click(function (e) {
                var aObj = $("a", this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#" + showDiv).addClass("active").addClass("in").show(500);
            })
            //查询会议列表
        }

        //评审方式修改
        vm.reviewTypeChange = function(){
            //自评的话，不需要会议和专家
            if("自评" == vm.work.reviewType){
                if("合并评审" == vm.work.isSigle){
                    vm.work.isSigle = "单个评审";
                    common.alert({
                        vm: vm,
                        msg: "自评方案不能进行合并评审！",
                        colseDialog:true,
                    })
                }

            //专家函评不需要会议室
            }else {
                //合并评审改为单个评审
                if( vm.businessFlag.isMainWorkProj && !vm.businessFlag.isSingleReview && "单个评审" == vm.work.isSigle){
                    common.confirm({
                        vm:vm,
                        title:"",
                        msg:"该项目已经关联其他合并评审会关联，您确定要改为单个评审吗？",
                        fn: function () {
                            $('.confirmDialog').modal('hide');
                            vm.work.isMainProject = "0";
                            workprogramSvc.deleteAllMerge(vm);
                        },
                        cancel:function(){
                            vm.work.isSigle = "合并评审"
                            $('.confirmDialog').modal('hide');
                        }
                    })
                }
            }
        }
        
        //关闭窗口
        vm.onWorkClose=function(){
        	window.parent.$(".workPro").data("kendoWindow").close();
        }

        vm.filterSign = function(item){
            var isMatch = true;
            if(vm.serchSign.projectname && (item.projectname).indexOf(vm.serchSign.projectname) == -1){
                isMatch = false;
            }
            if(vm.serchSign.reviewstage && (item.reviewstage).indexOf(vm.serchSign.reviewstage) == -1){
                isMatch = false;
            }
            if(isMatch){
                return item;
            }
        }

        //初始化合并评审弹框
        vm.initMergeWP = function(){
            if (!vm.work.id || vm.businessFlag.isMainWorkProj == false) {
                bsWin.alert("请先保存工作方案！");
            }else{
                //初始化合并评审信息
                workprogramSvc.initMergeInfo(vm,vm.work.signId);
                $("#mergeSignWP").kendoWindow({
                    width: "1200px",
                    height: "600px",
                    title: "合并评审",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            }
        }

        //选择项目
        vm.chooseSign = function(){
            var selIds = $("input[name='mergeSign']:checked");
            if(selIds.length == 0){
                bsWin.alert("请选择要合并评审的项目！");
            }else{
                var signIdArr = [];
                $.each(selIds, function (i, obj) {
                    signIdArr.push(obj.value);
                });
                workprogramSvc.chooseSign(vm.work.signId,signIdArr.join(","),function (data) {
                    if (data.flag || data.reCode == "ok") {
                        workprogramSvc.initMergeInfo(vm,vm.work.signId);
                    }
                    bsWin.alert(data.reMsg);
                });
            }
        }

        //取消项目
        vm.cancelSign = function(){
            var selIds = $("input[name='cancelMergeSignid']:checked");
            if(selIds.length == 0){
                bsWin.alert("请选择要取消合并评审的项目！");
            }else{
                var selSignIdArr = [];
                $.each(selIds, function (i, obj) {
                    selSignIdArr.push(obj.value);
                });
            }
        	workprogramSvc.cancelMergeSign(vm.work.signId,selSignIdArr.join(","),function (data) {
                if (data.flag || data.reCode == "ok") {
                    workprogramSvc.initMergeInfo(vm,vm.work.signId);
                }
                bsWin.alert(data.reMsg);
            });
        }
     
        //会议预定添加弹窗
        vm.addTimeStage = function(){
            if(vm.work.id){
                $state.go('room', {workProgramId:vm.work.id});
            }else{
                bsWin.alert("请先保存！");
            }
        }
        
        //保存预定会议室信息
        vm.saveRoom = function(){
            workprogramSvc.saveRoom(vm.roombook,vm.work,function(data){
                if (data.flag || data.reCode == "ok") {
                    window.parent.$("#stageWindow").data("kendoWindow").close();
                    bsWin.success("操作成功！");
                    //替换修改的会议信息
                    $.each( vm.work.roomBookingDtos, function(key, val) {
                        if(vm.roombook.id == val.id){
                            val = data.reObj;
                        }
                    } );
                } else {
                    bsWin.error(data.reMsg);
                }

            })
        }
        
        vm.onRoomClose = function(){
        	window.parent.$("#stageWindow").data("kendoWindow").close();
        }

        //删除会议室预定情况
        vm.deleteBookRoom = function(id){
            bsWin.confirm({
                title: "询问提示",
                message: "是否进行该操作！",
                onOk: function () {
                    workprogramSvc.deleteBookRoom(id,function(){
                        bsWin.success("操作成功！");
                        $.each( vm.work.roomBookingDtos, function(key, val) {
                            if(id == val.id){
                                vm.work.roomBookingDtos.splice(key, 1);
                            }
                        } );
                    })
                },
            });
        }

        //修改会议预定信息
        vm.updateBookRoom = function(id){
            if(vm.businessFlag.isLoadMeetRoom == false){
                //查找所有会议室地
                vm.roombookingList = {};
                workprogramSvc.findAllMeeting(function (data) {
                    vm.roombookingList = data;
                    vm.businessFlag.isLoadMeetRoom = true;
                    $.each( vm.work.roomBookingDtos, function(key, val) {
                        if(id == val.id){
                            vm.roombook = val;
                        }
                    } );
                    $("#stageWindow").kendoWindow({
                        width : "660px",
                        height : "550px",
                        title : "会议预定信息",
                        visible : false,
                        modal : true,
                        closable : true,
                        actions : [ "Pin", "Minimize", "Maximize", "Close" ]
                    }).data("kendoWindow").center().open();
                });
            }
            else{
                $.each( vm.work.roomBookingDtos, function(key, val) {
                    if(id == val.id){
                        vm.roombook = val;
                    }
                } );
                $("#stageWindow").kendoWindow({
                    width : "660px",
                    height : "550px",
                    title : "会议预定信息",
                    visible : false,
                    modal : true,
                    closable : true,
                    actions : [ "Pin", "Minimize", "Maximize", "Close" ]
                }).data("kendoWindow").center().open();
            }
        }

        //查询评估部门
        vm.findUsersByOrgId = function(type){
        	workprogramSvc.findUsersByOrgId(vm,type);
        }
        
        vm.create = function () {
            common.initJqValidation($("#work_program_form"));
            var isValid = $("#work_program_form").valid();
            if(isValid){
                workprogramSvc.createWP(vm.work,false,vm.iscommit,function(data){
                    if (data.flag || data.reCode == "ok") {
                        vm.work.id = data.reObj.id;
                        //初始化数值
                        if(data.reObj.reviewType == "自评"){
                            vm.businessFlag.isSelfReview = true;           //是否自评
                        }
                        if(data.reObj.isSigle == "合并评审"){
                            vm.businessFlag.isSingleReview = false;         //是否单个评审
                        }
                        if(data.reObj.isMainProject == "9"){
                            vm.businessFlag.isMainWorkProj = true;           //合并评审主项目
                        }
                        bsWin.success("操作成功！");
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("表格填写不正确，请检查相应的必填项信息！");
            }

        };

        //拟聘请专家
        vm.selectExpert = function(){
            if (vm.work.id) {
                $state.go('expertReviewEdit', {workProgramId: vm.work.id});
            } else {
                bsWin.alert("请先保存当前信息，再继续操作！");
            }
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').factory('workprogramSvc', workprogram);

    workprogram.$inject = ['sysfileSvc', '$http', '$state','$rootScope'];
    function workprogram(sysfileSvc, $http, $state,$rootScope) {
        var url_company = rootPath + "/company";
        var service = {
            initPage: initPage,				        //初始化页面参数
            createWP: createWP,				        //新增操作
            findCompanys: findCompanys,		        //查找主管部门
            findUsersByOrgId: findUsersByOrgId,     //查询评估部门
            saveRoom: saveRoom,					    //添加会议预定
            deleteBookRoom:deleteBookRoom,          //删除会议室
            findAllMeeting: findAllMeeting,         //查找会议室地点

            initMergeInfo : initMergeInfo,          //初始化合并项目信息
            getMergeSignBySignId: getMergeSignBySignId,   //初始化已选项目列表
            unMergeWPSign: unMergeWPSign,			//待选项目列表

            chooseSign: chooseSign,                 //选择合并评审的工作方案
            cancelMergeSign: cancelMergeSign,       //取消合并评审的工作方案
            deleteAllMerge:deleteAllMerge,          //删除所有合并评审的工作方案
        };

        return service;

        //S_初始化已选项目列表
        function getMergeSignBySignId(signId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/getMergeSignBySignId",
                params: {
                    signId:signId
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //E_初始化已选项目列表

        //S_待选项目列表
        function unMergeWPSign(signId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/unMergeWPSign",
                params: {
                    signId: signId
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //E_待选项目列表

        //S_initMergeInfo
        function initMergeInfo(vm,signId){
            unMergeWPSign(signId,function (data) {
                vm.unMergeSign = [];
                vm.unMergeSign = data;
            });//待选
            getMergeSignBySignId(signId,function (data) {
                vm.mergeSign = [];
                vm.mergeSign = data;
            });//初始化已选项目
        }
        //S_取消合并评审工作方案
        function cancelMergeSign(signId,cancelIds,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/cancelMergeSign",
                params: {
                    signId: signId,
                    cancelIds: cancelIds,
                    mergeType:"1"
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_cancelWP

        //S_选择合并评审工作方案
        function chooseSign(signId,mergeIds,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/mergeSign",
                params: {
                    signId:signId ,
                    mergeIds: mergeIds,
                    mergeType:"1"
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_chooseWP

        //S_会议预定添加
        function saveRoom(roombook,work,callBack) {
            common.initJqValidation($('#stageForm'));
            var isValid = $('#stageForm').valid();
            if (isValid) {
                if (new Date(roombook.endTime) < new Date(roombook.beginTime)) {
                    bsWin.error("开始时间不能大于结束时间!");
                    return;
                }
                roombook.workProgramId = work.id;
                roombook.stageOrgName = work.reviewOrgName;
                roombook.stageProject = "项目名称:" + work.projectName + ":" + work.buildCompany + ":" + work.reviewOrgName;
                roombook.beginTimeStr = $("#beginTime").val();
                roombook.endTimeStr = $("#endTime").val();
                roombook.beginTime = $("#rbDay").val() + " " + $("#beginTime").val() + ":00";
                roombook.endTime = $("#rbDay").val() + " " + $("#endTime").val() + ":00";

                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/room/saveRoom",
                    data: roombook
                }
                var httpSuccess = function success(response) {
                    if (callBack != undefined && typeof callBack == 'function') {
                        callBack(response.data);
                    }
                }
                common.http({
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }
        //E_会议预定添加

        //S_查找所有会议室地点
        function findAllMeeting(callBack) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/room/meeting"
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }

        //E_查找所有会议室地点

        //start 查找主管部门
        function findCompanys(vm) {
            var httpOptions = {
                method: 'get',
                url: common.format(url_company + "/findCcompanys")
            }
            var httpSuccess = function success(response) {
                vm.companys = {};
                vm.companys = response.data;
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //end 查找主管部门

        //S_根据部门ID选择用户
        function findUsersByOrgId(vm, type) {
            var param = {};
            if ("main" == type) {
                param.orgId = vm.work.reviewDept;
            }
            var httpOptions = {
                method: 'get',
                url: rootPath + "/user/findUsersByOrgId",
                params: param
            };
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        if ("main" == type) {
                            vm.mainUserList = {};
                            vm.mainUserList = response.data;
                        }
                    }
                });
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //E_根据部门ID选择用户

        //S_初始化页面参数
        function initPage(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/workprogram/html/initWorkProgram",
                params: {
                    signId: vm.work.signId,
                }
            }
            var httpSuccess = function success(response) {
                if (response.data != null && response.data != "") {
                    vm.work = response.data.eidtWP;
                    vm.model.workProgramDtoList = {};
                    if(response.data.WPList && response.data.WPList.length > 0){
                        vm.model.workProgramDtoList = response.data.WPList;
                    }
                    if(vm.work.branchId == "1"){
                        findCompanys(vm);//查找主管部门
                    }
                    vm.work.signId = $state.params.signid;		//收文ID(重新赋值)
                    if(vm.work.projectType){
                        vm.work.projectTypeDicts = $rootScope.topSelectChange(vm.work.projectType,$rootScope.DICT.PROJECTTYPE.dicts)
                    }
                    //初始化数值
                    if(vm.work.reviewType == "自评"){
                        vm.businessFlag.isSelfReview = true;           //是否自评
                    }
                    if(vm.work.isSigle == "合并评审"){
                        vm.businessFlag.isSingleReview = false;         //是否单个评审
                    }
                    if(vm.work.isMainProject == "9"){
                        vm.businessFlag.isMainWorkProj = true;           //合并评审主项目
                    }
                    if (vm.work.id) {
                        var sysfileType = "工作方案";
                        //初始化附件上传
                        sysfileSvc.initUploadOptions({
                            businessId: vm.work.id,
                            sysSignId: vm.work.signId,
                            sysfileType: sysfileType,
                            uploadBt: "upload_file_bt",
                            detailBt: "detail_file_bt",
                            vm: vm
                        });
                    }
                }
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//S_初始化页面参数

        //S_保存操作
        function createWP(work, isNeedWorkProgram,isCommit,callBack) {
            isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/workprogram/addWork",
                data: work,
                params: {
                    isNeedWorkProgram: isNeedWorkProgram
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                    isCommit = false;
                }
            });
        }//E_保存操作


        //S_删除所有合并评审工作方案
        function deleteAllMerge(vm){
            var httpOptions = {
                method: 'post',
                url: rootPath + "/workprogram/deleteMergeWork",
                params: {
                    mainBusinessId: vm.work.id,
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: "操作成功！",
                            closeDialog: true
                        });
                    }
                });
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_deleteAllMerge


        //S_删除会议室
        function deleteBookRoom(bookId,callBack){
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/room",
                params: {
                    id: bookId
                }
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack();
                }
            }
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }//E_deleteBookRoom
    }
})();
(function () {
    'use strict';

    angular.module('app').controller('orgCtrl', org);

    org.$inject = ['$location','orgSvc']; 

    function org($location, orgSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '部门列表';
        
        vm.del = function (id) {        	       	 
             common.confirm({
            	 vm:vm,
            	 title:"",
            	 msg:"确认删除数据吗？",
            	 fn:function () {
                  	$('.confirmDialog').modal('hide');             	
                    orgSvc.deleteOrg(vm,id);
                 }
             })
        }
        vm.dels = function () {     
        	var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
            	common.alert({
                	vm:vm,
                	msg:'请选择数据'
                	
                });
            } else {
            	var ids=[];
                for (var i = 0; i < selectIds.length; i++) {
                	ids.push(selectIds[i].value);
				}  
                var idStr=ids.join(',');
                vm.del(idStr);
            }   
       }
        //查询
        vm.queryOrg = function(){
        	orgSvc.queryOrg(vm);
        }
        activate();
        function activate() {
            orgSvc.grid(vm);
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('orgEditCtrl', org);

    org.$inject = ['$location','orgSvc','$state']; 

    function org($location, orgSvc,$state) {
        var vm = this;
        vm.title = '新增部门';
        vm.isorgExist=false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新部门';
        }
        
        vm.create = function () {
        	orgSvc.createOrg(vm);
        };
        vm.update = function () {
        	orgSvc.updateOrg(vm);
        };
        

        activate();
        function activate() {
        	orgSvc.initRoleUsers(vm);
        	if (vm.isUpdate) {
        		orgSvc.getOrgById(vm);
            }        	        	
        }
    }
})();

(function() {
	'use strict';

	angular.module('app').factory('orgSvc', org);

	org.$inject = [ '$http','$compile' ];	
	function org($http,$compile) {	
		var url_org = rootPath +"/org";
		var url_back = '#/org';
		var url_user=rootPath +'/user';
			
		var service = {
			grid : grid,
			createOrg : createOrg,			
			getOrgById : getOrgById,
			updateOrg:updateOrg,
			deleteOrg:deleteOrg	,
			getCompany : getCompany,
			initRoleUsers: initRoleUsers, //初始化角色数据
			queryOrg:queryOrg
		};		
		return service;	
		
		//查询
		function queryOrg(vm){
			vm.gridOptions.dataSource.read();
		}
				
		function grid(vm) {
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_org+"/fingByOData",$("#orgForm")),
				schema : common.kendoGridConfig().schema({
					id : "id",
					fields : {
						createdDate : {
							type : "date"
						}
					}
				}),
				serverPaging : true,
				serverSorting : true,
				serverFiltering : true,			
				pageSize: 10,
				sort : {
					field : "sort",
					dir : "asc"
				}
			});

			// End:dataSource
			
			 //S_序号
            var  dataBound=function () {  
                var rows = this.items();  
                var page = this.pager.page() - 1;  
                var pagesize = this.pager.pageSize();  
                $(rows).each(function () {  
                    var index = $(this).index() + 1 + page * pagesize;  
                    var rowLabel = $(this).find(".row-number");  
                    $(rowLabel).html(index);  
                });  
            } 
            //S_序号

			// Begin:column
			var columns = [
					{
						template : function(item) {
							return kendo
									.format(
											"<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
											item.id)
						},
						filterable : false,
						width : 40,
						title : "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
						
					}, 
					 {  
					    field: "rowNumber",  
					    title: "序号",  
					    width: 50,
					    filterable : false,
					    template: "<span class='row-number'></span>"  
					 }
					,
					
					{
						field : "orgIdentity",
						title : "部门标识",
						width : 80,						
						filterable : false
					}, 
					{
						field : "name",
						title : "部门名称",
						width : 130,						
						filterable : false
					},
					{
						field : "orgFunction",
						title : "职能",
						width :130,						
						filterable : false
					},
					{
						field : "orgDirectorName",
						title : "部门负责人",
						width : 100,						
						filterable : false
					},
					{
						field : "orgSLeaderName",
						title : "分管领导",
						width : 100,						
						filterable : false
					},
					{
						field : "orgPhone",
						title : "电话",
						width : 130,						
						filterable : false
					},					
					{
						field : "",
						title : "操作",
						width : 200,
						template:function(item){							
							return common.format($('#columnBtns').html(),"vm.del('"+item.id+"')",item.id);							
						}						
					}
			];
			// End:column
		
			vm.gridOptions={
				dataSource : common.gridDataSource(dataSource),			
				filterable : common.kendoGridConfig().filterable,
				pageable : common.kendoGridConfig().pageable,
				noRecords:common.kendoGridConfig().noRecordMessage,
				columns : columns,
				dataBound:dataBound,
				resizable: true
			};
			
		}// end fun grid

		function createOrg(vm) {
			common.initJqValidation();
			var isValid = $('form').valid();
			if (isValid && vm.isorgExist == false) {				
				vm.isSubmit = true;
				var httpOptions = {
					method : 'post',
					url : url_org,
					data : vm.model
				}
				vm.OrgDirectorUsers.forEach(function (u, number) {
                    if(u.id == vm.model.orgDirector){
                    	vm.model.orgDirectorName = u.displayName;
                    }
                });
				vm.orgMLeaderUsers.forEach(function (u, number) {
                    if(u.id == vm.model.orgMLeader){
                    	vm.model.orgMLeaderName = u.displayName;
                    }
                });
				vm.orgSLeaderUser.forEach(function (u, number) {
                    if(u.id == vm.model.orgSLeader){
                    	vm.model.orgSLeaderName = u.displayName;
                    }
                });
				var httpSuccess = function success(response) {									
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function() {														
							common.alert({
								vm:vm,
								msg:"操作成功",
								fn:function() {
									vm.isSubmit = false;
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
									location.href = url_back;
								}
							})
						}						
					});
				}

				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess
				});
			} 
		}// end fun createorg
		
		//获取单位信息
		function getCompany(vm){			
			var httpOptions = {
					method : 'get',
					url : common.format(url_org + "/getCompany")
				}
				var httpSuccess = function success(response) {
					vm.company ={};
					vm.company =response.data;
					//console.log(vm.company);
				}
				
				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess
				});
		}
		
		function getOrgById(vm) {			
			var httpOptions = {
				method : 'get',
				url :  url_org + "/html/getOrgById",
				params:{id:vm.id}
			}
			var httpSuccess = function success(response) {																			
				vm.model = response.data;				
			}			
			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}// end fun getorgById
		
		function updateOrg(vm){
			common.initJqValidation();			
			var isValid = $('form').valid();
			if (isValid && vm.isorgExist == false) {
				vm.isSubmit = true;
				vm.model.id=vm.id;
				vm.OrgDirectorUsers.forEach(function (u, number) {
                    if(u.id == vm.model.orgDirector){
                    	vm.model.orgDirectorName = u.displayName;
                    }
                });
				vm.orgMLeaderUsers.forEach(function (u, number) {
                    if(u.id == vm.model.orgMLeader){
                    	vm.model.orgMLeaderName = u.displayName;
                    }
                });
				vm.orgSLeaderUser.forEach(function (u, number) {
                    if(u.id == vm.model.orgSLeader){
                    	vm.model.orgSLeaderName = u.displayName;
                    }
                });
				
				var httpOptions = {
					method : 'put',
					url : url_org,
					data : vm.model
				}

				var httpSuccess = function success(response) {
					
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function() {
							
							common.alert({
								vm:vm,
								msg:"操作成功",
								fn:function() {
									vm.isSubmit = false;
									$('.alertDialog').modal('hide');							
								}
							})
						}
						
					})
				}

				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess
				});

			}
		}// end fun updateorg
		
		function deleteOrg(vm,id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url:url_org,
                data:id
                
            }
            var httpSuccess = function success(response) {                
                common.requestSuccess({
					vm:vm,
					response:response,
					fn:function () {
	                    vm.isSubmit = false;
	                    vm.gridOptions.dataSource.read();
	                }
					
				});

            }
            common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
        }// end fun deleteorg
		
		//S_initRoleUsers
		function initRoleUsers(vm){
			var httpOptions = {
                method: 'get',
                url:rootPath +'/user/initRoleUsers'               
            }
            var httpSuccess = function success(response) {	                
                common.requestSuccess({
					vm:vm,
					response:response,
					fn:function () {	
						vm.orgMLeaderUsers = response.data.DIRECTOR;
						vm.orgSLeaderUser = response.data.VICE_DIRECTOR;
						vm.OrgDirectorUsers = response.data.DEPT_LEADER;
	                }						
				});
            }
            common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}//E_initRoleUsers
		

	}
	
	
	
})();
(function () {
    'use strict';

    angular
        .module('app')
        .controller('orgUserCtrl', org);

    org.$inject = ['$location','$state','orgSvc','orgUserSvc']; 

    function org($location,$state, orgSvc,orgUserSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.id = $state.params.id;
       
        
        vm.showAddUserDialog = function () {
        	$('.addUser').modal({
                backdrop: 'static',
                keyboard:false
            });
        	 vm.orgUserGrid.dataSource.read();
        };
        vm.closeAddUserDialog=function(){
        	$('.addUser').modal('hide');		
        	
        }
        vm.add = function (userId) {
        	orgUserSvc.add(vm,userId);
        };
        vm.remove = function (userId) {
        	orgUserSvc.remove(vm,userId);
        };
        vm.removes = function () {     
        	var selectIds = common.getKendoCheckId('.orgUserGrid');
            if (selectIds.length == 0) {
                common.alert({
                	vm:vm,
                	msg:'请选择数据'
                	
                });
            } else {
            	var ids=[];
                for (var i = 0; i < selectIds.length; i++) {
                	ids.push(selectIds[i].value);
				}  
                var idStr=ids.join(',');
                vm.remove(idStr);
            }   
       }

        activate();
        function activate() {
        	
        	orgSvc.getOrgById(vm);
        	orgUserSvc.orgUserGrid(vm);
        	orgUserSvc.allUserGrid(vm);
        }
    }
})();

(function() {
	'use strict';

	angular.module('app').factory('orgUserSvc', org);

	org.$inject = [ '$http','$compile' ];	
	function org($http,$compile) {	
		var url_org = "/org";
		var url_back = '#/org';
		var user_userNotIn=rootPath+'/org/userNotIn';
		var url_orgUsers=rootPath+"/org/users";
		
			
		var service = {	
			orgUserGrid:orgUserGrid,
			allUserGrid:allUserGrid,
			add:add,
			remove:remove
		};		
		return service;	
		
		//begin#remove
		function remove(vm,userId){		
            var httpOptions = {
                method: 'delete',
                url:rootPath+"/org/deleteUsers",
                params:{
                	orgId:vm.id,
                	userId: userId
                }                
            }
            var httpSuccess = function success(response) {                
                common.requestSuccess({
					vm:vm,
					response:response,
					fn:function () {						
	                    vm.gridOptions.dataSource.read();	                   
	                }					
				});
            }
            common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}		
		
		//begin#add
		function add(vm,userId){		
            var httpOptions = {
                method: 'post',
                url:rootPath+"/org/addUsers",
                params:{
                	orgId:vm.id,
                	userId: userId
                }                
            }
            
            var httpSuccess = function success(response) {              
                common.requestSuccess({
					vm:vm,
					response:response,
					fn:function () {
						vm.orgUserGrid.dataSource.read();
	                    vm.gridOptions.dataSource.read();	                   
	                }					
				});
            }
            common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}
		
		//begin#allUserGrid
		function allUserGrid(vm){
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(user_userNotIn+"?orgId="+vm.id),
				schema : common.kendoGridConfig().schema({
					id : "id",
					fields : {
						createdDate : {
							type : "date"
						}
					}
				}),
				serverPaging : true,
				serverSorting : true,
				serverFiltering : true,
				pageSize : 10,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});

			// End:dataSource

			// Begin:column
			var columns = [
					
					{
						field : "loginName",
						title : "登录名",
						width : 200,
						filterable : true
					},
					{
						field : "displayName",
						title : "显示名",
						width : 200,
						filterable : true
					},
					{
						field : "comment",
						title : "描述",
						filterable : false
					},
					{
						field : "",
						title : "操作",
						width : 80,
						template : function(item) {
							return common.format($('#allUserGridBtns').html(),
									"vm.add('" + item.id + "')", item.id);

						}

					}

			];
			// End:column

			vm.orgUserGrid = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridConfig().filterable,
				pageable : common.kendoGridConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : columns,
				resizable : true
			};
			
		}
		
		//begin#orgUserGtid
		function orgUserGrid(vm){
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_orgUsers+"?orgId="+vm.id),
				schema : common.kendoGridConfig().schema({
					id : "id",
					fields : {
						createdDate : {
							type : "date"
						}
					}
				}),
				serverPaging : false,
				serverSorting : false,
				serverFiltering : false,
				pageSize : 10,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});

			// End:dataSource

			// Begin:column
			var columns = [
					{
						template : function(item) {
							return kendo
									.format(
											"<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
											item.id)
						},
						filterable : false,
						width : 40,
						title : "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"

					},
					{
						field : "loginName",
						title : "登录名",
						width : 200,
						filterable : false
					},
					{
						field : "displayName",
						title : "显示名",
						width : 200,
						filterable : false
					},
					{
						field : "comment",
						title : "描述",
						filterable : false
					},
					{
						field : "",
						title : "操作",
						width : 180,
						template : function(item) {
							return common.format($('#columnBtns').html(),
									"vm.remove('" + item.id + "')", item.id);

						}

					}

			];
			// End:column

			vm.gridOptions = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridConfig().filterable,
				pageable : common.kendoGridConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : columns,
				resizable : true
			};
		}
		
		
		
		
		

	}
	
	
	
})();
(function () {
    'use strict';

    angular.module('app').controller('registerFileCtrl', registerFile);

    registerFile.$inject = ['$location','registerFileSvc','$state','$http']; 

    function registerFile($location, registerFileSvc,$state,$http) {
        var vm = this;
        vm.title = '待办事项';
        vm.addregister={};
        vm.model={};
        vm.model.signid=$state.params.signid;
        activate();
        function activate() {
        	vm.showPrint=false;
        	registerFileSvc.grid(vm);
        }
        vm.print = function(){
        	//vm.registerFileList={};
    		//vm.signdto={};
            signcommon.registerFilePrint(vm,{$http: $http});
        }
        vm.findbySuppleDate = function(e){
        	
        }
    }
})();

(function() {
	'use strict';

	angular.module('app').factory('registerFileSvc', registerFile);

	registerFile.$inject = ['$rootScope', '$http'];

	function registerFile($rootScope, $http) {
		var addregister_url = rootPath + "/addRegisterFile";
		
		return {
			grid : grid
		};

		// begin#grid
		function grid(vm) {
			
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
						type : 'odata',
						transport : common.kendoGridinlineConfig().transport(
								vm, {
									readUrl : addregister_url
											+ "/findByOData",
									updateUrl : addregister_url + "/update",
									destroyUrl : addregister_url + "/delete",
									createUrl : addregister_url + "/create/"+vm.model.signid},
									{filter: "signid eq '"+vm.model.signid+"'"}),
						schema : common.kendoGridConfig().schema({
									id : "id",
									fields : {
										fileName : {
											validation: { required: true },
											type : "String"
										},
										totalNum : {
											type : "number",
											validation: { required: true, min: 1}
										},
										isHasOriginfile : {
											type : "boolean"
										},
										isHasCopyfile : {
											type : "boolean"
										},
										suppleDeclare : {
											type : "string"
										},
										suppleDate : {
											type : "date"
										},
										createdDate : {
											type : "date"
										},
										modifiedDate : {
											type : "date"
										}
									}
								}),
						batch : true,
						pageSize : 10,
						serverPaging : true,
						serverSorting : true,
						serverFiltering : true,
						sort : {
							field : "createdDate",
							dir : "desc"
						}
					});

			// End:dataSource

			// S_序号
			var dataBound = function() {
				var rows = this.items();
				var page = this.pager.page() - 1;
				var pagesize = this.pager.pageSize();
				$(rows).each(function() {
							var index = $(this).index() + 1 + page * pagesize;
							var rowLabel = $(this).find(".row-number");
							$(rowLabel).html(index);
						});
			}
			// S_序号
			// Begin:column
			var columns = [{
						field : "",
						title : "序号",
						width : 50,
						filterable : false,
						template : "<span class='row-number'></span>"
					}, {
						field : "fileName",
						title : "资料名称",
						width : "120px",
						filterable : false
					}, {
						field : "totalNum",
						title : "份数",
						width : "120px",
						filterable : false
					}, {
						field : "isHasOriginfile",
						title : "原件",
						width : "120px",
						filterable : false,
						template : function(item) {
							if (item.isHasOriginfile) {
								return "是";
							} else {
								return "否";
							}
						}
					}, {
						field : "isHasCopyfile",
						title : "复印件",
						width : "120px",
						filterable : false,
						template : function(item) {
							if (item.isHasCopyfile) {
								return "是";
							} else {
								return "否";
							}
						}
					}, {
						field : "suppleDeclare",
						title : "补充说明",
						width : "120px",
						filterable : false
					}, {
						field : "suppleDate",
						title : "补充日期",
						width : "120px",
						format : "{0:yyyy-MM-dd}",
						filterable : false
					}, {
						command : ["destroy"],
						title : "&nbsp;",
						width : "250px"
					}];// 列
			// End:column

			vm.gridOptions = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridinlineConfig().filterable,
				pageable : common.kendoGridinlineConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : columns,
				toolbar : ["create", "save", "cancel"],
				dataBound : dataBound,
				editable : true,
				navigatable : true,
				resizable : true
			};

		}// end fun grid

	}
})();
(function () {
    'use strict';

    angular.module('app').controller('suppletterCtrl', suppletter);

    suppletter.$inject = ['$location','suppletterSvc','$state','$http']; 

    function suppletter($location, suppletterSvc,$state,$http) {
        var vm = this;
        vm.showsupp=false;
        vm.title = '拟补资料函';
        vm.suppletter={};
        vm.model = {};
        vm.model.signid=$state.params.signid;
        vm.suppletter.id=$state.params.id;
        activate();
        function activate() {
        	common.initSuppData(vm,{$http:$http,$state:$state});
        }
        vm.addSuppContent=function(){
        	vm.showsupp=true;
        	 var ideaEditWindow = $("#addsuppContent");
       		 ideaEditWindow.kendoWindow({
	            width: "50%",
	            height: "80%",
	            title: "拟补资料函正文",
	            visible: false,
	            modal: true,
	            closable: true,
	            actions: ["Pin", "Minimize", "Maximize", "close"]
	        }).data("kendoWindow").center().open();

        }
    }
})();

(function () {
    'use strict';

    angular.module('app').factory('suppletterSvc', suppletter);

    suppletter.$inject = ['$rootScope', '$http'];

    function suppletter($rootScope, $http) {

        var service = {
        }
        return service;
    }
})();
(function () {
    'use strict';

    angular.module('app').controller('pauseProjectCtrl', pauseProject);

    pauseProject.$inject = ['$location','pauseProjectSvc','$state',"adminSvc","ideaSvc"];

    function pauseProject($location, pauseProjectSvc,$state,adminSvc,ideaSvc) {
        var vm = this;
        vm.title = '项目暂停审批';        		//标题

        active();
        function active(){
            pauseProjectSvc.grid(vm);
            ideaSvc.initIdea(vm);	//初始化个人常用意见
        }

        /**
         * 暂停项目弹出框
         */
        vm.pauseProjectWindow=function(signid,stopid){
            pauseProjectSvc.pauseProjectWindow(vm,signid,stopid);
        }

        /**
         *更新暂停项目审批信息
         */
        vm.commitProjectStop = function () {
            pauseProjectSvc.updateProjectStop(vm);
        }

        /**
         * 取消
         */
        vm.closewin=function(){
            window.parent.$("#spwindow").data("kendoWindow").close()
        }

        /**
         * 选择部长意见
         */
        vm.selectMinisterIdea=function(){

            vm.projectStop.directorIdeaContent += vm.directorIdea;
        }

        /**
         * 选择分管副主任意见
         */
        vm.selectDirectorIdea=function(){
            vm.projectStop.leaderIdeaContent += vm.leaderIdea;
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').factory('pauseProjectSvc', pauseProject);

    pauseProject.$inject = ['$http', '$state'];

    function pauseProject($http, $state) {
        var service = {
            pauseProjectWindow : pauseProjectWindow,    //项目暂停弹出框
            pauseProject: pauseProject,//保存暂停项目
            initProject : initProject, //初始化项目信息
            countUsedWorkday : countUsedWorkday,    //初始化已用工作日
            grid : grid,
            getProjectStopByStopId : getProjectStopByStopId,    //通过id获取暂停项目
            updateProjectStop : updateProjectStop,  //更新暂停项目审批信息
            findPausingProject : findPausingProject //查找正在申请暂停的项目
        };
        return service;

        function pauseProjectWindow(vm,signid,stopid){
            vm.sign={};
            vm.projectStop = {};
            vm.projectStop.signid = signid;
            $("#spwindow").kendoWindow({
                width: "1000px",
                height: "600px",
                title: "暂停项目审批处理",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
            initProject(vm,signid);
            countUsedWorkday(vm,signid);
            if(stopid !=""){
                vm.showIdea=true;
                getProjectStopByStopId(vm,stopid);
            }

        }

        //beign findPausingProject
        function findPausingProject(vm,signId,stopid){
            var httpOptions={
                method : "get",
                url : rootPath + "/projectStop/findPausingProject",
                params :{signId : signId}
            }

            var httpSuccess=function success(response){
                if(response.data !=""){
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            common.alert({
                                vm: vm,
                                msg: "该项目暂停申请正在处理",
                                fn: function () {
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                }
                            })
                        }

                    });
                }else{
                    pauseProjectWindow(vm,signId,stopid);
                }
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }
        //end findPausingProject

        //begin initProject
        function initProject(vm,signId){

            var httpOptions={
                method : "get",
                url : rootPath + "/projectStop/initProjectBySignId",
                params :{signId : signId}
            }

            var httpSuccess=function success(response){
                vm.sign=response.data;
            }
            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });

        }//end initProject

        //begin countUsedWorkday
        function countUsedWorkday(vm,signid){
            var httpOptions={
                method: "get",
                url : rootPath +"/projectStop/countUsedWorkday",
                params : {signId : signid}
            }

            var httpSuccess=function success(response){
                vm.sign.countUsedWorkday=response.data;
            }
            common.http({
                vm : vm,
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess
            });
        }
        //end countUsedWorkday

        //start_pauseProject
        function pauseProject(vm){
            common.initJqValidation();
            var isValid = $('#form').valid();
            if (isValid) {
                var materials = [];
                materials.push($('#file1').val());
                materials.push($('#file2').val());
                var materialStr = materials.join(",");
                vm.projectStop.material = materialStr;
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/projectStop/savePauseProject",
                    data: vm.projectStop
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            window.parent.$("#spwindow").data("kendoWindow").close();
                            vm.gridOptions.dataSource.read();
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                }
                            })
                        }

                    });
                }

                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        } //end_pauseProject

        //begin getProjectStopByStopId
        function getProjectStopByStopId(vm,stopId){
            var httpOptions={
                method : "get",
                url : rootPath + "/projectStop/getProjectStopByStopId",
                params : {stopId : stopId}
            }

            var httpSuccess=function success(response){
                vm.projectStop = response.data;
                if( vm.projectStop.directorIdeaContent == undefined){
                    vm.projectStop.directorIdeaContent="";
                }
                if(vm.projectStop.leaderIdeaContent == undefined){
                    vm.projectStop.leaderIdeaContent="";
                }
            }

            common.http({
                vm : vm,
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end getProjectStopByStopId

        //begin updataProjectStop
        function updateProjectStop(vm){
            common.initJqValidation();
            var isValid = $('#form').valid();
            if (isValid) {
                var httpOptions = {
                    method: "post",
                    url: rootPath + "/projectStop/updateProjectStop",
                    data: vm.projectStop
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            window.parent.$("#spwindow").data("kendoWindow").close();
                            vm.gridOptions.dataSource.read();
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                fn: function () {
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
                                }
                            })
                        }

                    });
                }
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
            }
        }
        //end updataProjectStop


        function grid(vm){
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/projectStop/findByOData"),
                schema: {
                    data: "value",
                    total: function (data) {
                        if (data['count']) {
                            $("#pauseCount").html(data['count']);
                        } else {
                            $("#pauseCount").html(0);
                        }
                        return data['count'];
                    },
                    model: {
                        id: "id",
                        fields: {
                            createdDate: {
                                type: "date"
                            },
                            modifiedDate: {
                                type: "date"
                            }
                        }
                    }
                },
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });
            // End:dataSource

            var columns = [
                {
                    field: "",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>",
                },
                {
                    field: "",
                    title: "项目名称",
                    width: 200,
                    filterable: false,
                    template: function (item) {
                        return item.sign.projectname;
                    }
                },
                {
                    field: "",
                    title : "评审阶段",
                    width : 100,
                    filterable : false,
                    template : function(item){
                        return item.sign.reviewstage;
                    }
                },
                {
                    field : "createdDate",
                    title : "申请日期",
                    width : 100,
                    format : "{0:yyyy-MM-dd}",
                    filterable : false
                },
                {
                    field : "",
                    title : "操作",
                    width : 100,
                    filterable : false,
                    template : function(item){
                            return common.format($("#columnBtns").html(),"vm.pauseProjectWindow('"+item.sign.signid+"','"+item.stopid+"')");
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                dataBound: function () {
                    var rows = this.items();
                    var page = this.pager.page() - 1;
                    var pagesize = this.pager.pageSize();
                    $(rows).each(function () {
                        var index = $(this).index() + 1 + page * pagesize;
                        var rowLabel = $(this).find(".row-number");
                        $(rowLabel).html(index);
                    });
                }
            };


        }


    }
})();