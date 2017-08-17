(function () {
    'use strict';

    angular.module('app').controller('addSuppLetterCtrl', addSuppLetter);
    addSuppLetter.$inject = ['$location', 'addSuppLetterSvc','$state','bsWin'];
    
    function addSuppLetter($location, addSuppLetterSvc,$state,bsWin) {
        var vm = this;
        vm.suppletter = {}; //补充资料对象$state
        vm.suppletter.signid=$state.params.signid;
        vm.title = '登记补充资料';
      
       //保存补充资料函
        vm.saveAddSuppletter = function(){
        	/* vm.isSubmit = true;
        	  addSuppLetterSvc.createAddSuppLetter(vm.suppletter,function(data){
        		  vm.isSubmit = false;
        		  bsWin.alert("保存成功！");
        	 });*/
        	 addSuppLetterSvc.createAddSuppLetter(vm);
        }
        //生成发文字号
        vm.getFilenum = function(){
        	if(!vm.suppletter.id){
        		bsWin.alert("请先保存拟补充资料函");
        	}else{
        		addSuppLetterSvc.createFilenum(vm);
        	}
        }
        
        //拟补充资料函正文
        vm.addSuppContent = function(){
        	vm.showsupp=true;
       	 var ideaEditWindow = $("#addsuppContent");
      		 ideaEditWindow.kendoWindow({
	            width: "50%",
	            height: "80%",
	            title: "拟补资料函正文",
	            visible: false,
	            modal: true,
	            closable: true,
	            actions: ["Pin", "Minimize", "Maximize", "close"]
	        }).data("kendoWindow").center().open();
        }
        
        vm.del = function (id) {
            common.confirm({
                vm: vm,
                title: "",
                msg: "确认删除数据吗？",
                fn: function () {
                    $('.confirmDialog').modal('hide');
                    addSuppLetterSvc.deleteAddSuppLetter(vm, id);
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
            //addSuppLetterSvc.grid(vm);
            addSuppLetterSvc.initSuppLetter(vm);
        }
    }
})();
