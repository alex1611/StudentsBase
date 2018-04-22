package view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.StudentInfo;
import utils.DBUtils;
import utils.MySqlConnection;


import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


public class TableStudents extends Application {
    private static Connection connection = null;
    private TextField idInput;
    private TextField nameInput;
    private TextField secondNameInput;
    private TextField lastNameInput;
    private TextField birthDayInput;
    private TextField classNameInput;
    private TableView<StudentInfo> table;
    public static void setConnection(Connection connection) {
        TableStudents.connection = connection;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        table = new TableView<>();
        TableColumn<StudentInfo, Integer> idCol = new TableColumn<>("ID");
        TableColumn<StudentInfo, String> nameCol = new TableColumn<>("Имя");
        TableColumn<StudentInfo, String> secondNameCol = new TableColumn<>("Фамилия");
        TableColumn<StudentInfo, String> lastNameCol = new TableColumn<>("Отчество");
        TableColumn<StudentInfo, LocalDate> birthdayCol = new TableColumn<>("Дата рождения");
        TableColumn<StudentInfo, String> classCol = new TableColumn<>("Номер группы");
        idCol.setMinWidth(30);
        nameCol.setMinWidth(200);
        secondNameCol.setMinWidth(200);
        lastNameCol.setMinWidth(200);
        birthdayCol.setMinWidth(200);
        classCol.setMinWidth(200);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        secondNameCol.setCellValueFactory(new PropertyValueFactory<>("secondName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        birthdayCol.setCellValueFactory(new PropertyValueFactory<>("birthDay"));
        classCol.setCellValueFactory(new PropertyValueFactory<>("className"));


        table.setEditable(true);
        nameCol.setCellFactory(TextFieldTableCell.<StudentInfo>forTableColumn());
        nameCol.setOnEditCommit(this::setNameCell);
        secondNameCol.setCellFactory(TextFieldTableCell.<StudentInfo>forTableColumn());
        secondNameCol.setOnEditCommit(this::setSecondNameCell);
        lastNameCol.setCellFactory(TextFieldTableCell.<StudentInfo>forTableColumn());
        lastNameCol.setOnEditCommit(this::setLastNameCell);
        birthdayCol.setCellFactory(TextFieldTableCell.<StudentInfo, LocalDate>forTableColumn(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                return object.toString();
            }

            @Override
            public LocalDate fromString(String string) {
                return LocalDate.parse(string);
            }
        }));
        birthdayCol.setOnEditCommit(this::setDateCell);
        classCol.setCellFactory(TextFieldTableCell.<StudentInfo>forTableColumn());
        classCol.setOnEditCommit(this::setClassCell);
        //поле для вставки и удаления нового студента
        Button addButton = new Button("Добавить");
        addButton.setOnAction(event -> addButtonClicked());
        Button delButton = new Button("Удалить");
        delButton.setOnAction(event -> delButtonClicked());
        idInput = new TextField();
        idInput.setPromptText("ID");
        idInput.setMaxWidth(30);
        nameInput = new TextField();
        nameInput.setPromptText("Имя");
        secondNameInput = new TextField();
        secondNameInput.setPromptText("Фамилия");
        lastNameInput = new TextField();
        lastNameInput.setPromptText("Отчество");
        birthDayInput = new TextField();
        birthDayInput.setPromptText("Дата рождения");
        birthDayInput.setMaxWidth(150);
        classNameInput = new TextField();
        classNameInput.setPromptText("Номер группы");
        classNameInput.setMaxWidth(150);
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(idInput,nameInput, secondNameInput, lastNameInput,
                birthDayInput, classNameInput,addButton,delButton);

        table.setItems(getStudents());
        table.getColumns().addAll(idCol, nameCol, secondNameCol, lastNameCol,birthdayCol,classCol);
        VBox root = new VBox();
        root.getChildren().addAll(table,hBox);
        Scene scene = new Scene(root,1100,400);
        primaryStage.setTitle("Студенты групп");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void addButtonClicked() {
        try {
            int newId = Integer.valueOf(idInput.getText());
            String newName = nameInput.getText();
            String newSecondName = secondNameInput.getText();
            String newLastName = lastNameInput.getText();
            LocalDate newBirthDay = LocalDate.parse(birthDayInput.getText());
            String newClassName = classNameInput.getText();
            StudentInfo newStudent = new StudentInfo(newId, newName, newSecondName, newLastName, newBirthDay, newClassName);
            DBUtils.insertStudent(connection, newStudent);
            table.getItems().add(newStudent);
            idInput.clear();
            nameInput.clear();
            secondNameInput.clear();
            lastNameInput.clear();
            birthDayInput.clear();
            classNameInput.clear();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error: insert error");
            alert.setContentText("Try again, fill all inputs");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void delButtonClicked(){
        ObservableList<StudentInfo> selectedStudents, allStudents;
        allStudents = table.getItems();
        selectedStudents = table.getSelectionModel().getSelectedItems();
        try{
            for (StudentInfo student: selectedStudents){
                allStudents.remove(student);
                DBUtils.deleteStudent(connection,student.getId());
            }
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error: delete student");
            alert.setContentText("Try again, fill all inputs");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    private ObservableList<StudentInfo> getStudents(){
        List<StudentInfo> students = null;
        try {
            students = DBUtils.getAllStudentList(connection);
        } catch (SQLException e) {
            MySqlConnection.rollback(connection);
            e.printStackTrace();
        }
        ObservableList<StudentInfo> list = FXCollections.observableArrayList(students);
        return list;
    }
    private void setNameCell(TableColumn.CellEditEvent<StudentInfo, String> event){
        TablePosition<StudentInfo, String> position = event.getTablePosition();
        String newName = event.getNewValue();
        int row = position.getRow();
        StudentInfo studentInfo = event.getTableView().getItems().get(row);
        studentInfo.setName(newName);
        try {
            DBUtils.updateStudent(connection,studentInfo);
        } catch (SQLException e){
            MySqlConnection.rollback(connection);
            e.printStackTrace();
        }
    }
    private void setSecondNameCell(TableColumn.CellEditEvent<StudentInfo, String> event){
        TablePosition<StudentInfo, String> position = event.getTablePosition();
        String newSecondName = event.getNewValue();
        int row = position.getRow();
        StudentInfo studentInfo = event.getTableView().getItems().get(row);
        studentInfo.setSecondName(newSecondName);
        try {
            DBUtils.updateStudent(connection,studentInfo);
        } catch (SQLException e){
            MySqlConnection.rollback(connection);
            e.printStackTrace();
        }
    }
    private void setLastNameCell(TableColumn.CellEditEvent<StudentInfo, String> event){
        TablePosition<StudentInfo, String> position = event.getTablePosition();
        String newLastName = event.getNewValue();
        int row = position.getRow();
        StudentInfo studentInfo = event.getTableView().getItems().get(row);
        studentInfo.setLastName(newLastName);
        try {
            DBUtils.updateStudent(connection,studentInfo);
        } catch (SQLException e){
            MySqlConnection.rollback(connection);
            e.printStackTrace();
        }
    }
    private void setDateCell(TableColumn.CellEditEvent<StudentInfo, LocalDate> event){
        TablePosition<StudentInfo, LocalDate> position = event.getTablePosition();
        LocalDate newBirthDay = event.getNewValue();
        int row = position.getRow();
        StudentInfo studentInfo = event.getTableView().getItems().get(row);
        studentInfo.setBirthDay(newBirthDay);
        try {
            DBUtils.updateStudent(connection,studentInfo);
        } catch (SQLException e){
            MySqlConnection.rollback(connection);
            e.printStackTrace();
        }
    }
    private void setClassCell(TableColumn.CellEditEvent<StudentInfo, String> event){
        TablePosition<StudentInfo, String> position = event.getTablePosition();
        String newClassName = event.getNewValue();
        int row = position.getRow();
        StudentInfo studentInfo = event.getTableView().getItems().get(row);
        studentInfo.setClassName(newClassName);
        try {
            DBUtils.updateStudent(connection,studentInfo);
        } catch (SQLException e){
            MySqlConnection.rollback(connection);
            e.printStackTrace();
        }
    }
    public static void run(String[] args,Connection connection) {
        setConnection(connection);
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        MySqlConnection.closeConnection(connection);
        super.stop();
    }
}
