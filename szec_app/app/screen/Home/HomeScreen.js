import React, {Component} from 'react';
import {View, Text, Alert, ScrollView, WebView, Dimensions, StyleSheet, TouchableOpacity,AsyncStorage} from 'react-native';
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
            proMeetInfo: '',
            userName:''
        };
    };
    loadData(){
        axios({
            url: "/agenda/getHomeProjInfo",
            method: "post",
            params: {
                username: this.state.userName
            },
        })
            .then((res) => {
                if (res.data.reCode === 'ok') {
                    let surplusDays = [], projectName = [],dayArr=[];
                    for (let item of res.data.reObj.lineSign) {
                        surplusDays.push(item.surplusDays);
                        projectName.push(item.projectName)
                    }
                    for(let item of surplusDays){
                        item = item < -3 ? -3:item;
                        dayArr.push(item);
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
                            },
                            min:-3,
                            max:15,
                        },
                        series: [
                            {
                                name: '剩余工作日',
                                type: 'line',
                                data: dayArr,
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
    componentDidMount() {
        AsyncStorage.getItem('userName', (error, result) => {
            if (!error) {
                this.setState({
                    userName: result
                },()=>this.loadData());
            } else {
                console.log(error);
            }
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
                    <TouchableOpacity activeOpacity={0.8} style={styles.headItem} onPress={()=>this.props.navigation.navigate('leadpro')}>
                        <View style={styles.iconView}>
                            <Icon name='ios-list-box' size={35} color={'#ff9700'}/>
                        </View>
                        <Text>项目审批</Text>
                    </TouchableOpacity>
                    <TouchableOpacity activeOpacity={0.8} style={styles.headItem} onPress={()=>this.props.navigation.navigate('Project')}>
                        <View style={styles.iconView}>
                            <Icon name='ios-list-box' size={35} color={'#1E5AAF'}/>
                        </View>
                        <Text>项目查询统计</Text>
                    </TouchableOpacity>
                    <TouchableOpacity activeOpacity={0.8} style={styles.headItem} onPress={()=>this.props.navigation.navigate('ProRepeatScreen')}>
                        <View style={styles.iconView}>
                            <Icon name='ios-list-box' size={35} color={'#94af10'}/>
                        </View>
                        <Text>项目重新分办</Text>
                    </TouchableOpacity>
                </View>
                <View>
                    <View style={styles.chart}>
                        <View style={styles.chartTitle}>
                            <View style={styles.line}/>
                            <Text style={styles.titleStyle}>项目办理情况</Text>
                           {/* <TouchableOpacity onPress={() => navigation.navigate('ProjectManagementScreen')}>
                                <Text style={styles.more}>查看更多</Text>
                            </TouchableOpacity>*/}
                        </View>
                        <View style={styles.chartView}>
                            <ChartComponent
                                height={240}
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
                        <View style={{height: 200, width: '100%'}}>
                            <ScrollableTabView
                                initialPage={0}
                                tabBarActiveTextColor='#1E5AAF'
                                tabBarUnderlineStyle={{backgroundColor: '#1E5AAF', height: 2}}
                                renderTabBar={() => <ScrollableTabBar/>}>
                                {
                                    meetingDate.map((item, index) => {
                                        return <MettingInfo index={index} proAmMeetDtoList={this.state.proMeetInfo}
                                                            tabLabel={item} key={index}/>
                                    })
                                }
                            </ScrollableTabView>
                        </View>
                    </View>
                </View>
            </View>

        );
    }
}

class MettingInfo extends Component {
    constructor(props) {
        super(props);
    }

    getMeetting(mt, index) {
        let meetting = [];
        for (let i in mt) {
            switch (index) {
                case 0:
                    mt[i].proName1 !== undefined && meetting.push(<Text style={styles.meettingText}
                                                                        key={i}>{mt[i].proName1}</Text>);
                    break;
                case 1:
                    mt[i].proName2 !== undefined && meetting.push(<Text style={styles.meettingText}
                                                                        key={i}>{mt[i].proName2}</Text>);
                    break;
                case 2:
                    mt[i].proName3 !== undefined && meetting.push(<Text style={styles.meettingText}
                                                                        key={i}>{mt[i].proName3}</Text>);
                    break;
                case 3:
                    mt[i].proName4 !== undefined && meetting.push(<Text style={styles.meettingText}
                                                                        key={i}>{mt[i].proName4}</Text>);
                    break;
                case 4:
                    mt[i].proName5 !== undefined && meetting.push(<Text style={styles.meettingText}
                                                                        key={i}>{mt[i].proName5}</Text>);
                    break;
                default:
                    break;
            }
        }
        return meetting;
    }

    render() {
        const {proAmMeetDtoList, proPmMeetDtoList} = this.props.proAmMeetDtoList;
        let amMeetting = this.getMeetting(proAmMeetDtoList, this.props.index);
        let pmMeetting = this.getMeetting(proPmMeetDtoList, this.props.index);
        let noMeetting = <View style={styles.noMeetting}><Text>暂无会议信息</Text></View>;
        let meettingView =
            <ScrollView>
                {
                    amMeetting.length > 0 &&
                <View style={{flexDirection: 'row', borderTopWidth: 0.5, borderTopColor: '#ccc'}}>
                    <View style={styles.meettingView}>
                        <Text style={styles.metDate}>上午</Text>
                    </View>
                    <View style={{width: '80%'}}>
                        {amMeetting}
                    </View>
                </View>
                }
                {
                    pmMeetting.length > 0 &&
                    <View style={{flexDirection: 'row', borderTopWidth: 0.5, borderTopColor: '#ccc'}}>
                        <View style={styles.meettingView}>
                            <Text style={styles.metDate}>下午</Text>
                        </View>
                        <View style={{width: '80%'}}>
                            {pmMeetting}
                        </View>
                    </View>
                }
        </ScrollView>;
        if (amMeetting.length <= 0 && pmMeetting <= 0) {
            return noMeetting
        } else {
            return meettingView
        }
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
        height: 250
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
    },
    metDate: {
        fontSize: 15,
        color: '#000',
        alignSelf: 'center',
        lineHeight: 35,
    },
    meettingText: {
        paddingLeft: 10,
        lineHeight: 35,
        borderBottomWidth: 0.5,
        borderBottomColor: '#ccc',
        fontSize: 14
    },
    meettingView: {
        width: '20%',
        backgroundColor: '#eee',
        justifyContent: 'center',
        borderWidth: 0.5,
        borderColor: '#ddd'
    },
    noMeetting:{
        alignItems:'center',
        marginTop:10
    }
});