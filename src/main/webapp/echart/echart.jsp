<%@page pageEncoding="UTF-8" isELIgnored="false" contentType="text/html; UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>123455</title>
    <script type="text/javascript" src="https://cdn.goeasy.io/goeasy-1.0.3.js"></script>
    <script src="${path}/bootstrap/js/jquery.min.js"></script>
    <script src="${path}/js/echarts.min.js"></script>
    <script type="text/javascript">
        var goEasy = new GoEasy({
            host:'hangzhou.goeasy.io', //应用所在的区域地址: 【hangzhou.goeasy.io |singapore.goeasy.io】
            appkey: "BC-b2ef222836d14a99b39876c04a64fa66", //替换为您的应用appkey
        });
        //GoEasy-OTP可以对appkey进行有效保护,详情请参考​ ​
        //如果需要使用HTTPS/WSS，请在连接初始化GoEasy对象的时候传入参数forceTLS，并设置为true。
        goEasy.subscribe({
            channel: "my_channel", //替换为您自己的channel
            onMessage: function (message) {
                alert("Channel:" + message.channel + " content:" + message.content);
            }
        });

    </script>
</head>
<body>
<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
<div id="main" style="width: 600px;height:400px;"></div>
</body>
</html>