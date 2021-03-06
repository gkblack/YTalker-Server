package com.raokii.web.ytalker.push.bean.api.base;

import com.google.gson.annotations.Expose;
import com.raokii.web.ytalker.push.utils.TextUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *  推送的具体model，内部维持了一个数组，可添加多个实体
 *  每次推送的详细数据是：将实体数据进行json操作，然后发送json字符串
 *  这样如果有多个消息需要推送可以合并进行
 * @author Rao
 * @date 2019/2/17
 */
@SuppressWarnings("WeakerAccess")
public class PushModel {
    public static final int ENTITY_TYPE_LOGOUT = -1;
    public static final int ENTITY_TYPE_MESSAGE = 200;
    public static final int ENTITY_TYPE_ADD_FRIEND = 1001;
    public static final int ENTITY_TYPE_ADD_GROUP = 1002;
    public static final int ENTITY_TYPE_ADD_GROUP_MEMBERS = 1003;
    public static final int ENTITY_TYPE_MODIFY_GROUP_MEMBERS = 2001;
    public static final int ENTITY_TYPE_EXIT_GROUP_MEMBERS = 3001;

    private List<Entity> entities = new ArrayList<>();

    public PushModel add(Entity entity) {
        entities.add(entity);
        return this;
    }

    public PushModel add(int type, String content) {
        return add(new Entity(type, content));
    }

    public String getPushString() {
        if (entities.size() == 0)
            return null;
        return TextUtil.toJson(entities);
    }

    /**
     * 具体的实体类型，在这个实体中包装了实体的内容和类型
     * 比如添加好友的推送：
     * content：用户信息的Json字符串
     * type=ENTITY_TYPE_ADD_FRIEND
     */
    public static class Entity {
        public Entity(int type, String content) {
            this.type = type;
            this.content = content;
        }

        // 消息类型
        @Expose
        public int type;
        // 消息实体
        @Expose
        public String content;
        // 消息生成时间
        @Expose
        public LocalDateTime createAt = LocalDateTime.now();
    }
}
