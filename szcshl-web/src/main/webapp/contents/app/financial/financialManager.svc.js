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