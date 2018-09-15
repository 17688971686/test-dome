import React, {Component} from 'react';
import {View, Text, Alert, ScrollView, WebView, Dimensions, StyleSheet, TouchableOpacity} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import Header from '../../component/HeaderComponent'
import ChartComponent from "../../component/ECharts/ChartComponent";
import ScrollableTabView, {ScrollableTabBar, DefaultTabBar} from 'react-native-scrollable-tab-view';
import axios from 'axios';

export default class ListScreen extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            lineOPtions: {},
            data: '',
            surplusDays: [],
            lineSign: '',
            proMeetInfo: ''
        };
    }

    componentDidMount() {
        axios({
            url: "/agenda/getHomeProjInfo",
            method: "post",
            params: {
                username: '张一帆'
            },
        })
            .then((res) => {
                if (res.data.reCode === 'ok') {
                    let surplusDays = [], projectName = [];
                    for (let item of res.data.reObj.lineSign) {
                        surplusDays.push(item.surplusDays);
                        projectName.push(item.projectName)
                    }
                    let lineOptions = {
                        title: {
                            text: '项目办理情况',
                            textStyle: {
                                fontSize: 16
                            },
                            x: 'center'
                        },
                        tooltip: {
                            trigger: 'axis',
                            enterable: true,
                            confine: true,
                        },
                        xAxis: {
                            name: '项目名称',
                            show: false,
                            type: 'category',
                            boundaryGap: false,
                            data: projectName
                        },
                        yAxis: {
                            type: 'value',
                            name: '剩余工作日',
                            axisLabel: {
                                formatter: '{value}天'
                            }
                        },
                        series: [
                            {
                                name: '剩余工作日',
                                type: 'line',
                                data: surplusDays,
                                showAllSymbol: true,
                                markPoint: {
                                    data: [
                                        {type: 'max', name: '最大值'},
                                        {type: 'min', name: '最小值'}
                                    ]
                                },
                            }
                        ]
                    };
                    console.log(res);
                    this.setState({
                        data: res.data.reObj,
                        proMeetInfo: res.data.reObj.proMeetInfo,
                        lineOPtions: lineOptions
                    })
                }

            })
            .catch(error => {
                console.log(error)
            });
    }

    //获取N天后的日期
    GetDateStr(n) {
        let dd = new Date();
        dd.setDate(dd.getDate() + n);//获取AddDayCount天后的日期
        let m = (dd.getMonth() + 1) < 10 ? "0" + (dd.getMonth() + 1) : (dd.getMonth() + 1);//获取当前月份的日期，不足10补0
        let d = dd.getDate() < 10 ? "0" + dd.getDate() : dd.getDate();//获取当前几号，不足10补0
        let w = "星期" + "日一二三四五六".charAt(dd.getDay());
        return m + '月' + d + '日' + w;
    }

    render() {
        let meetingDate = [];
        const {navigation} = this.props;
        for (let i = 0; i < 5; i++) {
            meetingDate.push(this.GetDateStr(i));
        }
        return (
            <View style={styles.container}>
                <Header title={'评审系统'}/>
                <View style={styles.headerBg}/>
                <View style={styles.header}>
                    <TouchableOpacity activeOpacity={0.8} style={styles.headItem}>
                        <View style={styles.iconView}>
                            <Icon name='ios-list-box' size={35} color={'#ff9700'}/>
                        </View>
                        <Text>待办项目</Text>
                    </TouchableOpacity>
                    <TouchableOpacity activeOpacity={0.8} style={styles.headItem}>
                        <View style={styles.iconView}>
                            <Icon name='ios-list-box' size={35} color={'#15BC83'}/>
                        </View>
                        <Text>待办任务</Text>
                    </TouchableOpacity>
                    <TouchableOpacity activeOpacity={0.8} style={styles.headItem}>
                        <View style={styles.iconView}>
                            <Icon name='ios-list-box' size={35} color={'#1E5AAF'}/>
                        </View>
                        <Text>项目查询</Text>
                    </TouchableOpacity>
                    <TouchableOpacity activeOpacity={0.8} style={styles.headItem}>
                        <View style={styles.iconView}>
                            <Icon name='ios-list-box' size={35} color={'#FF4800'}/>
                        </View>
                        <Text>项目统计</Text>
                    </TouchableOpacity>
                </View>
                <ScrollView>
                    <View style={styles.chart}>
                        <View style={styles.chartTitle}>
                            <View style={styles.line}/>
                            <Text style={styles.titleStyle}>项目办理情况</Text>
                            <TouchableOpacity onPress={() => navigation.navigate('ProjectManagementScreen')}>
                                <Text style={styles.more}>查看更多</Text>
                            </TouchableOpacity>
                        </View>
                        <View style={styles.chartView}>
                            <ChartComponent
                                height={280}
                                ref="charts"
                                option={this.state.lineOPtions}
                            />
                        </View>
                    </View>
                    <View style={styles.chart}>
                        <View style={styles.chartTitle}>
                            <View style={styles.line}/>
                            <Text style={styles.titleStyle}>调研和会议统计信息</Text>
                        </View>
                        <View style={{height: 300, width: '100%'}}>
                            <ScrollableTabView
                                initialPage={0}
                                tabBarActiveTextColor='#1E5AAF'
                                tabBarUnderlineStyle={{backgroundColor: '#1E5AAF', height: 2}}
                                renderTabBar={() => <ScrollableTabBar/>}>
                                {
                                    meetingDate.map((item, index) => {
                                        return <MettingInfo proAmMeetDtoList={this.state.proMeetInfo.proAmMeetDtoList}
                                                            tabLabel={item} index={index + 1} key={index}/>
                                    })
                                }
                            </ScrollableTabView>
                        </View>
                    </View>
                </ScrollView>
            </View>

        );
    }
}

class MettingInfo extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        let amMeetthis = this.props.proAmMeetDtoList;
        let meetting = [];
        for (let i in amMeetthis) {
            meetting.push(<Text>{amMeetthis[i].proName1}</Text>)
        }
        return (
            <View>
                <View>
                    <Text>上午</Text>
                </View>
                <View>
                    {meetting}
                </View>
                <View>
                    <Text>下午</Text>
                </View>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'flex-start'
    },
    headerBg: {
        width: '100%',
        height: 60,
        backgroundColor: '#1E5AAF',
        marginBottom: 70
    },
    header: {
        width: '90%',
        height: 120,
        backgroundColor: '#fff',
        position: 'absolute',
        top: 50,
        elevation: 5,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-around',
    },
    iconView: {
        height: 60,
        width: 60,
        borderRadius: 60,
        backgroundColor: '#F7F6F6',
        alignItems: 'center',
        justifyContent: 'center',
        marginBottom: 15
    },
    chart: {
        width: '100%',
        backgroundColor: '#fff',
        elevation: 5,
        marginBottom: 10,
    },
    chartTitle: {
        height: 35,
        borderBottomWidth: 0.8,
        borderBottomColor: '#1E5AAF',
        paddingLeft: 25,
        justifyContent: 'space-between',
        flexDirection: 'row',
        alignItems: 'center',
        paddingRight: 15
    },
    titleStyle: {
        fontWeight: 'bold'
    },
    chartView: {
        paddingTop: 10,
        height: 280
    },
    line: {
        position: 'absolute',
        width: 5,
        height: 17,
        backgroundColor: '#1956AD',
        left: 12
    },
    more: {
        fontSize: 12,
        color: '#999'
    }
});