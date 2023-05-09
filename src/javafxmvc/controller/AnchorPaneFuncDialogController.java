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

    //labels de erro
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

    private List<String> listTipos = new ArrayList<>();
    private ObservableList<String> observableListTipos;

    private Stage dialogStage;
    private boolean buttonConfirmed = false;
    private Funcionario funcionario;
    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        funcionarioDAO.setConnection(connection);
        loadTypes();
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

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
        this.textFieldNome.setText(funcionario.getNome());
        this.textFieldCPF.setText(funcionario.getCpf());
        this.textFieldEndereco.setText(funcionario.getEndereco());
        this.comboBoxTipos.setValue(funcionario.getTipo());
        this.textFieldUsuario.setText(funcionario.getUsuario());
        this.passwordFieldSenha.setText(funcionario.getSenha());
    }

    public boolean isButtonConfirmed() {
        return buttonConfirmed;
    }
    
    @FXML
    public void handleButtonConfirm() {
        cleanErrors();

        if (validateData()){
            funcionario.setNome(textFieldNome.getText());
            funcionario.setCpf(textFieldCPF.getText());
            funcionario.setEndereco(textFieldEndereco.getText());
            funcionario.setTipo(comboBoxTipos.getSelectionModel().getSelectedItem().toLowerCase());
            funcionario.setUsuario(textFieldUsuario.getText());
            funcionario.setSenha(passwordFieldSenhaConfirmada.getText());
            
            buttonConfirmed = true;
            dialogStage.close();

            //funcionarioDAO.insert(funcionario);

        }
    }

    @FXML
    public void handleButtonCancel(){
        getDialogStage().close();
    }

    public void loadTypes(){
        listTipos.add("Personal Trainer");
        listTipos.add("Recepcionista");

        observableListTipos = FXCollections.observableArrayList(listTipos);
        comboBoxTipos.setItems(observableListTipos);
    }

    public void cleanErrors() {
        labelErroNome.setText(null);
        labelErroCPF.setText(null);
        labelErroEndereco.setText(null);
        labelErroUsuario.setText(null);
        labelErroTipo.setText(null);
        labelErroSenha.setText(null);
        labelErroSenhaConfirmada.setText(null);
    }

    public boolean validateData(){
        int qdtErros = 0;
        if(textFieldNome.getText() == null || textFieldNome.getText().length() == 0) {
            labelErroNome.setText("O campo Nome não pode estar vazio");
            qdtErros++;
        }
        if(textFieldCPF.getText() == null || textFieldCPF.getText().length() == 0) {
            labelErroCPF.setText("O campo CPF não pode estar vazio");
            qdtErros++;
        }
        if(textFieldEndereco.getText() == null || textFieldEndereco.getText().length() == 0) {
            labelErroEndereco.setText("O campo Endereco não pode estar vazio");
            qdtErros++;
        }
        if(textFieldUsuario.getText() == null || textFieldUsuario.getText().length() == 0) {
            labelErroUsuario.setText("O campo Usuario não pode estar vazio");
            qdtErros++;
        }else{
            //verificar se o nome do usuário já existe
            Funcionario funcionario = funcionarioDAO.validateUserName(textFieldUsuario.getText());
            if(funcionario != null){
                //Verificar se nao é uma alteração do mesmo funcionário, pois caso seja, o nome de usuário pode ser o mesmo
                if(!labelTitulo.getText().equals("Alterar Funcionário") && !funcionario.getUsuario().equals(textFieldUsuario.getText())){
                    labelErroUsuario.setText("Este nome de usuário já foi usado. Tente outro");
                    qdtErros++;
                }
            }
        }
        if(comboBoxTipos.getSelectionModel().getSelectedItem() == null) {
            labelErroTipo.setText("Escolha um tipo");
            qdtErros++;
        }
        if(passwordFieldSenha.getText() == null || passwordFieldSenha.getText().length() == 0) {
            labelErroSenha.setText("O campo Senha não pode estar vazio");
            qdtErros++;
        }
        if(!passwordFieldSenha.getText().equals(passwordFieldSenhaConfirmada.getText())) {
            labelErroSenhaConfirmada.setText("As senhas não coincidem");
            qdtErros++;
        }

        if(qdtErros > 0){
            return false;
        }else{
            return true;
        }
    }
}
