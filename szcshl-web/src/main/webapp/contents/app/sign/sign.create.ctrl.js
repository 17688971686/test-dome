(function () {
    'use strict';

    angular.module('app').controller('signCreateCtrl', sign);

    sign.$inject = ['$location','signSvc','$state']; 

    function sign($location, signSvc,$state) {        
        var vm = this;
    	vm.model = {};						//创建一个form对象
        vm.title = '新增收文';        		//标题          
        vm.ispresignHide=true;
        vm.ischangeHide=true;
        
        vm.reviewstageSelect=function(){
        	var selectValue=$("select[name=reviewstage]").find(":selected").val();
        	if(selectValue=="项目建议书"||selectValue=="可行性研究报告"||selectValue=="项目概算"){
        		vm.ispresignHide=false;
	        		if(selectValue=="项目概算"){
	        			vm.ischangeHide=false;
	        		}else{
	        			vm.ischangeHide=true;
	        		}
        	}else{
        		vm.ispresignHide=true;
        	}
        	
        	
        }
        
        vm.create = function () {
        	signSvc.createSign(vm);
        };       
    }
})();
