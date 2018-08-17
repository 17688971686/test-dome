import React from "react";
import {AsyncStorage, View, Text, StyleSheet,FlatList,TouchableOpacity} from "react-native";
import Icon from 'react-native-vector-icons/Ionicons';

export default class SetUpScreen extends React.Component {
    static navigationOptions = ({navigation}) => ({
        headerTitle: (
            <Text style={{flex: 1,color:"#fff",fontSize:18,alignSelf:'center',textAlign:'center',fontWeight:"500"}}>设置</Text>
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
                itemName:'版本信息',
                value:'V0.0.1'
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
    toSignOutAsync = async () => {
        await AsyncStorage.clear();
        this.props.navigation.navigate('Auth');
    };
    render() {
        return (
            <View>
                <FlatList
                    style={{marginBottom:50}}
                    keyExtractor = {this._extraUniqueKey}
                    data={this.state.itemData}
                    renderItem={({item}) => this._renderItem(item)}
                />
                <TouchableOpacity style={styles.logoutView} onPress={this.toSignOutAsync}>
                    <Text style={styles.logoutText}>退出登录</Text>
                </TouchableOpacity>
            </View>
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
    },
    logoutView:{
        width:'90%',
        alignSelf:'center',
        height:40,
        borderRadius:40,
        backgroundColor:'#1E5AAF'
    },
    logoutText:{
        fontSize:18,
        lineHeight:40,
        color:'#fff',
        alignSelf:'center'
    }
});