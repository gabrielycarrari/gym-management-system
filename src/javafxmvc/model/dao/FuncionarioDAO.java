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

    public boolean inserir(Funcionario funcionario) {
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
    /* 
    public boolean alterar(Funcionario funcionario) {
        String sql = "UPDATE funcionarios SET nome=?, cpf=?, telefone=? WHERE cdFuncionario=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCpf());
            stmt.setString(3, funcionario.getTelefone());
            stmt.setInt(4, funcionario.getCdFuncionario());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Funcionario funcionario) {
        String sql = "DELETE FROM funcionarios WHERE cdFuncionario=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, funcionario.getCdFuncionario());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Funcionario> listar() {
        String sql = "SELECT * FROM funcionarios";
        List<Funcionario> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setCdFuncionario(resultado.getInt("cdFuncionario"));
                funcionario.setNome(resultado.getString("nome"));
                funcionario.setCpf(resultado.getString("cpf"));
                funcionario.setTelefone(resultado.getString("telefone"));
                retorno.add(funcionario);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
*/
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

    public Funcionario buscarNomeUsuario(String usuario) {
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
