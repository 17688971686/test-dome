(function () {
    'use strict';

    angular.module('app').controller('signFillinCtrl', sign);

    sign.$inject = ['signSvc', 'sysfileSvc','$state', '$http','bsWin'];

    function sign(signSvc,sysfileSvc, $state, $http,bsWin) {
        var vm = this;
        vm.model = {};		//创建一个form对象
        vm.title = '填写报审登记表';        		//标题
        vm.model.signid = $state.params.signid;	//收文ID
        vm.flowDeal = false;		//是否是流程处理标记

        vm.busiObj ={};             //业务对象，用于记录页面操作对象等信息

        active();
        function  active(){
            signSvc.initFillData(vm.model.signid,function(data){
                vm.model = data.reObj.sign;
                vm.deptlist = data.reObj.deptlist

                if (data.reObj.mainOfficeList) {
                    vm.mainOfficeList = data.reObj.mainOfficeList;
                }
                if (data.reObj.assistOfficeList) {
                    vm.assistOfficeList = data.reObj.assistOfficeList;
                }
                //建设单位和编制单位
                vm.companyList = data.reObj.companyList;
                //分管领导信息
                vm.busiObj.leaderList = data.reObj.leaderList;

                //初始化附件上传
                if(vm.model.signid){
                    sysfileSvc.initUploadOptions({
                        businessId:vm.model.signid,
                        sysSignId :vm.model.signid,
                        sysfileType:"审批登记表",
                        uploadBt:"upload_file_bt",
                        detailBt:"detail_file_bt",
                        vm:vm
                    });
                }
            });
        }

        //选择默认办理部门
        vm.checkOrgType = function($event){
            var checkbox = $event.target;
            var checked = checkbox.checked;
            var checkboxValue = checkbox.value;
            if(checked){
                vm.model.leaderName = signcommon.getDefaultLeader(checkboxValue);
                vm.model.comprehensivehandlesug = signcommon.getDefaultZHBYJ(checkboxValue);
                vm.model.comprehensiveName = '综合部';
                //设置综合部和分管领导ID
                $.each(vm.busiObj.leaderList,function(i,leader){
                    console.log(leader.mngOrgType+"---"+checkboxValue)
                    if(leader.mngOrgType == checkboxValue){
                        vm.model.leaderId = leader.id;
                        vm.model.leaderName = leader.displayName;
                        vm.model.comprehensivehandlesug = "请"+(leader.displayName).substring(0,2)+"主任阅示。";
                    }
                })
                if(!vm.model.leaderId){
                    bsWin.alert("选择的默认办理部门没有合适的分管领导，请先设置分管领导角色用户！");
                }
                var date = new Date();
                var monthValue = (date.getMonth()+1) < 10 ?"0"+(date.getMonth()+1):(date.getMonth()+1);
                var dayValue = (date.getDate()) < 10 ?"0"+(date.getDate()):(date.getDate());
                vm.model.comprehensiveDate = (date.getFullYear()+"-"+monthValue+"-"+dayValue);
            }
        }

        //发起流程
        vm.startNewFlow = function(){
            bsWin.confirm({
                title: "询问提示",
                message: "确定发起流程么",
                onOk: function () {
                    $('.confirmDialog').modal('hide');
                    var httpOptions = {
                        method : 'post',
                        url : rootPath+"/sign/startNewFlow",
                        params : {
                            signid:vm.model.signid
                        }
                    }
                    var httpSuccess = function success(response) {
                        if(response.data.reCode == "ok"){
                            bsWin.success("操作成功！");
                        }else{
                            bsWin.error(response.data.reMsg);
                        }
                    }
                    common.http({
                        vm : vm,
                        $http : $http,
                        httpOptions : httpOptions,
                        success : httpSuccess
                    });
                }
            });
        }

        //打印预览
        vm.signPreview = function (oper) {

            var htmlBody=$(".well").parents("body");
            var htmlsidebar=htmlBody.find(".main-sidebar");
            var htmlhedaer=htmlBody.find(".main-header");
            var htmlContentwrapper=htmlBody.find(".content-wrapper");
            //隐藏不需打印的区域;
             htmlsidebar.hide();
             htmlhedaer.hide();
             $(".toolbar").hide();

            //修改打印显示样式

                 //添加替换input的显示内容，打印后自动删除
                     $(".well input").each(function(){
                         var inptTpye=$(this).attr("type");
                         if(inptTpye=="text"){
                                 $(this).before('<span class="printmesge" data="text" style="white-space : nowrap;">'+$(this).val()+'</span>');
                         };
                         if(inptTpye=="checkbox"){
                             if($(this).is(':checked')){
                                 $(this).before('<span class="printmesge" data="text">有</span>');
                             }else{
                                 $(this).before('<span class="printmesge" data="text">无</span>');
                             }
                         }
                     });
                   $(".printmesge").show();
                   $(".well input[type=text]").hide();
                   $(".well input[type=checkbox]").hide();
                   $(".well button").hide();
                   htmlContentwrapper.find("td div select").hide();
                   htmlContentwrapper.find("td div span").css("margin","0");

               /*自定义表格样式*/
             $(".well").addClass("printbody");
             $(".well .table-bordered").addClass("tableBOX");
             htmlContentwrapper.find("input").addClass("noborder");
             htmlContentwrapper.addClass("nomargin");

             window.print();

         // 恢复原有
            htmlsidebar.show();
            htmlhedaer.show();
            $(".toolbar").show();
            $(".printmesge").hide();
            $(".well input[type=text]").show();
            $(".well input[type=checkbox]").show();
            $(".well button").show();
            htmlContentwrapper.find("td div select").show();
            htmlContentwrapper.find("td div span").css("margin-left","100px");
            $(".well").removeClass("printbody");
            $(".well .table-bordered").removeClass("tableBOX");
            $("[data=text]").remove();//删除临时添加的内容

            htmlContentwrapper.find("input").removeClass("noborder");
            htmlContentwrapper.removeClass("nomargin");


          /* if (oper < 5) {
               /!* var bdhtml = window.document.table.innerHTML;//获取当前页的html代码
                var sprnstr = "<!--startprint" + oper + "   ";//设置打印开始区域
                var eprnstr = "<!--endprint" + oper + "-->";//设置打印结束区域
                var prnhtml = bdhtml.substring(bdhtml.indexOf(sprnstr) + 10); //从开始代码向后取html
                var prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));//从结束代码向前取html
                window.document.table.innerHTML = prnhtml;
                window.print();
                window.document.table.innerHTML = bdhtml;*!/

            } else {
                window.print();
            }*/
        }

        //申报登记编辑
        vm.updateFillin = function () {
            vm.isSubmit = true;
            signSvc.updateFillin(vm.model,function (data) {
                vm.isSubmit = false;
                bsWin.alert("操作成功！");
            });
        }

        //根据协办部门查询用户
        vm.findOfficeUsersByDeptName = function (status) {
            var param = {};
            if ("main" == status) {
                param.maindeptName = vm.model.maindeptName;
            } else {
                param.assistdeptName = vm.model.assistdeptName;
            }
            signSvc.findOfficeUsersByDeptName(param,function(data){
                if ("main" == status) {
                    vm.mainOfficeList = {};
                    vm.mainOfficeList = data;
                } else {
                    vm.assistOfficeList = {};
                    vm.assistOfficeList = data;
                }
            });
        }

    }
})();
