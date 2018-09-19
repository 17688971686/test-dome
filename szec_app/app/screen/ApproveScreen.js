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
    DeviceEventEmitter,
    Image,
    Picker
} from "react-native";
import axios from 'axios';
import Header from '../component/HeaderComponent';
import Icon from 'react-native-vector-icons/Ionicons';
import PopupDialog, {SlideAnimation, DialogTitle, DialogButton} from 'react-native-popup-dialog';

const slideAnimation = new SlideAnimation({
    slideFrom: 'bottom',
});

export default class ApproveScreen extends Component {
    constructor(props) {
        super(props);
        const {isassistflow, taskId, processInstanceId, userName,DIS_ID} = this.props.navigation.state.params;
        this.state = {
            curNode: '',
            curNodeInfo: '',
            isassistflow: isassistflow,
            taskId: taskId,
            processInstanceId: processInstanceId,
            userName: userName,
            mainOrgs: [],
            subOrgs: [],
            mainOrgSelected: [],
            subOrgSelected: [],
            dealOption: '',
            choice: '',
            curNodeId: '',
            DIS_ID:DIS_ID
        }
    }

    back() {
        DeviceEventEmitter.emit('Refresh', 'Refresh');
        this.props.navigation.navigate('leadpro');
    }

    componentDidMount() {
        //查询节点信息
        axios({
            url: "/flowApp/flowNodeInfo",
            method: "post",
            params: {
                taskId: this.state.taskId,
                processInstanceId: this.state.processInstanceId,
                username: this.state.userName
            }
        })
            .then(res => {
                console.log(res);
                let curNodeInfo = res.data,
                    curNodeId = curNodeInfo.curNode.activitiId,
                    orgs = [],
                    choice = '请选择项目负责人';
                if (curNodeId === 'SIGN_FGLD_FB') {
                    choice = '请选择办理部门';
                    orgs = curNodeInfo.businessMap.orgs;
                } else if (curNodeId === 'SIGN_BMFB1' || curNodeId === 'SIGN_BMFB2') {
                    orgs = curNodeInfo.businessMap.users;
                }
                orgs.forEach((item) => {
                    item.mainChecked = false;
                    item.subChecked = false;
                });
                this.setState({
                    curNodeInfo: curNodeInfo,
                    curNode: res.data.curNode.activitiName,
                    curNodeId: curNodeId,
                    mainOrgs: orgs,
                    subOrgs: orgs,
                    choice: choice
                })
            })
            .catch(error => {
                console.log(error)
            })
    }

    SetDealOption = (text) => {
        let data = Object.assign({}, this.state.curNodeInfo, {dealOption: text});
        this.setState({
            curNodeInfo: data
        });
    };
    selectMainOrg = (i) => {
        let orgs = this.state.mainOrgs;
        let arr = this.state.mainOrgSelected;
        orgs.forEach((item, index) => {
            if (i === index) {
                arr = [];
                item.userType = '所有';
                item.userName = item.displayName;
                item.isMainUser = 9;
                item.userId = item.id;
                item.mainChecked = !item.mainChecked;
                item.mainChecked ? arr.push(item) : arr.splice(arr.indexOf(item), 1);
            } else {
                item.mainChecked = false;
            }
        });
        this.setState({
            mainOrgSelected: arr,
            mainOrgs: orgs
        });
    };
    selectSubOrg = (i) => {
        let orgs = this.state.subOrgs;
        let arr = this.state.subOrgSelected;
        orgs.forEach((item, index) => {
            if (i === index) {
                item.userType = '所有';
                item.isMainUser = 0;
                item.userId = item.id;
                item.userName = item.displayName;
                item.subChecked = !item.subChecked;
                item.subChecked ? arr.push(item) : arr.splice(arr.indexOf(item), 1);
            }
        });
        this.setState({
            subOrgs: orgs,
            subOrgSelected: arr
        });
    };

    renderMainOrgs() {
        let orgView = [], orgs = this.state.mainOrgs;
        for (let i = 0; i < orgs.length; i++) {
            orgView.push(
                <TouchableOpacity activeOpacity={0.1} style={styles.checkBox} onPress={() => this.selectMainOrg(i)}
                                  key={i}>
                    <Text>{orgs[i].name || orgs[i].displayName}</Text>
                    {
                        orgs[i].mainChecked ?
                            <Icon name='ios-checkbox' size={25} style={styles.iconStyle}/>
                            :
                            <Icon name='ios-checkbox-outline' size={25} style={styles.iconStyle}/>
                    }
                </TouchableOpacity>
            )
        }
        return orgView;
    }

    renderSubOrgs() {
        let subOrgView = [], orgs = this.state.subOrgs;
        for (let i = 0; i < orgs.length; i++) {
            subOrgView.push(
                <TouchableOpacity activeOpacity={0.1} style={styles.checkBox} onPress={() => this.selectSubOrg(i)}
                                  key={i}>
                    <Text>{orgs[i].name || orgs[i].displayName}</Text>
                    {
                        orgs[i].subChecked ?
                            <Icon name='ios-checkbox' size={25} style={styles.iconStyle}/>
                            :
                            <Icon name='ios-checkbox-outline' size={25} style={styles.iconStyle}/>
                    }
                </TouchableOpacity>
            )
        }
        return subOrgView;
    }

    setDealOption() {
        let data = this.state.curNodeInfo,
            arrName = [],
            arrId = [];
        //如果是项目概算的部门部门分办流程
        if (this.state.isassistflow === '9' && (this.state.curNodeId === 'SIGN_BMFB1' || this.state.curNodeId === 'SIGN_BMFB2')) {
            if (this.state.curNodeId === 'SIGN_BMFB1') {
                arrName = [this.state.mainOrgSelected[0].displayName];
                arrId.push(this.state.mainOrgSelected[0]);
            }
            for (let item of this.state.subOrgSelected) {
                arrName.push(item.displayName);
                arrId.push(item)
            }
            data.businessMap.PRINCIPAL = arrId;
        } else {
            if (this.state.curNodeId === 'SIGN_BMFB1') {
                arrName = [this.state.mainOrgSelected[0].displayName];
                for (let item of this.state.subOrgSelected) {
                    arrName.push(item.displayName);
                    arrId.push(item.id)
                }
                data.businessMap.M_USER_ID = this.state.mainOrgSelected[0].id;
                data.businessMap.A_USER_ID = arrId.join();

            } else if (this.state.curNodeId === 'SIGN_FGLD_FB') {
                arrName = [this.state.mainOrgSelected[0].name];
                for (let item of this.state.subOrgSelected) {
                    arrName.push(item.name);
                    arrId.push(item.id)
                }
                data.businessMap.MAIN_ORG = this.state.mainOrgSelected[0].id;
                data.businessMap.ASSIST_ORG = arrId.join();
            } else {
                for (let item of this.state.subOrgSelected) {
                    arrName.push(item.displayName);
                    arrId.push(item.id)
                }
                data.businessMap.A_USER_ID = arrId.join();
            }
        }
        data.dealOption = '请(' + arrName.join() + ')组织评审!';
        this.setState({
            curNodeInfo: data
        }, () => this.popupDialog.dismiss());
    }

    commitNextStep() {
        let data = this.state.curNodeInfo;
        data.businessMap.DIS_ID = this.state.DIS_ID;
        this.setState({
            curNodeInfo:data
        },()=>{
            axios({
                url: "flowApp/commit",
                method: "post",
                params: {
                    flowObj: this.state.curNodeInfo,
                    username: this.state.userName
                },
            })
                .then((res) => {
                    console.log(res);
                    if (res.status === 200 && res.data.reCode === 'ok') {
                        Alert.alert(
                            res.data.reMsg,
                            '下一环节:' + this.state.curNodeInfo.nextNode[0].activitiName,
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
        });

    }

    isassistflow() {
        if (this.state.isassistflow === '9' && (this.state.curNodeId === 'SIGN_BMFB1' || this.state.curNodeId === 'SIGN_BMFB2')) {
            return (
                <View>
                    {
                        (this.state.mainOrgSelected.length > 0 || this.state.subOrgSelected.length > 0) &&
                        <View style={{flexDirection: 'row', marginLeft: 10, marginRight: 10, height: 35}}>
                            <Text style={styles.tableHeaderLeft}>项目负责人</Text>
                            <Text style={styles.tableHeaderRight}>负责分工</Text>
                        </View>
                    }
                    {this._renderMainItem()}
                    {this._renderSubItem()}
                </View>
            )
        }
    }

    selectMainUserType(val) {
        let arr = this.state.mainOrgSelected;
        arr[0].userType = val;
        this.setState({
            mainOrgSelected: arr
        }, () => console.log(this.state.mainOrgSelected))
    }

    selectSubUserType(val, i) {
        console.log(val);
        let arr = this.state.subOrgSelected;
        arr.forEach((item, index) => {
            if (i === index) {
                item.userType = val;
            }
        });
        this.setState({
            subOrgSelected: arr
        }, () => console.log(this.state.subOrgSelected))
    }

    _renderMainItem() {
        if (this.state.mainOrgSelected.length > 0) {
            return (
                <View style={styles.listView}>
                    <View style={[styles.listVietLeft, {borderRightWidth: 0}]}>
                        <Text>{this.state.mainOrgSelected[0].displayName}</Text>
                    </View>
                    <View style={styles.listVietLeft}>
                        <Picker
                            androidmode={'dropdown'}
                            style={{height: 50, width: '70%'}}
                            selectedValue={this.state.mainOrgSelected[0].userType}
                            onValueChange={(itemValue, itemIndex) => this.selectMainUserType(itemValue)}>
                            <Picker.Item label="所有" value="所有"/>
                            <Picker.Item label="土建" value="土建"/>
                            <Picker.Item label="安装" value="安装"/>
                        </Picker>
                    </View>
                </View>
            )
        }
    }

    _renderSubItem() {
        let subItemArr = [];
        if (this.state.subOrgSelected.length > 0) {
            for (let i = 0; i < this.state.subOrgSelected.length; i++) {
                subItemArr.push(
                    <View key={i} style={styles.listView}>
                        <View style={[styles.listVietLeft, {borderRightWidth: 0}]}>
                            <Text>{this.state.subOrgSelected[i].displayName}</Text>
                        </View>
                        <View style={styles.listVietLeft}>
                            <Picker
                                androidmode={'dropdown'}
                                style={{height: 50, width: '70%'}}
                                selectedValue={this.state.subOrgSelected[i].userType}
                                onValueChange={(itemValue, itemIndex) => this.selectSubUserType(itemValue, i)}>
                                <Picker.Item label="所有" value="所有"/>
                                <Picker.Item label="土建" value="土建"/>
                                <Picker.Item label="安装" value="安装"/>
                            </Picker>
                        </View>
                    </View>
                )
            }
        }
        return subItemArr
    }


    render() {
        let popupDialog = <PopupDialog
            width={0.95}
            height={500}
            ref={(popupDialog) => {
                this.popupDialog = popupDialog
            }}
            dialogAnimation={slideAnimation}
            dialogTitle={<DialogTitle titleTextStyle={styles.titleStyle} titleStyle={styles.dialogTitle}
                                      title={'请选择'}/>}>
            <ScrollView>
                <View style={{alignItems: 'center'}}>
                    {
                        this.state.curNodeId === 'SIGN_BMFB2' ||
                        <View style={{width: '100%'}}>
                            <View style={styles.orgView}>
                                <Text style={{color: '#000', fontSize: 16}}>
                                    {this.state.curNodeId === 'SIGN_FGLD_FB' ? '主办部门' : '第一负责人'}
                                </Text>
                            </View>
                            <View style={{flexDirection: 'row', flexWrap: 'wrap',}}>
                                {this.renderMainOrgs()}
                            </View>
                        </View>
                    }
                    <View style={styles.orgView}>
                        <Text style={{color: '#000', fontSize: 16}}>
                            {this.state.curNodeId === 'SIGN_FGLD_FB' ? '协办部门' : '其他负责人'}
                        </Text>
                    </View>
                    <View style={{flexDirection: 'row', flexWrap: 'wrap',}}>
                        {this.renderSubOrgs()}
                    </View>
                </View>
            </ScrollView>
            <View style={{flexDirection: 'row', backgroundColor: '#eee'}}>
                <TouchableOpacity style={styles.conCancel} onPress={() => this.popupDialog.dismiss()}>
                    <Text>取消</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.textCon} onPress={() => this.setDealOption()}>
                    <Text style={{color: '#fff'}}>确定</Text>
                </TouchableOpacity>
            </View>
        </PopupDialog>;
        return (
            <View style={styles.container}>
                {popupDialog}
                <View style={styles.body}>
                    <Header title={this.state.curNode} showBackTitle={true} {...this.props}/>
                    <View>
                        <TextInput
                            style={styles.textInputStyle}
                            clearButtonMode="while-editing"
                            placeholder="请输入审批意见"
                            multiline={true}
                            onChangeText={(text) => {
                                this.SetDealOption(text)
                            }}
                            underlineColorAndroid='transparent'
                            defaultValue={this.state.curNodeInfo.dealOption}
                        />
                    </View>
                    <TouchableOpacity style={styles.addOrgs} onPress={() => this.popupDialog.show()}>
                        <Icon name='md-add' size={20} style={[styles.iconStyle, {color: 'green', marginRight: 5}]}/>
                        <Text style={{color: 'green'}}>{this.state.choice}</Text>
                    </TouchableOpacity>
                    {this.isassistflow()}
                </View>
                <View>
                    <TouchableOpacity onPress={() => this.commitNextStep()} style={styles.commitBtn}>
                        <Text style={{color: '#fff', fontSize: 16}}>提交</Text>
                    </TouchableOpacity>
                </View>
            </View>
        )
    }
}
const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        justifyContent: 'space-between'
    },
    textInputStyle: {
        borderWidth: 1,
        padding: 10,
        borderColor: '#ccc',
        height: 100,
        textAlignVertical: 'top',
        borderRadius: 5,
        margin: 10
    },
    textCon: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
        margin: 10,
        paddingHorizontal: 0,
        backgroundColor: '#1E5AAF',
        height: 35,
        borderRadius: 5
    },
    conCancel: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: '#ddd',
        height: 35,
        borderRadius: 5,
        margin: 10,
        paddingHorizontal: 0,
    },
    dialogTitle: {
        backgroundColor: '#1E5AAF',
    },
    titleStyle: {
        color: '#fff',
        alignSelf: 'flex-start'
    },
    iconStyle: {
        color: '#1E5AAF'
    },
    checkBox: {
        width: '50%',
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        height: 40,
        borderBottomWidth: 0.5,
        borderBottomColor: '#ddd',
        padding: 10,
    },
    orgView: {
        height: 40,
        width: '100%',
        alignItems: 'center',
        justifyContent: 'center',
        borderBottomColor: '#1E5AAF',
        borderBottomWidth: 1
    },
    commitBtn: {
        width: '100%',
        justifyContent: 'center',
        alignItems: 'center',
        height: 50,
        backgroundColor: '#1E5AAF'
    },
    addOrgs: {
        width: '100%',
        height: 40,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center'
    },
    tableHeaderLeft: {
        flex: 1,
        backgroundColor: '#eee',
        borderColor: '#ddd',
        borderWidth: 1,
        borderRightWidth: 0,
        textAlign: 'center',
        textAlignVertical: 'center'
    },
    tableHeaderRight: {
        flex: 1,
        backgroundColor: '#eee',
        borderColor: '#ddd',
        borderWidth: 1,
        textAlign: 'center',
        textAlignVertical: 'center'
    },
    listView: {
        flexDirection: 'row',
        height: 40,
        marginLeft: 10,
        marginRight: 10,
        borderBottomColor: '#eee',
        borderBottomWidth: 1
    },
    listVietLeft: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderColor: '#eee'
    }
});