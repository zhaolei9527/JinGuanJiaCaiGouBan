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
 * @date 2019/1/30
 * 功能描述：
 */
public class proddfxx {


    /**
     * err : 订单已成功分享！
     */

    private String err;

    public static List<proddfxx> arrayproddfxxFromData(String str) {

        Type listType = new TypeToken<ArrayList<proddfxx>>() {
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
