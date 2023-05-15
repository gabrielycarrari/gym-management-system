package src.javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.javafxmvc.model.dao.AlunoDAO;
import src.javafxmvc.model.dao.CheckInDAO;
import src.javafxmvc.model.dao.CheckOutDAO;
import src.javafxmvc.model.database.Database;
import src.javafxmvc.model.database.DatabaseFactory;
import src.javafxmvc.model.domain.Aluno;
import src.javafxmvc.model.domain.CheckIn;

public class AnchorPaneCheckInDialogController implements Initializable{

    @FXML
    private ComboBox<Aluno> comboBoxAlunos;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label labelValor;

    @FXML
    private TextField textFieldHora;

    @FXML
    private Label labelTitulo;


    // Labels de erro
    @FXML
    private Label labelErroAluno;

    @FXML
    private Label labelErroData;
    
    @FXML
    private Label labelErroHora;

    private List<Aluno> listAlunos = new ArrayList<>(); 
    private ObservableList<Aluno> observableListAlunos;

    DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");

    private Stage dialogStage;
    
    private boolean buttonConfirmed = false;

    private CheckIn checkIn;


    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final CheckInDAO checkInDAO = new CheckInDAO();
    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final CheckOutDAO checkOutDAO = new CheckOutDAO();
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alunoDAO.setConnection(connection);
        checkInDAO.setConnection(connection);
        checkOutDAO.setConnection(connection);

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> { loadTime();});
        loadAlunos();
    }
    
    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setTitle(int button){
        switch(button){
            case 0:
                this.labelTitulo.setText("Registrar CheckIn");
                break;
            case 1:
                this.labelTitulo.setText("Alterar CheckIn");
                break;
        }
        
    }

    public CheckIn getCheckIn() {
        return this.checkIn;
    }

    public void setCheckIn(CheckIn checkIn) {
        this.checkIn = checkIn;
        Aluno alunoComboBox = alunoDAO.findById(checkIn.getAluno_id());
        comboBoxAlunos.getSelectionModel().select(alunoComboBox);
        datePicker.setValue(checkIn.getData());
        if (checkIn.getHora() != null) {
            textFieldHora.setText(checkIn.getHora().toString());
        } else {
            textFieldHora.setText("");
        }
    }

    public boolean isButtonConfirmed() {
        return buttonConfirmed;
    }
    
    @FXML
    public void handleButtonConfirm() {
        cleanErrors();

        if (validateData()){
            Aluno aluno = comboBoxAlunos.getSelectionModel().getSelectedItem();

            checkIn.setData(datePicker.getValue());
            checkIn.setHora(LocalTime.parse(textFieldHora.getText(), formatoHora));      
            checkIn.setAluno_id(aluno.getIdAluno());
            
            buttonConfirmed = true;
            dialogStage.close();
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
    
    public boolean validateData(){
        Aluno alunoSelecionado = comboBoxAlunos.getSelectionModel().getSelectedItem();

        int qdtErros = 0;

        //verifica se selecionou um aluno
        if (alunoSelecionado == null) { 
            labelErroAluno.setText("Selecione um aluno.");
            qdtErros++;
        }

        //verifica se a data está vazia
        if (datePicker.getValue() == null) {
            labelErroData.setText("Selecione uma data de checkIn válida.");
            qdtErros++;
        }

        //verifica se é uma data futura
        if (datePicker.getValue() != null) {
            if(datePicker.getValue().isAfter(LocalDate.now())) {
                labelErroData.setText("A data de checkIn não pode ser futura.");
                qdtErros++;
            }

            /* 
            * Verifica se já existe um check out realizado por aquele aluno naquela data 
            * e se é um novo registro, pois caso seja uma alteração ele permite realizar. 
            */
            CheckIn checkInAnterior = checkInDAO.search(alunoSelecionado.getIdAluno(), datePicker.getValue());
            
            if ((checkInAnterior.getIdCheckIn() != 0) && labelTitulo.getText().equals("Registrar CheckIn")){
                labelErroData.setText("Já existe um check in realizado por este aluno nesta data.");
                qdtErros++;
            }
        }

        //verifica se a hora está vazia
        if (textFieldHora.getText() == null) {
            labelErroHora.setText("O campo hora não pode estar vazio.");
            qdtErros++;
        }
        else {
            //verifica se a hora está no formato correto
            if (!validateTime(textFieldHora.getText())){
                labelErroHora.setText("Hora inválida! A hora deve estar no formato HH:mm:ss (por exemplo, 09:30:00)");
                qdtErros++;
            }
            else if(checkOutDAO.search(checkIn.getIdCheckIn())) {
                if(checkIn.getHora().compareTo(LocalTime.parse(textFieldHora.getText(), formatoHora)) > 0 ) {
                    labelErroHora.setText("O horário de checkin não pode ser menor que o de checkout");
                    qdtErros++;
                }
            }
        }


        // Se houver erros, retorna falso, caso contrário retorna verdadeiro
        if(qdtErros > 0){
            return false;
        }else{
            return true;
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
        getDialogStage().close();
    }
}
