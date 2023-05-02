package src.javafxmvc.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public boolean insert(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (nome, cpf, endereco, tipo, usuario, senha) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCpf());
            stmt.setString(3, funcionario.getEndereco());
            stmt.setString(4, funcionario.getTipo());
            stmt.setString(5, funcionario.getUsuario());
            stmt.setString(6, funcionario.getSenha());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean update(Funcionario funcionario) {
        String sql = "UPDATE funcionario SET nome=?, cpf=?, endereco=?, tipo=?, usuario=?, senha=? WHERE idFuncionario=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCpf());
            stmt.setString(3, funcionario.getEndereco());
            stmt.setString(4, funcionario.getTipo());
            stmt.setString(5, funcionario.getUsuario());
            stmt.setString(6, funcionario.getSenha());
            stmt.setInt(7, funcionario.getIdFuncionario());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean delete(Funcionario funcionario) {
        String sql = "DELETE FROM funcionario WHERE idFuncionario=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, funcionario.getIdFuncionario());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Funcionario> list() {
        String sql = "SELECT * FROM funcionario";
        List<Funcionario> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Funcionario funcionario = new Funcionario();               
                funcionario.setIdFuncionario(resultado.getInt("idFuncionario"));
                funcionario.setNome(resultado.getString("nome"));
                funcionario.setCpf(resultado.getString("cpf"));
                funcionario.setEndereco(resultado.getString("endereco"));
                funcionario.setTipo(resultado.getString("tipo"));
                funcionario.setUsuario(resultado.getString("usuario"));
                funcionario.setSenha(resultado.getString("senha"));
                retorno.add(funcionario);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Funcionario validate(String usuario, String senha) { // mudar validate
        String sql = "SELECT * FROM funcionario WHERE usuario=? AND senha=?";
        Funcionario retorno = null;

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, senha);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = new Funcionario();
                retorno.setUsuario(resultado.getString("usuario"));
                retorno.setSenha(resultado.getString("senha"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Funcionario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Funcionario validateUserName(String usuario) { 
        String sql = "SELECT * FROM funcionario WHERE usuario=?";
        Funcionario retorno = null;

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, usuario);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = new Funcionario();
                retorno.setUsuario(resultado.getString("usuario"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Funcionario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
