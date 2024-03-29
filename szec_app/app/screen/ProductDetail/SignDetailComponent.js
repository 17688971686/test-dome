import React, {Component} from "react";
import {
    View,
    Text,
    StyleSheet
} from "react-native";


/*审批登记*/
export default class SignDetailComponent extends Component {
    render() {
        const {data} = this.props;
        return (
            <View style={styles.container}>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>项目名称：</Text>
                    <Text style={styles.itemText}>{data.projectname}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>收文编号：</Text>
                    <Text style={styles.itemText}>{data.signNum}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>建设单位：</Text>
                    <Text style={styles.itemText}>{data.builtcompanyName}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>项目代码：</Text>
                    <Text style={styles.itemText}>{data.projectcode}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>编制单位：</Text>
                    <Text style={styles.itemText}>{data.designcompanyName}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>缓急程度：</Text>
                    <Text style={styles.itemText}>{data.urgencydegree}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>秘密等级：</Text>
                    <Text style={styles.itemText}>{data.secrectlevel}</Text>
                </View>
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