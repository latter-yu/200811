package OnlineMusic.Dao;

import OnlineMusic.entity.MV;
import OnlineMusic.util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MVDao {
    //  有关音乐的数据库操作

    public List<MV> findMV(){
        // 查询全部 mv
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null; // 结果集
        List<MV> mvList = new ArrayList();

        try {
            String sql = "select * from mv";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql); // 对 sql 语句的预编译
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                MV mv = new MV();
                mv.setId(resultSet.getInt("id"));
                mv.setTitle(resultSet.getString("title"));
                mv.setSinger(resultSet.getString("singer"));
                mv.setTime(resultSet.getDate("time"));
                mv.setUrl(resultSet.getString("url"));
                mv.setUserid(resultSet.getInt("userid"));
                mvList.add(mv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, resultSet);
        }
        return mvList;
    }

    public MV findmvById(int id){
        // 根据 id 查找 MV

        MV mv = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            String sql = "select * from mv where id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql); // 对 sql 语句的预编译
            ps.setInt(1, id);
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                mv = new MV();
                mv.setId(resultSet.getInt("id"));
                mv.setTitle(resultSet.getString("title"));
                mv.setSinger(resultSet.getString("singer"));
                mv.setTime(resultSet.getDate("time"));
                mv.setUrl(resultSet.getString("url"));
                mv.setUserid(resultSet.getInt("userid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, resultSet);
        }
        return mv;
    }

    public List<MV> ifMV(String str){
        // 根据关键字查询 MV(不是精确查找，可能查出多首歌)

        List<MV> mvList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            String sql = "select * from mv where title like BINARY '%" + str + "%'";
            connection = DBUtils.getConnection();
//            ps = connection.prepareStatement("select * from mv where title like title=?"); // 模糊查询
//            ps.setString(1, str);
            ps = connection.prepareStatement(sql);
            resultSet = ps.executeQuery(); // 执行
            while (resultSet.next()) {
                MV mv = new MV();
                mv.setId(resultSet.getInt("id"));
                mv.setTitle(resultSet.getString("title"));
                mv.setSinger(resultSet.getString("singer"));
                mv.setTime(resultSet.getDate("time"));
                mv.setUrl(resultSet.getString("url"));
                mv.setUserid(resultSet.getInt("userid"));
                mvList.add(mv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, resultSet);
        }
        return mvList;
    }

    public int insert(String title, String singer, String time, String url, int userid) {
        // 上传音乐
        // 1. 上传文件到服务器
        // 2. 将歌曲信息插入到数据库

        Connection connection = DBUtils.getConnection();
        PreparedStatement ps = null;
        int number = 0;
        try {
            ps = connection.prepareStatement("insert into mv(title, singer, time, url, userid) values(?, ?, ?, ?, ?)");
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
    public boolean findmvBymvId(String title, String singer, String url, int userid) {
        // 判断一首歌是否已经上传过

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            String sql = "select * from mv where title=? and singer=? and url=? and userid=?";
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

    public int deletemvById(int id) {
        // 删除音乐

        Connection connection = null;
        PreparedStatement ps = null;
        int ret = 0;
        try {
            String sql = "delete from mv where id=?";
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
    public boolean findLovemvOnDel(int mvId) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            String sql = "select * from lovemv where mv_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, mvId);
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
    public int removeLovemvOnDelete(int mvid) {
        // 当删除服务器上的音乐时，同时在我喜欢的列表的数据库中进行删除

        Connection connection = null;
        PreparedStatement ps = null;
        int ret = 0;

        try {
            String sql = "delete from lovemv where mv_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, mvid);
            ret = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, null);
        }
        return ret;
    }

    public boolean insertLovemv(int userId, int mvId) {
        // 添加音乐到 lovemv 中
        // 1. 先判断该音乐是否存在
        // 2. 不存在则添加

        Connection connection = DBUtils.getConnection();
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement("insert into lovemv(user_id, mv_id) values(?, ?)");
            ps.setInt(1, userId);
            ps.setInt(2, mvId);
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
    public boolean findLovemvBymvId(int user_id, int mv_id) {
        // 判断一首歌是否已经添加过喜欢的音乐

        boolean ret = false;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            String sql = "select * from lovemv where user_id=? and mv_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, user_id);
            ps.setInt(2, mv_id);
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                ret = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, resultSet);
        }
        return ret;
    }

    public int removeLovemv(int userId,int mvId) {
        // 移除当前用户喜欢的这首音乐(因为同一首音乐可能多个用户喜欢)

        Connection connection = null;
        PreparedStatement ps = null;
        int ret = 0;

        try {
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement("delete from lovemv where user_id=? and mv_id=?");
            ps.setInt(1, userId);
            ps.setInt(2, mvId);
            ret = ps.executeUpdate(); // 影响的行数
            // ps.executeUpdate(); 更新数据库
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, null);
        }
        return ret;
    }

    public List<MV> findLovemv(int user_id) {
        // 查询用户喜欢的全部歌单

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<MV> mvList = new ArrayList();

        try {
            String sql = "select mv.id, title, singer, time, url, userid from mv, lovemv where lovemv.mv_id=mv.id and user_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql); // 对 sql 语句的预编译
            ps.setInt(1, user_id); // 值与输入的 user_id 相同
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                MV mv = new MV();
                mv.setId(resultSet.getInt("id"));
                mv.setTitle(resultSet.getString("title"));
                mv.setSinger(resultSet.getString("singer"));
                mv.setTime(resultSet.getDate("time"));
                mv.setUrl(resultSet.getString("url"));
                mv.setUserid(resultSet.getInt("userid"));
                mvList.add(mv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, resultSet);
        }
        return mvList;
    }

    public List<MV> ifmvLove(String str,int user_id){
        // 根据关键字查询喜欢的歌单

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<MV> mvList = new ArrayList();

        try {
            String sql = "select mv.id, title, singer, time, url, userid from mv, lovemv where lovemv.mv_id=mv.id and title like '%" + str + "%' and user_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, user_id);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                MV mv = new MV();
                mv.setId(resultSet.getInt("id"));
                mv.setTitle(resultSet.getString("title"));
                mv.setSinger(resultSet.getString("singer"));
                mv.setTime(resultSet.getDate("time"));
                mv.setUrl(resultSet.getString("url"));
                mv.setUserid(resultSet.getInt("userid"));
                mvList.add(mv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, ps, resultSet);
        }
        return mvList;
    }


//   public static void main(String[] args) {
//        List<mv> ret = findmv();
//        System.out.println(ret);

//        mv mv = findmvById(1);
//        mv mv1 = findmvById(2);
//        System.out.println(mv); // 能找到
//        System.out.println(mv1); // null

//        List<mv> mvList = ifmv("光");
//        System.out.println(mvList);

//        insert("竹石", "肖战", "2020/07/11", "https://www.mv.com", 1);

//        System.out.println(deletemvById(2));

//        insertLovemv(1, 3);

//        removeLovemv(3, 1);

//        List<mv> mvList = findLovemv(1);
//        System.out.println(mvList);

//        List<mv> mvList = ifmvLove("竹", 1);
//        System.out.println(mvList);

//       boolean ret = findLovemvBymvId(20, 13);
//       System.out.println(ret);
//   }
}
