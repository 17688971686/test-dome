(function(){
    'use strict';
    angular.module('app').factory('reviewFeeSvc' , reviewFee);
    reviewFee.$inject = ['$http'];
    function reviewFee($http){
        var service = {
            projectGrid : projectGrid ,//查询发文申请阶段，评审费发放超期的项目列表
            findExpertReview : findExpertReview,//通过业务ID获取专家评审方案信息

        }

        return service;
        //begin findExpertReview
        function findExpertReview(vm , businessId , callBack){
            var httpOptions = {
                method : 'post',
                url : rootPath + "/expertReview/initBybusinessId",
                params : {businessId : businessId  , minBusinessId : ""}
            }

            var httpSuccess = function success(response){
                if(callBack !=undefined || typeof callBack == 'function'){
                    callBack(response.data);
                }

            }
            common.http({
                vm : vm ,
                $http : $http ,
                httpOptions : httpOptions ,
                success : httpSuccess
            });
        }
        //end findExpertReview


        //begin projectGrid
        function projectGrid(vm){
            // Begin:dataSource
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath + "/reviewFee/findSignDispaWork"),
                schema: common.kendoGridConfig().schema({
                    id: "id",
                    fields: {
                    }
                }),
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true,
                pageSize: 10,
                sort: {
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
                    field: "",
                    title: "序号",
                    template: "<span class='row-number'></span>",
                    width: 50
                },
                {
                    field: "",
                    title: "项目名称",
                    width: 160,
                    filterable: false,
                    template: function (item) {
                        if(item.processInstanceId){
                            return '<a href="#/signDetails/'+item.signid+'/'+item.processInstanceId+'" >'+item.projectname+'</a>';
                        }else{
                            return '<a href="#/signDetails/'+item.signid+'/" >'+item.projectname+'</a>';
                        }

                    }
                },
                {
                    field: "reviewstage",
                    title: "评审阶段",
                    width: 140,
                    filterable: false
                },
                {
                    field: "",
                    title: "操作",
                    width: "6%",
                    template: function (item) {
                      return common.format($('#columnBtns').html(), "vm.dealWindow('" +item.signid+ "')");
                    }
                }
            ];// End:column
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                pageable: common.kendoGridConfig().pageable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                dataBound: dataBound,
                resizable: true
            };

        }
        //end projectGrid
    }

})();