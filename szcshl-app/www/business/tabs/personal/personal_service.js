angular.module('personal.service', ['common.service', 'global_variable'])
	.service('personalService', function(HttpUtil) {
		return {
			doModifyPwd:function(user,callBack){
				var url = "/mobile/api/personal/modifyPwd";
				
				HttpUtil.post(url,user,callBack);
			}
		}

	});