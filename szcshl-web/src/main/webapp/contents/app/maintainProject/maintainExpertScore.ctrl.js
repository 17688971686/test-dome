(function(){
    'use strict';
    angular.module('app').controller('maintainExpertScoreCtrl' , maintainExpertScore);
    maintainExpertScore.$inject = ['expertReviewSvc' , 'bsWin' , 'signSvc' , '$state','companySvc'];
    function maintainExpertScore(expertReviewSvc , bsWin , signSvc , $state,companySvc){
        var vm = this;
        vm.signid = $state.params.signid;
        vm.showExpertScore = true;
        vm.showFlag ={};
        vm.showFlag.isMainPrinUser = true;
        activate();
        function activate(){
            expertReviewSvc.initReview(vm.signid,"", function (data) {
                if(data){
                    vm.expertReviewDto = data;
                }else{
                    vm.expertReviewDto = {};
                }
            });

            signSvc.findSignUnitScore(vm.signid, function (data) {
                if(data){
                    vm.unitScoreDto = data;
                }else{
                    vm.unitScoreDto = {};
                }
            });
        }

        // 编辑专家评分
        vm.editSelectExpert = function (id) {
            vm.scoreExpert = {};
            $.each(vm.expertReviewDto.expertSelectedDtoList, function (i, scopeEP) {
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
            }  else {
                expertReviewSvc.saveMark(vm.scoreExpert, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        angular.forEach(vm.expertReviewDto.expertSelectedDtoList, function (scopeEP, index) {
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

        /*****************S_单位评分******************/
        vm.editUnitScore = function (id) {
            $("#star").raty({
                number: 5,
                score: function () {
                    $(this).attr("data-num", angular.isUndefined(vm.unitScoreDto.score) ? 0 : vm.unitScoreDto.score);
                    return $(this).attr("data-num");
                },
                starOn: '../contents/libs/raty/lib/images/star-on.png',
                starOff: '../contents/libs/raty/lib/images/star-off.png',
                starHalf: '../contents/libs/raty/lib/images/star-half.png',
                readOnly: false,
                halfShow: true,
                hints: ['不合格', '合格', '中等', '良好', '优秀'],
                size: 34,
                click: function (score, evt) {
                    vm.unitScoreDto.score = score;
                }
            });

            $("#unitscore_win").kendoWindow({
                width: "820px",
                height: "365px",
                title: "编辑-单位星级",
                visible: false,
                modal: true,
                closable: true,
                actions: ["Close"]
            }).data("kendoWindow").center().open();

        }
        //保存单位评分
        vm.saveUnit=function () {
            if (!vm.unitScoreDto.score || vm.unitScoreDto.score == 0) {
                bsWin.alert("请对单位进行评分！");
            }else {
                companySvc.saveUnit(vm.unitScoreDto, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        bsWin.success("保存成功！", function () {
                            vm.closeEditUnit();
                        });
                    } else {
                        bsWin.alert(data.reMsg);
                    }
                });
            }

        }
        // 关闭单位评分
        vm.closeEditUnit = function () {
            window.parent.$("#unitscore_win").data("kendoWindow").close();
        }

        /*****************E_单位评分******************/


    }
})();