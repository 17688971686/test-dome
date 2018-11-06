(function () {
    'use strict';

    angular.module('app').controller('postdoctoralBaseCtrl', postdoctoralBase);

    postdoctoralBase.$inject = ['postdoctoralBaseSvc' , 'bsWin' , '$scope' , "$state" , 'sysfileSvc'];

    function postdoctoralBase(postdoctoralBaseSvc , bsWin , $scope , $state , sysfileSvc) {
        var vm = this;
        vm.title = '博士后基地管理';  //标题
        vm.postdoctoralBase = {};
        vm.businessFlag = {};
        vm.id = $state.params.id;

        active();
        function active(){
            postdoctoralBaseSvc.postdoctoralBaseGrid(vm);
            if(vm.id){
                vm.isShowUpdate = true;
                postdoctoralBaseSvc.findPostdoctoralBaseById(vm.id , function(data){
                    vm.postdoctoralBase = data;
                });
            }
        }

        /**
         * 保存博士后基地
         */
        vm.createPostdoctoralBase = function(){
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                postdoctoralBaseSvc.createPostdoctoralBase(vm, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        vm.postdoctoralBase = data.reObj;
                        vm.isShowUpdate = true;
                        bsWin.alert("保存成功！");
                    }
                });
            }
        }

        /**
         * 更新博士后基地
         * @constructor
         */
        vm.updatePostdoctoralBase = function(){
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                postdoctoralBaseSvc.updatePostdoctoralBase(vm, function (data) {
                    bsWin.alert("保存成功！");
                });
            }
        }

        /**
         * 删除信息
         * @param id
         */
        vm.deletePostdoctoralBase = function(id){
            bsWin.confirm("确定删除？" , function(){
                postdoctoralBaseSvc.deletePostdoctoralBase(id , function(data){
                    if(data.flag || data.reCode == 'ok'){
                        bsWin.alert("删除成功！");
                        vm.gridOptions.dataSource.read();
                    }
                });
            });

        }

    }
})();
