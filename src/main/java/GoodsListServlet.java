import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

import java.io.PrintWriter;
import java.sql.*;
import java.util.Base64;

@WebServlet(name = "GoodsListServlet", value = "/GoodsListServlet")
public class GoodsListServlet extends HttpServlet {
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
        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();

        // 建立数据库连接
        String mysql_url = "jdbc:mysql://localhost:3306/citrusbuy?characterEncoding=utf-8&rewriteBatchedStatement=true";
        String mysql_username = "root";
        String mysql_pswd = "mysql330388bkTML";

        try {
            connection = DriverManager.getConnection(mysql_url, mysql_username, mysql_pswd);
            System.out.printf("[CitrusBuy] GoodsListServlet: 数据库连接成功\n");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        String sql;

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
                out.println("<li>");
                out.println("<div class=\"mng-gl-item-wrap\" data-p_id=\"" + p_id + "\" data-p_name=\"" + p_name + "\" data-p_price=\"" + p_price + "\" data-p_category=\"" + p_category + "\">");
                out.println("<div class=\"mng-gli-id\">");
                out.println("<a href=\"#\" onclick=\"showContent(event)\">");
                out.println("<p>商品编号：" + p_id + "</p>");
                out.println("</a>");
                out.println("</div>");
                out.println("<div class=\"mng-gli-name\">");
                out.println("<a href=\"#\" onclick=\"showContent(event)\">");
                out.println("<p>商品名称：" + p_name + "</p>");
                out.println("</a>");
                out.println("</div>");
                out.println("</div>");
                out.println("</li>");
            }
            statement.close();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
