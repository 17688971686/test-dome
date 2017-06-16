(function () {
    'use strict';

    angular.module('app').controller('expertSelectCtrl', expertReview);

    expertReview.$inject = ['$location', 'expertReviewSvc', 'expertConditionSvc', '$state'];

    function expertReview($location, expertReviewSvc, expertConditionSvc, $state) {
        var vm = this;
        vm.title = '选择专家';
        vm.conditionIndex = 1;                //条件号
        vm.conditions = new Array();          //条件列表
        vm.expertReview = {};                 //评审方案对象

        vm.autoSelExperts = new Array();     //专家抽取结果
        vm.autoExpertList = {};              //随机待抽取专家列表
        vm.workProgramId = $state.params.workProgramId;

        activate();
        function activate() {
            expertReviewSvc.initReview(vm);
            expertReviewSvc.initExpertGrid(vm);
        }

        //弹出自选专家框
        vm.showSelfExpertGrid = function () {
            expertReviewSvc.initSelfExpert(vm);
        }

        //保存自选的专家
        vm.saveSelfExpert = function () {
            expertReviewSvc.saveSelfExpert(vm);
        }

        //删除自选专家
        vm.delertSelfExpert = function () {
            var isCheck = $("input[name='seletedEp']:checked");
            if (isCheck.length < 1) {
                common.alert({
                    vm: vm,
                    msg: "请选择操作对象"
                })
            } else {
                common.confirm({
                    vm: vm,
                    title: "温馨提示",
                    msg: "删除数据不可恢复，确定删除么？",
                    fn: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < isCheck.length; i++) {
                            ids.push(isCheck[i].value);
                        }
                        var idStr = ids.join(',');
                        expertReviewSvc.delSelectedExpert(vm, idStr);
                    }
                })
            }
        }

        //境外专家
        vm.showOutExpertGrid = function () {
            expertReviewSvc.showOutExpertGrid(vm);
        }

        //删除选择的境外专家
        vm.delertOutSelfExpert = function () {
            var isCheck = $("input[name='seletedOutEp']:checked");
            if (isCheck.length < 1) {
                common.alert({
                    vm: vm,
                    msg: "请选择操作对象"
                })
            } else {
                common.confirm({
                    vm: vm,
                    title: "温馨提示",
                    msg: "删除数据不可恢复，确定删除么？",
                    fn: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < isCheck.length; i++) {
                            ids.push(isCheck[i].value);
                        }
                        expertReviewSvc.delSelectedExpert(vm, ids.join(','));
                    }
                })
            }
        }

        //保存选择的境外专家
        vm.saveOutExpert = function () {
            expertReviewSvc.saveOutExpert(vm);
        }

        //计算符合条件的专家
        vm.countMatchExperts = function (sortIndex) {
            if (vm.expertReview.id) {
                expertReviewSvc.countMatchExperts(vm, sortIndex);
            } else {
                common.alert({
                    vm: vm,
                    msg: "请保存整体抽取方案再计算"
                });
            }
        }

        vm.checkIntegerValue = function (checkValue, idStr, idSort) {
            if (expertConditionSvc.isUnsignedInteger(checkValue)) {
                $("#" + idStr + idSort).val(checkValue);
                $("#errorsOfficialNum" + idSort).html("");
                $("#errorsAlternativeNum" + idSort).html("");
            } else {
                $("#errorsOfficialNum" + idSort).html("只能填写数字");
                $("#errorsAlternativeNum" + idSort).html("只能填写数字");
            }
        }

        //添加随机抽取条件
        vm.addCondition = function () {
            if (vm.expertReview.isSelete) {
                common.alert({
                    vm: vm,
                    msg: "当前项目已经进行整体专家方案的抽取，不能再修改方案！"
                })
            } else {
                vm.condition = {};
                vm.condition.sort = vm.conditionIndex;
                if (vm.expertReview.id) {
                    vm.condition.expertReviewDto = {};
                    vm.condition.expertReviewDto.id = vm.expertReview.id;   //抽取方案ID
                }
                vm.condition.selectType = "1";    //选择类型，这个一定不能少
                vm.conditions.push(vm.condition);
                vm.conditionIndex++;
            }
        }

        //删除专家抽取条件
        vm.removeCondition = function () {
            if (vm.expertReview.isSelete) {
                common.alert({
                    vm: vm,
                    msg: "当前项目已经进行整体专家方案的抽取，不能再修改方案！"
                })
            } else {
                var isCheck = $("#conditionTable input[name='epConditionSort']:checked");
                if (isCheck.length >= 1) {
                    common.confirm({
                        vm: vm,
                        title: "温馨提示",
                        msg: "删除数据不可恢复，确定删除么？",
                        fn: function () {
                            $('.confirmDialog').modal('hide');
                            var delIds = [];
                            for (var i = 0; i < isCheck.length; i++) {
                                vm.conditions.forEach(function (t, number) {
                                    if (isCheck[i].value == t.sort) {
                                        vm.conditions.splice(number, 1);
                                        if (t.id) {
                                            delIds.push(t.id);
                                        }
                                    }
                                });
                            }
                            if (delIds.length > 0) {
                                expertConditionSvc.deleteSelConditions(vm, delIds.join(","));
                            }
                        }
                    })
                }
            }
        }

        //检查是否为正整数
        function isUnsignedInteger(value) {
            if ((/^(\+|-)?\d+$/.test(value)) && value > 0) {
                return true;
            } else {
                return false;
            }
        }

        /******************************  以下是专家抽取方法 ***********************************/
        //封装专家抽取条件信息
        function buildCondition(checkId) {
            if (vm.conditions.length > 0) {
                var validateResult = true;
                vm.conditions.forEach(function (t, number) {
                    if (checkId) {
                        if (angular.isUndefined(t.id) || t.id == "") {
                            validateResult = false;
                        }
                    }
                    if (vm.expertReview.id) {
                        t.expertReviewDto = {};
                        t.expertReviewDto.id = vm.expertReview.id;   //抽取方案ID
                    }
                    t.workProgramId = vm.expertReview.workProgramId;
                    t.maJorBig = $("#maJorBig" + t.sort).val();
                    t.maJorSmall = $("#maJorSmall" + t.sort).val();
                    t.expeRttype = $("#expeRttype" + t.sort).val();
                    if ($("#officialNum" + t.sort).val() && isUnsignedInteger($("#officialNum" + t.sort).val())) {
                        t.officialNum = $("#officialNum" + t.sort).val();
                    } else {
                        $("#errorsOfficialNum" + t.sort).html("必填，且为数字");
                        validateResult = false;
                    }
                    if ($("#alternativeNum" + t.sort).val() && isUnsignedInteger($("#alternativeNum" + t.sort).val())) {
                        t.alternativeNum = $("#alternativeNum" + t.sort).val();
                    } else {
                        $("#errorsAlternativeNum" + t.sort).html("必填，且为数字");
                        validateResult = false;
                    }
                    if (validateResult) {
                        $("#errorsOfficialNum" + t.sort).html("");
                        $("#errorsAlternativeNum" + t.sort).html("");
                    }
                });
                return validateResult;
            } else {
                return false;
            }
        }

        //保存专家抽取条件
        vm.saveCondition = function () {
            if (vm.expertReview.isSelete) {
                common.alert({
                    vm: vm,
                    msg: "当前项目已经进行整体专家方案的抽取，不能再修改方案！"
                })
            } else {
                if (buildCondition(false)) {
                    expertConditionSvc.saveCondition(vm);
                } else {
                    common.alert({
                        vm: vm,
                        msg: "请先设置好条件再保存！",
                        closeDialog: true
                    })
                }
            }
        }

        //开始随机抽取
        vm.startAutoExpertWin = function () {
            if (buildCondition(true)) {
                if (vm.expertReview.isSelete == 9) {
                    common.alert({
                        vm: vm,
                        msg: "该方案已经进行整体专家方案的抽取，不能在继续抽取！"
                    })
                } else {
                    expertReviewSvc.queryAutoExpert(vm);
                }
            } else {
                common.alert({
                    vm: vm,
                    msg: "请先保存编辑的抽取方案！"
                })
            }
        }

        //显示随机抽取结果
        vm.showAutoExpertWin = function () {
            $("#aotuExpertDiv").kendoWindow({
                width: "800px",
                height: "600px",
                title: "专家抽取",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //再次抽取专家
        vm.resetAutoExpert = function () {
            common.alert({
                vm: vm,
                msg: "功能待开发！"
            })
        }

        //确认已抽取的专家
        vm.affirmAutoExpert = function () {
           /* var updteIdArr = new Array();
            vm.autoSelExperts.forEach(function (t, number) {
                updteIdArr.push(t.id);
            });
            expertReviewSvc.affirmAutoExpert(vm, updteIdArr.join(","))
            */

            expertReviewSvc.affirmAutoExpert(vm)
        }

        //确定实际参加会议的专家
        vm.affirmJoinExpert = function () {
            $("#confirmJoinExpert").kendoWindow({
                width: "960px",
                height: "600px",
                title: "参加评审会专家确认",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //未参加改为参加
        vm.updateToJoin = function () {
            var isCheck = $("#notJoinExpertTable input[name='notJoinExpert']:checked");
            if (isCheck.length < 1) {
                common.alert({
                    vm: vm,
                    msg: "请选择操作对象"
                })
            } else {
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    ids.push(isCheck[i].value);
                }
                expertReviewSvc.updateJoinState(vm, ids.join(','), '9');
            }
        }

        //参加改为未参加
        vm.updateToNotJoin = function () {
            var isCheck = $("#joinExpertTable input[name='joinExpert']:checked");
            if (isCheck.length < 1) {
                common.alert({
                    vm: vm,
                    msg: "请选择操作对象"
                })
            } else {
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    ids.push(isCheck[i].value);
                }
                expertReviewSvc.updateJoinState(vm, ids.join(','), '0');
            }
        }

        //确认已选的专家
        vm.affirmExpert = function () {
            var isCheck = $("input[name='seletedEp']:checked");
            if (isCheck.length < 1) {
                common.alert({
                    vm: vm,
                    msg: "请选择操作对象"
                })
            } else {
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    ids.push(isCheck[i].value);
                }
                var idStr = ids.join(',');
                expertReviewSvc.updateExpertState(vm, idStr, "9");
            }
        }
    }
})();
