<%--
  Created by IntelliJ IDEA.
  User: yanan
  Date: 2023/12/17
  Time: 0:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>
  <meta charset="UTF-8">
  <title>橘子夹的电商小站 - 首页</title>
  <link rel="stylesheet" type="text/css" href="css/base.css">
  <link rel="stylesheet" type="text/css" href="css/index.css">
</head>
<body>
<!-- 顶部栏开始 -->
<div class="header">
  <div class="w clearfit">
    <div class="user fl">
      <% String username = (String) session.getAttribute("username"); %>
      <% if (username == null || username.isEmpty()) { %>
        <a href="login.jsp" class="btn login">登录</a>
        &nbsp;&nbsp;
        <a href="register.jsp" class="btn register f60">注册</a>
      <% } else { %>
        欢迎您！<%= username %>
        &nbsp;&nbsp;
        <a href="LogoutServlet" class="btn register f60">注销</a>
      <% } %>
    </div>
    <div class="my_order fr">
      <a href="productManagement.jsp" class="btn order">后台管理</a>
    </div>
  </div>
</div>
<!-- 顶部栏结束 -->

<!-- 主体开始 -->
<div class="main">
  <div class="w">
    <!-- 顶部 logo 以及搜索框 -->
    <div class="search-wrap">
      <div class="w">
        <div class="logo-wrap">
          <a href="#" class="logo"></a>
        </div>
      </div>
      <form action="" class="search-input" onsubmit="searchProducts(event)">
        <input type="text" name="search" placeholder="点击搜索">
        <input type="submit" value="搜索">
      </form>
      <script>
        function searchProducts(event) {
          event.preventDefault(); // 阻止表单默认提交行为

          var searchInput = document.querySelector('input[name="search"]');
          var searchKeyword = searchInput.value.toLowerCase(); // 获取用户输入的搜索关键字，转换为小写

          var productNames = document.querySelectorAll('.gli-name p');

          productNames.forEach(function (productName) {
            var name = productName.getAttribute('data-name').toLowerCase();

            if (name.includes(searchKeyword)) {
              productName.closest('.gl-item-wrap').style.display = 'block';
            } else {
              productName.closest('.gl-item-wrap').style.display = 'none';
            }
          });
        }
      </script>
    </div>
    <!-- 左右分栏：商品列表 -->
    <div class="row">
      <div class="w">
        <!-- 左侧边栏：商品分类 -->
        <div class="bar left">
          <div class="cate">
            <ul class="category">
              <li>
                <a href="#" class="ctgr">手机</a><em>/</em>
                <a href="#" class="ctgr">运营商</a><em>/</em>
                <a href="#" class="ctgr">智能数码</a>
              </li>
              <li>
                <a href="#" class="ctgr">电视</a><em>/</em>
                <a href="#" class="ctgr">冰箱</a><em>/</em>
                <a href="#" class="ctgr">空调</a><em>/</em>
                <a href="#" class="ctgr">洗衣机</a>
              </li>
              <li>
                <a href="#" class="ctgr">电脑</a><em>/</em>
                <a href="#" class="ctgr">相机</a><em>/</em>
                <a href="#" class="ctgr">DIY</a>
              </li>
              <li>
                <a href="#" class="ctgr">厨卫大电</a><em>/</em>
                <a href="#" class="ctgr">生活家电</a><em>/</em>
                <a href="#" class="ctgr">厨具</a>
              </li>
              <li>
                <a href="#" class="ctgr">家居</a><em>/</em>
                <a href="#" class="ctgr">家具</a><em>/</em>
                <a href="#" class="ctgr">家装</a>
              </li>
              <li>
                <a href="#" class="ctgr">食品</a><em>/</em>
                <a href="#" class="ctgr">酒水</a><em>/</em>
                <a href="#" class="ctgr">生鲜</a>
              </li>
              <li>
                <a href="#" class="ctgr">母婴</a><em>/</em>
                <a href="#" class="ctgr">玩具</a><em>/</em>
                <a href="#" class="ctgr">童装</a>
              </li>
              <li>
                <a href="#" class="ctgr">男装</a><em>/</em>
                <a href="#" class="ctgr">女装</a><em>/</em>
                <a href="#" class="ctgr">内衣</a>
              </li>
              <li>
                <a href="#" class="ctgr">鞋靴</a><em>/</em>
                <a href="#" class="ctgr">箱包</a><em>/</em>
                <a href="#" class="ctgr">钟表</a><em>/</em>
                <a href="#" class="ctgr">珠宝</a>
              </li>
              <li>
                <a href="#" class="ctgr">图书</a><em>/</em>
                <a href="#" class="ctgr">教辅教材</a>
              </li>
            </ul>
          </div>
        </div>
        <!-- 右侧边栏：商品 -->
        <div class="bar right">
          <!-- 按要求筛选商品 -->
          <div class="filter">
            <div class="ranking">
              <ul class="rank">
                <li class="rk">
                  <a href="#">综合</a><em>↓</em>
                  &nbsp;&nbsp;
                </li>
                <li class="rk">
                  <a href="#">销量</a><em>↓</em>
                  &nbsp;&nbsp;
                </li>
                <li class="rk">
                  <a href="#">价格</a><em>↓</em>
                </li>
              </ul>
            </div>
          </div>
          <!-- 这部分展示各种乱七八糟的商品 -->
          <div class="goods">
            <script>
              var xhr = new XMLHttpRequest();
              xhr.onreadystatechange = function () {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                  if (xhr.status === 200) {
                    document.querySelector('.goods').innerHTML = xhr.responseText;
                  }
                  else {
                    console.error('[CitrusBuy] 商品列表更新失败' + xhr.status);
                  }
                }
              };
              xhr.open('GET', 'GetDataServlet', true);
              xhr.send();
            </script>
          </div>
        </div>
        <!-- 最右侧悬浮窗 -->
        <div id="cart" class="cart">
          <div class="cart-inner">
            <h2 style="font-size: 20px;">购物车</h2><br>
            <div id="cart-items" class="cart-items">
              <!-- 购物车中的商品显示在这里 -->
              呵呵
            </div>
            <div class="cart-total">
              <p style="font-size: 16px;">总价：<span id="total-price" class="total-price">￥0.00</span></p>
              <br>
              <div class="checkout">
                <button id="checkout-btn" class="checkout-btn">去结算</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<!-- 主体结束 -->
<!-- 页尾开始 -->
<div class="footer">
  <p>21网工 - 赖言安 - 202130440811</p>
</div>
<!-- 页尾结束 -->
</body>
<script type="text/javascript" src="js/add_to_cart.js"></script>
</html>
