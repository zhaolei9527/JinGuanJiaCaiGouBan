package com.jinguanjiacaigouban.test;

import com.jinguanjiacaigouban.db.DBOpenHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * com.ocrguojiadianwang
 *
 * @author 赵磊
 * @date 2018/10/26
 * 功能描述：
 */
public class aaaa {

    public static void main(String[] args) {
        Connection conn = DBOpenHelper.getConn();
        try {
            if (!conn.isClosed()) {
                Statement stmt = conn.createStatement();// 创建SQL命令对象
                ResultSet resultSet = stmt.executeQuery("SELECT NAME FROM SYSDATABASES\n" +
                        " WHERE NAME <> 'MASTER'\n" +
                        "   AND NAME <> 'MODEL'\n" +
                        "   AND NAME <> 'MSDB'\n" +
                        "   AND NAME <> 'NORTHWIND'\n" +
                        "   AND NAME <> 'TEMPDB'\n" +
                        "   AND NAME <> 'PUBS'\n" +
                        " ORDER BY NAME");
                // 循环输出每一条记录
                while (resultSet.next()) {
                    // 输出每个字段
                    System.out.println(resultSet.getString("NAME"));
                }
                System.out.println("读取完毕");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
