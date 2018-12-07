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
public class proFdlxEditBean {

    /**
     * err :
     * col1 : 0006
     * col0 : 0
     * col3 : 123456
     * col2 : 123456
     */

    private String err;
    private String col1;
    private String col0;
    private String col3;
    private String col2;

    public static List<proFdlxEditBean> arrayproFdlxEditBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proFdlxEditBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
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
}
