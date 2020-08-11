package OnlineMusic.Dao;

import OnlineMusic.entity.Music;
import OnlineMusic.util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MusicDao {
    //  有关音乐的数据库操作

    public List<Music> findMusic(){
        // 查询全部歌单
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null; // 结果集
        List<Music> musicList = new ArrayList();

        try {
            String sql = "select * from music";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql); // 对 sql 语句的预编译
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Music music = new Music();
                music.setId(resultSet.getInt("id"));
                music.setTitle(resultSet.getString("title"));
                music.setSinger(resultSet.getString("singer"));
                music.setTime(resultSet.getDate("time"));
                music.setUrl(resultSet.getString("url"));
                music.setUserid(resultSet.getInt("userid"));
                musicList.add(music);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, resultSet);
        }
        return musicList;
    }

    public Music findMusicById(int id){
        // 根据 id 查找音乐

        Music music = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            String sql = "select * from music where id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql); // 对 sql 语句的预编译
            ps.setInt(1, id);
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                music = new Music();
                music.setId(resultSet.getInt("id"));
                music.setTitle(resultSet.getString("title"));
                music.setSinger(resultSet.getString("singer"));
                music.setTime(resultSet.getDate("time"));
                music.setUrl(resultSet.getString("url"));
                music.setUserid(resultSet.getInt("userid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, resultSet);
        }
        return music;
    }

    public List<Music> ifMusic(String str){
        // 根据关键字查询歌单(不是精确查找，可能查出多首歌)

        List<Music> musicList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            String sql = "select * from music where title like BINARY '%" + str + "%'";
            connection = DBUtils.getConnection();
//            ps = connection.prepareStatement("select * from music where title like title=?"); // 模糊查询
//            ps.setString(1, str);
            ps = connection.prepareStatement(sql);
            resultSet = ps.executeQuery(); // 执行
            while (resultSet.next()) {
                Music music = new Music();
                music.setId(resultSet.getInt("id"));
                music.setTitle(resultSet.getString("title"));
                music.setSinger(resultSet.getString("singer"));
                music.setTime(resultSet.getDate("time"));
                music.setUrl(resultSet.getString("url"));
                music.setUserid(resultSet.getInt("userid"));
                musicList.add(music);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, resultSet);
        }
        return musicList;
    }

    public int insert(String title, String singer, String time, String url, int userid) {
        // 上传音乐
        // 1. 上传文件到服务器
        // 2. 将歌曲信息插入到数据库

        Connection connection = DBUtils.getConnection();
        PreparedStatement ps = null;
        int number = 0;
        try {
            ps = connection.prepareStatement("insert into music(title, singer, time, url, userid) values(?, ?, ?, ?, ?)");
            ps.setString(1, title);
            ps.setString(2, singer);
            ps.setString(3, time);
            ps.setString(4, url);
            ps.setInt(5, userid);
            number = ps.executeUpdate(); // 影响的行数
            // ps.executeUpdate(); 更新数据库
            return number;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, null);
        }
        return 0;
    }
    public boolean findMusicByMusicId(String title, String singer, String url, int userid) {
        // 判断一首歌是否已经上传过

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            String sql = "select * from music where title=? and singer=? and url=? and userid=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, resultSet);
        }
        return false;
    }

    public int deleteMusicById(int id) {
        // 删除音乐

        Connection connection = null;
        PreparedStatement ps = null;
        int ret = 0;
        try {
            String sql = "delete from music where id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ret = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, null);
        }
        return ret;
    }
    public boolean findLoveMusicOnDel(int musicId) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            String sql = "select * from lovemusic where music_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, musicId);
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, resultSet);
        }
        return false;
    }
    public int removeLoveMusicOnDelete(int musicid) {
        // 当删除服务器上的音乐时，同时在我喜欢的列表的数据库中进行删除

        Connection connection = null;
        PreparedStatement ps = null;
        int ret = 0;

        try {
            String sql = "delete from lovemusic where music_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, musicid);
            ret = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, null);
        }
        return ret;
    }

    public boolean insertLoveMusic(int userId, int musicId) {
        // 添加音乐到 lovemusic 中
        // 1. 先判断该音乐是否存在
        // 2. 不存在则添加

        Connection connection = DBUtils.getConnection();
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement("insert into lovemusic(user_id, music_id) values(?, ?)");
            ps.setInt(1, userId);
            ps.setInt(2, musicId);
            int ret = ps.executeUpdate(); // 影响的行数
            // ps.executeUpdate(); 更新数据库
            if (ret == 1) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, null);
        }
        return false;
    }
    public boolean findLoveMusicByMusicId(int user_id, int music_id) {
        // 判断一首歌是否已经添加过喜欢的音乐

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            String sql = "select * from lovemusic where user_id=? and music_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, user_id);
            ps.setInt(2, music_id);
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, resultSet);
        }
        return false;
    }

    public int removeLoveMusic(int userId,int musicId) {
        // 移除当前用户喜欢的这首音乐(因为同一首音乐可能多个用户喜欢)

        Connection connection = null;
        PreparedStatement ps = null;
        int ret = 0;

        try {
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement("delete from lovemusic where user_id=? and music_id=?");
            ps.setInt(1, userId);
            ps.setInt(2, musicId);
            ret = ps.executeUpdate(); // 影响的行数
            // ps.executeUpdate(); 更新数据库
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, null);
        }
        return ret;
    }

    public List<Music> findLoveMusic(int user_id) {
        // 查询用户喜欢的全部歌单

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Music> musicList = new ArrayList();

        try {
            String sql = "select music.id, title, singer, time, url, userid from music, lovemusic where lovemusic.music_id=music.id and user_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql); // 对 sql 语句的预编译
            ps.setInt(1, user_id); // 值与输入的 user_id 相同
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Music music = new Music();
                music.setId(resultSet.getInt("id"));
                music.setTitle(resultSet.getString("title"));
                music.setSinger(resultSet.getString("singer"));
                music.setTime(resultSet.getDate("time"));
                music.setUrl(resultSet.getString("url"));
                music.setUserid(resultSet.getInt("userid"));
                musicList.add(music);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, resultSet);
        }
        return musicList;
    }

    public List<Music> ifMusicLove(String str,int user_id){
        // 根据关键字查询喜欢的歌单

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Music> musicList = new ArrayList();

        try {
            String sql = "select music.id, title, singer, time, url, userid from music, lovemusic where lovemusic.music_id=music.id and title like '%" + str + "%' and user_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, user_id);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Music music = new Music();
                music.setId(resultSet.getInt("id"));
                music.setTitle(resultSet.getString("title"));
                music.setSinger(resultSet.getString("singer"));
                music.setTime(resultSet.getDate("time"));
                music.setUrl(resultSet.getString("url"));
                music.setUserid(resultSet.getInt("userid"));
                musicList.add(music);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, resultSet);
        }
        return musicList;
    }


//    public static void main(String[] args) {
//        List<Music> ret = findMusic();
//        System.out.println(ret);

//        Music music = findMusicById(1);
//        Music music1 = findMusicById(2);
//        System.out.println(music); // 能找到
//        System.out.println(music1); // null

//        List<Music> musicList = ifMusic("光");
//        System.out.println(musicList);

//        insert("竹石", "肖战", "2020/07/11", "https://www.music.com", 1);

//        System.out.println(deleteMusicById(2));

//        insertLoveMusic(1, 3);

//        removeLoveMusic(3, 1);

//        List<Music> musicList = findLoveMusic(1);
//        System.out.println(musicList);

//        List<Music> musicList = ifMusicLove("竹", 1);
//        System.out.println(musicList);

//        boolean ret = findLoveMusicByMusicId(24,6);
//        System.out.println(ret);
//    }
}
