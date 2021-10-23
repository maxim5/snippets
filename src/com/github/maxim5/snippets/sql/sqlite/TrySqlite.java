package com.github.maxim5.snippets.sql.sqlite;

import java.sql.*;

/**
 * https://github.com/xerial/sqlite-jdbc
 * https://www.tutorialspoint.com/sqlite/sqlite_java.htm
 * https://www.baeldung.com/java-connect-mysql
 */
public class TrySqlite {
    public static void main(String[] args) {
        // connect("data/sqlite/data.db");
        connect(":memory:");
    }

    private static void connect(String path) {
        // Class.forName("org.sqlite.JDBC");
        String url = "jdbc:sqlite:%s".formatted(path);
        System.out.println(url);
        try (Connection connection = DriverManager.getConnection(url)) {
            System.out.println("Opened database successfully: " + connection);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.


            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");

            try (ResultSet result = statement.executeQuery("select count(*) from person")) {
                System.out.println("count=" + result.getInt(1));
            }

            statement.executeUpdate("insert into person values(1, 'leo')");
            statement.executeUpdate("insert into person values(2, 'yui')");

            try (ResultSet result = statement.executeQuery("select count(*) from person")) {
                System.out.println("count=" + result.getInt(1));
            }

            PreparedStatement prepared = connection.prepareStatement("select id, name from person where id=?");
            prepared.setObject(1, 2);
            try (ResultSet result = prepared.executeQuery()) {
                while (result.next()) {
                    System.out.println(result.getString(1) + " " + result.getString(2));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
