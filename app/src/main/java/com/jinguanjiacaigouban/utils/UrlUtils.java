package com.jinguanjiacaigouban.utils;

import android.util.Log;

/**
 * sakura.sweepcodesignin.utils
 *
 * @author 赵磊
 * @date 2017/10/26
 * 功能描述：UrlUtils 接口连接url地址存放
 */
public class UrlUtils {

    public static String driver = "net.sourceforge.jtds.jdbc.Driver";//MySQL 驱动
    public static String url = "";//MYSQL数据库连接Url
    public static String user = "";//用户名
    public static String password = "";//密码
    public static String databaseName = "";//数据库定向
    public static String BBH = "1.0.1";//数据库定向

    public static void checkUrl(String serviceIP, String host, String user, String password) {
        url = "jdbc:jtds:sqlserver://" + serviceIP + ":" + host + ";instanceName=SQLEXPRESS;databaseName=master";//MYSQL数据库连接Url
        UrlUtils.user = user;
        UrlUtils.password = password;
    }

    public static void checkUrl(String serviceIP, String host, String user, String password, String databaseName) {
        url = "jdbc:jtds:sqlserver://" + serviceIP + ":" + host + ";instanceName=SQLEXPRESS;databaseName=" + databaseName;//MYSQL数据库连接Url
        Log.e("UrlUtils", url);
        UrlUtils.user = user;
        UrlUtils.password = password;
        UrlUtils.databaseName = databaseName;
    }


}
