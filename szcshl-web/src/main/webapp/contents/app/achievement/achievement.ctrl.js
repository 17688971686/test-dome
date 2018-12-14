(function () {
    'use strict';

    angular.module('app').controller('achievementListCtrl', achievementList);

    achievementList.$inject = ['$location', 'achievementSvc','$state','$http','bsWin'];

    function achievementList($location, achievementSvc,$state,$http,bsWin) {
        var vm = this;
        vm.title = '业绩统计';
        vm.model={};
        vm.mainDoc = {};
        vm.assistDoc = {};
        vm.model.year = $state.params.year;
        vm.model.quarter = $state.params.quarter;
        vm.conMaxIndex = 0;        //条件号
        vm.conditions = [];        //条件列表
        activate(true);
        function activate(firstLoad) {
            achievementSvc.achievementSum(vm,function (data) {
                if(data){
                    var level = data.level;
                    if(level == 0){
                        //普通用户
                        vm.orgAchievement =  data.userSum;
                        vm.userId = vm.orgAchievement.userId;
                        achievementSvc.findTopicDetail(vm.userId,function(data){
                            if(data != undefined){
                                vm.conditions = data;
                                for(var i=0;i<vm.conditions.length;i++){
                                    vm.conditions[i]["sort"]= (i+1);
                                }
                            }
                        });
                    }else if(level == 1 || level == 2){
                        //如果是主任或者副主任
                        vm.comprehensive = data.orgDeptCount["综合部"];
                        vm.evaluateOne = data.orgDeptCount["评估一部"];
                        vm.evaluateTwo = data.orgDeptCount["评估二部"];
                        vm.budgetaryOne = data.orgDeptCount["概算一部"];
                        vm.budgetaryTwo = data.orgDeptCount["概算二部"];
                        vm.evaluateOneGroup = data.orgDeptCount["评估一部信息化组"];

                        //部门员工业绩统计明细
                        vm.achievementDeptDetailList = data.orgDeptDetailList;
                    }else if(level == 3 || level == 4){
                        //部长或者组长
                        vm.orgAchievement =  data.orgDeptSum;
                    }

                    vm.level = data.level;
                    //分管的部门列表
                    if(firstLoad){
                        vm.orgDeptList = data.orgDeptList;
                        if(vm.orgDeptList ){
                            vm.model.deptNames = "";
                            for(var i=0,l=vm.orgDeptList.length;i<l;i++){
                                if(i > 0){
                                    vm.model.deptNames += ",";
                                }
                                vm.model.deptNames += vm.orgDeptList[i].name;
                            }
                        }
                    }
                }
            })
        }

        /**
         * 主办人评审项目一览表
         */
        vm.showMainDocDetail = function () {
            $("#mainDocDetail").kendoWindow({
                width: "80%",
                height: "680px",
                title: "主办人评审项目一览表",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        /**
         * 协办人评审项目一览表
         */
        vm.showAssistDocDetail = function () {
            $("#assistDocDetail").kendoWindow({
                width: "80%",
                height: "680px",
                title: "协办人评审项目一览表",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        vm.countAchievementDetail = function () {
            if(vm.level !=0){
                //初始化部门工作业绩
                var orgCheck = $("input[name='orgDept']:checked");
                var ids = [];
                $.each(orgCheck, function (i, obj) {
                    ids.push(obj.value);
                });
                vm.model.deptIds = ids.join(",");
                //直接调用查询
                activate(false);
            }
        }

        /**
         * 课题业务
         */
        vm.showTopicDetail = function () {
            $("#topicDetail").kendoWindow({
                width: "80%",
                height: "680px",
                title: "课题研究及其他业务工作一览表",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }

        //添加课题业务信息
        vm.addTopic = function () {
            vm.condition = {};
            if(vm.conditions.length == 0){
                vm.condition.sort = vm.conMaxIndex+1;
                vm.condition.endTime = new Date();
            }else{
                vm.conMaxIndex = vm.conditions.length;
                vm.condition.sort = vm.conditions.length+1;
                vm.condition.endTime = new Date();
            }
            vm.conditions.push(vm.condition);
            vm.conMaxIndex++;
        }

        //保存课题业务信息
        vm.saveTopic = function () {
            if (buildCondition()) {
                achievementSvc.saveTopicDetailList(vm.conditions,function(data){
                    if(data.flag || data.reCode == 'ok'){
                        vm.conditions = data.reObj;
                        for(var i=0;i<vm.conditions.length;i++){
                            vm.conditions[i]["sort"]= (i+1);
                        }
                        bsWin.success("保存成功！");
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }
        }

        //删除课题业务信息
        vm.removeTopic = function () {
            var isCheck = $("#topicform input[name='topicInfo']:checked");
            if (isCheck.length > 0) {
                bsWin.confirm({
                    title: "询问提示",
                    message: "删除数据不可恢复，确定删除么？",
                    onOk: function () {
                        $('.confirmDialog').modal('hide');
                        var ids = [];
                        for (var i = 0; i < isCheck.length; i++) {
                            for(var k = 0; k < vm.conditions.length; k++){
                                var con = vm.conditions[k];
                                if (isCheck[i].value == con.sort) {
                                    if (con.id) {
                                        ids.push(con.id);
                                    }else{
                                        vm.conditions.splice(k, 1);
                                        $("#conTr"+con.sort).remove();
                                        break;
                                    }
                                }
                            }
                        }
                        if(ids.length > 0){
                            achievementSvc.deleteTopicMaintainDel(ids.join(","),function(data){
                                if(data.flag || data.reCode == 'ok'){
                                    bsWin.success("操作成功！");
                                    for(var i = 0; i < ids.length; i++ ){
                                        for(var k = 0; k < vm.conditions.length; k++){
                                            var con = vm.conditions[k];
                                            if (ids[i] == con.id) {
                                                vm.conditions.splice(k, 1);
                                                $("#conTr"+con.sort).remove();
                                                break;
                                            }
                                        }
                                    }
                                }else{
                                    bsWin.error(data.reMsg);
                                }
                            });
                        }
                    },
                });
            }else{
                bsWin.alert("请选择要删除的合同信息！");
            }
        }

        function buildCondition() {
            common.initJqValidation($('#topicform'));
            var isValid = $('#topicform').valid();
            if (isValid) {
                if (vm.conditions.length > 0) {
                    var validateResult = true;
                    vm.conditions.forEach(function (p, number) {
                        p.businessType = $("#businessType" + p.sort).val();
                        p.topicName = $("#topicName" + p.sort).val();
                        p.endTime = $("#endTime" + p.sort).val();
                    });
                    return validateResult;
                } else {
                    bsWin.error("没有分录数据，无法保存！");
                    return false;
                }
            }else{
                bsWin.alert("页面未填报完整或者为正确，请检查！");
                return false;
            }
        }

        /**
         * 部门或者个人的业绩统计明细
         * @param userId
         */
        vm.showAchievementDetail = function (id,level,orgName,userName) {
            console.log(level+"==="+orgName+"===="+userName);
            vm.achievementDetail = {};
            achievementSvc.findAchievementDetail(vm.model,id,level,function(data){
                vm.showDetailType = level;
                vm.showOrgName = orgName;
                vm.showUserName = userName;
                if(level==3){
                    vm.achievementDetail = data.orgDeptSum;
                    console.log(vm.achievementDetail);
                    $("#achievementDetail").kendoWindow({
                        width: "75%",
                        height: "500px",
                        title: "评审中心员工工作业绩统计表",
                        visible: false,
                        modal: true,
                        closable: true,
                        actions: ["Pin", "Minimize", "Maximize", "Close"]
                    }).data("kendoWindow").center().open();

                }else if(level == 0){
                    vm.achievementDetail = data.userSum;
                    //个人业绩统计信息还包括课题信息
                    achievementSvc.findTopicDetail(id,function(data){
                        if(data != undefined){
                            vm.conditions = data;
                            for(var i=0;i<vm.conditions.length;i++){
                                vm.conditions[i]["sort"]= (i+1);
                            }
                            $("#achievementDetail").kendoWindow({
                                width: "75%",
                                height: "700px",
                                title: "评审中心员工工作业绩统计表",
                                visible: false,
                                modal: true,
                                closable: true,
                                actions: ["Pin", "Minimize", "Maximize", "Close"]
                            }).data("kendoWindow").center().open();
                        }
                    });
                }
            });


        }

        /**
         * 部门业绩明细
         */
        /*vm.showAchievementDetail = function () {
            //初始化部门工作业绩
            var orgCheck = $("input[name='orgDept']:checked");
            var ids = [];
            var deptNamesArr = [];
            $.each(orgCheck, function (i, obj) {
                ids.push(obj.value);
                var objR = $(obj);
                deptNamesArr.push(objR.attr("tit"));
            });
            vm.model.deptIds = ids.join(",");
            vm.model.deptNames = deptNamesArr.join(",");
            achievementSvc.achievementDeptDetail(vm,function (data) {
                if(data.flag || data.reCode == 'ok'){
                    vm.achievementDeptDetailList = data.reObj.achievementDeptDetailList;
                    $("#achievementDeptDetail").kendoWindow({
                        width: "80%",
                        height: "680px",
                        title: "部门工作业绩统计表",
                        visible: false,
                        modal: true,
                        closable: true,
                        actions: ["Pin", "Minimize", "Maximize", "Close"]
                    }).data("kendoWindow").center().open();
                }
            })
        }*/

        /***
         * 初始化业绩汇总
         */
        vm.initAchievementSum = function ($event) {
            //初始化部门工作业绩
            var orgCheck = $("input[name='orgDept']:checked");
            var orgIds = [];
            var deptNamesArr = [];
            $.each(orgCheck, function (i, obj) {
                orgIds.push(obj.value);
                var objR = $(obj);
                deptNamesArr.push(objR.attr("tit"));

            });
            if(orgIds.length == 0){
                var checkbox = $event.target;
                bsWin.alert("至少需要选择一个部门！");
                checkbox.checked = true;
                return;
            }
            vm.model.deptIds = orgIds.join(",");
            vm.model.deptNames = deptNamesArr.join(",");
            activate(false);
        }

        /**
         * 员工业绩汇总导出
         */
        vm.exportAchievementDetail = function () {
            achievementSvc.exportAchievementDetail(vm)
        }

        /**
         * 部门业绩汇总导出
         */
        vm.exportDeptAchievementDetail = function () {
            //设置部门名字
            achievementSvc.exportDeptAchievementDetail(vm);

        }

        /**
         * 课题维护导出
         */
        vm.exportTopicMaintainInfo = function () {
            achievementSvc.exportTopicMaintainInfo(vm);
        }

        /**
         * 主/协办项目一览表
         */
        vm.exportProReview = function (isMainPro) {
            vm.model.isMainPro = isMainPro;
            achievementSvc.exportProReview(vm);
        }

        /**
         * 统计统计的部门是否包含校验的部门
         * @param deptNames
         * @param checkDeptName
         * @returns {boolean}
         */
        vm.checkIsHaveDept = function(deptNames,checkDeptName){
            var deptNameArr = deptNames.split(",");
            for(var i=0,l=deptNameArr.length;i<l;i++){
                if(deptNameArr[i] == checkDeptName){
                    return true;
                }
            }
            return false;
        }
    }
})();
