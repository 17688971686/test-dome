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
        //initDictData: initDictData,                 // 初始化数字字典
        kendoGridDataSource: kendoGridDataSource,   // 获取gridDataSource
        getTaskCount: getTaskCount,                 // 用户待办总数
        uuid : uuid,                                // js
        downloadReport : downloadReport,            //报表下载
    };
    window.common = service;

    /**
     *
     * @param data 数据
     * @param fileName 文件名
     * @param fileType 文件类型
     * 使用{type: "application/vnd.ms-excel"}的写法，可以保存为xls格式的excel文件
     * 而使用“application/vnd.openxmlformats-officedocument.spreadsheetml.sheet”则会保存为xlsx
     */
    function downloadReport(data , fileName , fileType){
        var blob = new Blob([data] , {type : "application/" + fileType});
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

    function kendoGridDataSource(url, searchForm) {
        var dataSource = new kendo.data.DataSource({
            type: 'odata',
            transport: kendoGridConfig().transport(url, searchForm),
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
            pageSize: 10,
            sort: {
                field: "createdDate",
                dir: "desc"
            }
        });
        return dataSource;
    }//end

    function kendoGridConfig() {
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
                pageSizes: true
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
            transport: function (url, form, paramObj) {
                return {
                    read: {
                        url: url,
                        dataType: "json",
                        type: "post",
                        cache : false ,
                        beforeSend: function (req) {
                            req.setRequestHeader('Token', service.getToken());
                        },
                        data: function () {
                            if (form) {
                                var filterParam = common.buildOdataFilter(form);
                                if (filterParam) {
                                    if (paramObj && paramObj.filter) {
                                        return {
                                            "$filter": filterParam + " and "
                                            + paramObj.filter
                                        };
                                    } else {
                                        return {
                                            "$filter": filterParam
                                        };
                                    }
                                } else {
                                    if (paramObj && paramObj.filter) {
                                        return {
                                            "$filter": paramObj.filter
                                        };
                                    } else {
                                        return {};
                                    }
                                }
                            } else {
                                return {};
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
                val = "'" + val + "'";
                if ("String" != dataType) {
                    val = dataType + val;
                }
                var operator = $me.attr("operator") || "eq",
                    dataRole = $me.attr("data-role") || ""; // data-role="datepicker"
                if (dataRole == "datepicker") {
                    val = "date" + val;
                } else if (dataRole == "datetimepicker") {
                    val = "datetime" + val;
                }

                return operator == "like" ? ("substringof(" + val + ", "
                + elem.name + ")") : (elem.name + " " + operator
                + " " + val);
            }).get().join(" and ");
    }// E_封装filer的参数

    /*function initDictData(options) {
        if (!DICT_ITEMS) {
            options.$http({
                method: 'get',
                url: rootPath + '/dict/dictItems'
            }).then(function (response) {
                options.scope.dictMetaData = response.data;
                var dictsObj = {};
                reduceDict(dictsObj, response.data);
                options.scope.DICT = dictsObj;
            }, function (response) {
                alert('初始化数据字典失败');
            });
        } else {
            options.scope.dictMetaData = DICT_ITEMS;
            var dictsObj = {};
            reduceDict(dictsObj, options.scope.dictMetaData);
            options.scope.DICT = dictsObj;
        }
    }//end*/

    /*function reduceDict(dictsObj, dicts, parentId) {
        if (!dicts || dicts.length == 0) {
            return;
        }
        if (!parentId) {
            for (var i = 0; i < dicts.length; i++) {
                var dict = dicts[i];
                if (!dict.parentId) {
                    dictsObj[dict.dictCode] = {};
                    dictsObj[dict.dictCode].dictId = dict.dictId;
                    dictsObj[dict.dictCode].dictCode = dict.dictCode;
                    dictsObj[dict.dictCode].dictName = dict.dictName;
                    dictsObj[dict.dictCode].dictKey = dict.dictKey;
                    dictsObj[dict.dictCode].dictSort = dict.dictSort;
                    reduceDict(dictsObj[dict.dictCode], dicts, dict.dictId);
                }
            }
        } else {
            for (var i = 0; i < dicts.length; i++) {
                var dict = dicts[i];
                if (dict.parentId && dict.parentId == parentId) {
                    if (!dictsObj.dicts) {
                        dictsObj.dicts = new Array();
                    }
                    var subDict = {};
                    subDict.dictId = dict.dictId;
                    subDict.dictName = dict.dictName;
                    subDict.dictCode = dict.dictCode;
                    subDict.dictKey = dict.dictKey;
                    subDict.dictSort = dict.dictSort;
                    dictsObj.dicts.push(subDict);
                    reduceDict(subDict, dicts, dict.dictId);
                }
            }
        }
    }//end*/

    // S_获取项目待办总数
    function getTaskCount(options) {
        options.$http({
            method: 'get',
            url: rootPath + '/admin/myCountInfo'
        }).then(function (response) {
            if(response.data.DO_SIGN_COUNT){
                $('#DO_SIGN_COUNT').html(response.data.DO_SIGN_COUNT);
            }
            if(response.data.DO_TASK_COUNT){
                $('#DO_TASK_COUNT').html(response.data.DO_TASK_COUNT);
            }
        });
    }// E_获取待办总数

    function uuid(){
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,function(c){
            var r=Math.random()*16|0,
                v=c=='x'?r:(r&0x3|0x8);
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