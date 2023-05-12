package src.javafxmvc.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.javafxmvc.model.domain.CheckIn;

public class CheckInDAO {
    
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    // falta colocar o resto dos métodos (insertm, update, delete, list)
    
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
    
}
