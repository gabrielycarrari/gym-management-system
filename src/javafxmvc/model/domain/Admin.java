package src.javafxmvc.model.domain;

import java.io.Serializable;

public class Admin implements Serializable {
    private int idAdmin;
    private String usuario;
    private String senha;

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }
  
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String toString() {
        return "Id: " + this.idAdmin + "\tUsuário: " + this.usuario + "\tSenha: " + this.senha;
    }
}
