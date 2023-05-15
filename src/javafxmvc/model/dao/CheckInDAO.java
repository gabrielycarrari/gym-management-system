package src.javafxmvc.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.chart.XYChart;
import src.javafxmvc.model.domain.CheckIn;

public class CheckInDAO {
    
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(CheckIn checkIn) {
        String sql = "INSERT INTO CheckIn (data, hora, aluno_id) VALUES (?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDate(1,  java.sql.Date.valueOf(checkIn.getData()));
            stmt.setTime(2,  java.sql.Time.valueOf(checkIn.getHora()));
            stmt.setInt(3, checkIn.getAluno_id());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CheckInDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean update(CheckIn checkIn) {
        String sql = "UPDATE checkIn SET data=?, hora=?, aluno_id=? WHERE idCheckIn=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(checkIn.getData()));
            stmt.setTime(2, java.sql.Time.valueOf(checkIn.getHora()));
            stmt.setInt(3, checkIn.getAluno_id());
            stmt.setInt(4, checkIn.getIdCheckIn());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CheckInDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean delete(CheckIn checkIn) {
        String sql = "DELETE FROM checkIn WHERE idCheckIn=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, checkIn.getIdCheckIn());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CheckInDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<CheckIn> list() {
        String sql = "SELECT * FROM checkIn ORDER BY data ASC";
        List<CheckIn> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                CheckIn checkIn = new CheckIn();               
                checkIn.setIdCheckIn(resultado.getInt("idCheckIn"));
                checkIn.setData(resultado.getDate("data").toLocalDate());
                checkIn.setHora(resultado.getTime("hora").toLocalTime());
                checkIn.setAluno_id(resultado.getInt("aluno_id"));
                retorno.add(checkIn);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CheckInDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    public CheckIn findById(int idCheckIn) { 
        String sql = "SELECT * FROM CheckIn WHERE idcheckin=?";
        CheckIn retorno = new CheckIn();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idCheckIn);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno.setIdCheckIn(resultado.getInt("idcheckin"));
                retorno.setData(resultado.getDate("data").toLocalDate());
                retorno.setHora(resultado.getTime("hora").toLocalTime());
                retorno.setAluno_id(resultado.getInt("aluno_id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CheckIn.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public CheckIn search(int aluno_id, LocalDate data) { 
        String sql = "SELECT * FROM CheckIn WHERE aluno_id=? and data=?";
        CheckIn retorno = new CheckIn();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, aluno_id);
            stmt.setDate(2, java.sql.Date.valueOf(data));
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno.setIdCheckIn(resultado.getInt("idcheckin"));
                retorno.setData(resultado.getDate("data").toLocalDate());
                retorno.setHora(resultado.getTime("hora").toLocalTime());
                retorno.setAluno_id(resultado.getInt("aluno_id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CheckIn.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    public List<Object[]> extractMonth() { 
        String sql = "SELECT EXTRACT(MONTH FROM data) AS mes, COUNT(*) AS total FROM CheckIn GROUP BY mes ORDER BY mes";
        List<Object[]> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                int month = resultado.getInt("mes");
                int total = resultado.getInt("total");
                Object[] row = new Object[2];
                row[0] = month;
                row[1] = total;
                retorno.add(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CheckIn.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
