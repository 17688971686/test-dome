(function(){
    'use strict';
    angular.module('app').controller('documentListCtrl',documentList);

    documentList.$inject=['$scope','$state','$location', 'bsWin' , '$interval' , 'fileLibrarySvc' , 'sysfileSvc'];

    function documentList($scope,$state,$location, bsWin , $interval , fileLibrarySvc , sysfileSvc){
        var vm = this;

        vm.fileLibrary = {};
        activate();
        function activate() {

            fileLibrarySvc.getFiles(function (data) {
                vm.data = data;
                /*vm.ids = [];
                vm.key = [];
                vm.value = [];
                if(vm.data){
                    var i = 0;
                    for(var key in vm.data){
                        vm.ids.push("div_" + i);
                        vm.key.push(key);
                        vm.value.push(vm.data[key]);
                        i++;
                    }

                }*/
            });
        }

        //附件下载
        vm.commonDownloadSysFile = function (sysFileId) {
            sysfileSvc.downloadFile(sysFileId);
        }

        /**
         * 点击左边导航栏，右边定位指定内容到顶部
         * @param key
         */
        vm.animate = function(index){
            $("html, body").animate({
                scrollTop: $("#div_" + index ).offset().top }, {duration: 500,easing: "swing"});
            return false;
        }


    }
})();