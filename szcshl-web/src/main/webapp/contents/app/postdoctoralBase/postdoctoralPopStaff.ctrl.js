(function () {
    'use strict';

    angular.module('app').controller('postdoctoralPopStaffCtrl', postdoctoralPopStaffCtrl);

    postdoctoralPopStaffCtrl.$inject = ['postdoctoralStaffSvc' , 'bsWin' , '$scope' , "$state" , 'sysfileSvc'];

    function postdoctoralPopStaffCtrl(postdoctoralStaffSvc , bsWin , $scope , $state , sysfileSvc) {
        var vm = this;
        vm.title = '出站人员列表';  //标题
        vm.postdoctoralStaff = {};
        vm.businessFlag = {};
        vm.searchModel = {};
        vm.id = $state.params.id;
        //初始化附件上传控件
        vm.initFileUpload = function () {
            if (!vm.id) {
                //监听ID，如果有新值，则自动初始化上传控件
                $scope.$watch("vm.id", function (newValue, oldValue) {
                    if (newValue && newValue != oldValue && !vm.initUploadOptionSuccess) {
                        vm.initFileUpload();
                    }
                });
            }

            //创建附件对象
            vm.sysFile = {
                businessId: vm.id,
                mainId: vm.id,
                mainType: "博士后在站人员附件",
                sysBusiType: "",
                showBusiType: false,
            };
            sysfileSvc.initUploadOptions({
                inputId: "sysfileinput",
                vm: vm,
                uploadSuccess: function () {
                    sysfileSvc.findByBusinessId(vm.id, function (data) {
                        vm.sysFilelists = data;
                    });
                }
            });
        }
        active();
        function active(){
            postdoctoralStaffSvc.postdoctoralPopStaffGrid(vm);
            if(vm.id){
                vm.isShowUpdate = true;
                postdoctoralStaffSvc.findPostdoctoralStaffById(vm.id , function(data){
                    vm.postdoctoralStaff = data;
                    vm.id = vm.postdoctoralStaff.id;
                });
            }
            //初始化上传附件
            vm.initFileUpload();
        }

        /**
         * 保存博士后人员
         */
        vm.commitPostdoctoralStaff = function(){
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.postdoctoralStaff.status = '4';
                postdoctoralStaffSvc.createPostdoctoralStaff(vm, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        vm.postdoctoralStaff = data.reObj;
                        vm.id = vm.postdoctoralStaff.id;
                        vm.isShowUpdate = true;
                        vm.initFileUpload();
                        bsWin.alert("保存成功！");
                    }else{
                        bsWin.alert(data.reMsg);
                    }
                });
            }
        }



        /**
         * 更新博士后基地
         * @constructor
         */
        vm.commitPostdoctoralPopStaff = function(){
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                vm.postdoctoralStaff.status = "3";
                postdoctoralStaffSvc.updatePostdoctoralStaff(vm, function (data) {
                    bsWin.alert("提交成功！");
                });
            }
        }

        /**
         * 删除信息
         * @param id
         */
        vm.deletePostdoctoralStaff = function(id){
            bsWin.confirm("确定删除？" , function(){
                postdoctoralStaffSvc.deletePostdoctoralStaff(id , function(data){
                    if(data.flag || data.reCode == 'ok'){
                        bsWin.alert("删除成功！");
                        vm.gridOptions.dataSource.read();
                    }
                });
            });
        }

        /**
         * 审核信息
         * @param id
         */
        vm.approvePostdoctoralStaff = function(id,status){
            if(status == 'undefined' || status == undefined || status == null || status == ''){
                status = "";
            }
            bsWin.confirm("确定审核？" , function(){
                postdoctoralStaffSvc.approvePostdoctoralStaff(id,status , function(data){
                    if(data.flag || data.reCode == 'ok'){
                        bsWin.alert(data.reMsg);
                        vm.gridOptions.dataSource.read();
                    }else{
                        bsWin.alert(data.reMsg);
                    }
                });
            });
        }

        /**
         * 回退信息
         * @param id
         */
        vm.backPostdoctoralStaff = function(id,status){
            if(status == 'undefined' || status == undefined || status == null || status == ''){
                status = "";
            }
            bsWin.confirm("确定回退？" , function(){
                if(status == '2'){
                    status = '0'
                }else if(status == '4'){
                    status = '2'
                }
                postdoctoralStaffSvc.backPostdoctoralStaff(id,status , function(data){
                    if(data.flag || data.reCode == 'ok'){
                        bsWin.alert(data.reMsg);
                        vm.gridOptions.dataSource.read();
                    }else{
                        bsWin.alert(data.reMsg);
                    }
                });
            });
        }

        //表单查询
        vm.searchForm = function(){
            vm.gridOptions.dataSource._skip=0;
            vm.gridOptions.dataSource.read();
        }

        //重置查询表单
        vm.formReset = function(){
            vm.searchModel = {};
        }

    }
})();
