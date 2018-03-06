angular.module('dateUtil.service', ['ionic-datepicker'])

	//时间格式化
	.filter('formatTime', function($filter) {
		return function(input) {
			// input是我们传入的字符串
			var time = new Date(input);
			var result = $filter('date')(time, 'yyyy/MM/dd');
			return result;
		}
	})
	.service('dateUtil', function($filter,ionicDatePicker) {
		var ipObj1 = {
	      	callback: function (val) {  //Mandatory
	      		alert('您没有传入callback方法，你选择的时间是：'+ $filter('date')(val,'yyyy年MM月dd日'));
	      	},
	      	from: new Date(2017, 0, 1), //Optional
	      	to: new Date((new Date().getUTCFullYear()+1), 12, 31), //Optional
	      	inputDate: new Date(),      //Optional
	      	mondayFirst: false,          //Optional
	      	closeOnSelect: false,       //Optional
	      	templateType: 'popup',      //Optional
	      	showTodayButton: true,
	      	dateFormat: 'yyyy/MM/dd'
	    };
	
		return {
			openDatePicker: function(callback,date){
				if(typeof callback === 'function'){
					ipObj1.callback = function(val){
						var time = new Date(val);
						var result = $filter('date')(time, ipObj1.dateFormat);
						callback(result);
					}
				}
				if(typeof date === 'string' && date.length == ipObj1.dateFormat.length){
					var strs = date.split("/"); //字符分割 
					ipObj1.inputDate = new Date(strs[0], strs[1] - 1, strs[2]);
				}
				if(typeof date === 'object'){
					ipObj1.inputDate = date;
				}
	      		ionicDatePicker.openDatePicker(ipObj1);
	    	},
	    	config: function(option){
	    		ipObj1 = option;
	    	}
		}
	})