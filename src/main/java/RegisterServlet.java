import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

import java.sql.*;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
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
            System.out.printf("[CitrusBuy] 数据库连接成功\n");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // 获取用户名和密码
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String db_username = "";
        String sql;

        // 从数据库查找，看看是否已经有这个用户了
        // 如果有，提示无法重复注册
        try {
            sql = "SELECT * FROM `citrusbuy`.`users` WHERE `username` = ?;";
            statement = connection.prepareCall(sql);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                db_username = resultSet.getString("username");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // 验证用户名和密码是否正确
        if (!db_username.isEmpty()) {
            req.setAttribute("Msg", "用户名已存在，无法重复注册");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        }
        else {
            try {
                sql = "INSERT INTO `citrusbuy`.`users` (username, password) VALUES (?, ?);";
                statement = connection.prepareCall(sql);
                statement.setString(1, username);
                statement.setString(2, password);
                int success = statement.executeUpdate();
                if (success != 0) {
                    System.out.printf("[CitrusBuy] 成功插入用户" + username + "的数据");
                }
                req.getRequestDispatcher("index.jsp").forward(req, resp);
                statement.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
