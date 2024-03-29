/**
 * bootstrap table的表头过滤插件（针对odata的过滤参数）
 *  @author tzg
 *  @version: v0.1
 */
(function ($) {

    'use strict';

    var sprintf = $.fn.bootstrapTable.utils.sprintf;

    var getOptionsFromSelectControl = function (selectControl) {
        return selectControl.get(selectControl.length - 1).options;
    };

    var hideUnusedSelectOptions = function (selectControl, uniqueValues) {
        var options = getOptionsFromSelectControl(selectControl);

        for (var i = 0; i < options.length; i++) {
            if (options[i].value !== "") {
                if (!uniqueValues.hasOwnProperty(options[i].value)) {
                    selectControl.find(sprintf("option[value='%s']", options[i].value)).hide();
                } else {
                    selectControl.find(sprintf("option[value='%s']", options[i].value)).show();
                }
            }
        }
    };

    var addOptionToSelectControl = function (selectControl, value, text) {
        value = $.trim(value);
        selectControl = $(selectControl.get(selectControl.length - 1));
        if (!existOptionInSelectControl(selectControl, value)) {
            selectControl.append($("<option></option>")
                .attr("value", value)
                .text($('<div />').html(text).text()));
        }
    };

    var sortSelectControl = function (selectControl) {
        var $opts = selectControl.find('option:gt(0)');
        $opts.sort(function (a, b) {
            a = $(a).text().toLowerCase();
            b = $(b).text().toLowerCase();
            if ($.isNumeric(a) && $.isNumeric(b)) {
                // Convert numerical values from string to float.
                a = parseFloat(a);
                b = parseFloat(b);
            }
            return a > b ? 1 : a < b ? -1 : 0;
        });

        selectControl.find('option:gt(0)').remove();
        selectControl.append($opts);
    };

    var existOptionInSelectControl = function (selectControl, value) {
        var options = getOptionsFromSelectControl(selectControl);
        for (var i = 0; i < options.length; i++) {
            if (options[i].value === value.toString()) {
                //The value is not valid to add
                return true;
            }
        }

        //If we get here, the value is valid to add
        return false;
    };

    var getCurrentHeader = function (that) {
        var header = that.$header;
        if (that.options.height) {
            header = that.$tableHeader;
        }

        return header;
    };

    var getCurrentSearchControls = function (that) {
        var searchControls = 'select, input';
        if (that.options.height) {
            searchControls = 'table select, table input';
        }

        return searchControls;
    };

    var getCursorPosition = function (el) {
        if ($.fn.bootstrapTable.utils.isIEBrowser()) {
            if ($(el).is('input')) {
                var pos = 0;
                // if ('selectionStart' in el) {
                //     pos = el.selectionStart;
                // } else
                if ('selection' in document) {
                    el.focus();
                    var Sel = document.selection.createRange();
                    var SelLength = Sel.text.length;
                    Sel.moveStart('character', -el.value.length);
                    pos = Sel.text.length - SelLength;
                }
                return pos;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    };

    var setCursorPosition = function (el, index) {
        if ($.fn.bootstrapTable.utils.isIEBrowser()) {
            // if (el.setSelectionRange !== undefined) {
            //     el.setSelectionRange(index, index);
            // } else {
            $(el).val(el.value);
            // }
        }
    };

    var copyValues = function (that) {
        var header = getCurrentHeader(that),
            searchControls = getCurrentSearchControls(that);

        that.options.valuesFilterControl = [];

        header.find(searchControls).each(function () {
            that.options.valuesFilterControl.push(
                {
                    field: $(this).closest('[data-field]').data('field'),
                    value: $(this).val(),
                    position: getCursorPosition($(this).get(0))
                });
        });
    };

    var setValues = function (that) {
        var field = null,
            result = [],
            header = getCurrentHeader(that),
            searchControls = getCurrentSearchControls(that);

        if (that.options.valuesFilterControl.length > 0) {
            header.find(searchControls).each(function (index, ele) {
                field = $(this).closest('[data-field]').data('field');
                result = $.grep(that.options.valuesFilterControl, function (valueObj) {
                    return valueObj.field === field;
                });

                if (result.length > 0) {
                    $(this).val(result[0].value);
                    setCursorPosition($(this).get(0), result[0].position);
                }
            });
        }
    };

    var collectBootstrapCookies = function cookiesRegex() {
        var cookies = [],
            foundCookies = document.cookie.match(/(?:bs.table.)(\w*)/g);

        if (foundCookies) {
            $.each(foundCookies, function (i, cookie) {
                if (/./.test(cookie)) {
                    cookie = cookie.split(".").pop();
                }

                if ($.inArray(cookie, cookies) === -1) {
                    cookies.push(cookie);
                }
            });
            return cookies;
        }
    };

    var getDirectionOfSelectOptions = function (alignment) {
        alignment = alignment === undefined ? 'left' : alignment.toLowerCase();

        switch (alignment) {
            case 'left':
                return 'ltr';
            case 'right':
                return 'rtl';
            case 'auto':
                return 'auto';
            default:
                return 'ltr';
        }
    };

    var initFilterSelectControls = function (that) {
        var data = that.data,
            itemsPerPage = that.pageTo < that.options.data.length ? that.options.data.length : that.pageTo,

            isColumnSearchableViaSelect = function (column) {
                return column.filterControl && typeof column.filterControl == 'string'
                    && column.filterControl.toLowerCase() === 'select' && column.searchable;
            },

            isFilterDataNotGiven = function (column) {
                var filterData = column.filterData;
                return filterData === undefined || (typeof filterData == 'string' && filterData.toLowerCase() === 'column');
            },

            hasSelectControlElement = function (selectControl) {
                return selectControl && selectControl.length > 0;
            };

        var z = that.options.pagination ?
            (that.options.sidePagination === 'server' ? that.pageTo : that.options.totalRows) :
            that.pageTo;

        $.each(that.header.fields, function (j, field) {
            var column = that.columns[$.fn.bootstrapTable.utils.getFieldIndex(that.columns, field)],
                selectControl = $('.bootstrap-table-filter-control-' + escapeID(column.field));

            if (isColumnSearchableViaSelect(column) && isFilterDataNotGiven(column) && hasSelectControlElement(selectControl)) {
                if (selectControl.get(selectControl.length - 1).options.length === 0) {
                    //Added the default option
                    addOptionToSelectControl(selectControl, '', '');
                }

                var uniqueValues = {};
                for (var i = 0; i < z; i++) {
                    //Added a new value
                    var fieldValue = data[i][field],
                        formattedValue = $.fn.bootstrapTable.utils.calculateObjectValue(that.header, that.header.formatters[j], [fieldValue, data[i], i], fieldValue);

                    uniqueValues[formattedValue] = fieldValue;
                }

                for (var key in uniqueValues) {
                    addOptionToSelectControl(selectControl, uniqueValues[key], key);
                }

                sortSelectControl(selectControl);

                if (that.options.hideUnusedSelectOptions) {
                    hideUnusedSelectOptions(selectControl, uniqueValues);
                }
            }
        });
    };

    var escapeID = function (id) {
        return String(id).replace(/(:|\.|\[|\]|,)/g, "\\$1");
    };


    $.fn.bootstrapTable.FILTER_OPERATOR = {
        eq: "等于",
        neq: "不等于",
        gt: "大于",
        gte: "大于等于",
        lt: "小于",
        lte: "小于等于",
        like: "包含",
        nolike: "不包含",
        contains: "包含",
        doesnotcontain: "不包含",
        endswith: "从……结束于",
        startswith: "从……开始"
    };

    $.extend($.fn.bootstrapTable.defaults, {
        filterControl: false,
        onColumnSearch: function (field, text) {
            return false;
        },
        defaultSort: null,
        defaultFilters: null,
        queryParams: function (params) {
            var me = this,
                filters = params.filter,
                _params = {
                    "$skip": params.offset,
                    "$top": params.limit,
                    "$orderby": !params.sort ? me.defaultSort : (params.sort + " " + params.order),
                    "$filter": filters.length == 0 ? "" : $.toOdataFilter({
                        logic: "and",
                        filters: filters
                    })
                };
            if (me.pagination) {
                _params["$inlinecount"] = "allpages";
            }
            return _params;
        },
        filterShowClear: false,
        alignmentSelectControlOptions: undefined,
        filterTemplate: {
            input: function (that, field, isVisible, operator, placeholder) {
                operator = operator || "eq";
                placeholder = placeholder || $.fn.bootstrapTable.FILTER_OPERATOR[operator] || "";
                return sprintf('<input type="text" class="form-control bootstrap-table-filter-control-%s" name="%s" style="width: 100%; visibility: %s" operator="%s" placeholder="%s">',
                    field, field, isVisible, operator, placeholder);
            },
            number: {
                init: function (that, field, isVisible, operator, placeholder) {
                    operator = operator || "eq";
                    placeholder = placeholder || $.fn.bootstrapTable.FILTER_OPERATOR[operator] || "";
                    return sprintf('<input type="text" class="form-control number-input bootstrap-table-filter-control-%s" name="%s" style="width: 100%; visibility: %s" operator="%s" placeholder="%s">',
                        field, field, isVisible, operator, placeholder);
                },
                initAfter: function (header, column) {
                    header.find('.number-input.bootstrap-table-filter-control-' + column.field).on("keyup paste", function (e) {
                        this.value = this.value.replace(/\D|^0/g, '');
                    }).css("ime-mode", "disabled");
                }
            },
            select: function (that, field, isVisible) {
                return sprintf('<select class="form-control select-filter-control bootstrap-table-filter-control-%s" name="%s" style="width: 100%; visibility: %s" dir="%s"></select>',
                    field, field, isVisible, getDirectionOfSelectOptions(that.options.alignmentSelectControlOptions));
            },
            datepicker: {
                init: function (that, field, isVisible, operator, placeholder) {
                    operator = operator || "eq";
                    placeholder = placeholder || $.fn.bootstrapTable.FILTER_OPERATOR[operator] || "";
                    return sprintf('<div class="input-group date date-filter-control bootstrap-table-filter-control-%s"><input type="text" class="form-control date-input" readonly name="%s" style="width: 100%; visibility: %s" operator="%s" placeholder="%s"><span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span></div>',
                        field, field, isVisible, operator, placeholder);
                },
                initAfter: function (header, column) {
                    var dateBox = header.find('.date-filter-control.bootstrap-table-filter-control-' + column.field).datetimepicker($.extend({
                        language: 'zh-CN',
                        format: 'yyyy-mm-dd',
                        minView: 2,
                        autoclose: true,
                        todayBtn: true,
                        todayHighlight: true,
                        clearBtn: true
                    }, column.filterDatepickerOptions)).on('changeDate', function (e) {
                        dateBox.children("input").keyup();
                    });
                }
            }
        },
        //internal variables
        valuesFilterControl: [],
        initCellAfterQueue: []
    });

    $.extend($.fn.bootstrapTable.COLUMN_DEFAULTS, {
        filterControl: undefined,
        filterData: undefined,
        filterDatepickerOptions: undefined,
        filterStrictSearch: false,
        filterStartsWithSearch: false,
        filterOperator: undefined,
        filterControlPlaceholder: ""
    });

    $.extend($.fn.bootstrapTable.Constructor.EVENTS, {
        'column-search.bs.table': 'onColumnSearch'
    });

    $.extend($.fn.bootstrapTable.defaults.icons, {
        clear: 'glyphicon-trash icon-clear'
    });

    $.extend($.fn.bootstrapTable.locales, {
        formatClearFilters: function () {
            return '清除查询条件';
        }
    });
    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales);

    var BootstrapTable = $.fn.bootstrapTable.Constructor,
        _initToolbar = BootstrapTable.prototype.initToolbar,
        _initBody = BootstrapTable.prototype.initBody,
        _initSearch = BootstrapTable.prototype.initSearch;

    BootstrapTable.prototype.initToolbar = function () {
        this.showToolbar = this.options.filterControl && this.options.filterShowClear;

        _initToolbar.apply(this, Array.prototype.slice.apply(arguments));

        if (this.options.filterControl && this.options.filterShowClear) {
            var $btnGroup = this.$toolbar.find('>.btn-group'),
                $btnClear = $btnGroup.find('.filter-show-clear');

            if (!$btnClear.length) {
                $btnClear = $([
                    '<button class="btn btn-default filter-show-clear" ',
                    sprintf('type="button" title="%s">', this.options.formatClearFilters()),
                    sprintf('<i class="%s %s"></i> ', this.options.iconsPrefix, this.options.icons.clear),
                    '</button>'
                ].join('')).appendTo($btnGroup);

                $btnClear.off('click').on('click', $.proxy(this.clearFilterControl, this));
            }
        }
    };

    BootstrapTable.prototype.initFhtCell = function (column) {
        var that = this;
        if (!that.options.filterControl || !column.visible) {
            return "";
        }
        var isVisible = 'hidden', html = [],
            _filterControl = column.filterControl || "";

        if (!column.field || !_filterControl) {
            html.push('<div class="no-filter-control"></div>');
        } else {
            var tag;
            if (typeof _filterControl == "string"){
                var nameControl = _filterControl.toLowerCase(),
                    _ft = that.options.filterTemplate[nameControl];
                if (column.searchable) {
                    isVisible = 'visible';
                    if (!_ft) {
                        tag = _filterControl;
                    } else if ($.isFunction(_ft)) {
                        tag = _ft(that, column.field, isVisible, column.filterOperator, column.filterControlPlaceholder);
                    } else {
                        tag = _ft.init(that, column.field, isVisible, column.filterOperator, column.filterControlPlaceholder);
                        if (_ft.initAfter) {
                            that.options.initCellAfterQueue.push([_ft.initAfter, column])
                        }
                    }
                }
            } else if ($.isFunction(_filterControl)) {
                tag = _filterControl(that, column.field);
            } else if ($.isObject(_filterControl)){
                tag = _filterControl.init(that, column.field, isVisible, column.filterOperator, column.filterControlPlaceholder);
                if (_filterControl.initAfter) {
                    that.options.initCellAfterQueue.push([_filterControl.initAfter, column])
                }
            }
            if (!tag) {
                html.push('<div class="no-filter-control"></div>');
            } else {
                html.push('<div class="filter-control">');
                html.push(tag);
                html.push("</div>");
            }
        }
        return html.join("");
    }

    BootstrapTable.prototype.initHeaderAfter = function (header) {
        var that = this, timeoutId = 0;
        header.off('keyup', 'input').on('keyup', 'input', function (event) {
            clearTimeout(timeoutId);
            timeoutId = setTimeout(function () {
                that.onColumnSearch(event);
            }, that.options.searchTimeOut);
        });

        header.off('change', 'select').on('change', 'select', function (event) {
            clearTimeout(timeoutId);
            timeoutId = setTimeout(function () {
                that.onColumnSearch(event);
            }, that.options.searchTimeOut);
        });

        header.off('mouseup', 'input').on('mouseup', 'input', function (event) {
            var $input = $(this),
                oldValue = $input.val();

            if (oldValue === "") {
                return;
            }

            setTimeout(function () {
                var newValue = $input.val();

                if (newValue === "") {
                    clearTimeout(timeoutId);
                    timeoutId = setTimeout(function () {
                        that.onColumnSearch(event);
                    }, that.options.searchTimeOut);
                }
            }, 1);
        });

        if (that.options.initCellAfterQueue) {
            $.each(that.options.initCellAfterQueue, function () {
                this[0](header, this[1]);
            })
        }
    };

    BootstrapTable.prototype.initBody = function () {
        _initBody.apply(this, Array.prototype.slice.apply(arguments));

        initFilterSelectControls(this);
    };

    BootstrapTable.prototype.initSearch = function () {
        _initSearch.apply(this, Array.prototype.slice.apply(arguments));

        if (this.options.sidePagination === 'server') {
            return;
        }

        var that = this;
        var fp = $.isEmptyObject(this.filterColumnsPartial) ? null : this.filterColumnsPartial;

        //Check partial column filter
        this.data = fp ? $.grep(this.data, function (item, i) {
            for (var key in fp) {
                var thisColumn = that.columns[$.fn.bootstrapTable.utils.getFieldIndex(that.columns, key)];
                var fval = fp[key].toLowerCase();
                var value = item[key];

                // Fix #142: search use formated data
                if (thisColumn && thisColumn.searchFormatter) {
                    value = $.fn.bootstrapTable.utils.calculateObjectValue(that.header,
                        that.header.formatters[$.inArray(key, that.header.fields)],
                        [value, item, i], value);
                }

                if (thisColumn.filterStrictSearch) {
                    if (!($.inArray(key, that.header.fields) !== -1 &&
                        (typeof value === 'string' || typeof value === 'number') &&
                        value.toString().toLowerCase() === fval.toString().toLowerCase())) {
                        return false;
                    }
                } else if (thisColumn.filterStartsWithSearch) {
                    if (!($.inArray(key, that.header.fields) !== -1 &&
                        (typeof value === 'string' || typeof value === 'number') &&
                        (value + '').toLowerCase().indexOf(fval) === 0)) {
                        return false;
                    }
                } else {
                    if (!($.inArray(key, that.header.fields) !== -1 &&
                        (typeof value === 'string' || typeof value === 'number') &&
                        (value + '').toLowerCase().indexOf(fval) !== -1)) {
                        return false;
                    }
                }
            }
            return true;
        }) : this.data;
    };

    BootstrapTable.prototype.onColumnSearch = function (event) {
        if ($.inArray(event.keyCode, [37, 38, 39, 40]) > -1) {
            return;
        }

        copyValues(this);
        var el = event.currentTarget, $el = $(el),
            operator = $el.attr("operator") || "eq",
            text = $.trim($el.val()),
            $field = el.name || $el.closest('[data-field]').data('field');

        if ($.isEmptyObject(this.filterColumnsPartial)) {
            this.filterColumnsPartial = {};
        }

        $field = sprintf("filter_%s_%s", operator, $field);
        if (text) {
            // 针对日期的处理
            if ($el.hasClass("date-input")) {
                text = new Date(text);
            } else if ($el.hasClass("number-input")) {
                text = text * 1;
                text = isNaN(text) ? 0 : text;
            }
            this.filterColumnsPartial[$field] = text;
        } else {
            delete this.filterColumnsPartial[$field];
        }

        // if the searchText is the same as the previously selected column value,
        // bootstrapTable will not try searching again (even though the selected column
        // may be different from the previous search).  As a work around
        // we're manually appending some text to bootrap's searchText field
        // to guarantee that it will perform a search again when we call this.onSearch(event)
        this.searchText += "randomText";

        this.options.pageNumber = 1;
        this.onSearch(event);
        this.trigger('column-search', $field, text);
    };

    /**
     * 初始化查询条件，针对odata的处理
     * @param params
     */
    BootstrapTable.prototype.initServerFilter = function (params) {
        var filters = this.options.defaultFilters || [];

        if (!$.isEmptyObject(filters) && !$.isArray(filters)) {
            filters = [filters];
        }

        var fc = this.filterColumnsPartial;

        if (!($.isEmptyObject(fc))) {
            for (var name in fc) {
                var val = fc[name], operator = "eq";
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
                }
                filters.push({
                    field: name,
                    operator: operator,
                    value: val
                });
            }
        }
        params.filter = filters;
    };

    BootstrapTable.prototype.clearFilterControl = function () {
        if (this.options.filterControl && this.options.filterShowClear) {
            var that = this,
                cookies = collectBootstrapCookies(),
                header = getCurrentHeader(that),
                table = header.closest('table'),
                controls = header.find(getCurrentSearchControls(that)),
                search = that.$toolbar.find('.search input'),
                timeoutId = 0;

            $.each(that.options.valuesFilterControl, function (i, item) {
                item.value = '';
            });

            setValues(that);

            // Clear each type of filter if it exists.
            // Requires the body to reload each time a type of filter is found because we never know
            // which ones are going to be present.
            if (controls.length > 0) {
                this.filterColumnsPartial = {};
                $(controls[0]).trigger(controls[0].tagName === 'INPUT' ? 'keyup' : 'change');
            } else {
                return;
            }

            if (search.length > 0) {
                that.resetSearch();
            }

            // use the default sort order if it exists. do nothing if it does not
            if (that.options.sortName !== table.data('sortName') || that.options.sortOrder !== table.data('sortOrder')) {
                var sorter = header.find(sprintf('[data-field="%s"]', $(controls[0]).closest('table').data('sortName')));
                if (sorter.length > 0) {
                    that.onSort(table.data('sortName'), table.data('sortName'));
                    $(sorter).find('.sortable').trigger('click');
                }
            }

            // clear cookies once the filters are clean
            clearTimeout(timeoutId);
            timeoutId = setTimeout(function () {
                if (cookies && cookies.length > 0) {
                    $.each(cookies, function (i, item) {
                        if (that.deleteCookie !== undefined) {
                            that.deleteCookie(item);
                        }
                    });
                }
            }, that.options.searchTimeOut);
        }
    };

})(jQuery);
