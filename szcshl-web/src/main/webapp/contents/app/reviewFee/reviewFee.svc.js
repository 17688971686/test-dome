(function(){
    'use strict';
    angular.module('app').factory('reviewFeeSvc' , reviewFee);
    reviewFee.$inject = ['$http'];
    function reviewFee($http){
        var service = {
            projectGrid : projectGrid ,         //查询发文申请阶段，评审费发放超期的项目列表
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
            var dataSource = common.kendoGridDataSource(rootPath + "/reviewFee/findReviewFee",$("#reviewFeeForm"),vm.queryParams.page,vm.queryParams.pageSize,vm.gridParams);
            // End:dataSource

            // Begin:column
            var columns = [
                {
                    field: "",
                    title: "序号",
                    template: "<span class='row-number'></span>",
                    width: 50
                },
                {
                    field: "reviewTitle",
                    title: "评审费标题",
                    width: "40%",
                    filterable: false,
                },
                {
                    field: "",
                    title: "评审费类型",
                    width: "20%",
                    filterable: false,
                    template: function (item) {
                        if(item.businessType && item.businessType.trim() == "SIGN"){
                            return "项目评审费";
                        }else if(item.businessType && item.businessType.trim() == "TOPIC"){
                            return "课题评审费";
                        }else{
                            return "";
                        }
                    }
                },
                {
                    field: "reviewDate",
                    title: "评审(函评)日期",
                    width: "15%",
                    filterable: false,
                    format: "{0: yyyy-MM-dd}"
                },
                {
                    field: "",
                    title: "操作",
                    width: "20%",
                    template: function (item) {
                      return common.format($('#columnBtns').html(), "vm.dealWindow('" +item.businessId+ "')"
                          , "vm.detail('" + item.businessId + "','" + item.businessType + "')");
                    }
                }
            ];
            // End:column

            vm.gridOptions = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                pageable : common.kendoGridConfig(vm.queryParams).pageable,
                dataBound:common.kendoGridConfig(vm.queryParams).dataBound,
                resizable: true
            };

        }
        //end projectGrid
    }

})();