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
            filter: {},
        }
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
            default:
                break;
        }
        this.setState({
            filter: filter
        })
    }

    _search() {
        let filter = this.state.filter,
            filterStr = '';
        for (let i in filter) {
            if (filter[i] !== '请选择') {
                filterStr += " and " + i + " eq " + "'" + filter[i] + "'"
            }
        }
        DeviceEventEmitter.emit('search', filterStr);
        this.setState({
            isOpen: false
        });
        Keyboard.dismiss();
    }

    sideMenuContent() {
        return (
            <View style={{flex: 1, backgroundColor: '#fff', padding: 15, borderWidth: 0.5, borderColor: '#ddd'}}>
                <View>
                    <Text style={styles.inputItem}>项目名称</Text>
                    <TextInput
                        multiline={true}
                        underlineColorAndroid='transparent'
                        style={styles.textInputStyle}
                        onChangeText={(text) => this.combination('projectname', text)}
                    />
                </View>
                <View>
                    <Text style={styles.inputItem}>评审阶段</Text>
                    <View style={{borderWidth: 0.5, borderColor: '#333', marginTop: 10, marginBottom: 10}}>
                        <Picker
                            selectedValue={this.state.filter.reviewstage}
                            style={{height: 30, width: '100%'}}
                            itemStyle={{fontSize: 12, color: 'red'}}
                            onValueChange={(itemValue, itemIndex) => this.combination('reviewstage', itemValue)}>
                            <Picker.Item label="请选择" value="请选择"/>
                            <Picker.Item label="部长审批" value="部长审批"/>
                            <Picker.Item label="发文申请	" value="发文申请"/>
                            <Picker.Item label="项目负责人办理" value="项目负责人办理"/>
                            <Picker.Item label="主办部长审批" value="主办部长审批"/>
                        </Picker>
                    </View>
                </View>
                <View>
                    <Text style={styles.inputItem}>评审部门</Text>
                    <View style={{borderWidth: 0.5, borderColor: '#333', marginTop: 10, marginBottom: 10,}}>
                        <Picker
                            selectedValue={this.state.stage} style={{height: 30, width: '100%',}}
                            onValueChange={(itemValue, itemIndex) => this.setState({stage: itemValue})}>
                            <Picker.Item label="请选择" value="请选择"/>
                            <Picker.Item label="综合部" value="综合部"/>
                            <Picker.Item label="评估一部" value="评估一部"/>
                            <Picker.Item label="评估二部" value="评估二部"/>
                            <Picker.Item label="概算一部" value="概算一部"/>
                            <Picker.Item label="概算二部" value="概算二部"/>
                            <Picker.Item label="评估一部信息化组" value="评估一部信息化组"/>
                        </Picker>
                    </View>
                </View>
                <View>
                    <Text style={styles.inputItem}>秘密等级</Text>
                    <View style={{borderWidth: 0.5, borderColor: '#333', marginTop: 10, marginBottom: 10,}}>
                        <Picker
                            selectedValue={this.state.filter.secrectlevel} style={{height: 30, width: '100%',}}
                            onValueChange={(itemValue, itemIndex) => this.combination('secrectlevel', itemValue)}>
                            <Picker.Item label="请选择" value="请选择"/>
                            <Picker.Item label="公开" value="公开"/>
                            <Picker.Item label="秘密" value="秘密"/>
                        </Picker>
                    </View>
                </View>
                <View>
                    <TouchableOpacity style={{width: '100%', backgroundColor: '#1E5AAF', marginTop: 15}}
                                      onPress={() => this._search()}>
                        <Text style={{color: '#fff', alignSelf: 'center', lineHeight: 30}}>查询</Text>
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
                        <ProjectListComponent
                            api={api}
                            navigation={this.props.navigation}
                        />
                    </View>
                </View>
            </SideMenu>
        );

    }

}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        height: '100%',
        width: '100%',
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
        borderWidth: 0.5,
        borderColor: '#333',
        lineHeight: 30,
        fontSize: 15,
        paddingLeft: 10,
        paddingTop: 0,
        paddingBottom: 0,
        marginTop: 10,
        marginBottom: 10
    },
    inputItem: {
        color: '#000',
        fontSize: 16
    },
    itemsView: {
        width: '100%'
    },
    item: {
        width: '100%',
        backgroundColor: '#eee',
        marginBottom: 15,
        padding: 15,
        elevation: 4,
    },
    itemName: {
        fontSize: 18,
        color: '#000',
        fontWeight: 'bold',
        marginBottom: 5,
    },
    desView: {
        height: 80,
    },
    itemFooter: {
        height: 50,
        alignItems: 'center',
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    more: {
        paddingLeft: 10,
        paddingRight: 10,
        backgroundColor: '#1E5AAF',
        height: 30,
        borderRadius: 30,
        marginTop: 15
    },
    itemView: {
        flexDirection: 'row',
        justifyContent: 'flex-start',
        height: 30,
        alignItems: 'center'
    },
    itemText: {
        fontSize: 14,
        color: '#000',
    }
});