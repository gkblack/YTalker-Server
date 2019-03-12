package com.raokii.web.ytalker.push.service;

import com.google.common.base.Strings;
import com.raokii.web.ytalker.push.bean.api.account.AccountRspModel;
import com.raokii.web.ytalker.push.bean.api.account.LoginModel;
import com.raokii.web.ytalker.push.bean.api.account.RegisterModel;
import com.raokii.web.ytalker.push.bean.api.base.ResponseModel;
import com.raokii.web.ytalker.push.bean.db.User;
import com.raokii.web.ytalker.push.factory.UserFactory;
import com.raokii.web.ytalker.push.utils.Hib;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Rao
 * @date 2019/1/26
 */
// localhost/api/account/...
@Path("/account")
public class AccountService extends BaseService{

    // localhost/api/account/login
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> login(LoginModel loginModel){
        if (!LoginModel.check(loginModel)){
            return ResponseModel.buildParameterError();
        }

        User user = UserFactory.login(loginModel.getAccount(), loginModel.getPassword());
        if( user != null){

            // 如果有PushId
            if (!Strings.isNullOrEmpty(loginModel.getPushId())){
                return bind(user, loginModel.getPushId());
            }

            // 返回当前账户
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        }else {
            return ResponseModel.buildLoginError();
        }
    }

    // localhost/api/account/login
    @POST
    @Path("/register")
    // 指定请求和返回的数据格式为json
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> register(RegisterModel model){

        if(!RegisterModel.check(model)){
            return ResponseModel.buildParameterError();
        }

//        User user = null;
        User user = UserFactory.findByPhone(model.getAccount().trim());
        if (user != null){
            return ResponseModel.buildHaveAccountError();
        }
        user = UserFactory.findByName(model.getName().trim());
        if(user != null){
            return ResponseModel.buildHaveNameError();
        }

        // 注册
        user = UserFactory.register(model.getAccount(),model.getPassword(),model.getName());

        if( user != null){
            // 如果当前存在PushId
            if(!Strings.isNullOrEmpty(model.getPushId())){
                return bind(user, model.getPushId());
            }
            // 返回当前的账户
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk();
        }else {
            // 注册异常
            return ResponseModel.buildRegisterError();
        }
    }

    /**
     * 绑定设备Id
     * @param token
     * @param pushid
     * @return
     */
    @POST
    @Path("/bind/{pushId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> bind(@HeaderParam("token") String token, @PathParam("pushId") String pushid){
        if(Strings.isNullOrEmpty(token) || Strings.isNullOrEmpty(pushid)){
            return ResponseModel.buildParameterError();
        }

        // 拿到自己的个人信息
//        User self = UserFactory.findByToken(token);
        User self = getSelf();
        return bind(self, pushid);
    }

    /**
     * 绑定操作
     * @param self
     * @param pushId
     * @return
     */
    public ResponseModel<AccountRspModel> bind(User self, String pushId){
        // 设备绑定
        User user = UserFactory.bindPushId(self, pushId);
        if(user == null){
            return ResponseModel.buildServiceError();
        }

        // 返回当前已经绑定的账户
        AccountRspModel rspModel = new AccountRspModel(user, true);
        return ResponseModel.buildOk(rspModel);
    }

}
