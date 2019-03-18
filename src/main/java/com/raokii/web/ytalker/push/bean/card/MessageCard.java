package com.raokii.web.ytalker.push.bean.card;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;

/**
 * @author Rao
 * @date 2019/2/17
 */
public class MessageCard {

    @Expose
    private String id;
    @Expose
    private String content;
    @Expose
    private String type;
    @Expose
    private LocalDateTime createAt;




}
