package com.erolkocoglu.ibb_ecodation_javafx.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class SingletonPropertiesDBConnection {
    // Field
    // Database  Information Data
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    // Singleton Design pattern
    private static SingletonPropertiesDBConnection instance;
    private Connection connection;

    // Parametresiz Constructor (private ile dışarıdan erişilemez olmasını sağlamak)
    private SingletonPropertiesDBConnection() {
        try {
            // JDBC Yükle
            loadDatabaseConfig(); // Konfigürasyonu oku
            Class.forName("org.h2.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Veritabanı bağlantısı başarılı");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Veritabanı bağlantısı başarısız!");
        }
    }

    // Konfigürasyonu yükleme
    private static void loadDatabaseConfig() {
        try (FileInputStream fis = new FileInputStream("C:\\Users\\User\\IdeaProjects\\ibb_ecodation_javafx\\src\\main\\resources\\config.properties")) {
            Properties properties = new Properties();
            properties.load(fis);
            URL = properties.getProperty("db.url", "jdbc:h2:./h2db/user_management");
            //URL = properties.getProperty("db.url", "jdbc:h2:~/h2db/user_management");
            USERNAME = properties.getProperty("db.username", "sa");
            PASSWORD = properties.getProperty("db.password", "");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Veritabanı yapılandırması yüklenemedi!");
        }
    }

    // Singleton Instance
    public static synchronized SingletonPropertiesDBConnection getInstance() {
        if (instance == null) {
            instance = new SingletonPropertiesDBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        if (instance != null && instance.connection != null) {
            try {
                instance.connection.close();
                System.out.println("Veritabanı bağlantısı kapatıldı.");
            } catch (SQLException e) {
                throw new RuntimeException("Bağlantı kapatılırken hata oluştu!", e);
            }
        }
    }

    // Database Test
    public static void dataSet()  throws SQLException {
        // Singleton Instance ile Bağlantıyı Al
        SingletonPropertiesDBConnection dbInstance = SingletonPropertiesDBConnection.getInstance();
        Connection conn = dbInstance.getConnection();

        Statement stmt = conn.createStatement();

        // Örnek bir tablo oluştur
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(255), "
                + "email VARCHAR(255))";
        stmt.execute(createTableSQL);
        System.out.println("Users tablosu oluşturuldu!");

        // Veri Ekleme
        String insertDataSQL = "INSERT INTO Users (name, email) VALUES "
                + "('Ali Veli', 'ali@example.com'), "
                + "('Ayşe Fatma', 'ayse@example.com')";
        stmt.executeUpdate(insertDataSQL);
        System.out.println("Veriler eklendi!");

        // Veri Okuma
        String selectSQL = "SELECT * FROM Users";
        ResultSet rs = stmt.executeQuery(selectSQL);

        System.out.println("\nUsers Tablosu İçeriği:");
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") +
                    ", Name: " + rs.getString("name") +
                    ", Email: " + rs.getString("email"));
        }

        // Bağlantıyı Kapat
        SingletonDBConnection.closeConnection();
    }
    public static void main(String[] args) throws SQLException {
        dataSet();
    }
}
