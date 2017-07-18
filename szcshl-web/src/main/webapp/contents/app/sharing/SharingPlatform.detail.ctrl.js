(function () {
    'use strict';

    angular.module('app').controller('sharingDetailCtrl', sharingPlatlform);

    sharingPlatlform.$inject = ['$location', '$state','$http','sharingPlatlformSvc'];

    function sharingPlatlform($location,$state, $http,sharingPlatlformSvc) {
        var vm = this;
        
        vm.title = '资料共享详情页';
        vm.model = {}; //创建资料共享对象
        
        vm.model.sharId = $state.params.sharId;
      
        //打印
        vm.printText = function (){
        	var html ='<div style="width:80%;height:80%;text-align:center;margin:auto;font-family:\'Microsoft YaHei\'">'
        	+$("#context-print").html();
        	+"</div>";
        	
        	var newWindow;
        	var height=$(window).height();
        	var width = $(window).width();
        	newWindow = window.open('','','width='+width+',height='+height);
        	newWindow.document.body.innerHTML = html;
        	newWindow.print();
        }
        //弹出
        vm.alertWindow = function(){
        	 $("section").addClass("cont-alert");
        }
        //关闭弹出窗口
        vm.closeWindow = function(){
        	 $("section").removeClass("cont-alert");
        }
        
        //上一篇
        vm.post=function(id){
        	sharingPlatlformSvc.getSharingDetailById(vm,id);
        }
        
        //下一篇
        vm.next=function(id){
        	
        	sharingPlatlformSvc.getSharingDetailById(vm,id);
        }
        
        //下载
        vm.downloadSysFile = function(id){
        	sharingPlatlformSvc.downloadSysfile(id);
        }
        
        
        activate();
        function activate() {
            sharingPlatlformSvc.getSharingDetailById(vm,vm.model.sharId);
            sharingPlatlformSvc.findFileList(vm);  //附件查询
        }
    }
})();