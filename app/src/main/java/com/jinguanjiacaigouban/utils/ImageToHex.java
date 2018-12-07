package com.jinguanjiacaigouban.utils;

import com.jinguanjiacaigouban.db.DBOpenHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * com.jinguanjiacaigouban.utils
 *
 * @author 赵磊
 * @date 2018/12/6
 * 功能描述：
 */
public class ImageToHex {

    public static String ImageToHex(InputStream fis) {
        try {
            byte[] b;
            b = new byte[fis.available()];
            fis.read(b);
            String s = byte2HexStr(b);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 描述：byte转字符串
     */
    public static String byte2HexStr(byte[] b) {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            hs.append((stmp.length() == 1 ? "0" : "") + stmp);
        }
        return hs.toString().toUpperCase();
    }

    public void writeFileToDb(String fileName) {
        System.out.println("Writing to database... from file:" + fileName);
        Connection con = null;
        try {
            con = DBOpenHelper.getConn();
            File myFile = new File(fileName);
            InputStream in = new FileInputStream(myFile);
            String sql = "insert into Table_1 (picname,pic) values(?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, fileName);
            ps.setBinaryStream(2, in, (int) myFile.length());
            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("Writing to database picfile success");
            } else {
                System.out.println("Writing to database picfile error");
            }
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
    }


}