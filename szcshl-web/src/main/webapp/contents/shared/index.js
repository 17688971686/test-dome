/**
 * Created by Administrator on 2017/10/11.
 */
$(".content-nav ul li").click(function(){
    var n=$(this).index();
    $(".content-nav ul li").removeClass("cs-active");
    $(this).addClass("cs-active");
    $(".sidebar-menu").hide();
    $(".sidebar-menu:eq("+n+")").show();
})

//打印功能
function printpage(act){
$(".main-sidebar,.header,.breadcrumb,.toolbar,#myTab").addClass("print-hide");
$(act).addClass("print-hide");
$(".content-wrapper").addClass("print-content");
print();
$(".main-sidebar,.header,.breadcrumb,.toolbar,#myTab").removeClass("print-hide");
$(act).removeClass("print-hide");
$(".content-wrapper").removeClass("print-content");

}