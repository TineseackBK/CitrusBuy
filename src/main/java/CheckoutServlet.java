import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

@WebServlet(name = "CheckoutServlet", value = "/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
    private List<String> p_id = new ArrayList<>();
    private List<String> p_name = new ArrayList<>();
    private List<Integer> p_quantity = new ArrayList<>();
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

        // 写入 html 代码
        int size = p_id.size();
        for (int i = 0; i < size; i++) {
            String p_id_ele = p_id.get(i);
            String p_name_ele = p_name.get(i);
            int p_quantity_ele = p_quantity.get(i);
            out.println("<div class=\"checkout-goods-wrap\">");
            out.println("<div class=\"cg-id\">");
            out.println("<p>ID" + p_id_ele + "</p>");
            out.println("</div>");
            out.println("<div class=\"cg-name\">");
            out.println("<p>" + p_name_ele + "</p>");
            out.println("</div>");
            out.println("<div class=\"cg-quantity\">");
            out.println("<p>数量 " + p_quantity_ele + "</p>");
            out.println("</div><br>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 建立数据库连接
        String mysql_url = "jdbc:mysql://localhost:3306/citrusbuy?characterEncoding=utf-8&rewriteBatchedStatement=true";
        String mysql_username = "root";
        String mysql_pswd = "mysql330388bkTML";

        try (
            Connection connection = DriverManager.getConnection(mysql_url, mysql_username, mysql_pswd);
        ) {
            System.out.printf("[CitrusBuy] GetDataServlet: 数据库连接成功\n");

            String sql = "UPDATE `citrusbuy`.`goods` SET sales = sales + ? WHERE id = ?;";
            try (
                    PreparedStatement statement = connection.prepareStatement(sql);
            ) {
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = req.getReader();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                String jsonString = sb.toString();

                // 解析 JSON 对象
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

                // 获取用户信息
                String username = jsonObject.get("username").getAsString();
                String email = jsonObject.get("email").getAsString();

                // 获取购物车中的商品信息
                JsonArray cartItemsArray = jsonObject.getAsJsonArray("cartItems");

                // 遍历购物车中的每个商品
                for (JsonElement itemElement : cartItemsArray) {
                    JsonObject itemObject = itemElement.getAsJsonObject();
                    String productId = itemObject.get("productId").getAsString();
                    String productName = itemObject.get("productName").getAsString();
                    int quantity = itemObject.get("quantity").getAsInt();

                    // 更新数据库
                    statement.setInt(1, quantity);
                    statement.setString(2, productId);
                    int success = statement.executeUpdate();
                    if (success != 0) {
                        System.out.printf("[CitrusBuy] 成功更新数据库\n");
                    }

                    // 更新 checkout.jsp
                    p_id.add(productId);
                    p_name.add(productName);
                    p_quantity.add(quantity);
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        // 返回成功响应
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        out.print("结算成功");
    }
}
