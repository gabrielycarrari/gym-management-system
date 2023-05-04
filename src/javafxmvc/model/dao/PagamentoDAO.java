package src.javafxmvc.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.javafxmvc.model.domain.Pagamento;

public class PagamentoDAO {
    
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(Pagamento pagamento) {
        String sql = "INSERT INTO Pagamento (data, valor, aluno_id) VALUES (?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(pagamento.getData()));
            stmt.setFloat(2, pagamento.getValor());
            stmt.setInt(3, pagamento.getAluno_id());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PagamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean update(Pagamento pagamento) {
        String sql = "UPDATE pagamento SET data=?, valor=?, aluno_id=? WHERE idPagamento=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDate(1, java.sql.Date.valueOf(pagamento.getData()));
            stmt.setFloat(2, pagamento.getValor());
            stmt.setInt(3, pagamento.getAluno_id());
            stmt.setInt(4, pagamento.getIdPagamento());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PagamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean delete(Pagamento pagamento) {
        String sql = "DELETE FROM pagamento WHERE idPagamento=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, pagamento.getIdPagamento());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PagamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Pagamento> list() {
        String sql = "SELECT * FROM pagamento";
        List<Pagamento> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Pagamento pagamento = new Pagamento();               
                pagamento.setIdPagamento(resultado.getInt("idPagamento"));
                pagamento.setData(resultado.getDate("data").toLocalDate());
                pagamento.setValor(resultado.getFloat("valor"));
                pagamento.setAluno_id(resultado.getInt("aluno_id"));
                retorno.add(pagamento);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PagamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
