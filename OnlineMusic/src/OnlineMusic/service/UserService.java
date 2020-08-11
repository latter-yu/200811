package OnlineMusic.service;

import OnlineMusic.Dao.UserDao;
import OnlineMusic.entity.User;

public class UserService {

    public int findUser(User findUser) {
        UserDao userDao = new UserDao();
        int num = userDao.findUser(findUser);
        return num;
    }

    public void register(User registerUser) {
        // 注册
        UserDao userDao = new UserDao();
        userDao.register(registerUser);
        return;
    }

    public User login(User loginUser) {
        // 登录
        UserDao userDao = new UserDao();
        User user = userDao.login(loginUser);
        return user;
    }

    public int cancel(User cancelUser) {
        UserDao userDao = new UserDao();
        int ret = userDao.cancel(cancelUser);
        return ret;
    }
}
