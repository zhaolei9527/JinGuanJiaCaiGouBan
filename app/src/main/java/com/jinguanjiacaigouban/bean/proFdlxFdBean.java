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
 * @date 2018/12/7
 * 功能描述：
 */
public class proFdlxFdBean {

    /**
     * MC : 蔡忠武
     * err :
     * XSNR : [0001] 蔡忠武
     * BH : 0001
     */

    private String MC;
    private String err;
    private String XSNR;
    private String BH;

    public static List<proFdlxFdBean> arrayproFdlxFdBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proFdlxFdBean>>() {
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
}
