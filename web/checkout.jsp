<%--
  Created by IntelliJ IDEA.
  User: yanan
  Date: 2023/12/19
  Time: 22:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>橘子夹的电商小站 - 结算</title>
    <link rel="stylesheet" type="text/css" href="css/base.css">
    <link rel="stylesheet" type="text/css" href="css/checkout.css">
</head>
<body>
<div class="main">
    <div class="thank-you">
        <h1>感谢您的购买！</h1>
        <h2>下列是您的购买清单，请核对</h2>
        <br><br>
    </div>
    <div class="my-goods">
        <script>
            var xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function () {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status === 200) {
                        document.querySelector('.my-goods').innerHTML = xhr.responseText;
                    }
                    else {
                        console.error('[CitrusBuy] 商品列表更新失败' + xhr.status);
                    }
                }
            };
            xhr.open('GET', 'CheckoutServlet', true);
            xhr.send();
        </script>
    </div>
    <br><br><a href="index.jsp" class="btn login">返回主页</a>
</div>
</body>
</html>
