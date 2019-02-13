package com.raokii.web.ytalker.push;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
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

        // 注册Json解析器
        register(JacksonJsonProvider.class);
        register(Logger.class);

    }


}
