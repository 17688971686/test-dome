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
        const {isassistflow, taskId, signId, processInstanceId, userName, DIS_ID} = this.props.navigation.state.params;
        this.state = {
            curNode: '',
            curNodeInfo: '',
            signId: signId,
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
            DIS_ID: DIS_ID || '',
            AGREE: '9'
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
                let curNodeInfo = res.data,
                    curNodeId = curNodeInfo.curNode.activitiId,
                    orgs = [],
                    choice = '请选择项目负责人';
                if (curNodeId === 'SIGN_FGLD_FB') {
                    choice = '请选择办理部门';
                    orgs = curNodeInfo.businessMap.orgs;
                } else if (curNodeId === 'SIGN_BMFB1' || curNodeId === 'SIGN_BMFB2') {
                    orgs = curNodeInfo.businessMap.users;
                } else if (curNodeId === 'SIGN_BMLD_QRFW_XB' || curNodeId === 'SIGN_FGLD_QRFW_XB') {
                    curNodeInfo.dealOption = '核稿无误'
                }
                orgs.forEach((item) => {
                    item.mainChecked = false;
                    item.subChecked = false;
                    item.userType = '所有';
                    item.isMainUser = 0;
                    item.userId = item.id;
                    item.userName = item.displayName;
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
        let orgs = this.state.mainOrgs,
            subOrgs = this.state.subOrgs,
            mainOrgSelected = [],
            subOrgSelected = this.state.subOrgSelected;
        orgs.forEach((item, index) => {
            if (i === index) {
                item.isMainUser = 9;
                item.mainChecked = true;
                mainOrgSelected.push(item);
                subOrgs[i].subChecked = false;
                subOrgSelected.indexOf(item) >= 0 && subOrgSelected.splice(subOrgSelected.indexOf(item), 1);

            } else {
                item.mainChecked = false;
            }
        });
        this.setState({
            mainOrgSelected: mainOrgSelected,
            subOrgs: subOrgs,
            mainOrgs: orgs,
            subOrgSelected: subOrgSelected
        });
    };
    selectSubOrg = (i) => {
        if (this.state.curNodeId === 'SIGN_BMFB1' || this.state.curNodeId === 'SIGN_FGLD_FB') {
            let tips1 = this.state.curNodeId === 'SIGN_FGLD_FB' ? '请先选择主办部门' : '请先选择第一负责人';
            let tips2 = this.state.curNodeId === 'SIGN_FGLD_FB' ? '该部门已是主办部门' : '该负责人已是第一负责人';
            if (this.state.mainOrgSelected.length === 0) {
                Alert.alert(
                    tips1,
                    '',
                    [
                        {text: '确定',},
                    ],
                    {cancelable: false}
                );
                return
            }
            if (this.state.mainOrgs[i].mainChecked) {
                Alert.alert(
                    tips2,
                    '',
                    [
                        {text: '确定',},
                    ],
                    {cancelable: false}
                );
                return
            }
        }
        let orgs = this.state.subOrgs;
        let arr = this.state.subOrgSelected;
        orgs.forEach((item, index) => {
            if (i === index) {
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
                <TouchableOpacity activeOpacity={0.9} style={styles.checkBox} onPress={() => this.selectMainOrg(i)}
                                  key={i}>
                    <Text>{orgs[i].name || orgs[i].displayName}</Text>
                    {
                        orgs[i].mainChecked ?
                            <Icon name='ios-checkbox' size={25} style={styles.iconStyle}/>
                            :
                            <View style={styles.checkboxView}/>
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
                <TouchableOpacity activeOpacity={0.9} style={styles.checkBox} onPress={() => this.selectSubOrg(i)}
                                  key={i}>
                    <Text>{orgs[i].name || orgs[i].displayName}</Text>
                    {
                        orgs[i].subChecked ?
                            <Icon name='ios-checkbox' size={25} style={styles.iconStyle}/>
                            :
                            <View style={styles.checkboxView}/>
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

    //流程提交
    commitNextStep() {
        let data = this.state.curNodeInfo;
        data.businessMap.DIS_ID = this.state.DIS_ID;
        data.businessMap.AGREE = this.state.AGREE;
        this.setState({
            curNodeInfo: data
        }, () => {
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

    //流程回退
    rollbacklast() {
        axios({
            url: "flowApp/rollbacklast",
            method: "post",
            params: {
                flowObj: this.state.curNodeInfo,
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


    //如果是概算的部门分办流程显示项目负责人信息
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
        })
    }

    selectSubUserType(val, i) {
        let arr = this.state.subOrgSelected;
        arr.forEach((item, index) => {
            if (i === index) {
                item.userType = val;
            }
        });
        this.setState({
            subOrgSelected: arr
        })
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

    addOrgs() {
        if (this.state.curNodeId === 'SIGN_FGLD_FB' || this.state.curNodeId === 'SIGN_BMFB1' || this.state.curNodeId === 'SIGN_BMFB2') {
            return (
                <TouchableOpacity style={styles.addOrgs} onPress={() => this.popupDialog.show()}>
                    <Icon name='md-add' size={20} style={[styles.iconStyle, {color: 'green', marginRight: 5}]}/>
                    <Text style={{color: 'green'}}>{this.state.choice}</Text>
                </TouchableOpacity>
            )
        }
    }

    auditingRight() {
        let arr = this.state.curNodeInfo;
        arr.dealOption = '核稿无误';
        this.setState({
            AGREE: 9,
            curNodeInfo: arr,
        })
    }

    auditingError() {
        let arr = this.state.curNodeInfo;
        arr.dealOption = '核稿有误';
        this.setState({
            AGREE: 0,
            curNodeInfo: arr,
        })
    }

    auditing() {
        //如果是协办部门审批发文或者协办副主任审批发文流程 显示核稿按钮
        if (this.state.curNodeId === 'SIGN_BMLD_QRFW_XB' || this.state.curNodeId === 'SIGN_FGLD_QRFW_XB') {
            return (
                <View style={{flexDirection: 'row', justifyContent: 'flex-start', alignItems: 'center', margin: 10}}>
                    <TouchableOpacity
                        style={{flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'center'}}
                        onPress={() => this.auditingRight()}>
                        <Text style={{marginRight: 5}}>核稿无误</Text>
                        {
                            this.state.AGREE ?
                                <Icon name='ios-checkbox' size={25} style={styles.iconStyle}/>
                                :
                                <View style={styles.checkboxView}/>
                        }
                    </TouchableOpacity>
                    <TouchableOpacity
                        style={{flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'center'}}
                        onPress={() => this.auditingError()}>
                        <Text style={{marginRight: 5}}>核稿有误</Text>
                        {
                            !this.state.AGREE ?
                                <Icon name='ios-checkbox' size={25} style={styles.iconStyle}/>
                                :
                                <View style={styles.checkboxView}/>
                        }
                    </TouchableOpacity>
                </View>
            )
        }
    }

    submitBtn() {
        const submitBtn = [];
        switch (this.state.curNodeId) {
            case "SIGN_FGLD_FB":
            //部长审核工作方案
            case "SIGN_BMLD_SPW1":
            case "SIGN_BMLD_SPW2":
            case "SIGN_BMLD_SPW3":
            case "SIGN_BMLD_SPW4":
            //分管领导审批工作方案
            case "SIGN_FGLD_SPW1":
            case "SIGN_FGLD_SPW2":
            case "SIGN_FGLD_SPW3":
            case "SIGN_FGLD_SPW4":
            //部长审批发文
            case "SIGN_BMLD_QRFW":
            //分管领导审批发文
            case "SIGN_FGLD_QRFW":
            //主任审批发文
            case "SIGN_ZR_QRFW":
                submitBtn.push(
                    <TouchableOpacity key={1}
                                      onPress={() =>  Alert.alert(
                                          '确认操作',
                                          '回退流程？',
                                          [
                                              {text:'取消'},
                                              {text: '确定', onPress: () => this.rollbacklast()},
                                          ],
                                          {cancelable: false}
                                      )} style={[styles.submitBtn, {
                        backgroundColor: '#eee',
                        borderRightWidth: 0.5, borderRightColor: '#aaa'
                    }]}>
                        <Text style={styles.submitText}>回退</Text>
                    </TouchableOpacity>
                )
                break;
        }

/*        if (this.state.curNodeId === 'SIGN_BMFB1' || this.state.curNodeId === 'SIGN_BMFB2') {
                submitBtn.push(
                    <TouchableOpacity key={2}
                                      onPress={() =>  Alert.alert(
                                          '确认操作',
                                          '重新分办？',
                                          [
                                              {text:'取消'},
                                              {text: '确定', onPress: () => this.getBack()},
                                          ],
                                          {cancelable: false})}
                                      style={[styles.submitBtn, {backgroundColor: '#eee'}]}>
                        <Text style={styles.submitText}>取回</Text>
                    </TouchableOpacity>
                );
        }*/
        submitBtn.push(
            <TouchableOpacity key={3} onPress={() => this.commitNextStep()} style={[styles.submitBtn, {flex: 2}]}>
                <Text style={[styles.submitText, {color: '#fff'}]}>提交</Text>
            </TouchableOpacity>
        );
        return submitBtn
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
                    {this.addOrgs()}
                    {this.auditing()}
                    {this.isassistflow()}
                </View>
                <View style={styles.submitBtnView}>
                    {this.submitBtn()}
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
    },
    submitBtnView: {
        flexDirection: 'row',
    },
    submitBtn: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        height: 50,
        backgroundColor: '#1E5AAF',
        borderTopWidth: 0.5,
        borderTopColor: '#aaa'
    },
    submitText: {
        color: '#000',
        fontSize: 16,
    },
    checkboxView: {
        borderWidth: 0.5,
        borderColor: '#333',
        backgroundColor: '#fff',
        width: 18,
        height: 18
    }
});