(function () {
    'use strict';

    angular.module('app').controller('annountmentDetailCtrl',annountmentDetail);

    annountmentDetail.$inject = ['$location','$state','annountmentSvc'];

    function annountmentDetail($location, $state,annountmentSvc) {
        var vm = this;
        vm.title = '通知公告详情页';
        vm.annountment = {};    //通知公告对象
        vm.annountment.anId = $state.params.id;
        activate();
        function activate() {
            annountmentSvc.findDetailById(vm,vm.annountment.anId);
        }
        
        vm.post=function(id){
            annountmentSvc.findDetailById(vm,id);
        }
        
        vm.next=function(id){
            annountmentSvc.findDetailById(vm,id);
        }


        vm.alertwd=function(){
            $("section").addClass("cont-alert");
        }
        vm.closed=function(){
            $("section").removeClass("cont-alert");
        }
        //打印
        vm.prints=function(){
            var html='<div  style="width: 80%;height:80%; text-align: center; margin: auto; font-family: \'Microsoft YaHei\'">'
                +$("#context-body").html()
                +"</div>";
            var newWindow;
            var ht=$(window).height();
            var wt=$(window).width();
            newWindow=window.open('','','width='+wt+',height='+ht);
            newWindow.document.body.innerHTML=html;
            newWindow.print();
        }


    }





})();

