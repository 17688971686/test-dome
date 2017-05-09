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
                templateUrl: rootPath + '/admin/welcome.html',
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
            //begin#demo
            .state('demo', {
                url: '/demo',
                templateUrl: rootPath + '/demo/html/list.html',
                controller: 'demoCtrl',
                controllerAs: 'vm'
            })
            //end#demo
            //begin#mytest
            .state('mytest', {
                url: '/mytest',
                templateUrl: rootPath + '/mytest/html/list.html',
                controller: 'mytestCtrl',
                controllerAs: 'vm'
            }).state('mytestrEdit', {
            url: '/mytestEdit/:id',
            templateUrl: rootPath + '/mytest/html/edit.html',
            controller: 'mytestEditCtrl',
            controllerAs: 'vm'
        })
        //end#mytest
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
            .state('expert', {
                url: '/edit/:id',
                templateUrl: rootPath + '/expert/html/edit.html',
                controller: 'expertCtrl',
                controllerAs: 'vm'
            })
            .state('queryAll', {
                templateUrl: rootPath + '/expert/html/queryAllList.html',
                controller: 'expertCtrl',
                controllerAs: 'vm'
            })
            .state('queryRe', {
                //url: '/queryRe/:id',
                templateUrl: rootPath + '/expert/html/queryReList.html',
                controller: 'expertCtrl',
                controllerAs: 'vm'
            })
            .state('audit', {
                //url: '/edit/:id',
                templateUrl: rootPath + '/expert/html/audit.html',
                controller: 'expertCtrl',
                controllerAs: 'vm'
            })
            .state('expertEdit', {
                url: '/expertEdit/:expertID',
                templateUrl: rootPath + '/expert/html/edit.html',
                controller: 'expertEditCtrl',
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
        }).state('signDetails', {//详细信息
            url: '/signDetails/:signid',
            templateUrl: rootPath + '/sign/html/signDetails.html',
            controller: 'signDetailsCtrl',
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
            })
        //end#dispatch
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
            $state.go($rootScope.previousState_name, $rootScope.previousState_params);
        };

        $rootScope.topSelectChange = function (dictName, dicts) {
            for (var i = 0; i < dicts.length; i++) {
                if (dicts[i].dictName == dictName) {
                    return dicts[i].dicts;
                }
            }
        }

        common.initDictData({$http: $http, scope: $rootScope});
    });

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
                    total: function (data) { return data['count']; },
                    model: model
                };
            },
            transport: function (url,form) {
                return {
                    read: {
                        url: url,
                        dataType: "json",
                        type: "GET",
                        beforeSend: function (req) {                            
                            req.setRequestHeader('Token', service.getToken());
                        },
                        data : function(){
                        	if(form){
                        		var filterParam = common.buildOdataFilter(form);
                            	if(filterParam){
                            		return {"$filter":filterParam};
                            	}else{
                            		return {};
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
    	var t = new Array();
    	var arrIndex = 0;
    	$(from).find('input,radio,select,textarea').each(function(index,obj){
    		if(obj.name && obj.value){
    			var param = {};
    			if($(this).attr('operator')){
    				param.operator = $(this).attr('operator');
    			}else{
    				param.operator = 'eq';
    			}	
                param.name =  obj.name;
                param.value =  obj.value;
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
	    return filterStr;		
    }//E_封装filer的参数 
    
    function initDictData(options){
        options.$http({
       		method : 'get',
				url : rootPath+'/dict/dictItems'
        }).then(function(response){
         //save the metadata
       	 options.scope.dictMetaData = response.data;
       	 var dictsObj = {};
       	 reduceDict(dictsObj,response.data);
       	 options.scope.DICT = dictsObj;
        }, function (response) {         
        	alert('初始化数据字典失败');
        });
   }
    
    function reduceDict(dictsObj,dicts,keyName){
    	if(!dicts||dicts.length == 0){
    		return ;
    	}
    	if(!keyName){
    		//find the top dict
    		for(var i = 0;i<dicts.length;i++){
    			var dict = dicts[i];
    			if(!dict.parentId){   				
    				dictsObj[dict.dictCode] = {};
    				dictsObj[dict.dictCode].dictId = dict.dictId;
    				dictsObj[dict.dictCode].dictName = dict.dictName;   				
    				reduceDict(dictsObj[dict.dictCode],dicts,dict.dictId);
    			}
    		}
    	}else{
    		//find sub dicts  		
    		for(var i = 0;i<dicts.length;i++){
    			var dict = dicts[i];
    			if(dict.parentId&&dict.parentId == keyName){
    				if(!dictsObj.dicts){
    					dictsObj.dicts = new Array();
    				}
    				var subDict = {};
    				subDict.dictId = dict.dictId;
    				subDict.dictName = dict.dictName;
    				dictsObj.dicts.push(subDict);    				
    				//recruce
    				reduceDict(subDict,dicts,dict.dictId);
    			}
    		}
    	}
    }
  
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
			deletecompany:deletecompany			
		};		
		return service;	
		
		
		
		function grid(vm) {

			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_company),
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
						template : function(item) {
							return kendo
									.format(
											"<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
											item.id)
						},
						filterable : false,
						width : 40,
						title : "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
						
					},  {
						field : "coName",
						title : "单位名称",
						width : 200,						
						filterable : false
					},
					{
						field : "coPhone",
						title : "单位电话",
						width : 200,						
						filterable : false
					},
					{
						field : "coPC",
						title : "邮编",
						width : 200,						
						filterable : false
					},
					{
						field : "coAddress",
						title : "地址",
						width : 200,						
						filterable : false
					},
					{
						field : "coSite",
						title : "网站",
						width : 200,						
						filterable : false
					},
					{
						field : "coSynopsis",
						title : "单位简介",
						width : 200,						
						filterable : false
					},
					/*{
						field : "coDept",
						title : "直属部门",
						width : 200,						
						filterable : false
					},
					{
						field : "coDeptName",
						title : "直属部门名称",
						width : 200,						
						filterable : false
					},*/
					{
						field : "coFax",
						title : "传真",
						filterable : false
					}, 
					{
						field : "",
						title : "操作",
						width : 280,
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
					resizable: true
				};
			
		}// end fun grid

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

		

		function getcompanyById(vm) {
			var httpOptions = {
				method : 'get',
				url : common.format(url_company + "?$filter=id eq '{0}'", vm.id)
			}
			var httpSuccess = function success(response) {
				vm.model = response.data.value[0];
				if (vm.isUpdate) {
					//initZtreeClient(vm);
				}
			}
			
			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}// end fun getcompanyById
		
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

    angular
        .module('app')
        .controller('demoCtrl', demo);

    demo.$inject = ['$location','demoSvc']; 

    function demo($location, demoSvc) {
        /* jshint validthis:true */
    	var vm = this;
        vm.title = '';
        
        
        vm.showDialog=function(){
        	
        	 $('.myDialog').modal({
                 backdrop: 'static',
                 keyboard:false
             });
        }
        
        function datetimePicker(){
        	$("#datepicker").kendoDatePicker({
        		culture:'zh-CN'
        	});
        }
        
        function upload(){
        	 $("#files").kendoUpload({
                 async: {
                     saveUrl: "/demo/save",
                     removeUrl: "/demo/remove",
                     autoUpload: true
                 }
             });
        }
       
       
        activate();
        function activate() {
        	datetimePicker();
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
				url : url_dictgroup
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
				url : url_dictgroup
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
    'use strict';

    angular.module('app').controller('dispatchEditCtrl', dispatch);

    dispatch.$inject = ['$location','dispatchSvc','$state']; 

    function dispatch($location, dispatchSvc, $state) {     
        var vm = this;
        vm.title = '项目发文编辑';

        vm.dispatchDoc = {};
        vm.dispatchDoc.signId = $state.params.signid;
        
        dispatchSvc.initDispatchData();
        
        vm.create = function(){
        	dispatchSvc.saveDispatch(vm);
        }
    }
})();

(function() {
	'use strict';
	
	angular.module('app').factory('dispatchSvc', dispatch);
	
	dispatch.$inject = ['$rootScope','$http'];

	function dispatch($rootScope,$http) {
		var service = {
			initDispatchData : initDispatchData,		//初始化流程数据	
			saveDispatch : saveDispatch					//保存
		};
		return service;			
	
		//S_初始化
		function initDispatchData(){
			
		}//E_初始化
		
		//S_保存
		function saveDispatch(vm){
			common.initJqValidation($("#dispatch_form"));
			var isValid = $("#dispatch_form").valid();
			if (isValid) {
				vm.saveProcess = true;
				var httpOptions = {
						method : 'post',
						url : rootPath+"/dispatch",
						data : vm.dispatchDoc
					}
				var httpSuccess = function success(response) {									
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function() {		
							common.alert({
								vm:vm,
								msg:"操作成功,请继续处理流程！",
								fn:function() {
									$rootScope.back();	//返回到流程页面
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
		}//E_保存
		
	}
})();
(function () {
    'expert strict';

    angular
        .module('app')
        .controller('expertCtrl', expert);
    expert.$inject = ['$location','expertSvc']; 
    function expert($location, expertSvc) {
        /* jshint validthis:true */
    	var vm = this;
    	vm.data={};
        vm.titles = '专家列表';
        
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
        
        vm.forward=function(flag){
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
                expertSvc.updateAudit(vm,idStr,flag);
            }
        }
        vm.back=function(flag){
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
        		expertSvc.updateAudit(vm,idStr,5);
        	}
        }
        activate();
        function activate() {
        	//expertSvc.getDict(vm,"SEX,QUALIFICATIONS,DEGREE,JOB,TITLE,EXPERTTYPE,PROCOSTTYPE,PROTECHTYPE,EXPERTRANGE");
        	expertSvc.grid(vm,rootPath + "/expert");
        	//expertSvc.gridWork(vm,"");
        	expertSvc.gridAudit(vm,rootPath + "/expert?$filter=state eq '1'",1);
        	expertSvc.gridAudit(vm,rootPath + "/expert?$filter=state eq '2'",2);
        	expertSvc.gridAudit(vm,rootPath + "/expert?$filter=state eq '3'",3);
        	expertSvc.gridAudit(vm,rootPath + "/expert?$filter=state eq '4'",4);
        	expertSvc.gridAudit(vm,rootPath + "/expert?$filter=state eq '5'",5);
        	//vm.gridOptions1=vm.data;
        	//expertSvc.getAudit(vm,"state=3");
        	//expertSvc.getAudit(vm,"state=4");
        	//expertSvc.getAudit(vm,"expeRttype=1");
        	//expertSvc.getAudit(vm,"expeRttype=2");
        }
    }
})();

(function () {
    'expert strict';

    angular
        .module('app')
        .controller('expertEditCtrl', expert);

    expert.$inject = ['$location','projectExpeSvc','workExpeSvc','expertSvc','$state']; 

    function expert($location,projectExpeSvc,workExpeSvc,expertSvc,$state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '专家录入';
        vm.isuserExist=false;
        vm.isHide=true;
        vm.id = $state.params.expertID;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新专家';
            vm.isHide=false;
            vm.expertID=vm.id;
            expertSvc.getExpertById(vm);
            
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
        activate();
        function activate() {
        	 kendo.culture("zh-CN");
             $("#birthDay").kendoDatePicker({
             	 format: "yyyy-MM-dd",
             	 weekNumber: true
             });
             $("#createDate").kendoDatePicker({
            	 format: "yyyy-MM-dd",
            	 weekNumber: true
             });
             $("#beginTime").kendoDatePicker({
            	 format: "yyyy-MM-dd",
            	 weekNumber: true
             });
             $("#endTime").kendoDatePicker({
            	 format: "yyyy-MM-dd",
            	 weekNumber: true
             });
             $("#projectendTime").kendoDatePicker({
            	 format: "yyyy-MM-dd",
            	 weekNumber: true
             });
             $("#projectbeginTime").kendoDatePicker({
            	 format: "yyyy-MM-dd",
            	 weekNumber: true
             });
        	if (vm.isUpdate) {
        		//userSvc.getUserById(vm);
            } else {
            	//userSvc.initZtreeClient(vm);
            	//userSvc.getDict(vm);
            }
        	//userSvc.getOrg(vm);
        }
    }
})();

(function() {
	'expert strict';

	angular.module('app').factory('expertSvc', expert);

	expert.$inject = [ '$http'];
	
	//begin expert
	function expert($http) {
		var url_back = '#/expert';
		var url_expert = rootPath + "/expert";
		var service = {
			grid : grid,
			getExpertById : getExpertById,
			createExpert : createExpert,
			deleteExpert : deleteExpert,
			updateExpert : updateExpert,
			searchMuti : searchMuti,
			getDict : getDict,
			gridAudit:gridAudit,
			updateAudit:updateAudit,
			searchMAudit:searchMAudit
		};
		//end expert
		return service;
		//begin updateAudit
		function updateAudit(vm,ids,flag){
			//alert(ids);
			vm.isSubmit = true;
			var httpOptions = {
				method : 'post',
				url : url_expert+"/updateAudit",
				//data : ids
				params:{
					id:ids,
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
		//end updateAudit
		
		// begin#updateExpert
		function updateExpert(vm) {
			common.initJqValidation();
			var isValid = $('form').valid();
			//alert(isValid);
			//if (isValid) {
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

			//} else {
				// common.alert({
				// vm:vm,
				// msg:"您填写的信息不正确,请核对后提交!"
				// })
			//}

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
		
		// begin#searchMuti
		function searchMuti(vm) {
			//vm.isSubmit = true;
			var url=common.buildOdataFilter($("#form"));
			//alert(url_expert+"?$filter="+url);
			var httpOptions = {
					method : 'get',
					url :url_expert+"?$filter="+url
				}
			//alert(url);
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {						
						vm.gridOptions.dataSource.data([]);
						vm.gridOptions.dataSource.data(response.data.value);
						vm.gridOptions.dataSource.total(response.data.count);
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
		// end#searchMuti
		
		// begin#searchMAudit
		function searchMAudit(vm) {
			//vm.isSubmit = true;
			//alert(url_expert+"?$filter="+url+" and state eq '1'");
			var url=common.buildOdataFilter($("#form"));
			var httpOptions = {
					method : 'get',
					url :url_expert+"?$filter="+url+" and state eq '1'"
			}
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {						
						vm.gridOptions1.dataSource.data([]);
						vm.gridOptions1.dataSource.data(response.data.value);
						vm.gridOptions1.dataSource.total(response.data.count);
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
		// end#searchMAudit
		
		// begin#getDict
		function getDict(vm,dictCodes){
			//var param=new Array();
			//alert(dictCode);
			var httpOptions = {
					method : 'GET',
					url :rootPath +"/dict/dictMapItems",
					params:{
						dictCodes:dictCodes
					}
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.sex=response.data.SEX;
							vm.qualifiCations=response.data.QUALIFICATIONS;
							vm.degRee=response.data.DEGREE;
							vm.job=response.data.JOB;
							vm.title=response.data.TITLE;
							vm.expeRttype=response.data.EXPERTTYPE;
							vm.procoSttype=response.data.PROCOSTTYPE;
							vm.proteChtype=response.data.PROTECHTYPE;
							vm.expertrange=response.data.EXPERTRANGE;
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
		// end#getDict
		
		
		
		// begin#createUser
		
		function createExpert(vm) {	
			/*vm.work = new Array();
			
			var obj = {};
			obj.beginTime = "1234"
			obj.endTime = "1234"
			obj.companyName = "1234"
			obj.job = "1234"
			vm.work.push(obj);

			
			return ;*/
			common.initJqValidation();
			var isValid = $('form').valid();
				vm.model.birthDay=$('#birthDay').val();
				vm.model.createDate=$('#createDate').val();
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
							common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									//vm.isSubmit = false;
									vm.isUpdate=false;
									vm.isHide=false;
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
									vm.model.expertID=response.data.expertID;
									 // $("#add").html(common.format($('#wk').html(),response.data.expertID));
									 // $("#addPj").html(common.format($('#pj').html(),response.data.expertID));
									  //location.href ="#/expertEdit/#";
									
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
		// end#createUser
		
		
		
		// begin#getUserById
		function getExpertById(vm) {
			var httpOptions = {
				method : 'get',
				url : common.format(url_expert + "?$filter=expertID eq '{0}'", vm.id)
			}
			var httpSuccess = function success(response) {
				vm.model = response.data.value[0];
				vm.work=response.data.value[0].work;
				vm.project=response.data.value[0].project;
				console.log(vm.model);
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
		
		function gridData(url){
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url),
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
			return dataSource;
		}
		
		
		// begin#gridAudit
		function gridAudit(vm,url,flag){
			// Begin:dataSource
			var  dataSource=gridData(url);
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
			// End:dataSource
			//alert("dfdsf");
			// Begin:column
		   var columns=[
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
				    width: 100,
				    template: "<span class='row-number'></span>"  
				    },
					{
						field : "name",
						title : "姓名",
						width : 100,
						filterable : false
					},
					{
						field : "sex",
						title : "性别",
						width : 50,
						filterable : false
					},
					{
						field : "expeRttype",
						title : "区域类别",
						width : 100,
						filterable : false
					},
					{
						field : "comPany",
						title : "工作单位",
						width : 100,
						filterable : false
					},
					{
						field : "userPhone",
						title : "手机",
						width : 100,
						filterable : false
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
					},
					{
						field : "maJor",
						title : "现从事专业",
						width : 100,
						filterable : false
					},
					{
						field : "acaDemy",
						title : "毕业院校",
						width : 100,
						filterable : false
					},
					];	
			// End:column
		   if(flag==1){
		   			vm.gridOptions1 = {
		   				dataSource : common.gridDataSource(dataSource),
		   				filterable : common.kendoGridConfig().filterable,
		   				pageable : common.kendoGridConfig().pageable,
		   				noRecords : common.kendoGridConfig().noRecordMessage,
		   				columns : columns,
		   				dataBound:dataBound,
		   				resizable : true
		   			};
		   }else if(flag==2){
			   vm.gridOptions2 = {
		   				dataSource : common.gridDataSource(dataSource),
		   				filterable : common.kendoGridConfig().filterable,
		   				pageable : common.kendoGridConfig().pageable,
		   				noRecords : common.kendoGridConfig().noRecordMessage,
		   				columns : columns,
		   				dataBound:dataBound,
		   				resizable : true
		   			};
		   }else if(flag==3){
			   vm.gridOptions3 = {
		   				dataSource : common.gridDataSource(dataSource),
		   				filterable : common.kendoGridConfig().filterable,
		   				pageable : common.kendoGridConfig().pageable,
		   				noRecords : common.kendoGridConfig().noRecordMessage,
		   				columns : columns,
		   				dataBound:dataBound,
		   				resizable : true
		   			};
		   }else if(flag==4){
			   vm.gridOptions4 = {
		   				dataSource : common.gridDataSource(dataSource),
		   				filterable : common.kendoGridConfig().filterable,
		   				pageable : common.kendoGridConfig().pageable,
		   				noRecords : common.kendoGridConfig().noRecordMessage,
		   				columns : columns,
		   				dataBound:dataBound,
		   				resizable : true
		   			};
		   }else if(flag==5){
			   vm.gridOptions5 = {
		   				dataSource : common.gridDataSource(dataSource),
		   				filterable : common.kendoGridConfig().filterable,
		   				pageable : common.kendoGridConfig().pageable,
		   				noRecords : common.kendoGridConfig().noRecordMessage,
		   				columns : columns,
		   				dataBound:dataBound,
		   				resizable : true
		   			};
		   }
		   
			//alert("dfdf");
		
		}// end#grid
		
		// begin#showWin
		/*function showWin(vm){
			var WorkeWindow = $("#test");
            WorkeWindow.kendoWindow({
                width: "1000px",
                height: "500px",
                title: "添加工作经历",
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
		}*/
		
		// begin#grid
		function grid(vm,url) {
			// Begin:dataSource
			var  dataSource=gridData(url);
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
			
			// End:dataSource
			//alert("dfdsf");
			// Begin:column
			var columns = [
					{
					template : function(item) {
						return kendo
								.format(
										"<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
										item.expertID)
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
							return common.format($('#columnBtns').html(),
									"vm.del('" + item.expertID + "')", item.expertID);
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
			//alert("dfdf");
		}// end fun grid

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
				vm.model = response.data[0];
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
	                width: "1000px",
	                height: "500px",
	                title: "添加工作经历",
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
			common.initJqValidation();
			var isValid = $('form').valid();
				//vm.isSubmit = true;
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
									//vm.isSubmit = false;
									//vm.isUpdate=true;
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
})();
(function() {
	'workExpe strict';

	angular.module('app').factory('workExpeSvc', workExpe);

	workExpe.$inject = [ '$http'];

	function workExpe($http) {
		var service = {
				createWork:createWork,
				saveWork:saveWork,
				deleteWork:deleteWork,
				updateWork:updateWork,
				gotoWPage:gotoWPage,
				getWorkById:getWorkById,
				getWork:getWork
			 
			 
		};

		return service;
		//begin#getWork
		function getWork(vm){
			var httpOptions = {
					method : 'GET',
					url : rootPath + "/workExpe/getWork?$filter=expert.expertID eq '"+vm.model.expertID+"'"
				}
				var httpSuccess = function success(response) {
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.work=response.data;
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
		
		//begin#deleteWork
		function deleteWork(vm){
			common.initJqValidation();
			var isCheck=$("input[name='checkwr']:checked");
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
					url :  rootPath + "/workExpe/deleteWork",
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
		//end#delertWork
		
		//begin#updateWork
		function updateWork(vm){
			var isCheck=$("input[name='checkwr']:checked");
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
				    vm.weID=isCheck.val();
				    getWorkById(vm);
				    vm.expertID = vm.model.expertID;
				    gotoWPage(vm);
					
				
				
				
			}
		}
		
		//begin#getWorkById
		function getWorkById(vm){
			var httpOptions = {
					method : 'get',
					url : common.format( rootPath + "/workExpe/getWork?$filter=weID eq '{0}'", vm.weID)
				}
				var httpSuccess = function success(response) {
					vm.model = response.data[0];
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
		
		//清空页面数据
		//begin#cleanValue
		function cleanValue(){
			var tab=$("#wrwindow").find('input');
			 $.each(tab,function(i,obj){
				 obj.value="";
			 });
		}
		
		// begin#gotoWPage
		function gotoWPage(vm){
			var WorkeWindow = $("#wrwindow");
			//WorkeWindow.show();
            WorkeWindow.kendoWindow({
                width: "1000px",
                height: "500px",
                title: "添加工作经历",
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
		// end#gotoWPage
		
		
		//begin#getWork
		function getWork(vm){
			var httpOptions = {
					method : 'GET',
					url : rootPath + "/workExpe/getWork?$filter=expert.expertID eq '"+vm.model.expertID+"'"
				}
				var httpSuccess = function success(response) {
					console.log(response);
					common.requestSuccess({
						vm : vm,
						response : response,
						fn : function() {
							vm.work=response.data;
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
		
		//begin#saveWork
		function saveWork(vm){
			vm.isSubmit = true;
			vm.model.weID=vm.weID;
			vm.model.expertID = vm.expertID;
			vm.model.beginTime=$('#beginTime').val();
			vm.model.endTime=$('#endTime').val();

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
						 window.parent.$("#wrwindow").data("kendoWindow").close();
						 getWork(vm);
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
		//begin#createWork
		function createWork(vm){
			var isValid = $('form').valid();
				//vm.isSubmit = true;
				vm.model.beginTime=$('#beginTime').val();
				vm.model.endTime=$('#endTime').val();
				//console.log(vm.model.expertID);
				var httpOptions = {
					method : 'post',
					url : rootPath+"/workExpe/workExpe",
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
									//vm.isSubmit = false;
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
})();
(function() {
	'use strict';
	
	angular.module('app').factory('flowSvc', flow);
	
	flow.$inject = ['$http','$state'];

	function flow($http,$state) {
		var service = {
				initFlowData : initFlowData,		//初始化流程数据
				getNextStepInfo : getNextStepInfo,	//获取下一环节信息
				commit : commit,					//提交
				disableButton : disableButton,		//禁用按钮
				enableButton : enableButton			//启用按钮
		};
		return service;			
		
		//S_初始化流程数据
	    function initFlowData(vm){
	    	var processInstanceId = vm.flow.processInstanceId;
			
			vm.picture = rootPath+"/flow/proccessInstance/img/"+processInstanceId;		
			
			
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/flow/proccessInstance/history/"+processInstanceId),
				schema : common.kendoGridConfig().schema({
					id : "id"
				}),
				rowNumber: true,  
	            headerCenter: true,  
			});
						
			var columns = [	
				{
	                field: "",
	                title: "序号",
	                template: "<span class='row-number'></span>",
	                width:30
	            },
				{
					field : "activityName",
					title : "环节名称",
					width : 120,
					filterable : false
				},
				{
					field : "assignee",
					title : "处理人",
					width : 80,
					filterable : false
				},
				{
					field : "startTime",
					title : "开始时间",
					width : 120,
					filterable : false,
					template: "#=  (startTime == null)? '' : kendo.toString(new Date(startTime), 'yyyy-MM-dd hh:mm:ss') #"	
				},											
				{
					field : "",
					title : "结束时间",
					width : 120,
					filterable : false,
					template: function(item) {
						if(item.endTime){
							return kendo.toString(new Date(item.endTime), 'yyyy-MM-dd hh:mm:ss');
						}
						else{
							return " ";
						}
					}	
				},
				{
					field : "duration",
					title : "处理时长",
					width : 120,
					filterable : false
				},
				{
					field : "message",
					title : "处理信息",
					width : 300,
					filterable : false
				}
			];
			// End:column
			vm.historygrid = {
				dataSource : common.gridDataSource(dataSource),
				filterable : common.kendoGridConfig().filterable,
				pageable : common.kendoGridConfig().pageable,
				noRecords : common.kendoGridConfig().noRecordMessage,
				columns : columns,
				resizable : true,
				dataBound: function () {  
	                var rows = this.items();   
	                var pagesize = this.pager.pageSize();  
	                $(rows).each(function (i) {                    	
	                     $(this).find(".row-number").html(i+1);                
	                });  
	            } 
			};				
	    }//E_初始化流程数据
	    
	    //S_获取下一环节信息
	    function getNextStepInfo(vm){
	    	var httpOptions = {
					method : 'get',
					url : rootPath+"/flow/proccessInstance/nextNodeDeal",
					params : {proccessInstanceId:vm.flow.processInstanceId}						
				}

			var httpSuccess = function success(response) {					
				common.requestSuccess({
					vm:vm,
					response:response,
					fn:function() {			
						vm.flow.nextGroup = response.data.nextGroup;	
						vm.nextDealUserList = response.data.nextDealUserList;	
						if(response.data.nextDealUserList){
							vm.flow.nextDealUser = response.data.nextDealUserList[0].loginName;	//默认选中
						}	
						
						if(response.data.curNode){
							vm.flow.curNodeName = response.data.curNode.activitiName;
							vm.flow.curNodeAcivitiId = response.data.curNode.activitiId;
							/****************************以下为添加业务按钮部分**************************/
							if(response.data.curNode.activitiId == "approval"){	
								vm.hideWorkBt();								
							}
						}
						
						
						if(response.data.nextNode){
							vm.nextNode = response.data.nextNode;
							vm.flow.nextNodeAcivitiId = response.data.nextNode[0].activitiId;
						}
						vm.isOverStep = response.data.isEnd;
						vm.isHaveNext = response.data.isEnd == true?false:true;
					}
					
				})
			}

			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
	    }//E_获取下一环节信息
	    
	    //S_提交下一步
		function commit(vm){
			common.initJqValidation($("#flow_form"));			
			var isValid = $("#flow_form").valid();
			if(isValid){
				disableButton(vm);
				var httpOptions = {
						method : 'post',
						url : rootPath+"/flow/commit",
						data : vm.flow						
					}

				var httpSuccess = function success(response) {					
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function() {	
							if(response.data.reCode == "error"){
								enableButton(vm);
							}
							common.alert({
								vm:vm,
								msg: response.data.reMsg
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
		}//E_提交下一步
		
		//S_禁用按钮
		function disableButton(vm){
			vm.disabledButton = true;
		}//E_禁用按钮
		
		//S_启用按钮
		function enableButton(vm){
			vm.disabledButton = false;
		}//E_启用按钮
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
				transport : common.kendoGridConfig().transport(url_log),
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

    angular
        .module('app')
        .controller('meetingCtrl', meeting);

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
            	//meetingSvc.initZtreeClient(vm);
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
	//	var url_role = rootPath + "/role";
		var service = {
			grid : grid,
			getMeetingById : getMeetingById,
			initZtreeClient : initZtreeClient,
			createMeeting : createMeeting,
			deleteMeeting : deleteMeeting,
			updateMeeting : updateMeeting
		};

		return service;

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
					success : httpSuccess
				});

			}
		}

		// begin#initZtreeClient
		function initZtreeClient(vm) {
			
			var httpOptions = {
				method : 'get',
				url : url_role
			}
			var httpSuccess = function success(response) {

				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						var zTreeObj;
						var setting = {
							check : {
								chkboxType : {
									"Y" : "ps",
									"N" : "ps"
								},
								enable : true
							}
						};
						var zNodes = $linq(response.data.value).select(
								function(x) {
									return {
										id : x.id,
										name : x.roleName
									};
								}).toArray();
						var rootNode = {
							id : '',
							name : '角色集合',
							children : zNodes
						};
						zTreeObj = $.fn.zTree.init($("#zTree"), setting,
								rootNode);
						if (vm.isUpdate) {
							//updateZtree(vm);

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
		}

		// begin#getUserById
		function getMeetingById(vm) {
			var httpOptions = {
				method : 'get',
				url : common.format(url_meeting + "?$filter=id eq '{0}'", vm.id)
			}
			var httpSuccess = function success(response) {
				vm.model = response.data.value[0];
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
		// begin#grid
		function grid(vm) {
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_meeting),
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
						field : "num",
						title : "会议室编号",
						width : 200,
						filterable : true
					},
					{
						field : "mrName",
						title : "会议室名称",
						width : 200,
						filterable : true
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
						title : "会议室容量（人）",
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
						field : "createDate",
						title : "创建时间",
						filterable : false
					},
					{
						field : "mrType",
						title : "会议室类型",
						width : 180,
						filterable : false,
						

					},
					{
						field : "",
						title : "操作",
						width : 180,
						template : function(item) {
							return common.format($('#columnBtns').html(),
									"vm.del('" + item.id + "')", item.id);

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

		}// end fun grid

		
	}
})();
(function () {
    'use strict';

    angular.module('app').controller('mytestCtrl', mytest);

    mytest.$inject = ['$location', 'mytestSvc'];

    function mytest($location, mytestSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = 'My test';

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    mytestSvc.deleteMytest(vm, id);
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
            mytestSvc.grid(vm);
        }
    }
})();

(function () {
    'use strict';

    angular
        .module('app')
        .controller('mytestEditCtrl', mytest);

    mytest.$inject = ['$location', 'mytestSvc', '$state'];

    function mytest($location, mytestSvc, $state) {
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
            mytestSvc.createMytest(vm);
        };
        vm.update = function () {
            mytestSvc.updateMytest(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                mytestSvc.getMytestById(vm);
            }
        }
    }
})();

(function () {
    'use strict';

    angular.module('app').factory('mytestSvc', mytest);

    mytest.$inject = ['$http'];

    function mytest($http) {
        var url_user = rootPath + "/mytest", url_back = '#/mytest';
        var service = {
            grid: grid,
            getMytestById: getMytestById,
            createMytest: createMytest,
            deleteMytest: deleteMytest,
            updateMytest: updateMytest
        };

        return service;

        // begin#updateMytest
        function updateMytest(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

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

        // begin#deleteMytest
        function deleteMytest(vm, id) {
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

        // begin#createMytest
        function createMytest(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;

                var httpOptions = {
                    method: 'post',
                    url: url_user,
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

        // begin#getMytestById
        function getMytestById(vm) {
            var httpOptions = {
                method: 'get',
                url: common.format(url_user + "?$filter=id eq '{0}'", vm.id)
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
                transport: common.kendoGridConfig().transport(url_user),
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
                    title: "主键",
                    width: 100,
                    filterable: true
                },
                {
                    field: "testName",
                    title: "显示名",
                    width: 100,
                    filterable: true
                },
                {
                    field: "test01",
                    title: "test01",
                    width: 80,
                    filterable: false
                },
                {
                    field: "test02",
                    title: "test02",
                    width: 80,
                    filterable: false
                },
                {
                    field: "",
                    title: "创建时间",
                    width: 80,
                    filterable: false,
                    template: function(item) {
                        if(!item.createdDate){
                            return " ";
                        }
                        else{
                            return kendo.toString(new Date(item.createdDate), 'yyyy-MM-dd hh:mm:ss');
                        }
                    }
                },
                {
                    field: "",
                    title: "更新时间",
                    width: 80,
                    filterable: false,
                    template: function(item) {
                        if(!item.modifiedDate){
                            return " ";
                        }
                        else{
                            return kendo.toString(new Date(item.modifiedDate), 'yyyy-MM-dd hh:mm:ss');
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
        	if (vm.isUpdate) {
        		orgSvc.getOrgById(vm);
            }        	
        	//orgSvc.getCompany(vm);
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
		};		
		return service;	
		
		
		
		function grid(vm) {
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_org),
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
						template : function(item) {
							return kendo
									.format(
											"<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
											item.id)
						},
						filterable : false,
						width : 40,
						title : "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
						
					},  {
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
						field : "orgPhone",
						title : "电话",
						width : 130,						
						filterable : false
					},
					{
						field : "orgFax",
						title : "传真",
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
						field : "orgFunction",
						title : "职能",
						width :130,						
						filterable : false
					},
					{
						field : "orgDirectorName",
						title : "科长",
						width : 100,						
						filterable : false
					},
					{
						field : "orgAssistantName",
						title : "副科长",
						width : 100,						
						filterable : false
					},
					{
						field : "orgCompanyName",
						title : "单位名称",
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
				if(response.data.userDtos){
					vm.userDtos = {}
					vm.userDtos = response.data.userDtos;
					
				}
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
				vm.model.id=vm.id;// id
							              
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

			} else {
//				common.alert({
//				vm:vm,
//				msg:"您填写的信息不正确,请核对后提交!"
//			})
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
		var user_userNotIn='/org/{0}/userNotIn';
		var url_orgUsers="/org/{0}/users";
		
			
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
                url:common.format(url_orgUsers,vm.id),
                data:userId
                
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
                url:common.format(url_orgUsers,vm.id),
                data:userId
                
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
				transport : common.kendoGridConfig().transport(common.format(user_userNotIn,vm.id)),
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
				transport : common.kendoGridConfig().transport(common.format(url_orgUsers,vm.id)),
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

    angular
        .module('app')
        .controller('roleEditCtrl', role);

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
				transport : common.kendoGridConfig().transport(url_role),
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
						template : function(item) {
							return kendo
									.format(
											"<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
											item.id)
						},
						filterable : false,
						width : 40,
						title : "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
						
					}, {
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
						format : "{0:yyyy/MM/dd HH:mm:ss}"

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

		function getRoleById(vm) {
			var httpOptions = {
				method : 'get',
				url : common.format(url_role + "?$filter=id eq '{0}'", vm.id)
			}
			var httpSuccess = function success(response) {
				vm.model = response.data.value[0];
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
		}// end fun getRoleById
		
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
        
        //多条件查询
        vm.showClick = function(){
        	
        	roomCountSvc.showClick(vm);
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
            roomCountSvc.roomShow(vm);
           
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
		//alert(url_roomCount);
		var url_back = '#/roomCount';
		var url_user=rootPath +'/user';
			
		var service = {
			grid : grid,
			roomShow : roomShow,
			showClick : showClick,
			
		//	getroomCountById : getroomCountById,
			
		};		
		return service;	
		
		//会议预定查询下拉框使用
		function roomShow(vm){
			
			 $http.get(url_room+"/roomShow" 
			  ).success(function(data) {  
				  //console.log(data);
				  vm.room ={};
				  vm.room=data;
				
			  }).error(function(data) {  
			      //处理错误  
				  alert("查询会议室失败");
			  }); 
			
		}
		
		//多条件查询
		function conneFilter(vm){
			var arr=new Array();
			var filter="?";
			var value="";
			var url=url_room+filter+","+value;
			var i=0;
			if(vm.model.rbName!=null){
				var paramObj = {}
				paramObj.name = "rbName";
				paramObj.value = vm.model.rbName;
				
				arr[i++]=paramObj;
				
			}
			if(vm.model.mrID!=null){
				var paramObj = {}
				paramObj.name = "mrID";
				paramObj.value = vm.model.mrID;
				arr[i++]=paramObj;
			}
			if(vm.model.rbType!=null){
				var paramObj = {}
				paramObj.name = "rbType";
				paramObj.value = vm.model.rbType;
				arr[i++]=paramObj;
			}
		
			/*var rbDay=$("#beginTime").val();
			
			if(rbDay!=null){
				var paramObj = {}
				paramObj.name = "rbDay";
				paramObj.value = rbDay;
				alert(paramObj.value)
				arr[i++]=paramObj;
			}
			var end = $("#endTime").val();
			if(end!=null){
				var paramObj = {}
				paramObj.name ="rbDay";
				paramObj.value=end;
				arr[i++] =paramObj;
			}*/
			if(vm.model.dueToPeople!=null){
				var paramObj = {}
				paramObj.name = "dueToPeople";
				paramObj.value = vm.model.dueToPeople;
				arr[i++]=paramObj;
			}
			
			var param = "?$filter=";
			for(var k=0;k<arr.length;k++){
				if(k > 0){
					param += " and ";
				}
				param += arr[k].name +" eq " + "\'"+arr[k].value+"\'";
			}
			//url=url_expert+filter+value;
			return param;
		}
		function showClick(vm){
			var url=conneFilter(vm);
		//	alert(url);
			var httpOptions = {
					method : 'get',
					url : url_room+url
			};
			
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {						
						vm.gridOptions.dataSource.data([]);
						vm.gridOptions.dataSource.data(response.data.value);
						vm.gridOptions.dataSource.total(response.data.count);
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
		
		function grid(vm) {
			//时间控件start
			$(document).ready(function () {
			  	 kendo.culture("zh-CN");
			      $("#beginTime").kendoDatePicker({
			      	 format: "yyyy-MM-dd",
			      
			      
			      });
			  }); 
			$(document).ready(function () {
			 	 kendo.culture("zh-CN");
			     $("#endTime").kendoDatePicker({
			     	 format: "yyyy-MM-dd",
			     
			     
			     });
			 }); 
			//时间控件end
			

			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_room),
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
					    field: "rowNumber",  
					    title: "序号",  
					    width: 80,
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
						field : "beginTime",
						title : "会议开始时间",
						width : 160,						
						filterable : false
					},
					{
						field : "endTime",
						title : "会议结束时间",
						width : 160,						
						filterable : false
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
					
					
					/*{
						field : "",
						title : "操作",
						width : 100,
						template:function(item){							
							return common.format($('#columnBtns').html(),"vm.del('"+item.id+"')",item.id);
							
						}
						

					}*/

			];
			// End:column
		
			vm.gridOptions={
					dataSource : common.gridDataSource(dataSource),
					filterable : common.kendoGridConfig().filterable,
					pageable : common.kendoGridConfig().pageable,
					noRecords:common.kendoGridConfig().noRecordMessage,
					columns : columns,
					//dataBounds :dataBounds,
					resizable: true
				};
		
			
		}// end fun grid

		


	}
	
	
})();
(function () {
    'use strict';

    angular
        .module('app')
        .controller('roomCtrl', room);

    room.$inject = ['$location','roomSvc','$scope']; 

    function room($location, roomSvc,$scope) {
        /* jshint validthis:true */
    	var vm = this;
        vm.title = '会议室预定列表';
        
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
        	
        	//调用room.svc.js的初始化方法
           roomSvc.initRoom(vm);
           roomSvc.showMeeting(vm);
           //roomSvc.findUser(vm);
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
        
        vm.create = function () {
        	
        	roomSvc.createroom(vm);
        };
        vm.update = function () {
        	roomSvc.updateroom(vm);
        };      

     // begin#createUser
		function createroom(vm) {
			alert("sss");
			common.initJqValidation();
			var isValid = $('form').valid();
			if (isValid) {
				vm.isSubmit = true;

				var httpOptions = {
					method : 'post',
					url : url_room,
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
					success : httpSuccess
				});

			}
		}
		//end
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
			createRoom : createRoom,
			updateRoom : updateRoom,
			//deleteRoom : deleteRoom,
			showMeeting : showMeeting,
			findMeeting : findMeeting,
			exportWeek : exportWeek,
			exportThisWeek : exportThisWeek,
			exportNextWeek : exportNextWeek,
			stageNextWeek : stageNextWeek,
			//findUser : findUser,
		};

		return service;
		
		//start0
		function initRoom(vm){
			
			//start1
			var dataSource = new kendo.data.SchedulerDataSource({
	          	 batch: true,
				sync: function() {
					  this.read();
					}, 
	           transport: {
				  read:function(options){
					 //console.log(options);
					  var mrID = options.data.mrID;
					  var url =  rootPath + "/room" ;
					  if(mrID){
						  url += "?"+mrID;
					  }
					  //alert(url);
					  $http.get(url 
					  ).success(function(data) {  
						  //console.log(data);
						//  console.log(data.value);
						  options.success(data.value);
						 // console.log(vm.success);
					  }).error(function(data) {  
					     
						  alert("查询失败");
					  });  
				  },
				
				  create:function(vm){
					  createRoom(vm);
						
				  },
				  update:function(vm){
					
					  updateRoom(vm);
				  },
				  destroy:function(vm){
					//console.log(vm);
						deleteRoom(vm);
				  },

				  parameterMap: function(options, operation) {
					//  console.log(options);
	                if (operation !== "read" && options.models) {
	                  return {models: kendo.stringify(options.models)};
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
	                    taskId: {
	                        from: "id"
	                        //type: ""
	                    },
	                    title: { from: "addressName", defaultValue: "addressName" },
	                    start: { type: "date", from: "beginTime" },
	                    end: { type: "date", from: "endTime" },
						host:{ type: "string", from: "host"},
						}
				  }
	            },

	          });
		
			
			vm.schedulerOptions = {
			            date: new Date(),
			            startTime: new Date(),
			            height: 600,
			            views: [
			                "day",
			                "workWeek",
			                { type: "week", selected: true },
			                
			                "month",
			                "agenda",
			            ],
			           //statr 时间
			            editable: {
			                template: $("#customEditorTemplate").html(),

			              },
			            eventTemplate: $("#event-template").html(),
			            edit: function(e) {

			            },
			            //end
			            timezone: "Etc/UTC",
			            dataSource :dataSource,
			            
			 
			          /*  resources: [
			                {
			                    field: "ownerId",
			                    title: "Owner",
			                    dataSource: [
			                        { text: "Alex", value: 1, color: "#f8a398" },
			                        { text: "Bob", value: 2, color: "#51a0ed" },
			                        { text: "Charlie", value: 3, color: "#56ca85" }
			                    ]
			                }
			            ]*/
			          
			            
			        };
			//end1
			
			 
		}
		//end0
		
		//start#showMeeting
		function showMeeting(vm){
		
			 $http.get(url_room+"/meeting" 
			  ).success(function(data) {  
				  //console.log(data);
				  vm.meeting ={};
				  vm.meeting=data;
				
			  }).error(function(data) {  
			      //处理错误  
				  alert("查询会议室失败");
			  }); 
		}
		
		//查询会议室
		function findMeeting(vm){
			

			vm.schedulerOptions.dataSource.read({"mrID":common.format("$filter=mrID eq '{0}'", vm.mrID)});
		}
		//start#showMeeting
		
		//start#sava
		function createRoom(vm) {
			var model = vm.data.models[0];
			var rb = {};
			rb.rbName=model.rbName;
			
			//rb.beginTime = kendo.toString(model.beginTime, "yyyy-MM-dd HH:mm");
			rb.rbDay=$("#rbDay").val();
			rb.beginTime = $("#beginTime").val();
			rb.endTime = $("#endTime").val();
			//alert(rb.rbDay);
			//alert(rb.beginTime);
			//alert(rb.endTime);
			rb.host = model.host;
			rb.mrID = model.mrID;
			rb.rbType=model.rbType;
			rb.dueToPeople=model.dueToPeople;
			rb.content=model.content;
			rb.remark=model.remark;
			
			common.initJqValidation();
			var isValid = $('form').valid();
			alert(isValid);
			if (isValid) {
				vm.isSubmit = true;
				var httpOptions = {
					method : 'post',
					url : url_room,
					data : rb
				}
				var httpSuccess = function success(response) {
					 vm.isSubmit = false;   
					//console.log(response.data.message);
					common.requestSuccess({
                    	vm:vm,
                    	response:response,
                    	fn:function () {
                            
                            var isSuccess = response.data.isSuccess;
                        //  console.log(isSuccess);
                            if (isSuccess) {
                                vm.message = "";
                               
                                //common.cookie().set("data", "token", response.data.Token, "", "/");
                              //  location.href = "${path}/admin/index.html";
                            } else {
                                
                                vm.message=response.data.message
                              //  console.log(vm.message);
                            }
                    	}
                    });
					 alert("添加成功");

				}

				common.http({
					vm : vm,
					$http : $http,
					httpOptions : httpOptions,
					success : httpSuccess
				});

			}
		}
		//end#save
		
		
		
		//start#update
		function updateRoom(vm){
			var model = vm.data.models[0];
			var rb = {};
			rb.rbName=model.rbName;
			rb.id=model.id;
			//rb.beginTime = kendo.toString(model.beginTime, "yyyy-MM-dd HH:mm");
			rb.rbDay=$("#rbDay").val();
			rb.beginTime = $("#beginTime").val();
			rb.endTime = $("#endTime").val();
			//alert(rb.rbDay);
			//alert(rb.beginTime);
			//alert(rb.endTime);
			rb.host = model.host;
			rb.mrID = model.mrID;
			rb.rbType=model.rbType;
			rb.dueToPeople=model.dueToPeople;
			rb.content=model.content;
			rb.remark=model.remark;
			
			common.initJqValidation();
			var isValid = $('form').valid();
			if (isValid) {
				vm.isSubmit = true;
				var httpOptions = {
					method : 'put',
					url : url_room,
					data : rb
				}
				var httpSuccess = function success(response) {

					common.requestSuccess({
						vm : vm,
						response : response,
						
						fn : function() {

							/*common.alert({
								vm : vm,
								msg : "操作成功",
								fn : function() {
									vm.isSubmit = false;
									$('.alertDialog').modal('hide');
									//$('.modal-backdrop').remove();
									location.href = url_back;
								}
							})*/
							var msg ="编辑成功";
							alert(msg);
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
		//end#update
		
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
//					fn : function() {
//						vm.isSubmit = false;
//						vm.dataSource.dataSource.read();
//					}

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
		
		
		//start#time
		//校验结束时间不能小于开始时间
		function startEnd(){
			
			var start = $("#beginTime").val();
			var end = $("#endTime").val();
			if(end<start){
				alert("结束时间不能小于开始时间");
			}
		}
		//endTime#time
		
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

    sign.$inject = ['$location','signSvc','$state']; 

    function sign($location,signSvc,$state) {        
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
                    	signSvc.stopFlow(vm,signid);
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
                    	signSvc.restartFlow(vm,signid);
                 }
              })
         }//end 重启流程
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
        
        //申报登记编辑
        vm.updateFillin = function (){   	   
    	   signSvc.updateFillin(vm);  	   
        }
             
       //申报登记编辑
       vm.updateFillin = function (){
    	   signSvc.updateFillin(vm);
       }
       
       //根据部门查询用户
       vm.findUsersByOrgId = function(type){
    	   signSvc.findUsersByOrgId(vm,type);
       }

    }
})();

(function () {
    'use strict';

    angular.module('app').controller('signFlowCtrl', sign);

    sign.$inject = ['$location','signSvc','$state']; 

    function sign($location,signSvc,$state) {        
        var vm = this;
        vm.title = "项目待处理";
        signSvc.flowgrid(vm);        
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('signFlowDealCtrl', sign);

    sign.$inject = ['$location','signSvc','$state','flowSvc']; 

    function sign($location,signSvc,$state,flowSvc) {        
        var vm = this;
        vm.title = "项目流程处理";       
        vm.model = {};
        vm.flow = {};
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
        	vm.flowDeal = true;
        	flowSvc.initFlowData(vm);
        	flowSvc.getNextStepInfo(vm);
        	
        	//再初始化业务信息
        	signSvc.initFlowPageData(vm);
        }
        
        vm.commitNextStep = function (){
        	flowSvc.commit(vm);
        }
        
        vm.commitBack = function(){
        	alert("流程回退！");
        }
        
        vm.commitOver = function(){
        	alert("流程结束！");
        }  
        
        //S_隐藏工作方案按钮判断
        vm.hideWorkBt = function(){
        	if(vm.flow.curNodeAcivitiId == "approval"){
        		if(vm.model.isreviewcompleted > 0 ){
            		vm.showWorkBt = false;
            		return true;
            	}else{
            		vm.showWorkBt = true;
            		return false;
            	}
    		}else{
    			vm.showWorkBt = false;
        		return true;
    		}
        }//E_隐藏工作方案按钮判断
       
        //S_跳转到 工作方案 编辑页面
        vm.addWorkProgram = function(){
        	$state.go('workprogramEdit', {signid:vm.model.signid});
        }//E_跳转到 工作方案 编辑页面
        
        //E_隐藏收文按钮判断
        vm.hideDisPatchBt = function(){
        	var hidden = true;
        	if(vm.flow.curNodeAcivitiId == "dispatch"){
        		if(vm.model.isDispatchCompleted > 0 ){
        			hidden = true;
            	}else{
            		hidden = false;
            	}
    		}        	
        	return hidden;
        }//E_隐藏收文按钮判断
        
        //S_跳转到 发文 编辑页面
        vm.addDisPatch = function(){
        	$state.go('dispatchEdit', {signid:vm.model.signid});
        }//E_跳转到 发文 编辑页面
    }
})();

(function () {
    'use strict';

    angular.module('app').controller('signDetailsCtrl', sign);

    sign.$inject = ['$location','signSvc','$state']; 

    function sign($location, signSvc,$state) {        
        var vm = this;
    	vm.model = {};							//创建一个form对象   	
        vm.title = '查看详情信息';        		//标题
        vm.model.signid = $state.params.signid;	//收文ID
              
        signSvc.initDetailData(vm);     	                        
    }
})();

(function() {
	'use strict';
	
	angular.module('app').factory('signSvc', sign);
	
	sign.$inject = ['$http','$state'];

	function sign($http,$state) {
		var service = {
			grid : grid,						//初始化项目列表
			querySign : querySign,				//查询
			createSign : createSign,			//新增
			initFillData : initFillData,		//初始化表单填写页面（可编辑）
			initDetailData : initDetailData,	//初始化详情页（不可编辑）
			updateFillin : updateFillin,		//申报编辑
			deleteSign :　deleteSign,			//删除收文
			
			flowgrid : flowgrid,				//初始化待处理页面			
			startFlow : startFlow,				//发起流程
			stopFlow : stopFlow,				//停止流程
			restartFlow : restartFlow,			//重启流程
			findUsersByOrgId : findUsersByOrgId,//根据部门ID选择用户
			initFlowPageData : initFlowPageData //初始化流程收文信息
		};
		return service;			
		
		//S_初始化grid
		function grid(vm){
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata', 
				transport :common.kendoGridConfig().transport(rootPath+"/sign",$("#searchform")),
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
				pageSize : 3,
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
						title : "项目编号",
						width : 160,
						filterable : false,
					},
					{
						field : "maindeptName",
						title : "主办事处名称",
						width : 100,
						filterable : false,
					},
					{
						field : "mainDeptUserName",
						title : "主办事处联系人",
						width : 100,
						filterable : false,
					},
					{
						field : "createdDate",
						title : "创建时间",
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
									return "进行中"
								}else if(item.folwState == 2){
									return "已暂停"
								}else if(item.folwState == 8){
									return "强制结束"
								}else if(item.folwState == 9){
									return "已完成"
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
							var isFlowStart = false,hideStopButton = true,hideRestartButton=true;
							if(item.folwState && item.folwState > 0){
								isFlowStart = true;
								if(item.folwState == 1){
									hideStopButton = false;
								}
								if(item.folwState == 2){
									hideRestartButton = false;
								}								
							}						
							return common.format($('#columnBtns').html(), item.signid, 
									item.signid,isFlowStart,"vm.del('" + item.signid + "')",isFlowStart,
									"vm.startFlow('" + item.signid + "')", isFlowStart,
									"vm.stopFlow('" + item.signid + "')", hideStopButton,
									"vm.restartFlow('" + item.signid + "')", hideRestartButton);
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
									fn:function() {
										$state.go('fillSign', {signid: response.data});
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
		
		//S_根据部门ID选择用户
		function findUsersByOrgId(vm,type){
			var param = {};
			if("main" == type){
				param.orgId = vm.model.maindepetid;
			}else{
				param.orgId = vm.model.assistdeptid;
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
						}else{
							vm.assistUserList = {};
							vm.assistUserList = response.data;
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
		
		//Start 申报登记编辑
		function updateFillin(vm){
	
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
						vm.orglist =response.data.orgList	
						if(response.data.mainUserList){
							vm.mainUserList = response.data.mainUserList;
						}
						if(response.data.assistUserList){
							vm.assistUserList = response.data.assistUserList;
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
		}//E_初始化填报页面数据				
		
		//S_初始化详情数据	
		function initDetailData(vm){
			var httpOptions = {
					method : 'get',
					url : rootPath+"/sign/html/initDetailPageData",
					params : {signid : vm.model.signid}						
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
		
		
		//S_初始化待处理页面
		function flowgrid(vm){
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(rootPath+"/sign/html/initflow"),
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
						
			var columns = [
				 {
                     field: "",
                     title: "序号",
                     template: "<span class='row-number'></span>",
                     width:30
                 },
				{
					field : "projectname",
					title : "项目名称",
					width : 200,
					filterable : true
				},
				{
					field : "projectcode",
					title : "项目编号",
					width : 200,
					filterable : true
				},
				{
					field : "createdDate",
					title : "创建时间",
					width : 180,
					filterable : false,
					format : "{0:yyyy/MM/dd HH:mm:ss}"

				},
				{
					field : "",
					title : "操作",
					width : 180,
					template:function(item){
						//如果项目已暂停，则停止对流程操作
						var hideDealButton = false;
						if(item.folwState && item.folwState == 2){
							hideDealButton = true;
						}
						return common.format($('#columnBtns').html(),item.signid,item.taskId,item.processInstanceId,hideDealButton);
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
		}//E_初始化待处理页面
		
		//S_发起流程
		function startFlow(vm,signid){
			var httpOptions = {
					method : 'post',
					url : rootPath+"/sign/html/startFlow",
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
		}//E_发起流程
		
		//S_停止流程
		function stopFlow(vm,signid){
			var httpOptions = {
					method : 'post',
					url : rootPath+"/sign/html/stopFlow",
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
		}//E_停止流程
		
		//S_重启流程
		function restartFlow(vm,signid){
			var httpOptions = {
					method : 'post',
					url : rootPath+"/sign/html/restartFlow",
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
		}//E_重启流程
		
		//S_初始化流程页面
		function initFlowPageData(vm){
			var httpOptions = {
					method : 'get',
					url : rootPath+"/sign/html/initFlowPageData",
					params : {signid:vm.model.signid,taskId:vm.flow.taskId}
				}
				
			var httpSuccess = function success(response) {
				common.requestSuccess({
					vm : vm,
					response : response,
					fn : function() {
						vm.model = response.data;
						vm.hideWorkBt();	//控制工作方案按钮显示和隐藏																	
						if(vm.model.isreviewcompleted > 0){
							vm.work = response.data.workProgramDto;	//显示工作方案tab		
						}
						
						vm.hideDisPatchBt(); 	//控制发文按钮显示和隐藏	
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
                userSvc.getDict(vm);
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
        var url_user = rootPath + "/user", url_back = '#/user', url_role = rootPath + "/role",
            url_dictgroup = rootPath + "/dict";
        var service = {
            grid: grid,
            getUserById: getUserById,
            initZtreeClient: initZtreeClient,
            createUser: createUser,
            deleteUser: deleteUser,
            updateUser: updateUser,
            getOrg: getOrg,
            getDict: getDict
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

        //获取字典信息
        function getDict(vm) {
            var httpOptions = {
                method: 'get',
                url: url_dictgroup
            };

            var httpSuccess = function success(response) {
                //vm.dict ={};
                vm.sex = response.data;
                console.log(vm.sex);
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
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

                console.log(vm.org);

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
                method: 'get',
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
                method: 'get',
                url: common.format(url_user + "?$filter=id eq '{0}'", vm.id)
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
                transport: common.kendoGridConfig().transport(url_user),
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
                    field: "loginName",
                    title: "登录名",
                    width: 100,
                    filterable: true
                },
                {
                    field: "displayName",
                    title: "显示名",
                    width: 100,
                    filterable: true
                },
                {
                    field: "userSex",
                    title: "性别",
                    width: 80,
                    filterable: false
                },
                {
                    field: "userPhone",
                    title: "联系电话",
                    width: 80,
                    filterable: false
                },
                {
                    field: "userMPhone",
                    title: "联系手机",
                    width: 80,
                    filterable: false
                },
                {
                    field: "email",
                    title: "电子邮件",
                    width: 80,
                    filterable: false
                },
                {
                    field: "orgDto.name",
                    title: "所属部门",
                    width: 100,
                    filterable: false
                },
                {
                    field: "jobState",
                    title: "在职情况",
                    width: 80,
                    filterable: false
                },
                {
                    field: "useState",
                    title: "是否停用",
                    width: 80,
                    filterable: false
                },
                {
                    field: "pwdState",
                    title: "更改密码",
                    width: 80,
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
                    field: "createdDate",
                    title: "创建时间",
                    width: 160,
                    filterable: false,
                    format: "{0:yyyy/MM/dd HH:mm:ss}"

                },
                {
                    field: "remark",
                    title: "描述",
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
                columns: columns,
                resizable: true
            };

        }// end fun grid

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
         
        vm.work.signId = $state.params.signid;		//这个是收文ID
        alert(vm.work.signId);
        
        workprogramSvc.initPage(vm);
       
        vm.create = function () {
        	workprogramSvc.createWP(vm);
        };       
    }
})();

(function() {
	'use strict';
	
	angular.module('app').factory('workprogramSvc', workprogram);
	
	workprogram.$inject = ['$rootScope','$http','$state'];

	function workprogram($rootScope,$http,$state) {
		var service = {
			initPage : initPage,		//初始化页面参数
			createWP : createWP			//新增操作
		};
		return service;			
		
		//S_初始化页面参数
		function initPage(vm){
			
		}//S_初始化页面参数	
		
		
		//S_保存操作
		function createWP(vm){
			common.initJqValidation($("#work_program_form"));
			var isValid = $("#work_program_form").valid();
			if (isValid) {
				vm.commitProcess = true;
				var httpOptions = {
						method : 'post',
						url : rootPath+"/workprogram",
						data : vm.work
					}
				var httpSuccess = function success(response) {									
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function() {		
							common.alert({
								vm:vm,
								msg:"操作成功,请继续处理流程！",
								fn:function() {
									$rootScope.back();	//返回到流程页面
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
		}//E_保存操作
	}		
})();