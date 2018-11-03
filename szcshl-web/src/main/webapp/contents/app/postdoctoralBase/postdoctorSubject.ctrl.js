(function(){
    'use strict';
    angular.module('app').controller('postdoctorSubjectCtrl' , postdoctorSubject);
    postdoctorSubject.$inject = ['postdoctorSubjectSvc'];

    function postdoctorSubject(postdoctorSubjectSvc){
        var vm = this ;
        vm.title = "博士后课题列表";


        activate();
        function activate(){
            postdoctorSubjectSvc.subjectGrid(vm);
        }

        /**
         * 删除课题
         * @param id
         */
        vm.deleteSubject = function(id){

        }
    }
})();