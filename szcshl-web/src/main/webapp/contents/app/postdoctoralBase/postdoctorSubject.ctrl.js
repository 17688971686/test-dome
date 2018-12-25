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
            vm.id = id;
            bsWin.confirm("确定删除？" , function(){
                postdoctorSubjectSvc.deleteSubject(vm , function(data){
                    if (data.flag || data.reCode == 'ok') {
                        vm.subject = data.reObj;
                        bsWin.success("删除成功！");
                        vm.subjectGridOptions.dataSource.read();
                    } else {
                        bsWin.error(data.reMsg);
                    }
                })
            })

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

        /**
         * 查看详情页
         * @param id
         */
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