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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;

import src.javafxmvc.model.dao.FuncionarioDAO;
import src.javafxmvc.model.domain.Funcionario;
import src.javafxmvc.model.database.Database;
import src.javafxmvc.model.database.DatabaseFactory;

public class AnchorPaneFuncDialogController implements Initializable {
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
    private TextField textFieldUsuario;
    @FXML
    private ComboBox<String> comboBoxTipos;
    @FXML
    private PasswordField passwordFieldSenha;
    @FXML
    private PasswordField passwordFieldSenhaConfirmada;
    @FXML
    private Button buttonConfirmar;

    // labels de erro
    @FXML
    private Label labelErroNome;
    @FXML
    private Label labelErroCPF;
    @FXML
    private Label labelErroEndereco;
    @FXML
    private Label labelErroUsuario;   
    @FXML
    private Label labelErroTipo;
    @FXML
    private Label labelErroSenha;
    @FXML
    private Label labelErroSenhaConfirmada;

    // Listas e objetos para manipulação dos dados
    private List<String> listTipos = new ArrayList<>();
    private ObservableList<String> observableListTipos;
    private Stage dialogStage;
    private boolean buttonConfirmed = false;
    private Funcionario funcionario;

    // Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    // Método de inicialização do controller, executado quando a view é carregada
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Configura a conexão com o banco de dados
        funcionarioDAO.setConnection(connection);
        
        // Carrega os tipos de funcionários no combobox
        loadTypes();
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
                this.labelTitulo.setText("Cadastrar Funcionário");
                break;
            case 1:
                this.labelTitulo.setText("Alterar Funcionário");
                break;
        }
        
    }

    public Funcionario getFuncionario() {
        return this.funcionario;
    }

    // Método para preencher os campos da janela com as informações do funcionário selecionado, caso seja uma alteração
     public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
        this.textFieldNome.setText(funcionario.getNome());
        this.textFieldCPF.setText(funcionario.getCpf());
        this.textFieldEndereco.setText(funcionario.getEndereco());
        this.comboBoxTipos.setValue(funcionario.getTipo());
        this.textFieldUsuario.setText(funcionario.getUsuario());
        this.passwordFieldSenha.setText(funcionario.getSenha());
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
            // Define os valores inseridos pelo usuário como propriedades do objeto "funcionario"
            String CPF = textFieldCPF.getText();
            funcionario.setNome(textFieldNome.getText());
            funcionario.setCpf(convertToCPF(CPF));
            funcionario.setEndereco(textFieldEndereco.getText());
            funcionario.setTipo(comboBoxTipos.getSelectionModel().getSelectedItem().toLowerCase());
            funcionario.setUsuario(textFieldUsuario.getText());
            funcionario.setSenha(passwordFieldSenhaConfirmada.getText());
            
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

    // Método responsável por carregar os tipos de funcionários na caixa de seleção
    public void loadTypes(){
        // Adiciona os tipos de funcionários à lista
        listTipos.add("Personal Trainer");
        listTipos.add("Recepcionista");

        // Converte a lista em um objeto ObservableList e define a caixa de seleção como o seu destino
        observableListTipos = FXCollections.observableArrayList(listTipos);
        comboBoxTipos.setItems(observableListTipos);
    }  

    // Método responsável por limpar as mensagens de erro exibidas na interface
    public void cleanErrors() {
        labelErroNome.setText(null);
        labelErroCPF.setText(null);
        labelErroEndereco.setText(null);
        labelErroUsuario.setText(null);
        labelErroTipo.setText(null);
        labelErroSenha.setText(null);
        labelErroSenhaConfirmada.setText(null);
    }

    // Método responsável por validar os dados inseridos pelo usuário
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

        // Verifica se o campo Usuario está vazio
        if(textFieldUsuario.getText() == null || textFieldUsuario.getText().length() == 0) {
            labelErroUsuario.setText("O campo Usuario não pode estar vazio");
            qdtErros++;
        }else{
            // Verifica se o nome do Usuário já existe
            Funcionario funcionarioExiste = funcionarioDAO.validateUserName(textFieldUsuario.getText());
            if(funcionarioExiste != null){                
                // Verifica se é uma alteração
                if(funcionario.getIdFuncionario() != 0){
                    //Verifica se o nome de usuário é diferente, pois em uma alteração pode ser o mesmo nome
                    if(!funcionario.getUsuario().equals(funcionarioExiste.getUsuario())){
                        labelErroUsuario.setText("Este nome de usuário já foi usado. Tente outro");
                        qdtErros++;
                    }
                }else{
                    labelErroUsuario.setText("Este nome de usuário já foi usado. Tente outro");
                    qdtErros++;
                }
            }
        }

        // Verifica se algum tipo foi selecionado
        if(comboBoxTipos.getSelectionModel().getSelectedItem() == null) {
            labelErroTipo.setText("Escolha um tipo");
            qdtErros++;
        }

        // Verifica se o campo Senha está vazio
        if(passwordFieldSenha.getText() == null || passwordFieldSenha.getText().length() == 0) {
            labelErroSenha.setText("O campo Senha não pode estar vazio");
            qdtErros++;
        }

        // Verifica se as senhas digitadas coincidem
        if(passwordFieldSenhaConfirmada.getText() == null || passwordFieldSenhaConfirmada.getText().length() == 0) {
            labelErroSenhaConfirmada.setText("O campo Confirmar Senha não pode estar vazio");
            qdtErros++;
        }
        else if(!passwordFieldSenha.getText().equals(passwordFieldSenhaConfirmada.getText())) {
            labelErroSenhaConfirmada.setText("As senhas não coincidem");
            qdtErros++;
        }

        // Se houver erros, retorna falso, caso contrário retorna verdadeiro
        if(qdtErros > 0){
            return false;
        }else{
            return true;
        }
    }

    // Método responsável por validar o formato do cpf
    public static boolean validateCPFFormat(String cpf) {
        String cleanedCPF = cpf.replaceAll("\\D", "");

        if (cleanedCPF.length() != 11 || !cleanedCPF.matches("\\d{11}")) {
            return false;
        }

        return true;
    }

    // Método responsável por converter para o formato do cpf o texto inserido pelo usuário
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
