/**
 * Created by Administrator on 2018/5/12 0012.
 */

import * as config from '../constants/config.js';

/**
 * 数组转String，用逗号拼接
 * @param arr
 * @returns {string}
 * @constructor
 */

export function arrayToString(arr = []) {
    return arr.join(config.DEFAULT_LINK_SYMBOL);
}

/**
 * 检验obj是否为空
 * @param obj
 */
export function checkObjNull(obj) {
   if(obj === null || obj === undefined || obj === '' || obj === 'null' || obj === 'undefined'){
        return true;
   }else{
       return false;
   }
}