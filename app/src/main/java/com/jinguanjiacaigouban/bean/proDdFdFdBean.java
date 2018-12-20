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
 * @date 2018/12/20
 * 功能描述：
 */
public class proDdFdFdBean {


    /**
     * MC : 代玲
     * err :
     * XSNR : [0035] 代玲
     * BH : 0035
     */

    private String MC;
    private String err;
    private String XSNR;
    private String BH;
    private String col1;
    private String col2;

    public static List<proDdFdFdBean> arrayproDdFdFdBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proDdFdFdBean>>() {
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

    public String getXSNR() {
        return XSNR;
    }

    public void setXSNR(String XSNR) {
        this.XSNR = XSNR;
    }

    public String getBH() {
        return BH;
    }

    public void setBH(String BH) {
        this.BH = BH;
    }


    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public String getCol2() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }

}
