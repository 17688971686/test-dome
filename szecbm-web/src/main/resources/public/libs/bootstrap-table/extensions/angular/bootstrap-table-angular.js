// JavaScript source code
(function () {
    if (typeof angular === 'undefined') {
        return;
    }
    angular.module('bsTable', [])
        .constant('uiBsTables', {bsTables: {}})
        .directive('bsTableControl', ['uiBsTables', "$compile", "$timeout", function (uiBsTables, $compile, $timeout) {
            var CONTAINER_SELECTOR = '.bootstrap-table';
            var SCROLLABLE_SELECTOR = '.fixed-table-body';
            var SEARCH_SELECTOR = '.search input';
            var bsTables = uiBsTables.bsTables;

            function getBsTable(el) {
                var result;
                $.each(bsTables, function (id, bsTable) {
                    if (!bsTable.$el.closest(CONTAINER_SELECTOR).has(el).length) return;
                    result = bsTable;
                    return true;
                });
                return result;
            }

            $(window).resize(function () {
                $.each(bsTables, function (id, bsTable) {
                    bsTable.$el.bootstrapTable('resetView');
                });
            });
            function onScroll() {
                var bsTable = this;
                var state = bsTable.$s.bsTableControl.state;
                bsTable.$s.$apply(function () {
                    state.scroll = bsTable.$el.bootstrapTable('getScrollPosition');
                });
            }

            $(document)
                .on('post-header.bs.table', CONTAINER_SELECTOR + ' table', function (evt) { // bootstrap-table calls .off('scroll') in initHeader so reattach here
                    var bsTable = getBsTable(evt.target);
                    if (!bsTable) return;
                    bsTable.$el
                        .closest(CONTAINER_SELECTOR)
                        .find(SCROLLABLE_SELECTOR)
                        .on('scroll', onScroll.bind(bsTable));
                })
                .on('sort.bs.table', CONTAINER_SELECTOR + ' table', function (evt, sortName, sortOrder) {
                    var bsTable = getBsTable(evt.target);
                    if (!bsTable) return;
                    var state = bsTable.$s.bsTableControl.state;
                    bsTable.$s.$apply(function () {
                        state.sortName = sortName;
                        state.sortOrder = sortOrder;
                    });
                })
                .on('page-change.bs.table', CONTAINER_SELECTOR + ' table', function (evt, pageNumber, pageSize) {
                    var bsTable = getBsTable(evt.target);
                    if (!bsTable) return;
                    var state = bsTable.$s.bsTableControl.state;
                    bsTable.$s.$apply(function () {
                        state.pageNumber = pageNumber;
                        state.pageSize = pageSize;
                    });
                })
                .on('search.bs.table', CONTAINER_SELECTOR + ' table', function (evt, searchText) {
                    var bsTable = getBsTable(evt.target);
                    if (!bsTable) return;
                    var state = bsTable.$s.bsTableControl.state;
                    bsTable.$s.$apply(function () {
                        state.searchText = searchText;
                    });
                })
                .on('focus blur', CONTAINER_SELECTOR + ' ' + SEARCH_SELECTOR, function (evt) {
                    var bsTable = getBsTable(evt.target);
                    if (!bsTable) return;
                    var state = bsTable.$s.bsTableControl.state;
                    bsTable.$s.$apply(function () {
                        state.searchHasFocus = $(evt.target).is(':focus');
                    });
                });

            var BootstrapTable = $.fn.bootstrapTable.Constructor,
                _initColumn = BootstrapTable.prototype.initColumn;

            // TODO bootstrap-table-filter-odata2.js  允许表头过滤条件插件支持angular的语法和标签
            // $.fn.bootstrapTable.Constructor.prototype.createHeader = function (html) {
            //     if (html) {
            //         var h = this.$header;
            //         var me = this.$el;
            //         $timeout(function () {
            //             var scope = me.scope().$new();
            //             scope.$apply(function () {
            //                 h.html($compile(html)(scope));
            //             })
            //         })
            //     }
            // };
            // TODO bootstrap-table-filter-odata.js 允许表头过滤条件插件支持angular的语法和标签
            BootstrapTable.prototype.appendControls = function (cell, column, html, initAfter) {
                if (html) {
                    var that = this,
                        header = that.$header
                    meEl = that.$el;
                    $timeout(function () {
                        var scope = meEl.scope().$new();
                        scope.column = column;
                        scope.$apply(function () {
                            cell.append($compile(html)(scope));
                        })
                        if ($.isFunction(initAfter)) {
                            initAfter(header, column);
                        }
                    })
                }
            };

            // 允许column的formatter支持angular的语法和标签
            BootstrapTable.prototype.initRowAfter = function (trFragments, tr, row, index) {
                if (tr && tr !== true) {
                    var scope = this.$el.scope();
                    if (scope) {
                        scope = scope.$new();
                        scope.row = row;
                        scope.rowIndex = index;
                        scope.$apply(function () {
                            trFragments.append($compile(tr)(scope));
                        })
                    }
                }
            };

            BootstrapTable.prototype.initColumn = function (column) {
                // 添加 bsFormatter 的支持，避免提早的执行了angular的解析
                if (column.bsFormatter && !column.formatter) {
                    column.formatter = column.bsFormatter.replace(/\{/g, "{{").replace(/\}/g, "}}");
                }
                return $.extend({}, _initColumn.apply(this, Array.prototype.slice.apply(arguments)), column);
            }

            return {
                restrict: 'EA',
                scope: {bsTableControl: '='},
                link: function ($s, $el) {
                    bsTables[$s.$id] = {$s: $s, $el: $el};
                    $s.instantiated = false;

                    $s.$watch('bsTableControl.options', function (options) {
                        if (!options) options = $s.bsTableControl.options = {};
                        var state = $s.bsTableControl.state || {};

                        if ($s.instantiated) $el.bootstrapTable('destroy');
                        $el.bootstrapTable(angular.extend(angular.copy(options), state));
                        $s.instantiated = true;

                        /**
                         * 添加getSelections方法
                         * 返回所选的行，当没有选择任何行的时候返回一个空数组。
                         */
                        $s.bsTableControl.getSelections = function () {
                            return $el.bootstrapTable('getSelections');
                        }
                        /**
                         * 添加getAllSelections方法
                         * 返回所有选择的行，包括搜索过滤前的，当没有选择任何行的时候返回一个空数组。
                         */
                        $s.bsTableControl.getAllSelections = function () {
                            return $el.bootstrapTable('getAllSelections');
                        }
                        /**
                         * 添加load方法
                         * 加载数据到表格中，旧数据会被替换。
                         * @param data
                         */
                        $s.bsTableControl.load = function (data) {
                            $el.bootstrapTable('load', data);
                        }
                        /**
                         * 添加reload方法
                         * 刷新表格数据
                         * @param params
                         */
                        $s.bsTableControl.reload = function (params) {
                            $el.bootstrapTable("refresh", params);
                        }
                        $s.bsTableControl.refresh = function (params) {
                            $el.bootstrapTable("refresh", params);
                        }
                        $s.bsTableControl.expandRow = function (index) {
                            $el.bootstrapTable("expandRow", index);
                        }
                        $s.bsTableControl.collapseRow = function (index) {
                            $el.bootstrapTable("collapseRow", index);
                        }

                        // Update the UI for state that isn't settable via options
                        if ('scroll' in state) $el.bootstrapTable('scrollTo', state.scroll);
                        if ('searchHasFocus' in state) $el.closest(CONTAINER_SELECTOR).find(SEARCH_SELECTOR).focus(); // $el gets detached so have to recompute whole chain
                    }, true);

                    $s.$watch('bsTableControl.state', function (state) {
                        if (!state) state = $s.bsTableControl.state = {};
                        $el.trigger('directive-updated.bs.table', [state]);
                    }, true);

                    $s.$on('$destroy', function () {
                        bsTables[$s.$id] = null;
                        delete bsTables[$s.$id];
                    });

                }
            };
        }])

})();
