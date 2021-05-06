/*生成options*/
function genTableOptions() {
    return {
        ajaxOptions: {headers: ajaxHeaders()},                              //自定义ajax请求参数
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
        //自定义工具栏图标
        icons: {
            refresh: "glyphicon-search",
            toggleOn: "glyphicon-list-alt",
            toggleOff: "glyphicon-list-alt",
            fullscreen: "glyphicon-fullscreen",
            columns: "glyphicon-th icon-th",

        },
        //表头样式
        headerStyle: function (column) {
            return {
                css: {
                    'background': '#6ecadc',
                    "color": "white",
                    'border': '0px'
                }
            }
        },
        //行样式
        rowStyle: rowStyle,
        //接口返回数据处理
        responseHandler: function (res) {
            return {
                "rows": res.data.records,
                "total": res.data.total
            };
        },
        //表格渲染前
        onPreBody: function () {
            if (this.xtipLoadId == null) {
                this.xtipLoadId = xtip.load()
            }
        },
        //表格渲染后
        onPostBody: function () {
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
        //加载中信息
        formatLoadingMessage: function () {
            return '';
        },
        //无内容信息
        formatNoMatches: function () {
            return '<span style="color: #c9c9c9">&lt;无数据&gt;</span>';
        },
        //查询参数
        queryParams: queryParams

    }
}

/*行样式*/
function rowStyle(column) {
    return {
        css: {
            'border-left': '0px',
            'border-right': '0px',
            'border-top': '0px'
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
function pushParamTableOptions(tableOptions,
                               method,
                               url,
                               columns,
                               uniqueId,
                               $toolBar,
                               $table,
                               $singleBtnArray,
                               $multiBtnArray,
                               $queryInputArray) {
    //注册查询框回车查询
    for (let i = 0; i < $queryInputArray.length; i++) {
        $queryInputArray[i].keydown(function (event) {
            const code = event.keyCode;
            //按下回车时
            if (code === 13) {
                tableOptions.tableSelect.bootstrapTable('refresh');
            }
        })
    }

    return $.extend(tableOptions, {
        method: method,
        url: url,
        columns: columns,
        uniqueId: uniqueId,
        toolbar: $toolBar,
        tableSelect: $table,
        //复选框选择时
        onCheck: function () {
            const checkIdList = $table.bootstrapTable('getAllSelections');
            //多选
            for (let i = 0; i < $multiBtnArray.length; i++) {
                $multiBtnArray[i].removeClass("disabled");
                $multiBtnArray[i].removeClass("button-disabled");
            }
            //单选
            for (let i = 0; i < $singleBtnArray.length; i++) {
                $singleBtnArray[i].removeClass("disabled");
                $singleBtnArray[i].removeClass("button-disabled");
                if (checkIdList.length !== 1) {
                    $singleBtnArray[i].addClass("disabled");
                    $singleBtnArray[i].addClass("button-disabled");
                }
            }
        },
        //复选框取消选择时
        onUncheck: function () {
            const checkIdList = $table.bootstrapTable('getAllSelections');
            if (checkIdList.length <= 0) {
                //多选
                for (let i = 0; i < $multiBtnArray.length; i++) {
                    $multiBtnArray[i].addClass("disabled");
                    $multiBtnArray[i].addClass("button-disabled");
                }
            }
            //单选
            for (let i = 0; i < $singleBtnArray.length; i++) {
                $singleBtnArray[i].addClass("disabled");
                $singleBtnArray[i].addClass("button-disabled");
                if (checkIdList.length === 1) {
                    $singleBtnArray[i].removeClass("disabled");
                    $singleBtnArray[i].removeClass("button-disabled");
                }
            }
        },
        //全选时
        onCheckAll: function () {
            const checkIdList = $table.bootstrapTable('getAllSelections');
            //单选
            for (let i = 0; i < $singleBtnArray.length; i++) {
                if (checkIdList.length !== 1) {
                    $singleBtnArray[i].addClass("disabled");
                    $singleBtnArray[i].addClass("button-disabled");
                } else {
                    $singleBtnArray[i].removeClass("disabled");
                    $singleBtnArray[i].removeClass("button-disabled");
                }
            }
            //多选
            for (let i = 0; i < $multiBtnArray.length; i++) {
                $multiBtnArray[i].removeClass("disabled");
                $multiBtnArray[i].removeClass("button-disabled");
            }
        },
        //取消全选时
        onUncheckAll: function () {
            //单选
            for (let i = 0; i < $singleBtnArray.length; i++) {
                $singleBtnArray[i].addClass("disabled");
                $singleBtnArray[i].addClass("button-disabled");
            }
            //多选
            for (let i = 0; i < $multiBtnArray.length; i++) {
                $multiBtnArray[i].addClass("disabled");
                $multiBtnArray[i].addClass("button-disabled");
            }
        },
    });
}

/*生成行内操作按钮*/
function genTableOptionA(color, text, icon, onclick) {
    return '' +
        '<a class="table-options" style="color: ' + color + '" ' +
        'onclick="' + onclick + '">' +
        '   <i class="glyphicon ' + icon + '"></i>' + text + ' ' +
        '</a>'
}