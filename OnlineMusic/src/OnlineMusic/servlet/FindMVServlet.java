package OnlineMusic.servlet;

import OnlineMusic.Dao.MVDao;
import OnlineMusic.entity.MV;
import com.fasterxml.jackson.databind.ObjectMapper;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/findMV")
public class FindMVServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        String mvName = req.getParameter("mvName");
        MVDao mvDao = new MVDao();
        List<MV> mvList = new ArrayList<>();

        if(mvName != null) {
            mvList = mvDao.ifMV(mvName);
        }else {
            mvList = mvDao.findMV();
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(), mvList);
    }
}

