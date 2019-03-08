(function(){
    'static use';
    angular.module('app').controller('encodePwdCtrl', encodePwd);
    encodePwd.$inject = ['adminSvc', 'bsWin',];
    function encodePwd(adminSvc , bsWin){
        var vm = this;

        activate();
        function activate(){
            adminSvc.encodePwd(function(data){
                    bsWin.alert(data.reMsg);
            })
        }
    }
})();