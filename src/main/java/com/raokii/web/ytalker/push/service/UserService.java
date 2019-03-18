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
public class UserService extends BaseService {

    /**
     * 更新个人信息
     *
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> update(UpdateInfoModel model) {
        if (!UpdateInfoModel.check(model)) {
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
     *
     * @return
     */
    @GET
    @Path("/contact")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> contact() {
        User self = getSelf();

        // 获取联系人
        List<User> users = UserFactory.contacts(self);

        List<UserCard> userCards = users.stream()
                // map操作，即转置操作 user -> userCard
                .map(user -> new UserCard(user, true))
                .collect(Collectors.toList());

        return ResponseModel.buildOk(userCards);
    }

    // 关注人,修改信息用PUT
    // 简化为关注人操作为双方同时关注
    @PUT
    @Path("/follow/{followId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> follow(@PathParam("followId") String followId) {
        User self = getSelf();

        // 不能关注自己
        if (self.getId().equalsIgnoreCase(followId) || Strings.isNullOrEmpty(followId)) {
            return ResponseModel.buildParameterError();
        }

        // 找到我关注的人
        User followUser = UserFactory.findById(followId);
        if (followUser == null) {
            return ResponseModel.buildNotFoundUserError(null);
        }

        followUser = UserFactory.follow(self, followUser, null);
        if (followUser == null) {
            // 未找到关注的人
            return ResponseModel.buildServiceError();
        }
        // 通知我关注的人消息，提示我已关注他
        PushFactory.pushFollow(self, new UserCard(followUser));

        // 返回已关注的人的信息
        return ResponseModel.buildOk(new UserCard(followUser, true));
    }

    @GET // 获取某人的信息
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> getUser(@PathParam("id") String id) {
        if (Strings.isNullOrEmpty(id)) {
            return ResponseModel.buildParameterError();
        }

        User self = getSelf();
        if (self.getId().equalsIgnoreCase(id)) {
            // 返回自身的数据
            return ResponseModel.buildOk(new UserCard(self, true));
        }

        User user = UserFactory.findById(id);
        if (user == null) {
            // 未找到用户
            return ResponseModel.buildNotFoundUserError(null);
        }

        // 如果我与查询用户之间有关注记录，则我只需要查询这些有关注信息的用户
        boolean isFollow = UserFactory.getUserFollow(self, user) != null;

        return ResponseModel.buildOk(new UserCard(user, isFollow));
    }

    // 搜索人接口
    @GET // 不涉及数据更改，只查询，所以为get
    @Path("/search/{name:(.*)?}") // 正则，名字为任意字符，可以为空
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> search(@DefaultValue("") @PathParam("name") String name) { // 默认值为空
        User self = getSelf();

        // 拿到搜索到的数据
        List<User> searchUsers = UserFactory.search(name);
        // 把查询的人封装成UserCard
        // 判断这些人中是否有我已经关注的人
        // 如果有，返回的关注status应该已经设置好状态

        // 拿出我的关注列表
        List<User> contacts = UserFactory.contacts(self);

        // User转UserCard
        List<UserCard> userCards = searchUsers.stream()
                .map(user -> {
                    // 判断这个人是否是我或者在我的联系人中
                    boolean isFollow = user.getId().equalsIgnoreCase(self.getId())
                            // 进行联系人的任意匹配，匹配其中的id字段，只适合查询少量数据
                            || contacts.stream().anyMatch(
                                    contactUser -> contactUser.getId()
                                            .equalsIgnoreCase(user.getId()));
                    return new UserCard(user, isFollow);
                })
                .collect(Collectors.toList());

        return ResponseModel.buildOk(userCards);
    }
}
