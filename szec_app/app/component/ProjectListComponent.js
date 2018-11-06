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
    TouchableOpacity,
    DeviceEventEmitter
} from "react-native";
import axios from 'axios'

export default class ProjectScreen extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoading: false,
            totalNum: 0,
            pageIndex: 0,
            api: this.props.api,
            pageSize: this.props.pageSize || 10,
            projectData: [],
            filterStr:''
        }
    }

    /*为每个item生产唯一的key*/
    _extraUniqueKey(item, index) {
        return "index" + index + item;
    }

    _loadData(pageIndex) {
        this.setState({
            isLoading:true
        });
        if (this.state.projectData.length <= this.state.totalNum) {
            axios.get(this.state.api, {
                params: {
                    "$top": this.state.pageSize,
                    "$skip": this.state.pageSize * pageIndex,
                    "$filter" : "signState ne 7 and issign eq 9" + this.state.filterStr
                }
            })
                .then(res => {
                    console.log(res);
                    this.setState({
                        isLoading:false,
                        totalNum: res.data.count,
                        projectData: pageIndex ? this.state.projectData.concat(res.data.value) : res.data.value,
                    })
                })
                .catch(error => {
                    console.log(error);
                });
        }
    }

    componentWillMount() {
        this._loadData(this.state.pageIndex);
        this.deEmitter = DeviceEventEmitter.addListener('search', (filterStr) => {
            this.setState({
                filterStr:filterStr
            },()=>{this._loadData(this.state.pageIndex)})
        });
    }

    enIndicator() {
        return (
            this.state.projectData.length < this.state.totalNum ?
                <View>
                    <ActivityIndicator
                        size={'large'}
                        animating={true}
                    />
                </View>
                :
                <View style={styles.noMore}>
                    <Text>到底啦,没有更多数据了</Text>
                </View>
        )

    }

    _renderItem(item) {
        return (
            <TouchableOpacity style={styles.item} activeOpacity={1}
                              onPress={() => this.props.navigation.navigate('ProDetailsScreen', {
                                  signId: item.signid,
                                  projectName:item.projectname,
                                  processInstanceId:item.processInstanceId
                              })}>
                <Text style={styles.itemName} activeOpacity={1}>{item.projectname}</Text>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>评审阶段：</Text>
                    <Text>{item.reviewstage}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>申报投资：</Text>
                    <Text>100万</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>签收时间：</Text>
                    <Text>{item.signdate}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemText}>剩余工作日：</Text>
                    <Text>{item.surplusdays}</Text>
                </View>
            </TouchableOpacity>
        )
    }

    render() {
        return (
            <FlatList
                style={{width: '100%'}}
                keyExtractor={this._extraUniqueKey}
                data={this.state.projectData}
                renderItem={({item}) => this._renderItem(item)}
                refreshControl={
                    <RefreshControl
                        title={'Loading'} //IOS
                        colors={['orange', 'green']} //android
                        tintColor={['orange']} //IOS
                        refreshing={this.state.isLoading}
                        onRefresh={() => {
                            this.setState({
                                pageIndex: 0
                            }, () => this._loadData(this.state.pageIndex))
                        }}
                    />
                }
                ListFooterComponent={() => this.enIndicator()}
                onEndReachedThreshold={0.1}
                onEndReached={() => {
                    this.setState({
                        pageIndex: this.state.pageIndex + 1
                    }, () => this._loadData(this.state.pageIndex))
                }}
            />
        );
    }

}

const styles = StyleSheet.create({
    itemsView: {
        width: '100%'
    },
    item: {
        width: '100%',
        backgroundColor: '#fff',
        marginBottom: 15,
        padding: 15,
    },
    itemName: {
        fontSize: 18,
        color: '#000',
        fontWeight: 'bold',
        marginBottom: 5,
    },
    desView: {
        height: 80,
    },
    itemFooter: {
        height: 50,
        alignItems: 'center',
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    itemView: {
        flexDirection: 'row',
        justifyContent: 'flex-start',
        height: 30,
        alignItems: 'center'
    },
    itemText: {
        fontSize: 14,
        color: '#000',
    },
    more: {
        paddingLeft: 10,
        paddingRight: 10,
        backgroundColor: '#1E5AAF',
        height: 25,
        borderRadius: 25
    },
    noMore: {
        alignItems: 'center',
        marginBottom: 20,
        marginTop: 10
    }
});