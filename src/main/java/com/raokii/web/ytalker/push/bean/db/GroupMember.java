package com.raokii.web.ytalker.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 群组Model
 * @author Rao
 * @date 2019/2/13
 */
@Entity
@Table(name = "TB_GROUP_MEMBER")
public class GroupMember {

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
    private int notifyLevel = 0;

    @Column(nullable = false)
    private int permissionType = 0;

    @Column(nullable = false)
    private String userId;

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



}
