package com.raokii.web.ytalker.push.bean.card;

import com.google.gson.annotations.Expose;
import com.raokii.web.ytalker.push.bean.db.Group;
import com.raokii.web.ytalker.push.bean.db.GroupMember;

import java.time.LocalDateTime;

/**
 * @author Rao
 * @date 2019/2/17
 */
public class GroupCard {
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String ownerId;
    @Expose
    private String picture;
    @Expose
    private String desc;
    // 对当前用户的群通知的级别
    @Expose
    private int notifyLevel;
    @Expose
    private LocalDateTime joinAt;
    @Expose
    private LocalDateTime modifyAt;

    public GroupCard(GroupMember member){
        this(member.getGroup(), member);
    }

    public GroupCard(Group group){
        this(group, null);
    }

    public GroupCard(Group group,GroupMember member){
        this.id = group.getId();
        this.name = group.getName();
        this.ownerId = group.getOwner().getId();
        this.picture = group.getPicture();
        this.desc = group.getDescription();
        this.notifyLevel = member != null ? member.getNotifyLevel() : 0;
        this.joinAt = member != null ? member.getCreatAt() : null;
        this.modifyAt = group.getUpdateAt();
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwenrId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getNotifyLevel() {
        return notifyLevel;
    }

    public void setNotifyLevel(int notifyLevel) {
        this.notifyLevel = notifyLevel;
    }

    public LocalDateTime getJoinAt() {
        return joinAt;
    }

    public void setJoinAt(LocalDateTime joinAt) {
        this.joinAt = joinAt;
    }

    public LocalDateTime getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(LocalDateTime modifyAt) {
        this.modifyAt = modifyAt;
    }
}
