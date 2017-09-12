(function () {
    'use strict';

    angular.module('app').controller('filingEditCtrl', filing);

    filing.$inject = ['bsWin', '$scope', 'addRegisterFileSvc', 'filingSvc','$state'];

    function filing(bsWin, $scope, addRegisterFileSvc, filingSvc,$state) {
        var vm = this;
        vm.title = '课题研究存档表';
        vm.filing = {};
        vm.zlList = [];     //新增的资料列表

        activate();
        function activate() {
            filingSvc.findByTopicId($state.params.topicId,function(data){
                vm.filing = data;
                vm.filing.topicId = $state.params.topicId;
            })
        }

        //S_保存存档信息
        vm.saveFiling = function(){
            filingSvc.save(vm.filing,function (data) {
                if(data.flag || data.reCode == 'ok'){
                    vm.filing = data.reObj;
                    vm.filing.topicId = $state.params.topicId;
                    bsWin.alert("保存成功！");
                }else{
                    bsWin.error(data.reMsg);
                }
            })
        }//E_saveFiling

        //S_新增资料
        vm.addZL = function(){
            if(!vm.filing.registerFileDto){
                vm.filing.registerFileDto = [];
            }
            var newFile = {};
            newFile.id = common.uuid();
            vm.filing.registerFileDto.push(newFile);
        }//E_addZL

        //S_删除资料
        vm.delZL = function(){
            var isCheck = $("#filingform input[tit='dynamicCheckbox']:checked");
            if (isCheck.length < 1) {
                bsWin.alert("请选择要删除的意见！");
            } else {
                var ids = [];
                for (var i = 0; i < isCheck.length; i++) {
                    if(isCheck[i].value){
                        alert(isCheck[i].value);
                        ids.push(isCheck[i].value);
                    }
                    $.each(vm.filing.registerFileDto,function(c,obj){
                        if(obj.id == isCheck[i].value ){
                            vm.filing.registerFileDto.splice(c, 1);
                        }
                    })
                }
                if(ids.length > 0){
                    addRegisterFileSvc.deleteByIds(ids.join(","),function(data){
                        bsWin.alert("删除成功！");
                    });
                }
            }
        }//E_delZL

        //S_初始化input框的值
        vm.initInputValue = function($event,defaultValue){
            var checkbox = $event.target;
            var checked = checkbox.checked;
            if (checked && !defaultValue) {
                return 1;
            }else{
                return defaultValue;
            }
        }//E_initInputValue
    }
})();
