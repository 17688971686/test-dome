(function () {
    'use strict';

    angular.module('app').controller('archivesLibraryCtrl', archivesLibrary);

    archivesLibrary.$inject = ['$location', 'archivesLibrarySvc','bsWin'];

    function archivesLibrary($location, archivesLibrarySvc,bsWin) {
        var vm = this;
        vm.model = {};
        vm.title = '档案借阅管理';

      //保存中心档案借阅
        vm.createLibrary = function () {
        	 common.initJqValidation();
             var isValid = $('form').valid();
           if(isValid){
            	archivesLibrarySvc.createArchivesLibrary(vm.model,function(data){
                    if (data.flag || data.reCode == "ok") {
                            bsWin.alert("操作成功！");
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }
        };
        
        //保存市档案借阅
        vm.createCityLibrary =function(){
        	 common.initJqValidation();
             var isValid = $('form').valid();
           if(isValid){
            	archivesLibrarySvc.createCityLibrary(vm.model,function(data){
                    if (data.flag || data.reCode == "ok") {
                            bsWin.alert("操作成功！");
                    }else{
                        bsWin.error(data.reMsg);
                    }
                });
            }
        }
       
        
        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    archivesLibrarySvc.deleteArchivesLibrary(vm, id);
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

        activate();
        function activate() {
            archivesLibrarySvc.grid(vm);
            //中心档案查询列表
            archivesLibrarySvc.centerLibraryGrid(vm);
            //市档案查询列表
            archivesLibrarySvc.cityGridOptions(vm);
        }
    }
})();
