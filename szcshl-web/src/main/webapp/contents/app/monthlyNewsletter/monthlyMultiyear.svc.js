(function () {
    'use strict';

    angular.module('app').factory('monthlyMultiyearSvc', monthlyMultiyear);

    monthlyMultiyear.$inject = ['$http','bsWin'];

    function monthlyMultiyear($http,bsWin) {
        var url_monthlyMultiyear = rootPath + "/monthlyNewsletter", url_back = '#/monthlyNewsletterList';
        var url_user = rootPath + "/user";
        var service = {
        	createmonthlyMultiyear: createmonthlyMultiyear,//添加中心文件稿纸
        	initMonthlyMultiyear:initMonthlyMultiyear,     //初始化中心文件稿纸
            monthlyMultiyearGrid: monthlyMultiyearGrid,    //年度（中心）文件查询列表
            monthlyAppoveGrid: monthlyAppoveGrid,          //年度（中心）文件审批列表
            monthlyYearGrid:monthlyYearGrid,  			   //年度月报简报列表
            getmonthlyMultiyearById: getmonthlyMultiyearById,//根据ID查找中心文件稿纸
            updatemonthlyMultiyear: updatemonthlyMultiyear,	//更新中心文件稿纸
            findByBusinessId:findByBusinessId,				//根据业务ID获取附件列表
            updateApprove:updateApprove,					//领导审批中心文件
            addSuppQuery:addSuppQuery,						//查询
            findAllOrg:findAllOrg,							//查询部门
            findAllUser:findAllUser,  						//查询用户
            deletemonthlyMultiyear: deletemonthlyMultiyear,//删除年度（中心）月报简报记录
        };

        return service;

        //S_查询用户
		function findAllUser(vm){
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
		//E_查询用户
		
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
		
       //查询
      function addSuppQuery(vm){
    	  vm.monthlyYearGrid.dataSource.read();
      }
        //S 领导审批中心文件处理
       function  updateApprove(vm){
    	   var httpOptions = {
                   method: 'post',
                   url: url_monthlyMultiyear + "/updateApprove",
                  data:vm.suppletter
               };
               var httpSuccess = function success(response) {
            	   bsWin.success("操作成功！")
               };
               common.http({
                   vm: vm,
                   $http: $http,
                   httpOptions: httpOptions,
                   success: httpSuccess
               });       
        }
       //E 领导审批中心文件处理
       
        //S 根据主业务获取所有的附件信息
        function findByBusinessId(vm){
        	var httpOptions = {
                    method: 'post',
                    url: rootPath + "/file/findByBusinessId",
                    params: {
                        businessId: vm.suppletter.id
                    }
                };
                var httpSuccess = function success(response) {
                	vm.sysFilelists =response.data;
                };
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });            
        }
       //E 根据主业务获取所有的附件信息
        
        //S 初始化中心文件稿纸
        function initMonthlyMultiyear(vm){
        	 vm.isSubmit = true;
             var httpOptions = {
                 method: 'post',
                 url: url_monthlyMultiyear+"/initMonthlyMultiyear",
             };

             var httpSuccess = function success(response) {
            	 vm.suppletter = response.data;
            	//初始化附件上传
                 vm.initFileUpload();
             };
             common.http({
                 vm: vm,
                 $http: $http,
                 httpOptions: httpOptions,
                 success: httpSuccess
             });
        }
        //E 初始化中心文件稿纸
        
        // begin#updatemonthlyMultiyear
        function updatemonthlyMultiyear(suppletter,callBack) {
        	   var httpOptions = {
                       method: 'post',
                       url: url_monthlyMultiyear+"/saveMonthlyMultiyear",
                       data: suppletter
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

        // begin#删除年度(中心)月报简报记录
        function deletemonthlyMultiyear(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_monthlyMultiyear+"/deleteMutiyear",
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

        // begin#添加中心文件稿纸
        function createmonthlyMultiyear(suppletter,callBack) {
                var httpOptions = {
                    method: 'post',
                    url: url_monthlyMultiyear+"/saveMonthlyMultiyear",
                    data: suppletter
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
      //end#添加月报简报历史数据

        // begin#getmonthlyMultiyearById
        function getmonthlyMultiyearById(vm) {
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/addSuppLetter/findById",
                params:{id:vm.id}
            };
            var httpSuccess = function success(response) {
                vm.suppletter = response.data;
            };

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });                       
        }
        
        //S 年度月报简报列表
        function monthlyYearGrid(vm){
        	// Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_monthlyMultiyear+"/monthlyMultiyearList",$("#form_monthly") ),
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
                    field: "title",
                    title: "文件标题",
                    width: 180,
                    filterable : false,
                    template: function (item) {
                    	return '<a href="#/monthlyMultiyearEdit/'+item.id+'" >'+item.title+'</a>';
                    }
                },
                {
                    field: "orgName",
                    title: "拟办部门",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "userName",
                    title: "拟稿人",
                    width: 120,
                    filterable: false
                },
                
                {
                    field: "suppLetterTime",
                    title: "拟稿时间",
                    width: 100,
                    filterable: false
                },
                {
                    field: "secretLevel",
                    title: "秘密等级",
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
            vm.monthlyYearGrid = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                dataBound:dataBound,
                columns: columns,
                resizable: true
            };
        }
        //E 年度月报简报列表
        
        
       //S 年度（中心）文件审批列表
        function monthlyAppoveGrid(vm){
        	// Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_monthlyMultiyear+"/monthlyAppoveList"),
                schema: {
                    data: "value",
                    total: function (data) {
                        if (data['count']) {
                            $("#MONTHLY_COUNT").html(data['count']);
                        } else {
                            $("#MONTHLY_COUNT").html(0);
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
                    field: "title",
                    title: "文件标题",
                    width: 180,
                    filterable : false,
                    template: function (item) {
                    	return '<a href="#/monthlyMultiyearEdit/'+item.id+'" >'+item.title+'</a>';
                    }
                },
                {
                    field: "orgName",
                    title: "拟办部门",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "userName",
                    title: "拟稿人",
                    width: 120,
                    filterable: false
                },
                
                {
                    field: "suppLetterTime",
                    title: "拟稿时间",
                    width: 100,
                    filterable: false
                },
                {
                    field: "secretLevel",
                    title: "秘密等级",
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

            vm.monthlyAppoveGrid = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                dataBound:dataBound,
                columns: columns,
                resizable: true
            };
        }
      //E 年度（中心）文件审批列表

        // begin#中心文件查询列表
        function monthlyMultiyearGrid(vm) {
        	//alert(vm.monthly.id);
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_monthlyMultiyear+"/monthlyMultiyearList",$("#form")),
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
                    field: "title",
                    title: "文件标题",
                    width: 180,
                    filterable : false,
                    template: function (item) {
                    	return '<a href="#/monthlyMultiyearEdit/'+item.id+'" >'+item.title+'</a>';
                    }
                },
                {
                    field: "orgName",
                    title: "拟办部门",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "userName",
                    title: "拟稿人",
                    width: 120,
                    filterable: false
                },
                
                {
                    field: "suppLetterTime",
                    title: "拟稿时间",
                    width: 100,
                    filterable: false
                },
                {
                    field: "secretLevel",
                    title: "秘密等级",
                    width: 100,
                    filterable: false
                },
               
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                             item.id);
                    }
                }
            ];
            // End:column

            vm.multiyearGrid = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                dataBound:dataBound,
                columns: columns,
                resizable: true
            };

        }// end#中心文件查询列表
        
    }
})();