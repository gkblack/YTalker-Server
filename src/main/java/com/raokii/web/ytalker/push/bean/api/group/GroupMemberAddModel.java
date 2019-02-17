package com.raokii.web.ytalker.push.bean.api.group;

import com.google.gson.annotations.Expose;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Rao
 * @date 2019/2/17
 */
public class GroupMemberAddModel {
    @Expose
    private Set<String> users = new HashSet<>();

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public static boolean check(GroupMemberAddModel member) {
        return !(member == null
                || member.getUsers().size() == 0);
    }
}
