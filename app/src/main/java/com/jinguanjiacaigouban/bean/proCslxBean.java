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
 * @date 2018/12/3
 * 功能描述：
 */
public class proCslxBean {


    /**
     * err :
     * BH : 0001
     * MC : 西柳
     * PYM : xl
     */

    private String err;
    private String BH;
    private String MC;
    private String PYM;

    public static List<proCslxBean> arrayproCslxBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proCslxBean>>() {
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

    public String getMC() {
        return MC;
    }

    public void setMC(String MC) {
        this.MC = MC;
    }

    public String getPYM() {
        return PYM;
    }

    public void setPYM(String PYM) {
        this.PYM = PYM;
    }
}
