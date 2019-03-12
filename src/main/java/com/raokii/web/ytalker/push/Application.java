package com.raokii.web.ytalker.push;

import com.raokii.web.ytalker.push.provider.AuthRequestFilter;
import com.raokii.web.ytalker.push.provider.GsonProvider;
import com.raokii.web.ytalker.push.service.AccountService;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

/**
 * @author Rao
 * @date 2019/1/26
 */
public class Application extends ResourceConfig {

    public Application(){
        // 注册逻辑处理的包名
//        packages("com.raokii.web.ytalker.service");
        packages(AccountService.class.getPackage().getName());

        // 进行全局的请求拦截
        register(AuthRequestFilter.class);
//        // 注册Json解析器
//        register(JacksonJsonProvider.class);
        // 替换为Gson解析方法
        register(GsonProvider.class);

        register(Logger.class);

    }


}
