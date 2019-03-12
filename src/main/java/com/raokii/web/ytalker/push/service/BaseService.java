package com.raokii.web.ytalker.push.service;

import com.raokii.web.ytalker.push.bean.db.User;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * @author Rao
 * @date 2019/3/11
 */
public class BaseService {

    @Context
    protected SecurityContext securityContext;


    protected User getSelf(){
        return (User) securityContext.getUserPrincipal();
    }
}
