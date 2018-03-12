(function () {
    'use strict';

    angular.module('app').controller('partyEditCtrl', partyEdit);

    partyEdit.$inject = ['partySvc' , 'bsWin'];

    function partyEdit(partySvc , bsWin) {
        var vm = this;
        vm.title = '党员信息录入';        		//标题
        vm.party = {};

        active();
        function active(){
            $('#myTab li').click(function (e) {
                var aObj = $("a",this);
                e.preventDefault();
                aObj.tab('show');
                var showDiv = aObj.attr("for-div");
                $(".tab-pane").removeClass("active").removeClass("in");
                $("#"+showDiv).addClass("active").addClass("in").show(500);
            })
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
                        console.log(data);
                        bsWin.alert("保存成功！");
                    }
                });
            }

        }


    }
})();
