(function () {
    'use strict';
    angular.module('app').controller('maintainExpertPaymentCtrl', maintainExpertPayment);
    maintainExpertPayment.$inject = ['signSvc', '$state', 'bsWin', 'expertReviewSvc'];
    function maintainExpertPayment(signSvc, $state, bsWin, expertReviewSvc) {
        var vm = this;
        vm.signid = $state.params.signid;
        vm.showExpertPayment = true;

        activate();
        function activate() {
            expertReviewSvc.initReview(vm.signid,"", function (data) {
                if(data){
                    vm.expertReviewDto = data;
                }else{
                    vm.expertReviewDto = {};
                }
            });
        }

        // 计算应纳税额
        vm.countTaxes = function (expertReview) {
            if (expertReview == undefined) {
                return;
            }
            if (expertReview.expertSelectedDtoList == undefined || expertReview.expertSelectedDtoList.length == 0) {
                bsWin.alert("该方案还没有选择评审专家，请先选取评审专家！");
                return;
            }
            if (expertReview.reviewDate == undefined) {
                bsWin.alert("(函评/评审会)日期为空，无法进行专家纳税计算，请联系系统管理员处理！");
                return;
            }
            var reg = /^(\d{4}-\d{1,2}-\d{1,2})$/;
            if (!reg.exec(expertReview.reviewDate)) {
                bsWin.alert("请输入正确的日期格式");
                return;
            }
            //设置一个评审费发放日期默认值
            if (!expertReview.payDate) {
                expertReview.payDate = vm.curDate;
            }
            common.initJqValidation($('#payform'));
            var isValid = $('#payform').valid();
            if (isValid) {
                //自动保存
                expertReviewSvc.savePayment(expertReview, vm.isCommit, true,function (data) {
                    if (data.flag || data.reCode == "ok") {
                        var resultMap = data.reObj, totalCost = 0, totaoTaxes = 0;
                        for (var k = 0, lk = expertReview.expertSelectedDtoList.length; k < lk; k++) {
                            var v = expertReview.expertSelectedDtoList[k];
                            if ((v.isConfrim == '9' || v.isConfrim == 9) && (v.isJoin == '9' || v.isJoin == 9)) {
                                for (var i = 0, l = resultMap.length; i < l; i++) {
                                    var epId = resultMap[i].EXPERTID;
                                    if (v.expertDto.expertID == epId) {
                                        v.reviewTaxes = parseFloat(resultMap[i].MONTAXES == undefined ? 0 : resultMap[i].MONTAXES).toFixed(2);
                                        v.totalCost = (parseFloat(v.reviewCost) + parseFloat(v.reviewTaxes)).toFixed(2);
                                        totalCost = parseFloat(totalCost) + parseFloat(v.reviewCost);
                                        totaoTaxes = parseFloat(totaoTaxes) + parseFloat(v.reviewTaxes);
                                    }
                                }
                            }
                        }

                        expertReview.reviewCost = parseFloat(totalCost).toFixed(2);
                        expertReview.reviewTaxes = parseFloat(totaoTaxes).toFixed(2);
                        expertReview.totalCost = (parseFloat(totalCost) + parseFloat(totaoTaxes)).toFixed(2);
                        bsWin.alert("操作成功！", function () {
                            vm.isCommit = false;
                            expertReview.state = '9';
                        });
                    } else {
                        bsWin.alert(data.reMsg);
                    }
                });
            } else {
                bsWin.alert("请正确填写专家评审费信息！");
            }
        }

        // 关闭专家费用
        vm.closeEditPay = function () {
            window.parent.$("#payment").data("kendoWindow").close();
        }


        /**
         * 保存评审费，不包括计税
         * @param expertReview
         */
        vm.savePayment = function(expertReview){
            if (expertReview == undefined) {
                return;
            }
            if (expertReview.expertSelectedDtoList == undefined || expertReview.expertSelectedDtoList.length == 0) {
                bsWin.alert("该方案还没有选择评审专家，请先选取评审专家！");
                return;
            }
            common.initJqValidation($('#payform'));
            var isValid = $('#payform').valid();
            if (isValid) {
                //自动保存
                expertReviewSvc.savePayment(expertReview, vm.isCommit,false, function (data) {
                    if (data.flag || data.reCode == "ok") {
                        var resultMap = data.reObj, totalCost = 0, totaoTaxes = 0;
                        for (var k = 0, lk = expertReview.expertSelectedDtoList.length; k < lk; k++) {
                            var v = expertReview.expertSelectedDtoList[k];
                            if ((v.isConfrim == '9' || v.isConfrim == 9) && (v.isJoin == '9' || v.isJoin == 9)) {
                                for (var i = 0, l = resultMap.length; i < l; i++) {
                                    var epId = resultMap[i].EXPERTID;
                                    if (v.expertDto.expertID == epId) {
                                        v.reviewTaxes = parseFloat(resultMap[i].MONTAXES == undefined ? 0 : resultMap[i].MONTAXES).toFixed(2);
                                        v.totalCost = (parseFloat(v.reviewCost) + parseFloat(v.reviewTaxes)).toFixed(2);
                                        totalCost = parseFloat(totalCost) + parseFloat(v.reviewCost);
                                        totaoTaxes = parseFloat(totaoTaxes) + parseFloat(v.reviewTaxes);
                                    }
                                }
                            }
                        }

                        expertReview.reviewCost = parseFloat(totalCost).toFixed(2);
                        expertReview.reviewTaxes = parseFloat(totaoTaxes).toFixed(2);
                        expertReview.totalCost = (parseFloat(totalCost) + parseFloat(totaoTaxes)).toFixed(2);
                        bsWin.alert("操作成功！", function () {
                            vm.isCommit = false;
                        });
                    } else {
                        bsWin.alert(data.reMsg);
                    }
                });
            } else {
                bsWin.alert("请正确填写专家评审费信息！");
            }
        }
    }
})();