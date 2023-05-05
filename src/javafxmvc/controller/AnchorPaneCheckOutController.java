package src.javafxmvc.controller;

import java.net.URL;
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

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import src.javafxmvc.model.domain.Aluno;
import src.javafxmvc.model.dao.AlunoDAO;
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

    private List<Aluno> listAlunos = new ArrayList<>(); //Mudar para aluno depois
    private ObservableList<Aluno> observableListAlunos;
    private DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final CheckOutDAO checkOutDAO = new CheckOutDAO();

    private AlunoDAO alunoDAO = new AlunoDAO();
   
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        checkOutDAO.setConnection(connection);
        alunoDAO.setConnection(connection);

        loadAlunos();
    }
    
    @FXML
    public void handleButtonSave() {
        cleanErrors();

        //talvez fazer aquela parada de rollback

        if (validateData()){
            String dataFormatada = datePicker.getValue().format(formato);
            CheckOut checkOut = new CheckOut();
            Aluno aluno = comboBoxAlunos.getSelectionModel().getSelectedItem();

            /*
            checkOut.setAluno_id(aluno.getIdAluno());
            checkOut.setData(LocalDate.parse(dataFormatada, formato));
            checkOut.setValor(Float.parseFloat(labelValor.getText()));            
            
            checkOutDAO.insert(checkOut);
            */
        }
    }

    public void handleButtonCancel(){
        //getDialogStage().close();
    }

    public void loadAlunos(){
        listAlunos = alunoDAO.list();
        observableListAlunos = FXCollections.observableArrayList(listAlunos);
        comboBoxAlunos.setItems(observableListAlunos);
    }

    public void cleanErrors() {
        labelErroAluno.setText(null);
        labelErroData.setText(null);
    }

    public boolean validateData(){
        int qdtErros = 0;

        if (comboBoxAlunos.getSelectionModel().getSelectedItem() == null) {
            labelErroAluno.setText("Selecione um aluno");
            qdtErros++;
        }
        if (datePicker.getValue() == null) {
            labelErroData.setText("Selecione uma data de checkOut válida.");
            qdtErros++;
        } else if (datePicker.getValue().isAfter(LocalDate.now())) {
            labelErroData.setText("A data de checkOut não pode ser futura.");
            qdtErros++;
        }//verificar se existe um check in neste dia

        //verificar hora 
        //verificar formato da hora
        if(qdtErros > 0){
            return false;
        }else{
            return true;
        }
    }

    /*
    @FXML
    public void handleComboBoxAlunos() {
        Aluno alunoSelecionado = comboBoxAlunos.getSelectionModel().getSelectedItem();

        if (alunoSelecionado != null) {
            if(alunoSelecionado.getPontos() >= 150) {
                float preco = planoDAO.getPrice(alunoSelecionado.getPlano_id());
                preco *= 0.85;
                labelValor.setText(String.valueOf(preco));
                labelDesconto.setText("Desconto aplicado");
                labelDesconto.setStyle("-fx-text-fill: #3CB370;");
            }else {
                labelValor.setText(String.valueOf(planoDAO.getPrice(alunoSelecionado.getPlano_id())));
                labelDesconto.setText("Não foi possível aplicar desconto, quantidade de pontos insuficiente (" + alunoSelecionado.getPontos() + ")");
            }
        }
    }
    */
}
