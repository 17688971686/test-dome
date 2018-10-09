import React, {Component} from "react";
import {
    View,
    Text,
    StyleSheet,
    ScrollView,
    FlatList,
    TouchableOpacity,
    Button,
    TextInput,
    Alert,
    DeviceEventEmitter
} from "react-native";
import Header from '../component/HeaderComponent'
import Icon from 'react-native-vector-icons/Ionicons';
import ScrollableTabView, {ScrollableTabBar, DefaultTabBar} from 'react-native-scrollable-tab-view';
import PopupDialog, {SlideAnimation, DialogTitle, DialogButton} from 'react-native-popup-dialog';
import axios from 'axios'
/*列表明细*/
import VdealDetail from './ProductDetail/VdealDetailComponent'
import SignDetail from  './ProductDetail/SignDetailComponent'
import SignWorkprogram from  './ProductDetail/SignWorkprogramComponent'
import SignDispatch from  './ProductDetail/SignDispatchComponent'
/*历史记录*/
import SignHistory from './ProjectSearch/SignHistoryComponent'


export default class ProRepeatDetailScreen extends Component {
    constructor(props) {
        super(props);
        const {projectName, taskId, signId, processInstanceId, userName} = this.props.navigation.state.params;
        this.state = {
            projectData: '',
            approvalOpinion: '',
            projectName: projectName,
            taskId: taskId,
            processInstanceId: processInstanceId,
            userName: userName,
            signId: signId,
            curNode: '',
            curNodeInfo: ''
        };

    }

    componentDidMount() {
        axios.get('/sign/findSignById', {
            params: {
                signId: this.state.signId,
                queryAll: true
            }
        })
            .then(res => {
                this.setState({
                    projectData: res.data,
                })
            })
            .catch(error => {
                console.log(error);
            });
    }


    _approve() {
        return (
            <TouchableOpacity style={styles.filterView} key={2}
                              onPress={() =>  Alert.alert(
                                  '确认操作',
                                  '确认取回?',
                                  [
                                      {text:'取消'},
                                      {text: '确定', onPress: () => this.getBack()},
                                  ],
                                  {cancelable: false})}
                       /*       style={[styles.submitBtn, {backgroundColor: '#eee'}]}*/>
                <Icon name={'ios-create-outline'} size={20} color={'#fff'}/>
                <Text style={styles.filterText}>取回</Text>
            </TouchableOpacity>



        )
    }


    //流程取回
    getBack() {
        axios({
            url: "flowApp/getBack",
            method: "post",
            params: {
                taskId: this.state.taskId,
                username: this.state.userName,
                businessKey: this.state.signId
            },
        })
            .then((res) => {
                if (res.status === 200 && res.data.reCode === 'ok') {
                    Alert.alert(
                        res.data.reMsg,
                        '',
                        [
                            {text: '确定', onPress: () => this.back()},
                        ],
                        {cancelable: false}
                    )
                } else {
                    Alert.alert(
                        '操作异常',
                        res.data.reMsg,
                        [
                            {text: '确定', onPress: () => this.popupDialog.dismiss()},
                        ],
                        {cancelable: false}
                    )
                }
            })
            .catch(error => {
                console.log(error)
            })
    }

    back() {
        DeviceEventEmitter.emit('Refresh', 'Refresh');
        this.props.navigation.navigate('ProRepeatScreen');
    }

    render() {
        const {approve} = this.props.navigation.state.params;
        return (
            <View style={styles.container}>
                <Header title={this.state.projectName} showBackTitle={true}
                        navigation={this.props.navigation} headerRight={approve && this._approve()}/>
                <ScrollableTabView
                    tabBarInactiveTextColor={'#ddd'}
                    tabBarActiveTextColor='#fff'//设置选中Tab的文字颜色。
                    tabBarUnderlineStyle={{backgroundColor: '#fff', height: 2}}
                    tabBarBackgroundColor='#1E5AAF'
                    renderTabBar={() => <ScrollableTabBar/>}>
                    <VdealDetail tabLabel="委处理表" data={this.state.projectData}/>
                    <SignDetail tabLabel="审批登记" data={this.state.projectData}/>
                    <SignWorkprogram tabLabel="工作方案" data={this.state.projectData.workProgramDtoList}/>
                    <SignDispatch tabLabel="发文信息" data={this.state.projectData.dispatchDocDto}/>
                    <SignHistory tabLabel="处理记录" processInstanceId={this.state.processInstanceId}/>
                </ScrollableTabView>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        width: '100%',
    },

    filterView: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'center',
    },
    filterText: {
        color: '#fff',
    },
    submitBtn: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        height: 20,


    },
    submitText: {
        color: '#000',
        fontSize: 16,
    },

});