(function(){
    'use strict';
    angular.module('app').controller('postdoctorSubjectCtrl' , postdoctorSubject);
    postdoctorSubject.$inject = ['postdoctorSubjectSvc' , '$state' , 'bsWin'];

    function postdoctorSubject(postdoctorSubjectSvc , $state , bsWin){
        var vm = this ;
        vm.title = "博士后课题列表";
        vm.searchModel = {};


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

        /**
         * 重置
         */
        vm.formReset = function(){
            vm.searchModel = {};
        }

        /**
         * 查询
         */
        vm.searchForm = function(){
            vm.subjectGridOptions.dataSource._skip=0;
            vm.subjectGridOptions.dataSource.read();
        }

        vm.details = function(id){
            postdoctorSubjectSvc.isPermission(function(data){
                if(data.flag || data.reCode == 'ok'){
                    $state.go('postdoctoralSubjectDetail' , {id : id})
                }else{
                    bsWin.error(data.reMsg);
                }
            })

        }

    }
})();