(function () {
    'use strict';

    angular.module('app').controller('assistPlanCtrl', assistPlan);

    assistPlan.$inject = ['$location','$state','assistSvc','$http','$interval','bsWin','ideaSvc'];

    function assistPlan($location,$state,assistSvc,$http,$interval,bsWin , ideaSvc) {
        var vm = this;
        vm.model = {};							//创建一个form对象
        vm.filterModel = {};                    //filter对象
        vm.filterLow = {};
        vm.title = '协审计划管理';        		//标题
        vm.splitNumArr = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15];
        vm.plan = {};                           //添加的协审对象
        vm.planList = new Array();              //在办协审计划列表
        vm.showPlan = {};                       //显示协审计划信息

        vm.assistSign = new Array();            //待选项目列表
        vm.pickSign = new Array();              //协审计划已选的项目列表
        vm.pickMainSign = new Array();          //主项目对象
        vm.lowerSign = new Array();             //次项目对象
        vm.selectPlanId = "";                   //选择显示的协审计划ID
        vm.selectMainSignId = "";               //查看的主项目ID
        vm.initPickLowSign = false;             //是否初始化选择的次项目信息
        vm.drawType="";

        active();
        function active(){
            assistSvc.initPlanPage(vm);
            assistSvc.initPlanGrid(vm);
            $('#planInfo li').click(function (e) {
                var aObj = $("a",this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#"+showDiv).addClass("active").addClass("in").show(500);
            })
        }

        //待选择过来器
        vm.filterSign = function(item){
            var isMatch = true;
            if(!angular.isUndefined(item)){
                if(!angular.isUndefined(vm.filterModel.filterFilecode)){
                     if((item.filecode).indexOf(vm.filterModel.filterFilecode) == -1){
                         isMatch = false;
                     }
                }
                if(isMatch){
                    if(!angular.isUndefined(vm.filterModel.filterProjectCode)){
                        if((item.projectcode).indexOf(vm.filterModel.filterProjectCode) == -1){
                            isMatch = false;
                        }
                    }
                }
                if(isMatch){
                    if(!angular.isUndefined(vm.filterModel.filterProjectName)){
                        if((item.projectname).indexOf(vm.filterModel.filterProjectName) == -1){
                            isMatch = false;
                        }
                    }
                }
                if(isMatch){
                    if(!angular.isUndefined(vm.filterModel.filterBuiltName)){
                        if(angular.isUndefined(item.builtcompanyName)){
                            isMatch = false;
                        }
                        if(isMatch && (item.builtcompanyName).indexOf(vm.filterModel.filterBuiltName) == -1){
                            isMatch = false;
                        }
                    }
                }
                if(isMatch){
                    return item;
                }
            }
        }

        //次项目待选择器
        vm.filterLowSign = function(item){
            var isMatch = true;
            if(!angular.isUndefined(item)){
                if(!angular.isUndefined(vm.filterLow.filterFilecode)){
                    if((item.filecode).indexOf(vm.filterLow.filterFilecode) == -1){
                        isMatch = false;
                    }
                }
                if(isMatch){
                    if(!angular.isUndefined(vm.filterLow.filterProjectCode)){
                        if((item.projectcode).indexOf(vm.filterLow.filterProjectCode) == -1){
                            isMatch = false;
                        }
                    }
                }
                if(isMatch){
                    if(!angular.isUndefined(vm.filterLow.filterProjectName)){
                        if((item.projectname).indexOf(vm.filterLow.filterProjectName) == -1){
                            isMatch = false;
                        }
                    }
                }
                if(isMatch){
                    if(!angular.isUndefined(vm.filterLow.filterBuiltName)){
                        if(angular.isUndefined(item.builtcompanyName)){
                            isMatch = false;
                        }
                        if(isMatch && (item.builtcompanyName).indexOf(vm.filterLow.filterBuiltName) == -1){
                            isMatch = false;
                        }
                    }
                }
                if(isMatch){
                    return item;
                }
            }
        }


        //重置拆分值
        vm.initSplit = function(typeName){
            if(vm.plan.assistType == typeName){
                if(!angular.isUndefined(vm.plan.spliNum)){
                    vm.plan.spliNum = 0;
                }
            }
        }

        //挑选项目
        vm.affirmSign = function () {
            var isCheckSign = $("input[name='selASTSign']:checked");
            if (isCheckSign.length < 1) {
                bsWin.alert("请选择要挑选的项目");
            }else{
                if(isCheckSign.length > 1){
                    if(vm.plan.assistType == '合并项目'){
                        bsWin.alert("合并项目要先挑选一个主项目，再挑选次项目！");
                    }else{
                        bsWin.alert("独立项目，每次只能选择一个！");
                    }
                }else{
                    vm.model.signId = isCheckSign[0].value;
                    vm.model.assistType = vm.plan.assistType;
                    vm.model.single = vm.plan.assistType == '合并项目'?false:true;
                    vm.model.splitNum = vm.plan.spliNum;
                    vm.model.id = vm.selectPlanId;
                    vm.assistSign.forEach(function (st,index) {
                        if(st.signid == vm.model.signId){
                            vm.model.projectName = st.projectname;
                        }
                    });
                    vm.model.isDrawed="0";
                    vm.model.assistPlanSignDtoList = vm.showPlan.assistPlanSignDtoList;
                    assistSvc.saveAssistPlan(vm.model,vm.isCommit,function(data){
                        vm.isCommit = false;
                        //如果是新增，则重新刷新列表
                        if(!vm.showPlan.id){
                            vm.gridOptions.dataSource.read();
                        }
                        vm.showPlan = data.reObj;
                        vm.selectPlanId = vm.showPlan.id;
                        assistSvc.initPlanPage(vm);
                        //如果是合并对象，则选择次项目
                        if(vm.plan.assistType == '合并项目'){
                            vm.showPickLowSign(vm.model.signId);
                        }else{
                            bsWin.success("操作成功！");
                        }
                    });
                }
            }
        }

        //取消
        vm.cancelSign = function(){
            var isCheckSign = $("input[name='checkASTSign']:checked");
            if (isCheckSign.length < 1) {
                bsWin.alert("请选择取消的项目");
            }else{
                common.confirm({
                    vm: vm,
                    title: "",
                    msg: "确认取消挑选项目吗?",
                    fn: function () {
                        $('.confirmDialog').modal('hide');
                        var ids=[];
                        for (var i = 0; i < isCheckSign.length; i++) {
                            ids.push(isCheckSign[i].value);
                        }
                        assistSvc.cancelPlanSign(vm,ids.join(','));
                    }
                });
            }
        }

        //初始化选择的协审计划信息
        vm.initSelPlan = function(){
            assistSvc.initSelPlan(vm);
        }

        //删除操作
        vm.doDelete  = function(){
           if(vm.showPlan.id){
               bsWin.confirm({
                   title: "询问提示",
                   message: "确认删除数据吗？删除数据不可恢复，请慎重！",
                   onOk: function () {
                       $('.confirmDialog').modal('hide');
                       assistSvc.deletePlan(vm.showPlan.id,vm.isCommit,function(data){
                           vm.isCommit = false;
                           assistSvc.initPlanPage(vm);
                           //刷新列表信息
                           vm.gridOptions.dataSource.read();
                       });
                   }
               });
           }else{
               bsWin.alert("请选择要删除的协审计划包");
           }
        }

        //显示次项目信息
        vm.showPickLowSign = function(mainSignId){
            vm.selectMainSignId = mainSignId;
            assistSvc.showPickLowSign(vm);
            //显示次项目窗口
            $("#lowerSignWin").kendoWindow({
                width : "1024px",
                height : "600px",
                title : "次项目信息",
                visible : false,
                modal : true,
                closable : true,
                actions : [ "Pin", "Minimize", "Maximize", "Close" ]
            }).data("kendoWindow").center().open();
        }

        //挑选次项目
        vm.affirmLowerSign = function(){
            var checkSign = $("input[name='selLowSign']:checked");
            if (checkSign.length < 1) {
                common.alert({
                    vm : vm,
                    msg : "请选择要挑选的次项目"
                })
            }else{
                var ids = [];
                for (var i = 0; i < checkSign.length; i++) {
                    ids.push(checkSign[i].value);
                }
                assistSvc.saveLowPlanSign(vm,ids);
            }
        }

        //取消次项目
        vm.cancelLowerSign = function(){
            var checkSign = $("input[name='checkLowSign']:checked");
            if (checkSign.length < 1) {
                bsWin.alert("请选择要挑选的次项目");
            }else{
                var ids = [];
                for (var i = 0; i < checkSign.length; i++) {
                    ids.push(checkSign[i].value);
                }
                assistSvc.cancelLowPlanSign(vm,ids.join(","));
            }
        }

        //查询协审计划信息
        vm.queryPlan = function () {
            assistSvc.queryPlan(vm);
        }
        
         
        var assistPlanId='';//协审计划Id
        vm.planId=''; //
       
        //查看协审计划的详情信息
        vm.showPlanDetail = function(planId){
            $("#planInfo").kendoWindow({
                width : "1024px",
                height : "600px",
                title : "协审项目清单",
                visible : false,
                modal : true,
                closable : true,
                actions : [ "Pin", "Minimize", "Maximize", "Close" ]
            }).data("kendoWindow").center().open();
           	vm.signNum=0;//抽取单位个数
            assistPlanId=planId;
            vm.planId=planId;
            assistSvc.getPlanSignByPlanId(vm,planId);
        }
        
        vm.ministerOpinionEdit=function (options){	//部长意见
            if(!angular.isObject(options)){
                options = {};
            }
            ideaSvc.initIdeaData(vm,options);
        	// common.initIdeaData(vm,$http,ministerOpinion);
        }
        
        vm.viceDirectorOpinionEdit=function(options){	//副主任意见
            if(!angular.isObject(options)){
                options = {};
            }
            ideaSvc.initIdeaData(vm,options);
        	// common.initIdeaData(vm,$http,viceDirectorOpinion);
        }
        
        vm.directorOpinionEdit=function (options){	//主任意见
            if(!angular.isObject(options)){
                options = {};
            }
            ideaSvc.initIdeaData(vm,options);
        	// common.initIdeaData(vm,$http,directorOpinion);
        }
        
        vm.assistPlan={};
        vm.savePlanSign=function(){//保存协审项目信息
	       	assistSvc.savePlanSign(vm);
	        vm.assistPlan.id=assistPlanId;
	       	assistSvc.savePlan(vm);
        }
        
        
       vm.checked='option1';
        vm.chooseAssistUnit=function(){
        	vm.number=0;
        	vm.drawType="";
        	if(vm.checked=='option1'){
        		vm.drawType="1";
        		vm.number=vm.assistPlanSign.length+1;
        	}
        	if(vm.checked=='option2'){
        		vm.drawType="0";
        		vm.number=vm.assistPlanSign.length;
        	}
        	assistSvc.chooseAssistUnit(vm);
        
        }
        
         vm.againChooleAssistUnit=function(){
        	$("#againChooleAssistUnit").kendoWindow({
	        	title:"选择参加协审单位",
	        	width:"600px",
    			height:"500px",
	        	visible : false,
	            modal : true,
	            closable : true,
	            actions : [ "Pin", "Minimize", "Maximize", "Close" ]
            }).data("kendoWindow").center().open();
            
            assistSvc.getAllUnit(vm);
            vm.num=0;
        	if(vm.showPlan.drawType=="0"){
        		vm.num=vm.assistPlanSign.length;
        	}else {
        		console.log(123);
        		vm.num=vm.assistPlanSign.length+1;
        	}
        }
        
        vm.saveAddChooleUnit=function(unitObject){
        	assistSvc.saveAddChooleUnit(vm,unitObject);
        
        }

        //协审项目抽签
        vm.drawAssistUnit = function(){
        	console.log(vm.showPlan.drawType);
            if(vm.assistPlanSign != undefined&&vm.assistPlanSign.length>0){
                vm.assistPlanSign.forEach(function(t,n){
                    t.assistUnit = null;
                });
            }else{
                return ;
            }
            //待被抽取的协审单位
            vm.drawAssistUnits = vm.unitList.slice(0);
            
            //判断协审单位个数是否不少于协审计划个数，若少则先手动选择参与的协审单位，不少则可以直接抽签 drawType
            if(vm.drawType=="1"? (vm.drawAssistUnits.length>vm.assistPlanSign.length):(vm.drawAssistUnits.length>=vm.assistPlanSign.length)){
            
//            var drawAssistPlanSign
            var drawPlanSignIndex = 0;
            var signIndex=-1;//记录被抽取的协审单位下标
            //先让上次轮空的协审单位进行抽取项目
	            for(var i=0;i<vm.drawAssistUnits.length;i++){ //遍历协审单位，判断是否为空，9表示为空，如果为空，则进行抽签协审计划，分配协审单位
	            	if(vm.drawAssistUnits[i].isLastUnSelected=='9'){
	            		var selscope = Math.floor(Math.random()*(vm.assistPlanSign.length));//产生随机数
	            		signIndex=selscope;
	            		vm.assistPlanSign[selscope].assistUnit=vm.drawAssistUnits[i];//将协审单位分配给协审计划
	            		vm.drawPlanSign = vm.assistPlanSign[selscope];
	            		vm.drawAssistUnits.splice(i,1);//将上轮轮空的协审单位移除
	            	}
	            	
	            }
            
            //当前抽取第一个项目的协审单位
            vm.drawPlanSign = vm.assistPlanSign[drawPlanSignIndex];
            var timeCount = 0;
            vm.isStartDraw = true;
            vm.isDrawDone = false;
            vm.t = $interval(function() {
                vm.drawPlanSign = vm.assistPlanSign[drawPlanSignIndex];
                var selscope = Math.floor(Math.random()*(vm.drawAssistUnits.length));
              	var selAssistUnit = vm.drawAssistUnits[selscope];
                vm.showAssitUnitName = selAssistUnit.unitName;
                timeCount++;
                //一秒后，选中协审单位
                if(timeCount % 20 == 0){
                    //选中协审单位
                	if(drawPlanSignIndex!=signIndex){
                    	vm.assistPlanSign[drawPlanSignIndex].assistUnit = selAssistUnit;
                	}else{
                		if(drawPlanSignIndex!=vm.assistPlanSign.length-1){
	                		vm.assistPlanSign[++drawPlanSignIndex].assistUnit = selAssistUnit;
                		}
                	}
                    drawPlanSignIndex ++;
                    if(drawPlanSignIndex==signIndex && signIndex==vm.assistPlanSign.length-1){ //判断轮空抽签的是不是最后一个，并且协审计划轮抽到最后一个时，停止抽签
                    	$interval.cancel(vm.t);
                        vm.isDrawDone = true;
                    }
                    if(drawPlanSignIndex == vm.assistPlanSign.length){
                        //抽签完毕
                        $interval.cancel(vm.t);
                        vm.isDrawDone = true;
                    }

                    vm.drawAssistUnits.forEach(function (t,n){
                        if(t.id == selAssistUnit.id){
                            vm.drawAssistUnits.splice(n,1);
                        }
                    });
                    
            	}
            }, 50);
        }else{
        	common.alert({
        		vm:vm,
        		msg:"当前协审单位少于项目计划数目，不能抽签！请先到项目计划表中选择参加的协审单位后再进行抽签！"
        	});
        }
        }


        vm.saveDrawAssistUnit = function(){
            assistSvc.saveDrawAssistUnit(vm);
        }

    }
})();
