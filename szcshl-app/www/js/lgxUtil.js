function lgxStringGetNumber(a,b){var c,d;if(0==b){if(a=a.replace(/\./g,""),x=a.split(""),x.length>1)for(;0==x[0];)x.shift();return x.join("")}return x=a.split(""),x.length>1&&0==x[0]&&x.splice(1,0,"."),a=x.join(""),b>0?(x=a.split("."),x.length>2?(c=x.shift(),d=x.join(""),d.length>b&&(d=d.substring(0,b)),c+"."+d):2==x.length?(c=x.shift(),d=x.join(""),d.length>b&&(d=d.substring(0,b)),(c||0)+"."+d):a):(x=a.split("."),x.length>2?x.shift()+"."+x.join(""):2==x.length?(x.shift()||0)+"."+x.join(""):a)}String.prototype.getNumber=function(a){var c,d,b=[];for(c=0;c<this.length;c++)b.push(this[c]);return b=b.join("").replace(/[^0-9.-]/g,""),d=b.indexOf("-"),b=b.replace(/-/g,""),b=lgxStringGetNumber(b,a),0==d?"-"+b:b},String.prototype.getPositive=function(a){var c,b=[];for(c=0;c<this.length;c++)b.push(this[c]);return b=b.join("").replace(/[^0-9.]/g,""),lgxStringGetNumber(b,a)},String.prototype.getNegative=function(a){var c,b=[];for(c=0;c<this.length;c++)b.push(this[c]);return b=b.join("").replace(/[^0-9.]/g,""),"-"+lgxStringGetNumber(b,a)},String.prototype.getMobile=function(){var b,a=[];for(b=0;b<this.length;b++)a.push(this[b]);return a=a.join("").replace(/\D/g,""),a.length>=2&&!/(13|14|15|17|18)/.test(a)?"":a.length>11?a.substring(0,11):a},String.prototype.getPhone=function(a){var c,b=[];for(c=0;c<this.length;c++)b.push(this[c]);return b=b.join("").replace(/[^0-9-()（）]/g,""),b=b.replace(/-{2,}/g,"-"),b=b.replace(/\(\)/g,"("),b=b.replace(/（）/g,"（"),a>0&&b.length>a?b.substring(0,a):b},String.prototype.getPostcode=function(){var b,a=[];for(b=0;b<this.length;b++)a.push(this[b]);return a=a.join("").replace(/\D/g,""),a.length>6?a.substring(0,6):a};Object.prototype.copy=function(){return JSON.parse(JSON.stringify(this))};Array.prototype.removeByValue=function(a){for(var b=0;b<this.length;b++)if(this[b]==a)return this.splice(b,1),b},Array.prototype.insert=function(a,b){this.splice(a,0,b)},Date.prototype.Format=function(a){var c,b={"M+":this.getMonth()+1,"d+":this.getDate(),"h+":this.getHours(),"m+":this.getMinutes(),"s+":this.getSeconds(),"q+":Math.floor((this.getMonth()+3)/3),S:this.getMilliseconds()};/(y+)/.test(a)&&(a=a.replace(RegExp.$1,(this.getFullYear()+"").substr(4-RegExp.$1.length)));for(c in b)new RegExp("("+c+")").test(a)&&(a=a.replace(RegExp.$1,1==RegExp.$1.length?b[c]:("00"+b[c]).substr((""+b[c]).length)));return a};