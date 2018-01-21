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
      /*      var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(url_sign + "/reserveListSign", $("#reserveFrom")),
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
            });*/
            var dataSource = common.kendoGridDataSource(url_sign + "/reserveListSign",$("#reserveFrom"),vm.queryParams.page,vm.queryParams.pageSize,vm.gridParams );
            // End:dataSourc


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
                    width: 160,
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
                    width: 150,
                    filterable: false,
                },
                {
                    field: "reviewstage",
                    title: "项目阶段",
                    width: 80,
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
                    title: "收文时间",
                    width: 160,
                    filterable: false,
                    format: "{0:yyyy/MM/dd HH:mm:ss}"

                },
                {
                    field: "",
                    title: "流程状态",
                    width: 80,
                    filterable: false,
                    template: function (item) {
                        if (item.ispresign) {
                            if (item.ispresign == 0) {
                                return '<span style="color:green;">预签收</span>';
                            }
                        } else {
                            return " "
                        }
                    }
                },
                {
                    field: "",
                    title: "操作",
                    width: 180,
                    template: function (item) {
                        return common.format($('#columnBtns').html(), item.signid,
                            "vm.del('" + item.signid + "')");
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