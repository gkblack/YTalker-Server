package com.raokii.web.ytalker.push.bean.db;

import com.raokii.web.ytalker.push.bean.api.group.GroupCreateModel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Rao
 * @date 2019/2/11
 */
@Entity
@Table(name = "TB_GROUP")
public class Group {

    // 主键
    @Id
    @PrimaryKeyJoinColumn
    // 主键生成存储的类型为UUID,自动生成uuid
    @GeneratedValue(generator = "uuid")
    // 把uuid的生成器定义为uuid2， uuid2是常规的uuid toString
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    // 不允许修改， 不允许为null
    @Column(updatable = false, nullable = false)
    private String id;

    // 群组描述
    @Column(nullable = false)
    private String description;

    // 群组名
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String picture;

    // 定义为创建时间戳，在创建时就写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime creatAt = LocalDateTime.now();

    // 更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    // 群创建者
    // optional为false ，必须有一个创建者
    // fetch 急加载
    // cascade 联级级别为all, 更新时，所有更改都将进行关系更新
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ownerId")
    private User owner;
    @Column(nullable = false, updatable = false, insertable = false)
    private String ownerId;

    public Group(){

    }

    public Group(User owner, GroupCreateModel model){
        this.owner = owner;
        this.name = model.getName();
        this.description = model.getDesc();
        this.picture = model.getPicture();

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
