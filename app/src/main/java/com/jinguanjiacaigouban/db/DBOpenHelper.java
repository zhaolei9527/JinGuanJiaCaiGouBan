package com.jinguanjiacaigouban.db;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.jinguanjiacaigouban.utils.UrlUtils.driver;
import static com.jinguanjiacaigouban.utils.UrlUtils.password;
import static com.jinguanjiacaigouban.utils.UrlUtils.url;
import static com.jinguanjiacaigouban.utils.UrlUtils.user;

/**
 * com.zxp.jdbc4mysql
 *
 * @author 赵磊
 * @date 2018/10/26
 * 功能描述：
 */
public class DBOpenHelper {

    private static Connection conn = null;

    /**
     * 连接数据库
     */
    public static Connection getConn() {
        try {
            Class.forName(driver);//获取MYSQL驱动
            conn = (Connection) DriverManager.getConnection(url, user, password);
            Log.e("DBOpenHelper", url);
            Log.e("DBOpenHelper", user);
            Log.e("DBOpenHelper", password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭数据库
     */
    public static void closeAll(Connection conn, Statement ps) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 关闭数据库
     */

    public static void closeAll(Connection conn, Statement ps, ResultSet rs) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 关闭连接
    public static void closeConn(Connection conn) {
        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("关闭连接失败！");
                e.printStackTrace();
            }
        }
    }


}
