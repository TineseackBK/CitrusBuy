import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.sql.*;
import java.util.Objects;

@WebServlet(name = "StoreDataServlet", value = "/StoreDataServlet")
@MultipartConfig
public class StoreDataServlet extends HttpServlet {
    private Connection connection;
    private PreparedStatement statement;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // 加载驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.printf("[CitrusBuy] 驱动加载成功\n");
        } catch (ClassNotFoundException e) {
            System.out.printf("[CitrusBuy] 驱动加载失败\n");
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        // 建立数据库连接
        String mysql_url = "jdbc:mysql://localhost:3306/citrusbuy?characterEncoding=utf-8&rewriteBatchedStatement=true";
        String mysql_username = "root";
        String mysql_pswd = "mysql330388bkTML";

        try {
            connection = DriverManager.getConnection(mysql_url, mysql_username, mysql_pswd);
            System.out.printf("[CitrusBuy] GetDataServlet: 数据库连接成功\n");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // 判断 submit 类型是删除还是修改
        String submitModify = req.getParameter("modify");
        String submitDelete = req.getParameter("delete");
        String submitType = null;
        if (submitModify != null) {
            submitType = "m";
        }
        else if (submitDelete != null){
            submitType = "d";
        }

        // 获取表单信息
        String p_id = req.getParameter("productID"); // 新建时，ID 为 null
        String p_name = req.getParameter("productName");
        String p_price = req.getParameter("productPrice");
        String p_category = req.getParameter("productCategory");
        Part filePart = req.getPart("productImage");
        String sql;

        System.out.printf("%s", p_id);

        // 将图片转换为 blob 类型
        byte[] imageBytes = null;
        if (filePart != null)  {
            InputStream inputStream = filePart.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[16000000];
            int bytesRead = -1;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                System.out.println("Read " + bytesRead + " bytes\n");
            }
            imageBytes = outputStream.toByteArray();
        }
        else {
            System.out.printf("[CitrusBuy] 文件导入失败");
        }

        // 储存到数据库
        try {
            if (submitType.equals("m")) {
                if (p_id != null && !p_id.isEmpty()) {
                    sql = "INSERT INTO `citrusbuy`.`goods` (id, name, price, img, category) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE name = ?, price = ?, img = ?, category = ?;";
                    statement = connection.prepareCall(sql);
                    statement.setInt(1, Integer.parseInt(p_id));
                    statement.setString(2, p_name);
                    statement.setString(3, p_price);
                    statement.setBytes(4, imageBytes);
                    statement.setString(5, p_category);
                    statement.setString(6, p_name);
                    statement.setString(7, p_price);
                    statement.setBytes(8, imageBytes);
                    statement.setString(9, p_category);
                }
                else {
                    sql = "INSERT INTO `citrusbuy`.`goods` (name, price, img, category) VALUES (?, ?, ?, ?)";
                    statement = connection.prepareCall(sql);
                    statement.setString(1, p_name);
                    statement.setString(2, p_price);
                    statement.setBytes(3, imageBytes);
                    statement.setString(4, p_category);
                }
                int success = statement.executeUpdate();
                if (success != 0) {
                    System.out.printf("[CitrusBuy] 成功保存商品" + p_name + "的数据\n");

                    // 向客户端发送成功消息
                    resp.getWriter().write("商品信息成功保存");
                    resp.setStatus(HttpServletResponse.SC_OK);
                }
            }
            else if (submitType.equals("d")) {
                sql = "DELETE FROM goods WHERE id = ?;";
                statement = connection.prepareCall(sql);
                statement.setString(1, p_id);

                int success = statement.executeUpdate();
                if (success != 0) {
                    System.out.printf("[CitrusBuy] 成功删除商品" + p_name + "的数据\n");

                    // 向客户端发送成功消息
                    resp.getWriter().write("商品信息成功保存");
                    resp.setStatus(HttpServletResponse.SC_OK);
                }
            }
            req.getRequestDispatcher("productManagement.jsp").forward(req, resp);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
