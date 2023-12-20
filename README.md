![预览图](/web/img/logo1.png)
————————————

作者：橘子夹

邮箱：651547166@qq.com

————————————

WEB 应用练习：橘子夹的电商小站

功能：

1. 可显示商品图片、名字、价格

2. 可关键词搜索商品

3. 可选择商品进购物车，显示总价，购买

4. 可后台管理商品信息，包括新建、修改、删除商品的名字、价格、图片、类型

————————————

项目编写和运行环境：

IntelliJ IDEA 2023

JDK 17

Tomcat 10.1.17

JDBC MySQL 8.0.25

————————————

项目部署：

http://teyvatlore.com:8080/CitrusBuyWeb/

Ubuntu 20

————————————

测试用户：

用户名 = user0

密码 = 123

————————————

学号 202130440811

姓名 赖言安

————————————

部分原理展示：

// index.jsp

// 下面这部分展示了如何在主页中动态更新显示商品

```
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

```
     
在这个 div 里使用了 js 脚本，通过 AJAX 异步加载商品，发送 GET 请求到 GetDataServlet.java，然后通过 Servlet 的响应创建 html 代码。

// GetDataServlet.java

// 下面这部分展示了上述的 GET 请求发送到 Servlet 之后，Servlet 如何处理以及响应的

```
try {
    sql = "SELECT * FROM `citrusbuy`.`goods`;";
    statement = connection.prepareCall(sql);

    ResultSet resultSet = statement.executeQuery();

    while (resultSet.next()) {
        // 商品的各个属性
        String p_id = resultSet.getString("id");
        String p_name = resultSet.getString("name");
        String p_price = resultSet.getString("price");
        String p_sales = resultSet.getString("sales");
        String p_category = resultSet.getString("category");

        // 获取图片 blob
        byte[] imageData = null;
        Blob blob = resultSet.getBlob("img");
        imageData = blob.getBytes(1, (int)blob.length());

        // blob 转换为 base64
        String base64Image = Base64.getEncoder().encodeToString(imageData);

        // 写入 html 代码
        out.println("<div class=\"gl-item-wrap\" data-p_id=\"" + p_id + "\" data-p_name=\"" + p_name + "\" data-p_price=\"" + p_price + "\" data-p_category=\"" + p_category + "\">");
        out.println("<div class=\"gli-image\">");
        out.println("<a href=\"#\">");
        out.println("<img src=\"data:image/png;base64," + base64Image + "\" alt=\"\" height=\"340\" width=\"340\">");
        out.println("</a>");
        out.println("</div>");
        out.println("<div class=\"gli-price\">");
        out.println("<a href=\"#\">");
        out.println("<p>￥" + p_price + "</p>");
        out.println("</a>");
        out.println("</div>");
        out.println("<div class=\"gli-name\">");
        out.println("<a href=\"#\">");
        out.println("<p data-name=\"" + p_name + "\">" + p_name + "</p>");
        out.println("</a>");
        out.println("</div>");
        out.println("<div class=\"cart-btn\">");
        out.println("<button class=\"add-to-cart-btn\">添加到购物车</button>");
        out.println("</div>");
        out.println("</div>");
    }
    statement.close();
    connection.close();
}
catch (SQLException e) {
    e.printStackTrace();
}
```

可以看到，Servlet 从数据库中查询所有符合条件的商品，然后通过 PrintWriter 创建一个对象 out，由 out 把 html 代码作为响应发送回去，这样就可以在 index.jsp 动态显示当前的所有符合条件的商品了。

// index.jsp
// 下面这部分展示了购物车的处理

```
<div id="cart" class="cart">
  <div class="cart-inner">
    <h2 style="font-size: 20px;">购物车</h2><br>
    <div id="cart-items" class="cart-items">
      <!-- 购物车中的商品显示在这里 -->
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
```

前端里，先通过 jsp 简单编写一个购物车的框架，留出位置给购物车放商品。然后再调用 js 脚本如下。

```
// add_to_cart.js
document.addEventListener('DOMContentLoaded', function () {
    const cartItemsContainer = document.getElementById('cart-items');
    const totalPriceSpan = document.getElementById('total-price');
    const checkoutBtn = document.getElementById('checkout-btn');
    let total = 0;

    // 购物车数据，用于存储用户已选商品
    const cartItems = [];

    // 使用事件委托监听点击事件
    document.body.addEventListener('click', function (event) {
        if (event.target.classList.contains('add-to-cart-btn')) {
            const productContainer = event.target.closest('.gl-item-wrap');
            const productId = productContainer.dataset.p_id;

            // 检查购物车中是否已存在该商品
            const existingItem = cartItems.find(item => item.productId === productId);

            if (existingItem) {
                // 商品已存在，增加数量
                existingItem.quantity++;
            } else {
                // 商品不存在，添加到购物车
                const maxLength = 10;
                const productNameOld = productContainer.dataset.p_name;
                const productName = productNameOld.length > maxLength ? productNameOld.substring(0, maxLength) + '...' : productNameOld;
                const productPrice = parseFloat(productContainer.dataset.p_price);
                cartItems.push({ productId, productName, productPrice, quantity: 1 });
            }

            // 更新购物车显示
            renderCart();
        } else if (event.target.classList.contains('remove-from-cart-btn')) {
            const productIdToRemove = event.target.dataset.productId;

            // 从购物车数据中移除对应商品
            const indexToRemove = cartItems.findIndex(item => item.productId === productIdToRemove);
            cartItems.splice(indexToRemove, 1);

            // 更新购物车显示
            renderCart();
        }
    });

    // 渲染购物车内容
    function renderCart() {
        // 清空购物车显示
        cartItemsContainer.innerHTML = '';

        // 渲染购物车中的每个商品
        cartItems.forEach(item => {
            const cartItemDiv = document.createElement('div');
            cartItemDiv.innerHTML = `
                <div class="remove-from">
                    <button class="remove-from-cart-btn" data-product-id="${item.productId}">移除</button>
                </div>
                <p style="float: left; font-size: 16px; padding-left: 4px; padding-right: 8px;">${item.productName}</p>
                <p style="float: right">数量 ${item.quantity}</p>
                <br><br>
            `;
            cartItemsContainer.appendChild(cartItemDiv);
        });

        // 计算并更新总价
        total = cartItems.reduce((acc, item) => acc + item.productPrice * item.quantity, 0);
        totalPriceSpan.textContent = `￥${total.toFixed(2)}`;

        // 显示或隐藏去结算按钮
        checkoutBtn.style.display = cartItems.length > 0 ? 'block' : 'none';
    }

    // 去结算按钮点击事件
    document.body.addEventListener('click', function (event) {
        if (event.target.classList.contains('checkout-btn')) {
            // 构建请求数据
            const requestData = {
                cartItems: cartItems,
                totalPrice: total.toFixed(2),
                username: '<%= session.getAttribute("username") %>',
                email: '<%= session.getAttribute("email") %>'
            };

            // 发送异步请求
            fetch('CheckoutServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            })
            .then(response => {
                if (response.ok) {
                    // 跳转到结算页面
                    window.location.href = 'checkout.jsp';
                } else {
                    console.error('结算请求失败');
                }
            })
            .catch(error => console.error('结算请求错误:', error));
        }
    });
});
```

在这个 js 里，首先读取了 index.jsp 里面点击加入购物车按钮所对应的商品 div 里面的信息，然后构造了一个购物车项 cartItem 对象，把这些信息都包装进去。随后，判断购物车内是否已存在该商品，如果存在就把数量 +1，否则就添加进购物车内。此外，还在其中一个函数里实时渲染购物车的内容到 index.jsp 上，这样用户就可以实时查看当前购物车的内容。

当用户点击了结算按钮后，这个 js 就会构造一个请求数据，它主要由 cartItems（包含若干属性）、总价、用户名和用户邮箱（未使用）组成，并且转换成 JSON 形式包装为信息的 body，通过 fetch 向结算页面 checkout.jsp 发送异步请求。这个请求将在 checkout.jsp 中获取并解开 JSON 的格式然后重新放在页面上。

