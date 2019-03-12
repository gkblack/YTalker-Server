package com.raokii.web.ytalker.push.factory;

import com.raokii.web.ytalker.push.bean.api.base.PushModel;
import com.raokii.web.ytalker.push.bean.db.PushHistory;
import com.raokii.web.ytalker.push.bean.db.User;

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



}
