(function(){
    'use strict';
    angular.module('app').controller('reviewFeeCtrl' , reviewFee);
    reviewFee.$inject = [ 'reviewFeeSvc' , 'expertReviewSvc' , 'bsWin'];

    function reviewFee(reviewFeeSvc , expertReviewSvc , bsWin){
        var vm = this;
        vm.title = '项目列表';
        vm.reviewFee = {};


        activate();

        function activate(){
            reviewFeeSvc.projectGrid(vm);
        }

        /**
         * 查询
         */
        vm.query = function(){
            vm.gridOptions.dataSource.read();
        }

        /**
         * 评审费发放处理 弹出框
         * @param businessId
         */
        vm.dealWindow = function(businessId){
            reviewFeeSvc.findExpertReview(vm , businessId , function(data){
                vm.expertReview = data ;
                $("#payFromWindow").kendoWindow({
                    width: "70%",
                    height: "60%",
                    title: "评审费发放",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "close"]
                }).data("kendoWindow").center().open();
            })
        }


        /**
         *  计算应纳税额
         */
        vm.countTaxes = function (expertReview) {
            if(expertReview == undefined){
                return ;
            }
            if(expertReview.payDate == undefined){
                bsWin.alert("请选择评审费发放日期");
                return ;
            }
            var reg = /^(\d{4}-\d{1,2}-\d{1,2})$/;
            if(!reg.exec(expertReview.payDate)){
                bsWin.alert("请输入正确的日期格式");
                return ;
            }
            if (expertReview.expertSelectedDtoList == undefined || expertReview.expertSelectedDtoList.length == 0) {
                bsWin.alert("该方案还没评审专家");
                return;
            }
            common.initJqValidation($('#payform'));
            var isValid = $('#payform').valid();
            if(isValid){
                var len = expertReview.expertSelectedDtoList.length, ids = '', month;
                $.each(expertReview.expertSelectedDtoList,function (i,v) {
                    ids += "'" + v.id + "'";
                    if (i != (len - 1)) {
                        ids += ",";
                    }
                })
                var payDate = expertReview.payDate;
                month = payDate.substring(0, payDate.lastIndexOf('-'));
                expertReviewSvc.countTaxes(ids,month,function (data) {
                    var allExpertCost = data;
                    expertReview.reviewCost = 0;
                    expertReview.reviewTaxes = 0;
                    expertReview.totalCost = 0;

                    $.each(expertReview.expertSelectedDtoList,function(i,v){
                        var expertId = v.EXPERTID;
                        var expertSelectedId = v.id;
                        var totalCost = 0;
                        //console.log("计算专家:"+expertDto.name);
                        if (allExpertCost != undefined && allExpertCost.length > 0) {
                            //累加专家改月的评审费用
                            allExpertCost.forEach(function (v, i) {
                                if (v.EXPERTID == expertId && v.ESID != expertSelectedId) {
                                    v.REVIEWCOST = v.REVIEWCOST == undefined ? 0 : v.REVIEWCOST;
                                    v.REVIEWCOST = parseFloat(v.REVIEWCOST);
                                    totalCost = parseFloat(totalCost) + v.REVIEWCOST;
                                }
                            });
                        }
                        //console.log("专家当月累加:" + totalCost);
                        //计算评审费用
                        v.reviewCost = v.reviewCost == undefined ? 0 : v.reviewCost;
                        var reviewTaxesTotal = totalCost + parseFloat(v.reviewCost);
                        //console.log("专家当月累加加上本次:" + reviewTaxesTotal);
                        v.reviewTaxes = countNum(reviewTaxesTotal).toFixed(2);
                        v.totalCost = (parseFloat(v.reviewCost) + parseFloat(v.reviewTaxes)).toFixed(2);
                        expertReview.reviewCost = (parseFloat(expertReview.reviewCost) + parseFloat(v.reviewCost)).toFixed(2);
                        expertReview.reviewTaxes = (parseFloat(expertReview.reviewTaxes) + parseFloat(v.reviewTaxes)).toFixed(2);
                        expertReview.totalCost = (parseFloat(expertReview.reviewCost) + parseFloat(expertReview.reviewTaxes)).toFixed(2);
                    });
                });
            }
        }

        // S_countNum
        function countNum(reviewCost) {
            reviewCost = reviewCost == undefined ? 0 : reviewCost;
            var reviewTaxes = 0;
            if (reviewCost > 800 && reviewCost <= 4000) {
                reviewTaxes = (reviewCost - 800) * 0.2;
            } else if (reviewCost > 4000 && reviewCost <= 20000) {
                reviewTaxes = reviewCost * (1 - 0.2) * 0.2
            } else if (reviewCost > 20000 && reviewCost <= 50000) {
                reviewTaxes = reviewCost * (1 - 0.2) * 0.3 - 2000;
            } else if (reviewCost > 50000) {
                //待确认
            }
            return reviewTaxes;
        }// E_countNum

        /**
         * 保存专家费用
         * @param expertReview
         */
        vm.savePayment = function (expertReview) {
            common.initJqValidation($('#payform'));
            var isValid = $('#payform').valid();
            if (isValid) {
                expertReviewSvc.savePayment(expertReview,vm.isCommit,function(data){
                    if(data.flag || data.reCode == "ok"){
                        bsWin.alert("操作成功！",function(){
                            vm.isCommit = false;
                            window.parent.$("#payFromWindow").data("kendoWindow").close();
                            vm.gridOptions.dataSource.read();
                        });
                    }else{
                        bsWin.alert(data.reMsg);
                    }
                });
            }else{
                bsWin.alert("请正确填写专家评审费信息！");
            }
        }
    }
})();