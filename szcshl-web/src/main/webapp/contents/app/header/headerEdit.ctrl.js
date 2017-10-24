(function(){
    'use strict';
    angular.module('app').controller('headerEditCtrl' , headerEdit);
    headerEdit.$inject=['headerSvc','$state'];

    function headerEdit(headerSvc,$state){
        var vm = this;
        vm.selectedHeaderList=[];
        vm.selectList={};
        vm.headerType= $state.params.headerType;

        //全选
        vm.allChecked =function(){
            var tab = $('#selectHeaderForm').find('input');
            $.each(tab , function(i , obj){
                obj.checked = true;
            });

        }

        //全取消
        vm.allCancel = function(){
            var tab = $('#selectHeaderForm').find('input');
            $.each(tab , function(i , obj) {
                obj.checked = false;
                // $('input:checkbox').attr('checked', false);
            });
        }

        /**
         * 左添加
         */
        vm.addHeader = function(){
            var ids =[];
            for(var i=0 ; i<vm.allHeaderList.length ; i++){
                var s = vm.allHeaderList[i];
                if(s.checkbox){
                    ids.push(s.id);
                    vm.selectedHeaderList.push(s);
                    vm.allHeaderList.splice(i,1);
                    i=0;
                    s.checkbox = false;
                }
            }

            var idStr = ids.join(",");
            headerSvc.updateSelectedHeader(vm,idStr);

        }

        /**
         * 右取消
         */
        vm.cancelHeader = function (){
            var ids =[];
            for(var i=0 ; i< vm.selectedHeaderList.length ; i++){
                var s = vm.selectedHeaderList[i];
                if(s.checkbox){
                    ids.push(s.id);
                    vm.allHeaderList.push(s);
                    vm.selectedHeaderList.splice(i,1);
                    i=0;
                    s.checkbox = false;
                }
            }

            var idStr = ids.join(",");
            headerSvc.updateCancelHeader(vm,idStr);
        }


      /*  vm.createTable = function(){
            headerSvc.findHeaderListByState(vm);
            var ids =[];
            for(var i=0; i<vm.selectedHeaderList.length; i++){
                ids.push(vm.selectedHeaderList[i].id);
            }
            var idStr = ids.join(",");
            headerSvc.updateHeader(idStr , function(){
                $state.go('statisticalList',{selectHeaderList : vm.selectList});
            })

        }*/

        vm.changeType = function(){
            headerSvc.findHeaderListNoSelected(vm);
            vm.selectedHeaderList=[];
        }

        activate();
        function activate(){
            headerSvc.findHeaderListNoSelected(vm);
            headerSvc.findHeaderListSelected(vm , function(data){
                vm.selectedHeaderList = data;
                vm.header = true;
            });
        }
    }
})();