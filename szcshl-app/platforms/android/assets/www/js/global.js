// 全局变量文件
angular.module('global', [])
	.constant("GlobalVariable", {
		VERSION: '1.1.0',//版本号
		DefaultPageSize: 15,//默认页面大小
		DefaultTimeOut: 60*1000,//默认请求超时（单位：ms）
		//正式环境
		//SERVER_PATH: 'http://120.77.216.41:8081',
		//本地开发环境
		SERVER_PATH: 'http://192.168.1.18:8080/wtssgl',//本地开发ip
		//本机开发环境
		//SERVER_PATH: 'http://192.168.1.237:8080/wtssgl',//本地开发ip
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

		Years: [
			{ 'id': 1, 'name': '2016年', 'value': '2016' },
			{ 'id': 2, 'name': '2017年', 'value': '2017' }
		],

		Months: [
			{ 'id': 0, 'name': '1月', 'value': '1' },
			{ 'id': 1, 'name': '2月', 'value': '2' },
			{ 'id': 2, 'name': '3月', 'value': '3' },
			{ 'id': 3, 'name': '4月', 'value': '4' },
			{ 'id': 4, 'name': '5月', 'value': '5' },
			{ 'id': 5, 'name': '6月', 'value': '6' },
			{ 'id': 6, 'name': '7月', 'value': '7' },
			{ 'id': 7, 'name': '8月', 'value': '8' },
			{ 'id': 8, 'name': '9月', 'value': '9' },
			{ 'id': 9, 'name': '10月', 'value': '10' },
			{ 'id': 10, 'name': '11月', 'value': '11' },
			{ 'id': 11, 'name': '12月', 'value': '12' }
		],

		IndustryTypes: [
			{ 'id': 0, 'name': '- 不限 -', 'value': '' },
			{ 'id': 1, 'name': '战略性新兴产业和未来产业', 'value': '1' },
			{ 'id': 2, 'name': '高技术制造业和优势传统产业', 'value': '2' },
			{ 'id': 3, 'name': '现代服务业', 'value': '3' },
			{ 'id': 4, 'name': '社会民生', 'value': '4' },
			{ 'id': 5, 'name': '城市更新', 'value': '5' },
			{ 'id': 6, 'name': '道路机场港口', 'value': '6' },
			{ 'id': 7, 'name': '城市安全环境资源', 'value': '7' },
			{ 'id': 8, 'name': '宜居环境', 'value': '8' },
			{ 'id': 9, 'name': '轨道交通', 'value': '9' },
			{ 'id': 10, 'name': '资源能源保障', 'value': '10' },
			{ 'id': 11, 'name': '挂点项目', 'value': '11' }
		],

		ProjectTypes: [
			{ 'id': 0, 'name': '- 不限 -', 'value': '' },
			{ 'id': 1, 'name': '前期', 'value': '1' },
			{ 'id': 2, 'name': '新建', 'value': '2' },
			{ 'id': 3, 'name': '续建', 'value': '3' },
			{ 'id': 4, 'name': '培育', 'value': '4' }
		],

		Areas: [
			{ 'id': 0, 'name': '- 不限 -', 'value': '' },
			{ 'id': 1, 'name': '罗湖区', 'value': '罗湖区' },
			{ 'id': 2, 'name': '福田区', 'value': '福田区' },
			{ 'id': 3, 'name': '南山区', 'value': '南山区' },
			{ 'id': 4, 'name': '盐田区', 'value': '盐田区' },
			{ 'id': 5, 'name': '宝安区', 'value': '宝安区' },
			{ 'id': 6, 'name': '龙岗区', 'value': '龙岗区' },
			{ 'id': 7, 'name': '坪山新区', 'value': '坪山新区' },
			{ 'id': 8, 'name': '光明新区', 'value': '光明新区' },
			{ 'id': 9, 'name': '龙华新区', 'value': '龙华新区' },
			{ 'id': 10, 'name': '大鹏新区', 'value': '大鹏新区' },
			{ 'id': 11, 'name': '前海合作区', 'value': '前海合作区' },
			{ 'id': 12, 'name': '深汕合作区', 'value': '深汕合作区' }
		],
		Streets: [
			{ 'id': 0, 'name': '- 不限 -', 'value': '' },
			{ 'id': 1, 'name': '龙城街道', 'value': '龙城街道' },
			{ 'id': 2, 'name': '龙岗街道', 'value': '龙岗街道' },
			{ 'id': 3, 'name': '平湖街道', 'value': '平湖街道' },
			{ 'id': 4, 'name': '布吉街道', 'value': '布吉街道' },
			{ 'id': 5, 'name': '横岗街道', 'value': '横岗街道' },
			{ 'id': 6, 'name': '坪地街道', 'value': '坪地街道' },
			{ 'id': 7, 'name': '南湾街道', 'value': '南湾街道' },
			{ 'id': 8, 'name': '坂田街道', 'value': '坂田街道' }
		],
		InvestTypes: [
			{ 'id': 0, 'name': '- 不限 -', 'value': '' },
			{ 'id': 1, 'name': '政府投资', 'value': '1' },
			{ 'id': 2, 'name': '社会投资', 'value': '2' }
		],

		HostRooms: [
			{ 'id': 0, 'name': '- 不限 -', 'value': '' },
			{ 'id': 1, 'name': '投资处', 'value': '5' },
			{ 'id': 2, 'name': '产业协调处', 'value': '7' },
			{ 'id': 3, 'name': '社会发展处', 'value': '7' },
			{ 'id': 4, 'name': '高技术产业处', 'value': '10' },
			{ 'id': 5, 'name': '重大项目协调处', 'value': '12' },
			{ 'id': 6, 'name': '碳交办', 'value': '21' }
		],

		MainOranizations: [
			{ 'id': 0, 'name': '- 不限 -', 'value': '' },
			{ 'id': 1, 'name': '市发展改革委', 'value': '1' },
			{ 'id': 2, 'name': '市经贸信息委', 'value': '3' },
			{ 'id': 3, 'name': '市交通运输委', 'value': '6' },
			{ 'id': 4, 'name': '市卫生计生委', 'value': '9' },
			{ 'id': 5, 'name': '市教育局', 'value': '10' },
			{ 'id': 6, 'name': '市城管局', 'value': '18' },
			{ 'id': 7, 'name': '罗湖区政府', 'value': '21' },
			{ 'id': 8, 'name': '福田区政府', 'value': '22' },
			{ 'id': 9, 'name': '南山区政府', 'value': '23' },
			{ 'id': 10, 'name': '盐田区政府', 'value': '24' },
			{ 'id': 11, 'name': '宝安区政府', 'value': '25' },
			{ 'id': 12, 'name': '龙岗区政府', 'value': '26' },
			{ 'id': 13, 'name': '光明新区管委会', 'value': '27' },
			{ 'id': 14, 'name': '坪山新区管委会', 'value': '28' },
			{ 'id': 15, 'name': '龙华新区管委会', 'value': '29' },
			{ 'id': 16, 'name': '大鹏新区管委会', 'value': '30' },
			{ 'id': 17, 'name': '市建筑工务署', 'value': '31' },
			{ 'id': 18, 'name': '市水务局', 'value': '33' },
			{ 'id': 19, 'name': '前海管理局', 'value': '35' },
			{ 'id': 20, 'name': '市地铁集团', 'value': '48' }
		],

		Categories: [
			{ 'id': 0, 'name': '按行业类型', 'value': '1' },
			{ 'id': 1, 'name': '按投资来源', 'value': '2' },
			{ 'id': 2, 'name': '按建设阶段', 'value': '3' },
			{ 'id': 3, 'name': '按主办处室', 'value': '4' },
			{ 'id': 4, 'name': '按责任单位', 'value': '5' },
			// {'id': 5, 'name': '按申报方式', 'value': '6'},
		],

		ClassTypes: [
			{ 'id': 0, 'name': '- 不限 -', 'value': '' },
			{ 'id': 1, 'name': '中长期规划', 'value': '1' },
			{ 'id': 2, 'name': '年度计划', 'value': '2' },
			{ 'id': 3, 'name': '进度情况分析', 'value': '3' },
			{ 'id': 4, 'name': '项目协调情况', 'value': '4' },
			{ 'id': 5, 'name': '省重点项目', 'value': '5' },
			{ 'id': 6, 'name': '日常文件', 'value': '6' }
		],

		// 申报方式
		ApplyStyles: [
			{ 'id': 0, 'name': '所有', 'value': '' },
			{ 'id': 1, 'name': '基本', 'value': '1' },
			{ 'id': 2, 'name': '增补', 'value': '2' }
		],
	})
	.constant('APP_EVENTS', {
	    loggedIn: 'login-success',
	    loginFailed: 'login-failed',
	    loggedOut: 'logout-success',
	    sessionTimeout: 'session-timeout',
	    notAuthenticated: 'not-authenticated',
	    notAuthorized: 'not-authorized'
	});

