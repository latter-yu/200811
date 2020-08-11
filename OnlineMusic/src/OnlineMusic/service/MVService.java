package OnlineMusic.service;

import OnlineMusic.Dao.MVDao;
import OnlineMusic.entity.MV;

import java.util.List;

public class MVService {
    MVDao mvDao = new MVDao();

    public List<MV> findMV() {
        // 查询全部歌单
        List<MV> mvList = mvDao.findMV();
        return mvList;
    }

    public MV findmvById(int id) {
        // 根据 id 查找音乐
        MV mv = mvDao.findmvById(id);
        return mv;
    }

    public List<MV> ifMV(String str) {
        // 根据关键字查询歌单(不是精确查找，可能查出多首歌)
        List<MV> mvList =mvDao.ifMV(str);
        return mvList;
    }

    public int insert(String title, String singer, String time, String url, int userid) {
        // 上传音乐
        int number = 0;
        if (mvDao.findmvBymvId(title, singer, url, userid) == false) {
            mvDao.insert(title, singer, time, url, userid);
            number++;
        }
        return number;
    }

    public int deletemvById(int id) {
        // 删除音乐
        int ret = 0;
        ret = mvDao.deletemvById(id);
        if (ret == 1) {
            // music 表中删除成功

            // 同时删除中间表中的数据(我喜欢列表)
            // 看中间表是否有数据，如果有删除
            if(mvDao.findLovemvOnDel(id)) {
                int resout =  mvDao.removeLovemvOnDelete(id);
                if (resout == 1) {
                    // lovemusic 表中数据删除成功
                    return 1;
                }
            } else {
                // 如果没找到，说明没有被加入 lovemusic 列表
                return 1;
            }
        }
        return 0;
    }

    public boolean insertLovemv(int userId, int mvId) {
        // 添加到 lovemusic 列表
        if (mvDao.findLovemvBymvId(userId, mvId)) {
            return false;
        } else {
            boolean ret = mvDao.insertLovemv(userId, mvId);
            return ret;
        }
    }

    public int removeLovemv(int userId,int mvId) {
        // 移除当前用户喜欢的这首音乐(因为同一首音乐可能多个用户喜欢)
        int ret = mvDao.removeLovemv(userId, mvId);
        return ret;
    }

    public List<MV> findLovemv(int user_id) {
        // 查询用户喜欢的全部歌单
        List<MV> mvList = mvDao.findLovemv(user_id);
        return mvList;
    }

    public List<MV> ifmvLove(String str,int user_id) {
        // 根据关键字查询喜欢的歌单
        List<MV> mvList = mvDao.ifmvLove(str, user_id);
        return mvList;
    }
}
