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
 * @date 2018/12/4
 * 功能描述：
 */
public class proPmSmBean {

    /**
     * MC : 星星伊人测试#三分平口加肥
     * err :
     * TM : 0024640008
     * DJ : 188.000
     */

    private String MC;
    private String err;
    private String TM;
    private String DJ;

    public static List<proPmSmBean> arrayproPmSmBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proPmSmBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getMC() {
        return MC;
    }

    public void setMC(String MC) {
        this.MC = MC;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getTM() {
        return TM;
    }

    public void setTM(String TM) {
        this.TM = TM;
    }

    public String getDJ() {
        return DJ;
    }

    public void setDJ(String DJ) {
        this.DJ = DJ;
    }
}
