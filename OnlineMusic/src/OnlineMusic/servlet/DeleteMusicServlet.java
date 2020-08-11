package OnlineMusic.servlet;

import OnlineMusic.Dao.MusicDao;
import OnlineMusic.entity.Music;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/deleteMusic")
public class DeleteMusicServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        String idStr = req.getParameter("id");

        int id = Integer.parseInt(idStr);

        MusicDao musicDao = new MusicDao();
        //1、当前id的音乐是否存在
        Music music = musicDao.findMusicById(id);
        if(music == null) return;

        int delete = musicDao.deleteMusicById(id);

        Map<String ,Object> return_map = new HashMap<>();

        if(delete == 1) {
            //仅仅代表数据库删除了，但是服务器上的音乐是否存在
            //File file = new File("E:\\javacode\\Java16\\OnlineMusic\\web\\"+music.getUrl()+".mp3");

            File file = new File("E:\\java练习\\200723\\web\\" + music.getUrl()+".mp3");
            if(file.delete()) {
                return_map.put("msg",true);
                System.out.println("服务器删除成功！");
            }else {
                return_map.put("msg",false);
                System.out.println("服务器删除失败！");
            }
        }else {
            return_map.put("msg",false);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);

    }
}
