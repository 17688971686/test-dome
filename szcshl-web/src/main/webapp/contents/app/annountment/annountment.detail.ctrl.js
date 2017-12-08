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
        vm.printNotice = function(id){
            var LODOP = getLodop();
            var strStylePath = rootPath +"/contents/shared/annountmentPrint.css";
            var strStyleCSS="<link href="+strStylePath+" type='text/css' rel='stylesheet'>";
            var strFormHtml="<head>"+strStyleCSS+"</head><body>"+$("#"+id).html()+"</body>";
            LODOP.PRINT_INIT("");
            LODOP.SET_PRINT_STYLEA("FontName","System");
            LODOP.ADD_PRINT_HTML(10,20,"100%","100%",strFormHtml);
            LODOP.PREVIEW();

        }

    }

})();

