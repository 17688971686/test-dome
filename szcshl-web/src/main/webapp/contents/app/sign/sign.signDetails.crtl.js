(function () {
    'use strict';

    angular.module('app').controller('signDetailsCtrl', sign);

    sign.$inject = ['$location','signSvc','$state','flowSvc'];

    function sign($location, signSvc,$state,flowSvc) {
        var vm = this;
    	vm.model = {};							//创建一个form对象   	
        vm.title = '查看详情信息';        			//标题
        vm.model.signid = $state.params.signid;	//收文ID
       
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

            // 初始化业务信息
            signSvc.initFlowPageData(vm.model.signid,function(data){
                vm.model = data;
                //有关联，则显示项目
                if(vm.model.isAssociate && vm.model.isAssociate == 1){
                    signSvc.initAssociateSigns(vm,vm.model.signid);
                    //没有则初始化关联表格
                }
                //发文
                if (vm.model.dispatchDocDto) {
                    vm.dispatchDoc = vm.model.dispatchDocDto;
                }
                //归档
                if (vm.model.fileRecordDto) {
                    vm.fileRecord = vm.model.fileRecordDto;
                }

                if(vm.model.processInstanceId ){
                    vm.flow = {};
                    vm.flow.processInstanceId = vm.model.processInstanceId;
                    if(vm.model.processState ==0 ||  vm.model.processState == 9){
                        vm.flow.hideFlowImg = true;
                    }
                    flowSvc.initFlowData(vm);
                }
            });
            

        }

    }
})();
