(function () {
    'use strict';

    angular.module('app').controller('monthlyMultiyearCtrl', monthlyMultiyear);

    monthlyMultiyear.$inject = ['$location', 'monthlyMultiyearSvc','$state','bsWin','$rootScope'];

    function monthlyMultiyear($location, monthlyMultiyearSvc,$state,bsWin,$rootScope) {
        var vm = this;
        vm.title = '年度月报简报';
        vm.suppletter = {};
        vm.suppletter.fileYear = $state.params.year;
      //给年份赋值
        $("#fileYear").val(vm.suppletter.fileYear);
        //获取到当前的列表
        vm.stateName = $state.current.name;
        //查询参数
        vm.queryParams = {};
        //点击时。保存查询的条件和grid列表的条件
        vm.saveView = function(){
            $rootScope.storeView(vm.stateName,{gridParams:vm.monthlyYearGrid.dataSource.transport.options.read.data(),queryParams:vm.queryParams,data:vm});

        }

        activate();
        function activate() {
            if($rootScope.view[vm.stateName]){
                var preView = $rootScope.view[vm.stateName];
                //恢复grid
                if(preView.gridParams){
                    vm.gridParams = preView.gridParams;
                }
                //恢复表单参数
                if(preView.data){
                    vm.model = preView.data.model;
                }
                //恢复数据
                /*vm.project = preView.data.project;*/
                //恢复页数页码
                if(preView.queryParams){
                    vm.queryParams=preView.queryParams;
                }

                monthlyMultiyearSvc.monthlyYearGrid(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            }else {
                monthlyMultiyearSvc.monthlyYearGrid(vm);
            }

            monthlyMultiyearSvc.findAllOrg(vm);
        }

        //查询
         vm.addSuppQuery = function(){
         	 monthlyMultiyearSvc.addSuppQuery(vm);
         }
         //重置
         vm.resetAddSupp = function(){
        	 var tab = $("#form_monthly").find('input,select');
 			$.each(tab, function(i, obj) {
 				obj.value = "";
 			});
         }
         
        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    monthlyMultiyearSvc.deletemonthlyMultiyear(vm, id);
                }
            });
        }
        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: '请选择数据'
                });
            } else {
                var ids = [];
                for (var i = 0; i < selectIds.length; i++) {
                    ids.push(selectIds[i].value);
                }
                var idStr = ids.join(',');
                vm.del(idStr);
            }
        };
    }
})();
