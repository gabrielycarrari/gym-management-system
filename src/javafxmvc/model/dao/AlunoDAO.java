package src.javafxmvc.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.javafxmvc.model.domain.Aluno;

public class AlunoDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    public boolean insert(Aluno aluno) {
        String sql = "INSERT INTO Aluno (nome, cpf, endereco, genero, pontos, plano_id) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getCpf());
            stmt.setString(3, aluno.getEndereco());
            stmt.setString(4, aluno.getGenero());
            stmt.setInt(5, aluno.getPontos());
            stmt.setInt(6, aluno.getPlano_id());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AlunoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean update(Aluno aluno) {
        String sql = "UPDATE aluno SET nome=?, cpf=?, endereco=?, genero=?, pontos=?, plano_id=? WHERE idAluno=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getCpf());
            stmt.setString(3, aluno.getEndereco());
            stmt.setString(4, aluno.getGenero());
            stmt.setInt(5, aluno.getPontos());
            stmt.setInt(6, aluno.getPlano_id());
            stmt.setInt(7, aluno.getIdAluno());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AlunoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean delete(Aluno aluno) {
        String sql = "DELETE FROM aluno WHERE idAluno=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, aluno.getIdAluno());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AlunoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public List<Aluno> list() {
        String sql = "SELECT * FROM aluno";
        List<Aluno> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Aluno aluno = new Aluno();               
                aluno.setIdAluno(resultado.getInt("idAluno"));
                aluno.setNome(resultado.getString("nome"));
                aluno.setCpf(resultado.getString("cpf"));
                aluno.setEndereco(resultado.getString("endereco"));
                aluno.setGenero(resultado.getString("genero"));
                aluno.setPontos(resultado.getInt("pontos"));
                aluno.setPlano_id(resultado.getInt("plano_id"));
                retorno.add(aluno);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AlunoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
        
}

