import React, {Component} from "react";
import {
    View,
    Text,
    FlatList,
    StyleSheet

} from "react-native";


/*工作方案*/
export default class SignWorkprogramComponent extends Component {
    _renderItem(item) {
        return (
            <View style={styles.container}>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>评审方式：</Text>
                    <Text style={styles.itemText}>{item.reviewType} | {item.isSigle}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>主管部门：</Text>
                    <Text style={styles.itemText}>{item.mainDeptName}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>是否有环评：</Text>
                    <Text style={styles.itemText}>{item.isHaveEIA ? '是' : '否'}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>项目类别：</Text>
                    <Text style={styles.itemText}>{item.projectType} | {item.projectSubType}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>行业类别：</Text>
                    <Text style={styles.itemText}>{item.industryType}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>联系人：</Text>
                    <Text style={styles.itemText}>{item.contactPerson}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>手机：</Text>
                    <Text style={styles.itemText}>{item.contactPersonPhone}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>电话：</Text>
                    <Text style={styles.itemText}>{item.contactPersonTel}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>传真：</Text>
                    <Text style={styles.itemText}>{item.contactPersonFax}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>申报投资：</Text>
                    <Text style={styles.itemText}>{item.appalyInvestment}万元</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>申报建设规模：</Text>
                    <Text style={styles.itemText}>{item.buildSize}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>申报建设内容：</Text>
                    <Text style={styles.itemText}>{item.buildContent}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>项目背景：</Text>
                    <Text style={styles.itemText}>{item.projectBackGround}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>评估部门：</Text>
                    <Text style={styles.itemText}>{item.reviewOrgName}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>第一负责人：</Text>
                    <Text style={styles.itemText}>{item.mianChargeUserName}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>第二负责人：</Text>
                    <Text style={styles.itemText}>{item.secondChargeUserName}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>是否有补充资料函：</Text>
                    <Text style={styles.itemText}>{item.isHaveSuppLetter ? '有' : '无'}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>补充资料函发文日期：</Text>
                    <Text style={styles.itemText}>{item.suppLetterDate}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>调研时间：</Text>
                    <Text style={styles.itemText}>{item.studyAllDay}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>专家费用：</Text>
                    <Text style={styles.itemText}>{item.expertCost}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>评审会日期：</Text>
                    <Text style={styles.itemText}>{item.rbDate}</Text>
                </View>
                <View style={styles.itemView}>
                    <Text style={styles.itemName}>会议室名称：</Text>
                    <Text style={styles.itemText}>{item.addressName}</Text>
                </View>
            </View>
        )
    }

    _extraUniqueKey(item, index) {
        return "index" + index + item;
    }

    render() {
        const {data} = this.props;
        return (
            data ?
                <FlatList
                    style={{width: '100%'}}
                    keyExtractor={this._extraUniqueKey}
                    data={data}
                    renderItem={({item}) => this._renderItem(item)}
                />
                :
                <View style={{height: 50, justifyContent: 'center', alignItems: 'center'}}>
                    <Text>暂无工作方案</Text>
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