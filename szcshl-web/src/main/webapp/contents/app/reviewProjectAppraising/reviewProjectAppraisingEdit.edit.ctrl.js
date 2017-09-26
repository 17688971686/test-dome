(function(){
    'use strict';
    angular.module('app').controller('reviewProjectAppraisingEditCtrl' , reviewProjectAppraisingEdit);
    reviewProjectAppraisingEdit.$inject = ['reviewProjectAppraisingSvc'];
    function reviewProjectAppraisingEdit(reviewProjectAppraisingSvc){
        var vm = this ;
        vm.title = "评审项目评优列表";

        activate();
        function activate(){
            reviewProjectAppraisingSvc.endProjectGrid(vm);
        }


        vm.appraisingWindow = function(vm , signId){

        }
    }
})();