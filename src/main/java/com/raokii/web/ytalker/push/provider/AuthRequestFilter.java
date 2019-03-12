package com.raokii.web.ytalker.push.provider;

import com.google.common.base.Strings;
import com.raokii.web.ytalker.push.bean.api.base.ResponseModel;
import com.raokii.web.ytalker.push.bean.db.User;
import com.raokii.web.ytalker.push.factory.UserFactory;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.security.Principal;

/**
 * 全局请求接口过滤及拦截
 * @author Rao
 * @date 2019/3/11
 */
public class AuthRequestFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String relationPath = ((ContainerRequest)requestContext).getPath(false);
        // 判断当前的请求接口是否为登录注册的接口，是则不做拦截
        if(relationPath.startsWith("account/login") || relationPath.startsWith("account/register")){
            return;
        }

        // 从header请求头中获取token
        String token = requestContext.getHeaders().getFirst("token");
        if(!Strings.isNullOrEmpty(token)){

            // 查询自己的信息
            final User self = UserFactory.findByToken(token);
            if(self!=null){
                // 给请求添加上下文
                requestContext.setSecurityContext(new SecurityContext() {
                    @Override
                    public Principal getUserPrincipal() {
                        return self;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        // 可此写入用户权限，
                        return true;
                    }

                    @Override
                    public boolean isSecure() {
                        return false;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return null;
                    }
                });
                return;
            }
        }

        ResponseModel model = ResponseModel.buildAccountError();

        Response response = Response.status(Response.Status.OK)
                .entity(model)
                .build();

        // 拦截请求，直接返回结果
        requestContext.abortWith(response);


    }
}
