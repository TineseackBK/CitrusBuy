<%--
  Created by IntelliJ IDEA.
  User: yanan
  Date: 2023/12/17
  Time: 20:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>橘子夹的电商小站 - 登录</title>
    <link rel="stylesheet" type="text/css" href="css/base.css">
    <link rel="stylesheet" type="text/css" href="css/index.css">
</head>
<body>
<div class="main">
    <div style="text-align: center;">
        <form action="LoginServlet" method="post">
            <br><br><br><br><br>
            <span class="errorMsg">
                ${"登录页面"}<br>
                ${empty requestScope.Msg ? "请输入用户名和密码" : requestScope.Msg}
            </span><br>
            <label for="username">用户名：</label>
            <input type="text" id="username" name="username"><br><br>
            <label for="password">密码：</label>
            <input type="password" id="password" name="password"><br><br>
            <input type="submit" value="登录"><br><br>
        </form>
        <a href="index.jsp" class="btn login">返回主页</a>
    </div>
</div>
</body>
</html>
