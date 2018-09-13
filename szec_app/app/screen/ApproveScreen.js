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
    Image
} from "react-native";
import axios from 'axios';
import Header from '../component/HeaderComponent';
import CheckBox from 'react-native-check-box';
import Icon from 'react-native-vector-icons/Ionicons';
import PopupDialog, {SlideAnimation, DialogTitle, DialogButton} from 'react-native-popup-dialog';

const slideAnimation = new SlideAnimation({
    slideFrom: 'bottom',
});

export default class ApproveScreen extends Component {
    constructor(props) {
        super(props);
        const {isassistflow, taskId, processInstanceId, userName} = this.props.navigation.state.params;
        this.state = {
            curNode: '',
            curNodeInfo: '',
            isassistflow: isassistflow,
            taskId: taskId,
            processInstanceId: processInstanceId,
            userName: userName,
            mainOrgs: [],
            subOrgs:[],
            mainOrgSelected:[],
            subOrgSelected:[],
            dealOption: ''
        }
    }

    commitData() {
        if(this.state.mainOrgSelected.length===0){
            alert('请选择主办部门');
            return;
        }
        if(this.state.subOrgSelected.length>3){
            alert('协办部门最多只能选3个');
            return;
        }
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
                this.setState({})
            })
            .catch(error => {
                console.log(error)
            })
    }

    componentDidMount() {
        axios({
            url: "/flowApp/flowNodeInfo",
            method:"post",
            params:{
                taskId: this.state.taskId,
                processInstanceId: this.state.processInstanceId,
                username: this.state.userName
            }
        })
            .then(res => {
                console.log(res);
                res.data.businessMap.orgs.forEach((item, index) => {
                    item.mainChecked = false;
                    item.subChecked= false;
                });
                this.setState({
                    curNodeInfo: res.data,
                    curNode: res.data.curNode.activitiName,
                    mainOrgs: res.data.businessMap.orgs,
                    subOrgs:res.data.businessMap.orgs,
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
        },()=>console.log(this.state.curNodeInfo.dealOption));
    };

    selectMainOrg = (i)=> {
        let orgs = this.state.mainOrgs;
        let arr = this.state.mainOrgSelected;
        orgs.forEach((item, index) => {
            if (i === index) {
                arr=[];
                item.mainChecked = !item.mainChecked;
                item.mainChecked? arr.push(item.id): arr.splice(arr.indexOf(item.id), 1);
            }else{
                item.mainChecked = false;
            }
        });
        this.setState({
            mainOrgSelected:arr,
            mainOrgs: orgs
        });
    };
    checkSubOrg = (i)=>{
        let orgs = this.state.subOrgs;
        let arr = this.state.subOrgSelected;
        orgs.forEach((item, index) => {
            if (i === index) {
                item.subChecked = !item.subChecked;
                item.subChecked?arr.push(item.id): arr.splice(arr.indexOf(item.id), 1);
            }
        });
        this.setState({
            subOrgs:orgs,
            subOrgSelected: arr
        });
    };
    renderMainOrgs(){
        let orgView = [], orgs = this.state.mainOrgs;
        for (let i = 0; i < orgs.length; i++) {
            orgView.push(
                <View style={{width: '50%', height:40, borderBottomWidth: 0.5, borderBottomColor: '#ddd',}} key={i}>
                    <CheckBox
                        style={styles.checkBox}
                        onClick={() => this.selectMainOrg(i)}
                        leftText={orgs[i].name}
                        isChecked={orgs[i].mainChecked}
                        checkedImage={<Icon name='ios-checkbox' size={25} style={styles.iconStyle}/>}
                        unCheckedImage={<Icon name='ios-checkbox-outline' size={25} style={styles.iconStyle}/>}
                    />
                </View>
            )
        }
        return orgView;
    }
    renderSubOrgs(){
        let subOrgView = [], orgs = this.state.subOrgs;
        for (let i = 0; i < orgs.length; i++) {
            subOrgView.push(
                <View style={{width: '50%', height:40, borderBottomWidth: 0.5, borderBottomColor: '#ddd',}} key={i+orgs[i].name}>
                    <CheckBox
                        style={styles.checkBox}
                        onClick={() => this.checkSubOrg(i)}
                        leftText={orgs[i].name}
                        isChecked={orgs[i].subChecked}
                        disabled={orgs[i].mainChecked}
                        checkedImage={<Icon name='ios-checkbox' size={25} style={styles.iconStyle}/>}
                        unCheckedImage={<Icon name='ios-checkbox-outline' size={25} style={styles.iconStyle}/>}
                    />
                </View>
            )
        }
        return subOrgView;
    }
    setDealOption(){
        let dealOption = '请（'+this.state.mainOrgSelected.concat(this.state.subOrgSelected)+'）组织评审!';
        let data = this.state.curNodeInfo;
        data.dealOption=dealOption;
        data.businessMap.MAIN_ORG=this.state.mainOrgSelected.join();
        data.businessMap.ASSIST_ORG=this.state.subOrgSelected.join();
        this.setState({
            curNodeInfo: data
        },()=>console.log(this.state.curNodeInfo));
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
                                      title={'选择下一环节办理部门'}/>}>
            <ScrollView>
                <View style={{alignItems:'center'}}>
                    <View style={styles.orgView}>
                        <Text style={{color:'#000',fontSize:16}}>主办部门</Text>
                    </View>
                    <View style={{flexDirection: 'row', flexWrap: 'wrap',}}>
                        {this.renderMainOrgs()}
                    </View>
                    <View style={styles.orgView}>
                        <Text style={{color:'#000',fontSize:16}}>协办部门</Text>
                    </View>
                    <View style={{flexDirection: 'row', flexWrap: 'wrap',}}>
                        {this.renderSubOrgs()}
                    </View>
                    <View style={{flexDirection:'row',marginTop:20}}>
                        <TouchableOpacity style={styles.conCancel} onPress={()=>this.popupDialog.dismiss()}>
                            <Text>取消</Text>
                        </TouchableOpacity>
                        <TouchableOpacity style={styles.textCon} onPress={()=>this.setDealOption()}>
                            <Text style={{color:'#fff'}}>确定</Text>
                        </TouchableOpacity>
                    </View>
                </View>
            </ScrollView>
        </PopupDialog>;
        return (
            <View style={styles.container}>
                {popupDialog}
                <Header title={this.state.curNode} showBackTitle={true} {...this.props}/>
                <View style={styles.body}>
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
                    <TouchableOpacity onPress={() => this.popupDialog.show()} style={{width:'100%',height:50,justifyContent:'center'}}>
                        <Text>请选择办理部门</Text>
                    </TouchableOpacity>
                    <TouchableOpacity onPress={()=>this.commitData()} style={{width:'100%',marginTop:30,justifyContent:'center',
                        alignItems:'center',alignSelf: 'center',height:40,borderRadius:5,backgroundColor:'#1E5AAF'}}>
                        <Text style={{color:'#fff'}}>提交</Text>
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
    },
    body: {
        padding: 10,
    },
    textInputStyle: {
        borderWidth: 1,
        padding: 10,
        borderColor: '#ccc',
        height: 100,
        textAlignVertical: 'top',
        borderRadius: 5
    },
    textCon: {
        flex:1,
        alignItems:'center',
        justifyContent:'center',
        margin: 10,
        paddingHorizontal: 0,
        backgroundColor: '#1E5AAF',
        height: 35,
        borderRadius: 5
    },
    conCancel: {
        flex:1,
        alignItems:'center',
        justifyContent:'center',
        backgroundColor: '#eee',
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
    checkBox:{
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        paddingLeft: 10,
        paddingRight: 10
    },
    orgView:{
        height:40,
        width:'100%',
        alignItems:'center',
        justifyContent:'center',
        borderBottomColor:'#1E5AAF',
        borderBottomWidth:1
    }
});