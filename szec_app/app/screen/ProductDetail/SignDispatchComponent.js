import React, {Component} from "react";
import {
    View,
    Text,
    StyleSheet
} from "react-native";


/*发文信息*/
export default class SignDispatchComponent extends Component {
    render() {
        const {data} = this.props;
        return (
            data ?
                <View style={styles.container}>
                    <View style={styles.itemView}>
                        <Text style={styles.itemName}>发文方式：</Text>
                        <Text style={styles.itemText}>{data.dispatchWay === 1 ? "单个发文" : "合并发文"}</Text>
                    </View>
                    <View style={styles.itemView}>
                        <Text style={styles.itemName}>发文类型：</Text>
                        <Text style={styles.itemText}>{data.dispatchType}</Text>
                    </View>
                    <View style={styles.itemView}>
                        <Text style={styles.itemName}>文件标题：</Text>
                        <Text style={styles.itemText}>{data.fileTitle}</Text>
                    </View>
                    <View style={styles.itemView}>
                        <Text style={styles.itemName}>发文日期：</Text>
                        <Text style={styles.itemText}>{data.dispatchDate}</Text>
                    </View>
                    <View style={styles.itemView}>
                        <Text style={styles.itemName}>文号：</Text>
                        <Text style={styles.itemText}>{data.fileNum}</Text>
                    </View>
                    <View style={styles.itemView}>
                        <Text style={styles.itemName}>秘密等级：</Text>
                        <Text style={styles.itemText}>{data.secretLevel}</Text>
                    </View>
                    <View style={styles.itemView}>
                        <Text style={styles.itemName}>缓急程度：</Text>
                        <Text style={styles.itemText}>{data.urgentLevel}</Text>
                    </View>
                    <View style={styles.itemView}>
                        <Text style={styles.itemName}>是否与其他阶段关联：</Text>
                        <Text style={styles.itemText}>{data.isRelated ? '是' : '否'}</Text>
                    </View>
                    <View style={styles.itemView}>
                        <Text style={styles.itemName}>发行范围：</Text>
                        <Text style={styles.itemText}>{data.dispatchScope}</Text>
                    </View>
                    <View style={styles.itemView}>
                        <Text style={styles.itemName}>相关说明：</Text>
                        <Text style={styles.itemText}>{data.description}</Text>
                    </View>
                    <View style={styles.itemView}>
                        <Text style={styles.itemName}>评审意见摘要：</Text>
                        <Text style={styles.itemText}>{data.reviewAbstract}</Text>
                    </View>
                </View>
                :
                <View style={{height: 50, justifyContent: 'center', alignItems: 'center'}}>
                    <Text>暂无发文信息</Text>
                </View>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        width: '100%',
    },
    itemView: {
        width: '100%',
        flexDirection: 'row',
        padding: 10,
        justifyContent: 'flex-start',
        borderBottomWidth: 0.5,
        borderBottomColor: '#ccc',
        backgroundColor: '#fff'
    },
    itemName: {
        fontSize: 14,
        color: '#000',
        marginRight: 5,
        lineHeight: 25,
    },
    itemText: {
        color: '#999',
        lineHeight: 25,
        paddingRight: 70
    },
});