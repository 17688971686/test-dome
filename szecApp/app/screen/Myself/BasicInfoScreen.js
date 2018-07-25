import React from "react";
import {AsyncStorage, View, Text, StyleSheet,FlatList} from "react-native";
import Icon from 'react-native-vector-icons/Ionicons';

export default class BasicInfoScreen extends React.Component {
    static navigationOptions = ({navigation}) => ({
        headerTitle: (
            <Text style={{flex: 1,color:"#fff",fontSize:18,alignSelf:'center',textAlign:'center',fontWeight:"500"}}>我的基本信息</Text>
        ),
        headerLeft:(
            <View>
                <Icon onPress={()=>{navigation.goBack();}} name="ios-arrow-back" size={30} style={{width:40,height:40,marginLeft:15,marginTop:12,color:'#fff'}}/>
            </View>
        ),
        headerRight:<View/>
    });
    constructor(props) {
        super(props);
        this.state={
            itemData:[],
        }
    }

    componentWillMount() {
        this.setState({
            itemData:[{
                itemName:'登录名',
                value:'admin'
            },{
                itemName:'用户姓名',
                value:'超级管理员'
            },{
                itemName:'电话号码',
                value:'0775-0000000'
            },{
                itemName:'手机号码',
                value:'15588888888'
            },{
                itemName:'序号',
                value:'001'
            },{
                itemName:'所属机构',
                value:'超级管理员'
            }]
        })
    }

    /*为每个item生产唯一的key*/
    _extraUniqueKey(item , index){
        return "index"+index+item;
    }
    _renderItem(item){
        return(
            <View style={styles.itemView}>
                <Text style={styles.itemName}>{item.itemName}</Text>
                <Text style={styles.itemValue}>{item.value}</Text>
            </View>
        )
    }
    render() {
        return (
            <FlatList
                keyExtractor = {this._extraUniqueKey}
                data={this.state.itemData}
                renderItem={({item}) => this._renderItem(item)}
            />
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'flex-start'
    },
    itemView:{
        height:60,
        flexDirection:'row',
        justifyContent:'space-between',
        alignItems:'center',
        paddingLeft:15,
        paddingRight:15,
        borderBottomWidth:0.5,
        borderBottomColor:'#c3c3c3',
        backgroundColor:'#fff'
    },
    itemName:{
        fontSize:16,
        color:'#000'
    },
    itemValue:{
        fontSize:16,
        color:'#999'
    }


});