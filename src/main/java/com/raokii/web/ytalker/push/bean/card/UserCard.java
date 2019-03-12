package com.raokii.web.ytalker.push.bean.card;

import com.google.gson.annotations.Expose;
import com.raokii.web.ytalker.push.bean.db.User;
import com.raokii.web.ytalker.push.utils.Hib;

import java.time.LocalDateTime;

/**
 * 用户查询所能看到信息
 * @author Rao
 * @date 2019/2/15
 */
public class UserCard {

    @Expose
    private String id;

    @Expose
    private String name;

    @Expose
    private String phone;

    // 头像
    @Expose
    private String protrait;

    // 描述
    @Expose
    private String desc;

    @Expose
    private int sex = 0;

    // 自己关注的数量
    @Expose
    private int follows;

    // 粉丝数量
    @Expose
    private int following;

    // 我与当前User的状态，是否关注了这个人
    @Expose
    private boolean isFollow;

    // 信息的最后更新时间
    @Expose
    private LocalDateTime modifyAt;

    public UserCard(User user){
        this(user, false);
    }

    public UserCard(final User user,boolean isFollow) {
        this.isFollow = isFollow;

        this.id = user.getId();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.sex = user.getSex();
        this.desc = user.getDescription();
        this.protrait = user.getPortrait();
        this.modifyAt = user.getUpdateAt();

        // 懒加载下没有session，会报错
        Hib.queryOnly(session -> {
            // 加载用户信息
            session.load(user,user.getId());
            // 仅仅是进行数量查询，不遍历集合
            // 要查询集合，必须在session存在情况下遍历
            // 或使用Hibernate.initalize(user.getFollowers());
            follows = user.getFollowers().size();
            following = user.getFollowing().size();
        });
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProtrait() {
        return protrait;
    }

    public void setProtrait(String protrait) {
        this.protrait = protrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public LocalDateTime getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(LocalDateTime modifyAt) {
        this.modifyAt = modifyAt;
    }
}
