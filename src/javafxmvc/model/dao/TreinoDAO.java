package src.javafxmvc.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import src.javafxmvc.model.domain.Aluno;
import src.javafxmvc.model.domain.Funcionario;
import src.javafxmvc.model.domain.Treino;

public class TreinoDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(Treino treino) {
        String sql = "INSERT INTO Treino (dataInicio, dataFinal, aluno_id, funcionario_id) VALUES (?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDate(1,  java.sql.Date.valueOf(treino.getDataInicio()));
            stmt.setDate(2,  java.sql.Date.valueOf(treino.getDataFinal()));
            stmt.setInt(3, treino.getIdAluno());
            stmt.setInt(4, treino.getIdFuncionario());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TreinoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean update(Treino treino) {
        String sql = "UPDATE treino SET dataInicio=?, dataFinal=?, aluno_id=?, funcionario_id=? WHERE idTreino=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(treino.getDataInicio()));
            stmt.setDate(2, java.sql.Date.valueOf(treino.getDataFinal()));
            stmt.setInt(3, treino.getIdAluno());
            stmt.setInt(4, treino.getIdFuncionario());
            stmt.setInt(5, treino.getIdTreino());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TreinoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean delete(Treino treino) {
        String sql = "DELETE FROM treino WHERE idTreino=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, treino.getIdTreino());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TreinoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }


    public List<Treino> list() {
        String query = "SELECT * FROM treino ";
        List<Treino> treinos = new ArrayList<>();

        try  {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                Treino treino = new Treino();               
                treino.setIdTreino(resultado.getInt("idTreino"));
                treino.setDataInicio(resultado.getDate("dataInicio").toLocalDate());
                treino.setDataFinal(resultado.getDate("dataFinal").toLocalDate());
                
                AlunoDAO alunoDAO = new AlunoDAO();
                alunoDAO.setConnection(connection);
                Aluno aluno = alunoDAO.findById(resultado.getInt("aluno_id")); 
                treino.setIdAluno(aluno.getIdAluno());
                treino.setAlunoNome(aluno.getNome());

                FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
                funcionarioDAO.setConnection(connection);
                Funcionario funcionario = funcionarioDAO.findById(resultado.getInt("funcionario_id")); //ver com as meninas a necessidade dessa classe
                treino.setIdFuncionario(funcionario.getIdFuncionario());
                treino.setFuncionarioNome(funcionario.getNome());

                treinos.add(treino);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TreinoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return treinos;
    }

    public Treino findById(int idTreino) { 
        String sql = "SELECT * FROM Treino WHERE idTreino=?";
        Treino retorno = new Treino();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idTreino);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno.setIdTreino(resultado.getInt("idTreino"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Treino.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Boolean findByPeriodo(int id, Treino treino) { 
        String sql = "SELECT count(*) FROM treino " +
        "WHERE idtreino = ? " +
        "AND aluno_id = ? " +
        "AND ? BETWEEN datainicio AND datafinal " +
        "OR ? BETWEEN datainicio AND datafinal ";
        Boolean isValidate = false;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, id);
            stmt.setInt(2, treino.getIdAluno());
            stmt.setTimestamp(3, Timestamp.valueOf(treino.getDataInicio().atStartOfDay()));
            stmt.setTimestamp(4, Timestamp.valueOf(treino.getDataFinal().atStartOfDay()));
            ResultSet resultado = stmt.executeQuery();
            
            if (resultado.next()) {
                if(resultado.getInt("count")>=3){
                    isValidate = false;
                }else {
                    isValidate = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Treino.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isValidate;
    }

    public Treino buscarUltimoTreino() {
        String sql = "SELECT max(idTreino) FROM treino";
        Treino retorno = new Treino();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                retorno.setIdTreino(resultado.getInt("max"));
                return retorno;
            }
        } catch (SQLException e) {
            Logger.getLogger(TreinoDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return retorno;
    }
}
