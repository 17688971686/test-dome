(function () {
    'use strict';

    angular.module('app').controller('signDetailsCtrl', sign);

    sign.$inject = ['$location','signSvc','$state','flowSvc'];

    function sign($location, signSvc,$state,flowSvc) {
        var vm = this;
    	vm.model = {};							//创建一个form对象   	
        vm.title = '查看详情信息';        			//标题
        vm.model.signid = $state.params.signid;	//收文ID
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
            expertReviews : []
        }
       
        active();
        function active(){
            $('#myTab li').click(function (e) {
                var aObj = $("a",this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#"+showDiv).addClass("active").addClass("in").show(500);
            })

            flowSvc.initFlowData(vm);

            // 初始化业务信息
            signSvc.initFlowPageData(vm.model.signid,function(data){
                vm.model = data;
                //有关联，则显示项目
                if(vm.model.isAssociate && vm.model.isAssociate == 1){
                    signSvc.initAssociateSigns(vm,vm.model.signid);
                    //没有则初始化关联表格
                }
                //工作方案
                if(vm.model.processState > 2){
                    vm.showFlag.tabWorkProgram=true;
                    //初始化专家评分
                    signSvc.paymentGrid(vm.model.signid,function(data){
                        vm.businessFlag.expertReviews = data.value;
                        if (vm.businessFlag.expertReviews && vm.businessFlag.expertReviews.length > 0) {
                            vm.showFlag.tabExpert = true;   //显示专家信息tab
                        }
                    });
                }
                //发文
                if (vm.model.dispatchDocDto) {
                    vm.showFlag.tabDispatch=true;
                    vm.dispatchDoc = vm.model.dispatchDocDto;
                }
                //归档
                if (vm.model.fileRecordDto) {
                    vm.showFlag.tabFilerecord=true;
                    vm.fileRecord = vm.model.fileRecordDto;
                }

                //处理记录
                if(vm.model.processInstanceId ){
                    vm.flow = {};
                    vm.flow.processInstanceId = vm.model.processInstanceId;
                    if(vm.model.processState ==0 ||  vm.model.processState == 9){
                        vm.flow.hideFlowImg = true;
                    }
                }
            });
            

        }

    }
})();
