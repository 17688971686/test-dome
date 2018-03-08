(function () {
    'use strict';

    angular.module('app').factory('bookBuyBusinessSvc', bookBuyBusiness);
    ;
    bookBuyBusiness.$inject = ['$http']

    function bookBuyBusiness($http) {
        var url_bookBuyBusiness = rootPath + "/bookBuyBusiness", url_back = '#/bookBuyBusinessList';
        var service = {
            grid: grid,
            getBookBuyBusinessById: getBookBuyBusinessById,
            createBookBuyBusiness: createBookBuyBusiness,
            deleteBookBuyBusiness: deleteBookBuyBusiness,
            updateBookBuyBusiness: updateBookBuyBusiness,
            saveBookBuyBusinessDetail:saveBookBuyBusinessDetail,
            startFlow:startFlow,
            initFlowDeal : initFlowDeal,                //初始化图书采购流程信息
            deleteBooksConditions : deleteBooksConditions //删除图书信息
        };

        return service;

        /**
         * 删除图书信息
         * @param delIds
         * @param isCommit
         * @param callBack
         */
        function deleteBooksConditions(delIds,callBack){
            var httpOptions = {
                method : 'delete',
                url : url_bookBuyBusiness + "/bookDel",
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
            });
        }

        // begin#updateBookBuyBusiness
        function updateBookBuyBusiness(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_bookBuyBusiness,
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

        // begin#deleteBookBuyBusiness
        function deleteBookBuyBusiness(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_bookBuyBusiness,
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

        // begin#createBookBuyBusiness
        function createBookBuyBusiness(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            var isValid = true;
            if (isValid) {
                vm.isSubmit = true;
                var httpOptions = {
                    method: 'post',
                    url: url_bookBuyBusiness,
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

        function saveBookBuyBusinessDetail(conditions,callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/bookBuyBusiness/saveBooksDetailList",
                headers:{
                    "contentType":"application/json;charset=utf-8"  //设置请求头信息
                },
                traditional: true,
                dataType : "json",
                data : angular.toJson(conditions)//将Json对象序列化成Json字符串，JSON.stringify()原生态方法
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
        }

        //S_启动流程
        function startFlow(conditions,isCommit,callBack){
            isCommit = true;
            var httpOptions = {
                method : 'post',
                url : rootPath + "/bookBuyBusiness/startFlow",
                headers:{
                    "contentType":"application/json;charset=utf-8"  //设置请求头信息
                },
                traditional: true,
                dataType : "json",
                data : angular.toJson(conditions)//将Json对象序列化成Json字符串，JSON.stringify()原生态方法
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
                onError : function(){isCommit = false;}
            });
        }//E_startFlow

        //S_初始化图书采购流程信息
        function initFlowDeal(vm){
            //vm.businessKey,vm.taskId,vm.instanceId
            var httpOptions = {
                method: 'get',
                url: rootPath + "/bookBuyBusiness/html/findById",
                params : {
                    id:vm.businessKey
                }
            };
            var httpSuccess = function success(response) {
                vm.model = response.data;
                for(var i=0;i<vm.model.bookBuyList.length;i++){
                    vm.model.bookBuyList[i]["totalPrice"] = parseFloat(vm.model.bookBuyList[i].booksPrice)*(vm.model.bookBuyList[i].bookNumber);
                }
            };
            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError : function(){}
            });
        }//E_initFlowDeal


        // begin#getBookBuyBusinessById
        function getBookBuyBusinessById(vm,callBack) {
        	var httpOptions = {
                method: 'get',
                url: rootPath + "/bookBuyBusiness/html/findById",
                params:{id:vm.businessId}
            };
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
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
                transport: common.kendoGridConfig().transport(rootPath + "/bookBuyBusiness/findByOData", $("#bookBuyForm"),{filter: "createdBy eq ${CURRENT_USER.id}"}),
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
                    field: "applyDept",
                    title: "申请部门",
                    width: "20%",
                    filterable: false
                },
                {
                    field: "operator",
                    title: "申请人",
                    width: "10%",
                    filterable: false
                },
                {
                    field: "buyChannel",
                    title: "购买渠道",
                    width: "15%",
                    filterable: false
                },
                {
                    field: "applyDate",
                    title: "申请日期",
                    width: "15%",
                    filterable: false,
                },
                {
                    field: "",
                    title: "状态",
                    width: "15%",
                    filterable: false,
                    template: function (item) {
                        if(item.processInstanceId && item.filer){
                            return "已办理";
                        }else if(item.processInstanceId){
                            return "正在办理";
                        }else{
                            return "未确定申购";
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        var isStartFlow = item.processInstanceId?true:false;
                        return common.format($('#columnBtns').html(), item.businessId, isStartFlow);
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