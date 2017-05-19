(function () {
    'use strict';

    angular.module('app').controller('fileRecordEditCtrl', fileRecord);

    fileRecord.$inject = ['$location','fileRecordSvc','$state']; 

    function fileRecord($location, fileRecordSvc,$state) {     
        var vm = this;
        vm.title = '项目归档编辑';

        vm.fileRecord = {};
        vm.fileRecord.signId = $state.params.signid;
        vm.signId = $state.params.signid;
        	
        activate();
        function activate(){
        	fileRecordSvc.initFileRecordData(vm);
        }
                
        vm.create = function(){
        	fileRecordSvc.saveFileRecord(vm);
        }
    }
})();
