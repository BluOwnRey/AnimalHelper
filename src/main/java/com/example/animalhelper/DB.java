package com.example.animalhelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DB {
    Connection conn;
    private static final String DB_URL = "jdbc:sqlite:animalhelper.db";

    private static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        return conn;
    }

    // Функция для создания таблиц
    public static void createTables() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            // Создание таблицы "person"
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS person (id INTEGER PRIMARY KEY, name TEXT, phone TEXT)");
            // Создание таблицы "animal"
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS animal (id INTEGER PRIMARY KEY, personId INTEGER, name TEXT, type TEXT, " +
                    "FOREIGN KEY (personId) REFERENCES person(id))");
            // Создание таблицы "registry"
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS registry (animalId INTEGER, date TEXT, FOREIGN KEY (animalId) REFERENCES animal(id))");
        } catch (SQLException e) {
            System.out.println("ERROR(createTables): " + e.getMessage());
        }
    }

    public static String[][] getAllRegistryData() throws SQLException {
        List<String[]> records = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT p.name AS person_name, p.phone AS person_phone, " +
                     "a.name AS animal_name, a.type AS animal_type, r.animalId AS registry_id, r.date " +
                     "FROM registry r " +
                     "JOIN animal a ON r.animalId = a.id " +
                     "JOIN person p ON a.personId = p.id")) {

            // Определяем количество столбцов
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Перебираем результаты и добавляем их в список
            while (resultSet.next()) {
                String[] record = new String[columnCount];
                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                    record[columnIndex] = resultSet.getString(columnIndex + 1);
                }
                records.add(record);
            }
        }

        // Преобразуем список записей в массив и возвращаем его
        String[][] matrix = new String[records.size()][];
        for (int i = 0; i < records.size(); i++) {
            matrix[i] = records.get(i);
        }
        return matrix;
    }

    public static int addPerson(String name, String phone) throws SQLException {
        if (!personExists(name, phone)) {
            try {
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO person (name, phone) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, name);
                statement.setString(2, phone);
                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    return 0;
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        return 0;
                    }
                }
            } catch (Exception e){
                System.out.println("ERROR(addPerson): " + e.getMessage());
                return 0;
            }
        } else {
            return -1;
        }
    }

    public static int addAnimal(int personId, String animalName, String animalType) throws SQLException {
        if (!animalExists(personId, animalName, animalType)) {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO animal (personId, name, type) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, personId);
                preparedStatement.setString(2, animalName);
                preparedStatement.setString(3, animalType);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    return 0;
                }
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        return 0;
                    }
                }
            } catch (SQLException e) {
                System.out.println("ERROR(addAnimal): " + e.getMessage());
                return -1;
            }
        } else {
            return -1; // Что-то вроде кода ошибки, чтобы указать, что животное уже существует
        }
    }

    public static int addRegistry(int animalId, String date) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO registry (animalId, date) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, animalId);
            preparedStatement.setString(2, date);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Adding registry failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR(addRegistry): " + e.getMessage());
            return 0;
        }
    }


    private static boolean personExists(String name, String phone) throws SQLException {
        String sql = "SELECT COUNT(*) FROM person WHERE name = ? AND phone = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, phone);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR(personExists): " + e.getMessage());
        }
        return false;
    }

    private static boolean animalExists(int personId, String animalName, String animalType) throws SQLException {
        String sql = "SELECT COUNT(*) FROM animal WHERE personId = ? AND name = ? AND type = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, personId);
            statement.setString(2, animalName);
            statement.setString(3, animalType);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR(personExists): " + e.getMessage());
        }
        return false;
    }


    public static void clearTables() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            // Отключаем проверку внешних ключей для удаления
            statement.executeUpdate("PRAGMA foreign_keys = OFF;");

            // Очищаем таблицы
            statement.executeUpdate("DELETE FROM registry;");
            statement.executeUpdate("DELETE FROM animal;");
            statement.executeUpdate("DELETE FROM person;");

            // Включаем проверку внешних ключей
            statement.executeUpdate("PRAGMA foreign_keys = ON;");

            System.out.println("Tables cleared successfully.");
        } catch (SQLException e) {
            System.out.println("ERROR(clearTables): " + e.getMessage());
        }
    }
}