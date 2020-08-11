package OnlineMusic.servlet;

import OnlineMusic.Dao.MusicDao;
import OnlineMusic.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/loveMusic")
public class LoveMusicServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");


        String idStr = req.getParameter("id");
        int musicId = Integer.parseInt(idStr);

        User user = (User) req.getSession().getAttribute("user");
        int userid = user.getId();

        MusicDao musicDao = new MusicDao();
        // 以前这首歌是否被你添加过为喜欢 因为不能重复添加
        boolean effect = musicDao.findLoveMusicByMusicId(userid, musicId);

        Map<String ,Object> return_map = new HashMap<>();

        if(effect) {
            //以前这首歌被你添加过为喜欢 不能重复添加
            return_map.put("msg",false);
        }else {
            boolean flg = musicDao.insertLoveMusic(userid, musicId);
            if(flg) {
                return_map.put("msg",true);
            }else {
                return_map.put("msg",false);
            }
        }

        //如何将return_map返回给前端
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);

    }
}
