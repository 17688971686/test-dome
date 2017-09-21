(function () {
    'use strict';

    angular.module('app').controller('monthlyMultiyearEditCtrl', monthlyMultiyear);

    monthlyMultiyear.$inject = [ 'monthlyMultiyearSvc','sysfileSvc', '$state','bsWin','$scope'];

    function monthlyMultiyear( monthlyMultiyearSvc,sysfileSvc, $state,bsWin,$scope) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加中心文件稿纸';
        vm.isuserExist = false;
        vm.isSubmit = true;
        vm.suppletter ={};//文件稿纸对象
        vm.id = $state.params.id;
        vm.suppletter.id = $state.params.id;
       
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新中心文件稿纸';
        }

         vm.businessFlag ={
                isInitFileOption : false,   //是否已经初始化附件上传控件
         }
      
       //初始化附件上传控件
         vm.initFileUpload = function(){
             if(!vm.suppletter.id){
                 //监听ID，如果有新值，则自动初始化上传控件
                 $scope.$watch("vm.suppletter.id",function (newValue, oldValue) {
                     if(newValue && newValue != oldValue && !vm.initUploadOptionSuccess){
                         vm.initFileUpload();
                     }
                 });
             }
             vm.sysFile = {
                 businessId : vm.suppletter.id,
                 mainId : vm.suppletter.id,
                 mainType : sysfileSvc.mainTypeValue().SIGN,
                 sysfileType:sysfileSvc.mainTypeValue().CENTER_FILE,
                 sysBusiType:sysfileSvc.mainTypeValue().CENTER_FILE,
             };
             sysfileSvc.initUploadOptions({
                 inputId:"sysfileinput",
                 vm:vm
             });
         }
        
        //跳转上传附件页面
        vm.addSuppContent = function(){
        	if(vm.suppletter.id){
        		$state.go('uploadMonthly',{id : vm.suppletter.id});
        	}else{
        		bsWin.alert("请先保存数据！");
        	}
     }
        
        //保存中心文件（稿纸）
        vm.saveAddSuppletter = function () {
            common.initJqValidation();
            var isValid = $('form').valid();
	          if(isValid){
	        	  monthlyMultiyearSvc.createmonthlyMultiyear(vm.suppletter,function(data){
	                   if (data.flag || data.reCode == "ok") {
	                           bsWin.alert("操作成功！");
	                   }else{
	                       bsWin.error(data.reMsg);
	                   }
	               });
	           }else{
	        	   bsWin.alert("缺少部分没有填写，请仔细检查");
	           }
        };
        //更新中心文件（稿纸）
        vm.updateAddSuppletter = function () {
        	 common.initJqValidation();
             var isValid = $('form').valid();
 	          if(isValid){
 	        	  monthlyMultiyearSvc.updatemonthlyMultiyear(vm.suppletter,function(data){
 	                   if (data.flag || data.reCode == "ok") {
 	                           bsWin.alert("操作成功！");
 	                   }else{
 	                       bsWin.error(data.reMsg);
 	                   }
 	               });
 	           }else{
 	        	   bsWin.alert("缺少部分没有填写，请仔细检查");
 	           }
        };

        
        activate();
        function activate() {
          if (vm.isUpdate) {
                monthlyMultiyearSvc.getmonthlyMultiyearById(vm);
            }
            monthlyMultiyearSvc.initMonthlyMultiyear(vm);
          //根据主业务获取所有的附件信息
            monthlyMultiyearSvc.findByBusinessId(vm);
        }
    }
})();
