package OnlineMusic.servlet;

import OnlineMusic.Dao.MVDao;
import OnlineMusic.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/uploadsuccessMV")
public class uploadsuccessMVServlet extends HttpServlet{
    // 将视频信息，写入到数据库当中

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        String singer =  req.getParameter("singer");

        String fileName = (String)req.getSession().getAttribute("fileName");

        String[] strings = fileName.split("\\.");
        String title = strings[0];

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 获得时间 格式：2020-07-29
        String time = sdf.format(new Date());

        User user = (User) req.getSession().getAttribute("user");
        int user_id = user.getId();

        String url = "mv\\"+ title;

        MVDao mvDao = new MVDao();
        int ret = mvDao.insert(title,singer,time,url,user_id);
        if(ret == 1) {
            resp.sendRedirect("MV.html");
        }
    }

}
