package com.raokii.web.ytalker.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 群组Model
 *
 * @author Rao
 * @date 2019/2/13
 */
@Entity
@Table(name = "TB_GROUP_MEMBER")
public class GroupMember {
    public static final int PERMISSION_TYPE_NONE = 0; // 默认权限，普通成员
    public static final int PERMISSION_TYPE_ADMIN = 1; // 管理员
    public static final int PERMISSION_TYPE_ADMIN_SU = 100; // 创建者

    public static final int NOTIFY_DEVEL_INVALID = -1; // 默认不接收消息
    public static final int NOTIFY_DEVEL_NONE = 0; // 默认通知级别
    public static final int NOTIFY_DEVEL_CLOSE = 1; // 接收消息但不提示

    // 主键
    @Id
    @PrimaryKeyJoinColumn
    // 主键生成存储的类型为UUID
    @GeneratedValue(generator = "uuid")
    // 把uuid的生成器定义为uuid2， uuid2是常规的uuid toString
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    // 不允许修改， 不允许为null
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    private int notifyLevel = NOTIFY_DEVEL_NONE;

    @Column(nullable = false)
    private int permissionType = PERMISSION_TYPE_NONE;

    @Column
    private String alias;

    // 定义为创建时间戳，在创建时就写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime creatAt = LocalDateTime.now();

    // 更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    // 成员信息对应的用户信息
    @JoinColumn(name = "userId")
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;
    @Column(nullable = false, updatable = false, insertable = false)
    private String userId;

    // 成员信息对应的群信息
    @JoinColumn(name = "groupId")
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Group group;
    @Column(nullable = false, updatable = false, insertable = false)
    private String groupId;

    public GroupMember(){

    }

    public GroupMember(User user, Group group) {
        this.user = user;
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNotifyLevel() {
        return notifyLevel;
    }

    public void setNotifyLevel(int notifyLevel) {
        this.notifyLevel = notifyLevel;
    }

    public int getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(int permissionType) {
        this.permissionType = permissionType;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public LocalDateTime getCreatAt() {
        return creatAt;
    }

    public void setCreatAt(LocalDateTime creatAt) {
        this.creatAt = creatAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
