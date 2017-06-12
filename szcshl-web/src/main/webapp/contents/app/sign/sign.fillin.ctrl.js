(function () {
    'use strict';

    angular.module('app').controller('signFillinCtrl', sign);

    sign.$inject = ['$location','signSvc','$state']; 

    function sign($location, signSvc, $state) {        
        var vm = this;
    	vm.model = {};		//创建一个form对象   	
        vm.title = '填写报审登记表';        		//标题
        vm.model.signid = $state.params.signid;	//收文ID
       
        vm.flowDeal = false;		//是否是流程处理标记
        
        signSvc.initFillData(vm);
        
        vm.signDownload = function(){
        	window.open("ss");
        }
        //删除系统文件
        vm.delsSysFile = function(id){
        /*	 common.confirm({
            	 vm:vm,
            	 title:"",
            	 msg:"确认删除数据吗？",
            	 fn:function () {
                  	$('.confirmDialog').modal('hide');             	
                 }
             })*/
           signSvc.deleteSysFile(vm,id);
        }
       
        //附件上传
        vm.signUpload = function(){
        	 $("#signUploadWin").kendoWindow({
                 width : "660px",
                 height : "400px",
                 title : "附件上传",
                 visible : false,
                 modal : true,
                 closable : true,
                 actions : [ "Pin", "Minimize", "Maximize", "Close" ]
             }).data("kendoWindow").center().open();
        }
        //查看附件
        vm.signJquery = function(){
        	$("#signAttachments").kendoWindow({
                width : "800px",
                height : "400px",
                title : "查看附件",
                visible : false,
                modal : true,
                closable : true,
                actions : [ "Pin", "Minimize", "Maximize", "Close" ]
            }).data("kendoWindow").center().open();
        	window.location.Reload();
        }
        //申报登记编辑
        vm.updateFillin = function (){   	   
    	   signSvc.updateFillin(vm);  	   
        }                  
       
       //根据协办部门查询用户
       vm.findOfficeUsersByDeptName =function(status){
    	   signSvc.findOfficeUsersByDeptName(vm,status);
       }
       $("input").click(function(){
//    	   signSvc.checkboxAdd(vm);
       })
	}
})();
