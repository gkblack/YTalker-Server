package com.raokii.web.ytalker.push.bean.db;

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
    @Column(updatable = false, nullable = false)
    private String id;

    // 推送实体存储的是json字符串
    // BLOB是比Text更大的字段类型
    @Column(nullable = false, columnDefinition = "BLOB")
    private String entity;

    //
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "receiverId")
    private User receiver;
    @Column(updatable = false, insertable = false)
    private String receiverId;

    //
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "senderId")
    private User sender;
    @Column(updatable = false, insertable = false)
    private String senderId;

    // 接收者当前状态下的设备推送id
    // User.pushId 可以为空
    @Column
    private String receiverPushId;

    // 消息送达时间，可以为空
    @Column
    private LocalDateTime arrivalAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
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

    public String getReceiverPushId() {
        return receiverPushId;
    }

    public void setReceiverPushId(String receiverPushId) {
        this.receiverPushId = receiverPushId;
    }

    public LocalDateTime getArrivalAt() {
        return arrivalAt;
    }

    public void setArrivalAt(LocalDateTime arrivalAt) {
        this.arrivalAt = arrivalAt;
    }
}
