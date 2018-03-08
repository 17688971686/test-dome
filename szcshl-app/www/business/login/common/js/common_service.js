angular.module('common.service', [])
	//弹窗
	.service('Message', function($ionicPopup, $ionicModal, $ionicBackdrop, $timeout, global) {
		var template = '<ion-popover-view><ion-content> Hello! </ion-content></ion-popover-view>';

		function show(msg,callback) {
			return $ionicPopup.alert({
				title: '提示',
				template: "<center>" + msg + "</center>",
				okText: '确定'
			}).then(function(res) {
		           if(typeof callback === 'function'){
		        	if(res) {callback();} 
		        }
            });
		}
		return {
			show: show,
			confirm: function(msg, okFun, canFun) {
				var r = $ionicPopup.confirm({
					title: '请确认您的操作',
					template: msg,
					cancelText: '取消',
					okText: '确定'
				});

				r.then(function(res) {
					if(res) {
						if(typeof okFun === 'function') {
							okFun();
						}
					} else {
						if(typeof canFun === 'function') {
							canFun();
						}
					}
				});

				return r;
			},
			showModal: function($scope, templateUrl, showFun) {
				if(!(templateUrl && typeof templateUrl === 'string')) {
					console.log('未设置弹窗模板');
				}
				$ionicModal.fromTemplateUrl(templateUrl, {
					scope: $scope
				}).then(function(modal) {
					$scope.modal = modal;
					modal.show();
					if(typeof showFun === 'function') {
						showFun();
					}
				});
			},
		}
	})