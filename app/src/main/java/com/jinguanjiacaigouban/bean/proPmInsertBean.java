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
public class proPmInsertBean {


    /**
     * err :
     */

    private String err;

    public static List<proPmInsertBean> arrayproPmInsertBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proPmInsertBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }
}
