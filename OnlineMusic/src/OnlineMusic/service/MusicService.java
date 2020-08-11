package OnlineMusic.service;

import OnlineMusic.Dao.MusicDao;
import OnlineMusic.entity.Music;

import java.util.List;

// 与 Dao 层分离开，加强代码扩展性
// 调用 Dao 层函数，体现业务逻辑
public class MusicService {
    MusicDao musicDao = new MusicDao();

    public List<Music> findMusic() {
        // 查询全部歌单
        List<Music> musicList = musicDao.findMusic();
        return musicList;
    }

    public Music findMusicById(int id) {
        // 根据 id 查找音乐
        Music music = musicDao.findMusicById(id);
        return music;
    }

    public List<Music> ifMusic(String str) {
        // 根据关键字查询歌单(不是精确查找，可能查出多首歌)
        List<Music> musicList = musicDao.ifMusic(str);
        return musicList;
    }

    public int insert(String title, String singer, String time, String url, int userid) {
        // 上传音乐
        int number = 0;
        if (musicDao.findMusicByMusicId(title, singer, url, userid) == false) {
            musicDao.insert(title, singer, time, url, userid);
            number++;
        }
        return number;
    }

    public int deleteMusicById(int id) {
        // 删除音乐
        int ret = 0;
        ret = musicDao.deleteMusicById(id);
        if (ret == 1) {
            // music 表中删除成功

            // 同时删除中间表中的数据(我喜欢列表)
            // 看中间表是否有数据，如果有删除
            if(musicDao.findLoveMusicOnDel(id)) {
                int resout =  musicDao.removeLoveMusicOnDelete(id);
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

    public boolean insertLoveMusic(int userId, int musicId) {
        // 添加到 lovemusic 列表
        if (musicDao.findLoveMusicByMusicId(userId, musicId)) {
            return false;
        }
        boolean ret = musicDao.insertLoveMusic(userId, musicId);
        return ret;
    }

    public int removeLoveMusic(int userId,int musicId) {
        // 移除当前用户喜欢的这首音乐(因为同一首音乐可能多个用户喜欢)
        int ret = musicDao.removeLoveMusic(userId, musicId);
        return ret;
    }

    public List<Music> findLoveMusic(int user_id) {
        // 查询用户喜欢的全部歌单
        List<Music> musicList = musicDao.findLoveMusic(user_id);
        return musicList;
    }

    public List<Music> ifMusicLove(String str,int user_id) {
        // 根据关键字查询喜欢的歌单
        List<Music> musicList = musicDao.ifMusicLove(str, user_id);
        return musicList;
    }
}
