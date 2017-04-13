(function () {
    'use strict';

    angular
        .module('app')
        .controller('demoCtrl', demo);

    demo.$inject = ['$location','demoSvc']; 

    function demo($location, demoSvc) {
        /* jshint validthis:true */
    	var vm = this;
        vm.title = '';
        
        
        vm.showDialog=function(){
        	
        	 $('.myDialog').modal({
                 backdrop: 'static',
                 keyboard:false
             });
        }
        
        function datetimePicker(){
        	$("#datepicker").kendoDatePicker({
        		culture:'zh-CN'
        	});
        }
        
        function upload(){
        	 $("#files").kendoUpload({
                 async: {
                     saveUrl: "/demo/save",
                     removeUrl: "/demo/remove",
                     autoUpload: true
                 }
             });
        }
       
       
        activate();
        function activate() {
        	datetimePicker();
        	upload();
        }
    }
})();
