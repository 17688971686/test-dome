angular.module('userinfo.service', ['common.service', 'global_variable'])
	.service('userinfoSvc', function() {
		return {
			doModifyPwd:function(user,callBack){
				var url = "/mobile/api/personal/modifyPwd";
			}
		}
	});