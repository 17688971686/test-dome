(function () {
    'use strict';

    angular.module('app').factory('reserveSignSvc', sign);

    sign.$inject = ['$http','$state'];

    function sign($http,$state) {
        var url_sign = rootPath + "/sign";
        var url_back = '#/sign';
        var service = {
            grid: grid,                                  //预签收列表
            getsignById: getsignById,                    //根据id查询
            reserveAdd: reserveAdd,                     //新增预签收记录
            deleteReserveSign: deleteReserveSign,       //删除预签收记录
        };

        return service;

        //S_查询grid
        function querySign(vm) {
            vm.gridOptions.dataSource.read();
        }//E_查询grid

        // begin# E_项目预签收
        function reserveAdd(model ,callBack) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/sign/html/reserveAddPost",
                data: model
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
        //end E_项目预签收


        // begin#deleteUser
        function deleteReserveSign(vm, id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url: url_sign +"/deleteReserve",
                params:{signid :id}

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



        
        // begin#getUserById
        function getsignById(vm) {
            var httpOptions = {
                method: 'get',
                url: url_sign + "/html/findByIdsign",
                params: {id: vm.id}
            }
            var httpSuccess = function success(response) {
                vm.model = response.data;
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
                transport: common.kendoGridConfig().transport(url_sign + "/reserveListSign", $("#reserveFrom"),vm.gridParams),
                schema: {
                    data: "value",
                    total: function (data) {
                        if (data['count']) {
                            $('#GET_RESERVESIGN_COUNT').html(data['count']);
                        } else {
                            $('#GET_RESERVESIGN_COUNT').html(0);
                        }
                        return data['count'];
                    },
                    model: {
                        id: "signid"
                    }
                },
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize : vm.queryParams.pageSize ||10,
                page:vm.queryParams.page||1,
                sort: {
                    field: "createdDate",
                    dir: "desc"
                }
            });

            // End:dataSourc


            // Begin:column
            var columns = [
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
                    width: 280,
                    filterable: false
                },
                {
                    field: "projectcode",
                    title: "收文编号",
                    width: 120,
                    filterable: false,
                },
                {
                    field: "designcompanyName",
                    title: "项目单位",
                    width: 220,
                    filterable: false,
                },
                {
                    field: "reviewstage",
                    title: "项目阶段",
                    width: 130,
                    filterable: false,
                },
                {
                    field: "projectcode",
                    title: "项目代码",
                    width: 120,
                    filterable: false,
                },
                {
                    field: "receivedate",
                    title: "收文日期",
                    width: 160,
                    filterable: false,
                    format: "{0:yyyy-MM-dd HH:mm:ss}"

                },
                {
                    field: "",
                    title: "操作",
                    width: 320,
                    template: function (item) {
                        var isStartFlow = false;
                        if(item.processInstanceId){
                            isStartFlow = true;
                        }
                        var isRealSign = false;
                        if(item.issign && (item.issign == 9 || item.issign == '9' )){
                            isRealSign = true;
                        }
                        //如果已经发起流程，则只能查看
                        return common.format($('#columnBtns').html(),
                            item.signid, isStartFlow,
                            item.signid + "/" + item.processInstanceId,
                            "vm.del('" + item.signid + "')",
                            "vm.startNewFlow('" + item.signid + "')",
                            "vm.realSign('" + item.signid + "')", isRealSign);
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable : common.kendoGridConfig(vm.queryParams).pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound:common.kendoGridConfig(vm.queryParams).dataBound,
                resizable: true
            };
        }// end fun grid


    }
})();