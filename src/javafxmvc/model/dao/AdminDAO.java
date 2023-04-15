package src.javafxmvc.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.javafxmvc.model.domain.Admin;

public class AdminDAO {
    
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Admin buscar(String usuario, String senha) {
        String sql = "SELECT * FROM admin WHERE usuario=? AND senha=?";
        Admin retorno = null;

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, senha);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = new Admin();
                retorno.setIdAdmin(resultado.getInt("idAdmin"));
                retorno.setUsuario(resultado.getString("usuario"));
                retorno.setSenha(resultado.getString("senha"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
