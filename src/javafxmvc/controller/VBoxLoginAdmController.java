package src.javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import src.javafxmvc.Main;
import src.javafxmvc.model.dao.AdminDAO;
import src.javafxmvc.model.domain.Admin;

import java.sql.Connection;
import src.javafxmvc.model.database.Database;
import src.javafxmvc.model.database.DatabaseFactory;

public class VBoxLoginAdmController implements Initializable {

    @FXML
    private TextField textFieldUsuario;

    @FXML
    private PasswordField passwordFieldSenha;

    @FXML
    private Button buttonLogin;

    @FXML
    private Button buttonLink;


    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final AdminDAO adminDAO = new AdminDAO();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        adminDAO.setConnection(connection);
    }
    
    @FXML
    public void login() {
        String usuario = textFieldUsuario.getText();
        String senha = passwordFieldSenha.getText();

        if(!usuario.isEmpty() && !senha.isEmpty()) {
            Admin admin = adminDAO.buscar(usuario, senha);

            if(admin != null) {
                System.out.println("User: " + usuario + "\tPass: " + senha);
            }
            else {
                System.out.println("Usuário não encontrado");
            }
        }
        else {
            if(usuario.isEmpty()) {
                System.out.println("O Campo Usuário não pode estar vazio");
            }
            if(senha.isEmpty()) {
                System.out.println("O Campo Senha não pode estar vazio");
            }
        }
    }

    public void switchToLoginFunc() throws IOException {
        Main.setRoot("view/VBoxLoginFunc");
    }
}
