package com.mckuai.mcwa.bean;
import com.mckuai.imc.bean.Page;

import java.util.ArrayList;

/**
 * Created by kyly on 2015/10/16.
 */
public class ContributionBean {
    private ArrayList<Question> data;
    private Page pageBean;

    public ArrayList<Question> getData() {
        return data;
    }

    public void setData(ArrayList<Question> data) {
        this.data = data;
    }

    public Page getPageBean() {
        return pageBean;
    }

    public void setPageBean(Page pageBean) {
        this.pageBean = pageBean;
    }
}
