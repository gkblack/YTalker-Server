package com.raokii.web.ytalker.push.factory;

import com.google.common.base.Strings;
import com.raokii.web.ytalker.push.bean.card.UserCard;
import com.raokii.web.ytalker.push.bean.db.User;
import com.raokii.web.ytalker.push.bean.db.UserFollow;
import com.raokii.web.ytalker.push.utils.Hib;
import com.raokii.web.ytalker.push.utils.TextUtil;
import org.hibernate.mapping.Collection;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Rao
 * @date 2019/2/28
 */
public class UserFactory {

    /**
     * 根据token查找用户
     *
     * @param token
     * @return
     */
    public static User findByToken(String token) {
        return (User) Hib.query(session ->
                session.createQuery("from User where token=:token")
                        .setParameter("token", token)
                        .uniqueResult()
        );
    }

    public static User findByName(String name) {
        return (User) Hib.query(session ->
                session.createQuery("from User where name=:name")
                        .setParameter("name", name)
                        .uniqueResult()

        );
    }

    // 通过Phone找到User
    public static User findByPhone(String phone) {
        return Hib.query(session -> (User) session
                .createQuery("from User where phone=:inPhone")
                .setParameter("inPhone", phone)
                .uniqueResult());
    }

    public static User findById(String id) {
        return Hib.query(session -> (User) session.get(User.class, id));
//        return (User) Hib.query(session ->
//                session.createQuery("from User where id=:id")
//                        .setParameter("id", id)
//                        .uniqueResult());
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    public static User update(User user) {
        return Hib.query(session -> {
            session.saveOrUpdate(user);
            return user;
        });
    }


    /**
     * 绑定用户id
     *
     * @param user   当前用户信息
     * @param pushId 用户该次登录pushId
     * @return
     */
    public static User bindPushId(User user, String pushId) {
        if (Strings.isNullOrEmpty(pushId)) {
            return null;
        }

        // 查询是否有其他账户绑定了该设备
        // 取消之前的绑定
        // 查询的列表不包括自身
        Hib.queryOnly(session -> {
            List<User> userList = (List<User>) session
                    .createQuery("from User where lower(pushId)=:pushId and id!=:userId") //lower 忽略大小写
                    .setParameter("pushId", pushId.toLowerCase())
                    .setParameter("userId", user.getId())
                    .list();

            for (User u : userList) {
                // 将之前绑定的用户pushId置为null
                u.setPushId(null);
                session.saveOrUpdate(u);
            }
        });

        if (pushId.equalsIgnoreCase(user.getPushId())) {
            // 如果之前该用户已经绑定过了当前id则直接返回
            return user;
        } else {
            // 如果当前账户要绑定的设备id和之前不同，则需要让之前的设备退出并给之前绑定的用户设备推送退出消息
            if (Strings.isNullOrEmpty(user.getPushId())) {
                PushFactory.pushLogout(user, user.getPushId());
            }
        }
        user.setPushId(pushId);
        return update(user);
    }

    /**
     * 用戶注冊
     * 写入数据库，返回数据库中的User信息
     * 注册成功后生成登录token
     *
     * @param account
     * @param password
     * @param name
     * @return
     */
    public static User register(String account, String password, String name) {
        account = account.trim();
        password = encodePassword(password);

        User user = createUser(account, password, name);
        if (user != null) {
            user = login(user);
        }
        return user;
    }

    /**
     * @param account
     * @param password
     * @return
     */
    public static User login(String account, String password) {
        final String phone = account.trim();
        final String pwd = encodePassword(password);
        // 查询是否有该用户数据
        User user = (User) Hib.query(session -> session
                .createQuery("from User where phone=:phone and password=:password")
                .setParameter("phone", phone)
                .setParameter("password", pwd)
                .uniqueResult());
        if (user != null) {
            user = login(user);
        }
        return user;
    }


    /**
     * 登录时生成token
     *
     * @param user
     * @return
     */
    public static User login(User user) {
        // 生成随机的uuid为token
        String newToken = UUID.randomUUID().toString();
        newToken = TextUtil.encodeBase64(newToken);
        user.setToken(newToken);
        return update(user);
    }

    /**
     * 创建帐号数据
     *
     * @param account
     * @param password
     * @param name
     * @return
     */
    public static User createUser(String account, String password, String name) {
        User user = new User();
        user.setPhone(account);
        user.setName(name);
        user.setPassword(password);

        return (User) Hib.query(session -> {
            session.save(user);
            return user;
        });
    }

    /**
     * 对密码进行加密，登录和注册都需要进行此步骤
     */
    private static String encodePassword(String password) {
        password = password.trim();
        password = TextUtil.getMD5(password);
        return TextUtil.encodeBase64(password);
    }

    /**
     * 搜索联系人
     *
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<User> search(String name) {
        if (Strings.isNullOrEmpty(name)) {
            name = ""; // 防止name为null所导致的后续判断问题和查询报错
        }
        String searchName = "%" + name + "%"; // 模糊查询
        return Hib.query(session -> (List<User>) session
                .createQuery("from User where lower(name) like :name and portrait is not null" +
                        "and description is not null")
                .setParameter("name", searchName)
                .setMaxResults(20) // 至多查询20条数据
                .list());

    }

    /**
     * 获取联系人列表
     *
     * @param self
     * @return
     */
    public static List<User> contacts(User self) {
        if (self == null) {
            return null;
        }
        return Hib.query(session -> {
            // 重新加载一次用户信息到self中，和当前session绑定
            // 直接self.getFollowing 会报空，因为之前的session已经关闭了，需要重新获取session才能取到值
            session.load(self, self.getId());

            // 获取关注列表
            Set<UserFollow> follows = self.getFollowers();


            return follows.stream() // 替代for循环方式去赋值
                    .map(UserFollow::getTarget)
                    .collect(Collectors.toList());
        });
    }

    public static User follow(User origin, User target, String alias) {
        UserFollow follow = getUserFollow(origin, target);
        if(follow !=null){
            // 已经关注则直接返回
            return follow.getTarget();
        }

        return Hib.query(session -> {
            // 操作懒加载的数据需要重新load
            session.load(origin, origin.getId());
            session.load(target, target.getId());
            // 我关注他的时候，同时他也关注我
            // 需要加载双方的UserFollow数据
            UserFollow originFollow = new UserFollow();
            originFollow.setOrigin(origin);
            originFollow.setTarget(target);
            // 我对关注的人的备注
            originFollow.setAlias(alias);

            // 发起人是他时，我是被关注的人的记录
            UserFollow targetFollow = new UserFollow();
            targetFollow.setOrigin(target);
            targetFollow.setTarget(origin);

            // 保存到数据库中
            session.save(originFollow);
            session.save(targetFollow);

            return target;
        });
    }

    /**
     * 查询双方是否相互关注
     * @param origin 发起人
     * @param target 被关注人
     * @return 返回中间类UserFollow
     */
    public static UserFollow getUserFollow(User origin, User target) {
        return Hib.query(session -> (UserFollow) session
                .createQuery("from UserFollow where originId =:originId and targetId =:targetId")
                .setParameter("originId", origin.getId())
                .setParameter("targetId", target.getId())
                .setMaxResults(1)
                // 唯一查询返回
                .uniqueResult());
    }


}
