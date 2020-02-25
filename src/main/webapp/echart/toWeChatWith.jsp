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
        <title>聊天室</title>
        <script src="${path}/bootstrap/js/jquery.min.js"></script>
        <script type="text/javascript" src="https://cdn.goeasy.io/goeasy-1.0.3.js"></script>
        <script>
            /*初始化GoEasy*/
            var goEasy = new GoEasy({
                host:'hangzhou.goeasy.io', //应用所在的区域地址: 【hangzhou.goeasy.io |singapore.goeasy.io】
                appkey: "BC-df0a45499f274b2bae29ae50a6a12dc9", //替换为您的应用appkey
            });

            $(function(){

                var inputMsg;

                /*订阅消息*/
                goEasy.subscribe({
                    channel: "myWeChatChannel",
                    onMessage: function (message) {

                        //获取接收的内容
                        var send = message.content;

                        //接收的是不是我发的内容
                        if(inputMsg==send){
                            //是 不处理
                        }else{
                            //不是追加处理
                            //渲染发送页面
                            var msgDiv=("<div style=';width:auto;height: 30px;'>" +
                                "<div style='float:left;background-color: #9a9afb;border-radius: 12px'>"+message.content+"</div>" +
                                "</div>");
                            //每次发消息追加
                            $("#showMsg").append(msgDiv);
                        }
                    }
                });

                //点击发送按钮发送消息
                $("#sendMsg").click(function(){

                    //获取输入框输入的内容
                    var content=$("#content").val();

                    //给InputMsg赋值
                    inputMsg=content;

                    //发送消息
                    goEasy.publish({
                        channel: "myWeChatChannel", //替换为您自己的channel
                        message: content, //替换为您想要发送的消息内容
                        onSuccess:function(){
                            //清空输入框
                            $("#content").val("");

                            //渲染发送页面
                            var msgDiv=("<div style=';width:auto;height: 30px;'>" +
                                "<div style='float:right;background-color: #acdd4a;border-radius: 12px'>"+content+"</div>" +
                                "</div>");

                            //每次发消息追加
                            $("#showMsg").append(msgDiv);
                        },
                        onFailed: function (error) {
                            alert("消息发送失败，错误编码："+error.code+" 错误信息："+error.content);
                        }
                    });
                });
            })
        </script>
    </head>
    <body>
        <div align="center">
            <h1>184 聊天室</h1>
            <div style="width: 600px;height: 600px;border: 3px #ccaadd solid">
                <%--内容展示区域--%>
                <div id="showMsg" style="width: 594px;height: 500px;border: 3px #aaccdd solid"></div>
                <%--内容输入区域--%>
                <div style="width: 594px;height: 88px;border: 3px #ddccaa solid">
                    <%--输入文本框--%>
                    <input id="content" style="width: 500px;height: 80px;" type="area"/>
                    <%--发送按钮--%>
                    <button id="sendMsg" style="width: 70px;height: 80px;background-color: greenyellow">发送</button>
                </div>
            </div>
        </div>
    </body>
</html>