(function () {
    'use strict';

    angular.module('app').controller('demoCtrl', demo);

    demo.$inject = ['$location', 'demoSvc'];

    function demo($location, demoSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '';

        vm.showDialog = function () {

            $('.myDialog').modal({
                backdrop: 'static',
                keyboard: false
            });
        }

        // function datetimePicker() {
            // $("#datepicker").kendoDatePicker();
            // $("#datetiempicker").kendoDateTimePicker();
        // }

        function upload() {
            $("#files").kendoUpload({
                async: {
                    saveUrl: rootPath + "/demo/save",
                    removeUrl: rootPath + "/demo/remove",
                    autoUpload: true
                }
            });
        }


        activate();
        function activate() {
            // datetimePicker();
            upload();
        }
    }
})();
