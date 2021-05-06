/*生成options*/
function genTableOptions() {
    return {
        ajaxOptions: {headers: ajaxHeaders()},                              //自定义ajax请求参数
        showToggle: true,                                                   //是否启用卡片视图切换按钮
        showColumns: true,                                                  //是否启用列筛选按钮
        showColumnsToggleAll: true,                                         //是否期用列筛选切换所有
        showRefresh: true,                                                  //是否期用刷新按钮
        showFullscreen: true,                                               //是否启用全屏按钮
        cache: false,                                                       //是否启用缓存
        pagination: true,                                                   //是否分页
        sidePagination: "server",                                           //分页方式：client客户端分页，server服务端分页
        pageNumber: 1,                                                      //分页默认第一页
        pageList: [10, 25, 50, 100],                                        //可供选择的每页的行数
        queryParamsType: "",                                                //查询条件类型，留空为pageNumber,pageSize,sortName,sortOrder
        clickToSelect: true,                                                //是否启用点击选中行
        multipleSelectRow: true,                                            //是否启用shift/ctrl选中行
        buttonsPrefix: 'btn btn-sm btn-info toolbarBtn',                    //所有按钮class前缀
        undefinedText: '<span style="color: #c9c9c9">&lt;null&gt;</span>',  //数据为空时的占位符
        icons: {
            refresh: "glyphicon-search",
            toggleOn: "glyphicon-list-alt",
            toggleOff: "glyphicon-list-alt",
            fullscreen: "glyphicon-fullscreen",
            columns: "glyphicon-th icon-th",

        },
        headerStyle: function (column) {                         //表头样式
            return {
                css: {
                    'background': '#6ecadc',
                    "color": "white",
                    'border': '0px'
                }
            }
        },
        rowStyle: rowStyle,
        responseHandler: function (res) {                     //接口返回数据处理
            return {
                "rows": res.data.records,
                "total": res.data.total
            };
        },
        onPreBody: function () {
            if (this.xtipLoadId == null) {
                this.xtipLoadId = xtip.load()
            }
        },
        onPostBody: function () {                                 //列表渲染前执行的方法
            this.tableSelect.find("input:checkbox").each(function (i) {
                var $check = $(this);
                if ($check.attr("id") && $check.next("label")) {
                    return;
                }
                $check.next().remove();
                var name = $check.attr("name");
                var id = name + "-" + i;
                var $label = $('<label for="' + id + '"></label>');
                $check.attr("id", id).parent().addClass("checkbox-custom").append($label);
            });

            //把刷新改成查询
            $("button[title='刷新']").attr("title", "查询");
            xtip.close(this.xtipLoadId)

        },
        formatLoadingMessage: function () {
            return '';
        },
        formatNoMatches: function () {
            return '暂无数据';
        },
        queryParams: queryParams

    }
}

/*行样式*/
function rowStyle(column) {
    return {
        css: {
            'border-left': '0px',
            'border-right': '0px'
        }
    }
}

/*请求参数*/
function queryParams(params) {
    return {
        pageNumber: params.pageNumber,
        pageSize: params.pageSize,
        sortName: params.sortName,
        sortOrder: params.sortOrder,
    };
}

/*补全表格参数*/
function pushParamTableOptions(tableOptions, method, url, columns, uniqueId, toolBarSelect, tableSelect) {
    tableOptions.method = method;
    tableOptions.url = url;
    tableOptions.columns = columns;
    tableOptions.uniqueId = uniqueId;
    tableOptions.toolbar = toolBarSelect;
    tableOptions.tableSelect = tableSelect;
}

