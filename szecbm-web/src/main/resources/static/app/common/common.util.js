/**
 *************************************************
 ******      JS工具类，定义常用的方法           ******
 *************************************************
 */
(function (window) {
    'use strict';

    var util = {
        loginUrl: formatUrl("login"),
        initJqValidation: initJqValidation,
        /**
         *请求加上ContextPath
         *
         */
        formatUrl: formatUrl,
        checkLength: checkLength,
        /**
         * 字符串格式，第一个参数为需要格式的字符串，以一对花括号和数字{0}获取后面的参数，第二个参数开始为字符串提供格式的数据
         * 如：util.format("{0} {1} !", "Hello", "World");  浏览器的控制台的输出结果为Hello World !
         * @returns {*}
         */
        format: format,
        /**
         * 根据表单根据odata格式数据   TODO 还需要处理or
         * @param from
         * @param filters
         * @returns {string|object}
         */
        buildOdataFilter: buildOdataFilter,
        getTableOption: getTableOption,
        getTableFilterOption: getTableFilterOption,
        initUpload: initUpload,
        /**
         * 高频执行事件/方法的防抖
         * @param func      延时调用函数
         * @param wait      延迟多长时间
         * @param immediate 是否立刻执行
         * @returns {Function}
         */
        debounce: function (func, wait, immediate) {
            var timeout, result;

            var debounced = function () {
                var context = this;
                var args = Array.prototype.slice.call(arguments);

                if (timeout) clearTimeout(timeout);
                if (immediate) {
                    // 如果已经执行过，不再执行
                    var callNow = !timeout;
                    timeout = setTimeout(function () {
                        timeout = null;
                    }, wait);
                    if (callNow) {
                        result = func.apply(context, args)
                    }
                }
                else {
                    timeout = setTimeout(function () {
                        result = func.apply(context, args)
                    }, wait);
                }
                return result;
            };

            debounced.cancel = function () {
                clearTimeout(timeout);
                timeout = null;
            };

            return debounced;
        },
        UUID: function () {
            var d = new Date().getTime();
            var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = (d + Math.random() * 16) % 16 | 0;
                d = Math.floor(d / 16);
                return (c == 'x' ? r : (r & 0x7 | 0x8)).toString(16);
            });
            return uuid;
        }
    };

    window.util = util;

    function initJqValidation(selector) {
        selector = selector || "form";
        $(selector).removeData("validator").removeData("unobtrusiveValidation");
        $.validator.unobtrusive.parse(selector);
    }

    function formatUrl(url) {
        url = url || "";
        // 如果链接带有协议头，则返回原始链接
        if (url.indexOf("http://") == 0 || url.indexOf("https://") == 0
            || typeof contextPath == "undefined" || url.indexOf('javascript:;') > -1) {
            return url;
        }
        if (url.indexOf("/") != 0) {
            url = '/' + url;
        }
        return contextPath + format(url, Array.prototype.slice.call(arguments, 1));
    }

    var formatReg = /\{(\w+)(:[^\}]+)?(\.[^\}]+)?\}/g;
    function format() {
        var str = arguments[0] || "", _d;
        if (!str) return false;
        var data = Array.prototype.slice.call(arguments, 1);
        if (data.length == 1 && (Object.prototype.toString.call(data[0]) == "[object Object]")
            || Object.prototype.toString.call(data[0]) == "[object Array]") {
            data = data[0];
        }
        return str.replace(formatReg, function (m, i, f) {
            _d = data[i];
            if (typeof _d == "undefined" || _d == null) {
                return "";
            } else if (Object.prototype.toString.call(_d) == "[object Date]") {
                return _d.format(f.substr(1));
            } else if (typeof _d == "object") {
                return JSON.stringify(_d);
            } else {
                if (f && f.length > 1) {
                    _d = new Date(_d).format(f.substr(1));
                }
                return _d;
            }
        });
    }

    function checkLength(obj, max, id) {
        if (!id) {
            return false;
        }
        var n = 0;
        max = max || 0;
        if (obj) {
            var length = obj.length;
            if (length <= max) {
                n = max - length;
            } else {
                n = 0;
            }
        } else {
            n = max;
        }
        $("#" + id).html("<font size='5'>" + n + "</font>");
    }

    // 封装bs table的默认值
    var tableDefaultOption = {
        sidePagination: "server",
        cache: false,
        striped: true,
        pagination: true,
        pageSize: 10,
        pageList: [10, 25, 50, 100],
        showColumns: true,
        showRefresh: true,
        minimumCountColumns: 2,
        clickToSelect: true,
        maintainSelected: true,
        totalField: "count",            // 修改bs table默认统计字段
        dataField: "value",             // 修改bs table默认数据字段
        defaultSort: "createdDate desc",
        defaultFilters: null
    };

    function buildOdataFilter(from, filters) {
        filters = filters || [];
        if (!angular.isArray(filters)) {
            filters = [filters];
        }
        $(from).find('input,select,textarea').each(function (index, obj) {
            var $me = $(this), val = obj.value, name = obj.name, operator;
            if (!name || !val) return;
            if (name.indexOf("filter_") == 0) {
                var tmp = name.split("_");
                if (tmp.length == 3) {
                    operator = tmp[1];
                    name = tmp[2] || "";
                } else if (tmp.length == 2) {
                    operator = "like";
                    name = tmp[1] || "";
                } else {
                    return;
                }
            } else if (val) {
                operator = $me.attr("operator") || "eq";
            }
            var names = name.split("|");
            if (names.length > 1) {
                var orLogic = [];
                for (var n in names) {
                    orLogic.push({
                        field: names[n],
                        operator: operator,
                        value: val
                    });
                }
                filters.push({
                    logic: "or",
                    filters: orLogic
                });
            } else {
                filters.push({
                    field: name,
                    operator: operator,
                    value: val
                });
            }
        });

        return filters.length == 0 ? "" : $.toOdataFilter({
            logic: "and",
            filters: filters
        });
    }

    /**
     * 获取bs table的配置项（默认封装，带分页）
     * @param option    自定义配置项
     */
    function getTableOption(option) {
        option = option || {};
        return $.extend({}, tableDefaultOption, {
            queryParams: function (params) {
                var me = this,
                    _params = {
                        "$skip": params.offset,
                        "$top": params.limit,
                        "$orderby": !params.sort ? me.defaultSort : (params.sort + " " + params.order),
                        "$filter": buildOdataFilter(me.toolbar || "#toolbar", me.defaultFilters)
                    };
                if (me.pagination) {
                    _params["$inlinecount"] = "allpages";
                }
                return _params;
            }
        }, option);
    }

    function getTableFilterOption(option) {
        option = option || {};
        return $.extend({}, tableDefaultOption, {
            filterControl: true,
            filterShowClear: true
        }, option);
    }

    function initUpload(selector, options, bsWin) {
        if (!selector) {
            return false;
        }
        if (!bsWin) {
            alert("缺少参数，上传控件无法初始化");
            return false;
        }
        var $scope = angular.element("body").scope();
        options = $.extend({}, {
            uploader: formatUrl("sys/attachment/upload"),
            swf: formatUrl('libs/uploadify/uploadify.swf'),
            cancelImage: formatUrl('libs/uploadify/uploadify-cancel.png'),
            multi: false,
            fileObjName: 'files',       // 上传参数名称
            fileSizeLimit: "40MB",      // 上传文件大小限制
            buttonText: '<span style="font-size: 12px">选择文件</span>',
            fileExt: '*.pdf;*.txt;*.png;*.jpg;*.doc;*.docx;*.wps',
            fileTypeExts: '*.pdf;*.txt;*.png;*.jpg;*.doc;*.docx;*.wps',
            fileTypeDesc: "请选择*.pdf;*.txt;*.png;*.jpg;*.doc;*.docx;*.wps文件",     // 文件说明
            removeCompleted: true,     // 设置已完成上传的文件是否从队列中移除，默认为true
            // 'onSelectError' : function(file) {
            //     alert('The file ' + file.name + ' returned an error and was not added to the queue.');
            // },
            onUploadError: function (file, errorCode, errorMsg, errorString) {
                $scope.$apply(function () {
                    switch (errorCode) {
                        case -100:
                            bsWin.error("上传的文件数量已经超出系统限制的" + myUploadify.uploadify('settings', 'queueSizeLimit') + "个文件！");
                            break;
                        case -110:
                            bsWin.error("文件 [" + file.name + "] 大小超出系统限制的" + myUploadify.uploadify('settings', 'fileSizeLimit') + "大小！");
                            break;
                        case -120:
                            bsWin.error("文件 [" + file.name + "] 大小异常！");
                            break;
                        case -130:
                            bsWin.error("文件 [" + file.name + "] 类型不正确！");
                            break;
                        default:
                            bsWin.error("上传失败");
                            break;
                    }
                })
            },
            onCancel: function (file) {
                $scope.$apply(function () {
                    bsWin.confirm("询问提示", "确认删除该文件吗！");
                })
            },
            onFallback: function () {
                setTimeout(function () {
                    $scope.$apply(function () {
                        bsWin.warning("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。"); //检测FLASH失败调用
                    })
                }, 100)
            },
            onQueueComplete: function () {
                $('.upload_load').remove();
            }
        }, options || {});

        var myUploadify = $(selector).uploadify(options);
        return myUploadify;
    }

    // 对Date的扩展，将 Date 转化为指定格式的String
    // 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
    // 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
    // 例子：
    // (new Date()).format("yyyy-MM-dd HH:mm:ss.S") ==> 2006-07-02 08:09:04.423
    // (new Date()).format("yyyy-M-d H:m:s.S")      ==> 2006-7-2 8:9:4.18
    if (!Date.prototype.format) {
        Date.prototype.format = function (fmt) {
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "H+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds() //毫秒
            };
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt))
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }
    }

    // 判断是否引入 bootstrapTable 插件包
    if ($.fn.bootstrapTable && $.fn.bootstrapTable.defaults.filterTemplate) {
        var BootstrapTable = $.fn.bootstrapTable.Constructor,
            _initColumn = BootstrapTable.prototype.initColumn;

        // 根据 filterControl 的配置，设置数据的格式化，主要针对数据字典
        BootstrapTable.prototype.initColumn = function (column) {
            var filterControl = column.filterControl;
            if (typeof filterControl == "string" && filterControl.toLowerCase() == "dict") {
                var filterData = column.filterData;
                if (typeof filterData == "string") {
                    var filterDataName = column.filterDataName || "dictName";
                    if (!column.formatter) {
                        column.formatter = util.format('{{{0}.dicts[row.{1}].{2}}}',
                            filterData, column.field, filterDataName
                        );
                    }
                }
            }

            return $.extend({}, _initColumn.apply(this, Array.prototype.slice.apply(arguments)), column);
        }

        // 扩展表头 filter
        $.extend($.fn.bootstrapTable.defaults.filterTemplate, {
            // 针对数据字典的处理（单选）
            dict: function (that, field, isVisible, operator, placeholder, column) {
                var filterData = column.filterData || "DICT",
                    filterDataKey = column.filterDataKey || "key",
                    filterDataName = column.filterDataName || "value";
                if (typeof filterData == "string") {
                    if (filterDataKey == "key") {
                        filterDataKey = "dictKey";
                    }
                    if (!filterDataName || filterDataName == "value") {
                        filterDataName = "dictName";
                    }
                    // if (!column.formatter) {
                    //     column.formatter = util.format("{{{0}.dicts[row.{1}].dictName}}", filterData, field);
                    // }

                    var tag = util.format('<select class="form-control odata-filter select-filter-control bootstrap-table-filter-control-{0}" name="{0}" style="width: 100%; visibility: {1}">' +
                        '<option value="">请选择……</option><option ng-repeat="x in {2}.dictList" value="\{\{{3}\}\}">{{{4}}}</option></select>',
                        field, isVisible, filterData, "x." + filterDataKey, "x." + filterDataName
                    );
                    return tag;
                } else if ($.isArray(filterData)) {
                    var tags = [util.format('<select class="form-control odata-filter select-filter-control bootstrap-table-filter-control-{0}" name="{0}" style="width: 100%; visibility: {1}">', field, isVisible)];
                    tags.push('<option value="">请选择……</option>');
                    $.each(filterData, function (i, o) {
                        var key = o[filterDataKey], name = o[filterDataName];
                        if (key && name) {
                            tags.push(util.format('<option value="{0}">{1}</option>', key, name));
                        }
                    });
                    tags.push('</select>');
                    return tags.join("");
                } else {
                    return "";
                }
            }
        });

    }

})(window);