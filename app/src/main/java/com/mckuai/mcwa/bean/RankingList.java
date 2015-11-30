package com.mckuai.mcwa.bean;

import com.mckuai.imc.bean.MCUser;
import com.mckuai.imc.bean.Page;

import java.util.ArrayList;

/**
 * Created by kyly on 2015/10/16.
 */
public class RankingList {
    private Page pageBean;
    private ArrayList<MCUser> data;

    public Page getPageBean() {
        return pageBean;
    }

    public void setPageBean(Page pageBean) {
        this.pageBean = pageBean;
    }

    public ArrayList<MCUser> getData() {
        return data;
    }

    public void setData(ArrayList<MCUser> data) {
        this.data = data;
    }
}
