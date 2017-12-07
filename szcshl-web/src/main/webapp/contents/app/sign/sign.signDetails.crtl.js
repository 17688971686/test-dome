(function () {
    'use strict';

    angular.module('app').controller('signDetailsCtrl', sign);

    sign.$inject = ['sysfileSvc','signSvc','$state','flowSvc','$scope','templatePrintSvc' , 'assistSvc'];

    function sign(sysfileSvc, signSvc,$state,flowSvc,$scope,templatePrintSvc , assistSvc) {
        var vm = this;
    	vm.model = {};							    //创建一个form对象
        vm.flow = {};                               //收文对象
        vm.model.signid = $state.params.signid;	    //收文ID
        vm.flow.processInstanceId = $state.params.processInstanceId;	//流程实例ID
        //按钮显示控制，全部归为这个对象控制
        vm.showFlag = {
            tabWorkProgram:false,       // 显示工作方案标签tab
            tabBaseWP:false,            // 项目基本信息tab
            tabDispatch:false,          // 发文信息tab
            tabFilerecord:false,        // 归档信息tab
            tabExpert:false,            // 专家信息tab
            tabSysFile:false,           // 附件信息tab
            tabAssociateSigns:false,    // 关联项目tab
        };
        //业务控制对象
        vm.businessFlag = {
            expertReviews : [],         // 专家评审方案
        }

        vm.expertList =  new Array(15); //用于打印页面的专家列表，控制行数

        active();
        function active(){
            $('#myTab li').click(function (e) {
                var aObj = $("a",this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#"+showDiv).addClass("active").addClass("in").show(500);
                vm.model.showDiv = showDiv;
            })

            //流程图和流程处理记录信息
            if($state.params.processInstanceId){
                flowSvc.initFlowData(vm);
            }

            //初始化业务信息
            signSvc.initFlowPageData(vm.model.signid,function(data){
                vm.model = data;
                var deActive = $("#myTab .active");
                var deObj = $("a", deActive);
                vm.model.showDiv = deObj.attr("for-div");
                //发文
                if (vm.model.dispatchDocDto) {
                    vm.showFlag.tabDispatch = true;
                    vm.dispatchDoc = vm.model.dispatchDocDto;
                    assistSvc.findAssistPlanSignById(vm.model.signid , function(data){
                        vm.assistPlanSign = data;
                    })
                }
                //归档
                if (vm.model.fileRecordDto) {
                    vm.showFlag.tabFilerecord = true;
                    vm.fileRecord = vm.model.fileRecordDto;
                }

                //初始化专家评分
                if (vm.model.processState > 1) {
                    vm.showFlag.tabWorkProgram=true;        //显示工作方案
                }

                //显示拟补充资料函
                if(vm.model.suppLetterDtoList){
                    vm.showSupperIndex = 0;
                }
            });

            // 初始化上传附件
            sysfileSvc.findByMianId(vm.model.signid,function(data){
                if(data && data.length > 0){
                    vm.showFlag.tabSysFile = true;
                    vm.sysFileList = data;
                    sysfileSvc.initZtreeClient(vm,$scope);//树形图
                }
            });

        }
        //签收模板打印
        vm.printpage = function ($event) {
            templatePrintSvc.templatePrint($event.target,vm.model);
        }

        /**
         * 打印功能 - 分页
         */
        vm.templatePage = function(id){
            templatePrintSvc.templatePage(id);
        }
    }
})();
