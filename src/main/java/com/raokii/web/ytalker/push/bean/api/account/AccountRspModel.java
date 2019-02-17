package com.raokii.web.ytalker.push.bean.api.account;

import com.google.gson.annotations.Expose;
import com.raokii.web.ytalker.push.bean.card.UserCard;
import com.raokii.web.ytalker.push.bean.db.User;

/**
 * 帐号部分返回的Model
 * @author Rao
 * @date 2019/2/14
 */
public class AccountRspModel {

    // 用户的基本信息
    @Expose
    private UserCard user;

    // 用户账号
    @Expose
    private String account;

    // 登录成功后返回的token
    // 通过token可以获取用户的所有信息
    @Expose
    private String token;

    // 判断是否绑定用户设备的pushId;
    @Expose
    private boolean isBind;

    public AccountRspModel(User user){
        // 默认无绑定
        this(user, false);
    }

    public AccountRspModel(User user, boolean isBind){
        this.user = new UserCard(user);
        this.account = user.getPhone();
        this.token = user.getToken();
        this.isBind = isBind;
    }

    public UserCard getUser() {
        return user;
    }

    public void setUser(UserCard user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }
}
