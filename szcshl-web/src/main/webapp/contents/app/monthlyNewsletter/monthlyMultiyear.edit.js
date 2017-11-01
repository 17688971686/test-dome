(function () {
    'use strict';

    angular.module('app').controller('monthlyMultiyearEditCtrl', monthlyMultiyear);

    monthlyMultiyear.$inject = [ 'monthlyMultiyearSvc','sysfileSvc', '$state','bsWin','$scope'];

    function monthlyMultiyear( monthlyMultiyearSvc,sysfileSvc, $state,bsWin,$scope) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加月报简报稿纸';
        vm.isuserExist = false;
        vm.isSubmit = true;
        vm.suppletter ={};//文件稿纸对象
        vm.id = $state.params.id;
        vm.type=$state.params.type;
        vm.suppletter.id = $state.params.id;
        vm.businessId = $state.params.businessId;

        if (vm.id) {
            vm.isUpdate = true;
            vm.isflow=true;//显示发起流程的按钮
            vm.title = '更新月报简报稿纸';
        }

         vm.businessFlag ={
                isInitFileOption : false,   //是否已经初始化附件上传控件
         }
         
         //领导审批中心文件
         vm.updateApprove = function(){
        	 monthlyMultiyearSvc.updateApprove(vm);
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
                vm.showsupp = true;
                var ideaEditWindow = $("#addsuppMonthly");
                ideaEditWindow.kendoWindow({
                    width: "60%",
                    height: "90%",
                    title: "中心文件附件",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "close"]
                }).data("kendoWindow").center().open();
                monthlyMultiyearSvc.findByBusinessId(vm);
            	}else{
            		bsWin.alert("请先保存业务数据");
            	}
     }
        
        //保存中心文件（稿纸）
        vm.saveAddSuppletter = function () {
            common.initJqValidation();
            var isValid = $('form').valid();
	          if(isValid){
	              vm.suppletter.fileType="2";//区分是不是简报
                  vm.suppletter.businessId=vm.businessId;//业务ID
                  monthlyMultiyearSvc.createmonthlyMultiyear(vm.suppletter,function(data){
	                   if (data.flag || data.reCode == "ok") {
	                           bsWin.alert("操作成功！",function(){
                                   vm.id=data.reObj.id;//保存后取得id,流程发起需要
                                   location.href =  "#/monthlyFindByMultiyear/"+vm.businessId+"";//保存成功后跳转到列表
	                           })

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
 	        	      console.log(data);
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
        //************************** S 以下是新流程处理js **************************//
        vm.startNewFlow = function(id){
            bsWin.confirm({
                title: "询问提示",
                message: "确认已经完成填写，并且发起流程么？",
                onOk: function () {
                    $('.confirmDialog').modal('hide');
                    if(id!=""){//当是更新提交时，先更新在提交
                        monthlyMultiyearSvc.updatemonthlyMultiyear(vm.suppletter,function(data){//提交时先更新在提交
                            if (data.flag || data.reCode == "ok") {
                                monthlyMultiyearSvc.startFlow(id,function(data){//更新的提交
                                    if(data.flag || data.reCode == 'ok'){
                                        bsWin.success("操作成功！",function(){
                                            location.href =  "#/monthlyFindByMultiyear/"+vm.suppletter.businessId+"";//保存成功后跳转到列表
                                        });
                                    }else{
                                        bsWin.error(data.reMsg);
                                    }
                                });
                            }else{
                                bsWin.error(data.reMsg);
                            }
                        });
                    }else{//当是保存时提交就先保存
                        vm.suppletter.fileType="2";//区分是不是简报
                        vm.suppletter.businessId=vm.businessId;//业务ID
                        monthlyMultiyearSvc.createmonthlyMultiyear(vm.suppletter,function(data) {
                            if (data.flag || data.reCode == "ok") {
                                vm.id = data.reObj.id;//保存后取得id,流程发起需要
                                monthlyMultiyearSvc.startFlow(vm.id,function(data){//更新的提交
                                    if(data.flag || data.reCode == 'ok'){
                                        bsWin.success("操作成功！",function(){
                                            location.href =  "#/monthlyFindByMultiyear/"+vm.suppletter.businessId+"";//保存成功后跳转到列表
                                        });
                                    }else{
                                        bsWin.error(data.reMsg);
                                    }
                                });
                            } else {
                                bsWin.error(data.reMsg);
                            }
                        });
                    }

                }
            });
        }
        
        activate();
        function activate() {
          if (vm.isUpdate) {
                monthlyMultiyearSvc.getmonthlyMultiyearById(vm.id,function (data) {
                    vm.suppletter = data;
                    vm.businessId=vm.suppletter.businessId;//返回的链接业务Id
                });
            }
            monthlyMultiyearSvc.initMonthlyMultiyear(vm);
          //根据主业务获取所有的附件信息
            monthlyMultiyearSvc.findByBusinessId(vm);
        }
    }
})();
