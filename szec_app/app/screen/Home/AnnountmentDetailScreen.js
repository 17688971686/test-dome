import React from 'react';
import {ScrollView, StyleSheet, Text, TouchableOpacity, View,WebView,Platform} from 'react-native';
import Header from '../../component/HeaderComponent'
import ScrollableTabView, {ScrollableTabBar, DefaultTabBar} from 'react-native-scrollable-tab-view';

export default class AnnountmentDetailScreen extends React.Component {
    constructor(props) {
        super(props);
        const {item} = this.props.navigation.state.params;
        this.state = {
            item:item,
            data:10
        };
    }

    componentDidMount() {
    }
    sendMessage() {
        this.refs.webview.postMessage(this.state.item.anContent);
    }
    render() {
        const source = (Platform.OS === 'ios') ? require('./content.html') : { uri: 'file:///android_asset/content.html' };
        return (
            <View style={styles.container}>
                <Header title={'通知公告'} showBackTitle={true} {...this.props}/>
                <View style={styles.header}>
                    <Text style={{fontSize:18,color:'#000',marginBottom:10}}>{this.state.item.anTitle}</Text>
                    <View style={{flexDirection:'row'}}>
                        <Text style={{marginRight:15}}>发布人：{this.state.item.issueUser}</Text>
                        <Text>发布日期：{this.state.item.createdDate.substring(0,10)}</Text>
                    </View>
                </View>
                <View style={styles.content}>
                    <WebView
                        ref={'webview'}
                        style={{
                            width:'100%',
                            height:'100%',
                        }}
                        onLoad={()=>this.sendMessage()}
                        source={source}
                        scalesPageToFit={Platform.OS !== 'ios'}
                    />
                </View>
            </View>
        );
    }

}
const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'flex-start',
        backgroundColor:'#FFF',
    },
    header:{
        backgroundColor:'#efefef',
        padding:20,
        width:'100%',
        justifyContent: 'center',
        alignItems: 'center',
        borderBottomWidth:0.5,
    },
    content:{
        flex:1,
        width:'100%',
    },
    contentText:{
        fontSize: 15,
        lineHeight:30
    }
});