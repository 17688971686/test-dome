/**
 * 自定义弹窗窗口（在对应的方法中注入bsWin对象）
 * 提示窗口使用方法：
 *  bsWin.alert("消息提示", "操作成功！", function () { console.log("This's close!"); } );
 * 或者
 *  bsWin.alert({
 *      title: "消息提示",
 *      message: "操作成功！",
 *      onClose: function () { console.log("This's close!"); }
 *  });
 * 询问窗口使用方法：
 *  bsWin.confirm("询问提示", "是否进行该操作！", function () { console.log("This's ok!"); }, function () { console.log("This's close!"); },function(){console.log("This's onCancel!");} );
 * 或者
 *  bsWin.confirm({
 *      title: "询问提示",
 *      message: "是否进行该操作！",
 *      onOk: function () { console.log("This's ok!"); },
 *      onClose: function () { console.log("This's close!"); },
 *      onCancel:function(){console.log("This's onCancel!");}
 *  });
 */
(function () {
    'use strict';

    var app = angular.module('app');
    angular.module('app').factory('bsWin', bsWinFactory);

    function bsWinFactory(){
        return {
            success: function (message, onClose) {
                return this.alert({
                    message: message,
                    type: "success",
                    onClose: onClose,
                    icon: 1,
                });
            },
            warning:  function (message, onClose) {
                return this.alert({
                    message: message,
                    type: "warning",
                    onClose: onClose,
                    icon: 5,
                });
            },
            error: function (message, onClose) {
                return this.alert({
                    message: message,
                    type: "error",
                    onClose: onClose,
                    icon: 2,
                });
            },
            alert: function (options, message, onClose) {
                if (!angular.isObject(options)) {
                    var title;
                    if (!message || angular.isFunction(message)) {
                        onClose = message;
                        message = options;
                    } else {
                        title = options;
                    }
                    options = {
                        message: message,
                        onClose: onClose || function () {
                        }
                    };
                    if (title) {
                        options.title = title;
                    }
                }
                options.onClose = options.onClose || function(){};
                layer.alert(
                    options.message,
                    {
                        title:(options.title)?options.title:"系统消息",
                        icon: (!options.icon) ? 6 : options.icon,
                        skin: 'layui-layer-lan',
                    },
                    function(index){
                        layer.close(index);
                        options.onClose();
                    }
                );
            },
            confirm: function (options, message, onOk, onClose,onCancel) {
                if (!angular.isObject(options)) {
                    var title;
                    if (!message || angular.isFunction(message)) {
                        onCancel = onClose || function(){
                            };
                        onClose = onOk || function () {
                            };
                        onOk = message || function () {
                            };
                        message = options;
                    } else {
                        title = options;
                    }

                    options = {
                        message: message,
                        onOk: onOk,
                        onClose: onClose,
                        onCancel: onCancel
                    };
                    if (title) {
                        options.title = title;
                    }
                }
                options.onClose = options.onClose || function(){};
                //询问框
                layer.confirm(
                    options.message,
                    {
                        title:(options.title)?options.title:"询问提示",
                        icon: 3,
                        skin: 'layui-layer-lan',
                        btn: ['确认','取消'],
                        end : options.onClose
                    },
                    function(index){
                        layer.close(index);
                        if ((options.onOk) != undefined && typeof (options.onOk) == 'function') {
                            options.onOk();
                        }
                    },
                    function(index){
                        layer.close(index);
                        if ((options.onCancel) != undefined && typeof (options.onCancel) == 'function') {
                            options.onCancel();
                        }
                    }
                );
            }

        };
    }

})();