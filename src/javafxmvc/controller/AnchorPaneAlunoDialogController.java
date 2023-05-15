package src.javafxmvc.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.javafxmvc.model.dao.AlunoDAO;
import src.javafxmvc.model.database.Database;
import src.javafxmvc.model.database.DatabaseFactory;
import src.javafxmvc.model.domain.Aluno;

import java.sql.Connection;



public class AnchorPaneAlunoDialogController implements Initializable {

    // Declaração dos elementos da interface gráfica
    @FXML
    private Label labelTitulo;

    @FXML
    private TextField textFieldNome;

    @FXML
    private TextField textFieldCPF;

    @FXML
    private TextField textFieldEndereco;

    @FXML
    private ComboBox<String> comboBoxGeneros;

    @FXML
    private ComboBox<String> comboBoxPlanos;

    @FXML
    private Button buttonConfirmar;


    // Labels de erro
    @FXML
    private Label labelErroNome;

    @FXML
    private Label labelErroCPF;

    @FXML
    private Label labelErroEndereco;

    @FXML
    private Label labelErroGenero;

    @FXML
    private Label labelErroPlano;


    // Listas e objetos para manipulação dos dados
    private List<String> listGeneros = new ArrayList<>();
    private ObservableList<String> observableListGeneros;

    private List<String> listPlanos = new ArrayList<>();
    private ObservableList<String> observableListPlanos;

    private Stage dialogStage;

    private boolean buttonConfirmed = false;

    private Aluno aluno;


    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final AlunoDAO alunoDAO = new AlunoDAO();


    // Método de inicialização do controller, executado quando a view é carregada
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configura a conexão com o banco de dados
        alunoDAO.setConnection(connection);
                
        // Carrega os generos no combobox
        loadGeneros();

        // Carrega os tipos de planos no combobox
        loadPlanos();
    }


    // Métodos getters e setters
    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    // Método para definir o título da janela de acordo com o botão clicado pelo usuário
    public void setTitle(int button){
        switch(button){
            case 0:
                this.labelTitulo.setText("Cadastrar Aluno");
                break;
            case 1:
                this.labelTitulo.setText("Alterar Aluno");
                break;
        }
        
    }

    public Aluno getAluno() {
        return this.aluno;
    }

    // Método para preencher os campos da janela com as informações do aluno selecionado, caso seja uma alteração
     public void setAluno(Aluno aluno) {
        this.aluno = aluno;

        Integer plano_id = aluno.getPlano_id();
        String plano = (plano_id == 1) ? "Plano mensal" : "Plano anual";

        this.textFieldNome.setText(aluno.getNome());
        this.textFieldCPF.setText(aluno.getCpf());
        this.textFieldEndereco.setText(aluno.getEndereco());
        this.comboBoxGeneros.setValue(aluno.getGenero());
        this.comboBoxPlanos.setValue(plano);
    }

    /*
    * Método que verifica se o botão de confirmação foi pressionado
    * @return true se o botão de confirmação foi pressionado, caso contrário false
    */
    public boolean isButtonConfirmed() {
        return buttonConfirmed;
    }

    //Método chamado quando o botão "Confirmar" é pressionado
    @FXML
    public void handleButtonConfirm() {
        cleanErrors();

        // Valida os dados inseridos pelo usuário
        if (validateData()){
            // Define os valores inseridos pelo usuário como propriedades do objeto "aluno"
            String plano = comboBoxPlanos.getSelectionModel().getSelectedItem();
            Integer plano_id = (plano == "Plano mensal") ? 1 : 2;
            
            String CPF = textFieldCPF.getText();
            aluno.setNome(textFieldNome.getText());
            aluno.setCpf(convertToCPF(CPF));
            aluno.setEndereco(textFieldEndereco.getText());
            aluno.setGenero(comboBoxGeneros.getSelectionModel().getSelectedItem());
            aluno.setPontos(0);
            aluno.setPlano_id(plano_id);
            
            // Define a variável "buttonConfirmed" como true e fecha a janela de diálogo
            buttonConfirmed = true;
            dialogStage.close();
        }
    }


    //Método chamado quando o botão "Cancelar" é pressionado
    @FXML
    public void handleButtonCancel(){
        //fecha a janela de diálogo
        getDialogStage().close();
    }

    // Método responsável por carregar os tipos de alunos na caixa de seleção
    public void loadGeneros(){
        // Adiciona os gêneros à lista
        listGeneros.add("Feminino");
        listGeneros.add("Masculino");
        listGeneros.add("Outro");

        // Converte a lista em um objeto ObservableList e define a caixa de seleção como o seu destino
        observableListGeneros = FXCollections.observableArrayList(listGeneros);
        comboBoxGeneros.setItems(observableListGeneros);
    }  


    // Método responsável por carregar os tipos de alunos na caixa de seleção
    public void loadPlanos(){
        // Adiciona os tipos de alunos à lista
        listPlanos.add("Plano mensal");
        listPlanos.add("Plano anual");

        // Converte a lista em um objeto ObservableList e define a caixa de seleção como o seu destino
        observableListPlanos = FXCollections.observableArrayList(listPlanos);
        comboBoxPlanos.setItems(observableListPlanos);
    }  

    // Método responsável por limpar as mensagens de erro exibidas na interface
    public void cleanErrors() {
        labelErroNome.setText(null);
        labelErroCPF.setText(null);
        labelErroEndereco.setText(null);
        labelErroGenero.setText(null);
        labelErroPlano.setText(null);
    }

    public boolean validateData(){
        int qdtErros = 0;

        // Verifica se o campo Nome está vazio
        if(textFieldNome.getText() == null || textFieldNome.getText().length() == 0) {
            labelErroNome.setText("O campo Nome não pode estar vazio");
            qdtErros++;
        }

        // Verifica se o campo CPF está vazio
        if(textFieldCPF.getText() == null || textFieldCPF.getText().length() == 0) {
            labelErroCPF.setText("O campo CPF não pode estar vazio");
            qdtErros++;
        }
        else if(!validateCPFFormat(textFieldCPF.getText())) {
            labelErroCPF.setText("CPF inválido");
            qdtErros++;
        }

        // Verifica se o campo Endereco está vazio
        if(textFieldEndereco.getText() == null || textFieldEndereco.getText().length() == 0) {
            labelErroEndereco.setText("O campo Endereco não pode estar vazio");
            qdtErros++;
        }

        // Verifica se algum gênero foi selecionado
        if(comboBoxGeneros.getSelectionModel().getSelectedItem() == null) {
            labelErroGenero.setText("Escolha um gênero");
            qdtErros++;
        }

        // Verifica se algum plano foi selecionado
        if(comboBoxPlanos.getSelectionModel().getSelectedItem() == null) {
            labelErroPlano.setText("Escolha um plano");
            qdtErros++;
        }

        // Se houver erros, retorna falso, caso contrário retorna verdadeiro
        if(qdtErros > 0){
            return false;
        }else{
            return true;
        }
    }
    
    public static boolean validateCPFFormat(String cpf) {
        String cleanedCPF = cpf.replaceAll("\\D", "");

        if (cleanedCPF.length() != 11 || !cleanedCPF.matches("\\d{11}")) {
            return false;
        }

        return true;
    }

    public String convertToCPF(String inputString) {
        String cleanedString = inputString.replaceAll("\\D", "");

        while (cleanedString.length() < 11) {
            cleanedString = "0" + cleanedString;
        }

        String formattedCPF = cleanedString.substring(0, 3) + "." +
                cleanedString.substring(3, 6) + "." +
                cleanedString.substring(6, 9) + "-" +
                cleanedString.substring(9);

        return formattedCPF;
    }
}
