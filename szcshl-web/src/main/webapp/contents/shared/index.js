/**
 * Created by Administrator on 2017/10/11.
 */
/*tab菜单操作*/
/*$(".content-nav ul li").click(function(){
    var n=$(this).index();
    $(".content-nav ul li").removeClass("cs-active");
    $(this).addClass("cs-active");
    $(".sidebar-menu").hide();
    $(".sidebar-menu:eq("+n+")").show();
})*/

//打印功能
function printpage(act){
    $(act).parent().hide();
    $("#"+$(act).parent().attr("id")+"_templ").show();
    $(".main-sidebar,#flow_form,.header,.breadcrumb,.toolbar,#myTab,#wpTab").addClass("print-hide");
    $(act).addClass("print-hide");
    $(".content-wrapper").addClass("print-content")
    var mb = getBrowserType();
    if(mb == 'IE'){
        document.all.WebBrowser.ExecWB(7,1);
    }else{
        print();
    }
    var showId = $(act).parent().attr("id")
    $(act).parent().show();
    if(showId == "expert_score" || showId == "expert_payment" || showId == "suppLetter_list" || showId == "registerFile_list" || showId == "busi_workplan" || showId == "busi_filerecord"){
        $(act).parent().removeAttr("style");
    }
    $("#"+$(act).parent().attr("id")+"_templ").hide();
    $(".main-sidebar,#flow_form,.header,.breadcrumb,.toolbar,#myTab,#wpTab").removeClass("print-hide");
    $(act).removeClass("print-hide");
    $(".content-wrapper").removeClass("print-content");
}


//获取浏览器类型
function getBrowserType(){
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
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