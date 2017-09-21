(function () {
    'use strict';

    angular.module('app').controller('monthlyMultiyearEditCtrl', monthlyMultiyear);

    monthlyMultiyear.$inject = ['$location', 'monthlyMultiyearSvc','sysfileSvc', '$state','bsWin'];

    function monthlyMultiyear($location, monthlyMultiyearSvc,sysfileSvc, $state,bsWin) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加中心文件稿纸';
        vm.isuserExist = false;
        vm.isSubmit = true;
        vm.suppletter ={};//文件稿纸对象
        vm.id = $state.params.id;
        vm.fileId = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新中心文件稿纸';
        }

         vm.businessFlag ={
                isInitFileOption : false,   //是否已经初始化附件上传控件
         }
         
         
       //初始化附件上传控件
         vm.initFileUpload = function(){
        	
                 //监听ID，如果有新值，则自动初始化上传控件
                 $scope.$watch("vm.fileId",function (newValue , oldValue){
                     if(newValue && newValue != oldValue && !vm.initUploadOptionSuccess){
                         vm.initFileUpload();
                     }
                 });
             //创建附件对象
             vm.sysFile = {
                 businessId : vm.fileId,
                 mainId : '',
                 mainType : sysfileSvc.mainTypeValue().FILELIBRARY,
                 sysBusiType :vm.fileUrl.substring(vm.fileUrl.lastIndexOf(sysfileSvc.mainTypeValue().FILELIBRARY),vm.fileUrl.lastIndexOf(vm.fileName))
             };
        	 
             sysfileSvc.initUploadOptions({
                 inputId : "sysfileinput",
                 vm :vm ,
                 uploadSuccess : function(){
                     sysfileSvc.findByBusinessId(vm.fileId,function(data){
                         vm.sysFilelists = data;
                     });
                 }
             });
         }
        
        //上传附件页面
        vm.addSuppContent = function(){
        	  $state.go('uploadMonthly',{id :  vm.id});
        	/* $("#addsuppMonthly").kendoWindow({
                 width : "50%",
                 height : "620px",
                 title : "会议预定信息",
                 visible : false,
                 modal : true,
                 closable : true,
                 actions : [ "Pin", "Minimize", "Maximize", "Close" ]
             }).data("kendoWindow").center().open();*/
  		 
     }
        
        
        //保存中心文件（稿纸）
        vm.saveAddSuppletter = function () {
           // monthlyMultiyearSvc.createmonthlyMultiyear(vm);
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
            
        }
    }
})();
