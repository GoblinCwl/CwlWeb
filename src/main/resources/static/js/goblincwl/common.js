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
        //(自定义)请求类型为api
        'GoblinCwlRequestType': 'api'
    }
}

//Ajax封装
const HttpRequest = function (options) {
    const defaults = {
        type: 'get',
        headers: {},
        data: {},
        dataType: 'json',
        async: true,
        cache: false,
        beforeSend: null,
        success: null,
        complete: null
    };
    const o = $.extend({}, defaults, options);
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
            o.success && o.success(res);
        },
        complete: function () {
            o.complete && o.complete();
        }
    });
};

const ajaxHttp = function (options) {
    HttpRequest(options);
};
