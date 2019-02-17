package com.raokii.web.ytalker.push.bean.api.message;

import com.google.gson.annotations.Expose;
import com.raokii.web.ytalker.push.bean.db.Message;

/**
 * @author Rao
 * @date 2019/2/17
 */
public class MessageCreateModel {
    @Expose
    private String id;
    @Expose
    private String content;
    @Expose
    private int type = Message.TYPE_STR;
    // 接收者，可以为空
    @Expose
    private String receiveId;
    // 接收者的类型，群或人
    @Expose
    private int receiveType = Message.RECEIVE_TYPE_NONE;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public int getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(int receiveType) {
        this.receiveType = receiveType;
    }
}
