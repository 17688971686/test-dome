(function () {
    'dispatch strict';

    angular.module('app').controller('dispatchEditCtrl', dispatch);

    dispatch.$inject = ['$location','dispatchSvc','$state']; 

    function dispatch($location, dispatchSvc, $state) {     
        var vm = this;
        vm.title = '项目发文编辑';
        vm.isHide=true;
        vm.isHide2=true;
        vm.isSubmit = false;
        vm.saveProcess=false;
        vm.sign={};
        vm.dispatchDoc = {};
        vm.dispatchDoc.signId = $state.params.signid;
        //创建发文
        vm.linkSignId=" ";
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
        	//var selectValue=$("#dispatchWay").find("option:selected").text();
        	if(vm.dispatchDoc.dispatchWay=="合并发文"){
        		vm.isHide=false;
        	}else{
        		vm.isHide=true;
        		
        	}
        }
        
        //选主项目
        vm.ischecked=function(){
        		vm.dispatchDoc.isRelated="是";
    			vm.isHide2=false;
        }
        
      //选次项目
        vm.ischecked2=function(){
        	vm.dispatchDoc.isRelated="否";
        	vm.isHide2=true;
        }
        //打开合并页面
        vm.gotoMergePage=function(){
        	dispatchSvc.gotoMergePage(vm);
        }
        
        vm.search=function(){
        	dispatchSvc.getSign(vm);
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
        
        vm.formReset=function(){
        	var values=$("#searchform").find("input,select");
        	values.val("");
        }
        
        vm.search=function(){
        	dispatchSvc.getsign(vm);
        }
        //生成文件字号
        vm.fileNum=function(){
        	dispatchSvc.fileNum(vm);
        }
        activate();
        function activate() {
             dispatchSvc.initDispatchData(vm);
        }
    }
})();
