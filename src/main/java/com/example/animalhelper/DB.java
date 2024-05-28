package com.example.animalhelper;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
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
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();

            // Создание таблицы "person"
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS person (id INTEGER PRIMARY KEY, name TEXT, phone TEXT)");

            // Создание таблицы "animal"
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS animal (id INTEGER PRIMARY KEY, personId INTEGER, name TEXT, type TEXT, " +
                    "FOREIGN KEY (personId) REFERENCES person(id))");

            // Создание таблицы "registry"
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS registry (id INTEGER PRIMARY KEY, doctorId INTEGER, animalId INTEGER, date TEXT, status BOOLEAN, FOREIGN KEY (animalId) REFERENCES animal(id))");

            // Создание таблицы "animal_info"
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS animal_info (id INTEGER PRIMARY KEY, animalId INTEGER, breed TEXT, gender TEXT, " +
                    "weight TEXT, age TEXT, castration TEXT, date_birth TEXT, vaccine BOOLEAN, vaccine_name TEXT, chronic_diseases TEXT, FOREIGN KEY (animalId) REFERENCES animal(id))");

            // Создание таблицы "treatment"
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS treatment (id INTEGER PRIMARY KEY, registryId INTEGER, " +
                    "diagnosis TEXT, method_treatment TEXT, doctor TEXT, treatment_start_date TEXT, status TEXT, FOREIGN KEY (registryId) REFERENCES registry(id))");


        } catch (SQLException e) {
            System.out.println("ERROR(createTables): " + e.getMessage());
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR(closing resources): " + e.getMessage());
            }
        }
    }

    // Метод для проверки существования человека и получения его id
    private static boolean personExists(String name, String phone) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement("SELECT id FROM person WHERE name = ? AND phone = ?");
            statement.setString(1, name);
            statement.setString(2, phone);
            resultSet = statement.executeQuery();
            return resultSet.next();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR(closing resources): " + e.getMessage());
            }
        }
    }

    // Метод для получения id существующего человека
    private static int getPersonId(String name, String phone) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement("SELECT id FROM person WHERE name = ? AND phone = ?");
            statement.setString(1, name);
            statement.setString(2, phone);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                throw new SQLException("Person not found in database.");
            }
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR(closing resources): " + e.getMessage());
            }
        }
    }

    // Добавление владельца животного
    public static int addPerson(String name, String phone) throws SQLException {
        if (!personExists(name, phone)) {
            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet generatedKeys = null;
            try {
                connection = getConnection();
                statement = connection.prepareStatement("INSERT INTO person (name, phone) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, name);
                statement.setString(2, phone);
                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new Exception("person not added (affectedRows)");
                }
                generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new Exception("no add person (generatedKeys)");
                }
            } catch (Exception e) {
                System.out.println("ERROR (addPerson): " + e.getMessage());
                return 0;
            } finally {
                try {
                    if (generatedKeys != null) generatedKeys.close();
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    System.out.println("ERROR(closing resources): " + e.getMessage());
                }
            }
        } else {
            return getPersonId(name, phone);
        }
    }

    // Метод для проверки существования животного
    private static boolean animalExists(int personId, String animalName, String animalType) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement("SELECT id FROM animal WHERE personId = ? AND name = ? AND type = ?");
            statement.setInt(1, personId);
            statement.setString(2, animalName);
            statement.setString(3, animalType);
            resultSet = statement.executeQuery();
            return resultSet.next();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR(closing resources): " + e.getMessage());
            }
        }
    }

    // Метод для получения id существующего животного
    private static int getAnimalId(int personId, String animalName, String animalType) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement("SELECT id FROM animal WHERE personId = ? AND name = ? AND type = ?");
            statement.setInt(1, personId);
            statement.setString(2, animalName);
            statement.setString(3, animalType);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                throw new SQLException("Animal not found in database.");
            }
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR(closing resources): " + e.getMessage());
            }
        }
    }

    // Добавление животного
    public static int addAnimal(int personId, String animalName, String animalType) throws SQLException {
        if (!animalExists(personId, animalName, animalType)) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet generatedKeys = null;
            try {
                connection = getConnection();
                preparedStatement = connection.prepareStatement("INSERT INTO animal (personId, name, type) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, personId);
                preparedStatement.setString(2, animalName);
                preparedStatement.setString(3, animalType);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new Exception("animal not added (affectedRows)");
                }

                generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new Exception("animal not added (generatedKeys)");
                }
            } catch (Exception e) {
                System.out.println("ERROR (addAnimal): " + e.getMessage());
                return 0;
            } finally {
                try {
                    if (generatedKeys != null) generatedKeys.close();
                    if (preparedStatement != null) preparedStatement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    System.out.println("ERROR(closing resources): " + e.getMessage());
                }
            }
        } else {
            // Если животное уже существует, возвращаем его id
            return getAnimalId(personId, animalName, animalType);
        }
    }

    // Добавление записи о сеансе животного
    public static int addRegistry(int doctorId, int animalId, LocalDate date) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO registry (doctorId, animalId, date, status) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setInt(2, animalId);
            preparedStatement.setString(3, date.toString());
            preparedStatement.setBoolean(4, false);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Adding registry failed, no rows affected.");
            }

            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Adding registry failed, no rows affected.");
            }
        } catch (SQLException e) {
            System.out.println("ERROR(addRegistry): " + e.getMessage());
            return 0;
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR(closing resources): " + e.getMessage());
            }
        }
    }

    // Метод для получения данных о записях
    public static String[][] getAllRegistryData() throws SQLException {
        List<String[]> records = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT p.name AS person_name, p.phone AS person_phone, " +
                    "a.name AS animal_name, a.type AS animal_type, r.id AS registry_id, r.date " +
                    "FROM registry r " +
                    "JOIN animal a ON r.animalId = a.id " +
                    "JOIN person p ON a.personId = p.id");

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
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR(closing resources): " + e.getMessage());
            }
        }

        // Преобразуем список записей в массив и возвращаем его
        String[][] matrix = new String[records.size()][];
        for (int i = 0; i < records.size(); i++) {
            matrix[i] = records.get(i);
        }
        return matrix;
    }

    // Метод для получения данных по идентификатору записи
    public static String[] getRegistryDetails(int registryId) {
        String query = "SELECT animal.id AS animal_id, animal.name AS animal_name, animal.type AS animal_type, " +
                "person.name AS person_name, person.phone AS person_phone " +
                "FROM registry " +
                "JOIN animal ON registry.animalId = animal.id " +
                "JOIN person ON animal.personId = person.id " +
                "WHERE registry.id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, registryId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String animalId = rs.getString("animal_id");
                String animalName = rs.getString("animal_name");
                String animalType = rs.getString("animal_type");
                String personName = rs.getString("person_name");
                String personPhone = rs.getString("person_phone");
                return new String[]{animalId, animalName, animalType, personName, personPhone};
            } else {
                System.out.println("No registry found with id: " + registryId);
            }
        } catch (SQLException e) {
            System.out.println("ERROR(getRegistryDetails): " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("ERROR(closing resources): " + e.getMessage());
            }
        }
        return null; // Если запись не найдена
    }

    // Метод для занесения дополнительных данных о животном
    public static void addOrUpdateAnimalInfo(int animalId, String breed, String gender, String weight,
                                             String age, String castration, String dateBirth,
                                             boolean vaccine, String vaccineName, String chronicDiseases) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DB.getConnection();
            // Используем INSERT OR REPLACE INTO для добавления или обновления записи
            String query = "INSERT OR REPLACE INTO animal_info (animalId, breed, gender, weight, age, castration, date_birth, vaccine, vaccine_name, chronic_diseases) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, animalId);
            statement.setString(2, breed);
            statement.setString(3, gender);
            statement.setString(4, weight);
            statement.setString(5, age);
            statement.setString(6, castration);
            statement.setString(7, dateBirth);
            statement.setBoolean(8, vaccine);
            statement.setString(9, vaccineName);
            statement.setString(10, chronicDiseases);
            statement.executeUpdate();
        } catch (Exception e){
            System.out.println(e);
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    // Метод для получения дополнительных данных о животном
    public static String[] getAnimalInfo(int animalId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DB.getConnection();
            // Запрос для получения данных из таблицы animal_info по animalId
            String query = "SELECT * FROM animal_info WHERE animalId = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, animalId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Если запись существует, получаем данные и возвращаем их в виде массива строк
                String breed = resultSet.getString("breed");
                String gender = resultSet.getString("gender");
                String weight = resultSet.getString("weight");
                String age = resultSet.getString("age");
                String castration = resultSet.getString("castration");
                String dateBirth = resultSet.getString("date_birth");
                boolean vaccine = resultSet.getBoolean("vaccine");
                String vaccineName = resultSet.getString("vaccine_name");
                String chronicDiseases = resultSet.getString("chronic_diseases");
                return new String[]{breed, gender, weight, age, castration, dateBirth, String.valueOf(vaccine), vaccineName, chronicDiseases};
            } else {
                // Если запись не найдена, возвращаем null
                return null;
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    // Метод для занесения данных о проходящем лечении животного
    public static void addTreatment(int registryId, String diagnosis, String methodTreatment, String doctor, String treatmentStartDate, String status) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO treatment (registryId, diagnosis, method_treatment, doctor, treatment_start_date, status) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, registryId);
            preparedStatement.setString(2, diagnosis);
            preparedStatement.setString(3, methodTreatment);
            preparedStatement.setString(4, doctor);
            preparedStatement.setString(5, treatmentStartDate);
            preparedStatement.setString(6, status);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating treatment failed, no rows affected.");
            }

            System.out.println("Treatment added successfully.");
        } catch (SQLException e) {
            System.out.println("ERROR(addTreatment): " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR(closing resources): " + e.getMessage());
            }
        }
    }

    // Метод для получения данных о проходящем лечении животных
    public static String[] getTreatmentDetails(int registryId) {
        String query = "SELECT diagnosis, method_treatment, doctor, treatment_start_date, status " +
                "FROM treatment " +
                "WHERE registryId = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, registryId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String diagnosis = rs.getString("diagnosis");
                String methodTreatment = rs.getString("method_treatment");
                String doctor = rs.getString("doctor");
                String treatmentStartDate = rs.getString("treatment_start_date");
                String status = rs.getString("status");
                return new String[]{diagnosis, methodTreatment, doctor, treatmentStartDate, status};
            } else {
                System.out.println("No treatment found with registryId: " + registryId);
            }
        } catch (SQLException e) {
            System.out.println("ERROR(getTreatmentDetails): " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("ERROR(closing resources): " + e.getMessage());
            }
        }
        return null; // Если запись не найдена
    }

    // Метод для получения данных о записях в treatment
    public static String[][] getAllPatientData() throws SQLException {
        List<String[]> records = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT p.name AS person_name, p.phone AS person_phone, " +
                    "a.name AS animal_name, a.type AS animal_type, r.id AS registry_id, r.date, " +
                    "t.diagnosis, t.status " +
                    "FROM registry r " +
                    "JOIN animal a ON r.animalId = a.id " +
                    "JOIN person p ON a.personId = p.id " +
                    "LEFT JOIN treatment t ON r.id = t.registryId");

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
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR(closing resources): " + e.getMessage());
            }
        }

        // Преобразуем список записей в массив и возвращаем его
        String[][] matrix = new String[records.size()][];
        for (int i = 0; i < records.size(); i++) {
            matrix[i] = records.get(i);
        }
        return matrix;
    }


    // Метод для очистки таблиц
    public static void clearTables() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
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
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR(closing resources): " + e.getMessage());
            }
        }
    }

    // Метод для удаления базы данных
    public static void dropDB() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            // Удаление существующей таблицы "person"
            statement.executeUpdate("DROP TABLE IF EXISTS person");
            // Удаление существующей таблицы "animal"
            statement.executeUpdate("DROP TABLE IF EXISTS animal");
            // Удаление существующей таблицы "registry"
            statement.executeUpdate("DROP TABLE IF EXISTS registry");
            System.out.println("Database dropped.");
        } catch (SQLException e) {
            System.out.println("ERROR(dropDB): " + e.getMessage());
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR(closing resources): " + e.getMessage());
            }
        }
    }
}
