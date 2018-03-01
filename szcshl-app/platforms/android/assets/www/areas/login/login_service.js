angular.module('login.service', ['common.service'])
	.service('loginservice', function($q,HttpUtil,GlobalVariable,$http) {
		return {
			login: function(user) {
				var deferred = $q.defer();
				var url = GlobalVariable.SERVER_PATH + "/mobile/api/personal/login";
			/*	HttpUtil.ajax({
					method:'POST',//String
					url:url,//String
					data:user,//Object
					dataType:"",//String
					success:suFn,//function
					error:errFn//function
				});*/
				
				
				$http({
					method: 'POST',
					url: url,
					params: user,
					isIgnoreLoading:true
				}).success(function(data){
					deferred.resolve(data);
				}).error(function(res,status){
					deferred.reject(status);
				});
				
				return deferred.promise;
			}
		}

	});