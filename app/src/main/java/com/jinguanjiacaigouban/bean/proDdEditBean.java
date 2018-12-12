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
 * @date 2018/12/12
 * 功能描述：
 */
public class proDdEditBean {

    /**
     * col5 : 月结
     * col4 : 实创金管家
     * col7 : 5
     * col6 : 3
     * col1 : 181204-0001
     * col0 :
     * col3 : 超级管理
     * col2 : 2018-12-04
     * err :
     * col8 :
     */

    private String col5;
    private String col4;
    private String col7;
    private String col6;
    private String col1;
    private String col0;
    private String col3;
    private String col2;
    private String err;
    private String col8;

    public static List<proDdEditBean> arrayproDdEditBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proDdEditBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getCol5() {
        return col5;
    }

    public void setCol5(String col5) {
        this.col5 = col5;
    }

    public String getCol4() {
        return col4;
    }

    public void setCol4(String col4) {
        this.col4 = col4;
    }

    public String getCol7() {
        return col7;
    }

    public void setCol7(String col7) {
        this.col7 = col7;
    }

    public String getCol6() {
        return col6;
    }

    public void setCol6(String col6) {
        this.col6 = col6;
    }

    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public String getCol0() {
        return col0;
    }

    public void setCol0(String col0) {
        this.col0 = col0;
    }

    public String getCol3() {
        return col3;
    }

    public void setCol3(String col3) {
        this.col3 = col3;
    }

    public String getCol2() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getCol8() {
        return col8;
    }

    public void setCol8(String col8) {
        this.col8 = col8;
    }
}
