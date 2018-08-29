/**
 * 广西百色重大项目管理系统 市层面重大项目信息
 * @author: tzg
 */
import React from "react";
import {
    View,
    Text,
    Button,
    StyleSheet,
    FlatList,
    RefreshControl,
    ActivityIndicator,
    TouchableOpacity,
    TextInput,
    Picker
} from "react-native";
import Icon from 'react-native-vector-icons/Ionicons';
import ProjectListComponent from '../component/ProjectListComponent'
import Header from '../component/HeaderComponent'
import DatePicker from '../component/DatePickerComponent'
import SideMenu from 'react-native-side-menu';


type Props = {};
export default class ProjectSearchScreen extends React.Component {
    constructor(props){
        super(props);
        this.state={
            projectData:[],
            totalNum:'',
            isOpen:false
        }
    }
    componentWillMount() {
        this.setState({
            totalNum:15,
            projectData:[{
                itemName:'(市层面)田阳地税局新建项目1',
                itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
                totalInvestment:'500万',
                startDate:'2018-08-08',
            },{
                itemName:'(市层面)田阳地税局新建项目2',
                itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
                totalInvestment:'800万',
                startDate:'2018-09-08',
            },{
                itemName:'(市层面)田阳地税局新建项目3',
                itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
                totalInvestment:'805万',
                startDate:'2018-12-08',
            },{
                itemName:'(市层面)田阳地税局新建项目4',
                itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
                totalInvestment:'805万',
                startDate:'2018-12-08',
            },{
                itemName:'(市层面)田阳地税局新建项目5',
                itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
                totalInvestment:'805万',
                startDate:'2018-12-08',
            }]
        })
    };

    _Search(){
        this.setState({
            isOpen:false,
            projectData:[{
                itemName:'搜索结果1',
                itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
                totalInvestment:'500万',
                startDate:'2018-08-08',
                },{
                itemName:'搜索结果2',
                itemDescription:'项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述项目描述',
                totalInvestment:'500万',
                startDate:'2018-08-08',
            }
            ]
        })
    }
    _filter(){
        return(
            <TouchableOpacity style={styles.filterView} onPress={()=>this.setState({isOpen:true})}>
                <Icon name={'ios-search-outline'} size={18} color={'#fff'}/>
                <Text style={styles.filterText}>查找</Text>
            </TouchableOpacity>
        )
    };
    _renderItem(item){
        return(
            <View style={styles.item}>
                <Text style={styles.itemName}>{item.itemName}</Text>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>项目阶段：</Text>
                    <Text>项目概算</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>当前环节：</Text>
                    <Text>项目概算</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>签收时间：</Text>
                    <Text>2018-05-05</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>剩余工作日：</Text>
                    <Text>8</Text>
                </View>
                <TouchableOpacity activeOpacity={0.8} style={styles.more}>
                    <Text style={{color:'#fff',lineHeight:30,alignSelf:'center'}}>项目审批</Text>
                </TouchableOpacity>
            </View>
        )
    }
    _extraUniqueKey(item , index){
        return "index"+index+item;
    }
    sideMenuContent(){
        return(
            <View style={{flex:1,backgroundColor:'#fff',padding:15}}>
                <View>
                    <Text style={styles.inputItem}>项目名称</Text>
                    <TextInput
                        underlineColorAndroid='transparent'
                        style={styles.textInputStyle}
                    />
                </View>
                <View>
                    <Text style={styles.inputItem}>项目阶段</Text>
                    <View style={{borderWidth:0.5,borderColor:'#333',marginTop:10,marginBottom:10}}>
                        <Picker
                            selectedValue={this.state.stage}
                            style={{ height: 30, width: '100%',}}
                            onValueChange={(itemValue, itemIndex) => this.setState({stage: itemValue})}>
                            <Picker.Item label="项目概算" value="1" />
                            <Picker.Item label="项目建议书" value="2" />
                            <Picker.Item label="可行性研究报告" value="3" />
                        </Picker>
                    </View>
                </View>
                <View>
                    <Text style={styles.inputItem}>办理环节</Text>
                    <View style={{borderWidth:0.5,borderColor:'#333',marginTop:10}}>
                        <Picker
                            selectedValue={this.state.link}
                            style={{ height: 30, width: '100%'}}
                            itemStyle={{fontSize:12,color:'red'}}
                            onValueChange={(itemValue, itemIndex) => this.setState({link: itemValue})}>
                            <Picker.Item label="部长审批" value="1" />
                            <Picker.Item label="发文申请	" value="2" />
                            <Picker.Item label="项目负责人办理" value="3" />
                            <Picker.Item label="主办部长审批" value="4" />
                        </Picker>
                    </View>
                </View>
                <View>
                    <TouchableOpacity style={{width:'100%',backgroundColor:'#1E5AAF',marginTop:15}} onPress={()=>this._Search()}>
                        <Text style={{color:'#fff',alignSelf:'center',lineHeight:30}}>查询</Text>
                    </TouchableOpacity>
                </View>
            </View>
        )


    }
    render() {
        return (
            <SideMenu
                menu={this.sideMenuContent()}
                isOpen={this.state.isOpen}
                menuPosition='right'
            >
                <View style={styles.container}>
                    <Header title={'项目查询'} headerRight={this._filter()}/>
                    <View style={styles.container}>
                        <FlatList
                            style={styles.itemsView}
                            keyExtractor = {this._extraUniqueKey}
                            data={this.state.projectData}
                            renderItem={({item}) => this._renderItem(item)}
                        />
                    </View>
                </View>
            </SideMenu>
        );

    }

}

const styles = StyleSheet.create({
    container: {
        flex:1,
        height:'100%',
        width:'100%',
        alignItems: 'center',
        justifyContent: 'flex-start',
        backgroundColor:'#fff'
    },
    filterView:{
        flexDirection:'row',
        justifyContent:'flex-end',
        alignItems:'center'
    },
    filterText:{
        color:'#fff',
        lineHeight:50,
        marginLeft:5
    },
    textInputStyle:{
        borderWidth:0.5,
        borderColor:'#333',
        lineHeight:30,
        fontSize: 15,
        paddingLeft:10,
        paddingTop:0,
        paddingBottom:0,
        marginTop:10,
        marginBottom:10
    },
    inputItem:{
        color:'#000',
        fontSize:16
    },
    itemsView:{
        width:'100%'
    },
    item:{
        width:'100%',
        backgroundColor:'#eee',
        marginBottom:15,
        padding:15,
        elevation: 4,
    },
    itemName:{
        fontSize:18,
        color:'#000',
        fontWeight:'bold',
        marginBottom:5,
    },
    desView:{
        height:80,
    },
    itemFooter:{
        height:50,
        alignItems:'center',
        flexDirection:'row',
        justifyContent:'space-between',
    },
    more:{
        paddingLeft:10,
        paddingRight:10,
        backgroundColor:'#1E5AAF',
        height:30,
        borderRadius:30,
        marginTop:15
    },
    itemView:{
        flexDirection:'row',
        justifyContent:'flex-start',
        height:30,
        alignItems:'center'
    },
    itemText:{
        fontSize:14,
        color:'#000',
    }
});