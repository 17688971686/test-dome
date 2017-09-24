(function () {
    'use strict';

    angular.module('app').controller('addSuppLetterQueryEditCtrl', addSuppLetterQuery);

    addSuppLetterQuery.$inject = ['$location', 'addSuppLetterQuerySvc', '$state','bsWin'];

    function addSuppLetterQuery($location, addSuppLetterQuerySvc, $state,bsWin) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加登记补充资料';
        vm.isuserExist = false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新登记补充资料';
        }
       
        //部长审批处理
        vm.saveSuppletterApprove = function () {
        	 /* common.initJqValidation();
              var isValid = $('suppletter_form').valid();
             if(isValid){
              }else{
              	bsWin.alert("请填写审批意见，再提交！");
              }*/
        	addSuppLetterQuerySvc.createaddSuppLetterQuery(vm);
            
        };
        vm.update = function () {
            addSuppLetterQuerySvc.updateaddSuppLetterQuery(vm);
        };
        activate();
        function activate() {
        	//查看补充资料详细信息
            addSuppLetterQuerySvc.getaddSuppLetterQueryById(vm);
            
        }
    }
})();
