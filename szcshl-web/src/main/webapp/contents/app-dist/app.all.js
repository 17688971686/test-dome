(function () {
    'use strict';

    angular.module('app', [
        // Angular modules 
        "ui.router",
        "kendo.directives"

        // Custom modules 

        // 3rd Party Modules

    ]).config(["$stateProvider", "$urlRouterProvider", function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise('/index');
        $stateProvider
            .state('index', {
                url: '/index',
                templateUrl: rootPath + '/admin/gtasks.html',
                controller: 'adminCtrl',
                controllerAs: 'vm'
            })
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
            //begin#mytest
            .state('myTest', {
			  url: '/myTest',
			  templateUrl: rootPath + '/myTest/html/list.html',
			  controller: 'myTestCtrl',
			  controllerAs: 'vm'
			}).state('myTestEdit', {
			  url: '/myTestEdit/:id',
			  templateUrl: rootPath + '/myTest/html/edit.html',
			  controller: 'myTestEditCtrl',
			  controllerAs: 'vm'
			})
            //end#mytest
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
            //end expert
            
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
            })//end#sign

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
            }).state('UnitAndUser', { //单位成员
                url: '/UnitAndUser/:id',
                templateUrl: rootPath + '/assistUnit/html/unitUser.html',
                controller: 'unitAndUserCtrl',
                controllerAs: 'vm'
            })
            //end##assistUnit
             .state('assistUnitUser', {
                url: '/assistUnitUser',
                templateUrl: rootPath + '/assistUnitUser/html/assistUnitUserList.html',
                controller: 'assistUnitUserCtrl',
                controllerAs: 'vm'
            }).state('assistUnitUserEdit', {
                url: '/assistUnitUserEdit/:id',
                templateUrl: rootPath + '/assistUnitUser/html/assistUnitUserEdit.html',
                controller: 'assistUnitUserEditCtrl',
                controllerAs: 'vm'
            })
            
            
        	
        	
        ;
    }]).run(function ($rootScope, $http, $state, $stateParams) {
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;
        $rootScope.$on("$stateChangeSuccess", function (event, toState, toParams, fromState, fromParams) {
            $rootScope.previousState_name = fromState.name;
            $rootScope.previousState_params = fromParams;
        });
        //实现返回的函数
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

        common.getTaskCount({$http: $http});
    	common.initDictData({$http: $http, scope: $rootScope});
    	
//    	common.initIdeaData({$http: $http, scope: $rootScope});
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
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('adminDoingCtrl', admin);

    admin.$inject = ['$location','adminSvc']; 

    function admin($location, adminSvc) {
        var vm = this;
        vm.title = '在办任务';
             
        activate();
        function activate() {
        	adminSvc.dtasksGrid(vm);
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

(function() {
	'use strict';

	angular.module('app').factory('adminSvc', admin);

	admin.$inject = ['$rootScope', '$http'];	
	
	function admin($rootScope,$http) {
		
		var service = {
			gtasksGrid : gtasksGrid,		//个人待办
            etasksGrid : etasksGrid,		//个人办结
            dtasksGrid : dtasksGrid,        //在办任务
		}
		return service;	

		//S_gtasksGrid
		function gtasksGrid(vm){
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/flow/html/tasks"),
				schema : {
					data: "value",
                    total: function (data) {
					    if(data['count']){
                            $('#GtasksCount').html(data['count']);
                        }else{
                            $('#GtasksCount').html(0);
                        }
                    	return data['count']; 
                    },
                    model:{
                    	id : "id",                 
						fields : {
							createdDate : {
								type : "date"
							}
						}
                    }
				},
				serverPaging : true,
				serverSorting : true,
				serverFiltering : true,
				pageSize : 10,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});
			var columns = [
				 {
                     field: "",
                     title: "序号",
                     template: "<span class='row-number'></span>",
                     width:30
                 },
                 {
                     field: "businessName",
                     title: "任务名称",
                     filterable : false,
                     width:180
                 },
                 {
                     field: "flowName",
                     title: "所属流程",
                     width: 180,
                     filterable : false,
                 },                 
                 {
                     field: "taskName",
                     title: "当前环节",
                     width: 180,
                     filterable : false,
                 },
                 {
                     field: "createDate",
                     title: "接收时间",
                     width: 150,
                     filterable : false,
                     format: "{0: yyyy-MM-dd HH:mm:ss}"
                 },
                 {
                     field: "",
                     title: "流程状态",
                     width: 80,
                     filterable : false,
                     template:function(item){
 						if(item.isSuspended){
 							return '<span style="color:orange;">已暂停</span>';
 						}else{
 							return '<span style="color:green;">进行中</span>';
 						}
 					}
                 },
				{
					field : "",
					title : "操作",
					width : 180,
					template:function(item){
						//项目签收流程，则跳转到项目签收流程处理野人
						if(item.flowKey=="FINAL_SIGN_FLOW" || item.flowKey=="SIGN_XS_FLOW"){
							return common.format($('#columnBtns').html(),"signFlowDeal",item.businessKey,item.taskId,item.processInstanceId);
						}else{
							return '<a class="btn btn-xs btn-danger" >流程已停用</a>';
						}						
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
				resizable : true,
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
                type : 'odata',
                transport : common.kendoGridConfig().transport(rootPath+"/flow/html/endTasks"),
                schema : {
                    data: "value",
                    total: function (data) {
                        return data['count'];
                    },
                    model:{
                        id : "id",
                        fields : {
                            createdDate : {
                                type : "date"
                            }
                        }
                    }
                },
                serverPaging : true,
                serverSorting : true,
                serverFiltering : true,
                pageSize : 10,
                sort : {
                    field : "createdDate",
                    dir : "desc"
                }
            });
            var columns = [
                {
                    field: "",
                    title: "序号",
                    template: "<span class='row-number'></span>",
                    width:30
                },
                {
                    field: "businessName",
                    title: "任务名称",
                    filterable : false,
                    width:180
                },
                {
                    field: "createDate",
                    title: "开始时间",
                    width: 150,
                    filterable : false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "endDate",
                    title: "结束时间",
                    width: 150,
                    filterable : false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "",
                    title: "用时",
                    width: 180,
                    filterable : false,
                    template:function(item){
                        if(item.durationTime){
                            return item.durationTime;
                        }else{
                            return '<span style="color:orangered;">已办结</span>';
                        }
                    }
                },
                {
                    field: "",
                    title: "流程状态",
                    width: 120,
                    filterable : false,
                    template:function(item){
                        return '<span style="color:orangered;">已办结</span>';
                    }
                },
                {
                    field : "",
                    title : "操作",
                    width : 80,
                    template:function(item){
                        if((item.processDefinitionId).indexOf("FINAL_SIGN_FLOW") >= 0){
                            return common.format($('#columnBtns').html(),"endSignDetail",item.businessKey,item.processInstanceId);
                        }
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
                resizable : true,
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
        function dtasksGrid(vm){
            var dataSource = new kendo.data.DataSource({
                type : 'odata',
                transport : common.kendoGridConfig().transport(rootPath+"/flow/html/doingtasks"),
                schema : {
                    data: "value",
                    total: function (data) {
                        return data['count'];
                    },
                    model:{
                        id : "id"
                    }
                },
                serverPaging : true,
                serverSorting : true,
                serverFiltering : true,
                pageSize : 10,
                sort : {
                    field : "createdDate",
                    dir : "desc"
                }
            });
            var columns = [
                {
                    field: "",
                    title: "序号",
                    template: "<span class='row-number'></span>",
                    width:30
                },
                {
                    field: "businessName",
                    title: "任务名称",
                    filterable : false,
                    width:180
                },
                {
                    field: "flowName",
                    title: "所属流程",
                    width: 180,
                    filterable : false,
                },
                {
                    field: "taskName",
                    title: "当前环节",
                    width: 180,
                    filterable : false,
                },
                {
                    field: "createDate",
                    title: "接收时间",
                    width: 150,
                    filterable : false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
                {
                    field: "",
                    title: "流程状态",
                    width: 80,
                    filterable : false,
                    template:function(item){
                        if(item.isSuspended){
                            return '<span style="color:orange;">已暂停</span>';
                        }else{
                            return '<span style="color:green;">进行中</span>';
                        }
                    }
                },
                {
                    field : "",
                    title : "操作",
                    width : 180,
                    template:function(item){
                        //项目签收流程，则跳转到项目签收流程处理野人
                        if(item.flowKey=="FINAL_SIGN_FLOW" || item.flowKey=="SIGN_XS_FLOW"){
                            return common.format($('#columnBtns').html(),"signFlowDetail",item.businessKey,item.taskId,item.processInstanceId);
                        }else{
                            return '<a class="btn btn-xs btn-danger" >流程已停用</a>';
                        }
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
                resizable : true,
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

	}
})();
(function () {
    'use strict';

    angular.module('app').controller('assistPlanCtrl', assistPlan);

    assistPlan.$inject = ['$location','$state','assistSvc','$http'];

    function assistPlan($location,$state,assistSvc,$http) {
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
        vm.reviewNum=''; //几个评审单位
       
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
           assistPlanId=planId;
           vm.planId=planId;
           assistSvc.getPlanSignByPlanId(vm,planId);
           assistSvc.initPlanByPlanId(vm,planId);
           assistSvc.initAssistUnit(vm,planId);
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
        
        
        vm.checked='';
        vm.chooseAssistUnit=function(){
        	vm.number='';
        	if(vm.checked=='option1'){
        	
        		vm.number=vm.assistPlanSign.length+1;
        	}
        	if(vm.checked=='option2'){
        		vm.number=vm.assistPlanSign.length;
        	}
        	assistSvc.chooseAssistUnit(vm);
        
        }

    }
})();

(function() {
	'use strict';

	angular.module('app').controller('assistUnitCtrl', assistUnit);

	assistUnit.$inject = ['$location', 'assistUnitSvc'];

	function assistUnit($location, assistUnitSvc) {
		var vm = this;
		vm.title = '协审单位';
		vm.resource = {};

		vm.del = function(id) {
			vm.id = id;
			assistUnitSvc.getUnitUser(vm, function() {
						if (vm.resource.count == 0) {
							common.confirm({
										vm : vm,
										title : "",
										msg : "确认要删除数据吗？",
										fn : function() {
											$('.confirmDialog').modal('hide');
											vm.resource = {};
											assistUnitSvc.deleteAssistUnit(vm, id);
										}
									});
						} else {
							common.confirm({
										vm : vm,
										title : "",
										msg : "必须先删除单位下的所有成员！",
										fn : function() {
											$('.confirmDialog').modal('hide');

										}
									});

						}
					});

		}

		vm.dels = function() {
			var selectIds = common.getKendoCheckId('.grid');
			if (selectIds.length == 0) {
				common.alert({
							vm : vm,
							mag : "请选择数据"
						});
			} else {
				var ids = [];
				for (var i = 0; i < selectIds.length; i++) {
					ids.push(selectIds[i].value);
				}
				var idStr = ids.join(",");
				assistUnitSvc.deleteAssistUnit(vm, idStr);

			}
		}

		vm.queryAssistUnit = function() {
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
            queryAssistUnit : queryAssistUnit,			//模糊查询
            getUnitUser : getUnitUser
            
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
        			 vm.isSubmit=false;
        			 vm.gridOptions.dataSource.read();
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

        
        function getUnitUser(vm, fn){
        	var httpOptions={
        		method:'post',
        		url: rootPath+ '/assistUnit/unitUser',
        		params:{assistUnitID:vm.id}
        	}
        	
        	var httpSuccess=function success(response){
        		vm.resource.count=response.data.count;
        	
        		fn && fn.apply(vm);
        	}
        	common.http({
        		vm:vm,
        		$http:$http,
        		httpOptions:httpOptions,
        		success:httpSuccess
        	});
        
        }
        // begin#grid
        function grid(vm) {

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_assistUnit+"/fingByOData?$orderby=unitSort",$("#assistUnitform")),
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
                {
                    field: "phoneNum",
                    title: "电话号码",
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

(function () {
    'use strict';

    angular.module('app').controller('assistUnitUserCtrl', assistUnitUser);

    assistUnitUser.$inject = ['$location', 'assistUnitUserSvc'];

    function assistUnitUser($location, assistUnitUserSvc) {
        var vm = this;
        vm.title = '协审单位人员';

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    assistUnitUserSvc.deleteAssistUnitUser(vm, id);
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
        
        vm.queryAssistUnitUser=function(){
        	assistUnitUserSvc.queryAssistUnitUser(vm);
        }

        activate();
        function activate() {
            assistUnitUserSvc.grid(vm);
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('assistUnitUserEditCtrl', assistUnitUser);

    assistUnitUser.$inject = ['$location', 'assistUnitUserSvc', '$state'];

    function assistUnitUser($location, assistUnitUserSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加协审单位用户';
        vm.isUserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新协审单位用户';
        }

        vm.create = function () {
            assistUnitUserSvc.createAssistUnitUser(vm);
        };
        vm.update = function () {
            assistUnitUserSvc.updateAssistUnitUser(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                assistUnitUserSvc.getAssistUnitUserById(vm);
            }
            
            assistUnitUserSvc.getAssistUnit(vm);
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').factory('assistUnitUserSvc', assistUnitUser);

    assistUnitUser.$inject = ['$http'];

    function assistUnitUser($http) {
        var url_assistUnitUser = rootPath + "/assistUnitUser", url_back = '#/assistUnitUser';
        var service = {
            grid: grid,
            getAssistUnitUserById: getAssistUnitUserById,
            createAssistUnitUser: createAssistUnitUser,
            deleteAssistUnitUser: deleteAssistUnitUser,
            updateAssistUnitUser: updateAssistUnitUser,
            getAssistUnit: getAssistUnit,		//获取协审单位
            queryAssistUnitUser:queryAssistUnitUser		//模糊查询
        };

        return service;

        // begin#updateAssistUnitUser
        function updateAssistUnitUser(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid && vm.isUserExist==false) {
                vm.isSubmit = true;
                vm.assistUnitUser.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_assistUnitUser,
                    data: vm.assistUnitUser
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
                                    location.href=url_back;
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

        // begin#deleteAssistUnitUser
        function deleteAssistUnitUser(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_assistUnitUser,
                data: id
            };

            var httpSuccess = function success(response) {
            	   common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.isSubmit = false;
                        vm.gridOptions.dataSource.read();
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

        // begin#createAssistUnitUser
        function createAssistUnitUser(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid && vm.isUserExist==false) {
                vm.isSubmit = true;

                var httpOptions = {
                    method: 'post',
                    url: url_assistUnitUser,
                    data: vm.assistUnitUser
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

        // begin#getAssistUnitUserById
        function getAssistUnitUserById(vm) {
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/assistUnitUser/html/findById",
                params:{id:vm.id}
            };
            var httpSuccess = function success(response) {
                vm.assistUnitUser = response.data;
                vm.assistUnitDto=vm.assistUnitUser.assistUnitDto;
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });                       
        }
        
        function getAssistUnit(vm){
        	var httpOptions={
        		method: 'post',
        		url: rootPath+"/assistUnitUser/getAssistUnit"
        	}
        	var httpSuccess=function success(response){
	        	vm.assistUnit={},
	        	vm.assistUnit=response.data;
        	
        	}
        	
        	common.http({
        		vm:vm,
	        	$http:$http,
	        	httpOptions:httpOptions,
	        	success:httpSuccess
        	});
        }

             // begin#grid
        function grid(vm) {

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_assistUnitUser+"/findByOData",$("#assistUnitUserform")),
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
            
            //序号
            var dataBound=function(){
            var rows=this.items();
            var page=this.pager.page() - 1;
            var pagesize=this.pager.pageSize();
            $(rows).each(function(){
            var index=$(this).index() + 1 + page * pagesize;
            var rowLabel=$(this).find(".row-number");
            $(rowLabel).html(index);
            });
            
            }//end 序号

            // Begin:column
            var columns = [
                {
                    template: function (item) {
                        return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
                            item.id)
                    },
                    filterable: false,
                    width: 10,
                    title: "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
                },
                {
                    field: "rowNumber",
                    title: "序号",
                    width: 20,
                    filterable: false,
                    template:"<span class='row-number'></span>"
                },
                {
                    field: "userName",
                    title: "名称",
                    width: 100,
                    filterable: false
                },
                {
                    field: "phoneNum",
                    title: "电话号码",
                    width: 100,
                    filterable: false
                },
                {
                    field: "position",
                    title: "职位",
                    width: 100,
                    filterable: false
                },
                {
                    field: "assistUnit.unitName",
                    title: "协审单位",
                    width: 100,
                    filterable: false
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
                dataBound:dataBound,
                columns: columns,
                resizable: true
            };

        }// end fun grid
        
        function queryAssistUnitUser(vm){
        	 vm.gridOptions.dataSource.read();
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
            getPlanSignByPlanId : getPlanSignByPlanId,				//通过协审计划id或取协审项目信息
            savePlanSign : savePlanSign,						//保存协审项目信息
            savePlan : savePlan,									//保存协审计划
            initPlanByPlanId : initPlanByPlanId,					//初始化协审计划
            chooseAssistUnit : chooseAssistUnit,							//选择协审单位
            initAssistUnit : initAssistUnit
            
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
	        var httpOptions={
	        	method:'get',
	        	url:rootPath+'/assistPlanSign/getPlanSignByPlanId',
	        	params:{planId:planId}
	        }
	        var httpSuccess=function success(response){
	        	vm.assistPlanSign=response.data;
	        	vm.review=vm.assistPlanSign.length;
	        	console.log(vm.review);
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
        	
        	var httpOptions={
        		method:"put",
        		url: rootPath +"/assistPlanSign/savePlanSign",
        		headers:{
                 "contentType":"application/json;charset=utf-8"  //设置请求头信息
              },
			  dataType : "json",
			  data:angular.toJson(vm.assistPlanSign)
        	}
        	
        	console.log(vm.assistPlanSign);
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
        		params:{planId:vm.planId,number:vm.number}
        	}
        	
        	var httpSuccess=function success(response){
        		vm.unitList=response.data;
        		console.log(vm.unitList);
//        		vm.isChoose=true;
        	}
        	
        	common.http({
        		vm: vm,
        		$http: $http,
        		httpOptions: httpOptions,
        		success: httpSuccess
        	});
        }//end chooseAssistUnit
        
        //begin initAssistUnit
        function initAssistUnit(vm,planId){
        	
        	var httpOptions={
        		method:"get",
        		url:rootPath+'/assistUnit/getAssistUnitByPlanId',
        		params:{planId:planId}
        	}
        	
        	var httpSuccess=function success(response){
        		vm.unitList=response.data;
        		vm.signNum=vm.unitList.length;
        		if(vm.unitList.length>0){
//        			vm.isChoose=true;
        		}
        		
        	}
        	
        	common.http({
        		vm: vm,
        		$http: $http,
        		httpOptions: httpOptions,
        		success: httpSuccess
        	});
        	
        }
        //end initAssistUnit

	}		
})();
(function () {
    'use strict';

    angular.module('app').controller('unitAndUserCtrl', unitAndUser);

    unitAndUser.$inject = ['$location','unitAndUserSvc','$state']; 

    function unitAndUser($location, unitAndUserSvc,$state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '人员列表';
        vm.id=$state.params.id;
        
        vm.showAddUserDialog=function(){
        	$('.addUser').modal({
        		backdrop:'static',
        		keyboard: false
        	});
        	 vm.unitAndUserGrid.dataSource.read();
        }
        
         vm.closeAddUserDialog=function(){
        	$('.addUser').modal('hide');		
        	
        }
        
        vm.add=function(assistUnitUserId){
        
      	  unitAndUserSvc.addUser(vm,assistUnitUserId);
        }
        
        vm.remove=function(assistUnitUserId){
        	 unitAndUserSvc.removeUser(vm,assistUnitUserId);
        }
        
        vm.removes=function(){
        	var selectIds=common.getKendoCheckId('.unitAndUserGrid');
        	if(selectIds.length==0){
        		common.alert({
        			vm:vm,
        			msg:"请选择数据"
        		});
        	}else{
        		var ids=[];
        		for(var i=0;i<selectIds.length;i++){
        			ids.push(selectIds[i].value);
        		}
        		var idStr=ids.join(',');
        		vm.remove(idStr);
        	}
        }
        
        
        activate();
        function activate() {
            unitAndUserSvc.unitAndUserGrid(vm);
            unitAndUserSvc.allUserGrid(vm);
        }
    }
})();

(function() {
	'use strict';

	angular.module('app').factory('unitAndUserSvc', unitAndUser);

	unitAndUser.$inject = [ '$http','$compile' ];	
	function unitAndUser($http,$compile) {	
		var url_unitAndUser=rootPath+ '/assistUnit/unitUser';
		var url_userNotIn=rootPath + '/assistUnit/userNotIn';
		
			
		var service = {	
			unitAndUserGrid : unitAndUserGrid,	//获取单位所有人员
			allUserGrid : allUserGrid,			//获取所有非本单位用户
			addUser : addUser,		//添加人员
			removeUser : removeUser	//移除成员
		};		
		return service;	
		
		//begin addUser
		function addUser(vm,assistUnitUserId){
			var httpOptions={
				method:'post',
				url:rootPath+ '/assistUnit/addUser',
				params:{
					unitId:vm.id,
					userId:assistUnitUserId
				}
			}
			 var httpSuccess=function success(response){
			 
			 	common.requestSuccess({
			 	
			 		vm:vm,
			 		response:response,
			 		fn:function(){
			 			vm.unitAndUserGrid.dataSource.read();
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
		
		}//end addUser
		
		
		//begin removeUser
		function removeUser(vm,assistUnitUserId){
			var httpOptions={
				method:'delete',
				url: rootPath+ '/assistUnit/removeUser',
				params:{
					unitId: vm.id,
					userId: assistUnitUserId
				}
			}
			
			var httpSuccess=function success(response){
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function(){
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
		
		}//end removeUser
		
		//begin 
		function unitAndUserGrid(vm){
		
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_unitAndUser+"?assistUnitID="+vm.id),
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
						field : "userName",
						title : "名称",
						width : 200,
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
		
		}//end
		
		//begin 
		function allUserGrid(vm){
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_userNotIn+"?assistUnitID="+vm.id),
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
						field : "userName",
						title : "名称",
						width : 200,
						filterable : true
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

			vm.unitAndUserGrid = {
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

	var service = {
			initJqValidation: initJqValidation,//重置form验证
			requestError: requestError,//请求错误时执行
			requestSuccess: requestSuccess,//请求成功时执行
			format: format,//string格式化
			blockNonNumber: blockNonNumber,//只允许输入数字
			floatNumberInput: floatNumberInput,
			adminContentHeight: adminContentHeight,//当前Content高度
			alert: alertDialog,//显示alert窗口
			confirm: confirmDialog,//显示Confirm窗口
			getQuerystring: getQuerystring,//取得Url参数
			kendoGridConfig: kendoGridConfig,//kendo grid配置
			getKendoCheckId: getKendoCheckId,//获得kendo grid的第一列checkId
			cookie: cookie,//cookie操作
			getToken:getToken,//获得令牌
			appPath: "",//app路径
			http: http,//http请求    
			gridDataSource: gridDataSource,//gridDataSource
			loginUrl: window.rootPath +'/home/login',
			buildOdataFilter:buildOdataFilter,	//创建多条件查询的filter
			initDictData : initDictData, 	//初始化数字字典
			kendoGridDataSource : kendoGridDataSource, 	//获取gridDataSource
			initUploadOption : initUploadOption,		//附件上传参数
			getTaskCount : getTaskCount	,				//用户待办总数
			initIdeaData:initIdeaData,			//初始化选择意见窗口数据
			deleteCommonIdea:deleteCommonIdea,	//删除常用意见
			addCommonIdea:addCommonIdea,		//添加常用意见
			saveCommonIdea:saveCommonIdea,		//保存常用意见
			addCorrentIdea:addCorrentIdea,		//添加当前意见
			saveCurrentIdea:saveCurrentIdea		//绑定当前意见
			
	};
	window.common = service;

	function initJqValidation(formObj) {
		if(formObj){
			formObj.removeData("validator");
			formObj.removeData("unobtrusiveValidation");
			$.validator.unobtrusive.parse(formObj);
		}else{
			$("form").removeData("validator");
			$("form").removeData("unobtrusiveValidation");
			$.validator.unobtrusive.parse("form");
		}     
	}   
	function requestError(options) {    	
		var message = '发生错误,系统已记录,我们会尽快处理！';
		if (options.response != undefined) {
			if (options.response.status == 401) {
				location.href = service.loginUrl;
			}
			message = options.response.data.message || message;
		}       
		service.alert({
			vm:options.vm,
			msg:message,
			fn:function() {
				options.vm.isSubmit = false;
				options.vm.disabledButton = false;
				$('.alertDialog').modal('hide');							
			}
		});
	}
	function requestSuccess(options) {    
		var showError=function(msg){ 
			service.alert({
				vm:options.vm,
				msg:msg,
				fn:function() {
					options.vm.isSubmit = false;
					$('.alertDialog').modal('hide');							
				}
			});
		};
		if (options.response.status > 400) {          
			showError( "发生错误！");

		} else {          	
			var data = options.response.data;        	
			if(data&&data.status==555){        		
				showError(data.message);
			}
			else if (options.fn) {
				options.fn(data);
			}
		}
	}
	function format() {
		var theString = arguments[0];

		// start with the second argument (i = 1)
		for (var i = 1; i < arguments.length; i++) {
			// "gm" = RegEx options for Global search (more than one instance)
			// and for Multiline search
			var regEx = new RegExp("\\{" + (i - 1) + "\\}", "gm");
			theString = theString.replace(regEx, arguments[i]);
		}       
		return theString;
	}
	function blockNonNumber(val) {
		var str = val.toString().replace(/[^0-9]/g, '');
		return parseInt(str, 10);
	}
	function floatNumberInput(val) {
		return isNaN(parseFloat(val, 10)) ? 0 : parseFloat(val, 10);
	}
	function adminContentHeight() {
		return $(window).height() - 180;
	}
	function alertDialog(options) {

		//$('.alertDialog').modal('hide');//bug:backdrop:static会失效
		options.vm.alertDialogMessage = options.msg;
		options.vm.alertDialogFn = function () {
            if(options.closeDialog && options.closeDialog == true){
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
			keyboard:false
		});
	}
	function confirmDialog(options) {    	
		options.vm.dialogConfirmTitle = options.title;
		options.vm.dialogConfirmMessage = options.msg;
		$('.confirmDialog').modal({ backdrop: 'static' });
		options.vm.dialogConfirmSubmit = options.fn;
		if(options.cancel){
            options.vm.dialogConfirmCancel = options.cancel;
		}else{
            options.vm.dialogConfirmCancel = function(){
                $('.confirmDialog').modal('hide');
            }
        }

	}
	function getQuerystring(key, default_) {
		if (default_ == null) default_ = "";
		key = key.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
		var regex = new RegExp("[\\?&]" + key + "=([^&#]*)");
		var qs = regex.exec(window.location.href);
		if (qs == null)
			return default_;
		else
			return qs[1];
	}

	function kendoGridDataSource(url,searchForm){
		var dataSource = new kendo.data.DataSource({
			type : 'odata',
			transport : kendoGridConfig().transport(url,searchForm),
			schema : kendoGridConfig().schema({
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
		return dataSource;
	}
	function kendoGridConfig() {
		return {
			filterable: {
				extra: false,
				//mode: "row", 将过滤条件假如title下,如果不要直接与title并排
				operators: {
					string: {
						"contains": "包含",
						"eq": "等于"
							//"neq": "不等于",                        
							//"doesnotcontain": "不包含"
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
					total: function (data) {return data['count']; },
					model: model
				};
			},
			transport: function (url,form,paramObj) {
				return {
					read: {
						url: url,
						dataType: "json",
						type: "post",
						beforeSend: function (req) {                            
							req.setRequestHeader('Token', service.getToken());
						},
						data : function(){
							if(form){
								var filterParam = common.buildOdataFilter(form);
								if(filterParam){
									if(paramObj && paramObj.filter){
										return {"$filter":filterParam+" and "+paramObj.filter};
									} else{
										return {"$filter":filterParam};
									}                      		
								}else{
									if(paramObj && paramObj.filter){
										return {"$filter":paramObj.filter};
									}else{
										return {};
									}                       		
								}
							}else{
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
	}

	function getKendoCheckId($id) {
		var checkbox = $($id).find('tr td:nth-child(1)').find('input:checked')
		var data = [];
		checkbox.each(function () {
			var id = $(this).attr('relId');
			data.push({ name: 'id', value: id });
		});
		return data;
	}

	function http(options) {
		options.headers = { Token: service.getToken()};
		options.$http(options.httpOptions).then(options.success, function (response) { 
			if(options.onError){
				options.onError(response);
			}
			common.requestError({        		
				vm:options.vm,
				response:response
			});         	       	
		});
	}

	//begin:cookie
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
					var cookieName = encodeURIComponent(name) + "=",
					cookieStart = document.cookie.indexOf(cookieName),
					cookieValue = null,
					result = {};
					if (cookieStart > -1) {
						var cookieEnd = document.cookie.indexOf(";", cookieStart)
						if (cookieEnd == -1) {
							cookieEnd = document.cookie.length;
						}
						cookieValue = document.cookie.substring(cookieStart + cookieName.length, cookieEnd);
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
						if (subName.length > 0 && subcookies.hasOwnProperty(subName)) {
							subcookieParts.push(encodeURIComponent(subName) + "=" + encodeURIComponent(subcookies[subName]));
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
	//end:cookie

	function getToken() {
		var data = cookie().getAll("data");
		return data != null ? data.token : "";
	}

	function gridDataSource(dataSource) {
		dataSource.error = function (e) {
			if (e.status == 401) {
				location.href = service.loginUrl;
			}else{

			}
		};        
		return dataSource;
	}

	//S_封装filer的参数
	function buildOdataFilter(from){
		/*var t = new Array();
		var arrIndex = 0;
		$(from).find('input,radio,select,textarea').each(function(index,obj){
			if(obj.name && obj.value){
				var param = {};
				if($(this).attr('operator')){
					param.operator = $(this).attr('operator');
				}else{
					param.operator = 'eq';
				}	
				param.name = $.trim(obj.name);
				param.value = $.trim(obj.value);
				t[arrIndex] = param;
				arrIndex++;
			} 		
		});   	 

		var i = 0;
		var filterStr = "";
		$.each(t, function() {
			if(this.value){
				if(i > 0){
					filterStr += " and ";
				}
				filterStr += this.name + " " + this.operator + " '"+ this.value +"'";
				i++;
			}		    	
		});
		return filterStr;*/

        var manipulation_rcheckableType = /^(?:checkbox|radio)$/i,
            rsubmitterTypes = /^(?:submit|button|image|reset|file)$/i,
            rsubmittable = /^(?:input|select|textarea|keygen)/i;

        return $(from).map(function () {
            var elements = jQuery.prop(this, "elements");
            return elements ? jQuery.makeArray(elements) : this;
        }).filter(function () {
            var type = this.type;
            return this.value && this.name && !$(this).is(":disabled") &&
                rsubmittable.test(this.nodeName) && !rsubmitterTypes.test(type) &&
                ( this.checked || !manipulation_rcheckableType.test(type));
        }).map(function (i, elem) {
            var $me = $(this), val = $me.val();
            if(!val) return false;
            val = "'" + val + "'";
            var operator = $me.attr("operator") || "eq",
                dataRole = $me.attr("data-role") || "";   //data-role="datepicker"
            if(dataRole == "datepicker") {
                val = "date" + val;
            } else if(dataRole == "datetimepicker") {
                val = "datetime" + val;
            }

            return operator == "like" ? ("substringof(" + val + ", "+ elem.name + ")") : (elem.name + " " + operator + " " + val);
        }).get().join(" and ");
	}//E_封装filer的参数 

	function initDictData(options){
		options.$http({
			method : 'get',
			url : rootPath+'/dict/dictItems'
		}).then(function(response){
			options.scope.dictMetaData = response.data;
			var dictsObj = {};
			reduceDict(dictsObj,response.data);
			options.scope.DICT = dictsObj;
		}, 
		function (response) {         
			alert('初始化数据字典失败');
		});
	}

	function reduceDict(dictsObj,dicts,parentId){
		if(!dicts||dicts.length == 0){
			return ;
		}
		if(!parentId){
			//find the top dict
			for(var i = 0;i<dicts.length;i++){
				var dict = dicts[i];

				if(!dict.parentId){   				
					dictsObj[dict.dictCode] = {};
					dictsObj[dict.dictCode].dictId = dict.dictId;
					dictsObj[dict.dictCode].dictCode = dict.dictCode;
					dictsObj[dict.dictCode].dictName = dict.dictName;  
					dictsObj[dict.dictCode].dictKey = dict.dictKey; 
					dictsObj[dict.dictCode].dictSort=dict.dictSort;

					reduceDict(dictsObj[dict.dictCode],dicts,dict.dictId);
				}
			}
		}else{
			//find sub dicts  		
			for(var i = 0;i<dicts.length;i++){
				var dict = dicts[i];
				if(dict.parentId && dict.parentId == parentId){
					if(!dictsObj.dicts){
						dictsObj.dicts = new Array();
					}
					var subDict = {};
					subDict.dictId = dict.dictId;
					subDict.dictName = dict.dictName;
					subDict.dictCode = dict.dictCode;
					subDict.dictKey = dict.dictKey;
					subDict.dictSort=dict.dictSort;
					dictsObj.dicts.push(subDict);
					//recruce
					reduceDict(subDict,dicts,dict.dictId);
				}
			}
		}
	}

	//S_附件上传参数初始化
	function initUploadOption(options){
		return {
			async: {
				saveUrl: rootPath + "/file",
				removeUrl: rootPath + "/file/delete",
				autoUpload: false
			},
			select: function(e){
				if(options.onSelect){
					options.onSelect(e)
				}else{
					$.each(e.files, function (index, value) {
						console.log("Name: " + value.name+"Size: " + value.size + " bytes" + "Extension: " + value.extension);
					});
				}
			},
			upload: function(e){
				if(options.onUpload){
					options.onUpload(e)
				}else{
					var files = e.files;
					console.log(e.response)
				}
			},
			success: function(e){
				if(options.onSuccess){
					options.onSuccess(e)
				}else{
					var files = e.files;	        		                
					if (e.operation == "upload") {
						files[0].sysFileId = e.response.sysFileId;	                	
					}
				}
			},
			remove: function(e){
				if(options.onRemove){
					options.onRemove(e)
				}else{
					var files = e.files;
					e.data = {'sysFileId':files[0].sysFileId};
				}
			}
		}
	}//E_附件上传参数初始化

	//S_获取待办总数
	function getTaskCount(options){
		options.$http({
			method : 'get',
			url : rootPath+'/flow/html/tasksCount'
        }).then(function(response){
        	$('#GtasksCount').html(response.data);
        });
    }//E_获取待办总数
    
    //init
    init();
    function init() {
    	//begin#grid 处理
    	//全选
        $(document).on('click', '#checkboxAll', function () {
            var isSelected = $(this).is(':checked');
            $('.grid').find('tr td:nth-child(1)').find('input:checkbox').prop('checked', isSelected);
        });
        //点击行，改变背景
        $('body').on('click', '.grid tr', function (e) {
            $(this).parent().find('tr').removeClass('selected');
            $(this).addClass('selected');
            //$(this).find('td:nth-child(1)').find('input').prop('checked', true);
            //$(this).find('td:nth-child(2)').find('input').prop('checked', true);
        })
        //end#grid 处理
    }
    

    //初始化常用意见
    function initIdeaData(vm,$http,options){
    	vm.ideaContent='';//初始化当前意见
    	vm.$http=$http;
    	vm.i=1;    	
    	
    	var ideaEditWindow=$("#ideaWindow");
    	ideaEditWindow.kendoWindow({
    		width:"50%",
    		height:"80%",
    		title:"意见选择",
    		visible:false,
    		modal:true,
    		closable:true,
    		actions:["Pin","Minimize","Maximize","close"]
    	}).data("kendoWindow").center().open();
    	
    	vm.$http({
    		method:'get',
    		url:rootPath+"/idea"
    	}).then(function(response){
    		vm.commonIdeas=response.data;
    		
    		vm.deleteCommonIdea=function(){//删除常用意见
    			deleteCommonIdea(vm);
    		};
    		
    		vm.addCorrentIdea=function(ideaContent){//添加当前意见
    			addCorrentIdea(vm,ideaContent);
    		};
    		
    		vm.addCommonIdea=function(){//添加常用意见
    			addCommonIdea(vm);
    		}
    		
    		vm.saveCommonIdea=function(){//保存常用意见
    			
    			saveCommonIdea(vm);
    		}
    		
    		vm.saveCurrentIdea=function(){
    			saveCurrentIdea(vm,options);
    		}
    	});
    	
    }
    
    function deleteCommonIdea(options){
    	var isCheck=$("#commonIdeaTable input[name='ideaCheck']:checked");
    	if(isCheck.length<1){
    		alert("请选择要删除的意见！");
    	}else{
    		var ids=[];
    		for(var i=0;i<isCheck.length;i++){
    			options.commonIdeas.forEach(function (c,number){
    				if(isCheck[i].value==c.ideaID || c.ideaID==undefined){
    					options.commonIdeas.splice(number,1);
    				}
    				ids.push(isCheck[i].value);
    			});
    		}
    		var idsStr=ids.join(",");
    		
    		options.$http({
    			method:'delete',
    			url:rootPath+'/idea',
    			params:{ideas:idsStr}
    		})

		}
    }//end
    
    function addCorrentIdea(options,ideaContent){
    
    	options.ideaContent=options.ideaContent+ideaContent;
    }//end
    
    function addCommonIdea(options){
    	options.commonIdea={};
    	options.commonIdea.ideaType="个人常用意见";
    	options.commonIdeas.push(options.commonIdea);
    	options.i++;
    }//end 
    
    function saveCommonIdea(options){
    	
    	options.$http({
    		 method:'post',
			  url:rootPath+"/idea",
			  headers:{
                 "contentType":"application/json;charset=utf-8"  //设置请求头信息
              },
			  dataType : "json",
			  data:angular.toJson(options.commonIdeas)
    	}).then(function(response){
    		alert("保存成功！");
    	});
    }//end
    
 
    function saveCurrentIdea(vm,options){
    	var targetObj = $("#"+options.targetId);
    	targetObj.val(targetObj.val() + vm.ideaContent);
		window.parent.$("#ideaWindow").data("kendoWindow").close();
    }//end
    
    
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
					    width: 70,
					    filterable : false,
					    template: "<span class='row-number'></span>"  
					 }
					,
					{
						field : "coName",
						title : "单位名称",
						width : 260,						
						filterable : false
					},
					{
						field : "coPhone",
						title : "单位电话",
						width : 160,						
						filterable : false
					},
					{
						field : "coPC",
						title : "邮编",
						width : 160,						
						filterable : false
					},
					{
						field : "coAddress",
						title : "地址",
						width : 160,						
						filterable : false
					},
					{
						field : "coSite",
						title : "网站",
						width : 160,						
						filterable : false
					},
					{
						field : "coSynopsis",
						title : "单位简介",
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

    angular.module('app').controller('demoCtrl', demo);

    demo.$inject = ['$location', 'demoSvc'];

    function demo($location, demoSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '';

        vm.showDialog = function () {

            $('.myDialog').modal({
                backdrop: 'static',
                keyboard: false
            });
        }

        // function datetimePicker() {
            // $("#datepicker").kendoDatePicker();
            // $("#datetiempicker").kendoDateTimePicker();
        // }

        function upload() {
            $("#files").kendoUpload({
                async: {
                    saveUrl: rootPath + "/demo/save",
                    removeUrl: rootPath + "/demo/remove",
                    autoUpload: true
                }
            });
        }


        activate();
        function activate() {
            // datetimePicker();
            upload();
        }
    }
})();

(function() {
	'use strict';

	angular.module('app').factory('demoSvc', demo);

	demo.$inject = [ '$http' ];

	function demo($http) {
		var url_account_password = "/account/password";
		
		var service = {			
			upload : upload
		};

		return service;

		// begin#updatedemo
		function upload(vm) {
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
(function() {
	'dispatch strict';

	angular.module('app').controller('dispatchEditCtrl', dispatch);

	dispatch.$inject = ['$location', 'dispatchSvc', '$state'];

	function dispatch($location, dispatchSvc, $state) {
		var vm = this;
		vm.title = '项目发文编辑';
		vm.isHide = true;
		vm.isHide2 = true;
		vm.saveProcess = false;
		vm.showFileNum = false;
		vm.mwindowHide = true;
		vm.showCreate = false;
		vm.linkSignId = " ";
		vm.sign = {};
		vm.dispatchDoc = {};
		vm.dispatchDoc.signId = $state.params.signid;

		// 创建发文
		vm.create = function() {
			dispatchSvc.saveDispatch(vm);
		}
		// 核减（增）/核减率（增）计算
		vm.count = function() {
			var declareValue = vm.dispatchDoc.declareValue;
			var authorizeValue = vm.dispatchDoc.authorizeValue;
			if (declareValue && authorizeValue) {
				var dvalue = declareValue - authorizeValue;
				var extraRate = ((dvalue / declareValue).toFixed(4)) * 100;
				vm.dispatchDoc.extraRate = extraRate;
				vm.dispatchDoc.extraValue = dvalue;
			}
		}

		/*
		 * if(vm.dispatchDoc.dispatchWay=="1"||vm.dispatchDoc.isMainProject=="0"){
		 * vm.dispatchDoc.isRelated="否"; }
		 */

		vm.isSelect = function() {
			if (vm.dispatchDoc.dispatchWay == "1") {
				vm.dispatchDoc.isRelated = "否";
			}
		}

		vm.sigleProject = function() {
			common.confirm({
				title : "删除提示",
				vm : vm,
				msg : "你确定删除关联信息?",
				fn : function() {
					if (vm.dispatchDoc.dispatchWay == "1" && vm.dispatchDoc.id) {
						vm.dispatchDoc.isRelated = "否";
						dispatchSvc.deletemerge(vm);
					}
				}
			});
		}

		vm.isrelated = function() {
			vm.dispatchDoc.isRelated = "是";

		}

		vm.isrelated2 = function() {
			common.confirm({
				title : "删除提示",
				vm : vm,
				msg : "你确定删除关联信息?",
				fn : function() {
					if (vm.dispatchDoc.id) {
						vm.dispatchDoc.isRelated = "否";
						dispatchSvc.deletemerge(vm);
			}
				}
			});
			
		}

		// 打开合并页面
		vm.gotoMergePage = function() {
			dispatchSvc.gotoMergePage(vm);
		}

		vm.search = function() {
			dispatchSvc.getSign(vm);
		}

		// 选择待选项目
		vm.chooseProject = function() {
			dispatchSvc.chooseProject(vm);
		}

		// 取消选择
		vm.cancelProject = function() {
			dispatchSvc.cancelProject(vm);
		}

		// 关闭窗口
		vm.onClose = function() {
			window.parent.$("#mwindow").data("kendoWindow").close();
		}

		// 确定合并
		vm.mergeDispa = function() {
			dispatchSvc.mergeDispa(vm);
		}

		vm.formReset = function() {
			var values = $("#searchform").find("input,select");
			values.val("");
		}

		vm.search = function() {
			dispatchSvc.getsign(vm);
		}
		// 生成文件字号
		vm.fileNum = function() {
			dispatchSvc.fileNum(vm);
		}
		activate();
		function activate() {
			dispatchSvc.initDispatchData(vm);
		}
	}
})();

(function() {
	'dispatch strict';
	
	angular.module('app').factory('dispatchSvc', dispatch);
	
	dispatch.$inject = ['$rootScope','$http'];

	function dispatch($rootScope,$http) {
		var service = {
			initDispatchData : initDispatchData,		//初始化流程数据	
			saveDispatch : saveDispatch,				//保存
			gotoMergePage : gotoMergePage,               //打开合并发文页面
			chooseProject : chooseProject,			     //选择待选项目
			getsign : getsign,							 //显示待选项目
			cancelProject : cancelProject,				 //取消选择
			mergeDispa : mergeDispa,					 //合并发文
			fileNum : fileNum,                           //生成文件字号
			getselectedSign : getselectedSign,
			getSeleSignBysId : getSeleSignBysId,
			deletemerge : deletemerge,                   //删除关联信息
			getRelatedFileNum : getRelatedFileNum,        //获取关联文件字号
			initDisptUpload:initDisptUpload,				//初始化上传附件
			delDisptSysFile:delDisptSysFile,			//系统文件删除
		};
		return service;			
		
		function delDisptSysFile(vm,id){
			var httpOptions = {
					method : 'delete',
					url : rootPath+"/file/deleteSysFile",
					data : id

				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn:function(){		
							window.parent.$("#dispatchqueryWin").data("kendoWindow").close();
							common.alert({
								vm:vm,
								msg:"删除成功",
								fn:function() {
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
		//S_初始化上传附件
		function initDisptUpload(vm){
			var businessId = vm.dispatchDoc.signId;
            var projectfileoptions = {
                language : 'zh',
                allowedPreviewTypes : ['image'],
                allowedFileExtensions : [ 'jpg', 'png', 'gif',"xlsx","docx" ,"pdf"],
                maxFileSize : 2000,
                showRemove: false,
                uploadUrl:rootPath + "/file/fileUpload",
                uploadExtraData:{businessId:businessId}
            };
            $("#dispatchphotofile").fileinput(projectfileoptions).on("filebatchselected", function(event, files){

            }).on("fileuploaded", function(event, data) {
                $("#dispatchPhotoSrc").removeAttr("src");
                $("#dispatchPhotoSrc").attr("src",rootPath+"/sign/transportImg?signid="+businessId+"&t="+Math.random());
            });
		}
		//E 初始化上传附件
		
		function fileNum(vm){
			vm.isSubmit = true;
			if(!vm.dispatchDoc.id){
				common.alert({
					vm:vm,
					msg:"请先保存再生成文件字号",
					fn:function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
					}
				})	
				return;
			}
			
			var httpOptions = {
					method : 'post',
					url : rootPath+"/dispatch/fileNum",
					params:{dispaId:vm.dispatchDoc.id}
				}
			
			var httpSuccess = function success(response) {	
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.showFileNum=false;
						vm.isSubmit = false;
						vm.dispatchDoc.fileNum=response.data;	
						common.alert({ 
							vm : vm,
							msg : "操作成功"
						})	
					}
				
				});
			}

			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess,
				onError: function(response){vm.isSubmit = false;}
			});
		}
		
		
		// begin#gotoWPage
		function gotoMergePage(vm) {
			vm.mwindowHide=false;
			if(!vm.dispatchDoc.id){
				common.alert({
					vm:vm,
					msg:"请先保存再进行关联！",
					fn:function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
					}
				})	
				return;
			}
			var selectValue=$("#isRelated").find("option:selected").text();
			if(selectValue=="否"){
				common.alert({
					vm:vm,
					msg:"你已选择未关联,请重新选择再进行关联！",
					fn:function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
					}
				})	
				return;
			}
			
			var WorkeWindow = $("#mwindow");
			WorkeWindow.kendoWindow({
				width : "1200px",
				height : "630px",
				title : "合并发文",
				visible : false,
				modal : true,
				closable : true,
				actions : [ "Pin", "Minimize", "Maximize", "Close" ]
			}).data("kendoWindow").center().open();
			
			getSeleSignBysId(vm);
			getsign(vm);
			
		}
		// end#gotoWPage
		
		//S_初始化
		function initDispatchData(vm){
			if(vm.dispatchDoc.id!=null){
			  getRelatedFileNum(vm);
			}
			var httpOptions = {
					method : 'get',
					url : rootPath+"/dispatch/initData",
					params : {signid : vm.dispatchDoc.signId}						
				}
			
			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {
						vm.dispatchDoc=response.data.dispatch;
						vm.proofread = response.data.mainUserList;
						vm.org = response.data.orgList;
						 vm.showCreate=true;
						//初始化获取合并发文关联的linkSignId
						vm.linkSignId=response.data.linkSignId;
//						console.log(vm.dispatchDoc);
						
						if( vm.dispatchDoc.id&&!vm.dispatchDoc.fileNum){
				        	 vm.showFileNum=true;
				        }
						vm.sysfilelist = response.data.sysfilelist;
						initDisptUpload(vm);
						$("#dispatchPhotoSrc").attr("src",rootPath+"/sign/transportImg?signId="+vm.dispatchDoc.signId+"&t="+Math.random());
					}		
				})
			}
			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		
		}//E_初始化
		
		function mergeDispa(vm){
			var httpOptions = {
					method : 'get',
					url : rootPath+"/dispatch/mergeDispa",
					params : {signId : vm.dispatchDoc.signId,linkSignId : vm.linkSignId}						
				}
			
			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function(){		
						window.parent.$("#mwindow").data("kendoWindow").close();
						common.alert({
							vm:vm,
							msg:"操作成功！",
							fn:function() {
								$('.alertDialog').modal('hide');
								$('.modal-backdrop').remove();
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
		
		
		//S_保存
		function saveDispatch(vm){
			common.initJqValidation($("#dispatch_form"));
			var isValid = $("#dispatch_form").valid();
			if(isValid){
				//是否关联其它项目判断
				if(vm.dispatchDoc.isMainProject =="9"&&vm.dispatchDoc.id){
						if(vm.linkSignId==" "){
							common.alert({
								vm:vm,
								msg:"请关联其它项目",
								fn:function() {
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
								}
							})	
							return;
						}
					
				}
				var httpOptions = {
						method : 'post',
						url : rootPath+"/dispatch",
						data : vm.dispatchDoc
					}
				var httpSuccess = function success(response) {									
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function(){		
							common.alert({
								vm:vm,
								msg:"操作成功！",
								fn:function() {
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
									 //vm.showFileNum=true;
									
									//初始化数据获得保存后的数据
									initDispatchData(vm);
									//$rootScope.back();	//返回到流程页面
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
					//onError: function(response){vm.saveProcess = false;}
				});
				
			}
		}//E_保存
		
		//begin##chooseProject
		function chooseProject(vm){
			var idStr=vm.linkSignId;
			var linkSignId=$("input[name='checksign']:checked");
			var ids=[];
			if(linkSignId.length!=0){
				 $.each(linkSignId, function(i, obj) {
					ids.push(obj.value);
				 });
				 if(idStr){
					 idStr+=","+ids.join(',');
				 }else{
					 idStr=ids.join(',');
				 }
				 vm.linkSignId=idStr;
				 getselectedSign(vm);
				 getsign(vm);
			}
		}
		//end##chooseProject
		
		//begin##chooseProject
		function cancelProject(vm){
			var idStr=vm.linkSignId;
			var linkSignId=$("input[name='checkss']:checked");
			if(linkSignId.lenght!=0){
				$.each(linkSignId, function(i, obj) {
					if(idStr.lastIndexOf(obj.value)==0){
						idStr=idStr.replace(obj.value,"");
					}else{
						idStr=idStr.replace(","+obj.value,"");
					}
				});
				vm.linkSignId=idStr
				getselectedSign(vm);
				getsign(vm);
			}
		}
		//end##chooseProject
		
		//begin##getSeleSignBysId
		function getSeleSignBysId(vm){
			var httpOptions = {
					method : 'get',
					url : rootPath+"/dispatch/getSignByDId",
					params:{
						bussnessId:vm.dispatchDoc.id
					}
				}
			var httpSuccess = function success(response) {
				vm.selectedSign=response.data.signDtoList;
				console.log(vm.selectedSign);
				vm.linkSignId=response.data.linkSignId;
				console.log(vm.linkSignId);
			} 
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}
		//end##getSeleSignBysId
		
		//获取已选项目
		//begin##getselectedSign
		function getselectedSign(vm){
			
			var httpOptions = {
					method : 'get',
					url : rootPath+"/dispatch/getSelectedSign",
					params:{
						linkSignIds:vm.linkSignId
					}
				}
			var httpSuccess = function success(response) {
				vm.selectedSign=response.data;
			} 
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}
		
		//begin##getsign
		function getsign(vm){
				vm.sign.signid=vm.linkSignId;
				var httpOptions = {
						method : 'post',
						url : rootPath+"/dispatch/getSign",
						data : vm.sign
					}
				var httpSuccess = function success(response) {	
					vm.signs=response.data;
					console.log(vm.signs);
				}
				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess,
					onError: function(response){}
				});
				
		}//end##getsign
		
		
		//begin##deletemerge
		function deletemerge(vm){
				var httpOptions = {
						method : 'post',
						url : rootPath+"/dispatch/deleteMerge",
						params : {dispatchId:vm.dispatchDoc.id}
					}
				var httpSuccess = function success(response) {	
				}
				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess,
					onError: function(response){}
				});
				
		}//end##deletemerge
		
		
		function getRelatedFileNum(vm){
			var httpOptions = {
						method : 'get',
						url : rootPath+"/dispatch/getRelatedFileNum",
						params : {dispatchId:vm.dispatchDoc.id}
					}
				var httpSuccess = function success(response) {	
					vm.dispatchDoc.fileNum=response.data;
				}
				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess,
					onError: function(response){}
				});
		}
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
        
        vm.search = function () {
        	expertSvc.searchMuti(vm);
        };
        
        vm.searchAudit = function () {
        	expertSvc.searchMAudit(vm);
        };
        
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
                    
        activate();
        function activate() {
        	expertSvc.grid(vm);       	
        }
    }
})();

(function () {
    'expert strict';

    angular.module('app').controller('expertEditCtrl', expert);

    expert.$inject = ['$location','projectExpeSvc','workExpeSvc','expertSvc','$state']; 

    function expert($location,projectExpeSvc,workExpeSvc,expertSvc,$state) {
        var vm = this;
        vm.model = {};
        vm.title = '专家信息录入';
        vm.isuserExist=false;
        vm.isHide=true;
        vm.expertID = $state.params.expertID;

        activice();
        function activice(){
            if (vm.expertID) {
                vm.isUpdate = true;
                vm.title = '更新专家';
                vm.isHide=false;
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
        	workExpeSvc.gotoWPage(vm);
        }
        
        vm.updateWork=function(){
        	workExpeSvc.updateWork(vm);
        }
        
        vm.createWork=function(){
        	workExpeSvc.createWork(vm);
        }
        
        vm.saveWork=function(){
        	workExpeSvc.saveWork(vm);
        }
        
        vm.deleteWork=function(){
        	workExpeSvc.deleteWork(vm);
        }
        
        vm.onWClose=function(){
        	window.parent.$("#wrwindow").data("kendoWindow").close();
        }
        
        vm.onPClose=function(){
        	window.parent.$("#pjwindow").data("kendoWindow").close();
        }
        
        vm.gotoJPage=function(){
        	projectExpeSvc.gotoJPage(vm);
        }
        
        vm.updateProject=function(){
        	projectExpeSvc.updateProject(vm);
        }
        
        vm.createProject=function(){
        	projectExpeSvc.createProject(vm);
        }
        
        vm.saveProject=function(){
        	projectExpeSvc.saveProject(vm);
        }
        
        vm.delertProject=function(){
        	projectExpeSvc.delertProject(vm);
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
		var url_back = '#/expert';
		var url_expert = rootPath + "/expert";
		var service = {
			grid : grid,						//初始化综合查询grid
			auditGrid : auditGrid,				//初始化审核页面的所有grid
			getExpertById : getExpertById,
			createExpert : createExpert,
			deleteExpert : deleteExpert,
			updateExpert : updateExpert,
			searchMuti : searchMuti,		//综合查询
			searchAudit : searchAudit,		//审核查询
			repeatGrid : repeatGrid,		//重复专家查询
			updateAudit : updateAudit,		//专家评审
			toAudit : toAudit,				//由个状态回到审核状态
			auditTo : auditTo,				//由审核状态去到各个状态
            initUpload : initUpload,        //初始化附件上传
		};
		return service;				
		
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
		function getExpertById(vm) {
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
				if(response.data.work && response.data.work.length > 0){
					vm.showWorkHistory = true;
					vm.work=response.data.work;
				}
				if(response.data.project && response.data.project.length > 0){
					vm.projectkHistory = true;
					vm.project=response.data.project;					
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
					width : 100,
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
					width : 100,
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
					title : "职务",
					width : 100,
					filterable : true
				},
				{
					field : "idCard",
					title : "身份证号",
					width : 100,
					filterable : true
				},
				{
					field : "maJor",
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
					field : "expeRttype",
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
					field : "expeRttype",
					title : "专家类别",
					width : 100,
					filterable : false
				}
			];
			
			return columns;
		}
				
		//S_auditGrid
		function auditGrid(vm){
			var dataSource1 = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '1'"}),
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
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '2'"}),
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
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '3'"}),
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
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '4'"}),
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
				transport : common.kendoGridConfig().transport(rootPath+"/expert/findByOData",$("#auditform"),{filter:"state eq '5'"}),
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
	            headerCenter: true,  
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
(function() {
	'projectExpe strict';

	angular.module('app').factory('projectExpeSvc', projectExpe);

	projectExpe.$inject = [ '$http'];
	
	function projectExpe($http) {
		var service = {
				getProject:getProject,
				createProject:createProject,
				updateProject:updateProject,
				saveProject:saveProject,
				getProjectById:getProjectById,
				delertProject:delertProject,
				gotoJPage:gotoJPage
		};
		return service;
		
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
				vm.model.projectName=response.data[0].projectName;
				vm.model.projectType=response.data[0].projectType;
				$('#projectbeginTime').val(response.data[0].projectbeginTime);
				$('#projectendTime').val(response.data[0].projectendTime);
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
		
		
		//begin#saveProject
		function saveProject(vm){
			common.initJqValidation($('#ProjectForm'));
			var isValid = $('#ProjectForm').valid();
			if (isValid) {
			vm.isSubmit = true;
			vm.model.peID=vm.peID;
			vm.model.expertID = vm.expertID;
			vm.model.projectbeginTime=$('#projectbeginTime').val();
			vm.model.projectendTime=$('#projectendTime').val();
			//alert(vm.model.projectendTime);
			var httpOptions = {
					method : 'put',
					url : rootPath + "/projectExpe/updateProject",
					data : vm.model
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
						vm.project=response.data;
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
		function updateProject(vm){
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
	                title: "添加项目经历",
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
				vm.model.projectbeginTime=$('#projectbeginTime').val();
				vm.model.projectendTime=$('#projectendTime').val();
				var httpOptions = {
					method : 'post',
					url : rootPath + "/projectExpe/projectExpe",
					data : vm.model
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

	workExpe.$inject = [ '$http' ];

	function workExpe($http) {
		var service = {
			createWork : createWork,
			saveWork : saveWork,
			deleteWork : deleteWork,
			updateWork : updateWork,
			gotoWPage : gotoWPage,
			getWorkById : getWorkById,
			getWork : getWork

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
		function updateWork(vm) {
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
				// vm.model = response.data[0];
				vm.model.companyName = response.data[0].companyName;
				vm.model.job = response.data[0].job;
				$('#beginTime').val(response.data[0].beginTime);
				$('#endTime').val(response.data[0].endTime);
				if (vm.isUpdate) {
				}
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
			// WorkeWindow.show();
			WorkeWindow.kendoWindow({
				width : "690px",
				height : "330px",
				title : "添加工作经历",
				visible : false,
				modal : true,
				closable : true,
				actions : [ "Pin", "Minimize", "Maximize", "Close" ]
			}).data("kendoWindow").center().open();
		}
		// end#gotoWPage

		// begin#getWork
		function getWork(vm) {
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

		}
		// begin#createWork
		function createWork(vm) {
			common.initJqValidation($('#workForm'));
			var isValid = $('#workForm').valid();
			if (isValid) {
				vm.model.beginTime = $('#beginTime').val();
				vm.model.endTime = $('#endTime').val();
				
				var httpOptions = {
					method : 'post',
					url : rootPath + "/workExpe/workExpe",
					data : vm.model
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

		// begin#saveWork
		function saveWork(vm) {
			common.initJqValidation();
			var isValid = $('form').valid();
			if (isValid) {
				vm.model.weID = vm.weID;
				vm.model.expertID = vm.expertID;
				vm.model.beginTime = $('#beginTime').val();
				vm.model.endTime = $('#endTime').val();

				var httpOptions = {
					method : 'put',
					url : rootPath + "/workExpe/updateWork",
					data : vm.model
				}

				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {							
							getWork(vm);
							cleanValue();
							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									window.parent.$("#wrwindow").data("kendoWindow").close();
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

    expertReview.$inject = ['$location', 'expertReviewSvc', 'expertConditionSvc', '$state'];

    function expertReview($location, expertReviewSvc, expertConditionSvc, $state) {
        var vm = this;
        vm.title = '选择专家';
        vm.conditionIndex = 1;                //条件号
        vm.conditions = new Array();          //条件列表
        vm.expertReview = {};                 //评审方案对象

        vm.autoSelExperts = new Array();     //专家抽取结果
        vm.autoExpertList = {};              //随机待抽取专家列表
        vm.workProgramId = $state.params.workProgramId;

        activate();
        function activate() {
            expertReviewSvc.initReview(vm);
            expertReviewSvc.initExpertGrid(vm);
        }

        //弹出自选专家框
        vm.showSelfExpertGrid = function () {
            expertReviewSvc.initSelfExpert(vm);
        }

        //保存自选的专家
        vm.saveSelfExpert = function () {
            expertReviewSvc.saveSelfExpert(vm);
        }

        //删除自选专家
        vm.delertSelfExpert = function () {
            var isCheck = $("input[name='seletedEp']:checked");
            if (isCheck.length < 1) {
                common.alert({
                    vm: vm,
                    msg: "请选择操作对象"
                })
            } else {
                common.confirm({
                    vm: vm,
                    title: "温馨提示",
                    msg: "删除数据不可恢复，确定删除么？",
                    fn: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < isCheck.length; i++) {
                            ids.push(isCheck[i].value);
                        }
                        var idStr = ids.join(',');
                        expertReviewSvc.delSelectedExpert(vm, idStr);
                    }
                })
            }
        }

        //境外专家
        vm.showOutExpertGrid = function () {
            expertReviewSvc.showOutExpertGrid(vm);
        }

        //删除选择的境外专家
        vm.delertOutSelfExpert = function () {
            var isCheck = $("input[name='seletedOutEp']:checked");
            if (isCheck.length < 1) {
                common.alert({
                    vm: vm,
                    msg: "请选择操作对象"
                })
            } else {
                common.confirm({
                    vm: vm,
                    title: "温馨提示",
                    msg: "删除数据不可恢复，确定删除么？",
                    fn: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < isCheck.length; i++) {
                            ids.push(isCheck[i].value);
                        }
                        expertReviewSvc.delSelectedExpert(vm, ids.join(','));
                    }
                })
            }
        }

        //保存选择的境外专家
        vm.saveOutExpert = function () {
            expertReviewSvc.saveOutExpert(vm);
        }

        //计算符合条件的专家
        vm.countMatchExperts = function (sortIndex) {
            if (vm.expertReview.id) {
                expertReviewSvc.countMatchExperts(vm, sortIndex);
            } else {
                common.alert({
                    vm: vm,
                    msg: "请保存整体抽取方案再计算"
                });
            }
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
            if (vm.expertReview.isSelete) {
                common.alert({
                    vm: vm,
                    msg: "当前项目已经进行整体专家方案的抽取，不能再修改方案！"
                })
            } else {
                vm.condition = {};
                vm.condition.sort = vm.conditionIndex;
                if (vm.expertReview.id) {
                    vm.condition.expertReviewDto = {};
                    vm.condition.expertReviewDto.id = vm.expertReview.id;   //抽取方案ID
                }
                vm.condition.selectType = "1";    //选择类型，这个一定不能少
                vm.conditions.push(vm.condition);
                vm.conditionIndex++;
            }
        }

        //删除专家抽取条件
        vm.removeCondition = function () {
            if (vm.expertReview.isSelete) {
                common.alert({
                    vm: vm,
                    msg: "当前项目已经进行整体专家方案的抽取，不能再修改方案！"
                })
            } else {
                var isCheck = $("#conditionTable input[name='epConditionSort']:checked");
                if (isCheck.length >= 1) {
                    common.confirm({
                        vm: vm,
                        title: "温馨提示",
                        msg: "删除数据不可恢复，确定删除么？",
                        fn: function () {
                            $('.confirmDialog').modal('hide');
                            var delIds = [];
                            for (var i = 0; i < isCheck.length; i++) {
                                vm.conditions.forEach(function (t, number) {
                                    if (isCheck[i].value == t.sort) {
                                        vm.conditions.splice(number, 1);
                                        if (t.id) {
                                            delIds.push(t.id);
                                        }
                                    }
                                });
                            }
                            if (delIds.length > 0) {
                                expertConditionSvc.deleteSelConditions(vm, delIds.join(","));
                            }
                        }
                    })
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
            if (vm.expertReview.isSelete) {
                common.alert({
                    vm: vm,
                    msg: "当前项目已经进行整体专家方案的抽取，不能再修改方案！"
                })
            } else {
                if (buildCondition(false)) {
                    expertConditionSvc.saveCondition(vm);
                } else {
                    common.alert({
                        vm: vm,
                        msg: "请先设置好条件再保存！",
                        closeDialog: true
                    })
                }
            }
        }

        //开始随机抽取
        vm.startAutoExpertWin = function () {
            if (buildCondition(true)) {
                if (vm.expertReview.isSelete == 9) {
                    common.alert({
                        vm: vm,
                        msg: "该方案已经进行整体专家方案的抽取，不能在继续抽取！"
                    })
                } else {
                    expertReviewSvc.queryAutoExpert(vm);
                }
            } else {
                common.alert({
                    vm: vm,
                    msg: "请先保存编辑的抽取方案！"
                })
            }
        }

        //显示随机抽取结果
        vm.showAutoExpertWin = function () {
            $("#aotuExpertDiv").kendoWindow({
                width: "800px",
                height: "600px",
                title: "专家抽取",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //再次抽取专家
        vm.resetAutoExpert = function () {
            common.alert({
                vm: vm,
                msg: "功能待开发！"
            })
        }

        //确认已抽取的专家
        vm.affirmAutoExpert = function () {
           /* var updteIdArr = new Array();
            vm.autoSelExperts.forEach(function (t, number) {
                updteIdArr.push(t.id);
            });
            expertReviewSvc.affirmAutoExpert(vm, updteIdArr.join(","))
            */

            expertReviewSvc.affirmAutoExpert(vm)
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
                common.alert({
                    vm: vm,
                    msg: "请选择操作对象"
                })
            } else {
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    ids.push(isCheck[i].value);
                }
                expertReviewSvc.updateJoinState(vm, ids.join(','), '9');
            }
        }

        //参加改为未参加
        vm.updateToNotJoin = function () {
            var isCheck = $("#joinExpertTable input[name='joinExpert']:checked");
            if (isCheck.length < 1) {
                common.alert({
                    vm: vm,
                    msg: "请选择操作对象"
                })
            } else {
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    ids.push(isCheck[i].value);
                }
                expertReviewSvc.updateJoinState(vm, ids.join(','), '0');
            }
        }

        //确认已选的专家
        vm.affirmExpert = function () {
            var isCheck = $("input[name='seletedEp']:checked");
            if (isCheck.length < 1) {
                common.alert({
                    vm: vm,
                    msg: "请选择操作对象"
                })
            } else {
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    ids.push(isCheck[i].value);
                }
                var idStr = ids.join(',');
                expertReviewSvc.updateExpertState(vm, idStr, "9");
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
            initSelfExpert: initSelfExpert,	            //初始化自选专家页面
            saveSelfExpert: saveSelfExpert,		        //保存自选专家
            updateExpertState: updateExpertState,	    //更改专家状态
            showOutExpertGrid: showOutExpertGrid,       //境外专家选择框
            saveOutExpert: saveOutExpert,               //保存选择的境外专家
            countMatchExperts: countMatchExperts,       //计算符合条件的专家
            getReviewList: getReviewList,               //查询专家评分

            //以下为新方法
            initReview: initReview,                      //初始化评审方案信息
            delSelectedExpert: delSelectedExpert,         //删除已选专家信息
            queryAutoExpert: queryAutoExpert,           //查询符合抽取条件的专家
            validateAutoExpert: validateAutoExpert,     //验证查询的专家是否符合条件
            affirmAutoExpert: affirmAutoExpert,	     //确认已经抽取的专家
            updateJoinState: updateJoinState,           //更改是否参加状态
            initParamValue: initParamValue,             //初始化值
        };
        return service;

        function initParamValue(vm) {
            vm.conditions = new Array();          //条件列表
            vm.customCondition = new Array();
            vm.expertReview = {};                 //评审方案对象
            vm.selfExperts = [],
            vm.selectExperts = [],
            vm.selectIds = [],
            vm.autoExperts = [],
            vm.autoSelExperts = [],
            vm.outsideExperts = [];
        }

        //S_initReview
        function initReview(vm) {
            vm.iscommit = true;
            initParamValue(vm);
            var httpOptions = {
                method: 'get',
                url: rootPath + "/expertReview/html/initByWorkProgramId",
                params: {workProgramId: vm.workProgramId}
            };
            var httpSuccess = function success(response) {
                vm.iscommit = false;
                vm.expertReview = response.data;
                //专家抽取条件
                if (vm.expertReview.expertSelConditionDtoList && vm.expertReview.expertSelConditionDtoList.length > 0) {
                    vm.conditions = vm.expertReview.expertSelConditionDtoList;
                    vm.conditionIndex = vm.expertReview.expertSelConditionDtoList.length;//下标值
                }
                //获取已经抽取的专家
                if (vm.expertReview.expertSelectedDtoList && vm.expertReview.expertSelectedDtoList.length > 0) {
                    vm.expertReview.expertSelectedDtoList.forEach(function (sep, index) {
                        vm.selectIds.push(sep.expertDto.expertID);
                        vm.selectExperts.push(sep);
                        if (sep.selectType == '1') {           //抽取专家
                            vm.autoExperts.push(sep);
                            vm.autoSelExperts.push(sep.expertDto)
                        } else if (sep.selectType == '2') {     //自选专家
                            vm.selfExperts.push(sep);
                        } else if (sep.selectType == '3') {     //境外专家
                            vm.outsideExperts.push(sep);
                        }
                    });
                    if (vm.selectIds.length > 0) {
                        vm.excludeIds = vm.selectIds.join(',');
                    } else {
                        vm.excludeIds = '';
                    }
                }
            };
            common.http({
                vm: vm,
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

        //S initSelfExpert
        function initSelfExpert(vm) {
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
        }//E initSelfExpert

        //S_saveSelfExpert
        function saveSelfExpert(vm) {
            var selectIds = common.getKendoCheckId('#selfExpertGrid');
            if (selectIds.length == 0) {
                $("#selfExpertError").html("请选择一条专家数据才能保存！");
            } else if (selectIds.length > 1) {
                $("#selfExpertError").html("自选专家最多只能选择一个！");
            } else {
                $("#selfExpertError").html("");
                window.parent.$("#selfExpertDiv").data("kendoWindow").close();
                vm.iscommit = true;
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/expertReview/saveExpertReview",
                    params: {
                        expertIds: selectIds[0].value,
                        reviewId: vm.expertReview.id,
                        selectType: "2"
                    }
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            vm.iscommit = false;
                            initReview(vm); //重新加载
                            common.alert({
                                vm: vm,
                                msg: "操作成功！",
                                closeDialog: true
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

        }//E_saveSelfExpert


        //S_updateExpertState
        function updateExpertState(vm, expertIds, state) {
            vm.iscommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/updateExpertState",
                params: {expertIds: expertIds, state: state}
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.iscommit = false;
                        common.alert({
                            vm: vm,
                            msg: "操作成功！",
                            closeDialog: true
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
        }//E_updateExpertState

        //S_showOutExpertGrid
        function showOutExpertGrid(vm) {
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
        }//E_showOutExpertGrid

        //S_saveOutExpert
        function saveOutExpert(vm) {
            var selectIds = common.getKendoCheckId('#outExpertGrid');
            if (selectIds.length == 0) {
                $("#outExpertError").html("请选择一条专家数据才能保存！");
            } else {
                $("#outExpertError").html("");
                window.parent.$("#outExpertDiv").data("kendoWindow").close();
                vm.iscommit = true;
                var selExpertIds = "";
                $.each(selectIds, function (i, obj) {
                    if (i == 0) {
                        selExpertIds += obj.value;
                    } else {
                        selExpertIds += "," + obj.value;
                    }
                });

                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/expertReview/saveExpertReview",
                    params: {
                        expertIds: selExpertIds,
                        reviewId: vm.expertReview.id,
                        selectType: "3"
                    }
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            vm.iscommit = false;
                            initReview(vm); //重新加载
                            common.alert({
                                vm: vm,
                                msg: "操作成功！",
                                closeDialog: true
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
        }//E_saveOutExpert

        //S_countMatchExperts
        function countMatchExperts(vm, sortIndex) {
            var data = {};
            vm.conditions.forEach(function (t, number) {
                if (t.sort == sortIndex) {
                    data = t;
                    data.maJorBig = $("#maJorBig" + t.sort).val();
                    data.maJorSmall = $("#maJorSmall" + t.sort).val();
                    data.expeRttype = $("#expeRttype" + t.sort).val();
                }
            });
            data.expertReviewDto = {};
            data.expertReviewDto.id = vm.expertReview.id;   //抽取方案ID
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expert/countReviewExpert",
                data: data,
                params: {
                    workprogramId: vm.workProgramId,
                    reviewId: vm.expertReview.id
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        $("#expertCount" + sortIndex).html(response.data);
                    }
                });
            }
            common.http({
                vm: vm,
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
                    vm.iscommit = false;
                }
            });
        }//end##getReviewList

        //S_queryAutoExpert
        function queryAutoExpert(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expert/findReviewExpert",
                headers: {
                    "contentType": "application/json;charset=utf-8"  //设置请求头信息
                },
                traditional: true,
                dataType: "json",
                data: angular.toJson(vm.conditions),//将Json对象序列化成Json字符串，JSON.stringify()原生态方法
                params: {
                    workprogramId: vm.workProgramId,
                    reviewId: vm.expertReview.id
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        if (response.data) {
                            vm.autoExpertList = response.data;
                            validateAutoExpert(vm);
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
        }//E_queryAutoExpert


        //S_validateAutoExpert
        function validateAutoExpert(vm) {
            if (!vm.autoExpertList || vm.autoExpertList.length < 1) {
                common.alert({
                    vm: vm,
                    msg: "本次被抽取的专家人数不满足抽取条件，抽取无效！请重新设置抽取条件！"
                })
                return;
            }
            //重置参数
            var totalExpertCount = 0;
            var officeExperts = new Array();
            var nativeExperts = new Array();

            vm.autoExpertList.forEach(function (e, number) {
                if (e.state == '2' || e.state == 2) {
                    officeExperts.push(e);
                } else {
                    nativeExperts.push(e);
                }
            });
            vm.conditions.forEach(function (c, number) {
                totalExpertCount += parseInt(c.officialNum);
            });

            if (totalExpertCount > officeExperts.length) {
                common.alert({
                    vm: vm,
                    msg: "本次被抽取的正选专家人数不满足抽取条件，抽取无效！请重新设置抽取条件！"
                })
                return;
            } else if (totalExpertCount > nativeExperts.length) {
                common.alert({
                    vm: vm,
                    msg: "本次被抽取的备选专家人数不满足抽取条件，抽取无效！请重新设置抽取条件！"
                })
                return;
            } else {
                vm.showAutoExpertWin();

                //随机抽取
                var timeCount = 0;
                var selAutoExpertIds = "";
                vm.t = $interval(function () {
                    if (totalExpertCount == 0) {
                        $interval.cancel(vm.t);
                        //保存抽取的专家
                        vm.iscommit = true;
                        var httpOptions = {
                            method: 'post',
                            url: rootPath + "/expertReview/saveExpertReview",
                            params: {
                                expertIds: selAutoExpertIds,
                                reviewId: vm.expertReview.id,
                                selectType: "1",
                                isDraw : true
                            }
                        }
                        var httpSuccess = function success(response) {
                            common.requestSuccess({
                                vm: vm,
                                response: response,
                                fn: function () {
                                    vm.iscommit = false;
                                    initReview(vm);
                                    common.alert({
                                        vm: vm,
                                        msg: "操作成功！",
                                        closeDialog: true
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
                    }else{
                        var selscope = Math.floor(Math.random() * (vm.autoExpertList.length));
                        vm.showAutoExpertName = vm.autoExpertList[selscope].name;
                        timeCount++;
                        if (timeCount % 10 == 0) {
                            var selskey = Math.floor(Math.random() * (officeExperts.length));
                            vm.autoSelExperts.push(officeExperts[selskey]);
                            selAutoExpertIds += officeExperts[selskey].expertID + ",";
                            officeExperts.forEach(function (t, number) {
                                if (officeExperts[selskey].expertID == t.expertID) {
                                    officeExperts.splice(number, 1);
                                }
                            });
                            selskey = Math.floor(Math.random() * (nativeExperts.length));
                            vm.autoSelExperts.push(nativeExperts[selskey]);
                            selAutoExpertIds += nativeExperts[selskey].expertID + ",";
                            nativeExperts.forEach(function (t, number) {
                                if (nativeExperts[selskey].expertID == t.expertID) {
                                    nativeExperts.splice(number, 1);
                                }
                            });
                            totalExpertCount--;
                        }
                    }
                }, 200);
            }
        }//E_validateAutoExpert

        //S_updateJoinState
        function updateJoinState(vm, ids, joinState) {
            vm.iscommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/updateJoinState",
                params: {expertIds: ids, state: joinState}
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.iscommit = false;
                        vm.selectExperts.forEach(function (e, number) {
                            if (ids.indexOf(e.id) >= 0) {
                                e.isJoin = joinState;
                            }
                        });
                        common.alert({
                            vm: vm,
                            msg: "操作成功！",
                            closeDialog: true
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
        }//E_updateJoinState

        //S_affirmAutoExpert(确认抽签结果)
        function affirmAutoExpert(vm) {
            vm.iscommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/expertReview/affirmAutoExpert",
                params: {
                    reviewId: vm.expertReview.id
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.iscommit = false;
                        window.parent.$("#aotuExpertDiv").data("kendoWindow").close();
                        common.alert({
                            vm: vm,
                            msg: "操作成功！",
                            closeDialog: true
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
        }//E_affirmAutoExpert

        //S_delSelectedExpert(删除已选专家)
        function delSelectedExpert(vm, delIds) {
            vm.iscommit = true;
            var httpOptions = {
                method: 'delete',
                url: rootPath + "/expertSelected",
                params: {
                    id: delIds,
                    reviewId: vm.expertReview.id,
                    deleteAll: false
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.iscommit = false;
                        initReview(vm);
                        common.alert({
                            vm: vm,
                            msg: "操作成功！",
                            closeDialog: true
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
		function saveCondition(vm) {
            var httpOptions = {
                method : 'post',
                url : rootPath + "/expertSelCondition/saveConditionList",
                headers:{
                    "contentType":"application/json;charset=utf-8"  //设置请求头信息
                },
                traditional: true,
                dataType : "json",
                data : angular.toJson(vm.conditions),//将Json对象序列化成Json字符串，JSON.stringify()原生态方法
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm : vm,
                    response : response,
                    fn : function() {
                        vm.conditions = response.data;
                        common.alert({
                            vm: vm,
                            msg: "操作成功！",
                            closeDialog: true
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
        function deleteSelConditions(vm,delIds){
            vm.iscommit = true;
            var httpOptions = {
                method : 'delete',
                url : rootPath + "/expertSelCondition",
                params:{
                    ids : delIds
                }
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm : vm,
                    response : response,
                    fn : function() {
                        vm.iscommit = false;
                        common.alert({
                            vm: vm,
                            msg: "操作成功！",
                            closeDialog: true
                        })
                    }
                });
            }
            common.http({
                vm : vm,
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess,
                onError: function(response){vm.iscommit = false;}
            });
        }//E_deleteSelConditions
    }
})();
(function () {
    'use strict';

    angular.module('app').controller('fileRecordEditCtrl', fileRecord);

    fileRecord.$inject = ['$location','fileRecordSvc','$state']; 

    function fileRecord($location, fileRecordSvc,$state) {     
        var vm = this;
        vm.title = '项目归档编辑';

        vm.fileRecord = {};
        vm.fileRecord.signId = $state.params.signid;
        vm.signId = $state.params.signid;
        
        $("input").click(function(){
        	
        })
        //文件下载
        vm.fileDownload = function(id){
        	fileRecordSvc.fileDownload(vm,id);
        }
        //删除系统文件
        vm.delfileSysFile = function(id){
        	fileRecordSvc.delfileSysFile(vm,id);
        }
        //查看附件
        vm.fileRecordJquery = function(){
        	 $("#filequeryWin").kendoWindow({
                 width : "800px",
                 height : "400px",
                 title : "附件上传",
                 visible : false,
                 modal : true,
                 closable : true,
                 actions : [ "Pin", "Minimize", "Maximize", "Close" ]
             }).data("kendoWindow").center().open();
        	 fileRecordSvc.initFileRecordData(vm);
        }
        
        //上传附件弹窗
        vm.fileRecordUpload = function(){
        	 $("#fileRecordUploadWin").kendoWindow({
                 width : "660px",
                 height : "400px",
                 title : "附件上传",
                 visible : false,
                 modal : true,
                 closable : true,
                 actions : [ "Pin", "Minimize", "Maximize", "Close" ]
             }).data("kendoWindow").center().open();
        }
        
        activate();
        function activate(){
        	fileRecordSvc.initFileRecordData(vm);
        }
                
        vm.create = function(){
        	fileRecordSvc.saveFileRecord(vm);
        }
    }
})();

(function() {
	'use strict';
	
	angular.module('app').factory('fileRecordSvc', fileRecord);
	
	fileRecord.$inject = ['$rootScope','$http'];

	function fileRecord($rootScope,$http) {
		var service = {
			initFileRecordData : initFileRecordData,		//初始化流程数据	
			saveFileRecord : saveFileRecord,				//保存
			initfileRecordUpload:initfileRecordUpload,		//初始化上传附件
			delfileSysFile:delfileSysFile,					//删除系统文件
			fileDownload:fileDownload,						//文件下载
		};
		return service;	
		
		//S 文件下载
		function fileDownload(vm,fileId){
			var sysfileId = fileId;
			window.open(rootPath+"/file/fileDownload?sysfileId="+fileId);
		}
		//S 文件下载
		
		//S 删除系统文件
		function delfileSysFile(vm,id){
			var httpOptions = {
					method : 'delete',
					url : rootPath+"/file/deleteSysFile",
					data : id

				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn:function(){		
							window.parent.$("#filequeryWin").data("kendoWindow").close();
							common.alert({
								vm:vm,
								msg:"删除成功",
								fn:function() {
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
		//E 删除系统文件
		
		//S 初始化上传附件
		function initfileRecordUpload(vm){
			var businessId = vm.fileRecord.signId;
            var projectfileoptions = {
                language : 'zh',
                allowedPreviewTypes : ['image'],
                allowedFileExtensions : [ 'jpg', 'png', 'gif',"xlsx","docx" ,"pdf","doc","xls"],
                maxFileSize : 2000,
                showRemove: false,
                uploadUrl:rootPath + "/file/fileUpload",
                uploadExtraData:{businessId:businessId}
            };
            $("#fileRecordphotofile").fileinput(projectfileoptions).on("filebatchselected", function(event, files){

            }).on("fileuploaded", function(event, data) {
                $("#fileRecordPhotoSrc").removeAttr("src");
                $("#fileRecordPhotoSrc").attr("src",rootPath+"/sign/transportImg?signid="+businessId+"&t="+Math.random());
            });
		}
		//E 初始化上传附件
		
		//S_初始化
		function initFileRecordData(vm){				
			var httpOptions = {
					method : 'get',
					url : rootPath+"/fileRecord/html/initFillPage",
					params : {signId:vm.fileRecord.signId}
				}
			var httpSuccess = function success(response) {									
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {		
						if(response.data != null && response.data != ""){
							vm.fileRecord = response.data.file_record;	
							vm.fileRecord.signId = vm.signId;
							vm.signUserList = response.data.sign_user_List;	
							vm.sysFiles = response.data.sysFilelist;
						}	
						initfileRecordUpload(vm);
					}	
						
				});
			}
			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}//E_初始化
		
		//S_保存
		function saveFileRecord(vm){	
			common.initJqValidation($("#fileRecord_form"));
			var isValid = $("#fileRecord_form").valid();
			if (isValid) {
				if(vm.signUser){
					vm.fileRecord.signUserid = vm.signUser.id;
					vm.fileRecord.signUserName = vm.signUser.displayName;
				}				
				vm.saveProcess = true;
				var httpOptions = {
						method : 'post',
						url : rootPath+"/fileRecord",
						data : vm.fileRecord
					}
				var httpSuccess = function success(response) {									
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function() {		
							vm.saveProcess = false;
							common.alert({
								vm:vm,
								msg:"操作成功！",	
							})								
						}						
					});
				}
				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess,
					onError: function(response){vm.saveProcess = false;}
				});
			}
		}//E_保存
		
	}
})();
(function() {
	'use strict';

	angular.module('app').factory('flowSvc', flow);

	flow.$inject = ['$http', '$state', 'signFlowSvc'];

	function flow($http, $state, signFlowSvc) {
		var service = {
			initFlowData : initFlowData, // 初始化流程数据
			getFlowInfo : getFlowInfo, // 获取流程信息
			commit : commit, // 提交
			rollBackToLast : rollBackToLast, // 回退到上一环节
			rollBack : rollBack, // 回退到选定环节
			initBackNode : initBackNode, // 初始化回退环节信息
			initDealUerByAcitiviId : initDealUerByAcitiviId,
			suspendFlow : suspendFlow, // 流程挂起
			activeFlow : activeFlow, // 重启流程
			deleteFlow : deleteFlow, // 流程终止

			gotoExpertmark : gotoExpertmark, // 打开专家评分弹窗
			saveMark : saveMark, // 保存专家评分
			savePayment : savePayment, // 保存专家费用
			countTaxes : countTaxes, // 计算应纳税额
			gotopayment : gotopayment,
			getselectExpertById : getselectExpertById

		};
		return service;

		// S_初始化流程数据
		function initFlowData(vm) {
			var processInstanceId = vm.flow.processInstanceId;
			if (angular.isUndefined(vm.flow.hideFlowImg)
					|| vm.flow.hideFlowImg == false) {
				vm.picture = rootPath + "/flow/processInstance/img/"
						+ processInstanceId;
			}

			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath
						+ "/flow/processInstance/history/" + processInstanceId),
				schema : common.kendoGridConfig().schema({
							id : "id"
						}),
				rowNumber : true,
				headerCenter : true
			});

			var columns = [{
						field : "",
						title : "序号",
						template : "<span class='row-number'></span>",
						width : 30
					}, {
						field : "activityName",
						title : "环节名称",
						width : 120,
						filterable : false
					}, {
						field : "assignee",
						title : "处理人",
						width : 80,
						filterable : false
					}, {
						field : "startTime",
						title : "开始时间",
						width : 120,
						filterable : false,
						format : "{0: yyyy-MM-dd HH:mm:ss}"
					}, {
						field : "endTime",
						title : "结束时间",
						width : 120,
						filterable : false,
						format : "{0: yyyy-MM-dd HH:mm:ss}"
					}, {
						field : "duration",
						title : "处理时长",
						width : 120,
						filterable : false
					}, {
						field : "message",
						title : "处理信息",
						width : 300,
						filterable : false
					}];
			// End:column
			vm.historygrid = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridConfig().filterable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : columns,
				resizable : true,
				dataBound : function() {
					var rows = this.items();
					$(rows).each(function(i) {
								if (i == rows.length - 1) {
									initBackNode(vm);
								}
								$(this).find(".row-number").html(i + 1);
							});
				}
			};
		}// E_初始化流程数据

		// S_getFlowInfo
		function getFlowInfo(vm) {
			var httpOptions = {
				method : 'get',
				url : rootPath + "/flow/processInstance/flowNodeInfo",
				params : {
					taskId : vm.flow.taskId,
					processInstanceId : vm.flow.processInstanceId
				}
			}

			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.flow = response.data;
						if (vm.flow.businessMap) {
							if (vm.flow.businessMap.viceDirectors) {
								vm.businessTr = true;
								vm.ZHB_SP_SW = true;
								vm.viceDirectors = vm.flow.businessMap.viceDirectors;
							}
							if (vm.flow.businessMap.orgs) {
								vm.businessTr = true;
								vm.FGLD_SP_SW = true;
								vm.orgs = vm.flow.businessMap.orgs;
							}
							if (vm.flow.businessMap.users) {
								vm.businessTr = true;
								vm.BM_FB = true;
								vm.users = vm.flow.businessMap.users;
							}
							// 协审分管领导审批
							if (vm.flow.businessMap.xsOrgs) {
								vm.businessTr = true;
								vm.XS_FGLD_SP_SW = true;
								vm.xsOrgs = vm.flow.businessMap.xsOrgs;
							}
						}
						signFlowSvc.initBusinessParams(vm);
					}

				})
			}

			common.http({
						vm : vm,
						$http : $http,
						httpOptions : httpOptions,
						success : httpSuccess
					});
		}// E_getFlowInfo

		// S_提交下一步
		function commit(vm) {
			common.initJqValidation($("#flow_form"));
			var isValid = $("#flow_form").valid();
			if (isValid) {
				vm.isCommit = true;
				var httpOptions = {
					method : 'post',
					url : rootPath + "/flow/commit",
					data : vm.flow
				}

				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							common.alert({
										vm : vm,
										msg : response.data.reMsg,
										closeDialog : true,
										fn : function() {
											if (response.data.reCode == "error") {
												vm.isCommit = false;
											} else {
												$state.go('index');
											}
										}
									})
						}

					})
				}

				common.http({
							vm : vm,
							$http : $http,
							httpOptions : httpOptions,
							success : httpSuccess,
							onError : function(response) {
								vm.isCommit = false;
							}
						});
			}
		}// E_提交下一步

		// S_回退到上一步
		function rollBackToLast(vm) {
			var httpOptions = {
				method : 'post',
				url : rootPath + "/flow/rollbacklast",
				data : vm.flow
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
							vm : vm,
							response : response,
							fn : function() {
								common.alert({
											vm : vm,
											msg : response.data.reMsg
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
		}// E_回退到上一步

		// S_回退到指定环节
		function rollBack(vm) {
			if (vm.flow.back == null || vm.flow.back.activitiId == null
					|| vm.flow.back.activitiId == "") {
				common.alert({
							vm : vm,
							msg : "请先选择要会退的环节！"
						})
				return;
			}

			common.confirm({
						vm : vm,
						title : "",
						msg : "确认回退吗？",
						fn : function() {
							// 设置
							vm.flow.rollBackActiviti = vm.flow.back.activitiId;
							vm.flow.backNodeDealUser = vm.flow.back.assignee;

							var httpOptions = {
								method : 'post',
								url : rootPath + "/flow/rollback",
								data : vm.flow
							}
							var httpSuccess = function success(response) {
								common.requestSuccess({
											vm : vm,
											response : response,
											fn : function() {
												common.alert({
															vm : vm,
															msg : response.data.reMsg
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
					})
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
								"activitiId" : datas[i].activityId,
								"activitiName" : datas[i].activityName,
								"assignee" : datas[i].assignee
							});
				}
			}
		}// E_初始化回退环节信息

		// S_初始化下一环节处理人
		function initDealUerByAcitiviId(vm) {
			vm.nextDealUserList = vm.nextDealUserMap[vm.flow.nextNodeAcivitiId];
			if (vm.nextDealUserList) {
				vm.flow.nextDealUser = vm.nextDealUserList[0].loginName; // 默认选中
			}
		}// E_初始化下一环节处理人

		// S_流程挂起
		function suspendFlow(vm, businessKey) {
			var httpOptions = {
				method : 'post',
				url : rootPath + "/flow/suspend/" + businessKey
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
							vm : vm,
							response : response,
							fn : function() {
								common.alert({
											vm : vm,
											msg : "操作成功！"
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
		}// E_流程挂起

		// S_流程激活
		function activeFlow(vm, businessKey) {
			var httpOptions = {
				method : 'post',
				url : rootPath + "/flow/active/" + businessKey
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
							vm : vm,
							response : response,
							fn : function() {
								common.alert({
											vm : vm,
											msg : "操作成功！"
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
		}// E_流程激活

		// S_终止流程
		function deleteFlow(vm) {
			if (vm.flow.dealOption == null || vm.flow.dealOption == "") {
				common.alert({
							vm : vm,
							msg : "请填写处理信息！"
						})
				return;
			}
			var httpOptions = {
				method : 'post',
				url : rootPath + "/flow/deleteFLow",
				data : vm.flow
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
							vm : vm,
							response : response,
							fn : function() {
								common.alert({
											vm : vm,
											msg : "操作成功！"
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
		}// E_终止流程


		// S_gotoExpertmark
		function gotoExpertmark(vm) {
			$("#star").raty({
						score : function() {
							$(this).attr("data-num",vm.expertReview.score);
							return $(this).attr("data-num");
						},
						starOn : '../contents/libs/raty/lib/images/star-on.png',
						starOff : '../contents/libs/raty/lib/images/star-off.png',
						starHalf : '../contents/libs/raty/lib/images/star-half.png',
						readOnly : false,
						halfShow : true,
						size : 34,
						click : function(score, evt) {
							vm.expertReview.score = score;
						}
					});
			vm.showExpertRemark = true;
			var WorkeWindow = $("#expertmark");
			WorkeWindow.kendoWindow({
						width : "1000px",
						height : "630px",
						title : "编辑-专家星级",
						visible : false,
						modal : true,
						closable : true,
						actions : ["Pin", "Minimize", "Maximize", "Close"]
					}).data("kendoWindow").center().open();
					getselectExpertById(vm);
		}// E_gotoExpertmark

		// S_saveMark
		function saveMark(vm) {
			common.initJqValidation($('#markform'));
			var isValid = $('#markform').valid();
			if (isValid) {
				var httpOptions = {
					method : 'post',
					url : rootPath + "/expertReview/html/expertMark",
					data : vm.expertReview
				}

				var httpSuccess = function success(response) {
					common.requestSuccess({
								vm : vm,
								response : response,
								fn : function() {
									window.parent.$("#expertmark")
											.data("kendoWindow").close();
									vm.gridOptions.dataSource.read();
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
		}// E_saveMark

		// S_getpaymentColumns
		function getpaymentColumns() {
			var columns = [{
						field : "rowNumber",
						title : "序号",
						width : 50,
						template : "<span class='row-number'></span>"
					}, {
						field : "name",
						title : "姓名",
						width : 100,
						filterable : true
					}, {
						field : "idCard",
						title : "身份证号码",
						width : 100,
						filterable : true
					}, {
						field : "openingBank",
						title : "开户行",
						width : 100,
						filterable : true
					}, {
						field : "bankAccount",
						title : "银行账号",
						width : 100,
						filterable : true
					}, {
						field : "expertReviewDto.reviewCost",
						title : "评审费",
						width : 100,
						filterable : true
					},

					{
						field : "expertReviewDto.reviewTaxes",
						title : "应纳税额",
						width : 100,
						filterable : true
					}, {
						field : "expertReviewDto.totalCost",
						title : "合计（元）",
						width : 100
					}, {
						field : "",
						title : "操作",
						width : 100,
						template : function(item) {

							return common.format($('#columnBtn').html(),
									"vm.editpayment('" + item.expertID + "')",
									item.expertID);
						}
					}];
			return columns;
		}// E_getpaymentColumns

		// S_savePayment
		function savePayment(vm) {
			common.initJqValidation($('#payform'));getSelectExpert
			var isValid = $('#payform').valid();
			if (isValid) {
				if (!validateNum(vm)) {
					window.parent.$("#payment").data("kendoWindow").close();
					common.alert({
								vm : vm,
								msg : "应纳税额计算错误,保存失败！",
								fn : function() {
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
								}
							})
					return;
				}
				vm.isCommit = true;
				var httpOptions = {
					method : 'post',
					url : rootPath + "/expertReview/html/savePayment",
					data : vm.expertReview
				}

				var httpSuccess = function success(response) {
					vm.isCommit = false;
					window.parent.$("#payment").data("kendoWindow").close();
					vm.paymentgrid.dataSource.read();
					common.alert({
								vm : vm,
								msg : "操作成功"
							})
				}

				common.http({
							vm : vm,
							$http : $http,
							httpOptions : httpOptions,
							success : httpSuccess
						});
			}
		}// E_savePayment

		// S_countNum
		function countNum(vm) {
			var XSum = vm.expertReview.reviewCost;
			var Rate;
			var Balan;
			var TSum_7;
			if (XSum <= 1500) {
				Rate = 3;
				Balan = 0;
			}
			if ((1500 < XSum) && (XSum <= 4500)) {
				Rate = 10;
				Balan = 105;
			}
			if ((4500 < XSum) && (XSum <= 9000)) {
				Rate = 20;
				Balan = 555;
			}
			if ((9000 < XSum) && (XSum <= 35000)) {
				Rate = 25;
				Balan = 1005;
			}
			if ((35000 < XSum) && (XSum <= 55000)) {
				Rate = 30;
				Balan = 2755;
			}
			if ((55000 < XSum) && (XSum <= 80000)) {
				Rate = 35;
				Balan = 5505;
			}
			if (XSum > 80000) {
				Rate = 45;
				Balan = 13505;
			}
			TSum_7 = (XSum * Rate) / 100 - Balan
			if (TSum_7 < 0) {
				TSum_7 = 0
			}
			return TSum_7;
		}// E_countNum

		// S_countTaxes
		function countTaxes(vm) {
			var XSum = vm.expertReview.reviewCost;
			var TSum_7 = countNum(vm);
			vm.expertReview.reviewTaxes = TSum_7.toFixed(2);
			vm.expertReview.totalCost = XSum - TSum_7.toFixed(2);
			console.log(vm.expertReview.reviewTaxes);

		}// E_countTaxes

		// S_validateNum
		function validateNum(vm) {
			var is = true;
			var XSum = vm.expertReview.reviewCost;
			var TSum_7 = countNum(vm);
			var totalCost = XSum - TSum_7.toFixed(2);
			var reviewTaxes = TSum_7.toFixed(2);
			if (reviewTaxes != vm.expertReview.reviewTaxes
					&& totalCost != vm.expertReview.totalCost) {
				is = false;
			}

			return is;
		}// E_validateNum

		// S_gotopayment
		function gotopayment(vm) {
			vm.showExpertpayment = true;
			var WorkeWindow = $("#payment");
			WorkeWindow.kendoWindow({
						width : "1000px",
						height : "630px",
						title : "编辑-专家费用",
						visible : false,
						modal : true,
						closable : true,
						actions : ["Pin", "Minimize", "Maximize", "Close"]
					}).data("kendoWindow").center().open();
			getselectExpertById(vm);
		}// E_gotopayment

		// S_getselectExpertById
		function getselectExpertById(vm) {
			vm.isCommit = true;
			var httpOptions = {
				method : 'get',
				url : rootPath + "/expertReview/html/getSelectExpertById",
				params : {
					expertId : vm.expertReview.expertId
				}

			}

			var httpSuccess = function success(response) {
				vm.isCommit = false;
				vm.expertReview = response.data;
				vm.expertReview.expertId = vm.expertReview.expertDto.expertID;
			}

			common.http({
						vm : vm,
						$http : $http,
						httpOptions : httpOptions,
						success : httpSuccess
					});
		}// E_getselectExpertById
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

(function() {
	'use strict';

	angular.module('app').factory('meetingSvc', meeting);

	meeting.$inject = [ '$http' ];

	function meeting($http) {
		var url_meeting = rootPath + "/meeting";
		var url_back = '#/meeting';
		var service = {
			grid : grid,
			getMeetingById : getMeetingById,
			createMeeting : createMeeting,
			deleteMeeting : deleteMeeting,
			updateMeeting : updateMeeting,
			queryMeeting : queryMeeting		//会议室查询
		};

		return service;
		
		//会议室查询
		function queryMeeting(vm){
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
					method : 'put',
					url : url_meeting,
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

		// begin#deleteUser
		function deleteMeeting(vm, id) {
			vm.isSubmit = true;
			var httpOptions = {
				method : 'delete',
				url : url_meeting,
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

		// begin#createUser
		function createMeeting(vm) {
			common.initJqValidation();
			var isValid = $('form').valid();
			if (isValid) {
				vm.isSubmit = true;
				var httpOptions = {
					method : 'post',
					url : url_meeting,
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
									location.href = url_back;
								}
							})
						}
					});

				}

				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess,
                    onError: function(response){vm.iscommit = false;}
				});

			}
		}

		// begin#getUserById
		function getMeetingById(vm) {
			var httpOptions = {
					method : 'get',
					url : url_meeting +"/html/findByIdMeeting",
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
		}
		// begin#grid
		function grid(vm) {
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_meeting+"/fingByOData",$("#meetingFrom")),
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

			// End:dataSourc
			
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
						field : "num",
						title : "会议室编号",
						width : 200,
						filterable : false
					},
					{
						field : "mrName",
						title : "会议室名称",
						width : 200,
						filterable : false
					},
                    {
                        field : "mrType",
                        title : "会议室类型",
                        width : 180,
                        filterable : false,
                    },
					{
						field : "addr",
						title : "会议室地点",
						filterable : false
					},
					{
						field : "mrStatus",
						title : "会议室状态",
						filterable : false
					},
					{
						field : "capacity",
						title : "会议室容量",
						filterable : false
					},
					{
						field : "userName",
						title : "会议室负责人",
						filterable : false
					},
					{
						field : "userPhone",
						title : "负责人电话",
						filterable : false
					},
					{
						field : "",
						title : "操作",
						width : 180,
						template : function(item) {
							return common.format($('#columnBtns').html(),"vm.del('" + item.id + "')", item.id);
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
				dataBound:dataBound,
				resizable : true
			};
		}// end fun grid

		
	}
})();
(function () {
    'use strict';

    angular.module('app').controller('myTestCtrl', myTest);

    myTest.$inject = ['$location', 'myTestSvc'];

    function myTest($location, myTestSvc) {
        var vm = this;
        vm.title = 'My test';

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    myTestSvc.deleteMytest(vm, id);
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
            myTestSvc.grid(vm);
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('myTestEditCtrl', myTest);

    myTest.$inject = ['$location', 'myTestSvc', '$state'];

    function myTest($location, myTestSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加My test';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新My test';
        }

        vm.create = function () {
            myTestSvc.createMyTest(vm);
        };
        vm.update = function () {
            myTestSvc.updateMyTest(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                myTestSvc.getMyTestById(vm);
            }
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').factory('myTestSvc', myTest);

    myTest.$inject = ['$http'];

    function myTest($http) {
        var url_myTest = rootPath + "/myTest", url_back = '#/myTest';
        var service = {
            grid: grid,
            getMyTestById: getMyTestById,
            createMyTest: createMyTest,
            deleteMyTest: deleteMyTest,
            updateMyTest: updateMyTest
        };

        return service;

        // begin#updateMyTest
        function updateMyTest(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_myTest,
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

        // begin#deleteMyTest
        function deleteMyTest(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_myTest,
                data: id
            };

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.isSubmit = false;
                        vm.gridOptions.dataSource.read();
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

        // begin#createMyTest
        function createMyTest(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;

                var httpOptions = {
                    method: 'post',
                    url: url_myTest,
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
                                fn: function () {
                                    vm.isSubmit = false;
                                    $('.alertDialog').modal('hide');
                                    $('.modal-backdrop').remove();
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

        // begin#getMyTestById
        function getMyTestById(vm) {
            var httpOptions = {
                method: 'get',
                url: common.format(url_myTest + "?$filter=id eq '{0}'", vm.id)
            };
            var httpSuccess = function success(response) {
                vm.model = response.data.value[0];
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
                transport: common.kendoGridConfig().transport(url_myTest),
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
                    field: "testName",
                    title: "测试名",
                    width: 100,
                    filterable: true
                },
                {
                    field: "test01",
                    title: "测试01",
                    width: 100,
                    filterable: true
                },
                {
                    field: "test02",
                    title: "测试02",
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
					    width: 70,
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
						field : "orgAddress",
						title : "地址",
						width : 130,						
						filterable : false
					},										
					{
						field : "remark",
						title : "描述",
						width : 130,	
						filterable : false
					}, {
						field : "createdDate",
						title : "创建时间",
						width : 180,
						filterable : false,
						format : "{0:yyyy/MM/dd HH:mm:ss}"

					},  {
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
			
		};		
		return service;	
		
		function queryRoomCount(vm){
			vm.gridOptions.dataSource.read();
		}
		
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
					url: common.format(url_room + "/roomNamelist")
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

        //预定会议编辑
       vm.editRoom = function(){
        	roomSvc.editRoom(vm);
        }
        //预定会议室添加
        vm.addRoom = function(){
        	roomSvc.addRoom(vm);
        }

        //导出本周评审会议安排
        vm.exportWeek = function(){
        	roomSvc.exportWeek();
        }
        //导出本周全部会议安排
        vm.exportThisWeek = function(){
        	
        	roomSvc.exportThisWeek();
        }
        //导出下周全部会议安排
        vm.exportNextWeek = function(){
        	
        	roomSvc.exportNextWeek();
        }
        //导出下周评审会议安排
        vm.stageNextWeek = function(){
        	
        	roomSvc.stageNextWeek();
        }
        //会议室查询
        vm.findMeeting = function(){
        	roomSvc.findMeeting(vm);
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
           roomSvc.initRoom(vm);
           roomSvc.showMeeting(vm);
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

(function() {
	'use strict';

	angular.module('app').factory('roomSvc', room);

	room.$inject = [ '$http' ];

	function room($http) {
		var url_room = rootPath + "/room";
		var url_back = '#/room';
		
		var service = {
			initRoom : initRoom,
			showMeeting : showMeeting,
			findMeeting : findMeeting,
			exportWeek : exportWeek,
			exportThisWeek : exportThisWeek,
			exportNextWeek : exportNextWeek,
			stageNextWeek : stageNextWeek,
			addRoom : addRoom,  //添加会议室预定
			editRoom : editRoom,//编辑
		};

		return service;
		
		//S_会议预定编辑
		function editRoom(vm){	
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
					method : 'put',
					url : rootPath + "/room/updateRoom",
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
		//E_会议预定编辑
		
		// 清空页面数据
		// begin#cleanValue
		function cleanValue() {
			var tab = $("#stageWindow").find('input');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
		}
		// end#cleanValue
		
		//S_添加会议室预定(停用)
		function addRoom(vm){
            common.initJqValidation($('#formRoom'));
            var isValid = $('#formRoom').valid();
            if (isValid) {
                var httpOptions = {
                    method : 'post',
                    url : rootPath + "/room/addRoom",
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
		//E_添加会议室预定
		
		//start 初始化会议预定页面
		function initRoom(vm){
			var dataSource = new kendo.data.SchedulerDataSource({
				batch: true,
				sync: function() {
					this.read();
				},
	           transport: {
				  read:function(options){
					  var mrID = options.data.mrID;
					  var url =  rootPath + "/room" ;
					  if(mrID){
						  url += "?"+mrID;
					  }
					  $http.get(
					  	url
					  ).success(function(data) {  
						  options.success(data.value);
					  }).error(function(data) {
					  		console.log(data);
						  console.log("查询数据失败！");
					  });  
				  },
				  create:function(options){
                      common.initJqValidation($('#formRoom'));
                      var isValid = $('#formRoom').valid();
                      if (isValid) {
                          var model = options.data.models[0];
                          model.rbDay = $("#rbDay").val();
                          model.beginTimeStr = $("#beginTime").val();
                          model.endTimeStr = $("#endTime").val();
                          model.beginTime = $("#rbDay").val()+" "+$("#beginTime").val()+":00";
                          model.endTime = $("#rbDay").val()+" "+$("#endTime").val()+":00";
                          if(vm.workProgramId){
                              model.workProgramId = vm.workProgramId;
						  }
                          if(new Date(model.endTime) < new Date(model.beginTime)){
                              $("#errorTime").html("开始时间不能大于结束时间!");
                              return ;
                          }
                          var httpOptions = {
                              method : 'post',
                              url : rootPath + "/room/addRoom",
                              data : model
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
                              vm : vm,
                              $http : $http,
                              httpOptions : httpOptions,
                              success : httpSuccess
                          });
                      }
				  },
				  update:function(options){
                      common.initJqValidation($('#formRoom'));
                      var isValid = $('#formRoom').valid();
                      if (isValid) {
                          var model = options.data.models[0];
                          model.rbDay = $("#rbDay").val();
                          model.beginTimeStr = $("#beginTime").val();
                          model.endTimeStr = $("#endTime").val();
                          model.beginTime = $("#rbDay").val()+" "+$("#beginTime").val()+":00";
                          model.endTime = $("#rbDay").val()+" "+$("#endTime").val()+":00";
                          if(new Date(model.endTime) < new Date(model.beginTime)){
                              $("#errorTime").html("开始时间不能大于结束时间!");
                              return ;
                          }
                          var httpOptions = {
                              method : 'put',
                              url : rootPath + "/room/updateRoom",
                              data : model
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
                              vm : vm,
                              $http : $http,
                              httpOptions : httpOptions,
                              success : httpSuccess
                          });
                      }
				  },
				  destroy:function(options){
                      var id = options.data.models[0].id;
                      var httpOptions = {
                          method : 'delete',
                          url : url_room,
                          data : id
                      }
                      var httpSuccess = function success(response) {
                          common.requestSuccess({
                              vm : vm,
                              response : response,
                              fn : function() {
                                  common.alert({
                                      vm : vm,
                                      msg : "删除成功",
                                      closeDialog:true
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
				  },
				  parameterMap: function(options, operation) {
				      console.log(operation);
	                if (operation !== "read" && options.models) {
	                  return { models: kendo.stringify(options.models)};
	                }
	              }
	           },
	           serverPaging : true,
               serverSorting : true,
               serverFiltering : true,
			   pageSize : 10,
	           schema: {
	              model: {
	              	id: "taskId", 
	                fields: {
	                    taskId: {from: "id"},
	                    title: { from: "addressName", defaultValue: "会议室" },
                        start: { type: "date", from: "beginTime" },
                        end: { type: "date", from: "endTime" }
                    }
				  }
	            },
	          });
		
			vm.schedulerOptions = {
                date: new Date(),
                startTime:vm.startDateTime,
                endTime:vm.endDateTime,
                height: 600,
                views: [
                    "day",
                    "workWeek",
                    {type: "week", selected: true },
                    "month"
                ],
                editable: {
                    template: $("#customEditorTemplate").html(),
                },
                eventTemplate: $("#event-template").html(),
                timezone: "Etc/UTC",
                dataSource :dataSource,
                footer: false,
            };
		}
		//end 初始化会议预定页面
		
		//start#会议室地点查询
		function showMeeting(vm){
			 $http.get(
				  url_room+"/meeting" 
			  ).success(function(data) {  
				  vm.meetings ={};
				  vm.meetings=data;
			  }).error(function(data) {  
				  //alert("查询会议室失败");
			  }); 
		}
		//end #会议室地点查询
		
		//查询会议室
		function findMeeting(vm){
			if(vm.mrID){
                vm.schedulerOptions.dataSource.read({"mrID":common.format("$filter=mrID eq '{0}'", vm.mrID)});
			}else{
                vm.schedulerOptions.dataSource.read();
			}
		}
		//start#deleteRoom
		function deleteRoom(vm){
			var model = vm.data.models[0];
			var id = model.id;
			var httpOptions = {
					method : 'delete',
					url : url_room,
					data : id
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						common.alert({
							vm : vm,
							msg : "删除成功",
							fn : function() {
							$('.alertDialog').modal('show');
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
		//end#deleteRoom
		
		//start#exportWeek
		//本周评审会议
		function exportWeek(){
			window.open(url_room+"/exports");
		}
		//本周全部会议
		function exportThisWeek(){
			window.open(url_room+"/exportWeek");
		}
		//下周全部会议
		function exportNextWeek(){
			window.open(url_room+"/exportNextWeek");
		}
		//下周评审会议
		function stageNextWeek(){
			window.open(url_room+"/stageNextWeek");
		}
		//end#exportWeek
		
	}
})();


(function () {
    'use strict';

    angular.module('app').controller('signCreateCtrl', sign);

    sign.$inject = ['$location','signSvc','$state']; 

    function sign($location, signSvc,$state) {        
        var vm = this;
    	vm.model = {};						//创建一个form对象
        vm.title = '新增收文';        		//标题

        vm.create = function () {
        	signSvc.createSign(vm);
        };       
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('signCtrl', sign);

    sign.$inject = ['$location','signSvc','$state','flowSvc','signFlowSvc']; 

    function sign($location,signSvc,$state,flowSvc,signFlowSvc) {        
        var vm = this;
        vm.title = "收文列表";
        
        //initGrid
        signSvc.grid(vm);
                
        vm.querySign = function(){
        	signSvc.querySign(vm);
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
         
         //start 发起流程
         vm.startFlow = function(signid){
        	 common.confirm({
              	 vm:vm,
              	 title:"",
              	 msg:"发起流程后，将不能对信息进行修改，确认发起流程么？",
              	 fn:function () {
                    	$('.confirmDialog').modal('hide');             	
                    	signSvc.startFlow(vm,signid);
                 }
              })
         }//end 发起流程
         
         //start 停止流程
         vm.stopFlow = function(signid){
        	 common.confirm({
              	 vm:vm,
              	 title:"",
              	 msg:"停止流程后，将无法对流程环节进行操作，确认停止么？",
              	 fn:function () {
                    $('.confirmDialog').modal('hide');             	
                    flowSvc.suspend(vm,signid);
                 }
              })
         }//end 停止流程
         
         //start 重启流程
         vm.restartFlow = function(signid){
        	 common.confirm({
              	 vm:vm,
              	 title:"",
              	 msg:"确认重启流程么？",
              	 fn:function () {
                    $('.confirmDialog').modal('hide');             	
                    flowSvc.activeFlow(vm,signid);
                 }
              })
         }//end 重启流程
         
         //************************** S 以下是新流程处理js **************************//
         vm.startNewFlow = function(signid){
        	 common.confirm({
              	 vm:vm,
              	 title:"",
              	 msg:"确认签收完成了么？",
              	 fn:function () {
                    	$('.confirmDialog').modal('hide');             	
                    	signFlowSvc.startFlow(vm,signid);
                 }
              })
         }
         //************************** S 以下是新流程处理js **************************//

         signSvc.associateGrid(vm);
         //start 项目关联
         vm.associateSign = function(signId){
            vm.currentSignId = signId;
            //选中要关联的项目
            vm.currentAssociateSign = vm.gridOptions.dataSource.get(signId);
            var signAssociateWindow=$("#associateWindow");
            signAssociateWindow.kendoWindow({
                width:"50%",
                height:"80%",
                title:"项目关联",
                visible:false,
                modal:true,
                closable:true,
                actions:["Pin","Minimize","Maximize","close"]
            }).data("kendoWindow").center().open();

            //初始化associateGrid

         }
         //end 项目关联

         //start 解除项目关联
         vm.disAssociateSign = function(signId){
             signSvc.saveAssociateSign(vm,signId);
         }
         //end 项目关联

         vm.saveAssociateSign = function(associateSignId){
            if(vm.currentSignId == associateSignId){
                common.alert({
                    vm:vm,
                    msg:"不能关联自身项目",
                    closeDialog:true,
                    fn:function() {
                    }
                });
                return ;
            }
            signSvc.saveAssociateSign(vm,vm.currentSignId,associateSignId);
         }
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('signFillinCtrl', sign);

    sign.$inject = ['$location','signSvc','$state']; 

    function sign($location, signSvc, $state) {        
        var vm = this;
    	vm.model = {};		//创建一个form对象   	
        vm.title = '填写报审登记表';        		//标题
        vm.model.signid = $state.params.signid;	//收文ID
       
        vm.flowDeal = false;		//是否是流程处理标记
        
        signSvc.initFillData(vm);
        
       //附件下载
        vm.signDownload = function(id){
        	signSvc.signDownload(vm,id);
        }
        //删除系统文件
        vm.delsSysFile = function(id){
        /*	 common.confirm({
            	 vm:vm,
            	 title:"",
            	 msg:"确认删除数据吗？",
            	 fn:function () {
                  	$('.confirmDialog').modal('hide');             	
                 }
             })*/
           signSvc.deleteSysFile(vm,id);
        }
       
        //附件上传
        vm.signUpload = function(){
        	 $("#signUploadWin").kendoWindow({
                 width : "660px",
                 height : "400px",
                 title : "附件上传",
                 visible : false,
                 modal : true,
                 closable : true,
                 actions : [ "Pin", "Minimize", "Maximize", "Close" ]
             }).data("kendoWindow").center().open();
        }
        //查看附件
        vm.signJquery = function(){
        	$("#signAttachments").kendoWindow({
                width : "800px",
                height : "400px",
                title : "查看附件",
                visible : false,
                modal : true,
                closable : true,
                actions : [ "Pin", "Minimize", "Maximize", "Close" ]
            }).data("kendoWindow").center().open();
        	signSvc.initFillData(vm);
        }
        //申报登记编辑
        vm.updateFillin = function (){   	   
    	   signSvc.updateFillin(vm);  	   
        }                  
       
       //根据协办部门查询用户
       vm.findOfficeUsersByDeptName =function(status){
    	   signSvc.findOfficeUsersByDeptName(vm,status);
       }
       $("input").click(function(){
//    	   signSvc.checkboxAdd(vm);
       })
	}
})();

(function () {
    'use strict';

    angular.module('app').controller('signEndCtrl', sign);

    sign.$inject = ['$location','signFlowSvc','$state','flowSvc'];

    function sign($location,signFlowSvc,$state,flowSvc) {
        var vm = this;
        vm.title = "已办结项目详情";
        vm.model = {};
        vm.flow = {};
        vm.model.signid = $state.params.signid;   //业务ID
        vm.flow.processInstanceId = $state.params.processInstanceId;	//流程实例ID
        vm.flow.hideFlowImg = true;

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
            signFlowSvc.endSignDetail(vm);
        }

    }
})();

(function() {
	'use strict';
	
	angular.module('app').factory('signFlowSvc', signFlow);
	
	signFlow.$inject = ['$http','$state'];

	function signFlow($http,$state) {
		var service = {
			startFlow : startFlow,			//启动流程
			initBusinessParams:initBusinessParams,	//初始化业务参数
			checkBusinessFill : checkBusinessFill,	//检查相应的表单填写
			getChargeWorkProgram:getChargeWorkProgram,//获取工作方案
			getChargeDispatch : getChargeDispatch,		//获取发文	（停用）
			getChargeFilerecord : getChargeFilerecord,	//获取归档信息（停用）
            endSignDetail:endSignDetail,                 //已办结的签收信息（停用）
		};
		return service;		
		
		
		//S_startFlow
		function startFlow(vm,signid){
			var httpOptions = {
					method : 'post',
					url : rootPath+"/sign/html/startNewFlow",
					params : {signid:signid}
				}
				
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.gridOptions.dataSource.read();
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
		}//E_startFlow

		//S_initBusinessParams
		function initBusinessParams(vm){
            switch (vm.flow.curNode.activitiId)
            {
                case "XMFZR_SP_GZFA1":  //项目负责人承办
                    vm.businessTr = true;
                    vm.XMFZR_SP_GZFA = true;
                    vm.MarkAndPay=false;//专家评分费用编辑权限
                    if(vm.model.isreviewCompleted && vm.model.isreviewCompleted == 9){ //如果填报完成，则显示
                        vm.show_workprogram = true;
                        $("#show_workprogram_a").click();
                    }
                    break;
                case "XMFZR_SP_GZFA2":
                    vm.businessTr = true;
                    vm.XMFZR_SP_GZFA = true;
                    vm.MarkAndPay=false;//专家评分费用编辑权限
                    if(vm.model.isreviewACompleted && vm.model.isreviewACompleted == 9){ //如果填报完成，则显示
                        vm.show_workprogram = true;
                        $("#show_workprogram_a").click();
                    };
                    break;
                case "BZ_SP_GZAN1":
                    vm.show_workprogram = true;
                    $("#show_workprogram_a").click();
                    break;
                case "FGLD_SP_GZFA1":
                    vm.show_workprogram = true;
                    $("#show_workprogram_a").click();
                    break;
                case "BZ_SP_GZAN2":
                    vm.show_workprogram = true;
                    $("#show_workprogram_a").click();
                    break;
                case "FGLD_SP_GZFA2":
                    vm.show_workprogram = true;
                    $("#show_workprogram_a").click();
                    break;
                case "FW_SQ":
                    vm.businessTr = true;
                    vm.FW_SQ = true;
                    if(vm.model.isDispatchCompleted && vm.model.isDispatchCompleted == 0){
                        vm.show_dispatch = true;
                        $("#show_dispatch_a").click();
                    };
                    break;
                case "BZ_SP_FW":
                    vm.show_dispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                case "FGLD_SP_FW":
                    vm.show_dispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                case "ZR_SP_FW":
                    vm.show_dispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                case "MFZR_GD":
                    vm.businessTr = true;
                    vm.MFZR_GD = true;
                    if(vm.model.filenum){
                        vm.show_filerecord = true;
                        $("#show_filerecord_a").click();
                    }
                    break;
                case "AZFR_SP_GD":
                    vm.show_filerecord = true;
                    $("#show_filerecord_a").click();
                    break;
                case "BMLD_QR_GD":
                    vm.show_filerecord = true;
                    $("#show_filerecord_a").click();
                    break;
                //以下为协审流程
                case "XS_XMFZR_GZFA":       //项目负责人承办
                    vm.businessTr = true;
                    vm.XS_XMFZR_GZFA = true;
                    if(vm.model.isreviewCompleted && vm.model.isreviewCompleted == 9){ //如果填报完成，则显示
                        vm.show_workprogram = true;
                        $("#show_workprogram_a").click();
                    }
                    break;
                case "XS_BZSP_GZFA":             //部长审批工作方案
                    vm.show_workprogram = true;
                    $("#show_workprogram_a").click();
                    break;
                case "XS_FGLDSP_GZFA":           //分管审批工作方案
                    vm.show_workprogram = true;
                    $("#show_workprogram_a").click();
                    break;
                case "XS_FW":                    //发文申请
                    vm.businessTr = true;
                    vm.FW_SQ = true;
                    if(vm.model.isDispatchCompleted && vm.model.isDispatchCompleted == 9){
                        vm.show_dispatch = true;
                        $("#show_dispatch_a").click();
                    };
                    break;
                case "XS_BZSP_FW":               //部长审批发文
                    vm.show_dispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                case "XS_FGLDSP_FW":             //分管领导审批发文
                    vm.show_dispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                case "XS_ZRSP_FW":               //主任审批发文
                    vm.show_dispatch = true;
                    $("#show_dispatch_a").click();
                    break;
                case "XS_FZR_GD":                //第一负责人归档
                    vm.businessTr = true;
                    vm.MFZR_GD = true;
                    if(vm.model.filenum){
                        vm.show_filerecord = true;
                        $("#show_filerecord_a").click();
                    }
                    break;
                case "XS_FZR_SP":                //第二负责人审批归档

                    break;
                case "XS_QRGD":                  //确认归档

                    break;
                default:
                    ;
            }
		}//E_initBusinessParams
		
		//S_checkBusinessFill
		function checkBusinessFill(vm){
			vm.flow.businessMap = {};
			var seleteCount = 0;
			var resultTag = true;
			switch (vm.flow.curNode.activitiId){
                case "ZHB_SP_SW":       //综合部拟办
                    if($("#viceDirector").val()){
                        vm.flow.businessMap.FGLD = $("#viceDirector").val();
                    }else{
                        resultTag = false;
                    }
                    break;
                case "FGLD_SP_SW":      //分管领导审批，要选择主办部门
                    $('.seleteTable input[selectType="main"]:checked').each(function(){
                        vm.flow.businessMap.hostdept = $(this).val();
                        seleteCount++;
                    });
                    if(seleteCount == 0){
                        resultTag = false;
                        break;
                    }
                    $('.seleteTable input[selectType="assist"]:checked').each(function(){
                        vm.flow.businessMap.assistdept = $(this).val();
                    });
                    break;
                case "BM_FB1":      //部门分办，要选择主办部门
                    $('.seleteTable input[selectType="main"]:checked').each(function(){
                        vm.flow.businessMap.M_USER_ID = $(this).val();
                        seleteCount++;
                    });
                    if(seleteCount == 0){
                        resultTag = false;
                        break;
                    }
                    $('.seleteTable input[selectType="assist"]:checked').each(function(){
                        vm.flow.businessMap.A_USER_ID = $(this).val();
                    });
                    break;
                case "BM_FB2":      //部门分办，要选择主办部门
                    $('.seleteTable input[selectType="main"]:checked').each(function(){
                        vm.flow.businessMap.M_USER_ID = $(this).val();
                        seleteCount++;
                    });
                    if(seleteCount == 0){
                        resultTag = false;
                        break;
                    }
                    $('.seleteTable input[selectType="assist"]:checked').each(function(){
                        vm.flow.businessMap.A_USER_ID = $(this).val();
                    });
                    break;
                case "XMFZR_SP_GZFA1":
                    if(vm.model.isreviewCompleted && vm.model.isreviewCompleted==9){
                        resultTag = true;
                    }else{
                        resultTag = false;
                    }
                    break;
                case "XMFZR_SP_GZFA2":
                    if(vm.model.isreviewACompleted && vm.model.isreviewACompleted==9){
                        resultTag = true;
                    }else{
                        resultTag = false;
                    }
                    break;
                case "BZ_SP_GZAN1":
                    vm.flow.businessMap.M_WP_ID = vm.mainwork.id;
                    break;
                case "BZ_SP_GZAN2":
                    vm.flow.businessMap.A_WP_ID = vm.assistwork.id;
                    break;
                case "FGLD_SP_GZFA1":
                    vm.flow.businessMap.M_WP_ID = vm.mainwork.id;
                    break;
                case "FGLD_SP_GZFA2":
                    vm.flow.businessMap.A_WP_ID = vm.assistwork.id;
                    break;
                case "FW_SQ":
                    if(vm.model.isDispatchCompleted && vm.model.isDispatchCompleted==9){
                        resultTag = true;
                    }else{
                        resultTag = false;
                    }
                    break;
                case "BZ_SP_FW":
                    vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id
                    break;
                case "FGLD_SP_FW":
                    vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id
                    break;
                case "ZR_SP_FW":
                    vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id
                    break;
                case "MFZR_GD":
                    if(vm.model.filenum){
                        resultTag = true;
                    }else{
                        resultTag = false;
                    }
                    break;

                 //以下是协审流程环节
                case "XS_ZHBBL":            //综合部办理
                    if($("#viceDirector").val()){
                        vm.flow.businessMap.FGLD = $("#viceDirector").val();
                    }else{
                        resultTag = false;
                    }
                    break;
                case "XS_FGLD_SP":          //分管领导审批
                    $('#xs_table input[selectType="xs_main"]:checked').each(function(){
                        vm.flow.businessMap.deptid = $(this).val();
                    });
                    if(!vm.flow.businessMap.deptid){
                        resultTag = false;
                    }
                    break;
                case "XS_BMFB":          //部门分办
                    $('.seleteTable input[selectType="main"]:checked').each(function(){
                        vm.flow.businessMap.M_USER_ID = $(this).val();
                        seleteCount++;
                    });
                    if(seleteCount == 0){
                        resultTag = false;
                        break;
                    }
                    $('.seleteTable input[selectType="assist"]:checked').each(function(){
                        vm.flow.businessMap.A_USER_ID = $(this).val();
                    });
                    break;
                case "XS_XMFZR_GZFA":           //项目负责人承办
                    vm.flow.businessMap.PSFA == vm.model.isNeedWrokPrograml
                    //选择了工作方案没填写的，不给通过
                    if(vm.model.isNeedWrokPrograml == '9' && (!vm.mainwork || !vm.mainwork.id)){
                        resultTag = false;
                    }
                    break;
                case "XS_BZSP_GZFA":             //部长审批工作方案
                    vm.flow.businessMap.WP_ID = vm.mainwork.id;
                    break;
                case "XS_FGLDSP_GZFA":           //分管领导审批
                    vm.flow.businessMap.WP_ID = vm.mainwork.id;
                    break;
                case "XS_FW":                    //发文申请
                    if(vm.model.isDispatchCompleted && vm.model.isDispatchCompleted==9){
                        resultTag = true;
                    }else{
                        resultTag = false;
                    }
                    break;
                case "XS_BZSP_FW":               //部长审批发文
                    vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id
                    break;
                case "XS_FGLDSP_FW":             //分管领导审批发文
                    vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id
                    break;
                case "XS_ZRSP_FW":               //主任审批发文
                    vm.flow.businessMap.DIS_ID = vm.dispatchDoc.id
                    break;
                case "XS_FZR_GD":                //第一负责人归档
                    if(vm.model.filenum){
                        resultTag = true;
                    }else{
                        resultTag = false;
                    }
                    break;
                case "XS_FZR_SP":                //第二负责人审批归档

                    break;
                case "XS_QRGD":                  //确认归档

                    break;
                default:
                    ;
            }

            return resultTag;
		}//E_checkBusinessFill
		
		//S_getChargeWorkProgram
		function getChargeWorkProgram(vm,isMain){
			var mainState = isMain == true?"9":"0";
			var httpOptions = {
					method : 'get',
					url : rootPath+"/workprogram/html/initWorkBySignId",
					params : {signId:vm.model.signid,isMain:mainState}
				}
			var httpSuccess = function success(response) {									
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {								
						vm.work = response.data;	
						$("#show_workprogram_a").click();
					}						
				});
			}
			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});		
		}//E_getChargeWorkProgram
		
		//S_getChargeDispatch
		function getChargeDispatch(vm){
			var httpOptions = {
					method : 'get',
					url : rootPath+"/dispatch/html/initDispatchBySignId",
					params : {signId:vm.model.signid}
				}
			var httpSuccess = function success(response) {									
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {								
						vm.dispatchDoc = response.data;	
						$("#show_dispatch_a").click();
					}						
				});
			}
			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});		
		}//E_getChargeDispatch
		
		//S_getChargeFilerecord
		function getChargeFilerecord(vm){
			var httpOptions = {
					method : 'get',
					url : rootPath+"/fileRecord/html/initBySignId",
					params : {signId:vm.model.signid}
				}
			var httpSuccess = function success(response) {									
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {								
						vm.fileRecord = response.data;	
						$("#show_filerecord_a").click();
					}						
				});
			}
			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});		
		}//E_getChargeFilerecord

        //S_endSignDetail
        function endSignDetail(vm){
            var httpOptions = {
                method : 'get',
                url : rootPath+"/sign/html/initDetailPageData",
                params : {signid:vm.model.signid,queryAll:true}
            }

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm:vm,
                    response:response,
                    fn:function() {
                        vm.model = response.data;
                        if(vm.model.workProgramDtoList && vm.model.workProgramDtoList.length > 0){
                            vm.show_workprogram = true;
                            vm.model.workProgramDtoList.forEach(function(w,index){
                            	if(w.isMain == 9){
                                    vm.showMainwork = true;
                                    vm.mainwork = {};
                                    vm.mainwork = w;
								}else if(w.isMain == 0){
                                    vm.showAssistwork = true;
                                    vm.assistwork = {};
                                    vm.assistwork = w;
								}
							});
                        }
                        if(vm.model.dispatchDocDto){
                            vm.show_dispatch = true;
                            vm.dispatchDoc = vm.model.dispatchDocDto;
                        }
                        if(vm.model.fileRecordDto){
                            vm.show_filerecord = true;
                            vm.fileRecord = vm.model.fileRecordDto;
                        }
                    }
                })
            }

            common.http({
                vm:vm,
                $http:$http,
                httpOptions:httpOptions,
                success:httpSuccess
            });
        }//E_endSignDetail
        
	}//E_signFlow		
})();
(function () {
    'use strict';

    angular.module('app').controller('signFlowDealCtrl', sign);

    sign.$inject = ['$location', 'signSvc', '$state', 'flowSvc', 'signFlowSvc',
        '$http'];

    function sign($location, signSvc, $state, flowSvc, signFlowSvc, $http) {
        var vm = this;
        vm.title = "项目流程处理";
        vm.model = {};
        vm.flow = {};
        vm.work = {};
        vm.dispatchDoc = {};
        vm.fileRecord = {};
        vm.model.signid = $state.params.signid;
        vm.flow.taskId = $state.params.taskId; // 流程任务ID
        vm.flow.processInstanceId = $state.params.processInstanceId; // 流程实例ID
        vm.dealFlow = true;
        vm.expertReview = {};
        vm.showExpertRemark = false;// 专家评分弹窗内容显示
        vm.showExpertpayment = false;// 专家费用弹窗内容显示
        vm.MarkAndPay = true;// 专家评分费用编辑权限

        active();
        function active() {
            $('#myTab li').click(function (e) {
                var aObj = $("a", this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#" + showDiv).addClass("active").addClass("in")
                    .show(500);
            })

            // 先初始化流程信息
            flowSvc.initFlowData(vm);
            // 再初始化业务信息
            signSvc.initFlowPageData(vm);

            // 初始化专家评分费用
            //flowSvc.markGrid(vm);
            //flowSvc.paymentGrid(vm);
        }

        // 编辑专家评分
        vm.editSelectExpert = function (id, score) {
            vm.expertReview.score = score;
            vm.expertReview.expertId = id;
            flowSvc.gotoExpertmark(vm);
        }
        // 关闭专家评分
        vm.closeEditMark = function () {
            window.parent.$("#expertmark").data("kendoWindow").close();
        }

        // 保存专家评分
        vm.saveMark = function () {
            flowSvc.saveMark(vm);
        }

        vm.editpayment = function (id) {
            vm.expertReview.expertId = id;
            flowSvc.gotopayment(vm);
        }
        // 计算应纳税额
        vm.countTaxes = function () {
            flowSvc.countTaxes(vm);
        }
        // 关闭专家费用
        vm.closeEditPay = function () {
            window.parent.$("#payment").data("kendoWindow").close();
        }
        // 保存专家费用
        vm.savePayment = function () {
            flowSvc.savePayment(vm);
        }
        // begin 添加审批意见
        vm.ideaEdit = function (options) {
            console.log(options);
            common.initIdeaData(vm, $http, options);
        }
        // end 添加审批意见

        //流程提交
        vm.commitNextStep = function () {
            if (signFlowSvc.checkBusinessFill(vm)) {
                flowSvc.commit(vm);
            } else {
                common.alert({
                    vm: vm,
                    msg: "请先完成相应的业务操作才能提交"
                })
            }
        }

        vm.commitBack = function () {
            flowSvc.rollBack(vm); // 回退到上一个环节
        }

        vm.deleteFlow = function () {
            common.confirm({
                vm: vm,
                title: "",
                msg: "终止流程将无法恢复，确认挂起么？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    flowSvc.deleteFlow(vm);
                }
            })
        }

        vm.initDealUerByAcitiviId = function () {
            flowSvc.initDealUerByAcitiviId(vm);
        }

        // 根据特定的环节隐藏相应的业务按钮
        vm.showBtByAcivitiId = function (acivitiId) {
            return vm.flow.curNodeAcivitiId == acivitiId ? true : false;
        }

        // S_跳转到 工作方案 编辑页面
        vm.addWorkProgram = function () {
            $state.go('workprogramEdit', {
                signid: vm.model.signid
            });
        }// E_跳转到 工作方案 编辑页面

        // S_跳转到 发文 编辑页面
        vm.addDisPatch = function () {
            $state.go('dispatchEdit', {
                signid: vm.model.signid
            });
        }// E_跳转到 发文 编辑页面

        vm.addDoFile = function () {
            $state.go('fileRecordEdit', {
                signid: vm.model.signid
            });
        }

        // 业务判断
        vm.checkBox = function ($event, type, disabletype) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            var checkboxValue = checkbox.value;
            if (checked) {
                $('.seleteTable input[selectType=\"' + type + '\"]').each(
                    function () {
                        var id = $(this).attr("id");
                        var value = $(this).attr("value");
                        if (id != (type + "_" + checkboxValue)) {
                            $("#" + disabletype + "_" + value)
                                .removeAttr("disabled");
                            $(this).removeAttr("checked");
                        } else {
                            $("#" + disabletype + "_" + checkboxValue)
                                .attr("disabled", "disabled");
                        }
                    });
            } else {
                $("#" + disabletype + "_" + checkboxValue)
                    .removeAttr("disabled");
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

        //协审项目负责人环节，确认是否要填写工作方案
        vm.checkNeedWP = function ($event) {
            var checkbox = $event.target;
            var checked = checkbox.checked;
            if (checked) {
                vm.model.isNeedWrokPrograml = '9'
            } else {
                //如果有发文信息，询问是否删除
                if (vm.mainwork && vm.mainwork.id) {
                    common.confirm({
                        vm: vm,
                        title: "",
                        msg: "取消会对填报的工作方案进行删除，确认删除么？",
                        fn: function () {
                            $('.confirmDialog').modal('hide');
                            signSvc.removeWP(vm);
                        },
                        cancel: function () {
                            checkbox.checked = !checked;
                            vm.model.isNeedWrokPrograml = '9'
                            $('.confirmDialog').modal('hide');
                        }
                    })
                }
            }

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

        	//先初始化流程信息        
        	flowSvc.initFlowData(vm);
        	//再初始化业务信息
        	signSvc.initFlowPageData(vm);
        }

    }
})();

(function () {
    'use strict';

    angular.module('app').controller('signDetailsCtrl', sign);

    sign.$inject = ['$location','signSvc','$state','flowSvc'];

    function sign($location, signSvc,$state,flowSvc) {
        var vm = this;
    	vm.model = {};							//创建一个form对象   	
        vm.title = '查看详情信息';        		//标题
        vm.model.signid = $state.params.signid;	//收文ID


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

            signSvc.initAssociateSigns(vm,vm.model.signid,function(response){
                if(response.data != undefined){
                    vm.associateSign = response.data;
                    var signs = response.data;
                   // console.log(signs);
                    var steps = [];
                    var html_ = '';
                    for(var i = (signs.length-1);i>=0;i--){
                        var s = signs[i];
                      /*  var htm = '<div style="position: absolute;bottom: 1px;">名称：<span style="color:red;">'+s.projectname+'</span><br/>'
                                  +s.reviewstage
                                  +'</div>';*/
                        //steps.push({title:htm});

                        var signdate = s.signdate||'';
                        html_ += '<div class="intro-list">'+
                                    '<div class="intro-list-left">'+
                                        '项目阶段：'+s.reviewstage+"<br/>签收时间："+signdate+
                                    '</div>'+
                                    '<div class="intro-list-right">'+
                                        '<span></span>'+
                                        '<div class="intro-list-content">'+
                                            '名称：<span style="color:red;">'+s.projectname+'</span><br/>'+
                                            '送件人：<span style="color:red;">'+s.sendusersign+'</span><br/>'+
                                        '</div>'+
                                    '</div>'+
                                 '</div>';

                    }
                    $('#introFlow').html(html_);
                    var step= $("#myStep").step({
                    			animate:true,
                    			initStep:1,
                    			speed:1000
                    		});

                }
            });
            if($state.params.processInstanceId){
                vm.flow = {}
                vm.flow.processInstanceId = $state.params.processInstanceId;	//流程实例ID
                flowSvc.initFlowData(vm);
            }
        }

    }
})();

(function() {
	'use strict';
	
	angular.module('app').factory('signSvc', sign);
	
	sign.$inject = ['$http','$state','flowSvc'];

	function sign($http,$state,flowSvc) {
		var service = {
			grid : grid,						//初始化项目列表
			querySign : querySign,				//查询
			createSign : createSign,			//新增
			initFillData : initFillData,		//初始化表单填写页面（可编辑）
			initDetailData : initDetailData,	//初始化详情页（不可编辑）
			updateFillin : updateFillin,		//申报编辑
			deleteSign :　deleteSign,			//删除收文
			findOfficeUsersByDeptName :findOfficeUsersByDeptName,//根据协办部门名称查询用户
			initFlowPageData : initFlowPageData, //初始化流程收文信息
			initUpload:initUpload,//初始化上传附件控件
			deleteSysFile:deleteSysFile,//删除系统文件

            removeWP : removeWP,             //删除工作方案
            signDownload:signDownload,	//附件下载
            associateGrid:associateGrid,//项目关联列表
            saveAssociateSign:saveAssociateSign,//保存项目关联
            initAssociateSigns:initAssociateSigns//初始化项目关联信息

		};
		return service;
		
		//附件下载
		function signDownload(vm,id){
			var sysfileId = id;
			window.open(rootPath+"/file/fileDownload?sysfileId="+id);
		}
		
		//S 删除系统文件
		function deleteSysFile(vm,id){
			var httpOptions = {
				method : 'delete',
				url : rootPath+"/file/deleteSysFile",
				data : id

			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn:function(){		
						window.parent.$("#signAttachments").data("kendoWindow").close();
						common.alert({
							vm:vm,
							msg:"删除成功",
							fn:function() {
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
		//E 删除系统文件
		
		//S_初始化上传附件控件
        function initUpload(vm){
        	var businessId = vm.model.signid;
            var projectfileoptions = {
                language : 'zh',
                allowedPreviewTypes : ['image'],
                allowedFileExtensions : [ 'jpg', 'png', 'gif',"xlsx","docx","doc","xls","pdf" ],
                maxFileSize : 2000,
                showRemove: false,
                uploadUrl:rootPath + "/file/fileUpload",
                uploadExtraData:{businessId:businessId}
            };
            $("#signphotofile").fileinput(projectfileoptions).on("filebatchselected", function(event, files){

            }).on("fileuploaded", function(event, data) {
                $("#signPhotoSrc").removeAttr("src");
                $("#signPhotoSrc").attr("src",rootPath+"/sign/transportImg?signid="+businessId+"&t="+Math.random());
            });
        }
		//E_初始化上传附件控件
        
        
		//S_初始化grid
		function grid(vm){
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata', 
				transport :common.kendoGridConfig().transport(rootPath+"/sign/fingByOData",$("#searchform")),
				schema : common.kendoGridConfig().schema({
					id : "signid",
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
						template : function(item) {
							return kendo.format("<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",item.signid)
						},
						filterable : false,
						width : 40,
						title : "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"

					},
					{
						field : "projectname",
						title : "项目名称",
						width : 160,
						filterable : false
					},
					{
						field : "projectcode",
						title : "收文编号",
						width : 160,
						filterable : false,
					},
					{
						field : "designcompanyName",
						title : "项目单位",
						width : 200,
						filterable : false,
					},
					{
						field : "reviewstage",
						title : "项目阶段",
						width : 160,
						filterable : false,
					},
					{
						field : "projectcode",
						title : "项目代码",
						width : 160,
						filterable : false,
					},
					{
						field : "receivedate",
						title : "收文时间",
						width : 160,
						filterable : false,
						format : "{0:yyyy/MM/dd HH:mm:ss}"

					},					
					{
						field : "",
						title : "流程状态",
						width : 160,
						filterable : false,
						template : function(item) {
							if(item.folwState){
								if(item.folwState == 1){
									return '<span style="color:green;">进行中</span>';
								}else if(item.folwState == 2){
									return '<span style="color:orange;">已暂停</span>';
								}else if(item.folwState == 8){
									return '<span style="color:red;">强制结束</span>';
								}else if(item.folwState == 9){
									return '<span style="color:blue;">已完成</span>';
								}
							}else{
								return "未发起"
							}
						}
					},
					{
						field : "",
						title : "操作",
						width : 180,
						template : function(item) {
							//如果已经发起流程，则只能查看
							var isFlowStart = false,hideStopButton = true,hideRestartButton=true,isAlreadyAssociate = false;
							if(item.folwState && item.folwState > 0){
								isFlowStart = true;
								if(item.folwState == 1){
									hideStopButton = false;
								}
								if(item.folwState == 2){
									hideRestartButton = false;
								}
								if(item.isAssociate == 1){
								    isAlreadyAssociate = true;
								}
							}
							return common.format($('#columnBtns').html(), item.signid, item.folwState,
                                    item.signid+"/"+ item.processInstanceId,"vm.del('" + item.signid + "')",isFlowStart,
									"vm.startNewFlow('" + item.signid + "')", isFlowStart,
									"vm.stopFlow('" + item.signid + "')", hideStopButton,
									"vm.restartFlow('" + item.signid + "')", hideRestartButton,
									isAlreadyAssociate,"vm.associateSign('" + item.signid + "')","vm.disAssociateSign('" + item.signid + "')"
									);
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
		}//E_初始化grid
		
		//S_查询grid
		function querySign(vm){	
			vm.gridOptions.dataSource.read();			
		}//E_查询grid		
								
		//S_创建收文
		function createSign(vm){
			common.initJqValidation();
			var isValid = $('form').valid();				
			if (isValid) {				
				var httpOptions = {
						method : 'post',
						url : rootPath+"/sign",
						data : vm.model
					}
					var httpSuccess = function success(response) {									
						common.requestSuccess({
							vm:vm,
							response:response,
							fn:function() {				
								common.alert({
									vm:vm,
									msg:"操作成功,请继续填写报审登记表！",
									closeDialog:true,
									fn:function() {
										$state.go('fillSign', {signid: response.data.signid});
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
		}//E_创建收文	
		
		//start  根据协办部门查询用户
		function findOfficeUsersByDeptName(vm,status){
			var param = {};
			if("main" == status){
                param.maindeptName = vm.model.maindeptName;
			}else{
				param.assistdeptName = vm.model.assistdeptName;
			}
			var httpOptions = {
					method : 'post',
					url  : rootPath+"/officeUser/findOfficeUsersByDeptName",
					data:param
				};
				
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						if("main" == status){
							vm.mainOfficeList = {};
							vm.mainOfficeList = response.data;
						}else{
							vm.assistOfficeList = {};
							vm.assistOfficeList = response.data;
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
		//end  根据协办部门查询用户
		
		//Start 申报登记编辑
		function updateFillin(vm){
				common.initJqValidation($('#sign_fill_form'));
				var isValid = $('#sign_fill_form').valid();	 
				if (isValid) {
				var httpOptions = {
					method : 'put',
					url : rootPath+"/sign",
					data : vm.model,
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
			}
		}
		//End 申报登记编辑
		
		//Start 删除收文 
		function deleteSign(vm, signid) {			
			vm.isSubmit = true;
			var httpOptions = {
				method : 'delete',
				url : rootPath+"/sign",
				data : signid
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
		//End 删除收文				
		
		//S_初始化填报页面数据
		function initFillData(vm){		
			var httpOptions = {
					method : 'get',
					url : rootPath+"/sign/html/initFillPageData",
					params : {signid : vm.model.signid}						
				}

			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {											
						vm.model = response.data.sign;	
						vm.deptlist =response.data.deptlist	
						
						if(response.data.mainOfficeList){
							vm.mainOfficeList=response.data.mainOfficeList;
						}
						if(response.data.assistOfficeList){
							vm.assistOfficeList=response.data.assistOfficeList;
						}
						//建设单位
						vm.builtcomlist = response.data.builtcomlist;
						//编制单位
						vm.designcomlist = response.data.designcomlist;
						 //系统文件
		                vm.sysFilelist = response.data.sysFiles;
						initUpload(vm);
					}					
				})
			}

			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}//E_初始化填报页面数据				
		
		//S_初始化详情数据	
		function initDetailData(vm){
			var httpOptions = {
					method : 'get',
					url : rootPath+"/sign/html/initDetailPageData",
					params : {signid:vm.model.signid}
				}

			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {
						vm.model = response.data;													
					}					
				})
			}

			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}//E_初始化详情数据
						
		//S_初始化流程页面
		function initFlowPageData(vm){
			var httpOptions = {
					method : 'get',
					url : rootPath+"/sign/html/initFlowPageData",
					params : {signid:vm.model.signid,queryAll:true}
				}
				
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.model = response.data;
                        //工作方案
                        if(vm.model.workProgramDtoList && vm.model.workProgramDtoList.length > 0){
                            vm.show_workprogram = true;
                            vm.model.workProgramDtoList.forEach(function(w,index){
                            	if(w.isMain == 9){
                                    vm.showMainwork = true;
                                    vm.mainwork = {};
                                    vm.mainwork = w;
								}else if(w.isMain == 0){
                                    vm.showAssistwork = true;
                                    vm.assistwork = {};
                                    vm.assistwork = w;
								}
							});
                        }
                        //发文
                        if(vm.model.dispatchDocDto){
                            vm.show_dispatch = true;
                            vm.dispatchDoc = vm.model.dispatchDocDto;
                        }
                        //归档
                        if(vm.model.fileRecordDto){
                            vm.show_filerecord = true;
                            vm.fileRecord = vm.model.fileRecordDto;
                        }
                        //抽取专家
						if(vm.model.expertSelectedDtoList && vm.model.expertSelectedDtoList.length > 0){
                            vm.show_expert = true;
						}
                        //先加载完业务数据，再加载流程业务数据
						if(vm.dealFlow){
                            flowSvc.getFlowInfo(vm);
						}
					}
				});
			}
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}//E_初始化流程页面

        //S_removeWP
        function removeWP(vm){
            var httpOptions = {
                method : 'delete',
                url : rootPath+"/workprogram/deleteBySignId",
                params : {signId:vm.model.signid}
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
                });
            }
            common.http({
                vm : vm,
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess
            });
        }//E_removeWP

        //associateGrid
        function associateGrid(vm){
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type : 'odata',
                transport :common.kendoGridConfig().transport(rootPath+"/sign/fingByOData",$("#searchform")),
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
                        field : "projectname",
                        title : "项目名称",
                        width : 160,
                        filterable : false
                    },
                    {
                        field : "projectcode",
                        title : "收文编号",
                        width : 140,
                        filterable : false,
                    },
                    {
                        field : "designcompanyName",
                        title : "项目单位",
                        width : 200,
                        filterable : false,
                    },
                    {
                        field : "reviewstage",
                        title : "项目阶段",
                        width : 160,
                        filterable : false,
                    },
                    {
                        field : "projectcode",
                        title : "项目代码",
                        width : 140,
                        filterable : false,
                    }/*,
                    {
                        field : "",
                        title : "流程状态",
                        width : 160,
                        filterable : false,
                        template : function(item) {
                            if(item.folwState){
                                if(item.folwState == 1){
                                    return '<span style="color:green;">进行中</span>';
                                }else if(item.folwState == 2){
                                    return '<span style="color:orange;">已暂停</span>';
                                }else if(item.folwState == 8){
                                    return '<span style="color:red;">强制结束</span>';
                                }else if(item.folwState == 9){
                                    return '<span style="color:blue;">已完成</span>';
                                }
                            }else{
                                return "未发起"
                            }
                        }
                    }*/,
                    {
                        field : "",
                        title : "操作",
                        width : 180,
                        template : function(item) {
                            return common.format($('#associateColumnBtns').html(),"vm.saveAssociateSign('" + item.signid + "')");
                        }
                    }
            ];
            // End:column

            vm.associateGridOptions = {
                dataSource : common.gridDataSource(dataSource),
                filterable : common.kendoGridConfig().filterable,
                pageable : common.kendoGridConfig().pageable,
                noRecords : common.kendoGridConfig().noRecordMessage,
                columns : columns,
                resizable : true
            };
            vm.associateGridOptions.dataSource.read();
        }//E_初始化associateGrid

        //start saveAssociateSign
        //如果associateSignId为空，解除关联
        function saveAssociateSign(vm,signId,associateSignId){
            var httpOptions = {
                method : 'post',
                headers:{'Content-Type':'application/x-www-form-urlencoded'},
                url : rootPath+"/sign/associate",
                data : $.param({signId:signId,associateId:associateSignId},true),

            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm:vm,
                    response:response,
                    fn:function() {
                        common.alert({
                            vm:vm,
                            msg:associateSignId != undefined?"项目关联成功":"项目解除关联成功",
                            closeDialog:true,
                            fn:function() {
                                //关闭项目关联窗口
                                vm.gridOptions.dataSource.read();
                            }
                        });
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
        //end saveAssociateSign

        //start initAssociateSigns
        function initAssociateSigns(vm,singid,callBack){

            var httpOptions = {
                method : 'get',
                headers:{'Content-Type':'application/x-www-form-urlencoded'},
                url : rootPath+"/sign/associate?signId="+singid,

            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm:vm,
                    response:response,
                    fn:function() {
                        if(callBack != undefined&&typeof callBack == "function"){
                            callBack(response);
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
        }
        //end initAssociateSigns
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

    angular
        .module('app')
        .controller('userEditCtrl', user);

    user.$inject = ['$location', 'userSvc', '$state'];

    function user($location, userSvc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加用户';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新用户';
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
            queryUser:queryUser
        };

        return service;
        
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
                vm.model.roles = nodes_role;

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
                vm.model.roles = nodes_roles;

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
                url: common.format(url_user + "/getOrg")
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
                url: url_role
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
                        var zNodes = $linq(response.data.value).select(
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
                        zTreeObj = $.fn.zTree.init($("#zTree"), setting,
                            rootNode);
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
                method: 'POST',
                url: common.format(url_user +"/fingByOData"+ "?$filter=id eq '{0}'", vm.id)
            }
            var httpSuccess = function success(response) {
                vm.model = response.data.value[0];
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
                transport: common.kendoGridConfig().transport(url_user+"/fingByOData?$orderby=userSort",$("#usersform")),
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
				    width: 70,
				    filterable : false,
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
                    field: "userMPhone",
                    title: "联系手机",
                    width: 120,
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
                    template: function(item) {
						if(item.roles){
							var resultStr = "";
							for(var i=0,l=item.roles.length;i<l;i++){
							    if(i == 0){
                                    resultStr += item.roles[i].roleName
                                }else{
                                    resultStr += ", "+item.roles[i].roleName ;
                                }
							}
							return resultStr;
						}
						else{
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
                dataBound:dataBound,
                resizable: true
            };

        }// end fun grid

        //查询
        function queryUser(vm){
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
            var checkedNodes = $linq(vm.model.roles).select(function (x) {
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

    angular.module('app').controller('workprogramEditCtrl', workprogram);

    workprogram.$inject = ['$location','workprogramSvc','$state']; 

    function workprogram($location,workprogramSvc,$state) {        
        var vm = this;
    	vm.work = {};						//创建一个form对象
        vm.title = '创建评审方案';        	//标题
        vm.startDateTime = new Date("2006/6/1 08:00");
        vm.endDateTime = new Date("2030/6/1 21:00"); 
        vm.work.signId = $state.params.signid;		//这个是收文ID
        vm.linkSignId=" ";
        vm.sign = {};						//创建收文对象
        vm.isRoomBook = false;              //是否已经预定了会议时间
        vm.isHavePre = false;               //预定多个会议室的时候，查看上一个
        vm.isHaveNext = false;              //预定多个会议室 的时候，查看下一个
        
        vm.isHideProject = true;
        vm.isHideProject2 = true;
                	
        
        activate();
        function activate() {
//        	workprogramSvc.getInitSeleSignBysId(vm);//初始化已选项目列表
        	workprogramSvc.getInitRelateData(vm);
        	workprogramSvc.initPage(vm);
            workprogramSvc.findAllMeeting(vm);//查找所有会议室地
            workprogramSvc.findCompanys(vm);//查找主管部门
          
        }
        //文件下载
        vm.workDownload = function(id){
        	workprogramSvc.workDownload(vm,id);
        }
        //删除系统文件
        vm.delsWorkSysFile = function(id){
        	workprogramSvc.delsWorkSysFile(vm,id);
        }
        //查看附件列表
        vm.workJquery =function(){
        	$("#workqueryWin").kendoWindow({
                width : "800px",
                height : "400px",
                title : "查看附件列表",
                visible : false,
                modal : true,
                closable : true,
                actions : [ "Pin", "Minimize", "Maximize", "Close" ]
            }).data("kendoWindow").center().open();
        	workprogramSvc.getInitRelateData(vm);
        }
        //附件上传
        vm.workUpload = function(){
        	 $("#workUploadWin").kendoWindow({
                 width : "660px",
                 height : "400px",
                 title : "附件上传",
                 visible : false,
                 modal : true,
                 closable : true,
                 actions : [ "Pin", "Minimize", "Maximize", "Close" ]
             }).data("kendoWindow").center().open();
        }
        //重置
        vm.formResetWork=function(){
        	var values=$("#searchformi").find("input,select");
        	values.val("");
        }
        //查询
        vm.searchWorkSign = function(){
        	workprogramSvc.waitProjects(vm);
        }
        
        //关闭窗口
        vm.onWorkClose=function(){
        	window.parent.$(".workPro").data("kendoWindow").close();
        }
        //保存合并评审
        vm.mergeAddWork = function(){
        	workprogramSvc.mergeAddWork(vm);
        }
        //合并评审
        vm.reviewType = function(){
       	if(vm.work.isSigle=="1"){
      		var isHideProject=false;
       	}
        if(vm.work.isSigle == vm.work.isMainProject){
       		common.confirm({
    	           	 vm:vm,
    	           	 title:"",
    	           	 msg:"该项目已经关联其他合并评审会关联，您确定要改为单个评审吗？",
    	           	 fn:function () {
   	               	$('.confirmDialog').modal('hide');             	
    	              }
    	         })
       
        		var isHideProject=false;
        	}
        	
        }
        
        //主项目
        vm.mainIschecked = function(){
        	vm.isHideProject2 = false;
        }
        //次项目
        vm.subIschecked = function(){
//        	vm.isHideProject2 = true;
//        	workprogramSvc.subIschecked(vm);
        }
        //项目关联页面
        vm.gotoProjcet = function(){
        	workprogramSvc.gotoProjcet(vm);
        
        }
        //选择项目
        vm.selectworkProject = function(){
        	workprogramSvc.selectworkProject(vm);
        }
        //取消项目
        vm.cancelworkProject = function(){
        	workprogramSvc.cancelworkProject(vm);
        }
     
        //会议预定添加弹窗
        vm.addTimeStage = function(){
            //如果已经预定了会议室，则显示
            if(vm.isRoomBook){
            	console.log($("#stageWindow"));
                $("#stageWindow").kendoWindow({
                    width : "660px",
                    height : "550px",
                    title : "会议预定添加",
                    visible : false,
                    modal : true,
                    closable : true,
                    actions : [ "Pin", "Minimize", "Maximize", "Close" ]
                }).data("kendoWindow").center().open();
            //否则，跳转到选择会议室页面
            }else{
                if(vm.work.id){
                    $state.go('room', {workProgramId:vm.work.id});
                }else{
                    common.alert({
                        vm:vm,
                        msg:"请先保存，再选择评审会日期！"
                    })
                }
            }

        }
        
        //会议预定添加
        vm.saveRoom = function(){
        	workprogramSvc.saveRoom(vm);
        }

        //调整到会议室预定页面
        vm.gotoRoom = function(){
            window.parent.$("#stageWindow").data("kendoWindow").close();
            if(vm.work.id){
                $state.go('room', {workProgramId:vm.work.id});
            }else{
                common.alert({
                    vm:vm,
                    msg:"请先保存！"
                })
            }
        }

        //下一个会议预定信息
        vm.nextBookRoom = function(){
            var curIndex = 0;
            vm.RoomBookings.forEach(function (u, number) {
                if(u.id == vm.roombook.id){
                    curIndex = number;
                }
            });
            vm.isHavePre = true;
            if(curIndex == (vm.RoomBookings.length-2)){
                vm.isHaveNext = false;
            }else{
                vm.isHaveNext = true;
            }
            vm.roombook = vm.RoomBookings[curIndex+1];
        }

        //上一次会议预定信息
        vm.preBookRoom = function(){
            var curIndex = 0;
            vm.RoomBookings.forEach(function (u, number) {
                if(u.id == vm.roombook.id){
                    curIndex = number;
                }
            });
            vm.isHaveNext = true;
            if(curIndex == 1){
                vm.isHavePre = false;
            }else{
                vm.isHavePre = true;
            }
            vm.roombook = vm.RoomBookings[curIndex-1];
        }
        
        vm.onRoomClose = function(){
        	window.parent.$("#stageWindow").data("kendoWindow").close();
        }
        
        vm.queryRoom = function(){
        	workprogramSvc.queryRoom(vm);
        }
        //查询评估部门
        vm.findUsersByOrgId = function(type){
        	workprogramSvc.findUsersByOrgId(vm,type);
        }
        
        vm.create = function () {  
        	workprogramSvc.createWP(vm);
        };  
        
        vm.selectExpert = function(){
        	workprogramSvc.selectExpert(vm);
        }
            
        vm.findReviewDept = function(){
        
        }
    }
})();

(function() {
	'use strict';
	
	angular.module('app').factory('workprogramSvc', workprogram);
	
	workprogram.$inject = ['$rootScope','$http','$state'];
	function workprogram($rootScope,$http,$state) {
		var url_user = rootPath + "/user";
		var url_company = rootPath + "/company";
		var url_work = rootPath + "/workprogram";
		var service = {
			initPage : initPage,				//初始化页面参数
			createWP : createWP,				//新增操作
			findCompanys : findCompanys,		//查找主管部门
			findUsersByOrgId : findUsersByOrgId,//查询评估部门
			selectExpert:selectExpert,			//选择专家
			saveRoom:saveRoom,					//添加会议预定
            findAllMeeting:findAllMeeting,      //查找会议室地点
			gotoProjcet:gotoProjcet,			//项目关联弹窗
			waitProjects:waitProjects,			//待选项目列表
			selectedProject:selectedProject,	//已选项目列表
			selectworkProject:selectworkProject,//选择项目
			cancelworkProject:cancelworkProject,//取消项目
			mergeAddWork:mergeAddWork,			//保存合并评审
			getInitSeleSignBysId:getInitSeleSignBysId,//初始化已选项目列表
			getInitRelateData:getInitRelateData,	//初始化关联数据
			initWorkUpload:initWorkUpload,//初始上传附件控件
			delsWorkSysFile:delsWorkSysFile,//删除系统文件
			workDownload:workDownload,//文件下载
		};
		return service;
		
		function workDownload(vm,id){
			var sysfileId = id;
			window.open(rootPath+"/file/fileDownload?sysfileId="+id);
		}
		//S_删除系统文件
		function delsWorkSysFile(vm,id){
			var httpOptions = {
					method : 'delete',
					url : rootPath+"/file/deleteSysFile",
					data : id

				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn:function(){		
							window.parent.$("#workqueryWin").data("kendoWindow").close();
							common.alert({
								vm:vm,
								msg:"删除成功",
								fn:function() {
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
		//E_删除系统文件
		
		//S_初始化已选项目列表
		function getInitSeleSignBysId(vm){
			 var bussnessId = vm.work.id;
			var httpOptions = {
					method : 'get',
					url : rootPath+"/workprogram/getInitSeleSignBysId",
					params:{
						bussnessId: bussnessId
					}
				}
			var httpSuccess = function success(response) {
				vm.selectedSign=response.data.signDtoList;
				console.log(vm.selectedSign);
				vm.linkSignId=response.data.linkSignId;
				console.log(vm.linkSignId);
			} 
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}
		//E_初始化已选项目列表
		
		//S_初始化关联数据
		function getInitRelateData(vm){
			 var signid = vm.work.signId;
				var httpOptions = {
						method : 'post',
						url : rootPath+"/workprogram/getInitRelateData",
						params:{
							signid: signid
						}
					}
				var httpSuccess = function success(response) {
					vm.linkSignId=response.data.linkSignId;
					console.log(vm.linkSignId);
					//系统附件
	                vm.sysFiles = response.data.sysFilelist;
	                
				} 
				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});
		
		}//E_初始化关联数据
		
		//S_保存合并评审
		function mergeAddWork(vm){
			var httpOptions = {
					method : 'get',
					url : rootPath+"/workprogram/mergeAddWork",
					params : {signId : vm.work.signId,linkSignId :vm.linkSignId }						
			}
			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function(){		
						window.parent.$(".workPro").data("kendoWindow").close();
						common.alert({
							vm:vm,
							msg:"操作成功",
							fn:function() {
								$('.alertDialog').modal('hide');
								$('.modal-backdrop').remove();
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
		//E_保存合并评审
		
		//S_已选项目列表
		function selectedProject(vm){
			
			var httpOptions = {
					method : 'post',
					url : rootPath+"/workprogram/selectedProject",
					params:{
						linkSignIds:vm.linkSignId
					}
			}
			var httpSuccess = function success(response) {
				vm.selectedSign=response.data;
				console.log(vm.selectedSign);
			} 
			common.http({
				vm : vm,
				$http : $http,
				httpOptions : httpOptions,
				success : httpSuccess
			});
		}
		//E_已选项目列表
		
		//S_待选项目列表
		function waitProjects(vm){
			vm.sign.signid = vm.linkSignId;
			
			var httpOptions = {
	                method: 'post',
	                url: common.format(url_work + "/waitProjects"),
	                data:vm.sign
	            }
	            var httpSuccess = function success(response) {
	                vm.signs = {};
	                vm.signs = response.data;
	                console.log(vm.signs);
	            }
	            common.http({
	                vm: vm,
	                $http: $http,
	                httpOptions: httpOptions,
	                success: httpSuccess
	            });
		}
		//E_待选项目列表
		
		//S_选择取消
		function cancelworkProject(vm){
			var idStr=vm.linkSignId;
			var linkSignId=$("input[name='checkcancel']:checked");
			if(linkSignId){
				$.each(linkSignId, function(i, obj) {
					if(idStr.lastIndexOf(obj.value)==0){
						idStr=idStr.replace(obj.value,"");
					}else{
						idStr=idStr.replace(","+obj.value,"");
					}
				});
				vm.linkSignId=idStr
				selectedProject(vm);//已选
				waitProjects(vm);//待选
			}
		}
		//S_选择取消
		
		//S_选择项目
		function selectworkProject(vm){
			
			var idStr=vm.linkSignId;
			var linkSignId=$("input[name='checkwork']:checked");
			
			var ids=[];
			if(linkSignId){
				 $.each(linkSignId, function(i, obj) {
					ids.push(obj.value);
				 });
				 if(idStr){
					 idStr+=","+ids.join(',');
				 }else{
					 idStr=ids.join(',');
				 }
				 vm.linkSignId=idStr;
				 selectedProject(vm);//已选
				 waitProjects(vm);//待选
			}
		}
		
		//S_关联项目弹窗
		function gotoProjcet(vm) {
			if(!vm.work.id){
				common.alert({
					vm:vm,
					msg:"请先保存再进行关联！",
					fn:function() {
						$('.alertDialog').modal('hide');
						$('.modal-backdrop').remove();
					}
				})	
				return;
			}
			 $(".workPro").kendoWindow({
                 width : "1200px",
                 height : "630px",
                 title : "合并评审",
                 visible : false,
                 modal : true,
                 closable : true,
                 actions : [ "Pin", "Minimize", "Maximize", "Close" ]
             }).data("kendoWindow").center().open();
			 getInitSeleSignBysId(vm);//初始化
			 waitProjects(vm);//待选
			
		}
		//S_关联项目弹窗			
		
		// 清空页面数据
		// begin#cleanValue
		function cleanValue() {
			var tab = $("#stageWindow").find('input');
			$.each(tab, function(i, obj) {
				obj.value = "";
			});
		}
		
		//S_会议预定添加
		function saveRoom(vm){	
			common.initJqValidation($('#stageForm'));
			var isValid = $('#stageForm').valid();
			if (isValid) {
				vm.roombook.workProgramId = vm.work.id;
                vm.roombook.stageOrg = vm.work.reviewOrgName;
                vm.roombook.stageProject = "项目名称:"+vm.work.projectName+":"+vm.work.buildCompany+":"+vm.work.reviewOrgName;
                vm.roombook.beginTimeStr = $("#beginTime").val();
                vm.roombook.endTimeStr = $("#endTime").val();
                if($("#endTime").val() <$("#beginTime").val()){
                    $("#errorTime").html("开始时间不能大于结束时间!");
                    return ;
                }
                vm.roombook.beginTime = $("#rbDay").val()+" "+$("#beginTime").val()+":00";
                vm.roombook.endTime = $("#rbDay").val()+" "+$("#endTime").val()+":00";
				var httpOptions = {
					method : 'post',
					url : rootPath + "/room/saveRoom",
					data : vm.roombook
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							window.parent.$("#stageWindow").data("kendoWindow").close();
							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
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
		//E_会议预定添加

		//S_查找所有会议室地点
		function findAllMeeting(vm){
			var httpOptions = {
	                method: 'get',
	                url: common.format(rootPath + "/room/meeting")
	            }
	            var httpSuccess = function success(response) {
	                vm.roombookings = {};
	                vm.roombookings = response.data;
	            }
	            common.http({
	                vm: vm,
	                $http: $http,
	                httpOptions: httpOptions,
	                success: httpSuccess
	            });
		}
		//E_查找所有会议室地点
		
		//start 查找主管部门
		function findCompanys(vm){
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
		function findUsersByOrgId(vm,type){
			var param = {};
			if("main" == type){
				param.orgId = vm.work.reviewDept;
			}
			var httpOptions = {
					method : 'get',
					url : rootPath+"/user/findUsersByOrgId",
					params:param					
				};
				
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {		
						if("main" == type){
							vm.mainUserList = {};
							vm.mainUserList = response.data;
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
		//E_根据部门ID选择用户
		
		//S_初始化页面参数
		function initPage(vm){
		
			var httpOptions = {
				method : 'get',
				url : rootPath+"/workprogram/html/initWorkBySignId",
				params : {signId:vm.work.signId}
			}
			var httpSuccess = function success(response) {									
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {		
						if(response.data != null && response.data != ""){
							vm.work = response.data;
							vm.work.signId = $state.params.signid
							if(response.data.roomBookingDtos && response.data.roomBookingDtos.length > 0){
                                vm.isRoomBook = true;
                                vm.RoomBookings = {};
                                vm.RoomBookings = response.data.roomBookingDtos;
                                vm.roombook = vm.RoomBookings[0];
                                if(vm.RoomBookings.length > 1){
                                    vm.isHaveNext = true;
                                }
							}
							
						}
						initWorkUpload(vm);
					}						
				});
			}
			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}//S_初始化页面参数	
		
		//S_初始化上传附件控件
        function initWorkUpload(vm){
        	var businessId = vm.work.signId;
            var projectfileoptions = {
                language : 'zh',
                allowedPreviewTypes : ['image'],
                allowedFileExtensions : [ 'jpg', 'png', 'gif',"xlsx","docx" ,"pdf","doc","xls"],
                maxFileSize : 2000,
                showRemove: false,
                uploadUrl:rootPath + "/file/fileUpload",
                uploadExtraData:{businessId:businessId}
            };
            $("#workphotofile").fileinput(projectfileoptions).on("filebatchselected", function(event, files){

            }).on("fileuploaded", function(event, data) {
                $("#workPhotoSrc").removeAttr("src");
                $("#workPhotoSrc").attr("src",rootPath+"/sign/transportImg?signid="+businessId+"&t="+Math.random());
            });
        }
		//E_初始化上传附件控件
		
		//S_保存操作
		function createWP(vm){
			common.initJqValidation($("#work_program_form"));			
			var isValid = $("#work_program_form").valid();
			if(isValid){
				vm.iscommit = true;
				var httpOptions = {
						method : 'post',
						url : rootPath+"/workprogram/addWork",
						data : vm.work
					}
				var httpSuccess = function success(response) {	
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function() {		
							vm.iscommit = false;
							vm.work.id = response.data.id;
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
					onError: function(response){vm.iscommit = false;}
				});			
			}			
		}//E_保存操作
		
		//S_selectExpert
		function selectExpert(vm){
			if(vm.work.id && vm.work.id != ''){
				$state.go('expertReviewEdit', { workProgramId:vm.work.id});
			}else{
				common.alert({
					vm:vm,
					msg:"请先保存，再继续执行操作！"					
				})
			}
		}//E_selectExpert
		
	}		
})();