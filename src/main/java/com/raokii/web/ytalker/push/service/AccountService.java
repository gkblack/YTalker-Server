package com.raokii.web.ytalker.push.service;

import com.raokii.web.ytalker.push.bean.db.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Rao
 * @date 2019/1/26
 */
// localhost/api/account/...
@Path("/account")
public class AccountService {
    private String account;
    private String code;

    // localhost/api/account/login
    @GET
    @Path("/login")
    public String get(){
        return "get login:";
    }

    // localhost/api/account/login
    @POST
    @Path("/login")
    // 指定请求和返回的数据格式为json
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User post(){
        User user = new User();
        user.setName("ming");
        user.setSex(2);
        return user;
    }

}
