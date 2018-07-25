/**
 * 项目列表组件
 * @author: wzb
 */
import React from "react";
import {
    AsyncStorage,
    View,
    Text,
    Button,
    StyleSheet,
    FlatList,
    RefreshControl,
    ActivityIndicator,
    TouchableOpacity
} from "react-native";


type Props = {};
export default class ProjectScreen extends React.Component {
    constructor(props){
        super(props);
        this.state={
            isLoading:false,
            projectData:this.props.projectData
        }
    }
    /*为每个item生产唯一的key*/
    _extraUniqueKey(item , index){
        return "index"+index+item;
    }

    enIndicator(){
        return(
            <View>
                <ActivityIndicator
                    size={'large'}
                    animating={true}
                />
            </View>
        )
    }

    _loadData(refreshing){
        let newDataArray=[];
        if(refreshing){
            this.setState({
                isLoading:true
            });
        }
        setTimeout(()=>{
            if(refreshing){
                for (let i=this.state.projectData.length-1;i>=0;i--){
                    newDataArray.push(this.state.projectData[i]);
                }
            }else{
                newDataArray=this.state.projectData.concat(this.state.projectData);
            }
            this.setState({
                isLoading:false,
                projectData:newDataArray
            });
        },1000)
    }

    _renderItem(item){
        return(
            <View style={styles.item}>
                <Text style={styles.itemName}>{item.itemName}</Text>
                <View style={styles.desView}>
                    <Text style={{lineHeight:20}}>{item.itemDescription}</Text>
                </View>
                <View style={styles.itemFooter}>
                    <Text>总投资：{item.totalInvestment}</Text>
                    <Text>开工日期：{item.startDate}</Text>
                    <TouchableOpacity activeOpacity={0.8} style={styles.more} onPress={()=>alert('跳转详情页面')}>
                        <Text style={{color:'#fff',lineHeight:30}}>查看详情</Text>
                    </TouchableOpacity>
                </View>
            </View>
        )
    }
    render() {
        return (
            <View style={styles.container}>
                <FlatList
                    style={styles.itemVie}
                    keyExtractor = {this._extraUniqueKey}
                    data={this.state.projectData}
                    renderItem={({item}) => this._renderItem(item)}
                    refreshControl={
                        <RefreshControl
                            title={'Loading'} //IOS
                            colors={['orange','green']} //android
                            tintColor={['orange']} //IOS
                            refreshing={this.state.isLoading}
                            onRefresh={()=> {
                                this._loadData(true);
                            }}
                        />
                    }
                    ListFooterComponent={()=> this.enIndicator()}
                    onEndReachedThreshold={0.1}
                    onEndReached={() => {
                        this._loadData();
                    } }

                />
            </View>
        );
    }

}

const styles = StyleSheet.create({
    container: {
        width:'100%',
        height:'100%',
        alignItems: 'center',
        justifyContent: 'center'
    },
    itemVie:{
        width:'100%'
    },
    item:{
        width:'100%',
        height:180,
        backgroundColor:'#fff',
        marginBottom:10,
        padding:15
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
        borderRadius:30
    }
});