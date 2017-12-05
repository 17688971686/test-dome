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
    var strStylePath = rootPath +"/contents/shared/styleversion.css";
    var strStyleCSS="<link href="+strStylePath+" type='text/css' rel='stylesheet'>";
    var strFormHtml="<head>"+strStyleCSS+"</head><body>"+$("#"+$(act).parent().attr("id")+"_templ").html()+"</body>";
    LODOP=getLodop();
    LODOP.PRINT_INIT("打印控件Lodop初始化");
    LODOP.ADD_PRINT_HTM(0,0,"100%","100%",strFormHtml);
    LODOP.PREVIEW();
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