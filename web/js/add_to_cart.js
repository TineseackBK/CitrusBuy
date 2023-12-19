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


// // checkoutBtn.addEventListener('click', function () {
// // 跳转到结算页面和触发后续操作
// // 构造购物车商品数据
// const cartItemData = cartItems.map(item => ({
//     name: item.productName,
//     price: item.productPrice,
//     quantity: item.quantity
// }));
//
// // 构造需要传递到结算页面的数据
// const data = {
//     cartItems: cartItemData,
//     totalPrice: total.toFixed(2),
//     username: '<%= username %>',
//     email: '<%= email %>'
// };
//
// // 使用表单提交数据，跳转到结算页面
// const form = document.createElement('form');
// form.action = 'checkout.jsp';
// form.method = 'post';
//
// // 创建一个隐藏的 input，用于传递 JSON 格式的数据
// const input = document.createElement('input');
// input.type = 'hidden';
// input.name = 'data';
// input.value = JSON.stringify(data);
//
// // 将 input 添加到 form 中
// form.appendChild(input);
//
// // 将 form 添加到 body 中
// document.body.appendChild(form);
//
// // 提交表单
// form.submit();
