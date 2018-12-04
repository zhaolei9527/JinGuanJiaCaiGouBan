package com.jinguanjiacaigouban.db;


import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    public static String doConnection(final String method, final String... key) {
        try {
            java.sql.Connection conn = DBOpenHelper.getConn();
            if (!conn.isClosed()) {
                Statement stmt = conn.createStatement();// 创建SQL命令对象
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
                ResultSet resultSet = stmt.executeQuery(EXEC.toString());
                return resultSetToJson(resultSet);
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
