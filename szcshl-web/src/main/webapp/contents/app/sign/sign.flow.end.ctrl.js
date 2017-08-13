(function () {
    'use strict';

    angular.module('app').controller('signEndCtrl', sign);

    sign.$inject = ['$location','signSvc','$state','flowSvc'];

    function sign($location,signSvc,$state,flowSvc) {
        var vm = this;
        vm.title = "已办结项目详情";
        vm.model = {};
        vm.flow = {};
        vm.model.signid = $state.params.signid;   //业务ID
        vm.flow.processInstanceId = $state.params.processInstanceId;	//流程实例ID
        vm.flow.hideFlowImg = true;

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
            //初始化业务信息
            signSvc.initFlowPageData(vm.model.signid,function(data){
                vm.model = data;
                //有关联，则显示项目
                if(vm.model.isAssociate && vm.model.isAssociate == 1){
                    signSvc.initAssociateSigns(vm,vm.model.signid);
                }
                //发文
                if (vm.model.dispatchDocDto) {
                    vm.showFlag.tabDispatch = true;
                    vm.dispatchDoc = vm.model.dispatchDocDto;
                    //如果是合并发文次项目，则不用生成发文编号
                    if((vm.dispatchDoc.dispatchWay == 2 && vm.dispatchDoc.isMainProject == 0)
                        || vm.dispatchDoc.fileNum){
                        vm.businessFlag.isCreateDisFileNum = true;
                    }else{
                        vm.showFlag.buttDisFileNum = true;
                    }
                }
                //归档
                if (vm.model.fileRecordDto) {
                    vm.showFlag.tabFilerecord = true;
                    vm.fileRecord = vm.model.fileRecordDto;
                }
            });
        }
        //获取专家评星
        vm.getExpertStar = function(id ,score){
            var returnStr = "";
            if (score != undefined) {
                for (var i = 0; i <score; i++) {
                    returnStr += "<span style='color:gold;font-size:20px;'><i class='fa fa-star' aria-hidden='true'></i></span>";
                }
            }
            $("#"+id+"_starhtml").html(returnStr);
        }
    }
})();
