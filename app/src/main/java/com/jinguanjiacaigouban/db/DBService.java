package com.jinguanjiacaigouban.db;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * com.zxp.jdbc4mysql
 *
 * @author 赵磊
 * @date 2018/10/26
 * 功能描述：
 */
public class DBService {

    public static Statement stmt;
    public static ResultSet resultSet;


    public static String doConnection(final String method, final String... key) {
        try {
            java.sql.Connection conn = DBOpenHelper.getConn();
            if (!conn.isClosed()) {
                stmt = conn.createStatement();// 创建SQL命令对象
                StringBuffer EXEC = new StringBuffer();
                EXEC.append("EXEC " + method);
                for (int i = 0; i < key.length; i++) {
                    if (i == 0) {
                        EXEC.append("'" + key[i] + "'");
                    } else {
                        EXEC.append(",'" + key[i] + "'");
                    }
                }
                Log.e("DBService", EXEC.toString());
                resultSet = stmt.executeQuery(EXEC.toString());
                return resultSetToJson(resultSet);
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String doConnectionIMG(final String method, String fileName, final String... key) {
        System.out.println("Writing to database... from file:" + fileName);
        Connection con = null;
        CallableStatement ps = null;
        InputStream in = null;
        File myFile = null;
        try {
            con = DBOpenHelper.getConn();

            if (!TextUtils.isEmpty(fileName)) {
                myFile = new File(fileName);
                in = new FileInputStream(myFile);
            }

            String sql = "EXEC " + method + " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            Log.e("DBService", sql);

            ps = con.prepareCall(sql);

            ps.setString(1, key[0]);
            ps.setString(2, key[1]);
            ps.setString(3, key[2]);
            ps.setString(4, key[3]);
            ps.setString(5, key[4]);
            ps.setString(6, key[5]);
            ps.setString(7, key[6]);
            ps.setString(8, key[7]);
            ps.setString(9, key[8]);
            ps.setString(10, key[9]);
            ps.setString(11, key[10]);
            ps.setString(12, key[11]);
            ps.setString(13, key[12]);
            ps.setString(14, key[13]);
            ps.setString(15, key[14]);
            ps.setString(16, key[15]);
            if (in != null) {
                ps.setBinaryStream(17, in, (int) myFile.length());
            } else {
                ps.setBinaryStream(17, null, 0);
            }
            ps.setString(18, key[16]);

            for (int i = 0; i < key.length; i++) {
                Log.e("DBService", key[i]);
            }

            ResultSet resultSet = ps.executeQuery();
            return resultSetToJson(resultSet);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                con = null;
            }
        }
        return "";
    }

    public static ResultSet doConnectionForResultSet(final String method, final String... key) {
        try {
            java.sql.Connection conn = DBOpenHelper.getConn();
            if (!conn.isClosed()) {
                stmt = conn.createStatement();
                // 创建SQL命令对象
                StringBuffer EXEC = new StringBuffer();
                EXEC.append("EXEC " + method);
                for (int i = 0; i < key.length; i++) {
                    if (i == 0) {
                        EXEC.append("'" + key[i] + "'");
                    } else {
                        EXEC.append(",'" + key[i] + "'");
                    }
                }
                Log.e("DBService", EXEC.toString());
                resultSet = stmt.executeQuery(EXEC.toString());
                return resultSet;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String resultSetToJson(ResultSet rs) throws SQLException, JSONException {
        // json数组
        JSONArray array = new JSONArray();
        // 获取列数
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        // 遍历ResultSet中的每条数据
        while (rs.next()) {
            JSONObject jsonObj = new JSONObject();
            // 遍历每一列
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                String value = rs.getString(columnName);
                jsonObj.put(columnName, value);
            }
            array.put(jsonObj);
        }
        rs.close();
        Log.e("DBService", array.toString());
        return array.toString();
    }


}
