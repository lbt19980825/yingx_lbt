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
            $("#imgVcode").click(function () {
                $("#imgVcode").prop("src","${pageContext.request.contextPath}/admin/code?time="+new Date().getTime());
            })
            $("#PhoneLogin").click(function () {
                location.href="${path}/login/phoneLogin.jsp";
            })

            $.extend($.validator.messages,{
                //表单验证  必填验证  需要在要验证的input框上加入  required
                required: "<span style='color:red'>这是必填字段</span>",
                //表单验证  最小字符验证  需要在要验证的input框上加入  minlength=4
                minlength: $.validator.format("<span style='color:red'>最少要输入4个字符</span>"),
                maxlength: $.validator.format("<span style='color:red'>最多输入10个字符</span>")
            })
            //异步提交
            $("#loginButtonId").click(function () {
                //判断表单验证是否通过
                var isOk = $("#loginForm").valid();
                if(isOk){
                    $.ajax({
                        url:"${pageContext.request.contextPath}/admin/loginAdmin",
                        type:"post",
                        dataType:"json",
                        data:$("#loginForm").serialize(), //将表单的值序列化
                        success:function(data){
                            if(data.status=="200"){
                                location.href="${pageContext.request.contextPath}/main/main.jsp";
                            }else{
                                //向警告框中追加错误信息
                                $("#msgDiv").html(data.message);
                                //展示警告框
                                $("#deleteMsg").show();
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
                            <p>Enter your username and password to log on:</p>
                        </div>
                        <div class="form-top-right">
                            <i class="fa fa-key"></i>
                        </div>
                    </div>
                    <div class="form-bottom" style="width: 450px">
                        <%--警告提示框--%>
                        <div id="deleteMsg" class="alert alert-danger" style="height: 50px;width: 250px;display: none" align="center">
                            <span id="msgDiv">sadsad</span>
                        </div>
                        <form   role="form" method="post" class="login-form" id="loginForm">
                            <div class="form-group">
                                <label class="sr-only" for="form-username">Username</label>
                                <input required minlength="4" maxlength="10" id="username"  type="text" name="username" placeholder="请输入用户名..." class="form-username form-control" id="form-username">
                            </div>
                            <div class="form-group">
                                <label class="sr-only" for="form-password">Password</label>
                                <input required minlength="4" maxlength="10" id="password"  type="password" name="password" placeholder="请输入密码..." class="form-password form-control"  id="form-password">
                            </div>
                            <div class="form-group">
                                <%--<label class="sr-only" for="form-code">Code</label>--%>
                                <img id="imgVcode" style="height: 46px" class="captchaImage" src="${pageContext.request.contextPath}/admin/code" >
                                <input style="width: 289px;height: 50px;border:3px solid #ddd;border-radius: 4px;" required minlength="4" type="test" name="code" id="form-code">
                            </div>
                            <input  type="button" style="width: 400px;border:1px solid #9d9d9d;border-radius: 4px;" id="loginButtonId" value="登录">
                            <input  type="button" style="width: 400px;border:1px solid #9d9d9d;border-radius: 4px;" id="PhoneLogin" value="使用手机号登录">
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