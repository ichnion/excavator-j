/**
 * パッケージ名：org.georepublic.excavator.db
 * ファイル名  ：DbUtils.java
 * 
 * @author mbasa
 * @since Dec 14, 2023
 */
package org.georepublic.excavator.db;

import java.sql.Connection;
import java.sql.Statement;

/**
 * 説明：
 *
 */
public class DbUtils {

    /**
     * コンストラクタ
     *
     */
    public DbUtils() {
    }

    public static void sqlExecute(Connection con, String sql) {
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.execute(sql);
            stmt.close();
        } catch (Exception e) {
            // System.out.println("SQL Execute Error: " + e.getMessage());
            ;
        }
    }
}
