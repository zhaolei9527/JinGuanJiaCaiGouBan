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
public class proDdPmBean {

    /**
     * col5 :
     * err :
     * col4 : 0.000
     * col1 : 01
     * col3 : 88.000
     * col2 : 女童#90-95
     */

    private String col5;
    private String err;
    private String col4;
    private String col1;
    private String col3;
    private String col2;

    public static List<proDdPmBean> arrayproDdPmBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proDdPmBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getCol5() {
        return col5;
    }

    public void setCol5(String col5) {
        this.col5 = col5;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getCol4() {
        return col4;
    }

    public void setCol4(String col4) {
        this.col4 = col4;
    }

    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
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
}
