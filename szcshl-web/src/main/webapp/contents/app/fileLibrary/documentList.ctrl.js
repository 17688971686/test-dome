(function(){
    'use strict';
    angular.module('app').controller('documentListCtrl',documentList);

    documentList.$inject=['$scope','$state','$location', 'bsWin' , '$interval' , 'fileLibrarySvc'];

    function documentList($scope,$state,$location, bsWin , $interval , fileLibrarySvc){
        var vm = this;

        vm.fileLibrary = {};
        activate();
        function activate(){
            fileLibrarySvc.getFiles(function(data){
                console.log(data);
                vm.fileList1 =data.one;
                vm.fileList2 =data.two;
                vm.fileList3 =data.three;
                vm.fileList4 =data.four;
                vm.fileList5 =data.five;
                // vm.fileList1 =data.five;
                // vm.fileList2 =data.four;
                // vm.fileList3 =data.three;
                // vm.fileList4 =data.two;
                // vm.fileList5 =data.one;
                vm.index1 = 0;
                vm.index2 = 0;
                vm.index3 = 0;
                vm.index4 = 0;
                vm.index42 = 0;
            });
            //监听
            $scope.$watch("vm.index42",function (newValue , oldValue){
                if(newValue && vm.index42 == vm.fileList4[vm.index4].length-1){
                    vm.index42 = 0;
                    vm.index4 ++;
                }
            });
        }



    }
})();