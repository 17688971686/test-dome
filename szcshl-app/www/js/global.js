// 全局变量文件
angular.module('global_variable', [])
	.constant("global", {
		VERSION: '1.1.3',//版本号
		DefaultPageSize: 15,//默认页面大小
		DefaultTimeOut: 60*1000,//默认请求超时（单位：ms）
		//正式环境
		//SERVER_PATH: 'http://203.91.36.247',
		//本机开发环境
		SERVER_PATH: 'http://192.168.1.188:8080/szcshl-web',//本地开发ip
		//测试环境
		//SERVER_PATH:'http://120.77.208.59:8088/wtssgl',
		SERVER_PATH_PROJECT: 'http://120.77.216.41:9004/ProjectAppService.svc',
		//请求key
		SERVER_CALLKEY: '5DF50E30-B215-45BC-B361-776AE1D09994',
		// gulp environment: injects environment vars
		ENV: {
			'SERVICE_ROOT': 'http://203.91.46.81:8013'
		},
		CurrentYear: new Date().getFullYear(),
		//方便的年月日设置方式
		WeekDaysList: ["日", "一", "二", "三", "四", "五", "六"], //["S", "M", "T", "W", "T", "F", "S"],
		MonthList: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],

		BUILD: {},
		UUID: '00000000000000',
		DEVICEID: '000000000000000',
		PLATFORM: 'iOS',

		Years: [],
    
	})
	.constant('APP_EVENTS', {
	    loggedIn: 'login-success',
	    loginFailed: 'login-failed',
	    loggedOut: 'logout-success',
	    sessionTimeout: 'session-timeout',
	    notAuthenticated: 'not-authenticated',
	    notAuthorized: 'not-authorized'
	});

