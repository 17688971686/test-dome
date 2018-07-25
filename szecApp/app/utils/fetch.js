import 'isomorphic-fetch';
import 'es6-promise';
import {message} from "antd";
import history from 'history/createBrowserHistory'
import * as config from '../constants/config.js';
/**
 * 将对象转成 a=1&b=2的形式
 * @param obj 对象
 */
function obj2params(obj, arr = [], idx = 0) {
    var result = '';
    var item;
    for (item in obj) {
        if (obj[item] !== null && obj[item] !== undefined && obj[item] !== '') {
            result += '&' + item + '=' + encodeURIComponent(obj[item]);
        }
    }
    if (result) {
        result = result.slice(1);
    }
    return result;
}

/**
 * 真正的请求
 * @param url 请求地址
 * @param options 请求参数
 * @param method 请求方式
 */
function commonFetcdh(url, options, method = 'GET', isCommitForm) {
    let initObj = {}
    method = method.toUpperCase();
    //url = config.js.SYSTEM_ROOT + url;
    const searchStr = obj2params(options);
    if (method === 'GET') {
        // fetch的GET不允许有body，参数只能放在url中
        url += '?' + searchStr
        initObj = {
            method: method,
            mode: "cors",
            credentials: 'include',
            headers: new Headers({
                'Accept': 'application/json',
                'sysToken': localStorage.getItem(config.TOKEN_NAME) || '',
                'userType': config.USER_TYPE
            }),
        }
    } else {
        if (isCommitForm) {
            initObj = {
                method: method,
                mode: 'cors',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                    'sysToken': localStorage.getItem(config.TOKEN_NAME) || '',
                    'userType': config.USER_TYPE
                },
                body: JSON.stringify(options)
            }
        } else {
            initObj = {
                method: method,
                mode: "cors",
                credentials: 'include',
                headers: new Headers({
                    'Accept': 'application/json',
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                    'sysToken': localStorage.getItem(config.TOKEN_NAME) || '',
                    'userType': config.USER_TYPE
                }),
                body: searchStr
            }
        }
    }
    return fetch(url, initObj).then(callback).catch(errHandle);
}

//创建fetch中then方法的回调
function callback(res) {
    const token = res.headers.get(config.TOKEN_NAME);
    if (token) {
        localStorage.setItem(config.TOKEN_NAME, token);
    }
    if (res.ok || (res.status > 199 && res.status < 300)) {
        return res.json().then(response => {
            return response;
        });
    } else {
        return Promise.reject(res);
    }
}

//创建容错方法
function errHandle(res) {
    if (res.status == 401 || res.status == 403) {
        message.error("你的权限已失效，请重新登录！");
        history().push("/");
    } else if (res.status == 500) {
        message.error("请求异常，错误信息已记录，请联系管理员处理！");
    }
}

/**
 * GET请求
 * @param url 请求地址
 * @param options 请求参数
 */
export function GET(url, options = {}) {
    return commonFetcdh(url, options, 'GET', false)
}

/**
 * POST请求
 * @param url 请求地址
 * @param options 请求参数
 */
export function POST(url, options = {}, obj = false) {
    return commonFetcdh(url, options, 'POST', obj)
}

//发送 post 请求（首先会发送option）
export function DOWNLOAD(url, paramsObj) {
    const searchStr = obj2params(paramsObj)
    url += '?' + searchStr;
    fetch(url).then(res => res.blob().then(blob => {
        var a = document.createElement('a');
        var url = window.URL.createObjectURL(blob);
        var filename = res.headers.get('Content-disposition');
        a.href = url;
        a.download = filename;
        a.click();
        window.URL.revokeObjectURL(url);
    }));
}

//发送 post 请求（首先会发送option）
export function DOWNLOAD_CLOSE_WINDOW(url, paramsObj) {
    const searchStr = obj2params(paramsObj)
    url += '?' + searchStr;
    fetch(url).then(res => res.blob().then(blob => {
        var a = document.createElement('a');
        var url = window.URL.createObjectURL(blob);
        var filename = res.headers.get('Content-disposition');
        a.href = url;
        a.download = filename;
        a.click();
        window.URL.revokeObjectURL(url);
        window.close();
    }));
}

/**
 * 封装分页查询参数
 * @param options
 */
export function getPageParams(page, size, options = {}) {
    options[config.PAGE_PARAMS.page] = page;
    options[config.PAGE_PARAMS.size] = size;
    return options;
}

export function buildParams(options = {}) {
    return obj2params(options);
}

/**
 * 封装随机码
 */
export function UUID() {
    var d = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c == 'x' ? r : (r & 0x7 | 0x8)).toString(16);
    });
    return uuid;
}