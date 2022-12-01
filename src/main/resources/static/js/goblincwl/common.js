$(function () {
    const a = getClientInfo();
    if (a !== "PC") {
        alert("移动端暂未开发，请使用电脑浏览！");
        //关闭此页面
        window.close();
    }
});

function getWebSocketUrl() {
    let socketUrl;
    if (url.indexOf("https") !== -1) {
        socketUrl = url.replace('https', 'wss');
    } else {
        socketUrl = url.replace('http', 'ws');
    }
    return socketUrl;
}

//判断访问设备方法
function getClientInfo() {
    const userAgentInfo = navigator.userAgent;
    const Agents = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"];
    let agentinfo = null;
    for (let i = 0; i < Agents.length; i++) {
        if (userAgentInfo.indexOf(Agents[i]) > 0) {
            agentinfo = userAgentInfo;
            break;
        }
    }
    if (agentinfo) {
        return agentinfo;
    } else {
        return "PC";
    }
}

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
        validate: function () {
            return true;
        },
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
        if (!fromValidate(o.validateForm) || !o.validate()) {
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
        contentType: o.contentType,
        accept: o.accept,
        beforeSend: function () {
            if (o.doBefore === false) {
                return;
            }
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

// 文件下载
$.download = function (url, method, filedir, filename) {
    $('<form action="' + url + '" method="' + (method || 'post') + '">' +  // action请求路径及推送方法
        '<input type="text" name="filedir" value="' + filedir + '"/>' + // 文件路径
        '<input type="text" name="filename" value="' + filename + '"/>' + // 文件名称
        '</form>')
        .appendTo('body').submit().remove();
};

//获取字符串准确长度
var jmz = {};
jmz.GetLength = function (str) {
    var realLength = 0, len = str.length, charCode = -1;
    for (var i = 0; i < len; i++) {
        charCode = str.charCodeAt(i);
        if (charCode >= 0 && charCode <= 128) realLength += 1;
        else realLength += 2;
    }
    return realLength;

};

function reLengthStr(str, length) {
    if (jmz.GetLength(str) > length) {
        return str.substr(0, (length - 1)) + "...";
    }
    return str;
}

function getRequestParam() {
    const url = location.search; //获取url中"?"符后的字串
    const theRequest = {};
    if (url.indexOf("?") !== -1) {
        const str = url.substr(1);
        const strs = str.split("&");
        for (let i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = decodeURIComponent(strs[i].split("=")[1]);
        }
    }
    return theRequest;
};

function getRequestHash() {
    const hashUrl = location.hash;
    var hashVal = "";
    if (hashUrl.indexOf("#") !== -1) {
        return hashUrl.substring(1);
    }
    return hashVal;
};

function splitArrayToString(array) {
    var str = "";
    for (let i = 0; i < array.length; i++) {
        const arrayElement = array[i];
        str += arrayElement;
        str += ",";
    }
    if (str.length > 1) {
        str = str.substring(0, str.length - 1);
    }
    return str;
}

function PrefixInteger(num, length) {
    return (Array(length).join('0') + num).slice(-length);
}

function dateDiffDay(sDate1, sDate2) {  //sDate1和sDate2是yyyy-MM-dd格式  eg:2021-09-09、2021-09-10
    var startdate = new Date(sDate1);
    var enddate = new Date(sDate2);
    //把相差的毫秒数转换为天数
    var iDays = parseInt(Math.abs(enddate.getTime() - startdate.getTime()) / 1000 / 60 / 60 / 24);

    return iDays;  //返回相差天数
}

//生成min_v到max_v之间的随机数
function generate_rand_num(min_v, max_v) {
    //min_v下限，max_v上限
    const rand_num = parseInt(Math.random() * (max_v - min_v + 1) + min_v);
    return rand_num
}

/* 在新窗口中打开 */
function openNewWindow(url) {
    var a = document.createElement('a');
    a.setAttribute('href', url);
    a.setAttribute('target', '_blank');
    var id = Math.random(10000, 99999);
    a.setAttribute('id', id);
    // 防止反复添加
    if (!document.getElementById(id)) {
        document.body.appendChild(a);
    }
    a.click();
}

function notEmpty(str) {
    return str != null && str !== '' && str !== undefined;
}

//将毫秒转换为 X天X时X分X秒
function convertMilliSecondToTimeStr(milliSecond) {
    let sec;
    let min;
    let hour;
    let day;
    let month;
    let year;
    if (milliSecond / 1000 >= 1) {
        sec = milliSecond / 1000;
        if (sec / 60 >= 1) {
            min = parseInt(sec / 60);
            sec = parseInt((sec / 60 - min) * 60);
            if (min / 60 >= 1) {
                hour = parseInt(min / 60);
                min = (min / 60 - hour) * 60;
                if (hour / 24 >= 1) {
                    day = parseInt(hour / 24);
                    hour = (hour / 24 - day) * 24;
                    sec = null;
                    if (day / 30 >= 1) {
                        month = parseInt(day / 30);
                        day = (day / 30 - month) * 30;
                        min = null;
                        if (month / 12 >= 1) {
                            year = parseInt(month / 12);
                            month = (month / 12 - year) * 12;
                            hour = null;
                        }
                    }
                }
            }
        } else {
            sec = parseInt(milliSecond / 1000);
        }
    }
    return (notEmpty(year) ? year + "年" : "")
    + (notEmpty(month) ? month + "月" : "")
    + (notEmpty(day) ? day + "日" : "")
    + (notEmpty(hour) ? hour + "时" : "")
    + (notEmpty(min) ? min + "分" : "")
    + (notEmpty(sec) ? sec + "秒" : "")
}