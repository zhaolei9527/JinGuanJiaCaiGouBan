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
public class proFdlxSelectBean {


    /**
     * err :
     * XSNR : 男装店 (0)
     * BH : 0007
     * MC : 男装店
     * PYM : Nzd
     */

    private String err;
    private String XSNR;
    private String BH;
    private String MC;
    private String PYM;

    public static List<proFdlxSelectBean> arrayproFdlxSelectBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proFdlxSelectBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
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
