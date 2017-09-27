(function () {
    'use strict';

    angular.module('app').factory('expertOfferSvc', expertOffer);

    expertOffer.$inject = ['$http','expertSvc'];

    function expertOffer($http,expertSvc) {
        var service = {
            saveOffer: saveOffer,	            //保存专家聘书
            updateOffer  : updateOffer      //更新专家聘书

        };
        return service;

        //S_saveOffer
        function saveOffer(expertOffer,callBack) {
            var httpOptions = {
                method : 'post',
                url : rootPath + "/expertOffer",
                data : expertOffer
            }
            var httpSuccess = function success(response) {
                if (callBack != undefined && typeof callBack == 'function') {
                    callBack(response.data);
                }
            }
            common.http({
                $http : $http,
                httpOptions : httpOptions,
                success : httpSuccess
            });
        }//E_saveOffer

        //begin updateOffer
        function updateOffer(vm){
            common.initJqValidation($("#expert_offer_form"));
            var isValid = $('#expert_offer_form').valid();
            if (isValid) {
                vm.expertOffer.expertId = vm.model.expertID
                var httpOptions = {
                    method : 'put',
                    url : rootPath + "/expertOffer",
                    data : vm.expertOffer
                }
                var httpSuccess = function success(response) {
                    common.requestSuccess({
                        vm : vm,
                        response : response,
                        fn : function() {
                            expertSvc.getExpertById(vm);
                            common.alert({
                                vm : vm,
                                msg : "操作成功"
                            })
                        }

                    });
                }
                common.http({
                    vm : vm,
                    $http : $http,
                    httpOptions : httpOptions,
                    success : httpSuccess
                });
            }
        }
        //end updateOffer

    }
})();