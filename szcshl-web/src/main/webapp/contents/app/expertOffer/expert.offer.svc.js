(function () {
    'use strict';

    angular.module('app').factory('expertOfferSvc', expertOffer);

    expertOffer.$inject = ['$http','expertSvc'];

    function expertOffer($http,expertSvc) {
        var service = {
            saveOffer: saveOffer,	            //保存专家聘书

        };
        return service;

        //S_saveOffer
        function saveOffer(vm) {
            common.initJqValidation($("#expert_offer_form"));
            var isValid = $('#expert_offer_form').valid();
            if (isValid) {
                vm.expertOffer.expertId = vm.model.expertID
                var httpOptions = {
                    method : 'post',
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
        }//E_saveOffer

    }
})();