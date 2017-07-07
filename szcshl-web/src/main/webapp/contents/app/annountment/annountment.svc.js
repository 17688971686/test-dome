(function () {
    'use strict';

    angular.module('app').factory('annountmentSvc', annountment);

    annountment.$inject = ['$http','sysfileSvc'];

    function annountment($http,sysfileSvc) {
    	
    	var url_annountment=rootPath+"/annountment";
    	var url_back="#/annountment";
        var service = {
        	
        	grid : grid,		//	初始化列表
        	createAnnountment :createAnnountment,	//新增通知公告
        	initAnOrg :initAnOrg,		//初始化发布单位
        	findAnnountmentById : findAnnountmentById,	//获取通知公告信息
        	
        	updateAnnountment : updateAnnountment,	//	更新通知公告
        	deleteAnnountment : deleteAnnountment	//删除通知公告
            
        };

        return service;
        
        //begin initAnOrg
        function initAnOrg(vm){
        	var httpOptions={
        		method:"get",
        		url:url_annountment+"/initAnOrg"
        	}
        	
        	var httpSuccess=function success(response){
        		vm.annountment.anOrg="";
        		vm.annountment.anOrg=response.data.substring(1,response.data.length-1);
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
        function findAnnountmentById(vm){
        	var httpOptions={
        		method:"get",
        		url:url_annountment+"/findAnnountmentById",
        		params:{anId:vm.anId}
        	}
        	
        	var httpSuccess=function success(response){
        		vm.annountment=response.data;
        			
        		//初始化附件上传
                sysfileSvc.initUploadOptions({
                     businessId:vm.annountment.anId,
                     sysfileType:"通知公告",
                     uploadBt:"upload_file_bt",
                     detailBt:"detail_file_bt",
                     vm:vm
               });
        	}
        	 common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });
        }//end findAnnountmentById
        
        //begin createAnnountment
        function createAnnountment(vm){
        	var httpOptions={
        		method:"post",
        		url:url_annountment,
        		data : vm.annountment
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
//                                    location.href = url_back;
                                }
                            })
                        }

                    })
                 vm.annountment.anId=response.data.anId;
                    //初始化附件上传
                sysfileSvc.initUploadOptions({
                     businessId:vm.annountment.anId,
                     sysfileType:"通知公告",
                     uploadBt:"upload_file_bt",
                     detailBt:"detail_file_bt",
                     vm:vm
               });
        	}
        	
        	 common.http({
                    vm: vm,
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess
                });

        	
        }//end createAnnountment
        
        //begin updateAnnountment
        function updateAnnountment(vm){
        	var httpOptions={
        		method : "put",
        		url : url_annountment,
        		data : vm.annountment
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
        function deleteAnnountment(vm,anId){
        
        	var httpOptions={
        		method : "delete",
        		url :url_annountment,
        		data : anId
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
        //end deleteAnnountment
       // begin#grid
        function grid(vm) {

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_annountment+"/fingByOData?$orderby=isStick"),
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
                sort:
                 {
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
                    field: "anOrg",
                    title: "发布单位",
                    width: 100,
                    filterable: false
                },
                {
                    field: "anDate",
                    title: "发布时间",
                    format:"{0:yyyy-MM-dd}",
                    width: 100,
                    filterable: false
                },
                {
                    field: "anUser",
                    title: "发布人",
                    width: 100,
                    filterable: false
                },
//                {
//                    field: "anSort",
//                    title: "排序序号",
//                    width: 80,
//                    filterable: false
//                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),
                            "vm.del('" + item.anId + "')", item.anId);
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

        
 	}
})();