package utils;

import model.StudentInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;


public class DBUtils {
    //найти студента по ID(В графическои интерфейсе не используется)
    public static StudentInfo findStudent(Connection connection, int id) throws SQLException{
        String query = "SELECT * FROM STUDENTS WHERE PERSON_ID=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            String name = rs.getString("FIRST_NAME");
            String secondName = rs.getString("SECOND_NAME");
            String lastName = rs.getString("LAST_NAME");
            LocalDate birthDay = rs.getDate("BIRTH_DAY").toLocalDate();
            String className = rs.getString("CLASS");
            StudentInfo studentInfo = new StudentInfo(id, name, secondName, lastName, birthDay, className);
            return studentInfo;
        }
        return null;
    }
    //получить список студентов
    public static List<StudentInfo> getAllStudentList(Connection connection) throws SQLException{
        String query = "SELECT * FROM STUDENTS;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        List<StudentInfo> students = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("PERSON_ID");
            String name = rs.getString("FIRST_NAME");
            String secondName = rs.getString("SECOND_NAME");
            String lastName = rs.getString("LAST_NAME");
            LocalDate birthDay = rs.getDate("BIRTH_DAY").toLocalDate();
            String className = rs.getString("CLASS");
            students.add(new StudentInfo(id, name, secondName, lastName, birthDay, className));
        }
        return students;
    }
    //получить список студентов группы
    public static List<StudentInfo> getStudentInGroupList(Connection connection, String className) throws SQLException{
        String query = "SELECT * FROM STUDENTS WHERE CLASS=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, className);
        ResultSet rs = statement.executeQuery();
        List<StudentInfo> students = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("PERSON_ID");
            String name = rs.getString("FIRST_NAME");
            String secondName = rs.getString("SECOND_NAME");
            String lastName = rs.getString("LAST_NAME");
            LocalDate birthDay = rs.getDate("BIRTH_DAY").toLocalDate();
            students.add(new StudentInfo(id, name, secondName, lastName, birthDay, className));
        }
        return students;
    }
    //обновить иформацию о студенте
    public static void updateStudent(Connection connection, StudentInfo studentInfo)throws SQLException {
        String query = "UPDATE STUDENTS SET FIRST_NAME = ?, SECOND_NAME = ?, LAST_NAME = ?, BIRTH_DAY = ?, CLASS = ? WHERE PERSON_ID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, studentInfo.getName());
        statement.setString(2, studentInfo.getSecondName());
        statement.setString(3, studentInfo.getLastName());
        statement.setDate(4, Date.valueOf(studentInfo.getBirthDay()));
        statement.setString(5, studentInfo.getClassName());
        statement.setInt(6, studentInfo.getId());
        statement.executeUpdate();
    }
    //вставить нового студента в базу
    public static void insertStudent(Connection connection, StudentInfo studentInfo)throws SQLException{
        String query = "INSERT INTO STUDENTS (PERSON_ID, FIRST_NAME, SECOND_NAME, LAST_NAME, BIRTH_DAY, CLASS) VALUES(?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, studentInfo.getId());
        statement.setString(2, studentInfo.getName());
        statement.setString(3, studentInfo.getSecondName());
        statement.setString(4, studentInfo.getLastName());
        statement.setDate(5, Date.valueOf(studentInfo.getBirthDay()));
        statement.setString(6, studentInfo.getClassName());
        statement.executeUpdate();
    }
    //удалить студента
    public static void deleteStudent(Connection connection, int id) throws SQLException{
        String query = "DELETE FROM STUDENTS WHERE PERSON_ID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        statement.executeUpdate();
    }
}
