(function () {
    'use strict';

    angular.module('app').controller('expertPaymentDetailCountCtrl', expertPaymentCount);

    expertPaymentCount.$inject = ['$location', 'expertPaymentCountSvc', '$state', '$http'];

    function expertPaymentCount($location, expertPaymentCountSvc, $state, $http) {
        var vm = this;
        vm.title = '专家缴税统计管理';
        vm.model = {};
        vm.financials = new Array;
        vm.sign = {}; //收文对象
        vm.financial = {};//财务对象
        vm.model.year = $state.params.year;
        vm.model.month = $state.params.month;


        //查看汇总
        vm.getExpertCoust = function () {
            $state.go('expertPaymentCountList', {year: vm.model.year, month: vm.model.month});
        }

        //按月份统计专家明细
        vm.countExpertCostDetail = function () {
            expertPaymentCountSvc.expertCostDetailTotal(vm, function (data) {
                vm.expertCostTotalInfo = data.reObj.expertCostTotalInfo
                var trCount = $("#expertCostTable tr").length;
                for (var i = 1; i < trCount; i++) {
                    $("#option" + i).remove();
                }
                createExpertCostTable(vm.expertCostTotalInfo);
            });
        }

        activate();
        function activate() {
            if ($state.params.year) {
                vm.model.year = $state.params.year;
                vm.model.month = $state.params.month;
            } else {
                var date = new Date;
                var year = date.getFullYear();
                var month = date.getMonth() + 1;
                vm.model.year = year;
                vm.model.month = month;
            }
            expertPaymentCountSvc.expertCostDetailTotal(vm, function (data) {
                vm.expertCostTotalInfo = data.reObj.expertCostTotalInfo
                createExpertCostTable(vm.expertCostTotalInfo);
            });
        }

        //生成专家评审费明细表格
        function createExpertCostTable(expertCostTotalInfo) {
            var expertCostTr = "";
            var rowIndex = 0;
            if (expertCostTotalInfo.length > 0) {
                for (var i = 0; i < expertCostTotalInfo.length; i++) {
                    rowIndex++;
                    expertCostTr += "<tr id='option" + rowIndex + "'>";
                    expertCostTr += "<td colspan='5'>";
                    if (expertCostTotalInfo[i].name.length == 2) {
                        expertCostTr += "<span style='margin-left: 6.5%;'><strong>" + expertCostTotalInfo[i].name + "</strong></span>";
                    } else {
                        expertCostTr += "<span style='margin-left: 5%;'><strong>" + expertCostTotalInfo[i].name + "</strong></span>";
                    }
                    expertCostTr += "<span style='margin-left: 8%;'></span><strong>" + expertCostTotalInfo[i].expertNo + "</strong></span>";
                    expertCostTr += "<span style='margin-left: 58%;'></span><strong>合计:</strong></span>";
                    expertCostTr += "<span style='margin-left: 4%;'></span><strong>" + expertCostTotalInfo[i].monthTotal + "</strong></span>";
                    expertCostTr += "</td>";
                    expertCostTr += "<td class='text-center'>";
                    expertCostTr += expertCostTotalInfo[i].reviewcost;
                    expertCostTr += "</td>"
                    expertCostTr += "<td class='text-center'>";
                    expertCostTr += expertCostTotalInfo[i].reviewtaxes;
                    expertCostTr += "</td>"
                    expertCostTr += "</tr>";
                    if (expertCostTotalInfo[i].expertCostDetailCountDtoList.length > 0) {
                        for (var j = 0; j < expertCostTotalInfo[i].expertCostDetailCountDtoList.length; j++) {
                            rowIndex++;
                            var expertCostDetailTr = "";
                            expertCostDetailTr += "<tr id='option" + rowIndex + "'>";
                            expertCostDetailTr += "<td class='text-center' colspan='2'>";
                            expertCostDetailTr += expertCostTotalInfo[i].expertCostDetailCountDtoList[j].reviewTitle == undefined ? "" : expertCostTotalInfo[i].expertCostDetailCountDtoList[j].reviewTitle;
                            expertCostDetailTr += "</td>"
                            expertCostDetailTr += "<td class='text-center'>";
                            expertCostDetailTr += expertCostTotalInfo[i].expertCostDetailCountDtoList[j].reviewType == undefined ? "" : expertCostTotalInfo[i].expertCostDetailCountDtoList[j].reviewType;
                            expertCostDetailTr += "</td>"
                            expertCostDetailTr += "<td class='text-center'>";
                            if (expertCostTotalInfo[i].expertCostDetailCountDtoList[j].reviewDate != undefined) {
                                expertCostDetailTr += expertCostTotalInfo[i].expertCostDetailCountDtoList[j].reviewDate;
                            }
                            expertCostDetailTr += "</td>"
                            expertCostDetailTr += "<td class='text-center'>";
                            if (expertCostTotalInfo[i].expertCostDetailCountDtoList[j].reviewDate != undefined) {
                                expertCostDetailTr += expertCostTotalInfo[i].expertCostDetailCountDtoList[j].reviewDate;
                            }
                            expertCostDetailTr += "</td>"
                            expertCostDetailTr += "<td class='text-center'>";
                            expertCostDetailTr += expertCostTotalInfo[i].expertCostDetailCountDtoList[j].reviewcost;
                            expertCostDetailTr += "</td>"
                            expertCostDetailTr += "<td class='text-center'>";
                            expertCostDetailTr += expertCostTotalInfo[i].expertCostDetailCountDtoList[j].reviewtaxes;
                            expertCostDetailTr += "</td>"
                            expertCostDetailTr += "</tr>";
                            expertCostTr += expertCostDetailTr;
                        }
                    }
                }
                $("#expertCostHead").after(expertCostTr);
            }
        }
    }
})();
