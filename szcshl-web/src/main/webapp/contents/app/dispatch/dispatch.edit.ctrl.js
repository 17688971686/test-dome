(function () {
    'dispatch strict';

    angular.module('app').controller('dispatchEditCtrl', dispatch);

    dispatch.$inject = ['$location','dispatchSvc','$state']; 

    function dispatch($location, dispatchSvc, $state) {     
        var vm = this;
        vm.title = '项目发文编辑';

        vm.dispatchDoc = {};
        vm.dispatchDoc.signId = $state.params.signid;
        //创建发文
        vm.linkSignId="";
        vm.create = function(){
        	dispatchSvc.saveDispatch(vm);
        }
        
        //核减（增）/核减率（增）计算
        vm.count=function(){
        	var declareValue=vm.dispatchDoc.declareValue;
        	var authorizeValue=vm.dispatchDoc.authorizeValue;
        	if(declareValue&&authorizeValue){
        		var dvalue=declareValue-authorizeValue;
        		var extraRate=(dvalue/declareValue).toFixed(4)*100;
        		vm.dispatchDoc.extraRate=extraRate;
        		vm.dispatchDoc.extraValue=dvalue;
        	}
        }
        
        //选择发文方式
        vm.isSelect=function(){
        	var selectValue=$("#dispatchWay").find("option:selected").text();
        	if(selectValue=="合并发文"){
        		$("#mergeOptions").css("visibility","visible");
        		var isCheck = $("#mainproject").attr("checked");
        		console.log(isCheck);
        		if (isCheck) {
        			$("#linkbtn").css("visibility","visible");
        		}else{
        			$("#linkbtn").css("visibility","hidden");
        		}
        	}else{
        		$("#mergeOptions").css("visibility","hidden");
        		
        	}
        	
        }
        
        //打开合并页面
        vm.gotoMergePage=function(){
        	dispatchSvc.gotoMergePage(vm);
        }
        
        //选择待选项目
        vm.chooseProject=function(){
        	dispatchSvc.chooseProject(vm);
        }
        
        //取消选择
        vm.cancelProject=function(){
        	dispatchSvc.cancelProject(vm);
        }
        
        //关闭窗口
        vm.onClose=function(){
        	window.parent.$("#mwindow").data("kendoWindow").close();
        }
        
        //确定合并
        vm.mergeDispa=function(){
        	dispatchSvc.mergeDispa(vm);
        }
        
        //生成文件字号
        vm.fileNum=function(){
        	dispatchSvc.fileNum(vm);
        }
        activate();
        function activate() {
        	 kendo.culture("zh-CN");
             $("#draftDate").kendoDatePicker({
             	 format: "yyyy-MM-dd",
             	 weekNumber: true
             });
             $("#dispatchDate").kendoDatePicker({
            	 format: "yyyy-MM-dd",
            	 weekNumber: true
             });
             dispatchSvc.initDispatchData(vm);
        }
    }
})();
