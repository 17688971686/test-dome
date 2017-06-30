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

        //主办处
        vm.mainDeptUser = function(){
        	if(!vm.model.maindeptName){
                 common.alert({
                     vm:vm,
                     msg:"请选择办事处，再选择选择联系人！"
                 })
             }
        }
        //协办处
        vm.assistDeptUser = function(){
        	if(!vm.model.assistdeptName){
                common.alert({
                    vm:vm,
                    msg:"请选择办事处，再选择选择联系人！"
                })
            }
        }
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
