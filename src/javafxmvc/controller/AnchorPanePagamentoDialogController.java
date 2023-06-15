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
import javafx.stage.Stage;

import java.sql.Connection;
import java.time.LocalDate;

import src.javafxmvc.model.domain.Aluno;
import src.javafxmvc.model.dao.AlunoDAO;
import src.javafxmvc.model.domain.Pagamento;
import src.javafxmvc.model.dao.PagamentoDAO;
import src.javafxmvc.model.dao.PlanoDAO;
import src.javafxmvc.model.database.Database;
import src.javafxmvc.model.database.DatabaseFactory;

public class AnchorPanePagamentoDialogController implements Initializable {
    // Declaração dos elementos da interface gráfica
    @FXML
    private ComboBox<Aluno> comboBoxAlunos;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label labelValor;
    @FXML
    private Label labelDesconto;
    @FXML
    private Label labelTitulo;

    //labels de erro
    @FXML
    private Label labelErroAluno;
    @FXML
    private Label labelErroData;

    // Listas e objetos para manipulação dos dados
    private List<Aluno> listAlunos = new ArrayList<>();
    private ObservableList<Aluno> observableListAlunos;
    private Stage dialogStage;
    private boolean buttonConfirmed = false;
    private Pagamento pagamento;
    private AlunoDAO alunoDAO = new AlunoDAO();
    private PlanoDAO planoDAO = new PlanoDAO();

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final PagamentoDAO pagamentoDAO = new PagamentoDAO();

    
    // Método de inicialização do controller, executado quando a view é carregada
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Configura as conexões com o banco de dados
        pagamentoDAO.setConnection(connection);
        alunoDAO.setConnection(connection);
        planoDAO.setConnection(connection);

        // Carrega os alunos no combobox
        loadAlunos();
    }
    
    // Retorna o Stage do diálogo
    public Stage getDialogStage() {
        return dialogStage;
    }

    // Define o Stage do diálogo
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    // Define o título do diálogo com base no botão pressionado
    public void setTitle(int button){
        switch(button){
            case 0:
                this.labelTitulo.setText("Registrar Pagamento");
                break;
            case 1:
                this.labelTitulo.setText("Alterar Pagamento");
                break;
        }
        
    }

    // Retorna o pagamento
    public Pagamento getPagamento() {
        return this.pagamento;
    }

    // Define o objeto de pagamento e atualiza os campos correspondentes no diálogo
    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
        Aluno alunoComboBox = alunoDAO.findById(pagamento.getAluno_id());
        comboBoxAlunos.getSelectionModel().select(alunoComboBox);
        datePicker.setValue(pagamento.getData());
        labelValor.setText(String.valueOf(pagamento.getValor()));
    }

    // Retorna se o botão de confirmação foi pressionado
    public boolean isButtonConfirmed() {
        return buttonConfirmed;
    }
    
    // Método chamado quando o botão de confirmação é clicado
    @FXML
    public void handleButtonConfirm() {
        // limpa os erros
        cleanErrors();

        // Valida os dados inseridos
        if (validateData()){
            // Obtém o aluno selecionado no combobox
            Aluno aluno = comboBoxAlunos.getSelectionModel().getSelectedItem();
                                  
            //atualizando os pontos do aluno casa ele esteja apto ao desconto
            if(aluno.getPontos() >= 150){
                aluno.setPontos(aluno.getPontos() - 150);
                alunoDAO.update(aluno);
            }

            // Define os dados de pagamento com base nas entradas do usuário
            pagamento.setAluno_id(aluno.getIdAluno());
            pagamento.setData(datePicker.getValue());
            pagamento.setValor(Float.parseFloat(labelValor.getText()));            
            
            // Define que o botão de confirmação foi pressionado e fecha o diálogo
            buttonConfirmed = true;
            dialogStage.close();
        }
    }

    // Método responsável por fechar o diálogo quando o botão de cancelar é clicado
    public void handleButtonCancel(){
        getDialogStage().close();
    }

    // Carrega a lista de alunos no combobox
    public void loadAlunos(){
        listAlunos = alunoDAO.list();
        observableListAlunos = FXCollections.observableArrayList(listAlunos);
        comboBoxAlunos.setItems(observableListAlunos);
    }

    // Limpa as mensagens de erro exibidas
    public void cleanErrors() {
        labelErroAluno.setText(null);
        labelErroData.setText(null);
    }

    // Valida os dados inseridos pelo usuário
    public boolean validateData(){
        int qdtErros = 0;

        // Verifica se um aluno foi selecionado
        if (comboBoxAlunos.getSelectionModel().getSelectedItem() == null) {
            labelErroAluno.setText("Selecione um aluno");
            qdtErros++;
        }
        // Verifica se uma data de pagamento válida foi selecionada
        if (datePicker.getValue() == null) {
            labelErroData.setText("Selecione uma data de pagamento válida.");
            qdtErros++;
        // Verifica se é uma data futura
        } else if (datePicker.getValue().isAfter(LocalDate.now())) {
            labelErroData.setText("A data de pagamento não pode ser futura.");
            qdtErros++;
        }

        // Retorna false se houver erros, caso contrário, retorna true
        if(qdtErros > 0){
            return false;
        }else{
            return true;
        }
    }

    /* Método responsável por verificar a pontuação 
     * e a possibilidade de um desconto quando um aluno é selcionado 
    */ 
    @FXML
    public void handleComboBoxAlunos() {
        Aluno alunoSelecionado = comboBoxAlunos.getSelectionModel().getSelectedItem();

        if (alunoSelecionado != null) {
            if(alunoSelecionado.getPontos() >= 150) {
                // Calcula o preço com desconto para o aluno que possui pontos suficientes
                float preco = planoDAO.getPrice(alunoSelecionado.getPlano_id());
                // Aplica desconto de 15%
                preco *= 0.85;
                labelValor.setText(String.valueOf(preco));
                labelDesconto.setText("Desconto aplicado");
                // Define a cor do texto como verde para indicar o desconto aplicado
                labelDesconto.setStyle("-fx-text-fill: #3CB370;");
            }else {
                // Define o preço normal 
                labelValor.setText(String.valueOf(planoDAO.getPrice(alunoSelecionado.getPlano_id())));
                // Exibe uma mensagem indicando a falta de pontos para o desconto
                labelDesconto.setText("Não foi possível aplicar desconto, quantidade de pontos insuficiente (" + alunoSelecionado.getPontos() + ")");
            }
        }
    }
    
    
}