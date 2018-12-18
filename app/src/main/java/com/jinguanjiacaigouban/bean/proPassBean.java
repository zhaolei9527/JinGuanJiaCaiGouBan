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
 * @date 2018/12/18
 * 功能描述：
 */
public class proPassBean {


    /**
     * err : 原密码错误！
     */

    private String err;

    public static List<proPassBean> arrayproPassBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proPassBean>>() {
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
