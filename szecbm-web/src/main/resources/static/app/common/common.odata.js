/**
 * 参照kendo.data.odata.js，主要处理中文乱码参数的问题
 * 源码地址：https://github.com/telerik/kendo-ui-core/blob/f68dbc2b3a4b51958c77be30234a44b39b9efbc1/src/kendo.data.odata.js
 * var params = {
 *  filter: { field: "name", operator: "contains", value: "测试" },
 *  sort: { field: "itemOrder", dir: "desc" },
 *  take: 10
 * };
 * $.toOdata(params);
 */
(function ($, undefined) {
    if (typeof jQuery === 'undefined') {
        return;
    }

    var odataFilters = {
            eq: "eq",
            neq: "ne",
            gt: "gt",
            gte: "ge",
            lt: "lt",
            lte: "le",
            ni:"ni",
            in: "in",
            like: "substringof",
            nolike: "substringof",
            contains: "substringof",
            doesnotcontain: "substringof",
            endswith: "endswith",
            startswith: "startswith",
            isnull: "eq",
            isnotnull: "ne",
            isempty: "eq",
            isnotempty: "ne"
        },
        mappers = {
            pageSize: $.noop,
            page: $.noop,
            filter: function (params, filter, useVersionFour) {
                if (filter) {
                    filter = toOdataFilter(filter, useVersionFour);
                    if (filter) {
                        params.$filter = filter;
                    }
                }
            },
            sort: function (params, orderby) {
                if (!$.isArray(orderby)) {
                    orderby = [orderby];
                }
                var expr = $.map(orderby, function (value) {
                    var order = value.field.replace(/\./g, "/");

                    if (value.dir === "desc") {
                        order += " desc";
                    }

                    return order;
                }).join(",");

                if (expr) {
                    params.$orderby = expr;
                }
            },
            skip: function (params, skip) {
                if (skip) {
                    params.$skip = skip;
                }
            },
            take: function (params, take) {
                if (take) {
                    params.$top = take;
                }
            }
        };

    function toOdataFilter(filter, useOdataFour) {
        var result = [],
            logic = filter.logic || "and",
            idx,
            length,
            field,
            type,
            format,
            operator,
            value,
            ignoreCase,
            filters;

        if (filter.field) {
            filters = [filter];
        } else {
            filters = filter.filters || [];
        }

        for (idx = 0, length = filters.length; idx < length; idx++) {
            filter = filters[idx];
            field = filter.field;
            value = filter.value;
            operator = filter.operator;

            if (filter.filters) {
                filter = toOdataFilter(filter, useOdataFour);
            } else {
                ignoreCase = filter.ignoreCase;
                field = field.replace(/\./g, "/");
                filter = odataFilters[operator];
                if (useOdataFour) {
                    filter = odataFilters[operator];
                }

                if (operator === "isnull" || operator === "isnotnull") {
                    filter = util.format("{0} {1} null", field, filter);
                } else if (operator === "isempty" || operator === "isnotempty") {
                    filter = util.format("{0} {1} ''", field, filter);
                } else if (filter && value !== undefined) {
                    type = $.type(value);
                    if (type === "string") {
                        format = "'{1}'";
                        value = value.replace(/'/g, "''");

                        if (ignoreCase === true) {
                            field = "tolower(" + field + ")";
                        }
                        value = encodeURIComponent(value);

                    } else if (type === "date") {
                        format = "datetime'{1:yyyy-MM-dd HH:mm:ss}'";
                    } else {
                        format = "{1}";
                    }

                    if (filter.length > 3) {
                        if (filter !== "substringof") {
                            format = "{0}({2}," + format + ")";
                        } else {
                            format = "{0}(" + format + ",{2})";
                            if (operator === "doesnotcontain") {
                                if (useOdataFour) {
                                    format = "{0}({2},'{1}') eq -1";
                                    filter = "indexof";
                                } else {
                                    format += " eq false";
                                }
                            }
                        }
                    } else {
                        format = "{2} {0} " + format;
                    }

                    filter = util.format(format, filter, value, field);
                }
            }

            filter && result.push(filter);
        }

        filter = result.join(" " + logic + " ");

        if (result.length > 1) {
            filter = "(" + filter + ")";
        }

        return filter;
    }

    $.extend({
        toOdata: function (options) {
            options = options || {};

            var params = {
                $inlinecount: "allpages"
            };

            for (var option in options) {
                if (mappers[option]) {
                    mappers[option](params, options[option], true);
                } else {
                    params[option] = options[option];
                }
            }
            return params;
        },
        toOdataFilter: function (filters) {
            return toOdataFilter(filters, true)
        }
    })

})(jQuery);