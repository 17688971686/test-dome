import React ,{Component} from 'react';
import { View, Text,ScrollView ,WebView, Dimensions,StyleSheet,TouchableOpacity} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import ChartComponent from "../component/ECharts/ChartComponent";


export default class ListScreen extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            barOPtions:{},
            pieOptions:{}
        };
    }
    componentDidMount() {
        this.setState({
            barOPtions:{
                title: {
                    text: '',
                },
                tooltip: {},
                legend: {
                    data:['销量']
                },
                xAxis: {
                    data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
                },
                yAxis: {},
                series: [{
                    name: '销量',
                    type: 'bar',
                    data: [5, 20, 36, 10, 10, 20]
                }]
            },
            pieOptions:{
                series : [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius: '55%',
                        data:[
                            {value:235, name:'视频广告'},
                            {value:274, name:'联盟广告'},
                            {value:310, name:'邮件营销'},
                            {value:335, name:'直接访问'},
                            {value:400, name:'搜索引擎'}
                        ]
                    }
                ]
            }
        })
    }
    render() {
        return (
                <View style={styles.container}>
                    <View style={{height: 50,width:'100%', backgroundColor: '#1E5AAF',elevation:0,alignItems:'center'}}>
                        <Text style={{fontSize: 18, color: '#fff',lineHeight:50}}>深圳评审中心项目评审系统</Text>
                    </View>
                    <View style={styles.headerBg}/>
                    <View style={styles.header}>
                        <TouchableOpacity activeOpacity={0.8} style={styles.headItem}>
                            <View style={styles.iconView}>
                                <Icon name='ios-list-box' size={35} color={'#ff9700'}/>
                            </View>
                            <Text>待办事项</Text>
                        </TouchableOpacity>
                        <TouchableOpacity activeOpacity={0.8} style={styles.headItem}>
                            <View style={styles.iconView}>
                                <Icon name='ios-list-box' size={35} color={'#15BC83'}/>
                            </View>
                            <Text>待办事项</Text>
                        </TouchableOpacity>
                        <TouchableOpacity activeOpacity={0.8} style={styles.headItem}>
                            <View style={styles.iconView}>
                                <Icon name='ios-list-box' size={35} color={'#1E5AAF'}/>
                            </View>
                            <Text>待办事项</Text>
                        </TouchableOpacity>
                        <TouchableOpacity activeOpacity={0.8} style={styles.headItem}>
                            <View style={styles.iconView}>
                                <Icon name='ios-list-box' size={35} color={'#FF4800'}/>
                            </View>
                            <Text>待办事项</Text>
                        </TouchableOpacity>
                    </View>
                    <ScrollView>
                        <View style={styles.chart}>
                            <View style={styles.chartTitle}>
                                <View style={styles.line}/>
                                <Text style={styles.titleStyle}>市领导重大项目进度统计</Text>
                            </View>
                            <View style={styles.chartView}>
                                <ChartComponent
                                    ref="charts"
                                    option={this.state.barOPtions}
                                />
                            </View>
                        </View>
                        <View style={styles.chart}>
                            <View style={styles.chartTitle}>
                                <View style={styles.line}/>
                                <Text style={styles.titleStyle}>市层面重大项目进度统计</Text>
                            </View>
                            <View style={styles.chartView}>
                                <ChartComponent
                                    ref="charts"
                                    option={this.state.pieOptions}
                                />
                            </View>
                        </View>
                    </ScrollView>
                </View>

        );
    }
}
const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'flex-start'
    },
    headerBg:{
        width:'100%',
        height:70,
        backgroundColor:'#1E5AAF',
        marginBottom:70
    },
    header:{
        width:'90%',
        height:120,
        backgroundColor:'#fff',
        position:'absolute',
        top:60,
        elevation:5,
        flexDirection:'row',
        alignItems:'center',
        justifyContent:'space-around',
    },
    iconView:{
        height:60,
        width:60,
        borderRadius:60,
        backgroundColor:'#F7F6F6',
        alignItems:'center',
        justifyContent:'center',
        marginBottom:15
    },
    chart:{
        width:'100%',
        backgroundColor:'#fff',
        elevation:5,
        marginBottom:10
    },
    chartTitle:{
        height:35,
        borderBottomWidth:0.5,
        borderBottomColor:'#ddd',
        paddingLeft:25,
        justifyContent:'center'
    },
    titleStyle:{
      fontWeight:'bold'
    },
    chartView:{
        height:250
    },
    line:{
        position:'absolute',
        width:5,
        height:17,
        backgroundColor:'#1956AD',
        left:12
    }
});