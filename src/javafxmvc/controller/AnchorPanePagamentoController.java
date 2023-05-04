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

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import src.javafxmvc.model.domain.Aluno;
import src.javafxmvc.model.dao.AlunoDAO;
import src.javafxmvc.model.domain.Pagamento;
import src.javafxmvc.model.dao.PagamentoDAO;
import src.javafxmvc.model.dao.PlanoDAO;
import src.javafxmvc.model.database.Database;
import src.javafxmvc.model.database.DatabaseFactory;

public class AnchorPanePagamentoController implements Initializable {
    @FXML
    private ComboBox<Aluno> comboBoxAlunos;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label labelValor;
    @FXML
    private Label labelDesconto;

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
    private final PagamentoDAO pagamentoDAO = new PagamentoDAO();

    private AlunoDAO alunoDAO = new AlunoDAO();
    private PlanoDAO planoDAO = new PlanoDAO();
    
   
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        pagamentoDAO.setConnection(connection);
        alunoDAO.setConnection(connection);
        planoDAO.setConnection(connection);

        loadAlunos();
    }
    
    @FXML
    public void handleButtonRegister() {
        cleanErrors();

        //talvez fazer aquela parada de rollback

        if (validateData()){
            String dataFormatada = datePicker.getValue().format(formato);
            Pagamento pagamento = new Pagamento();
            Aluno aluno = comboBoxAlunos.getSelectionModel().getSelectedItem();
                      
            //atualizando os pontos do aluno casa ele esteja apto ao desconto
            if(aluno.getPontos() >= 150){
                aluno.setPontos(aluno.getPontos() - 150);
                alunoDAO.update(aluno);
            }

            pagamento.setAluno_id(aluno.getIdAluno());
            pagamento.setData(LocalDate.parse(dataFormatada, formato));
            pagamento.setValor(Float.parseFloat(labelValor.getText()));            
            
            pagamentoDAO.insert(pagamento);

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
            labelErroData.setText("Selecione uma data de pagamento válida.");
            qdtErros++;
        } else if (datePicker.getValue().isAfter(LocalDate.now())) {
            labelErroData.setText("A data de pagamento não pode ser futura.");
            qdtErros++;
        }
        if(qdtErros > 0){
            return false;
        }else{
            return true;
        }
    }


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
}
