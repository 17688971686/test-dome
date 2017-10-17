(function () {
    'use strict';

    angular.module('app').factory('assistCostCountSvc', assistCostCount);

    assistCostCount.$inject = ['$http'];

    function assistCostCount($http) {
        var url_assistCostCount = rootPath + "/financialManager", url_back = '#/assistCostCountList';
        var service = {
            grid: grid,
            deleteassistCostCount: deleteassistCostCount,			//删除协审费用记录
            saveAssistCost:saveAssistCost,							//协审费用保存
            sumAssistCount:sumAssistCount,								//统计评审费用总和
            initAssistlProject:initAssistlProject,					//初始化协审费用关联的项目
            isUnsignedInteger:isUnsignedInteger,					//	数字校验
            assistExportExcel:assistExportExcel,					//专家协申费用导出
            assistCostCountList:assistCostCountList,				//协审费用统计列表
            assistCostList :assistCostList,							//协审费录入列表
           
        };
        
        return service;
        
      //S 协审费录入列表
       function assistCostList(vm,callBack){
    	   var httpOptions = {
                   method: 'post',
                   url: rootPath + "/expertSelected/assistCostTotal",
                   data: vm.model
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
      // E 协审费录入列表
       
        //S 专家协申费用导出
        function assistExportExcel(vm , businessId , fileName){
        	 var   fileName = escape(encodeURIComponent(fileName));
             window.open(rootPath + '/financialManager/exportExcel?fileName=' + fileName + '&businessId=' + businessId);
        }
      // E 专家协申费用导出
        
        //S 协审费用统计列表
        function assistCostCountList(vm,callBack){
        	  var httpOptions = {
                      method: 'post',
                      url: rootPath + "/expertSelected/assistCostTotal",
                      data: vm.model
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
        //E 协审费用统计列表
        
      //检查是否为正整数
        function isUnsignedInteger(value){
            if((/^(\+|-)?\d+$/.test(value)) && value>0 ){
                return true;
            }else{
                return false;
            }
        }
       //S 初始化协审费用关联的项目
        function initAssistlProject(vm){
        	var httpOptions = {
                    method: 'get',
                    url: rootPath + "/financialManager/initfinancial",
                    params:{
                    	signid: vm.financial.businessId
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
       // E 初始化协审费用关联的项目
        
        //S 统计评审费用总和
       function  sumAssistCount(vm){
    		var httpOptions = {
                    method: 'get',
                    url: rootPath + "/financialManager/html/sumfinancial",
                    params:{
                    	businessId: vm.financial.businessId
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
       function saveAssistCost(vm){
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

        // begin#删除协审费用记录
        function deleteassistCostCount(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_assistCostCount,
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
        // end#删除协审费用记录

        //S_初始化grid
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/financialManager/assistCostCountList", $("#searchform")),
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
                    width: 100,
                    filterable: false
                },
               
                {
                    field: "builtcompanyName",
                    title: "协审单位",
                    width: 100,
                    filterable: false,
                },
               
                {
                    field: "aUserName",
                    title: "项目负责人",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "filecode",
                    title: "协审登记号",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "declaration",
                    title: "计划协审费用",
                    width: 120,
                    filterable: false,
                },
                {
                    field: "declaration",
                    title: "实付协审费用",
                    width: 120,
                    filterable: false,
                },
                {
                    field: "signdate",
                    title: "付款日期",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "declaration",
                    title: "申报金额",
                    width: 100,
                    filterable: false,
                },
                {
                    field: "",
                    title: "操作",
                    width: 100,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                             item.signid 
                            );
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

    }
})();