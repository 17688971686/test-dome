(function () {
    'use strict';

    angular.module('app').controller('partyEditCtrl', partyEdit);

    partyEdit.$inject = ['partySvc' , 'bsWin' , '$state'];

    function partyEdit(partySvc , bsWin , $state) {
        var vm = this;
        vm.title = '党员信息录入';        		//标题
        vm.party = {};
        vm.patryId = $state.params.id;

        active();
        function active(){
            /*$('#myTab li').click(function (e) {
                var aObj = $("a",this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#"+showDiv).addClass("active").addClass("in").show(500);
            })*/
            if(vm.patryId){
                vm.isShow = true;
                partySvc.findById(vm.patryId , function(data){
                    if(data.flag || data.reCode == 'ok'){
                        vm.party = data.reObj;
                    }

                });
            }
        }

        /**
         * 保存党员信息
         */
        vm.saveParty = function(){
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                partySvc.createParty(vm , function(data){
                    if(data.flag || data.reCode == 'ok'){
                        bsWin.alert("保存成功！");
                    }
                });
            }

        }

        /**
         * 更新党员信息
         */
        vm.updateParty = function(){
            common.initJqValidation();
            var isValid = $('form').valid();
            if (isValid) {
                partySvc.updateParty(vm , function(data){
                    if(data.flag || data.reCode == 'ok'){
                        bsWin.alert("保存成功！");
                    }
                });
            }

        }


    }
})();
