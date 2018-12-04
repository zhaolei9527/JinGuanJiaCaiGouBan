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
 * @date 2018/11/20
 * 功能描述：
 */
public class proLoginBean {

    /**
     * err :
     * MC : 测试
     * TYM :
     */

    private String err;
    private String MC;
    private String TYM;

    public static List<proLoginBean> arrayproLoginBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proLoginBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getMC() {
        return MC;
    }

    public void setMC(String MC) {
        this.MC = MC;
    }

    public String getTYM() {
        return TYM;
    }

    public void setTYM(String TYM) {
        this.TYM = TYM;
    }
}
