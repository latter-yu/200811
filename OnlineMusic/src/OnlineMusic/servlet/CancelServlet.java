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

@WebServlet("/cancel")
public class CancelServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        User user = (User) req.getSession().getAttribute("user");
        Map<String ,Object> return_map = new HashMap<>();

        if (user == null) {
            return_map.put("msg",false);
        } else {
            UserService userService = new UserService();
            int ret = userService.cancel(user);

            if (ret == 0) {
                System.out.println("注销账号失败！");
                return_map.put("msg", false);
            } else {
                System.out.println("注销账号成功！");
                return_map.put("msg", true);
            }
        }

        //如何将return_map返回给前端
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);
    }
}

