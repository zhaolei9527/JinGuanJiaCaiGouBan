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
public class proPymBean {


    /**
     * err :
     * PYM : 2
     */

    private String err;
    private String PYM;

    public static List<proPymBean> arrayproPymBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proPymBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getPYM() {
        return PYM;
    }

    public void setPYM(String PYM) {
        this.PYM = PYM;
    }
}
