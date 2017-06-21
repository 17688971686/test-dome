(function () {
    'use strict';

    angular.module('app').controller('signFillinCtrl', sign);

    sign.$inject = ['$location', 'signSvc', '$state', '$http'];

    function sign($location, signSvc, $state, $http) {
        var vm = this;
        var options = this;
        vm.model = {};		//创建一个form对象
        vm.title = '填写报审登记表';        		//标题
        vm.model.signid = $state.params.signid;	//收文ID
     
        vm.flowDeal = false;		//是否是流程处理标记

        signSvc.initFillData(vm);

        //打印预览
        vm.signPreview = function (oper) {
            if (oper < 5) {
                /*bdhtml = window.document.table.innerHTML;//获取当前页的html代码
                sprnstr = "<!--startprint" + oper + "   ";//设置打印开始区域
                eprnstr = "<!--endprint" + oper + "-->";//设置打印结束区域
                prnhtml = bdhtml.substring(bdhtml.indexOf(sprnstr) + 10); //从开始代码向后取html
                prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));//从结束代码向前取html
                window.document.table.innerHTML = prnhtml;
                window.print();
                window.document.table.innerHTML = bdhtml;*/

            } else {
                window.print();
            }
        }

   
        //文件上传窗口
        vm.commonUploadWin = function () {
        	var signid =  vm.model.signid;
        	var fileType="收文";
            common.initcommonUploadWin({businessId: vm.model.signid},signid,fileType);
        }
        //查看附件
        vm.signQuery = function () {
            common.initcommonQueryWin(vm);
            vm.sysSignId = vm.model.signid;
            common.commonSysFilelist(vm, $http);
        }
        //删除系统文件
        vm.commonDelSysFile = function (id) {
            common.commonDelSysFile(vm, id, $http);
        }
        //附件下载
        vm.commonDownloadSysFile = function (id) {
            common.commonDownloadFile(vm, id);
        }

       
        //选主办处联系人判断
        vm.selecedMDUN=function(){
        	if(!vm.model.maindeptName){
        		common.alert({
                            vm : vm,
                            msg : "请先填写主办处室",
                            fn : function() {
                                vm.isSubmit = false;
                                $('.alertDialog').modal('hide');
                            }
                        })
        	}
        }
        
        //选协办处联系人判断
        vm.selectADUN=function(){
        	if(!vm.model.assistdeptName){
        		common.alert({
                            vm : vm,
                            msg : "请先填写主办处室",
                            fn : function() {
                                vm.isSubmit = false;
                                $('.alertDialog').modal('hide');
                            }
                        })
        	}
        }
        
        
        //附件上传
        vm.signUpload = function () {
            $("#signUploadWin").kendoWindow({
                width: "660px",
                height: "400px",
                title: "附件上传",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
        }
        //查看附件
        vm.signJquery = function () {
            $("#signAttachments").kendoWindow({
                width: "800px",
                height: "400px",
                title: "查看附件",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Pin", "Minimize", "Maximize", "Close"]
            }).data("kendoWindow").center().open();
            signSvc.initFillData(vm);
        }
        //申报登记编辑
        vm.updateFillin = function () {
            signSvc.updateFillin(vm);
        }

        //根据协办部门查询用户
        vm.findOfficeUsersByDeptName = function (status) {
            signSvc.findOfficeUsersByDeptName(vm, status);
        }

    }
})();
