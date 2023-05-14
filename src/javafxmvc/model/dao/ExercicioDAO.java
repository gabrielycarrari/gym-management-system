package src.javafxmvc.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import src.javafxmvc.model.domain.Exercicio;

public class ExercicioDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(Exercicio exercicio) {
        String sql = "INSERT INTO Exercicio (tipo, musculo, numSeries, numRepeticoes) VALUES (?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, exercicio.getTipo());
            stmt.setString(2,  exercicio.getMusculo());
            stmt.setInt(3, exercicio.getNumSeries());
            stmt.setInt(4, exercicio.getNumRepeticoes());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ExercicioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean update(Exercicio exercicio) {
        String sql = "UPDATE exercicio SET tipo=?, musculo=?, numSeries=?, numRepeticoes=? WHERE idExercicio=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, exercicio.getTipo());
            stmt.setString(2, exercicio.getMusculo());
            stmt.setInt(3, exercicio.getNumSeries());
            stmt.setInt(4, exercicio.getNumRepeticoes());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ExercicioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean delete(Exercicio exercicio) {
        String sql = "DELETE FROM exercicio WHERE idExercicio=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, exercicio.getIdExercicio());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ExercicioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Exercicio> list() {
        String sql = "SELECT * FROM exercicio";
        List<Exercicio> exercicios = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Exercicio exercicio = new Exercicio();               
                exercicio.setIdExercicio(resultado.getInt("idExercicio"));
                exercicio.setTipo(resultado.getString("tipo"));
                exercicio.setMusculo(resultado.getString("musculo"));
                exercicio.setNumSeries(resultado.getInt("numSeries"));
                exercicio.setNumRepeticoes(resultado.getInt("numRepeticoes"));
                exercicios.add(exercicio);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExercicioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exercicios;
    }
}
