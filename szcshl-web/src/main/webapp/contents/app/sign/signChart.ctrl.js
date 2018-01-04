(function () {
    'use strict';
    angular.module('app').controller('signChartCtrl', signChart);
    signChart.$inject = ['signChartSvc', 'bsWin'];
    function signChart(signChartSvc, bsWin) {
        var vm = this;
        vm.startTime = (new Date()).halfYearAgo();
        vm.endTime = new Date().Format("yyyy-MM-dd");
        vm.chartType = 'histogram';//默认为柱状图
        vm.reviewStage = [];//评审阶段
        vm.appalyinvestment = [];//申报金额
        vm.authorizeValue = [];//审定金额
        vm.sz = [];//市政工程
        vm.fj = [];//房建工程
        vm.xx = [];//信息工程
        vm.sb = [];//设备采购
        vm.qt = [];//其他
        vm.series = [];
        vm.resultData = [];//存series中Data的值
        vm.projectCount = [];//各阶段项目数目
        vm.tooltipFormater = []; // 饼图提示框对应
        vm.capital = ['申报金额', '审定金额'];
        vm.projectType = ['市政工程', '房建工程', '信息工程', '设备采购', '其他'];
        vm.stage = ['3000万以下', '3000万-1亿', '1亿-10亿', '10亿以上'];
        vm.review = ['项目概算', '项目建议书', '进口设备', '资金申请报告', '设备清单（进口）', '设备清单（国产）', '可行性研究报告', '其他'];

        /**
         * 通过开始和结束日期重新统计项目信息情况
         */
        vm.resetChart = function () {
            vm.reviewStage = [];//评审阶段
            vm.appalyinvestment = [];//申报金额
            vm.authorizeValue = [];//审定金额
            vm.sz = [];//市政工程
            vm.fj = [];//房建工程
            vm.xx = [];//信息工程
            vm.sb = [];//设备采购
            vm.qt = [];//其他
            vm.series = [];
            vm.resultData = [];//存series中Data的值
            vm.projectCount = [];//各阶段项目数目
            vm.tooltipFormater = []; // 饼图提示框对应
            var typeChecked = $("input[type='radio']:checked").val();
            if (typeChecked == 'lineChart') {
                vm.gotoLineChart();
            } else if (typeChecked == 'histogram') {
                activate();
            } else if (typeChecked == 'pie') {
                vm.gotoPie();
            }
        }

        /**
         * 柱状图
         */
        vm.gotoHistogram = function () {
            vm.showHistogram = true;
            vm.showLineChart = false;
            vm.showPie = false;
            for (var i = 0; i < vm.capital.length; i++) {
                vm.series.push(
                    {
                        name: vm.capital[i],
                        type: 'bar', //bar line
                        data: vm.resultData[i],
                        barWidth: 30,//设置柱子的宽度
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    position: 'top',
                                    formatter: function (params) {
                                        return params.value;
                                    }
                                }
                            }
                        },
                    }
                );
            }
            vm.initHistogram();

        }

        /**
         * 折线图
         */
        vm.gotoLineChart = function () {
            vm.showHistogram = false;
            vm.showPie = false;
            vm.showLineChart = true;
            signChartSvc.findByTypeAndReview(vm, function (data) {
                if (data.flag || data.reCode == 'ok') {
                    var resultData = data.reObj;
                    if (resultData != undefined && resultData.length > 0) {
                        for (var i = 0; i < resultData.length; i++) {
                            $.each(resultData[i], function (key, value) {
                                vm.reviewStage.push(key);
                                vm.sz.push(value[0]);
                                vm.fj.push(value[1]);
                                vm.xx.push(value[2]);
                                vm.sb.push(value[3]);
                                vm.qt.push(value[4]);

                            });
                        }
                        vm.resultData.push(vm.sz, vm.fj, vm.xx, vm.sb, vm.qt);
                        for (var i = 0; i < vm.projectType.length; i++) {
                            vm.series.push(
                                {
                                    name: vm.projectType[i],
                                    type: 'line', //bar line
                                    data: vm.resultData[i],
                                    itemStyle: {
                                        normal: {
                                            label: {
                                                show: true,
                                                position: 'top',
                                                textStyle: { //设置图表上数目的大小
                                                    fontWeight: 'normal',
                                                    fontSize: 15
                                                },
                                                formatter: function (params) {
                                                    return params.value;
                                                },

                                            }
                                        }
                                    },
                                }
                            );
                        }
                    }
                    vm.initLineChart();
                } else {
                    bsWin.error(data.reMsg);
                }
            });
        }

        /**
         * 饼图
         */
        vm.gotoPie = function () {
            vm.showHistogram = false;
            vm.showLineChart = false;
            vm.showPie = true;
            signChartSvc.pieData(vm, function (data) {
                if (data.flag || data.reCode == 'ok') {
                    var resultData = data.reObj;
                    //console.log(235);
                    //console.log(resultData);
                    if (resultData != undefined && resultData != null) {
                        if (resultData[1] != undefined && resultData[1].length > 0) {
                            for (var i = 0; i < resultData[1].length; i++) {
                                vm.series.push(
                                    {
                                        value: resultData[1][i],
                                        name: vm.stage[i],
                                        label: {
                                            normal: {
                                                formatter: ' {b} : {c}%',
                                                textStyle: {
                                                    fontWeight: 'normal',
                                                    fontSize: 15
                                                }
                                            }
                                        }
                                    }
                                )

                                vm.tooltipFormater.push(
                                    {
                                        seriesName: "项目数",
                                        totalName: "项目总数",
                                        totalValue: resultData[2],
                                        name: vm.stage[i],
                                        pidName: "占百分比",
                                        data: resultData[0][i],
                                        value: resultData[1][i]

                                    }
                                );
                            }
                        }
                        vm.initPie();
                    } else {
                        bsWin.error("该时间段没有数据！");
                    }

                } else {
                    bsWin.error(data.reMsg);
                }

            });

        }

        /**
         * 初始化柱状图数据
         */
        vm.initHistogram = function () {
            var myChart = echarts.init(document.getElementById('histogram')); //只能用javaScript获取节点，如用jquery方式则找不到节点
            var option = {
                title: {
                    text: "申报金额与审定金额统计情况",
                    subtext: '按评审阶段划分',
                    x: 'center'
                },
                tooltip: {//提示框设置
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross', //cross  line  shadow
                        label: {
                            backgroundColor: '#283b56'
                        }
                    }
                },
                legend: { //头部显示说明，注意：data值要与series中的name一致，顺序可以不一致
                    orient: 'vertical',
                    left: 'left',
                    data: vm.capital
                },
                //工具栏设置
                toolbox: {
                    show: true,
                    x: '80%',
                    feature: {
                        dataView: {show: true, readOnly: true},//数据统计
                        restore: {show: true},//还原
                        saveAsImage: {show: true}//下载
                    }
                },

                //设置坐标
                grid: {
                    // 这些是设置坐标边距
                    left: '13%',
                    right: '10%',
                    bottom: '10%',
                    containLabel: true,
                    //设置xy轴宽度
                    // y : 70,
                    // x : 60
                },
                xAxis: {
                    type: 'category',
                    name: '评审阶段',
                    data: vm.review,
                    axisTick: {
                        alignWithLabel: true
                    },
                    axisLabel: {
                        interval: 0,
                        rotate: 35,//倾斜度 -90 至 90 之间，默认为0
                        margin: 2,
                        textStyle: {
                            fontWeight: 'bolder',
                            color: '#295645'
                        }
                    }
                },
                yAxis: {
                    type: 'value',
                    name: '金额（亿元）',
                    min: 0,
                    // max: 1000,
                    // interval: 100, //刻度值
                },
                series: vm.series,
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0,0,0,0.5)'
                    }
                }
            };

            myChart.setOption(option);
        }//end initHistogram

        /**
         * 初始化折线图
         */
        vm.initLineChart = function () {
            var myChart = echarts.init(document.getElementById('lineChart'));
            var option = {
                title: {
                    text: '评审项目类别统计情况',
                    subtext: '按评审阶段划分',
                    x: 'center'
                },
                tooltip: {
                    trigger: 'item',
                    formatter: '{a} <br/> {b} : {c}'
                },
                //设置坐标
                grid: {
                    left: '13%',
                    right: '10%',
                    bottom: '10%',
                    containLabel: true,
                },
                toolbox: {
                    show: true,
                    x: '80%',
                    feature: {
                        dataView: {show: true, readOnly: true},//数据统计
                        restore: {show: true},//还原
                        saveAsImage: {show: true}//下载
                    }
                },
                legend: {
                    orient: 'vertical',
                    left: 'left',
                    data: vm.projectType
                },
                xAxis: {
                    type: 'category',
                    name: '评审阶段',
                    splitLine: {show: false},
                    data: vm.review,
                    axisLabel: {
                        interval: 0,
                        rotate: 35,//倾斜度 -90 至 90 之间，默认为0
                        margin: 2,
                        textStyle: {
                            fontWeight: 'bolder',
                            color: '#295645'
                        }
                    }
                },
                yAxis: {
                    type: 'value',
                    name: '项目个数',
                    min: 0,
                    // max: 100,
                    // interval: 20

                },
                series: vm.series,
                lineStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0,0,0,0.5)'
                    }
                }
            };
            myChart.setOption(option);

        }//end initLineChart

        /**
         * 初始化饼图
         */
        vm.initPie = function () {
            var myChart = echarts.init(document.getElementById('pie'));
            var option = {
                title: {
                    text: '评审项目比例情况',
                    subtext: '按申报金额范围划分',
                    x: 'center'
                },
                tooltip: {
                    trigger: 'item',
                    width: 150,
                    formatter: function (params) {
                        for (var i = 0; i < vm.tooltipFormater.length; i++) {
                            if (vm.tooltipFormater[i].name == params.name) {
                                return params.name + "<br/>"
                                    + vm.tooltipFormater[i].seriesName + " : " + vm.tooltipFormater[i].data + "<br/>"
                                    + vm.tooltipFormater[i].totalName + " : " + vm.tooltipFormater[i].totalValue + "<br/>"
                                    + vm.tooltipFormater[i].pidName + " : " + vm.tooltipFormater[i].value + "%";
                            }

                        }
                    }
                },
                grid: {
                    left: '10%',
                    right: '10%',
                    bottom: '10%',
                    containLabel: true,
                },
                toolbox: {
                    show: true,
                    x: '80%',
                    feature: {
                        dataView: {show: true, readOnly: true},//数据统计
                        restore: {show: true},//还原
                        saveAsImage: {show: true}//下载
                    }
                },
                legend: {
                    orient: 'vertical',
                    left: 'left',
                    data: vm.stage
                },
                series: [
                    {
                        type: 'pie',
                        radius: '30%',//半径
                        center: ['50%', '50%'],
                        selectMode: 'single',
                        data: vm.series,
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0,0,0,0.5)'
                            }
                        }

                    }
                ]

            };

            myChart.setOption(option);
        }//end initPie


        activate();
        function activate() {
            signChartSvc.findByTime(vm, function (data) {
                if (data.flag || data.reCode == 'ok') {
                    var resultData = data.reObj;
                    if (resultData != undefined && resultData.length > 0) {
                        for (var i = 0; i < resultData.length; i++) {
                            $.each(resultData[i], function (key, value) {
                                vm.reviewStage.push(key);
                                vm.appalyinvestment.push(value[0]);
                                vm.authorizeValue.push(value[1]);
                                vm.projectCount.push(value[2]);
                            });
                        }
                        vm.resultData.push(vm.appalyinvestment, vm.authorizeValue);
                        vm.gotoHistogram();
                    }
                } else {
                    bsWin.error(data.reMsg);
                }
            });
        }//end_activate


    }
})();