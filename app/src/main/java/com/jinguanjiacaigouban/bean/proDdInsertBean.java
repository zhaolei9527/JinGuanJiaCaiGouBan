package com.jinguanjiacaigouban.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * com.jinguanjiacaigouban.bean
 *
 * @author 赵磊
 * @date 2018/12/12
 * 功能描述：
 */
public class proDdInsertBean {

    /**
     * err :
     * BH : 181212-0003
     */

    private String err;
    private String BH;

    public static List<proDdInsertBean> arrayproDdInsertBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proDdInsertBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getBH() {
        return BH;
    }

    public void setBH(String BH) {
        this.BH = BH;
    }
}
