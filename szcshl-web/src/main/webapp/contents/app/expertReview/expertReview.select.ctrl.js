(function () {
    'use strict';

    angular.module('app').controller('expertSelectCtrl', expertReview);

    expertReview.$inject = [ 'expertReviewSvc', 'expertConditionSvc', '$state','bsWin'];

    function expertReview(expertReviewSvc, expertConditionSvc, $state,bsWin) {
        var vm = this;
        vm.title = '选择专家';
        vm.conMaxIndex = 1;                   //条件号
        vm.conditions = new Array();         //条件列表
        vm.customCondition = new Array();
        vm.expertReview = {};                 //评审方案对象
        vm.confirmEPList = [];                //拟聘请专家列表（已经经过确认的专家）
        vm.matchEPMap = {};                  //保存符合条件的专家信息
        vm.selectIds = [],                    //已经抽取的专家信息ID（用于排除查询）
        vm.autoSelectedEPList = [];           //抽取结果列表，抽取方法在后面封装
        vm.workProgramId = $state.params.workProgramId;

        //刷新已经选择的专家信息
        vm.reFleshSelEPInfo = function(explist) {
            $.each(explist,function(i, obj){
                vm.confirmEPList.push(obj);
                vm.selectIds.push(obj.expertDto.expertID);
            })
            vm.excludeIds = vm.selectIds.join(',');
        }

        //删除后刷新
        vm.reFleshAfterRemove = function(ids){
            $.each(ids,function(i, obj){
                //1、删除已确认的专家
                $.each(vm.confirmEPList,function(index, epObj){
                    if(obj == epObj.id){
                        vm.confirmEPList.splice(index, 1);
                    }
                })
            })
        }

        //更新参加未参加状态
        vm.reFleshJoinState = function(ids,state){
            $.each(ids,function(i, obj){
                //1、删除已确认的专家
                $.each(vm.confirmEPList,function(index, epObj){
                    if(obj == epObj.id){
                        epObj.isJoin = state;
                    }
                })
            })
        }

        //更新是否确认状态
        vm.reFleshJoinState = function(ids,state){
            $.each(ids,function(i, obj){
                //1、删除已确认的专家
                $.each(vm.confirmEPList,function(index, epObj){
                    if(obj == epObj.id){
                        epObj.isConfrim = state;
                    }
                })
            })
        }

        //更新抽取条件的抽取次数
        vm.updateSelectedIndex = function(sort){
            if(sort){
                $.each(vm.conditions,function(i,con){
                    if(con.sort == sort){
                        con.selectIndex = (!con.selectIndex)?1:con.selectIndex+1;
                    }
                })
            }else{
                $.each(vm.conditions,function(i,con){
                    con.selectIndex = (!con.selectIndex)?1:con.selectIndex+1;
                })
            }
        }

        vm.init = function(workProgramId){
            expertReviewSvc.initReview(workProgramId,function(data){
                vm.expertReview = data;
                //专家抽取条件
                if (!angular.isUndefined(vm.expertReview.expertSelConditionDtoList) && angular.isArray(vm.expertReview.expertSelConditionDtoList)) {
                    vm.conditions = vm.expertReview.expertSelConditionDtoList;
                    $.each(vm.conditions,function(i,scdObj){
                        if(scdObj.sort > vm.conMaxIndex){
                            vm.conMaxIndex = scdObj.sort;
                        }
                    });
                }
                //获取已经抽取的专家
                if (!angular.isUndefined(vm.expertReview.expertSelectedDtoList) && angular.isArray(vm.expertReview.expertSelectedDtoList)) {
                    $.each(vm.expertReview.expertSelectedDtoList,function(i, sep){
                        vm.selectIds.push(sep.expertDto.expertID);
                        vm.confirmEPList.push(sep);
                    })
                    if (vm.selectIds.length > 0) {
                        vm.excludeIds = vm.selectIds.join(',');
                    } else {
                        vm.excludeIds = '';
                    }
                }

            });
        }

        activate();
        function activate() {
            expertReviewSvc.initExpertGrid(vm);
            vm.init(vm.workProgramId);
        }

        //弹出自选专家框
        vm.showSelfExpertGrid = function () {
            vm.selfExpertOptions.dataSource.read();
            $("#selfExpertDiv").kendoWindow({
                width: "860px",
                height: "500px",
                title: "自选评审专家",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //保存自选的专家
        vm.saveSelfExpert = function () {
            var selectIds = common.getKendoCheckId('#selfExpertGrid');
            if (selectIds.length == 0) {
                bsWin.alert("请先选择专家！");
            } else if (selectIds.length > 1) {
                bsWin.alert("自选专家最多只能选择一个！");
            }else{
                expertReviewSvc.saveSelfExpert(vm.workProgramId,selectIds[0].value, vm.expertReview.id,vm.isCommit,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        if(!vm.expertReview.id){
                            vm.expertReview.id = data.idCode;
                        }
                        //刷新
                        vm.reFleshSelEPInfo(data.reObj);
                        bsWin.success("操作成功！",function(){
                            window.parent.$("#selfExpertDiv").data("kendoWindow").close();
                        });
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }
        }

        //删除自选专家
        vm.delertSelfExpert = function () {
            var isCheck = $("input[name='seletedEp']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择要删除的专家");
            } else {
                bsWin.confirm({
                    title: "询问提示",
                    message: "删除数据不可恢复，确定删除么？",
                    onOk: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < isCheck.length; i++) {
                            ids.push(isCheck[i].value);
                        }
                        expertReviewSvc.delSelectedExpert(vm.expertReview.id, ids.join(','),vm.isCommit,function(data){
                            if(data.flag || data.reCode == 'ok'){
                                vm.reFleshAfterRemove(ids);
                                bsWin.success("操作成功！");
                            }else{
                                bsWin.error(data.reMsg);
                            }
                        });
                    },
                });
            }
        }

        //境外专家
        vm.showOutExpertGrid = function () {
            vm.outExpertOptions.dataSource.read();
            $("#outExpertDiv").kendoWindow({
                width: "860px",
                height: "500px",
                title: "自选新专家、市外、境外专家",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //删除选择的境外专家
        vm.delertOutSelfExpert = function () {
            var isCheck = $("input[name='seletedOutEp']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择要删除的专家");
            } else {
                bsWin.confirm({
                    title: "询问提示",
                    message: "删除数据不可恢复，确定删除么？",
                    onOk: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < isCheck.length; i++) {
                            ids.push(isCheck[i].value);
                        }
                        expertReviewSvc.delSelectedExpert(vm.expertReview.id, ids.join(','),vm.isCommit,function(data){
                            if(data.flag || data.reCode == 'ok'){
                                vm.reFleshAfterRemove(ids);
                                bsWin.success("操作成功！");
                            }else{
                                bsWin.error(data.reMsg);
                            }
                        });
                    },
                });
            }
        }

        //保存选择的境外专家
        vm.saveOutExpert = function () {
            var selectIds = common.getKendoCheckId('#outExpertGrid');
            if (selectIds.length == 0) {
                bsWin.alert("请先选择专家！");
                //$("#outExpertError").html("请选择一条专家数据才能保存！");
            } else {
                var selExpertIdArr = [];
                $.each(selectIds, function (i, obj) {
                    selExpertIdArr.push(obj.value);
                });
                expertReviewSvc.saveOutExpert(vm.workProgramId,selExpertIdArr.join(","), vm.expertReview.id, vm.isCommit, function (data) {
                    if(data.flag || data.reCode == 'ok'){
                        if(!vm.expertReview.id){
                            vm.expertReview.id = data.idCode;
                        }
                        vm.reFleshSelEPInfo(data.reObj);

                        bsWin.success("操作成功！",function(){
                            window.parent.$("#outExpertDiv").data("kendoWindow").close();
                        });
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }
        }

        //计算符合条件的专家
        vm.countMatchExperts = function (sortIndex) {
            if (vm.expertReview.id) {
                var postData = {};
                vm.conditions.forEach(function (t, number) {
                    if (t.sort == sortIndex) {
                        postData = t;
                        postData.maJorBig = $("#maJorBig" + t.sort).val();
                        postData.maJorSmall = $("#maJorSmall" + t.sort).val();
                        postData.expeRttype = $("#expeRttype" + t.sort).val();
                    }
                });
                postData.expertReviewDto = {};
                postData.expertReviewDto.id = vm.expertReview.id;   //抽取方案ID

                expertReviewSvc.countMatchExperts(postData,vm.workProgramId,vm.expertReview.id,function(data){
                    vm.matchEPMap[sortIndex] = data;
                    $("#expertCount" + sortIndex).html(data.length);
                });
            } else {
                bsWin.alert("请保存整体抽取方案再计算");
            }
        }

        //查看符合条件的专家信息
        vm.showMatchExperts = function(sortIndex){
            vm.matchExpertList = [];
            vm.matchExpertList = vm.matchEPMap[sortIndex];
            $("#matchExpertDiv").kendoWindow({
                width: "800px",
                height: "500px",
                title: "专家信息",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
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
            if (vm.expertReview.isComfireResult == '9' || vm.expertReview.isComfireResult == 9) {
                bsWin.alert("当前项目已经进行整体专家方案的抽取，不能再修改方案！");
            } else {
                vm.condition = {};
                vm.condition.sort = vm.conMaxIndex+1;
                if (vm.expertReview.id) {
                    vm.condition.expertReviewDto = {};
                    vm.condition.expertReviewDto.id = vm.expertReview.id;   //抽取方案ID
                }
                vm.condition.selectType = "1";    //选择类型，这个一定不能少
                vm.conditions.push(vm.condition);
                vm.conMaxIndex++;
            }
        }

        //删除专家抽取条件
        vm.removeCondition = function () {
            if (vm.expertReview.isComfireResult == 9 || vm.expertReview.isComfireResult == '9' || vm.expertReview.selCount > 0 ) {
                bsWin.alert("当前项目已经进行整体专家方案的抽取，不能再修改方案！");
            } else {
                var isCheck = $("#conditionTable input[name='epConditionSort']:checked");
                if (isCheck.length > 0) {
                    bsWin.confirm({
                        title: "询问提示",
                        message: "删除数据不可恢复，确定删除么？",
                        onOk: function () {
                            $('.confirmDialog').modal('hide');
                            var ids = [];
                            for (var i = 0; i < isCheck.length; i++) {
                                $.each(vm.conditions,function(c,con){
                                    if (isCheck[i].value == con.sort) {
                                        if (con.id) {
                                            ids.push(con.id);
                                        }else{
                                            vm.conditions.splice(c, 1);     //没有保存抽取条件的直接删除
                                        }
                                    }
                                })
                            }
                            if(ids.length > 0){
                                expertConditionSvc.deleteSelConditions(ids.join(","),vm.isCommit,function(data){
                                    if(data.flag || data.reCode == 'ok'){
                                        bsWin.success("操作成功！");
                                        $.each(ids,function(i,id){
                                            $.each(vm.conditions,function(c,con){
                                                if (id == con.sort) {
                                                    vm.conditions.splice(c, 1);     //没有保存抽取条件的直接删除
                                                }
                                            })
                                        })
                                    }else{
                                        bsWin.error(data.reMsg);
                                    }
                                });
                            }else{
                                bsWin.success("操作成功！");
                            }
                        },
                    });
                }else{
                    bsWin.alert("请选择要删除的抽取条件！");
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
            if (vm.expertReview.isComfireResult == '9') {
                bsWin.alert("当前项目已经进行整体专家方案的抽取，不能再修改方案！");
            }else {
                if (buildCondition(false)) {
                    expertConditionSvc.saveCondition(vm.workProgramId,vm.conditions,function(data){
                        if(data.flag || data.reCode == 'ok'){
                            vm.conditions = data.reObj;
                            if(!vm.expertReview.id){
                                vm.expertReview.id = vm.conditions[0].expertReviewId;
                            }
                            //抽取方案ID
                            $.each(vm.conditions, function (i, obj) {
                                obj.expertReviewDto = {};
                                obj.expertReviewDto.id = vm.expertReview.id;
                            });
                            bsWin.success("保存成功！");
                        }else{
                            bsWin.error(data.reMsg);
                        }
                    });
                } else {
                    bsWin.alert("专家抽取条件设置不完整！");
                }
            }
        }

        //（整体方案抽取）开始随机抽取
        vm.startAutoExpertWin = function () {
            if (buildCondition(true)) {
                if(vm.expertReview.selCount > 0){
                    bsWin.alert("您已经进行整体专家抽取，不能再进行整体方案的抽取！");
                    return ;
                }
                if (vm.expertReview.isComfireResult == 9 || vm.expertReview.isComfireResult == '9' || vm.expertReview.selCount > 0) {
                    bsWin.alert("该方案已经进行整体专家方案的抽取，不能在继续抽取！");
                } else {
                    expertReviewSvc.queryAutoExpert(vm.conditions,vm.workProgramId,vm.expertReview.id,function(data){
                        if(data.flag || data.reCode == 'ok'){
                            //刷新页面抽取的专家
                            vm.reFleshSelEPInfo(data.reObj.autoEPList);
                            //抽取次数加一
                            vm.expertReview.selCount = data.reObj.selCount;
                            //抽取结果数组
                            vm.autoSelectedEPList = [];
                            vm.autoSelectedEPList = data.reObj.autoEPList;
                            //刷新抽取次数
                            vm.updateSelectedIndex();
                            //弹框
                            vm.showAutoExpertWin();
                            //显示抽取效果
                            expertReviewSvc.validateAutoExpert(data.reObj.allEPList,vm);
                        }else{
                            bsWin.error(data.reMsg);
                        }
                    });
                }
            } else {
                bsWin.alert("请先保存编辑的抽取方案！");
            }
        }

        //显示随机抽取框
        vm.showAutoExpertWin = function () {
            $("#aotuExpertDiv").kendoWindow({
                width: "1024px",
                height: "600px",
                title: "专家抽取",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //显示随机抽取结果
        vm.showAutoMatchResultWin = function () {
            $("#aotuMatchResultDiv").kendoWindow({
                width: "1024px",
                height: "500px",
                title: "专家抽取结果",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //再次抽取专家
        vm.repeatAutoExpert = function(conSort) {
            var condition = [];
            $.each(vm.conditions,function(i,con){
                if(con.sort == conSort){
                    condition.push(con);
                }
            })
            if(condition[0].selectIndex > 3){
                bsWin.alert("该条件已经进行了3次抽取，不能再继续抽取！");
                return ;
            }
            expertReviewSvc.queryAutoExpert(condition,vm.workProgramId,vm.expertReview.id,function(data){
                if(data.flag || data.reCode == 'ok'){
                    //刷新页面抽取的专家
                    vm.reFleshSelEPInfo(data.reObj.autoEPList);
                    //抽取次数加一
                    vm.expertReview.selCount = data.reObj.selCount;
                    //抽取结果数组
                    vm.autoSelectedEPList = [];
                    vm.autoSelectedEPList = data.reObj.autoEPList;
                    //刷新抽取次数
                    vm.updateSelectedIndex(conSort);
                    //弹框
                    vm.showAutoExpertWin();
                    //显示抽取效果
                    expertReviewSvc.validateAutoExpert(data.reObj.allEPList,vm);
                }else{
                    bsWin.error(data.reMsg);
                }
            });
        }

        //确认已抽取的专家
        vm.affirmAutoExpert = function () {
            var isCheck = $("#allAutoEPTable input[name='autoEPCheck']:checked");
            if(isCheck.length < 1){
                bsWin.alert("请选择要确认的抽取专家！");
                return ;
            }
            var ids = [];
            for (var i = 0; i < isCheck.length; i++) {
                ids.push(isCheck[i].value);
            }
            expertReviewSvc.affirmAutoExpert(vm.expertReview.id,ids.join(","),'9',function(data){
                bsWin.success("操作成功");
                vm.reFleshJoinState(ids,"9");
            })

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
                bsWin.alert("请选择要改为参加会议的专家");
            } else {
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    ids.push(isCheck[i].value);
                }
                expertReviewSvc.updateJoinState( ids.join(','), '9',vm.isCommit,function(data){
                    bsWin.success("操作成功！");
                    vm.reFleshJoinState(ids,'9');
                });
            }
        }

        //参加改为未参加
        vm.updateToNotJoin = function () {
            var isCheck = $("#joinExpertTable input[name='joinExpert']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择未参加会议的专家");
            } else {
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    ids.push(isCheck[i].value);
                }
                expertReviewSvc.updateJoinState( ids.join(','), '0',vm.isCommit,function(data){
                    bsWin.success("操作成功！");
                    vm.reFleshJoinState(ids,'0');
                });
            }
        }
    }
})();
