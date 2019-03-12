package com.raokii.web.ytalker.push.bean.api.user;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;
import com.raokii.web.ytalker.push.bean.db.User;

/**
 * @author Rao
 * @date 2019/2/17
 */
public class UpdateInfoModel {
    @Expose
    private String name;
    @Expose
    private int sex;
    @Expose
    private String desc;
    @Expose
    private String portrait;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    /**
     * 把当前的信息，填充到用户Model中
     * 方便UserModel进行写入
     *
     * @param user User Model
     * @return User Model
     */
    public User updateToUser(User user) {
        if (!Strings.isNullOrEmpty(name))
            user.setName(name);

        if (!Strings.isNullOrEmpty(portrait))
            user.setPortrait(portrait);

        if (!Strings.isNullOrEmpty(desc))
            user.setDescription(desc);

        if (sex != 0)
            user.setSex(sex);

        return user;
    }

    public static boolean check(UpdateInfoModel model) {
        // Model 不允许为null，
        // 并且只需要具有一个及其以上的参数即可
        return model != null
                && (!Strings.isNullOrEmpty(model.name) ||
                !Strings.isNullOrEmpty(model.portrait) ||
                !Strings.isNullOrEmpty(model.desc) ||
                model.sex != 0);
    }
}
