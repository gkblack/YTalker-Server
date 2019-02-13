package com.raokii.web.ytalker.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Rao
 * @date 2019/2/13
 */
@Entity
@Table(name = "TB_PUSH_HISTORY")
public class PushHistory {


    // 主键
    @Id
    @PrimaryKeyJoinColumn
    // 主键生成存储的类型为uuid
    @GeneratedValue(generator = "uuid")
    // 把uuid的生成器定义为uuid2， uuid2 是常规的uuid toString
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false,nullable = false)
    private String id;


    // 创建时写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime arrivalAt;


}
