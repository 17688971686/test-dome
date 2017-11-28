(function () {
    'use strict';

    angular.module('app').controller('expertAuditCtrl', expert);

    expert.$inject = ['$location', 'expertSvc','templatePrintSvc'];

    function expert($location, expertSvc,templatePrintSvc) {
    	var vm = this;
        vm.title = "专家审核";

        activate();
        function activate() {
            expertSvc.auditGrid(vm);
        }

    	vm.searchAudit = function(){
    		expertSvc.searchAudit(vm);
    	}

    	//审核状态去到各状态
        vm.auditToOfficial = function() {
     	  expertSvc.auditTo(vm,2);
	    };
	    
	    vm.auditToAlternative=function() {
	      	expertSvc.auditTo(vm,3);
	    };
	    
	    vm.auditToStop=function() {
	      	expertSvc.auditTo(vm,4);
	    };
	    
	    vm.auditToRemove=function(){
	      	expertSvc.auditTo(vm,5);
	    };
	    
	    //各状态回到审核状态
	    vm.officialToAudit=function(){
	      	expertSvc.toAudit(vm,2);
	    };
	    
	    vm.alternativeToAudit=function(){
	      	expertSvc.toAudit(vm,3);
	    };
	    
	    vm.stopToAudit=function(){
	      	expertSvc.toAudit(vm,4);
	    };
	    
	    vm.temoveToAudit=function(){
	      	expertSvc.toAudit(vm,5);
	    };

        //S 查看专家详细
        vm.findExportDetail = function (id) {
            expertSvc.getExpertById(id, function (data) {
                vm.model = data;
                $("#auditExportDetail").kendoWindow({
                    width: "80%",
                    height: "auto",
                    title: "专家详细信息",
                    visible: false,
                    modal: true,
                    open:function(){
                        $("#expertPhotoSrc").attr("src", rootPath + "/expert/transportImg?expertId=" + vm.model.expertID + "&t=" + Math.random());
                       //tab标签
                        $('#myTab li').click(function (e) {
                            var aObj = $("a", this);
                            e.preventDefault();
                            aObj.tab('show');
                            var showDiv = aObj.attr("for-div");
                            $(".tab-pane").removeClass("active").removeClass("in");
                            $("#" + showDiv).addClass("active").addClass("in").show(500);
                        })
                        //项目签收编辑模板打印
                        vm.editPrint = function () {
                            var mb = templatePrintSvc.getBrowserType();
                            $("#expertApply").hide();
                            $("#auditExportDetail").data("kendoWindow").close();
                            $("#expertApply_templ").show();
                            $(".main-sidebar,#flow_form,.header,.breadcrumb,.toolbar,#myTab").addClass("print-hide");
                            $(".content-wrapper").addClass("print-content");
                            if(mb == 'IE'){
                                document.all.WebBrowser.ExecWB(7,1);
                            }else{
                                print();
                            }
                            $("#expertApply").show();
                            $("#auditExportDetail").data("kendoWindow").open();
                            $("#expertApply_templ").hide();
                            $(".main-sidebar,#flow_form,.header,.breadcrumb,.toolbar,#myTab,#wpTab").removeClass("print-hide");
                            $(".content-wrapper").removeClass("print-content");

                        }
                        //评审过项目
                        expertSvc.reviewProjectGrid(vm.model.expertID,function(data){
                            vm.isLoading = false;
                            if(data && data.length > 0){
                                vm.reviewProjectList = data;
                                vm.noData = false;
                            }else{
                                vm.noData = true;
                            }

                        });
                    },
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "Close"]
                }).data("kendoWindow").center().open();
            });
        }
        //S 查看专家详细

    }
})();
