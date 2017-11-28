(function () {
    'use strict';

    angular.module('app').controller('assistPlanCtrl', assistPlan);

    assistPlan.$inject = ['$location', '$state', 'assistSvc',  '$interval', 'bsWin', 'ideaSvc'];

    function assistPlan($location, $state, assistSvc, $interval, bsWin, ideaSvc) {
        var vm = this;
        vm.model = {};							//创建一个form对象
        vm.filterModel = {};                    //filter对象
        vm.filterLow = {};
        vm.title = '协审计划管理';        		//标题
        vm.splitNumArr = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11,12,13,14,15];
        vm.plan = {};                           //添加的协审对象
        vm.plan.assistType = "独立项目";        //默认的协审类型
        vm.planList = [];                       //在办协审计划列表
        vm.assistSign = [];                     //待选项目列表
        vm.pickSign = [];                       //协审计划已选的项目列表
        vm.pickMainSign =[];                    //主项目对象
        vm.lowerSign = [];                      //次项目对象
        vm.showPlanId = "";                     //显示的协审计划ID

        vm.assistPlan = {};                      //弹窗协审计划对象
        var weekArray = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];

        /**
         * 刷新页面数据
         * @param isOnlySign
         */
        vm.refleshPageInfo = function(isOnlySign){
            vm.assistSign = [];
            assistSvc.initPlanPage(isOnlySign,function(data){
                vm.assistSign = data.signList;
                //刷新协审计划信息
                vm.refleshPlanInfo(vm.plan.id);
            });
        }

        /**
         * 刷新协审计划信息
         */
        vm.refleshPlanInfo = function(planId){
            //查询对应的协审项目信息
            assistSvc.findPlanSign(planId,vm.isCommit,function(data) {
                vm.pickMainSign = [];                   //主项目对象全部清空
                vm.lowerSign = [];                      //次项目对象
                vm.plan = data.planDto;
                //合并评审，还要挑选出主项目和次项目
                if(vm.plan.assistType == '合并项目'){
                    angular.forEach(vm.plan.assistPlanSignDtoList,function(obj,i){
                        if(obj.isMain == '9'||obj.isMain == 9){
                            angular.forEach(data.signList,function(sObj,index){
                                if(obj.signId == sObj.signid){
                                    vm.pickMainSign.push(sObj);
                                }else{
                                    vm.lowerSign.push(sObj);
                                }
                            })
                        }
                    })
                }else{
                    vm.pickMainSign = data.signList;
                }
            });
        }

        active();
        function active() {
            assistSvc.initPlanPage(false,function(data){
                if (data.signList && data.signList.length > 0) {
                    vm.assistSign = data.signList;
                }
                if (data.planList && data.planList.length > 0) {
                    vm.planList = data.planList;
                }
            });
            assistSvc.initPlanGrid(vm);
            $('#planInfo li').click(function (e) {
                var aObj = $("a", this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#" + showDiv).addClass("active").addClass("in").show(500);
            })
        }

        //待选择过来器
        vm.filterSign = function (item) {
            var isMatch = true;
            if (!angular.isUndefined(item)) {
                if (!angular.isUndefined(vm.filterModel.filterFilecode)) {
                    if ((item.filecode).indexOf(vm.filterModel.filterFilecode) == -1) {
                        isMatch = false;
                    }
                }
                if (isMatch) {
                    if (!angular.isUndefined(vm.filterModel.filterProjectCode)) {
                        if ((item.projectcode).indexOf(vm.filterModel.filterProjectCode) == -1) {
                            isMatch = false;
                        }
                    }
                }
                if (isMatch) {
                    if (!angular.isUndefined(vm.filterModel.filterProjectName)) {
                        if ((item.projectname).indexOf(vm.filterModel.filterProjectName) == -1) {
                            isMatch = false;
                        }
                    }
                }
                if (isMatch) {
                    if (!angular.isUndefined(vm.filterModel.filterBuiltName)) {
                        if (angular.isUndefined(item.builtcompanyName)) {
                            isMatch = false;
                        }
                        if (isMatch && (item.builtcompanyName).indexOf(vm.filterModel.filterBuiltName) == -1) {
                            isMatch = false;
                        }
                    }
                }
                if (isMatch) {
                    return item;
                }
            }
        }

        //次项目待选择器
        vm.filterLowSign = function (item) {
            var isMatch = true;
            if (!angular.isUndefined(item)) {
                if (!angular.isUndefined(vm.filterLow.filterFilecode)) {
                    if ((item.filecode).indexOf(vm.filterLow.filterFilecode) == -1) {
                        isMatch = false;
                    }
                }
                if (isMatch) {
                    if (!angular.isUndefined(vm.filterLow.filterProjectCode)) {
                        if ((item.projectcode).indexOf(vm.filterLow.filterProjectCode) == -1) {
                            isMatch = false;
                        }
                    }
                }
                if (isMatch) {
                    if (!angular.isUndefined(vm.filterLow.filterProjectName)) {
                        if ((item.projectname).indexOf(vm.filterLow.filterProjectName) == -1) {
                            isMatch = false;
                        }
                    }
                }
                if (isMatch) {
                    if (!angular.isUndefined(vm.filterLow.filterBuiltName)) {
                        if (angular.isUndefined(item.builtcompanyName)) {
                            isMatch = false;
                        }
                        if (isMatch && (item.builtcompanyName).indexOf(vm.filterLow.filterBuiltName) == -1) {
                            isMatch = false;
                        }
                    }
                }
                if (isMatch) {
                    return item;
                }
            }
        }


        //重置拆分值
        vm.initSplit = function (typeName) {
            if (vm.plan.assistType == typeName) {
                if (!angular.isUndefined(vm.plan.spliNum)) {
                    vm.plan.spliNum = 0;
                }
            }
        }

        //挑选项目
        vm.affirmSign = function () {
            var isCheckSign = $("input[name='selASTSign']:checked");
            if (isCheckSign.length < 1) {
                bsWin.alert("请选择要挑选的项目");
            } else {
                if (isCheckSign.length > 1) {
                    if (vm.plan.assistType == '合并项目') {
                        bsWin.alert("合并项目要先挑选一个主项目，再挑选次项目！");
                    } else {
                        bsWin.alert("独立项目，每次只能选择一个！");
                    }
                } else {
                    var saveSignId = isCheckSign[0].value;
                    vm.plan.signId = saveSignId;
                    /**
                     * 保存协审计划信息
                     */
                    assistSvc.saveAssistPlan(vm.plan, vm.isCommit, function (data) {
                        vm.isCommit = false;
                        if(data.flag || date.reCode=='ok'){
                            //如果是新增，则重新刷新列表
                            if (!vm.plan.id) {
                                vm.gridOptions.dataSource.read();
                            }
                            vm.plan = data.reObj;
                            assistSvc.initPlanPage(true,function(data) {
                                if (data.signList && data.signList.length > 0) {
                                    vm.assistSign = data.signList;
                                }
                            });
                            vm.planList.push(vm.plan);
                            vm.showPickLowSign(vm.plan.id);
                        }else{
                            bsWin.alert(data.reMsg);
                        }
                    });
                }
            }
        }

        //取消
        vm.cancelSign = function () {
            var isCheckSign = $("input[name='checkASTSign']:checked");
            if (isCheckSign.length < 1) {
                bsWin.alert("请选择取消的项目");
            } else {
                bsWin.confirm({
                    title: "询问提示",
                    message: "确认取消挑选项目吗?",
                    onOk: function () {
                        var ids = [];
                        for (var i = 0; i < isCheckSign.length; i++) {
                            ids.push(isCheckSign[i].value);
                        }
                        assistSvc.cancelPlanSign(vm, ids.join(','),function(data){
                            vm.isCommit = false;
                            vm.refleshPageInfo(true);
                            bsWin.alert("操作成功！");
                        });
                    }
                });
            }
        }

        //初始化选择的协审计划信息
        vm.initSelPlan = function () {
            vm.pickMainSign = [];                   //主项目对象全部清空
            vm.lowerSign = [];                      //次项目对象
            if(vm.showPlanId){
                //查询对应的协审项目信息
                assistSvc.findPlanSign(vm.showPlanId,vm.isCommit,function(data) {
                    vm.plan = data.planDto;
                    vm.refleshPlanInfo(vm.showPlanId);
                });
            }else{
                vm.plan = {};
            }
        }
        //删除操作
        vm.doDelete = function () {
            if (vm.plan.id) {
                bsWin.confirm({
                    title: "询问提示",
                    message: "确认删除吗？删除数据不可恢复，请慎重！",
                    onOk: function () {
                        assistSvc.deletePlan(vm.plan.id, vm.isCommit, function(data) {
                            assistSvc.initPlanPage(true,function(data) {
                                if (data.signList && data.signList.length > 0) {
                                    vm.assistSign = data.signList;
                                }
                            });
                            vm.isCommit = false;
                            vm.pickMainSign = [];                   //主项目对象全部清空
                            vm.lowerSign = [];                      //次项目对象
                            angular.forEach(vm.planList,function(pObj,i){
                                if(pObj.id ==vm.plan.id){
                                    vm.planList.splice(pObj, 1);
                                }
                            })
                            vm.plan = {};
                            //刷新列表信息
                            vm.gridOptions.dataSource.read();
                            bsWin.alert("操作成功！");
                        });
                    }
                });
            } else {
                bsWin.alert("请选择要删除的协审计划包");
            }
        }

        vm.showPickLowSign = function(planId){
            //查询对应的协审项目信息
            assistSvc.findPlanSign(planId,vm.isCommit,function(data) {
                vm.pickMainSign = [];                   //主项目对象全部清空
                vm.lowerSign = [];                      //次项目对象
                vm.plan = data.planDto;
                //合并评审，还要挑选出主项目和次项目
                if(vm.plan.assistType == '合并项目'){
                    angular.forEach(vm.plan.assistPlanSignDtoList,function(obj,i){
                        if(obj.isMain == '9'||obj.isMain == 9){
                            angular.forEach(data.signList,function(sObj,index){
                                if(obj.signId == sObj.signid){
                                    vm.pickMainSign.push(sObj);
                                }else{
                                    vm.lowerSign.push(sObj);
                                }
                            })
                        }
                    })
                    //显示次项目窗口
                    $("#lowerSignWin").kendoWindow({
                        width: "1024px",
                        height: "600px",
                        title: "次项目信息",
                        visible: false,
                        modal: true,
                        closable: true,
                        actions: ["Pin", "Minimize", "Maximize", "Close"]
                    }).data("kendoWindow").center().open();
                }else{
                    vm.pickMainSign = data.signList;
                }
            });
        }

        //挑选次项目
        vm.affirmLowerSign = function () {
            var checkSign = $("input[name='selLowSign']:checked");
            if (checkSign.length < 1) {
                bsWin.alert("请选择要挑选的次项目!");
            } else {
                var ids = [];
                for (var i = 0; i < checkSign.length; i++) {
                    ids.push(checkSign[i].value);
                }
                assistSvc.saveLowPlanSign(vm,ids,function(data){
                    vm.isCommit = false;
                    bsWin.alert("操作成功！");
                    vm.refleshPageInfo(true);
                });
            }
        }

        //取消次项目
        vm.cancelLowerSign = function () {
            var checkSign = $("input[name='checkLowSign']:checked");
            if (checkSign.length < 1) {
                bsWin.alert("请选择要挑选的次项目");
            } else {
                var ids = [];
                for (var i = 0; i < checkSign.length; i++) {
                    ids.push(checkSign[i].value);
                }
                assistSvc.cancelLowPlanSign(vm, ids.join(","),function(){
                    vm.isCommit = false;
                    bsWin.alert("操作成功！");
                    vm.refleshPageInfo(true);
                });
            }
        }

        //查询协审计划信息
        vm.queryPlan = function () {
            assistSvc.queryPlan(vm);
        }

        //查看协审计划的详情信息
        vm.showPlanDetail = function (planId) {
            $("#planInfo").kendoWindow({
                width: "80%",
                height: "600px",
                title: "协审项目清单",
                visible: false,
                modal: true,
                open:function(){
                    //初始化协审计划
                    vm.assistPlan = {};
                    assistSvc.initPlanByPlanId(planId,function(data1){
                        vm.assistPlan = data1;
                    });
                    //初始化协审单位
                    vm.unitList = [];
                    assistSvc.initAssistUnitByPlanId(planId,function(data2){
                        vm.unitList = data2;
                    });
                    //初始化协审项目
                    vm.planId = planId;
                    assistSvc.getPlanSignByPlanId(planId,function(data){
                        vm.assistPlanSign = data;
                    });
                },
                closable: true,
                actions: ["Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();

        }

        vm.ministerOpinionEdit = function (options) {	//部长意见
            if (!angular.isObject(options)) {
                options = {};
            }
            ideaSvc.initIdeaData(vm, options);
            // common.initIdeaData(vm,$http,ministerOpinion);
        }

        vm.viceDirectorOpinionEdit = function (options) {	//副主任意见
            if (!angular.isObject(options)) {
                options = {};
            }
            ideaSvc.initIdeaData(vm, options);
            // common.initIdeaData(vm,$http,viceDirectorOpinion);
        }

        vm.directorOpinionEdit = function (options) {	//主任意见
            if (!angular.isObject(options)) {
                options = {};
            }
            ideaSvc.initIdeaData(vm, options);
            // common.initIdeaData(vm,$http,directorOpinion);
        }

        /**
         * 保存协审费用信息
         */
        vm.savePlanSign = function () {
            vm.assistPlan.ministerOpinion = $("#ministerOpinion").val();
            vm.assistPlan.viceDirectorOpinion = $("#viceDirectorOpinion").val();
            vm.assistPlan.directorOpinion = $("#directorOpinion").val();
            //通过关联对象，一次传递
            vm.assistPlan.assistPlanSignDtoList = vm.assistPlanSign;
            assistSvc.savePlan(vm.assistPlan,function(data){
                if(data.flag || data.reCode == 'ok'){
                   bsWin.alert("操作成功！");
                }else{
                    bsWin.alert(data.reMsg);
                }
            });
            //保存协审项目信息(停用 2017-11-16，修改ldm)
            /*assistSvc.savePlanSign(vm.assistPlanSign,function(data){
                if(data.flag || data.reCode == 'ok'){
                    vm.assistPlan.ministerOpinion = $("#ministerOpinion").val();
                    vm.assistPlan.viceDirectorOpinion = $("#viceDirectorOpinion").val();
                    vm.assistPlan.directorOpinion = $("#directorOpinion").val();
                    //vm.assistPlan.id = assistPlanId;
                    assistSvc.savePlan(vm);
                }else{
                    bsWin.alert(data.reMsg);
                }
            });*/
        }

        /**
         * 抽取协审单位
         */
        vm.chooseAssistUnit = function () {
            vm.number = 0;
            if (vm.assistPlan.drawType == '1') {
                vm.number = vm.assistPlanSign.length + 1;
            }else{
                vm.assistPlan.drawType = '0';
                vm.number = vm.assistPlanSign.length;
            }
            //抽取协审单位
            assistSvc.chooseAssistUnit(vm.assistPlan.id,vm.number,vm.assistPlan.drawType,function(data){
                if(data.flag || data.reCode == 'ok'){
                    vm.unitList = data.reObj;
                }else{
                    bsWin.alert(data.reMsg);
                }
            });
        }

        vm.againChooleAssistUnit = function () {
            $("#againChooleAssistUnit").kendoWindow({
                title: "选择参加协审单位",
                width: "820px",
                height: "560px",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();

            assistSvc.getAllUnit(vm);
            vm.num = 0;
            if (vm.showPlan.drawType == "0") {
                vm.num = vm.assistPlanSign.length;
            } else {
                vm.num = vm.assistPlanSign.length + 1;
            }
        }

        /**
         * 手动添加协审单位
         * @param unitObject
         */
        vm.saveAddChooleUnit = function (unitObject) {
            assistSvc.saveAddChooleUnit(vm, unitObject);
        }

        /**
         * 协审项目抽签
         */
        vm.drawAssistUnit = function () {
            if (vm.assistPlanSign && vm.assistPlanSign.length > 0) {
                vm.assistPlanSign.forEach(function (t, n) {
                    t.assistUnit = null;
                });
            } else {
                return;
            }
            //待被抽取的协审单位
            vm.drawAssistUnits = vm.unitList.slice(0);

            //判断协审单位个数是否不少于协审计划个数，若少则先手动选择参与的协审单位，不少则可以直接抽签 drawType
            if (vm.drawType == "1" ? (vm.drawAssistUnits.length > vm.assistPlanSign.length) : (vm.drawAssistUnits.length >= vm.assistPlanSign.length)) {
                var drawPlanSignIndex = 0;
                var signIndex = -1;//记录被抽取的协审单位下标
                //先让上次轮空的协审单位进行抽取项目
                for (var i = 0; i < vm.drawAssistUnits.length; i++) { //遍历协审单位，判断是否为空，9表示为空，如果为空，则进行抽签协审计划，分配协审单位
                    if (vm.drawAssistUnits[i].isLastUnSelected == '9') {
                        var selscope = Math.floor(Math.random() * (vm.assistPlanSign.length));//产生随机数
                        signIndex = selscope;
                        vm.assistPlanSign[selscope].assistUnit = vm.drawAssistUnits[i];//将协审单位分配给协审计划
                        vm.drawPlanSign = vm.assistPlanSign[selscope];
                        vm.drawAssistUnits.splice(i, 1);//将上轮轮空的协审单位移除
                    }
                }

                //当前抽取第一个项目的协审单位
                vm.drawPlanSign = vm.assistPlanSign[drawPlanSignIndex];
                var timeCount = 0;
                vm.isStartDraw = true;
                vm.isDrawDone = false;
                vm.t = $interval(function () {
                    vm.drawPlanSign = vm.assistPlanSign[drawPlanSignIndex];
                    var selscope = Math.floor(Math.random() * (vm.drawAssistUnits.length));
                    var selAssistUnit = vm.drawAssistUnits[selscope];
                    vm.showAssitUnitName = selAssistUnit.unitName;
                    timeCount++;
                    //一秒后，选中协审单位
                    if (timeCount % 5 == 0) {
                        //选中协审单位
                        if (drawPlanSignIndex != signIndex) {
                            vm.assistPlanSign[drawPlanSignIndex].assistUnit = selAssistUnit;
                        } else {
                            if (drawPlanSignIndex != vm.assistPlanSign.length - 1) {
                                vm.assistPlanSign[++drawPlanSignIndex].assistUnit = selAssistUnit;
                            }
                        }
                        drawPlanSignIndex++;
                        if (drawPlanSignIndex == signIndex && signIndex == vm.assistPlanSign.length - 1) { //判断轮空抽签的是不是最后一个，并且协审计划轮抽到最后一个时，停止抽签
                            $interval.cancel(vm.t);
                            vm.isDrawDone = true;
                        }
                        if (drawPlanSignIndex == vm.assistPlanSign.length) {
                            //抽签完毕
                            $interval.cancel(vm.t);
                            vm.isDrawDone = true;
                        }

                        vm.drawAssistUnits.forEach(function (t, n) {
                            if (t.id == selAssistUnit.id) {
                                vm.drawAssistUnits.splice(n, 1);
                            }
                        });
                    }
                }, 200);
            } else {
                bsWin.alert("当前协审单位少于项目计划数目，不能抽签！请先到项目计划表中选择参加的协审单位后再进行抽签！");
            }
        }


        /**
         * 保存抽签结果
         */
        vm.saveDrawAssistUnit = function () {
            assistSvc.saveDrawAssistUnit(vm);
        }

        /**
         * 生成抽签日期
         */
        vm.buildDrawTime = function(){
            var isHaveBegin = false;
            var isAM = false;
            var drawTime = new Date(vm.drawTimeDay);
            var weekIndex = drawTime.getDay();
            var drawDay = drawTime.Format("yyyy年MM月dd日")+"("+weekArray[weekIndex]+")";
            if(vm.drawTimeStart){
                isHaveBegin = true;
                var beginTime = vm.drawTimeStart.split(":")[0];
                if(beginTime > 12){
                    drawDay +="下午"
                }else{
                    isAM = true;
                    drawDay +="上午"
                }
                drawDay += vm.drawTimeStart;
            }
            if(vm.drawTimeEnd){
                var beginTime = vm.drawTimeEnd.split(":")[0];
                if(isHaveBegin){
                    drawDay += "至"
                }
                if(beginTime > 12){
                    if(isAM || !isHaveBegin) {
                        drawDay += "下午"
                    }
                }else{
                    if(!isAM || !isHaveBegin){
                        drawDay +="上午"
                    }
                }
                drawDay += vm.drawTimeEnd;
            }
            vm.assistPlan.drawTime = drawDay;
        }

        /**
         * 拼接报批时间
         */
        vm.buildApprovalTime = function(){
            var isHaveBegin = false;
            var isAM = false;
            var approvalTime = new Date(vm.approvalTimeDay);
            var weekIndex = approvalTime.getDay();
            var drawDay = approvalTime.Format("yyyy年MM月dd日")+"("+weekArray[weekIndex]+")";
            if(vm.approvalStart){
                isHaveBegin = true;
                var beginTime = vm.approvalStart.split(":")[0];
                if(beginTime > 12){
                    drawDay +="下午"
                }else{
                    isAM = true;
                    drawDay +="上午"
                }
                drawDay += vm.approvalStart;
            }
            if(vm.approvalTimeEnd){
                var beginTime = vm.approvalTimeEnd.split(":")[0];
                if(isHaveBegin){
                    drawDay += "至"
                }
                if(beginTime > 12){
                    if(isAM || !isHaveBegin) {
                        drawDay += "下午"
                    }
                }else{
                    if(!isAM || !isHaveBegin){
                        drawDay +="上午"
                    }
                }
                drawDay += vm.approvalTimeEnd;
            }
            vm.assistPlan.approvalTime = drawDay;
        }
    }
})();
