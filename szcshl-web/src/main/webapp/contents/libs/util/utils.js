/**
 * Created by shenning on 2017/11/1.
 */
// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
//调用： var time1 = new Date().Format("yyyy-MM-dd");var time2 = new Date().Format("yyyy-MM-dd HH:mm:ss");

//根据传入的时间,获取半年前日期
Date.prototype.Days = function(n){
    // 先获取当前时间
    var curDate = (this).getTime();
    // 将半年的时间单位换算成毫秒
    var weekDay = n * 24 * 3600 * 1000;
    var pastResult = curDate + weekDay;  // 半年前的时间（毫秒单位）

    // 日期函数，定义起点为半年前
    var pastDate = new Date(pastResult),
        pastYear = pastDate.getFullYear(),
        pastMonth = pastDate.getMonth() + 1,
        pastDay = pastDate.getDate();

    return pastYear + '-' + pastMonth + '-' + pastDay;
}
//根据传入的时间,获取半年前日期
Date.prototype.halfYearAgo = function(){
    // 先获取当前时间
    var curDate = (this).getTime();
    // 将半年的时间单位换算成毫秒
    var halfYear = 365 / 2 * 24 * 3600 * 1000;
    var pastResult = curDate - halfYear;  // 半年前的时间（毫秒单位）

    // 日期函数，定义起点为半年前
    var pastDate = new Date(pastResult),
        pastYear = pastDate.getFullYear(),
        pastMonth = pastDate.getMonth() + 1,
        pastDay = pastDate.getDate();

    return pastYear + '-' + pastMonth + '-' + pastDay;
}

//根据传入的数字，获取相差year年前的日期
Date.prototype.yearAgo = function(year){
    // 先获取当前时间
    var curDate = (this).getTime();
    // 日期函数，定义起点为半年前
    var pastDate = new Date(curDate),
        pastYear = pastDate.getFullYear()-year,
        pastMonth = pastDate.getMonth() + 1,
        pastDay = pastDate.getDate();

    return pastYear + '-' + pastMonth + '-' + pastDay;
}

String.prototype.trim = function() {
    return this.replace(/(^\s*)|(\s*$)/g, "");
}