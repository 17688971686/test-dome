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
/*历史记录*/
import SignHistory from './ProjectSearch/SignHistoryComponent'

/*委处理表*/
class VdealDetail extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        const {data} = this.props;
        return (
            <View style={styles.container}>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>项目名称：</Text>
                    <Text style={styles.itemText}>{data.projectname}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>项目代码：</Text>
                    <Text style={styles.itemText}>{data.projectcode}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>建设单位：</Text>
                    <Text style={styles.itemText}>{data.builtcompanyName}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>负责人：</Text>
                    <Text style={styles.itemText}>{data.builtCompUserName}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>编制单位：</Text>
                    <Text style={styles.itemText}>{data.designcompanyName}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>窗口受理时间：</Text>
                    <Text style={styles.itemText}>{data.acceptDate}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>主办处室意见：</Text>
                    <Text style={styles.itemText}>{data.maindeptOpinion}</Text>
                </View>
            </View>
        )
    }
}

/*审批登记*/
class SignDetail extends Component {
    render() {
        const {data} = this.props;
        return (
            <View style={styles.container}>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>项目名称：</Text>
                    <Text style={styles.itemText}>{data.projectname}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>收文编号：</Text>
                    <Text style={styles.itemText}>{data.signNum}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>建设单位：</Text>
                    <Text style={styles.itemText}>{data.builtcompanyName}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>项目代码：</Text>
                    <Text style={styles.itemText}>{data.projectcode}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>编制单位：</Text>
                    <Text style={styles.itemText}>{data.designcompanyName}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>缓急程度：</Text>
                    <Text style={styles.itemText}>{data.urgencydegree}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>秘密等级：</Text>
                    <Text style={styles.itemText}>{data.secrectlevel}</Text>
                </View>
            </View>
        )
    }
}

/*工作方案*/
class SignWorkprogram extends Component {
    _renderItem(item) {
        return (
            <View style={styles.container}>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>评审方式：</Text>
                    <Text style={styles.itemText}>{item.reviewType} | {item.isSigle}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>主管部门：</Text>
                    <Text style={styles.itemText}>{item.mainDeptName}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>是否有环评：</Text>
                    <Text style={styles.itemText}>{item.isHaveEIA ? '是' : '否'}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>项目类别：</Text>
                    <Text style={styles.itemText}>{item.projectType} | {item.projectSubType}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>行业类别：</Text>
                    <Text style={styles.itemText}>{item.industryType}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>联系人：</Text>
                    <Text style={styles.itemText}>{item.contactPerson}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>手机：</Text>
                    <Text style={styles.itemText}>{item.contactPersonPhone}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>电话：</Text>
                    <Text style={styles.itemText}>{item.contactPersonTel}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>传真：</Text>
                    <Text style={styles.itemText}>{item.contactPersonFax}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>申报投资：</Text>
                    <Text style={styles.itemText}>{item.appalyInvestment}万元</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>申报建设规模：</Text>
                    <Text style={styles.itemText}>{item.buildSize}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>申报建设内容：</Text>
                    <Text style={styles.itemText}>{item.buildContent}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>项目背景：</Text>
                    <Text style={styles.itemText}>{item.projectBackGround}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>评估部门：</Text>
                    <Text style={styles.itemText}>{item.reviewOrgName}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>第一负责人：</Text>
                    <Text style={styles.itemText}>{item.mianChargeUserName}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>第二负责人：</Text>
                    <Text style={styles.itemText}>{item.secondChargeUserName}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>是否有补充资料函：</Text>
                    <Text style={styles.itemText}>{item.isHaveSuppLetter ? '有' : '无'}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>补充资料函发文日期：</Text>
                    <Text style={styles.itemText}>{item.suppLetterDate}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>调研时间：</Text>
                    <Text style={styles.itemText}>{item.studyAllDay}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>专家费用：</Text>
                    <Text style={styles.itemText}>{item.expertCost}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>评审会日期：</Text>
                    <Text style={styles.itemText}>{item.rbDate}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>会议室名称：</Text>
                    <Text style={styles.itemText}>{item.addressName}</Text>
                </View>
            </View>
        )
    }

    _extraUniqueKey(item, index) {
        return "index" + index + item;
    }

    render() {
        const {data} = this.props;
        return (
            <FlatList
                style={{width: '100%'}}
                keyExtractor={this._extraUniqueKey}
                data={data}
                renderItem={({item}) => this._renderItem(item)}
            />
        )
    }
}

/*发文信息*/
class SignDispatch extends Component {
    render() {
        const {data} = this.props;
        return (
            <View style={styles.container}>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>发文方式：</Text>
                    <Text style={styles.itemText}>{data.dispatchWay && data.dispatchWay === 1 ? "单个发文" : "合并发文"}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>发文类型：</Text>
                    <Text style={styles.itemText}>{data.dispatchType}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>文件标题：</Text>
                    <Text style={styles.itemText}>{data.fileTitle}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>发文日期：</Text>
                    <Text style={styles.itemText}>{data.dispatchDate}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>文号：</Text>
                    <Text style={styles.itemText}>{data.fileNum}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>秘密等级：</Text>
                    <Text style={styles.itemText}>{data.secretLevel}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>缓急程度：</Text>
                    <Text style={styles.itemText}>{data.urgentLevel}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>是否与其他阶段关联：</Text>
                    <Text style={styles.itemText}>{data.isRelated ? '是' : '否'}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>发行范围：</Text>
                    <Text style={styles.itemText}>{data.dispatchScope}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>相关说明：</Text>
                    <Text style={styles.itemText}>{data.description}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>评审意见摘要：</Text>
                    <Text style={styles.itemText}>{data.reviewAbstract}</Text>
                </View>
            </View>
        )
    }
}

const slideAnimation = new SlideAnimation({
    slideFrom: 'bottom',
});

export default class ProDetailsScreen extends Component {
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
            curNodeInfo:''
        };

    }

    componentWillMount() {
        axios.get('/sign/findSignById', {
            params: {
                signId: this.state.signId,
                queryAll: true
            }
        })
            .then(res => {
                this.setState({
                    projectData: res.data
                })
            })
            .catch(error => {
                console.log(error);
            });
        /*查询当前节点信息*/
        axios({
            url: "/flowApp/flowNodeInfo",
            method: "post",
            params: {
                taskId: this.state.taskId,
                processInstanceId: this.state.processInstanceId,
                username: this.state.userName
            },
        })
            .then((res) => {
                console.log(res);
                this.setState({
                    curNodeInfo:res.data,
                    curNode: res.data.curNode.activitiName
                })
            })
            .catch(error => {
                console.log(error)
            })
    }

    _approve() {
        return (
            <TouchableOpacity style={styles.filterView} onPress={() => this.popupDialog.show()}>
                <Icon name={'ios-create-outline'} size={22} color={'#fff'}/>
                <Text style={styles.filterText}>审批</Text>
            </TouchableOpacity>
        )
    }
    commitData(){
        Alert.alert(
            '审批成功',
            '下一环节:部门领导审批',
            [
                {text: 'OK', onPress: () => {this.props.navigation.navigate('leadpro');DeviceEventEmitter.emit('Refresh', true);}}
            ],
            { cancelable: false }
        )
       /* axios({
            url: "flowApp/commit",
            method: "post",
            params: {
                flowObj:'',
                username: this.state.userName
            },
        })
            .then((res) => {
                console.log(res);
                this.setState({
                    curNode: res.data.curNode.activitiName
                })
            })
            .catch(error => {
                console.log(error)
            })*/
    }
    render() {
        const {approve} = this.props.navigation.state.params;
        return (
            <View style={styles.container}>
                <PopupDialog
                    width={0.95}
                    ref={(popupDialog) => {
                        this.popupDialog = popupDialog;
                    }}
                    dialogAnimation={slideAnimation}
                    dialogTitle={<DialogTitle titleTextStyle={styles.titleStyle} titleStyle={styles.dialogTitle}
                                              title={'当前环节：' + this.state.curNode}/>}
                >
                    <View style={{justifyContent: 'space-between', flex: 1}}>
                        <TextInput
                            style={styles.textInputStyle}
                            clearButtonMode="while-editing"
                            placeholder="请输入审批意见"
                            multiline={true}
                            onChangeText={(approvalOpinion) => this.setState({approvalOpinion})}
                            underlineColorAndroid='transparent'
                        />
                        <View style={{flexDirection: 'row', marginLeft: 10, marginRight: 10, marginBottom: 10}}>
                            <DialogButton textContainerStyle={styles.conCancel} activeOpacity={0.9}
                                          textStyle={styles.btnCancel} buttonStyle={styles.buttonStyle} text={'取消'}
                                          onPress={() => this.popupDialog.dismiss()}/>
                            <DialogButton textContainerStyle={styles.textCon} activeOpacity={0.9}
                                          textStyle={styles.btnText} buttonStyle={styles.buttonStyle} text={'审批'}
                                          onPress={() => this.commitData()}/>
                        </View>
                    </View>
                </PopupDialog>
                <Header title={this.state.projectName} showBackTitle={true}
                        navigation={this.props.navigation} fontSize={16} headerRight={approve && this._approve()}/>
                <ScrollableTabView
                    tabBarActiveTextColor='#1E5AAF'//设置选中Tab的文字颜色。
                    tabBarUnderlineStyle={{backgroundColor: '#1E5AAF', height: 2}}
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
        width: '100%'
    },
    dialogTitle: {
        backgroundColor: '#1E5AAF',
    },
    titleStyle: {
        color: '#fff',
        alignSelf: 'flex-start'
    },
    itemView: {
        width: '100%',
        flexDirection: 'row',
        padding: 10,
        justifyContent: 'flex-start',
        borderBottomWidth: 0.5,
        borderBottomColor: '#ddd'
    },
    itemName: {
        fontSize: 14,
        color: '#000',
        marginRight: 5,
        lineHeight: 25,
    },
    itemText: {
        color: '#999',
        lineHeight: 25,
        paddingRight: 70
    },
    desc: {
        paddingLeft: 20,
        paddingRight: 15,
    },
    lastItemView: {
        borderBottomWidth: 0
    },
    filterView: {
        flexDirection: 'row',
        justifyContent:'center',
        alignItems: 'center'
    },
    filterText: {
        color: '#fff',
        lineHeight: 50,
        marginLeft: 5
    },
    btnCancel: {
        fontSize: 16
    },
    textInputStyle: {
        borderWidth: 1,
        margin: 10,
        borderColor: '#ddd',
        height: 100,
        textAlignVertical: 'top'
    },
    buttonStyle: {
        flex: 1,
    },
    btnText: {
        fontSize: 16,
        color: '#fff',
    },
    textCon: {
        margin: 5,
        paddingHorizontal: 0,
        backgroundColor: '#1E5AAF',
        height: 35,
        borderRadius: 5
    },
    conCancel: {
        backgroundColor: '#eee',
        height: 35,
        borderRadius: 5,
        margin: 5,
        paddingHorizontal: 0,
    }
});