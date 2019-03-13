package com.raokii.web.ytalker.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Rao
 * @date 2019/2/13
 */
@Entity
@Table(name = "TB_MESSAGE")
public class Message {
    // 针对人的
    public static final int RECEIVE_TYPE_NONE = 0;
    // 针对群的
    public static final int RECEIVE_TYPE_GROUP = 1;

    // 文本类型
    public static final int TYPE_STR = 1; // 文本
    public static final int TYPE_PIC = 2; // 图片
    public static final int TYPE_FILE = 3; // 文件
    public static final int TYPE_AUD = 4; // 音频


    // 主键
    @Id
    @PrimaryKeyJoinColumn
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    // 不允许修改， 不允许为null
    @Column(updatable = false, nullable = false)
    private String id;

    // 文本，类型为TEXT
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 附件
    @Column
    private String attach;

    @Column(nullable = false)
    private int type;



    // 发送者，不为空
    @ManyToOne(optional = false)
    // 对应 User.id
    @JoinColumn(name = "senderId")
    private User sender;
    // senderId 是从user表中拿到的，不可修改
    @Column(nullable = false, updatable = false, insertable = false)
    private String senderId;

    // 多个消息对应一个接收者
    // Many对应这个类， One对应User
    @ManyToOne
    // 对应 User.id
    @JoinColumn(name = "receiverId")
    private User receiver;
    @Column(updatable = false, insertable = false)
    private String receiverId;

    // 一个群可以接受多条消息
    @ManyToOne
    // 对应 Group.id
    @JoinColumn(name = "groupId")
    private Group group;
    @Column(updatable = false, insertable = false)
    private String groupId;


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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
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
}
