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
 * @date 2018/12/3
 * 功能描述：
 */
public class proCsInsertBean {


    /**
     * err : 区域<aaa> 不存在！
     */

    private String err;

    public static List<proCsInsertBean> arrayproCsInsertBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proCsInsertBean>>() {
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
