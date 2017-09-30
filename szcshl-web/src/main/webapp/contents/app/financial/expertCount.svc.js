(function () {
    'use strict';

    angular.module('app').factory('exportCountSvc', exportCount);

    exportCount.$inject = ['$http'];

    function exportCount($http) {
        var url_exportCount = rootPath + "/exportCount", url_back = '#/exportCountList';
        var service = {
            grid: grid,
            deleteexportCount: deleteexportCount,			//删除报销记录
            savefinancial:savefinancial,							//保存报销记录
            sumFinancial:sumFinancial,								//统计评审费用总和
            initFinancialProject:initFinancialProject,				//初始化关联项目评审费
            isUnsignedInteger:isUnsignedInteger,					//	数字校验
            excelExport:excelExport
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
                    url: rootPath + "/exportCount/initfinancial",
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
                    url: rootPath + "/exportCount/html/sumfinancial",
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
    				   url : rootPath + "/exportCount",
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

        // begin#deleteexportCount
        function deleteexportCount(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_exportCount,
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
        // end#deleteexportCount

        //begin excelExport
        function excelExport(vm,exportData,fileName){
            var paramStr = {fileName:fileName};
            var url = rootPath + "/expertSelected/expertDetailExport";
            if ($("#searchform")) {
                var filterParam = common.buildOdataFilter($("#searchform"));
                if (filterParam) {
                    paramStr.$filter = filterParam;
                }
            }
            var httpOptions ={
                method : 'post',
                url : url,
                traditional : true,
                dataType : "json",
                responseType: 'arraybuffer',
                params:paramStr
            }
            var httpSuccess = function success(response){
                fileName =fileName + ".xls";
                var fileType ="vnd.ms-excel";
                common.downloadReport(response.data , fileName , fileType);
            }
            common.http({
                vm : vm,
                $http : $http ,
                httpOptions : httpOptions,
                success : httpSuccess
            });
        }
        //end excelExport

        //S_初始化grid(过滤已签收和已经完成的项目)
        function grid(vm) {
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/expertSelected/findByOData", $("#searchform")),
                schema: common.kendoGridConfig().schema({
                    id: "id"
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10
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
				    filterable : false,
				    template: "<span class='row-number'></span>"
				 },
                {
                    field: "expertDto.name",
                    title: "姓名",
                    width: 80,
                    filterable: false
                },
               
                {
                    field: "expertDto.idCard",
                    title: "身份证号",
                    width: 180,
                    filterable: false,
                },
                {
                    field: "expertDto.openingBank",
                    title: "开户行",
                    width: 80,
                    filterable: false,
                },
                {
                    field: "expertDto.bankAccount",
                    title: "银行账号",
                    width: 180,
                    filterable: false,
                },
                {
                    field: "reviewCost",
                    title: "评审费",
                    width: 80,
                    filterable: false,
                },
                {
                    field: "reviewTaxes",
                    title: "应缴税",
                    width: 80,
                    filterable: false,
                },
                {
                    field: "expertReviewDto.reviewTitle",
                    title: "项目名称",
                    width: 225,
                    filterable: false,
                },
                {
                    field: "expertReviewDto.reviewDate",
                    title: "评审时间",
                    width: 80,
                    filterable: false,
                },
                {
                    field: "expertReviewDto.reviewDate",
                    title: "函评时间",
                    width: 80,
                    filterable: false,
                },
                {
                    field: "principal",
                    title: "负责人",
                    width: 75,
                    filterable: false,
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