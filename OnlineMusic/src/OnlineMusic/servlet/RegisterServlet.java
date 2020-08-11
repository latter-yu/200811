package OnlineMusic.servlet;

import OnlineMusic.entity.User;
import OnlineMusic.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User registerUser = new User();
        registerUser.setUsername(username);
        registerUser.setPassword(password);

        UserService userService = new UserService();
        int num = userService.findUser(registerUser);

        Map<String ,Object> return_map = new HashMap<>();

        if(num == 0) {
            System.out.println("注册成功！");
            return_map.put("msg", true);
            userService.register(registerUser);
        }else if (username == null || password == null) {
            return_map.put("msg", null);
        }else {
                System.out.println("注册失败！");
                return_map.put("msg",false);
        }

        //如何将return_map返回给前端
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);
    }
}
