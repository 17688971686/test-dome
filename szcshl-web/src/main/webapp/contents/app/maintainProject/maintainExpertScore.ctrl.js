(function(){
    'use strict';
    angular.module('app').controller('maintainExpertScoreCtrl' , maintainExpertScore);
    maintainExpertScore.$inject = ['expertReviewSvc' , 'bsWin' , 'signSvc' , '$state'];
    function maintainExpertScore(expertReviewSvc , bsWin , signSvc , $state){
        var vm = this;
        vm.signid = $state.params.signid;
        vm.showExpertScore = true;
        vm.showFlag ={};
        vm.showFlag.isMainPrinUser = true;
        activate();
        function activate(){
            signSvc.initFlowPageData(vm.signid, function (data) {
                vm.model = data;
            });
        }

        // 编辑专家评分
        vm.editSelectExpert = function (id) {
            vm.scoreExpert = {};
            $.each(vm.model.expertReviewDto.expertSelectedDtoList, function (i, scopeEP) {
                if (scopeEP.id == id) {
                    vm.scoreExpert = angular.copy(scopeEP);
                    return;
                }
            })

            $("#star_" + vm.scoreExpert.id).raty({
                number: 5,
                score: function () {
                    $(this).attr("data-num", angular.isUndefined(vm.scoreExpert.score) ? 0 : vm.scoreExpert.score);
                    return $(this).attr("data-num");
                },
                starOn: '../contents/libs/raty/lib/images/star-on.png',
                starOff: '../contents/libs/raty/lib/images/star-off.png',
                starHalf: '../contents/libs/raty/lib/images/star-half.png',
                readOnly: false,
                halfShow: true,
                hints   : ['不合格','合格','中等','良好','优秀'],
                size: 34,
                click: function (score, evt) {
                    vm.scoreExpert.score = score;
                }
            });

            $("#score_win").kendoWindow({
                width: "820px",
                height: "365px",
                title: "编辑-专家星级",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Close"]
            }).data("kendoWindow").center().open();
        }

        // 关闭专家评分
        vm.closeEditMark = function () {
            window.parent.$("#score_win").data("kendoWindow").close();
        }

        // 保存专家评分
        vm.saveMark = function () {
            if (!vm.scoreExpert.score || vm.scoreExpert.score == 0) {
                bsWin.alert("请对专家进行评分！");
            } else if (!vm.scoreExpert.describes) {
                bsWin.alert("请对专家进行评分描述！");
            } else {
                expertReviewSvc.saveMark(vm.scoreExpert, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        angular.forEach(vm.model.expertReviewDto.expertSelectedDtoList, function (scopeEP, index) {
                            if (scopeEP.id == vm.scoreExpert.id) {
                                scopeEP.score = vm.scoreExpert.score;
                                scopeEP.describes = vm.scoreExpert.describes;
                            }
                        })
                        bsWin.success("保存成功！", function () {
                            vm.closeEditMark();
                        });
                    } else {
                        bsWin.alert(data.reMsg);
                    }

                });
            }
        }

    }
})();