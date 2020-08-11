package OnlineMusic.servlet;

import OnlineMusic.Dao.MVDao;
import OnlineMusic.entity.MV;
import OnlineMusic.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/findLovemv")
public class FindLovemvServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        String lovemvName = req.getParameter("lovemvName");
        MVDao mvDao = new MVDao();
        User user = (User) req.getSession().getAttribute("user");
        int user_id = user.getId();

        List<MV> mvList = new ArrayList<>();
        if(lovemvName != null) {
            mvList = mvDao.ifmvLove(lovemvName, user_id);
        }else {
            mvList = mvDao.findLovemv(user_id);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(), mvList);
    }
}

