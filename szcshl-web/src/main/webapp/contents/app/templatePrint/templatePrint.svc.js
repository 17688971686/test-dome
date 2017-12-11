(function () {
    'use strict';

    angular.module('app').factory('templatePrintSvc', templatePrint);

    templatePrint.$inject = ['$http', 'bsWin','$state'];
    function templatePrint($http, bsWin,$state) {
        var vm = this;
        vm.model={};
        var service = {
            templatePrint: templatePrint,       //模板打印
            getBrowserType: getBrowserType,     //获取浏览器类型
            templatePage : templatePage , //打印 （分页）
        };
        return service;

        function templatePrint(id) {
            var LODOP = getLodop();
            var strStylePath = rootPath +"/contents/shared/templatePrint.css";
            var strStyleCSS="<link href="+strStylePath+" type='text/css' rel='stylesheet'>";
            var strFormHtml="<head>"+strStyleCSS+"</head><body>"+$("#"+id).html()+"</body>";
            LODOP.PRINT_INIT("");
            LODOP.ADD_PRINT_HTML(10,20,"100%","100%",strFormHtml);
            LODOP.PREVIEW();
        }

        //获取浏览器类型
        function getBrowserType(){
            var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
            console.log(userAgent);
            var isOpera = userAgent.indexOf("Opera") > -1;
            if (isOpera) {
                return "Opera"
            }; //判断是否Opera浏览器
            if (userAgent.indexOf("Firefox") > -1) {
                return "FF";
            } //判断是否Firefox浏览器
            if (userAgent.indexOf("Chrome") > -1){
                return "Chrome";
            }
            if (userAgent.indexOf("Safari") > -1) {
                return "Safari";
            } //判断是否Safari浏览器
            if (!!window.ActiveXObject || "ActiveXObject" in window) {
                return "IE";
            }; //判断是否IE浏览器
        }

        //begin templatePage
        function templatePage(id){
            var LODOP = getLodop();
            var strStylePath = rootPath +"/contents/shared/templatePrint.css";
            var strStyleCSS="<link href="+strStylePath+" type='text/css' rel='stylesheet'>";
            var strFormHtml1="<head>"+strStyleCSS+"</head><body>"+$("#"+ id +"_templ1").html()+"</body>";

            LODOP.PRINT_INIT("");
            LODOP.ADD_PRINT_HTML(10,20,"100%","100%",strFormHtml1);

            //分页
            if($("#"+ id +"_templ2").html()){
                LODOP.NewPage();
                var strFormHtml2="<head>"+strStyleCSS+"</head><body>"+$("#"+ id +"_templ2").html()+"</body>";
                LODOP.ADD_PRINT_HTML(50,20,"100%","100%",strFormHtml2);
            }


            if($("#"+ id +"_templ3").html()){
                LODOP.NewPage();
                var strFormHtml3="<head>"+strStyleCSS+"</head><body>"+$("#"+ id +"_templ3").html()+"</body>";
                LODOP.ADD_PRINT_HTML(50,20,"100%","100%",strFormHtml3);
            }
            //打印预览
            LODOP.PREVIEW();
        }
        //end templatePage
    }
})();