import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

import java.sql.*;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
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
            System.out.printf("[CitrusBuy] LoginServlet: 数据库连接成功\n");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // 获取用户名和密码
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String db_password = "";
        String sql;

        // 从数据库读取密码
        try {
            sql = "SELECT * FROM `citrusbuy`.`users` WHERE `username` = ?";
            statement = connection.prepareCall(sql);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                db_password = resultSet.getString("password");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // 验证用户名和密码是否正确
        if (!username.isEmpty() && password.equals(db_password)) {
            try {
                // 建立新的会话，把用户名储存到会话属性里
                // 使得用户可以在主页看到“欢迎您！（用户名）”
                HttpSession session = req.getSession(true);
                session.setAttribute("username", username);
                req.getRequestDispatcher("index.jsp").forward(req, resp);
                statement.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            req.setAttribute("Msg", "用户名或密码错误");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
