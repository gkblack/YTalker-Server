package com.raokii.web.ytalker.push.utils;

import com.gexin.rp.sdk.base.IBatch;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.google.common.base.Strings;
import com.raokii.web.ytalker.push.bean.api.base.PushModel;
import com.raokii.web.ytalker.push.bean.db.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 消息推送工具类
 * @author Rao
 * @date 2019/2/17
 */
public class PushDispatcher {
    private static final String appId = "";
    private static final String appKey = "";
    private static final String masterSecret = "";
    private static final String host = "http://sdk.open.api.igexin.com/apiex.htm";

    private final IGtPush pusher;
    private final List<BatchBean> beans = new ArrayList<>();

    public PushDispatcher(){
        pusher = new IGtPush(host, appKey, masterSecret);
    }

    /**
     *
     * @param receiver
     * @param model
     * @return
     */
    public boolean add(User receiver, PushModel model){
        if(receiver == null || model == null){
            Strings.isNullOrEmpty(receiver.getPushId());
            return false;
        }

        String pushString = model.getPushString();
        if(Strings.isNullOrEmpty(pushString))
        return false;

        // 构建一个目标+内容
        BatchBean bean = buildMessage(receiver.getPushId(), pushString);
        beans.add(bean);
        return true;
    }

    /**
     * 封装要发送的消息
     * @param clientId 接收人的设备id
     * @param text
     * @return
     */
    private BatchBean buildMessage(String clientId, String text){
        // 透传消息，不在通知栏显示，只在MessageReceiver收到
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionContent(text);
        template.setTransmissionType(0); // 填写为1则是为启动app

        SingleMessage message = new SingleMessage();
        message.setData(template);
        message.setOffline(true);
        message.setOfflineExpireTime(24 * 3600 * 1000);
        // 设置推送目标
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(clientId);

        // 返回封装好的消息bean类
        return new BatchBean(message, target);
    }

    public boolean submit(){
        // 构建打包的工具类
        IBatch batch = pusher.getBatch();
        // 是否有数据需要发送
        boolean haveData = false;

        for (BatchBean bean : beans){
            try {
                batch.add(bean.message, bean.target);
                haveData = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 无数据则直接返回
        if(!haveData){
            return false;
        }

        IPushResult result = null;
        try {
            result = batch.submit();
        } catch (IOException e) {
            e.printStackTrace();
            // 失败时再尝试发送一次
            try {
                batch.retry();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (result != null){
            try {
                Logger.getLogger("PushDispatcher")
                        .log(Level.INFO, (String) result.getResponse().get("result"));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Logger.getLogger("PushDispatcher")
                .log(Level.WARNING, "推送服务器响应异常！！！");
        return false;
    }

    // 给每个人发送消息的Bean类封装
    private static class BatchBean{
        SingleMessage message;
        Target target;

        BatchBean(SingleMessage message, Target target){
            this.message = message;
            this.target = target;
        }
    }

}
