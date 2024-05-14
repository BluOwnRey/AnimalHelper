package com.example.animalhelper;

//import java.sql.*;


public class DB {
//    // Функция для получения подключения к базе данных
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection("jdbc:sqlite:database.db");
//    }
//
//    // Функция для создания таблиц
//    public static void createTables() {
//        try (Connection connection = getConnection();
//             Statement statement = connection.createStatement()) {
//            // Создание таблицы "person"
//            statement.executeUpdate("CREATE TABLE IF NOT EXISTS person (id INTEGER PRIMARY KEY, name TEXT, phone TEXT)");
//
//            // Создание таблицы "animal"
//            statement.executeUpdate("CREATE TABLE IF NOT EXISTS animal (id INTEGER PRIMARY KEY, id_person INTEGER, name TEXT, type TEXT, " +
//                    "FOREIGN KEY (id_person) REFERENCES person(id))");
//
//            // Создание таблицы "registry"
//            statement.executeUpdate("CREATE TABLE IF NOT EXISTS registry (id_animal INTEGER, date TEXT, FOREIGN KEY (id_animal) REFERENCES animal(id))");
//        } catch (SQLException e) {
//            System.out.println("ERROR: "+e);
//        }
//    }
//
//
//    public static int addPerson(String name, String phone) {
//        try (Connection connection = getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM person WHERE name = ? AND phone = ?")) {
//
//            // Проверяем, существует ли уже пользователь с таким именем и номером телефона
//            preparedStatement.setString(1, name);
//            preparedStatement.setString(2, phone);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            if (resultSet.next()) {
//                // Если пользователь уже существует, возвращаем его id
//                return resultSet.getInt("id");
//            } else {
//                // Если пользователь новый, добавляем его в базу данных
//                try (PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO person (name, phone) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
//                    insertStatement.setString(1, name);
//                    insertStatement.setString(2, phone);
//                    insertStatement.executeUpdate();
//
//                    // Получаем id нового пользователя
//                    ResultSet generatedKeys = insertStatement.getGeneratedKeys();
//                    if (generatedKeys.next()) {
//                        return generatedKeys.getInt(1);
//                    } else {
//                        throw new SQLException("Failed to get new person id.");
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            System.out.println("ERROR: "+e);
//            return -1; // В случае ошибки возвращаем -1
//        }
//    }
//
//    public static int addAnimal(int personId, String animalName, String animalType) {
//        try (Connection connection = getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM animal WHERE id_person = ? AND name = ? AND type = ?")) {
//
//            // Проверяем, существует ли уже животное у данного человека с таким именем и типом
//            preparedStatement.setInt(1, personId);
//            preparedStatement.setString(2, animalName);
//            preparedStatement.setString(3, animalType);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            if (resultSet.next()) {
//                // Если животное уже существует, возвращаем его id
//                return resultSet.getInt("id");
//            } else {
//                // Если животное новое, добавляем его в базу данных
//                try (PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO animal (id_person, name, type) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
//                    insertStatement.setInt(1, personId);
//                    insertStatement.setString(2, animalName);
//                    insertStatement.setString(3, animalType);
//                    insertStatement.executeUpdate();
//
//                    // Получаем id нового животного
//                    ResultSet generatedKeys = insertStatement.getGeneratedKeys();
//                    if (generatedKeys.next()) {
//                        return generatedKeys.getInt(1);
//                    } else {
//                        throw new SQLException("Failed to get new animal id.");
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            System.out.println("ERROR: " + e);
//            return -1; // В случае ошибки возвращаем -1
//        }
//    }
//
//    public static void addRegistry(int animalId, String date) {
//        try (Connection connection = getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO registry (id_animal, date) VALUES (?, ?)")) {
//
//            preparedStatement.setInt(1, animalId);
//            preparedStatement.setString(2, date);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println("ERROR: " + e);
//        }
//    }
}
