<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>yingx Login</title>
    <!-- CSS -->
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/login/assets/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/login/assets/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/login/assets/css/form-elements.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/login/assets/css/style.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/login/assets/ico/favicon.png">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="${pageContext.request.contextPath}/login/assets/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="${pageContext.request.contextPath}/login/assets/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="${pageContext.request.contextPath}/login/assets/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="${pageContext.request.contextPath}/login/assets/ico/apple-touch-icon-57-precomposed.png">
    <script src="${pageContext.request.contextPath}/bootstrap/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/login/assets/bootstrap/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/login/assets/js/jquery.backstretch.min.js"></script>
    <script src="${pageContext.request.contextPath}/login/assets/js/scripts.js"></script>
    <script src="${pageContext.request.contextPath}/login/assets/js/jquery.validate.min.js"></script>
    <script type="text/javascript">
        $(function () {

            $("#PasswordLogin").click(function () {
                location.href="${path}/login/login.jsp";
            })

            $.extend($.validator.messages,{
                //表单验证  必填验证  需要在要验证的input框上加入  required
                required: "<span style='color:red'>这是必填字段</span>",
                //表单验证  最小字符验证  需要在要验证的input框上加入  minlength=4
                minlength: $.validator.format("<span style='color:red'>最少要输入4个字符</span>"),
                maxlength: $.validator.format("<span style='color:red'>最多输入11个字符</span>")
            })
            //异步提交发送验证码
            $("#sendPhoneCode").click(function () {
                var phone = $("#phone").valueOf()
                $.ajax({
                    url:"${pageContext.request.contextPath}/admin/getMessage",
                    type:"post",
                    dataType:"json",
                    data:$("#loginForm").serialize(), //将表单的值序列化
                    success:function(data){

                    }
                })
            })
            //异步提交
            $("#loginButtonId").click(function () {
                //判断表单验证是否通过
                var isOk = $("#loginForm").valid();
                if(isOk){
                    $.ajax({
                        url:"${pageContext.request.contextPath}/admin/phoneLogin",
                        type:"post",
                        dataType:"text",
                        data:$("#loginForm").serialize(), //将表单的值序列化
                        success:function(message){
                            if(message=="验证成功"){
                                location.href="${pageContext.request.contextPath}/main/main.jsp";
                            }else{
                                $("#msgDiv").html(message);
                            }
                        }
                    })
                }
            })
        })

    </script>
</head>

<body background="${pageContext.request.contextPath}/login/assets/img/backgrounds/1.jpg">

<!-- Top content -->
<div class="top-content">

    <div class="inner-bg">
        <div class="container">
            <div class="row">
                <div class="col-sm-8 col-sm-offset-2 text">
                    <h1><strong>YINGX</strong> Login Form</h1>
                    <div class="description">
                        <p>
                            <a href="#"><strong>YINGX</strong></a>
                        </p>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6 col-sm-offset-3 form-box">
                    <div class="form-top" style="width: 450px">
                        <div class="form-top-left">
                            <h3>Login to showAll</h3>
                            <p>Enter your Phone and Message to log on:</p>
                        </div>
                        <div class="form-top-right">
                            <i class="fa fa-key"></i>
                        </div>
                    </div>
                    <div class="form-bottom" style="width: 450px">
                        <form   role="form" method="post" class="login-form" id="loginForm">
                            <span id="msgDiv"></span>
                            <div class="form-group">
                                <label class="sr-only" for="form-username">Username</label>
                                <input required minlength="4" maxlength="11" id="phone"  type="text" name="phone" placeholder="请输入手机号..." class="form-username form-control" id="form-username">
                                <span><a href="javascript:void(0)" id="sendPhoneCode">发送短信验证码</a></span>
                            </div>
                            <div class="form-group">
                                <label class="sr-only" for="form-password">Password</label>
                                <input required minlength="4" maxlength="10" id="phoneCode"  type="text" name="phoneCode" placeholder="请输入验证码..." class="form-password form-control"  id="form-password">
                            </div>

                            <input  type="button" style="width: 400px;border:1px solid #9d9d9d;border-radius: 4px;" id="loginButtonId" value="登录">
                            <input  type="button" style="width: 400px;border:1px solid #9d9d9d;border-radius: 4px;" id="PasswordLogin" value="使用账号密码登录">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
<div class="copyrights">Collect from <a href="http://www.cssmoban.com/" title="网站模板">网站模板</a></div>


<!-- Javascript -->

<!--[if lt IE 10]>
<script src="assets/js/placeholder.js"></script>
<![endif]-->

</body>

</html>