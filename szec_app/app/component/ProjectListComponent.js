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
            projectData:this.props.projectData,
        }
    }
    /*为每个item生产唯一的key*/
    _extraUniqueKey(item , index){
        return "index"+index+item;
    }
    componentWillReceiveProps(nextProps){
        console.log(nextProps);
        this.setState({
            projectData:nextProps.projectData,
        })
    }
    enIndicator(){
        return(
            this.state.projectData.length < this.props.totalNum ?
            <View>
                <ActivityIndicator
                    size={'large'}
                    animating={true}
                />
            </View>
            :
            <View>
                <Text>到底啦</Text>
            </View>
        )

    }

    _loadMore(){
        if(this.state.projectData.length < this.props.totalNum){
            setTimeout(()=>{
                this.setState({
                    projectData:this.state.projectData.concat(this.props.loadMore())
                })
            },1000)
        }
    }
    _renderItem(item){
        return(
            <View style={styles.item}>
                <Text style={styles.itemName}>{item.itemName}</Text>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>项目阶段：</Text>
                    <Text>项目概算</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>当前环节：</Text>
                    <Text>项目概算</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>签收时间：</Text>
                    <Text>2018-05-05</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>剩余工作日：</Text>
                    <Text>8</Text>
                </View>
                <TouchableOpacity activeOpacity={0.8} style={styles.more}>
                    <Text style={{color:'#fff',lineHeight:30,alignSelf:'center'}}>项目审批</Text>
                </TouchableOpacity>
            </View>
        )
    }
    render() {
        return (
            <View style={styles.container}>
                <FlatList
                    style={styles.itemsView}
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
                                this.setState({
                                    projectData:this.props.onRefresh()
                                })
                            }}
                        />
                    }
                    ListFooterComponent={()=> this.enIndicator()}
                    onEndReachedThreshold={0.1}
                    onEndReached={() => {
                        this._loadMore()
                    } }
                />
            </View>
        );
    }

}

const styles = StyleSheet.create({
    container: {
        width:'100%',
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor:'#fff',
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