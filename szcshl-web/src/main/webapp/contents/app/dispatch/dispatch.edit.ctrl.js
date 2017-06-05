(function () {
    'dispatch strict';

    angular.module('app').controller('dispatchEditCtrl', dispatch);

    dispatch.$inject = ['$location','dispatchSvc','$state']; 

    function dispatch($location, dispatchSvc, $state) {     
        var vm = this;
        vm.title = '项目发文编辑';
        vm.isHide=true;
        vm.isHide2=true;
        vm.saveProcess=false;
        vm.showFileNum=false;
        vm.mwindowHide=true;
        vm.showCreate=false;
        vm.linkSignId=" ";
        vm.sign={};
        vm.dispatchDoc = {};
        vm.dispatchDoc.signId = $state.params.signid;
        
        //创建发文
        vm.create = function(){
        	dispatchSvc.saveDispatch(vm);
        }
        //核减（增）/核减率（增）计算
        vm.count=function(){
        	var declareValue=vm.dispatchDoc.declareValue;
        	var authorizeValue=vm.dispatchDoc.authorizeValue;
        	if(declareValue&&authorizeValue){
        		var dvalue=declareValue-authorizeValue;
        		var extraRate=((dvalue/declareValue).toFixed(4))*100;
        		vm.dispatchDoc.extraRate=extraRate;
        		vm.dispatchDoc.extraValue=dvalue;
        	}
        }
        
        vm.isrelated=function(){
        		vm.dispatchDoc.isRelated="是";
        }
        
        vm.isrelated2=function(){
        	vm.dispatchDoc.isRelated="否";
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
