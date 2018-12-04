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
public class proCsDeleteBean {

    /**
     * err : 已登记商品，不能删除！
     */

    private String err;

    public static List<proCsDeleteBean> arrayproCsDeleteBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<proCsDeleteBean>>() {
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
