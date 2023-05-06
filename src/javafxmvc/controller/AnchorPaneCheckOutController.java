package src.javafxmvc.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import src.javafxmvc.model.domain.Aluno;
import src.javafxmvc.model.dao.AlunoDAO;
import src.javafxmvc.model.domain.CheckIn;
import src.javafxmvc.model.dao.CheckInDAO;
import src.javafxmvc.model.domain.CheckOut;
import src.javafxmvc.model.dao.CheckOutDAO;
import src.javafxmvc.model.database.Database;
import src.javafxmvc.model.database.DatabaseFactory;

public class AnchorPaneCheckOutController implements Initializable {
    @FXML
    private ComboBox<Aluno> comboBoxAlunos;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label labelValor;
    @FXML
    private TextField textFieldHora;

    //labels de erro
    @FXML
    private Label labelErroAluno;
    @FXML
    private Label labelErroData;
    @FXML
    private Label labelErroHora;

    private List<Aluno> listAlunos = new ArrayList<>(); 
    private ObservableList<Aluno> observableListAlunos;
    DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final CheckOutDAO checkOutDAO = new CheckOutDAO();
    private final CheckInDAO checkInDAO = new CheckInDAO();
    private final AlunoDAO alunoDAO = new AlunoDAO();
   
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        checkOutDAO.setConnection(connection);
        alunoDAO.setConnection(connection);
        checkInDAO.setConnection(connection);

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> { loadTime();});
        loadAlunos();
    }
    
    @FXML
    public void handleButtonSave() {
        cleanErrors();
        int idCheckIn = validateData();
        if ( idCheckIn != 0){
            CheckOut checkOut = new CheckOut();
            checkOut.setData(datePicker.getValue());
            checkOut.setHora(LocalTime.parse(textFieldHora.getText(), formatoHora));      
            checkOut.setCheckIn_id(idCheckIn);
            checkOutDAO.insert(checkOut);
            
            showConfirmationAlert();
        }
    }

    public void loadAlunos(){
        listAlunos = alunoDAO.list();
        observableListAlunos = FXCollections.observableArrayList(listAlunos);
        comboBoxAlunos.setItems(observableListAlunos);
    }

    public void loadTime(){
        String horaFormatada = LocalTime.now().format(formatoHora);
        textFieldHora.setText(horaFormatada);
    }

    public void cleanErrors() {
        labelErroAluno.setText(null);
        labelErroData.setText(null);
        labelErroHora.setText(null);
    }

    /** 
     * Retorna idCheckIn; caso todas as validações estejam corretas
     * Retorna 0; caso contrário
     */
    public int validateData(){
        Aluno alunoSelecionado = comboBoxAlunos.getSelectionModel().getSelectedItem();
        int idCheckIn = 0;
        
        //verifica se selecionou um aluno
        if (alunoSelecionado == null) { 
            labelErroAluno.setText("Selecione um aluno.");
            return idCheckIn;
        }

        //verifica se a data está vazia
        if (datePicker.getValue() == null) {
            labelErroData.setText("Selecione uma data de checkOut válida.");
            return idCheckIn;
        } 

        //verifica se é uma data futura
        if (datePicker.getValue().isAfter(LocalDate.now())) {
            labelErroData.setText("A data de checkOut não pode ser futura.");
            return idCheckIn;
        }
        
        CheckIn checkInAnterior = checkInDAO.search(alunoSelecionado.getIdAluno(), datePicker.getValue());
    
        //verifica se existe um check in realizado por aquele aluno naquela data
        if(checkInAnterior.getIdCheckIn() == 0){
            labelErroData.setText("Não foi realizado nenhum check in por este aluno nesta data.");
            return idCheckIn;
        }

        //verifica se já existe um check out realizado por aquele aluno naquela data
        if (checkOutDAO.search(checkInAnterior.getIdCheckIn())){
            labelErroData.setText("Já existe um check out realizado por este aluno nesta data.");
            return idCheckIn;
        }

        //verifica se a hora está vazia
        if (textFieldHora.getText() == null) {
            labelErroHora.setText("O campo hora não pode estar vazio.");
            return idCheckIn;
        }

        //verifica se a hora está no formato correto
        if (!validateTime(textFieldHora.getText())){
            labelErroHora.setText("Hora inválida! A hora deve estar no formato HH:mm:ss (por exemplo, 09:30:00)");
            return idCheckIn;
        }

        //verifica se a hora do check out é maior que a hora do check in
        if (checkInAnterior.getHora().compareTo(LocalTime.parse(textFieldHora.getText(), formatoHora)) < 0) {
            idCheckIn = checkInAnterior.getIdCheckIn(); 
            return idCheckIn; 
        }else{
            labelErroHora.setText("A hora do check out deve ser superior a hora do check in");
            return idCheckIn;
        }
    }

    public boolean validateTime (String hora) {
        try {
            LocalTime horaLocal = LocalTime.parse(hora, formatoHora);
            return horaLocal != null;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @FXML
    public void handleButtonCancel(){
        //getDialogStage().close();
    }

    public void showConfirmationAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Check-Out Registrado");
        alert.setHeaderText(null);
        alert.setContentText("Seu check-out foi registrado com sucesso!!");

        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            alert.close();
            //getDialogStage().close();
        }
    }
}