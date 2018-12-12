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
public class proDdFdBean {

    /**
     * col1 : 董明
     * err :
     * col2 : 20
     */

    private String col1;
    private String err;
    private String col2;

    public static List<proDdFdBean> arrayproDdFdBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proDdFdBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getCol2() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }
}
