package src.javafxmvc.model.dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.javafxmvc.model.domain.CheckOut;
public class CheckOutDAO {
    private Connection connection;
    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    public boolean insert(CheckOut checkOut) {
        String sql = "INSERT INTO CheckOut (data, hora, checkIn_id) VALUES (?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDate(1,  java.sql.Date.valueOf(checkOut.getData()));
            stmt.setTime(2,  java.sql.Time.valueOf(checkOut.getHora()));
            stmt.setInt(3, checkOut.getCheckIn_id());
            stmt.setInt(3, checkOut.getCheckin_id());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
@@ -46,7 +46,7 @@ public boolean update(CheckOut checkOut) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(checkOut.getData()));
            stmt.setTime(2, java.sql.Time.valueOf(checkOut.getHora()));
            stmt.setInt(3, checkOut.getCheckIn_id());
            stmt.setInt(3, checkOut.getCheckin_id());
            stmt.setInt(4, checkOut.getIdCheckOut());
            stmt.execute();
            return true;
@@ -80,7 +80,7 @@ public List<CheckOut> list() {
                checkOut.setIdCheckOut(resultado.getInt("idCheckOut"));
                checkOut.setData(resultado.getDate("data").toLocalDate());
                checkOut.setHora(resultado.getTime("hora").toLocalTime());
                checkOut.setCheckIn_id(resultado.getInt("checkIn_id"));
                checkOut.setCheckin_id(resultado.getInt("checkIn_id"));
                retorno.add(checkOut);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CheckOutDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    public boolean search(int checkIn_id) { 
        String sql = "SELECT idCheckOut FROM CheckOut WHERE checkIn_id=?";
        boolean retorno = false;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, checkIn_id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CheckOut.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
