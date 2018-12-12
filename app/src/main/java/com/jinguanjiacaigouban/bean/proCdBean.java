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
 * @date 2018/12/11
 * 功能描述：
 */
public class proCdBean {

    /**
     * err :
     * BH : 181204-0001
     * CSMC : 实创金管家
     * XSRN : 181204-0001  (95)    实创金管家
     * CRDATE : 2018-12-04
     */

    private String err;
    private String BH;
    private String CSMC;
    private String XSRN;
    private String CRDATE;

    public static List<proCdBean> arrayproCdBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proCdBean>>() {
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

    public String getCSMC() {
        return CSMC;
    }

    public void setCSMC(String CSMC) {
        this.CSMC = CSMC;
    }

    public String getXSRN() {
        return XSRN;
    }

    public void setXSRN(String XSRN) {
        this.XSRN = XSRN;
    }

    public String getCRDATE() {
        return CRDATE;
    }

    public void setCRDATE(String CRDATE) {
        this.CRDATE = CRDATE;
    }
}
