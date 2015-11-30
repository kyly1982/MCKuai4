package com.mckuai.mcwa.bean;

import com.mckuai.imc.bean.MCUser;

/**
 * Created by kyly on 2015/10/16.
 */
public class RankingListBean {
    private RankingList list;
    private MCUser user;

    public RankingList getList() {
        return list;
    }

    public void setList(RankingList list) {
        this.list = list;
    }

    public MCUser getUser() {
        return user;
    }

    public void setUser(MCUser user) {
        this.user = user;
    }
}
