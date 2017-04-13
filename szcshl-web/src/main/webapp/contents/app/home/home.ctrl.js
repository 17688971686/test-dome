(function () {
    'use strict';

    angular
        .module('app')
        .controller('homeCtrl', home);

    home.$inject = ['$location','homeSvc']; 

    function home($location, homeSvc) {
        /* jshint validthis:true */
    	var vm = this;
        vm.title = '';
        

        vm.changePwd = function () {        	
        	 homeSvc.changePwd(vm);
          
        }
       
        activate();
        function activate() {
        }
    }
})();
