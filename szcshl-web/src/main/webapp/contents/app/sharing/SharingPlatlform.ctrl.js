(function () {
    'use strict';

    angular.module('app').controller('sharingPlatlformCtrl', sharingPlatlform);

    sharingPlatlform.$inject = ['$location', '$state', '$http', 'sharingPlatlformSvc','$rootScope','monthlyMultiyearSvc'];

    function sharingPlatlform($location, $state, $http, sharingPlatlformSvc,$rootScope,monthlyMultiyearSvc) {
        var vm = this;
        vm.title = '共享资料管理';
        //获取到当前的列表
        vm.stateName = $state.current.name;
        //查询参数
        vm.queryParams = {};
        //点击时。保存查询的条件和grid列表的条件
        vm.saveView = function(){
            $rootScope.storeView(vm.stateName,{gridParams:vm.gridOptions.dataSource.transport.options.read.data(),queryParams:vm.queryParams,data:vm});

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

                sharingPlatlformSvc.grid(vm);
                //清除返回页面数据
                $rootScope.view[vm.stateName] = undefined;
            }else {
                sharingPlatlformSvc.grid(vm);
            }

           /* monthlyMultiyearSvc.findAllOrg(vm);
            monthlyMultiyearSvc.findAllUser(vm);*/

        }

        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    sharingPlatlformSvc.deleteSharingPlatlform(vm, id);
                }
            });
        }
        vm.dels = function () {
            var selectIds = common.getKendoCheckId('.grid');
            if (selectIds.length == 0) {
                common.alert({
                    vm: vm,
                    msg: '请选择要删除的数据'
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

        /**
         * 批量发布
         */
        vm.bathPublish = function () {
            sharingPlatlformSvc.updatePublish(vm, true);
        }

        /**
         * 批量取消发布
         */
        vm.bathDown = function () {
            sharingPlatlformSvc.updatePublish(vm, false);
        }

        //查询
        vm.querySharing = function () {
            vm.gridOptions.dataSource._skip=0;
            vm.gridOptions.dataSource.read();
        }

        /**
         * 重置
         */
        vm.resetSharing = function(){
            var tab = $("#formSharing").find('input,select');
            $.each(tab, function(i, obj) {
                obj.value = "";
            });
        }

    }
})();
