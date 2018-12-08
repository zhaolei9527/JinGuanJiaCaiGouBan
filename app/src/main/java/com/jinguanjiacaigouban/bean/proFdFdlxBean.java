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
 * @date 2018/12/8
 * 功能描述：
 */
public class proFdFdlxBean {


    /**
     * MC : 高丽娟
     * err :
     * PYM : glj
     * BH : 0028
     */

    private String MC;
    private String err;
    private String PYM;
    private String BH;

    public static List<proFdFdlxBean> arrayproFdFdlxBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proFdFdlxBean>>() {
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

    public String getPYM() {
        return PYM;
    }

    public void setPYM(String PYM) {
        this.PYM = PYM;
    }

    public String getBH() {
        return BH;
    }

    public void setBH(String BH) {
        this.BH = BH;
    }
}
