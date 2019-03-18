package com.raokii.web.ytalker.push.service;

import com.google.common.base.Strings;
import com.raokii.web.ytalker.push.bean.api.base.ResponseModel;
import com.raokii.web.ytalker.push.bean.api.user.UpdateInfoModel;
import com.raokii.web.ytalker.push.bean.card.UserCard;
import com.raokii.web.ytalker.push.bean.db.User;
import com.raokii.web.ytalker.push.bean.db.UserFollow;
import com.raokii.web.ytalker.push.factory.PushFactory;
import com.raokii.web.ytalker.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rao
 * @date 2019/3/15
 */
// 127.0.0.1/ytalker/api/user
@Path("/user")
public class UserService extends BaseService{

    /**
     * 更新个人信息
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> update(UpdateInfoModel model){
        if(!UpdateInfoModel.check(model)){
            return ResponseModel.buildParameterError();
        }

        User self = getSelf();
        // 更新个人信息
        self = model.updateToUser(self);
        self = UserFactory.update(self);
        // 建立自己的用户信息
        UserCard card = new UserCard(self, true);

        return ResponseModel.buildOk(card);
    }


    /**
     * 拉取联系人
     * @return
     */
    @GET
    @Path("/contact")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> contact(){
        User self = getSelf();

        // 获取联系人
        List<User> users = UserFactory.contacts(self);

        List<UserCard> userCards = users.stream()
                // map操作，即转置操作 user -> userCard
                .map(user -> new UserCard(user, true))
                .collect(Collectors.toList());

        return ResponseModel.buildOk(userCards);
    }

    // 关注人
    @GET
    @Path("/follow/{followId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> follow(@PathParam("followId") String followId){
        User self = getSelf();

        // 不能关注自己
        if(self.getId().equalsIgnoreCase(followId) || Strings.isNullOrEmpty(followId)){
            return ResponseModel.buildParameterError();
        }

        // 找到我关注的人
        User followUser = UserFactory.findById(followId);
        if(followUser == null){
            return ResponseModel.buildNotFoundUserError(null);
        }

        followUser = UserFactory.follow(self,followUser,null);
        if(followUser == null){
            // 未找到关注的人
            return ResponseModel.buildServiceError();
        }
        // 通知我关注的人消息，提示我已关注他
        PushFactory.pushFollow(self, new UserCard(followUser));

        // 返回已关注的人的信息
        return ResponseModel.buildOk(new UserCard(followUser, true));
    }
}
