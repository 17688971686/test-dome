(function(){
    'use strict';
    angular.module('app').controller('documentListCtrl',documentList);

    documentList.$inject=['$scope','$state','$location', 'bsWin' , '$interval' , 'fileLibrarySvc'];

    function documentList($scope,$state,$location, bsWin , $interval , fileLibrarySvc){
        var vm = this;

        vm.fileLibrary = {};
        activate();
        function activate() {
            fileLibrarySvc.getFiles(function (data) {
                console.log(data);
                vm.data2 = data.data;
            });
            /* //监听
             $scope.$watch("vm.index42",function (newValue , oldValue){
             if(newValue && vm.index42 == vm.fileList4[vm.index4].length-1){
             vm.index42 = 0;
             vm.index4 ++;
             }
             });*/

        }

    }
})();