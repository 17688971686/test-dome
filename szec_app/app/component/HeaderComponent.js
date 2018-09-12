import React from "react";
import {
    View,
    Text,
    StyleSheet,
    TouchableOpacity,
    Platform,
    StatusBar
} from "react-native";
import Icon from 'react-native-vector-icons/Ionicons';

const NAV_BAR_HEIGHT_ANDROID = 50;
const NAV_BAR_HEIGHT_IOS = 44;
const STATUS_BAR_HEIGHT = 20;

export default class HeaderComponent extends React.Component {
    static defaultProps = {
        titleColor: '#fff',
        title: '',
        backTitle: <Icon name="ios-arrow-back" size={25}/>,
        fontSize: 18,
        statusBar: {
            barStyle:'light-content',
            backgroundColor:'#1E5AAF',
            hidden:false
        }
    };

    constructor(props) {
        super(props);
    }

    render() {
        /*IOS平台需要自定义状态栏样式 */
        let statusBar = <View style={[styles.statusBar, Platform.OS === 'ios' ?this.props.statusBar:'']}>
            {/*android状态栏样式*/}
            <StatusBar {...this.props.statusBar}/>
        </View>;
        const {titleColor,title, showBackTitle, backTitle, fontSize, headerRight, navigation} = this.props;
        return (
            <View>
                {statusBar}
                <View style={styles.container}>
                    <TouchableOpacity style={styles.headerLeft} onPress={() => showBackTitle && navigation.goBack()}>
                        <Text style={{color:titleColor}}>
                            {showBackTitle && backTitle}
                        </Text>
                    </TouchableOpacity>
                    <View style={styles.titleView}>
                        <Text numberOfLines={1} ellipsizeMode={'middle'} style={{color: titleColor, fontSize: fontSize}}>{title}</Text>
                    </View>
                    <View style={styles.headerRight}>
                        {headerRight}
                    </View>
                </View>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        height: Platform.OS === 'ios' ? NAV_BAR_HEIGHT_IOS : NAV_BAR_HEIGHT_ANDROID,
        width: '100%',
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        backgroundColor: '#1E5AAF',
        paddingLeft:15,
        paddingRight:15,
    },
    headerRight:{
        width:50,
    },
    headerLeft: {
        height: Platform.OS === 'ios' ? NAV_BAR_HEIGHT_IOS : NAV_BAR_HEIGHT_ANDROID,
        justifyContent:'center',
        alignItems:'flex-start',
        width: 50,
    },
    titleView:{
        flex:1,
        height:50,
        justifyContent: 'center',
        alignItems: 'center',
    },
    statusBar: {
        height: Platform.OS === 'ios' ? STATUS_BAR_HEIGHT : 0,
    }
});