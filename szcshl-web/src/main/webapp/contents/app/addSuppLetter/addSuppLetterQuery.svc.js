(function () {
    'use strict';

    angular.module('app').factory('addSuppLetterQuerySvc', addSuppLetterQuery);
    addSuppLetterQuery.$inject = ['$http', 'bsWin'];
    function addSuppLetterQuery($http, bsWin) {
        var url_addSuppLetterQuery = rootPath + "/addSuppLetter", url_back = '#/addSuppLetterQueryList';
        var service = {
        	approveGrid: approveGrid,//补充资料函审批列表
        	addQueryGrid:addQueryGrid,//补充资料函查询列表
        	createaddSuppLetterQuery: createaddSuppLetterQuery,   //部长审批处理
        	deleteaddSuppLetterQuery: deleteaddSuppLetterQuery,
            updateaddSuppLetterQuery: updateaddSuppLetterQuery,
            getaddSuppLetterQueryById: getaddSuppLetterQueryById, //根据ID查看拟补充资料函
            initSuppLetter: initSuppLetter,             //初始化补充资料函
            createFilenum: createFilenum,               //生成文件字号
            initSuppListDate: initSuppListDate,         //初始化拟补充资料函列表
            checkIsApprove:checkIsApprove,              //检查
        };

        return service;

      
        //S 初始化拟补充资料函列表
        function initSuppListDate(businessId,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/addSuppLetterQuery/initSuppListDate",
                params: {
                    businessId: businessId,
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
        }

        //E 初始化拟补充资料函列表
        //S 生成文件字号 
        function createFilenum(id,callBack) {
            var httpOptions = {
                method: 'post',
                url: url_addSuppLetterQuery + "/createFileNum",
                params: {id: id}
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
        //E 生成文件字号


        //S 初始化补充资料函
        function initSuppLetter(businessId,businessType,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/addSuppLetterQuery/initSuppLetter",
                params: {
                    businessId: businessId,
                    businessType:businessType,
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
        }

        //E 初始化补充资料函

        // begin#updateaddSuppLetterQuery
        function updateaddSuppLetterQuery(vm) {
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.isSubmit = true;
                vm.model.id = vm.id;// id

                var httpOptions = {
                    method: 'put',
                    url: url_addSuppLetterQuery,
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

        // begin#deleteaddSuppLetterQuery
        function deleteaddSuppLetterQuery(id,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/addSuppLetter/deleteById",
                params: {id:id}
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

        // begin#部长审批处理
        function createaddSuppLetterQuery(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/addSuppLetter/updateApprove",
                data: vm.suppletter
            };
            var httpSuccess = function success(response) {
            	 bsWin.success("操作成功！")
            };

            common.http({
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
            });


        }

        // begin#getaddSuppLetterQueryById
        function getaddSuppLetterQueryById(vm,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/addSuppLetter/findById",
                params: {id: vm.id}
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

     // begin#补充资料函查询列表
        function addQueryGrid(vm){
        	// Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/addSuppLetter/addsuppListData" , $('#supQueryForm')),
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
                    field: "rowNumber",
                    title: "序号",
                    width: 50,
                    filterable: false,
                    template: "<span class='row-number'></span>"
                },
                {
                    field: "",
                    title: "文件标题",
                    filterable: false,
                    template: function (item) {
                        return "<a ng-click='vm.showSuppLetterDetail( " + '"' +(item.id) + '"'+ ")'>"+item.title+"</a>";
                    }
                },
                {
                    field: "orgName",
                    title: "拟稿部门",
                    width: 120,
                    filterable: false
                },
                {
                    field: "userName",
                    title: "拟稿人",
                    width: 90,
                    filterable: false
                },
                {
                    field: "suppLetterTime",
                    title: "拟稿时间",
                    width: 120,
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "filenum",
                    title: "文件字号",
                    width: 120,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: 80,
                    filterable: false,
                    template: function (item) {
                        //如果拟补充资料函还未发起流程（保存异常或者提交附件失败）
                        //则这里可以编辑
                        if((item.createdBy == curUserId  || isSuperUser )&& (!item.processInstanceId) ){
                            return "<a class='btn btn-xs btn-primary' href='#addSuppLetterEdit/"+item.id+"'><span class='glyphicon glyphicon-pencil'></span>编辑</a><a class='btn btn-xs btn-danger' ng-click='vm.deleteById(\""+item.id+"\")'><span class='glyphicon glyphicon-remove'></span>删除</a>";
                        }else{
                            return "";
                        }
                    }
                }
            ];
            // End:column

            vm.queryGridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound: dataBound,
                resizable: true
            };

        }
        //end# 补充资料函查询列表
        
        
        // begin#补充资料函审批列表
        function approveGrid(vm) {

            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_addSuppLetterQuery+"/addSuppApproveList"),
                schema: {
                    data: "value",
                    total: function (data) {
                        if (data['count']) {
                            $("#ADDSUPPLETTER_COUNT").html(data['count']);
                        } else {
                            $("#ADDSUPPLETTER_COUNT").html(0);
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
                    field: "title",
                    title: "文件标题",
                    width: 100,
                    filterable: false
                },
                {
                    field: "orgName",
                    title: "拟稿部门",
                    width: 100,
                    filterable: false
                },
                {
                    field: "userName",
                    title: "拟稿人",
                    width: 100,
                    filterable: false
                },
                {
                    field: "suppLetterTime",
                    title: "拟稿时间",
                    width: 100,
                    filterable: false,
                    format: "{0: yyyy-MM-dd HH:mm:ss}"
                },
              
                {
                    field: "filenum",
                    title: "文件字号",
                    width: 100,
                    filterable: false
                },
              
                {
                    field: "",
                    title: "操作",
                    width: 140,
                    template: function (item) {
                        return common.format($('#columnBtns').html(),item.id);
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

        //begin 检查是否有未完成审批的拟补充资料函
        function checkIsApprove(signId,fileType,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/addSuppLetter/checkIsApprove",
                params: {signId: signId,fileType:fileType}
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
        }//end 检查是否有未完成审批的拟补充资料函
    }
})();