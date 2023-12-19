// 添加商品图片
document.addEventListener('DOMContentLoaded', function () {
    // 获取表单元素
    var form = document.querySelector('form');

    var input  = document.getElementById('productImage');
    var fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.style.display = 'none';

    form.appendChild(fileInput);

    var button = document.createElement('button');
    button.type = 'button';
    button.textContent = '选择文件';
    button.className = 'addImageBtn';

    button.addEventListener('click', function () {
        fileInput.click();
    });

    var label = input.previousElementSibling;
    label.appendChild(button);

    fileInput.addEventListener('change', function () {
        if (fileInput.files.length > 0) {
            var file = fileInput.files[0];
            // 按钮旁边的输入框显示为文件名
            input.value = file.name;
        }
    });

    // 监听表单的 submit
    form.addEventListener('submit', function (event) {
        // 阻止默认的表单提交，方便把文件一起提交
        event.preventDefault();

        // 创一个 formData 把其他东西和文件一起存进来
        var formData = new FormData(form);

        if (fileInput.files.length > 0) {
            formData.append('productImage', fileInput.files[0]);
        }

        // 发送 formData 到 Servlet
        fetch('GetDataServlet', {
            method: 'POST',
            body: formData
        }).then(response => {
            // 处理响应
            console.log('[CitrusBuy] 成功加载商品信息', response)
        }).catch(error => {
            // 处理错误
            console.error('[CitrusBuy] 加载商品信息时发生错误：', error);
        });
    });
});