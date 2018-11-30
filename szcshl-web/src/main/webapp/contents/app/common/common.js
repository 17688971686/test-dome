(function () {
    'use strict';

    var service = {
        initJqValidation: initJqValidation,         // 重置form验证
        requestSuccess: requestSuccess,             // 请求成功时执行
        format: format,                             // string格式化
        blockNonNumber: blockNonNumber,             // 只允许输入数字
        floatNumberInput: floatNumberInput,
        adminContentHeight: adminContentHeight,     // 当前Content高度
        alert: alertDialog,                         // 显示alert窗口
        confirm: confirmDialog,                     // 显示Confirm窗口
        getQuerystring: getQuerystring,             // 取得Url参数
        kendoGridConfig: kendoGridConfig,           // kendo grid配置
        getKendoCheckId: getKendoCheckId,           // 获得kendo grid的第一列checkId
        cookie: cookie,                             // cookie操作
        getToken: getToken,                         // 获得令牌
        http: http,                                 // http请求
        gridDataSource: gridDataSource,             // gridDataSource
        buildOdataFilter: buildOdataFilter,         // 创建多条件查询的filter
        kendoGridDataSource: kendoGridDataSource,   // 获取gridDataSource
        getTaskCount: getTaskCount,                 // 用户待办总数
        uuid: uuid,                                 // js
        downloadReport: downloadReport,             //报表下载
        htmlEscape: htmlEscape,                     //实现html转码
        htmlDecode: htmlDecode,                     //实现html解码
        removeXss : removeXss                       //解除xss
    };
    window.common = service;

    function removeXss(str){
        if (str.length == 0) return "";
        var val = str.toString();
        val = val.replace(/([\x00-\x08][\x0b-\x0c][\x0e-\x20])/g, '');

        var search = 'abcdefghijklmnopqrstuvwxyz';
        search += 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
        search += '1234567890!@#$%^&*()';
        search += '~`";:?+/={}[]-_|\'\\';

        for (var i = 0; i < search.length; i++) {
            var re = new RegExp('(&#[x|X]0{0,8}'+ parseInt(search[i].charCodeAt(),16)+';?)','gi');
            val = val.replace(re, search[i]);
            re = new RegExp('(&#0{0,8}'+ search[i].charCodeAt() + ';?)','gi');
            val = val.replace(re, search[i]);
        }

        var ra1 = ['javascript', 'vbscript', 'expression', 'applet', 'meta','blink', 'link',  'script', 'embed', 'object', 'iframe', 'frame', 'frameset', 'ilayer', 'layer', 'bgsound', 'title'];
        var ra2 = ['onabort', 'onactivate', 'onafterprint', 'onafterupdate', 'onbeforeactivate', 'onbeforecopy', 'onbeforecut', 'onbeforedeactivate', 'onbeforeeditfocus', 'onbeforepaste', 'onbeforeprint', 'onbeforeunload', 'onbeforeupdate', 'onblur', 'onbounce', 'oncellchange', 'onchange', 'onclick', 'oncontextmenu', 'oncontrolselect', 'oncopy', 'oncut', 'ondataavailable', 'ondatasetchanged', 'ondatasetcomplete', 'ondblclick', 'ondeactivate', 'ondrag', 'ondragend', 'ondragenter', 'ondragleave', 'ondragover', 'ondragstart', 'ondrop', 'onerror', 'onerrorupdate', 'onfilterchange', 'onfinish', 'onfocus', 'onfocusin', 'onfocusout', 'onhelp', 'onkeydown', 'onkeypress', 'onkeyup', 'onlayoutcomplete', 'onload', 'onlosecapture', 'onmousedown', 'onmouseenter', 'onmouseleave', 'onmousemove', 'onmouseout', 'onmouseover', 'onmouseup', 'onmousewheel', 'onmove', 'onmoveend', 'onmovestart', 'onpaste', 'onpropertychange', 'onreadystatechange', 'onreset', 'onresize', 'onresizeend', 'onresizestart', 'onrowenter', 'onrowexit', 'onrowsdelete', 'onrowsinserted', 'onscroll', 'onselect', 'onselectionchange', 'onselectstart', 'onstart', 'onstop', 'onsubmit', 'onunload'];
        var ra = [].concat(ra1, ra2);

        var found = true;
        while (found == true) {
            var val_before = val;
            for (var i = 0; i < ra.length; i++) {
                var pattern = '';
                for (var j = 0; j < ra[i].length; j++) {
                    if (j > 0) {
                        pattern += '(';
                        pattern += '(&#[x|X]0{0,8}([9][a][b]);?)?';
                        pattern += '|(&#0{0,8}([9][10][13]);?)?';
                        pattern += ')?';
                    }
                    pattern += ra[i][j];
                }
                pattern = new RegExp(pattern,'gi');
                var replacement = ra[i].substr(0, 2) + '<x>'+ ra[i].substr(2);
                val = val.replace(parttern, replacement);
                if (val_before  == val) {
                    found = false;
                }
            }
        }
        return val;
    }

    function htmlDecode(str) {
        if (str.length == 0) return "";
        var s = str.toString();
        s = s.replace(/&amp;/g, "&");
        s = s.replace(/&lt;/g, "<");
        s = s.replace(/&gt;/g, ">");
        s = s.replace(/&nbsp;/g, " ");
        s = s.replace(/&#39;/g, "\'");
        s = s.replace(/&quot;/g, "\"");
        return s;
    }

    /**
     * 字符串转义，防止dom xss攻击
     * @param str
     */
    function htmlEscape(str) {
        if (str.length == 0) return "";
        var s = str.toString();
        s = s.replace(/&/g, "&amp;");
        s = s.replace(/</g, "&lt;");
        s = s.replace(/>/g, "&gt;");
        s = s.replace(/ /g, "&nbsp;");
        s = s.replace(/'/g, "&#39;");
        s = s.replace(/"/g, "&quot;");
        return s;
    }

    /**
     *
     * @param data 数据
     * @param fileName 文件名
     * @param fileType 文件类型
     * 使用{type: "application/vnd.ms-excel"}的写法，可以保存为xls格式的excel文件
     * 而使用“application/vnd.openxmlformats-officedocument.spreadsheetml.sheet”则会保存为xlsx
     */
    function downloadReport(data, fileName, fileType) {
        var blob = new Blob([data], {type: "application/" + fileType});
        var objectUrl = URL.createObjectURL(blob);
        var aForExcel = $("<a><span class='forExcel'>下载</span></a>").attr("href", objectUrl).attr("download", fileName);
        $("body").append(aForExcel);
        $(".forExcel").click();
        aForExcel.remove();
    }

    function initJqValidation(formObj) {
        if (formObj) {
            formObj.removeData("validator");
            formObj.removeData("unobtrusiveValidation");
            $.validator.unobtrusive.parse(formObj);
        } else {
            $("form").removeData("validator");
            $("form").removeData("unobtrusiveValidation");
            $.validator.unobtrusive.parse("form");
        }
    }//end

    function requestSuccess(options) {
        var showError = function (msg) {
            service.alert({
                vm: options.vm,
                msg: msg,
                fn: function () {
                    options.vm.isSubmit = false;
                    $('.alertDialog').modal('hide');
                }
            });
        };
        if (options.response.status > 400) {
            showError("发生错误！");
        } else {
            var data = options.response.data;
            if (data && data.reCode == 555) {
                showError(data.message);
            } else if (options.fn) {
                options.fn(data);
            }
        }
    }//end

    function format() {
        var theString = arguments[0];
        for (var i = 1; i < arguments.length; i++) {
            var regEx = new RegExp("\\{" + (i - 1) + "\\}", "gm");
            theString = theString.replace(regEx, arguments[i]);
        }
        return theString;
    }//end

    function blockNonNumber(val) {
        var str = val.toString().replace(/[^0-9]/g, '');
        return parseInt(str, 10);
    }//end

    function floatNumberInput(val) {
        return isNaN(parseFloat(val, 10)) ? 0 : parseFloat(val, 10);
    }//end

    function adminContentHeight() {
        return $(window).height() - 180;
    }//end

    function alertDialog(options) {
        options.vm.alertDialogMessage = options.msg;
        options.vm.alertDialogFn = function () {
            if (options.closeDialog && options.closeDialog == true) {
                $('.alertDialog').modal('hide');
                $('.modal-backdrop').remove();
            }
            if (options.fn) {
                options.fn();
            } else {
                $('.alertDialog').modal('hide');
            }
        };
        $('.alertDialog').modal({
            backdrop: 'static',
            keyboard: false
        });
    }//end

    function confirmDialog(options) {
        options.vm.dialogConfirmTitle = options.title;
        options.vm.dialogConfirmMessage = options.msg;
        $('.confirmDialog').modal({
            backdrop: 'static'
        });
        options.vm.dialogConfirmSubmit = options.fn;
        if (options.cancel) {
            options.vm.dialogConfirmCancel = options.cancel;
        } else {
            options.vm.dialogConfirmCancel = function () {
                $('.confirmDialog').modal('hide');
            }
        }
    }//end

    function getQuerystring(key, default_) {
        if (default_ == null)
            default_ = "";
        key = key.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
        var regex = new RegExp("[\\?&]" + key + "=([^&#]*)");
        var qs = regex.exec(window.location.href);
        if (qs == null)
            return default_;
        else
            return qs[1];
    }//end

    function kendoGridDataSource(url, searchForm, page, pageSize, queryParams, isKeepParams) {
        var dataSource = new kendo.data.DataSource({
            type: 'odata',
            transport: kendoGridConfig().transport(url, searchForm, queryParams, isKeepParams),
            schema: kendoGridConfig().schema({
                id: "id",
                fields: {
                    createdDate: {
                        type: "date"
                    }
                }
            }),
            serverPaging: true,
            serverSorting: true,
            serverFiltering: true,
            pageSize: pageSize || 10,
            page: page || 1,
            sort: {
                field: "createdDate",
                dir: "desc"
            }
        });
        return dataSource;
    }//end

    function kendoGridConfig(scope) {
        return {
            filterable: {
                extra: false,
                operators: {
                    string: {
                        "contains": "包含",
                        "eq": "等于"
                    },
                    number: {
                        "eq": "等于",
                        "neq": "不等于",
                        gt: "大于",
                        lt: "小于"
                    },
                    date: {
                        gt: "大于",
                        lt: "小于"
                    }
                }
            },
            pageable: {
                pageSize: 10,
                previousNext: true,
                buttonCount: 5,
                refresh: true,
                pageSizes: true,
                change: function () {
                    if (scope && scope.page) {
                        scope.page = this.dataSource.page();
                    }
                }
            },
            dataBound: function (e) {
                // this.dataSource.page(2);
                var rows = this.items();
                var page = this.pager.page() - 1;
                var pagesize = this.pager.pageSize();
                $(rows).each(function () {
                    var index = $(this).index() + 1 + page * pagesize;
                    var rowLabel = $(this).find(".row-number");
                    $(rowLabel).html(index);
                });
                scope.pageSize = e.sender.dataSource.pageSize();
                scope.page = e.sender.dataSource.page();
            },
            schema: function (model) {
                return {
                    data: "value",
                    total: function (data) {
                        return data['count'];
                    },
                    model: model
                };
            },
            transport: function (url, form, paramObj, isKeepParams) {
                return {
                    read: {
                        url: url,
                        dataType: "json",
                        type: "post",
                        cache: false,
                        beforeSend: function (req) {
                            req.setRequestHeader('Token', service.getToken());
                        },
                        data: function () {
                            if (form) {
                                var filterParam = common.buildOdataFilter(form);
                                if (filterParam) {
                                    if (paramObj && paramObj.$filter) {
                                        var extendFilter = paramObj.$filter;
                                        if (!isKeepParams) {
                                            paramObj = undefined;
                                        }
                                        return {"$filter": filterParam + " and " + extendFilter};
                                    } else {
                                        return {
                                            "$filter": filterParam
                                        };
                                    }
                                } else {
                                    if (paramObj && paramObj.$filter) {
                                        return {
                                            "$filter": paramObj.$filter
                                        };
                                    } else {
                                        return {};
                                    }
                                }
                            } else {
                                return paramObj || {};
                            }
                        }
                    }
                }
            },
            noRecordMessage: {
                template: '暂时没有数据.'
            }
        }
    }//end

    function getKendoCheckId($id) {
        var checkbox = $($id).find('tr td:nth-child(1)').find('input:checked')
        var data = [];
        checkbox.each(function () {
            var id = $(this).attr('relId');
            data.push({
                name: 'id',
                value: id
            });
        });
        return data;
    }//end

    function http(options) {
        options.headers = {
            Token: service.getToken()
        };
        options.$http(options.httpOptions).then(options.success,
            function (response) {
                if (options.onError) {
                    options.onError(response);
                }
            });
    }//end

    // begin:cookie
    function cookie() {
        var cookieUtil = {
            get: function (name, subName) {
                var subCookies = this.getAll(name);
                if (subCookies) {
                    return subCookies[subName];
                } else {
                    return null;
                }
            },
            getAll: function (name) {
                var cookieName = encodeURIComponent(name) + "=", cookieStart = document.cookie
                    .indexOf(cookieName), cookieValue = null, result = {};
                if (cookieStart > -1) {
                    var cookieEnd = document.cookie.indexOf(";", cookieStart)
                    if (cookieEnd == -1) {
                        cookieEnd = document.cookie.length;
                    }
                    cookieValue = document.cookie.substring(cookieStart
                        + cookieName.length, cookieEnd);
                    if (cookieValue.length > 0) {
                        var subCookies = cookieValue.split("&");
                        for (var i = 0, len = subCookies.length; i < len; i++) {
                            var parts = subCookies[i].split("=");
                            result[decodeURIComponent(parts[0])] = decodeURIComponent(parts[1]);
                        }
                        return result;
                    }
                }
                return null;
            },
            set: function (name, subName, value, expires, path, domain, secure) {
                var subcookies = this.getAll(name) || {};
                subcookies[subName] = value;
                this.setAll(name, subcookies, expires, path, domain, secure);
            },
            setAll: function (name, subcookies, expires, path, domain, secure) {
                var cookieText = encodeURIComponent(name) + "=";
                var subcookieParts = new Array();
                for (var subName in subcookies) {
                    if (subName.length > 0
                        && subcookies.hasOwnProperty(subName)) {
                        subcookieParts.push(encodeURIComponent(subName) + "="
                            + encodeURIComponent(subcookies[subName]));
                    }
                }
                if (subcookieParts.length > 0) {
                    cookieText += subcookieParts.join("&");
                    if (expires instanceof Date) {

                        cookieText += ";expires=" + expires.toGMTString();
                    }
                    if (path) {
                        cookieText += ";path=" + path;
                    }
                    if (domain) {
                        cookieText += ";domain=" + domain;
                    }
                    if (secure) {
                        cookieText += ";secure";
                    }
                } else {

                    cookieText += ";expires=" + (new Date(0)).toGMTString();
                }
                document.cookie = cookieText;
            },
            unset: function (name, subName, path, domain, secure) {
                var subcookies = this.getAll(name);
                if (subcookies) {
                    delete subcookies[subName];
                    this.setAll(name, subcookies, null, path, domain, secure);
                }
            },
            unsetAll: function (name, path, domain, secure) {
                this.setAll(name, null, new Date(0), path, domain, secure);
            }
        };
        return cookieUtil;
    }

    // end:cookie

    function getToken() {
        var data = cookie().getAll("data");
        return data != null ? data.token : "";
    }//end

    function gridDataSource(dataSource) {
        dataSource.error = function (e) {
            if (e.status == 401) {
                location.href = window.rootPath + '/home/login';
            } else {

            }
        };
        return dataSource;
    }//end

    // S_封装filer的参数
    function buildOdataFilter(from) {
        var manipulation_rcheckableType = /^(?:checkbox|radio)$/i,
            rsubmitterTypes = /^(?:submit|button|image|reset|file)$/i,
            rsubmittable = /^(?:input|select|textarea|keygen)/i;

        return $(from).map(function () {
            var elements = jQuery.prop(this, "elements");
            return elements ? jQuery.makeArray(elements) : this;
        }).filter(
            function () {
                var type = this.type;
                return this.value
                    && this.name
                    && !$(this).is(":disabled")
                    && rsubmittable.test(this.nodeName)
                    && !rsubmitterTypes.test(type)
                    && (this.checked || !manipulation_rcheckableType
                        .test(type));
            }).map(
            function (i, elem) {
                var $me = $(this), val = $me.val();
                if (!val) {
                    return false;
                }
                var dataType = $me.attr("data-type") || "String";
                if (dataType == "array") {
                    val = "(" + val + ")";
                } else {
                    val = "'" + val + "'";
                    if ("String" != dataType) {
                        val = dataType + val;
                    }
                }

                var operator = $me.attr("operator") || "eq",
                    dataRole = $me.attr("data-role") || ""; // data-role="datepicker"
                if (dataRole == "datepicker") {
                    val = "date" + val;
                } else if (dataRole == "datetimepicker") {
                    val = "datetime" + val;
                }

                return operator == "like" ? ("substringof(" + val + ", " + elem.name + ")") : (elem.name + " " + operator + " " + val);
            }).get().join(" and ");
    }// E_封装filer的参数

    // S_获取项目待办总数
    function getTaskCount(options) {
        options.$http({
            method: 'get',
            url: rootPath + '/admin/myCountInfo'
        }).then(function (response) {
            if (response.data.DO_SIGN_COUNT) {
                $('#DO_SIGN_COUNT').html(htmlEscape(response.data.DO_SIGN_COUNT));
            }
            if (response.data.DO_TASK_COUNT) {
                $('#DO_TASK_COUNT').html(htmlEscape(response.data.DO_TASK_COUNT));
            }
            if (response.data.GET_SIGN_COUNT) {
                $('#GET_SIGN_COUNT').html(htmlEscape(response.data.GET_SIGN_COUNT));
            }
            if (response.data.GET_RESERVESIGN_COUNT) {
                $('#GET_RESERVESIGN_COUNT').html(htmlEscape(response.data.GET_RESERVESIGN_COUNT));
            }
        });
    }// E_获取待办总数

    function uuid() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0,
                v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16)
        }).toUpperCase()
    }

    //init
    init();
    function init() {
        //begin#grid 处理
        //全选
        $(document).on('click', '#checkboxAll', function () {
            var isSelected = $(this).is(':checked');
            $('.grid').find('tr td:nth-child(1)').find('input:checkbox[ng-disabled!="true"]').prop('checked', isSelected);
        });
    }

})();