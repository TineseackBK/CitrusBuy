// 往 index.jsp 更新商品列表

var goods_image;
var goods_price;
var goods_name;

// ajax
$.ajax({
   url: 'GetDataServlet',
   method: 'POST',
   success: function (data) {
       updateGoodsContents(data);
   },
   error: function () {

   }
});

function updateGoodsContents(data) {
    goods_image = data.goods_image;
    goods_price = data.goods_price;
    goods_name = data.goods_name;

    // 获取商品列表容器
    var goodsContainer = document.querySelector('.goods');

    // 创建新商品
    var newGoods = document.createElement('div');
    newGoods.className = 'gl-item-wrap';

    // 图片
    var goodsImage = document.createElement('img');
    // goodsImage.className = 'gli-image';
    goodsImage.src = goods_image;
    goodsImage.alt = 'New Goods';
    newGoods.appendChild(goodsImage);

    // 价格
    var goodsPrice = document.createElement('p');
    goodsPrice.textContent = goods_price;
    newGoods.appendChild(goodsPrice);

    // 名称
    var goodsName = document.createElement('p');
    goodsName.textContent = goods_name;
    newGoods.appendChild(goodsName);

    // 商品放到容器里
    goodsContainer.appendChild(newGoods);
}