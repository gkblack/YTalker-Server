package com.raokii.web.ytalker.push.factory;

import com.raokii.web.ytalker.push.bean.api.base.PushModel;
import com.raokii.web.ytalker.push.bean.card.UserCard;
import com.raokii.web.ytalker.push.bean.db.PushHistory;
import com.raokii.web.ytalker.push.bean.db.User;
import com.raokii.web.ytalker.push.utils.Hib;
import com.raokii.web.ytalker.push.utils.PushDispatcher;
import com.raokii.web.ytalker.push.utils.TextUtil;

/**
 * @author Rao
 * @date 2019/3/7
 */
public class PushFactory {

    /**
     * 退出登录
     * @param receiver
     * @param pushId
     */
    public static void pushLogout(User receiver,String pushId){
        PushHistory history = new PushHistory();

        history.setEntityType(PushModel.ENTITY_TYPE_LOGOUT);
        history.setEntity("Account logout");
        history.setReceiverPushId(pushId);
        history.setId(receiver.getId());

    }


    /**
     * 通知我关注的人消息，提示我已经关注了他
     * @param receiver 接收人
     * @param userCard 我的卡片信息
     */
    public static void pushFollow(User receiver, UserCard userCard){
        // 相互关注
        userCard.setFollow(true);
        String entity = TextUtil.toJson(userCard);

        // 历史字段表建立
        PushHistory history = new PushHistory();
        // 添加的历史类型
        history.setEntityType(PushModel.ENTITY_TYPE_ADD_FRIEND);
        history.setEntity(entity);
        history.setReceiver(receiver);
        history.setReceiverPushId(receiver.getPushId());
        // 保存到历史记录表
        Hib.queryOnly(session -> session.save(history));
        // 推送
        PushDispatcher dispatcher = new PushDispatcher();
        PushModel pushModel = new PushModel()
                .add(history.getEntityType(), history.getEntity());
        dispatcher.add(receiver, pushModel);
        dispatcher.submit();

    }

}
