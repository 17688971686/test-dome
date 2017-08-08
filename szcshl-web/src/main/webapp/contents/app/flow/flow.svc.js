(function () {
    'use strict';

    angular.module('app').factory('flowSvc', flow);

    flow.$inject = ['$http', '$state', 'signFlowSvc'];

    function flow($http, $state, signFlowSvc) {
        var service = {
            initFlowData: initFlowData, // 初始化流程数据
            getFlowInfo: getFlowInfo, // 获取流程信息
            commit: commit, // 提交
            rollBackToLast: rollBackToLast, // 回退到上一环节
            rollBack: rollBack, // 回退到选定环节
            initBackNode: initBackNode, // 初始化回退环节信息
            initDealUerByAcitiviId: initDealUerByAcitiviId,
            suspendFlow: suspendFlow, // 流程挂起
            activeFlow: activeFlow, // 重启流程
            deleteFlow: deleteFlow, // 流程终止
            saveMark: saveMark, // 保存专家评分
            savePayment: savePayment, // 保存专家费用
            countTaxes: countTaxes, // 计算应纳税额
            gotopayment: gotopayment,
        };
        return service;

        // S_初始化流程数据
        function initFlowData(vm) {
            var processInstanceId = vm.flow.processInstanceId;
            if (angular.isUndefined(vm.flow.hideFlowImg)
                || vm.flow.hideFlowImg == false) {
                vm.picture = rootPath + "/flow/processInstance/img/"
                    + processInstanceId;
            }
            var dataSource = new kendo.data.DataSource({
                type: 'odata',
                transport: common.kendoGridConfig().transport(rootPath
                    + "/flow/processInstance/history/" + processInstanceId),
                schema: common.kendoGridConfig().schema({
                    id: "id"
                }),
                rowNumber: true,
                headerCenter: true
            });

            var columns = [{
                field: "",
                title: "序号",
                template: "<span class='row-number'></span>",
                width: 40
            }, {
                field: "nodeName",
                title: "环节名称",
                width: 120,
                filterable: false
            }, {
                field: "",
                title: "处理人",
                width: 80,
                filterable: false,
                template:function(item) {
                    if(item.assignee){
                        return item.assignee;
                    }else if(item.userName){
                        return item.userName;
                    }else{
                        return "";
                    }
                }
            }, {
                field: "startTime",
                title: "开始时间",
                width: 120,
                filterable: false,
                format: "{0: yyyy-MM-dd HH:mm:ss}"
            }, {
                field: "endTime",
                title: "结束时间",
                width: 120,
                filterable: false,
                format: "{0: yyyy-MM-dd HH:mm:ss}"
            }, {
                field: "durationStr",
                title: "处理时长",
                width: 120,
                filterable: false
            }, {
                field: "message",
                title: "处理信息",
                width: 300,
                filterable: false
            }];
            // End:column
            vm.historygrid = {
                dataSource: common.gridDataSource(dataSource),
                filterable: common.kendoGridConfig().filterable,
                noRecords: common.kendoGridConfig().noRecordMessage,
                columns: columns,
                resizable: true,
                dataBound: function () {
                    var rows = this.items();
                    $(rows).each(function (i) {
                        if (i == rows.length - 1) {
                            initBackNode(vm);
                        }
                        $(this).find(".row-number").html(i + 1);
                    });
                }
            };
        }// E_初始化流程数据

        // S_getFlowInfo
        function getFlowInfo(vm) {
            var httpOptions = {
                method: 'get',
                url: rootPath + "/flow/processInstance/flowNodeInfo",
                params: {
                    taskId: vm.flow.taskId,
                    processInstanceId: vm.flow.processInstanceId
                }
            }

            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        vm.flow = response.data;
                        //如果是结束环节，则不显示下一环节信息
                        if (vm.flow.end) {
                            vm.showFlag.nodeNext = false;
                        }
                        //更改状态,并初始化业务参数
                        vm.businessFlag.isLoadFlow = true;
                        vm.initBusinessParams();
                    }
                })
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });
        }// E_getFlowInfo

        // S_提交下一步
        function commit(isCommit,flowObj,callBack) {
            common.initJqValidation($("#flow_form"));
            var isValid = $("#flow_form").valid();
            if (isValid) {
                isCommit = true;
                var httpOptions = {
                    method: 'post',
                    url: rootPath + "/flow/commit",
                    data: flowObj
                }
                var httpSuccess = function success(response) {
                    isCommit = false;
                    if (callBack != undefined && typeof callBack == 'function') {
                        callBack(response.data);
                    }
                }
                common.http({
                    $http: $http,
                    httpOptions: httpOptions,
                    success: httpSuccess,
                    onError: function (response) {
                        isCommit = false;
                    }
                });
            }
        }// E_提交下一步

        // S_回退到上一步
        function rollBackToLast(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/flow/rollbacklast",
                data: vm.flow
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: response.data.reMsg,
                            closeDialog: true,
                            fn: function () {
                                if (response.data.reCode == "error") {
                                    vm.isCommit = false;
                                } else {
                                    $state.go('gtasks');
                                }
                            }
                        })
                    }

                })
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError: function (response) {
                    vm.isCommit = false;
                }
            });
        }// E_回退到上一步

        // S_回退到指定环节
        function rollBack(vm) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/flow/rollbacklast",
                data: vm.flow
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: response.data.reMsg
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
        }// E_回退到指定环节

        // S_初始化回退环节信息
        function initBackNode(vm) {
            vm.backNode = [];
            // 初始化可回退环节
            var datas = vm.historygrid.dataSource.data()
            var totalNumber = datas.length;
            for (var i = 0; i < totalNumber; i++) {
                if (datas[i].assignee && datas[i].endTime) {
                    vm.backNode.push({
                        "activitiId": datas[i].activityId,
                        "activitiName": datas[i].activityName,
                        "assignee": datas[i].assignee
                    });
                }
            }
        }// E_初始化回退环节信息

        // S_初始化下一环节处理人
        function initDealUerByAcitiviId(vm) {
            vm.nextDealUserList = vm.nextDealUserMap[vm.flow.nextNodeAcivitiId];
            if (vm.nextDealUserList) {
                vm.flow.nextDealUser = vm.nextDealUserList[0].loginName; // 默认选中
            }
        }// E_初始化下一环节处理人

        // S_流程挂起
        function suspendFlow(vm, businessKey) {
            var httpOptions = {
                method: 'post',
                url: rootPath + "/flow/suspend/" + businessKey,
                data : vm.projectStop
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: response.data.reMsg,
                            closeDialog: true,
                            fn: function () {
                                if (response.data.reCode == "error") {
                                    vm.isCommit = false;
                                } else {
                                    window.parent.$("#spwindow").data("kendoWindow").close();
                                    vm.gridOptions.dataSource.read();
                                }
                            }
                        })
                    }
                })
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError : function () {
                    vm.isCommit = false;
                }
            });
        }// E_流程挂起

        // S_流程激活
        function activeFlow(vm, businessKey) {
            vm.isCommit = true;
            var httpOptions = {
                method: 'post',
                url: rootPath + "/flow/active/" + businessKey
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: response.data.reMsg,
                            closeDialog: true,
                            fn: function () {
                                if (response.data.reCode == "error") {
                                    vm.isCommit = false;
                                } else {
                                    vm.gridOptions.dataSource.read();
                                }
                            }
                        })
                    }
                })
            }

            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess,
                onError : function () {
                    vm.isCommit = false;
                }
            });
        }// E_流程激活

        // S_终止流程
        function deleteFlow(vm) {
            if (vm.flow.dealOption == null || vm.flow.dealOption == "") {
                common.alert({
                    vm: vm,
                    msg: "请填写处理信息！"
                })
                return;
            }
            var httpOptions = {
                method: 'post',
                url: rootPath + "/flow/deleteFLow",
                data: vm.flow
            }
            var httpSuccess = function success(response) {
                common.requestSuccess({
                    vm: vm,
                    response: response,
                    fn: function () {
                        common.alert({
                            vm: vm,
                            msg: "操作成功！"
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
        }// E_终止流程

        // S_saveMark
        function saveMark(vm,callBack) {
            common.initJqValidation($('#markform'));
            var isValid = $('#markform').valid();
            if (isValid) {
                var httpOptions = {
                    method: 'put',
                    url: rootPath + "/expertSelected",
                    data: vm.businessFlag.expertScore,
                }

                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm: vm,
                        response: response,
                        fn: function () {
                            window.parent.$("#expertmark") .data("kendoWindow").close();
                            vm.isSubmit = false;
                            common.alert({
                                vm: vm,
                                msg: "操作成功",
                                closeDialog: true,
                                fn: function () {
                                    //重新初始化评审费发放数据
                                    if (callBack != undefined && typeof callBack == "function") {
                                        callBack();
                                    }
                                }
                            });
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
        }// E_saveMark

        // S_savePayment
        function savePayment(vm,callBack) {
            common.initJqValidation($('#payform'));
            var isValid = $('#payform').valid();
            if (isValid) {
                if (!validateNum(vm, vm.businessFlag.expertReviews)) {
                    common.alert({
                        vm: vm,
                        msg: "应纳税额计算错误,保存失败！",
                        fn: function () {
                            $('.alertDialog').modal('hide');
                            $('.modal-backdrop').remove();
                        }
                    });
                    vm.isCommit = false;
                    return;
                }
                vm.isCommit = true;
                var httpOptions = {
                    method: 'post',
                    //headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    url: rootPath + "/expertReview/html/saveExpertReviewCost",
                    data: JSON.stringify(vm.businessFlag.expertReviews)
                }


                var httpSuccess = function success(response) {
                    vm.isCommit = false;
                    common.alert({
                        vm: vm,
                        msg: "操作成功",
                        closeDialog: true,
                        fn: function () {
                            //重新初始化评审费发放数据
                            if (callBack != undefined && typeof callBack == "function") {
                                callBack();
                            }
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
        }// E_savePayment

        // S_countNum
        function countNum(reviewCost) {
            reviewCost = reviewCost == undefined ? 0 : reviewCost;
            // console.log('评审费：'+reviewCost);
            //var XSum = vm.expertReview.reviewCost;
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

        // S_countTaxes
        function countTaxes(vm, expertReview) {
            if (expertReview.expertSelectedDtoList == undefined || expertReview.expertSelectedDtoList.length == 0) {
                return;
            }

            var expertSelectedDtoList = expertReview.expertSelectedDtoList;
            var len = expertReview.expertSelectedDtoList.length, ids = '', month;
            expertReview.expertSelectedDtoList.forEach(function (v, i) {
                ids += "'" + v.id + "'";
                if (i != (len - 1)) {
                    ids += ",";
                }
            });
            var payDate = expertReview.payDate

            month = payDate.substring(0, payDate.lastIndexOf('-'));
            var url = rootPath + "/expertReview/html/getExpertReviewCost?expertIds={0}&month={1}";

            //取得该评审方案评审专家在这个月的所有评审费用
            var httpOptions = {
                method: 'get',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                url: common.format(url, ids, month)
            }

            var httpSuccess = function success(response) {
                var allExpertCost = response.data;
                expertReview.reviewCost = 0;
                expertReview.reviewTaxes = 0;
                expertReview.totalCost = 0;

                expertSelectedDtoList.forEach(function (v, i) {

                    var expertDto = v.expertDto;
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

                //console.log(expertReview);

            }


            common.http({
                vm: vm,
                $http: $http,
                httpOptions: httpOptions,
                success: httpSuccess
            });


        }// E_countTaxes

        // S_validateNum
        function validateNum(vm, expertReviews) {
            var isVilad = true;
            //计算每个评审的评审费是否正确
            if (expertReviews != undefined && expertReviews.length > 0) {
                expertReviews.forEach(function (v, i) {
                    if (v.payDate == undefined) {
                        v.errorMsg = "请选择发放日期";
                        isVilad = false;
                        return;
                    }
                    v.errorMsg = "";
                    //总评审费
                    var totalReviewCost = v.reviewCost == undefined ? 0 : v.reviewCost;
                    //总税额
                    var totalReviwTaxes = v.reviewTaxes == undefined ? 0 : v.reviewTaxes;
                    //总合计
                    var totalCost = v.totalCost == undefined ? 0 : v.totalCost;

                    //计算每个专家
                    if (v.expertSelectedDtoList != undefined && v.expertSelectedDtoList.length > 0) {

                        var tempTotalReviewCost = 0;
                        var tempTotalReviwTaxes = 0;
                        var tempTotalCost = 0;

                        v.expertSelectedDtoList.forEach(function (expertSelected, i) {
                            //评审费用
                            var reviewCost = expertSelected.reviewCost == undefined ? 0 : expertSelected.reviewCost;
                            //税额
                            var reviewTaxes = expertSelected.reviewTaxes == undefined ? 0 : expertSelected.reviewTaxes;
                            //合计
                            var totalCost = expertSelected.totalCost == undefined ? 0 : expertSelected.totalCost;
                            var tempCost = parseFloat(reviewCost) + parseFloat(reviewTaxes);
                            if (tempCost.toFixed(2) != parseFloat(totalCost).toFixed(2)) {
                                isVilad = false;
                                return;
                            }
                            tempTotalReviewCost = parseFloat(tempTotalReviewCost) + parseFloat(reviewCost);
                            tempTotalReviwTaxes = parseFloat(tempTotalReviwTaxes) + parseFloat(reviewTaxes);
                            tempTotalCost = parseFloat(tempTotalCost) + parseFloat(totalCost);
                        });

                        if (parseFloat(tempTotalReviewCost).toFixed(2) != parseFloat(totalReviewCost).toFixed(2)) {
                            isVilad = false;
                            return;
                        }
                        if (parseFloat(tempTotalReviwTaxes).toFixed(2) != parseFloat(totalReviwTaxes).toFixed(2)) {
                            isVilad = false;
                            return;
                        }
                        if (parseFloat(tempTotalCost).toFixed(2) != parseFloat(totalCost).toFixed(2)) {
                            isVilad = false;
                            return;
                        }
                    }
                });
            }

            return isVilad;
        }// E_validateNum

        // S_gotopayment
        function gotopayment(vm) {
            vm.showExpertpayment = true;
            var WorkeWindow = $("#payment");
            WorkeWindow.kendoWindow({
                width: "1000px",
                height: "630px",
                title: "编辑-专家费用",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();

        }// E_gotopayment

    }
})();
