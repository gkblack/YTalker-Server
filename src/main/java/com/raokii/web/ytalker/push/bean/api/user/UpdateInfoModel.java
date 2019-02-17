package com.raokii.web.ytalker.push.bean.api.user;

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

    public UpdateInfoModel(User user){
        this.name = user.getName();
        this.sex = user.getSex();
        this.desc = user.getDesc();
        this.portrait = user.getPortrait();
    }

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
}
