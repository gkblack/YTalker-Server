package com.raokii.web.ytalker.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户关系的Model
 * 用于用户直接进行好友关系的实现
 * @author Rao
 * @date 2019/2/11
 */
@Entity
@Table(name = "TB_USER_FOLLOW")
public class UserFollow {

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

    // 定义一个发起人，你关注某人，这里就是你
    // 多对一 --> 你可以关注很多个人,你的每一次关注都是一条记录
    // 你可以创建多个关注的信息，所以是多对一
    // 这里的多对一是 User 对应多个UserFollow
    // option 不可选，必须存储，一条关注记录一定要有一个关注的人
    @ManyToOne(optional = false)
    // 定义管理的表字段名为orginId, 对应的是User.id
    @JoinColumn(name = "originId")
    private User origin;
    // 定义是数据库存储的字段
    // 把这个列提取到我们的Model中，不允许为空
    @Column(nullable = false, updatable = false, insertable = false)
    private String originId;

    // 定义关注的目标，你所关注的人
    // 多对一， 你可以被很多人关注，每次关注都是一条记录
    //可以 多个UserFollow 对应 一个User
    @ManyToOne(optional = false)
    // 定义管理的表字段名为targetId, 对应的是User.id
    @JoinColumn(name = "targetId")
    private User target;
    @Column(nullable = false, updatable = false, insertable = false)
    private String targetId;

    // 别名，即对target的备注名,可以为null
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getOrigin() {
        return origin;
    }

    public void setOrigin(User origin) {
        this.origin = origin;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
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

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
