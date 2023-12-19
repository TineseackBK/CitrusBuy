<%--
  Created by IntelliJ IDEA.
  User: yanan
  Date: 2023/12/18
  Time: 2:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>
    <title>橘子夹的电商小站 - 后台管理 - 商品</title>
    <link rel="stylesheet" type="text/css" href="css/base.css">
    <link rel="stylesheet" type="text/css" href="css/productManagement.css">
</head>
<body>
<div class="container">
    <div class="row w">
        <!-- 左侧栏 -->
        <div class="bar-manager left">
            <div class="cate">
                <ul class="category">
                    <script>
                        var xhr = new XMLHttpRequest();
                        xhr.onreadystatechange = function () {
                            if (xhr.readyState === XMLHttpRequest.DONE) {
                                if (xhr.status === 200) {
                                    document.querySelector('.category').innerHTML = xhr.responseText;
                                }
                                else {
                                    console.error('[CitrusBuy] 商品列表更新失败' + xhr.status);
                                }
                            }
                        };
                        xhr.open('GET', 'GoodsListServlet', true);
                        xhr.send();
                    </script>
                </ul>
            </div>
        </div>

        <!-- 右侧栏 -->
        <div class="bar-manager right">
            <div class="product-details">
                <form id="productForm" action="StoreDataServlet" method="post" enctype="multipart/form-data">
                    <br><br>
                    <input type="reset" class="form-btn" value="新建商品">
                    <input type="submit" class="form-btn" name="delete" value="删除商品"><br>
                    <label for="productID" class="inputLabel">商品编号：</label>
                    <input type="text" id="productID" name="productID" readonly><br><br>

                    <label for="productName" class="inputLabel">商品名称：</label>
                    <input type="text" id="productName" name="productName"><br><br>

                    <label for="productPrice" class="inputLabel">商品价格：</label>
                    <input type="text" id="productPrice" name="productPrice"><br><br>

                    <label for="productImage" class="inputLabel">商品图片：</label>
                    <input type="file" id="productImage" name="productImage"><br><br>

                    <label for="productImage" class="inputLabel">商品类别</label><br><br>
                    <div class="all-category">
                        <div class="radio-group">
                            <input type="radio" id="mobile" name="productCategory" value="手机">
                            <label for="mobile">手机</label>

                            <input type="radio" id="telecom" name="productCategory" value="运营商">
                            <label for="telecom">运营商</label>

                            <input type="radio" id="digital" name="productCategory" value="智能数码">
                            <label for="digital">智能数码</label>
                        </div><br>

                        <div class="radio-group">
                            <input type="radio" id="tv" name="productCategory" value="电视">
                            <label for="tv">电视</label>

                            <input type="radio" id="fridge" name="productCategory" value="冰箱">
                            <label for="fridge">冰箱</label>

                            <input type="radio" id="ac" name="productCategory" value="空调">
                            <label for="ac">空调</label>

                            <input type="radio" id="washer" name="productCategory" value="洗衣机">
                            <label for="washer">洗衣机</label>
                        </div><br>

                        <div class="radio-group">
                            <input type="radio" id="computer" name="productCategory" value="电脑">
                            <label for="computer">电脑</label>

                            <input type="radio" id="camera" name="productCategory" value="相机">
                            <label for="camera">相机</label>

                            <input type="radio" id="diy" name="productCategory" value="DIY">
                            <label for="diy">DIY</label>
                        </div><br>

                        <div class="radio-group">
                            <input type="radio" id="kitchen" name="productCategory" value="厨卫大电">
                            <label for="kitchen">厨卫大电</label>

                            <input type="radio" id="appliances" name="productCategory" value="生活家电">
                            <label for="appliances">生活家电</label>

                            <input type="radio" id="kitchenware" name="productCategory" value="厨具">
                            <label for="kitchenware">厨具</label>
                        </div><br>

                        <div class="radio-group">
                            <input type="radio" id="home" name="productCategory" value="家居">
                            <label for="home">家居</label>

                            <input type="radio" id="furniture" name="productCategory" value="家具">
                            <label for="furniture">家具</label>

                            <input type="radio" id="decoration" name="productCategory" value="家装">
                            <label for="decoration">家装</label>
                        </div><br>

                        <div class="radio-group">
                            <input type="radio" id="food" name="productCategory" value="食品">
                            <label for="food">食品</label>

                            <input type="radio" id="beverages" name="productCategory" value="酒水">
                            <label for="beverages">酒水</label>

                            <input type="radio" id="fresh" name="productCategory" value="生鲜">
                            <label for="fresh">生鲜</label>
                        </div><br>

                        <div class="radio-group">
                            <input type="radio" id="baby" name="productCategory" value="母婴">
                            <label for="baby">母婴</label>

                            <input type="radio" id="toys" name="productCategory" value="玩具">
                            <label for="toys">玩具</label>

                            <input type="radio" id="childrenClothing" name="productCategory" value="童装">
                            <label for="childrenClothing">童装</label>
                        </div><br>

                        <div class="radio-group">
                            <input type="radio" id="mensClothing" name="productCategory" value="男装">
                            <label for="mensClothing">男装</label>

                            <input type="radio" id="womensClothing" name="productCategory" value="女装">
                            <label for="womensClothing">女装</label>

                            <input type="radio" id="underwear" name="productCategory" value="内衣">
                            <label for="underwear">内衣</label>
                        </div><br>

                        <div class="radio-group">
                            <input type="radio" id="shoes" name="productCategory" value="鞋靴">
                            <label for="shoes">鞋靴</label>

                            <input type="radio" id="bags" name="productCategory" value="箱包">
                            <label for="bags">箱包</label>

                            <input type="radio" id="watches" name="productCategory" value="钟表">
                            <label for="watches">钟表</label>

                            <input type="radio" id="jewelry" name="productCategory" value="珠宝">
                            <label for="jewelry">珠宝</label>
                        </div><br>

                        <div class="radio-group">
                            <input type="radio" id="books" name="productCategory" value="图书">
                            <label for="books">图书</label>

                            <input type="radio" id="teachingAids" name="productCategory" value="教辅教材">
                            <label for="teachingAids">教辅教材</label>
                        </div><br><br>
                    </div>
                    <input type="submit" class="form-btn" name="modify" value="添加/修改商品">
                    <input type="reset" class="form-btn" value="重置"><br><br>
                </form>
                <a href="index.jsp" class="btn login">返回主页</a>
                <script>
                    function showContent(event) {
                        // 阻止默认点击事件，以避免页面跳转
                        event.preventDefault();

                        // 获取点击的 a 元素的父元素 li
                        var liElement = event.target.closest('li');

                        // 获取 li 的子元素 div
                        var divElement = liElement.querySelector('.mng-gl-item-wrap');

                        // 获取 mng-gl-item-wrap 中的内容
                        var productID = divElement.dataset.p_id;
                        var productName = divElement.dataset.p_name;
                        var productPrice = divElement.dataset.p_price;
                        var productCategory = divElement.dataset.p_category;

                        // 将获取的内容插入到表单中
                        document.getElementById('productID').value = productID;
                        document.getElementById('productName').value = productName;
                        document.getElementById('productPrice').value = productPrice;

                        // 选择对应的 radio
                        var form = document.getElementById("productForm");
                        var radio = form.querySelector('input[name="productCategory"][value="' + productCategory + '"]');

                        // 将对应的 radio 设置为选中状态
                        if (radio) {
                            radio.checked = true;
                            }
                        }
                </script>
            </div>
        </div>
    </div>
</div>
</body>
</html>
