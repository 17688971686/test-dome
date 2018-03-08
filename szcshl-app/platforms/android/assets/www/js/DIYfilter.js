// 配置模块，控制不平平台的兼容性
angular.module('DIYfilter', ['global'])
	//时间转换
	.filter('convertTime',function() {
		function convert(time){
			
			var second = Math.floor(time/1000);
			var day = Math.floor(second/86400);//24*3600=86400
			second = Math.floor(second%86400);
			var hour= Math.floor(second/3600);
			second = Math.floor(second%3600);
			var minute = Math.floor(second/60);
			second = Math.floor(second%60);
			return day+"天 "+hour+"时 "+minute+"分 "+second+"秒";
		}
		return function(time){
//			console.log(time)
			if(!isNaN(time) && typeof time === 'number'){
				if(time<0){ return "超时 " + convert(0-time); }
				return convert(time);
			}else if(typeof time === 'string'){
				return time;
			}else{
				return "任务未计时";
			}
			
		}
	})

