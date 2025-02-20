package com.example.demo.sql;

import com.example.demo.config.Config;

import java.sql.*;
import java.util.*;

public class SqlHelper {
    private static final Config CFG = Config.getInstance();

    public static List<Map<String, Object>> executeQuery(String sql) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/postgres?user=postgres&password=1234");
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                Map<String, Object> rowMap = new HashMap<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    String columnName = rsmd.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    rowMap.put(columnName, columnValue);
                }
                resultList.add(rowMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return resultList;
    }

    public static void insertQuery(String query) {
        try {
            Connection conn = DriverManager.getConnection(CFG.postgresUrl());
            PreparedStatement statement = conn.prepareStatement(query);
            statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Can't execute SQL request!");
        }
    }
}

