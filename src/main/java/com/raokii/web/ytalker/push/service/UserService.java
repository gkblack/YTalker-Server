package com.raokii.web.ytalker.push.service;

import com.raokii.web.ytalker.push.bean.api.base.ResponseModel;
import com.raokii.web.ytalker.push.bean.api.user.UpdateInfoModel;
import com.raokii.web.ytalker.push.bean.card.UserCard;
import com.raokii.web.ytalker.push.bean.db.User;
import com.raokii.web.ytalker.push.factory.UserFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Rao
 */
@Path("/user")
public class UserService extends BaseService{

    // 用户信息修改接口
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> update(UpdateInfoModel model) {
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
}
