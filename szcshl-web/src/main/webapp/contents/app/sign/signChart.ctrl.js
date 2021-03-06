(function () {
    'use strict';
    angular.module('app').controller('signChartCtrl', signChart);
    signChart.$inject = ['signChartSvc', 'bsWin'];
    function signChart(signChartSvc, bsWin) {
        var vm = this;
        vm.startTime = (new Date()).halfYearAgo();
        vm.endTime = new Date().Format("yyyy-MM-dd");

        vm.statsType = '1' ;// 默认是 申报金额统计
        vm.chartType = 'lineChart';//默认为折线图

        vm.reviewStage = [];//评审阶段



        /**
         * 图形切换触发事件
         */
        $("input[type='radio']").click(function(){
            var selectType = $("input[type='radio']:checked").val();
            if (selectType == 'lineChart') {
                vm.gotoLineChart();
            } else if (selectType == 'histogram') {
                vm.gotoHistogram();
            } else if (selectType == 'pie') {
                vm.gotoPie();
            }

        })


        /**
         * 通过开始和结束日期重新统计项目信息情况
         */
        vm.resetChart = function () {

            vm.resultData = []; //柱状图 - y轴的值
            //申报金额统计
            if(vm.statsType == '1'){
                vm.chartType = 'lineChart';//默认为折线图
                activate();
            }
            //项目类别
            if(vm.statsType == '2'){
                vm.chartType = 'histogram';//默认为柱状图
                vm.review = [ '项目建议书','可行性研究报告','项目概算', '资金申请报告', '设备清单（进口）', '设备清单（国产）',  '进口设备', '其它']; // 横坐标
                vm.title = "项目类别分布情况"; //统一标题
                vm.capital = ['市政工程', '房建工程', '信息工程', '设备采购', '其他']; // 柱状图类型
                vm.yAxisName = "项目个数"; // 柱状图 - y坐标单位
                vm.sz = [];//市政工程
                vm.fj = [];//房建工程
                vm.xx = [];//信息工程
                vm.sb = [];//设备采购
                vm.qt = [];//其他
                vm.lineLegendData = ['审定/申报']; // 折线 - 类型
                vm.lineResultValue = []; //折线 -
                vm.LineYAxisName = "百分比（%）"; //折线 - y坐标单位

                signChartSvc.findByTypeAndReview(vm, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        var resultData = data.reObj;
                        for(var j = 0 ; j < vm.review.length ; j ++ ){
                            for(var i =0 ; i < resultData.length ; i++) {
                                $.each(resultData[i], function (key, value) {
                                    if(key == vm.review[j]){
                                        vm.sz.push(value[0]);
                                        vm.fj.push(value[1]);
                                        vm.xx.push(value[2]);
                                        vm.sb.push(value[3]);
                                        vm.qt.push(value[4]);
                                    }
                                });
                            }
                        }
                        vm.resultData.push(vm.sz, vm.fj, vm.xx, vm.sb, vm.qt);
                        vm.gotoHistogram();
                    } else {
                        bsWin.error(data.reMsg);
                    }
                });
            }

            //项目金额
            if(vm.statsType == '3'){
                vm.chartType = 'pie';//默认为饼图
                vm.title = "项目申报投资金额分布情况"; //统一标题
                vm.stage = ['3000万以下', '3000万-1亿', '1亿-10亿', '10亿以上']; // 饼图 - 各分布范围

                signChartSvc.pieData(vm, function (data) {
                    if (data.flag || data.reCode == 'ok') {
                        vm.resultData = data.reObj;
                        if (vm.resultData != undefined && vm.resultData != null) {
                            vm.gotoPie();
                        } else {
                            bsWin.error("该时间段没有数据！");
                        }

                    } else {
                        bsWin.error(data.reMsg);
                    }

                });


            }

        }


        /**
         * 柱状图
         */
        vm.gotoHistogram = function () {
            vm.showHistogram = true;
            vm.showLineChart = false;
            vm.showPie = false;
            vm.series = [];
            for (var i = 0; i < vm.capital.length; i++) {
                vm.series.push(
                    {
                        name: vm.capital[i],
                        type: 'bar', //bar line
                        data: vm.resultData[i],
                        barWidth: 10,//设置柱子的宽度
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
         * 初始化柱状图数据
         */
        vm.initHistogram = function () {
            var myChart = echarts.init(document.getElementById('histogram')); //只能用javaScript获取节点，如用jquery方式则找不到节点
            var option = {
                title: {
                    text: vm.title,
                    // subtext: '按评审阶段划分',
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
                    },
                    optionToContent : function(opt){

                        var axisData = opt.xAxis[0].data;
                        var series = opt.series;
                        var table ='<table id="test" class="table-bordered table-striped" style="width:100%;text-align:center">';
                        table += '<tbody><tr>';

                        //遍历表头
                        table += '<th style="text-align: center;">评审阶段</th>';
                        for(var i=0 ; i<series.length; i++){
                            if(vm.statsType == '1'){

                                table += '<th style="text-align: center;">' + series[i].name + '(亿元)' + '</th>';
                            }
                            if(vm.statsType == '2'){
                                table += '<th style="text-align: center;">' + series[i].name + '(个)' + '</th>';
                            }
                        }
                        table  += '</tr>';

                        //遍历行
                        for (var i = 0, l = axisData.length; i < l; i++) {
                            table += '<tr>' + '<td>' + axisData[i] + '</td>';
                            for(var j = 0 ; j < series.length ; j++){
                                table += '<td>' + series[j].data[i] + '</td>';
                            }
                        }
                        table += '</tbody>';
                        return table;

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
                    name: vm.yAxisName,
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
            myChart.clear(); // 再次加载数据时，先清空表格，后再设置值，以防会保留原先的数据信息
            myChart.setOption(option);
        }//end initHistogram

        /**
         * 折线图
         */
        vm.gotoLineChart = function () {
            vm.showHistogram = false;
            vm.showPie = false;
            vm.showLineChart = true;
            vm.lineSeries = [];
            for (var i = 0; i < vm.lineLegendData.length; i++) {
                vm.lineSeries.push(
                    {
                        name: vm.lineLegendData[i],
                        type: 'line', //bar line
                        data: vm.lineResultValue,
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
            vm.initLineChart();

            //申报金额分布情况
            /*  if(vm.statsType == '1'){
             vm.series.push(
             {
             name: "审定/申报",
             type: 'line', //bar line
             data: vm.sdbsRatio,
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
             },
             }
             });
             vm.initLineChart();
             }*/
            /* if(vm.statsType == '2'){

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
             }*/
        }

        /**
         * 初始化折线图
         */
        vm.initLineChart = function () {
            var myChart = echarts.init(document.getElementById('lineChart'));
            var option = {
                title: {
                    text: vm.title,
                    // subtext: '按评审阶段划分',
                    x: 'center'
                },
                tooltip: {
                    trigger: 'item',
                    formatter: function(params){
                        for(var i = 0 ; i < vm.extraRate.length ; i++){
                            if(params.name == vm.extraRate[i].reviewStage){
                                return params.name + '<br/>' + ' 核减率 : ' + vm.extraRate[i].value + "%";
                            }

                        }

                    }
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
                    },
                    optionToContent : function(opt){
                        var axisData = opt.xAxis[0].data;
                        var series = opt.series;
                        var table ='<table id="test" class="table-bordered table-striped" style="width:100%;text-align:center">';
                        table += '<tbody><tr>';

                        //遍历表头
                        table +=  '<th style="text-align: center;">评审阶段</th>';
                        for(var i=0 ; i<series.length; i++){
                            table +=  '<th style="text-align: center;">' + series[i].name + '</th>';
                        }
                        table  +=  '</tr>';

                        //遍历行
                        for (var i = 0, l = axisData.length; i < l; i++) {
                            table += '<tr>' + '<td>' + axisData[i] + '</td>';
                            for(var j = 0 ; j < series.length ; j++){
                                table += '<td>' + series[j].data[i] + '</td>';
                            }
                        }
                        table += '</tbody>';
                        return table;

                    }
                },
                legend: {
                    orient: 'vertical',
                    left: 'left',
                    data: vm.lineLegendData
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
                    name: vm.LineYAxisName,
                    min: 0,
                    // max: 100,
                    // interval: 20

                },
                series: vm.lineSeries,
                lineStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0,0,0,0.5)'
                    }
                }
            };
            myChart.clear();
            myChart.setOption(option);

        }//end initLineChart


        /**
         * 饼图
         */
        vm.gotoPie = function () {
            vm.showHistogram = false;
            vm.showLineChart = false;
            vm.showPie = true;
            vm.series = [];
            vm.tooltipFormater = []; //饼图 - 提示框内容设置
            for (var i = 0; i < vm.resultData[1].length; i++) {
                vm.series.push(
                    {
                        value: vm.resultData[1][i],
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
                        totalValue: vm.resultData[2],
                        name: vm.stage[i],
                        pidName: "占百分比",
                        data: vm.resultData[0][i],
                        value: vm.resultData[1][i]

                    }
                );
            }
            vm.initPie();

            /*signChartSvc.pieData(vm, function (data) {
                if (data.flag || data.reCode == 'ok') {
                    var resultData = data.reObj;
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
*/
        }


        /**
         * 初始化饼图
         */
        vm.initPie = function () {
            var myChart = echarts.init(document.getElementById('pie'));
            var option = {
                title: {
                    text: vm.title,
                    // subtext: '按申报金额范围划分',
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
                    },
                    optionToContent : function(opt){
                        var series = opt.series[0].data;
                        var table ='<table id="test" class="table-bordered table-striped" style="width:100%;text-align:center">';
                        table += '<tbody><tr>';

                        //遍历表头
                        table += '<th style="text-align: center;">申报金额范围</th>';
                        table += '<th style="text-align: center;">' + '占百分比(%)' + '</th>';
                        table  += '</tr>';

                        //遍历行
                        for (var i = 0, l = series.length; i < l; i++) {
                            table += '<tr>' + '<td>' + series[i].name + '</td>';
                            table += '<td>' + series[i].value + '</td>';
                        }
                        table += '</tbody>';
                        return table;
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
            myChart.clear();
            myChart.setOption(option);
        }//end initPie


        activate();
        function activate() {
            vm.review = ['项目建议书', '可行性研究报告', '项目概算',  '资金申请报告',  '其它']; // 横坐标
            vm.title = "项目申报投资金额分布情况"; //统一标题
            vm.capital = ['申报金额', '审定金额']; // 柱状图类型
            vm.yAxisName = "金额（亿元）"; // 柱状图 - y坐标单位
            vm.lineLegendData = ['审定/申报']; // 折线 - 类型
            vm.lineResultValue = []; //折线 -
            vm.LineYAxisName = "百分比（%）"; //折线 - y坐标单位
            vm.appalyinvestment = []; //申报金额
            vm.authorizeValue = []; //审定金额
            vm.extraRate = [] ; //核减率
            // vm.projectCount = []; //项目数目
            vm.resultData = []; //柱状图- y轴的值

            signChartSvc.findByTime(vm, function (data) {
                if (data.flag || data.reCode == 'ok') {
                    var resultData = data.reObj;
                    if (resultData != undefined && resultData.length > 0) {
                        for(var j = 0 ; j < vm.review.length ; j ++ ){
                            for (var i = 0; i < resultData.length; i++) {
                                $.each(resultData[i], function (key, value) {
                                    if( key == vm.review[j]){
                                        // vm.reviewStage.push(key);
                                        vm.appalyinvestment.push(value[0]);
                                        vm.authorizeValue.push(value[1]);
                                        // vm.projectCount.push(value[2]);
                                        vm.lineResultValue.push(value[3] == undefined ? 0 : value[3]);
                                        vm.extraRate.push({
                                            reviewStage : key ,
                                            value : value[4] == undefined ? 0 : value[4]
                                        });
                                    }

                                });
                            }
                        }
                        vm.resultData.push(vm.appalyinvestment, vm.authorizeValue);
                        vm.gotoLineChart();
                    }
                } else {
                    bsWin.error(data.reMsg);
                }
            });
        }//end_activate







    }
})();