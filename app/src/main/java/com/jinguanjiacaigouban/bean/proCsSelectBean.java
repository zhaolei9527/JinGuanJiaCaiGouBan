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
 * @date 2018/11/21
 * 功能描述：
 */
public class proCsSelectBean {


    /**
     * err :
     * BH : 0322
     * MC : 1-192
     * PYM : 1-192
     * XSNR : 1-192
     * FLMC1 : 童装
     * FLMC2 : 秋季
     * FLMC3 : 123
     * DZ : UnknownServer
     */

    private String err;
    private String BH;
    private String MC;
    private String PYM;
    private String XSNR;
    private String FLMC1;
    private String FLMC2;
    private String FLMC3;
    private String DZ;

    public static List<proCsSelectBean> arrayproCsSelectBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proCsSelectBean>>() {
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

    public String getXSNR() {
        return XSNR;
    }

    public void setXSNR(String XSNR) {
        this.XSNR = XSNR;
    }

    public String getFLMC1() {
        return FLMC1;
    }

    public void setFLMC1(String FLMC1) {
        this.FLMC1 = FLMC1;
    }

    public String getFLMC2() {
        return FLMC2;
    }

    public void setFLMC2(String FLMC2) {
        this.FLMC2 = FLMC2;
    }

    public String getFLMC3() {
        return FLMC3;
    }

    public void setFLMC3(String FLMC3) {
        this.FLMC3 = FLMC3;
    }

    public String getDZ() {
        return DZ;
    }

    public void setDZ(String DZ) {
        this.DZ = DZ;
    }
}
