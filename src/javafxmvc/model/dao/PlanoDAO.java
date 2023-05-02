package src.javafxmvc.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import src.javafxmvc.model.domain.Plano;

public class PlanoDAO {
    
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(Plano plano) {
        String sql = "INSERT INTO Plano (tipo, preco) VALUES (?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, plano.getTipo());
            stmt.setFloat(2, plano.getPreco());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PlanoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean update(Plano plano) {
        String sql = "UPDATE Plano SET tipo=?, preco=? WHERE idPlano=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, plano.getTipo());
            stmt.setFloat(2, plano.getPreco());
            stmt.setInt(3, plano.getIdPlano());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PlanoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean delete(Plano plano) {
        String sql = "DELETE FROM Plano WHERE idPlano=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, plano.getIdPlano());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PlanoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Plano> list() {
        String sql = "SELECT * FROM Plano";
        List<Plano> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Plano plano = new Plano();               
                plano.setIdPlano(resultado.getInt("idPlano"));
                plano.setTipo(resultado.getString("tipo"));
                plano.setPreco(resultado.getFloat("preco"));
                retorno.add(plano);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlanoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    
}
