<#assign idField = "id">
<#list info.fields as f><#if f.isId=true><#assign idField = f.name><#break></#if></#list>
(function () {
    'use strict';

    angular.module('app').controller('${info.beanName?uncap_first}EditCtrl', ${info.beanName?uncap_first});

    ${info.beanName?uncap_first}.$inject = ['$location', '${info.beanName?uncap_first}Svc', '$state'];

    function ${info.beanName?uncap_first}($location, ${info.beanName?uncap_first}Svc, $state) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '添加${info.comment!''}';
        vm.isuserExist = false;
        vm.${idField} = $state.params.${idField};
        if (vm.${idField}) {
            vm.isUpdate = true;
            vm.title = '更新${info.comment!''}';
        }

        vm.create = function () {
            ${info.beanName?uncap_first}Svc.create${info.beanName}(vm);
        };
        vm.update = function () {
            ${info.beanName?uncap_first}Svc.update${info.beanName}(vm);
        };

        activate();
        function activate() {
            if (vm.isUpdate) {
                ${info.beanName?uncap_first}Svc.get${info.beanName}ById(vm);
            }
        }
    }
})();
