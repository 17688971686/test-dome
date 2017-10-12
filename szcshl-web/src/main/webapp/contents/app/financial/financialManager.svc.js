(function () {
    'use strict';

    angular.module('app').factory('financialManagerSvc', financialManager);

    financialManager.$inject = ['$http'];

    function financialManager($http) {
        var url_financialManager = rootPath + "/financialManager", url_back = '#/financialManagerList';
        var service = {
            grid: grid, //评审费录入列表
            deleteFinancialManager: deleteFinancialManager,			//删除报销记录
            savefinancial:savefinancial,							//保存报销记录
            sumFinancial:sumFinancial,								//统计评审费用总和
            initFinancialProject:initFinancialProject,				//初始化关联项目评审费
            isUnsignedInteger:isUnsignedInteger,					//	数字校验
            stageCostCountList:stageCostCountList,		 //评审费用统计列表
            findStageCostTableList:findStageCostTableList, //查看评审费发放表
            exportExcel : exportExcel , //评审费用统计表导出
        };

        return service;
        vm.businessFlag = {
        expertReviews : [], 
        }

        //begin reportExcel
        function exportExcel(vm , exportData , fileName){
            var httpOptions = {
                method : 'post' ,
                url : rootPath + '/financialManager/exportExcel',
                headers : {
                    "contentType" : "application/json;charset=utf-8"
                },
                traditional : true,
                dataType : "json",
                responseType: 'arraybuffer',
                data : angular.toJson(exportData),
                params:{
                    fileName :fileName
                }
            }

            var httpSuccess = function success(response){
                fileName =fileName + ".xls";
                var fileType ="vnd.ms-excel";
                common.downloadReport(response.data , fileName , fileType);
            }

            common.http({
                vm : vm ,
                $http : $http ,
                httpOptions : httpOptions,
                success : httpSuccess
            });
        }
        //end reportExcel


        //S 查看评审费发放表
        function findStageCostTableList (signId,callBack){
        	 var httpOptions = {
                     method: 'post',
                     url: rootPath + "/expertReview/getBySignId/" + signId
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
      // E 查看评审费发放表
        
       //S 评审费用统计列表
        function stageCostCountList(vm){
        	var httpOptions = {
                    method: 'post',
                    url: rootPath + "/financialManager/findByOData",
                };
                var httpSuccess = function success(response) {
                    vm.stageCountList = response.data.value;
                    
                };
                common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });          
        }
      //E 评审费用统计列表
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
       // E 初始化关联项目评审费
        
        //S 统计评审费用总和
       function  sumFinancial(vm){
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

        //S_初始化grid 评审费录入列表
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/financialManager/findByOData", $("#searchform")),
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
                    width: 140,
                    filterable: false
                },
               
                {
                    field: "designcompanyName",
                    title: "建设单位",
                    width: 180,
                    filterable: false,
                },
                {
                    field: "reviewstage",
                    title: "项目阶段",
                    width: 80,
                    filterable: false,
                },
                {
                    field: "aUserName",
                    title: "项目负责人",
                    width: 120,
                    filterable: false,
                },
                {
                    field: "projectcode",
                    title: "计划专家费用",
                    width: 160,
                    filterable: false,
                    template: function (item) {
                    	return '<a href="#/financialManager/'+item.signid+'" >'+item.projectcode+'</a>';
                    }
                },
                {
                    field: "signdate",
                    title: "签收日期",
                    width: 120,
                    filterable: false,
                },
              
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