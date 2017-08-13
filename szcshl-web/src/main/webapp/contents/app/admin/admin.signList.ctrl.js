(function () {
    'use strict';

    angular.module('app').controller('adminSignListCtrl', admin);

    admin.$inject = ['signSvc', 'adminSvc','bsWin'];

    function admin(signSvc, adminSvc,bsWin) {
        var vm = this;
        vm.title = '项目查询统计';
        vm.currentAssociateSign = {};
        activate();
        function activate() {
            //初始化查询参数
            adminSvc.initSignList(function(data){
                if(data.flag || data.reCode == 'ok'){
                    vm.orgsList = data.reObj;
                }
            });
            adminSvc.getSignList(vm);
            //项目关联初始化
            signSvc.associateGrid(vm);
        }

        //重置
        vm.formReset = function () {
            var tab = $("#searchform").find('input,select');
            $.each(tab, function (i, obj) {
                obj.value = "";
            });
        }

        //項目查詢統計
        vm.searchSignList = function () {
            vm.signListOptions.dataSource.read();
        }

        //项目关联弹窗
        vm.showAssociate = function(signid){
            signSvc.initDetailData(signid,function(data){
                vm.currentAssociateSign = data;
                vm.associateGridOptions.dataSource.read();
                //选中要关联的项目
                $("#associateWindow").kendoWindow({
                    width: "80%",
                    height: "625px",
                    title: "项目关联",
                    visible: false,
                    modal: true,
                    closable: true,
                    actions: ["Pin", "Minimize", "Maximize", "close"]
                }).data("kendoWindow").center().open();
            })
        }//end_项目关联弹窗

        //start 保存项目关联
        vm.saveAssociateSign = function(associateSignId){
            if(vm.currentAssociateSign.signid == associateSignId){
                bsWin.alert("不能关联自身项目");
                return ;
            }
            signSvc.saveAssociateSign(vm.currentAssociateSign.signid,associateSignId,function(){
                vm.associateGridOptions.dataSource.read();
                vm.signListOptions.dataSource.read();
                bsWin.alert(associateSignId != undefined ? "项目关联成功" : "项目解除关联成功");
            });
        }
        //end 保存项目关联

        //start 查询功能
        vm.associateQuerySign = function(){
            vm.associateGridOptions.dataSource.read();
        }//end 查询功能

        //start 解除项目关联
        vm.disAssociateSign = function(signid){
            signSvc.saveAssociateSign(signid,null,function(){
                bsWin.alert("项目解除关联成功");
                vm.associateGridOptions.dataSource.read();
                vm.signListOptions.dataSource.read();
            });
        }
        //end 解除项目关联
    }
})();
