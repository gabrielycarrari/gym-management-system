package src.javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;

import src.javafxmvc.Main;
import src.javafxmvc.model.database.Database;
import src.javafxmvc.model.database.DatabaseFactory;

public class VBoxLoginFuncController implements Initializable {

    @FXML
    private TextField textFieldUsuario;

    @FXML
    private PasswordField passwordFieldSenha;

    @FXML
    private Button buttonLogin;

    @FXML
    private Button buttonLink;

    @FXML
    private Label labelErroUsuario;

    @FXML
    private Label labelErroSenha;

    @FXML
    private Label labelNotFound;


    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    // Create DAO

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Set DAO connection
    }
    
    @FXML
    public void login() {
        labelErroUsuario.setText(null);
        labelErroSenha.setText(null);
        labelNotFound.setText(null);

        String usuario = textFieldUsuario.getText();
        String senha = passwordFieldSenha.getText();

        if(!usuario.isEmpty() && !senha.isEmpty()) {
            labelNotFound.setText("Usuário não encontrado");
        }
        else {
            if(usuario.isEmpty()) {
                labelErroUsuario.setText("O campo Usuário não pode estar vazio");
            }
            if(senha.isEmpty()) {
                labelErroSenha.setText("O campo Senha não pode estar vazio");
            }
        }
    }

    public void switchToLoginAdm() throws IOException {
        Main.setRoot("view/VBoxLoginAdm");
    }
}
