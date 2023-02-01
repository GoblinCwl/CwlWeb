insert into app (id, name, html, description, is_lock, uses_times, icon_file, color, sort)
values  (99, 'JRebel激活', '<html lang="zh">
<body>
<h1>您好，这里是Jrebel许可证服务器！</h1>
<p>
    许可证服务器启动于
    <a target="_blank" class="url" style="color: #6ecadc;font-weight: bold"></a>
</p>
<p>
    JRebel 7.1及更早版本激活地址为：
    <span style=''color:red''><span class="url"></span>/{tokenname}</span>
    , 填写任意电子邮件。
</p>
<p>
    JRebel 2018.1及更高版本激活地址为：
    <span class="url"></span>/随机生成的UUID<br>
    (示例(可直接使用):
    <span style=''color:#3ec45e;font-weight: bold''>
        <span class="url"></span>/<span class="uuid"></span>
    </span>
    ), 填写任意电子邮件。
</p>
</body>
<div>
    <script src="/webjars/jquery/3.6.1/jquery.min.js"></script>
    <script src="/webjars/jquery-cookie/1.4.1-1/jquery.cookie.js"></script>
    <script>
        const ctx = window.parent.location.protocol + "//" + window.parent.location.host;
        $(function () {
            $(".url").text(ctx)
            $("a.url").attr("href", ctx + "/app/jrebel")
            $(".uuid").text(''xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx''.replace(/[xy]/g, function (c) {
                const r = Math.random() * 16 | 0, v = c === ''x'' ? r : (r & 0x3 | 0x8);
                return v.toString(16);
            }))
        })
    </script>
</div>
</html>', '通过本站的许可证服务器，激活你的JRebel/XRebel。', 0, 144, 'appIcon/13ff3bc7-7db7-4d13-bf6c-f7322a3ad2c5.png', '#5fb53b', 1),
        (100, '邮件模板预览', '', '', 1, 10, 'appIcon/f2cd1741-93e1-416d-be03-8591fc6e33a1.png', '#24710e', 2),
        (101, '1', '', '', 0, 2, 'appIcon/ab5af727-6615-4e44-84f7-48d42c2c1cb5.png', '#000000', 2);