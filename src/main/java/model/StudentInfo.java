package model;

import java.time.LocalDate;
//информация о студенте
public class StudentInfo {
    private int id;
    private String name;
    private String secondName;
    private String lastName;
    private LocalDate birthDay;
    private String className;

    public StudentInfo(int id, String name, String secondName, String lastName, LocalDate birthDay, String className) {
        this.id = id;
        this.name = name;
        this.secondName = secondName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.className = className;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public String getClassName() {
        return className;
    }
}
