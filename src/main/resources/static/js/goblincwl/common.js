//日期格式化
Date.prototype.Format = function (fmt) {
    const o = {
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
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

//随机颜色
function randomColor() {
    let r = Math.floor(Math.random() * 256);
    let g = Math.floor(Math.random() * 256);
    let b = Math.floor(Math.random() * 256);
    if (r < 16) {
        r = "0" + r.toString(16);
    } else {
        r = r.toString(16);
    }
    if (g < 16) {
        g = "0" + g.toString(16);
    } else {
        g = g.toString(16);
    }
    if (b < 16) {
        b = "0" + b.toString(16);
    } else {
        b = b.toString(16);
    }
    return "#" + r + g + b;
}

/*自定义请求头*/
function ajaxHeaders() {
    return {
        //默认请求类型
        'Content-Type': 'application/x-www-form-urlencoded',
        // 每次请求携带token
        'Authorization': localStorage.getItem('access_token'),
    }
}

//Ajax封装
const ajaxHttp = function (options) {
    //默认值
    const defaults = {
        type: 'get',
        headers: {},
        validateForm: $(".validateForm"),
        data: {},
        dataType: 'json',
        async: true,
        cache: false,
        beforeSend: function () {
            this.loadId = xtip.load();
        },
        success: null,
        successFalse: function (res) {
            xtip.msg(res.msg, {icon: 'e', type: 'w'});
        },
        complete: function () {
            xtip.close(o.loadId);
        },
        error: function () {
            xtip.msg("前端发生未知错误，请联系站长！", {icon: 'e', type: 'w'});
        }
    };
    //合并
    const o = $.extend({}, defaults, options);
    //表单校验
    if (o.validateForm != null && o.validateForm.length > 0) {
        if (!fromValidate(o.validateForm)) {
            return;
        }
    }
    //执行Ajax
    $.ajax({
        url: o.url,
        type: o.type,
        headers: ajaxHeaders(),
        data: o.data,
        dataType: o.dataType,
        async: o.async,
        beforeSend: function () {
            o.beforeSend && o.beforeSend();
        },
        success: function (res) {
            if (res != null && res.code === 200) {
                o.success && o.success(res);
                xtip.close(o.winId);
            } else {
                o.successFalse && o.successFalse(res);
            }
        },
        complete: function () {
            o.complete && o.complete();
        },
        error: function () {
            o.error && o.error();
        }
    });
};

/*获取表格Jquery对象*/
function getTable(tableId) {
    return $("#" + tableId);
}

/*获取url参数*/
$.getUrlParam = function (name) {
    const reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    const r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}


/*将富文本替换成纯文本*/
function getSimpleText(html) {
    const re1 = new RegExp("<.+?>", "g");//匹配html标签的正则表达式，"g"是搜索匹配多个符合的内容
    //执行替换成空字符
    return html.replace(re1, ' ');
}

/*判断数据是否为Null或者undefined或者为空字符串*/
$.isEmpty = function (value) {
    //正则表达式用于判斷字符串是否全部由空格或换行符组成
    const reg = /^\s*$/;
    //返回值为true表示不是空字符串
    return (value != null && !reg.test(value))
}

/*表单校验*/
function fromValidate($from) {
    const requiredList = $from.find(".required");
    for (let i = 0; i < requiredList.length; i++) {
        const $element = $(requiredList[i]);
        if (!inputValidate($element)) {
            return false;
        }
    }
    return true;
}

/*INPUT校验*/
function inputValidate($input) {
    if ($input.is("input") || $input.is("textarea")) {
        if (!($.isEmpty($input.val()))) {
            checkFail($input, '请填写必填项');
            return false;
        } else {
            $input.css("border-color", "green")
        }
    }
    return true;
}

/*校验不通过时*/
function checkFail($element, msg) {
    $element.css("border-color", "red")
    if ($element.attr('pos') == null) {
        $element.attr('pos', 'r');
    }
    xtip.tips(msg, '#' + $element.attr('id'), {
        bgcolor: 'red',
        pos: $element.attr('pos')
    })
    $element.focus();
}

/*生成随机数*/
function randomInt(x, y) {
    //x上限，y下限
    return parseInt(Math.random() * (x - y + 1) + y);
}