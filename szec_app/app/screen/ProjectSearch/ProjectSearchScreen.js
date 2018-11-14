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
    Picker,
    DeviceEventEmitter,
    Keyboard
} from "react-native";
import axios from 'axios'
import Icon from 'react-native-vector-icons/Ionicons';
import ProjectListComponent from '../../component/ProjectListComponent'
import Header from '../../component/HeaderComponent'
import SideMenu from 'react-native-side-menu';


type Props = {};

const api = '/sign/findSignByPage';
export default class ProjectSearchScreen extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isOpen: false,
            filter: {
                manager:{
                    managerUser:'allPriUser'
                }
            },
            PRO_STAGE:[],
            SECRECTLEVEL:[],
            mOrgId:[]
        }
    }

    componentWillMount() {
        //获取评审阶段、秘密等级数据接口
        axios.post('/sign/initDictList')
            .then(res=> {
                this.setState({
                    PRO_STAGE:res.data.reObj.PRO_STAGE,
                    SECRECTLEVEL:res.data.reObj.SECRECTLEVEL
                })
            })
            .catch(error=>{
                console.log(error);
            });
        //获取评审部门
        axios.post('/sign/initDeptList')

            .then(res=> {
                console.log(res);
                this.setState({
                    mOrgId:res.data.reObj
                })
            })
            .catch(error=>{
                console.log(error);
            });
    }

    _filter() {
        return (
            <TouchableOpacity style={styles.filterView} onPress={() => this.setState({isOpen: true})}>
                <Icon name={'ios-search-outline'} size={18} color={'#fff'}/>
                <Text style={styles.filterText}>查找</Text>
            </TouchableOpacity>
        )
    };

    combination(parmName, parmVal) {
        let filter = this.state.filter;
        switch (parmName) {
            case 'projectname' :
                filter.projectname = parmVal;
                break;
            case 'reviewstage' :
                filter.reviewstage = parmVal;
                break;
            case 'secrectlevel' :
                filter.secrectlevel = parmVal;
                break;
            case 'mOrgId' :
                filter.mOrgId = parmVal;
                break;
            case 'manager':
                filter.manager.managerUser = parmVal;
                break;
            case 'managerName':
                filter.manager.managerName = parmVal;
                break;
            default:
                break;
        }
        this.setState({
            filter: filter
        },()=>{
            console.log(filter)
        })
    }

    _search() {
        let filter = this.state.filter,
            filterStr = '';
        for (let i in filter) {
            if (filter[i] !== '请选择' && filter[i] !== '') {
                switch (i) {
                    case 'projectname':
                        filterStr += " and substringof(" + "'" + filter[i] + "'" + "," + i + ")";
                        break;
                    case 'manager':
                        if (filter[i].managerName) {
                            filterStr += " and substringof(" + "'" + filter[i].managerName + "'" + "," + filter[i].managerUser + ")";
                        }
                        break;
                    default:
                        filterStr += " and " + i + " eq " + "'" + filter[i] + "'";
                        break;
                }
            }
        }
        DeviceEventEmitter.emit('search', filterStr);
        this.setState({
            isOpen: false
        });
        Keyboard.dismiss();
    }
    _clearFilter(){
        let filterStr = '';
        this.setState({
            filter:{
                manager:{
                    managerUser:'allPriUser'
                }
            },
        },()=>{
            DeviceEventEmitter.emit('search', filterStr);
            this.setState({
                isOpen: false
            });
            Keyboard.dismiss();
        })
    }
    renderPickItem(itemList){
        let pickItemList=[];
        pickItemList.push(
            <Picker.Item label={'请选择'} value={'请选择'} key={'请选择'}/>
        );
        for(let i of itemList){
            pickItemList.push(
                <Picker.Item label={i.dictName || i.name} value={i.dictName || i.id} key={i.dictName || i.id}/>
            )
        }
        return pickItemList;
    }
    sideMenuContent() {
        return (
            <View style={{flex: 1, backgroundColor: '#fff',borderWidth: 0.5, borderColor: '#ddd'}}>
                <View style={styles.filterItemView}>
                    <Text style={styles.inputItem}>项目名称</Text>
                    <TextInput
                        placeholder={'请输入项目名称'}
                        multiline={true}
                        underlineColorAndroid='transparent'
                        style={styles.textInputStyle}
                        onChangeText={(text) => this.combination('projectname', text)}
                        value={this.state.filter.projectname}
                    />
                </View>
                <View style={styles.filterItemView}>
                    <Text style={styles.inputItem}>项目负责人</Text>
                    <TextInput
                        placeholder={'请输入负责人名字'}
                        multiline={true}
                        underlineColorAndroid='transparent'
                        style={styles.textInputStyle}
                        onChangeText={(text) => this.combination('managerName', text)}
                        value={this.state.filter.manager.managerName}/>
                    <Picker
                        mode={'dropdown'}
                        iositemStyle={{fontSize:14,color:'#f00'}}
                        selectedValue={this.state.filter.manager.managerUser}
                        style={styles.picker}
                        onValueChange={(itemValue) => this.combination('manager', itemValue)}>
                        <Picker.Item label={'全部'} value={'allPriUser'} />
                        <Picker.Item label={'第一项目负责人'} value={'mUserName'} />
                        <Picker.Item label={'第二项目负责人'} value={'aUserName'} />
                    </Picker>

                </View>
                <View style={styles.filterItemView}>
                    <Text style={styles.inputItem}>评审阶段</Text>
                    <View style={{borderWidth: 0.5, borderColor: '#333', marginTop: 10, marginBottom: 10}}>
                        <Picker
                            selectedValue={this.state.filter.reviewstage}
                            style={styles.picker}
                            onValueChange={(itemValue) => this.combination('reviewstage', itemValue)}>
                            {this.renderPickItem(this.state.PRO_STAGE)}
                        </Picker>
                    </View>
                </View>
                <View style={styles.filterItemView}>
                    <Text style={styles.inputItem}>评审部门</Text>
                    <View style={{borderWidth: 0.5, borderColor: '#333', marginTop: 10, marginBottom: 10}}>
                        <Picker
                            selectedValue={this.state.filter.mOrgId}
                            style={styles.picker}
                            onValueChange={(itemValue) => this.combination('mOrgId', itemValue)}>
                            {this.renderPickItem(this.state.mOrgId)}
                        </Picker>
                    </View>
                </View>
                <View style={styles.filterItemView}>
                    <Text style={styles.inputItem}>秘密等级</Text>
                    <View style={{borderWidth: 0.5, borderColor: '#333', marginTop: 10, marginBottom: 10,}}>
                        <Picker
                            selectedValue={this.state.filter.secrectlevel}
                            style={styles.picker}
                            onValueChange={(itemValue) => this.combination('secrectlevel', itemValue)}>
                            {this.renderPickItem(this.state.SECRECTLEVEL)}
                        </Picker>
                    </View>
                </View>
                <View style={{padding:10}}>
                    <TouchableOpacity style={{width: '100%',height:30,borderRadius:30, backgroundColor: '#1E5AAF', marginTop: 20}}
                                      onPress={() => this._search()}>
                        <Text style={{color: '#fff', alignSelf: 'center', lineHeight: 30,fontSize:15}}>查询</Text>
                    </TouchableOpacity>

                    <TouchableOpacity style={{width: '100%', marginTop: 15}}
                                      onPress={() => this._clearFilter()}>
                        <Text style={{color: '#000', alignSelf: 'center', lineHeight: 30,fontSize:15}}>清空</Text>
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
                style={styles.container}
            >
                <View style={styles.container}>
                    <Header title={'项目查询'} headerRight={this._filter()}/>
                    <ProjectListComponent
                        api={api}
                        navigation={this.props.navigation}
                    />
                </View>
            </SideMenu>
        );

    }

}

const styles = StyleSheet.create({
    container: {
        height: '100%',
        alignItems: 'center',
        justifyContent: 'flex-start',
        backgroundColor: '#eee'
    },
    filterView: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'center',
    },
    filterText: {
        color: '#fff',
        lineHeight: 50,
        marginLeft: 5
    },
    textInputStyle: {
        fontSize: 14,
        paddingLeft: 12,
        paddingTop: 0,
        paddingBottom: 0,
        marginTop: 10,
        marginBottom: 10,
        backgroundColor:'#efefef',
        borderRadius: 30,
    },
    inputItem: {
        color: '#1E5AAF',
        fontSize: 15
    },
    picker:{
        height:30,
    },
    filterItemView:{
        borderBottomWidth:0.5,
        borderBottomColor:'#dfdfdf',
        padding:10
    }
});