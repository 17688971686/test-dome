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
import ProjectListComponent from '../../component/ProjectListComponent'
import Header from '../../component/HeaderComponent'
import SideMenu from 'react-native-side-menu';


type Props = {};

const api ='/sign/findSignByPage';
export default class ProjectSearchScreen extends React.Component {
    constructor(props){
        super(props);
        this.state={
            isOpen:false
        }
    }
    _filter(){
        return(
            <TouchableOpacity style={styles.filterView} onPress={()=>this.setState({isOpen:true})}>
                <Icon name={'ios-search-outline'} size={18} color={'#fff'}/>
                <Text style={styles.filterText}>查找</Text>
            </TouchableOpacity>
        )
    };

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
                    <Header title={'项目查询'}/>
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
        flex:1,
        height:'100%',
        width:'100%',
        alignItems: 'center',
        justifyContent: 'flex-start',
        backgroundColor:'#eee'
    },
    filterView:{
        flexDirection:'row',
        justifyContent:'flex-end',
        alignItems:'center',
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