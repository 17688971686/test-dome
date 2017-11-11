(function () {
    'use strict';

    angular.module('app').factory('templatePrintSvc', templatePrint);

    templatePrint.$inject = ['$http', 'bsWin','$state'];
    function templatePrint($http, bsWin,$state) {
        var vm = this;
        vm.model={};
        var service = {
            templatePrint: templatePrint,       //模板打印
        };
        return service;

        function templatePrint(act,model) {
            vm.model = model;
            if(vm.model.showDiv=='expert_score' || vm.model.showDiv=='expert_payment' || vm.model.showDiv=='suppLetter_list' || vm.model.showDiv=='registerFile_list'){
                $("#"+vm.model.showDiv).hide();
                $("#"+vm.model.showDiv+"_templ").show();
                $(".main-sidebar,#flow_form,.header,.breadcrumb,.toolbar,#myTab").addClass("print-hide");
                $(act).addClass("print-hide");
                $(".content-wrapper").addClass("print-content");
                print();
                $("#"+vm.model.showDiv).show();
                $("#"+vm.model.showDiv+"_templ").hide();
                $(".main-sidebar,#flow_form,.header,.breadcrumb,.toolbar,#myTab").removeClass("print-hide");
                $(act).removeClass("print-hide");
                $(".content-wrapper").removeClass("print-content");
            }else if(vm.model.reviewstage=='项目建议书'|| vm.model.reviewstage=='可行性研究报告'  ||vm.model.reviewstage=='项目概算' || vm.model.reviewstage=='其它'){
                $("#"+vm.model.showDiv+"_xmjys").hide();
                $("#"+vm.model.showDiv+"_xmjys_templ").show();
                $(".main-sidebar,#flow_form,.header,.breadcrumb,.toolbar,#myTab").addClass("print-hide");
                $(act).addClass("print-hide");
                $(".content-wrapper").addClass("print-content");
                print();
                $("#"+vm.model.showDiv+"_xmjys").show();
                $("#"+vm.model.showDiv+"_xmjys_templ").hide();
                $(".main-sidebar,#flow_form,.header,.breadcrumb,.toolbar,#myTab").removeClass("print-hide");
                $(act).removeClass("print-hide");
                $(".content-wrapper").removeClass("print-content");
            }else{
               $(".main-sidebar,#flow_form,.header,.breadcrumb,.toolbar,#myTab").addClass("print-hide");
               $(act).addClass("print-hide");
               $(".content-wrapper").addClass("print-content");
               print();
               $(".main-sidebar,#flow_form,.header,.breadcrumb,.toolbar,#myTab").removeClass("print-hide");
               $(act).removeClass("print-hide");
               $(".content-wrapper").removeClass("print-content");
           }
        }
    }
})();