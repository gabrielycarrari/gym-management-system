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
import src.javafxmvc.model.domain.Exercicio;
import src.javafxmvc.model.domain.Funcionario;
import src.javafxmvc.model.domain.Treino;
import src.javafxmvc.model.domain.TreinoExercicio;

public class TreinoExercicioDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(TreinoExercicio treinoExercicio) {
        String sql = "INSERT INTO TreinoExercicio (treino_id, exercicio_id) VALUES (?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, treinoExercicio.getIdTreino());
            stmt.setInt(2, treinoExercicio.getIdExercicio());

            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TreinoExercicioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean update(TreinoExercicio treinoExercicio) {
        String sql = "UPDATE TreinoExercicio SET treino_id=?, exercicio_id=? WHERE idTreinoExercicio=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, treinoExercicio.getIdTreino());
            stmt.setInt(2, treinoExercicio.getIdExercicio());
            stmt.setInt(3, treinoExercicio.getIdTreino());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TreinoExercicioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean delete(TreinoExercicio treinoExercicio) {
        String sql = "DELETE FROM TreinoExercicio WHERE idTreinoExercicio=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, treinoExercicio.getIdTreino());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TreinoExercicioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean deleteByIdTreino(Treino treino){
        String sql = "DELETE FROM TreinoExercicio WHERE treino_id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, treino.getIdTreino());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TreinoExercicioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Treino> list() {
        String query = "SELECT * FROM treinoExercicio ";
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
            Logger.getLogger(TreinoExercicioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return treinos;
    }

    public TreinoExercicio findById(int idTreinoExercicio) { 
        String sql = "SELECT * FROM TreinoExercicio WHERE idTreinoExercicio=?";
        TreinoExercicio retorno = new TreinoExercicio();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idTreinoExercicio);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno.setIdTreinoExercicio(resultado.getInt("idTreinoExercicio"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TreinoExercicioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public List<Exercicio> findByIdTreino(int idTreino) { 
        String sql = "SELECT * FROM treinoexercicio LEFT JOIN exercicio ON exercicio_id = idexercicio WHERE treino_id=?";
        List<Exercicio> exercicios = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idTreino);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Exercicio exercicio = new Exercicio(); 

                exercicio.setIdExercicio(resultado.getInt("idExercicio"));
                exercicio.setMusculo(resultado.getString("musculo"));
                exercicio.setNumRepeticoes(resultado.getInt("numRepeticoes"));
                exercicio.setNumSeries(resultado.getInt("numSeries"));
                exercicio.setTipo(resultado.getString("tipo"));

                exercicios.add(exercicio);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TreinoExercicioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exercicios;
    }

    public List<TreinoExercicio> findByMusculo(Exercicio musculo) { 
        String sql = "SELECT * FROM exercicio " +
        "INNER JOIN treinoexercicio ON treinoexercicio.exercicio_id = exercicio.idexercicio " +
        "INNER JOIN treino ON treino.idtreino = treinoexercicio.treino_id " +
        "INNER JOIN aluno ON aluno.idaluno = treino.aluno_id " +
        "WHERE exercicio.musculo = ?";
        List<TreinoExercicio> treinoExercicios = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, musculo.getMusculo());
            System.out.println(stmt);
            
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                TreinoExercicio treinoExercicio = new TreinoExercicio(); 
                Exercicio exercicio = new Exercicio(); 

                exercicio.setIdExercicio(resultado.getInt("idExercicio"));
                exercicio.setMusculo(resultado.getString("musculo"));
                exercicio.setNumRepeticoes(resultado.getInt("numRepeticoes"));
                exercicio.setNumSeries(resultado.getInt("numSeries"));
                exercicio.setTipo(resultado.getString("tipo"));

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

                treinoExercicio.setExercicio(exercicio);
                treinoExercicio.setTreino(treino);

                treinoExercicios.add(treinoExercicio);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TreinoExercicioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return treinoExercicios;
    }
}
