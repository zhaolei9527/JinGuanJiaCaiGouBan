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
public class proPmSelectBean {


    /**
     * err :
     * BH : 0000007
     * MC : 名度＃10010
     * PYM : md＃10010
     * XSNR : 名度＃10010
     * XSNR2 : 0
     * TM :
     * JHDJ : 0.000
     * JHSL : 0.000
     * BZ :
     */

    private String err;
    private String BH;
    private String MC;
    private String PYM;
    private String XSNR;
    private String XSNR2;
    private String TM;
    private String JHDJ;
    private String JHSL;
    private String BZ;

    public static List<proPmSelectBean> arrayproPmSelectBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proPmSelectBean>>() {
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

    public String getXSNR2() {
        return XSNR2;
    }

    public void setXSNR2(String XSNR2) {
        this.XSNR2 = XSNR2;
    }

    public String getTM() {
        return TM;
    }

    public void setTM(String TM) {
        this.TM = TM;
    }

    public String getJHDJ() {
        return JHDJ;
    }

    public void setJHDJ(String JHDJ) {
        this.JHDJ = JHDJ;
    }

    public String getJHSL() {
        return JHSL;
    }

    public void setJHSL(String JHSL) {
        this.JHSL = JHSL;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }
}
