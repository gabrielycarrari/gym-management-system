package src.javafxmvc.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.javafxmvc.model.domain.Funcionario;

public class FuncionarioDAO {
    
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Funcionario buscar(String usuario, String senha) {
        String sql = "SELECT * FROM funcionario WHERE usuario=? AND senha=?";
        Funcionario retorno = null;

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, senha);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = new Funcionario();
                retorno.setIdFuncionario(resultado.getInt("idFuncionario"));
                retorno.setUsuario(resultado.getString("usuario"));
                retorno.setSenha(resultado.getString("senha"));
                retorno.setNome(resultado.getString("nome"));
                retorno.setCpf(resultado.getString("cpf"));
                retorno.setEndereco(resultado.getString("endereco"));
                retorno.setTipo(resultado.getString("tipo"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Funcionario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
